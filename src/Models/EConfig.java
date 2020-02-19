package Models;

/**
 * Properties that can be read from the config.properties file.
 *
 * @author Daniel Braun
 */

public enum EConfig {
    OAUTHTOKEN("OAuthToken"),
    LANGUAGE("language"),
    LASTPUSHEDDATE("lastPushedDate"),
    STARSDECREASEAMOUNT("starsDecreaseAmount"),
    BUILDSYSTEM("buildSystem"),
    FILEPATH("filePath"),
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
            case "OAuthToken":
                return EConfig.OAUTHTOKEN;
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
            default:
                return EConfig.UNKNOWN;
        }
    }
}

