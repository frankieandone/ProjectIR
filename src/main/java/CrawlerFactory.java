import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;

class CrawlerFactory implements CrawlController.WebCrawlerFactory {
    private Object object;
    
    CrawlerFactory(Object object) {
        // Object parameter and member are used for demonstration for future reference.
        this.object = object;
    }
    
    public WebCrawler newInstance() throws Exception {
        return new Crawler(object);
    }
}
