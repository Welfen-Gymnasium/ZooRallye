package onl.deepspace.zoorallye.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.ImageView;
import android.widget.Toast;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.helper.GPSTracker;
import onl.deepspace.zoorallye.helper.Tools;
import onl.deepspace.zoorallye.helper.activities.AppCompatAchievementActivity;
import onl.deepspace.zoorallye.helper.interfaces.AsyncTaskCallback;
import onl.deepspace.zoorallye.helper.interfaces.GPSCallback;

/**
 * Created by Sese on 30.03.2016.
 *
 * Fragment for the zoo maps
 */
public class MapFragment extends Fragment implements GPSCallback, AsyncTaskCallback{

    private boolean inZooInformation = false;
    private final int locationCallback = 1;

    GPSTracker gps;
    View view;
    ImageView map;
    Drawable marker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);

        //Init Lianas
        // Liana.addLiana(view);

        //Map
        map = (ImageView) view.findViewById(R.id.fragment_map_map);
        //Marker
        marker = ResourcesCompat.getDrawable(map.getResources(), R.drawable.ic_map_marker, null);
        //GPS
        Tools.requestPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION, locationCallback);
        gps = new GPSTracker(getActivity(), this);


        //Start AsyncTask with location callback
        new GetPosition().execute(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        gps.stopUsingGPS();
    }

    @Override
    public void GPSLocationChanged(Location location) {
       setMarkerPosition(location);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case locationCallback:
                if(PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(getActivity(), "GPS Permission denied!", Toast.LENGTH_SHORT).show(); // TODO: 11.04.2016 String ressource
                }
                else{ Log.d(Const.LOGTAG, "GPS Permission granted");
                }
                break;
        }
    }

    private void setMarkerPosition(final Location location){
        //WhereAmI overlay AFTER Inflation
        Log.d(Const.LOGTAG, String.valueOf(Tools.getEnclosures(getContext(), location, 20)));

        final ViewOverlay overlay;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            overlay = map.getOverlay();

            map.post(new Runnable() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void run() {
                    //Log.d(Const.LOGTAG, String.valueOf(view.getWidth() + " " + map.getWidth()));
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();

                    double xRange = (Const.maxLongitude - Const.minLongitude);
                    double yRange = (Const.maxLatitude - Const.minLatitude);
                    double xStep = map.getWidth() / xRange;
                    double yStep = map.getHeight() / yRange;

                    double userXRange = (longitude - Const.minLongitude);
                    double userYRange = (Const.maxLatitude - latitude);

                    int xMarker = (int) (userXRange * xStep);
                    int yMarker = (int) (userYRange * yStep);

                    if (xMarker >= 0 && xMarker <= view.getWidth() && yMarker >= 0 && yMarker <= view.getHeight()) {

                        try {
                            ((AppCompatAchievementActivity) getActivity()).unlockAchievement(getResources().getString(R.string.achievement_welcome_to_zoo));
                        } catch (Exception e) {
                            Log.e(Const.LOGTAG, e.getMessage());
                        }

                        marker.setBounds(xMarker - 50, yMarker - 110, xMarker + 100 - 50, yMarker + 110 - 110); //Better understanding, 100*110 icon with movements
                        overlay.add(marker);
                    } else if (!inZooInformation) {
                        Log.d(Const.LOGTAG, longitude + " " + latitude);
                        Snackbar.make(view, view.getResources().getString(R.string.outside_zoo), Snackbar.LENGTH_LONG).show();
                        inZooInformation = true;
                    }
                }
            });
        }
    }

    @Override
    public void asyncTaskCallback(Object object) {
        Location l = (Location) object;
        setMarkerPosition(l);
    }

    private class GetPosition extends AsyncTask<AsyncTaskCallback, Void, Location>{

        private AsyncTaskCallback asyncTaskCallback;

        @Override
        protected Location doInBackground(AsyncTaskCallback... params) {
            this.asyncTaskCallback = params[0];
            Location l = null;

            if(gps.canGetLocation()){
                //Toast.makeText(getContext(), gps.getLongitude() + " " + gps.getLatitude(), Toast.LENGTH_LONG).show();
                l = new Location("prov");
                l.setLongitude( gps.getLongitude() );
                l.setLatitude( gps.getLatitude() );

                setMarkerPosition(l);
            }
            else{
                gps.showSettingsAlert();
            }
            return l;
        }

        @Override
        protected void onPostExecute(Location location) {
            super.onPostExecute(location);
            if(location != null){
                asyncTaskCallback.asyncTaskCallback(location);
            }
        }
    }
}

