package onl.deepspace.zoorallye.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Sese on 03.04.2016.
 */
public class Tools {

    private static final String ZOO_DB = "zooDb";

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
}
