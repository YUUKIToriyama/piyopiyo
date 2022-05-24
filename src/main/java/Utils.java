import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Utils {
    public static JsonNode fetchJson(String urlString) {
        String jsonString = "";
        try {
            URL url = new URL(urlString);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
            boolean hasNext = true;
            while (hasNext) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    hasNext = false;
                } else {
                    jsonString = jsonString + line;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JsonNode json = null;
        try {
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jsonParser = jsonFactory.createParser(jsonString);
            ObjectMapper objectMapper = new ObjectMapper();
            json = objectMapper.readTree(jsonParser);
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}
