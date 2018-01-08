package de.smacsoftwares.aplanapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.fragment.FragmentDashBoard;
import de.smacsoftwares.aplanapp.fragment.FragmentDashBoardMobile;
import de.smacsoftwares.aplanapp.fragment.FragmentProfile;
import de.smacsoftwares.aplanapp.fragment.FragmentProject;
import de.smacsoftwares.aplanapp.fragment.FragmentResource;
import de.smacsoftwares.aplanapp.fragment.FragmentSelectFilter;
import de.smacsoftwares.aplanapp.fragment.FragmentSetting;
import de.smacsoftwares.aplanapp.fragment.FragmentSetting2;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.InterfaceChangeLanguageDashboard;
import de.smacsoftwares.aplanapp.util.InterfaceChangeLanguageDashboardMobile;
import de.smacsoftwares.aplanapp.util.InterfaceChangeLanguageDateFilter;
import de.smacsoftwares.aplanapp.util.InterfaceChangeLanguageFolderFilter;
import de.smacsoftwares.aplanapp.util.InterfaceChangeLanguageProfile;
import de.smacsoftwares.aplanapp.util.InterfaceChangeLanguageResource;
import de.smacsoftwares.aplanapp.util.InterfaceChangeLanguageSelectFilter;
import de.smacsoftwares.aplanapp.util.InterfaceChangeLanguageSetting;
import de.smacsoftwares.aplanapp.util.InterfaceChangeLanguageTextFilter;
import de.smacsoftwares.aplanapp.util.InterfaceChangeLanguageUserDefinedFilter;
import de.smacsoftwares.aplanapp.util.InterfaceLoadWebview;
import de.smacsoftwares.aplanapp.util.InterfaceLoadWebviewResource;
import de.smacsoftwares.aplanapp.util.InterfaceProjectClicked;
import de.smacsoftwares.aplanapp.util.InterfaceforSelectFilter;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.NonSwipeableViewPager;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;

import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by SSOFT4 on 10/26/2016.
 */

public class HomeActivity extends ActionBarActivity implements View.OnClickListener,
        FragmentDashBoard.Taskclicked, FragmentProfile.InterfaceProfileChanged,FragmentProfile.InterfaceProfileChanged2,
        FragmentProfile.InterfaceProfileChangedForSetting,InterfaceChangeLanguageTextFilter,
        InterfaceChangeLanguageDateFilter,InterfaceChangeLanguageUserDefinedFilter,
        InterfaceChangeLanguageFolderFilter,InterfaceProjectClicked,InterfaceChangeLanguageProfile,
        InterfaceChangeLanguageDashboard,InterfaceChangeLanguageSelectFilter,InterfaceChangeLanguageSetting,
        InterfaceChangeLanguageResource,FragmentProfile.InterfaceChangeLanguageHome,InterfaceChangeLanguageDashboardMobile,FragmentDashBoardMobile.TaskclickedDashboardMobile{
    LinearLayout.LayoutParams paramFooter;
    LinearLayout.LayoutParams paramPager;
    String strLangauge="";
    public InterfaceLoadWebview interfaceLoadWebview;
    public InterfaceLoadWebviewResource interfaceLoadWebviewResource;
    public InterfaceProjectClicked interfaceProjectClicked;
    public InterfaceChangeLanguageFolderFilter interfaceChangeLanguageFolderFilter;
    public InterfaceChangeLanguageResource interfaceChangeLanguageResource;
    public InterfaceChangeLanguageUserDefinedFilter interfaceChangeLanguageUserDefinedFilter;
    public InterfaceChangeLanguageTextFilter interfaceChangeLanguageTextFilter;
    public InterfaceChangeLanguageDateFilter interfaceChangeLanguageDateFilter;
    public InterfaceChangeLanguageDashboard interfaceChangeLanguage;
    public InterfaceChangeLanguageProfile interfaceChangeLanguageProfile;
    public InterfaceChangeLanguageSelectFilter interfaceChangeLanguageSelectFilter;
    public InterfaceChangeLanguageSetting interfaceChangeLanguageSetting;
    public InterfaceforSelectFilter interfaceforSelectFilter;
    public InterfaceChangeLanguageDashboardMobile interfaceChangeLanguageDashboardMobile;
    LinearLayout linearDashboard, linerProject, linearFilter, linearSetting,linearFooter,linearResource,
            linearContatiner;
    RelativeLayout relativeContainer;
    NonSwipeableViewPager mViewPager;
    SamplePagerAdapter2 adapter2;
    ImageView imgDashboard,imgProject,imgFilter,imgSetting,imgResource;
    TextView textViewDashboard, textViewProject, textViewFilter, textViewSetting,textViewResource;
    PreferencesHelper preferenes;

    private static HomeActivity homeActivity;
    boolean tabletSize;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        //MyApplication.component(this).inject(this);
        preferenes = new PreferencesHelper(this);
        GlobalClass.fontRegular = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPath);
        preferenes.clearCurrentFiredFilter();
        if (preferenes.isUserLooged()) {
            initControl();
        }else {
            if(preferenes.isAdminShow()){
                if(GlobalClass.isTablet(this)){
                    startActivity(new Intent(this, AdminSplashActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(this, AdminSplashActivityForMobile.class));
                    finish();
                }
            }
            else {
                startActivity(new Intent(this, SelectLoginType.class));
                finish();
            }
        }
    }
    //    this method intialize control and class all initial work
    public void initControl(){
        if(!GlobalClass.isTablet(this)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        homeActivity =this;
        linearContatiner = (LinearLayout) findViewById(R.id.linear_container);
        /* go to IF if app is disabled and got to duplicate setting fragment*/
        if(preferenes.isLogoutApp()){
            showAppLogout();
        }
        else if(preferenes.isDisableApp()){
            showAppDisabled(false);
        }
        /* app will be in else when app is enabled and load all viewpager fragment */
        else {
            if(preferenes.getActiveDialog().equalsIgnoreCase("yes")){
                /* show user is activited dialog only first time after activate */
                preferenes.saveShowActiveDialog("no");
                showUserActiveDialog();
            }
            linearContatiner.setVisibility(View.GONE);
            mViewPager = (NonSwipeableViewPager) findViewById(R.id.pager);
            /** set the adapter for ViewPager */
            mViewPager.setAdapter(new SamplePagerAdapter(
                    getSupportFragmentManager()));
            mViewPager.setOffscreenPageLimit(5);

            linearFooter = (LinearLayout)findViewById(R.id.linear_footer);
            GlobalClass.setupTypeface(linearFooter, GlobalClass.fontRegular);
            linearDashboard = (LinearLayout) findViewById(R.id.linear_dashboard);
            linearResource = (LinearLayout) findViewById(R.id.linear_resource);
            linerProject = (LinearLayout) findViewById(R.id.linear_project);
            linearFilter = (LinearLayout) findViewById(R.id.linear_filter);
            linearSetting = (LinearLayout) findViewById(R.id.linear_setting);

            linearDashboard.setOnClickListener(this);
            linerProject.setOnClickListener(this);
            linearFilter.setOnClickListener(this);
            linearSetting.setOnClickListener(this);
            linearResource.setOnClickListener(this);

            imgDashboard=(ImageView)findViewById(R.id.img_dashboard);
            imgFilter=(ImageView)findViewById(R.id.img_filter);
            imgProject=(ImageView)findViewById(R.id.img_project);
            imgSetting=(ImageView)findViewById(R.id.img_setting);
            imgResource = (ImageView)findViewById(R.id.img_resource);

            textViewDashboard =(TextView)findViewById(R.id.textview_dashboard);
            textViewProject =(TextView)findViewById(R.id.textview_project);
            textViewSetting =(TextView)findViewById(R.id.textview_setting);
            textViewFilter =(TextView)findViewById(R.id.textview_filter);
            textViewResource = (TextView)findViewById(R.id.textview_resource);

            imgDashboard.setBackgroundResource(R.drawable.dashboard);
            imgResource.setBackgroundResource(R.drawable.resource_unselect);
            imgSetting.setBackgroundResource(R.drawable.setting_unselect);
            imgProject.setBackgroundResource(R.drawable.project_unselect);
            imgFilter.setBackgroundResource(R.drawable.filter_unselect);

            textViewDashboard.setTextColor(this.getResources().getColor(R.color.red));
            textViewProject.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
            textViewSetting.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
            textViewFilter.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
            textViewResource.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
            tabletSize = getResources().getBoolean(R.bool.isTablet);
            setLanguage(preferenes.getLanguage());
            if(GlobalClass.getScreenOrientation(HomeActivity.this) ==1){
                // for portrait
                if(tabletSize){
                    paramFooter = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 0.93f);
                    paramPager = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 0.07f);
                }
                else {
                    paramFooter = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 0.90f);
                    paramPager = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 0.10f);
                }

            }
            else {
                /// for landscape
                paramFooter = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 0.90f);
                paramPager = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 0.10f);
            }
            linearFooter.setLayoutParams(paramFooter);
            mViewPager.setLayoutParams(paramPager);
        }
    }
    public void showAppDisabled(final boolean flag){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                preferenes.saveAppDisalbe(true);
                linearContatiner.setVisibility(View.VISIBLE);
                mViewPager = (NonSwipeableViewPager) findViewById(R.id.pager);
                mViewPager.setVisibility(View.GONE);
                linearFooter = (LinearLayout)findViewById(R.id.linear_footer);
                GlobalClass.setupTypeface(linearFooter, GlobalClass.fontRegular);
                linearDashboard = (LinearLayout) findViewById(R.id.linear_dashboard);

                linearResource = (LinearLayout) findViewById(R.id.linear_resource);
                linerProject = (LinearLayout) findViewById(R.id.linear_project);
                linearFilter = (LinearLayout) findViewById(R.id.linear_filter);
                linearSetting = (LinearLayout) findViewById(R.id.linear_setting);

                linearDashboard.setOnClickListener(HomeActivity.this);
                linerProject.setOnClickListener(HomeActivity.this);
                linearFilter.setOnClickListener(HomeActivity.this);
                linearSetting.setOnClickListener(HomeActivity.this);
                linearResource.setOnClickListener(HomeActivity.this);

                linearDashboard.setEnabled(false);
                linerProject.setEnabled(false);
                linearFilter.setEnabled(false);
                linearSetting.setEnabled(false);
                linearResource.setEnabled(false);

                imgDashboard=(ImageView)findViewById(R.id.img_dashboard);
                imgFilter=(ImageView)findViewById(R.id.img_filter);
                imgProject=(ImageView)findViewById(R.id.img_project);
                imgSetting=(ImageView)findViewById(R.id.img_setting);
                imgResource = (ImageView)findViewById(R.id.img_resource);

                textViewDashboard =(TextView)findViewById(R.id.textview_dashboard);
                textViewProject =(TextView)findViewById(R.id.textview_project);
                textViewSetting =(TextView)findViewById(R.id.textview_setting);
                textViewFilter =(TextView)findViewById(R.id.textview_filter);
                textViewResource = (TextView)findViewById(R.id.textview_resource);

                imgDashboard.setBackgroundResource(R.drawable.dashboard_unselect);
                imgResource.setBackgroundResource(R.drawable.resource_unselect);
                imgSetting.setBackgroundResource(R.drawable.setting);
                imgProject.setBackgroundResource(R.drawable.project_unselect);
                imgFilter.setBackgroundResource(R.drawable.filter_unselect);

                textViewDashboard.setTextColor(HomeActivity.this.getResources().getColor(R.color.textview_color_grey));
                textViewProject.setTextColor(HomeActivity.this.getResources().getColor(R.color.textview_color_grey));
                textViewSetting.setTextColor(HomeActivity.this.getResources().getColor(R.color.red));
                textViewFilter.setTextColor(HomeActivity.this.getResources().getColor(R.color.textview_color_grey));
                textViewResource.setTextColor(HomeActivity.this.getResources().getColor(R.color.textview_color_grey));

                setLanguage(preferenes.getLanguage());
                if(GlobalClass.getScreenOrientation(HomeActivity.this) ==1){
                    // for portrait
                    paramFooter = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 0.93f);
                    paramPager = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 0.07f);
                }
                else {
                    /// for landscape
                    paramFooter = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 0.90f);
                    paramPager = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 0.10f);
                }
                linearFooter.setLayoutParams(paramFooter);
                linearContatiner.setLayoutParams(paramPager);

                Bundle args = new Bundle();
                Fragment fr = new FragmentSetting2();
                // fr.setArguments(args);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                if(flag){
                    args.putString("file_missing","yes");
                }else {
                    args.putString("file_missing","no");
                }
                fr.setArguments(args);
                fragmentTransaction.replace(R.id.linear_container,fr);
                fragmentTransaction.commit();
            }
        });

    }
    public void showAppLogout(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                preferenes.saveAppLogout(true);
                linearContatiner.setVisibility(View.VISIBLE);
                mViewPager = (NonSwipeableViewPager) findViewById(R.id.pager);
                mViewPager.setVisibility(View.GONE);
                linearFooter = (LinearLayout)findViewById(R.id.linear_footer);
                GlobalClass.setupTypeface(linearFooter, GlobalClass.fontRegular);
                linearDashboard = (LinearLayout) findViewById(R.id.linear_dashboard);

                linearResource = (LinearLayout) findViewById(R.id.linear_resource);
                linerProject = (LinearLayout) findViewById(R.id.linear_project);
                linearFilter = (LinearLayout) findViewById(R.id.linear_filter);
                linearSetting = (LinearLayout) findViewById(R.id.linear_setting);

                linearDashboard.setOnClickListener(HomeActivity.this);
                linerProject.setOnClickListener(HomeActivity.this);
                linearFilter.setOnClickListener(HomeActivity.this);
                linearSetting.setOnClickListener(HomeActivity.this);
                linearResource.setOnClickListener(HomeActivity.this);

                linearDashboard.setEnabled(false);
                linerProject.setEnabled(false);
                linearFilter.setEnabled(false);
                linearSetting.setEnabled(false);
                linearResource.setEnabled(false);

                imgDashboard=(ImageView)findViewById(R.id.img_dashboard);
                imgFilter=(ImageView)findViewById(R.id.img_filter);
                imgProject=(ImageView)findViewById(R.id.img_project);
                imgSetting=(ImageView)findViewById(R.id.img_setting);
                imgResource = (ImageView)findViewById(R.id.img_resource);

                textViewDashboard =(TextView)findViewById(R.id.textview_dashboard);
                textViewProject =(TextView)findViewById(R.id.textview_project);
                textViewSetting =(TextView)findViewById(R.id.textview_setting);
                textViewFilter =(TextView)findViewById(R.id.textview_filter);
                textViewResource = (TextView)findViewById(R.id.textview_resource);

                imgDashboard.setBackgroundResource(R.drawable.dashboard_unselect);
                imgResource.setBackgroundResource(R.drawable.resource_unselect);
                imgSetting.setBackgroundResource(R.drawable.setting);
                imgProject.setBackgroundResource(R.drawable.project_unselect);
                imgFilter.setBackgroundResource(R.drawable.filter_unselect);

                textViewDashboard.setTextColor(HomeActivity.this.getResources().getColor(R.color.textview_color_grey));
                textViewProject.setTextColor(HomeActivity.this.getResources().getColor(R.color.textview_color_grey));
                textViewSetting.setTextColor(HomeActivity.this.getResources().getColor(R.color.red));
                textViewFilter.setTextColor(HomeActivity.this.getResources().getColor(R.color.textview_color_grey));
                textViewResource.setTextColor(HomeActivity.this.getResources().getColor(R.color.textview_color_grey));

                setLanguage(preferenes.getLanguage());
                if(GlobalClass.getScreenOrientation(HomeActivity.this) ==1){
                    // for portrait
                    paramFooter = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 0.93f);
                    paramPager = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 0.07f);
                }
                else {
                    /// for landscape
                    paramFooter = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 0.90f);
                    paramPager = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 0.10f);
                }
                linearFooter.setLayoutParams(paramFooter);
                linearContatiner.setLayoutParams(paramPager);

                Fragment fr = new FragmentSetting2();
                // fr.setArguments(args);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.linear_container,fr);
                fragmentTransaction.commit();

                //((FragmentSetting2) fr).actiononLogoutApp();
            }
        });
    }
    @Override
    public void onStart()
    {
        super.onStart();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen for landscape and portrait and set portrait mode always
        tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // for landscape
            paramFooter = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 0.90f);
            paramPager = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 0.10f);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // for portrait
            if(tabletSize){
                paramFooter = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 0.93f);
                paramPager = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 0.07f);
            }
            else {
                paramFooter = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 0.90f);
                paramPager = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, 0.10f);
            }
        }
        linearFooter.setLayoutParams(paramFooter);
        mViewPager.setLayoutParams(paramPager);
    }
    @Override
    public void changeLanguageForMobile() {}
    @Override
    public void profileChanged2() {
        // this iwll change language in resource screen
        interfaceChangeLanguageResource.changeLanguageResource();
    }
    /* this class is for setting adapter in Viewpager */
    public class SamplePagerAdapter extends FragmentPagerAdapter
    {
        public SamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            /** Show a Fragment based on the position of the current screen */
            if (position == 0) {
                tabletSize = getResources().getBoolean(R.bool.isTablet);
                if (tabletSize) {
                    return new FragmentDashBoard();
                }
                else {
                    return new FragmentDashBoardMobile();
                }
            }
            else if (position == 1) {
                return new FragmentProject();
            }
            else if (position == 2) {
                return new FragmentResource();
            }
            else if (position == 3) {
                return new FragmentSelectFilter();
            }
            else if (position == 4) {
                return new FragmentSetting();
            }
            else{
                boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
                if (tabletSize) {
                    return new FragmentDashBoard();
                }
                else {
                    return new FragmentDashBoardMobile();
                }
            }
        }
        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }
    }
    public class SamplePagerAdapter2 extends FragmentPagerAdapter
    {
        public SamplePagerAdapter2(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            /** Show a Fragment based on the position of the current screen */
                return new FragmentSetting();
        }
        @Override
        public int getCount() {
            // Show 1 total pages.
            return 1;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_dashboard:
                mViewPager.setCurrentItem(0,false);
                imgDashboard.setBackgroundResource(R.drawable.dashboard);
                imgSetting.setBackgroundResource(R.drawable.setting_unselect);
                imgProject.setBackgroundResource(R.drawable.project_unselect);
                imgFilter.setBackgroundResource(R.drawable.filter_unselect);
                imgResource.setBackgroundResource(R.drawable.resource_unselect);

                textViewDashboard.setTextColor(this.getResources().getColor(R.color.red));
                textViewProject.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                textViewResource.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                textViewSetting.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                textViewFilter.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                break;
            case R.id.linear_project:
                //interfaceProjectClicked.clickedProject();
                interfaceLoadWebview.callWebviewFromFooter();
                mViewPager.setCurrentItem(1,false);
                imgDashboard.setBackgroundResource(R.drawable.dashboard_unselect);
                imgProject.setBackgroundResource(R.drawable.project);
                imgFilter.setBackgroundResource(R.drawable.filter_unselect);
                imgSetting.setBackgroundResource(R.drawable.setting_unselect);
                imgResource.setBackgroundResource(R.drawable.resource_unselect);

                textViewDashboard.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                textViewProject.setTextColor(this.getResources().getColor(R.color.red));
                textViewFilter.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                textViewSetting.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                textViewResource.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                break;
            case R.id.linear_resource:
                //interfaceProjectClicked.clickedProject();
                interfaceLoadWebviewResource.callResourceWebviewFromFooter();
                mViewPager.setCurrentItem(2,false);
                imgDashboard.setBackgroundResource(R.drawable.dashboard_unselect);
                imgResource.setBackgroundResource(R.drawable.resource);
                imgFilter.setBackgroundResource(R.drawable.filter_unselect);
                imgSetting.setBackgroundResource(R.drawable.setting_unselect);
                imgProject.setBackgroundResource(R.drawable.project_unselect);


                textViewProject.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                textViewDashboard.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                textViewResource.setTextColor(this.getResources().getColor(R.color.red));
                textViewFilter.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                textViewSetting.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                break;
            case R.id.linear_filter:
                interfaceforSelectFilter.callSelectFilterFragment();
                interfaceforSelectFilter.callUpdateFilter();
                mViewPager.setCurrentItem(3,false);
                imgDashboard.setBackgroundResource(R.drawable.dashboard_unselect);
                imgSetting.setBackgroundResource(R.drawable.setting_unselect);
                imgFilter.setBackgroundResource(R.drawable.filtere);
                imgProject.setBackgroundResource(R.drawable.project_unselect);
                imgResource.setBackgroundResource(R.drawable.resource_unselect);

                textViewDashboard.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                textViewProject.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                textViewFilter.setTextColor(this.getResources().getColor(R.color.red));
                textViewSetting.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                textViewResource.setTextColor(this.getResources().getColor(R.color.textview_color_grey));

                break;
            case R.id.linear_setting:
                mViewPager.setCurrentItem(4,false);
                imgDashboard.setBackgroundResource(R.drawable.dashboard_unselect);
                imgProject.setBackgroundResource(R.drawable.project_unselect);
                imgFilter.setBackgroundResource(R.drawable.filter_unselect);
                imgSetting.setBackgroundResource(R.drawable.setting);
                imgResource.setBackgroundResource(R.drawable.resource_unselect);

                textViewDashboard.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                textViewProject.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                textViewFilter.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                textViewSetting.setTextColor(this.getResources().getColor(R.color.red));
                textViewResource.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                break;
            default:
                mViewPager.setCurrentItem(0,false);
                imgDashboard.setBackgroundResource(R.drawable.dashboard);
                imgSetting.setBackgroundResource(R.drawable.setting_unselect);
                imgProject.setBackgroundResource(R.drawable.project_unselect);
                imgFilter.setBackgroundResource(R.drawable.filter_unselect);
                imgResource.setBackgroundResource(R.drawable.resource_unselect);

                textViewDashboard.setTextColor(this.getResources().getColor(R.color.red));
                textViewProject.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                textViewResource.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                textViewSetting.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
                textViewFilter.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
        }
    }
    /* this method sets the page in ViewPager on click */
    public void setViewPagerPage(int pageNo){
        mViewPager.setCurrentItem(pageNo);
        imgDashboard.setBackgroundResource(R.drawable.dashboard_unselect);
        imgProject.setBackgroundResource(R.drawable.project);
        imgFilter.setBackgroundResource(R.drawable.filter_unselect);
        imgSetting.setBackgroundResource(R.drawable.setting_unselect);
        imgResource.setBackgroundResource(R.drawable.project_unselect);

        textViewDashboard.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
        textViewProject.setTextColor(this.getResources().getColor(R.color.red));
        textViewFilter.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
        textViewSetting.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
        textViewResource.setTextColor(this.getResources().getColor(R.color.textview_color_grey));
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(interfaceProjectClicked != null){
            interfaceProjectClicked.clickedProject();
        }
        preferenes.isContolFirstTime("");
        preferenes.saveTaskPosition(0);
        preferenes.saveAgreegatePosition(0);
        preferenes.clearCurrentFiredFilter();
        preferenes.saveIsProgressShow("yes");
        //preferenes.saveIsGeneralFilterDialogshow("no");

        preferenes.clearClickedAgreegatePosition();
        preferenes.clearClickedTaskPosition();
        preferenes.clearTaskLastFiredFilter();
        preferenes.clearAgreegateLastFiredFilter();

        preferenes.clearUserDefinedType();

        preferenes.clearResourcefilter();
        preferenes.clearFireResourcefilter();
        preferenes.clearLastFiredFilterAll();

        preferenes.clear404Count();
    }
    /* this method sets Language dynamically*/
    public void setLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config,this.getResources().getDisplayMetrics());

        textViewDashboard.setText(getString(R.string.dashboard_footer));
        textViewFilter.setText(getString(R.string.filter_footer));
        textViewSetting.setText(getString(R.string.setting_footer));
        textViewProject.setText(R.string.project_footer);
        textViewResource.setText(R.string.resource_header);
    }
    @Override
    public void onBackPressed() {
    }
    /* this is interface method called when click on task listview item */
    @Override
    public void ontaskClicked(String json,String id,String comefrom,int pos) {
        interfaceLoadWebview.callWebviewFromDashboard(json,id,comefrom,pos);
        setViewPagerPage(1);
    }

    /* this is interface method called when click on agreegate listview item */
    @Override
    public void onAgreegateClicked(String strjson, String taskId,String comefrom,int pos) {
        interfaceLoadWebview.callWebviewFromDashboard(strjson,taskId,comefrom,pos);
        setViewPagerPage(1);
    }

    /* this interface only for mobile screen */
    @Override
    public void ontaskClickedMobile(String json,String id,String comefrom,int pos) {
        interfaceLoadWebview.callWebviewFromDashboard(json,id,comefrom,pos);
        setViewPagerPage(1);
    }

    /* this interface only for mobile screen */
    @Override
    public void onAgreegateClickedMobile(String strjson, String taskId,String comefrom,int pos) {
        interfaceLoadWebview.callWebviewFromDashboard(strjson,taskId,comefrom,pos);
        setViewPagerPage(1);
    }

    @Override
    public void profileChangedSetting() {
    }
    @Override
    public void changeLanguageTextfilter() {
    }
    @Override
    public void changeLanguageDatefitler() {
    }
    @Override
    public void changeLanguageUserDefinedFilter() {
    }
    @Override
    public void changeLanguageFolderFilter() {
    }
    @Override
    public void changeLanguageProfile() {

    }
    @Override
    public void changeLanguage() {
    }
    @Override
    public void changeLanguageSelectFilter() {

    }
    @Override
    public void changeLanguageSetting() {

    }
    @Override
    public void changeLanguageResource() {
    }
    @Override
    public void changeLanguageHome() {
        setLanguage(preferenes.getLanguage());
    }
    /* this is interface method called when profile is change from profile fragment */
    @Override
    public void profileChanged() {
//        this will chanage language in dashboard
        if(interfaceChangeLanguage != null) {
            interfaceChangeLanguage.changeLanguage();
        }

//        this will change langauge in project screen
        if(interfaceChangeLanguageProfile != null) {
            interfaceChangeLanguageProfile.changeLanguageProfile();
        }

//        this will change langauge in select filter screen
        if(interfaceChangeLanguageSelectFilter != null) {
            interfaceChangeLanguageSelectFilter.changeLanguageSelectFilter();
        }

//        this will change langauge in setting screen
        if(interfaceChangeLanguageSetting != null) {
            interfaceChangeLanguageSetting.changeLanguageSetting();
        }

//        this will change language in text filter screen
        if(interfaceChangeLanguageTextFilter != null) {
            interfaceChangeLanguageTextFilter.changeLanguageTextfilter();
        }

//        this will change langauge in date filter screen
        if(interfaceChangeLanguageDateFilter != null) {
            interfaceChangeLanguageDateFilter.changeLanguageDatefitler();
        }

//        this will change langauge in userDefined filter screen
        if(interfaceChangeLanguageUserDefinedFilter != null) {
            interfaceChangeLanguageUserDefinedFilter.changeLanguageUserDefinedFilter();
        }

//        this will change lagnage in folder filter screen
        if(interfaceChangeLanguageFolderFilter != null) {
            interfaceChangeLanguageFolderFilter.changeLanguageFolderFilter();
        }

//        this will change langauge in current home activity
        setLanguage(preferenes.getLanguage());
    }
    @Override
    public void clickedProject() {
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
    public static HomeActivity getInstace(){
        return homeActivity;
    }
    public void showUserActiveDialog()
    {
        final Dialog dialog;
        dialog = new Dialog(this);
        View dialogView = View.inflate(this, R.layout.custom_dialog_user_inactive, null);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView textViewTitle = (TextView) dialogView.findViewById(R.id.title);
        TextView textViewMessage = (TextView) dialogView.findViewById(R.id.message);
        textViewTitle.setText(getString(R.string.user_active_title));
        textViewMessage.setText(getString(R.string.user_active_message));

        TextView textViewCancel = (TextView) dialogView.findViewById(R.id.cancel);
        LinearLayout linearMain = (LinearLayout)dialogView.findViewById(R.id.linear_sign_out_main);
        GlobalClass.setupTypeface(linearMain, GlobalClass.fontLight);

        dialog.show();
        if(GlobalClass.isTablet(this)){
            if(GlobalClass.getScreenOrientation(this) == 1){
                dialog.getWindow().setLayout(GlobalClass.screenWidth(this) / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
            }else {
                dialog.getWindow().setLayout(GlobalClass.screenWidth(this) / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

        }else {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }
}
