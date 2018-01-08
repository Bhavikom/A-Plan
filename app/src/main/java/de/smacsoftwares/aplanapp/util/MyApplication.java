package de.smacsoftwares.aplanapp.util;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

import de.smacsoftwares.aplanapp.dagger2.AppComponent;
import de.smacsoftwares.aplanapp.dagger2.AppModule;
import de.smacsoftwares.aplanapp.dagger2.DaggerAppComponent;
import io.fabric.sdk.android.Fabric;

/**
 * Created by SSoft-13 on 17-12-2016.
 */
// this applicatoin class
public class MyApplication extends Application
{
    private AppComponent component;
    private static MyApplication instance;
    //public ArrayList<ProfileModelClass> arraylistProfileGlobal = null;
    @Override
    public void onCreate()
    {
        super.onCreate();
        component = AppComponentInitializer.init(this);
        Fabric.with(this, new Crashlytics());
        instance = this;
        //Crashlytics.getInstance().crash();
    }
    public MyApplication()
    {
        //arraylistProfileGlobal = new ArrayList();
    }
    public static AppComponent component(Context context) {
        return ((MyApplication) context.getApplicationContext()).component;
    }

    private final static class AppComponentInitializer {

        public static AppComponent init(Context context) {
            return DaggerAppComponent.builder().appModule(new AppModule(context)).build();
        }

    }
}
