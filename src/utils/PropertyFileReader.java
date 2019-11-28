package utils;

import Models.EConfig;
import main.Config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Simple config file reader.
 *
 * @author Daniel Braun
 */
public class PropertyFileReader {

    // static variable single_instance of type Singleton
    private static PropertyFileReader single_instance = null;

    private Properties prop;

    private PropertyFileReader()  {
        this.prop = new Properties();
        readConfig();
    }

    // static method to create instance of Singleton class
    public static PropertyFileReader getInstance()
    {
        if (single_instance == null)
            single_instance = new PropertyFileReader();

        return single_instance;
    }

    private void readConfig() {
        FileInputStream configFile;
        {
            try {
                configFile = new FileInputStream(System.getProperty("user.dir") + "/config.properties");
                prop.load(configFile);
            } catch (FileNotFoundException e) {
                System.err.println("Can't find config.properties file in path:" + System.getProperty("user.dir"));
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't load properties of config.\nConfig file may be configured incorrectly.");
            }
        }
    }

    public String getProperty(EConfig config){
        return prop.getProperty(config.toString());
    }

}
