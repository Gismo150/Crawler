package utils;

import main.Config;
import org.glassfish.jersey.internal.util.Base64;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Utility to query the GitHib API through Curl requests.
 *
 * @author Lisa Nguyen Quang Do & Daniel Braun (modifications)
 */

public class Curl {

    public static String getHTML(String addr) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(addr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        String userCredentials = Config.USERNAME + ':' + Config.PASSWORD;
        String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes()));
        conn.setRequestProperty("Authorization", basicAuth);

        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        rd.close();
        return result.toString();
    }
}
