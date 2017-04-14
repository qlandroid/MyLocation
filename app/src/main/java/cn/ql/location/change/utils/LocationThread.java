package cn.ql.location.change.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by Administrator on 2017-4-13.
 */
public class LocationThread extends Thread {
    private static final String TAG = "LocationThread";
    private static LocationThread sThread = null;

    private Location mGPSLocation;
    private Location mNetWorkLocation;

    private double mLongitude;
    private double mLatitude;
    private LocationManager mLocationManager;
    private boolean isRun;

    private long sleepTime = 100;

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
        mGPSLocation = new Location(LocationManager.GPS_PROVIDER);
        mNetWorkLocation = new Location(LocationManager.NETWORK_PROVIDER);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void run() {
        super.run();
        isRun = true;
        while (!isStop) {
            try {
                Log.i("mtag", "run:  正在已经设置虚拟位置" + "--longitude=" + mLongitude + "----latitude = " + mLatitude);
                mGPSLocation.setLatitude(mLongitude);
                mGPSLocation.setLongitude(mLatitude);
                mGPSLocation.setTime(System.currentTimeMillis());
                mGPSLocation.setElapsedRealtimeNanos(System.currentTimeMillis());
                mGPSLocation.setAltitude(2.0f);
                mGPSLocation.setAccuracy(3.0f);
                mLocationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, mGPSLocation);
            }catch (Exception e){
                Log.e(TAG, "run: 设置GPS 虚拟位置有问题");
            }
            try {
                mNetWorkLocation.setLatitude(mLongitude);
                mNetWorkLocation.setLongitude(mLatitude);
                mNetWorkLocation.setTime(System.currentTimeMillis());
                mNetWorkLocation.setElapsedRealtimeNanos(System.currentTimeMillis());
                mNetWorkLocation.setAltitude(2.0f);
                mNetWorkLocation.setAccuracy(3.0f);
                mLocationManager.setTestProviderLocation(LocationManager.NETWORK_PROVIDER, mNetWorkLocation);
            }catch (Exception e){
                Log.e(TAG, "run: 设置NETWORK虚拟位置错误");
            }
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
        try {
            mLocationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
        }catch (Exception e){

        }
        isStop = true;
    }

}
