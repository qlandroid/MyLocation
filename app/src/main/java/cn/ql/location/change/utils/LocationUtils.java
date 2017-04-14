package cn.ql.location.change.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.provider.Settings;

import cn.ql.location.change.C;
import cn.ql.location.change.MyApp;

/**
 * Created by Administrator on 2017-4-13.
 */
public class LocationUtils {

    /**
     * 用于启动 虚拟位置
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return 是否开启
     */
    public static boolean startLocation(double longitude, double latitude) {
        boolean isUsesGPS = isUsesGPS();
        boolean isUsesNetWork = isUsesNetWork();
        boolean isPASSIVE_PROVIDER = isUsesPASSIVE_PROVIDER();
        if (!(isUsesGPS || isUsesNetWork|| isPASSIVE_PROVIDER)) {
            return false;
        }
        LocationThread thread = LocationThread.getInstance();
        thread.setLocationAddress(longitude, latitude);
        thread.setIsUsesGPS(isUsesGPS);
        thread.setIsUsesNetWork(isUsesNetWork);
        thread.setIsUsesPASSIVE_PROVIDER(isPASSIVE_PROVIDER);
        if (!thread.isRun()) {
            thread.start();
        }
        return true;
    }

    public static void setLocation(double longitude, double latitude) {
        LocationThread thread = LocationThread.getInstance();
        thread.setLocationAddress(longitude, latitude);
    }

    public static void stop() {
        LocationThread thread = LocationThread.getInstance();
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

    public static boolean isUsesNetWork() {
        return C.sLocationManger.isProviderEnabled(LocationManager.NETWORK_PROVIDER);//判断指定提供程序是否能用
    }

    public static boolean isUsesGPS() {
        return C.sLocationManger.isProviderEnabled(LocationManager.GPS_PROVIDER);//判断指定提供程序是否能用
    }

    public static boolean isUsesPASSIVE_PROVIDER(){
        return C.sLocationManger.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
    }
}
