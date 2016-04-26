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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

    private static final String BEACON_OVERLAY_FRAGMENT = "beaconOverlayFragment";

    private Fragment mBeaconOverlayFragment;
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
        // TODO: 26.04.2016 To large graphics
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

    /**
     * Show an overlay on on tap for several beacons,
     * such as enclosures, animal houses, kiosks, etc.
     *
     * @param beacon    The beacon object with information about the beacon
     * @param questions A Bundle of questions to display in the overlay with information to
     *                  invoke the {@link onl.deepspace.zoorallye.QuestionActivity}
     */
    private void showBeaconOverlay(JSONObject beacon, Bundle questions) {

        try {
            JSONArray animals = beacon.getJSONArray(Const.ZOO_ANIMALS);
            ArrayList<String> animalList = Tools.jsonArrayToArrayList(animals);

            mBeaconOverlayFragment = BeaconsOverlayFragment.newInstance(animalList, questions);
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(mBeaconOverlayFragment, BEACON_OVERLAY_FRAGMENT);
            transaction.commit();
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }

    }

    private void hideBeaconOverlay() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(mBeaconOverlayFragment);
        transaction.commit();
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
        drawEnclosures();
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
        ArrayList<JSONObject> nearBeacons = Tools.getEnclosures(getContext(), "4P1shyVmM4", location, 15); //near beacons in 15m range
        for (int i = 0; i < nearBeacons.size(); i++) {
            // TODO: 26.04.2016 show beacon overlay foreach near beacon
            Log.d(Const.LOGTAG, "Near beacons: " + String.valueOf(nearBeacons.get(i)));
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
            }
            else {
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

    private void drawEnclosures() {
        try {
            JSONArray zoos = new JSONArray("[{\"name\":\"Zoo Augsburg\",\"id\":\"4P1shyVmM4\",\"beacons\":[{\"type\":\"open-air enclosure\",\"latitude\":48.347018,\"longitude\":10.914766,\"animals\":[\"alpaca\",\"american rhea\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347336,\"longitude\":10.914838,\"animals\":[\"nilgai\",\"antelope\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.34731,\"longitude\":10.91504,\"animals\":[\"rabbit\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347455,\"longitude\":10.915163,\"animals\":[\"cavy\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347861,\"longitude\":10.914758,\"animals\":[\"donkey\",\"miniature donkey\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348067,\"longitude\":10.915548,\"animals\":[\"dahomey cow\",\"cow\",\"pony\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347945,\"longitude\":10.915042,\"animals\":[\"goat\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347779,\"longitude\":10.914616,\"animals\":[\"leopard\"]},{\"type\":\"animal house\",\"latitude\":48.347969,\"longitude\":10.914485,\"animals\":[\"tiger\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348477,\"longitude\":10.914232,\"animals\":[\"sumatran tiger\",\"tiger\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348349,\"longitude\":10.915138,\"animals\":[\"fennec\"]},{\"type\":\"animal house\",\"latitude\":48.34846,\"longitude\":10.915103,\"animals\":[\"lion\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348746,\"longitude\":10.915457,\"animals\":[\"lion\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348831,\"longitude\":10.914893,\"animals\":[\"leopard cat\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348674,\"longitude\":10.914644,\"animals\":[\"hyena\",\"striped hyena\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348993,\"longitude\":10.915271,\"animals\":[\"hamadryas baboon\",\"baboon\",\"red buffalo\",\"buffalo\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348789,\"longitude\":10.914536,\"animals\":[\"onager\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348602,\"longitude\":10.91419,\"animals\":[\"ibis\",\"glossy ibis\",\"scarlet ibis\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348568,\"longitude\":10.913813,\"animals\":[\"tropic birds\"]},{\"type\":\"animal house\",\"latitude\":48.348524,\"longitude\":10.91359,\"animals\":[\"tropic birds\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348394,\"longitude\":10.913034,\"animals\":[\"birds\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.3584,\"longitude\":10.913627,\"animals\":[\"sea bears\",\"seal\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348271,\"longitude\":10.913307,\"animals\":[\"magellanic penguin\",\"penguin\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348147,\"longitude\":10.912811,\"animals\":[\"seal\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348204,\"longitude\":10.912517,\"animals\":[\"capuchin\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.348086,\"longitude\":10.912538,\"animals\":[\"degus\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347874,\"longitude\":10.912866,\"animals\":[\"coati\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347491,\"longitude\":10.913079,\"animals\":[\"maned wolf\",\"wolf\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347776,\"longitude\":10.913067,\"animals\":[\"flamingo\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347276,\"longitude\":10.913167,\"animals\":[\"crane\",\"duck\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.347195,\"longitude\":10.913362,\"animals\":[\"buffalo\",\"water buffalo\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346919,\"longitude\":10.913379,\"animals\":[\"bennetts wallaby\",\"kangaroo\",\"emu\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346826,\"longitude\":10.913223,\"animals\":[\"water fowl\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346588,\"longitude\":10.913543,\"animals\":[\"stork\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346362,\"longitude\":10.913938,\"animals\":[\"alpine ibex\",\"ibex\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346081,\"longitude\":10.914131,\"animals\":[\"otter\",\"beaver\",\"native fish\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346037,\"longitude\":10.914411,\"animals\":[\"crane\",\"duck\",\"cormorant\",\"muntjac\",\"chinese muntjac\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346481,\"longitude\":10.914289,\"animals\":[\"east caucasian tur\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.345952,\"longitude\":10.914755,\"animals\":[\"mandrill\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346207,\"longitude\":10.915288,\"animals\":[\"ring tailed lemur\",\"lemur\",\"black lemur\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.345558,\"longitude\":10.91537,\"animals\":[\"rhino\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.345185,\"longitude\":10.915589,\"animals\":[\"pelican\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.344809,\"longitude\":10.915945,\"animals\":[\"brown bear\",\"bear\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.344523,\"longitude\":10.916289,\"animals\":[\"great gray owl\",\"owl\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.344394,\"longitude\":10.916471,\"animals\":[\"duck\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.344389,\"longitude\":10.916887,\"animals\":[\"lesser flamingo\",\"flamingo\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.344351,\"longitude\":10.917457,\"animals\":[\"mountain goat\",\"goat\"]},{\"type\":\"animal house\",\"latitude\":48.344735,\"longitude\":10.916964,\"animals\":[\"meerkat\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.34518,\"longitude\":10.917804,\"animals\":[\"grevys zebra\",\"zebra\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.345041,\"longitude\":10.917944,\"animals\":[\"mishmi takin\",\"takin\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.345415,\"longitude\":10.918084,\"animals\":[\"chinese muntjac\",\"muntjac\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.345735,\"longitude\":10.918176,\"animals\":[\"east caucasian tur\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346477,\"longitude\":10.917143,\"animals\":[\"asian elephant\",\"elephant\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346054,\"longitude\":10.915853,\"animals\":[\"ostrich\",\"african ostrich\"]},{\"type\":\"animal house\",\"latitude\":48.34649,\"longitude\":10.916789,\"animals\":[\"elephant\",\"chimpanzee\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346357,\"longitude\":10.916412,\"animals\":[\"chimpanzee\"]},{\"type\":\"animal house\",\"latitude\":48.346357,\"longitude\":10.916512,\"animals\":[\"reptiles\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346366,\"longitude\":10.916025,\"animals\":[\"native snake\",\"snake\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346449,\"longitude\":10.915697,\"animals\":[\"native amphibian\",\"amphibian\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346602,\"longitude\":10.915334,\"animals\":[\"sulcata tortoise\",\"tortoise\"]},{\"type\":\"open-air enclosure\",\"latitude\":48.346602,\"longitude\":10.915334,\"animals\":[\"rhino\",\"blesbok\",\"ground hornbill\"]}],\"openingHours\":[{\"period\":\"january\",\"opened\":\"16.30\"},{\"period\":\"february\",\"opened\":\"16.30\"},{\"period\":\"march\",\"opened\":\"17.00\"},{\"period\":\"april\",\"opened\":\"18.00\"},{\"period\":\"may\",\"opened\":\"18.00\"},{\"period\":\"june\",\"opened\":\"18.30\"},{\"period\":\"july\",\"opened\":\"18.30\"},{\"period\":\"august\",\"opened\":\"18.30\"},{\"period\":\"september\",\"opened\":\"18.00\"},{\"period\":\"october\",\"opened\":\"17.00\"},{\"period\":\"november\",\"opened\":\"16.30\"},{\"period\":\"december\",\"opened\":\"16.30\"}],\"pricing\":[{\"period\":\"01.03. - 31.10.\",\"prices\":[{\"group\":\"adults\",\"price\":\"10\\u20ac\"},{\"group\":\"children\",\"age\":\"<3;\",\"price\":\"free\"},{\"group\":\"children\",\"age\":\">3;<15;\",\"price\":\"5\\u20ac\"},{\"group\":\"discounted\",\"price\":\"9\\u20ac\"},{\"group\":\"dogs\",\"price\":\"3\\u20ac\"}]},{\"period\":\"01.11. - 28.02.\",\"prices\":[{\"group\":\"adults\",\"price\":\"8\\u20ac\"},{\"group\":\"children\",\"age\":\"<3;\",\"price\":\"free\"},{\"group\":\"children\",\"age\":\">3;<15;\",\"price\":\"4\\u20ac\"},{\"group\":\"discounted\",\"price\":\"7\\u20ac\"},{\"group\":\"dogs\",\"price\":\"3\\u20ac\"}]}]}]");
            // TODO: 25.04.2016 use fetched string

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
        } catch (JSONException e) {
            Log.e(Const.LOGTAG, e.getMessage());
        }

        /*BitmapDrawable dr = new BitmapDrawable(bmOverlay);
        dr.setBounds(0, 0, view.getWidth(), view.getHeight());

        map.setImageDrawable(dr);*/
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
}

