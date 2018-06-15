import com.mongodb.MongoClient;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;

class CrawlerFactory implements CrawlController.WebCrawlerFactory {
    private MongoClient mongoClient;
    
    CrawlerFactory(MongoClient mongoClient) {
        // Object parameter and member are used for demonstration for future reference.
        this.mongoClient = mongoClient;
    }
    
    public WebCrawler newInstance() throws Exception {
        return new Crawler(mongoClient);
    }
}
