package de.smacsoftwares.aplanapp.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.GestureDetector;
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
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;

import de.smacsoftwares.aplanapp.activity.HomeActivity;
import de.smacsoftwares.aplanapp.adapter.ProfileAdapter;
import de.smacsoftwares.aplanapp.adapter.ProjectWeekAdapter;
import de.smacsoftwares.aplanapp.util.CustomTypefaceSpan;
import de.smacsoftwares.aplanapp.util.DatabaseHandler;
import de.smacsoftwares.aplanapp.util.GenericHelper;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.InterfaceChangeLanguageProfile;
import de.smacsoftwares.aplanapp.util.InterfaceLoadWebview;
import de.smacsoftwares.aplanapp.util.InterfaceProjectClicked;
import de.smacsoftwares.aplanapp.util.LogApp;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;
import okhttp3.Headers;
import retrofit2.Call;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class FragmentProject extends Fragment implements View.OnClickListener, InterfaceLoadWebview,
        InterfaceChangeLanguageProfile, InterfaceProjectClicked {

    LinearLayout linearFilterdate, linearFilterStatus, linearFilterTrafficLights;
    Dialog dialogFilterSetted;
    Boolean isGeneralFilterDialogshow = false;
    String strJsonforTask = "", strJsonforAgreegate = "", strTaskId = "", strSelectedResolution = "";
    String webviewUrl = "", profileIdToSendControl = "", strRibbonFiltertoSendserver = "", strGeneralFiltertoSendserver = "";
    Context context;
    SwipeGestureListener gestureListener;
    TextView txtdatedheder, txtcomplition, txtstartproject, txtcompleteproject, txtcompltetoday, txtpendingproject,
            txtpassiveproject, txtcompltein1week, txttaskwithoutdate, txtcanceledprojecttask, textviewFilter, textViewSaveSetting;
    TextView textviewDateDialog, textviewStatusDialog, textviewTrafficLightDialog, textviewWeekDialog;
    ImageView imgCompilationOverrun, imgStartedProject, imgCompeletedProject, imgCompleteToday, imgPendingProject,
            imgPassiveProject, imgCompletedWeek, imgTaskWithoutDate, imgCanceledProject;
    LinearLayout linearCompilationOverrun, linearStartedProject, linearCompeletedProject, linearCompleteToday,
            linearPendingProject, linearPassiveProject, linearCompletedWeek, linearTaskWithoutDate, linearCanceledProject;

    String strCompilationOverrun = "", strStartedProject = "", strCompletedProject = "", strCompletedToday = "",
            strPendingProject = "", strPassiveProject = "", strCompletedWeek = "", strTaskWithoutDate = "", strCanceledProject = "";
    boolean isComplitionOverrun, isStartedProject, isCompletedProject, isCompletedToday, isPendingProject, isPassiveProject,
            isCompletedWeek, isTaskWithoutDate, isCanceledProject;
    TextView txttraffichheder, txtred, txtyellow, txtgreen;
    TextView txtstatusheder, txtnoticearrow, txtreshow, txtlimitoverun, txtcritical, txtrequring;
    TextView txtaddprofile;
    TextView textViewAddProfile, textViewTitleProfile;

    ImageView imgNoticeArrow, imgReshow, imgLimitoverrun, imgCritical, imgRequiringClarification, imgSaveSetting;
    LinearLayout linearNoticeArrow, linearReshow, linearLimitoverrun, linearCritical, linearRequiringClarification;
    RelativeLayout linearsortPanel;

    String strNoticeArrow = "", strReshow = "", strLimitoverrun = "", strCritical = "", strRequiringClarification = "";
    boolean isNoticeArrow, isReshow, isLimitOverrun, isCritical, isRequiringClarification;

    ImageView imgTrafficRed, imgTrafficYello, imgTrafficGreen;
    LinearLayout linearTrafficRed, linearTrafficYello, linearTrafficGreen;
    RelativeLayout relativeMain;

    String strTrafficRed = "", strTrafficYello = "", strTrafficGreen = "";
    boolean isTrafficRed, isTrafficYello, isTrafficGreen;

    TextView textViewNo;
    TextView textViewYes;
    TextView textviewDefaultProfile, textViewTitle;
    ListView listviewProfile;
    TextView textviewToday;
    ImageView imageViewRefresh;

    PreferencesHelper preferences;
    DatabaseHandler dbHandler;

    Button buttonOk, buttonStatusOk, buttonTrafficOk;
    ImageView imgCancel, imgStatusCancel, imgTrafficCancel;
    ArrayList<ProfileModelClass> arraylistProfile = new ArrayList<>();
    ArrayList<ProfileModelClass> arraylistProfileToSetasCurrent = new ArrayList<>();
    String[] arrayDate;
    String[] arrayStatus;
    String[] arrayTraffic;

    ArrayList<RibbonShowSelectionClass> listDate = new ArrayList<>();
    ArrayList<RibbonShowSelectionClass> listStatus = new ArrayList<>();
    ArrayList<RibbonShowSelectionClass> listTraffic = new ArrayList<>();
    WebView webview;
    ProjectWeekAdapter adapterWeek;
    ProfileAdapter adapterProfile;
    private ArrayList<FilterModelClass> arraylistPopupViewforWeek = new ArrayList<>();
    ArrayList<String> arraylistRibbonFilterValue = new ArrayList<>();
    ArrayList<String> arraylistRibbonFilterSettedFromServer = new ArrayList<>();
    ArrayList<String> arraylistRibbonFilterSettedFromLocal = new ArrayList<>();
    ArrayList<String> arraylistRibbonFilterSettedMerged = new ArrayList<>();
    String jsonTree = "";
    String taskId = "";
    View rootView;
    String strAssigment = "";
    String strResource = "";
    String strComeFrom = "";
    String strProfileName = "";
    String clickedProfileId = "";
    boolean isTabletSize;
    PopupWindow popupWindowWeek, popupWindowProfile;
    Dialog dialogCreateProfile;
    boolean isPopupDate = false, isPopupDefaultProfile = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_project, container, false);
        //MyApplication.component(getActivity()).inject(this);
        dbHandler = new DatabaseHandler(getActivity());
        preferences = new PreferencesHelper(getActivity());
        initControl();
        return rootView;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    //    this method intialize control and class all initial work
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

        for (int i = 0; i < arrayDate.length; i++) {
            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
            selection.setValue(arrayDate[i]);
            selection.setSelected(false);
            listDate.add(selection);
        }
        for (int i = 0; i < arrayStatus.length; i++) {
            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
            selection.setValue(arrayStatus[i]);
            selection.setSelected(false);
            listStatus.add(selection);
        }
        for (int i = 0; i < arrayTraffic.length; i++) {
            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
            selection.setValue(arrayTraffic[i]);
            selection.setSelected(false);
            listTraffic.add(selection);
        }
        GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);
        GlobalClass.fontBold = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPathBold);
        GlobalClass.fontLight = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPathLight);
        GlobalClass.fontMedium = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPathMedium);

        relativeMain = (RelativeLayout) rootView.findViewById(R.id.relative_project_main);
        GlobalClass.setupTypeface(relativeMain, GlobalClass.fontRegular);

        textviewFilter = (TextView) rootView.findViewById(R.id.txt_filter);
        textviewFilter.setOnClickListener(this);

        imgSaveSetting = (ImageView) rootView.findViewById(R.id.txt_save_setting);
        imgSaveSetting.setOnClickListener(this);

        textViewTitle = (TextView) rootView.findViewById(R.id.txt_project);
        textViewTitle.setTypeface(GlobalClass.fontBold);
        textviewDefaultProfile = (TextView) rootView.findViewById(R.id.txt_default_profile);
        textviewDateDialog = (TextView) rootView.findViewById(R.id.txt_date);
        textviewStatusDialog = (TextView) rootView.findViewById(R.id.txt_status);
        textviewTrafficLightDialog = (TextView) rootView.findViewById(R.id.txt_traffic_lights);
        textviewWeekDialog = (TextView) rootView.findViewById(R.id.txt_week_m);
        textviewToday = (TextView) rootView.findViewById(R.id.textviewToday);
        imageViewRefresh = (ImageView) rootView.findViewById(R.id.imageview_refresh);
        linearsortPanel = (RelativeLayout) rootView.findViewById(R.id.project_heder);

        textviewToday.setTypeface(GlobalClass.fontLight);
        GlobalClass.setupTypeface(linearsortPanel, GlobalClass.fontLight);

        textviewToday.setOnClickListener(this);
        imageViewRefresh.setOnClickListener(this);
        textviewDefaultProfile.setOnClickListener(this);
        textviewDateDialog.setOnClickListener(this);
        textviewStatusDialog.setOnClickListener(this);
        textviewTrafficLightDialog.setOnClickListener(this);
        textviewWeekDialog.setOnClickListener(this);
        webview = (WebView) rootView.findViewById(R.id.webView);

        textviewDefaultProfile.setTypeface(GlobalClass.fontRegular);
        //setTextviewBold(textviewDateDialog,getString(R.string.DateLightFilter));
        //setTextviewBold(textviewStatusDialog,getString(R.string.StatusLightFilter));
        //setTextviewBold(textviewTrafficLightDialog,getString(R.string.TrafficLightFilter));

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
        webviewUrl = GlobalClass.CONTROL_PATH + profileIdToSendControl + "&token=" +
                GlobalClass.strAccessToken+GlobalClass.getEpochTime() + "&HOST_URL=" +
                preferences.getDomain() + "&orgId=" + preferences.getTokenforControl()+GlobalClass.getEpochTime() +
                "&isWebViewer=0&Control=1&Lang=" + preferences.getLanguage();
    }

    //    getting all profile from database and show in popup
    public void getAllProfilefromDatabase() {
        /* gettting all profile from database to show*/
        arraylistProfile = new ArrayList<>();
        arraylistProfile = dbHandler.getAllUserProfile(preferences.getUserID(), preferences.getProfileId(),
                preferences.getLanguage());

        /* this is condition check that any profile is set or not for default to show */
        if (preferences.getUserProfile() != null && !preferences.getUserProfile().equalsIgnoreCase("")) {

            /* return current profile */
            ProfileModelClass model = new Gson().fromJson(preferences.getUserProfile(), ProfileModelClass.class);
            arraylistProfileToSetasCurrent = new ArrayList<>();
            arraylistProfileToSetasCurrent.add(model);
            profileIdToSendControl = arraylistProfileToSetasCurrent.get(0).getProfileId();
            textviewDefaultProfile.setText(arraylistProfileToSetasCurrent.get(0).getProfileName());
            //textviewWeekDialog.setText(arraylistProfileToSetasCurrent.get(0).getResolution());
            textviewWeekDialog.setText(getString(R.string.set_resolution));
            preferences.saveProfileId(arraylistProfileToSetasCurrent.get(0).getProfileId());
        } else {
            /* if current profile not setted set 0 INDEX defaulit as current profile(DEFAULT PROFILE)*/
            if (arraylistProfile.size() > 0) {
                for (int i = 0; i < arraylistProfile.size(); i++) {
                    //if(arraylistProfile.get(i).getProfileName().equalsIgnoreCase("Default Profile")){
                    if (i == 0) {
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
            if (arraylistProfileToSetasCurrent.size() > 0) {
                profileIdToSendControl = arraylistProfileToSetasCurrent.get(0).getProfileId();
                textviewDefaultProfile.setText(arraylistProfileToSetasCurrent.get(0).getProfileName());
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

    public void gettinRibbonFilterfromDB() {
        /* default profile arraylist data */
        if (arraylistProfileToSetasCurrent.size() > 0) {
            ArrayList<RibbonFilterDataSet> arrayListRibbon = dbHandler.getRibbonFilter(arraylistProfileToSetasCurrent.get(0).getProfileId(),
                    preferences.getUserID(), arraylistProfileToSetasCurrent.get(0).getLanguage());

            if (arrayListRibbon.size() > 0) {
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<String>>() {
                }.getType();
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
                if (arrayListValue != null && arrayListValue.size() > 0) {
                    if (arrayListValueLocal != null && arrayListValueLocal.size() > 0) {
                        arraylistRibbonFilterSettedFromServer.addAll(arrayListValue);
                        arraylistRibbonFilterSettedFromLocal.addAll(arrayListValueLocal);
                        arraylistRibbonFilterSettedMerged.addAll(arraylistRibbonFilterSettedFromServer);
                        arraylistRibbonFilterSettedMerged.addAll(arraylistRibbonFilterSettedFromLocal);
                        Set<String> setList = new LinkedHashSet<String>(arraylistRibbonFilterSettedMerged);
                        arraylistRibbonFilterSettedMerged.clear();
                        arraylistRibbonFilterSettedMerged.addAll(setList);
                        arraylistRibbonFilterSettedMerged.removeAll(Arrays.asList(null, ""));
                        arraylistRibbonFilterSettedFromServer.removeAll(Arrays.asList(null, ""));
                        arraylistRibbonFilterSettedFromLocal.removeAll(Arrays.asList(null, ""));

                        /*String valueServer = GlobalClass.removeCommaAttheEnd(arrayListValue.get(0));
                        String valueLocal = GlobalClass.removeCommaAttheEnd(arrayListValueLocal.get(0));
                        String strFinal = valueServer+","+valueLocal;
                        arraylistRibbonFilterSettedFromServer.add(valueServer);
                        arraylistRibbonFilterSettedFromLocal.add(valueLocal);
                        arraylistRibbonFilterSettedMerged.add(strFinal);*/
                    } else {
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
                if (arraylistRibbonFilterSettedMerged.size() > 0) {
                    setTextViewStyleForDateTextView(arraylistRibbonFilterSettedMerged);
                    setTextViewStyleForStatusTextView(arraylistRibbonFilterSettedMerged);
                    setTextViewStyleForTrafficTextView(arraylistRibbonFilterSettedMerged);
                } else {
                    textviewDateDialog.setTypeface(GlobalClass.fontLight);
                    textviewStatusDialog.setTypeface(GlobalClass.fontLight);
                    textviewTrafficLightDialog.setTypeface(GlobalClass.fontLight);
                }
            }

        }
    }

    public void setTextViewStyleForDateTextView(ArrayList<String> arrayList) {
        if (arrayList.size() > 0) {
            if (isDateFilterSettedFromService(arrayList)) {
                textviewDateDialog.setTypeface(GlobalClass.fontBold);
            } else {
                textviewDateDialog.setTypeface(GlobalClass.fontLight);
            }
        }
    }

    public void setTextViewStyleForStatusTextView(ArrayList<String> arrayList) {
        if (arrayList.size() > 0) {
            if (isStatusFilterSettedFromService(arrayList)) {
                textviewStatusDialog.setTypeface(GlobalClass.fontBold);
            } else {
                textviewStatusDialog.setTypeface(GlobalClass.fontLight);
            }
        }
    }

    public void setTextViewStyleForTrafficTextView(ArrayList<String> arrayList) {
        if (arrayList.size() > 0) {
            if (isTrafficFilterSettedFromService(arrayList)) {
                textviewTrafficLightDialog.setTypeface(GlobalClass.fontBold);
            } else {
                textviewTrafficLightDialog.setTypeface(GlobalClass.fontLight);
            }
        }
    }

    public void showDateFilterSettedFromService(ArrayList<String> arrayValue) {
        boolean flag = false;
        for (int i = 0; i < arrayValue.size(); i++) {
            if (arrayValue.get(i).equalsIgnoreCase(getString(R.string.completion_overrun_control_to_send_control))) {
                imgCompilationOverrun.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isComplitionOverrun = true;
                for (int k = 0; k < listDate.size(); k++) {
                    if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.completion_overrun_control_to_send_control))) {
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.completion_overrun_control_to_send_control));
                        selection.setSelected(true);
                        listDate.set(k, selection);
                    }
                }
            }
            /*else {
                imgCompilationOverrun.setBackgroundColor(Color.TRANSPARENT);
            }*/
            else if (arrayValue.get(i).equalsIgnoreCase(getString(R.string.started_project_task_control_to_send_control))) {
                imgStartedProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isStartedProject = true;
                for (int k = 0; k < listDate.size(); k++) {
                    if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.started_project_task_control_to_send_control))) {
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.started_project_task_control_to_send_control));
                        selection.setSelected(true);
                        listDate.set(k, selection);
                    }
                }
            }
            /*else {
                imgStartedProject.setBackgroundColor(Color.TRANSPARENT);
            }*/
            else if (arrayValue.get(i).equalsIgnoreCase(getString(R.string.complete_project_task_control_to_send_control))) {
                imgCompeletedProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isCompletedProject = true;
                for (int k = 0; k < listDate.size(); k++) {
                    if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.complete_project_task_control_to_send_control))) {
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.complete_project_task_control_to_send_control));
                        selection.setSelected(true);
                        listDate.set(k, selection);
                    }
                }
            }/*else {
                imgCompeletedProject.setBackgroundColor(Color.TRANSPARENT);
            }*/ else if (arrayValue.get(i).equalsIgnoreCase(getString(R.string.complete_today_control_to_send_control))) {
                imgCompleteToday.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isCompletedToday = true;
                for (int k = 0; k < listDate.size(); k++) {
                    if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.complete_today_control_to_send_control))) {
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.complete_today_control_to_send_control));
                        selection.setSelected(true);
                        listDate.set(k, selection);
                    }
                }
            }
            /*else {
                imgCompleteToday.setBackgroundColor(Color.TRANSPARENT);
            }*/
            else if (arrayValue.get(i).equalsIgnoreCase(getString(R.string.pending_project_task_control_to_send_control))) {
                imgPendingProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isPendingProject = true;
                for (int k = 0; k < listDate.size(); k++) {
                    if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.pending_project_task_control_to_send_control))) {
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.pending_project_task_control_to_send_control));
                        selection.setSelected(true);
                        listDate.set(k, selection);
                    }
                }
            }
            /*else {
                imgPendingProject.setBackgroundColor(Color.TRANSPARENT);
            }*/
            else if (arrayValue.get(i).equalsIgnoreCase(getString(R.string.passive_project_task_control_to_send_control))) {
                imgPassiveProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isPassiveProject = true;
                for (int k = 0; k < listDate.size(); k++) {
                    if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.passive_project_task_control_to_send_control))) {
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.passive_project_task_control_to_send_control));
                        selection.setSelected(true);
                        listDate.set(k, selection);
                    }
                }
            }/*else {
                imgPassiveProject.setBackgroundColor(Color.TRANSPARENT);
            }*/ else if (arrayValue.get(i).equalsIgnoreCase(getString(R.string.completed_in1week_control_to_send_control))) {
                imgCompletedWeek.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isCompletedWeek = true;
                for (int k = 0; k < listDate.size(); k++) {
                    if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.completed_in1week_control_to_send_control))) {
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.completed_in1week_control_to_send_control));
                        selection.setSelected(true);
                        listDate.set(k, selection);
                    }
                }
            }/*else {
                imgCompletedWeek.setBackgroundColor(Color.TRANSPARENT);
            }*/ else if (arrayValue.get(i).equalsIgnoreCase(getString(R.string.task_without_date_control_to_send_control))) {
                imgTaskWithoutDate.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isTaskWithoutDate = true;
                for (int k = 0; k < listDate.size(); k++) {
                    if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.task_without_date_control_to_send_control))) {
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.task_without_date_control_to_send_control));
                        selection.setSelected(true);
                        listDate.set(k, selection);
                    }
                }
            }
            /*else {
                imgTaskWithoutDate.setBackgroundColor(Color.TRANSPARENT);
            }*/
            else if (arrayValue.get(i).equalsIgnoreCase(getString(R.string.canceled_project_task_control_to_send_control))) {
                imgCanceledProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isCanceledProject = true;
                for (int k = 0; k < listDate.size(); k++) {
                    if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.canceled_project_task_control_to_send_control))) {
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.canceled_project_task_control_to_send_control));
                        selection.setSelected(true);
                        listDate.set(k, selection);
                    }
                }
            }
            /*else {
                imgCanceledProject.setBackgroundColor(Color.TRANSPARENT);
            }*/
        }
    }

    public void showStatusFilterSettedFromService(ArrayList<String> arrayValue) {
        boolean flag = false;
        for (int i = 0; i < arrayValue.size(); i++) {
            if (arrayValue.get(i).equalsIgnoreCase(getString(R.string.notice_arrow_control_to_send_control))) {
                imgNoticeArrow.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isNoticeArrow = true;
                for (int k = 0; k < listStatus.size(); k++) {
                    if (listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.notice_arrow_control_to_send_control))) {
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.notice_arrow_control_to_send_control));
                        selection.setSelected(true);
                        listStatus.set(k, selection);
                    }
                }
            }
            /*else {
                imgNoticeArrow.setBackgroundColor(Color.TRANSPARENT);
            }*/

            else if (arrayValue.get(i).equalsIgnoreCase(getString(R.string.reshow_control_to_send_control))) {
                imgReshow.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isReshow = true;
                for (int k = 0; k < listStatus.size(); k++) {
                    if (listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.reshow_control_to_send_control))) {
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.reshow_control_to_send_control));
                        selection.setSelected(true);
                        listStatus.set(k, selection);
                    }
                }
            }
            /*else {
                imgReshow.setBackgroundColor(Color.TRANSPARENT);
            }*/

            else if (arrayValue.get(i).equalsIgnoreCase(getString(R.string.limit_overrun_control_to_send_control))) {
                imgLimitoverrun.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isLimitOverrun = true;
                for (int k = 0; k < listStatus.size(); k++) {
                    if (listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.limit_overrun_control_to_send_control))) {
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.limit_overrun_control_to_send_control));
                        selection.setSelected(true);
                        listStatus.set(k, selection);
                    }
                }
            }/*else {
                imgLimitoverrun.setBackgroundColor(Color.TRANSPARENT);
            }*/ else if (arrayValue.get(i).equalsIgnoreCase(getString(R.string.critical_control_to_send_control))) {
                imgCritical.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isCritical = true;
                for (int k = 0; k < listStatus.size(); k++) {
                    if (listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.critical_control_to_send_control))) {
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.critical_control_to_send_control));
                        selection.setSelected(true);
                        listStatus.set(k, selection);
                    }
                }
            }
            /*else {
                imgCritical.setBackgroundColor(Color.TRANSPARENT);
            }*/

            else if (arrayValue.get(i).equalsIgnoreCase(getString(R.string.requiring_clarification_control_to_send_control))) {
                imgRequiringClarification.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isRequiringClarification = true;
                for (int k = 0; k < listStatus.size(); k++) {
                    if (listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.requiring_clarification_control_to_send_control))) {
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.requiring_clarification_control_to_send_control));
                        selection.setSelected(true);
                        listStatus.set(k, selection);
                    }
                }
            }
            /*else {
                imgRequiringClarification.setBackgroundColor(Color.TRANSPARENT);
            }*/

        }
    }

    public void showTrafficFilterSettedFromService(ArrayList<String> arrayValue) {
        boolean flag = false;
        for (int i = 0; i < arrayValue.size(); i++) {
            if (arrayValue.get(i).equalsIgnoreCase(getString(R.string.traffic_yellow_control_to_send_control))) {
                imgTrafficYello.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                strTrafficRed = getResources().getString(R.string.traffic_yellow);
                isTrafficYello = true;
                for (int k = 0; k < listTraffic.size(); k++) {
                    if (listTraffic.get(k).getValue().equalsIgnoreCase(getString(R.string.traffic_yellow_control_to_send_control))) {
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.traffic_yellow_control_to_send_control));
                        selection.setSelected(true);
                        listTraffic.set(k, selection);
                    }
                }
            }
            /*else {
                imgTrafficYello.setBackgroundColor(Color.TRANSPARENT);
            }*/

            else if (arrayValue.get(i).equalsIgnoreCase(getString(R.string.traffic_red_control_to_send_control))) {
                imgTrafficRed.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                strTrafficRed = getResources().getString(R.string.traffic_red);
                isTrafficRed = true;
                for (int k = 0; k < listTraffic.size(); k++) {
                    if (listTraffic.get(k).getValue().equalsIgnoreCase(getString(R.string.traffic_red_control_to_send_control))) {
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.traffic_red_control_to_send_control));
                        selection.setSelected(true);
                        listTraffic.set(k, selection);
                    }
                }
            }
            /*else {
                imgTrafficRed.setBackgroundColor(Color.TRANSPARENT);
            }*/

            else if (arrayValue.get(i).equalsIgnoreCase(getString(R.string.traffic_green_control_to_send_control))) {
                imgTrafficGreen.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                isTrafficGreen = true;
                strTrafficRed = getResources().getString(R.string.traffic_green);
                for (int k = 0; k < listTraffic.size(); k++) {
                    if (listTraffic.get(k).getValue().equalsIgnoreCase(getString(R.string.traffic_green_control_to_send_control))) {
                        RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                        selection.setValue(getString(R.string.traffic_green_control_to_send_control));
                        selection.setSelected(true);
                        listTraffic.set(k, selection);
                    }
                }
            }/*else {
                imgTrafficGreen.setBackgroundColor(Color.TRANSPARENT);
            }*/
        }
    }

    /* this function return that DATE filter is seted or not */
    public boolean isDateFilterSettedFromService(ArrayList<String> arrayValue) {
        boolean flag = false;
        for (int i = 0; i < arrayValue.size(); i++) {
            for (int j = 0; j < arrayDate.length; j++) {
                if (arrayValue.get(i).equalsIgnoreCase(arrayDate[j])) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /* this function return that STATUS filter is seted or not */
    public boolean isStatusFilterSettedFromService(ArrayList<String> arrayValue) {
        boolean flag = false;
        for (int i = 0; i < arrayValue.size(); i++) {
            for (int j = 0; j < arrayStatus.length; j++) {
                if (arrayValue.get(i).equalsIgnoreCase(arrayStatus[j])) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /* this function return that TRAFFIC filter is seted or not */
    public boolean isTrafficFilterSettedFromService(ArrayList<String> arrayValue) {
        boolean flag = false;
        for (int i = 0; i < arrayValue.size(); i++) {
            for (int j = 0; j < arrayTraffic.length; j++) {
                if (arrayValue.get(i).equalsIgnoreCase(arrayTraffic[j])) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_refresh:
                getProfiles();
                break;
            case R.id.textviewToday:
                fireToday();
                break;
            case R.id.txt_date:
                showFilteredDialog(1, R.style.DialogAnimation);
                break;
            case R.id.txt_status:
                showFilteredDialog(2, R.style.DialogAnimation);
                break;
            case R.id.txt_traffic_lights:
                showFilteredDialog(3, R.style.DialogAnimation);
                break;
            case R.id.txt_week_m:
                if (popupWindowWeek != null) {
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
                } else {
                /* this function will store data in arraylist to show in popup basen on condition */
                    addingDataInPopup(1);
                /* this will show popup */
                    showPopupWindow(1);
                    isPopupDate = true;
                }
                break;
            case R.id.txt_default_profile:
                if (popupWindowProfile != null) {
                    if (popupWindowProfile.isShowing()) {
                        if (popupWindowProfile != null) {
                            popupWindowProfile.dismiss();
                            isPopupDefaultProfile = false;
                        }
                    } else {
                /* this function will store data in arraylist to show in popup basen on condition */
                        //addingDataInPopup(2);
                /* this will show popup */
                        showPopupWindow(2);
                        isPopupDefaultProfile = true;
                    }
                } else {
                    showPopupWindow(2);
                    isPopupDefaultProfile = true;
                }
                break;
            case R.id.txt_filter:
                filterpopup();

                break;
            case R.id.txt_save_setting:
                if(preferences.isDemoUser()){
                    GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.access_denied_title),
                            getString(R.string.profile_update_disable));
                }else {
                    strGeneralFiltertoSendserver = getGeneralFilterJson();
                    if (arraylistRibbonFilterSettedMerged.size() > 0) {
                        strRibbonFiltertoSendserver = TextUtils.join(",", arraylistRibbonFilterSettedMerged);
                    }
                    saveSettingService();
                }
                break;
        }
    }

    //    this method shows filter dilaog based on condition
    public void showFilteredDialog(int pos, int animationSource) {
        /* 1 is for showing date filter dialog*/
        if (pos == 1) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.custom_dialog_date);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            LinearLayout linearMain = (LinearLayout) dialog.findViewById(R.id.linear_date_dialog_main);
            GlobalClass.setupTypeface(linearMain, GlobalClass.fontRegular);
            txtdatedheder = (TextView) dialog.findViewById(R.id.txt_dialog_date);
            txtcomplition = (TextView) dialog.findViewById(R.id.txt_completion_overrun);
            txtstartproject = (TextView) dialog.findViewById(R.id.txt_started_project);
            txtcompltetoday = (TextView) dialog.findViewById(R.id.txt_complete_today);
            txtcompleteproject = (TextView) dialog.findViewById(R.id.txt_complete_project);
            txtpendingproject = (TextView) dialog.findViewById(R.id.txt_pending_project);
            txtpassiveproject = (TextView) dialog.findViewById(R.id.txt_passive_project);
            txtcompltein1week = (TextView) dialog.findViewById(R.id.txt_complete_in_week);
            txttaskwithoutdate = (TextView) dialog.findViewById(R.id.txt_task_without_date);
            txtcanceledprojecttask = (TextView) dialog.findViewById(R.id.txt_cancel_task);
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

            if (arraylistRibbonFilterSettedMerged.size() > 0) {
                // String[] split = arraylistRibbonFilterSettedMerged.get(0).split(",");
                //if(split.length > 0){
                showDateFilterSettedFromService(arraylistRibbonFilterSettedMerged);
                //}
            }
            //getDateValuefromDataaseToShow();
            imgCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v == imgCancel) {
                        // Close dialog
                        dialog.dismiss();
                        //setTextviewBold(textviewDateDialog,getString(R.string.DateLightFilter));
                        setTextViewStyleForDateTextView(arraylistRibbonFilterSettedMerged);
                    }
                }
            });
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v == buttonOk) {
                        setTextViewStyleForDateTextView(arraylistRibbonFilterSettedMerged);
                        boolean shouldFireFilter = false;
                        StringBuilder commaSeparatedValue = new StringBuilder();
                        for (int i = 0; i < listDate.size(); i++) {
                            if (listDate.get(i).isSelected()) {

                                commaSeparatedValue.append(listDate.get(i).getValue());
                                if (i != listDate.size() - 1) {
                                    commaSeparatedValue.append(",");
                                }

                                if (arraylistRibbonFilterSettedFromServer.contains(listDate.get(i).getValue())) {
                                } else {
                                    if (arraylistRibbonFilterSettedFromLocal.size() > 0) {
                                        if (!arraylistRibbonFilterSettedFromLocal.contains(listDate.get(i).getValue())) {
                                            arraylistRibbonFilterSettedFromLocal.add(listDate.get(i).getValue());
                                        }

                                    } else {
                                        arraylistRibbonFilterSettedFromLocal.add(listDate.get(i).getValue());
                                    }
                                }

                            } else {
                                if (arraylistRibbonFilterSettedFromServer.contains(listDate.get(i).getValue())) {
                                    arraylistRibbonFilterSettedFromServer.remove(listDate.get(i).getValue());
                                } else {
                                    if (arraylistRibbonFilterSettedFromLocal.size() > 0) {
                                        if (arraylistRibbonFilterSettedFromLocal.contains(listDate.get(i).getValue())) {
                                            arraylistRibbonFilterSettedFromLocal.remove(listDate.get(i).getValue());
                                        }

                                    }
                                }
                            }
                        }
                        List<String> list = new ArrayList<String>();
                        List<String> listServer = new ArrayList<String>();
                        String valueComma = "";
                        String valueCommaSever = "";
                        if (arraylistRibbonFilterSettedFromLocal.size() > 0) {
                            valueComma = TextUtils.join(",", arraylistRibbonFilterSettedFromLocal);
                            list = new ArrayList<String>(Arrays.asList(valueComma.split(" , ")));
                        }
                        if (arraylistRibbonFilterSettedFromServer.size() > 0) {
                            valueCommaSever = TextUtils.join(",", arraylistRibbonFilterSettedFromServer);
                            listServer = new ArrayList<String>(Arrays.asList(valueCommaSever.split(" , ")));
                        }
                        String strRibbonFilterLocal = "";
                        String strRibbonFilterServer = "";
                        if (list.size() > 0 && !valueComma.equalsIgnoreCase("")) {
                            //Gson gson = new Gson();
                            strRibbonFilterLocal = TextUtils.join(",", list);
                        }
                        if (listServer.size() > 0 && !valueCommaSever.equalsIgnoreCase("")) {
                            //Gson gson = new Gson();
                            strRibbonFilterServer = TextUtils.join(",", listServer);
                        }
                        if (!shouldFireFilter && preferences.getLastDateFilterString().equalsIgnoreCase("")) {
                            textviewDateDialog.setTypeface(GlobalClass.fontLight);
                            dialog.dismiss();
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
                                    preferences.getUserID(), preferences.getLanguage(), strRibbonFilterLocal);
                            dbHandler.updateRibbonFilterFromServerSide(preferences.getProfileId(),
                                    preferences.getUserID(), preferences.getLanguage(), strRibbonFilterServer);
                            //}

                            //dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.DateLightFilter));
                            //setTextviewBold(textviewDateDialog,getString(R.string.DateLightFilter));
                            preferences.saveLastDateFilterString(commaSeparatedValue.toString());
                            fireGeneralAndRibbonFilter();
                        } else if (commaSeparatedValue.toString().equalsIgnoreCase(preferences.getLastDateFilterString())) {
                            //dbHandler.updateProfileAttribute(strRibbonFiltertoSendserver,preferences.getProfileId());
                            if(dialog != null){
                                dialog.dismiss();
                            }
                            if (shouldFireFilter) {
                                textviewDateDialog.setTypeface(GlobalClass.fontBold);
                            } else {
                                textviewDateDialog.setTypeface(GlobalClass.fontBold);
                            }
                            preferences.saveLastDateFilterString(commaSeparatedValue.toString());
                        } else {
                            preferences.saveIsProgressShow("yes");

                            dbHandler.updateRibbonFilterFromLocalSide(preferences.getProfileId(),
                                    preferences.getUserID(), preferences.getLanguage(), strRibbonFilterLocal);
                            dbHandler.updateRibbonFilterFromServerSide(preferences.getProfileId(),
                                    preferences.getUserID(), preferences.getLanguage(), strRibbonFilterServer);
                            updateProfileInPreferences(strRibbonFilterLocal);
                            updateProfileInPreferencesforServer(strRibbonFilterServer);
                            //dbHandler.updateProfileAttribute(mAttributeValue,preferences.getProfileId());
                            //dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.DateLightFilter));
                            //dbHandler.addGeneralFilterInfo(getString(R.string.DateLightFilter),finalCommaseperated,"","",
                            //preferences.getUserID(),preferences.getProfileId());
                            dialog.dismiss();

                            //setTextviewBold(textviewDateDialog,getString(R.string.DateLightFilter));
                            if (preferences.getCurrentFiredFilter().equalsIgnoreCase("7") ||
                                    preferences.getCurrentFiredFilter().equalsIgnoreCase("8")) {
                                preferences.saveCurrentFiredFilter("7");
                            } else {
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
        else if (pos == 2) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.custom_dialog_status);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            LinearLayout linearMain = (LinearLayout) dialog.findViewById(R.id.linear_status_dialog_main);
            GlobalClass.setupTypeface(linearMain, GlobalClass.fontRegular);

            txtstatusheder = (TextView) dialog.findViewById(R.id.txt_status_heading);
            txtnoticearrow = (TextView) dialog.findViewById(R.id.txt_notice_arrow);
            txtreshow = (TextView) dialog.findViewById(R.id.txt_reshow);
            txtlimitoverun = (TextView) dialog.findViewById(R.id.txt_limit_overrun);
            txtcritical = (TextView) dialog.findViewById(R.id.txt_critical);
            txtrequring = (TextView) dialog.findViewById(R.id.txt_requring);

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
            if (arraylistRibbonFilterSettedMerged.size() > 0) {
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
                        dialog.dismiss();
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
                        boolean shouldFireFilter = false;
                        StringBuilder commaSeparatedValue = new StringBuilder();
                        for (int i = 0; i < listStatus.size(); i++) {
                            if (listStatus.get(i).isSelected()) {

                                commaSeparatedValue.append(listStatus.get(i).getValue());
                                if (i != listStatus.size() - 1) {
                                    commaSeparatedValue.append(",");
                                }

                                if (arraylistRibbonFilterSettedFromServer.contains(listStatus.get(i).getValue())) {
                                } else {
                                    if (arraylistRibbonFilterSettedFromLocal.size() > 0) {
                                        if (!arraylistRibbonFilterSettedFromLocal.contains(listStatus.get(i).getValue())) {
                                            arraylistRibbonFilterSettedFromLocal.add(listStatus.get(i).getValue());
                                        }

                                    } else {
                                        arraylistRibbonFilterSettedFromLocal.add(listStatus.get(i).getValue());
                                    }
                                }

                            } else {
                                if (arraylistRibbonFilterSettedFromServer.contains(listStatus.get(i).getValue())) {
                                    arraylistRibbonFilterSettedFromServer.remove(listStatus.get(i).getValue());
                                } else {
                                    if (arraylistRibbonFilterSettedFromLocal.size() > 0) {
                                        if (arraylistRibbonFilterSettedFromLocal.contains(listStatus.get(i).getValue())) {
                                            arraylistRibbonFilterSettedFromLocal.remove(listStatus.get(i).getValue());
                                        }

                                    }
                                }
                            }
                        }

                        List<String> list = new ArrayList<String>();
                        List<String> listServer = new ArrayList<String>();
                        String valueComma = "";
                        String valueCommaSever = "";
                        if (arraylistRibbonFilterSettedFromLocal.size() > 0) {
                            valueComma = TextUtils.join(",", arraylistRibbonFilterSettedFromLocal);
                            list = new ArrayList<String>(Arrays.asList(valueComma.split(" , ")));
                        }
                        if (arraylistRibbonFilterSettedFromServer.size() > 0) {
                            valueCommaSever = TextUtils.join(",", arraylistRibbonFilterSettedFromServer);
                            listServer = new ArrayList<String>(Arrays.asList(valueCommaSever.split(" , ")));
                        }
                        String strRibbonFilterLocal = "";
                        String strRibbonFilterServer = "";
                        if (list.size() > 0 && !valueComma.equalsIgnoreCase("")) {
                            //Gson gson = new Gson();
                            strRibbonFilterLocal = TextUtils.join(",", list);
                        }
                        if (listServer.size() > 0 && !valueCommaSever.equalsIgnoreCase("")) {
                            //Gson gson = new Gson();
                            strRibbonFilterServer = TextUtils.join(",", listServer);
                        }

                        if (!shouldFireFilter && preferences.getLastStatusilterString().equalsIgnoreCase("")) {
                            textviewStatusDialog.setTypeface(GlobalClass.fontLight);
                            if(preferences.getLastStatusilterString().equalsIgnoreCase(commaSeparatedValue.toString())){
                            }else {
                                preferences.saveCurrentFiredFilter("7");
                                /*if (preferences.getCurrentFiredFilter().equalsIgnoreCase("7")) {
                                } else {
                                    preferences.saveCurrentFiredFilter("6");
                                }*/
                            }
                            dialog.dismiss();
                            preferences.saveIsProgressShow("yes");
                            updateProfileInPreferences(strRibbonFilterLocal);
                            updateProfileInPreferencesforServer(strRibbonFilterServer);
                            dbHandler.updateRibbonFilterFromLocalSide(preferences.getProfileId(),
                                    preferences.getUserID(), preferences.getLanguage(), strRibbonFilterLocal);
                            dbHandler.updateRibbonFilterFromServerSide(preferences.getProfileId(),
                                    preferences.getUserID(), preferences.getLanguage(), strRibbonFilterServer);

                            //dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.StatusLightFilter));
                            //setTextviewBold(textviewStatusDialog,getString(R.string.StatusLightFilter));

                            preferences.saveLastStatusFilterString(commaSeparatedValue.toString());
                            fireGeneralAndRibbonFilter();
                        } else if (commaSeparatedValue.toString().equalsIgnoreCase(preferences.getLastStatusilterString())) {
                            dialog.dismiss();
                            if (shouldFireFilter) {
                                textviewStatusDialog.setTypeface(GlobalClass.fontBold);
                            } else {
                                textviewStatusDialog.setTypeface(GlobalClass.fontBold);
                            }
                            preferences.saveLastStatusFilterString(commaSeparatedValue.toString());
                        } else {
                            preferences.saveIsProgressShow("yes");
                            updateProfileInPreferences(strRibbonFilterLocal);
                            updateProfileInPreferencesforServer(strRibbonFilterServer);
                            dbHandler.updateRibbonFilterFromLocalSide(preferences.getProfileId(),
                                    preferences.getUserID(), preferences.getLanguage(), strRibbonFilterLocal);
                            dbHandler.updateRibbonFilterFromServerSide(preferences.getProfileId(),
                                    preferences.getUserID(), preferences.getLanguage(), strRibbonFilterServer);
                            //dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.StatusLightFilter));
                            //dbHandler.addGeneralFilterInfo(getString(R.string.StatusLightFilter),finalCommaseperated,"","",
                            //preferences.getUserID(),preferences.getProfileId());
                            dialog.dismiss();
                            //setTextviewBold(textviewStatusDialog,getString(R.string.StatusLightFilter));

                            if (preferences.getCurrentFiredFilter().equalsIgnoreCase("7") ||
                                    preferences.getCurrentFiredFilter().equalsIgnoreCase("8")) {
                                preferences.saveCurrentFiredFilter("7");
                            } else {
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
        else if (pos == 3) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.custom_dialog_trafficlights);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            LinearLayout linearMain = (LinearLayout) dialog.findViewById(R.id.linear_traffic_dialog_main);
            GlobalClass.setupTypeface(linearMain, GlobalClass.fontRegular);

            txttraffichheder = (TextView) dialog.findViewById(R.id.txttraffic_light_heading);
            txtred = (TextView) dialog.findViewById(R.id.txt_red);
            txtyellow = (TextView) dialog.findViewById(R.id.txt_yellow);
            txtgreen = (TextView) dialog.findViewById(R.id.txt_green);

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
            if (arraylistRibbonFilterSettedMerged.size() > 0) {
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
                        dialog.dismiss();
                        setTextViewStyleForTrafficTextView(arraylistRibbonFilterSettedMerged);
                        //setTextviewBold(textviewTrafficLightDialog,getString(R.string.TrafficLightFilter));
                    }
                }
            });
            buttonTrafficOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    setTextViewStyleForTrafficTextView(arraylistRibbonFilterSettedMerged);
                    boolean shouldFireFilter = false;
                    StringBuilder commaSeparatedValue = new StringBuilder();
                    for (int i = 0; i < listTraffic.size(); i++) {
                        if (listTraffic.get(i).isSelected()) {

                            commaSeparatedValue.append(listTraffic.get(i).getValue());
                            if (i != listTraffic.size() - 1) {
                                commaSeparatedValue.append(",");
                            }

                            if (arraylistRibbonFilterSettedFromServer.contains(listTraffic.get(i).getValue())) {
                            } else {
                                if (arraylistRibbonFilterSettedFromLocal.size() > 0) {
                                    if (!arraylistRibbonFilterSettedFromLocal.contains(listTraffic.get(i).getValue())) {
                                        arraylistRibbonFilterSettedFromLocal.add(listTraffic.get(i).getValue());
                                    }

                                } else {
                                    arraylistRibbonFilterSettedFromLocal.add(listTraffic.get(i).getValue());
                                }
                            }

                        } else {
                            if (arraylistRibbonFilterSettedFromServer.contains(listTraffic.get(i).getValue())) {
                                arraylistRibbonFilterSettedFromServer.remove(listTraffic.get(i).getValue());
                            } else {
                                if (arraylistRibbonFilterSettedFromLocal.size() > 0) {
                                    if (arraylistRibbonFilterSettedFromLocal.contains(listTraffic.get(i).getValue())) {
                                        arraylistRibbonFilterSettedFromLocal.remove(listTraffic.get(i).getValue());
                                    }

                                }
                            }
                        }
                    }
                    List<String> list = new ArrayList<String>();
                    List<String> listServer = new ArrayList<String>();
                    String valueComma = "";
                    String valueCommaSever = "";
                    if (arraylistRibbonFilterSettedFromLocal.size() > 0) {
                        valueComma = TextUtils.join(",", arraylistRibbonFilterSettedFromLocal);
                        list = new ArrayList<String>(Arrays.asList(valueComma.split(" , ")));
                    }
                    if (arraylistRibbonFilterSettedFromServer.size() > 0) {
                        valueCommaSever = TextUtils.join(",", arraylistRibbonFilterSettedFromServer);
                        listServer = new ArrayList<String>(Arrays.asList(valueCommaSever.split(" , ")));
                    }
                    String strRibbonFilterLocal = "";
                    String strRibbonFilterServer = "";
                    if (list.size() > 0 && !valueComma.equalsIgnoreCase("")) {
                        //Gson gson = new Gson();
                        strRibbonFilterLocal = TextUtils.join(",", list);
                    }
                    if (listServer.size() > 0 && !valueCommaSever.equalsIgnoreCase("")) {
                        //Gson gson = new Gson();
                        strRibbonFilterServer = TextUtils.join(",", listServer);
                    }
                    //String finalCommaseperated = strTrafficRed+","+strTrafficGreen+","+strTrafficYello;
                    if (!shouldFireFilter && preferences.getLastTrafficFilterString().equalsIgnoreCase("")) {
                        textviewTrafficLightDialog.setTypeface(GlobalClass.fontLight);
                        dialog.dismiss();
                        if(preferences.getLastTrafficFilterString().equalsIgnoreCase(commaSeparatedValue.toString())){

                        }else {
                            preferences.saveCurrentFiredFilter("7");
                            /*if (preferences.getCurrentFiredFilter().equalsIgnoreCase("7")) {
                            } else {
                                preferences.saveCurrentFiredFilter("6");
                            }*/
                        }
                        preferences.saveLastTrafficFilterString(commaSeparatedValue.toString());
                        preferences.saveIsProgressShow("yes");

                        updateProfileInPreferences(strRibbonFilterLocal);
                        updateProfileInPreferencesforServer(strRibbonFilterServer);
                        dbHandler.updateRibbonFilterFromLocalSide(preferences.getProfileId(),
                                preferences.getUserID(), preferences.getLanguage(), strRibbonFilterLocal);
                        dbHandler.updateRibbonFilterFromServerSide(preferences.getProfileId(),
                                preferences.getUserID(), preferences.getLanguage(), strRibbonFilterServer);

                        //dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.TrafficLightFilter));
                        //setTextviewBold(textviewTrafficLightDialog,getString(R.string.TrafficLightFilter));

                        preferences.saveLastTrafficFilterString(commaSeparatedValue.toString());
                        fireGeneralAndRibbonFilter();
                    } else if (commaSeparatedValue.toString().equalsIgnoreCase(preferences.getLastTrafficFilterString())) {
                        dialog.dismiss();
                        if (shouldFireFilter) {
                            textviewTrafficLightDialog.setTypeface(GlobalClass.fontBold);
                        } else {
                            textviewTrafficLightDialog.setTypeface(GlobalClass.fontBold);
                        }
                        preferences.saveLastTrafficFilterString(commaSeparatedValue.toString());
                    } else {
                        updateProfileInPreferences(strRibbonFilterLocal);
                        updateProfileInPreferencesforServer(strRibbonFilterServer);
                        dbHandler.updateRibbonFilterFromLocalSide(preferences.getProfileId(),
                                preferences.getUserID(), preferences.getLanguage(), strRibbonFilterLocal);
                        dbHandler.updateRibbonFilterFromServerSide(preferences.getProfileId(),
                                preferences.getUserID(), preferences.getLanguage(), strRibbonFilterServer);

                        preferences.saveIsProgressShow("yes");
                        //dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.TrafficLightFilter));
                        //dbHandler.addGeneralFilterInfo(getString(R.string.TrafficLightFilter),finalCommaseperated,"","",
                        //preferences.getUserID(),preferences.getProfileId());
                        dialog.dismiss();
                        //setTextviewBold(textviewTrafficLightDialog,getString(R.string.TrafficLightFilter));
                        if (preferences.getCurrentFiredFilter().equalsIgnoreCase("7") ||
                                preferences.getCurrentFiredFilter().equalsIgnoreCase("8")) {
                            preferences.saveCurrentFiredFilter("7");
                        } else {
                            preferences.saveCurrentFiredFilter("6");
                        }
                        preferences.saveLastTrafficFilterString(commaSeparatedValue.toString());
                        fireGeneralAndRibbonFilter();
                    }
                }
            });
        }
        if (pos == 4) {
            dialogCreateProfile = new Dialog(getActivity());
            dialogCreateProfile.setContentView(R.layout.custom_dialog_addprofile);
            dialogCreateProfile.setCanceledOnTouchOutside(true);
            dialogCreateProfile.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            LinearLayout linearMain = (LinearLayout) dialogCreateProfile.findViewById(R.id.linear_add_profile_main);
            GlobalClass.setupTypeface(linearMain, GlobalClass.fontRegular);
            dialogCreateProfile.getWindow().setLayout(900, 600);
            txtaddprofile = (TextView) dialogCreateProfile.findViewById(R.id.addnewprofile);
            final Button btnOk = (Button) dialogCreateProfile.findViewById(R.id.btnOk);
            btnOk.setEnabled(false);
            btnOk.setTextColor(getResources().getColor(R.color.gray));
            Button btnCancel = (Button) dialogCreateProfile.findViewById(R.id.btnCancel);
            final EditText editTextProfile = (EditText) dialogCreateProfile.findViewById(R.id.edittext_profile_name);
            editTextProfile.setMaxWidth(editTextProfile.getWidth());
            editTextProfile.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        btnOk.setEnabled(true);
                        btnOk.setTextColor(getResources().getColor(R.color.textview_color_blue));
                    } else {
                        btnOk.setEnabled(false);
                        btnOk.setTextColor(getResources().getColor(R.color.gray));
                    }
                }
            });
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dialogCreateProfile != null){
                        dialogCreateProfile.dismiss();
                    }
                    if(preferences.isDemoUser()){
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.access_denied_title),
                                getString(R.string.profile_create_disable));
                    }else {
                        // call add profile service here
                        strProfileName = editTextProfile.getText().toString();
                        if (strProfileName.equalsIgnoreCase("")) {
                            //GlobalClass.showToast(getActivity(), "Please enter profile name");
                            GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.profile_name),
                                    getString(R.string.please_enter_profile_name));
                        } else {
                            if (GlobalClass.isNetworkAvailable(getActivity())) {
                                preferences.saveIsProgressShow("yes");
                                createProfile();
                            } else {
                                //GlobalClass.showToast(getActivity(), getString(R.string.please_check_internet_connection));
                                GlobalClass.showCustomDialogInternet(getActivity());
                            }

                        }
                    }

                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dialogCreateProfile != null){
                        dialogCreateProfile.dismiss();
                    }

                }
            });

            editTextProfile.setHint(getString(R.string.profile_name));
            setLanguageAddProfile(preferences.getLanguage());
            dialogCreateProfile.getWindow().getAttributes().windowAnimations = animationSource;
            dialogCreateProfile.show();
        }
    }

    public void updateProfileInPreferences(String valueToChange) {
        if (arraylistProfileToSetasCurrent.size() > 0) {
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

    public void updateProfileInPreferencesforServer(String valueToChange) {
        if (arraylistProfileToSetasCurrent.size() > 0) {
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
    public void initializeDateDialogControl(Dialog dialog) {
        imgCompilationOverrun = (ImageView) dialog.findViewById(R.id.img_completion_overrun);
        imgStartedProject = (ImageView) dialog.findViewById(R.id.img_started_project);
        imgCompeletedProject = (ImageView) dialog.findViewById(R.id.img_complete_project);
        imgCompleteToday = (ImageView) dialog.findViewById(R.id.img_complete_today);
        imgPendingProject = (ImageView) dialog.findViewById(R.id.img_pending_project);
        imgPassiveProject = (ImageView) dialog.findViewById(R.id.img_passive_project);
        imgCompletedWeek = (ImageView) dialog.findViewById(R.id.img_complete_in_week);
        imgTaskWithoutDate = (ImageView) dialog.findViewById(R.id.img_task_without_date);
        imgCanceledProject = (ImageView) dialog.findViewById(R.id.img_cancel_task);

        linearCompilationOverrun = (LinearLayout) dialog.findViewById(R.id.linear_competion_overrun);
        linearStartedProject = (LinearLayout) dialog.findViewById(R.id.linear_started_project);
        linearCompeletedProject = (LinearLayout) dialog.findViewById(R.id.linear_completed_project);
        linearCompleteToday = (LinearLayout) dialog.findViewById(R.id.linear_completed_today);
        linearPendingProject = (LinearLayout) dialog.findViewById(R.id.linear_pending_project);
        linearPassiveProject = (LinearLayout) dialog.findViewById(R.id.linear_passive_project);
        linearCompletedWeek = (LinearLayout) dialog.findViewById(R.id.linear_completed_week);
        linearTaskWithoutDate = (LinearLayout) dialog.findViewById(R.id.linear_task_without_date);
        linearCanceledProject = (LinearLayout) dialog.findViewById(R.id.linear_canceled_project);

        linearCompilationOverrun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isComplitionOverrun) {
                    imgCompilationOverrun.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isComplitionOverrun = true;
                    strCompilationOverrun = getResources().getString(R.string.completion_overrun);
                    for (int k = 0; k < listDate.size(); k++) {
                        if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.completion_overrun_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.completion_overrun_control_to_send_control));
                            selection.setSelected(true);
                            listDate.set(k, selection);
                        }
                    }
                } else {
                    imgCompilationOverrun.setBackgroundColor(Color.TRANSPARENT);
                    isComplitionOverrun = false;
                    strCompilationOverrun = "";
                    for (int k = 0; k < listDate.size(); k++) {
                        if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.completion_overrun_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.completion_overrun_control_to_send_control));
                            selection.setSelected(false);
                            listDate.set(k, selection);
                        }
                    }
                }

            }
        });
        linearStartedProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isStartedProject) {
                    imgStartedProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isStartedProject = true;
                    strStartedProject = getResources().getString(R.string.started_project_task);
                    for (int k = 0; k < listDate.size(); k++) {
                        if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.started_project_task_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.started_project_task_control_to_send_control));
                            selection.setSelected(true);
                            listDate.set(k, selection);
                        }
                    }
                } else {
                    imgStartedProject.setBackgroundColor(Color.TRANSPARENT);
                    isStartedProject = false;
                    strStartedProject = "";
                    for (int k = 0; k < listDate.size(); k++) {
                        if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.started_project_task_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.started_project_task_control_to_send_control));
                            selection.setSelected(false);
                            listDate.set(k, selection);
                        }
                    }
                }
            }
        });
        linearCompeletedProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCompletedProject) {
                    imgCompeletedProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isCompletedProject = true;
                    strCompletedProject = getResources().getString(R.string.complete_project_task);
                    for (int k = 0; k < listDate.size(); k++) {
                        if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.complete_project_task_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.complete_project_task_control_to_send_control));
                            selection.setSelected(true);
                            listDate.set(k, selection);
                        }
                    }
                } else {
                    imgCompeletedProject.setBackgroundColor(Color.TRANSPARENT);
                    isCompletedProject = false;
                    strCompletedProject = "";
                    for (int k = 0; k < listDate.size(); k++) {
                        if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.complete_project_task_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.complete_project_task_control_to_send_control));
                            selection.setSelected(false);
                            listDate.set(k, selection);
                        }
                    }
                }
            }
        });
        linearCompleteToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCompletedToday) {
                    imgCompleteToday.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isCompletedToday = true;
                    strCompletedToday = getResources().getString(R.string.complete_today);
                    for (int k = 0; k < listDate.size(); k++) {
                        if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.complete_today_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.complete_today_control_to_send_control));
                            selection.setSelected(true);
                            listDate.set(k, selection);
                        }
                    }
                } else {
                    imgCompleteToday.setBackgroundColor(Color.TRANSPARENT);
                    isCompletedToday = false;
                    strCompletedToday = "";
                    for (int k = 0; k < listDate.size(); k++) {
                        if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.complete_today_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.complete_today_control_to_send_control));
                            selection.setSelected(false);
                            listDate.set(k, selection);
                        }
                    }

                }
            }
        });
        linearPendingProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPendingProject) {
                    imgPendingProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isPendingProject = true;
                    strPendingProject = getResources().getString(R.string.pending_project_task);
                    for (int k = 0; k < listDate.size(); k++) {
                        if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.pending_project_task_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.pending_project_task_control_to_send_control));
                            selection.setSelected(true);
                            listDate.set(k, selection);
                        }
                    }
                } else {
                    imgPendingProject.setBackgroundColor(Color.TRANSPARENT);
                    strPendingProject = "";
                    isPendingProject = false;
                    for (int k = 0; k < listDate.size(); k++) {
                        if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.pending_project_task_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.pending_project_task_control_to_send_control));
                            selection.setSelected(false);
                            listDate.set(k, selection);
                        }
                    }
                }
            }
        });
        linearPassiveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPassiveProject) {
                    imgPassiveProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isPassiveProject = true;
                    strPassiveProject = getResources().getString(R.string.passive_project_task);
                    for (int k = 0; k < listDate.size(); k++) {
                        if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.passive_project_task_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.passive_project_task_control_to_send_control));
                            selection.setSelected(true);
                            listDate.set(k, selection);
                        }
                    }
                } else {
                    imgPassiveProject.setBackgroundColor(Color.TRANSPARENT);
                    isPassiveProject = false;
                    strPassiveProject = "";
                    for (int k = 0; k < listDate.size(); k++) {
                        if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.passive_project_task_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.passive_project_task_control_to_send_control));
                            selection.setSelected(false);
                            listDate.set(k, selection);
                        }
                    }
                }
            }
        });
        linearCompletedWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCompletedWeek) {
                    imgCompletedWeek.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isCompletedWeek = true;
                    strCompletedWeek = getResources().getString(R.string.completed_in1week);
                    for (int k = 0; k < listDate.size(); k++) {
                        if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.completed_in1week_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.completed_in1week_control_to_send_control));
                            selection.setSelected(true);
                            listDate.set(k, selection);
                        }
                    }
                } else {
                    imgCompletedWeek.setBackgroundColor(Color.TRANSPARENT);
                    isCompletedWeek = false;
                    strCompletedWeek = "";
                    for (int k = 0; k < listDate.size(); k++) {
                        if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.completed_in1week_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.completed_in1week_control_to_send_control));
                            selection.setSelected(false);
                            listDate.set(k, selection);
                        }
                    }
                }
            }
        });
        linearTaskWithoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTaskWithoutDate) {
                    imgTaskWithoutDate.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isTaskWithoutDate = true;
                    strTaskWithoutDate = getResources().getString(R.string.task_without_date);
                    for (int k = 0; k < listDate.size(); k++) {
                        if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.task_without_date_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.task_without_date_control_to_send_control));
                            selection.setSelected(true);
                            listDate.set(k, selection);
                        }
                    }
                } else {
                    imgTaskWithoutDate.setBackgroundColor(Color.TRANSPARENT);
                    isTaskWithoutDate = false;
                    strTaskWithoutDate = "";
                    for (int k = 0; k < listDate.size(); k++) {
                        if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.task_without_date_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.task_without_date_control_to_send_control));
                            selection.setSelected(false);
                            listDate.set(k, selection);
                        }
                    }
                }
            }
        });
        linearCanceledProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCanceledProject) {
                    imgCanceledProject.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isCanceledProject = true;
                    strCanceledProject = getResources().getString(R.string.canceled_project_task);
                    for (int k = 0; k < listDate.size(); k++) {
                        if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.canceled_project_task_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.canceled_project_task_control_to_send_control));
                            selection.setSelected(true);
                            listDate.set(k, selection);
                        }
                    }
                } else {
                    imgCanceledProject.setBackgroundColor(Color.TRANSPARENT);
                    isCanceledProject = false;
                    strCanceledProject = "";
                    for (int k = 0; k < listDate.size(); k++) {
                        if (listDate.get(k).getValue().equalsIgnoreCase(getString(R.string.canceled_project_task_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.canceled_project_task_control_to_send_control));
                            selection.setSelected(false);
                            listDate.set(k, selection);
                        }
                    }

                }
            }
        });
    }

    //    initialize status dialog
    public void initializeStatusDialogControl(Dialog dialog) {
        imgNoticeArrow = (ImageView) dialog.findViewById(R.id.img_notice_arrow);
        imgReshow = (ImageView) dialog.findViewById(R.id.img_started_status);
        imgLimitoverrun = (ImageView) dialog.findViewById(R.id.img_complate_project_status);
        imgCritical = (ImageView) dialog.findViewById(R.id.img_critical);
        imgRequiringClarification = (ImageView) dialog.findViewById(R.id.img_require);

        linearNoticeArrow = (LinearLayout) dialog.findViewById(R.id.linear_notice_arrow);
        linearReshow = (LinearLayout) dialog.findViewById(R.id.linear_reshow);
        linearLimitoverrun = (LinearLayout) dialog.findViewById(R.id.linear_limit_overrun);
        linearCritical = (LinearLayout) dialog.findViewById(R.id.linear_critical);
        linearRequiringClarification = (LinearLayout) dialog.findViewById(R.id.linear_require_clarification);

        linearNoticeArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNoticeArrow) {
                    imgNoticeArrow.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isNoticeArrow = true;
                    strNoticeArrow = getResources().getString(R.string.notice_arrow);
                    for (int k = 0; k < listStatus.size(); k++) {
                        if (listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.notice_arrow_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.notice_arrow_control_to_send_control));
                            selection.setSelected(true);
                            listStatus.set(k, selection);
                        }
                    }
                } else {
                    imgNoticeArrow.setBackgroundColor(Color.TRANSPARENT);
                    isNoticeArrow = false;
                    strNoticeArrow = "";
                    for (int k = 0; k < listStatus.size(); k++) {
                        if (listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.notice_arrow_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.notice_arrow_control_to_send_control));
                            selection.setSelected(false);
                            listStatus.set(k, selection);
                        }
                    }
                }

            }
        });
        linearReshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isReshow) {
                    imgReshow.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isReshow = true;
                    strReshow = getResources().getString(R.string.reshow);
                    for (int k = 0; k < listStatus.size(); k++) {
                        if (listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.reshow_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.reshow_control_to_send_control));
                            selection.setSelected(true);
                            listStatus.set(k, selection);
                        }
                    }
                } else {
                    imgReshow.setBackgroundColor(Color.TRANSPARENT);
                    isReshow = false;
                    strReshow = "";
                    for (int k = 0; k < listStatus.size(); k++) {
                        if (listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.reshow_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.reshow_control_to_send_control));
                            selection.setSelected(false);
                            listStatus.set(k, selection);
                        }
                    }
                }
            }
        });
        linearLimitoverrun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLimitOverrun) {
                    imgLimitoverrun.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isLimitOverrun = true;
                    strLimitoverrun = getResources().getString(R.string.limit_overrun);
                    for (int k = 0; k < listStatus.size(); k++) {
                        if (listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.limit_overrun_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.limit_overrun_control_to_send_control));
                            selection.setSelected(true);
                            listStatus.set(k, selection);
                        }
                    }
                } else {
                    imgLimitoverrun.setBackgroundColor(Color.TRANSPARENT);
                    isLimitOverrun = false;
                    strLimitoverrun = "";
                    for (int k = 0; k < listStatus.size(); k++) {
                        if (listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.limit_overrun_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.limit_overrun_control_to_send_control));
                            selection.setSelected(false);
                            listStatus.set(k, selection);
                        }
                    }
                }
            }
        });
        linearCritical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCritical) {
                    imgCritical.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isCritical = true;
                    strCritical = getResources().getString(R.string.critical);
                    for (int k = 0; k < listStatus.size(); k++) {
                        if (listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.critical_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.critical_control_to_send_control));
                            selection.setSelected(true);
                            listStatus.set(k, selection);
                        }
                    }
                } else {
                    imgCritical.setBackgroundColor(Color.TRANSPARENT);
                    isCritical = false;
                    strCritical = "";
                    for (int k = 0; k < listStatus.size(); k++) {
                        if (listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.critical_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.critical_control_to_send_control));
                            selection.setSelected(false);
                            listStatus.set(k, selection);
                        }
                    }
                }
            }
        });
        linearRequiringClarification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRequiringClarification) {
                    imgRequiringClarification.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isRequiringClarification = true;
                    strRequiringClarification = getResources().getString(R.string.requiring_clarification);
                    for (int k = 0; k < listStatus.size(); k++) {
                        if (listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.requiring_clarification_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.requiring_clarification_control_to_send_control));
                            selection.setSelected(true);
                            listStatus.set(k, selection);
                        }
                    }
                } else {
                    imgRequiringClarification.setBackgroundColor(Color.TRANSPARENT);
                    isRequiringClarification = false;
                    strRequiringClarification = "";
                    for (int k = 0; k < listStatus.size(); k++) {
                        if (listStatus.get(k).getValue().equalsIgnoreCase(getString(R.string.requiring_clarification_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.requiring_clarification_control_to_send_control));
                            selection.setSelected(false);
                            listStatus.set(k, selection);
                        }
                    }
                }
            }
        });
    }

    //    initialize traffic dilaog contol method
    public void initializeTrafficDialogControl(Dialog dialog) {
        imgTrafficRed = (ImageView) dialog.findViewById(R.id.img_traffic_red);
        imgTrafficGreen = (ImageView) dialog.findViewById(R.id.img_traffic_green);
        imgTrafficYello = (ImageView) dialog.findViewById(R.id.img_traffic_yellow);

        linearTrafficRed = (LinearLayout) dialog.findViewById(R.id.linear_red);
        linearTrafficGreen = (LinearLayout) dialog.findViewById(R.id.linear_green);
        linearTrafficYello = (LinearLayout) dialog.findViewById(R.id.linear_yellow);

        linearTrafficRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTrafficRed) {
                    imgTrafficRed.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isTrafficRed = true;
                    strTrafficRed = getResources().getString(R.string.traffic_red);
                    for (int k = 0; k < listTraffic.size(); k++) {
                        if (listTraffic.get(k).getValue().equalsIgnoreCase(getString(R.string.traffic_red_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.traffic_red_control_to_send_control));
                            selection.setSelected(true);
                            listTraffic.set(k, selection);
                        }
                    }
                } else {
                    imgTrafficRed.setBackgroundColor(Color.TRANSPARENT);
                    isTrafficRed = false;
                    strTrafficRed = "";
                    for (int k = 0; k < listTraffic.size(); k++) {
                        if (listTraffic.get(k).getValue().equalsIgnoreCase(getString(R.string.traffic_red_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.traffic_red_control_to_send_control));
                            selection.setSelected(false);
                            listTraffic.set(k, selection);
                        }
                    }
                }

            }
        });
        linearTrafficGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTrafficGreen) {
                    imgTrafficGreen.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isTrafficGreen = true;
                    strTrafficGreen = getResources().getString(R.string.traffic_green);
                    for (int k = 0; k < listTraffic.size(); k++) {
                        if (listTraffic.get(k).getValue().equalsIgnoreCase(getString(R.string.traffic_green_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.traffic_green_control_to_send_control));
                            selection.setSelected(true);
                            listTraffic.set(k, selection);
                        }
                    }
                } else {
                    imgTrafficGreen.setBackgroundColor(Color.TRANSPARENT);
                    isTrafficGreen = false;
                    strTrafficGreen = "";
                    for (int k = 0; k < listTraffic.size(); k++) {
                        if (listTraffic.get(k).getValue().equalsIgnoreCase(getString(R.string.traffic_green_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.traffic_green_control_to_send_control));
                            selection.setSelected(false);
                            listTraffic.set(k, selection);
                        }
                    }
                }
            }
        });
        linearTrafficYello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTrafficYello) {
                    imgTrafficYello.setBackgroundColor(getResources().getColor(R.color.grey_dashboard));
                    isTrafficYello = true;
                    strTrafficYello = getResources().getString(R.string.traffic_yellow);
                    for (int k = 0; k < listTraffic.size(); k++) {
                        if (listTraffic.get(k).getValue().equalsIgnoreCase(getString(R.string.traffic_yellow_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.traffic_yellow_control_to_send_control));
                            selection.setSelected(true);
                            listTraffic.set(k, selection);
                        }
                    }
                } else {
                    imgTrafficYello.setBackgroundColor(Color.TRANSPARENT);
                    isTrafficYello = false;
                    strTrafficYello = "";
                    for (int k = 0; k < listTraffic.size(); k++) {
                        if (listTraffic.get(k).getValue().equalsIgnoreCase(getString(R.string.traffic_yellow_control_to_send_control))) {
                            RibbonShowSelectionClass selection = new RibbonShowSelectionClass();
                            selection.setValue(getString(R.string.traffic_yellow_control_to_send_control));
                            selection.setSelected(false);
                            listTraffic.set(k, selection);
                        }
                    }
                }
            }
        });
    }

    // add static data in popup to show
    public void addingDataInPopup(int pos) {
        if (pos == 1) {
            String[] arraylistWeek = {getString(R.string.year), getString(R.string.quarter), getString(R.string.months),
                    getString(R.string.week_s), getString(R.string.week_m), getString(R.string.week_l), getString(R.string.days),
                    getString(R.string.hours), getString(R.string.minutes), getString(R.string.seconds),
                    getString(R.string.daynightshift)};
            String[] arraylistWeekToSend = {"Year", "Quarter", "Months", "Week S", "Week M", "Week L", "Days",
                    "Hours", "Minutes", "Seconds", "DayNightShift"};
            String[] arrayprojectDateOriginalvalue = {"ShowYear", "ShowQuarter", "ShowMonths", "ShowWeeks",
                    "ShowWeekM", "ShowWeekL", "ShowDays", "ShowHours", "ShowMinutes", "ShowSeconds", "ShowDayNightShift"};

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
        if (pos == 2) {
            if (arraylistProfile.size() > 0) {
            }
        }
    }

    //    showing popup window
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void showPopupWindow(final int popupNo) {
        if (popupNo == 1) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            final View popupView = layoutInflater.inflate(R.layout.popupview_project_week, null);
            popupWindowWeek = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            isTabletSize = getResources().getBoolean(R.bool.isTablet);
            if (isTabletSize) {
                popupWindowWeek.setWidth((GlobalClass.screenWidth(getContext()) * 30) / 100);
                popupWindowWeek.setHeight((GlobalClass.screenHeight(getContext()) * 50) / 100);
            } else {
                popupWindowWeek.setWidth((GlobalClass.screenWidth(getContext()) * 50) / 100);
                popupWindowWeek.setHeight((GlobalClass.screenHeight(getContext()) * 50) / 100);
            }

            popupWindowWeek.setOutsideTouchable(true);
            popupWindowWeek.showAsDropDown(textviewWeekDialog, 0 , 20, Gravity.RIGHT | Gravity.BOTTOM);
            ListView listviewWeeklist = (ListView) popupView.findViewById(R.id.listviewprojectdate);

            adapterWeek = new ProjectWeekAdapter(getActivity(), arraylistPopupViewforWeek);
            listviewWeeklist.setAdapter(adapterWeek);
            listviewWeeklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (arraylistProfileToSetasCurrent != null && arraylistProfileToSetasCurrent.size() > 0) {
                        arraylistProfileToSetasCurrent.get(0).setResolution(arraylistPopupViewforWeek.get(position).getName());
                        ProfileModelClass model = new ProfileModelClass();
                        model = arraylistProfileToSetasCurrent.get(0);
                        preferences.saveIsProgressShow("yes");
                        preferences.clearUserProfile();
                        preferences.saveUserProfile(new Gson().toJson(model));
                        preferences.saveProfileId(model.getProfileId());
                        if (arraylistProfile != null && arraylistProfile.size() > 0) {
                            for (int i = 0; i < arraylistProfile.size(); i++) {
                                if (arraylistProfile.get(i).getProfileId().equalsIgnoreCase(arraylistProfileToSetasCurrent.get(0).
                                        getProfileId())) {
                                    arraylistProfile.get(i).setResolution(arraylistProfileToSetasCurrent.get(0).getResolution());
                                }
                            }
                        }
                        dbHandler.updateProfile(arraylistPopupViewforWeek.get(position).getName(), arraylistProfileToSetasCurrent.get(0).
                                getProfileId());
                    }
                    if (adapterProfile != null) {
                        adapterProfile.notifyDataSetChanged();
                    }
                    isPopupDate = false;
                    popupWindowWeek.dismiss();
                    textviewWeekDialog.setText(arraylistPopupViewforWeek.get(position).getName());
                    fireResolution(arraylistPopupViewforWeek.get(position).getStrTosendControl());

                }
            });
        } else if (popupNo == 2) {
            LayoutInflater layoutInflater2 = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            final View popupViewProfile = layoutInflater2.inflate(R.layout.popupview_profile, null);
            popupWindowProfile = new PopupWindow(popupViewProfile, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            ///popupWindowProfile.setWidth((GlobalClass.screenWidth(getContext()) * 30) / 100);
            // popupWindowProfile.setHeight((GlobalClass.screenHeight(getContext()) * 40) / 100);
            popupWindowProfile.setOutsideTouchable(true);
            popupWindowProfile.showAsDropDown(textviewDefaultProfile,-10,20, Gravity.CENTER | Gravity.BOTTOM);

            textViewAddProfile = (TextView) popupViewProfile.findViewById(R.id.text_addprofile);
            textViewTitleProfile = (TextView) popupViewProfile.findViewById(R.id.textview_profile);

            textViewAddProfile.setTypeface(GlobalClass.fontRegular);
            textViewTitleProfile.setTypeface(GlobalClass.fontRegular);
            textViewTitleProfile.setText(getString(R.string.set_profile));
            LinearLayout linearAddProfile = (LinearLayout) popupViewProfile.findViewById(R.id.linear_add_profile);
            listviewProfile = (ListView) popupViewProfile.findViewById(R.id.listview_profile);
            arraylistProfile.clear();
            arraylistProfile = dbHandler.getAllUserProfile(preferences.getUserID(), preferences.getProfileId(),
                    preferences.getLanguage());
            adapterProfile = new ProfileAdapter(getActivity(), arraylistProfile);
            listviewProfile.setAdapter(adapterProfile);

            textViewAddProfile.setText(getString(R.string.add_new_profile));
            //textViewTitleProfile.setText(getString(R.string.title_profile));
            listviewProfile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (arraylistProfileToSetasCurrent != null && arraylistProfileToSetasCurrent.size() > 0) {

                        if (arraylistProfile.get(position).getProfileId().equalsIgnoreCase(
                                arraylistProfileToSetasCurrent.get(0).getProfileId())) {

                        } else {
                            arraylistProfileToSetasCurrent = new ArrayList<ProfileModelClass>();

                            /* here user wants to change profile that's why call get all profile*/
                            ProfileModelClass model = new ProfileModelClass();
                            model = arraylistProfile.get(position);
                            arraylistProfileToSetasCurrent.add(model);
                            preferences.saveIsProgressShow("yes");
                            preferences.clearUserProfile();
                            preferences.saveUserProfile(new Gson().toJson(model));
                            preferences.saveProfileId(model.getProfileId());
                            preferences.clearLastDateFilterString();
                            preferences.clearLastStatusilterString();
                            preferences.clearLastTrafficFilterString();
                            preferences.clearLastFolderfilterString();
                            preferences.clearLastTextFilterString();
                            preferences.clearLastUserDefinedFilterString();

                            preferences.clearResourcefilter();
                            preferences.clearFireResourcefilter();
                            preferences.clearLastFiredFilterAll();

                            textviewDefaultProfile.setText(arraylistProfileToSetasCurrent.get(0).getProfileName());
                            //textviewWeekDialog.setText(arraylistProfileToSetasCurrent.get(0).getResolution());
                            //getProfiles();
                            getProfileSingle();

                        }

                    }
                    popupWindowProfile.dismiss();
                    isPopupDefaultProfile = false;
                }
            });
            linearAddProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindowProfile.dismiss();
                    isPopupDefaultProfile = false;
                    showFilteredDialog(4, R.style.DialogAnimation);
                }
            });
            gestureListener = new SwipeGestureListener(getActivity());
            listviewProfile.setOnTouchListener(gestureListener);
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

    public void changeText(String someText) {
        Log.v("mylog", "changeText is called");
        //webview.loadUrl("javascript:document.getElementById('test1').innerHTML = '<strong>" + someText + "</strong>'");
    }

    public void getProfiles() {
        try {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());

            SpannableString ss = new SpannableString(getString(R.string.please_wait));
            ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new CustomTypefaceSpan("", GlobalClass.fontRegular), 0, ss.length(), 0);
            progressDialog.setMessage(ss);
            //progressDialog.show();

            GenericHelper helper = new GenericHelper(getActivity());
            Call<String> call = helper.getRetrofit().getProfiles(preferences.getLanguage());
            call.enqueue(new retrofit2.Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    Headers header = response.headers();
                    try {
                        if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 0) {
                            String attributName = "";
                            //String ColumnsDetail="";
                            //GlobalClass.showToast(LoginMobileActivity.this," 333333 success ");
                            String strResponse = response.body();
                            JSONObject obj = (JSONObject) new JSONTokener(strResponse).nextValue();
                            String status = obj.getString("Status");
                            if (status.equalsIgnoreCase("0")) {
                                JSONArray jsonArrayPayload = obj.getJSONArray("Payload");
                                if (jsonArrayPayload.length() > 0) {
                                    dbHandler.deleteAlluserProfile();
                                    dbHandler.deleteGeneralFilterDataWhileRefresh(preferences.getProfileId(), getString(R.string.TextFilter_text), preferences.getLanguage(), preferences.getUserID());
                                    dbHandler.deleteGeneralFilterDataWhileRefresh(preferences.getProfileId(), getString(R.string.DateFilter_text), preferences.getLanguage(), preferences.getUserID());
                                    dbHandler.deleteGeneralFilterDataWhileRefresh(preferences.getProfileId(), getString(R.string.UserDefindedFilterForNumber), preferences.getLanguage(), preferences.getUserID());
                                    dbHandler.deleteGeneralFilterDataWhileRefresh(preferences.getProfileId(), getString(R.string.UserDefinedFilter_text), preferences.getLanguage(), preferences.getUserID());
                                    for (int i = 0; i < jsonArrayPayload.length(); i++) {
                                        JSONObject objPayload = jsonArrayPayload.getJSONObject(i);

                                        //String ColumnsDetail = objSub.getString("ColsDetail");
                                        String ProfileId = objPayload.getString("Id");
                                        String Name = objPayload.getString("Name");
                                        String UserId = objPayload.getString("UserId");
                                        String ExpandedId = "";
                                        //String ExpandedId = objSub.getString("ExpandedIds");
                                        JSONArray jsonArrayAttribute = objPayload.getJSONArray("Attributes");

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
                                                } else if (attributName.equalsIgnoreCase("GeneralFilter")) {
                                                    attributeValue = objAttribute.getString("AttributeValue");
                                                    if (!attributeValue.equalsIgnoreCase("")) {
                                                        JSONObject objGeneralFilter = (JSONObject) new JSONTokener(attributeValue).nextValue();
                                                        if (objGeneralFilter.has(getString(R.string.folder_group_id))) {
                                                        } else {
                                                            if (dbHandler.getCursorCountForFolderFilter(ProfileId,
                                                                    getString(R.string.FolderFilter_text), preferences.getLanguage(), preferences.getUserID()) > 0) {
                                                                dbHandler.deleteGeneralFilterData(ProfileId, getString(R.string.FolderFilter_text),
                                                                        preferences.getLanguage(), preferences.getUserID());
                                                            }
                                                        }
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
                                                            if (key.equalsIgnoreCase(getString(R.string.folder_group_id))) {
                                                                if (dbHandler.getCursorCountForFolderFilter(ProfileId,
                                                                        getString(R.string.FolderFilter_text), preferences.getLanguage(), preferences.getUserID()) > 0) {
                                                                        /* there is already folder filter is fired in local that's why just update here*/
                                                                    ArrayList<GeneralFilterDataSet> arraylistFolderlist = dbHandler.getAllGeneralFilterData(preferences.getProfileId(), getString(R.string.FolderFilter_text), preferences.getLanguage(),
                                                                            preferences.getUserID());
                                                                    if (arraylistFolderlist.size() > 0) {
                                                                        List<String> list = GlobalClass.commaSeparetedtoArraylist(arraylistFolderlist.get(0).getValueLocal());
                                                                        List<String> listServer = GlobalClass.commaSeparetedtoArraylist(value);
                                                                        Set<String> newSet = null;
                                                                        if (list.size() > 0) {
                                                                            newSet = new HashSet<String>(list);
                                                                        }
                                                                        if (listServer.size() > 0) {
                                                                            if (newSet != null) {
                                                                                newSet.addAll(listServer);
                                                                            }
                                                                        }
                                                                        List<String> newList = null;
                                                                        if (newSet != null) {
                                                                            newList = new ArrayList<String>(newSet);
                                                                        }
                                                                        String joined = TextUtils.join(",", newList);
                                                                        dbHandler.updateGeneralFilter(ProfileId, getString(R.string.folder_group_id), getString(R.string.FolderFilter_text), joined, preferences.getLanguage(), preferences.getUserID());


                                                                    }
                                                                } else {
                                                                      /* no folder filter is fired that's why add new record here */
                                                                    dbHandler.addGeneralFilter(ProfileId, key, value, preferences.getLanguage(), type, preferences.getUserID(), value);
                                                                }


                                                            } else {
                                                                dbHandler.addGeneralFilter(ProfileId, key, value, preferences.getLanguage(), type, preferences.getUserID(), value);
                                                            }

                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        String mAttributeKey = "";
                                        String mAttributeValue = "";
                                        if (arraylistValue.size() > 0) {
                                            //Gson gson = new Gson();
                                            mAttributeKey = TextUtils.join(",", arraylistKey);
                                            mAttributeValue = TextUtils.join(",", arraylistValue);
                                        }

                                        /* adding all profile to database */
                                        dbHandler.addAllProfileToDB(ProfileId, Name, attributName,
                                                preferences.getUserID(), ExpandedId, preferences.getUserIdTemp(),
                                                preferences.getLanguage(), getResources().getString(R.string.weekm),
                                                mAttributeKey,mAttributeValue,mAttributeKey,mAttributeValue);

                                        /* adding default profile to database for table screen */
                                        dbHandler.updateRibbonFilterFromServerSide(ProfileId, preferences.getUserID(), preferences.getLanguage(), mAttributeValue);


                                    }
                                }
                            } else {
                                LogApp.e(" no response from service :  ", " response from service : ");
                                //progressDialog.dismiss();
                                //GlobalClass.showToast(getActivity(), getString(R.string.something_is_wrong));
                                GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.error_title),getString(R.string.something_is_wrong));
                            }
                            preferences.saveCurrentFiredFilter("6");
                            getAllProfilefromDatabase();
                            preferences.saveIsProgressShow("yes");
                            refreshControl();
                        } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1009) {
                                /* whenever access token is expire get 1009
                                from service and call here getNewdeviceToken servcie and replace onld one with new device token */
                            //updateAccessToken(4);
                            preferences.saveIsProgressShow("yes");
                            refreshControl();
                        } else {
                            preferences.saveIsProgressShow("yes");
                            refreshControl();
                            //progressDialog.dismiss();
                            //GlobalClass.showToast(activity,getString(R.string.something_is_wrong));
                        }
                    } catch (Exception e) {
                        preferences.saveIsProgressShow("yes");
                        refreshControl();
                        //progressDialog.dismiss();
                        //GlobalClass.showToast(activity,getString(R.string.something_is_wrong));
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    preferences.saveIsProgressShow("yes");
                    refreshControl();
                    //progressDialog.dismiss();
                    if (t instanceof IOException) {
                        //GlobalClass.showToast(getActivity(), getString(R.string.server_timeout_exception));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.server_timeout_exception)
                                ,getString(R.string.server_timeout_exception));
                    }
                    //GlobalClass.showToast(activity,getString(R.string.something_is_wrong));
                }
            });
        } catch (Exception e) {
            preferences.saveIsProgressShow("yes");
            refreshControl();
            LogApp.e(" while get proifle service : ", " in catch : " + e.toString());
        }
    }

    private void getProfileSingle() {
        try {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            //progressDialog.create();

            SpannableString ss = new SpannableString(getString(R.string.please_wait));
            ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new CustomTypefaceSpan("", GlobalClass.fontRegular), 0, ss.length(), 0);

            progressDialog.setMessage(ss);
            progressDialog.show();

            GenericHelper helper = new GenericHelper(getActivity());
            Call<String> call = helper.getRetrofit().callGetProfileFormID(preferences.getProfileId());
            call.enqueue(new retrofit2.Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    String body = response.body();
                    Headers header = response.headers();
                    if (body == null || body.equalsIgnoreCase("")) {
                        /* body is null that's why call new Access token service here */
                        updateAccessToken(1);
                    } else if (body != null || !body.equalsIgnoreCase("")) {
                        try {
                            if (header.get(getResources().getString(R.string.x_status)) != null &&
                                    Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 0) {
                                String attributName = "";
                                //String ColumnsDetail="";
                                //GlobalClass.showToast(LoginMobileActivity.this," 333333 success ");
                                String strResponse = response.body();
                                JSONObject obj = (JSONObject) new JSONTokener(strResponse).nextValue();
                                String status = obj.getString("Status");
                                if (status.equalsIgnoreCase("0")) {
                                    JSONObject jsonObjPayload = obj.getJSONObject("Payload");
                                    //if(jsonArrayPayload.length() > 0){
                                    dbHandler.deleteGeneralFilterDataWhileProfileChange(preferences.getProfileId(), getString(R.string.TextFilter_text), preferences.getLanguage(), preferences.getUserID());
                                    dbHandler.deleteGeneralFilterDataWhileProfileChange(preferences.getProfileId(), getString(R.string.DateFilter_text), preferences.getLanguage(), preferences.getUserID());
                                    dbHandler.deleteGeneralFilterDataWhileProfileChange(preferences.getProfileId(), getString(R.string.UserDefindedFilterForNumber), preferences.getLanguage(), preferences.getUserID());
                                    dbHandler.deleteGeneralFilterDataWhileProfileChange(preferences.getProfileId(), getString(R.string.UserDefinedFilter_text), preferences.getLanguage(), preferences.getUserID());
                                    //for (int i=0;i<jsonArrayPayload.length();i++){
                                    //JSONObject objPayload = jsonArrayPayload.getJSONObject(i);

                                    //String ColumnsDetail = objSub.getString("ColsDetail");
                                    String ProfileId = jsonObjPayload.getString("Id");
                                    String Name = jsonObjPayload.getString("Name");
                                    String UserId = jsonObjPayload.getString("UserId");
                                    String ExpandedId = "";
                                    //String ExpandedId = objSub.getString("ExpandedIds");
                                    JSONArray jsonArrayAttribute = jsonObjPayload.getJSONArray("Attributes");

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
                                            } else if (attributName.equalsIgnoreCase("GeneralFilter")) {
                                                attributeValue = objAttribute.getString("AttributeValue");
                                                if (!attributeValue.equalsIgnoreCase("")) {
                                                    JSONObject objGeneralFilter = (JSONObject) new JSONTokener(attributeValue).nextValue();
                                                    if (objGeneralFilter.has(getString(R.string.folder_group_id))) {
                                                    } else {
                                                        if (dbHandler.getCursorCountForFolderFilter(ProfileId,
                                                                getString(R.string.FolderFilter_text), preferences.getLanguage(), preferences.getUserID()) > 0) {
                                                            dbHandler.deleteGeneralFilterData(ProfileId, getString(R.string.FolderFilter_text),
                                                                    preferences.getLanguage(), preferences.getUserID());
                                                        }
                                                    }
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
                                                        if (key.equalsIgnoreCase(getString(R.string.folder_group_id))) {
                                                            if (dbHandler.getCursorCountForFolderFilter(preferences.getProfileId(),
                                                                    getString(R.string.FolderFilter_text), preferences.getLanguage(), preferences.getUserID()) > 0) {
                                                                        /* there is already folder filter is fired in local that's why just update here*/
                                                                ArrayList<GeneralFilterDataSet> arraylistFolderlist = dbHandler.getAllGeneralFilterData(preferences.getProfileId(), getString(R.string.FolderFilter_text), preferences.getLanguage(),
                                                                        preferences.getUserID());
                                                                if (arraylistFolderlist.size() > 0) {
                                                                    //ArrayList<String> list = new ArrayList<String>(Arrays.asList(arraylistFolderlist.get(0).getValueLocal().split(" , ")));
                                                                    List<String> list = GlobalClass.commaSeparetedtoArraylist(arraylistFolderlist.get(0).getValueLocal());
                                                                    List<String> listServer = GlobalClass.commaSeparetedtoArraylist(value);
                                                                    Set<String> newSet = null;
                                                                    if (list.size() > 0) {
                                                                        newSet = new HashSet<String>(list);
                                                                    }
                                                                    if (listServer.size() > 0) {
                                                                        if (newSet != null) {
                                                                            newSet.addAll(listServer);
                                                                        }
                                                                    }
                                                                    List<String> newList = null;
                                                                    if (newSet != null) {
                                                                        newList = new ArrayList<String>(newSet);
                                                                    }
                                                                    String joined = TextUtils.join(",", newList);
                                                                    dbHandler.updateGeneralFilter(preferences.getProfileId(), getString(R.string.folder_group_id), getString(R.string.FolderFilter_text), joined, preferences.getLanguage(), preferences.getUserID());
                                                                }
                                                            } else {
                                                                      /* no folder filter is fired that's why add new record here */
                                                                dbHandler.addGeneralFilter(preferences.getProfileId(), key, value, preferences.getLanguage(), type, preferences.getUserID(), value);
                                                            }


                                                        } else {
                                                            dbHandler.addGeneralFilter(preferences.getProfileId(), key, value, preferences.getLanguage(), type, preferences.getUserID(), value);
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }
                                    String mAttributeKey = "";
                                    String mAttributeValue = "";
                                    if (arraylistValue.size() > 0) {
                                        //Gson gson = new Gson();
                                        mAttributeKey = TextUtils.join(",", arraylistKey);
                                        mAttributeValue = TextUtils.join(",", arraylistValue);
                                    }

                                        /* adding default profile to database for table screen */
                                    dbHandler.updateRibbonFilterFromServerSide(ProfileId, preferences.getUserID(), preferences.getLanguage(), mAttributeValue);
                                    if(progressDialog != null){
                                        progressDialog.dismiss();
                                    }
                                } else {
                                    LogApp.e(" no response from service :  ", " response from service : ");
                                    if(progressDialog != null){
                                        progressDialog.dismiss();
                                    }
                                    //GlobalClass.showToast(getActivity(), getString(R.string.something_is_wrong));
                                    GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.error_title),getString(R.string.something_is_wrong));
                                }
                                getAllProfilefromDatabase();
                                preferences.saveIsProgressShow("yes");
                                refreshControl();
                            }
                        } catch (Exception e) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            getAllProfilefromDatabase();
                            preferences.saveIsProgressShow("yes");
                            refreshControl();
                        }
                    } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                            Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1009) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        /* whenever access token is expire get 1009
                        from service and call here getNewdeviceToken servcie and replace onld one with new device token */
                        updateAccessToken(1);
                    } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                            Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1004) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        // GlobalClass.showToast(activity,"Email is wrong");
                    } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                            Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1005) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        //GlobalClass.showToast(activity,"Password is wrong");
                    } else {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        //GlobalClass.showToast(getActivity(), getString(R.string.no_response_from_server));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.error_title),getString(R.string.no_response_from_server));
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    getAllProfilefromDatabase();
                    preferences.saveIsProgressShow("yes");
                    refreshControl();
                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }
                    if(t instanceof UnknownHostException){
                        //GlobalClass.showToast(getActivity(), getString(R.string.url_not_found));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.enter_organization_url_title),getString(R.string.enter_valid_url));
                        preferences.save404Count(preferences.get404Count()+1);
                    }
                    else {
                        //GlobalClass.showToast(getActivity(), getString(R.string.problem_while_deleting_profile));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.error_title),getString(R.string.problem_while_deleting_profile));
                    }

                }
            });
        } catch (Exception e) {
            LogApp.e(" while login service : ", " in catch : " + e.toString());
        }
    }

    /* this method will call get new device token service to get refreshed device token */
    public void updateAccessToken(final int pos) {
        GenericHelper helper = new GenericHelper(getActivity());
        Call<String> call = helper.getRetrofit().getNewAccessToken();
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                LogApp.e(" ##### ", " response from get device token : " + response.toString());
                Headers header = response.headers();
                try {
                    if (header.get(getResources().getString(R.string.x_status)) != null &&
                            Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1010) {
                        if (header.get(getResources().getString(R.string.x_message)) != null) {
                            String token = header.get(getResources().getString(R.string.x_message));
                                /* saving access token in preference to send in every service header */
                                /* access token = getfromservie+useridLength+userid+epochtime*/
                            String strFinalAccessToken = token + preferences.getUserID().length() + preferences.getUserID();

                            preferences.saveAccessToken(strFinalAccessToken);
                            GlobalClass.strAccessToken = preferences.getAccessToken();
                            if (pos == 1) {
                                createProfile();
                            } else if (pos == 2) {
                                deleteProfile();
                            } else if (pos == 3) {
                                saveSettingService();
                            } else if (pos == 4) {
                                getProfiles();
                            }

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

    private void saveSettingService() {
        try {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            //progressDialog.create();

            SpannableString ss = new SpannableString(getString(R.string.please_wait));
            ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new CustomTypefaceSpan("", GlobalClass.fontRegular), 0, ss.length(), 0);

            progressDialog.setMessage(ss);
            progressDialog.show();

            GenericHelper helper = new GenericHelper(getActivity());
            Call<String> call = helper.getRetrofit().saveSetting(
                    preferences.getProfileId(), strRibbonFiltertoSendserver, strGeneralFiltertoSendserver);
            call.enqueue(new retrofit2.Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    Headers header = response.headers();
                    try {
                        if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 0) {
                            String attributName = "";
                            //GlobalClass.showToast(LoginMobileActivity.this," 333333 success ");
                            String strResponse = response.body();
                            JSONObject obj = (JSONObject) new JSONTokener(strResponse).nextValue();
                            String status = obj.getString("Status");
                            if (status.equalsIgnoreCase("0")) {
                                if(progressDialog != null){
                                    progressDialog.dismiss();
                                }
                            } else {
                                LogApp.e(" no response from service :  ", " response from service : ");
                                if(progressDialog != null){
                                    progressDialog.dismiss();
                                }
                                //GlobalClass.showToast(getActivity(), getString(R.string.something_is_wrong));
                                GlobalClass.showCustomMessageDialog(getActivity(),
                                        getString(R.string.error_title),getString(R.string.something_is_wrong));
                            }
                        } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1009) {
                                /* whenever access token is expire get 1009
                                from service and call here getNewdeviceToken servcie and replace onld one with new device token */
                            updateAccessToken(3);
                        } else {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            //GlobalClass.showToast(activity,getString(R.string.something_is_wrong));
                        }
                    } catch (Exception e) {
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                        //GlobalClass.showToast(activity,getString(R.string.something_is_wrong));
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    dialogCreateProfile.dismiss();
                    if(t instanceof UnknownHostException){
                        //GlobalClass.showToast(getActivity(), getString(R.string.url_not_found));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.enter_organization_url_title),
                                getString(R.string.enter_valid_url));
                        preferences.save404Count(preferences.get404Count()+1);
                    }else {
                        //GlobalClass.showToast(getActivity(), getString(R.string.no_response_from_server));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.error_title),
                                getString(R.string.no_response_from_server));
                    }

                }
            });
        } catch (Exception e) {
            LogApp.e(" while login service : ", " in catch : " + e.toString());
        }
    }

    //    this method call service to create profile and store in database
    private void createProfile() {
        try {
            String userIdTemp = preferences.getUserIdTemp();
            //Creating an object of our api interface
            GenericHelper helper = new GenericHelper(getActivity());
            Call<String> call = helper.getRetrofit().createProfile(
                    userIdTemp, strProfileName, "", "0", preferences.getLanguage());
            call.enqueue(new retrofit2.Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    Headers header = response.headers();
                    try {
                        if (response != null) {
                            if (header.get(getResources().getString(R.string.x_status)) != null &&
                                    Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 0) {
                                /* get status 0 thats why parsing here*/
                                String attributName = "";
                                String finalResponse = response.body();
                                JSONObject obj = (JSONObject) new JSONTokener(finalResponse).nextValue();
                                JSONObject objPayload = obj.getJSONObject("Payload");

                                String profileId = objPayload.getString("Id");
                                String profileName = objPayload.getString("Name");
                                String colDetail = "";
                                String expandId = "";
                                JSONArray jsonArrayAttribute = objPayload.getJSONArray("Attributes");
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
                                                    dbHandler.addGeneralFilter(profileId, key, GlobalClass.removeCommaAttheEnd(value), preferences.getLanguage(), type, preferences.getUserID(), GlobalClass.removeCommaAttheEnd(value));
                                                }
                                            }
                                        }
                                    }
                                }
                                String mAttributeKey = "";
                                String mAttributeValue = "";
                                if (arraylistValue.size() > 0) {
                                    /*Gson gson = new Gson();
                                    mAttributeKey= gson.toJson(arraylistKey);
                                    mAttributeValue= gson.toJson(arraylistValue);*/

                                    mAttributeKey = TextUtils.join(",", arraylistKey);
                                    mAttributeValue = TextUtils.join(",", arraylistValue);
                                }
                                //String expandId = createProfilePojo.getPayloadCreateProfile().getExpandedIds();
                                dbHandler.addRibbonFilter(profileId, preferences.getUserID(), preferences.getLanguage(), mAttributeKey
                                        , mAttributeValue, "", getResources().getString(R.string.weekm));
                                dbHandler.addAllProfileToDB(profileId, profileName, colDetail, preferences.getUserID(),
                                        expandId, preferences.getUserIdTemp(), preferences.getLanguage(), "Week M", "", "", "", "");
                                if(dialogCreateProfile != null){
                                    dialogCreateProfile.dismiss();
                                }

                                if (arraylistProfileToSetasCurrent != null && arraylistProfileToSetasCurrent.size() > 0) {
                                    arraylistProfileToSetasCurrent = new ArrayList<ProfileModelClass>();
                                    ProfileModelClass model = new ProfileModelClass();
                                    model.setProfileId(profileId);
                                    model.setProfileName(profileName);
                                    model.setColDeTail(colDetail);
                                    model.setProfileUserId(preferences.getUserID());
                                    model.setResolution(getResources().getString(R.string.weekm));
                                    model.setLanguage(preferences.getLanguage());
                                    model.setKey("");
                                    model.setKeyLocal("");
                                    model.setValue("");
                                    model.setValueLocal("");

                                    arraylistProfileToSetasCurrent.add(model);
                                    textviewDefaultProfile.setText(arraylistProfileToSetasCurrent.get(0).getProfileName());
                                    //textviewWeekDialog.setText(arraylistProfileToSetasCurrent.get(0).getResolution());
                                    //textviewWeekDialog.setText(getString(R.string.weekm));
                                    preferences.clearUserProfile();
                                    preferences.clearLastDateFilterString();
                                    preferences.clearLastStatusilterString();
                                    preferences.clearLastTrafficFilterString();
                                    preferences.clearLastFolderfilterString();
                                    preferences.clearLastTextFilterString();
                                    preferences.clearLastUserDefinedFilterString();
                                    preferences.saveUserProfile(new Gson().toJson(model));
                                    preferences.saveProfileId(profileId);

                                    preferences.clearResourcefilter();
                                    preferences.clearFireResourcefilter();
                                    preferences.clearLastFiredFilterAll();

                                }
                                //GlobalClass.showToast(getActivity(), getString(R.string.profile_created_successfully));
                                GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.profile),getString(R.string.profile_created_successfully));
                                /* here profile is created that's why refresh and load control here */
                                refreshControl();
                            } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                    Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1009) {
                                /* whenever access token is expire get 1009
                                from service and call here getNewdeviceToken servcie and replace onld one with new device token */
                                updateAccessToken(1);
                            } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                    Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1004) {
                                // GlobalClass.showToast(activity,"Email is wrong");
                            } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                    Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1005) {
                                //GlobalClass.showToast(activity,"Password is wrong");
                            }
                        } else {
                            if(dialogCreateProfile != null){
                                dialogCreateProfile.dismiss();
                            }
                           // GlobalClass.showToast(getActivity(), getString(R.string.no_response_from_server));
                            GlobalClass.showCustomMessageDialog(getActivity(),
                                    getString(R.string.error_title),getString(R.string.no_response_from_server));
                            LogApp.e(" no response from service :  ", " response from service : ");
                        }
                    } catch (Exception e) {
                        if(dialogCreateProfile != null){
                            dialogCreateProfile.dismiss();
                        }

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if(dialogCreateProfile != null){
                        dialogCreateProfile.dismiss();
                    }
                    if(t instanceof UnknownHostException){
                        //GlobalClass.showToast(getActivity(), getString(R.string.url_not_found));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.enter_organization_url_title),getString(R.string.enter_valid_url));
                        preferences.save404Count(preferences.get404Count()+1);
                    }
                    else {
                        //GlobalClass.showToast(getActivity(), getString(R.string.no_response_from_server));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.error_title),
                                getString(R.string.no_response_from_server));
                    }

                }
            });
        } catch (Exception e) {
            LogApp.e(" while login service : ", " in catch : " + e.toString());
        }
    }

    //    this method delete profile from database and also from server
    private void deleteProfile() {
        try {
            //final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),"",getString(R.string.please_wait),false,false);
            GenericHelper helper = new GenericHelper(getActivity());
            Call<String> call = helper.getRetrofit().deleteProfile(clickedProfileId);
            call.enqueue(new retrofit2.Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    String body = response.body();
                    if (body == null || body.equalsIgnoreCase("")) {
                        /* body is null that's why call new Access token service here */
                        //updateAccessToken(2);
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.error_title),getString(R.string.no_response_from_server));
                    } else if (body != null || !body.equalsIgnoreCase("")) {
                        Headers header = response.headers();
                        try {
                            if (header.get(getResources().getString(R.string.x_status)) != null &&
                                    Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 0) {
                                dbHandler.deleteSingleUserProfile(preferences.getUserID(), clickedProfileId);
                                for (int i = 0; i < arraylistProfile.size(); i++) {
                                    if (clickedProfileId.equalsIgnoreCase(arraylistProfile.get(i).getProfileId())) {
                                        arraylistProfile.remove(i);
                                    }
                                }
                                adapterProfile.notifyDataSetChanged();
                                preferences.clearUserProfile();
                                //GlobalClass.showToast(getActivity(), getString(R.string.profile_deleted));
                                GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.profile),
                                        getString(R.string.profile_deleted));
                                getAllProfilefromDatabase();
                                preferences.clearResourcefilter();
                                preferences.clearFireResourcefilter();
                                preferences.clearLastFiredFilterAll();

                                refreshControl();

                            } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                    Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1009) {
                                /* whenever access token is expire get 1009
                                from service and call here getNewdeviceToken servcie and replace onld one with new device token */
                                updateAccessToken(2);
                            } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                    Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1004) {
                                // GlobalClass.showToast(activity,"Email is wrong");
                            } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                    Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1005) {
                                //GlobalClass.showToast(activity,"Password is wrong");
                            } else {
                                //progressDialog.dismiss();
                                if(dialogCreateProfile != null){
                                    dialogCreateProfile.dismiss();
                                }
                                //GlobalClass.showToast(getActivity(), getString(R.string.no_response_from_server));
                                GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.error_title),getString(R.string.no_response_from_server));
                            }
                        } catch (Exception e) {
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    //progressDialog.dismiss();
                    if(t instanceof UnknownHostException){
                        //progressDialog.dismiss();
                        //GlobalClass.showToast(getActivity(), getString(R.string.url_not_found));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.enter_organization_url_title),
                                getString(R.string.enter_valid_url));
                        preferences.save404Count(preferences.get404Count()+1);
                    }
                    else {
                        //GlobalClass.showToast(getActivity(), getString(R.string.problem_while_deleting_profile));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.error_title),
                                getString(R.string.problem_while_deleting_profile));
                    }

                }
            });
        } catch (Exception e) {
            LogApp.e(" while login service : ", " in catch : " + e.toString());
        }
    }

    //    show alert to confirmation use about delete or not
    public void showAlertDialogDelete() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(getString(R.string.delete_profile));
        alertDialog.setMessage(getString(R.string.are_you_sure_to_delete_profile));
        alertDialog.setIcon(R.drawable.delete);
        alertDialog.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        popupWindowProfile.dismiss();
                        isPopupDefaultProfile = false;
                        if(preferences.isDemoUser()){
                            GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.access_denied_title),
                                    getString(R.string.profile_delete_disable));
                        }else {
                            deleteProfile();
                        }
                    }
                });

        alertDialog.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        alertDialog.show();
    }

    @Override
    public void clickedProject() {

    }

    //    this class is use for swip on profile to delete
    class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
        Context context;
        GestureDetector gDetector;
        static final int SWIPE_MIN_DISTANCE = 120;
        static final int SWIPE_MAX_OFF_PATH = 250;
        static final int SWIPE_THRESHOLD_VELOCITY = 200;

        public SwipeGestureListener() {
            super();
        }

        public SwipeGestureListener(Context context) {
            this(context, null);
        }

        public SwipeGestureListener(Context context, GestureDetector gDetector) {
            if (gDetector == null)
                gDetector = new GestureDetector(context, this);
            this.context = context;
            this.gDetector = gDetector;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            final int position = listviewProfile.pointToPosition(
                    Math.round(e1.getX()), Math.round(e1.getY()));
            clickedProfileId = arraylistProfile.get(position).getProfileId();
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                if (Math.abs(e1.getX() - e2.getX()) > SWIPE_MAX_OFF_PATH || Math.abs(velocityY) < SWIPE_THRESHOLD_VELOCITY) {
                    return false;
                }
                if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) {
                } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE) {
                }
            } else {
                if (Math.abs(velocityX) < SWIPE_THRESHOLD_VELOCITY) {
                    return false;
                }
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {
                    if (position == 0) {
                        //GlobalClass.showToast(getActivity(), getString(R.string.you_can_not_delete_default_profile));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.access_denied_title),getString(R.string.problem_while_deleting_profile));
                    } else {
                        showAlertDialogDelete();
                    }
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
                    if (position == 0) {
                        //GlobalClass.showToast(getActivity(), getString(R.string.you_can_not_delete_default_profile));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.access_denied_title),getString(R.string.problem_while_deleting_profile));
                    } else {
                        showAlertDialogDelete();
                    }
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        public GestureDetector getDetector() {
            return gDetector;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gDetector.onTouchEvent(event);
        }
    }

    //    return general filter selected value into string
    public String getGeneralFilterJson() {

        //String jsonStrFolder="",jsonStrText="",jsonStrUserDefined="",jsonStrDate="";
        JSONObject jsonObjectFolder = null;//,jsonObjectText=null,jsonObjectUserDefined=null,jsonObjectDate=null;
        jsonObjectFolder = new JSONObject();
        /* getting folder filter from database */
        ArrayList<GeneralFilterDataSet> arraylistFolderlist = new ArrayList<>();
        if (dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(), getString(R.string.FolderFilter_text), preferences.getLanguage(),
                preferences.getUserID()) > 0) {
            /*arraylistFolderlist = dbHandler.getGeneralFilterData(getString(R.string.FolderFilter_text), preferences.getUserID(),
                    preferences.getProfileId());*/
            arraylistFolderlist = dbHandler.getAllGeneralFilterData(preferences.getProfileId(), getString(R.string.FolderFilter_text), preferences.getLanguage(),
                    preferences.getUserID());
            if (arraylistFolderlist.size() > 0) {
                String[] splitlocal = arraylistFolderlist.get(0).getValueLocal().split(",");
                String[] splitserver = arraylistFolderlist.get(0).getValue().split(",");
                String joined = "";
                if (splitlocal.length > 0) {
                    if (splitserver.length > 0) {
                        Set<String> set = new HashSet<>(Arrays.asList(splitlocal));
                        set.addAll(Arrays.asList(splitserver));
                        String[] combined = set.toArray(new String[0]);
                        joined = GlobalClass.arrayToCommaSeparated(combined);
                    } else {
                        joined = GlobalClass.arrayToCommaSeparated(splitlocal);
                    }

                } else if (splitserver.length > 0) {
                    joined = GlobalClass.arrayToCommaSeparated(splitserver);
                }
                //jsonObjectFolder = new JSONObject();
                try {
                    jsonObjectFolder.put("GroupId", joined);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //jsonStrFolder = jsonObjectFolder.toString();

            }
        }
        /* getting text filter from database */
        ArrayList<GeneralFilterDataSet> arraylistTextFilter = new ArrayList<>();
        if (dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(), getString(R.string.TextFilter_text)
                , preferences.getLanguage(), preferences.getUserID()) > 0) {
            /*arraylistFolderlist = dbHandler.getGeneralFilterData(getString(R.string.FolderFilter_text), preferences.getUserID(),
                    preferences.getProfileId());*/
            arraylistTextFilter = dbHandler.getAllGeneralFilterData(preferences.getProfileId(),
                    getString(R.string.TextFilter_text), preferences.getLanguage(),
                    preferences.getUserID());
            if (arraylistTextFilter.size() > 0) {
                //jsonObjectText = new JSONObject();
                for (int i = 0; i < arraylistTextFilter.size(); i++) {
                    try {
                        jsonObjectFolder.put(arraylistTextFilter.get(i).getKey(), arraylistTextFilter.get(i).getValue());
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
        if (dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(),
                getString(R.string.UserDefinedFilter_text), preferences.getLanguage(),
                preferences.getUserID()) > 0) {
            arraylisUserDefinedText = dbHandler.getAllGeneralFilterData(preferences.getProfileId()
                    , getString(R.string.UserDefinedFilter_text), preferences.getLanguage(),
                    preferences.getUserID());

        }
        if (dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(),
                getString(R.string.UserDefindedFilterForNumber), preferences.getLanguage(),
                preferences.getUserID()) > 0) {
            arraylisUserDefinedNumber = dbHandler.getAllGeneralFilterData(preferences.getProfileId(), getString(R.string.UserDefindedFilterForNumber)
                    , preferences.getLanguage(), preferences.getUserID());
        }
        ArrayList<GeneralFilterDataSet> arrayList = new ArrayList<>();
        if (arraylisUserDefinedText.size() > 0) {
            if (arraylisUserDefinedNumber.size() > 0) {
                for (int i = 0; i < arraylisUserDefinedText.size(); i++) {
                    arrayList.add(arraylisUserDefinedText.get(i));
                }
                for (int i = 0; i < arraylisUserDefinedNumber.size(); i++) {
                    arrayList.add(arraylisUserDefinedNumber.get(i));
                }
            } else {
                for (int i = 0; i < arraylisUserDefinedText.size(); i++) {
                    arrayList.add(arraylisUserDefinedText.get(i));
                }
            }
            if (arrayList.size() > 0) {
                //jsonObjectUserDefined = new JSONObject();
                for (int i = 0; i < arrayList.size(); i++) {
                    try {
                        jsonObjectFolder.put(arrayList.get(i).getKey(), arrayList.get(i).getValue());
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
        if (dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(),
                getString(R.string.DateFilter_text), preferences.getLanguage(),
                preferences.getUserID()) > 0) {
            arraylistValueDate = dbHandler.getAllGeneralFilterData(preferences.getProfileId(), getString(R.string.DateFilter_text)
                    , preferences.getLanguage(), preferences.getUserID());

        }
        if (arraylistValueDate.size() > 0) {
            //jsonObjectDate = new JSONObject();
            for (int i = 0; i < arraylistValueDate.size(); i++) {
                try {
                    jsonObjectFolder.put(arraylistValueDate.get(i).getKey(), arraylistValueDate.get(i).getValue());
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
    /*public void setTextviewBold(TextView textview,String filterType){
        ArrayList<FolderDataset> arraylistgetedFromDatabase = new ArrayList<>();
        ArrayList<String> arraylistValue = new ArrayList<>();
        //if(dbHandler.getCursorCountFilterDataForProfile(preferences.getUserID(),preferences.getProfileId(),filterType) > 0){
            if(dbHandler.getCursorCountFilterDataForProfile(preferences.getUserID(),preferences.getProfileId(),filterType) > 0)
            {
                arraylistgetedFromDatabase=dbHandler.getGeneralFilterData(filterType,preferences.getUserID(),
                        preferences.getProfileId());
            }
            arraylistValue = new ArrayList<>();
            if(arraylistgetedFromDatabase.size() > 0){
                String[] split = arraylistgetedFromDatabase.get(0).getName().split(",");

                for (int i=0;i<split.length;i++)
                {
                    if(split[i].equalsIgnoreCase("1")){
                        textview.setTypeface(GlobalClass.fontBold);
                        break;
                    }
                    else {
                        textview.setTypeface(GlobalClass.fontLight);
                    }
                }
            }
            else {
                textview.setTypeface(GlobalClass.fontLight);
            }
            //}
    }*/
    public void clearDashboardFilterPreferences() {
        preferences.clearAgreegateLastFiredFilter();
        preferences.clearTaskLastFiredFilter();
        preferences.clearClickedAgreegatePosition();
        preferences.clearClickedTaskPosition();
    }

    //    shortcut method for fire general and ribbon filter
    public void fireGeneralAndRibbonFilterShortCut() {

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
        String strRibbonFilter = "";
        String url = "";
        if (arraylistRibbonFilterSettedMerged.size() > 0) {
            strRibbonFilter = TextUtils.join(",", arraylistRibbonFilterSettedMerged);
            LogApp.e("", " arraylist ribbon filter : " + strRibbonFilter);
        }
        if (strGeneralFilterJson != null && !strGeneralFilterJson.equalsIgnoreCase("") && !strGeneralFilterJson.
                equalsIgnoreCase("{}")) {
            if (!strRibbonFilter.equalsIgnoreCase("") && strRibbonFilter != null) {
                url = "javascript:(function() { SetFiltersDataFromAppMobile('" + strRibbonFilter + "'," + strGeneralFilterJson + ");})()";
                //url = "javascript:(function() { SetFiltersDataFromAppMobile(void 0,"+finalJson+");})()";
            } else {
                url = "javascript:(function() { SetFiltersDataFromAppMobile(void 0," + strGeneralFilterJson + ");})()";
            }
            webview.loadUrl(url);
            preferences.saveIsProgressShow("no");
        } else if (strRibbonFilter != null && !strRibbonFilter.equalsIgnoreCase("")) {
            // problem here for general filter its solved already
            //url = "javascript:(function() { SetFiltersDataFromAppMobile('" + strRibbonFilter + "','');})()";
            url = "javascript:(function() { SetFiltersDataFromAppMobile('" + strRibbonFilter + "',void 0);})()";
            webview.loadUrl(url);
            preferences.saveIsProgressShow("no");
        } else if (preferences.getCurrentFiredFilter().equalsIgnoreCase("6")) {
            preferences.saveIsProgressShow("no");
        } /*else if (strRibbonFilter.equalsIgnoreCase("")) {
            if (preferences.getCurrentFiredFilter().equalsIgnoreCase("7")) {
                // problem here for general filter its solved already
                url = "javascript:(function() { SetFiltersDataFromAppMobile('" + strRibbonFilter + "','');})()";
                webview.loadUrl(url);
                preferences.saveIsProgressShow("no");
            }

        }*/ else {
            webview.setWebViewClient(new MyWebViewClient2());
            //String finalurl = "file:///android_asset/index.html?profile=1&userId=3&HOST_URL=http://project.ssoft.in:8090/management/";
            webviewUrl = GlobalClass.CONTROL_PATH + profileIdToSendControl + "&token=" +
                    GlobalClass.strAccessToken+GlobalClass.getEpochTime() + "&HOST_URL=" + preferences.getDomain()
                    + "&orgId=" + preferences.getTokenforControl()+GlobalClass.getEpochTime() +
                    "&isWebViewer=0&Control=1&Lang=" + preferences.getLanguage();
            //String finalurl = "file:///android_asset/index.html?profile=1&userId=3&HOST_URL=http://78.46.120.30/BerndTesting/management/";
            webview.loadUrl(webviewUrl);
        }
    }

    public class MyWebViewClientForDashboardFilter extends WebViewClient {
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
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            String urlOffunction = "javascript:(function() { LoadDashBoradDataOnControl(" + strJsonforTask.trim() + ",'" +
                    strTaskId.trim() + "');})()";
            fireDashboardFilter(urlOffunction);
            //startWebView(webviewUrl);
        }
    }

    //    webview client
    public class MyWebViewClient2 extends WebViewClient {
        ProgressDialog progressDialog = null;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            if (preferences.getIsProgressShow().equalsIgnoreCase("no")) {

            } else {
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

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            preferences.saveIsProgressShow("yes");
        }
    }

    //    webview client for genera and ribbon filter
    public class MyWebViewClientForGeneralFilter extends WebViewClient {
        ProgressDialog progressDialog = null;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (preferences.getIsProgressShow().equalsIgnoreCase("no")) {
            } else {
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
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            fireGeneralAndRibbonFilterShortCut();
            preferences.saveCurrentFiredFilter("7");
        }
    }

    //    just refresh control
    public void refreshControl() {
        webview.setWebViewClient(new MyWebViewClientForGeneralFilter());
        webviewUrl = GlobalClass.CONTROL_PATH + profileIdToSendControl + "&token=" +
                GlobalClass.strAccessToken + "&HOST_URL=" + preferences.getDomain()
                + "&orgId=" + preferences.getTokenforControl()+GlobalClass.getEpochTime() +
                "&isWebViewer=0&Control=1&Lang=" + preferences.getLanguage();
        webview.loadUrl(webviewUrl);
        //// fire general and ribbon filter after load url
    }

    //    load url and call control's javascript function
    public void refreshControlForDashboard() {
        webview.getSettings().setDomStorageEnabled(true);
        webview.setWebViewClient(new MyWebViewClientForDashboardFilter());
        webviewUrl = GlobalClass.CONTROL_PATH + profileIdToSendControl + "&token=" +
                GlobalClass.strAccessToken+GlobalClass.getEpochTime() + "&HOST_URL=" + preferences.getDomain()
                + "&orgId=" + preferences.getTokenforControl()+GlobalClass.getEpochTime() +
                "&isWebViewer=0&Control=1&Lang=" + preferences.getLanguage() + "&from=dashboard";
        webview.loadUrl(webviewUrl);
    }

    //    load contorl when came from dashboard
    public void fireDashboardFilter(String url) {
        webview.loadUrl(url);
    }

    //    method to load control on click of today textview
    public void fireToday() {
        String url = "javascript:(function() { ScrollToToday();})()";
        webview.loadUrl(url);
    }

    //    method to load control on selecting any resolution from popuview
    public void fireResolution(String resolution) {
        String url = "javascript:(function() { changeResolution('" + resolution + "');})()";
        webview.loadUrl(url);
    }

    //    alert dialog will show when general filter is already fired and user came from dashboard to load control
//    interface called when langeuage is changed from profile
    @Override
    public void changeLanguageProfile() {
        //GlobalClass.showToast(getActivity(),"Language has been changed ");
        preferences.clearUserProfile();
        textviewWeekDialog.setText(getString(R.string.set_resolution));
        setLanguage(preferences.getLanguage());
        // gettin all profile from database stored
        getAllProfilefromDatabase();
        refreshControl();
    }

    //    set language for date dialog
    public void setLanguageDateLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());

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
    public void setLanguageTrafficDialog(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());

        txtred.setText(getString(R.string.traffic_red));
        txtyellow.setText(getString(R.string.traffic_yellow));
        txtgreen.setText(getString(R.string.traffic_green));
        buttonTrafficOk.setText(R.string.ok);
    }

    //    setting language for status dialog
    public void setLanguageStatusDialog(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());

        txtstatusheder.setText(getString(R.string.status));
        txtnoticearrow.setText(getString(R.string.notice_arrow));
        txtreshow.setText(getString(R.string.reshow));
        txtlimitoverun.setText(getString(R.string.limit_overrun));
        txtcritical.setText(getString(R.string.critical));
        txtrequring.setText(getString(R.string.requiring_clarification));
        buttonStatusOk.setText(R.string.ok);
    }

    //    setting language for add profile dilaog
    public void setLanguageAddProfile(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());

        txtaddprofile.setText(getString(R.string.add_new_profile));
    }

    //    set language dynamically
    public void setLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());

        if(textviewDateDialog != null){
            textviewDateDialog.setText(getString(R.string.date));
        }

        if(textviewStatusDialog != null){
            textviewStatusDialog.setText(getString(R.string.status));
        }

        if(textviewTrafficLightDialog != null){
            textviewTrafficLightDialog.setText(getString(R.string.TrafficLights));
        }

        if(textviewToday != null){
            textviewToday.setText(getString(R.string.today));
        }

        if(textViewTitle != null){
            textViewTitle.setText(getString(R.string.project_header));
        }






    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        ((HomeActivity) context).interfaceChangeLanguageProfile = this;
        ((HomeActivity) context).interfaceLoadWebview = this;
        ((HomeActivity) context).interfaceProjectClicked = this;

    }

    public class CustomTouchListenerDate implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
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
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
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
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
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

    public void filterSettedDialog(final String json, final String id, final String comefrom, final int pos) {
        dialogFilterSetted = new Dialog(getActivity());
        View dialogView = View.inflate(getContext(), R.layout.custom_dialog_filter_setted, null);
        dialogFilterSetted.setContentView(dialogView);
        dialogFilterSetted.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogFilterSetted.setCancelable(false);

        TextView textViewTitle = (TextView) dialogView.findViewById(R.id.textview_title_filtersetted);
        TextView textViewMessage = (TextView) dialogView.findViewById(R.id.textview_message_filtersetted);

        textViewTitle.setText(getString(R.string.filter_setted));
        textViewTitle.setTextColor(getResources().getColor(R.color.red));
        textViewTitle.setTypeface(GlobalClass.fontRegular);

        textViewMessage.setText(getString(R.string.filter_setted_message));
        textViewMessage.setTextColor(getResources().getColor(R.color.black_color));
        textViewMessage.setTypeface(GlobalClass.fontRegular);


        textViewNo = (TextView) dialogView.findViewById(R.id.no);
        textViewYes = (TextView) dialogView.findViewById(R.id.yes);
        textViewNo.setTypeface(GlobalClass.fontRegular);
        textViewYes.setTypeface(GlobalClass.fontRegular);
        textViewNo.setTextColor(getResources().getColor(R.color.textview_color_blue));
        textViewYes.setTextColor(getResources().getColor(R.color.textview_color_blue));

        //textViewNo.setOnTouchListener(new CustomTouchListenerTextviewNo());
        //textViewYes.setOnTouchListener(new CustomTouchListenerTextviewYes());

        dialogFilterSetted.show();
        dialogFilterSetted.getWindow().setLayout(GlobalClass.screenWidth(getContext()) / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //preferences.saveClickedAgreegatePosition("");
                //preferences.saveClickedTaskPosition("");
                preferences.saveIsGeneralFilterDialogshow("noclicked");
                dialogFilterSetted.cancel();
                if (preferences.getIsFirsttimeToLoadControl().equalsIgnoreCase("yes")) {
                    preferences.saveIsProgressShow("yes");
                    preferences.saveIsFirsttimeToLoadControl("no");
                    refreshControl();
                    preferences.saveCurrentFiredFilter("6");
                } else {
                }
            }
        });
        textViewYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFilterSetted.cancel();
                //preferences.saveIsGeneralFilterDialogshow("no");
                if (preferences.getIsFirsttimeToLoadControl().equalsIgnoreCase("yes")) {
                    preferences.saveIsFirsttimeToLoadControl("");
                }
                callWebviewFromDashboardShortcut(json, id, comefrom, pos);
            }
        });
    }

    public class CustomTouchListenerTextviewYes implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    textViewYes.setBackgroundColor(getResources().getColor(R.color.select_filter));
                    //white
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    textViewYes.setBackgroundColor(getResources().getColor(android.R.color.transparent)); //black
                    break;
            }
            return false;
        }
    }

    public class CustomTouchListenerTextviewNo implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    textViewNo.setBackgroundColor(getResources().getColor(R.color.select_filter));
                    //white
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    textViewNo.setBackgroundColor(getResources().getColor(android.R.color.transparent)); //black
                    break;
            }
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void filterpopup() {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.popupview_filter, null);
        popupWindowWeek = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindowWeek.setWidth((GlobalClass.screenWidth(getContext()) * 50) / 100);
        popupWindowWeek.setHeight((GlobalClass.screenHeight(getContext()) * 30) / 100);
        popupWindowWeek.setOutsideTouchable(true);
        popupWindowWeek.showAsDropDown(textviewFilter, (int) getResources().getDimension(R.dimen.minus_five_sp), 20, Gravity.RIGHT | Gravity.BOTTOM);
        textviewDateDialog = (TextView) popupView.findViewById(R.id.txt_date);
        textviewStatusDialog = (TextView) popupView.findViewById(R.id.txt_status);
        textviewTrafficLightDialog = (TextView) popupView.findViewById(R.id.txt_traffic_lights);
        //setTextviewBold(textviewDateDialog, getString(R.string.DateLightFilter));
        //setTextviewBold(textviewStatusDialog, getString(R.string.StatusLightFilter));
        //setTextviewBold(textviewTrafficLightDialog, getString(R.string.TrafficLightFilter));
        setTextViewStyleForDateTextView(arraylistRibbonFilterSettedMerged);
        setTextViewStyleForStatusTextView(arraylistRibbonFilterSettedMerged);
        setTextViewStyleForTrafficTextView(arraylistRibbonFilterSettedMerged);
        linearFilterdate = (LinearLayout) popupView.findViewById(R.id.linear_Filter_date);
        linearFilterdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == linearFilterdate) {
                    showFilteredDialog(1, R.style.DialogAnimation);
                    if(popupWindowWeek != null){
                        popupWindowWeek.dismiss();
                    }

                }
            }
        });
        linearFilterStatus = (LinearLayout) popupView.findViewById(R.id.linear_filter_status);
        linearFilterStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == linearFilterStatus) {
                    showFilteredDialog(2, R.style.DialogAnimation);
                    if(popupWindowWeek != null){
                        popupWindowWeek.dismiss();
                    }
                }
            }
        });
        linearFilterTrafficLights = (LinearLayout) popupView.findViewById(R.id.linear_filter_traffic_lights);
        linearFilterTrafficLights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == linearFilterTrafficLights) {
                    showFilteredDialog(3, R.style.DialogAnimation);
                    if(popupWindowWeek != null){
                        popupWindowWeek.dismiss();
                    }
                }
            }
        });
    }

    //    shortcut method to load control from dashboard
    public void callWebviewFromDashboardShortcut(String json, String id, String comefrom, int pos) {
        strJsonforTask = json;
        strTaskId = id;
        if (comefrom.equalsIgnoreCase("task")) {
            preferences.saveLastFiredFilterAll("dashboard");
            preferences.saveLastDashboardFiredFilter("task");
            preferences.clearClickedAgreegatePosition();
            preferences.clearAgreegateLastFiredFilter();

            preferences.saveTaskLastFiredFilter(String.valueOf(pos));
            if (preferences.getCurrentFiredFilter().equalsIgnoreCase("1")) {
                //// refresh control here
                //GlobalClass.showToast(getActivity()," 11111 ");
                refreshControlForDashboard();
                //String url = "javascript:(function() { LoadDashBoradDataOnControl("+json+","+id+");})()";
                //webview.loadUrl(url);
            } else if (preferences.getCurrentFiredFilter().equalsIgnoreCase("2")) {
                //// don't refresh control here just call JS method
                //GlobalClass.showToast(getActivity()," 22222 ");
                //String url = "javascript:(function() { LoadDashBoradDataOnControl(void 0,"+id+");})()";
                String url = "javascript:(function() { AddSelectionToNode('" + id + "');})()";
                webview.loadUrl(url);
            }
        }
        ///// come from agreegate
        else {
            preferences.saveLastFiredFilterAll("dashboard");
            preferences.saveLastDashboardFiredFilter("agreegate");
            preferences.clearClickedTaskPosition();
            preferences.clearTaskLastFiredFilter();

            preferences.saveAgreegateLastFiredFilter(String.valueOf(pos));
            if (preferences.getCurrentFiredFilter().equalsIgnoreCase("3")) {
                //// refresh control here
                //GlobalClass.showToast(getActivity()," 33333 ");

                refreshControlForDashboard();
                //String url = "javascript:(function() { LoadDashBoradDataOnControl("+json+","+id+");})()";
                //webview.loadUrl(url);
            } else if (preferences.getCurrentFiredFilter().equalsIgnoreCase("4")) {
                ////// don't refresh control here just call JS function here
                //GlobalClass.showToast(getActivity()," 44444 ");
                //String url = "javascript:(function() { LoadDashBoradDataOnControl("+json+","+id+");})()";
                //webview.loadUrl(url);
            }
        }
    }

    //    fire general and ribbon filter based on condition
    public void fireGeneralAndRibbonFilter() {

        /// 5 == just refresh control don't call any js fuction
        /// 6 == refresh control and call js function
        /// 7 == not refresh control just call js function
        /// 8 === do nothing
        if (preferences.getCurrentFiredFilter().equalsIgnoreCase("7")) {
            preferences.clearClickedAgreegatePosition();
            ///// don't refresh control here just call JS function General filter
            preferences.saveIsGeneralFilterDialogshow("yes");
            fireGeneralAndRibbonFilterShortCut();
            preferences.saveCurrentFiredFilter("8");
            preferences.saveLastFiredFilterAll("general");
        } else if (preferences.getCurrentFiredFilter().equalsIgnoreCase("6")) {
            preferences.clearClickedAgreegatePosition();
            ////// refresh control and then call JS function of General filter
            preferences.saveIsGeneralFilterDialogshow("yes");
            refreshControl();
            preferences.saveCurrentFiredFilter("7");
            preferences.saveLastFiredFilterAll("general");
        } else if (preferences.getCurrentFiredFilter().equalsIgnoreCase("5")) {
            preferences.clearClickedAgreegatePosition();
            preferences.saveIsGeneralFilterDialogshow("yes");
            preferences.saveCurrentFiredFilter("8");
            refreshControl();
            preferences.saveLastFiredFilterAll("general");
        } else if (preferences.getCurrentFiredFilter().equalsIgnoreCase("8")) {
            /* do nothing */
            //preferences.saveLastFiredFilterAll("general");
        }


    }

    //    interface method called when click on Project from footer
    @Override
    public void callWebviewFromFooter() {
        /* when press project from footer this will fire */
        /// 5 == just refresh control don't call any js fuction
        /// 6 == refresh control and call js function
        /// 7 == not refresh control just call js function
        /// 8 === do nothing
        if (preferences.getIsFirsttimeToLoadControl().equalsIgnoreCase("yes")) {
            preferences.saveIsFirsttimeToLoadControl("");
        }

        if (preferences.getCurrentFiredFilter().equalsIgnoreCase("")) {
            preferences.saveCurrentFiredFilter("5");
            clearDashboardFilterPreferences();
        } else if (preferences.getCurrentFiredFilter().equalsIgnoreCase("1")
                || preferences.getCurrentFiredFilter().equalsIgnoreCase("2")
                || preferences.getCurrentFiredFilter().equalsIgnoreCase("3")
                || preferences.getCurrentFiredFilter().equalsIgnoreCase("4")) {
            /* this condition is for doing nothing */
            //preferences.saveCurrentFiredFilter("8");
        } else if (preferences.getCurrentFiredFilter().equalsIgnoreCase("5")) {
            /* this condition is for refresh control and call js function */
            preferences.saveCurrentFiredFilter("6");
            clearDashboardFilterPreferences();
        } else if (preferences.getCurrentFiredFilter().equalsIgnoreCase("6")) {
            clearDashboardFilterPreferences();
            /* this condtion refresh control and also call Js fucntion*/
            //preferences.saveCurrentFiredFilter("7");
        } else if (preferences.getCurrentFiredFilter().equalsIgnoreCase("7")) {
            clearDashboardFilterPreferences();
            /* this condition  is for doing nothing */
            //preferences.saveCurrentFiredFilter("8");
        }
        fireGeneralAndRibbonFilter();
    }

    //    interface method called when click on task or agreegate from dashboard
    @Override
    public void callWebviewFromDashboard(String json, String id, String comefrom, int pos) {
        if (dbHandler.getCursorCountForRibbonFilter(preferences.getProfileId(), preferences.getUserID(),
                preferences.getLanguage()) > 0 || dbHandler.getCursorCountForAnyGeneralFilter(preferences.getProfileId(), preferences.getUserID(),
                preferences.getLanguage()) > 0) {
            //if (dbHandler.getCursorCountFilterForAllFilter(preferences.getUserID(),preferences.getProfileId()) > 0) {
            if (preferences.getIsGeneralFilterDialogshow().equalsIgnoreCase("yes")) {
                //alertDialogFilterNotify(json,id,comefrom,pos);
                filterSettedDialog(json, id, comefrom, pos);
            } else {
                callWebviewFromDashboardShortcut(json, id, comefrom, pos);
            }
        } else {
            callWebviewFromDashboardShortcut(json, id, comefrom, pos);
        }
    }
}