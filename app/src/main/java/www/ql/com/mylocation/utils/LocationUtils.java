package www.ql.com.mylocation.utils;

import www.ql.com.mylocation.MyApp;

/**
 * Created by Administrator on 2017-4-13.
 */
public class LocationUtils {

    public static  void startLocation(double longitude, double latitude){
        LocationThread thread = LocationThread.getInstance(MyApp.myApp);
        thread.setLocationAddress(longitude,latitude);
        if (!thread.isRun()){
            thread.start();
        }
    }
    public static void setLocation(double longitude, double latitude){
        LocationThread thread = LocationThread.getInstance(MyApp.myApp);
        thread.setLocationAddress(longitude,latitude);
    }
    public static void stop(){
        LocationThread thread = LocationThread.getInstance(MyApp.myApp);
        thread.setStop();
    }
}
