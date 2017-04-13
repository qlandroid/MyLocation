package www.ql.com.mylocation.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;

/**
 * Created by Administrator on 2017-4-13.
 */
public class LocationThread extends Thread {
    private static LocationThread sThread = null;
    private Location mLocation;
    private double mLongitude;
    private double mLatitude;
    private LocationManager mLocationManager;
    private boolean isRun;

    private long sleepTime = 300;

    private boolean isStop = false;

    public static LocationThread getInstance(Context context) {
        if (sThread == null){
            synchronized (LocationThread.class){
                if (sThread == null){
                    sThread =  Instance.thread;
                    sThread.init(context);
                }
            }
        }
        return sThread;
    }
    private static class Instance{
        public static final LocationThread thread = new LocationThread();
    }

    private void init(Context context){
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mLocation = new Location(LocationManager.GPS_PROVIDER);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void run() {
        super.run();
        isRun = true;
        while (!isStop) {
            mLocation.setLatitude(mLongitude);
            mLocation.setLongitude(mLatitude);
            mLocation.setTime(System.currentTimeMillis());
            mLocation.setElapsedRealtimeNanos(System.currentTimeMillis());
            mLocation.setAltitude(2.0f);
            mLocation.setAccuracy(3.0f);
            mLocationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, mLocation);
            SystemClock.sleep(sleepTime);
        }
    }
    public boolean isRun(){
        return isRun;
    }
    public void setLocationAddress(double longitude,double latitude){
        this.mLongitude = longitude;
        this.mLatitude = latitude;
    }
    public void setStop(){
        isStop = true;
    }

}
