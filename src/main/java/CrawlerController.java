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
        config.setResumableCrawling(false);
        // config.setMaxDepthOfCrawling(20);
        // config.setMaxPagesToFetch(100);
        
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
            
            // Object parameter is used for demonstration for future reference.
            CrawlerFactory crawlerFactory = new CrawlerFactory(null);
            controller.startNonBlocking(crawlerFactory, numberOfCrawlers);
        }
    }
}
