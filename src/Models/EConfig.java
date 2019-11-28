package Models;

/**
 * Properties that can be read from the config.properties file.
 *
 * @author Daniel Braun
 */

public enum EConfig {
    USERNAME("username"),
    PASSWORD("password"),
    LANGUAGE("language"),
    LASTPUSHEDDATE("lastPushedDate"),
    STARSDECREASEAMOUNT("starsDecreaseAmount"),
    BUILDSYSTEM("buildSystem"),
    FILEPATH("filePath"),
    HOSTPATH("hostPath"),
    CONTAINERPATH("containerPath"),
    ANALSISTOOL("analysisTool"),
    UNKNOWN("unknown");


    private String name;

    EConfig(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static EConfig getEConfig(String name) {
        switch (name) {
            case "username":
                return EConfig.USERNAME;
            case "password":
                return EConfig.PASSWORD;
            case "language":
                return EConfig.LANGUAGE;
            case "lastPushedDate":
                return EConfig.LASTPUSHEDDATE;
            case "starsDecreaseAmount":
                return EConfig.STARSDECREASEAMOUNT;
            case "buildType":
                return EConfig.BUILDSYSTEM;
            case "filePath":
                return EConfig.FILEPATH;
            case "hostPath":
                return EConfig.HOSTPATH;
            case "containerPath":
                return EConfig.CONTAINERPATH;
            case "analysisTool":
                return EConfig.ANALSISTOOL;
            default:
                return EConfig.UNKNOWN;
        }
    }
}

