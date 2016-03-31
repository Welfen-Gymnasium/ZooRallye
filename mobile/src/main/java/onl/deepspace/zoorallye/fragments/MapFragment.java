package onl.deepspace.zoorallye.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import onl.deepspace.zoorallye.helper.Liane;

/**
 * Created by Sese on 30.03.2016.
 */
public class MapFragment extends Fragment {

    GPSTracker gps;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);

        //Init Lianas
        Liane.addLiane(view);

        //Map
        final ImageView map = (ImageView) view.findViewById(R.id.fragment_map_map);
        //map.setZoom(1.2f); //not working... Why? //TODO Make map-default-zoom work

        //GPS
        gps = new GPSTracker(getContext());

        if(gps.canGetLocation()){
            Toast.makeText(getContext(), gps.getLongitude() + " " + gps.getLatitude(), Toast.LENGTH_LONG).show();
            final double latitude = gps.getLongitude();
            final double longitude = gps.getLatitude();

            //WhereAmI overlay AFTER Inflation
            final ViewOverlay overlay = map.getOverlay();
            map.post(new Runnable() {
                @Override
                public void run() {
                    Log.d(Const.LOGTAG, String.valueOf(view.getWidth() + " " + map.getWidth()));

                    double xRange = (Const.maxLongitude -  Const.minLongitude);
                    double yRange = (Const.maxLatitude - Const.minLatitude);
                    double xStep = map.getWidth() / xRange;
                    double yStep =  map.getHeight() / yRange;

                    double userXRange = (longitude - Const.minLongitude);
                    double userYRange = (Const.maxLatitude - latitude);

                    int xMarker = (int) (userXRange * xStep);
                    int yMarker = (int) (userYRange * yStep);

                    if(xMarker >= 0 && xMarker <= view.getWidth() && yMarker >= 0 && yMarker <= view.getHeight()) {

                        Drawable marker = map.getResources().getDrawable(R.drawable.ic_map_marker);
                        marker.setBounds(xMarker - 50, yMarker - 110, xMarker + 100 - 50, yMarker + 110 - 110); //Better understanding, 100*110 icon with movments
                        overlay.add(marker);
                    }
                    else{
                        Snackbar.make(view, view.getResources().getString(R.string.outside_zoo), Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            gps.showSettingsAlert();
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        gps.stopUsingGPS();
    }
}
