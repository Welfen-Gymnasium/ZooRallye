package onl.deepspace.zoorallye.helper;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import onl.deepspace.zoorallye.MainActivity;
import onl.deepspace.zoorallye.R;

/**
 * Created by Sese on 03.04.2016.
 */
public class Tools {

    private static GoogleApiClient signinClient;

    public static GoogleApiClient getSigninClient(final Activity activity){
        if(signinClient != null){
            return signinClient;
        }
        else{
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(activity)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Toast.makeText(activity, connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                            Log.e(Const.LOGTAG, connectionResult.getErrorMessage());
                        }
                    })
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            signinClient = googleApiClient;
            return signinClient;
        }
    }

    public static boolean requestPermission(Activity activity, String permission, int callback){

        if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
             if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)){
                 // TODO: 11.04.2016 Add dialog to explain the user why this permission is needed
             }

            ActivityCompat.requestPermissions(activity, new String[]{permission}, callback);
        }
        return false;
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
