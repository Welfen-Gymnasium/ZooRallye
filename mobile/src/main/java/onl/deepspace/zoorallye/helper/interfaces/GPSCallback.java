package onl.deepspace.zoorallye.helper.interfaces;

import android.location.Location;

/**
 * Created by Sese on 31.03.2016.
 *
 * Interface the communicate with the activity when a new location arrives
 */
public interface GPSCallback {
    void GPSLocationChanged(Location location);
}
