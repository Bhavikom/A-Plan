package de.smacsoftwares.aplanapp.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.R;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.smacsoftwares.aplanapp.util.CustomTypefaceSpan;
import de.smacsoftwares.aplanapp.util.DatabaseHandler;
import de.smacsoftwares.aplanapp.util.GenericHelper;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.LogApp;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectLoginType extends AppCompatActivity {

    @BindView(R.id.btn_log_in) Button btnLogIn;
    @BindView(R.id.btn_try_demo) Button btnDemo;
    @BindView(R.id.img_english) ImageView imgEnglish;
    @BindView(R.id.img_germany) ImageView imgGermany;
    @BindView(R.id.textview_title)TextView textviewTitle;
    PreferencesHelper preferences;
    DatabaseHandler dbHandler;

    String  userIdTemp = "", strLangauge = "",userIdPermenent = "", strEmail = "",
            strUsername = "", strFullname = "", strAccessTokenfromserver = "", strOrganizationId = "",
            strFinalAccessToken = "",strTokenForControl="",strOrgName="";
    ProgressDialog progressDialog;
    Activity activity;
    TextView txtlink,textViewVersionNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_login_type);
        ButterKnife.bind(this);
        //MyApplication.component(this).inject(this);
        preferences = new PreferencesHelper(this);
        dbHandler = new DatabaseHandler(this);
        if (!GlobalClass.isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        activity = this;
        GlobalClass.fontRegular = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPath);
        GlobalClass.fontMedium = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPathMedium);
        GlobalClass.fontLight = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPathLight);
        GlobalClass.fontBold = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPathBold);
        setTypeFace();
        preferences.saveDomain("");
        btnDemo.setEnabled(true);
        if (preferences.getLanguage() != null) {
            if (preferences.getLanguage().equalsIgnoreCase(getString(R.string.language_english))) {
                strLangauge = getString(R.string.language_english);
                imgEnglish.setBackgroundResource(R.drawable.img_england_select);
                imgGermany.setBackgroundResource(R.drawable.img_germany_unselect);
            } else {
                strLangauge = getString(R.string.language_germany);
                imgEnglish.setBackgroundResource(R.drawable.img_england_unselect);
                imgGermany.setBackgroundResource(R.drawable.img_germany_select);
            }
        } else {
            strLangauge = getString(R.string.language_germany);
            imgEnglish.setBackgroundResource(R.drawable.img_england_unselect);
            imgGermany.setBackgroundResource(R.drawable.img_germany_select);
        }
        setLanguage(strLangauge);
        /* checking here that it should show inactive dailog or not == if user comes here
        * after inactive from notification show INACTIVE INFO dialog otherwise no neeed to show */
        if(preferences.getInActiveDialog().equalsIgnoreCase("yes")){
            preferences.saveShowInActiveDialog("no");
            userInactiveDialog();
        }
        textViewVersionNo = (TextView)findViewById(R.id.txt_version_no);
        textViewVersionNo.setText("Version "+GlobalClass.getAppversion(this));
        txtlink= (TextView)findViewById(R.id.txt_ssoft_link);
        txtlink.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString ss = new SpannableString(getString(R.string.my_smsc_link));
        ss.setSpan(new URLSpanNoUnderline("http://www.smacsoftwares.de/"), 0, 36, 0);
        ss.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 36, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtlink.setText(ss);
    }
    public void setTypeFace(){
        textviewTitle.setTypeface(GlobalClass.fontBold);
        btnDemo.setTypeface(GlobalClass.fontLight);
        btnLogIn.setTypeface(GlobalClass.fontLight);
    }
    @OnClick(R.id.img_germany)
    public void onClickGermany(){
        strLangauge = getString(R.string.language_germany);
        imgEnglish.setBackgroundResource(R.drawable.img_england_unselect);
        imgGermany.setBackgroundResource(R.drawable.img_germany_select);
        setLanguage(strLangauge);
        preferences.saveLanguage(getString(R.string.language_germany));
    }
    @OnClick(R.id.img_english)
    void onClickEnglish(){
        strLangauge = getString(R.string.language_english);
        imgEnglish.setBackgroundResource(R.drawable.img_england_select);
        imgGermany.setBackgroundResource(R.drawable.img_germany_unselect);
        setLanguage(strLangauge);
        preferences.saveLanguage(getString(R.string.language_english));
    }
    @OnClick(R.id.btn_log_in)
    void onClickLogIn(){
        if(GlobalClass.isTablet(activity)) {
            startActivity(new Intent(SelectLoginType.this,GetUrlActivity.class));
            //finish();
        }else {
            startActivity(new Intent(SelectLoginType.this, GetUrlMobileActivity.class));
            //finish();
        }
    }
    @OnClick(R.id.btn_try_demo)
    void onClickTryDemo(){
        getAuthenticateDetails();
    }

    @Override
    public void onBackPressed() {
        /*System.gc();
        System.exit(0);*/
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
    /* this method sets language dynamically */
    public void setLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());

        btnDemo.setText(getString(R.string.try_demo));
        btnLogIn.setText(getString(R.string.log_in));

    }
    /* this method is for calling authenticatin in application from server */
    private void getAuthenticateDetails() {
        try {
            progressDialog = new ProgressDialog(SelectLoginType.this);

            SpannableString ss = new SpannableString(getString(R.string.please_wait));
            ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new CustomTypefaceSpan("", GlobalClass.fontRegular), 0, ss.length(), 0);
            progressDialog.setMessage(ss);
            progressDialog.show();

            String userName="demo-2";
            String password="Demo@123";
            String deviceType=GlobalClass.getDeviceType(activity)+"/"+GlobalClass.getOsVersion()+"/"+GlobalClass.getDeviceModel();

            RequestBody User = RequestBody.create(MediaType.parse("text/plain"), userName);
            RequestBody Password = RequestBody.create(MediaType.parse("text/plain"), password);
            RequestBody DeviceType = RequestBody.create(MediaType.parse("text/plain"), deviceType);
            RequestBody DeviceToken = RequestBody.create(MediaType.parse("text/plain"),preferences.getDeviceToken());
            //RequestBody DeviceToken = RequestBody.create(MediaType.parse("text/plain"), "12345");
            RequestBody AppVersion = RequestBody.create(MediaType.parse("text/plain"), GlobalClass.getAppversion(SelectLoginType.this));
            RequestBody orgUser = RequestBody.create(MediaType.parse("text/plain"),preferences.getOrgUser());
            /* here user has selected demo that'w why user demo url in whole application also save in preferences */
            preferences.saveDomain(GlobalClass.DEMO_URL);
            GenericHelper helper = new GenericHelper(this);
            Call<String> call = helper.getRetrofit().getAuthenticate(User, Password, DeviceType, DeviceToken, AppVersion,orgUser);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        Headers header = response.headers();
                        if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 0) {
                            String strResponse = response.body();
                            JSONObject obj = (JSONObject) new JSONTokener(strResponse).nextValue();
                            String status = obj.getString("Status");
                            if (status.equalsIgnoreCase("0")) {
                                JSONObject objectPayload = obj.getJSONObject("Payload");
                                JSONObject objectLoginInfo = objectPayload.getJSONObject("LoggedInUser");

                                preferences.saveIsDemoUser(true);
                                userIdPermenent = objectLoginInfo.getString("Id");
                                strEmail = objectLoginInfo.getString("Email");
                                strFullname = objectLoginInfo.getString("FullName");
                                strUsername = objectLoginInfo.getString("UserName");
                                strAccessTokenfromserver = objectLoginInfo.getString("AccessToken");
                                strOrganizationId = objectPayload.getString("Id");
                                strTokenForControl = objectPayload.getString("Token");
                                strOrgName = objectPayload.getString("OrganizationName");

                                /* saving access token in preference to send in every service header */
                                /* access token = getfromservie+useridLength+userid+epochtime*/
                                strFinalAccessToken = strAccessTokenfromserver + "" + userIdPermenent.length() + userIdPermenent;//+GlobalClass.getEpochTime();

                                preferences.saveAccessToken(strFinalAccessToken);
                                preferences.saveOrganizationId(strOrganizationId);
                                preferences.saveTokenforControl(strTokenForControl);
                                preferences.saveOrganizationName(strOrgName);
                                preferences.saveEmail(strEmail);
                                preferences.saveUserName(strUsername);

                                GlobalClass.strAccessToken = strFinalAccessToken;
                                userIdTemp = userIdPermenent;
                                preferences.saveAppLogout(false);
//                                this will call getUserProfile service and navigate to dashboard
                                getProfiles();

                            } else {
                                if(progressDialog != null){
                                    progressDialog.dismiss();
                                }
                                GlobalClass.showCustomMessageDialog(activity,getString(R.string.error_title),getString(R.string.something_is_wrong));
                            }
                        } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == Integer.parseInt(getString(R.string.MAX_ALLOWED_USER_INCREASED))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.invalid_login_title),getString(R.string.invalid_login_message));

                        } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == Integer.parseInt(getString(R.string.MAX_USER_LOGIN_EXEEDED))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.max_user_login_exceed_title),
                                    getString(R.string.max_user_login_exceed));
                        } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == Integer.parseInt(getString(R.string.ORGANIZATION_IS_DISABLED_BY_SYS_ADMIN))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.organization_is_disable_by_sys_admin_title),
                                    getString(R.string.organization_is_disable_by_sys_admin_message));
                        } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == Integer.parseInt(getString(R.string.ORGANIZATION_USER_IS_DISABLED_BY_SYS_ADMIN))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.organization_user_is_disabled_by_sys_admin_title),
                                    getString(R.string.organization_user_is_disabled_by_sys_admin_message));
                        }
                        else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == Integer.parseInt(getString(R.string.LICENSE_EXPIRED))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.licence_expired_title),
                                    getString(R.string.licence_expired_message));
                        }
                        else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == Integer.parseInt(getString(R.string.LICENSE_FILE_MISSING))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.licence_file_missing_title),
                                    getString(R.string.licence_file_missing_message));
                        }
                        else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == Integer.parseInt(getString(R.string.USER_DOES_NOT_HAVE_ROLE))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.role_not_found_title),
                                    getString(R.string.app_right_has_been_revoked));
                        }
                        else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == Integer.parseInt(getString(R.string.USER_UNAUTHORIZED))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.invalid_login_title),
                                    getString(R.string.invalid_login_message));
                        }
                        else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == Integer.parseInt(getString(R.string.SERVER_NOT_FOUND_CODE))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.enter_organization_url_title),
                                    getString(R.string.enter_valid_url));
                            preferences.save404Count(preferences.get404Count()+1);
                            if(preferences.get404Count() > 3){
                                preferences.clear404Count();
                                if(GlobalClass.isTablet(activity)){
                                    startActivity(new Intent(SelectLoginType.this,AdminSplashActivity.class));
                                    finish();
                                }
                                else{
                                    startActivity(new Intent(SelectLoginType.this,AdminSplashActivityForMobile.class));
                                    finish();
                                }
                            }
                        }
                        else {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.error_title),
                                    getString(R.string.something_is_wrong));
                        }
                    } catch (Exception e) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        GlobalClass.showCustomMessageDialog(activity,getString(R.string.error_title),getString(R.string.something_is_wrong));
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }
                    if(t instanceof UnknownHostException){
                        GlobalClass.showCustomMessageDialog(activity,getString(R.string.enter_organization_url_title),getString(R.string.enter_valid_url));
                        preferences.save404Count(preferences.get404Count()+1);
                        if(preferences.get404Count() > 3){
                            preferences.clear404Count();
                            if(GlobalClass.isTablet(activity)){
                                startActivity(new Intent(SelectLoginType.this,AdminSplashActivity.class));
                                finish();
                            }
                            else{
                                startActivity(new Intent(SelectLoginType.this,AdminSplashActivityForMobile.class));
                                finish();
                            }
                        }
                    }
                    else if (t instanceof IOException) {
                        GlobalClass.showCustomMessageDialog(activity,getString(R.string.server_timeout_exception),
                                getString(R.string.server_timeout_exception));
                    }
                }
            });
        } catch (Exception e) {
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            GlobalClass.showCustomMessageDialog(activity,getString(R.string.error_title),
                    getString(R.string.something_is_wrong));
        }
    }

    //    this method call getProfile service
    public void getProfiles() {
        try {
            SpannableString ss = new SpannableString(getString(R.string.please_wait));
            ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new CustomTypefaceSpan("", GlobalClass.fontRegular), 0, ss.length(), 0);

            GenericHelper helper = new GenericHelper(this);
            Call<String> call = helper.getRetrofit().getProfiles(strLangauge);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Headers header = response.headers();
                    try {
                        if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 0) {
                            String attributName = "";
                            String strResponse = response.body();
                            JSONObject obj = (JSONObject) new JSONTokener(strResponse).nextValue();
                            String status = obj.getString("Status");
                            if (status.equalsIgnoreCase("0")) {
                                JSONArray jsonArrayPayload = obj.getJSONArray("Payload");
                                if (jsonArrayPayload.length() > 0) {
                                    for (int i = 0; i < jsonArrayPayload.length(); i++) {
                                        JSONObject objSub = jsonArrayPayload.getJSONObject(i);

                                        JSONArray jsonArrayAttribute = objSub.getJSONArray("Attributes");

                                        String ProfileId = objSub.getString("Id");
                                        ArrayList<String> arraylistKey = new ArrayList<String>();
                                        ArrayList<String> arraylistValue = new ArrayList<String>();
                                        String attributeValue = "";
                                        if (jsonArrayAttribute.length() > 0) {
                                            for (int j = 0; j < jsonArrayAttribute.length(); j++) {
                                                JSONObject objAttribute = jsonArrayAttribute.getJSONObject(j);
                                                attributName = objAttribute.getString("AttributeName");
                                                if (attributName.equalsIgnoreCase("RibbonFilter")) {
                                                    attributeValue = objAttribute.getString("AttributeValue");
                                                    arraylistKey.add(attributName);
                                                    arraylistValue.add(attributeValue);
                                                    //dbHandler.addProfileAttribute(ProfileId,attributName,attributeValue);

                                                } else if (attributName.equalsIgnoreCase("GeneralFilter")) {
                                                    attributeValue = objAttribute.getString("AttributeValue");
                                                    if (!attributeValue.equalsIgnoreCase("")) {
                                                        JSONObject objGeneralFilter = (JSONObject) new JSONTokener(attributeValue).nextValue();
                                                        Iterator keys = objGeneralFilter.keys();
                                                        String key = "";
                                                        String value = "";
                                                        String type = "";
                                                        while (keys.hasNext()) {
                                                            key = (String) keys.next();
                                                            value = objGeneralFilter.getString(key);
                                                            if (key.equalsIgnoreCase(getString(R.string.folder_group_id))) {
                                                                type = getString(R.string.FolderFilter_text);
                                                            } else if (key.equalsIgnoreCase(getString(R.string.key_ResourceId)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_ResourceGroupId)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_anyText)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_priFrom)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_priTO)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_level)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_indentyNo)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_Task)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_Client)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_Responsible))) {
                                                                type = getString(R.string.TextFilter_text);

                                                            } else if (key.equalsIgnoreCase(getString(R.string.key_text1_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_text2_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_text3_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_text4_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_text5_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_text6_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_text7_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_text8_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_text9_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_text10_string))) {
                                                                type = getString(R.string.UserDefinedFilter_text);

                                                            } else if (key.equalsIgnoreCase(getString(R.string.key_number1_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_number2_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_number3_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_number4_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_number5_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_number6_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_number7_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_number8_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_number9_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_number10_string))) {
                                                                type = getString(R.string.UserDefindedFilterForNumber);

                                                            } else if (key.equalsIgnoreCase(getString(R.string.key_criterion1_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_criterion1Type_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_criterion1FromDate_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_criterion1ToDate_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_criterion1TimeFrom_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_criterion1TimeTo_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_criterion1TimeUnit_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_criterion2_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_criterion2FromDate_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_criterion2ToDate_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_criterion2Type_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_criterion2TimeFrom_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_criterion2TimeTo_string)) ||
                                                                    key.equalsIgnoreCase(getString(R.string.key_criterion2TimeUnit_string))) {
                                                                type = getString(R.string.DateFilter_text);

                                                            }
                                                            if (type.equalsIgnoreCase(getString(R.string.FolderFilter_text))) {
                                                                dbHandler.addGeneralFilter(ProfileId, key, GlobalClass.removeCommaAttheEnd(value), preferences.getLanguage(), type, userIdPermenent, "");
                                                            } else {
                                                                dbHandler.addGeneralFilter(ProfileId, key, GlobalClass.removeCommaAttheEnd(value), preferences.getLanguage(), type, userIdPermenent, GlobalClass.removeCommaAttheEnd(value));
                                                            }

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        //String ColumnsDetail = objSub.getString("ColsDetail");
                                        String Name = objSub.getString("Name");
                                        String UserId = objSub.getString("UserId");
                                        String ExpandedId = "";
                                        //String ExpandedId = objSub.getString("ExpandedIds");

                                        preferences.saveProfileId(ProfileId);
                                        preferences.saveLanguage(strLangauge);
                                        /* adding default profile to database for table screen */
                                        String mAttributeKey = "";
                                        String mAttributeValue = "";
                                        if (arraylistValue.size() > 0) {
                                            mAttributeKey = TextUtils.join(",", arraylistKey);
                                            mAttributeValue = TextUtils.join(",", arraylistValue);
                                        }
                                        /* adding all profile from service to profile master table */
                                        dbHandler.addAllProfileToDB(ProfileId, Name, attributName, userIdPermenent,
                                                ExpandedId, userIdTemp, preferences.getLanguage(),
                                                getResources().getString(R.string.weekm), mAttributeKey, mAttributeValue, mAttributeKey, mAttributeValue);
                                        dbHandler.addRibbonFilter(ProfileId, userIdPermenent, preferences.getLanguage(), mAttributeKey
                                                , mAttributeValue, "", getResources().getString(R.string.weekm));

                                    }
                                }
                                /* saving logged in user detail in preferences */
                                preferences.saveUserID(userIdPermenent);
                                preferences.saveEmail(strEmail);
                                preferences.saveFullName(strFullname);
                                preferences.saveUserName(strUsername);
                                preferences.saveUserLogged(true);

                                if(progressDialog != null){
                                    progressDialog.dismiss();
                                }
                                Intent i = new Intent(activity, HomeActivity.class);
                                startActivity(i);
                                finish();

                            } else {
                                LogApp.e(" no response from service :  ", " response from service : ");
                                if(progressDialog != null){
                                    progressDialog.dismiss();
                                }
                                GlobalClass.showCustomMessageDialog(activity,getString(R.string.error_title),
                                        getString(R.string.something_is_wrong));
                            }
                        } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1009) {
                                /* whenever access token is expire get 1009
                                from service and call here getNewdeviceToken servcie and replace onld one with new device token */
                            updateAccessToken();
                        }
                        else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 404) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.enter_organization_url_title),
                                    getString(R.string.enter_valid_url));
                            preferences.save404Count(preferences.get404Count()+1);
                            if(preferences.get404Count() > 3){
                                preferences.clear404Count();
                                if(GlobalClass.isTablet(activity)){
                                    startActivity(new Intent(SelectLoginType.this,AdminSplashActivity.class));
                                    finish();
                                }
                                else{
                                    startActivity(new Intent(SelectLoginType.this,AdminSplashActivityForMobile.class));
                                    finish();
                                }
                            }
                        }
                        else {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                        }
                    } catch (Exception e) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }
                    if(t instanceof UnknownHostException){
                        GlobalClass.showCustomMessageDialog(activity,getString(R.string.enter_organization_url_title),
                                getString(R.string.enter_valid_url));
                        preferences.save404Count(preferences.get404Count()+1);
                        if(preferences.get404Count() > 3){
                            preferences.clear404Count();
                            if(GlobalClass.isTablet(activity)){
                                startActivity(new Intent(SelectLoginType.this,AdminSplashActivity.class));
                                finish();
                            }
                            else{
                                startActivity(new Intent(SelectLoginType.this,AdminSplashActivityForMobile.class));
                                finish();
                            }
                        }
                    }
                    else if (t instanceof IOException) {
                        GlobalClass.showCustomMessageDialog(activity,getString(R.string.server_timeout_exception),
                                getString(R.string.server_timeout_exception));
                    }
                }
            });
        } catch (Exception e) {
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            GlobalClass.showCustomMessageDialog(activity,getString(R.string.error_title),getString(R.string.something_is_wrong));
        }
    }
    /* this method will call get new device token service to get refreshed device token */
    public void updateAccessToken() {
        GenericHelper helper = new GenericHelper(this);
        Call<String> call = helper.getRetrofit().getNewAccessToken();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Headers header = response.headers();
                try {
                    if (header.get(getResources().getString(R.string.x_status)) != null &&
                            Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1010) {
                        if (header.get(getResources().getString(R.string.x_message)) != null) {
                            String token = header.get(getResources().getString(R.string.x_message));
                                /* saving access token in preference to send in every service header */
                            String strFinalAccessToken = token + preferences.getUserID().length() + preferences.getUserID();

                            preferences.saveAccessToken(strFinalAccessToken);
                            GlobalClass.strAccessToken = preferences.getAccessToken();
                            getProfiles();
                        }
                    }
                } catch (Exception e) {
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                LogApp.e(" ##### ", " response from get device token : " + t.toString());
            }
        });
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View v = getCurrentFocus();

        if (v != null &&
                (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                v instanceof EditText &&
                !v.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            v.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + v.getLeft() - scrcoords[0];
            float y = ev.getRawY() + v.getTop() - scrcoords[1];

            if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom())
                hideKeyboard(this);
        }
        return super.dispatchTouchEvent(ev);
    }
    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
    public void userInactiveDialog()
    {
        final Dialog dialog;
        dialog = new Dialog(this);
        View dialogView = View.inflate(this, R.layout.custom_dialog_user_inactive, null);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView textViewTitle = (TextView) dialogView.findViewById(R.id.title);
        TextView textViewMessage = (TextView) dialogView.findViewById(R.id.message);

        if(preferences.getNotificationActionType().equalsIgnoreCase("4")){
            textViewTitle.setText(getString(R.string.user_deleted));
            textViewMessage.setText(getString(R.string.user_deleted_message));
            preferences.saveNotificationActionType("");
            preferences.saveShowActiveDialog("no");
        }
        else if(preferences.getNotificationActionType().equalsIgnoreCase("5")){
            textViewTitle.setText(getString(R.string.invalid_aplan_licence));
            textViewMessage.setText(getString(R.string.invalid_aplan_licence_message));
            preferences.saveNotificationActionType("");
            preferences.saveShowActiveDialog("no");
        }
        else if(preferences.getShowDialogType().equalsIgnoreCase(getString(R.string.LICENSE_FILE_MISSING))){
            textViewTitle.setText(getString(R.string.licence_file_missing_title));
            textViewMessage.setText(getString(R.string.licence_file_missing_message));
            preferences.saveShowDialogType("");
        }
        else if(preferences.getShowDialogType().equalsIgnoreCase(getString(R.string.LICENSE_EXPIRED))){
            textViewTitle.setText(getString(R.string.licence_expired_title));
            textViewMessage.setText(getString(R.string.licence_expired_message));
            preferences.saveShowDialogType("");
        }
        else if(preferences.getShowDialogType().equalsIgnoreCase(getString(R.string.ORGANIZATION_IS_DISABLED_BY_SYS_ADMIN))){
            textViewTitle.setText(getString(R.string.organization_disalbed_title));
            textViewMessage.setText(getString(R.string.organization_disalbed_message));
            preferences.saveShowDialogType("");
        }
        else {
            textViewTitle.setText(getString(R.string.organization_inactive_title));
            textViewMessage.setText(getString(R.string.organization_inactive_message));
        }


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
    public class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(true);
        }
    }
}
