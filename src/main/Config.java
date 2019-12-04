package main;

import Models.BuildSystem;
import Models.EConfig;
import utils.PropertyFileReader;

public class Config {

    public static final String LANGUAGE = PropertyFileReader.getInstance().getProperty(EConfig.LANGUAGE);
    public static final String LASTPUSHEDDATE = PropertyFileReader.getInstance().getProperty(EConfig.LASTPUSHEDDATE);
    public static final String STARSDECREASEAMOUNT = PropertyFileReader.getInstance().getProperty(EConfig.STARSDECREASEAMOUNT);
    public static final BuildSystem BUILDSYSTEM = BuildSystem.getBuildType(PropertyFileReader.getInstance().getProperty(EConfig.BUILDSYSTEM));
    public static final String OAUTHTOKEN = PropertyFileReader.getInstance().getProperty(EConfig.OAUTHTOKEN);
    public static final String FILEPATH = PropertyFileReader.getInstance().getProperty(EConfig.FILEPATH);
    public static final String JSONFILENAME = "repositories.json";
    public static final String RESULTFILENAME = "results.json";
    public static  final String HOSTPATH = PropertyFileReader.getInstance().getProperty(EConfig.HOSTPATH);
    public static  final String CONTAINERPATH = PropertyFileReader.getInstance().getProperty(EConfig.CONTAINERPATH);
    public static  final String ANALYSISTOOL = PropertyFileReader.getInstance().getProperty(EConfig.ANALSISTOOL);


}
