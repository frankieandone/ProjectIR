import com.mongodb.MongoClient;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.util.List;

class CrawlerController {
    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = DataDumpController.filePath;
        int numberOfCrawlers = 7;
        
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setIncludeHttpsPages(true);
        config.setIncludeBinaryContentInCrawling(false);
        
        boolean completeStartOver = true;
        if (completeStartOver) {
            DataDumpController.deleteDataDump();
        }
        
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        
        List<String> urls = SeedURLFileReader.read();
        if (urls != null && !urls.isEmpty()) {
            for (String url : urls) {
                controller.addSeed(url);
            }
            
            MongoClient mongoClient = new MongoClient("mongodb://localhost:27017", 27017);
            
            // Ignore object parameter for now.
            CrawlerFactory crawlerFactory = new CrawlerFactory(mongoClient);
            controller.start(crawlerFactory, numberOfCrawlers);
        }
    }
}
