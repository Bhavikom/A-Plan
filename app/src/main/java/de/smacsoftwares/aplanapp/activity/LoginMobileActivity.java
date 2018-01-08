package de.smacsoftwares.aplanapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class LoginMobileActivity extends AppCompatActivity implements View.OnClickListener {
    String[] arrayGeneralFilter;
    String userIdTemp = "", strLangauge = "";
    String userIdPermenent = "", strEmail = "", strUsername = "", strFullname = "",
            strAccessTokenfromserver = "", strOrganizationId = "", strFinalAccessToken = "",strTokenForControl="",strOrgName="";;
    Button btnSingin;
    EditText editTextServer, editTextDatabase, editTextuser, editTextPassword, editSqlServerName, editSqlLogin, editSqlPassword;
    TextView textviewForgottPassword, txtlink;
    ImageView imgVisibility, imgEnglish, imgGermany,imgBack;
    Activity activity;
    PreferencesHelper preferences;
    DatabaseHandler dbHandler;
    TextView textviewUsername, textviewTitle, textviewBetavesion;
    TextView textviewPassword;
    RelativeLayout relativeLogin;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        //MyApplication.component(this).inject(this);
        preferences = new PreferencesHelper(this);
        dbHandler = new DatabaseHandler(this);
        initControl();
    }

    //    this method intialize control and class all initial work
    public void initControl() {
        arrayGeneralFilter = new String[]{getString(R.string.folder_group_id),
                getString(R.string.key_ResourceId), getString(R.string.key_ResourceGroupId),
                getString(R.string.key_anyText), getString(R.string.key_priFrom),
                getString(R.string.key_priTO), getString(R.string.key_level),
                getString(R.string.key_indentyNo), getString(R.string.key_Task),
                getString(R.string.key_Client), getString(R.string.key_Responsible),
                getString(R.string.key_text1_string), getString(R.string.key_text2_string),
                getString(R.string.key_text3_string), getString(R.string.key_text4_string),
                getString(R.string.key_text5_string), getString(R.string.key_text6_string),
                getString(R.string.key_text7_string), getString(R.string.key_text8_string),
                getString(R.string.key_text9_string), getString(R.string.key_text10_string),
                getString(R.string.key_number1_string), getString(R.string.key_number2_string),
                getString(R.string.key_number3_string), getString(R.string.key_number4_string),
                getString(R.string.key_number5_string), getString(R.string.key_number6_string),
                getString(R.string.key_number7_string), getString(R.string.key_number8_string),
                getString(R.string.key_number9_string), getString(R.string.key_number10_string),
                getString(R.string.key_criterion1_string), getString(R.string.key_criterion1Type_string),
                getString(R.string.key_criterion1FromDate_string), getString(R.string.key_criterion1ToDate_string),
                getString(R.string.key_criterion1TimeFrom_string), getString(R.string.key_criterion1TimeTo_string),
                getString(R.string.key_criterion1TimeUnit_string), getString(R.string.key_criterion2_string),
                getString(R.string.key_criterion2FromDate_string), getString(R.string.key_criterion2ToDate_string),
                getString(R.string.key_criterion2Type_string), getString(R.string.key_criterion2TimeFrom_string),
                getString(R.string.key_criterion2TimeTo_string), getString(R.string.key_criterion2TimeUnit_string),};
        if (!GlobalClass.isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        GlobalClass.fontRegular = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPath);
        GlobalClass.fontMedium = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPathMedium);
        GlobalClass.fontLight = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPathLight);
        GlobalClass.fontBold = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPathBold);
        activity = this;
        relativeLogin = (RelativeLayout) findViewById(R.id.relative_login);
        GlobalClass.setupTypeface(relativeLogin, GlobalClass.fontLight);

        imgBack=(ImageView) findViewById(R.id.imgback);
        imgBack.setOnClickListener(this);
        imgBack.setOnTouchListener(new CustomTouchListener());
        textviewBetavesion = (TextView) findViewById(R.id.lable_betaversion);
        textviewTitle = (TextView) findViewById(R.id.textview_login_title);
        textviewTitle.setTypeface(GlobalClass.fontBold);
        textviewBetavesion.setTypeface(GlobalClass.fontBold);
        textviewUsername = (TextView) findViewById(R.id.textviewusername);
        textviewPassword = (TextView) findViewById(R.id.textvewipasssword);
        imgEnglish = (ImageView) findViewById(R.id.img_english);
        imgGermany = (ImageView) findViewById(R.id.img_germany);
        imgEnglish.setOnClickListener(this);
        imgGermany.setOnClickListener(this);

        btnSingin = (Button) findViewById(R.id.btnsignin);
        editTextuser = (EditText) findViewById(R.id.edit_user);
        editTextPassword = (EditText) findViewById(R.id.editpassword);
        if(preferences.hasLicence()){
            editTextuser.setText("");
            editTextPassword.setText("");
            editTextuser.setTextColor(getResources().getColor(R.color.black_color));
            editTextPassword.setTextColor(getResources().getColor(R.color.black_color));
        }
        else {
            /*editTextuser.setEnabled(false);
            editTextPassword.setEnabled(false);
            editTextuser.setText("DemoUser");
            editTextPassword.setText("DemoUser@Braintool");
            editTextuser.setTextColor(getResources().getColor(R.color.grey_material));
            editTextPassword.setTextColor(getResources().getColor(R.color.grey_material));*/
        }
        textviewForgottPassword = (TextView) findViewById(R.id.textview_forgottpass);
        textviewForgottPassword.setEnabled(false);
        imgVisibility = (ImageView) findViewById(R.id.imgvisibility);
        imgVisibility.setOnClickListener(this);
        imgVisibility.setImageResource(R.drawable.visibility);

        txtlink = (TextView) findViewById(R.id.txt_ssoft_link);
        txtlink.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString ss = new SpannableString(getString(R.string.my_smsc_link));
        ss.setSpan(new URLSpanNoUnderline("http://www.smacsoftwares.de/"), 0, 36, 0);
        ss.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 36, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtlink.setText(ss);

        btnSingin.setOnClickListener(this);
        textviewForgottPassword.setOnClickListener(this);

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
        textviewUsername.setTypeface(GlobalClass.fontBold);
        editTextuser.setTypeface(GlobalClass.fontRegular);
        textviewPassword.setTypeface(GlobalClass.fontBold);
        editTextPassword.setTypeface(GlobalClass.fontRegular);
        btnSingin.setTypeface(GlobalClass.fontBold);
        setLanguage(strLangauge);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsignin:
                if (GlobalClass.isNetworkAvailable(activity)) {
                    if (checkValidation()) {
                        preferences.saveServerUrlUser(preferences.getServerUrl() + "/User");
                        preferences.saveServerUrlGet(preferences.getServerUrl() + "/Get/");
                        preferences.saveServerUrlTask(preferences.getServerUrl() + "/Tasks");
                        getAuthenticateDetails();
                    }
                } else {
                    GlobalClass.showCustomDialogInternet(activity);
                }
                break;
            case R.id.textview_forgottpass:
                Intent i = new Intent(activity, ForgottPasswordActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.imgvisibility:
                if (editTextPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    editTextPassword.setInputType(129);
                    imgVisibility.setImageResource(R.drawable.visibility);
                } else {
                    editTextPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    imgVisibility.setImageResource(R.drawable.visibility_off);
                }
                break;
            case R.id.img_english:
                strLangauge = getString(R.string.language_english);
                imgEnglish.setBackgroundResource(R.drawable.img_england_select);
                imgGermany.setBackgroundResource(R.drawable.img_germany_unselect);
                setLanguage(strLangauge);
                preferences.saveLanguage(getString(R.string.language_english));
                break;
            case R.id.img_germany:
                strLangauge = getString(R.string.language_germany);
                imgEnglish.setBackgroundResource(R.drawable.img_england_unselect);
                imgGermany.setBackgroundResource(R.drawable.img_germany_select);
                setLanguage(strLangauge);
                preferences.saveLanguage(getString(R.string.language_germany));
                break;
            case R.id.imgback:
                startActivity(new Intent(this,GetUrlMobileActivity.class));
                finish();
        }
    }
    /* this method checks validation */
    public boolean checkValidation() {
        boolean flag = true;
        if (TextUtils.isEmpty(editTextuser.getText().toString())) {
            GlobalClass.showCustomMessageDialog(activity,getString(R.string.enter_user_name_title),
                    getString(R.string.enter_valid_username));
            flag = false;
        }
        else if(TextUtils.isEmpty(editTextPassword.getText().toString())){
            GlobalClass.showCustomMessageDialog(activity,getString(R.string.enter_password_title),
                    getString(R.string.enter_valid_password));
            flag = false;
        }
        else {
            if (!GlobalClass.isEmailValid(editTextuser.getText().toString())) {
                flag = true;
            } else {
                flag = true;
            }
        }
        return flag;
    }
    /* this method is for calling authenticatin in application from server */
    private void getAuthenticateDetails() {
        try {
            progressDialog = new ProgressDialog(LoginMobileActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);

            SpannableString ss = new SpannableString(getString(R.string.please_wait));
            ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new CustomTypefaceSpan("", GlobalClass.fontRegular), 0, ss.length(), 0);
            progressDialog.setMessage(ss);
            progressDialog.show();

            String deviceType=GlobalClass.getDeviceType(activity)+"/"+GlobalClass.getOsVersion()+"/"+GlobalClass.getDeviceModel();

            RequestBody User = RequestBody.create(MediaType.parse("text/plain"), editTextuser.getText().toString());
            RequestBody Password = RequestBody.create(MediaType.parse("text/plain"), editTextPassword.getText().toString());
            RequestBody DeviceType = RequestBody.create(MediaType.parse("text/plain"), deviceType);
            RequestBody DeviceToken = RequestBody.create(MediaType.parse("text/plain"),preferences.getDeviceToken());
            //RequestBody DeviceToken = RequestBody.create(MediaType.parse("text/plain"), "12345");
            RequestBody AppVersion = RequestBody.create(MediaType.parse("text/plain"), GlobalClass.getAppversion(LoginMobileActivity.this));
            RequestBody orgUser = RequestBody.create(MediaType.parse("text/plain"),preferences.getOrgUser());

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

                                preferences.saveIsDemoUser(false);
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
                                GlobalClass.showCustomMessageDialog(activity,getString(R.string.error_title),
                                        getString(R.string.something_is_wrong));
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
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.licence_file_missing_title),getString(R.string.licence_file_missing_message));
                        }
                        else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == Integer.parseInt(getString(R.string.ORGANIZATION_USER_NOT_FOUND))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.invalid_login_title),
                                    getString(R.string.invalid_login_message));
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
                                    startActivity(new Intent(LoginMobileActivity.this,AdminSplashActivity.class));
                                    finish();
                                }
                                else{
                                    startActivity(new Intent(LoginMobileActivity.this,AdminSplashActivityForMobile.class));
                                    finish();
                                }
                            }
                        }
                        else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == Integer.parseInt(getString(R.string.MISSING_FIELDS))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.error_title),getString(R.string.something_is_wrong));
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
                        GlobalClass.showCustomMessageDialog(activity,getString(R.string.error_title),
                                getString(R.string.something_is_wrong));
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
                                startActivity(new Intent(LoginMobileActivity.this,AdminSplashActivity.class));
                                finish();
                            }
                            else{
                                startActivity(new Intent(LoginMobileActivity.this,AdminSplashActivityForMobile.class));
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
                                Intent i = new Intent(activity,HomeActivity.class);
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
                                    startActivity(new Intent(LoginMobileActivity.this,AdminSplashActivity.class));
                                    finish();
                                }
                                else{
                                    startActivity(new Intent(LoginMobileActivity.this,AdminSplashActivityForMobile.class));
                                    finish();
                                }
                            }
                        }
                        else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == Integer.parseInt(getString(R.string.MISSING_FIELDS))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.error_title),getString(R.string.something_is_wrong));
                        }
                        else {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.error_title),getString(R.string.something_is_wrong));
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
                        GlobalClass.showCustomMessageDialog(activity,getString(R.string.enter_organization_url_title),
                                getString(R.string.enter_valid_url));
                        preferences.save404Count(preferences.get404Count()+1);
                        if(preferences.get404Count() > 3){
                            preferences.clear404Count();
                            if(GlobalClass.isTablet(activity)){
                                startActivity(new Intent(LoginMobileActivity.this,AdminSplashActivity.class));
                                finish();
                            }
                            else{
                                startActivity(new Intent(LoginMobileActivity.this,AdminSplashActivityForMobile.class));
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
    public void onBackPressed() {
        if (!GlobalClass.isTablet(this)) {
            //preferences.clearAdminshow();
            startActivity(new Intent(LoginMobileActivity.this,GetUrlMobileActivity.class));
            finish();
        }
        else {
            //preferences.clearAdminshow();
            startActivity(new Intent(LoginMobileActivity.this,GetUrlActivity.class));
            finish();
        }

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
    /* this method sets language dynamically */
    public void setLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());

        textviewUsername.setText(getString(R.string.user_name));
        textviewPassword.setText(getString(R.string.password));
        btnSingin.setText(getString(R.string.log_in));
        textviewForgottPassword.setText(R.string.forgott_password);

        editTextuser.setHint(getString(R.string.hint_username));
        editTextPassword.setHint(getString(R.string.hint_password));
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
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onPause() {
        super.onPause();
        preferences.clear404Count();
    }
    private class CustomTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent)
        {
            switch (motionEvent.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    imgBack.setImageResource(R.drawable.back_press);
                    //white
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    imgBack.setImageResource(R.drawable.back);
                    break;
            }
            return false;
        }
    }
}
