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

    public static class ConnectionException extends Exception {
        public ConnectionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class APIException extends Exception {
        public APIException(String message) {
            super(message);
        }
    }

    /**
     * Execute an API call and return the data in JSON format
     * @param url String containing the URL
     * @return JSON data - might be a String, JSONArray, integer, etc... Depends on the request
     * @throws Exception with a message
     */
    public static Object callAPI(String url) throws Exception {

        String resp;
        try{
            resp = httpsCall(url);
        } catch (IOException e){
            throw new ConnectionException("Error in API call.", e);
        }

        JSONObject j;
        try{
            j = new JSONObject(resp);
        } catch (JSONException e){
            throw new ConnectionException("Error reading response from API.", e);
        }

        if(j.getInt("status") == 0) {
            throw new APIException( "Error from API -  " +
                    j.get("message").toString() + "\n" + j.get("result").toString() );
        }

        return j.get("result");     // might be a String, JSONArray, integer, etc... Depends on the request
    }

    /**
     * Executes an HTTPS request and returns raw data
     * @param url String containing the URL
     * @return String with raw response
     * @throws IOException If an error occurs attempting to, or during the connection
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
