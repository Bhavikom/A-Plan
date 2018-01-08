package de.smacsoftwares.aplanapp.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.LinearLayout;

import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;

import javax.inject.Inject;

public class SplashActivity extends AppCompatActivity {
    PreferencesHelper preference;

    LinearLayout linearSplaceActivity;
    Thread thread;
    //PreferencesHelper preference;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        //MyApplication.component(this).inject(this);
        preference = new PreferencesHelper(SplashActivity.this);
        if(preference.getAccessToken() != null){
            GlobalClass.strAccessToken = preference.getAccessToken();
        }
        if(!GlobalClass.isTablet(this)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        GlobalClass.fontRegular = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPath);
        linearSplaceActivity = (LinearLayout)findViewById(R.id.activity_splash);
        GlobalClass.setupTypeface(linearSplaceActivity, GlobalClass.fontRegular);

        thread = new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                super.run();
                try {
                    synchronized (this)
                    {
                        wait(2000);
                    }
                } catch (Exception e) {} finally {
                    if(preference.isUserLooged()){
                        /* if user is already logged in than go to home activity */
                        Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        /* check if user want to show tutorial screen again or not if yes than show tutorial
                        otherwise go to home activity */
                        if(preference.isTutorialShow()){
                            Intent intent = new Intent(SplashActivity.this,TutorialActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        };
        thread.start();
    }
}
