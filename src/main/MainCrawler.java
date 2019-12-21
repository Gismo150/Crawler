package main;

import java.io.IOException;
import java.util.logging.*;

/**
 * Main entry point of the whole program
 *
 * @author Daniel Braun
 */
public class MainCrawler {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.ALL);
        Handler handler = null;
        try {
            handler = new FileHandler("Crawler_Log", true);
            handler.setLevel(Level.ALL);
            logger.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.setUseParentHandlers(false);
        logger.config("CONFIGURATION:");
        logger.config("Language: " + Config.LANGUAGE);
        logger.config("Last pushed date: " + Config.LASTPUSHEDDATE);
        logger.config("Stars decrease amount: " + Config.STARSDECREASEAMOUNT);
        logger.config("Build system: " + Config.BUILDSYSTEM.toString());
        logger.config("Filepath: " + Config.FILEPATH);


        long startTime = System.nanoTime();
        //Init crawler with configuration.
        GitHubCrawler crawler = new GitHubCrawler(Config.LANGUAGE, Config.LASTPUSHEDDATE, Config.STARSDECREASEAMOUNT, Config.BUILDSYSTEM, Config.OAUTHTOKEN, logger, startTime);
        //Start the crawler.
        crawler.run();

    }
}
