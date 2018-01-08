package de.smacsoftwares.aplanapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.R;


import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.smacsoftwares.aplanapp.util.CustomTypefaceSpan;
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

public class GetUrlMobileActivity extends AppCompatActivity {

    @BindView(R.id.btn_continue) Button btnContinue;
    @BindView(R.id.edittext_url) EditText editTextUrl;
    @BindView(R.id.img_english)
    ImageView imgEnglish;
    @BindView(R.id.img_germany) ImageView imgGermany;
    @BindView(R.id.lable_organization) TextView textViewLable;
    @BindView(R.id.imgback) ImageView imgBack;
    @BindView(R.id.textview_title) TextView textViewTitle;
    String strLangauge="";
    PreferencesHelper preferences;
    ProgressDialog progressDialog;
    Activity activity;
    TextView txtlink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_get_url);
        //MyApplication.component(this).inject(this);
        preferences = new PreferencesHelper(this);
        ButterKnife.bind(this);
        activity=this;
        if (!GlobalClass.isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        GlobalClass.fontRegular = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPath);
        GlobalClass.fontMedium = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPathMedium);
        GlobalClass.fontLight = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPathLight);
        GlobalClass.fontBold = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPathBold);
        setTypeFace();
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
        if(preferences.getUrl() != null && !preferences.getUrl().equalsIgnoreCase("")){
            editTextUrl.setText(preferences.getUrl());
        }
        imgBack.setOnTouchListener(new CustomTouchListener());
        setLanguage(strLangauge);

        txtlink= (TextView)findViewById(R.id.txt_ssoft_link);
        txtlink.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString ss = new SpannableString(getString(R.string.my_smsc_link));
        ss.setSpan(new URLSpanNoUnderline("http://www.smacsoftwares.de/"), 0, 36, 0);
        ss.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 36, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtlink.setText(ss);

    }
    public void setTypeFace(){
        textViewLable.setTypeface(GlobalClass.fontLight);
        btnContinue.setTypeface(GlobalClass.fontBold);
        editTextUrl.setTypeface(GlobalClass.fontRegular);
        textViewTitle.setTypeface(GlobalClass.fontBold);
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
    @OnClick(R.id.btn_continue)
    public void onClick(){
        if (GlobalClass.isNetworkAvailable(activity)) {
            if(checkValidation()){
            /* call get url service call here */
                getUrlService();
            }
        }else {
            GlobalClass.showCustomDialogInternet(activity);
        }

    }
    @OnClick(R.id.imgback)
    public void onClickBack(){
        //startActivity(new Intent(GetUrlMobileActivity.this,SelectLoginType.class));
        finish();
    }
    /* this method checks validation */
    public boolean checkValidation() {
        boolean flag = true;
        if (TextUtils.isEmpty(editTextUrl.getText().toString())) {
            GlobalClass.showCustomMessageDialog(activity,getString(R.string.enter_organization_url_title),getString(R.string.enter_valid_url));
            flag = false;
        }
        else if(!GlobalClass.validateUrl(editTextUrl.getText().toString().trim())){
            GlobalClass.showCustomMessageDialog(activity,getString(R.string.enter_organization_url_title),getString(R.string.enter_valid_url));
            flag = false;
        }
        else {
            flag = true;
        }
        return flag;
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(GetUrlMobileActivity.this,SelectLoginType.class));
        finish();
    }
    /* this method sets language dynamically */
    public void setLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
        editTextUrl.setHint(getString(R.string.url));
        btnContinue.setText(getString(R.string.Continue));
        //imgBack.setText(getString(R.string.Cancel));
        textViewLable.setText(getString(R.string.organization));
    }
    /* this method is for calling authenticatin in application from server */
    private void getUrlService() {
        try {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            SpannableString ss = new SpannableString(getString(R.string.please_wait));
            ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new CustomTypefaceSpan("", GlobalClass.fontRegular), 0, ss.length(), 0);
            progressDialog.setMessage(ss);
            progressDialog.show();

            GlobalClass.GET_URL=editTextUrl.getText().toString()+GlobalClass.MANAGEMENT;
            final RequestBody orgId = RequestBody.create(MediaType.parse("text/plain"),"1");
            GenericHelper helper = new GenericHelper(this);
            Call<String> call = helper.getRetrofitGetUrl().getHostedUrl(orgId);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        Headers header = response.headers();
                        /*if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 0) {*/
                        if(response.body() != null) {
                            String strResponse = response.body();
                            JSONObject obj = (JSONObject) new JSONTokener(strResponse).nextValue();
                            String status = obj.getString("Status");
                            if (status.equalsIgnoreCase("0")) {
                                //JSONObject objectPayload = obj.getJSONObject("Payload");
                                //JSONObject objectLoginInfo = objectPayload.getJSONObject("LoggedInUser");
                                preferences.saveDomain(editTextUrl.getText().toString()+GlobalClass.MANAGEMENT);
                                preferences.saveUrl(editTextUrl.getText().toString());
                                if (GlobalClass.isTablet(activity)) {
                                    Intent i = new Intent(activity,LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(i);
                                    //startActivity(new Intent(GetUrlActivity.this, LoginActivity.class));
                                    finish();
                                } else {
                                    Intent i = new Intent(activity, LoginMobileActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(i);
                                    //startActivity(new Intent(GetUrlActivity.this, LoginActivity.class));
                                    finish();
                                }

                            } else {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                GlobalClass.showCustomMessageDialog(activity,getString(R.string.error_title),getString(R.string.something_is_wrong));
                            }
                        }else {
                            if (progressDialog != null) {
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
                        GlobalClass.showCustomMessageDialog(activity,getString(R.string.enter_organization_url_title),getString(R.string.enter_valid_url));
                        preferences.save404Count(preferences.get404Count()+1);
                        if(preferences.get404Count() > 3){
                            preferences.clear404Count();
                            if(GlobalClass.isTablet(activity)){
                                Intent i = new Intent(activity, AdminSplashActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(i);
                                //startActivity(new Intent(GetUrlActivity.this, LoginActivity.class));
                                finish();
                            }
                            else{
                                Intent i = new Intent(activity, AdminSplashActivityForMobile.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(i);
                                //startActivity(new Intent(GetUrlActivity.this, LoginActivity.class));
                                finish();
                            }
                        }
                    }
                    else if (t instanceof IOException) {
                        GlobalClass.showCustomMessageDialog(activity,getString(R.string.enter_organization_url_title),getString(R.string.enter_valid_url));
                    }
                }
            });
        } catch (Exception e) {
            GlobalClass.showCustomMessageDialog(activity,getString(R.string.enter_organization_url_title),getString(R.string.enter_valid_url));
            if(progressDialog != null) {
                progressDialog.dismiss();
            }
            LogApp.e(""," exception while login : "+e.toString());
        }
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
    public class CustomTouchListener implements View.OnTouchListener {
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

