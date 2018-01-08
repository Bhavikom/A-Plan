package de.smacsoftwares.aplanapp.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.smacsoftwares.aplanapp.Model.FilterModelClass;
import de.smacsoftwares.aplanapp.Model.GeneralFilterDataSet;
import de.smacsoftwares.aplanapp.Model.ProfileModelClass;
import de.smacsoftwares.aplanapp.Model.RibbonFilterDataSet;
import de.smacsoftwares.aplanapp.Model.RibbonShowSelectionClass;
import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.activity.HomeActivity;
import de.smacsoftwares.aplanapp.adapter.ProfileAdapter;
import de.smacsoftwares.aplanapp.adapter.ProjectWeekAdapter;
import de.smacsoftwares.aplanapp.util.CustomTypefaceSpan;
import de.smacsoftwares.aplanapp.util.DatabaseHandler;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.InterfaceChangeLanguageResource;
import de.smacsoftwares.aplanapp.util.InterfaceLoadWebviewResource;
import de.smacsoftwares.aplanapp.util.LogApp;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static de.smacsoftwares.aplanapp.R.id.webView;

/**
 * Created by SSoft-13 on 07-02-2017.
 */

public class FragmentResource extends Fragment implements InterfaceLoadWebviewResource,View.OnClickListener,
        InterfaceChangeLanguageResource, FragmentProfile.InterfaceProfileChanged2 {

    PreferencesHelper preferences;
    DatabaseHandler dbHandler;

    Context context;
    String strJsonforTask="",strTaskId="",profileIdToSendControl = "";

    TextView txtdatedheder,txtcomplition,txtstartproject,txtcompleteproject,txtcompltetoday,txtpendingproject,
            txtpassiveproject,txtcompltein1week,txttaskwithoutdate,txtcanceledprojecttask;
    TextView textviewDateDialog, textviewStatusDialog, textviewTrafficLightDialog, textviewWeekDialog;
    ImageView imgCompilationOverrun,imgStartedProject,imgCompeletedProject,imgCompleteToday,imgPendingProject,
            imgPassiveProject,imgCompletedWeek,imgTaskWithoutDate,imgCanceledProject;
    LinearLayout linearCompilationOverrun,linearStartedProject,linearCompeletedProject,linearCompleteToday,
            linearPendingProject,linearPassiveProject,linearCompletedWeek,linearTaskWithoutDate,linearCanceledProject;

    String webviewUrl="";
    String strCompilationOverrun="",strStartedProject="",strCompletedProject="",strCompletedToday="",
            strPendingProject="",strPassiveProject="",strCompletedWeek="",strTaskWithoutDate="",strCanceledProject="";
    boolean isComplitionOverrun,isStartedProject,isCompletedProject,isCompletedToday,isPendingProject,isPassiveProject,
            isCompletedWeek,isTaskWithoutDate,isCanceledProject;
    TextView txttraffichheder,txtred,txtyellow,txtgreen;
    TextView txtstatusheder,txtnoticearrow,txtreshow,txtlimitoverun,txtcritical,txtrequring;
    TextView txtaddprofile;
    TextView textViewAddProfile,textViewTitleProfile;

    ImageView imgNoticeArrow,imgReshow,imgLimitoverrun,imgCritical,imgRequiringClarification;
    LinearLayout linearNoticeArrow,linearReshow,linearLimitoverrun,linearCritical,linearRequiringClarification;

    String strNoticeArrow="",strReshow="",strLimitoverrun="",strCritical="",strRequiringClarification="";
    boolean isNoticeArrow,isReshow,isLimitOverrun,isCritical,isRequiringClarification;

    ImageView imgTrafficRed,imgTrafficYello,imgTrafficGreen;
    LinearLayout linearTrafficRed,linearTrafficYello,linearTrafficGreen;
    RelativeLayout relativeMain;

    String strTrafficRed="",strTrafficYello="",strTrafficGreen="";
    boolean isTrafficRed,isTrafficYello,isTrafficGreen;

    TextView textViewTitle;
    ListView listviewProfile;
    TextView textviewToday;
    ImageView imageViewRefresh;

    LinearLayout linearsortPanel;

    Button buttonOk, buttonStatusOk, buttonTrafficOk;
    ImageView imgCancel,imgStatusCancel,imgTrafficCancel;
    ArrayList<ProfileModelClass> arraylistProfile = new ArrayList<>();
    ArrayList<ProfileModelClass> arraylistProfileToSetasCurrent = new ArrayList<>();
    WebView webview;
    ProjectWeekAdapter adapterWeek;
    ProfileAdapter adapterProfile;
    private ArrayList<FilterModelClass> arraylistPopupViewforWeek = new ArrayList<>();
    String jsonTree = "";
    String taskId = "";
    View rootView;
    String strAssigment = "";
    String strResource = "";
    String strComeFrom = "";
    String strProfileName="";
    boolean isTabletSize;
    String clickedProfileId="";
    PopupWindow popupWindowWeek,popupWindowProfile;
    Dialog dialogCreateProfile;
    boolean isPopupDate = false, isPopupDefaultProfile = false;

    /* new declaration after changes in profile code */
    String[] arrayDate;
    String[] arrayStatus;
    String[] arrayTraffic;
    ArrayList<RibbonShowSelectionClass> listDate = new ArrayList<>();
    ArrayList<RibbonShowSelectionClass> listStatus = new ArrayList<>();
    ArrayList<RibbonShowSelectionClass> listTraffic = new ArrayList<>();
    ArrayList<String> arraylistRibbonFilterValue = new ArrayList<>();
    ArrayList<String> arraylistRibbonFilterSettedFromServer = new ArrayList<>();
    ArrayList<String> arraylistRibbonFilterSettedFromLocal = new ArrayList<>();
    ArrayList<String> arraylistRibbonFilterSettedMerged = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_resource, container, false);
        //MyApplication.component(getActivity()).inject(this);
        preferences = new PreferencesHelper(getActivity());
        dbHandler = new DatabaseHandler(getActivity());
        initControl();
        return rootView;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initControl() {

        arrayDate = new String[]{getString(R.string.completion_overrun_control_to_send_control),
                getString(R.string.started_project_task_control_to_send_control),
                getString(R.string.complete_project_task_control_to_send_control),
                getString(R.string.complete_today_control_to_send_control),
                getString(R.string.pending_project_task_control_to_send_control),
                getString(R.string.passive_project_task_control_to_send_control),
                getString(R.string.completed_in1week_control_to_send_control),
                getString(R.string.task_without_date_control_to_send_control),
                getString(R.string.canceled_project_task_control_to_send_control)};
        arrayStatus = new String[]{getString(R.string.notice_arrow_control_to_send_control),
                getString(R.string.reshow_control_to_send_control),
                getString(R.string.limit_overrun_control_to_send_control),
                getString(R.string.critical_control_to_send_control),
                getString(R.string.requiring_clarification_control_to_send_control)};
        arrayTraffic = new String[]{getString(R.string.traffic_yellow_control_to_send_control),
                getString(R.string.traffic_red_control_to_send_control),
                getString(R.string.traffic_green_control_to_send_control)};

        for (int i=0;i<arrayDate.length;i++){
            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
            selection.setValue(arrayDate[i]);
            selection.setSelected(false);
            listDate.add(selection);
        }
        for (int i=0;i<arrayStatus.length;i++){
            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
            selection.setValue(arrayStatus[i]);
            selection.setSelected(false);
            listStatus.add(selection);
        }
        for (int i=0;i<arrayTraffic.length;i++){
            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
            selection.setValue(arrayTraffic[i]);
            selection.setSelected(false);
            listTraffic.add(selection);
        }

        GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);
        GlobalClass.fontBold = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPathBold);
        GlobalClass.fontLight = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPathLight);
        GlobalClass.fontMedium = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPathMedium);

        relativeMain = (RelativeLayout)rootView.findViewById(R.id.relative_resource_main);
        GlobalClass.setupTypeface(relativeMain, GlobalClass.fontRegular);
        textViewTitle=(TextView)rootView.findViewById(R.id.txt_project);
        textViewTitle.setTypeface(GlobalClass.fontBold);
        textviewDateDialog = (TextView) rootView.findViewById(R.id.txt_date);
        textviewStatusDialog = (TextView) rootView.findViewById(R.id.txt_status);
        textviewTrafficLightDialog = (TextView) rootView.findViewById(R.id.txt_traffic_lights);
        textviewWeekDialog = (TextView) rootView.findViewById(R.id.txt_week_m);
        textviewToday = (TextView)rootView.findViewById(R.id.textviewToday);
        imageViewRefresh = (ImageView) rootView.findViewById(R.id.imageview_refresh);

        linearsortPanel = (LinearLayout)rootView.findViewById(R.id.project_heder);

        GlobalClass.setupTypeface(linearsortPanel, GlobalClass.fontLight);
        textviewToday.setTypeface(GlobalClass.fontLight);

        textviewToday.setOnClickListener(this);
        imageViewRefresh.setOnClickListener(this);
        textviewDateDialog.setOnClickListener(this);
        textviewStatusDialog.setOnClickListener(this);
        textviewTrafficLightDialog.setOnClickListener(this);
        textviewWeekDialog.setOnClickListener(this);
        webview = (WebView) rootView.findViewById(webView);

        /*setTextviewBold(textviewDateDialog,getString(R.string.DateLightFilter));
        setTextviewBold(textviewStatusDialog,getString(R.string.StatusLightFilter));
        setTextviewBold(textviewTrafficLightDialog,getString(R.string.TrafficLightFilter));*/

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            taskId = bundle.getString("task_id");
            jsonTree = bundle.getString("json_tree");
            strAssigment = bundle.getString("jsonarray_asignment");
            strResource = bundle.getString("jsonarray_resources");
            strComeFrom = bundle.getString("comefrom");
        }

        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webview.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // gettin all profile from database stored
        getAllProfilefromDatabase();
        setLanguage(preferences.getLanguage());
        isTabletSize = getResources().getBoolean(R.bool.isTablet);
        if (isTabletSize) {
            textviewWeekDialog.setTypeface(GlobalClass.fontLight);

        } else {
            textviewWeekDialog.setTypeface(GlobalClass.fontMedium);
        }
        webviewUrl = GlobalClass.CONTROL_PATH+ profileIdToSendControl +"&token="+
                GlobalClass.strAccessToken+GlobalClass.getEpochTime()+"&HOST_URL="+preferences.getDomain()
                +"&orgId="+preferences.getTokenforControl()+GlobalClass.getEpochTime()+
                "&isWebViewer=0&Control=2&Lang="+preferences.getLanguage();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_refresh:
                preferences.saveIsProgressShow("yes");
                refreshControl();
                break;
            case R.id.textviewToday:
                fireToday();
                break;
            case R.id.txt_date:
                showFilteredDialog(1,R.style.DialogAnimation);
                break;
            case R.id.txt_status:
                showFilteredDialog(2,R.style.DialogAnimation);
                break;
            case R.id.txt_traffic_lights:
                showFilteredDialog(3,R.style.DialogAnimation);
                break;
            case R.id.txt_week_m:
                if(popupWindowWeek != null){
                    if (popupWindowWeek.isShowing()) {
                        if (popupWindowWeek != null) {
                            popupWindowWeek.dismiss();
                            isPopupDate = false;
                        }
                    } else {
                /* this function will store data in arraylist to show in popup basen on condition */
                        addingDataInPopup(1);
                /* this will show popup */
                        showPopupWindow(1);
                        isPopupDate = true;
                    }
                }
                else {
                /* this function will store data in arraylist to show in popup basen on condition */
                    addingDataInPopup(1);
                /* this will show popup */
                    showPopupWindow(1);
                    isPopupDate = true;
                }
                break;
        }
    }
    //    this method shows filter dilaog based on condition
    public void showFilteredDialog(int pos, int animationSource)
    {
        /* 1 is for showing date filter dialog*/
        if(pos == 1){
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.custom_dialog_date);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            LinearLayout linearMain = (LinearLayout)dialog.findViewById(R.id.linear_date_dialog_main);
            GlobalClass.setupTypeface(linearMain, GlobalClass.fontRegular);

            txtdatedheder=(TextView) dialog.findViewById(R.id.txt_dialog_date);
            txtcomplition=(TextView) dialog.findViewById(R.id.txt_completion_overrun);
            txtstartproject=(TextView) dialog.findViewById(R.id.txt_started_project);
            txtcompltetoday=(TextView) dialog.findViewById(R.id.txt_complete_today);
            txtcompleteproject=(TextView) dialog.findViewById(R.id.txt_complete_project);
            txtpendingproject=(TextView) dialog.findViewById(R.id.txt_pending_project);
            txtpassiveproject=(TextView) dialog.findViewById(R.id.txt_passive_project);
            txtcompltein1week=(TextView) dialog.findViewById(R.id.txt_complete_in_week);
            txttaskwithoutdate=(TextView) dialog.findViewById(R.id.txt_task_without_date);
            txtcanceledprojecttask=(TextView) dialog.findViewById(R.id.txt_cancel_task);
            imgCancel = (ImageView) dialog.findViewById(R.id.imgclose);
            buttonOk = (Button) dialog.findViewById(R.id.btn_date);
            buttonOk.setOnTouchListener(new CustomTouchListenerDate());
            setLanguageDateLanguage(preferences.getLanguage());
            dialog.getWindow().getAttributes().windowAnimations = animationSource;

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
            dialog.show();
            initializeDateDialogControl(dialog);

            if(arraylistRibbonFilterSettedMerged.size() > 0){
                // String[] split = arraylistRibbonFilterSettedMerged.get(0).split(",");
                //if(split.length > 0){
                showDateFilterSettedFromService(arraylistRibbonFilterSettedMerged);
                //}
            }
            //getDateValuefromDataaseToShow();
            imgCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v== imgCancel) {
                        // Close dialog
                        if(dialog != null) {
                            dialog.dismiss();
                        }
                        //setTextviewBold(textviewDateDialog,getString(R.string.DateLightFilter));
                        setTextViewStyleForDateTextView(arraylistRibbonFilterSettedMerged);
                    }
                }
            });
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v== buttonOk) {
                        setTextViewStyleForDateTextView(arraylistRibbonFilterSettedMerged);
                        boolean shouldFireFilter=false;
                        StringBuilder commaSeparatedValue= new StringBuilder();
                        for (int i=0;i<listDate.size();i++){
                            if(listDate.get(i).isSelected()){

                                commaSeparatedValue.append(listDate.get(i).getValue());
                                if (i != listDate.size() - 1) {
                                    commaSeparatedValue.append(",");
                                }

                                if(arraylistRibbonFilterSettedFromServer.contains(listDate.get(i).getValue())){
                                }
                                else {
                                    if(arraylistRibbonFilterSettedFromLocal.size() > 0) {
                                        if (!arraylistRibbonFilterSettedFromLocal.contains(listDate.get(i).getValue())) {
                                            arraylistRibbonFilterSettedFromLocal.add(listDate.get(i).getValue());
                                        }

                                    }
                                    else {
                                        arraylistRibbonFilterSettedFromLocal.add(listDate.get(i).getValue());
                                    }
                                }

                            }else {
                                if(arraylistRibbonFilterSettedFromServer.contains(listDate.get(i).getValue())){
                                    arraylistRibbonFilterSettedFromServer.remove(listDate.get(i).getValue());
                                }
                                else {
                                    if(arraylistRibbonFilterSettedFromLocal.size() > 0) {
                                        if (arraylistRibbonFilterSettedFromLocal.contains(listDate.get(i).getValue())) {
                                            arraylistRibbonFilterSettedFromLocal.remove(listDate.get(i).getValue());
                                        }

                                    }
                                }
                            }
                        }
                        List<String> list = new ArrayList<String>();
                        List<String> listServer = new ArrayList<String>();
                        String valueComma="";
                        String valueCommaSever="";
                        if(arraylistRibbonFilterSettedFromLocal.size() > 0){
                            valueComma= TextUtils.join(",",arraylistRibbonFilterSettedFromLocal);
                            list = new ArrayList<String>(Arrays.asList(valueComma.split(" , ")));
                        }
                        if(arraylistRibbonFilterSettedFromServer.size() > 0){
                            valueCommaSever= TextUtils.join(",",arraylistRibbonFilterSettedFromServer);
                            listServer = new ArrayList<String>(Arrays.asList(valueCommaSever.split(" , ")));
                        }
                        String strRibbonFilterLocal="";
                        String strRibbonFilterServer="";
                        if(list.size() > 0 && !valueComma.equalsIgnoreCase("")){
                            //Gson gson = new Gson();
                            strRibbonFilterLocal = TextUtils.join(",",list);
                        }
                        if(listServer.size() > 0 && !valueCommaSever.equalsIgnoreCase("")){
                            //Gson gson = new Gson();
                            strRibbonFilterServer = TextUtils.join(",",listServer);
                        }
                        if(!shouldFireFilter && preferences.getLastDateFilterString().equalsIgnoreCase("")){
                            textviewDateDialog.setTypeface(GlobalClass.fontLight);
                            if(dialog != null) {
                                dialog.dismiss();
                            }
                            if(preferences.getLastDateFilterString().equalsIgnoreCase(commaSeparatedValue.toString())){
                            }else {
                                preferences.saveCurrentFiredFilter("7");
                                /*if (preferences.getCurrentFiredFilter().equalsIgnoreCase("7")) {
                                } else {
                                    preferences.saveCurrentFiredFilter("6");
                                }*/
                            }
                            preferences.saveIsProgressShow("yes");
                            updateProfileInPreferences(strRibbonFilterLocal);
                            updateProfileInPreferencesforServer(strRibbonFilterServer);
                            //dbHandler.updateProfileAttribute(mAttributeValue,preferences.getProfileId());
                            dbHandler.updateRibbonFilterFromLocalSide(preferences.getProfileId(),
                                    preferences.getUserID(),preferences.getLanguage(),strRibbonFilterLocal);
                            dbHandler.updateRibbonFilterFromServerSide(preferences.getProfileId(),
                                    preferences.getUserID(),preferences.getLanguage(),strRibbonFilterServer);
                            //}

                            //dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.DateLightFilter));
                            //setTextviewBold(textviewDateDialog,getString(R.string.DateLightFilter));
                            preferences.saveLastDateFilterString(commaSeparatedValue.toString());
                            fireGeneralAndRibbonFilter();
                        }
                        else if(commaSeparatedValue.toString().equalsIgnoreCase(preferences.getLastDateFilterString())){
                            //dbHandler.updateProfileAttribute(strRibbonFiltertoSendserver,preferences.getProfileId());
                            if(dialog != null) {
                                dialog.dismiss();
                            }
                            if(shouldFireFilter){
                                textviewDateDialog.setTypeface(GlobalClass.fontBold);
                            }
                            else {
                                textviewDateDialog.setTypeface(GlobalClass.fontBold);
                            }
                            preferences.saveLastDateFilterString(commaSeparatedValue.toString());
                        }
                        else {
                            preferences.saveIsProgressShow("yes");

                            dbHandler.updateRibbonFilterFromLocalSide(preferences.getProfileId(),
                                    preferences.getUserID(),preferences.getLanguage(),strRibbonFilterLocal);
                            dbHandler.updateRibbonFilterFromServerSide(preferences.getProfileId(),
                                    preferences.getUserID(),preferences.getLanguage(),strRibbonFilterServer);
                            updateProfileInPreferences(strRibbonFilterLocal);
                            updateProfileInPreferencesforServer(strRibbonFilterServer);
                            //dbHandler.updateProfileAttribute(mAttributeValue,preferences.getProfileId());
                            //dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.DateLightFilter));
                            //dbHandler.addGeneralFilterInfo(getString(R.string.DateLightFilter),finalCommaseperated,"","",
                            //preferences.getUserID(),preferences.getProfileId());
                            if(dialog != null) {
                                dialog.dismiss();
                            }

                            //setTextviewBold(textviewDateDialog,getString(R.string.DateLightFilter));
                            if(preferences.getCurrentFiredFilter().equalsIgnoreCase("7") ||
                                    preferences.getCurrentFiredFilter().equalsIgnoreCase("8")){
                                preferences.saveCurrentFiredFilter("7");
                            }
                            else {
                                preferences.saveCurrentFiredFilter("6");
                            }
                            preferences.saveLastDateFilterString(commaSeparatedValue.toString());
                            fireGeneralAndRibbonFilter();
                        }
                    }
                }
            });
        }
        /* this is for showing status filter dialog*/
        else  if(pos == 2){
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.custom_dialog_status);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            LinearLayout linearMain = (LinearLayout)dialog.findViewById(R.id.linear_status_dialog_main);
            GlobalClass.setupTypeface(linearMain, GlobalClass.fontRegular);

            txtstatusheder=(TextView)dialog.findViewById(R.id.txt_status_heading);
            txtnoticearrow=(TextView)dialog.findViewById(R.id.txt_notice_arrow);
            txtreshow=(TextView)dialog.findViewById(R.id.txt_reshow);
            txtlimitoverun=(TextView)dialog.findViewById(R.id.txt_limit_overrun);
            txtcritical=(TextView)dialog.findViewById(R.id.txt_critical);
            txtrequring=(TextView)dialog.findViewById(R.id.txt_requring);

            imgStatusCancel = (ImageView) dialog.findViewById(R.id.imgclose);
            buttonStatusOk = (Button) dialog.findViewById(R.id.btn_status);
            buttonStatusOk.setOnTouchListener(new CustomTouchListenerStatus());
            setLanguageStatusDialog(preferences.getLanguage());

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);

            dialog.getWindow().getAttributes().windowAnimations = animationSource;
            dialog.show();

            initializeStatusDialogControl(dialog);
            if(arraylistRibbonFilterSettedMerged.size() > 0){
                //String[] split = arraylistRibbonFilterSettedMerged.get(0).split(",");
                //if(split.length > 0){
                showStatusFilterSettedFromService(arraylistRibbonFilterSettedMerged);
                //}
            }
            //getStatusValuefromDataaseToShow();
            imgStatusCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (v == imgStatusCancel) {
                        // Close dialog
                        if(dialog != null) {
                            dialog.dismiss();
                        }
                        setTextViewStyleForStatusTextView(arraylistRibbonFilterSettedMerged);
                        //setTextviewBold(textviewStatusDialog,getString(R.string.StatusLightFilter));
                    }

                }
            });
            buttonStatusOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view == buttonStatusOk) {
                        setTextViewStyleForStatusTextView(arraylistRibbonFilterSettedMerged);
                        boolean shouldFireFilter=false;
                        StringBuilder commaSeparatedValue= new StringBuilder();
                        for (int i=0;i<listStatus.size();i++){
                            if(listStatus.get(i).isSelected()){

                                commaSeparatedValue.append(listStatus.get(i).getValue());
                                if (i != listStatus.size() - 1) {
                                    commaSeparatedValue.append(",");
                                }

                                if(arraylistRibbonFilterSettedFromServer.contains(listStatus.get(i).getValue())){
                                }
                                else {
                                    if(arraylistRibbonFilterSettedFromLocal.size() > 0) {
                                        if (!arraylistRibbonFilterSettedFromLocal.contains(listStatus.get(i).getValue())) {
                                            arraylistRibbonFilterSettedFromLocal.add(listStatus.get(i).getValue());
                                        }

                                    }
                                    else {
                                        arraylistRibbonFilterSettedFromLocal.add(listStatus.get(i).getValue());
                                    }
                                }

                            }else {
                                if(arraylistRibbonFilterSettedFromServer.contains(listStatus.get(i).getValue())){
                                    arraylistRibbonFilterSettedFromServer.remove(listStatus.get(i).getValue());
                                }
                                else {
                                    if(arraylistRibbonFilterSettedFromLocal.size() > 0) {
                                        if (arraylistRibbonFilterSettedFromLocal.contains(listStatus.get(i).getValue())) {
                                            arraylistRibbonFilterSettedFromLocal.remove(listStatus.get(i).getValue());
                                        }

                                    }
                                }
                            }
                        }

                        List<String> list = new ArrayList<String>();
                        List<String> listServer = new ArrayList<String>();
                        String valueComma="";
                        String valueCommaSever="";
                        if(arraylistRibbonFilterSettedFromLocal.size() > 0){
                            valueComma= TextUtils.join(",",arraylistRibbonFilterSettedFromLocal);
                            list = new ArrayList<String>(Arrays.asList(valueComma.split(" , ")));
                        }
                        if(arraylistRibbonFilterSettedFromServer.size() > 0){
                            valueCommaSever= TextUtils.join(",",arraylistRibbonFilterSettedFromServer);
                            listServer = new ArrayList<String>(Arrays.asList(valueCommaSever.split(" , ")));
                        }
                        String strRibbonFilterLocal="";
                        String strRibbonFilterServer="";
                        if(list.size() > 0 && !valueComma.equalsIgnoreCase("")){
                            //Gson gson = new Gson();
                            strRibbonFilterLocal = TextUtils.join(",",list);
                        }
                        if(listServer.size() > 0 && !valueCommaSever.equalsIgnoreCase("")){
                            //Gson gson = new Gson();
                            strRibbonFilterServer = TextUtils.join(",",listServer);
                        }

                        if(!shouldFireFilter && preferences.getLastStatusilterString().equalsIgnoreCase("")){
                            textviewStatusDialog.setTypeface(GlobalClass.fontLight);
                            if(dialog != null) {
                                dialog.dismiss();
                            }
                            if(preferences.getLastStatusilterString().equalsIgnoreCase(commaSeparatedValue.toString())){
                            }else {
                                preferences.saveCurrentFiredFilter("7");
                                /*if (preferences.getCurrentFiredFilter().equalsIgnoreCase("7")) {
                                } else {
                                    preferences.saveCurrentFiredFilter("6");
                                }*/
                            }
                            preferences.saveIsProgressShow("yes");

                            updateProfileInPreferences(strRibbonFilterLocal);
                            updateProfileInPreferencesforServer(strRibbonFilterServer);
                            dbHandler.updateRibbonFilterFromLocalSide(preferences.getProfileId(),
                                    preferences.getUserID(),preferences.getLanguage(),strRibbonFilterLocal);
                            dbHandler.updateRibbonFilterFromServerSide(preferences.getProfileId(),
                                    preferences.getUserID(),preferences.getLanguage(),strRibbonFilterServer);

                            //dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.StatusLightFilter));
                            //setTextviewBold(textviewStatusDialog,getString(R.string.StatusLightFilter));

                            preferences.saveLastStatusFilterString(commaSeparatedValue.toString());
                            fireGeneralAndRibbonFilter();
                        }
                        else if(commaSeparatedValue.toString().equalsIgnoreCase(preferences.getLastStatusilterString())){
                            if(dialog != null) {
                                dialog.dismiss();
                            }
                            if(shouldFireFilter){
                                textviewStatusDialog.setTypeface(GlobalClass.fontBold);
                            }
                            else {
                                textviewStatusDialog.setTypeface(GlobalClass.fontBold);
                            }
                            preferences.saveLastStatusFilterString(commaSeparatedValue.toString());
                        }
                        else {
                            preferences.saveIsProgressShow("yes");
                            updateProfileInPreferences(strRibbonFilterLocal);
                            updateProfileInPreferencesforServer(strRibbonFilterServer);
                            dbHandler.updateRibbonFilterFromLocalSide(preferences.getProfileId(),
                                    preferences.getUserID(),preferences.getLanguage(),strRibbonFilterLocal);
                            dbHandler.updateRibbonFilterFromServerSide(preferences.getProfileId(),
                                    preferences.getUserID(),preferences.getLanguage(),strRibbonFilterServer);
                            //dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.StatusLightFilter));
                            //dbHandler.addGeneralFilterInfo(getString(R.string.StatusLightFilter),finalCommaseperated,"","",
                            //preferences.getUserID(),preferences.getProfileId());
                            if(dialog != null) {
                                dialog.dismiss();
                            }
                            //setTextviewBold(textviewStatusDialog,getString(R.string.StatusLightFilter));

                            if(preferences.getCurrentFiredFilter().equalsIgnoreCase("7") ||
                                    preferences.getCurrentFiredFilter().equalsIgnoreCase("8")){
                                preferences.saveCurrentFiredFilter("7");
                            }
                            else {
                                preferences.saveCurrentFiredFilter("6");
                            }
                            preferences.saveLastStatusFilterString(commaSeparatedValue.toString());
                            fireGeneralAndRibbonFilter();
                        }
                    }
                }
            });
        }
        /* this is for showing traffic light filter dialog*/
        else if(pos == 3)
        {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.custom_dialog_trafficlights);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            LinearLayout linearMain = (LinearLayout)dialog.findViewById(R.id.linear_traffic_dialog_main);
            GlobalClass.setupTypeface(linearMain, GlobalClass.fontRegular);

            txttraffichheder=(TextView)dialog.findViewById(R.id.txttraffic_light_heading);
            txtred=(TextView)dialog.findViewById(R.id.txt_red);
            txtyellow=(TextView)dialog.findViewById(R.id.txt_yellow);
            txtgreen=(TextView)dialog.findViewById(R.id.txt_green);

            imgTrafficCancel = (ImageView) dialog.findViewById(R.id.imgclose);
            buttonTrafficOk = (Button) dialog.findViewById(R.id.btn_traffic);
            buttonTrafficOk.setOnTouchListener(new CustomTouchListenerTraffic());

            setLanguageTrafficDialog(preferences.getLanguage());

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);

            dialog.getWindow().getAttributes().windowAnimations = animationSource;
            dialog.show();

            initializeTrafficDialogControl(dialog);
            if(arraylistRibbonFilterSettedMerged.size() > 0){
                //String[] split = arraylistRibbonFilterSettedMerged.get(0).split(",");
                // if(split.length > 0){
                showTrafficFilterSettedFromService(arraylistRibbonFilterSettedMerged);
                //}
            }
            //getTrafficValuefromDataaseToShow();
            imgTrafficCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v == imgTrafficCancel) {
                        // Close dialog
                        if(dialog != null) {
                            dialog.dismiss();
                        }
                        setTextViewStyleForTrafficTextView(arraylistRibbonFilterSettedMerged);
                        //setTextviewBold(textviewTrafficLightDialog,getString(R.string.TrafficLightFilter));
                    }
                }
            });
            buttonTrafficOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    setTextViewStyleForTrafficTextView(arraylistRibbonFilterSettedMerged);
                    boolean shouldFireFilter=false;
                    StringBuilder commaSeparatedValue= new StringBuilder();
                    for (int i=0;i<listTraffic.size();i++){
                        if(listTraffic.get(i).isSelected()){

                            commaSeparatedValue.append(listTraffic.get(i).getValue());
                            if (i != listTraffic.size() - 1) {
                                commaSeparatedValue.append(",");
                            }

                            if(arraylistRibbonFilterSettedFromServer.contains(listTraffic.get(i).getValue())){
                            }
                            else {
                                if(arraylistRibbonFilterSettedFromLocal.size() > 0) {
                                    if (!arraylistRibbonFilterSettedFromLocal.contains(listTraffic.get(i).getValue())) {
                                        arraylistRibbonFilterSettedFromLocal.add(listTraffic.get(i).getValue());
                                    }

                                }
                                else {
                                    arraylistRibbonFilterSettedFromLocal.add(listTraffic.get(i).getValue());
                                }
                            }

                        }else {
                            if(arraylistRibbonFilterSettedFromServer.contains(listTraffic.get(i).getValue())){
                                arraylistRibbonFilterSettedFromServer.remove(listTraffic.get(i).getValue());
                            }
                            else {
                                if(arraylistRibbonFilterSettedFromLocal.size() > 0) {
                                    if (arraylistRibbonFilterSettedFromLocal.contains(listTraffic.get(i).getValue())) {
                                        arraylistRibbonFilterSettedFromLocal.remove(listTraffic.get(i).getValue());
                                    }

                                }
                            }
                        }
                    }
                    List<String> list = new ArrayList<String>();
                    List<String> listServer = new ArrayList<String>();
                    String valueComma="";
                    String valueCommaSever="";
                    if(arraylistRibbonFilterSettedFromLocal.size() > 0){
                        valueComma= TextUtils.join(",",arraylistRibbonFilterSettedFromLocal);
                        list = new ArrayList<String>(Arrays.asList(valueComma.split(" , ")));
                    }
                    if(arraylistRibbonFilterSettedFromServer.size() > 0){
                        valueCommaSever= TextUtils.join(",",arraylistRibbonFilterSettedFromServer);
                        listServer = new ArrayList<String>(Arrays.asList(valueCommaSever.split(" , ")));
                    }
                    String strRibbonFilterLocal="";
                    String strRibbonFilterServer="";
                    if(list.size() > 0 && !valueComma.equalsIgnoreCase("")){
                        //Gson gson = new Gson();
                        strRibbonFilterLocal = TextUtils.join(",",list);
                    }
                    if(listServer.size() > 0 && !valueCommaSever.equalsIgnoreCase("")){
                        //Gson gson = new Gson();
                        strRibbonFilterServer = TextUtils.join(",",listServer);
                    }
                    //String finalCommaseperated = strTrafficRed+","+strTrafficGreen+","+strTrafficYello;
                    if(!shouldFireFilter && preferences.getLastTrafficFilterString().equalsIgnoreCase("")){
                        textviewTrafficLightDialog.setTypeface(GlobalClass.fontLight);
                        if(dialog != null) {
                            dialog.dismiss();
                        }
                        if(preferences.getLastTrafficFilterString().equalsIgnoreCase(commaSeparatedValue.toString())){
                        }else {
                            preferences.saveCurrentFiredFilter("7");
                           /* if (preferences.getCurrentFiredFilter().equalsIgnoreCase("7")) {
                            } else {
                                preferences.saveCurrentFiredFilter("6");
                            }*/
                        }

                        preferences.saveIsProgressShow("yes");

                        updateProfileInPreferences(strRibbonFilterLocal);
                        updateProfileInPreferencesforServer(strRibbonFilterServer);
                        dbHandler.updateRibbonFilterFromLocalSide(preferences.getProfileId(),
                                preferences.getUserID(),preferences.getLanguage(),strRibbonFilterLocal);
                        dbHandler.updateRibbonFilterFromServerSide(preferences.getProfileId(),
                                preferences.getUserID(),preferences.getLanguage(),strRibbonFilterServer);

                        //dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.TrafficLightFilter));
                        //setTextviewBold(textviewTrafficLightDialog,getString(R.string.TrafficLightFilter));
                        preferences.saveLastTrafficFilterString(commaSeparatedValue.toString());
                        fireGeneralAndRibbonFilter();
                    }
                    else if(commaSeparatedValue.toString().equalsIgnoreCase(preferences.getLastTrafficFilterString())){
                        if(dialog != null) {
                            dialog.dismiss();
                        }
                        if(shouldFireFilter){
                            textviewTrafficLightDialog.setTypeface(GlobalClass.fontBold);
                        }
                        else {
                            textviewTrafficLightDialog.setTypeface(GlobalClass.fontBold);
                        }
                        preferences.saveLastTrafficFilterString(commaSeparatedValue.toString());
                    }
                    else {
                        updateProfileInPreferences(strRibbonFilterLocal);
                        updateProfileInPreferencesforServer(strRibbonFilterServer);
                        dbHandler.updateRibbonFilterFromLocalSide(preferences.getProfileId(),
                                preferences.getUserID(),preferences.getLanguage(),strRibbonFilterLocal);
                        dbHandler.updateRibbonFilterFromServerSide(preferences.getProfileId(),
                                preferences.getUserID(),preferences.getLanguage(),strRibbonFilterServer);

                        preferences.saveIsProgressShow("yes");
                        //dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.TrafficLightFilter));
                        //dbHandler.addGeneralFilterInfo(getString(R.string.TrafficLightFilter),finalCommaseperated,"","",
                        //preferences.getUserID(),preferences.getProfileId());
                        if(dialog != null) {
                            dialog.dismiss();
                        }
                        //setTextviewBold(textviewTrafficLightDialog,getString(R.string.TrafficLightFilter));
                        if(preferences.getCurrentFiredFilter().equalsIgnoreCase("7") ||
                                preferences.getCurrentFiredFilter().equalsIgnoreCase("8")){
                            preferences.saveCurrentFiredFilter("7");
                        }
                        else {
                            preferences.saveCurrentFiredFilter("6");
                        }
                        preferences.saveLastTrafficFilterString(commaSeparatedValue.toString());
                        fireGeneralAndRibbonFilter();
                    }
                }
            });
        }
    }
    public void updateProfileInPreferences(String valueToChange){
        if(arraylistProfileToSetasCurrent.size() > 0){
            ProfileModelClass model = new ProfileModelClass();
            model.setProfileId(arraylistProfileToSetasCurrent.get(0).getProfileId());
            model.setProfileName(arraylistProfileToSetasCurrent.get(0).getProfileName());
            model.setColDeTail(arraylistProfileToSetasCurrent.get(0).getColDeTail());
            model.setProfileUserId(arraylistProfileToSetasCurrent.get(0).getProfileUserId());
            model.setResolution(arraylistProfileToSetasCurrent.get(0).getResolution());
            model.setLanguage(preferences.getLanguage());
            model.setKey(arraylistProfileToSetasCurrent.get(0).getKey());
            model.setKeyLocal(arraylistProfileToSetasCurrent.get(0).getKeyLocal());
            model.setValue(arraylistProfileToSetasCurrent.get(0).getValue());
            model.setValueLocal(valueToChange);

            String json = new Gson().toJson(model);
            preferences.saveUserProfile(json);
            arraylistProfileToSetasCurrent = new ArrayList<>();
            arraylistProfileToSetasCurrent.add(model);
            preferences.saveProfileId(model.getProfileId());
        }
    }
    public void updateProfileInPreferencesforServer(String valueToChange){
        if(arraylistProfileToSetasCurrent.size() > 0){
            ProfileModelClass model = new ProfileModelClass();
            model.setProfileId(arraylistProfileToSetasCurrent.get(0).getProfileId());
            model.setProfileName(arraylistProfileToSetasCurrent.get(0).getProfileName());
            model.setColDeTail(arraylistProfileToSetasCurrent.get(0).getColDeTail());
            model.setProfileUserId(arraylistProfileToSetasCurrent.get(0).getProfileUserId());
            model.setResolution(arraylistProfileToSetasCurrent.get(0).getResolution());
            model.setLanguage(preferences.getLanguage());
            model.setKey(arraylistProfileToSetasCurrent.get(0).getKey());
            model.setKeyLocal(arraylistProfileToSetasCurrent.get(0).getKeyLocal());
            model.setValue(valueToChange);
            model.setValueLocal(arraylistProfileToSetasCurrent.get(0).getValueLocal());

            String json = new Gson().toJson(model);
            preferences.saveUserProfile(json);
            arraylistProfileToSetasCurrent = new ArrayList<>();
            arraylistProfileToSetasCurrent.add(model);
            preferences.saveProfileId(model.getProfileId());
        }
    }
    //initializing date dilaog control
    public void initializeDateDialogControl(Dialog dialog){
        imgCompilationOverrun =(ImageView)dialog.findViewById(R.id.img_completion_overrun);
        imgStartedProject=(ImageView)dialog.findViewById(R.id.img_started_project);
        imgCompeletedProject=(ImageView)dialog.findViewById(R.id.img_complete_project);
        imgCompleteToday=(ImageView)dialog.findViewById(R.id.img_complete_today);
        imgPendingProject=(ImageView)dialog.findViewById(R.id.img_pending_project);
        imgPassiveProject=(ImageView)dialog.findViewById(R.id.img_passive_project);
        imgCompletedWeek=(ImageView)dialog.findViewById(R.id.img_complete_in_week);
        imgTaskWithoutDate=(ImageView)dialog.findViewById(R.id.img_task_without_date);
        imgCanceledProject=(ImageView)dialog.findViewById(R.id.img_cancel_task);

        linearCompilationOverrun =(LinearLayout) dialog.findViewById(R.id.linear_competion_overrun);
        linearStartedProject =(LinearLayout) dialog.findViewById(R.id.linear_started_project);
        linearCompeletedProject =(LinearLayout) dialog.findViewById(R.id.linear_completed_project);
        linearCompleteToday =(LinearLayout) dialog.findViewById(R.id.linear_completed_today);
        linearPendingProject =(LinearLayout) dialog.findViewById(R.id.linear_pending_project);
        linearPassiveProject =(LinearLayout) dialog.findViewById(R.id.linear_passive_project);
        linearCompletedWeek =(LinearLayout) dialog.findViewById(R.id.linear_completed_week);
        linearTaskWithoutDate =(LinearLayout) dialog.findViewById(R.id.linear_task_without_date);
        linearCanceledProject =(LinearLayout) dialog.findViewById(R.id.linear_canceled_project);

        linearCompilationOverrun.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isComplitionOverrun){
                    imgCompilationOverrun.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isComplitionOverrun=true;
                    strCompilationOverrun=getResources().getString(R.string.completion_overrun);
                    for (int k=0;k<listDate.size();k++){
                        if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.completion_overrun_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.completion_overrun_control_to_send_control));
                            selection.setSelected(true);
                            listDate.set(k,selection);
                        }
                    }
                }
                else {
                    imgCompilationOverrun.setBackgroundColor(Color.TRANSPARENT);
                    isComplitionOverrun=false;
                    strCompilationOverrun="";
                    for (int k=0;k<listDate.size();k++){
                        if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.completion_overrun_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.completion_overrun_control_to_send_control));
                            selection.setSelected(false);
                            listDate.set(k,selection);
                        }
                    }
                }

            }
        });
        linearStartedProject.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isStartedProject){
                    imgStartedProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isStartedProject=true;
                    strStartedProject=getResources().getString(R.string.started_project_task);
                    for (int k=0;k<listDate.size();k++){
                        if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.started_project_task_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.started_project_task_control_to_send_control));
                            selection.setSelected(true);
                            listDate.set(k,selection);
                        }
                    }
                }
                else {
                    imgStartedProject.setBackgroundColor(Color.TRANSPARENT);
                    isStartedProject=false;
                    strStartedProject="";
                    for (int k=0;k<listDate.size();k++){
                        if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.started_project_task_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.started_project_task_control_to_send_control));
                            selection.setSelected(false);
                            listDate.set(k,selection);
                        }
                    }
                }
            }
        });
        linearCompeletedProject.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isCompletedProject){
                    imgCompeletedProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isCompletedProject=true;
                    strCompletedProject=getResources().getString(R.string.complete_project_task);
                    for (int k=0;k<listDate.size();k++){
                        if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.complete_project_task_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.complete_project_task_control_to_send_control));
                            selection.setSelected(true);
                            listDate.set(k,selection);
                        }
                    }
                }
                else {
                    imgCompeletedProject.setBackgroundColor(Color.TRANSPARENT);
                    isCompletedProject=false;
                    strCompletedProject="";
                    for (int k=0;k<listDate.size();k++){
                        if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.complete_project_task_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.complete_project_task_control_to_send_control));
                            selection.setSelected(false);
                            listDate.set(k,selection);
                        }
                    }
                }
            }
        });
        linearCompleteToday.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isCompletedToday){
                    imgCompleteToday.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isCompletedToday=true;
                    strCompletedToday=getResources().getString(R.string.complete_today);
                    for (int k=0;k<listDate.size();k++){
                        if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.complete_today_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.complete_today_control_to_send_control));
                            selection.setSelected(true);
                            listDate.set(k,selection);
                        }
                    }
                }
                else {
                    imgCompleteToday.setBackgroundColor(Color.TRANSPARENT);
                    isCompletedToday=false;
                    strCompletedToday="";
                    for (int k=0;k<listDate.size();k++){
                        if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.complete_today_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.complete_today_control_to_send_control));
                            selection.setSelected(false);
                            listDate.set(k,selection);
                        }
                    }

                }
            }
        });
        linearPendingProject.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isPendingProject){
                    imgPendingProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isPendingProject=true;
                    strPendingProject=getResources().getString(R.string.pending_project_task);
                    for (int k=0;k<listDate.size();k++){
                        if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.pending_project_task_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.pending_project_task_control_to_send_control));
                            selection.setSelected(true);
                            listDate.set(k,selection);
                        }
                    }
                }
                else {
                    imgPendingProject.setBackgroundColor(Color.TRANSPARENT);
                    strPendingProject="";
                    isPendingProject=false;
                    for (int k=0;k<listDate.size();k++){
                        if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.pending_project_task_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.pending_project_task_control_to_send_control));
                            selection.setSelected(false);
                            listDate.set(k,selection);
                        }
                    }
                }
            }
        });
        linearPassiveProject.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isPassiveProject){
                    imgPassiveProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isPassiveProject=true;
                    strPassiveProject=getResources().getString(R.string.passive_project_task);
                    for (int k=0;k<listDate.size();k++){
                        if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.passive_project_task_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.passive_project_task_control_to_send_control));
                            selection.setSelected(true);
                            listDate.set(k,selection);
                        }
                    }
                }
                else {
                    imgPassiveProject.setBackgroundColor(Color.TRANSPARENT);
                    isPassiveProject=false;
                    strPassiveProject="";
                    for (int k=0;k<listDate.size();k++){
                        if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.passive_project_task_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.passive_project_task_control_to_send_control));
                            selection.setSelected(false);
                            listDate.set(k,selection);
                        }
                    }
                }
            }
        });
        linearCompletedWeek.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isCompletedWeek){
                    imgCompletedWeek.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isCompletedWeek=true;
                    strCompletedWeek=getResources().getString(R.string.completed_in1week);
                    for (int k=0;k<listDate.size();k++){
                        if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.completed_in1week_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.completed_in1week_control_to_send_control));
                            selection.setSelected(true);
                            listDate.set(k,selection);
                        }
                    }
                }
                else {
                    imgCompletedWeek.setBackgroundColor(Color.TRANSPARENT);
                    isCompletedWeek=false;
                    strCompletedWeek="";
                    for (int k=0;k<listDate.size();k++){
                        if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.completed_in1week_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.completed_in1week_control_to_send_control));
                            selection.setSelected(false);
                            listDate.set(k,selection);
                        }
                    }
                }
            }
        });
        linearTaskWithoutDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isTaskWithoutDate){
                    imgTaskWithoutDate.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isTaskWithoutDate=true;
                    strTaskWithoutDate=getResources().getString(R.string.task_without_date);
                    for (int k=0;k<listDate.size();k++){
                        if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.task_without_date_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.task_without_date_control_to_send_control));
                            selection.setSelected(true);
                            listDate.set(k,selection);
                        }
                    }
                }
                else {
                    imgTaskWithoutDate.setBackgroundColor(Color.TRANSPARENT);
                    isTaskWithoutDate=false;
                    strTaskWithoutDate="";
                    for (int k=0;k<listDate.size();k++){
                        if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.task_without_date_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.task_without_date_control_to_send_control));
                            selection.setSelected(false);
                            listDate.set(k,selection);
                        }
                    }
                }
            }
        });
        linearCanceledProject.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isCanceledProject){
                    imgCanceledProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isCanceledProject=true;
                    strCanceledProject=getResources().getString(R.string.canceled_project_task);
                    for (int k=0;k<listDate.size();k++){
                        if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.canceled_project_task_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.canceled_project_task_control_to_send_control));
                            selection.setSelected(true);
                            listDate.set(k,selection);
                        }
                    }
                }
                else {
                    imgCanceledProject.setBackgroundColor(Color.TRANSPARENT);
                    isCanceledProject=false;
                    strCanceledProject="";
                    for (int k=0;k<listDate.size();k++){
                        if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.canceled_project_task_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.canceled_project_task_control_to_send_control));
                            selection.setSelected(false);
                            listDate.set(k,selection);
                        }
                    }

                }
            }
        });
    }
    //    initialize status dialog
    public void initializeStatusDialogControl(Dialog dialog){
        imgNoticeArrow=(ImageView)dialog.findViewById(R.id.img_notice_arrow);
        imgReshow=(ImageView)dialog.findViewById(R.id.img_started_status);
        imgLimitoverrun=(ImageView)dialog.findViewById(R.id.img_complate_project_status);
        imgCritical=(ImageView)dialog.findViewById(R.id.img_critical);
        imgRequiringClarification=(ImageView)dialog.findViewById(R.id.img_require);

        linearNoticeArrow=(LinearLayout) dialog.findViewById(R.id.linear_notice_arrow);
        linearReshow=(LinearLayout) dialog.findViewById(R.id.linear_reshow);
        linearLimitoverrun=(LinearLayout) dialog.findViewById(R.id.linear_limit_overrun);
        linearCritical=(LinearLayout) dialog.findViewById(R.id.linear_critical);
        linearRequiringClarification=(LinearLayout) dialog.findViewById(R.id.linear_require_clarification);

        linearNoticeArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isNoticeArrow){
                    imgNoticeArrow.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isNoticeArrow=true;
                    strNoticeArrow=getResources().getString(R.string.notice_arrow);
                    for (int k=0;k<listStatus.size();k++){
                        if(listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.notice_arrow_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.notice_arrow_control_to_send_control));
                            selection.setSelected(true);
                            listStatus.set(k,selection);
                        }
                    }
                }
                else {
                    imgNoticeArrow.setBackgroundColor(Color.TRANSPARENT);
                    isNoticeArrow=false;
                    strNoticeArrow="";
                    for (int k=0;k<listStatus.size();k++){
                        if(listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.notice_arrow_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.notice_arrow_control_to_send_control));
                            selection.setSelected(false);
                            listStatus.set(k,selection);
                        }
                    }
                }

            }
        });
        linearReshow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isReshow){
                    imgReshow.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isReshow=true;
                    strReshow=getResources().getString(R.string.reshow);
                    for (int k=0;k<listStatus.size();k++){
                        if(listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.reshow_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.reshow_control_to_send_control));
                            selection.setSelected(true);
                            listStatus.set(k,selection);
                        }
                    }
                }
                else {
                    imgReshow.setBackgroundColor(Color.TRANSPARENT);
                    isReshow=false;
                    strReshow="";
                    for (int k=0;k<listStatus.size();k++){
                        if(listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.reshow_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.reshow_control_to_send_control));
                            selection.setSelected(false);
                            listStatus.set(k,selection);
                        }
                    }
                }
            }
        });
        linearLimitoverrun.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isLimitOverrun){
                    imgLimitoverrun.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isLimitOverrun=true;
                    strLimitoverrun=getResources().getString(R.string.limit_overrun);
                    for (int k=0;k<listStatus.size();k++){
                        if(listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.limit_overrun_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.limit_overrun_control_to_send_control));
                            selection.setSelected(true);
                            listStatus.set(k,selection);
                        }
                    }
                }
                else {
                    imgLimitoverrun.setBackgroundColor(Color.TRANSPARENT);
                    isLimitOverrun=false;
                    strLimitoverrun="";
                    for (int k=0;k<listStatus.size();k++){
                        if(listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.limit_overrun_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.limit_overrun_control_to_send_control));
                            selection.setSelected(false);
                            listStatus.set(k,selection);
                        }
                    }
                }
            }
        });
        linearCritical.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isCritical){
                    imgCritical.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isCritical=true;
                    strCritical=getResources().getString(R.string.critical);
                    for (int k=0;k<listStatus.size();k++){
                        if(listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.critical_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.critical_control_to_send_control));
                            selection.setSelected(true);
                            listStatus.set(k,selection);
                        }
                    }
                }
                else {
                    imgCritical.setBackgroundColor(Color.TRANSPARENT);
                    isCritical=false;
                    strCritical="";
                    for (int k=0;k<listStatus.size();k++){
                        if(listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.critical_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.critical_control_to_send_control));
                            selection.setSelected(false);
                            listStatus.set(k,selection);
                        }
                    }
                }
            }
        });
        linearRequiringClarification.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isRequiringClarification){
                    imgRequiringClarification.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isRequiringClarification=true;
                    strRequiringClarification=getResources().getString(R.string.requiring_clarification);
                    for (int k=0;k<listStatus.size();k++){
                        if(listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.requiring_clarification_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.requiring_clarification_control_to_send_control));
                            selection.setSelected(true);
                            listStatus.set(k,selection);
                        }
                    }
                }
                else {
                    imgRequiringClarification.setBackgroundColor(Color.TRANSPARENT);
                    isRequiringClarification=false;
                    strRequiringClarification="";
                    for (int k=0;k<listStatus.size();k++){
                        if(listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.requiring_clarification_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.requiring_clarification_control_to_send_control));
                            selection.setSelected(false);
                            listStatus.set(k,selection);
                        }
                    }
                }
            }
        });
    }
    //    initialize traffic dilaog contol method
    public void initializeTrafficDialogControl(Dialog dialog){
        imgTrafficRed=(ImageView)dialog.findViewById(R.id.img_traffic_red);
        imgTrafficGreen=(ImageView)dialog.findViewById(R.id.img_traffic_green);
        imgTrafficYello=(ImageView)dialog.findViewById(R.id.img_traffic_yellow);

        linearTrafficRed=(LinearLayout) dialog.findViewById(R.id.linear_red);
        linearTrafficGreen=(LinearLayout) dialog.findViewById(R.id.linear_green);
        linearTrafficYello=(LinearLayout) dialog.findViewById(R.id.linear_yellow);

        linearTrafficRed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isTrafficRed){
                    imgTrafficRed.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isTrafficRed=true;
                    strTrafficRed=getResources().getString(R.string.traffic_red);
                    for (int k=0;k<listTraffic.size();k++){
                        if(listTraffic.get(k).getValue().equalsIgnoreCase(getString(R.string.traffic_red_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.traffic_red_control_to_send_control));
                            selection.setSelected(true);
                            listTraffic.set(k,selection);
                        }
                    }
                }
                else {
                    imgTrafficRed.setBackgroundColor(Color.TRANSPARENT);
                    isTrafficRed=false;
                    strTrafficRed="";
                    for (int k=0;k<listTraffic.size();k++){
                        if(listTraffic.get(k).getValue().equalsIgnoreCase(getString(R.string.traffic_red_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.traffic_red_control_to_send_control));
                            selection.setSelected(false);
                            listTraffic.set(k,selection);
                        }
                    }
                }

            }
        });
        linearTrafficGreen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isTrafficGreen){
                    imgTrafficGreen.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isTrafficGreen=true;
                    strTrafficGreen=getResources().getString(R.string.traffic_green);
                    for (int k=0;k<listTraffic.size();k++){
                        if(listTraffic.get(k).getValue().equalsIgnoreCase(getString(R.string.traffic_green_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.traffic_green_control_to_send_control));
                            selection.setSelected(true);
                            listTraffic.set(k,selection);
                        }
                    }
                }
                else {
                    imgTrafficGreen.setBackgroundColor(Color.TRANSPARENT);
                    isTrafficGreen=false;
                    strTrafficGreen="";
                    for (int k=0;k<listTraffic.size();k++){
                        if(listTraffic.get(k).getValue().equalsIgnoreCase(getString(R.string.traffic_green_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.traffic_green_control_to_send_control));
                            selection.setSelected(false);
                            listTraffic.set(k,selection);
                        }
                    }
                }
            }
        });
        linearTrafficYello.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isTrafficYello){
                    imgTrafficYello.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isTrafficYello=true;
                    strTrafficYello=getResources().getString(R.string.traffic_yellow);
                    for (int k=0;k<listTraffic.size();k++){
                        if(listTraffic.get(k).getValue().equalsIgnoreCase(getString(R.string.traffic_yellow_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.traffic_yellow_control_to_send_control));
                            selection.setSelected(true);
                            listTraffic.set(k,selection);
                        }
                    }
                }
                else {
                    imgTrafficYello.setBackgroundColor(Color.TRANSPARENT);
                    isTrafficYello=false;
                    strTrafficYello="";
                    for (int k=0;k<listTraffic.size();k++){
                        if(listTraffic.get(k).getValue().equalsIgnoreCase(getString(R.string.traffic_yellow_control_to_send_control))){
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.traffic_yellow_control_to_send_control));
                            selection.setSelected(false);
                            listTraffic.set(k,selection);
                        }
                    }
                }
            }
        });
    }
    // add static data in popup to show
    public void addingDataInPopup(int pos) {
        if(pos == 1){
            String[] arraylistWeek = {getString(R.string.year),getString(R.string.quarter),getString(R.string.months),
                    getString(R.string.week_s),getString(R.string.week_m),getString(R.string.week_l),getString(R.string.days),
                    getString(R.string.hours),getString(R.string.minutes),getString(R.string.seconds),
                    getString(R.string.daynightshift)};
            String[] arraylistWeekToSend = {"Year","Quarter","Months","Week S","Week M","Week L","Days",
                    "Hours","Minutes","Seconds","DayNightShift"};
            String[] arrayprojectDateOriginalvalue = {"ShowYear", "ShowQuarter", "ShowMonths", "ShowWeeks",
                    "ShowWeekM","ShowWeekL","ShowDays","ShowHours","ShowMinutes","ShowSeconds","ShowDayNightShift"};

            arraylistPopupViewforWeek.clear();
            for (int i = 0; i < arraylistWeek.length; i++) {
                FilterModelClass filter = new FilterModelClass();
                filter.setName(arraylistWeek[i]);
                filter.setStrOriginalValue(arrayprojectDateOriginalvalue[i]);
                filter.setStrTosendControl(arraylistWeekToSend[i]);
                // filter.setDrawable(imgDate[i]);
                filter.setSelected(false);
                /*for (int k = 0; k < selectedString.size(); k++) {
                    if (selectedString.get(k).equalsIgnoreCase(arraylistWeek[i])) {
                        filter.setSelected(true);
                    }

                }*/
                arraylistPopupViewforWeek.add(filter);
            }
        }
        if(pos == 2){
            if (arraylistProfile.size() > 0)
            {
            }
        }
    }
    //    showing popup window
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void showPopupWindow(final int popupNo) {
        if(popupNo == 1)
        {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            final View popupView = layoutInflater.inflate(R.layout.popupview_project_week, null);
            popupWindowWeek = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            isTabletSize = getResources().getBoolean(R.bool.isTablet);
            if(isTabletSize){
                popupWindowWeek.setWidth((GlobalClass.screenWidth(getContext()) * 30) / 100);
                popupWindowWeek.setHeight((GlobalClass.screenHeight(getContext()) * 50) / 100);
            }
            else {
                popupWindowWeek.setWidth((GlobalClass.screenWidth(getContext()) * 60) / 100);
                popupWindowWeek.setHeight((GlobalClass.screenHeight(getContext()) * 50) / 100);
            }
            popupWindowWeek.setOutsideTouchable(true);
            popupWindowWeek.showAsDropDown(textviewWeekDialog,0,20, Gravity.CENTER | Gravity.BOTTOM);
            ListView listviewWeeklist = (ListView) popupView.findViewById(R.id.listviewprojectdate);

            adapterWeek = new ProjectWeekAdapter(getActivity(), arraylistPopupViewforWeek);
            listviewWeeklist.setAdapter(adapterWeek);
            listviewWeeklist.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    if(arraylistProfileToSetasCurrent != null && arraylistProfileToSetasCurrent.size() > 0){
                        arraylistProfileToSetasCurrent.get(0).setResolution(arraylistPopupViewforWeek.get(position).getName());
                        ProfileModelClass model = new ProfileModelClass();
                        model = arraylistProfileToSetasCurrent.get(0);
                        preferences.saveIsProgressShow("yes");
                        preferences.clearUserProfile();
                        preferences.saveUserProfile(new Gson().toJson(model));
                        preferences.saveProfileId(model.getProfileId());
                        if(arraylistProfile != null && arraylistProfile.size() > 0){
                            for (int i=0;i<arraylistProfile.size();i++)
                            {
                                if(arraylistProfile.get(i).getProfileId().equalsIgnoreCase(arraylistProfileToSetasCurrent.get(0).
                                        getProfileId())){
                                    arraylistProfile.get(i).setResolution(arraylistProfileToSetasCurrent.get(0).getResolution());
                                }
                            }
                        }
                        dbHandler.updateProfile(arraylistPopupViewforWeek.get(position).getName(), arraylistProfileToSetasCurrent.get(0).
                                getProfileId());
                    }
                    if(adapterProfile != null){
                        adapterProfile.notifyDataSetChanged();
                    }
                    isPopupDate=false;
                    popupWindowWeek.dismiss();
                    textviewWeekDialog.setText(arraylistPopupViewforWeek.get(position).getName());
                    fireResolution(arraylistPopupViewforWeek.get(position).getStrTosendControl());

                }
            });
        }
    }
    //    return general filter selected value into string
    //    return general filter selected value into string
    public String getGeneralFilterJson(){
        //String jsonStrFolder="",jsonStrText="",jsonStrUserDefined="",jsonStrDate="";
        JSONObject jsonObjectFolder=null;//,jsonObjectText=null,jsonObjectUserDefined=null,jsonObjectDate=null;
        jsonObjectFolder = new JSONObject();
        /* getting folder filter from database */
        ArrayList<GeneralFilterDataSet> arraylistFolderlist = new ArrayList<>();
        if(dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(),getString(R.string.FolderFilter_text),preferences.getLanguage(),
                preferences.getUserID()) > 0){
            /*arraylistFolderlist = dbHandler.getGeneralFilterData(getString(R.string.FolderFilter_text), preferences.getUserID(),
                    preferences.getProfileId());*/
            arraylistFolderlist = dbHandler.getAllGeneralFilterData(preferences.getProfileId(),getString(R.string.FolderFilter_text),preferences.getLanguage(),
                    preferences.getUserID());
            if (arraylistFolderlist.size() > 0) {
                String[] split = arraylistFolderlist.get(0).getValueLocal().split(",");
                String joined = GlobalClass.arrayToCommaSeparated(split);
                //jsonObjectFolder = new JSONObject();
                try {
                    jsonObjectFolder.put("GroupId",joined);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //jsonStrFolder = jsonObjectFolder.toString();

            }
        }
        /* getting text filter from database */
        ArrayList<GeneralFilterDataSet> arraylistTextFilter = new ArrayList<>();
        if(dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(),getString(R.string.TextFilter_text)
                ,preferences.getLanguage(), preferences.getUserID()) > 0){
            /*arraylistFolderlist = dbHandler.getGeneralFilterData(getString(R.string.FolderFilter_text), preferences.getUserID(),
                    preferences.getProfileId());*/
            arraylistTextFilter = dbHandler.getAllGeneralFilterData(preferences.getProfileId(),
                    getString(R.string.TextFilter_text),preferences.getLanguage(),
                    preferences.getUserID());
            if(arraylistTextFilter.size() > 0){
                //jsonObjectText = new JSONObject();
                for (int i=0;i<arraylistTextFilter.size();i++){
                    try {
                        jsonObjectFolder.put(arraylistTextFilter.get(i).getKey(),arraylistTextFilter.get(i).getValue());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                //jsonStrText = jsonStrFolder.toString();
            }
        }

        /* getting userdefinde filter from database */
        ArrayList<GeneralFilterDataSet> arraylisUserDefinedText = new ArrayList<>();
        ArrayList<GeneralFilterDataSet> arraylisUserDefinedNumber = new ArrayList<>();
        if(dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(),
                getString(R.string.UserDefinedFilter_text),preferences.getLanguage(),
                preferences.getUserID()) > 0){
            arraylisUserDefinedText = dbHandler.getAllGeneralFilterData(preferences.getProfileId()
                    ,getString(R.string.UserDefinedFilter_text),preferences.getLanguage(),
                    preferences.getUserID());

        }
        if(dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(),
                getString(R.string.UserDefindedFilterForNumber),preferences.getLanguage(),
                preferences.getUserID()) > 0){
            arraylisUserDefinedText = dbHandler.getAllGeneralFilterData(preferences.getProfileId(),getString(R.string.UserDefindedFilterForNumber)
                    ,preferences.getLanguage(), preferences.getUserID());
        }
        ArrayList<GeneralFilterDataSet> arrayList = new ArrayList<>();
        if (arraylisUserDefinedText.size() > 0) {
            if(arraylisUserDefinedNumber.size() > 0){
                for (int i=0;i<arraylisUserDefinedText.size();i++){
                    arrayList.add(arraylisUserDefinedText.get(i));
                }
                for (int i=0;i<arraylisUserDefinedNumber.size();i++){
                    arrayList.add(arraylisUserDefinedText.get(i));
                }
            }
            else {
                for (int i=0;i<arraylisUserDefinedText.size();i++){
                    arrayList.add(arraylisUserDefinedText.get(i));
                }
            }
            if(arrayList.size() > 0){
                //jsonObjectUserDefined = new JSONObject();
                for (int i=0;i<arrayList.size();i++){
                    try {
                        jsonObjectFolder.put(arrayList.get(i).getKey(),arrayList.get(i).getValue());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                //jsonStrUserDefined = jsonObjectFolder.toString();
            }

        }
        /* getting date filter from database */
        ArrayList<GeneralFilterDataSet> arraylistValueDate = new ArrayList<>();
        if(dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(),
                getString(R.string.DateFilter_text),preferences.getLanguage(),
                preferences.getUserID()) > 0){
            arraylistValueDate = dbHandler.getAllGeneralFilterData(preferences.getProfileId(),getString(R.string.DateFilter_text)
                    ,preferences.getLanguage(), preferences.getUserID());

        }
        if(arraylistValueDate.size() > 0){
            //jsonObjectDate = new JSONObject();
            for (int i=0;i<arraylistValueDate.size();i++){
                try {
                    jsonObjectFolder.put(arraylistValueDate.get(i).getKey(),arraylistValueDate.get(i).getValue());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            //jsonStrDate = jsonObjectFolder.toString();
        }
        String finalStr = jsonObjectFolder.toString();
        return finalStr;
    }
    //     setting textview bold dynamicallly
    //    shortcut method for fire general and ribbon filter
    public void fireGeneralAndRibbonFilterShortCut(){

        /* checking that any filter is fire or not to display textview bold or normal*/
        setTextViewStyleForDateTextView(arraylistRibbonFilterSettedMerged);
        setTextViewStyleForStatusTextView(arraylistRibbonFilterSettedMerged);
        setTextViewStyleForTrafficTextView(arraylistRibbonFilterSettedMerged);
        gettinRibbonFilterfromDB();
        String strGeneralFilterJson = getGeneralFilterJson();
        arraylistRibbonFilterValue = new ArrayList<>();
        //getDateSelectedValue();
        //getStatusSelectedValue();
        //getTrafficSelectedValue();
        String strRibbonFilter="";
        String url="";
        if(arraylistRibbonFilterSettedMerged.size() > 0){
            strRibbonFilter = TextUtils.join(",", arraylistRibbonFilterSettedMerged);
            LogApp.e(""," arraylist ribbon filter : "+strRibbonFilter);
        }
        if(strGeneralFilterJson!= null && !strGeneralFilterJson.equalsIgnoreCase("") && !strGeneralFilterJson.
                equalsIgnoreCase("{}")){
            if(!strRibbonFilter.equalsIgnoreCase("") && strRibbonFilter != null){
                url = "javascript:(function() { SetFiltersDataFromAppMobile('"+strRibbonFilter+"',"+strGeneralFilterJson+");})()";
                //url = "javascript:(function() { SetFiltersDataFromAppMobile(void 0,"+finalJson+");})()";
            }
            else {
                url = "javascript:(function() { SetFiltersDataFromAppMobile(void 0,"+strGeneralFilterJson+");})()";
            }
            webview.loadUrl(url);
            preferences.saveIsProgressShow("no");
        }
        else if(strRibbonFilter != null && !strRibbonFilter.equalsIgnoreCase("")){
            // problem here for general filter its solved already
            url = "javascript:(function() { SetFiltersDataFromAppMobile('"+strRibbonFilter+"','');})()";
            webview.loadUrl(url);
            preferences.saveIsProgressShow("no");
        }
        else if(preferences.getCurrentFiredFilter().equalsIgnoreCase("6")){
            preferences.saveIsProgressShow("no");
        }
        else if(strRibbonFilter.equalsIgnoreCase("")){
            if(preferences.getCurrentFiredFilter().equalsIgnoreCase("7")){
                // problem here for general filter its solved already
                url = "javascript:(function() { SetFiltersDataFromAppMobile('"+strRibbonFilter+"','');})()";
                webview.loadUrl(url);
                preferences.saveIsProgressShow("no");
            }

        }
        else {
            webview.setWebViewClient(new MyWebViewClient2());
            //String finalurl = "file:///android_asset/index.html?profile=1&userId=3&HOST_URL=http://project.ssoft.in:8090/management/";
            webviewUrl = GlobalClass.CONTROL_PATH+ profileIdToSendControl +"&token="+
                    GlobalClass.strAccessToken+GlobalClass.getEpochTime()+"&HOST_URL="+preferences.getDomain()
                    +"&orgId="+preferences.getTokenforControl()+GlobalClass.getEpochTime()+
                    "&isWebViewer=0&Control=1&Lang="+preferences.getLanguage();
            //String finalurl = "file:///android_asset/index.html?profile=1&userId=3&HOST_URL=http://78.46.120.30/BerndTesting/management/";
            webview.loadUrl(webviewUrl);
        }
    }
    //    webview client
    public class MyWebViewClient2 extends  WebViewClient{
        ProgressDialog progressDialog = null;
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            if(preferences.getIsProgressShow().equalsIgnoreCase("no")){

            }
            else {
                progressDialog = new ProgressDialog(getActivity());
                //progressDialog.create();

                SpannableString ss=  new SpannableString(getString(R.string.please_wait));
                ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new CustomTypefaceSpan("", GlobalClass.fontRegular), 0, ss.length(), 0);

                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(ss);
                progressDialog.show();
            }

        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            preferences.saveIsProgressShow("yes");
        }
    }
    //    webview client for genera and ribbon filter
    public class MyWebViewClientForGeneralFilter extends  WebViewClient{
        ProgressDialog progressDialog = null;
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if(preferences.getIsProgressShow().equalsIgnoreCase("no")){
            }
            else {
                progressDialog = new ProgressDialog(getActivity());
                //progressDialog.create();

                SpannableString ss=  new SpannableString(getString(R.string.please_wait));
                ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new CustomTypefaceSpan("", GlobalClass.fontRegular), 0, ss.length(), 0);

                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage(ss);
                progressDialog.show();
            }
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            fireGeneralAndRibbonFilterShortCut();
            preferences.saveCurrentFiredFilter("7");
        }
    }
    //    just refresh control
    public void refreshControl(){
        webview.setWebViewClient(new MyWebViewClientForGeneralFilter());
        webviewUrl = GlobalClass.CONTROL_PATH+ profileIdToSendControl +"&token="+
                GlobalClass.strAccessToken+GlobalClass.getEpochTime()+"&HOST_URL="+preferences.getDomain()
                +"&orgId="+preferences.getTokenforControl()+GlobalClass.getEpochTime()+
                "&isWebViewer=0&Control=2&Lang="+preferences.getLanguage();
        webview.loadUrl(webviewUrl);
        //// fire general and ribbon filter after load url
    }
    //    method to load control on click of today textview
    public void fireToday(){
        String url = "javascript:(function() { ScrollToToday();})()";
        webview.loadUrl(url);
    }
    //    method to load control on selecting any resolution from popuview
    public void fireResolution(String resolution){
        String url = "javascript:(function() { changeResolution('"+resolution+"');})()";
        webview.loadUrl(url);
    }
    //    set language for date dialog
    public void setLanguageDateLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config,this.getResources().getDisplayMetrics());

        txtdatedheder.setText(getString(R.string.date));
        txtcomplition.setText(getString(R.string.completion_overrun));
        txtstartproject.setText(getString(R.string.started_project_task));
        txtcompltetoday.setText(getString(R.string.complete_today));
        txtcompleteproject.setText(getString(R.string.complete_project_task));
        txtpendingproject.setText(getString(R.string.pending_project_task));
        txtpassiveproject.setText(getString(R.string.passive_project_task));
        txtcompltein1week.setText(getString(R.string.completed_in1week));
        txttaskwithoutdate.setText(getString(R.string.task_without_date));
        txtcanceledprojecttask.setText(getString(R.string.canceled_project_task));
        buttonOk.setText(getString(R.string.ok));

    }
    //    setting langeuage for traffic dialog
    public void setLanguageTrafficDialog(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config,this.getResources().getDisplayMetrics());

        txtred.setText(getString(R.string.traffic_red));
        txtyellow.setText(getString(R.string.traffic_yellow));
        txtgreen.setText(getString(R.string.traffic_green));
        buttonTrafficOk.setText(R.string.ok);
    }
    //    setting language for status dialog
    public void setLanguageStatusDialog(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config,this.getResources().getDisplayMetrics());

        txtstatusheder.setText(getString(R.string.status));
        txtnoticearrow.setText(getString(R.string.notice_arrow));
        txtreshow.setText(getString(R.string.reshow));
        txtlimitoverun.setText(getString(R.string.limit_overrun));
        txtcritical.setText(getString(R.string.critical));
        txtrequring.setText(getString(R.string.requiring_clarification_control_to_send_control));
        buttonStatusOk.setText(R.string.ok);
    }
    //    set language dynamically
    public void setLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config,this.getResources().getDisplayMetrics());

        textviewDateDialog.setText(getString(R.string.date));
        textviewStatusDialog.setText(getString(R.string.status));
        textviewTrafficLightDialog.setText(getString(R.string.TrafficLights));

        textviewToday.setText(getString(R.string.today));
        textViewTitle.setText(getString(R.string.resource_header));
    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        context = activity;
        ((HomeActivity)context).interfaceLoadWebviewResource = this;
        ((HomeActivity)context).interfaceChangeLanguageResource = this;
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
    public void changeText(String someText) {
        Log.v("mylog", "changeText is called");
        //webview.loadUrl("javascript:document.getElementById('test1').innerHTML = '<strong>" + someText + "</strong>'");
    }
    //    fire general and ribbon filter based on condition
    public void fireGeneralAndRibbonFilter(){

        /// 9 == refresh control and call js fuction
        /// 9 == refresh control and call js function
        /// 10 == not refresh control just call js function
        /// 11 === do nothing
        /*if(preferences.getCurrentFiredFilter().equalsIgnoreCase("7")){
            ///// don't refresh control here just call JS function General filter
            preferences.saveIsGeneralFilterDialogshow("yes");
            fireGeneralAndRibbonFilterShortCut();
            preferences.saveCurrentFiredFilter("8");
        }
        else if(preferences.getCurrentFiredFilter().equalsIgnoreCase("6")){
            ////// refresh control and then call JS function of General filter
            preferences.saveIsGeneralFilterDialogshow("yes");
            refreshControl();
            preferences.saveCurrentFiredFilter("7");
        }*/
        if(preferences.getFireResourcefilter().equalsIgnoreCase("9")){
            // this condition will refresh control and call js function
            preferences.saveIsGeneralFilterDialogshow("yes");
            preferences.saveFireResourcefilter("11");
            if(preferences.getCurrentFiredFilter().equalsIgnoreCase("7")){
                preferences.saveCurrentFiredFilter("8");
            }
            refreshControl();
        }
        else if(preferences.getFireResourcefilter().equalsIgnoreCase("10")){
            // this condition will call js function
            preferences.saveIsGeneralFilterDialogshow("yes");
            fireGeneralAndRibbonFilterShortCut();
            /*if(preferences.getCurrentFiredFilter().equalsIgnoreCase("1") || preferences.getCurrentFiredFilter().equalsIgnoreCase("2")
                    || preferences.getCurrentFiredFilter().equalsIgnoreCase("3") || preferences.getCurrentFiredFilter().equalsIgnoreCase("4")){
                preferences.saveCurrentFiredFilter("8");
            }
            else {
                preferences.saveCurrentFiredFilter("7");
            }*/

            preferences.saveFireResourcefilter("11");
        }
        else if(preferences.getCurrentFiredFilter().equalsIgnoreCase("7")){
            // this condition will call js function
            preferences.saveIsGeneralFilterDialogshow("yes");
            fireGeneralAndRibbonFilterShortCut();
            //preferences.saveCurrentFiredFilter("8");
            preferences.saveFireResourcefilter("11");
        }
        else if(preferences.getFireResourcefilter().equalsIgnoreCase("11")){
            if(preferences.getCurrentFiredFilter().equalsIgnoreCase("8")){
                preferences.saveIsGeneralFilterDialogshow("yes");
                preferences.saveFireResourcefilter("11");
                if(preferences.getLastFiredFilterAll().equalsIgnoreCase("general")){
                    // this condition will call js function
                    /*preferences.saveIsGeneralFilterDialogshow("yes");
                    fireGeneralAndRibbonFilterShortCut();
                    preferences.saveFireResourcefilter("11");*/
                }

            }
            else if(preferences.getLastFiredFilterAll().equalsIgnoreCase("general")){
                // this condition will call js function
                preferences.saveIsGeneralFilterDialogshow("yes");
                fireGeneralAndRibbonFilterShortCut();
                //preferences.saveCurrentFiredFilter("8");
                preferences.saveFireResourcefilter("11");
            }
            else if(preferences.getLastFiredFilterAll().equalsIgnoreCase("general_resource")){
                // this condition will call js function
                preferences.saveIsGeneralFilterDialogshow("yes");
                fireGeneralAndRibbonFilterShortCut();
                //preferences.saveCurrentFiredFilter("8");
                preferences.saveFireResourcefilter("11");
            }
            /* do nothing */
        }


    }
    public class CustomTouchListenerDate implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent)
        {
            switch (motionEvent.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    buttonOk.setTextColor(getResources().getColor(R.color.grey_dashboard));
                    //white
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    buttonOk.setTextColor(getResources().getColor(R.color.black_color)); //black
                    break;
            }
            return false;
        }
    }
    public class CustomTouchListenerStatus implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent)
        {
            switch (motionEvent.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    buttonStatusOk.setTextColor(getResources().getColor(R.color.grey_dashboard));
                    //white
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    buttonStatusOk.setTextColor(getResources().getColor(R.color.black_color)); //black
                    break;
            }
            return false;
        }
    }
    public class CustomTouchListenerTraffic implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent)
        {
            switch (motionEvent.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    buttonTrafficOk.setTextColor(getResources().getColor(R.color.grey_dashboard));
                    //white
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    buttonTrafficOk.setTextColor(getResources().getColor(R.color.black_color)); //black
                    break;
            }
            return false;
        }
    }
    @Override
    public void callResourceWebviewFromFooter() {
        getAllProfilefromDatabase();
        setLanguage(preferences.getLanguage());
        preferences.saveIsProgressShow("yes");
        /* when press project from footer this will fire */
        /// 9 == just refresh control don't call any js fuction
        /// 9 == refresh control and call js function
        /// 10 == not refresh control just call js function
        /// 11 === do nothing
        if(preferences.getResourcefilter().equalsIgnoreCase("")){
            // fire firsttime here resource filter
            preferences.saveResourcefilter("no");
            if(preferences.getFireResourcefilter().equalsIgnoreCase("")){
                preferences.saveFireResourcefilter("9");
            }
        }
        else if(preferences.getResourcefilter().equalsIgnoreCase("no")){

            // don't refresh resource just call js method here
        }
        fireGeneralAndRibbonFilter();
    }
    /* new code after changes profile management code in project screen */
    //    getting all profile from database and show in popup
    public void getAllProfilefromDatabase(){
        /* gettting all profile from database to show*/
        arraylistProfile = new ArrayList<>();
        arraylistProfile = dbHandler.getAllUserProfile(preferences.getUserID(),preferences.getProfileId(),
                preferences.getLanguage());

        /* this is condition check that any profile is set or not for default to show */
        if(preferences.getUserProfile() != null && !preferences.getUserProfile().equalsIgnoreCase("")){

            /* return current profile */
            ProfileModelClass model = new Gson().fromJson(preferences.getUserProfile(),ProfileModelClass.class);
            arraylistProfileToSetasCurrent = new ArrayList<>();
            arraylistProfileToSetasCurrent.add(model);
            profileIdToSendControl = arraylistProfileToSetasCurrent.get(0).getProfileId();
            //textviewWeekDialog.setText(arraylistProfileToSetasCurrent.get(0).getResolution());
            //textviewWeekDialog.setText(getString(R.string.weekm));
            preferences.saveProfileId(arraylistProfileToSetasCurrent.get(0).getProfileId());
        }else {
            /* if current profile not setted set 0 INDEX defaulit as current profile(DEFAULT PROFILE)*/
            if (arraylistProfile.size() > 0) {
                for (int i=0;i<arraylistProfile.size();i++){
                    //if(arraylistProfile.get(i).getProfileName().equalsIgnoreCase("Default Profile")){
                    if(i==0){
                        //profileIdToSendControl = arraylistProfile.get(0).getProfileId();
                        //textviewDefaultProfile.setText(arraylistProfile.get(0).getProfileName());
                        ProfileModelClass model = new ProfileModelClass();
                        model = arraylistProfile.get(i);
                        String json = new Gson().toJson(model);
                        preferences.saveUserProfile(json);
                        arraylistProfileToSetasCurrent = new ArrayList<>();
                        arraylistProfileToSetasCurrent.add(model);
                        preferences.saveProfileId(model.getProfileId());
                    }
                }
            }
            if(arraylistProfileToSetasCurrent.size() > 0){
                profileIdToSendControl = arraylistProfileToSetasCurrent.get(0).getProfileId();
                //textviewWeekDialog.setText(arraylistProfileToSetasCurrent.get(0).getResolution());
                //textviewWeekDialog.setText(getString(R.string.weekm));

            }
        }
        isTabletSize = getResources().getBoolean(R.bool.isTablet);
        if (isTabletSize) {
            textviewWeekDialog.setTypeface(GlobalClass.fontLight);
        } else {
            textviewWeekDialog.setTypeface(GlobalClass.fontMedium);
        }
        gettinRibbonFilterfromDB();
    }
    public void gettinRibbonFilterfromDB(){
        /* default profile arraylist data */
        if(arraylistProfileToSetasCurrent.size() > 0){
            ArrayList<RibbonFilterDataSet> arrayListRibbon = dbHandler.getRibbonFilter(arraylistProfileToSetasCurrent.get(0).getProfileId(),
                    preferences.getUserID(), arraylistProfileToSetasCurrent.get(0).getLanguage());

            if(arrayListRibbon.size() > 0){
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                List<String> arrayListKey = new ArrayList<>();
                List<String> arrayListValue = new ArrayList<>();
                List<String> arrayListValueLocal = new ArrayList<>();
                /*arrayListKey = gson.fromJson(arrayListRibbon.get(0).getKey(),type);
                arrayListValue = gson.fromJson(arrayListRibbon.get(0).getValue(),type);
                arrayListValueLocal = gson.fromJson(arrayListRibbon.get(0).getValueLocal(),type);*/
                arrayListKey = GlobalClass.commaSeparetedtoArraylist(arrayListRibbon.get(0).getKey());
                arrayListValue = GlobalClass.commaSeparetedtoArraylist(arrayListRibbon.get(0).getValue());
                arrayListValueLocal = GlobalClass.commaSeparetedtoArraylist(arrayListRibbon.get(0).getValueLocal());
                arraylistRibbonFilterSettedFromServer = new ArrayList<>();
                arraylistRibbonFilterSettedFromLocal = new ArrayList<>();
                arraylistRibbonFilterSettedMerged = new ArrayList<>();
                if(arrayListValue !=null && arrayListValue.size() > 0 ){
                    if(arrayListValueLocal !=null && arrayListValueLocal.size() > 0){
                        arraylistRibbonFilterSettedFromServer.addAll(arrayListValue);
                        arraylistRibbonFilterSettedFromLocal.addAll(arrayListValueLocal);
                        arraylistRibbonFilterSettedMerged.addAll(arraylistRibbonFilterSettedFromServer);
                        arraylistRibbonFilterSettedMerged.addAll(arraylistRibbonFilterSettedFromLocal);
                        Set<String> setList = new LinkedHashSet<String>(arraylistRibbonFilterSettedMerged);
                        arraylistRibbonFilterSettedMerged.clear();
                        arraylistRibbonFilterSettedMerged.addAll(setList);
                        arraylistRibbonFilterSettedMerged.removeAll(Arrays.asList(null,""));
                        arraylistRibbonFilterSettedFromServer.removeAll(Arrays.asList(null,""));
                        arraylistRibbonFilterSettedFromLocal.removeAll(Arrays.asList(null,""));

                        /*String valueServer = GlobalClass.removeCommaAttheEnd(arrayListValue.get(0));
                        String valueLocal = GlobalClass.removeCommaAttheEnd(arrayListValueLocal.get(0));
                        String strFinal = valueServer+","+valueLocal;
                        arraylistRibbonFilterSettedFromServer.add(valueServer);
                        arraylistRibbonFilterSettedFromLocal.add(valueLocal);
                        arraylistRibbonFilterSettedMerged.add(strFinal);*/
                    }
                    else {
                        String str = GlobalClass.removeCommaAttheEnd(arrayListValue.get(0));
                        arraylistRibbonFilterSettedMerged.add(str);
                        arraylistRibbonFilterSettedFromServer.add(str);
                        /*for (int i =0;i<arrayListKey.size();i++){
                            if(arrayListKey.get(i).equalsIgnoreCase("RibbonFilter")){
                                String str = arrayListValue.get(i)*//*.substring(0, arrayListValue.get(i).lastIndexOf(","))*//*;
                                arraylistRibbonFilterSettedFromServer.add(str);
                                //break;
                            }
                        }*/
                    }
                }
                if(arraylistRibbonFilterSettedMerged.size() > 0){
                    setTextViewStyleForDateTextView(arraylistRibbonFilterSettedMerged);
                    setTextViewStyleForStatusTextView(arraylistRibbonFilterSettedMerged);
                    setTextViewStyleForTrafficTextView(arraylistRibbonFilterSettedMerged);
                }
                else {
                    textviewDateDialog.setTypeface(GlobalClass.fontLight);
                    textviewStatusDialog.setTypeface(GlobalClass.fontLight);
                    textviewTrafficLightDialog.setTypeface(GlobalClass.fontLight);
                }
            }

        }
    }
    public void setTextViewStyleForDateTextView(ArrayList<String> arrayList)
    {
        if(arrayList.size() > 0) {
            if (isDateFilterSettedFromService(arrayList)) {
                textviewDateDialog.setTypeface(GlobalClass.fontBold);
            } else {
                textviewDateDialog.setTypeface(GlobalClass.fontLight);
            }
        }
    }
    public void setTextViewStyleForStatusTextView(ArrayList<String> arrayList)
    {
        if(arrayList.size() > 0) {
            if (isStatusFilterSettedFromService(arrayList)) {
                textviewStatusDialog.setTypeface(GlobalClass.fontBold);
            } else {
                textviewStatusDialog.setTypeface(GlobalClass.fontLight);
            }
        }
    }
    public void setTextViewStyleForTrafficTextView(ArrayList<String> arrayList)
    {
        if(arrayList.size() > 0) {
            if (isTrafficFilterSettedFromService(arrayList)) {
                textviewTrafficLightDialog.setTypeface(GlobalClass.fontBold);
            } else {
                textviewTrafficLightDialog.setTypeface(GlobalClass.fontLight);
            }
        }
    }
    public void showDateFilterSettedFromService(ArrayList<String> arrayValue){
        boolean flag=false;
        for (int i=0;i<arrayValue.size();i++){
            if(arrayValue.get(i).equalsIgnoreCase(getString(R.string.completion_overrun_control_to_send_control))){
                imgCompilationOverrun.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isComplitionOverrun=true;
                for (int k=0;k<listDate.size();k++){
                    if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.completion_overrun_control_to_send_control))){
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.completion_overrun_control_to_send_control));
                        selection.setSelected(true);
                        listDate.set(k,selection);
                    }
                }
            }
            /*else {
                imgCompilationOverrun.setBackgroundColor(Color.TRANSPARENT);
            }*/
            else if(arrayValue.get(i).equalsIgnoreCase(getString(R.string.started_project_task_control_to_send_control))){
                imgStartedProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isStartedProject=true;
                for (int k=0;k<listDate.size();k++){
                    if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.started_project_task_control_to_send_control))){
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.started_project_task_control_to_send_control));
                        selection.setSelected(true);
                        listDate.set(k,selection);
                    }
                }
            }
            /*else {
                imgStartedProject.setBackgroundColor(Color.TRANSPARENT);
            }*/
            else if(arrayValue.get(i).equalsIgnoreCase(getString(R.string.complete_project_task_control_to_send_control))){
                imgCompeletedProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isCompletedProject=true;
                for (int k=0;k<listDate.size();k++){
                    if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.complete_project_task_control_to_send_control))){
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.complete_project_task_control_to_send_control));
                        selection.setSelected(true);
                        listDate.set(k,selection);
                    }
                }
            }/*else {
                imgCompeletedProject.setBackgroundColor(Color.TRANSPARENT);
            }*/
            else if(arrayValue.get(i).equalsIgnoreCase(getString(R.string.complete_today_control_to_send_control))){
                imgCompleteToday.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isCompletedToday=true;
                for (int k=0;k<listDate.size();k++){
                    if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.complete_today_control_to_send_control))){
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.complete_today_control_to_send_control));
                        selection.setSelected(true);
                        listDate.set(k,selection);
                    }
                }
            }
            /*else {
                imgCompleteToday.setBackgroundColor(Color.TRANSPARENT);
            }*/
            else if(arrayValue.get(i).equalsIgnoreCase(getString(R.string.pending_project_task_control_to_send_control))){
                imgPendingProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isPendingProject=true;
                for (int k=0;k<listDate.size();k++){
                    if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.pending_project_task_control_to_send_control))){
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.pending_project_task_control_to_send_control));
                        selection.setSelected(true);
                        listDate.set(k,selection);
                    }
                }
            }
            /*else {
                imgPendingProject.setBackgroundColor(Color.TRANSPARENT);
            }*/
            else if(arrayValue.get(i).equalsIgnoreCase(getString(R.string.passive_project_task_control_to_send_control))){
                imgPassiveProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isPassiveProject=true;
                for (int k=0;k<listDate.size();k++){
                    if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.passive_project_task_control_to_send_control))){
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.passive_project_task_control_to_send_control));
                        selection.setSelected(true);
                        listDate.set(k,selection);
                    }
                }
            }/*else {
                imgPassiveProject.setBackgroundColor(Color.TRANSPARENT);
            }*/
            else if(arrayValue.get(i).equalsIgnoreCase(getString(R.string.completed_in1week_control_to_send_control))){
                imgCompletedWeek.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isCompletedWeek=true;
                for (int k=0;k<listDate.size();k++){
                    if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.completed_in1week_control_to_send_control))){
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.completed_in1week_control_to_send_control));
                        selection.setSelected(true);
                        listDate.set(k,selection);
                    }
                }
            }/*else {
                imgCompletedWeek.setBackgroundColor(Color.TRANSPARENT);
            }*/
            else if(arrayValue.get(i).equalsIgnoreCase(getString(R.string.task_without_date_control_to_send_control))){
                imgTaskWithoutDate.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isTaskWithoutDate=true;
                for (int k=0;k<listDate.size();k++){
                    if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.task_without_date_control_to_send_control))){
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.task_without_date_control_to_send_control));
                        selection.setSelected(true);
                        listDate.set(k,selection);
                    }
                }
            }
            /*else {
                imgTaskWithoutDate.setBackgroundColor(Color.TRANSPARENT);
            }*/
            else if(arrayValue.get(i).equalsIgnoreCase(getString(R.string.canceled_project_task_control_to_send_control))){
                imgCanceledProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isCanceledProject=true;
                for (int k=0;k<listDate.size();k++){
                    if(listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.canceled_project_task_control_to_send_control))){
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.canceled_project_task_control_to_send_control));
                        selection.setSelected(true);
                        listDate.set(k,selection);
                    }
                }
            }
            /*else {
                imgCanceledProject.setBackgroundColor(Color.TRANSPARENT);
            }*/
        }
    }
    public void showStatusFilterSettedFromService(ArrayList<String> arrayValue){
        boolean flag=false;
        for (int i=0;i<arrayValue.size();i++){
            if(arrayValue.get(i).equalsIgnoreCase(getString(R.string.notice_arrow_control_to_send_control))){
                imgNoticeArrow.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isNoticeArrow=true;
                for (int k=0;k<listStatus.size();k++){
                    if(listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.notice_arrow_control_to_send_control))){
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.notice_arrow_control_to_send_control));
                        selection.setSelected(true);
                        listStatus.set(k,selection);
                    }
                }
            }
            /*else {
                imgNoticeArrow.setBackgroundColor(Color.TRANSPARENT);
            }*/

            else if(arrayValue.get(i).equalsIgnoreCase(getString(R.string.reshow_control_to_send_control))){
                imgReshow.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isReshow=true;
                for (int k=0;k<listStatus.size();k++){
                    if(listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.reshow_control_to_send_control))){
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.reshow_control_to_send_control));
                        selection.setSelected(true);
                        listStatus.set(k,selection);
                    }
                }
            }
            /*else {
                imgReshow.setBackgroundColor(Color.TRANSPARENT);
            }*/

            else if(arrayValue.get(i).equalsIgnoreCase(getString(R.string.limit_overrun_control_to_send_control))){
                imgLimitoverrun.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isLimitOverrun=true;
                for (int k=0;k<listStatus.size();k++){
                    if(listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.limit_overrun_control_to_send_control))){
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.limit_overrun_control_to_send_control));
                        selection.setSelected(true);
                        listStatus.set(k,selection);
                    }
                }
            }/*else {
                imgLimitoverrun.setBackgroundColor(Color.TRANSPARENT);
            }*/

            else if(arrayValue.get(i).equalsIgnoreCase(getString(R.string.critical_control_to_send_control))){
                imgCritical.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isCritical=true;
                for (int k=0;k<listStatus.size();k++){
                    if(listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.critical_control_to_send_control))){
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.critical_control_to_send_control));
                        selection.setSelected(true);
                        listStatus.set(k,selection);
                    }
                }
            }
            /*else {
                imgCritical.setBackgroundColor(Color.TRANSPARENT);
            }*/

            else if(arrayValue.get(i).equalsIgnoreCase(getString(R.string.requiring_clarification_control_to_send_control))){
                imgRequiringClarification.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isRequiringClarification=true;
                for (int k=0;k<listStatus.size();k++){
                    if(listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.requiring_clarification_control_to_send_control))){
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.requiring_clarification_control_to_send_control));
                        selection.setSelected(true);
                        listStatus.set(k,selection);
                    }
                }
            }
            /*else {
                imgRequiringClarification.setBackgroundColor(Color.TRANSPARENT);
            }*/

        }
    }
    public void showTrafficFilterSettedFromService(ArrayList<String> arrayValue){
        boolean flag=false;
        for (int i=0;i<arrayValue.size();i++){
            if(arrayValue.get(i).equalsIgnoreCase(getString(R.string.traffic_yellow_control_to_send_control))){
                imgTrafficYello.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                strTrafficRed=getResources().getString(R.string.traffic_yellow);
                isTrafficYello=true;
                for (int k=0;k<listTraffic.size();k++){
                    if(listTraffic.get(k).getValue().equalsIgnoreCase(getString(R.string.traffic_yellow_control_to_send_control))){
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.traffic_yellow_control_to_send_control));
                        selection.setSelected(true);
                        listTraffic.set(k,selection);
                    }
                }
            }
            /*else {
                imgTrafficYello.setBackgroundColor(Color.TRANSPARENT);
            }*/

            else if(arrayValue.get(i).equalsIgnoreCase(getString(R.string.traffic_red_control_to_send_control))){
                imgTrafficRed.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                strTrafficRed=getResources().getString(R.string.traffic_red);
                isTrafficRed=true;
                for (int k=0;k<listTraffic.size();k++){
                    if(listTraffic.get(k).getValue().equalsIgnoreCase(getString(R.string.traffic_red_control_to_send_control))){
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.traffic_red_control_to_send_control));
                        selection.setSelected(true);
                        listTraffic.set(k,selection);
                    }
                }
            }
            /*else {
                imgTrafficRed.setBackgroundColor(Color.TRANSPARENT);
            }*/

            else if(arrayValue.get(i).equalsIgnoreCase(getString(R.string.traffic_green_control_to_send_control))){
                imgTrafficGreen.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isTrafficGreen=true;
                strTrafficRed=getResources().getString(R.string.traffic_green);
                for (int k=0;k<listTraffic.size();k++){
                    if(listTraffic.get(k).getValue().equalsIgnoreCase(getString(R.string.traffic_green_control_to_send_control))){
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.traffic_green_control_to_send_control));
                        selection.setSelected(true);
                        listTraffic.set(k,selection);
                    }
                }
            }/*else {
                imgTrafficGreen.setBackgroundColor(Color.TRANSPARENT);
            }*/
        }
    }
    /* this function return that DATE filter is seted or not */
    public boolean isDateFilterSettedFromService(ArrayList<String> arrayValue){
        boolean flag=false;
        for (int i=0;i<arrayValue.size();i++){
            for (int j =0;j<arrayDate.length;j++){
                if(arrayValue.get(i).equalsIgnoreCase(arrayDate[j])){
                    flag=true;
                    break;
                }
            }
        }
        return flag;
    }
    /* this function return that STATUS filter is seted or not */
    public boolean isStatusFilterSettedFromService(ArrayList<String> arrayValue){
        boolean flag=false;
        for (int i=0;i<arrayValue.size();i++){
            for (int j =0;j<arrayStatus.length;j++){
                if(arrayValue.get(i).equalsIgnoreCase(arrayStatus[j])){
                    flag=true;
                    break;
                }
            }
        }
        return flag;
    }
    /* this function return that TRAFFIC filter is seted or not */
    public boolean isTrafficFilterSettedFromService(ArrayList<String> arrayValue){
        boolean flag=false;
        for (int i=0;i<arrayValue.size();i++){
            for (int j =0;j<arrayTraffic.length;j++){
                if(arrayValue.get(i).equalsIgnoreCase(arrayTraffic[j])){
                    flag=true;
                    break;
                }
            }
        }
        return flag;
    }
    @Override
    public void profileChanged2() {
        setLanguage(preferences.getLanguage());
        textviewWeekDialog.setText(getString(R.string.set_resolution));
        getAllProfilefromDatabase();
        refreshControl();
    }
    @Override
    public void changeLanguageResource() {
        setLanguage(preferences.getLanguage());
        textviewWeekDialog.setText(getString(R.string.set_resolution));
        getAllProfilefromDatabase();
        refreshControl();
    }
}
