package commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import configuration.ConfigurationService;
import configuration.CrawlerConfig;
import main.GitHubCrawler;

/**
 * The Crawl Command executes the GitHubCrawler with the settings passed through the commandline.
 *
 * @author Ali Bozna
 */
@Parameters(separators = "=")
public class CrawlCommand {
    @Parameter(names = "--language", description = "")
    private String language;

    @Parameter(names = { "--stars", "--stars-decrease-amount" }, description = "",  required = true)
    private int starDecreaseAmount;

    @Parameter(names = { "--build-system", "--bs" }, description = "", required = false)
    private String buildSystem;

    /**
     * Gets the language.
     *
     * @return Returns the language.
     */
    public String getLanguage() {
        return this.language;
    }

    /**
     * Gets the stars decrease amount.
     *
     * @return Returns the stars decrease amount.
     */
    public int getStarsDecreaseAmount() {
        return this.starDecreaseAmount;
    }

    /**
     * Gets the Build-System.
     *
     * @return Returns the Build-System.
     */
    public String getBuildSystem() {
        return this.buildSystem;
    }

    /**
     * Executes the GitHubCrawler with the settings passed through the commandline. Also it loads the
     * configured Build-Systems from the configuration file.
     */
    public void execute() {
        CrawlerConfig config = new CrawlerConfig();
        config.starDecreaseAmount = this.getStarsDecreaseAmount();
        config.buildSystem = this.getBuildSystem();
        config.language = this.getLanguage();

        // Get the Build-Systems from the configuration file.
        config.buildSystems = new ConfigurationService().getConfig().buildSystems;

        GitHubCrawler crawler = new GitHubCrawler(config);
        crawler.run();
    }
}
