package de.smacsoftwares.aplanapp.dagger2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.smacsoftwares.aplanapp.util.DatabaseHandler;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;

@Module
public class AppModule {

    private Context context;
    public AppModule(Context context) {
        this.context = context;
    }

    /*@Singleton
    @Provides
    SharedPreferences providePreference() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides @Singleton
    PreferencesHelper providesPreferences(SharedPreferences preferences){
        return  new PreferencesHelper(preferences);
    }

    @Provides @Singleton
    DatabaseHandler providesDbhelper(){
        return  new DatabaseHandler(context);
    }*/

}
