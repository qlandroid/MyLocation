package cn.ql.location.change;

import android.app.Application;
import android.content.Context;
import android.location.LocationManager;

/**
 * Created by Administrator on 2017-4-13.
 */
public class MyApp extends Application {
    public static MyApp myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
        C.sLocationManger = (LocationManager) MyApp.myApp.getSystemService(Context.LOCATION_SERVICE);
    }
}
