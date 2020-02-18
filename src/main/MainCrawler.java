package main;

/**
 * Main entry point of the whole program.
 *
 * @author Daniel Braun
 */
public class MainCrawler {

    public static void main(String[] args) {
        //Init crawler with configuration.
        GitHubCrawler crawler = new GitHubCrawler(Config.LANGUAGE, Config.LASTPUSHEDDATE, Config.STARSDECREASEAMOUNT, Config.BUILDSYSTEM, Config.OAUTHTOKEN);
        //Start the crawler.
        crawler.run();

    }
}
