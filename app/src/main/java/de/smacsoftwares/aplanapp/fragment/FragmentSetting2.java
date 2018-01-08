package de.smacsoftwares.aplanapp.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import de.smacsoftwares.aplanapp.Model.DrawerModelClass;
import de.smacsoftwares.aplanapp.Model.ProfileModelClass;
import de.smacsoftwares.aplanapp.R;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Stack;

import javax.inject.Inject;

import de.smacsoftwares.aplanapp.activity.AdminSplashActivity;
import de.smacsoftwares.aplanapp.activity.AdminSplashActivityForMobile;
import de.smacsoftwares.aplanapp.activity.HomeActivity;
import de.smacsoftwares.aplanapp.activity.SelectLoginType;
import de.smacsoftwares.aplanapp.adapter.DrawerAdapter;
import de.smacsoftwares.aplanapp.util.CustomTypefaceSpan;
import de.smacsoftwares.aplanapp.util.DatabaseHandler;
import de.smacsoftwares.aplanapp.util.GenericHelper;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.InterfaceChangeLanguageSetting;
import de.smacsoftwares.aplanapp.util.LogApp;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static de.smacsoftwares.aplanapp.R.id.subject;

/**
 * Created by SSoft-13 on 17-03-2017.
 */

public class FragmentSetting2 extends Fragment implements FragmentProfile.IntefaceChangepass,
        AdapterView.OnItemClickListener,InterfaceChangeLanguageSetting
{
    Dialog dialog;
    View footerView,footerViewBlank;
    TextView txtfooterview;
    RelativeLayout relativeFooter;
    Context context;
    PreferencesHelper preferences;
    DatabaseHandler dbHandler;
    boolean flag_errorLog=true;
    /* all for navigation drawer */
    RelativeLayout relativeMain;
    DrawerAdapter adapter;
    ListView listviewDrawerItem;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawer;
    Toolbar toolbar;
    TextView textviewTitle;
    ArrayList<DrawerModelClass> drawerItems;
    View view;
    ArrayList<Bitmap> arraylistBitmpap = new ArrayList<>();
    FragmentSupportContact framgnet;
    FragmentManager fragmentManager = getFragmentManager();
    private Stack<Fragment> fragmentStack;
    Activity activity;
    WebView webview;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container,false);
        //MyApplication.component(getActivity()).inject(this);
        preferences = new PreferencesHelper(getActivity());
        dbHandler = new DatabaseHandler(getActivity());
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
        initControl();
        return view;
    }
    //    this method intialize control and class all initial work
    public void initControl()
    {
        GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);
        getActivity();
        framgnet = new FragmentSupportContact();
        fragmentStack = new Stack<Fragment>();

        /* this is for toolbar */
        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        //setSupportActionBar(toolbar);
        getActivity().setTitle(null);
        //getSupportActionBar().setTitle(null);
        relativeMain = (RelativeLayout)view.findViewById(R.id.relative_setting_main);
        GlobalClass.setupTypeface(relativeMain, GlobalClass.fontRegular);
        textviewTitle = (TextView)view.findViewById(R.id.toolbar_title);
        textviewTitle.setText(getString(R.string.support));
        textviewTitle.setTypeface(GlobalClass.fontMedium);
        /* this is for drawer layout*/
        drawer = (DrawerLayout)view.findViewById(R.id.drawer_layout);
        GlobalClass.setupTypeface(drawer, GlobalClass.fontLight);
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar,
                R.string.open_drawer, R.string.close_drawer);

        /* this is for drawer listview and setting adapter */
        listviewDrawerItem = (ListView)view.findViewById(R.id.left_drawer);
        addDrawerItem();
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(actionBarDrawerToggle);
       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/
        adapter = new DrawerAdapter(getActivity(), drawerItems);
        adapter.setInflater((LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE), getActivity());
        listviewDrawerItem.setAdapter(adapter);
        listviewDrawerItem.setOnItemClickListener(this);
        actionBarDrawerToggle.syncState();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new FragmentSupport()).commit();

        footerView=View.inflate(getContext(),R.layout.list_footer_link,null);
        footerViewBlank=View.inflate(getContext(),R.layout.list_footer_link2,null);

        txtfooterview = (TextView)footerView.findViewById(R.id.txt_smsc_link);
        txtfooterview.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString ss = new SpannableString(getString(R.string.my_smsc_link));
        ss.setSpan(new URLSpanNoUnderline("http://www.smacsoftwares.de/"), 0, 36, 0);
        ss.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 36, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtfooterview.setText(ss);

        listviewDrawerItem.addFooterView(footerViewBlank);
        listviewDrawerItem.addFooterView(footerView);




        if(preferences.isLogoutApp()){
            actiononLogoutApp(1);
        }else {
            showUserInactiveDialog();
        }

    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.setSelectedItem(position);
        adapter.notifyDataSetChanged();
        Fragment fragment;
        String tag = "";
        if (position == 0) {
            if(preferences.isDisableApp()){

            }
            else {
                textviewTitle.setText(getString(R.string.title_profile));
                fragment = new FragmentProfile();
                tag = "Profile";
                drawer.closeDrawer(listviewDrawerItem);
                getActivity().getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                //fragmentManager.beginTransaction().replace(R.id.container, fragment,tag).addToBackStack(tag).commit();
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, tag).commit();
            }

        }
        else if (position == 1) {
            if(preferences.isDisableApp()){

            }
            else {
                if(preferences.getUserProfile() != null && !preferences.getUserProfile().equalsIgnoreCase("")) {
                /* getting currnet profile and set as title */
                    ProfileModelClass model = new Gson().fromJson(preferences.getUserProfile(), ProfileModelClass.class);
                    textviewTitle.setText(getString(R.string.profile)+":"+model.getProfileName());
                }
                else {
                    textviewTitle.setText(getString(R.string.profile_default));
                }
                fragment = new FragmentTable();
                tag = "Table";
                drawer.closeDrawer(listviewDrawerItem);
                getActivity().getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                //fragmentManager.beginTransaction().replace(R.id.container, fragment,tag).addToBackStack(tag).commit();
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, tag).commit();
            }

        }
        else if (position == 2) {
            textviewTitle.setText(getString(R.string.support));
            fragment = new FragmentSupport();
            tag = "Support";
            drawer.closeDrawer(listviewDrawerItem);
            getActivity().getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            //fragmentManager.beginTransaction().replace(R.id.container, fragment,tag).addToBackStack(tag).commit();
            getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, tag).commit();
        }
        else if (position == 3) {
            if(flag_errorLog) {
                // call javascript method to sent log in email
                webview = new WebView(getActivity());
                WebSettings settings = webview.getSettings();
                settings.setJavaScriptEnabled(true);
                settings.setAllowFileAccessFromFileURLs(true);
                settings.setAllowUniversalAccessFromFileURLs(true);
                settings.setDomStorageEnabled(true);
                webview.getSettings().setJavaScriptEnabled(true);
                webview.getSettings().setLoadsImagesAutomatically(true);
                webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webview.addJavascriptInterface(new IJavascriptHandler(), "cpjs");
                webview.setWebViewClient(new MyWebViewClient());
                webview.loadUrl("file:///android_asset/index.html");
            }
        }
        else if(position == 4){
            singOutDialog(1);
            /* sign out funcationality */
        }
        /*else if (position == 5) {
            singOutDialog(2);
            *//* sign out funcationality *//*
        }*/
    }
    //webview client to handle response from js function and mail it
    public class MyWebViewClient extends WebViewClient {
        ProgressDialog progressDialog = null;
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressDialog = new ProgressDialog(getActivity());
            //progressDialog.create();

            SpannableString ss = new SpannableString(getString(R.string.please_wait));
            ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new CustomTypefaceSpan("", GlobalClass.fontRegular), 0, ss.length(), 0);

            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(ss);
            progressDialog.show();
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String url2 = "javascript:(function() { GetErrorLogs();})()";
            webview.loadUrl(url2);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }
    final class IJavascriptHandler {
        IJavascriptHandler() {
        }

        // This annotation is required in Jelly Bean and later:
        @JavascriptInterface
        public void sendToAndroid(String text) {
            LogApp.e(""," string from js : "+text);
            if(TextUtils.isEmpty(text) || text.equalsIgnoreCase("[]")){
                flag_errorLog = false;
                Toast.makeText(getActivity(), getString(R.string.no_error_log_available),
                        Toast.LENGTH_LONG).show();
                /*adapter.setSelectedItem(lastClickedPos);
                adapter.notifyDataSetChanged();*/
            }else {
                // this is called from JS with passed value
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","aplansupport@sambinfo.in", null));
                intent.putExtra(Intent.EXTRA_SUBJECT,subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }

        }
    }
    public void singOutDialog(final int pos)
    {
        dialog = new Dialog(getActivity());
        View dialogView = View.inflate(getContext(), R.layout.custom_dialog_signout, null);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView textViewNo = (TextView) dialogView.findViewById(R.id.No);
        TextView textViewYes = (TextView) dialogView.findViewById(R.id.yes);
        LinearLayout linearMain = (LinearLayout)dialogView.findViewById(R.id.linear_sign_out_main);
        GlobalClass.setupTypeface(linearMain, GlobalClass.fontLight);

        dialog.show();
        if(GlobalClass.isTablet(getActivity())){
            if(GlobalClass.getScreenOrientation(getActivity()) == 1){
                dialog.getWindow().setLayout(GlobalClass.screenWidth(getContext()) / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
            }else {
                dialog.getWindow().setLayout(GlobalClass.screenWidth(getContext()) / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
            }

        }else {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        textViewNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        textViewYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new signOutService().execute();
                signOutService("no");

            }
        });
    }
    public void showUserInactiveDialog()
    {
        dialog = new Dialog(getActivity());
        View dialogView = View.inflate(getContext(), R.layout.custom_dialog_user_inactive, null);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView textViewTitle = (TextView) dialogView.findViewById(R.id.title);
        TextView textViewMessage = (TextView) dialogView.findViewById(R.id.message);

        if(preferences.getShowDialogType().equalsIgnoreCase(getString(R.string.USER_DOES_NOT_HAVE_ROLE))){
            /* disable appp */
            textViewTitle.setText(getString(R.string.role_revoked_title));
            textViewMessage.setText(getString(R.string.role_revoked_message));
            preferences.saveShowDialogType("");
        }
        else if(preferences.getShowDialogType().equalsIgnoreCase(getString(R.string.ORGANIZATION_USER_IS_DISABLED_BY_SYS_ADMIN))){
            /* disable app */
            textViewTitle.setText(getString(R.string.user_disabled_by_system_admin_title));
            textViewMessage.setText(getString(R.string.user_disabled_by_system_admin_message));
            preferences.saveShowDialogType("");
        }
        else {
            textViewTitle.setText(getString(R.string.user_inactive_title));
            textViewMessage.setText(getString(R.string.user_inactive_message));
        }
        TextView textViewCancel = (TextView) dialogView.findViewById(R.id.cancel);
        LinearLayout linearMain = (LinearLayout)dialogView.findViewById(R.id.linear_sign_out_main);
        GlobalClass.setupTypeface(linearMain, GlobalClass.fontLight);

        dialog.show();
        if(GlobalClass.isTablet(getActivity())){
            if(GlobalClass.getScreenOrientation(getActivity()) == 1){
                dialog.getWindow().setLayout(GlobalClass.screenWidth(getContext()) / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
            }else {
                dialog.getWindow().setLayout(GlobalClass.screenWidth(getContext()) / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
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
    public void actionOndisableApp(){
        Fragment fragment;
        String tag = "";
        textviewTitle.setText(getString(R.string.support));
        fragment = new FragmentSupport();
        tag = "Support";
        drawer.closeDrawer(listviewDrawerItem);
        getActivity().getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        //fragmentManager.beginTransaction().replace(R.id.container, fragment,tag).addToBackStack(tag).commit();
        getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, tag).commit();
    }
    public void actiononLogoutApp(final int pos){
        //if(getActivity() != null){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Fragment fragment;
                String tag = "";
                textviewTitle.setText(getString(R.string.support));
                fragment = new FragmentSupport();
                tag = "Support";
                drawer.closeDrawer(listviewDrawerItem);
                getActivity().getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                //fragmentManager.beginTransaction().replace(R.id.container, fragment,tag).addToBackStack(tag).commit();
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, tag).commit();

                signOutService("yes");
                //new signOutService().execute();
            }
        });

        //}

    }

    /* this method will call get new device token service to get refreshed device token */
    public void updateAccessToken(final int pos){
        GenericHelper helper = new GenericHelper(getActivity());
        Call<String> call = helper.getRetrofit().getNewAccessToken();
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                LogApp.e(" ##### "," response from get device token : "+response.toString());
                Headers header = response.headers();
                try {
                    if (header.get(getResources().getString(R.string.x_status)) != null &&
                            Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1010) {
                        if(header.get(getResources().getString(R.string.x_message)) != null){
                            String token = header.get(getResources().getString(R.string.x_message));
                                /* saving access token in preference to send in every service header */
                                /* access token = getfromservie+useridLength+userid+epochtime*/
                            String strFinalAccessToken = token+preferences.getUserID().length()+preferences.getUserID();

                            preferences.saveAccessToken(strFinalAccessToken);
                            GlobalClass.strAccessToken=preferences.getAccessToken();
                            signOutService("yes");


                        }
                    }
                }catch (Exception e){
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                LogApp.e(" ##### "," response from get device token : "+t.toString());
            }
        });
    }
    private void signOutService(final String str)
    {
        try
        {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            //progressDialog.create();

            SpannableString ss=  new SpannableString(getString(R.string.please_wait));
            ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new CustomTypefaceSpan("", GlobalClass.fontRegular), 0, ss.length(), 0);

            progressDialog.setMessage(ss);
            progressDialog.show();

            String deviceType=GlobalClass.getDeviceType(getActivity())+"/"+GlobalClass.getOsVersion()+"/"+GlobalClass.getDeviceModel();

            RequestBody DeviceType = RequestBody.create(MediaType.parse("text/plain"), deviceType);
            RequestBody DeviceToken = RequestBody.create(MediaType.parse("text/plain"),preferences.getDeviceToken());
            //RequestBody DeviceToken = RequestBody.create(MediaType.parse("text/plain"), "12345");
            RequestBody AppVersion = RequestBody.create(MediaType.parse("text/plain"), GlobalClass.getAppversion(getActivity()));

            GenericHelper helper = new GenericHelper(getActivity());
            Call<String> call = helper.getRetrofit().singOut(DeviceType,DeviceToken,AppVersion);

            call.enqueue(new retrofit2.Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    /*String body = response.body();
                    if (body == null || body.equalsIgnoreCase("")) {
                        *//* body is null that's why call new Access token service here *//*
                        updateAccessToken();
                    } else if (body != null || !body.equalsIgnoreCase("")) {*/
                    Headers header = response.headers();
                    try {
                        if (header.get(getResources().getString(R.string.x_status)) != null && Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 0) {
                                /*String body = response.body();
                                if (body == null || body.equalsIgnoreCase("")) {
                                    updateAccessToken();
                                } else if (body != null || !body.equalsIgnoreCase("")) {

                                }*/
                            preferences.isContolFirstTime("");
                            preferences.saveTaskPosition(0);
                            preferences.saveAgreegatePosition(0);

                            preferences.saveUserLogged(false);
                            preferences.clearLoginData();
                            preferences.clearUserProfile();
                            preferences.clearCurrentFiredFilter();
                            preferences.clearUserDefinedType();
                                /* delete all profile while logout */
                            dbHandler.deleteAllUserProfile();

                            preferences.saveAppDisalbe(false);
                            preferences.saveAppLogout(false);
                            preferences.saveIsFilemissing(false);
                            preferences.saveIsLicenceExpire(false);
                            if(preferences.getNotificationActionType().equalsIgnoreCase("4")){
                                /* clearing database and some of preferences here */
                                preferences.clearServiceUrl();
                                dbHandler.clearDatabase();
                                //getActivity().deleteDatabase(GlobalClass.DATABASE_NAME);
                            }
                            //dbHandler.deleteSingleUserProfile(preferences.getUserIdTemp(), preferences.getProfileId());
                            //dbHandler.deleteUserProfileFromUserId(preferences.getUserIdTemp());
                                if(str.equalsIgnoreCase("yes")){
                                    preferences.saveShowInActiveDialog("yes");
                                }else {
                                    preferences.saveShowInActiveDialog("no");
                                }

                                if(GlobalClass.isTablet(getActivity())) {
                                    startActivity(new Intent(getActivity(), SelectLoginType.class));
                                    getActivity().finish();
                                }else {
                                    startActivity(new Intent(getActivity(), SelectLoginType.class));
                                    getActivity().finish();
                                }

                        } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1009) {
                                /* whenever access token is expire get 1009
                                from service and call here getNewdeviceToken servcie and replace onld one with new device token */
                            updateAccessToken(1);
                        }
                        else if(header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1004)
                        {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            // GlobalClass.showToast(activity,"Email is wrong");
                        }
                        else if(header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1005)
                        {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            //GlobalClass.showToast(activity,"Password is wrong");
                        }
                        else {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            //GlobalClass.showToast(getActivity(),getString(R.string.no_response_from_server));
                            GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.error_title),
                                    getString(R.string.no_response_from_server));
                        }
                    } catch (Exception e) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                    // }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }
                    if(t instanceof UnknownHostException){
                        preferences.clear404Count();
                        if(preferences.get404Count() > 3){
                            if(GlobalClass.isTablet(getActivity())){
                                startActivity(new Intent(getActivity(),AdminSplashActivity.class));
                                getActivity().finish();
                            }else {
                                startActivity(new Intent(getActivity(),AdminSplashActivityForMobile.class));
                                getActivity().finish();
                            }
                        }
                    }

                    //GlobalClass.showToast(getActivity(),getString(R.string.sing));
                }
            });
        }
        catch (Exception e){
            LogApp.e(" while login service : "," in catch : "+e.toString());
        }
    }
    public void setTitle(String title)
    {
        textviewTitle.setText(title);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
    public void addDrawerItem() {
        drawerItems = new ArrayList<>();
        String[] arrayTitle = {getString(R.string.title_profile),getString(R.string.table),getString(R.string.support),getString(R.string.submit_error_log),getString(R.string.sign_out)};
        int[] arrayImage = {R.drawable.drawer_profile, R.drawable.drawer_table
                , R.drawable.drawer_support, R.drawable.drawer_error_log,R.drawable.sign_out};
        for (int i = 0; i < arrayTitle.length; i++) {
            DrawerModelClass model = new DrawerModelClass();
            model.setDrawable(arrayImage[i]);
            model.setTitle(arrayTitle[i]);
            drawerItems.add(model);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void changeTitle() {
    }
    public class URLSpanNoUnderline extends URLSpan
    {
        public URLSpanNoUnderline(String url)
        {
            super(url);
        }
    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        context = activity;
        activity = activity;
        ((HomeActivity)context).interfaceChangeLanguageSetting = this;
    }
    @Override
    public void changeLanguageSetting()
    {
        if(footerView!= null){
            listviewDrawerItem.removeFooterView(footerView);
        }
        initControl();
    }
}

