package onl.deepspace.zoorallye.helper.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.helper.Exceptions;
import onl.deepspace.zoorallye.helper.Variables;

public class AppCompatAchievementActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    private static final int API_REQUEST = 1001;
    private static final int RESOLUTION_CALLBACK = 1002;
    private static final int LEADER_BOARD_CALLBACK = 1003;
    private static final int ACHIEVEMENTS_CALLBACK = 1004;

    private static boolean signedIn = false; //once tried by script itself
    private static Runnable afterLogin = null;

    private static GoogleApiClient googleApiClient;
    private static GoogleApiClient signInClient = null;

    private static final String LOGTAG = "GooglePlayConnection";

    public void unlockAchievement(final String achievementId) throws Exceptions.GooglePlayUnconnectedException{
        if(googleApiClient.isConnected()){
            Log.i(LOGTAG, "Unlocking Achievement: " + achievementId);
            Games.Achievements.unlock(googleApiClient, achievementId);
        }
        else if(!signedIn){
            signIn();
            signedIn = true;
            afterLogin = new Runnable() {
                @Override
                public void run() {
                    try {
                        unlockAchievement(achievementId);
                    } catch (Exceptions.GooglePlayUnconnectedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        else{
            Log.e(LOGTAG, "Google Play Services unconnected!");
            signedIn = false;
            throw new Exceptions.GooglePlayUnconnectedException("Google Play Services unconnected!");
        }
    }

    public void displayAchievements() throws Exceptions.GooglePlayUnconnectedException{
        if(googleApiClient.isConnected()){
            startActivityForResult(Games.Achievements.getAchievementsIntent(googleApiClient), ACHIEVEMENTS_CALLBACK);
        }
        else if(!signedIn){
            signIn();
            signedIn = true;
            afterLogin = new Runnable() {
                @Override
                public void run() {
                    try {
                        displayAchievements();
                    } catch (Exceptions.GooglePlayUnconnectedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        else{
            Log.e(LOGTAG, "Google Play Services unconnected!");
            signedIn = false;
            throw new Exceptions.GooglePlayUnconnectedException("Google Play Services unconnected!");
        }
    }

    public void submitLeaderBoardScore(final String leaderBoardId, final int score) throws Exceptions.GooglePlayUnconnectedException{
        if(googleApiClient.isConnected()){
            Log.i(LOGTAG, "Submitting leaderboardscore: " + leaderBoardId + " -> " + String.valueOf(score));
            Games.Leaderboards.submitScore(googleApiClient, leaderBoardId, score);
        }
        else if(!signedIn) {
            signIn();
            signedIn = true;
            afterLogin = new Runnable() {
                @Override
                public void run() {
                    try {
                        submitLeaderBoardScore(leaderBoardId, score);
                    } catch (Exceptions.GooglePlayUnconnectedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        else{
            Log.e(LOGTAG, "Google Play Services unconnected!");
            signedIn = false;
            throw new Exceptions.GooglePlayUnconnectedException("Google Play Services unconnected!");
        }
    }

    public void displayLeaderBoard(final String leaderBoardId) throws Exceptions.GooglePlayUnconnectedException{
        if(googleApiClient.isConnected()){
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(googleApiClient, leaderBoardId), LEADER_BOARD_CALLBACK);
        }
        else if(!signedIn){
            signIn();
            signedIn = true;
            afterLogin = new Runnable() {
                @Override
                public void run() {
                    try {
                        displayLeaderBoard(leaderBoardId);
                    } catch (Exceptions.GooglePlayUnconnectedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        else{
            Log.e(LOGTAG, "Google Play Services unconnected!");
            signedIn = false;
            throw new Exceptions.GooglePlayUnconnectedException("Google Play Services unconnected!");
        }
    }

    public void signIn(){   googleApiClient.connect();  }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case RESOLUTION_CALLBACK:
                    Log.d(LOGTAG, "ResolutionErrorCallback");
                    if(!googleApiClient.isConnecting() && !googleApiClient.isConnected()){
                        googleApiClient.connect();
                    }
                    break;
                case Const.GoogleAuthIntent:
                    Log.d(LOGTAG, "AuthIntent");
            }
        }
        else{
            Log.e(LOGTAG, "Error onActivityResult?: " + String.valueOf(resultCode));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        int playServiceStatus = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if(playServiceStatus != ConnectionResult.SUCCESS){
            Log.w(LOGTAG, "Google Play Services Error!");
            GoogleApiAvailability.getInstance().getErrorDialog(this, playServiceStatus, API_REQUEST).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(LOGTAG, "Connection Failed: " + String.valueOf(connectionResult));

        if(!(Variables.signInTried && connectionResult.getErrorCode() == ConnectionResult.SIGN_IN_REQUIRED)){
            if(connectionResult.hasResolution()){
                try{
                    Log.d(LOGTAG, "Resolution available");
                    connectionResult.startResolutionForResult(this, RESOLUTION_CALLBACK);
                } catch (IntentSender.SendIntentException e){
                    Log.e(LOGTAG, e.getMessage());
                }

                Variables.signInTried = true;
            }
            else{
                Log.d(LOGTAG, "Resolution unavailable!");
                switch (connectionResult.getErrorCode()){
                    case ConnectionResult.SIGN_IN_REQUIRED: attemptGoogleSignIn(); break;
                    default: GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), RESOLUTION_CALLBACK).show();
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(LOGTAG, "Connected to G-Play!");
        if(afterLogin != null){
            afterLogin.run();
            afterLogin = null;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void attemptGoogleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(getSignInClient(this));
        startActivityForResult(signInIntent, Const.GoogleAuthIntent);
    }

    private static GoogleApiClient getSignInClient(final Activity activity){
        if(signInClient != null){
            return signInClient;
        }
        else{
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            signInClient = new GoogleApiClient.Builder(activity)
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
                            Log.e(LOGTAG, connectionResult.getErrorMessage());
                        }
                    })
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            return signInClient;
        }
    }

}
