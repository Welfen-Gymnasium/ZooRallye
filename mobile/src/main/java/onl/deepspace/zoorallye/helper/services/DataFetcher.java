package onl.deepspace.zoorallye.helper.services;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.helper.Tools;

/**
 * Created by Sese on 18.04.2016.
 */
public class DataFetcher extends IntentService {

    /*
    ! Implement DataFetcher.DownloadResultReceiver.Receiver !

    Get data via

        DataFetcher.DownloadResultReceiver mReceiver;

        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, DataFetcher.class);

        // Send optional extras to Download IntentService
        intent.putExtra("url", url);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 101);

        startService(intent);


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case DataFetcher.STATUS_RUNNING:
                break;
            case DataFetcher.STATUS_FINISHED:
                // Handle result
                String result = resultData.getString(DataFetcher.RESULT);
                break;
            case DataFetcher.STATUS_ERROR:
                // Handle the error
                String error = resultData.getString(Intent.EXTRA_TEXT);
                break;
        }
    }*/

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    public static final String RESULT = "result";

    private static final String LOGTAG = "DownloadService";

    public DataFetcher() {
        super(DataFetcher.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(LOGTAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String url = intent.getStringExtra("url");

        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(url)) {
            /* Update UI: Download Service is Running */
            // receiver.send(STATUS_RUNNING, Bundle.EMPTY);

            try {
                String result = downloadData(url);

                /* Sending result back to activity */
                if (url.contains(Const.ZOOS_API)) {
                    //JSONArray zoos = Tools.getZoos(this);
                    JSONArray fetchedZoos = new JSONArray(result);
                    /*if (zoos != null) {
                        for (int i = 0; i < fetchedZoos.length(); i++) {
                            boolean wasCached = false;
                            JSONObject fetchedZoo = fetchedZoos.getJSONObject(i);
                            String fetchedId = fetchedZoo.getString(Const.ZOO_ID);

                            for (int j = 0; j < zoos.length(); j++) {
                                JSONObject zoo = zoos.getJSONObject(j);
                                String id = zoo.getString(Const.ZOO_ID);

                                if (id.equals(fetchedId)) {
                                    zoos.put(j, fetchedZoo);
                                    wasCached = true;
                                    break;
                                }
                            }
                            if (!wasCached) {
                                zoos.put(fetchedZoo);
                            }
                        }
                    }*/
                    Tools.setZoos(this, fetchedZoos);
                    //Tools.setZoos(this, (zoos != null) ? zoos : fetchedZoos);
                } else if (url.contains(Const.QuestionsAPI)) {
                    Tools.setQuestions(this, new JSONObject(result));
                }

                bundle.putString(RESULT, result);
                receiver.send(STATUS_FINISHED, bundle);

            } catch (NullPointerException e) {
                Log.i(LOGTAG, "No receiver to send back found!");
            } catch (Exception e) {
                /* Sending error message back to activity */
                e.printStackTrace();
                Log.e(LOGTAG, e.getMessage());
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                if(receiver != null) receiver.send(STATUS_ERROR, bundle);
            }
        }
        Log.d(LOGTAG, "Service Stopping!");
        this.stopSelf();
    }

    private String downloadData(String requestUrl) throws IOException, DownloadException {
        // TODO: 25.04.2016 Show notification when fetching data with progress how much percent already downloaded
        InputStream inputStream;
        HttpURLConnection urlConnection;

        /* forming th java.net.URL object */
        URL url = new URL(requestUrl);
        urlConnection = (HttpURLConnection) url.openConnection();

        /* optional request header */
        urlConnection.setRequestProperty("Content-Type", "application/json");

        /* optional request header */
        urlConnection.setRequestProperty("Accept", "application/json");

        urlConnection.setRequestProperty("token",
                "x9j7qtRUOrGGg5fxHakK1sp6T4h9JbJjL44iulAKA8HPbbmBWd2QUEQpFD35i");

        /* for Get request */
        urlConnection.setRequestMethod("GET");
        int statusCode = urlConnection.getResponseCode();

        /* 200 represents HTTP OK */
        if (statusCode == 200) {
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            String response = convertInputStreamToString(inputStream);
            return response;
        } else {
            throw new DownloadException("Failed to fetch data!!");
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

            /* Close Stream */
        if (null != inputStream) {
            inputStream.close();
        }

        return result;
    }

    public class DownloadException extends Exception {

        public DownloadException(String message) {
            super(message);
        }

        public DownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @SuppressLint("ParcelCreator")
    public static class DownloadResultReceiver extends ResultReceiver {

        private Receiver mReceiver;

        public DownloadResultReceiver(Handler handler) {
            super(handler);
        }

        public void setReceiver(Receiver receiver) {
            mReceiver = receiver;
        }

        public interface Receiver {
            void onReceiveResult(int resultCode, Bundle resultData);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (mReceiver != null) {
                mReceiver.onReceiveResult(resultCode, resultData);
            }
        }
    }
}
