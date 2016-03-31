package onl.deepspace.zoorallye.fragments;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.ImageView;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.helper.GPSTracker;
import onl.deepspace.zoorallye.helper.Liane;
import onl.deepspace.zoorallye.helper.interfaces.AsyncTaskCallback;
import onl.deepspace.zoorallye.helper.interfaces.GPSCallback;

/**
 * Created by Sese on 30.03.2016.
 */
public class MapFragment extends Fragment implements GPSCallback, AsyncTaskCallback{

    boolean inZooInformation = false;

    GPSTracker gps;
    View view;
    ImageView map;
    Drawable marker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);

        //Init Lianas
        Liane.addLiane(view);

        //Map
        map = (ImageView) view.findViewById(R.id.fragment_map_map);
        //Marker
        marker = map.getResources().getDrawable(R.drawable.ic_map_marker);
        //gps
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
       setMarkerPostition(location);
    }

    private void setMarkerPostition(final Location location){
        //WhereAmI overlay AFTER Inflation
        final ViewOverlay overlay = map.getOverlay();
        map.post(new Runnable() {
            @Override
            public void run() {
                //Log.d(Const.LOGTAG, String.valueOf(view.getWidth() + " " + map.getWidth()));
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();

                double xRange = (Const.maxLongitude -  Const.minLongitude);
                double yRange = (Const.maxLatitude - Const.minLatitude);
                double xStep = map.getWidth() / xRange;
                double yStep =  map.getHeight() / yRange;

                double userXRange = (longitude - Const.minLongitude);
                double userYRange = (Const.maxLatitude - latitude);

                int xMarker = (int) (userXRange * xStep);
                int yMarker = (int) (userYRange * yStep);

                if(xMarker >= 0 && xMarker <= view.getWidth() && yMarker >= 0 && yMarker <= view.getHeight()) {
                    marker.setBounds(xMarker - 50, yMarker - 110, xMarker + 100 - 50, yMarker + 110 - 110); //Better understanding, 100*110 icon with movements
                    overlay.add(marker);
                }
                else if(!inZooInformation){
                    Snackbar.make(view, view.getResources().getString(R.string.outside_zoo), Snackbar.LENGTH_LONG).show();
                    inZooInformation = true;
                }
            }
        });
    }

    @Override
    public void asyncTaskCallback(Object object) {
        Location l = (Location) object;
        setMarkerPostition(l);
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

                setMarkerPostition(l);
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

