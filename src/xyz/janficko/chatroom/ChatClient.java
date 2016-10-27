package xyz.janficko.chatroom;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class that handles API calls and JSON transformation.
 */
public class ChatClient  {

    private String api_url = "<link_to_api>";

    public ChatClient() {} ;

    /**
     * Retrieves all chat messages from RESTful API call.
     *
     * @return String
     */
    public ArrayList<String> getChatMessages(){
        String output = null;

        try {
            URL url = new URL(api_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                        conn.getInputStream()
                    )
            );

            output = br.readLine();

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return messagesToArrayList(output);
    }

    /**
     *  Send new message through POST call.
     *
     * @param nickname
     * @param message
     */
    public void postChatMessage(String nickname, String message){

        try {
            URL url = new URL(api_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            // Insert both strings to JSON.
            String input = "{\"nickname\":\""+nickname+"\",\"message\":\""+message+"\"}";

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

             if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Transform JSON String into readable ArrayList.
     *
     * @param jsonString
     * @return ArrayList<String>
     */
    public ArrayList<String> messagesToArrayList(String jsonString){
        ArrayList<String> result = new ArrayList<String>();

        Gson gson = new Gson();
        JsonElement element = gson.fromJson (jsonString, JsonElement.class);
        JsonArray jsonArr = element.getAsJsonArray();

        for(JsonElement singleElement : jsonArr){
            String nickname = singleElement.getAsJsonObject().get("nickname").getAsString();
            String message = singleElement.getAsJsonObject().get("message").getAsString();
            String create_at = singleElement.getAsJsonObject().get("created_at").getAsString();

            result.add(nickname + " (" + create_at+ "): " + message);
        }

        return result;
    }
}
