package onl.deepspace.zoorallye.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
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

    private static final String ZOO_DB = "zooDb";
    private static final String NAME = "name";
    private static final String BEACONS = "beacons";


    public static JSONArray getZoos(Context context) {
        try {
            FileInputStream stream = context.openFileInput(ZOO_DB);

            StringBuilder builder = new StringBuilder();
            int character;
            while((character = stream.read()) != -1){
                builder.append((char) character);
            }

            stream.close();

            String jsonString = builder.toString();
            Log.d(Const.LOGTAG, jsonString);
            return new JSONArray(jsonString);
        } catch (IOException | JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
            return null;
        }
    }

    public static void setZoos(Context context, JSONArray zoos) {
        try {
            FileOutputStream stream = context.openFileOutput(ZOO_DB, Context.MODE_PRIVATE);
            stream.write(Byte.valueOf(zoos.toString()));
            stream.close();
        } catch (IOException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }
    }

    public static ArrayList<JSONObject> getEnclosures(Context context, String zooId,
                                                      Location location, int range){
        ArrayList<JSONObject> returnList = new ArrayList<>();

        try{
            JSONArray zooArray = getZoos(context);
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
             if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)){
                 // TODO: 11.04.2016 Add dialog to explain the user why this permission is needed
             }

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
}
