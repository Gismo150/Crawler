package main;

import com.beust.jcommander.JCommander;
import commands.CrawlCommand;
import configuration.ConfigurationService;

/**
 * Main entry point of the whole program.
 *
 * @author Daniel Braun
 */
public class MainCrawler {

    public static void main(String[] args) {
        if (args.length > 0) {
            // Arguments over configuration file
            try {
                CrawlCommand crawlCmd = new CrawlCommand();

                JCommander commander = new JCommander();
                commander.addCommand("crawl", crawlCmd);

                commander.parse(args);
                String parsed = commander.getParsedCommand();

                crawlCmd.execute();
            } catch(Exception exception) {
                System.err.println("Failed to start Crawler through Commandline...");
                System.err.println(exception.getMessage());

                // Run the GitHubCrawler with the configuration file
                GitHubCrawler crawler = new GitHubCrawler(new ConfigurationService().getConfig());
                crawler.run();
            }
        }
        else {
            GitHubCrawler crawler = new GitHubCrawler(new ConfigurationService().getConfig());
            crawler.run();
        }
    }
}
