package main;

import configuration.ConfigurationService;
import configuration.CrawlerConfig;

/**
 * Main entry point of the whole program.
 *
 * @author Daniel Braun
 */
public class MainCrawler {

    public static void main(String[] args) {
        CrawlerConfig config = new ConfigurationService().getConfig();
        GitHubCrawler crawler = new GitHubCrawler(config);
        //Start the crawler.
        crawler.run();

    }
}
