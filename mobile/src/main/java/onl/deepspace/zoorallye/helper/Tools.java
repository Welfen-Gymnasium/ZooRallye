package onl.deepspace.zoorallye.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import onl.deepspace.zoorallye.MainActivity;
import onl.deepspace.zoorallye.R;

/**
 * Created by Sese on 03.04.2016.
 */
public class Tools {

    public static final String ZOO_DB = "zooDb";
    public static final String QUESTIONS_DB = "questionsDb";
    public static final String ANSWER_DB = "answerDb";
    public static final String NAME = "name";
    public static final String BEACONS = "beacons";


    public static JSONArray getAnswers(Context context) {
        try {
            FileInputStream stream = context.openFileInput(ANSWER_DB);
            String jsonString = streamToString(stream);
            return new JSONArray(jsonString);
        } catch (IOException | JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
            return null;
        }
    }

    public static JSONObject getQuestions(Context context){
        return getQuestions(context, true);
    }

    public static JSONObject getQuestions(Context context, boolean downloadIfNotExist) {
        try {
            FileInputStream stream = context.openFileInput(QUESTIONS_DB);
            String jsonString = streamToString(stream);
            return new JSONObject(jsonString);
        } catch (IOException | JSONException e) {
            if(downloadIfNotExist) {
                Log.w(Const.LOGTAG, "Try to fetch new question data because of " + e.getMessage());

                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(Const.NAV_FRAGMENT, R.id.nav_offline);
                context.startActivity(intent);
            }

            return null;
        }
    }

    public static JSONArray getZoos(Context context){
        return getZoos(context, true);
    }

    public static JSONArray getZoos(Context context, boolean downloadIfNotExist) {
        try {
            FileInputStream stream = context.openFileInput(ZOO_DB);
            String jsonString = streamToString(stream);
            return new JSONArray(jsonString);
        } catch (IOException | JSONException e) {
            if(downloadIfNotExist){
                Log.w(Const.LOGTAG, "Try to fetch new zoo data because of " + e.getMessage());

                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra(Const.NAV_FRAGMENT, R.id.nav_offline);
                context.startActivity(intent);
            }

            return null;
        }
    }

    private static String streamToString(FileInputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        int character;
        while ((character = stream.read()) != -1) {
            builder.append((char) character);
        }
        stream.close();
        return builder.toString();
    }

    public static void insertAnswer(Context context, JSONObject answer) {
        try {
            JSONArray answers = getAnswers(context);
            answers = answers != null ? answers : new JSONArray();
            answers.put(answer);
            FileOutputStream stream = context.openFileOutput(ANSWER_DB, Context.MODE_PRIVATE);
            String jsonString = answers.toString();
            writeStringToStream(stream, jsonString);
        } catch (IOException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }
    }

    public static void setQuestions(Context context, JSONObject questions) {
        try {
            FileOutputStream stream = context.openFileOutput(QUESTIONS_DB, Context.MODE_PRIVATE);
            String jsonString = questions.toString();
            writeStringToStream(stream, jsonString);
        } catch (IOException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }
    }

    public static void setZoos(Context context, JSONArray zoos) {
        try {
            FileOutputStream stream = context.openFileOutput(ZOO_DB, Context.MODE_PRIVATE);
            String jsonString = zoos.toString();
            writeStringToStream(stream, jsonString);
        } catch (IOException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }
    }

    private static void writeStringToStream(FileOutputStream stream, String string)
            throws IOException {
        stream.write(string.getBytes());
        stream.close();
    }

    public static ArrayList<JSONObject> getEnclosures(Context context, String zooId,
                                                      Location location, int range) {
        ArrayList<JSONObject> returnList = new ArrayList<>();

        try {
            JSONArray zooArray = getZoos(context, false);
            if (zooArray != null) {
                for (int i = 0; i < zooArray.length(); i++) {
                    if (zooArray.getJSONObject(i).get(Const.ZOO_ID).equals(zooId)) {
                        JSONArray beacons = zooArray.getJSONObject(i).getJSONArray(BEACONS);

                        for (int j = 0; j < beacons.length(); j++) {
                            double latitude = (double) beacons.getJSONObject(j).get(Const.ZOO_LAT);
                            double longitude = (double) beacons.getJSONObject(j).get(Const.ZOO_LNG);

                            Location area = new Location("provider");
                            area.setLongitude(longitude);
                            area.setLatitude(latitude);

                            try {
                                if (inArea(location, area, (double) range)) {
                                    returnList.add(beacons.getJSONObject(j));
                                }
                            } catch (Exception e) {
                                Log.e(Const.LOGTAG, e.getMessage());
                            }
                        }
                    }
                }
            }

        } catch (JSONException e) {
            Log.e(Const.LOGTAG, "Invalid zoo JSON!\n" + e.getMessage());
        }

        return returnList;
    }

    public static void requestPermission(Activity activity, String permission, int callback) {

        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, callback);
        }
    }

    public static void deleteAppData(Context context) {
        context.deleteFile(QUESTIONS_DB);
        context.deleteFile(ANSWER_DB);
        context.deleteFile(ZOO_DB);
    }

    public static class ActionBarToggler extends ActionBarDrawerToggle {

        private Runnable runnable;

        public ActionBarToggler(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }
    }

    private static boolean inArea(Location currentPosition, Location area, double range) {

        double beaconRange = range; //m to each side from middle of beacon
        double r_earth = 6378000; //m

        double currentLongitude = currentPosition.getLongitude();
        double currentLatitdude = currentPosition.getLatitude();

        double areaCenterLongitude = area.getLongitude();
        double areaCenterLatitude = area.getLatitude();

        double northSouthRange = (beaconRange / r_earth) * (180 / Math.PI);
        double areaTop = areaCenterLatitude - northSouthRange;
        double areaBottom = areaCenterLatitude + northSouthRange;

        double areaEastWestRange = (beaconRange / r_earth) * (180 / Math.PI)
                / Math.cos(currentLatitdude * Math.PI / 180);
        double areaLeft = areaCenterLongitude - areaEastWestRange;
        double areaRight = areaCenterLongitude + areaEastWestRange;

        boolean inEastWest = areaLeft <= currentLongitude && areaRight >= currentLongitude;
        boolean inNorthSouth = areaTop <= currentLatitdude && areaBottom >= currentLatitdude;

        return (inEastWest && inNorthSouth);
    }

    public static ArrayList<String> jsonArrayToArrayList(JSONArray array) throws JSONException {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }
        return list;
    }

    public static ArrayList<String> string2ArrayList(String string) {
        String[] stringParts = string.split(",");
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, stringParts);
        return list;
    }
}
