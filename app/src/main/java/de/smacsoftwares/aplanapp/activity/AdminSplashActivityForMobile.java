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
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.URLSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import de.smacsoftwares.aplanapp.Model.TermsConditionModel;
import de.smacsoftwares.aplanapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import de.smacsoftwares.aplanapp.adapter.TermsConditionAdapter;
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

/**
 * Created by SSOFT4 on 10/8/2016.
 */
public class AdminSplashActivityForMobile extends Activity implements View.OnClickListener {
    Dialog dialogAlert;
    String strLangauge="",organizationId="";
    TextView txtadminheder,txtemail,txtnewslatter,txtcompanyname,txtname,textViewTermsCondition,
    textViewCancel,textviewAgree;
    private EditText editEmail, editName, editCompony;
    RelativeLayout relativeAdminSaplaceScreen;
    Button btnSend;
    ToggleButton toggleButtonNewsletter;
    ToggleButton toggleButtonisDemoUser;
    ImageView imgEnglish,imgGermany;
    CheckBox chkTermsCondition;
    Activity activity;
    PreferencesHelper preferences;

    ProgressDialog progressDialog = null;
    String strNewletter="0",strdemoUser="0";
    TextView txtlink;
    ToggleButton toggleButtonHasLicense;
    LinearLayout linearLayoutOrganizationIdContainer;
    EditText editOrganizationId;
    TextView labelHasLicense,lableOrganizationId;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_admin_splacescreen);
        //MyApplication.component(this).inject(this);
        preferences = new PreferencesHelper(this);
        if(!GlobalClass.isTablet(this)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        GlobalClass.fontRegular = Typeface.createFromAsset(this.getAssets(),GlobalClass.fontPath);
        GlobalClass.fontLight = Typeface.createFromAsset(this.getAssets(),GlobalClass.fontPathLight);
        GlobalClass.fontBold = Typeface.createFromAsset(this.getAssets(),GlobalClass.fontPathBold);
        GlobalClass.fontMedium = Typeface.createFromAsset(this.getAssets(),GlobalClass.fontPathMedium);
        activity = this;
        if(preferences.getLanguage() != null && !preferences.getLanguage().equalsIgnoreCase("")){
            strLangauge = preferences.getLanguage();
        }
        else {
            strLangauge = getString(R.string.language_germany);
            preferences.saveLanguage(strLangauge);
        }
        labelHasLicense = (TextView) findViewById(R.id.labelHasLicense);
        lableOrganizationId = (TextView) findViewById(R.id.lableOrganizationId);

        chkTermsCondition = (CheckBox)findViewById(R.id.checkbox_terms_condition);
        chkTermsCondition.setChecked(false);
        textViewTermsCondition = (TextView)findViewById(R.id.textview_terms_condition);
        textViewTermsCondition.setOnClickListener(this);
        imgGermany=(ImageView) findViewById(R.id.img_germany);
        imgEnglish=(ImageView) findViewById(R.id.img_english);
        if(preferences.getLanguage() != null && !preferences.getLanguage().equalsIgnoreCase("")){
            strLangauge = preferences.getLanguage();
        }
        else {
            strLangauge = getString(R.string.language_germany);
            preferences.saveLanguage(strLangauge);
        }
        if(strLangauge.equalsIgnoreCase(getString(R.string.language_english))){
            imgEnglish.setImageResource(R.drawable.img_england_select);
            imgGermany.setImageResource(R.drawable.img_germany_unselect);
        }else {
            imgEnglish.setImageResource(R.drawable.img_england_unselect);
            imgGermany.setImageResource(R.drawable.img_germany_select);
        }
        imgGermany.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                strLangauge=getString(R.string.language_germany);
                imgEnglish.setImageResource(R.drawable.img_england_unselect);
                imgGermany.setImageResource(R.drawable.img_germany_select);
                setLanguage(strLangauge);
                preferences.saveLanguage(strLangauge);
            }
        });
        imgEnglish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                strLangauge=getString(R.string.language_english);
                imgEnglish.setImageResource(R.drawable.img_england_select);
                imgGermany.setImageResource(R.drawable.img_germany_unselect);
                setLanguage(strLangauge);
                preferences.saveLanguage(strLangauge);
            }
        });
        editOrganizationId = (EditText) findViewById(R.id.edit_organizationId);
        relativeAdminSaplaceScreen = (RelativeLayout)findViewById(R.id.relative_admin_splash_screen);
        GlobalClass.setupTypeface(relativeAdminSaplaceScreen, GlobalClass.fontLight);

        relativeAdminSaplaceScreen = (RelativeLayout)findViewById(R.id.relative_admin_splash_screen);
        GlobalClass.setupTypeface(relativeAdminSaplaceScreen, GlobalClass.fontBold);

        txtadminheder = (TextView) findViewById(R.id.txt_admin_header);
        txtadminheder.setTypeface(GlobalClass.fontBold);
        txtemail = (TextView) findViewById(R.id.lableemail);
        txtcompanyname = (TextView) findViewById(R.id.lablecompony);
        txtnewslatter = (TextView) findViewById(R.id.lablenewslatter);
        txtname = (TextView) findViewById(R.id.lablename);

        linearLayoutOrganizationIdContainer = (LinearLayout) findViewById(R.id.linearLayoutOrganizationIdContainer);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editName = (EditText) findViewById(R.id.edit_name);
        editCompony = (EditText) findViewById(R.id.edit_compony);
        btnSend = (Button) findViewById(R.id.btn_send);
        toggleButtonNewsletter = (ToggleButton) findViewById(R.id.switch_newsletter);
        toggleButtonNewsletter.setChecked(true);
        toggleButtonisDemoUser = (ToggleButton) findViewById(R.id.toggle_button2);
        toggleButtonHasLicense = (ToggleButton) findViewById(R.id.switch_hasLicense);
        toggleButtonHasLicense.setChecked(false);
        btnSend.setOnClickListener(this);
        toggleButtonNewsletter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked)
            {
                LogApp.e(" &&&&&& ", " toggle chacked change : " + isChecked);
                if(isChecked){
                    strNewletter="1";
                }
                else {
                    strNewletter="0";
                }
            }
        });
        toggleButtonisDemoUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked)
            {
                if(isChecked){
                    strdemoUser="1";
                }
                else {
                    strdemoUser="0";
                }
            }
        });
        toggleButtonHasLicense.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    linearLayoutOrganizationIdContainer.setVisibility(View.VISIBLE);
                }
                else
                {
                    linearLayoutOrganizationIdContainer.setVisibility(View.GONE);
                }
                //hasLicense=isChecked;
            }
        });
        txtlink= (TextView)findViewById(R.id.txt_ssoft_link);
        txtlink.setVisibility(View.GONE);

        txtlink.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString ss = new SpannableString(getString(R.string.my_smsc_link));
        ss.setSpan(new URLSpanNoUnderline("http://www.smacsoftwares.de/"), 0, 36, 0);
        ss.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 36, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtlink.setText(ss);

        setLanguage(strLangauge);
    }
    @Override
    public void onBackPressed() {
        /*System.exit(0);*/
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                if (GlobalClass.isNetworkAvailable(activity)) {
                    if (checkValidation()) {
                        getUserDetailsservice();
                    }
                }
                else {
                    GlobalClass.showCustomDialogInternet(activity);
                }
                break;
            case R.id.textview_terms_condition:
                showTermsConditionDialog();
                break;
        }

    }
    /* this method checks validation - if validation is proper than it will return true otherwise it will false */
    public boolean checkValidation() {
        boolean flag = true;
        if (TextUtils.isEmpty(editName.getText().toString()))
        {
            GlobalClass.showCustomMessageDialog(activity,"",
                    getString(R.string.enter_valid_name));
            return false;
        }
        else if (!GlobalClass.isEmailValid(editEmail.getText().toString().trim())) {
            GlobalClass.showCustomDialogEmailValidation(activity);
            return false;
        }
        else if (TextUtils.isEmpty(editEmail.getText().toString()))
        {
            GlobalClass.showCustomMessageDialog(activity,"",
                    getString(R.string.enter_valid_email));
            return false;
        }
        else if (TextUtils.isEmpty(editCompony.getText().toString()))
        {
            GlobalClass.showCustomMessageDialog(activity,"",
                    getString(R.string.enter_valid_compony_name));
            return false;
        }
        else if(!chkTermsCondition.isChecked()){
            alertTermsCondition();
            return false;
        }
        return flag;
    }
    /* this method set Language runtime */
    public void setLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config,this.getResources().getDisplayMetrics());

        txtadminheder.setText(getString(R.string.admin_header));
        txtemail.setText(getString(R.string.email));
        txtcompanyname.setText(getString(R.string.companyname));
        txtnewslatter.setText(R.string.newsletter);
        txtname.setText(R.string.name_label);
        btnSend.setText(R.string.send);
        lableOrganizationId.setText(R.string.organizationId);
        labelHasLicense.setText(R.string.hasLicense);
        textViewTermsCondition.setText(getString(R.string.terms_condition));
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
    private void getUserDetailsservice() {
        try {
            String status="";
            String strEmail = editEmail.getText().toString().trim();
            String strCompony = editCompony.getText().toString().trim();
            String strName = editName.getText().toString().trim();
            organizationId = getString(R.string.static_organization);

            progressDialog = new ProgressDialog(AdminSplashActivityForMobile.this);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            SpannableString ss = new SpannableString(getString(R.string.sending_details));
            ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new CustomTypefaceSpan("", GlobalClass.fontRegular), 0, ss.length(), 0);
            progressDialog.setMessage(ss);
            progressDialog.show();

            if(toggleButtonHasLicense.isChecked()){
                organizationId = editOrganizationId.getText().toString().trim();
                strdemoUser="0";
            }
            else {
                strdemoUser="1";
            }
            String deviceType=GlobalClass.getDeviceType(activity)+"/"+GlobalClass.getOsVersion()+"/"+GlobalClass.getDeviceModel();

            RequestBody email = RequestBody.create(MediaType.parse("text/plain"),strEmail);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"),strName);
            RequestBody compony = RequestBody.create(MediaType.parse("text/plain"), strCompony);
            RequestBody isNewletter = RequestBody.create(MediaType.parse("text/plain"),strNewletter);
            RequestBody isDemouser = RequestBody.create(MediaType.parse("text/plain"),strdemoUser);
            RequestBody orgUser = RequestBody.create(MediaType.parse("text/plain"), organizationId);
            RequestBody DeviceToken = RequestBody.create(MediaType.parse("text/plain"),preferences.getDeviceToken());
            //RequestBody DeviceToken = RequestBody.create(MediaType.parse("text/plain"), "12345");
            RequestBody devicetype = RequestBody.create(MediaType.parse("text/plain"),"Android/Tablet");
            RequestBody appVersion = RequestBody.create(MediaType.parse("text/plain"),GlobalClass.getAppversion(this));
            RequestBody termsCondition = RequestBody.create(MediaType.parse("text/plain"),"1");

            GenericHelper helper = new GenericHelper(this);
            Call<String> call = helper.getRetrofitUserDetail().getUserDetail(email, name, compony, isNewletter, isDemouser,
                    orgUser,DeviceToken,devicetype,appVersion,termsCondition);
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
                                preferences.saveDomain(objectPayload.getString("Domain"));
                                preferences.saveOrgUser(organizationId);
                                GlobalClass.SERVICE_URL = preferences.getDomain() + "Get/";
                                if(toggleButtonHasLicense.isChecked()){
                                    preferences.saveIsLicence(true);
                                }
                                else {
                                    preferences.saveIsLicence(false);
                                }

                                preferences.saveIsAdminScreenShow(false);
                                Intent intent = new Intent(AdminSplashActivityForMobile.this, SelectLoginType.class);
                                startActivity(intent);
                                finish();
                            } else {
                                LogApp.e(" no response from service :  ", " response from service : ");
                                if(progressDialog != null){
                                    progressDialog.dismiss();
                                }
                                GlobalClass.showCustomMessageDialog(activity,getString(R.string.error_title),getString(R.string.something_is_wrong));
                            }
                        } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == Integer.parseInt(getString(R.string.ORGANIZATION_USER_NOT_FOUND))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.invalid_login_title),getString(R.string.invalid_login_message));
                        }
                        else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == Integer.parseInt(getString(R.string.SERVER_NOT_FOUND_CODE))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.enter_organization_url_title),getString(R.string.enter_valid_url));
                        }
                        else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == Integer.parseInt(getString(R.string.MISSING_FIELDS))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            GlobalClass.showCustomMessageDialog(activity,getString(R.string.enter_organization_url_title),getString(R.string.enter_valid_url));
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
                    if (t instanceof IOException) {
                        GlobalClass.showCustomMessageDialog(activity,getString(R.string.server_timeout_exception),getString(R.string.server_timeout_exception));
                    }
                }
            });
        } catch (Exception e) {
            LogApp.e(" while login service : ", " in catch : " + e.toString());
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }
    public void showTermsConditionDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_terms_condition);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtdatedheder = (TextView) dialog.findViewById(R.id.textview_title);
        textViewCancel = (TextView) dialog.findViewById(R.id.textview_cancel);
        textviewAgree = (TextView) dialog.findViewById(R.id.textview_agree);

        textViewCancel.setOnTouchListener(new CustomTouchListenerDate(textViewCancel));
        //textviewAgree.setOnTouchListener(new CustomTouchListenerDate(textviewAgree));

        textviewAgree.setEnabled(false);
        textviewAgree.setTextColor(getResources().getColor(R.color.grey_dashboard));

        txtdatedheder.setTypeface(GlobalClass.fontBold);

        String jsonToparse = getString(R.string.terms_condition_json);
        ArrayList<TermsConditionModel> arrayList = new ArrayList<>();
        try {
            JSONObject obj = (JSONObject) new JSONTokener(jsonToparse).nextValue();
            JSONArray jsonArray = obj.getJSONArray("Terms");
            if(jsonArray.length() > 0){
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject innerObject = jsonArray.getJSONObject(i);
                    String title = innerObject.getString("Title");
                    String detail = innerObject.getString("Detail");

                    TermsConditionModel model = new TermsConditionModel();
                    model.setStrMessage(detail);
                    model.setStrTitle(title);
                    arrayList.add(model);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final ListView listViewTerms = (ListView)dialog.findViewById(R.id.listviewTerms);
        if(arrayList.size() > 0){
            listViewTerms.setAdapter(null);
            TermsConditionAdapter adapter = new TermsConditionAdapter(this,arrayList);
            listViewTerms.setAdapter(adapter);
        }
        listViewTerms.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && (listViewTerms.getLastVisiblePosition() - listViewTerms.getHeaderViewsCount() -
                        listViewTerms.getFooterViewsCount()) >= (listViewTerms.getCount() - 1)) {

                    textviewAgree.setEnabled(true);
                    textviewAgree.setTextColor(getResources().getColor(R.color.black_color));

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        textviewAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                chkTermsCondition.setChecked(true);
            }
        });

        //setLanguageDateLanguage(preferences.getLanguage());
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }
    public class CustomTouchListenerDate implements View.OnTouchListener
    {
        TextView txt;
        CustomTouchListenerDate(TextView textView){
            txt=textView;
        }
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    txt.setTextColor(getResources().getColor(R.color.grey_dashboard));
                    //white
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    txt.setTextColor(getResources().getColor(R.color.black_color)); //black
                    break;
            }
            return false;
        }
    }
    public void alertTermsCondition()
    {
        dialogAlert = new Dialog(this);
        View dialogView = View.inflate(this, R.layout.custom_dilaog_terms_alert, null);
        dialogAlert.setContentView(dialogView);
        dialogAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAlert.setCanceledOnTouchOutside(false);
        TextView textViewNo = (TextView) dialogView.findViewById(R.id.cancel);
        TextView textViewYes = (TextView) dialogView.findViewById(R.id.agree);
        LinearLayout linearMain = (LinearLayout)dialogView.findViewById(R.id.linear_sign_out_main);
        GlobalClass.setupTypeface(linearMain, GlobalClass.fontLight);

        dialogAlert.show();
        if(GlobalClass.isTablet(this)){
            if(GlobalClass.getScreenOrientation(this) == 1){
                dialogAlert.getWindow().setLayout(GlobalClass.screenWidth(this) / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
            }else {
                dialogAlert.getWindow().setLayout(GlobalClass.screenWidth(this) / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

        }else {
            dialogAlert.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        textViewNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dialogAlert.dismiss();
            }
        });
        textViewYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalClass.isNetworkAvailable(activity)) {
                    dialogAlert.dismiss();
                    chkTermsCondition.setChecked(true);
                    getUserDetailsservice();
                }
                else {
                    GlobalClass.showCustomDialogInternet(activity);
                }

            }
        });
    }
}
