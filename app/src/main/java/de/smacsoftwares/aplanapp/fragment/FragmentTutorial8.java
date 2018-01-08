package de.smacsoftwares.aplanapp.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.activity.AdminSplashActivity;
import de.smacsoftwares.aplanapp.activity.AdminSplashActivityForMobile;
import de.smacsoftwares.aplanapp.activity.SelectLoginType;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.LogApp;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;

import java.util.Locale;

import javax.inject.Inject;

public class FragmentTutorial8 extends Fragment
{
    PreferencesHelper preferences;
    RelativeLayout relativeMain;
    String strLangauge="";
    ToggleButton switchButton;
    TextView textViewSkip,textviewDontShowItAgain,textViewLableAplan;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tutorial8, container,false);
        //MyApplication.component(getActivity()).inject(this);
        preferences = new PreferencesHelper(getActivity());
        GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);
        strLangauge = getString(R.string.language_germany);
        relativeMain=(RelativeLayout)rootView.findViewById(R.id.relative__tutorial8_main);
        GlobalClass.setupTypeface(relativeMain, GlobalClass.fontRegular);
        switchButton =(ToggleButton)rootView.findViewById(R.id.toggle_button);
        textViewSkip=(TextView)rootView.findViewById(R.id.textskip);
        textViewLableAplan = (TextView)rootView.findViewById(R.id.lable_aplan);
        textviewDontShowItAgain=(TextView)rootView.findViewById(R.id.textview_dontshowagain);
        textviewDontShowItAgain.setTypeface(GlobalClass.fontLight);
        textViewLableAplan.setTypeface(GlobalClass.fontBold);
        textViewSkip.setTypeface(GlobalClass.fontLight);
        textViewSkip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(switchButton.isChecked()){
                    preferences.saveIsTutorialShow(false);
                }
                else {
                    preferences.saveIsTutorialShow(true);
                }

                boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
                if (tabletSize) {
                    if(preferences.isAdminShow()){
                        startActivity(new Intent(getActivity(),AdminSplashActivity.class));
                    }
                    else {
                        startActivity(new Intent(getActivity(),SelectLoginType.class));
                    }
                    //showAdminSplashDialog(R.style.DialogAnimation);
                } else {
                    if(preferences.isAdminShow()){
                        startActivity(new Intent(getActivity(),AdminSplashActivityForMobile.class));
                    }
                    else {
                        startActivity(new Intent(getActivity(),SelectLoginType.class));
                    }
                }

                /*if(preferences.isAdminShow()){
                    startActivity(new Intent(getActivity(),AdminSplashActivity.class));
                }
                else {
                    startActivity(new Intent(getActivity(),LoginMobileActivity.class));
                }*/

            }
        });
        if(preferences.isTutorialShow()){
            switchButton.setChecked(false);
        }
        else {
            switchButton.setChecked(true);
        }
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {

                LogApp.e(" &&&&&& "," toggle chacked change : "+isChecked);
                if(isChecked){
                    preferences.saveIsTutorialShow(false);
                }
                else {
                    preferences.saveIsTutorialShow(true);
                }

            }
        });
        if(preferences.getLanguage() != null && !preferences.getLanguage().equalsIgnoreCase("")){
            strLangauge = preferences.getLanguage();
        }
        else {
            strLangauge = getString(R.string.language_germany);
            preferences.saveLanguage(strLangauge);
        }
        setLanguage(preferences.getLanguage());
        return rootView;
    }
    public void setLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config,this.getResources().getDisplayMetrics());
        textviewDontShowItAgain.setText(getString(R.string.dont_show_it_again));
    }
}
