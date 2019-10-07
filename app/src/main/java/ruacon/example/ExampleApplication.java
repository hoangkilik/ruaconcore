package ruacon.example;

import android.app.Application;

import com.base.util.LogUtil;

/**
 * Created by on 12/13/2016.
 */
public class ExampleApplication extends Application {
    private static ExampleApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        LogUtil.init(BuildConfig.DEBUG, "ExampleLog");
    }

    public static ExampleApplication getInstance() {
        return mInstance;
    }
}