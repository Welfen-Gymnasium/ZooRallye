package onl.deepspace.zoorallye.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.helper.Tools;

/**
 * Created by Sese on 03.04.2016.
 */
public class Achievements implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    public GoogleApiClient googleApiClient;
    private Activity activity;

    public static final int WELCOME_TO_ZOO = 1;

    public Achievements(Activity activity){
        this.activity = activity;

        googleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .build();
    }

    public void unlockAchievement(int achievementID){
        String intID;
        switch (achievementID){
            case WELCOME_TO_ZOO: intID = "CgkI17SSxeMPEAIQA"; break;
            default: intID = null;
        }
        if(intID != null){
            Games.Achievements.unlock(googleApiClient, intID);
        }
    }

    private void signIn(GoogleApiClient signinClient) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(signinClient);
        activity.startActivityForResult(signInIntent, 0);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if(connectionResult.getErrorCode() == ConnectionResult.SIGN_IN_REQUIRED){
            signIn(Tools.getSigninClient(activity));
        }
        Toast.makeText(activity, connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
        Log.e(Const.LOGTAG, connectionResult.getErrorMessage());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(Const.LOGTAG, "Play Service (Games) Connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
    }
}
