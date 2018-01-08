package de.smacsoftwares.aplanapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.Model.ForgottResponse;
import de.smacsoftwares.aplanapp.R;

import javax.inject.Inject;

import de.smacsoftwares.aplanapp.fragment.FragmentDashBoard;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.InterfaceServiceClass;
import de.smacsoftwares.aplanapp.util.LogApp;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ForgottPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSubmit;
    EditText editemail;
    TextView textviewBack;
    Activity activity;
    PreferencesHelper preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forgott_password);
        //MyApplication.component(this).inject(this);
        preferences = new PreferencesHelper(this);
        initControl();
    }
    //    this method intialize control and class all initial work
    public void initControl(){
        GlobalClass.fontRegular = Typeface.createFromAsset(this.getAssets(), GlobalClass.fontPath);
        if(!GlobalClass.isTablet(this)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        activity = this;
        btnSubmit=(Button)findViewById(R.id.btnsend);
        editemail=(EditText)findViewById(R.id.editemail);
        textviewBack=(TextView)findViewById(R.id.textviewback);
        btnSubmit.setOnClickListener(this);
        textviewBack.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsend:
                if(GlobalClass.isNetworkAvailable(activity)){
                    if(checkValidation())
                    {
                        //callForgottPasswordWebService();
                    }
                }
                else {
                    GlobalClass.showCustomDialogInternet(activity);
                }
                break;
            case R.id.textviewback:
                if(GlobalClass.isTablet(activity)) {
                    startActivity(new Intent(ForgottPasswordActivity.this,LoginActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(ForgottPasswordActivity.this, LoginMobileActivity.class));
                    finish();
                }
                break;
        }
    }
    /* this method checks validation */
    public boolean checkValidation() {
        boolean flag = true;
        if(TextUtils.isEmpty(editemail.getText().toString()))
        {
            GlobalClass.showCustomDialogFieldRequired(activity);
            flag=false;
        }
        else {
            if(!GlobalClass.isEmailValid(editemail.getText().toString())){
                GlobalClass.showCustomDialogEmailValidation(activity);
                flag=false;
            }
            else {
                flag=true;
            }
        }
        return flag;
    }
    /*private void callForgottPasswordWebService() {
        try {
            final ProgressDialog progressDialog = ProgressDialog.show(this,"",getString(R.string.please_wait),false,false);
            //Creating a rest adapter
            RestAdapter adapter = new RestAdapter.Builder().setEndpoint(preferences.getServerUrlUser()).build();
            //Creating an object of our api interface
            InterfaceServiceClass interfaceObject = adapter.create(InterfaceServiceClass.class);
            interfaceObject.callForgott(editemail.getText().toString(),new Callback<ForgottResponse>() {
                @Override
                public void success(ForgottResponse forgottResponse, Response response) {
                    try {
                        if(response != null) {
                            if (forgottResponse.getStatus() == 0) {
                                LogApp.e(" forgott success ", " response from service : " + forgottResponse.getStatus() + " id : " + forgottResponse.getMessage());
                                progressDialog.dismiss();
                                Intent i = new Intent(activity,FragmentDashBoard.class);
                                startActivity(i);
                                finish();
                            }
                            else if(forgottResponse.getStatus() == 1){
                            }
                        }
                    }
                    catch (Exception e){
                        progressDialog.dismiss();
                    }
                }
                @Override
                public void failure(RetrofitError error) {
                    LogApp.e(" forgottt failed "," response from service : "+error.toString());
                    progressDialog.dismiss();
                }
            });
        }
        catch (Exception e){
            LogApp.e(" while forgottt service : "," in catch : "+e.toString());
        }
    }*/
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
}
