import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import log.FMLogger;
import org.bson.Document;
import org.jsoup.Jsoup;

public class Crawler extends WebCrawler {
    
    public Crawler(Object object) {
        // Object parameter and member are used for demonstration for future reference.
    }
    
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        // FMLogger.log("#shouldVisit url: " + url + " to-visit: " + !URLFilters.URL_FILTER.matcher(href).matches());
        return !URLFilters.URL_FILTER.matcher(href).matches();
    }
    
    @Override
    public void visit(Page page) {
        FMLogger.log("#visit url: " + page.getWebURL());
        
        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
        String html = htmlParseData.getHtml();
        org.jsoup.nodes.Document htmlDocument = Jsoup.parseBodyFragment(html);
        
        String url = page.getWebURL().getURL();
        String author = htmlDocument.getElementsByClass("author-name").text();
        if (author.isEmpty()) {
            // It appears that if there is an author, then there is a date.
            // However this is a specific pattern filtered for therefore it could potentially be a false positive.
            FMLogger.log("url: " + url + " has no author therefore skipping.");
            return;
        }
        String date = htmlDocument.getElementsByClass("date").text();
        String entireBodyText = htmlDocument.body().text();
        
        // Filter articles to only include article text and not text from irrelevant parts of the web page.
        // It appears that the text from social media buttons could be used to indicate start and end of article text.
        // e.g. <social-media-button-text-sequence>...text... <social-media-button-text-sequence>
        String marker = "Reblog Share Tweet Share";
        int start = entireBodyText.indexOf(marker);
        int end = entireBodyText.indexOf(marker, start + marker.length());
        String articleText = entireBodyText.substring(start + marker.length(), end).trim();
        
        FMLogger.log("url: \n" + url);
        FMLogger.log("author-name: \n" + author);
        FMLogger.log("date: \n" + date);
        FMLogger.log("entireBodyText: \n" + entireBodyText);
        FMLogger.log("articleText: \n" + articleText);
        
        try {
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            // If the database does not exist, then MongoDB will create it.
            MongoDatabase database = mongoClient.getDatabase("ProjectIR");
            // Using fully qualified namespace to prevent conflict with JSoup Document.
            MongoCollection<Document> collection = database.getCollection("webpages");
            // BSON type of array corresponds to java.util.List in Java.
            org.bson.Document bsonDocument = new Document("url", url)
                    .append("author", author)
                    .append("date", date)
                    .append("article", entireBodyText);
            collection.insertOne(bsonDocument);
        } catch (Exception e) {
            e.printStackTrace();
            FMLogger.log("Exception: " + e);
        }
    }
}
