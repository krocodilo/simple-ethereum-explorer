package explorer.logic.utils;

import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class HttpsConnection {
    /**
     * Has the sole purpose to execute an HTTPS request and return the raw result if no errors occur
     */

    public static class HttpsConException extends Exception {
        public HttpsConException(Throwable cause) {
            super(cause);
        }
    }

    /**
     * Execute an API call and return the data in JSON format
     * @param url
     * @return JSON data
     * @throws JSONException
     * @throws HttpsConException
     */
    public static JSONObject callAPI(String url) throws JSONException, HttpsConException {

        String resp;
        try{
            resp = httpsCall(url);
        } catch (Exception e){
            throw new HttpsConnection.HttpsConException(e);
        }

        return new JSONObject(resp);
    }

    /**
     * Executes an HTTPS request and returns raw data
     * @param url
     * @return raw data
     * @throws IOException
     */
    private static String httpsCall(String url) throws IOException{

        HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        StringBuilder output = new StringBuilder();
        // Read content:
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            output.append(inputLine);

        in.close();
        return output.toString();
    }

}
