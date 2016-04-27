package onl.deepspace.zoorallye.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import onl.deepspace.zoorallye.R;
import onl.deepspace.zoorallye.helper.Const;
import onl.deepspace.zoorallye.helper.Question;
import onl.deepspace.zoorallye.helper.Tools;
import onl.deepspace.zoorallye.helper.activities.AppCompatAchievementActivity;
import onl.deepspace.zoorallye.helper.interfaces.AsyncTaskCallback;
import onl.deepspace.zoorallye.helper.interfaces.BeaconListener;
import onl.deepspace.zoorallye.helper.interfaces.GPSCallback;
import onl.deepspace.zoorallye.lib.GPSTracker;
import onl.deepspace.zoorallye.lib.ZoomView;

/**
 * Created by Sese on 30.03.2016.
 * <p/>
 * Fragment for the zoo maps
 */
public class MapFragment extends Fragment implements GPSCallback, AsyncTaskCallback {

    private boolean inZooInformation = false;
    private boolean markerCreated = false;
    private final int locationCallback = 1;

    private BeaconListener beaconListener;
    private GPSTracker gps;
    private View view;

    private ImageView map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        //Map
        ZoomView mapZoomView = (ZoomView) view.findViewById(R.id.fragment_map_zoom_view);
        mapZoomView.setMaxZoom(3f);

        map = (ImageView) view.findViewById(R.id.fragment_map_map);

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
        checkForBeacon(location);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case locationCallback:
                if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(
                        getContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.gps_permission_denied),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(Const.LOGTAG, "GPS Permission granted");
                }
                break;
        }
    }

    @Override
    public void asyncTaskCallback(Object object) {
        Location l = (Location) object;

        JSONArray zoos = Tools.getZoos(getContext(), false);
        if(zoos != null) createBeacons(zoos);

        setMarkerPosition(l);
        checkForBeacon(l);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BeaconListener) {
            beaconListener = (BeaconListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement BeaconListener");
        }
    }

    private void checkForBeacon(Location location) {
        checkForBeacon(location, 1);
    }

    private void checkForBeacon(Location location, int range) {
        ArrayList<JSONObject> nearBeacons = Tools.getEnclosures(getContext(), "4P1shyVmM4", location, range); //near beacons in range

        if (nearBeacons.size() == 0 && range < 16) {
            checkForBeacon(location, range + 1);
        } else if (nearBeacons.size() >= 1) {
            ((OverlayShow) getActivity()).onOverlayShow(nearBeacons.get(0), null);
        }
    }

    private void setMarkerPosition(final Location location) {
        //Log.d(Const.LOGTAG, String.valueOf(view.getWidth() + " " + map.getWidth()));
        Rect pos = calculateMarkerPosition(location, 100, 110, map.getWidth(), map.getHeight());

        int xMarker = pos.centerX();
        int yMarker = pos.bottom;

        if (xMarker > 0 && xMarker <= view.getWidth() && yMarker > 0 &&
                yMarker <= view.getHeight()) {

            try {
                ((AppCompatAchievementActivity) getActivity())
                        .unlockAchievement(getResources().getString(R.string.achievement_welcome_to_zoo));
            } catch (Exception e) {
                Log.e(Const.LOGTAG, e.getMessage());
            }

            FrameLayout.LayoutParams vp = new FrameLayout.LayoutParams(
                    map.getWidth() / 10, map.getWidth() / 10);
            vp.setMargins(pos.left, pos.top, 0, 0);

            if (!markerCreated) {
                final ImageView posMarker = new ImageView(getContext());

                posMarker.setLayoutParams(vp);
                posMarker.setImageResource(R.drawable.ic_map_marker);
                posMarker.setId(R.id.map_marker_id);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    posMarker.setElevation(2f);
                    posMarker.setTranslationZ(2f);
                }

                // Only the original thread that created a view hierarchy can touch its views.
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((FrameLayout) getActivity().findViewById(R.id.fragment_map_map_container)).addView(posMarker);
                    }
                });

                markerCreated = true;
            } else {
                getActivity().findViewById(R.id.map_marker_id).setLayoutParams(vp);
            }

        } else if (!inZooInformation) {
            Snackbar.make(view, view.getResources().getString(R.string.outside_zoo),
                    Snackbar.LENGTH_LONG).show();
            inZooInformation = true;
        }
    }

    private Rect calculateMarkerPosition(Location location, int width, int height, int refWidth, int refHeight) {
        Rect rect = new Rect();
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        double xRange = (Const.maxLongitude - Const.minLongitude);
        double yRange = (Const.maxLatitude - Const.minLatitude);
        double xStep = refWidth / xRange;
        double yStep = refHeight / yRange;

        double userXRange = (longitude - Const.minLongitude);
        double userYRange = (Const.maxLatitude - latitude);

        int xMarker = (int) (userXRange * xStep);
        int yMarker = (int) (userYRange * yStep);

        //Marker position is width center and height bottom bound
        rect.set(xMarker - width / 2, yMarker - height, xMarker + width / 2, yMarker);

        return rect;
    }

    private void createBeacons(JSONArray zoos) {

        try {
            for (int i = 0; i < zoos.length(); i++) {
                if (zoos.getJSONObject(i).get(Const.ZOO_ID).equals("4P1shyVmM4")) {
                    JSONObject zoo = zoos.getJSONObject(i);
                    for (int h = 0; h < zoo.getJSONArray("beacons").length(); h++) {
                        JSONObject enc = zoo.getJSONArray("beacons").getJSONObject(h);
                        final Location loc = new Location("beacon");

                        loc.setLatitude(enc.getDouble("latitude"));
                        loc.setLongitude(enc.getDouble("longitude"));

                        int refX = map.getWidth();//getActivity().findViewById(R.id.rally_content).getWidth()
                        //- ((ViewGroup.MarginLayoutParams) map.getLayoutParams()).leftMargin
                        //- ((ViewGroup.MarginLayoutParams) map.getLayoutParams()).rightMargin;
                        int refY = map.getHeight();

                        Rect rect = calculateMarkerPosition(loc, 24, 24, refX, refY);
                        int x = rect.left;
                        int y = rect.top;

                        ImageView encMarker = new ImageView(getContext());
                        FrameLayout.LayoutParams vp = new FrameLayout.LayoutParams(
                                refX / 20, refX / 20);
                        vp.setMargins(x, y, 0, 0);
                        encMarker.setLayoutParams(vp);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            encMarker.setElevation(2f);
                            encMarker.setTranslationZ(2f);
                        }
                        int icon = enc.getString("type").equals("animal house") ? R.drawable.ic_animal_house : R.drawable.ic_enclosure;
                        encMarker.setImageResource(icon);
                        encMarker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                beaconListener.onBeaconClick(loc);
                            }
                        });

                        ((FrameLayout) getActivity().findViewById(R.id.fragment_map_map_container)).addView(encMarker);
                    }
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }


    private class GetPosition extends AsyncTask<AsyncTaskCallback, Void, Location> {

        private AsyncTaskCallback asyncTaskCallback;

        @Override
        protected Location doInBackground(AsyncTaskCallback... params) {
            this.asyncTaskCallback = params[0];
            Location l = null;

            if (gps.canGetLocation()) {
                //Toast.makeText(getContext(), gps.getLongitude() + " " + gps.getLatitude(),
                // Toast.LENGTH_LONG).show();
                l = new Location("prov");
                l.setLongitude(gps.getLongitude());
                l.setLatitude(gps.getLatitude());

                setMarkerPosition(l);
            } else {
                gps.showSettingsAlert();
            }
            return l;
        }

        @Override
        protected void onPostExecute(Location location) {
            super.onPostExecute(location);
            if (location != null) {
                asyncTaskCallback.asyncTaskCallback(location);
            }
        }
    }

    public interface OverlayShow {
        void onOverlayShow(JSONObject beacon, Question questions);
    }
}

