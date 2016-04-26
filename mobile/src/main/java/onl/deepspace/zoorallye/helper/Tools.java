package onl.deepspace.zoorallye.helper;

import android.app.Activity;
import android.content.Context;
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

    public static JSONObject getQuestions(Context context) {
        try {
            FileInputStream stream = context.openFileInput(QUESTIONS_DB);
            String jsonString = streamToString(stream);
            return new JSONObject(jsonString);
        } catch (IOException | JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
            return null;
        }
    }

    public static JSONArray getZoos(Context context) {
        try {
            FileInputStream stream = context.openFileInput(ZOO_DB);
            String jsonString = streamToString(stream);
            return new JSONArray(jsonString);
        } catch (IOException | JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
            return null;
        }
    }

    private static String streamToString(FileInputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        int character;
        while((character = stream.read()) != -1){
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
                                                      Location location, int range){
        ArrayList<JSONObject> returnList = new ArrayList<>();

        try{
            JSONArray zooArray = new JSONArray("[{\"name\":\"Zoo Augsburg\",\"id\":\"4P1shyVmM4\",\"beacons\":[{\"type\":\"open-air enclosure\",\"latitude\":48.347018,\"longitude\":10.914766,\"animals\":[\"alpaca\",\"american rhea\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347336,\"longitude\":10.914838,\"animals\":[\"nilgai\",\"antelope\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.34731,\"longitude\":10.91504,\"animals\":[\"rabbit\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347455,\"longitude\":10.915163,\"animals\":[\"cavy\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347861,\"longitude\":10.914758,\"animals\":[\"donkey\",\"miniature donkey\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348067,\"longitude\":10.915548,\"animals\":[\"dahomey cow\",\"cow\",\"pony\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347945,\"longitude\":10.915042,\"animals\":[\"goat\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347779,\"longitude\":10.914616,\"animals\":[\"leopard\"]},{\"type\":\"animal house\",\"latitude\":48.347969,\"longitude\":10.914485,\"animals\":[\"tiger\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348477,\"longitude\":10.914232,\"animals\":[\"sumatran tiger\",\"tiger\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348349,\"longitude\":10.915138,\"animals\":[\"fennec\"]},{\"type\":\"animal house\",\"latitude\":48.34846,\"longitude\":10.915103,\"animals\":[\"lion\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348746,\"longitude\":10.915457,\"animals\":[\"lion\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348831,\"longitude\":10.914893,\"animals\":[\"leopard cat\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348674,\"longitude\":10.914644,\"animals\":[\"hyena\",\"striped hyena\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348993,\"longitude\":10.915271,\"animals\":[\"hamadryas baboon\",\"baboon\",\"red buffalo\",\"buffalo\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348789,\"longitude\":10.914536,\"animals\":[\"onager\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348602,\"longitude\":10.91419,\"animals\":[\"ibis\",\"glossy ibis\",\"scarlet ibis\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348568,\"longitude\":10.913813,\"animals\":[\"tropic birds\"]},{\"type\":\"animal house\",\"latitude\":48.348524,\"longitude\":10.91359,\"animals\":[\"tropic birds\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348394,\"longitude\":10.913034,\"animals\":[\"birds\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.3584,\"longitude\":10.913627,\"animals\":[\"sea bears\",\"seal\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348271,\"longitude\":10.913307,\"animals\":[\"magellanic penguin\",\"penguin\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348147,\"longitude\":10.912811,\"animals\":[\"seal\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348204,\"longitude\":10.912517,\"animals\":[\"capuchin\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348086,\"longitude\":10.912538,\"animals\":[\"degus\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347874,\"longitude\":10.912866,\"animals\":[\"coati\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347491,\"longitude\":10.913079,\"animals\":[\"maned wolf\",\"wolf\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347776,\"longitude\":10.913067,\"animals\":[\"flamingo\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347276,\"longitude\":10.913167,\"animals\":[\"crane\",\"duck\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347195,\"longitude\":10.913362,\"animals\":[\"buffalo\",\"water buffalo\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346919,\"longitude\":10.913379,\"animals\":[\"bennetts wallaby\",\"kangaroo\",\"emu\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346826,\"longitude\":10.913223,\"animals\":[\"water fowl\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346588,\"longitude\":10.913543,\"animals\":[\"stork\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346362,\"longitude\":10.913938,\"animals\":[\"alpine ibex\",\"ibex\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346081,\"longitude\":10.914131,\"animals\":[\"otter\",\"beaver\",\"native fish\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346037,\"longitude\":10.914411,\"animals\":[\"crane\",\"duck\",\"cormorant\",\"muntjac\",\"chinese muntjac\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346481,\"longitude\":10.914289,\"animals\":[\"east caucasian tur\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.345952,\"longitude\":10.914755,\"animals\":[\"mandrill\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346207,\"longitude\":10.915288,\"animals\":[\"ring tailed lemur\",\"lemur\",\"black lemur\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.345558,\"longitude\":10.91537,\"animals\":[\"rhino\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.345185,\"longitude\":10.915589,\"animals\":[\"pelican\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.344809,\"longitude\":10.915945,\"animals\":[\"brown bear\",\"bear\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.344523,\"longitude\":10.916289,\"animals\":[\"great gray owl\",\"owl\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.344394,\"longitude\":10.916471,\"animals\":[\"duck\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.344389,\"longitude\":10.916887,\"animals\":[\"lesser flamingo\",\"flamingo\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.344351,\"longitude\":10.917457,\"animals\":[\"mountain goat\",\"goat\"]},{\"type\":\"animal house\",\"latitude\":48.344735,\"longitude\":10.916964,\"animals\":[\"meerkat\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.34518,\"longitude\":10.917804,\"animals\":[\"grevys zebra\",\"zebra\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.345041,\"longitude\":10.917944,\"animals\":[\"mishmi takin\",\"takin\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.345415,\"longitude\":10.918084,\"animals\":[\"chinese muntjac\",\"muntjac\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.345735,\"longitude\":10.918176,\"animals\":[\"east caucasian tur\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346477,\"longitude\":10.917143,\"animals\":[\"asian elephant\",\"elephant\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346054,\"longitude\":10.915853,\"animals\":[\"ostrich\",\"african ostrich\"]},{\"type\":\"animal house\",\"latitude\":48.34649,\"longitude\":10.916789,\"animals\":[\"elephant\",\"chimpanzee\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346357,\"longitude\":10.916412,\"animals\":[\"chimpanzee\"]},{\"type\":\"animal house\",\"latitude\":48.346357,\"longitude\":10.916512,\"animals\":[\"reptiles\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346366,\"longitude\":10.916025,\"animals\":[\"native snake\",\"snake\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346449,\"longitude\":10.915697,\"animals\":[\"native amphibian\",\"amphibian\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346602,\"longitude\":10.915334,\"animals\":[\"sulcata tortoise\",\"tortoise\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346602,\"longitude\":10.915334,\"animals\":[\"rhino\",\"blesbok\",\"ground hornbill\"]}],\"openingHours\":[{\"period\":\"january\",\"opened\":\"16.30\"},{\"period\":\"february\",\"opened\":\"16.30\"},{\"period\":\"march\",\"opened\":\"17.00\"},{\"period\":\"april\",\"opened\":\"18.00\"},{\"period\":\"may\",\"opened\":\"18.00\"},{\"period\":\"june\",\"opened\":\"18.30\"},{\"period\":\"july\",\"opened\":\"18.30\"},{\"period\":\"august\",\"opened\":\"18.30\"},{\"period\":\"september\",\"opened\":\"18.00\"},{\"period\":\"october\",\"opened\":\"17.00\"},{\"period\":\"november\",\"opened\":\"16.30\"},{\"period\":\"december\",\"opened\":\"16.30\"}],\"pricing\":[{\"period\":\"01.03. - 31.10.\",\"prices\":[{\"group\":\"adults\",\"price\":\"10\\u20ac\"},{\"group\":\"children\",\"age\":\"<3;\",\"price\":\"free\"},{\"group\":\"children\",\"age\":\">3;<15;\",\"price\":\"5\\u20ac\"},{\"group\":\"discounted\",\"price\":\"9\\u20ac\"},{\"group\":\"dogs\",\"price\":\"3\\u20ac\"}]},{\"period\":\"01.11. - 28.02.\",\"prices\":[{\"group\":\"adults\",\"price\":\"8\\u20ac\"},{\"group\":\"children\",\"age\":\"<3;\",\"price\":\"free\"},{\"group\":\"children\",\"age\":\">3;<15;\",\"price\":\"4\\u20ac\"},{\"group\":\"discounted\",\"price\":\"7\\u20ac\"},{\"group\":\"dogs\",\"price\":\"3\\u20ac\"}]}]}]");
                    // TODO: 26.04.2016 getZoos(context);
            if (zooArray != null) {
                for (int i = 0; i < zooArray.length(); i++) {
                    if(zooArray.getJSONObject(i).get(Const.ZOO_ID).equals(zooId)){
                        JSONArray beacons = zooArray.getJSONObject(i).getJSONArray(BEACONS);

                        for (int j = 0; j < beacons.length(); j++) {
                            double latitude = (double) beacons.getJSONObject(j).get(Const.ZOO_LAT);
                            double longitude = (double) beacons.getJSONObject(j).get(Const.ZOO_LNG);

                            Location area = new Location("provider");
                            area.setLongitude(longitude);
                            area.setLatitude(latitude);

                            try{
                                if(inArea(location, area, (double) range)){
                                    returnList.add(beacons.getJSONObject(j));
                                }
                            } catch (Exception e){
                                Log.e(Const.LOGTAG, e.getMessage());
                            }
                        }
                    }
                }
            }

        } catch (JSONException e){
            Log.e(Const.LOGTAG, "Invalid zoo JSON!\n" + e.getMessage());
        }

        return returnList;
    }

    public static void requestPermission(Activity activity, String permission, int callback){

        if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{permission}, callback);
        }
    }

    public static class ActionBarToggler extends ActionBarDrawerToggle{

        private Runnable runnable;

        public ActionBarToggler (Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes){
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
            if (runnable != null && newState == DrawerLayout.STATE_IDLE){
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable){
            this.runnable = runnable;
        }
    }

    private static boolean inArea(Location currentPosition, Location area, double range){

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
                / Math.cos(currentLatitdude * Math.PI/180);
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
}
