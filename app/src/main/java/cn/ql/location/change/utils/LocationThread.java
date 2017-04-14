package cn.ql.location.change.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import cn.ql.location.change.C;

/**
 * Created by Administrator on 2017-4-13.
 */
public class LocationThread extends Thread {
    private static final String TAG = "LocationThread";
    private static LocationThread sThread = null;

    private Location mGPSLocation;
    private Location mNetWorkLocation;
    private Location mPassiveProviderLocation;
    private double mLongitude;
    private double mLatitude;
    private LocationManager mLocationManager;
    private boolean isRun;

    private boolean isUsesGPS;
    private boolean isUsesNetWork;

    private long sleepTime = 100;

    private boolean isStop = false;
    private boolean isUsesPASSIVE_PROVIDER;


    public static LocationThread getInstance() {
        if (sThread == null) {
            synchronized (LocationThread.class) {
                if (sThread == null) {
                    sThread = Instance.thread;
                    sThread.init();
                }
            }
        }
        return sThread;
    }

    public void setIsUsesPASSIVE_PROVIDER(boolean isUsesPASSIVE_PROVIDER) {
        this.isUsesPASSIVE_PROVIDER = isUsesPASSIVE_PROVIDER;
    }

    private static class Instance {
        public static final LocationThread thread = new LocationThread();
    }

    private void init() {
        mLocationManager = C.sLocationManger;
        mGPSLocation = new Location(LocationManager.GPS_PROVIDER);
        mNetWorkLocation = new Location(LocationManager.NETWORK_PROVIDER);
        try {
            mPassiveProviderLocation = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }catch (Exception e){
            e.printStackTrace();
            mPassiveProviderLocation = new Location(LocationManager.PASSIVE_PROVIDER);
        }

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void run() {
        super.run();
        isRun = true;
        if (!(isUsesGPS|| isUsesNetWork)){
            return;
        }
        while (!isStop) {
            try {
                if (isUsesGPS) {
                    Log.i("mtag", "run:  正在已经设置虚拟位置" + "--longitude=" + mLongitude + "----latitude = " + mLatitude);
                    mGPSLocation.setLatitude(mLongitude);
                    mGPSLocation.setLongitude(mLatitude);
                    mGPSLocation.setTime(System.currentTimeMillis());
                    mGPSLocation.setElapsedRealtimeNanos(System.currentTimeMillis());
                    mGPSLocation.setAltitude(2.0f);
                    mGPSLocation.setAccuracy(3.0f);
                    mLocationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, mGPSLocation);
                }
            }catch (Exception e){
                Log.e(TAG, "run: 设置GPS 虚拟位置有问题");
            }
            try {
                if (isUsesNetWork) {
                    mPassiveProviderLocation.setLatitude(mLongitude);
                    mPassiveProviderLocation.setLongitude(mLatitude);
                    mPassiveProviderLocation.setTime(System.currentTimeMillis());
                    mPassiveProviderLocation.setElapsedRealtimeNanos(System.currentTimeMillis());
                    mPassiveProviderLocation.setAltitude(2.0f);
                    mPassiveProviderLocation.setAccuracy(3.0f);
                    mLocationManager.setTestProviderLocation(LocationManager.NETWORK_PROVIDER, mPassiveProviderLocation);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                if (isUsesPASSIVE_PROVIDER) {
                    mNetWorkLocation.setLatitude(mLongitude);
                    mNetWorkLocation.setLongitude(mLatitude);
                    mNetWorkLocation.setTime(System.currentTimeMillis());
                    mNetWorkLocation.setElapsedRealtimeNanos(System.currentTimeMillis());
                    mNetWorkLocation.setAltitude(2.0f);
                    mNetWorkLocation.setAccuracy(3.0f);
                    mLocationManager.setTestProviderLocation(LocationManager.PASSIVE_PROVIDER, mNetWorkLocation);
                }
            }catch (Exception e){
               e.printStackTrace();
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
    public void setIsUsesGPS(boolean isUsesGPS){
        this.isUsesGPS = isUsesGPS;
    }
    public void setIsUsesNetWork(boolean isUsesNetWork){
        this.isUsesNetWork = isUsesNetWork;
    }
}
