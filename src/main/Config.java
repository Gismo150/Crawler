package main;

import Models.BuildSystem;
import Models.EConfig;
import utils.PropertyFileReader;

public class Config {

    public static final String LANGUAGE = null;//PropertyFileReader.getInstance().getProperty(EConfig.LANGUAGE);
    public static final String LASTPUSHEDDATE = null;//PropertyFileReader.getInstance().getProperty(EConfig.LASTPUSHEDDATE);
    public static final String STARSDECREASEAMOUNT = null;//PropertyFileReader.getInstance().getProperty(EConfig.STARSDECREASEAMOUNT);
    public static final BuildSystem BUILDSYSTEM = null;//BuildSystem.getBuildType(PropertyFileReader.getInstance().getProperty(EConfig.BUILDSYSTEM));
    public static final String OAUTHTOKEN = null;//PropertyFileReader.getInstance().getProperty(EConfig.OAUTHTOKEN);
    public static final String FILEPATH = null;//PropertyFileReader.getInstance().getProperty(EConfig.FILEPATH);
    public static final String JSONFILENAME = null;//"repositories.json";
    public static final String CUSTOMFILE = null;//PropertyFileReader.getInstance().getProperty(EConfig.CUSTOMFILE);


}
