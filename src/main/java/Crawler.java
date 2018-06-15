import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.bson.Document;
import org.jsoup.Jsoup;

public class Crawler extends WebCrawler {
    private MongoClient mongoClient;
    
    public Crawler(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }
    
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !URLFilters.URL_FILTER.matcher(href).matches();
    }
    
    @Override
    public void visit(Page page) {
        if (!(page.getParseData() instanceof HtmlParseData)) {
            return;
        }
        
        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
        String html = htmlParseData.getHtml();
        org.jsoup.nodes.Document htmlDocument = Jsoup.parseBodyFragment(html);
        // TODO: finish extracting relevant information and inserting into MongoDB.
        
        // If the database does not exist, then MongoDB will create it.
        MongoDatabase database = mongoClient.getDatabase("crawler4jdb");
        // Using fully qualified namespace to prevent conflict with JSoup Document.
        MongoCollection<org.bson.Document> collection = database.getCollection("webpages");
        // BSON type of array corresponds to java.util.List in Java.
        org.bson.Document bsonDocument = new Document("name", page.getWebURL())
                .append("charset", "<charset>")
                .append("description", "<description>")
                .append("keywords", "<keywords>")
                .append("author", "<author>")
                .append("date", "<date>")
                .append("title", "<title>")
                .append("content", "<content>")
                .append("links", "<links>");
        collection.insertOne(bsonDocument);
    }
}
