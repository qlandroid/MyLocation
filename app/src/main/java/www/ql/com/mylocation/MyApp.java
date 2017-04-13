package www.ql.com.mylocation;

import android.app.Application;

/**
 * Created by Administrator on 2017-4-13.
 */
public class MyApp extends Application {
    public static MyApp myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
    }
}
