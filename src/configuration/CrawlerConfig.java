package configuration;


import java.util.List;

/**
 * Configuration file that will be de/serialized from/to Yaml file.
 *
 * @author Ali Bozna
 */
public class CrawlerConfig {
    public String language;

    public String lastPushDate;

    public int starsDecreaseAmount;

    public String oauthtoken;

    public String filePath;

    public String jsonFileName;

    public String customFile;

    public String buildSystem;

    public List buildSystems;
}
