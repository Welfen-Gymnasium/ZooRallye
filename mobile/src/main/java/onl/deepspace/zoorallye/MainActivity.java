package onl.deepspace.zoorallye;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;
import org.json.JSONObject;

import onl.deepspace.zoorallye.fragments.AboutFragment;
import onl.deepspace.zoorallye.fragments.DonationFragment;
import onl.deepspace.zoorallye.fragments.InfoFragment;
import onl.deepspace.zoorallye.fragments.OfflineContentFragment;
import onl.deepspace.zoorallye.fragments.StatisticsFragment;
import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.helper.Exceptions;
import onl.deepspace.zoorallye.helper.Liana;
import onl.deepspace.zoorallye.helper.Tools;
import onl.deepspace.zoorallye.helper.activities.AppCompatAchievementActivity;

public class MainActivity extends AppCompatAchievementActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        InfoFragment.InfoFragmentCommunication {

    Tools.ActionBarToggler actionBarToggler;
    IInAppBillingService mService;
    ServiceConnection mServiceConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Lianas
        Liana.addLiana(findViewById(R.id.content_frame));

        // Opens a specific fragment if defined
        int fragment = getIntent().getIntExtra(Const.NAV_FRAGMENT, 0);
        if (fragment != 0) {
            openFragment(getFragmentByID(fragment));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarToggler = new Tools.ActionBarToggler(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        assert drawer != null;
        //noinspection deprecation
        drawer.setDrawerListener(actionBarToggler);
        actionBarToggler.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        //In App Billing
        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
            }
        };
    }

    @Override
    public void onStopRally() {
        // do nothing, as rally is not active
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_about:
                openFragment(getFragmentByID(R.id.nav_about));
                return true;
            case R.id.action_settings:
                Toast.makeText(MainActivity.this, R.string.coming_soon, Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            //int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String info = getResources().getString(R.string.donation_something_went_wrong);

            if (resultCode == RESULT_OK) {
                info = getResources().getString(R.string.donation_thank_you);
            }

            //consume purchase
            try{
                final IInAppBillingService service = mService;
                final String token = new JSONObject(
                        data.getStringExtra("INAPP_PURCHASE_DATA "))
                        .getString("purchaseToken");

                new AsyncTask<Object, Object, Object>(){
                    @Override
                    protected Object doInBackground(Object... params) {
                        try {
                            int response = service.consumePurchase(3, getPackageName(), token);

                            if(response == RESULT_OK){
                                Log.i(Const.LOGTAG, "Purchase consumed!");
                            }
                            else{
                                Log.i(Const.LOGTAG, "Purchase not consumed " + String.valueOf(response));
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }.execute();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }

            Snackbar.make(findViewById(R.id.drawer_layout), info, Snackbar.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        final Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_rally:
                intent = new Intent(this, RallyActivity.class);
                actionBarToggler.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                    }
                });
                break;
            default:
                final Fragment fragment = getFragmentByID(item.getItemId());
                actionBarToggler.runWhenIdle(new Runnable() {
                    @Override
                    public void run() {
                        openFragment(fragment);
                    }
                });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }

    public void donate() {
        try {
            Intent serviceIntent =
                    new Intent("com.android.vending.billing.InAppBillingService.BIND");
            serviceIntent.setPackage("com.android.vending");
            bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

            if(mService != null){
                Bundle buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
                        "zoo_rallye_donation", "inapp", "");
                PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                startIntentSenderForResult(pendingIntent.getIntentSender(),
                        1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                        Integer.valueOf(0));
            }
            else{
                Log.d(Const.LOGTAG, "Failed to bind billing-service");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    public void openFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }

    public Fragment getFragmentByID(int id) {
        switch (id) {
            case R.id.nav_statistics:
                return new StatisticsFragment();
            case R.id.nav_donate:
                return new DonationFragment();
            case R.id.nav_offline:
                return new OfflineContentFragment();
            case R.id.nav_about:
                return new AboutFragment();
            case R.id.nav_info:
                return new InfoFragment();
            default:
                return null;
        }
    }
}
