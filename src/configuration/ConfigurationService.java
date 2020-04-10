package configuration;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ali Bozna
 */
public class ConfigurationService {

    public ConfigurationService() {
    }

    /**
     * Deserializes the @see CrawlerConfig from the user path.
     *
     * @return Returns the @see CrawlerConfig.
     */
    public CrawlerConfig getConfig() {
        Path configPath = Paths.get(System.getProperty("user.home"), "crawler", "config.yaml");

        System.out.println(configPath.toFile().getAbsolutePath());

        // TODO If the configuration file is empty it will still deserialize it instead of failing.
        try(FileReader fileReader = new FileReader(configPath.toFile())) {
            YamlReader reader = new YamlReader(fileReader);
            CrawlerConfig config = (CrawlerConfig)reader.read();
            reader.close();

            return config;

        } catch(Exception exception) {
            System.err.println(exception.getMessage());
        }

        // If the configuration deserialization failed create overwrite the configuration with
        // some default configuration.
        CrawlerConfig config = new CrawlerConfig();
        config.oauthtoken = "";
        config.language = "CPP";
        config.lastPushDate =  DateTimeFormatter.ofPattern("YYYY-MM-dd").format(LocalDateTime.now());
        config.starsDecreaseAmount = 1;
        config.buildSystem = "CUSTOM";
        config.customFile = "CHANGELOG.md";
        config.filePath  = "shared";
        config.buildSystems = this.getDefaultBuildSystems();

        try {
            configPath.toFile().getParentFile().mkdirs();
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }

        // Save the created default configuration.
        try(FileWriter fileWriter = new FileWriter(configPath.toFile())) {
            YamlWriter writer = new YamlWriter(fileWriter);
            writer.write(config);
            writer.close();

        } catch(Exception exception) {
            System.err.println(exception.getMessage());
        }

        return config;
    }

    /**
     * @return Returns default build-systems.
     */
    private List getDefaultBuildSystems() {
        List systems = new ArrayList();
        systems.add(new BuildSystem("CMAKE", new String[] { "CMakeLists.txt" }));
        systems.add(new BuildSystem("AUTOTOOLS", new String[] { "configure.ac", "configure.in", "Makefile.am" }));
        systems.add(new BuildSystem("MAKE", new String[] { "Makefile" }));
        systems.add(new BuildSystem("CUSTOM", new String[] { "" }));
        systems.add(new BuildSystem("UNKNOWN", new String[] {}));

        return systems;
    }
}
