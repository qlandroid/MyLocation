package cn.ql.location.change.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.provider.Settings;

import cn.ql.location.change.MyApp;

/**
 * Created by Administrator on 2017-4-13.
 */
public class LocationUtils {

    public static void startLocation(double longitude, double latitude) {
        LocationThread thread = LocationThread.getInstance(MyApp.myApp);
        thread.setLocationAddress(longitude, latitude);
        if (!thread.isRun()) {
            thread.start();
        }
    }

    public static void setLocation(double longitude, double latitude) {
        LocationThread thread = LocationThread.getInstance(MyApp.myApp);
        thread.setLocationAddress(longitude, latitude);
    }

    public static void stop() {
        LocationThread thread = LocationThread.getInstance(MyApp.myApp);
        thread.setStop();
    }

    public static boolean isOpenSetting() {
        LocationManager locationManager = (LocationManager) MyApp.myApp.getSystemService(Context.LOCATION_SERVICE);
        boolean canMockPosition;
        if (Build.VERSION.SDK_INT <= 22) {
            canMockPosition = Settings.Secure.getInt(MyApp.myApp.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0;
        } else {
            boolean hasAddTestProvider = false;
            canMockPosition = (Settings.Secure.getInt(MyApp.myApp.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0)
                    || Build.VERSION.SDK_INT > 22;
            if (canMockPosition && hasAddTestProvider == false) {
                try {
                    String providerStr = LocationManager.GPS_PROVIDER;
                    LocationProvider provider = locationManager.getProvider(providerStr);
                    if (provider != null) {
                        locationManager.addTestProvider(
                                provider.getName()
                                , provider.requiresNetwork()
                                , provider.requiresSatellite()
                                , provider.requiresCell()
                                , provider.hasMonetaryCost()
                                , provider.supportsAltitude()
                                , provider.supportsSpeed()
                                , provider.supportsBearing()
                                , provider.getPowerRequirement()
                                , provider.getAccuracy());
                    } else {
                        locationManager.addTestProvider(
                                providerStr
                                , true, true, false, false, true, true, true
                                , Criteria.POWER_HIGH, Criteria.ACCURACY_FINE);
                    }
                    locationManager.setTestProviderEnabled(providerStr, true);
                    locationManager.setTestProviderStatus(providerStr, LocationProvider.AVAILABLE, null, System.currentTimeMillis());

                    // 模拟位置可用
                    hasAddTestProvider = true;
                    canMockPosition = true;
                } catch (SecurityException e) {
                    canMockPosition = false;
                }
            }
        }
        return canMockPosition;
    }
}
