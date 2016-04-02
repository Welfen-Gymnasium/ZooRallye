package onl.deepspace.zoorallye.helper;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sese on 30.03.2016.
 *
 * This class is for connection to online databases
 */
@SuppressWarnings("unused")
public final class Database {

    public static String getQuestions() {
        String urlString = Const.QuestionsAPI;
        String result = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000/*ms*/);
            connection.setConnectTimeout(15000/*ms*/);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            JSONObject body = new JSONObject();
            body.put(Const.QuestionsAPI_token, Const.API_TOKEN);

            byte[] outputInBytes = body.toString().getBytes(Const.CHAR_ENCODING);
            OutputStream os = connection.getOutputStream();
            os.write(outputInBytes);
            os.close();

            connection.connect();

            int responseCode = connection.getResponseCode();
            Log.d(Const.LOGTAG, "Response code: " + responseCode);
            InputStream in = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in, Const.CHAR_ENCODING);
            char[] buffer = new char[Const.MAX_RESPONSE_LENGTH];
            int charsRead;
            do {
                charsRead = reader.read(buffer);
                String resultPart = String.valueOf(buffer);
                result += resultPart;
                buffer = new char[Const.MAX_RESPONSE_LENGTH];
            } while (charsRead > -1);

        } catch (IOException | JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }
        return result;
    }
}
