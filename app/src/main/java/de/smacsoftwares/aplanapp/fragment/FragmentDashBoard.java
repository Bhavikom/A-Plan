package de.smacsoftwares.aplanapp.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;

import de.smacsoftwares.aplanapp.Model.AgreegateDataModelClass;
import de.smacsoftwares.aplanapp.Model.FilterModelClass;
import de.smacsoftwares.aplanapp.Model.TaskListModelClass;
import de.smacsoftwares.aplanapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import de.smacsoftwares.aplanapp.activity.HomeActivity;
import de.smacsoftwares.aplanapp.adapter.AgreegateAdapter;
import de.smacsoftwares.aplanapp.adapter.DateFilterDialogAdapter;
import de.smacsoftwares.aplanapp.adapter.DatesSelectedDialogAdapter;
import de.smacsoftwares.aplanapp.adapter.StatusFilterDialogAdapter;
import de.smacsoftwares.aplanapp.adapter.TasklistAdapter;
import de.smacsoftwares.aplanapp.adapter.TrafficFilterDialogAdapter;
import de.smacsoftwares.aplanapp.caldroid.CaldroidFragment;
import de.smacsoftwares.aplanapp.caldroid.CaldroidListener;
import de.smacsoftwares.aplanapp.util.CustomDialogSelectedDates;
import de.smacsoftwares.aplanapp.util.CustomTypefaceSpan;
import de.smacsoftwares.aplanapp.util.DatabaseHandler;
import de.smacsoftwares.aplanapp.util.GenericHelper;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.InterfaceChangeLanguageDashboard;
import de.smacsoftwares.aplanapp.util.LogApp;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;
import de.smacsoftwares.aplanapp.util.TaskComparableClass;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by SSOFT4 on 10/25/2016.
 */

@SuppressLint("SimpleDateFormat")
public class FragmentDashBoard extends Fragment implements View.OnClickListener, CustomDialogSelectedDates.
        IntefaceDialog, FragmentProfile.InterfaceProfileChanged, InterfaceChangeLanguageDashboard {

    PreferencesHelper preferenes;
    DatabaseHandler dbHandler;

    private CaldroidFragment caldroidFragment;
    Date startDate = null, endDate = null, clickedDate = null,startDateLast=null,endDateLast=null;;
    public boolean isDateDialogFirsttime = true;
    Bundle bundle;
    String strLoadmore1 = "", strLoadmore2 = "";
    ArrayList<String> arrayListSelectedDate = new ArrayList<>();
    ArrayList<String> arrayListSelectedStatus = new ArrayList<>();
    ArrayList<String> arrayListSelectedTraffic = new ArrayList<>();
    ArrayList<String> arrayListSelectedFinishDates = new ArrayList<>();

    public static ArrayList<Date> selectedDatesStartEnd;
    static Calendar nextYear;
    static Calendar lastYear;
    // all arraylist
    static List<Date> allDatesBetweenDates;
    public Context context;
    String strNodedata = "";
    Taskclicked taskclicked;
    Activity activity;
    SearchView searchViewProject, searchViewTask;
    TextView textViewHeader, textViewProjecName, textViewTaskName, textViewLableFinishDate;
    ImageView imgFilterDate, imgFilterStatus, imgFilterTrafficelights;
    ImageView imgSortProject, imgSortTask, imgRefresh;
    ImageView imgPlusDate, imgPlusTraffic, imgPlusStatus;
    LinearLayout linearCalendar, linearListview, linearDatepanel, linearMain;
    LinearLayout linearAgreegate;
    LinearLayout linearMiddlePanel;
    View viewBlank;
    RelativeLayout relativeHeader, relativeCalendar;
    LinearLayout.LayoutParams paramListview;
    LinearLayout.LayoutParams paramCalendar;
    LinearLayout.LayoutParams paramHeader;
    LinearLayout.LayoutParams paramDatepanel;

    LinearLayout.LayoutParams paramCalendarWidth;
    LinearLayout.LayoutParams paramAgreegateWidth;
    LinearLayout.LayoutParams paramViewBlank;


    //DatabaseHandler dbHandler;
    //PreferencesHelper preferenes;
    TaskListModelClass tasklistModelTemp;
    View rootView;
    // all adapter
    AgreegateAdapter adapterAgreegate;
    TasklistAdapter adapterTasklist = null;
    DateFilterDialogAdapter adapterDate;
    StatusFilterDialogAdapter adapterStatus;
    TrafficFilterDialogAdapter adapterTrafficLight;
    Boolean isFirsttimeClicked = true;
    String strFinalResponsesStringTemp = "";
    String clickedProjectId = "", clickedTaskId = "", clickedParentId = "", clickedGroupId = "";
    PopupWindow popupWindowDate, popupWindowStatus, popupWindowTraffic, popupWindowFinishDates;
    boolean isPopupDate = false, isPopupStatus = false, isPopupTraffic = false, isPopupFinishDate = false;
    boolean isAscendingProject = false, isAscendingTask = false;
    String strTaskFilter = "", strProjectFilter = "";
    ArrayList<String> arrayFinishDate = new ArrayList<>();
    ArrayList<String> arrayTrafficLightfilter = new ArrayList<>();
    ArrayList<String> arrayStatusfilter = new ArrayList<>();
    ArrayList<String> arrayDatefilter = new ArrayList<>();
    boolean isFilterFired = false;
    int totRecord = 10;
    int startRecord = 0;
    int endRecord = 0;
    ArrayList<String> arraylistStringDates = new ArrayList<>();
    String[] dateArray = null;
    ArrayList<TaskListModelClass> arrayTasklistAllDataPermenent = new ArrayList<>();
    ArrayList<TaskListModelClass> arrayTasklistafterFiltered = new ArrayList<>();
    ArrayList<TaskListModelClass> arrayTasklistLastTimeLoaded = new ArrayList<>();
    ArrayList<TaskListModelClass> arrayTasklistLastTimeLoaded2 = new ArrayList<>();
    ArrayList<JSONObject> arraylistJsonObjChildren = new ArrayList<JSONObject>();
    /// json elements
    JSONObject child;
    JSONArray jsonarrayAsignmentdata;
    JSONArray jsonarrayResources;
    JSONArray jsonarrayTaskAssignedTree;
    JSONArray jsonarrayTaskCompleteNextFiveDayTree;
    JSONArray jsonarrayTaskCompleteTodayTree;
    JSONArray jsonarrayTaskCompleteTree;
    JSONArray jsonarrayTaskPendingTree;
    JSONObject sentObje = new JSONObject();
    private View footerViewforListview;
    private Button footerButtonforListview;
    private TextView textViewDone, textviewCurrentMonth, textviewCurrentDate, textviewCurrentDate2,
            textviewSelectedDates, textviewLabledate, textviewLableTraffic, textviewLableStatus;
    private ListView listviewAgreegateList;
    ImageView imgDatePicker;
    ListView listviewTasklist;
    private ArrayList<String> arraylistFinishDates;
    private ArrayList<FilterModelClass> arraylistDatesToFilter;
    private ArrayList<AgreegateDataModelClass> arraylistAgreegate;
    private ArrayList<TaskListModelClass> arraylistTasklistforAdapter = new ArrayList<>();
    private ArrayList<TaskListModelClass> arraylistTaskAssignedTree = new ArrayList<>();
    private ArrayList<TaskListModelClass> arraylistTaskCompleteNextFiveDayTree = new ArrayList<>();
    private ArrayList<TaskListModelClass> arraylistTaskCompleteTodayTree = new ArrayList<>();
    private ArrayList<TaskListModelClass> arraylistTaskCompleteTree = new ArrayList<>();
    private ArrayList<TaskListModelClass> arraylistTaskPendingTree = new ArrayList<>();
    private ArrayList<FilterModelClass> arraylistPopupViewforFilter = new ArrayList<>();

    ProgressDialog progressDialog = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState2) {
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //MyApplication.component(getActivity()).inject(this);
        preferenes = new PreferencesHelper(getActivity());
        dbHandler = new DatabaseHandler(getActivity());
        preferenes.saveIsfirstTime(true);
        preferenes.saveShowToday(true);
        preferenes.saveIsFirsttimeToLoadControl("yes");
        bundle = savedInstanceState2;
        initControl();
        setCaldroidFragment(savedInstanceState2);
        return rootView;
    }

    //    this method intialize control and class all initial work
    public void initControl() {
        GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);
        GlobalClass.fontLight = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPathLight);
        GlobalClass.fontBold = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPathBold);
        GlobalClass.fontMedium = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPathMedium);
        /* footer button for load more records on listview fotter button */
        footerViewforListview = ((LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.footer_list_button, null, false);
        footerButtonforListview = (Button) rootView.findViewById(R.id.buttonLoadMore);
        footerButtonforListview.setOnClickListener(this);

        arraylistFinishDates = new ArrayList<>();

        relativeCalendar = (RelativeLayout) rootView.findViewById(R.id.relativeCalendar);
        viewBlank = (View) rootView.findViewById(R.id.viewblank);
        linearAgreegate = (LinearLayout) rootView.findViewById(R.id.linearagreegate);
        linearMiddlePanel = (LinearLayout) rootView.findViewById(R.id.linearmiddlepanel);

        linearCalendar = (LinearLayout) rootView.findViewById(R.id.linearCalendar);
        linearListview = (LinearLayout) rootView.findViewById(R.id.linearListview);
        linearDatepanel = (LinearLayout) rootView.findViewById(R.id.datepanel);
        relativeHeader = (RelativeLayout) rootView.findViewById(R.id.header);
        linearMain = (LinearLayout) rootView.findViewById(R.id.linearMain);

        GlobalClass.setupTypeface(linearMain, GlobalClass.fontRegular);
        GlobalClass.setupTypeface(linearMiddlePanel, GlobalClass.fontLight);


        textViewHeader = (TextView) rootView.findViewById(R.id.txt_header);
        textViewHeader.setTypeface(GlobalClass.fontMedium);
        textViewProjecName = (TextView) rootView.findViewById(R.id.txt_lbl_project);
        textViewTaskName = (TextView) rootView.findViewById(R.id.txt_lbl_task);
        textViewLableFinishDate = (TextView) rootView.findViewById(R.id.txt_lbl_Finishdate);
        textViewDone = (TextView) rootView.findViewById(R.id.textviewDone);
        //textViewDone.setOnTouchListener(new CustomTouchListener());
        setTouchListner(textViewDone);

        imgPlusDate = (ImageView) rootView.findViewById(R.id.imgplus_date);
        imgPlusStatus = (ImageView) rootView.findViewById(R.id.imgplus_status);
        imgPlusTraffic = (ImageView) rootView.findViewById(R.id.imgplus_traffic);

        imgPlusDate.setVisibility(View.INVISIBLE);
        imgPlusStatus.setVisibility(View.INVISIBLE);
        imgPlusTraffic.setVisibility(View.INVISIBLE);

        imgDatePicker = (ImageView) rootView.findViewById(R.id.img_date_picker);
        imgDatePicker.setOnClickListener(this);
        imgFilterDate = (ImageView) rootView.findViewById(R.id.imgsortdateicon);
        imgFilterStatus = (ImageView) rootView.findViewById(R.id.imgstatussorticon);
        imgFilterTrafficelights = (ImageView) rootView.findViewById(R.id.imgtrafficsorticon);
        imgRefresh = (ImageView) rootView.findViewById(R.id.imgrefresh);
        imgRefresh.setOnClickListener(this);
        imgFilterDate.setBackgroundResource(R.drawable.date_icon);
        imgFilterTrafficelights.setBackgroundResource(R.drawable.traffic_lights);
        imgFilterStatus.setBackgroundResource(R.drawable.status_icon);
        imgSortProject = (ImageView) rootView.findViewById(R.id.imgsortproject);
        imgSortTask = (ImageView) rootView.findViewById(R.id.imgsorttask);
        imgSortProject.setOnClickListener(this);
        imgSortTask.setOnClickListener(this);
        imgFilterDate.setOnClickListener(this);
        imgFilterTrafficelights.setOnClickListener(this);
        imgFilterStatus.setOnClickListener(this);

        textviewSelectedDates = (TextView) rootView.findViewById(R.id.textviewSelectedDates);
        textViewDone = (TextView) rootView.findViewById(R.id.textviewDone);
        textViewDone.setOnClickListener(this);
        textviewSelectedDates.setOnClickListener(this);
        //textviewCurrentMonth = (TextView) rootView.findViewById(R.id.textviewcurrentmonth);
//        textviewCurrentMonth.setOnClickListener(this);
        textviewCurrentDate = (TextView) rootView.findViewById(R.id.textviewcurrentdate);
        textviewCurrentDate2 = (TextView) rootView.findViewById(R.id.textviewcurrentdate2);
        textviewLabledate = (TextView) rootView.findViewById(R.id.txt_lbl_date);
        textviewLableTraffic = (TextView) rootView.findViewById(R.id.txt_lbl_trafficlight);
        textviewLableStatus = (TextView) rootView.findViewById(R.id.txt_lbl_status);

        if (GlobalClass.getScreenOrientation(getActivity()) == 1) {
            // for portrait

            /* this is to set HEIGHT as orientation change */
            linearMain.setWeightSum(1f);
            paramCalendar = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.27f);
            paramListview = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.61f);
            paramHeader = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.06f);
            paramDatepanel = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.05f);

            /* this is to set WIDTH as orientation change */
            paramAgreegateWidth = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 0.46f);
            paramCalendarWidth = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 0.46f);
            paramViewBlank = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 0.04f);
        } else {
            linearMain.setWeightSum(1f);
            /// for landscape
            /* this is to set HEIGHT as orientation change */
            paramCalendar = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.40f);
            paramListview = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.48f);
            paramHeader = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.06f);
            paramDatepanel = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.05f);

            /* this is to set WIDTH as orientation change */
            paramAgreegateWidth = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 0.34f);
            paramCalendarWidth = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 0.52f);
            paramViewBlank = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 0.10f);
        }
        linearListview.setLayoutParams(paramListview);
        linearCalendar.setLayoutParams(paramCalendar);
        linearDatepanel.setLayoutParams(paramDatepanel);
        relativeHeader.setLayoutParams(paramHeader);

        relativeCalendar.setLayoutParams(paramCalendarWidth);
        linearAgreegate.setLayoutParams(paramAgreegateWidth);
        viewBlank.setLayoutParams(paramViewBlank);


        long date = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String dateString = formatter.format(date);

        textviewCurrentDate.setText(GlobalClass.dateFormateChanged(new Date()));
        textviewCurrentDate2.setText(GlobalClass.dateFormateChanged(new Date()));
        listviewAgreegateList = (ListView) rootView.findViewById(R.id.listviewtaskstatuslist);
        listviewAgreegateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (arraylistAgreegate.get(position).isClickable()) {
                    if (preferenes.getClickedAgreegatePosition().equalsIgnoreCase("")) {
                        preferenes.saveClickedAgreegatePosition(String.valueOf(position));
                        if (preferenes.getAgreegateLastFiredFilter().equalsIgnoreCase(String.valueOf(position))) {
                            preferenes.saveIsGeneralFilterDialogshow("no");
                            preferenes.saveCurrentFiredFilter("4");
                        } else {
                            preferenes.saveIsGeneralFilterDialogshow("yes");
                            preferenes.saveCurrentFiredFilter("3");
                        }
                    } else if (preferenes.getClickedAgreegatePosition().equalsIgnoreCase(String.valueOf(position))) {
                        preferenes.saveClickedAgreegatePosition(String.valueOf(position));
                        preferenes.saveCurrentFiredFilter("4");

                        if (preferenes.getAgreegateLastFiredFilter().equalsIgnoreCase(String.valueOf(position))) {
                            preferenes.saveIsGeneralFilterDialogshow("no");
                        } else {
                            if (preferenes.getIsGeneralFilterDialogshow().equalsIgnoreCase("noclicked")) {
                                preferenes.saveIsGeneralFilterDialogshow("yes");
                                preferenes.saveCurrentFiredFilter("3");
                            } else {
                                preferenes.saveIsGeneralFilterDialogshow("no");
                                preferenes.saveCurrentFiredFilter("3");
                            }
                        }
                    } else {
                        preferenes.saveClickedAgreegatePosition(String.valueOf(position));
                        if (preferenes.getAgreegateLastFiredFilter().equalsIgnoreCase(String.valueOf(position))) {
                            preferenes.saveIsGeneralFilterDialogshow("no");
                            preferenes.saveCurrentFiredFilter("4");
                        }
                        /*else if(preferenes.getIsGeneralFilterDialogshow().equalsIgnoreCase("no")){
                            preferenes.saveIsGeneralFilterDialogshow("no");
                            preferenes.saveCurrentFiredFilter("3");
                        }*/
                        else {
                            preferenes.saveIsGeneralFilterDialogshow("yes");
                            preferenes.saveCurrentFiredFilter("3");
                        }
                    }
                    String str = arraylistAgreegate.get(position).getJsonString();
                    String GroupId = arraylistAgreegate.get(position).getStrGroupId();
                    taskclicked.onAgreegateClicked(str, GroupId, "agreegate", position);
                } else {
                }
            }
        });
        listviewTasklist = (ListView) rootView.findViewById(R.id.listviewProject);
        listviewTasklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* getting clicked items value on click */
                clickedProjectId = arraylistTasklistforAdapter.get(position).getStrProjectId();
                clickedTaskId = arraylistTasklistforAdapter.get(position).getStrTaskid();
                clickedParentId = arraylistTasklistforAdapter.get(position).getStrparentId();
                clickedGroupId = arraylistTasklistforAdapter.get(position).getStrGroupId();

                preferenes.saveClickedTaskPosition(String.valueOf(position));

                if (preferenes.getCurrentFiredFilter() != null && !preferenes.getCurrentFiredFilter().
                        equalsIgnoreCase("")) {
                    if (preferenes.getCurrentFiredFilter().equalsIgnoreCase("1")) {
                        if (preferenes.getTaskLastFiredFilter().equalsIgnoreCase(String.valueOf(position))) {
                            preferenes.saveCurrentFiredFilter("2");
                            preferenes.saveIsGeneralFilterDialogshow("no");
                        } else {
                            if (preferenes.getIsGeneralFilterDialogshow().equalsIgnoreCase("noclicked")) {
                                preferenes.saveIsGeneralFilterDialogshow("yes");
                                preferenes.saveCurrentFiredFilter("1");
                            } else {
                                preferenes.saveIsGeneralFilterDialogshow("no");
                                preferenes.saveCurrentFiredFilter("2");
                            }

                        }
                    } else if (preferenes.getCurrentFiredFilter().equalsIgnoreCase("2")) {
                        if (preferenes.getIsGeneralFilterDialogshow().equalsIgnoreCase("noclicked")) {
                            preferenes.saveIsGeneralFilterDialogshow("yes");
                        } else {
                            preferenes.saveIsGeneralFilterDialogshow("no");
                        }
                        preferenes.saveCurrentFiredFilter("2");
                    } else if (preferenes.getCurrentFiredFilter().equalsIgnoreCase("3") ||
                            preferenes.getCurrentFiredFilter().equalsIgnoreCase("4")) {
                        if (preferenes.getTaskLastFiredFilter().equalsIgnoreCase(String.valueOf(position))) {
                            preferenes.saveIsGeneralFilterDialogshow("no");
                            preferenes.saveCurrentFiredFilter("2");
                            preferenes.clearClickedAgreegatePosition();
                        } else if (preferenes.getIsGeneralFilterDialogshow().equalsIgnoreCase("noclicked")) {
                            /// problem here when given no
                            if (preferenes.getIsFirsttimeToLoadControl().equalsIgnoreCase("no")) {
                                preferenes.saveIsGeneralFilterDialogshow("yes");
                                preferenes.saveCurrentFiredFilter("1");
                                preferenes.clearClickedAgreegatePosition();
                                preferenes.saveIsFirsttimeToLoadControl("");
                            } else if (preferenes.getLastDashboardFiredFilter().equalsIgnoreCase("agreegate")) {
                                preferenes.saveIsGeneralFilterDialogshow("yes");
                                preferenes.saveCurrentFiredFilter("1");
                                preferenes.clearClickedAgreegatePosition();
                            }
                            ///// facing problem here that's why chang here on 9th feb
                            else if (preferenes.getLastFiredFilterAll().equalsIgnoreCase("general") ||
                                    preferenes.getLastFiredFilterAll().equalsIgnoreCase("general_resource")) {
                                preferenes.saveIsGeneralFilterDialogshow("yes");
                                preferenes.saveCurrentFiredFilter("1");
                                preferenes.clearClickedAgreegatePosition();
                            } else {
                                preferenes.saveIsGeneralFilterDialogshow("no");
                                preferenes.saveCurrentFiredFilter("2");
                                preferenes.clearClickedAgreegatePosition();
                            }

                        } else {
                            preferenes.saveIsGeneralFilterDialogshow("yes");
                            preferenes.saveCurrentFiredFilter("1");
                            preferenes.clearClickedAgreegatePosition();
                        }
                    } else if (preferenes.getCurrentFiredFilter().equalsIgnoreCase("5") ||
                            preferenes.getCurrentFiredFilter().equalsIgnoreCase("6") ||
                            preferenes.getCurrentFiredFilter().equalsIgnoreCase("7") ||
                            preferenes.getCurrentFiredFilter().equalsIgnoreCase("8")) {
                        if (preferenes.getTaskLastFiredFilter().equalsIgnoreCase(String.valueOf(position))) {
                            preferenes.saveIsGeneralFilterDialogshow("no");
                            preferenes.saveCurrentFiredFilter("2");
                            preferenes.clearClickedAgreegatePosition();
                        } else {
                            preferenes.saveIsGeneralFilterDialogshow("yes");
                            preferenes.saveCurrentFiredFilter("1");
                            preferenes.clearClickedAgreegatePosition();
                        }
                    }
                } else {
                    if (preferenes.getTaskLastFiredFilter().equalsIgnoreCase(String.valueOf(position))) {
                        preferenes.saveCurrentFiredFilter("2");
                        preferenes.saveIsGeneralFilterDialogshow("no");
                        preferenes.clearClickedAgreegatePosition();
                    }
                    /*else if(preferenes.getIsGeneralFilterDialogshow().equalsIgnoreCase("no")){
                        preferenes.saveCurrentFiredFilter("1");
                        preferenes.saveIsGeneralFilterDialogshow("no");
                        preferenes.clearClickedAgreegatePosition();
                    }*/
                    else {
                        preferenes.saveCurrentFiredFilter("1");
                        preferenes.saveIsGeneralFilterDialogshow("yes");
                        preferenes.clearClickedAgreegatePosition();
                    }

                }
                taskclicked.ontaskClicked(strNodedata, clickedTaskId, "task", position);
            }
        });
        searchViewProject = (SearchView) rootView.findViewById(R.id.search_viewproject);
        searchViewProject.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String filterText) {
                strProjectFilter = filterText;
                if (adapterTasklist != null) {
                    arrayTasklistafterFiltered = new ArrayList<>();
                    arrayTasklistafterFiltered = adapterTasklist.filterListview(strTaskFilter, strProjectFilter,
                            arrayFinishDate, arrayTrafficLightfilter, arrayStatusfilter, arrayDatefilter,
                            arrayTasklistAllDataPermenent);
                    isFilterFired = true;
                    setAdapterforFilter(arrayTasklistafterFiltered);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String filterText) {
                strProjectFilter = filterText;
                if (filterText.length() == 0) {
                    //searchViewProject.setFocusable(true);
                    //GlobalClass.hideSoftKeyboard(searchViewProject,getActivity());
                    if (adapterTasklist != null) {
                        arrayTasklistafterFiltered = new ArrayList<>();
                        arrayTasklistafterFiltered = adapterTasklist.filterListview(strTaskFilter, strProjectFilter,
                                arrayFinishDate, arrayTrafficLightfilter, arrayStatusfilter, arrayDatefilter,
                                arrayTasklistAllDataPermenent);
                        isFilterFired = true;
                        setAdapterforFilter(arrayTasklistafterFiltered);
                    }
                }
                return false;
            }
        });
        searchViewTask = (SearchView) rootView.findViewById(R.id.search_viewtask);
        searchViewTask.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String filterText) {
                strTaskFilter = filterText;
                if (adapterTasklist != null) {
                    arrayTasklistafterFiltered = new ArrayList<>();
                    arrayTasklistafterFiltered = adapterTasklist.filterListview(strTaskFilter, strProjectFilter,
                            arrayFinishDate, arrayTrafficLightfilter, arrayStatusfilter, arrayDatefilter,
                            arrayTasklistAllDataPermenent);
                    isFilterFired = true;
                    setAdapterforFilter(arrayTasklistafterFiltered);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String filterText) {
                strTaskFilter = filterText;
                if (filterText.length() == 0) {
                    //searchViewTask.setFocusable(true);
                    //GlobalClass.hideSoftKeyboard(searchViewTask,getActivity());
                    if (adapterTasklist != null) {
                        arrayTasklistafterFiltered = new ArrayList<>();
                        arrayTasklistafterFiltered = adapterTasklist.filterListview(strTaskFilter, strProjectFilter,
                                arrayFinishDate, arrayTrafficLightfilter, arrayStatusfilter, arrayDatefilter,
                                arrayTasklistAllDataPermenent);
                        isFilterFired = true;
                        setAdapterforFilter(arrayTasklistafterFiltered);
                    }
                }
                return false;
            }
        });
        nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 50);

        lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -50);


        /*calendarPicker = (CalendarPickerView) rootView.findViewById(R.id.calendar_view);
        calendarPicker.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.RANGE) //
                .withSelectedDate(new Date());*/


        /* getting today's date and pass to service */
        gettingTodayDateForService();
        /* calling dashboard service firtst time */
        if (GlobalClass.isNetworkAvailable(getActivity())) {
            getDashboardData("yes");
        } else {
            //listviewTasklist.addFooterView(footerViewforListview);
            footerButtonforListview.setText(getString(R.string.no_more_data_to_load_0_0));
            addOfflineDatatoAgreegate();
            //GlobalClass.showToastInternet(getActivity());
            GlobalClass.showCustomDialogInternet(getActivity());
        }
        setLanguage(preferenes.getLanguage());
        /* currently comment this code not use but it can be used in future if requiring */
        //showSelectedDates();
        //showSelectedDates();
        //showSelectedTrafficLight();
    }

    //    this method shows datepicker dialog to select dates
    public void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String selectedDate = dayOfMonth + "." + (monthOfYear + 1) + "." + year;
                Date dateselected = GlobalClass.convertStringToDate(selectedDate);
                clickedDate = dateselected;
                if (startDate != null && endDate != null) {
                    if (clickedDate.after(new Date()) || clickedDate.equals(new Date())) {
                        if(startDate.after(new Date())){
                            preferenes.saveShowToday(true);
                        }else {
                            preferenes.saveShowToday(false);
                        }
                    }
                    else if(clickedDate.before(new Date()) && endDate.after(new Date())){
                        preferenes.saveShowToday(false);
                    }
                    else {
                        if (endDate.after(new Date())) {
                            if(startDate.after(new Date())){
                                preferenes.saveShowToday(true);
                            }else {
                                preferenes.saveShowToday(false);
                            }

                        } else {
                            preferenes.saveShowToday(true);
                        }
                    }
                    if (clickedDate.equals(startDate) && startDate.equals(endDate)) {
                        startDate = null;
                        endDate = null;
                        isFirsttimeClicked=true;
                        caldroidFragment.setSelectedDates(startDate, endDate);
                        caldroidFragment.refreshView();
                    } else if (clickedDate.before(startDate)) {
                        startDate = clickedDate;
                    } else if (clickedDate.after(endDate)) {
                        endDate = clickedDate;
                        isFirsttimeClicked=true;
                    } else if (clickedDate.after(startDate)) {
                        endDate = clickedDate;
                    } else if (clickedDate.equals(startDate) || clickedDate.equals(endDate)) {
                        startDate = clickedDate;
                        endDate = clickedDate;
                    }
                } else {
                    if (startDate != null) {
                        endDate = clickedDate;
                    } else {
                        startDate = clickedDate;
                        endDate = clickedDate;
                    }
                    Date temp = null;
                    if (startDate.after(endDate)) {
                        temp = endDate;
                        endDate = startDate;
                        startDate = temp;

                    }
                }
                caldroidFragment.setSelectedDates(startDate, endDate);
                caldroidFragment.refreshView();
                /*Log.e(" %%%%%%% ", " called interface : " + Dateselected);
                allDatesBetweenDates = new ArrayList<>();
                allDatesBetweenDates = calendarPicker.getSelectedDates();
                if (allDatesBetweenDates.size() > 0) {
                    if (allDatesBetweenDates.contains(Dateselected)) {
                        if (Dateselected.after(allDatesBetweenDates.get(0))) {
                            selectedDatesStartEnd = new ArrayList<>();
                            selectedDatesStartEnd.add(allDatesBetweenDates.get(0));
                            selectedDatesStartEnd.add(Dateselected);
                            calendarPicker.init(lastYear.getTime(), nextYear.getTime()) //
                                    .inMode(CalendarPickerView.SelectionMode.RANGE) //
                                    .withSelectedDates(selectedDatesStartEnd);
                        }
                        else {
                            if (Dateselected.equals(allDatesBetweenDates.get(0))) {
                                selectedDatesStartEnd = new ArrayList<>();
                                selectedDatesStartEnd.add(Dateselected);
                                ///selectedDatesStartEnd.add(allDatesBetweenDates.get(allDatesBetweenDates.size()-1));
                                calendarPicker.init(lastYear.getTime(), nextYear.getTime()) //
                                        .inMode(CalendarPickerView.SelectionMode.RANGE) //
                                        .withSelectedDates(selectedDatesStartEnd);
                            }
                            else {
                                selectedDatesStartEnd = new ArrayList<>();
                                selectedDatesStartEnd.add(Dateselected);
                                selectedDatesStartEnd.add(allDatesBetweenDates.get(allDatesBetweenDates.size() - 1));
                                calendarPicker.init(lastYear.getTime(), nextYear.getTime()) //
                                        .inMode(CalendarPickerView.SelectionMode.RANGE) //
                                        .withSelectedDates(selectedDatesStartEnd);
                            }
                        }
                    }
                    else {
                        selectedDatesStartEnd = new ArrayList<>();
                        allDatesBetweenDates.add(Dateselected);
                        Collections.sort(allDatesBetweenDates);

                        selectedDatesStartEnd.add(allDatesBetweenDates.get(0));
                        selectedDatesStartEnd.add(allDatesBetweenDates.get(allDatesBetweenDates.size() - 1));
                        calendarPicker.init(lastYear.getTime(), nextYear.getTime()) //
                                .inMode(CalendarPickerView.SelectionMode.RANGE) //
                                .withSelectedDates(selectedDatesStartEnd);
                    }
                }
                else {
                    selectedDatesStartEnd = new ArrayList<>();
                    selectedDatesStartEnd.add(Dateselected);
                    calendarPicker.init(lastYear.getTime(), nextYear.getTime()) //
                            .inMode(CalendarPickerView.SelectionMode.RANGE) //
                            .withSelectedDates(selectedDatesStartEnd);
                }*/
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    //    this method is for dismissing popup passing any object of popupview
    public void dismissPopup(PopupWindow popup) {
        if (popup != null && popup.isShowing()) {
            popup.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        /* listview foooter to load more records*/
        switch (v.getId()) {
            case R.id.img_date_picker:
                //showDatePickerDialog(getActivity());
                break;
            case R.id.buttonLoadMore:
                paginationForListview();
                break;
            case R.id.textviewDone:
                if (startDateLast != null && endDateLast != null) {
                    if(startDate != null && endDate != null){
                        if(startDate == startDateLast && endDate == endDateLast){

                        }
                        else {
                            doneButoonTocallService();
                        }
                    }
                } else {
                    doneButoonTocallService();
                }
                break;
            case R.id.textviewSelectedDates:
                dismissPopup(popupWindowDate);
                dismissPopup(popupWindowStatus);
                dismissPopup(popupWindowTraffic);
                if (popupWindowFinishDates != null) {
                    if (popupWindowFinishDates.isShowing()) {
                        if (popupWindowFinishDates != null) {
                            popupWindowFinishDates.dismiss();
                            isPopupFinishDate = false;
                        }
                    } else {
                /* this function will store data in arraylist to show in popup basen on condition */
                        //addingDataInPopup(4);
                /* this will show popup of filtering based on contioin */
                        //showPopupWindow(4);
                        showSelectedDatesDialog();
                        isPopupFinishDate = true;
                    }
                } else {
                    showSelectedDatesDialog();
                    isPopupFinishDate = true;
                }
                break;
            case R.id.imgsortdateicon:
                dismissPopup(popupWindowFinishDates);
                dismissPopup(popupWindowStatus);
                dismissPopup(popupWindowTraffic);
                if (popupWindowDate != null) {
                    if (popupWindowDate.isShowing()) {
                        if (popupWindowDate != null) {
                            popupWindowDate.dismiss();
                            isPopupDate = false;
                        }
                    } else {
                /* this function will store data in arraylist to show in popup basen on condition */
                        addingDataInPopup(1);
                /* this will show popup of filtering based on contioin */
                        showPopupWindow(1);
                        isPopupDate = true;
                    }
                } else {
                 /* this function will store data in arraylist to show in popup basen on condition */
                    addingDataInPopup(1);
                /* this will show popup of filtering based on contioin */
                    showPopupWindow(1);
                    isPopupDate = true;
                }
                break;
            case R.id.imgstatussorticon:
                dismissPopup(popupWindowFinishDates);
                dismissPopup(popupWindowDate);
                dismissPopup(popupWindowTraffic);
                if (popupWindowStatus != null) {
                    if (popupWindowStatus.isShowing()) {
                        if(popupWindowStatus != null){
                            popupWindowStatus.dismiss();
                        }
                        isPopupStatus = false;
                    } else {
                /* this function will store data in arraylist to show in popup basen on condition */
                        addingDataInPopup(2);
                /* this will show popup of filtering based on contioin */
                        showPopupWindow(2);
                        isPopupStatus = true;
                    }
                } else {
                /* this function will store data in arraylist to show in popup basen on condition */
                    addingDataInPopup(2);
                /* this will show popup of filtering based on contioin */
                    showPopupWindow(2);
                    isPopupStatus = true;
                }
                break;
            case R.id.imgtrafficsorticon:
                dismissPopup(popupWindowFinishDates);
                dismissPopup(popupWindowDate);
                dismissPopup(popupWindowStatus);
                if (popupWindowTraffic != null) {
                    if (popupWindowTraffic.isShowing()) {
                        if(popupWindowTraffic != null){
                            popupWindowTraffic.dismiss();
                        }
                        isPopupTraffic = false;
                    } else {
                /* this function will store data in arraylist to show in popup basen on condition */
                        addingDataInPopup(3);
                /* this will show popup of filtering based on contioin */
                        showPopupWindow(3);
                        isPopupTraffic = true;
                    }
                } else {
               /* this function will store data in arraylist to show in popup basen on condition */
                    addingDataInPopup(3);
                /* this will show popup of filtering based on contioin */
                    showPopupWindow(3);
                    isPopupTraffic = true;
                }
                break;
            case R.id.imgsortproject:
                if (arraylistTasklistforAdapter.size() > 0) {
                    if (adapterTasklist != null) {
                        if (isAscendingProject) {
                            //((TextView)rootView.findViewById(R.id.labelAgendaWeeklyDueName)).setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, R.drawable.ic_sort_asc_24dp);
                            imgSortProject.setBackgroundResource(R.drawable.asending);
                            Collections.sort(arraylistTasklistforAdapter, new TaskComparableClass(1, 0));
                            isAscendingProject = false;
                        } else {
                            imgSortProject.setBackgroundResource(R.drawable.desending);
                            Collections.sort(arraylistTasklistforAdapter, new TaskComparableClass(1, 1));
                            isAscendingProject = true;
                        }
                        adapterTasklist.notifyDataSetChanged();

                    }
                }
                break;
            case R.id.imgsorttask:
                if (arraylistTasklistforAdapter.size() > 0) {
                    if (adapterTasklist != null) {
                        if (isAscendingTask) {
                            imgSortTask.setBackgroundResource(R.drawable.asending);
                            Collections.sort(arraylistTasklistforAdapter, new TaskComparableClass(2, 0));
                            isAscendingTask = false;
                        } else {
                            imgSortTask.setBackgroundResource(R.drawable.desending);
                            Collections.sort(arraylistTasklistforAdapter, new TaskComparableClass(2, 1));
                            isAscendingTask = true;
                        }
                        adapterTasklist.notifyDataSetChanged();
                    }
                }
                break;
            case R.id.imgrefresh:
                refreshActivityAndCallService();
                imgPlusDate.setVisibility(View.GONE);
                imgPlusTraffic.setVisibility(View.GONE);
                imgPlusStatus.setVisibility(View.GONE);
                textviewSelectedDates.setText(getString(R.string.selected_date));
                searchViewProject.setQuery("", false);
                searchViewProject.clearFocus();
                searchViewProject.setFocusable(false);
                searchViewTask.setQuery("", false);
                searchViewTask.clearFocus();
                searchViewTask.setFocusable(false);
                break;
        }
    }

    /* this method will call get new device token service to get refreshed device token */
    public void updateAccessToken() {
        GenericHelper helper = new GenericHelper(getActivity());
        Call<String> call = helper.getRetrofit().getNewAccessToken();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                LogApp.e(" ##### ", " response from get device token : " + response.toString());
                Headers header = response.headers();
                try {
                    if (header.get(getResources().getString(R.string.x_status)) != null &&
                            Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1010) {
                        if (header.get(getResources().getString(R.string.x_message)) != null) {
                            String token = header.get(getResources().getString(R.string.x_message));
                                /* saving access token in preference to send in every service header */
                                /* access token = getfromservie+useridLength+userid+epochtime*/
                            String strFinalAccessToken = token + preferenes.getUserID().length() + preferenes.getUserID();

                            preferenes.saveAccessToken(strFinalAccessToken);
                            GlobalClass.strAccessToken = preferenes.getAccessToken();
                            getDashboardData("no");
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

    //    this method call dashbord servcie and parse response
    private void getDashboardData(String str) {
        try {
            refreshArraylst();
            if(str.equalsIgnoreCase("yes")){
                progressDialog = new ProgressDialog(getActivity());
                //progressDialog.create();

                SpannableString ss = new SpannableString(getString(R.string.please_wait));
                ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new CustomTypefaceSpan("", GlobalClass.fontRegular), 0, ss.length(), 0);

                progressDialog.setMessage(ss);
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
            }
            String jsonDateString = JsonString(dateArray);
            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"),preferenes.getUserID());
            RequestBody dateRange = RequestBody.create(MediaType.parse("text/plain"), jsonDateString);
            RequestBody projectSearch = RequestBody.create(MediaType.parse("text/plain"), "");
            RequestBody taskSearch = RequestBody.create(MediaType.parse("text/plain"), "");
            RequestBody projectOrder = RequestBody.create(MediaType.parse("text/plain"), "1");
            RequestBody projectLimit = RequestBody.create(MediaType.parse("text/plain"), "12");
            RequestBody taskOrder = RequestBody.create(MediaType.parse("text/plain"), "1");

            GenericHelper helper = new GenericHelper(getActivity());
            Call<String> call = helper.getRetrofit().getDashboard(dateRange, projectSearch,
                    taskSearch, projectOrder, projectLimit, taskOrder);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Headers header = response.headers();
                    try {
                        if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 0) {
                            String finalResponseString = new Gson().toJson(response);
                            LogApp.e(" 555555 ", " response from retro : " + finalResponseString);
                            strFinalResponsesStringTemp = finalResponseString;
                            List<TaskListModelClass> arrayTask = new ArrayList<TaskListModelClass>();
                            try {
                                JSONObject obj = (JSONObject) new JSONTokener(finalResponseString).nextValue();
                                String body = obj.getString("body");
                                JSONObject objMain = (JSONObject) new JSONTokener(body).nextValue();

                                String status = objMain.getString("Status");
                                if (status.equalsIgnoreCase("0")) {
                                    JSONObject objPayload = objMain.getJSONObject("Payload");
                                    JSONArray jsonArrayMain = objPayload.getJSONArray("NodeData");
                                    strNodedata = jsonArrayMain.toString();
                                    JSONObject objResourceAssignement = objPayload.getJSONObject("ResourceAssignments");
                                    jsonarrayAsignmentdata = objResourceAssignement.getJSONArray("assignmentdata");
                                    jsonarrayResources = objResourceAssignement.getJSONArray("resources");

                                    jsonarrayTaskAssignedTree = objPayload.getJSONArray("TaskAssignedTree");
                                    jsonarrayTaskCompleteNextFiveDayTree = objPayload.getJSONArray("TaskCompleteNextFiveDayTree");
                                    jsonarrayTaskCompleteTodayTree = objPayload.getJSONArray("TaskCompleteTodayTree");
                                    jsonarrayTaskCompleteTree = objPayload.getJSONArray("TaskCompleteTree");
                                    jsonarrayTaskPendingTree = objPayload.getJSONArray("TaskPendingTree");
                                    arraylistAgreegate = new ArrayList<AgreegateDataModelClass>();

                                    //// TaskPendingTree
                                    if (jsonarrayTaskPendingTree.length() > 0) {
                                        getTaskCountofJsonArray(jsonarrayTaskPendingTree, 5);

                                        AgreegateDataModelClass model = new AgreegateDataModelClass();
                                        model.setStrName(getString(R.string.task_pending_tree));
                                        model.setDrawable(R.drawable.pending_task);
                                        model.setStrNo(String.valueOf(arraylistTaskPendingTree.size()));
                                        model.setJsonString(jsonarrayTaskPendingTree.toString());
                                        model.setStrGroupId(arraylistTaskPendingTree.get(0).getGroupId());
                                        if (arraylistTaskPendingTree.size() > 0) {
                                            model.setClickable(true);
                                        } else {
                                            model.setClickable(false);
                                        }
                                        arraylistAgreegate.add(model);
                                    } else {
                                        AgreegateDataModelClass model = new AgreegateDataModelClass();
                                        model.setStrName(getString(R.string.task_pending_tree));
                                        model.setDrawable(R.drawable.pending_task);
                                        model.setStrNo(String.valueOf(arraylistTaskPendingTree.size()));
                                        model.setJsonString("");
                                        model.setStrGroupId("");
                                        model.setClickable(false);
                                        arraylistAgreegate.add(model);
                                    }

                                    ///// TaskCompleteTree
                                    if (jsonarrayTaskCompleteTree.length() > 0) {
                                        getTaskCountofJsonArray(jsonarrayTaskCompleteTree, 4);

                                        AgreegateDataModelClass model = new AgreegateDataModelClass();
                                        model.setStrName(getString(R.string.task_completed_tree));
                                        model.setDrawable(R.drawable.completed_task);
                                        model.setStrNo(String.valueOf(arraylistTaskCompleteTree.size()));
                                        model.setJsonString(jsonarrayTaskCompleteTree.toString());
                                        model.setStrGroupId(arraylistTaskCompleteTree.get(0).getGroupId());
                                        if (arraylistTaskCompleteTree.size() > 0) {
                                            model.setClickable(true);
                                        } else {
                                            model.setClickable(false);
                                        }
                                        arraylistAgreegate.add(model);
                                    } else {
                                        AgreegateDataModelClass model = new AgreegateDataModelClass();
                                        model.setStrName(getString(R.string.task_completed_tree));
                                        model.setDrawable(R.drawable.completed_task);
                                        model.setStrNo(String.valueOf(arraylistTaskCompleteTree.size()));
                                        model.setJsonString("");
                                        model.setStrGroupId("");
                                        model.setClickable(false);
                                        arraylistAgreegate.add(model);
                                    }

                                    ///// TaskAssignedTree
                                    arraylistTaskAssignedTree.clear();
                                    arraylistTaskCompleteNextFiveDayTree.clear();
                                    arraylistTaskCompleteTodayTree.clear();
                                    arraylistTaskCompleteTree.clear();
                                    arraylistTaskPendingTree.clear();
                                    if (jsonarrayTaskAssignedTree.length() > 0) {
                                        getTaskCountofJsonArray(jsonarrayTaskAssignedTree, 1);
                                        AgreegateDataModelClass model = new AgreegateDataModelClass();

                                        model.setStrName(getString(R.string.task_assign_tree));
                                        model.setDrawable(R.drawable.started_task);
                                        model.setStrNo(String.valueOf(arraylistTaskAssignedTree.size()));
                                        model.setJsonString(jsonarrayTaskAssignedTree.toString());
                                        model.setStrGroupId(arraylistTaskAssignedTree.get(0).getGroupId());
                                        if (arraylistTaskAssignedTree.size() > 0) {
                                            model.setClickable(true);
                                        } else {
                                            model.setClickable(false);
                                        }
                                        arraylistAgreegate.add(model);

                                    } else {
                                        AgreegateDataModelClass model = new AgreegateDataModelClass();
                                        model.setStrName(getString(R.string.task_assign_tree));
                                        model.setDrawable(R.drawable.started_task);
                                        model.setStrNo(String.valueOf(arraylistTaskAssignedTree.size()));
                                        model.setJsonString("");
                                        model.setStrGroupId("");
                                        model.setClickable(false);
                                        arraylistAgreegate.add(model);
                                    }
                                    //// TaskCompleteTodayTree
                                    if (jsonarrayTaskCompleteTodayTree.length() > 0) {
                                        LogApp.e(" eeeee ", " json array of aggregate : " +
                                                jsonarrayTaskCompleteTodayTree.toString());
                                        getTaskCountofJsonArray(jsonarrayTaskCompleteTodayTree, 3);

                                        AgreegateDataModelClass model = new AgreegateDataModelClass();
                                        model.setStrName(getString(R.string.task_completed_today_tree));
                                        model.setDrawable(R.drawable.completed_today);
                                        model.setStrNo(String.valueOf(arraylistTaskCompleteTodayTree.size()));
                                        model.setJsonString(jsonarrayTaskCompleteTodayTree.toString());
                                        model.setStrGroupId(arraylistTaskCompleteTodayTree.get(0).getGroupId());
                                        if (arraylistTaskCompleteTodayTree.size() > 0) {
                                            model.setClickable(true);
                                        } else {
                                            model.setClickable(false);
                                        }
                                        arraylistAgreegate.add(model);
                                    } else {
                                        AgreegateDataModelClass model = new AgreegateDataModelClass();
                                        model.setStrName(getString(R.string.task_completed_today_tree));
                                        model.setDrawable(R.drawable.completed_today);
                                        model.setStrNo(String.valueOf(arraylistTaskCompleteTodayTree.size()));
                                        model.setJsonString("");
                                        model.setStrGroupId("");
                                        model.setClickable(false);
                                        arraylistAgreegate.add(model);
                                    }

                                    ///// TaskCompleteNextFiveDayTree
                                    if (jsonarrayTaskCompleteNextFiveDayTree.length() > 0) {
                                        getTaskCountofJsonArray(jsonarrayTaskCompleteNextFiveDayTree, 2);

                                        AgreegateDataModelClass model = new AgreegateDataModelClass();
                                        model.setStrName(getString(R.string.task_completed_next_five_day));
                                        model.setDrawable(R.drawable.completed_in_one_week);
                                        model.setStrNo(String.valueOf(arraylistTaskCompleteNextFiveDayTree.size()));
                                        model.setJsonString(jsonarrayTaskCompleteNextFiveDayTree.toString());
                                        model.setStrGroupId(arraylistTaskCompleteNextFiveDayTree.get(0).getGroupId());
                                        if (arraylistTaskCompleteNextFiveDayTree.size() > 0) {
                                            model.setClickable(true);
                                        } else {
                                            model.setClickable(false);
                                        }
                                        arraylistAgreegate.add(model);
                                    } else {
                                        AgreegateDataModelClass model = new AgreegateDataModelClass();
                                        model.setStrName(getString(R.string.task_completed_next_five_day));
                                        model.setDrawable(R.drawable.completed_in_one_week);
                                        model.setStrNo(String.valueOf(arraylistTaskCompleteNextFiveDayTree.size()));
                                        model.setJsonString("");
                                        model.setStrGroupId("");
                                        model.setClickable(false);
                                        arraylistAgreegate.add(model);
                                    }


                                    if (jsonArrayMain.length() > 0) {
                                        arraylistTasklistforAdapter.clear();
                                        arrayTasklistLastTimeLoaded.clear();
                                      /*  arraylistFinishDates.clear();*/
                                        isFirsttimeClicked = true;
                                    } else {
                                        arraylistTasklistforAdapter.clear();
                                        arrayTasklistLastTimeLoaded.clear();
                                        /*arraylistFinishDates.clear();*/
                                        listviewTasklist.setAdapter(null);
                                    }
                                    for (int i = 0; i < jsonArrayMain.length(); i++) {
                                        JSONObject objInner = jsonArrayMain.getJSONObject(i);
                                        LogApp.e(" in for loop : ", " Node object in string : " +
                                                jsonArrayMain.getJSONObject(i));
                                        JSONArray jsonArrayInner = objInner.getJSONArray("children");
                                        for (int j = 0; j < jsonArrayInner.length(); j++) {
                                            LogApp.e(" in for loop : ", " Project object in string : " +
                                                    jsonArrayInner.getJSONObject(j));

                                            String ProjectName = jsonArrayInner.getJSONObject(j).get("Name").toString();
                                            String groupId = jsonArrayInner.getJSONObject(j).get("GroupId").toString();
                                            String projectId = jsonArrayInner.getJSONObject(j).get("Id").toString();
                                            TaskListModelClass dataEntity = recursionTogetAllTaskforListviewtoSet(jsonArrayInner.getJSONObject(j), ProjectName, groupId, projectId);
                                            arrayTask.add(dataEntity);
                                        }
                                    }
                                    imgSortProject.setBackgroundResource(R.drawable.asending);
                                    imgSortTask.setBackgroundResource(R.drawable.asending);
                                    /* this is user defined function to set adapter*/
                                    setAdapterforTasklist(arraylistTasklistforAdapter);

                                    /* setting adapter for agreegate data */
                                    if (arraylistAgreegate.size() > 0) {
                                        adapterAgreegate = new AgreegateAdapter(getActivity(), arraylistAgreegate);
                                        listviewAgreegateList.setAdapter(adapterAgreegate);
                                    }
                                }

                            } catch (JSONException e) {
                                LogApp.e(" json exception ", " exception while parsing : " + e.toString());
                                e.printStackTrace();
                                arraylistTasklistforAdapter.clear();
                                arrayTasklistLastTimeLoaded.clear();
                                  /*  arraylistFinishDates.clear();*/
                                listviewTasklist.setAdapter(null);
                            }
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }


                        } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1009) {
                                /* whenever access token is expire get 1009
                                from service and call here getNewdeviceToken servcie and replace onld one with new device token */
                            updateAccessToken();
                            /*if(progressDialog != null){
                                progressDialog.dismiss();
                            }*/
                        }
                        else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == Integer.parseInt(getString(R.string.LICENSE_EXPIRED))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            // LICENSE_EXPIRED == SignOut app whene licence expire
                            if(preferenes.isUserLooged()){
                                if(HomeActivity.getInstace()!=null){
                                    HomeActivity.getInstace().showAppLogout();
                                    preferenes.saveShowDialogType(getString(R.string.LICENSE_EXPIRED));
                                }
                            }
                        }
                        else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) ==
                                    Integer.parseInt(getString(R.string.LICENSE_FILE_MISSING))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            // LICENSE_FILE_MISSING == SignOut app when licence file missing
                            if(preferenes.isUserLooged()){
                                if(HomeActivity.getInstace()!=null){
                                    HomeActivity.getInstace().showAppLogout();
                                    preferenes.saveShowDialogType(getString(R.string.LICENSE_FILE_MISSING));
                                }
                            }
                        }
                        else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) ==
                                    Integer.parseInt(getString(R.string.USER_DOES_NOT_HAVE_ROLE))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            //GlobalClass.showToast(activity, getString(R.string.app_right_has_been_revoked));
                            // USER_DOES_NOT_HAVE_ROLE == disable app and go to setting tab when right revoked from user
                            if(preferenes.isUserLooged()){
                                if(HomeActivity.getInstace()!=null){
                                    HomeActivity.getInstace().showAppDisabled(true);
                                    preferenes.saveShowDialogType(getString(R.string.USER_DOES_NOT_HAVE_ROLE));
                                }
                            }
                        }
                        else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) ==
                                        Integer.parseInt(getString(R.string.ORGANIZATION_IS_DISABLED_BY_SYS_ADMIN))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            //GlobalClass.showToast(activity, getString(R.string.something_is_wrong));
                            // ORGANIZATION_IS_DISABLED_BY_SYS_ADMIN == SingOut app when file missing
                            if(preferenes.isUserLooged()){
                                if(HomeActivity.getInstace()!=null){
                                    HomeActivity.getInstace().showAppLogout();
                                    preferenes.saveShowDialogType(getString(R.string.ORGANIZATION_IS_DISABLED_BY_SYS_ADMIN));
                                }
                            }
                        }
                        else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == Integer.parseInt(getString(R.string.ORGANIZATION_USER_IS_DISABLED_BY_SYS_ADMIN))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            //GlobalClass.showToast(activity, getString(R.string.something_is_wrong));
                            // ORGANIZATION_USER_IS_DISABLED_BY_SYS_ADMIN == disalble app and go to setting tab
                            if(preferenes.isUserLooged()){
                                if(HomeActivity.getInstace()!=null){
                                    HomeActivity.getInstace().showAppDisabled(true);
                                    preferenes.saveShowDialogType(getString(R.string.ORGANIZATION_USER_IS_DISABLED_BY_SYS_ADMIN));
                                }
                            }
                        }
                        else {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            if(response.code() == 400){

                            }else {
                                GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.error_title),getString(R.string.something_is_wrong));
                            }
                        }
                    } catch (Exception e) {
                        resetAlllistandAdapter();
                        if(progressDialog != null){
                            progressDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    LogApp.e(" dashboard failed ", " response from service : " + t.toString());
                    resetAlllistandAdapter();
                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }
                    addOfflineDatatoAgreegate();
                    strLoadmore2 = getString(R.string.no_more_data_to_load_0_0_static);
                    footerButtonforListview.setText(getString(R.string.no_more_data_to_load_0_0));
                    if(t instanceof UnknownHostException){
                        //progressDialog.dismiss();
                        //GlobalClass.showToast(activity, getString(R.string.url_not_found));
                        GlobalClass.showCustomMessageDialog(activity,getString(R.string.enter_organization_url_title),getString(R.string.enter_valid_url));
                        preferenes.save404Count(preferenes.get404Count()+1);
                    }
                    else if (t instanceof IOException) {
                        //GlobalClass.showToast(getActivity(), getString(R.string.server_timeout_exception));
                        GlobalClass.showCustomMessageDialog(activity,getString(R.string.server_timeout_exception),getString(R.string.server_timeout_exception));
                    }
                }
            });
        } catch (Exception e) {
            if(progressDialog != null) {
                progressDialog.dismiss();
            }
            GlobalClass.showCustomMessageDialog(activity,getString(R.string.enter_organization_url_title),getString(R.string.enter_valid_url));
            LogApp.e(" while login service : ", " in catch : " + e.toString());
        }
    }

    public void addOfflineDatatoAgreegate() {
        imgSortTask.setBackgroundResource(R.drawable.asending);
        imgSortProject.setBackgroundResource(R.drawable.asending);
        textviewSelectedDates.setTextColor(getResources().getColor(R.color.textview_color_grey));
        listviewTasklist.setAdapter(null);
        arraylistAgreegate = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            AgreegateDataModelClass model = new AgreegateDataModelClass();

            if (i == 0) {
                model.setStrName(getString(R.string.task_pending_tree));
            } else if (i == 1) {
                model.setStrName(getString(R.string.task_completed_tree));
            } else if (i == 2) {
                model.setStrName(getString(R.string.task_assign_tree));
            } else if (i == 3) {
                model.setStrName(getString(R.string.task_completed_today_tree));
            } else if (i == 4) {
                model.setStrName(getString(R.string.task_completed_next_five_day));
            }

            model.setStrNo("0");
            model.setJsonString("");
            model.setStrGroupId("");
            model.setClickable(false);

            arraylistAgreegate.add(model);
            /* setting adapter for agreegate data */

        }
        if (arraylistAgreegate.size() > 0) {
            adapterAgreegate = new AgreegateAdapter(getActivity(), arraylistAgreegate);
            listviewAgreegateList.setAdapter(adapterAgreegate);
        }

    }

    //    this function reset all listview and its adapter
    public void resetAlllistandAdapter() {
        arraylistTaskAssignedTree.clear();
        arraylistTaskCompleteNextFiveDayTree.clear();
        arraylistTaskCompleteTodayTree.clear();
        arraylistTaskCompleteTree.clear();
        arraylistTaskPendingTree.clear();
        listviewAgreegateList.setAdapter(null);
        arraylistTasklistforAdapter.clear();
        arrayTasklistLastTimeLoaded.clear();
        arraylistFinishDates.clear();
        listviewTasklist.setAdapter(null);
        strLoadmore2 = (getString(R.string.no_more_data_to_load_static));
        footerButtonforListview.setText(getString(R.string.no_more_data_to_load));
    }

    // this function show all selected dates
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void showSelectedDatesDialog() {
        if (isFirsttimeClicked) {
            addFinishDatefromAdapter();
        }
        if (arraylistFinishDates.size() > 0) {
            HashSet<String> hashSet = new HashSet<String>();
            hashSet.addAll(arraylistFinishDates);
            arraylistFinishDates.clear();
            arraylistFinishDates.addAll(hashSet);

                /* sorting arraylist*/
            ArrayList<Date> arrayListTemp = new ArrayList<>();
            for (int j = 0; j < arraylistFinishDates.size(); j++) {
                arrayListTemp.add(GlobalClass.convertStringToDate(arraylistFinishDates.get(j)));
            }
            Collections.sort(arrayListTemp);
            arraylistFinishDates = new ArrayList<>();
            for (int k = 0; k < arrayListTemp.size(); k++) {
                arraylistFinishDates.add(GlobalClass.convertDateToString(arrayListTemp.get(k)));
            }

                /* getting selected item from db to show in list as selected */
            //String selectedItem = dbHandler.getSelectedItems("4");
            /*ArrayList<String> selectedString = new ArrayList<>();
            if (!selectedItem.equalsIgnoreCase(""))
            {

                String[] split = selectedItem.split(",");

                for (int i = 0; i < split.length; i++)
                {
                    selectedString.add(split[i]);
                }
            }*/
            arraylistDatesToFilter = new ArrayList<>();
            for (int i = 0; i < arraylistFinishDates.size(); i++) {
                FilterModelClass filterModel = new FilterModelClass();
                filterModel.setName(arraylistFinishDates.get(i));
                filterModel.setSelected(false);
                for (int k = 0; k < arrayListSelectedFinishDates.size(); k++) {
                    if (arrayListSelectedFinishDates.get(k).equalsIgnoreCase(arraylistFinishDates.get(i))) {
                        filterModel.setSelected(true);
                    }
                }
                arraylistDatesToFilter.add(filterModel);
            }
            LayoutInflater layoutInflater4 = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView4 = layoutInflater4.inflate(R.layout.custom_dialog_selected_dates, null);
            popupWindowFinishDates = new PopupWindow(popupView4, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            if (GlobalClass.getScreenOrientation(getActivity()) == 1) {
                // for portrait
                popupWindowFinishDates.setWidth((GlobalClass.screenWidth(getContext()) * 33) / 100);
            } else {
                // for landscape
                popupWindowFinishDates.setWidth((GlobalClass.screenWidth(getContext()) * 17) / 100);
            }

            popupWindowFinishDates.setAnimationStyle(R.style.DialogAnimation);
            //popupWindowFinishDates.setHeight((GlobalClass.screenHeight(getContext()) * 40) / 100);
            //popupWindowFinishDates.setWidth(500);
            //popupWindowFinishDates.setHeight(600);
            popupWindowFinishDates.setOutsideTouchable(true);
            //popupWindowTraffic.showAtLocation(imgFilterTrafficelights, Gravity.CENTER|Gravity.BOTTOM,0, 0);
            if (GlobalClass.getScreenOrientation(getActivity()) == 1) {
                // for portrait
                popupWindowFinishDates.showAsDropDown(textviewSelectedDates, -(GlobalClass.screenWidth(getContext()) * 19) / 100, 0, Gravity.LEFT | Gravity.BOTTOM);
            } else {
                // for landscape
                popupWindowFinishDates.showAsDropDown(textviewSelectedDates, -(GlobalClass.screenWidth(getContext()) * 7) / 100, 0, Gravity.LEFT | Gravity.BOTTOM);
            }
            TextView textviewOk = (TextView) popupView4.findViewById(R.id.ok);
            //textviewOk.setOnTouchListener(new CustomTouchListener());
            setTouchListner(textviewOk);
            ListView listviewDates = (ListView) popupView4.findViewById(R.id.listviewdates);
            listviewDates.setSelector(R.drawable.list_selector);

            final DatesSelectedDialogAdapter adapter = new DatesSelectedDialogAdapter(getActivity(), arraylistDatesToFilter);
            listviewDates.setAdapter(adapter);
            textviewOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isPopupFinishDate = false;
                    ArrayList<FilterModelClass> arrayList = adapter.getSelectedItem();
                    ArrayList<String> arrayStringName = new ArrayList<>();
                    if (arrayList.size() > 0) {
                        textViewLableFinishDate.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);
                        arrayListSelectedFinishDates = new ArrayList<String>();
                        //dbHandler.deleteSelectedITem("4");
                        for (int i = 0; i < arrayList.size(); i++) {
                            arrayStringName.add(arrayList.get(i).getName());
                            arrayListSelectedFinishDates.add(arrayList.get(i).getName());
                        }
                        String commaSeperated = "";
                        String commaSeperatedID = "";
                        commaSeperated = TextUtils.join(",", arrayStringName);
                        //dbHandler.addSelectedItem("4", commaSeperated);
                        popupWindowFinishDates.dismiss();

                        if (arrayStringName.size() > 0) {
                            arrayFinishDate = new ArrayList<>();
                            arrayFinishDate.addAll(arrayStringName);
                            LogApp.e(" $$$$$$$$$$$ ", " clicked ok button fire : " + arrayStringName);
                            if (adapterTasklist != null) {
                                arrayTasklistafterFiltered = new ArrayList<>();
                                arrayTasklistafterFiltered = adapterTasklist.filterListview(strTaskFilter,
                                        strProjectFilter, arrayFinishDate, arrayTrafficLightfilter, arrayStatusfilter,
                                        arrayDatefilter, arrayTasklistAllDataPermenent);
                                isFilterFired = true;
                                setAdapterforFilter(arrayTasklistafterFiltered);
                            }
                            if (arrayStringName.size() > 1) {
                                textviewSelectedDates.setText(arrayStringName.get(0).toString() + " +");
                            } else {
                                textviewSelectedDates.setText(arrayStringName.get(0).toString());
                            }
                        } else {
                            textviewSelectedDates.setText(getString(R.string.selected_date));
                            arrayFinishDate = new ArrayList<>();
                            if (adapterTasklist != null) {
                                arrayTasklistafterFiltered = new ArrayList<>();
                                arrayTasklistafterFiltered = adapterTasklist.filterListview(strTaskFilter,
                                        strProjectFilter, arrayFinishDate, arrayTrafficLightfilter, arrayStatusfilter,
                                        arrayDatefilter, arrayTasklistAllDataPermenent);
                                isFilterFired = true;
                                setAdapterforFilter(arrayTasklistafterFiltered);
                            }
                        }
                    } else {
                        textViewLableFinishDate.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);
                        //dbHandler.deleteSelectedITem("4");
                        popupWindowFinishDates.dismiss();
                        if (arrayStringName.size() > 0) {
                            arrayFinishDate = new ArrayList<>();
                            arrayFinishDate.addAll(arrayStringName);
                            LogApp.e(" $$$$$$$$$$$ ", " clicked ok button fire : " + arrayStringName);

                            if (arrayListSelectedDate.size() > 0 || arrayListSelectedStatus.size() > 0
                                    || arrayListSelectedTraffic.size() > 0 || arrayListSelectedFinishDates.size() > 0) {
                                if (adapterTasklist != null) {
                                    arrayTasklistafterFiltered = new ArrayList<>();
                                    arrayTasklistafterFiltered = adapterTasklist.filterListview(strTaskFilter,
                                            strProjectFilter, arrayFinishDate, arrayTrafficLightfilter, arrayStatusfilter,
                                            arrayDatefilter, arrayTasklistAllDataPermenent);
                                    isFilterFired = true;
                                    setAdapterforFilter(arrayTasklistafterFiltered);
                                }
                            } else {
                                if (adapterTasklist != null) {
                                    if (arrayTasklistLastTimeLoaded.size() > 0) {
                                        arraylistTasklistforAdapter = new ArrayList<>();

                                        arraylistTasklistforAdapter.addAll(arrayTasklistLastTimeLoaded);
                                        adapterTasklist = new TasklistAdapter(getActivity(), arraylistTasklistforAdapter);
                                        listviewTasklist.setAdapter(adapterTasklist);
                                        if (arraylistTasklistforAdapter.size() == arrayTasklistAllDataPermenent.size()) {
                                            footerButtonforListview.setText(arraylistTasklistforAdapter.size() + "/" +
                                                    arrayTasklistAllDataPermenent.size() + " " + getString(R.string.no_more_data_to_load));
                                        } else {
                                            footerButtonforListview.setText(arraylistTasklistforAdapter.size() + "/" +
                                                    arrayTasklistAllDataPermenent.size() + " " + getString(R.string.load_more_data));
                                        }
                                        isFilterFired = false;


                                        totRecord = arraylistTasklistforAdapter.size();
                                        startRecord = arraylistTasklistforAdapter.size();
                                        endRecord = arraylistTasklistforAdapter.size() + 10;
                                    }

                                }
                            }


                            if (arrayStringName.size() > 1) {
                                textviewSelectedDates.setText(arrayStringName.get(0).toString() + " +");
                            } else {
                                textviewSelectedDates.setText(arrayStringName.get(0).toString());
                            }
                        } else {
                            arrayListSelectedFinishDates = new ArrayList<String>();
                            textviewSelectedDates.setText(getString(R.string.selected_date));
                            arrayFinishDate = new ArrayList<>();
                            if (arrayListSelectedDate.size() > 0 && arrayListSelectedStatus.size() > 0
                                    && arrayListSelectedTraffic.size() > 0 && arrayListSelectedFinishDates.size() > 0) {
                                if (adapterTasklist != null) {
                                    arrayTasklistafterFiltered = new ArrayList<>();
                                    arrayTasklistafterFiltered = adapterTasklist.filterListview(strTaskFilter,
                                            strProjectFilter, arrayFinishDate, arrayTrafficLightfilter, arrayStatusfilter,
                                            arrayDatefilter, arrayTasklistAllDataPermenent);
                                    isFilterFired = true;
                                    setAdapterforFilter(arrayTasklistafterFiltered);
                                }
                            } else {
                                if (adapterTasklist != null) {
                                    if (arrayTasklistLastTimeLoaded.size() > 0) {
                                        arraylistTasklistforAdapter = new ArrayList<>();

                                        arraylistTasklistforAdapter.addAll(arrayTasklistLastTimeLoaded);
                                        adapterTasklist = new TasklistAdapter(getActivity(), arraylistTasklistforAdapter);
                                        listviewTasklist.setAdapter(adapterTasklist);
                                        if (arraylistTasklistforAdapter.size() == arrayTasklistAllDataPermenent.size()) {
                                            footerButtonforListview.setText(arraylistTasklistforAdapter.size() + "/" +
                                                    arrayTasklistAllDataPermenent.size() + " " + getString(R.string.no_more_data_to_load));
                                        } else {
                                            footerButtonforListview.setText(arraylistTasklistforAdapter.size() + "/" +
                                                    arrayTasklistAllDataPermenent.size() + " " + getString(R.string.load_more_data));
                                        }
                                        isFilterFired = false;


                                        totRecord = arraylistTasklistforAdapter.size();
                                        startRecord = arraylistTasklistforAdapter.size();
                                        endRecord = arraylistTasklistforAdapter.size() + 10;
                                    }

                                }
                            }

                        }

                    }
                }
            });
            //&&&&&&&&& CustomDialogSelectedDates customDialog = new CustomDialogSelectedDates();
            //customDialog.showDialog(getActivity(), arraylistDatesToFilter);
        } else {
            //GlobalClass.showToast(getActivity(), getString(R.string.please_select_date_first));
            GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.select_date_title),getString(R.string.please_select_date_first));
        }
    }

    //    done button event to call dashboard service of selected dates
    public void doneButoonTocallService() {

        if (startDate != null || endDate != null) {
            resetAllvariable();
            if (startDate != null && endDate != null) {
                startDateLast=startDate;
                endDateLast=endDate;
                textviewCurrentDate.setText(GlobalClass.dateFormateChanged(startDate)
                        + " To " + GlobalClass.dateFormateChanged(endDate));
                textviewCurrentDate2.setText(GlobalClass.dateFormateChanged(startDate)
                        + " To " + GlobalClass.dateFormateChanged(endDate));
            }
            arraylistStringDates.clear();
            arraylistStringDates.add(GlobalClass.dateFormateChanged2(startDate));
            arraylistStringDates.add(GlobalClass.dateFormateChanged2(endDate));

            dateArray = new String[arraylistStringDates.size()];
            dateArray = arraylistStringDates.toArray(dateArray);
            if (GlobalClass.isNetworkAvailable(getActivity())) {
                getDashboardData("yes");
            } else {
                //GlobalClass.showToastInternet(getActivity());
                GlobalClass.showCustomDialogInternet(getActivity());
            }
        } else {
            //GlobalClass.showToast(getActivity(), getString(R.string.please_select_atleast_one_date));
            GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.select_date_title),
                    getString(R.string.please_select_atleast_one_date));
        }
    }

    //    this method is for appllying pagination for listview
    public void paginationForListview() {
    /* if any type of filter is fired this condition will call */
        if (isFilterFired) {
            if (arrayTasklistafterFiltered.size() > totRecord) {
                if ((arrayTasklistafterFiltered.size() - 10) < totRecord) {
                    int remain = arrayTasklistafterFiltered.size() - totRecord;
                    totRecord = arrayTasklistafterFiltered.size();
                    ArrayList<TaskListModelClass> arrayTask = new ArrayList<>();
                    arrayTask = new ArrayList<>(arrayTasklistafterFiltered.subList(startRecord, totRecord));
                    arraylistTasklistforAdapter.addAll(arrayTask);
                    //arrayTasklistLastTimeLoaded.addAll(arrayTask);
                    if (adapterTasklist != null) {
                        adapterTasklist.notifyDataSetChanged();
                    }
                    startRecord = 10;
                    endRecord = 20;
                    strLoadmore2 = getString(R.string.no_more_data_to_load_static);
                    footerButtonforListview.setText(totRecord + "/" + arraylistTasklistforAdapter.size() + " " +
                            getString(R.string.no_more_data_to_load));
                } else {
                    arraylistTasklistforAdapter.addAll(arrayTasklistafterFiltered.subList(startRecord, endRecord));
                    //arrayTasklistLastTimeLoaded.addAll(arrayTasklistafterFiltered.subList(startRecord, endRecord));
                    if (adapterTasklist != null) {
                        adapterTasklist.notifyDataSetChanged();
                    }
                    totRecord = totRecord + 10;
                    startRecord = startRecord + 10;
                    endRecord = endRecord + 10;
                    strLoadmore2 = getString(R.string.no_more_data_to_load_static);


                    if (arrayTasklistafterFiltered.size() == arraylistTasklistforAdapter.size()) {
                        startRecord = 10;
                        endRecord = 20;
                        footerButtonforListview.setText(totRecord + "/" + arrayTasklistafterFiltered.size() + " " +
                                getString(R.string.no_more_data_to_load));
                    } else {
                        footerButtonforListview.setText(totRecord + "/" + arrayTasklistafterFiltered.size() + " " +
                                getString(R.string.load_more_data));
                    }
                }
            }
        }
            /* if any filter is not fired this condition will call */
        else {
            if (arrayTasklistAllDataPermenent.size() > totRecord) {
                if ((arrayTasklistAllDataPermenent.size() - 10) < totRecord) {
                    int remain = arrayTasklistAllDataPermenent.size() - totRecord;
                    totRecord = arrayTasklistAllDataPermenent.size();
                    ArrayList<TaskListModelClass> arrayTask = new ArrayList<>();
                    arrayTask = new ArrayList<>(arrayTasklistAllDataPermenent.subList(startRecord, totRecord));
                    arraylistTasklistforAdapter.addAll(arrayTask);
                    arrayTasklistLastTimeLoaded.addAll(arrayTask);
                    if (adapterTasklist != null) {
                        adapterTasklist.notifyDataSetChanged();
                    }
                    startRecord = 10;
                    endRecord = 20;
                    strLoadmore2 = getString(R.string.no_more_data_to_load_static);
                    footerButtonforListview.setText(totRecord + "/" + arrayTasklistAllDataPermenent.size() + " " +
                            getString(R.string.no_more_data_to_load));
                } else {
                    arraylistTasklistforAdapter.addAll(arrayTasklistAllDataPermenent.subList(startRecord, endRecord));
                    arrayTasklistLastTimeLoaded.addAll(arrayTasklistAllDataPermenent.subList(startRecord, endRecord));
                    if (adapterTasklist != null) {
                        adapterTasklist.notifyDataSetChanged();
                    }
                    totRecord = totRecord + 10;
                    startRecord = startRecord + 10;
                    endRecord = endRecord + 10;
                    strLoadmore2 = getString(R.string.no_more_data_to_load_static);

                    if (arrayTasklistAllDataPermenent.size() == arraylistTasklistforAdapter.size()) {
                        startRecord = 10;
                        endRecord = 20;
                        footerButtonforListview.setText(totRecord + "/" + arrayTasklistAllDataPermenent.size() + " " +
                                getString(R.string.no_more_data_to_load));
                    } else {
                        footerButtonforListview.setText(totRecord + "/" + arrayTasklistAllDataPermenent.size() + " " +
                                getString(R.string.load_more_data));
                    }
                }
            }
        }
    }

    public void resetAllvariable() {
        totRecord = 10;
        startRecord = 0;
        endRecord = 0;

        preferenes.clearClickedAgreegatePosition();
        preferenes.clearClickedTaskPosition();
        preferenes.clearCurrentFiredFilter();
        preferenes.clearTaskLastFiredFilter();
        preferenes.clearAgreegateLastFiredFilter();

        textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);
        textviewLableTraffic.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);
        textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);
        textViewLableFinishDate.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);
        textviewSelectedDates.setTextColor(getResources().getColor(R.color.textview_color_grey));
        textviewSelectedDates.setText(getString(R.string.selected_date));

        imgFilterDate.setBackgroundResource(R.drawable.date_icon);
        imgFilterStatus.setBackgroundResource(R.drawable.status_icon);
        imgFilterTrafficelights.setBackgroundResource(R.drawable.traffic_lights);

        refreshArraylst();
        isFilterFired = false;
        isFirsttimeClicked = true;
        //dbHandler.deleteAllSelectedITem();
        preferenes.saveIsfirstTime(true);
        arraylistFinishDates = new ArrayList<>();
        arraylistTasklistforAdapter.clear();
        arrayTasklistLastTimeLoaded.clear();
        if (adapterTasklist != null) {
            adapterTasklist.notifyDataSetChanged();
        }
        if (selectedDatesStartEnd != null) {
            selectedDatesStartEnd.clear();
        }
        if (allDatesBetweenDates != null) {
            allDatesBetweenDates.clear();
        }
    }

    //    this method refresh activity and call service again on refresh click
    public void refreshActivityAndCallService() {
        startDate = null;
        endDate = null;
        startDateLast = null;
        endDateLast = null;
        preferenes.saveShowToday(true);
        textviewCurrentDate2.setText(GlobalClass.dateFormateChanged(new Date()));
        resetAllvariable();
        setCaldroidFragment(bundle);
        /// adding calendar to screen
        /*calendarPicker.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.RANGE) //
                .withSelectedDate(new Date());*/
        //caldroidFragment.setSelectedDates(new Date(), new Date());
        //caldroidFragment.refreshView();
        /// addding calendar to screen
        gettingTodayDateForService();
        // calling service
        if (GlobalClass.isNetworkAvailable(getActivity())) {
            getDashboardData("yes");
        } else {
            //listviewTasklist.addFooterView(footerViewforListview);
            footerButtonforListview.setText(getString(R.string.no_more_data_to_load_0_0));
            addOfflineDatatoAgreegate();
            //GlobalClass.showToastInternet(getActivity());
            GlobalClass.showCustomDialogInternet(getActivity());
        }
    }

    //    this is recursion method to get child data from service
    public TaskListModelClass recursionTogetAllTaskforListviewtoSet(JSONObject dataEntityObject, String ProjectName,
                                                                    String groupId, String ProjectId) throws JSONException {
        tasklistModelTemp = new TaskListModelClass();
        tasklistModelTemp.setProjectName(ProjectName);
        if (dataEntityObject.has("children")) {
            try {
                JSONArray children = dataEntityObject.getJSONArray("children");
                int count = children.length();
                LogApp.e(" in method : ", " children lenght : " + count);
                for (int i = 0; i < children.length(); i++) {
                    JSONObject jsonObject = children.getJSONObject(i);
                    tasklistModelTemp.setName(jsonObject.getString("Name"));
                    tasklistModelTemp.setEndDate(GlobalClass.epochTimeToDate(GlobalClass.epochFormatetoString(
                            jsonObject.getString("EndDate"))));
                    tasklistModelTemp.setTrafficLightObj(jsonObject.getString("TrafficLightObj"));
                    tasklistModelTemp.setStatusObj(jsonObject.getString("StatusObj"));
                    tasklistModelTemp.setTaskObject(children.getJSONObject(i).toString());

                    tasklistModelTemp.setStrGroupId(groupId);
                    tasklistModelTemp.setStrProjectId(ProjectId);
                    tasklistModelTemp.setStrTaskid(jsonObject.getString("Id"));
                    tasklistModelTemp.setStrparentId(jsonObject.getString("parentId"));

                    /* setting traffic light object */
                    if (!TextUtils.isEmpty(jsonObject.getString("TrafficLightObj"))) {
                        tasklistModelTemp.setTrafficLight(jsonObject.getString("TrafficLightObj"));
                    } else {
                        tasklistModelTemp.setTrafficLight("");
                    }

                    /* setting status and date object */
                    ArrayList<String> arrayliststatusObject = null;
                    ArrayList<String> arrayStatus = new ArrayList<>();
                    if (!TextUtils.isEmpty(jsonObject.getString("StatusObj"))) {
                        arrayliststatusObject = new ArrayList<String>(Arrays.asList(jsonObject.getString("StatusObj").
                                split(",")));
                    }
                    if (arrayliststatusObject != null) {
                        if (arrayliststatusObject.size() > 0) {
                            for (int k = 0; k < arrayliststatusObject.size(); k++) {
                                /* if this condition meet than string will be store in Date object  */
                                if (arrayliststatusObject.get(k).equalsIgnoreCase("ShowOverdueFinished") ||
                                        arrayliststatusObject.get(k).equalsIgnoreCase("ShowFinishToday") ||
                                        arrayliststatusObject.get(k).equalsIgnoreCase("ShowFinishWeek") ||
                                        arrayliststatusObject.get(k).equalsIgnoreCase("ShowStarted") ||
                                        arrayliststatusObject.get(k).equalsIgnoreCase("ShowPending") ||
                                        arrayliststatusObject.get(k).equalsIgnoreCase("ShowNoTime") ||
                                        arrayliststatusObject.get(k).equalsIgnoreCase("ShowPassiv") ||
                                        arrayliststatusObject.get(k).equalsIgnoreCase("ShowCanceled")) {
                                    tasklistModelTemp.setDateImage(arrayliststatusObject.get(k));
                                }
                                /* if this condition meet than string will be store in Status object */
                                else if (arrayliststatusObject.get(k).equalsIgnoreCase("ShowArrow") ||
                                        arrayliststatusObject.get(k).equalsIgnoreCase("ShowQuestion") ||
                                        arrayliststatusObject.get(k).equalsIgnoreCase("ShowCritical") ||
                                        arrayliststatusObject.get(k).equalsIgnoreCase("ShowReminder") ||
                                        arrayliststatusObject.get(k).equalsIgnoreCase("ShowFinished") ||
                                        arrayliststatusObject.get(k).equalsIgnoreCase("ShowOverdueLimit") ||
                                        arrayliststatusObject.get(k).equalsIgnoreCase("ShowCapacityTooHigh") ||
                                        arrayliststatusObject.get(k).equalsIgnoreCase("ShowCapacityTooLow")) {
                                    //tasklistModelTemp.setStatusImage(arrayliststatusObject.get(k));
                                    arrayStatus.add(arrayliststatusObject.get(k));

                                }
                            }
                            tasklistModelTemp.setStatusArray(arrayStatus);

                        }
                    } else {
                        tasklistModelTemp.setStatusImage("");
                        tasklistModelTemp.setDateImage("");
                    }


                    arraylistTasklistforAdapter.add(tasklistModelTemp);
                    //arrayTasklistLastTimeLoaded.add(tasklistModelTemp);
                    recursionTogetAllTaskforListviewtoSet(jsonObject, ProjectName, groupId, ProjectId);
                }
            } catch (Exception e) {
            }
        } else {
        }
        return tasklistModelTemp;
    }

    public TaskListModelClass recursionCallChild2(JSONObject dataEntityObject, int arrayNo) throws JSONException {
        if (arrayNo == 1) {
            tasklistModelTemp = new TaskListModelClass();
            if (dataEntityObject.has("children")) {
                try {
                    JSONArray children = dataEntityObject.getJSONArray("children");
                    int count = children.length();
                    LogApp.e(" in method : ", " children lenght : " + count);
                    for (int i = 0; i < children.length(); i++) {
                        JSONObject jsonObject = children.getJSONObject(i);
                        tasklistModelTemp.setName(jsonObject.getString("Name"));
                        tasklistModelTemp.setGroupId(jsonObject.getString("GroupId"));
                        arraylistTaskAssignedTree.add(tasklistModelTemp);
                        recursionCallChild2(jsonObject, arrayNo);
                    }
                } catch (Exception e) {
                }
            } else {
            }
        }
        if (arrayNo == 2) {
            tasklistModelTemp = new TaskListModelClass();
            if (dataEntityObject.has("children")) {
                try {
                    JSONArray children = dataEntityObject.getJSONArray("children");
                    int count = children.length();
                    LogApp.e(" in method : ", " children lenght : " + count);
                    for (int i = 0; i < children.length(); i++) {
                        JSONObject jsonObject = children.getJSONObject(i);
                        tasklistModelTemp.setName(jsonObject.getString("Name"));
                        tasklistModelTemp.setGroupId(jsonObject.getString("GroupId"));
                        arraylistTaskCompleteNextFiveDayTree.add(tasklistModelTemp);
                        recursionCallChild2(jsonObject, arrayNo);
                    }
                } catch (Exception e) {
                }
            } else {
            }
        }
        if (arrayNo == 3) {
            tasklistModelTemp = new TaskListModelClass();
            if (dataEntityObject.has("children")) {
                try {
                    JSONArray children = dataEntityObject.getJSONArray("children");
                    int count = children.length();
                    LogApp.e(" in method : ", " children lenght : " + count);
                    for (int i = 0; i < children.length(); i++) {
                        JSONObject jsonObject = children.getJSONObject(i);
                        tasklistModelTemp.setName(jsonObject.getString("Name"));
                        tasklistModelTemp.setGroupId(jsonObject.getString("GroupId"));
                        arraylistTaskCompleteTodayTree.add(tasklistModelTemp);
                        recursionCallChild2(jsonObject, arrayNo);
                    }
                } catch (Exception e) {
                }
            } else {
            }
        }
        if (arrayNo == 4) {
            tasklistModelTemp = new TaskListModelClass();
            if (dataEntityObject.has("children")) {
                try {
                    JSONArray children = dataEntityObject.getJSONArray("children");
                    int count = children.length();
                    LogApp.e(" in method : ", " children lenght : " + count);
                    for (int i = 0; i < children.length(); i++) {
                        JSONObject jsonObject = children.getJSONObject(i);
                        tasklistModelTemp.setName(jsonObject.getString("Name"));
                        tasklistModelTemp.setGroupId(jsonObject.getString("GroupId"));
                        arraylistTaskCompleteTree.add(tasklistModelTemp);
                        recursionCallChild2(jsonObject, arrayNo);
                    }
                } catch (Exception e) {
                }
            } else {
            }
        }
        if (arrayNo == 5) {
            tasklistModelTemp = new TaskListModelClass();
            if (dataEntityObject.has("children")) {
                try {
                    JSONArray children = dataEntityObject.getJSONArray("children");
                    int count = children.length();
                    LogApp.e(" in method : ", " children lenght : " + count);
                    for (int i = 0; i < children.length(); i++) {
                        JSONObject jsonObject = children.getJSONObject(i);
                        tasklistModelTemp.setName(jsonObject.getString("Name"));
                        tasklistModelTemp.setGroupId(jsonObject.getString("GroupId"));
                        arraylistTaskPendingTree.add(tasklistModelTemp);
                        recursionCallChild2(jsonObject, arrayNo);
                    }
                } catch (Exception e) {
                }
            } else {
            }
        }
        return tasklistModelTemp;
    }

    //    add finish data in listview from adapter
    private void addFinishDatefromAdapter() {
        arraylistFinishDates = new ArrayList<>();
        if (arrayTasklistAllDataPermenent.size() > 0) {
            for (int i = 0; i < arrayTasklistAllDataPermenent.size(); i++) {
                arraylistFinishDates.add(arrayTasklistAllDataPermenent.get(i).getEndDate());
            }
            isFirsttimeClicked = false;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
//    this method showing popup window
    private void showPopupWindow(int popupNo) {
        switch (popupNo) {
            /* for date filter */
            case 1:
                LayoutInflater layoutInflater = (LayoutInflater) getActivity().
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popupview_date, null);
                popupWindowDate = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                if (GlobalClass.getScreenOrientation(getActivity()) == 1) {
                    // for portrait
                    popupWindowDate.setWidth((GlobalClass.screenWidth(getContext()) * 37) / 100);
                } else {
                    // for landscape
                    popupWindowDate.setWidth((GlobalClass.screenWidth(getContext()) * 25) / 100);
                }
                popupWindowDate.setAnimationStyle(R.style.DialogAnimation);
                //popupWindowDate.setHeight((GlobalClass.screenHeight(getContext()) * 37) / 100);
                popupWindowDate.setOutsideTouchable(true);
                //popupWindowDate.showAtLocation(imgFilterDate, Gravity.CENTER|Gravity.BOTTOM,0, 0);
                popupWindowDate.showAsDropDown(imgFilterDate, 0, 0, Gravity.CENTER | Gravity.BOTTOM);

                ListView listviewDateFilter = (ListView) popupView.findViewById(R.id.listviewdatefilter);

                adapterDate = new DateFilterDialogAdapter(getActivity(), arraylistPopupViewforFilter);
                listviewDateFilter.setAdapter(adapterDate);

                TextView textViewDone = (TextView) popupView.findViewById(R.id.txtdone);
                //textViewDone.setOnTouchListener(new CustomTouchListener());
                setTouchListner(textViewDone);
                textViewDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filterDate(adapterDate);
                    }
                });
                break;

            /* for status filter */
            case 2:
                LayoutInflater layoutInflater2 = (LayoutInflater) getActivity().
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView2 = layoutInflater2.inflate(R.layout.popupview_status, null);
                popupWindowStatus = new PopupWindow(popupView2, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                if (GlobalClass.getScreenOrientation(getActivity()) == 1) {
                    // for portrait
                    popupWindowStatus.setWidth((GlobalClass.screenWidth(getContext()) * 33) / 100);
                } else {
                    // for landscape
                    popupWindowStatus.setWidth((GlobalClass.screenWidth(getContext()) * 22) / 100);
                }
                popupWindowStatus.setAnimationStyle(R.style.DialogAnimation);
                //popupWindowStatus.setHeight((GlobalClass.screenHeight(getContext()) * 37) / 100);
                popupWindowStatus.setOutsideTouchable(true);
                //popupWindowStatus.showAtLocation(imgFilterStatus, Gravity.CENTER|Gravity.BOTTOM,0, 0);
                popupWindowStatus.showAsDropDown(imgFilterStatus, 0, 0, Gravity.CENTER | Gravity.BOTTOM);
                ListView listviewStatusFilter = (ListView) popupView2.findViewById(R.id.listviewstatusfilter);
                adapterStatus = new StatusFilterDialogAdapter(getActivity(), arraylistPopupViewforFilter);
                listviewStatusFilter.setAdapter(adapterStatus);

                TextView textViewDone2 = (TextView) popupView2.findViewById(R.id.txtdone);
                //textViewDone2.setOnTouchListener(new CustomTouchListener());
                setTouchListner(textViewDone2);
                textViewDone2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filterStatus(adapterStatus);

                    }
                });
                break;

            /* for traffic light */
            case 3:
                LayoutInflater layoutInflater3 = (LayoutInflater) getActivity().
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView3 = layoutInflater3.inflate(R.layout.popup_view_traffic, null);
                popupWindowTraffic = new PopupWindow(popupView3, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                if (GlobalClass.getScreenOrientation(getActivity()) == 1) {
                    // for portrait
                    popupWindowTraffic.setWidth((GlobalClass.screenWidth(getContext()) * 35) / 100);
                } else {
                    // for landscape
                    popupWindowTraffic.setWidth((GlobalClass.screenWidth(getContext()) * 18) / 100);
                }
                popupWindowTraffic.setAnimationStyle(R.style.DialogAnimation);
                //popupWindowTraffic.setHeight((GlobalClass.screenHeight(getContext()) * 30) / 100);
                popupWindowTraffic.setOutsideTouchable(true);
                //popupWindowTraffic.showAtLocation(imgFilterTrafficelights, Gravity.CENTER|Gravity.BOTTOM,0, 0);
                popupWindowTraffic.showAsDropDown(imgFilterTrafficelights, 0, 0, Gravity.CENTER | Gravity.BOTTOM);

                ListView listviewTrafficFilter = (ListView) popupView3.findViewById(R.id.listviewtrafficfilter);

                adapterTrafficLight = new TrafficFilterDialogAdapter(getActivity(), arraylistPopupViewforFilter);
                listviewTrafficFilter.setAdapter(adapterTrafficLight);

                TextView textViewDone3 = (TextView) popupView3.findViewById(R.id.txtdone);
                setTouchListner(textViewDone3);
                //textViewDone3.setOnTouchListener(new CustomTouchListener());
                textViewDone3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filterTrafficLight(adapterTrafficLight);
                    }
                });
                break;
            /* for traffic light */
        }
    }

    //    add data in popupview based on position of clicked textview
    public void addingDataInPopup(int pos) {
        /* for date popup*/
        String[] arrayDate = {getString(R.string.completion_overrun), getString(R.string.complete_today),
                getString(R.string.completed_in1week), getString(R.string.started_project_task),
                getString(R.string.pending_project_task), getString(R.string.task_without_date),
                getString(R.string.passive_project_task), getString(R.string.canceled_project_task)};
        String[] arrayDateKey = {"1", "2", "3", "4", "5", "6", "7", "8"};
        int[] imgDate = {R.drawable.completion, R.drawable.completed_today, R.drawable.completed_in_one_week,
                R.drawable.started_task, R.drawable.pending_task, R.drawable.task_without_date,
                R.drawable.passive, R.drawable.canceled_task};
        String[] arrayDateOriginalvalue = {"ShowOverdueFinished", "ShowFinishToday", "ShowFinishWeek",
                "ShowStarted", "ShowPending", "ShowNoTime", "ShowPassiv", "ShowCanceled"};


        /* for status popupup*/
        String[] arrayStatus = {getString(R.string.notice_arrow), getString(R.string.not_clear),
                getString(R.string.critical), getString(R.string.reminder),
                getString(R.string.completed_task), getString(R.string.limit),
                getString(R.string.capacity_too_high), getString(R.string.capacity_too_low)};
        String[] arrayStatusKey = {"1", "2", "3", "4", "5", "6", "7", "8"};
        String[] arrayStatusOriginal = {"ShowArrow", "ShowQuestion", "ShowCritical", "ShowReminder",
                "ShowFinished", "ShowOverdueLimit", "ShowCapacityTooHigh", "ShowCapacityTooLow"};
        int[] imgStatus = {R.drawable.notice_arrow, R.drawable.not_clear, R.drawable.critical,
                R.drawable.reminders, R.drawable.completed_task, R.drawable.limit, R.drawable.capacity_too_high,
                R.drawable.capacity_too_low};

        /* for traffic popup */
        String[] arrayTraffic = {getString(R.string.traffic_red), getString(R.string.traffic_yellow),
                getString(R.string.traffic_green)};
        String[] arrayTrafficKey = {"1", "2", "3"};
        String[] arrayTrafficOriginalvalue = {"TrafficlightRed", "TrafficLightYellow", "TrafficLightGreen"};
        int[] imgTraffice = {R.drawable.traffic_red, R.drawable.traffic_yellow, R.drawable.traffic_green};

        /* for date */
        if (pos == 1) {
            /*String selectedItem = dbHandler.getSelectedItems("1");
            ArrayList<String> selectedString = new ArrayList<>();
            if (!selectedItem.equalsIgnoreCase(""))
            {

                String[] split = selectedItem.split(",");

                for (int i = 0; i < split.length; i++)
                {
                    selectedString.add(split[i]);
                }
            }*/
            arraylistPopupViewforFilter.clear();
            for (int i = 0; i < arrayDate.length; i++) {
                FilterModelClass filter = new FilterModelClass();
                filter.setName(arrayDate[i]);
                filter.setStrOriginalValue(arrayDateOriginalvalue[i]);
                filter.setKey(arrayDateKey[i]);
                filter.setDrawable(imgDate[i]);
                //filter.setSelected(false);
                if (arrayListSelectedDate.size() > 0) {
                    for (int k = 0; k < arrayListSelectedDate.size(); k++) {
                        if (arrayListSelectedDate.get(k).equalsIgnoreCase(arrayDateKey[i])) {
                            filter.setSelected(true);
                            break;
                        } else {
                            filter.setSelected(false);
                            //break;
                        }

                    }
                } else {
                    filter.setSelected(false);
                }
                arraylistPopupViewforFilter.add(filter);
            }
        }
        /* for status */
        if (pos == 2) {
            /*String selectedItem = dbHandler.getSelectedItems("2");
            ArrayList<String> selectedString = new ArrayList<>();
            if (!selectedItem.equalsIgnoreCase(""))
            {

                String[] split = selectedItem.split(",");

                for (int i = 0; i < split.length; i++)
                {
                    selectedString.add(split[i]);
                }
            }*/
            arraylistPopupViewforFilter.clear();
            for (int i = 0; i < arrayStatus.length; i++) {
                FilterModelClass filter = new FilterModelClass();
                filter.setName(arrayStatus[i]);
                filter.setStrOriginalValue(arrayStatusOriginal[i]);
                filter.setDrawable(imgStatus[i]);
                filter.setKey(arrayStatusKey[i]);
                //filter.setSelected(false);
                if (arrayListSelectedStatus.size() > 0) {
                    for (int k = 0; k < arrayListSelectedStatus.size(); k++) {
                        if (arrayListSelectedStatus.get(k).equalsIgnoreCase(arrayStatusKey[i])) {
                            filter.setSelected(true);
                            break;
                        } else {
                            filter.setSelected(false);
                            //break;
                        }

                    }
                } else {
                    filter.setSelected(false);
                }
                arraylistPopupViewforFilter.add(filter);
            }
        }
        /* for traffic light*/
        if (pos == 3) {
            //String selectedItem = dbHandler.getSelectedItems("3");
            /*ArrayList<String> selectedString = new ArrayList<>();
            if (!selectedItem.equalsIgnoreCase("")) {
                String[] split = selectedItem.split(",");
                for (int i = 0; i < split.length; i++) {
                    selectedString.add(split[i]);
                }
            }*/
            arraylistPopupViewforFilter.clear();
            for (int i = 0; i < arrayTraffic.length; i++) {
                FilterModelClass filter = new FilterModelClass();
                filter.setName(arrayTraffic[i]);
                filter.setStrOriginalValue(arrayTrafficOriginalvalue[i]);
                filter.setDrawable(imgTraffice[i]);
                filter.setKey(arrayTrafficKey[i]);
                //filter.setSelected(false);
                if (arrayListSelectedTraffic.size() > 0) {
                    for (int k = 0; k < arrayListSelectedTraffic.size(); k++) {
                        if (arrayListSelectedTraffic.get(k).equalsIgnoreCase(arrayTrafficKey[i])) {
                            filter.setSelected(true);
                            break;
                        } else {
                            filter.setSelected(false);
                            //break;
                        }

                    }
                } else {
                    filter.setSelected(false);
                }
                arraylistPopupViewforFilter.add(filter);
            }
        }
    }

    //    this method return last selected value and show in popup
    public void gettingFilterarraylistFromDb() {
        String[] arrayDate = {"Completion overrun", "Complete today", "Completed in 1 week", "Started tasks",
                "Pending tasks", "Task without date", "Passive tasks", "Canceled tasks"};
        String[] arrayDateOriginalvalue = {"ShowOverdueFinished", "ShowFinishToday", "ShowFinishWeek",
                "ShowStarted", "ShowPending", "ShowNoTime", "ShowPassiv", "ShowCanceled"};
        String[] arrayStatus = {"Notice Arrow", "Not Clear", "Critical", "Remindar", "Completed tasks",
                "Limit", "CapacityTooHigh", "CapacityTooLow"};
        String[] arrayStatusOriginal = {"ShowArrow", "ShowQuestion", "ShowCritical", "ShowReminder", "ShowFinished",
                "ShowOverdueLimit", "ShowCapacityTooHigh", "ShowCapacityTooLow"};
        String[] arrayTraffic = {"Red", "Yellow", "Green"};
        String[] arrayTrafficOriginalvalue = {"TrafficlightRed", "TrafficLightYellow", "TrafficLightGreen"};
        int[] imgDate = {R.drawable.completion, R.drawable.completed_today, R.drawable.completed_in_one_week,
                R.drawable.started_task, R.drawable.pending_task, R.drawable.task_without_date,
                R.drawable.reminder, R.drawable.canceled_task};

        int[] imgTraffice = {R.drawable.traffic_red, R.drawable.traffic_yellow, R.drawable.traffic_green};

        int[] imgStatus = {R.drawable.notice_arrow, R.drawable.not_clear, R.drawable.critical,
                R.drawable.reminder, R.drawable.completed_task, R.drawable.limit, R.drawable.capacity_too_high,
                R.drawable.capacity_too_low};

        /* for date */
        //String selectedItemDate = dbHandler.getSelectedItems("1");
        ArrayList<String> selectedStringDate = new ArrayList<>();
        /*if (!selectedItemDate.equalsIgnoreCase(""))
        {

            String[] split = selectedItemDate.split(",");
            for (int i = 0; i < split.length; i++)
            {
                selectedStringDate.add(split[i]);
            }
        }*/
        arrayDatefilter = new ArrayList<>();
        for (int i = 0; i < arrayDate.length; i++) {
            for (int k = 0; k < selectedStringDate.size(); k++) {
                if (selectedStringDate.get(k).equalsIgnoreCase(arrayDate[i])) {
                    arrayDatefilter.add(arrayDateOriginalvalue[i]);
                }
            }
        }
        /* for status */
        //String selectedItemStatus = dbHandler.getSelectedItems("2");
        ArrayList<String> selectedStringStatus = new ArrayList<>();
        /*if (!selectedItemStatus.equalsIgnoreCase(""))
        {
            String[] split = selectedItemStatus.split(",");
            for (int i = 0; i < split.length; i++)
            {
                selectedStringStatus.add(split[i]);
            }
        }*/
        arrayStatusfilter = new ArrayList<>();
        for (int i = 0; i < arrayStatus.length; i++) {
            for (int k = 0; k < selectedStringStatus.size(); k++) {
                if (selectedStringStatus.get(k).equalsIgnoreCase(arrayStatus[i])) {
                    arrayStatusfilter.add(arrayStatusOriginal[i]);
                }
            }
        }
        /* for traffic light*/
        //String selectedItemTraffic = dbHandler.getSelectedItems("3");
        ArrayList<String> selectedStringTraffic = new ArrayList<>();
        /*if (!selectedItemTraffic.equalsIgnoreCase(""))
        {
            String[] split = selectedItemTraffic.split(",");
            for (int i = 0; i < split.length; i++)
            {
                selectedStringTraffic.add(split[i]);
            }
        }*/
        arrayTrafficLightfilter = new ArrayList<>();
        for (int i = 0; i < arrayTraffic.length; i++) {
            for (int k = 0; k < selectedStringTraffic.size(); k++) {
                if (selectedStringTraffic.get(k).equalsIgnoreCase(arrayTraffic[i])) {
                    arrayTrafficLightfilter.add(arrayTrafficOriginalvalue[i]);
                }
            }
        }

        /* for finish date*/
        //String selectedItemFinishdate = dbHandler.getSelectedItems("4");
        arrayFinishDate = new ArrayList<>();
        /*if (!selectedItemFinishdate.equalsIgnoreCase(""))
        {

            String[] split = selectedItemFinishdate.split(",");

            for (int i = 0; i < split.length; i++)
            {
                arrayFinishDate.add(split[i]);
            }
        }*/
    }

    //    adapter for tasklistview
    public void setAdapterforTasklist(ArrayList<TaskListModelClass> arraylistTasklist) {
        if (arraylistTasklist.size() > 0) {
            if (arraylistTasklist.size() > totRecord) {
                //listviewTasklist.addFooterView(footerViewforListview);
                startRecord = 10;
                endRecord = 20;
                arrayTasklistAllDataPermenent = new ArrayList<TaskListModelClass>();
                arrayTasklistAllDataPermenent.addAll(arraylistTasklist);
                arraylistTasklistforAdapter = new ArrayList<>();
                arrayTasklistLastTimeLoaded = new ArrayList<>(arrayTasklistAllDataPermenent.subList(0, 10));
                arraylistTasklistforAdapter = new ArrayList<>(arrayTasklistAllDataPermenent.subList(0, 10));
                adapterTasklist = new TasklistAdapter(getActivity(), arraylistTasklistforAdapter);
                listviewTasklist.setAdapter(adapterTasklist);
                strLoadmore2 = getString(R.string.load_more_data_static);
                footerButtonforListview.setText("" + startRecord + "/" + "" + arrayTasklistAllDataPermenent.size() + " " +
                        getString(R.string.load_more_data));
            } else {
                arrayTasklistAllDataPermenent = new ArrayList<TaskListModelClass>();
                arrayTasklistAllDataPermenent.addAll(arraylistTasklist);

                arraylistTasklistforAdapter = new ArrayList<>();
                arraylistTasklistforAdapter.addAll(arraylistTasklist);
                arrayTasklistLastTimeLoaded.addAll(arraylistTasklist);
                adapterTasklist = new TasklistAdapter(getActivity(), arraylistTasklistforAdapter);
                listviewTasklist.setAdapter(adapterTasklist);

                strLoadmore2 = getString(R.string.no_more_data_to_load_static);
                footerButtonforListview.setText("" + arraylistTasklist.size() + "/" + "" + arraylistTasklist.size() + " " +
                        getString(R.string.no_more_data_to_load));
            }

            //gettingFilterarraylistFromDb();
            /*arrayTasklistafterFiltered = new ArrayList<>();
            arrayTasklistafterFiltered = adapterTasklist.filterListview(strTaskFilter, strProjectFilter,
                    arrayFinishDate, arrayTrafficLightfilter, arrayStatusfilter, arrayDatefilter,
                    arrayTasklistAllDataPermenent);
            isFilterFired = true;
            setAdapterforFilter(arrayTasklistafterFiltered);*/
        } else {
            strLoadmore1 = "";
            strLoadmore2 = getString(R.string.no_more_data_to_load_0_0_static);
            footerButtonforListview.setText(getString(R.string.no_more_data_to_load_0_0));
        }
        if (arrayTasklistAllDataPermenent.size() > 0) {
            textviewSelectedDates.setTextColor(getResources().getColor(R.color.black_color));
        } else {
            textviewSelectedDates.setTextColor(getResources().getColor(R.color.textview_color_grey));
        }
    }

    //    adapter for filter task list
    public void setAdapterforFilter(ArrayList<TaskListModelClass> arraylistTasklistlocal) {
        if (arraylistTasklistlocal.size() > 0) {
            totRecord = 10;
            if (arraylistTasklistlocal.size() > totRecord) {
                //listviewTasklist.addFooterView(footerViewforListview);
                startRecord = 10;
                endRecord = 20;
                ArrayList<TaskListModelClass> arrayTemp = new ArrayList<TaskListModelClass>();
                arrayTemp = new ArrayList<>(arraylistTasklistlocal.subList(0, 10));
                arraylistTasklistforAdapter = new ArrayList<>();
                arraylistTasklistforAdapter.addAll(arrayTemp);
                adapterTasklist = new TasklistAdapter(getActivity(), arraylistTasklistforAdapter);
                listviewTasklist.setAdapter(adapterTasklist);
                strLoadmore2 = getString(R.string.load_more_data_static);
                footerButtonforListview.setText("" + startRecord + "/" + "" + arraylistTasklistlocal.size() +
                        " " + getString(R.string.load_more_data));
            } else {
                arraylistTasklistforAdapter = new ArrayList<>();
                arraylistTasklistforAdapter.addAll(arraylistTasklistlocal);
                adapterTasklist = new TasklistAdapter(getActivity(), arraylistTasklistforAdapter);
                listviewTasklist.setAdapter(adapterTasklist);
                strLoadmore2 = getString(R.string.no_more_data_to_load_static);
                footerButtonforListview.setText("" + arraylistTasklistforAdapter.size() + "/" + "" +
                        arraylistTasklistforAdapter.size() + " " + getString(R.string.no_more_data_to_load));
            }
        } else {
            strLoadmore2 = getString(R.string.no_more_data_to_load_0_0_static);
            footerButtonforListview.setText(getString(R.string.no_more_data_to_load_0_0));
        }
    }

    //    return no of task from Agreegatelist
    public void getTaskCountofJsonArray(JSONArray jsonArrayMain, int arrayNo) {
        for (int i = 0; i < jsonArrayMain.length(); i++) {
            try {
                JSONObject objInner = jsonArrayMain.getJSONObject(i);
                LogApp.e(" in for loop : ", " Node object in string : " + jsonArrayMain.getJSONObject(i));
                JSONArray jsonArrayInner = objInner.getJSONArray("children");

                for (int j = 0; j < jsonArrayInner.length(); j++) {
                    LogApp.e(" in for loop : ", " Project object in string : " + jsonArrayInner.getJSONObject(j));
                    TaskListModelClass dataEntity = recursionCallChild2(jsonArrayInner.getJSONObject(j), arrayNo);
                }
            } catch (Exception e) {
            }
        }
    }

    //    return json string to send control
    public String JsonString(String[] dates) {
        //String[] data = {"2016/07/06", "2016/07/07","2016/07/08","2016/07/09"};
        String[] date = dates;
        JSONArray json = new JSONArray(Arrays.asList(date));
        return json.toString();
    }

    @Override
    public void onStop() {
        super.onStop();
        preferenes.saveIsfirstTime(true);
    }

    //    filter listview from date selected
    public void filterDate(DateFilterDialogAdapter adapterDate) {
        ArrayList<FilterModelClass> arrayListSelected = adapterDate.getSelectedItem();
        arrayListSelectedDate = new ArrayList<>();
        //ArrayList<String> arraylistOriginalValue = new ArrayList<String>();
        arrayDatefilter = new ArrayList<String>();
        ArrayList<Integer> arrayDrawable = new ArrayList<>();
        if (arrayListSelected.size() > 0) {
            //dbHandler.deleteSelectedITem("1");
            ArrayList<String> arrayStringName = new ArrayList<>();
            for (int i = 0; i < arrayListSelected.size(); i++) {
                arrayStringName.add(arrayListSelected.get(i).getName());
                arrayDatefilter.add(arrayListSelected.get(i).getStrOriginalValue());
                arrayDrawable.add(arrayListSelected.get(i).getDrawable());
                arrayListSelectedDate.add(arrayListSelected.get(i).getKey());
            }
            String commaSeperated = "";
            String commaSeperatedID = "";
            commaSeperated = TextUtils.join(",", arrayStringName);
            //dbHandler.addSelectedItem("1", commaSeperated);
            if(popupWindowDate != null){
                popupWindowDate.dismiss();
            }

            isPopupDate = false;

            if (adapterTasklist != null) {
                arrayTasklistafterFiltered = new ArrayList<>();
                arrayTasklistafterFiltered = adapterTasklist.filterListview(strTaskFilter, strProjectFilter,
                        arrayFinishDate, arrayTrafficLightfilter, arrayStatusfilter, arrayDatefilter,
                        arrayTasklistAllDataPermenent);
                //if(arrayTasklistafterFiltered.size() > 0){
                isFilterFired = true;
                setAdapterforFilter(arrayTasklistafterFiltered);
                //}
            }
            //adapterTasklist.filterDateLight(arraylistOriginalValue);
            //adapterTasklist.setIsFiltered(true);
            if (arrayListSelected.size() > 1) {
                textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);
                imgFilterDate.setBackgroundResource(R.drawable.date_icon);
                imgPlusDate.setVisibility(View.VISIBLE);
            } else if (arrayListSelected.size() == 1) {
                imgFilterDate.setBackgroundResource(arrayDrawable.get(0));
                textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);
                imgPlusDate.setVisibility(View.GONE);
            } else {
                imgFilterDate.setBackgroundResource(R.drawable.date_icon);
                textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);
                imgPlusDate.setVisibility(View.INVISIBLE);
            }
        } else {
            imgFilterDate.setBackgroundResource(R.drawable.date_icon);
            imgPlusDate.setVisibility(View.INVISIBLE);
            textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);
            //dbHandler.deleteSelectedITem("1");
            if(popupWindowDate != null){
                popupWindowDate.dismiss();
            }
            isPopupDate = false;
            if (arrayListSelectedDate.size() > 0 || arrayListSelectedStatus.size() > 0
                    || arrayListSelectedTraffic.size() > 0 || arrayListSelectedFinishDates.size() > 0) {
                if (adapterTasklist != null) {
                    arrayTasklistafterFiltered = new ArrayList<>();
                    arrayTasklistafterFiltered = adapterTasklist.filterListview(strTaskFilter, strProjectFilter,
                            arrayFinishDate, arrayTrafficLightfilter, arrayStatusfilter, arrayDatefilter,
                            arrayTasklistAllDataPermenent);
                    isFilterFired = true;
                    setAdapterforFilter(arrayTasklistafterFiltered);
                }
            } else {
                if (adapterTasklist != null) {
                    if (arrayTasklistLastTimeLoaded.size() > 0) {
                        arraylistTasklistforAdapter = new ArrayList<>();

                        arraylistTasklistforAdapter.addAll(arrayTasklistLastTimeLoaded);
                        //arrayTasklistLastTimeLoaded2.addAll(arrayTasklistLastTimeLoaded);
                        adapterTasklist = new TasklistAdapter(getActivity(), arraylistTasklistforAdapter);
                        listviewTasklist.setAdapter(adapterTasklist);

                        if (arraylistTasklistforAdapter.size() == arrayTasklistAllDataPermenent.size()) {
                            footerButtonforListview.setText(arraylistTasklistforAdapter.size() + "/" +
                                    arrayTasklistAllDataPermenent.size() + " " + getString(R.string.no_more_data_to_load));
                        } else {
                            footerButtonforListview.setText(arraylistTasklistforAdapter.size() + "/" +
                                    arrayTasklistAllDataPermenent.size() + " " + getString(R.string.load_more_data));
                        }

                        isFilterFired = false;
                        totRecord = arraylistTasklistforAdapter.size();
                        startRecord = arraylistTasklistforAdapter.size();
                        endRecord = arraylistTasklistforAdapter.size() + 10;
                    }

                }
            }
        }
    }

    //    filter listview from traffic
    public void filterTrafficLight(TrafficFilterDialogAdapter adapterTrafficLight) {
        ArrayList<FilterModelClass> arrayList = adapterTrafficLight.getSelectedItem();
        arrayListSelectedTraffic = new ArrayList<>();
        //ArrayList<String> arraylistOriginalValue = new ArrayList<String>();
        arrayTrafficLightfilter = new ArrayList<String>();
        ArrayList<Integer> arrayDrawable = new ArrayList<>();
        if (arrayList.size() > 0) {
            //dbHandler.deleteSelectedITem("3");
            ArrayList<String> arrayStringName = new ArrayList<>();
            for (int i = 0; i < arrayList.size(); i++) {
                arrayStringName.add(arrayList.get(i).getName());
                arrayTrafficLightfilter.add(arrayList.get(i).getStrOriginalValue());
                arrayListSelectedTraffic.add(arrayList.get(i).getKey());
                arrayDrawable.add(arrayList.get(i).getDrawable());
            }
            String commaSeperated = "";
            String commaSeperatedID = "";
            commaSeperated = TextUtils.join(",", arrayStringName);
            //dbHandler.addSelectedItem("3", commaSeperated);
            if(popupWindowTraffic != null){
                popupWindowTraffic.dismiss();
            }

            isPopupTraffic = false;

            if (adapterTasklist != null) {
                arrayTasklistafterFiltered = new ArrayList<>();
                arrayTasklistafterFiltered = adapterTasklist.filterListview(strTaskFilter, strProjectFilter,
                        arrayFinishDate, arrayTrafficLightfilter, arrayStatusfilter, arrayDatefilter,
                        arrayTasklistAllDataPermenent);
                //if(arrayTasklistafterFiltered.size() > 0){
                isFilterFired = true;
                setAdapterforFilter(arrayTasklistafterFiltered);
                //}
            }
            if (arrayList.size() > 1) {
                textviewLableTraffic.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);
                imgFilterTrafficelights.setBackgroundResource(R.drawable.traffic_lights);
                imgPlusTraffic.setVisibility(View.VISIBLE);
            } else if (arrayList.size() == 1) {
                imgFilterTrafficelights.setBackgroundResource(arrayDrawable.get(0));
                textviewLableTraffic.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);
                imgPlusTraffic.setVisibility(View.INVISIBLE);
            } else {
                imgFilterTrafficelights.setBackgroundResource(R.drawable.traffic_lights);
                textviewLableTraffic.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);
                imgPlusTraffic.setVisibility(View.INVISIBLE);
            }
        } else {
            imgFilterTrafficelights.setBackgroundResource(R.drawable.traffic_lights);
            imgPlusTraffic.setVisibility(View.INVISIBLE);
            textviewLableTraffic.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);
            if(popupWindowTraffic != null){
                popupWindowTraffic.dismiss();
            }
            isPopupTraffic = false;

            if (arrayListSelectedDate.size() > 0 || arrayListSelectedStatus.size() > 0
                    || arrayListSelectedTraffic.size() > 0 || arrayListSelectedFinishDates.size() > 0) {
                if (adapterTasklist != null) {
                    arrayTasklistafterFiltered = new ArrayList<>();
                    arrayTasklistafterFiltered = adapterTasklist.filterListview(strTaskFilter, strProjectFilter,
                            arrayFinishDate, arrayTrafficLightfilter, arrayStatusfilter, arrayDatefilter,
                            arrayTasklistAllDataPermenent);
                    //if(arrayTasklistafterFiltered.size() > 0){
                    isFilterFired = true;
                    setAdapterforFilter(arrayTasklistafterFiltered);
                    //}
                }
            } else {
                if (adapterTasklist != null) {
                    if (arrayTasklistLastTimeLoaded.size() > 0) {
                        arraylistTasklistforAdapter = new ArrayList<>();

                        arraylistTasklistforAdapter.addAll(arrayTasklistLastTimeLoaded);
                        adapterTasklist = new TasklistAdapter(getActivity(), arraylistTasklistforAdapter);
                        listviewTasklist.setAdapter(adapterTasklist);
                        if (arraylistTasklistforAdapter.size() == arrayTasklistAllDataPermenent.size()) {
                            footerButtonforListview.setText(arraylistTasklistforAdapter.size() + "/" +
                                    arrayTasklistAllDataPermenent.size() + " " + getString(R.string.no_more_data_to_load));
                        } else {
                            footerButtonforListview.setText(arraylistTasklistforAdapter.size() + "/" +
                                    arrayTasklistAllDataPermenent.size() + " " + getString(R.string.load_more_data));
                        }
                        isFilterFired = false;


                        totRecord = arraylistTasklistforAdapter.size();
                        startRecord = arraylistTasklistforAdapter.size();
                        endRecord = arraylistTasklistforAdapter.size() + 10;
                    }

                }
            }

        }
    }

    //    filter listview from status selected
    public void filterStatus(StatusFilterDialogAdapter adapterStatus) {
        ArrayList<FilterModelClass> arrayList = adapterStatus.getSelectedItem();
        arrayListSelectedStatus = new ArrayList<>();
        //ArrayList<String> arraylistOriginalValue = new ArrayList<String>();
        arrayStatusfilter = new ArrayList<String>();
        ArrayList<Integer> arrayDrawable = new ArrayList<>();
        if (arrayList.size() > 0) {
            //dbHandler.deleteSelectedITem("2");
            ArrayList<String> arrayStringName = new ArrayList<>();
            for (int i = 0; i < arrayList.size(); i++) {
                arrayStringName.add(arrayList.get(i).getName());
                arrayStatusfilter.add(arrayList.get(i).getStrOriginalValue());
                arrayListSelectedStatus.add(arrayList.get(i).getKey());
                arrayDrawable.add(arrayList.get(i).getDrawable());
            }
            String commaSeperated = "";
            String commaSeperatedID = "";
            commaSeperated = TextUtils.join(",", arrayStringName);
            //dbHandler.addSelectedItem("2", commaSeperated);
            if(popupWindowStatus != null){
                popupWindowStatus.dismiss();
            }
            isPopupStatus = false;

            if (adapterTasklist != null) {
                arrayTasklistafterFiltered = new ArrayList<>();
                arrayTasklistafterFiltered = adapterTasklist.filterListview(strTaskFilter, strProjectFilter,
                        arrayFinishDate, arrayTrafficLightfilter, arrayStatusfilter, arrayDatefilter,
                        arrayTasklistAllDataPermenent);
                //if(arrayTasklistafterFiltered.size() > 0){
                isFilterFired = true;
                setAdapterforFilter(arrayTasklistafterFiltered);
                //}
            }
            if (arrayList.size() > 1) {
                textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);
                imgFilterStatus.setBackgroundResource(R.drawable.status_icon);
                imgPlusStatus.setVisibility(View.VISIBLE);
            } else if (arrayList.size() == 1) {
                imgFilterStatus.setBackgroundResource(arrayDrawable.get(0));
                textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);
                imgPlusStatus.setVisibility(View.INVISIBLE);
            } else {
                imgFilterStatus.setBackgroundResource(R.drawable.status_icon);
                textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);
                imgPlusStatus.setVisibility(View.INVISIBLE);
            }

        } else {
            imgFilterStatus.setBackgroundResource(R.drawable.status_icon);
            imgPlusStatus.setVisibility(View.INVISIBLE);
            textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);
            //dbHandler.deleteSelectedITem("2");
            if(popupWindowStatus != null){
                popupWindowStatus.dismiss();
            }
            isPopupStatus = false;

            if (arrayListSelectedDate.size() > 0 || arrayListSelectedStatus.size() > 0
                    || arrayListSelectedTraffic.size() > 0 || arrayListSelectedFinishDates.size() > 0) {
                if (adapterTasklist != null) {
                    arrayTasklistafterFiltered = new ArrayList<>();
                    arrayTasklistafterFiltered = adapterTasklist.filterListview(strTaskFilter, strProjectFilter,
                            arrayFinishDate, arrayTrafficLightfilter, arrayStatusfilter, arrayDatefilter,
                            arrayTasklistAllDataPermenent);
                    //if(arrayTasklistafterFiltered.size() > 0){
                    isFilterFired = true;
                    setAdapterforFilter(arrayTasklistafterFiltered);
                    //}
                }
            } else {
                if (adapterTasklist != null) {
                    if (arrayTasklistLastTimeLoaded.size() > 0) {
                        arraylistTasklistforAdapter = new ArrayList<>();

                        arraylistTasklistforAdapter.addAll(arrayTasklistLastTimeLoaded);
                        adapterTasklist = new TasklistAdapter(getActivity(), arraylistTasklistforAdapter);
                        listviewTasklist.setAdapter(adapterTasklist);
                        if (arraylistTasklistforAdapter.size() == arrayTasklistAllDataPermenent.size()) {
                            footerButtonforListview.setText(arraylistTasklistforAdapter.size() + "/" +
                                    arrayTasklistAllDataPermenent.size() + " " + getString(R.string.no_more_data_to_load));
                        } else {
                            footerButtonforListview.setText(arraylistTasklistforAdapter.size() + "/" +
                                    arrayTasklistAllDataPermenent.size() + " " + getString(R.string.load_more_data));
                        }
                        isFilterFired = false;


                        totRecord = arraylistTasklistforAdapter.size();
                        startRecord = arraylistTasklistforAdapter.size();
                        endRecord = arraylistTasklistforAdapter.size() + 10;
                    }

                }
            }

        }
    }

    public void gettingTodayDateForService() {
        String todayDate = GlobalClass.dateFormateChanged2(new Date());
        ArrayList<String> arraylistTodaydate = new ArrayList<>();
        arraylistTodaydate.add(todayDate);
        arraylistTodaydate.add(todayDate);
        dateArray = new String[arraylistTodaydate.size()];
        dateArray = arraylistTodaydate.toArray(dateArray);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        preferenes.saveIsfirstTime(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        preferenes.saveIsfirstTime(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /* this is interface method will call when any date is selected from dialog */
    @Override
    public void clickedOkButton(ArrayList<String> selectedDates) {
        if (selectedDates.size() > 0) {
            arrayFinishDate = new ArrayList<>();
            arrayFinishDate.addAll(selectedDates);
            LogApp.e(" $$$$$$$$$$$ ", " clicked ok button fire : " + selectedDates);
            if (adapterTasklist != null) {
                arrayTasklistafterFiltered = new ArrayList<>();
                arrayTasklistafterFiltered = adapterTasklist.filterListview(strTaskFilter, strProjectFilter,
                        arrayFinishDate, arrayTrafficLightfilter, arrayStatusfilter, arrayDatefilter,
                        arrayTasklistAllDataPermenent);
                isFilterFired = true;
                setAdapterforFilter(arrayTasklistafterFiltered);
            }
            if (selectedDates.size() > 1) {
                textviewSelectedDates.setText(selectedDates.get(0).toString() + " +");
            } else {
                textviewSelectedDates.setText(selectedDates.get(0).toString());
            }
        } else {
            textviewSelectedDates.setText(getString(R.string.selected_date));
            arrayFinishDate = new ArrayList<>();
            if (adapterTasklist != null) {
                arrayTasklistafterFiltered = new ArrayList<>();
                arrayTasklistafterFiltered = adapterTasklist.filterListview(strTaskFilter, strProjectFilter,
                        arrayFinishDate, arrayTrafficLightfilter, arrayStatusfilter, arrayDatefilter,
                        arrayTasklistAllDataPermenent);
                isFilterFired = true;
                setAdapterforFilter(arrayTasklistafterFiltered);
            }
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        activity = context;
        ((HomeActivity) context).interfaceChangeLanguage = this;
        taskclicked = (Taskclicked) context;
    }

    //    setting language dynamically
    public void setLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
        textViewHeader.setText(getString(R.string.dashboard_header));
        textViewProjecName.setText(getString(R.string.project_footer));
        textViewTaskName.setText(getString(R.string.task));
        textviewLabledate.setText(R.string.date);
        textviewLableStatus.setText(getString(R.string.status));
        textviewLableTraffic.setText(getString(R.string.TrafficLights));
        textViewLableFinishDate.setText(getString(R.string.finish_date));
        textViewDone.setText(R.string.load);
        searchViewProject.setQueryHint(getResources().getString(R.string.search));
        searchViewTask.setQueryHint(getResources().getString(R.string.search));
        textviewSelectedDates.setText(R.string.selected_date);

    }

    //    interface method called when language is changed from profile and update here
    @Override
    public void changeLanguage() {
        setAdapterforTasklist(arraylistTasklistforAdapter);
        setLanguage(preferenes.getLanguage());
        if (arraylistAgreegate.size() > 0) {
            for (int i = 0; i < arraylistAgreegate.size(); i++) {
                if (i == 0) {
                    arraylistAgreegate.get(i).setStrName(getString(R.string.task_pending_tree));
                } else if (i == 1) {
                    arraylistAgreegate.get(i).setStrName(getString(R.string.task_completed_tree));
                } else if (i == 2) {
                    arraylistAgreegate.get(i).setStrName(getString(R.string.task_assign_tree));
                } else if (i == 3) {
                    arraylistAgreegate.get(i).setStrName(getString(R.string.task_completed_today_tree));
                } else if (i == 4) {
                    arraylistAgreegate.get(i).setStrName(getString(R.string.task_completed_next_five_day));
                }
            }

        }
        /*if(adapterAgreegate != null){
            adapterAgreegate.notifyDataSetChanged();
        }*/
        if (arraylistAgreegate.size() > 0) {
            listviewAgreegateList.setAdapter(null);
            adapterAgreegate = new AgreegateAdapter(getActivity(), arraylistAgreegate);
            listviewAgreegateList.setAdapter(adapterAgreegate);
        }

    }

    @Override
    public void profileChanged() {
    }

    public interface Taskclicked {
        void ontaskClicked(String strFinalResponsesStringTemp, String clickedTaskId, String task, int pos);

        void onAgreegateClicked(String strjson, String taskId, String agreegate, int pos);
    }

    //   this class is for textview presseffect
    public class CustomTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    textViewDone.setTextColor(getResources().getColor(R.color.black_color));
                    //white
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    textViewDone.setTextColor(getResources().getColor(R.color.white_color)); //black
                    break;
            }
            return false;
        }
    }

    public void setTouchListner(final TextView textview) {
        textview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textview.setTextColor(getResources().getColor(R.color.black_color));
                        //white
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        textview.setTextColor(getResources().getColor(R.color.white_color)); //black
                        break;
                }
                return false;
            }
        });
    }
    public void refreshArraylst() {
        arrayListSelectedFinishDates = new ArrayList<>();
        arrayListSelectedDate = new ArrayList<>();
        arrayListSelectedTraffic = new ArrayList<>();
        arrayListSelectedStatus = new ArrayList<>();

        arraylistAgreegate = new ArrayList<>();
        arrayDatefilter = new ArrayList<>();
        arrayTrafficLightfilter = new ArrayList<>();
        arrayStatusfilter = new ArrayList<>();
        arrayFinishDate = new ArrayList<>();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen for landscape and portrait and set portrait mode always
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            linearMain.setWeightSum(1f);
            /// for landscape
            /* this is to set HEIGHT as orientation change */
            paramCalendar = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.40f);
            paramListview = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.48f);
            paramHeader = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.06f);
            paramDatepanel = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.05f);

            /* this is to set WIDTH as orientation change */
            paramAgreegateWidth = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 0.34f);
            paramCalendarWidth = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 0.52f);
            paramViewBlank = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 0.10f);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            //setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // for portrait
            /* this is to set HEIGHT as orientation change */
            linearMain.setWeightSum(1f);
            paramCalendar = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.27f);
            paramListview = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.61f);
            paramHeader = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.06f);
            paramDatepanel = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 0.05f);

            /* this is to set WIDTH as orientation change */
            paramAgreegateWidth = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 0.46f);
            paramCalendarWidth = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 0.46f);
            paramViewBlank = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.MATCH_PARENT, 0.04f);
        }
        linearListview.setLayoutParams(paramListview);
        linearCalendar.setLayoutParams(paramCalendar);
        linearDatepanel.setLayoutParams(paramDatepanel);
        relativeHeader.setLayoutParams(paramHeader);

        relativeCalendar.setLayoutParams(paramCalendarWidth);
        linearAgreegate.setLayoutParams(paramAgreegateWidth);
        viewBlank.setLayoutParams(paramViewBlank);

    }
    public void setCaldroidFragment(Bundle savedInstanceState) {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        caldroidFragment = new CaldroidFragment();
        // If Activity is created after rotation
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        Calendar cal = Calendar.getInstance();
        // Min date is last 7 days
        cal.add(Calendar.YEAR, -7);
        Date minDate = cal.getTime();

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 7);
        Date maxDate = cal.getTime();

        // Attach to the activity
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        /*Date Currentdate=cal.getTime();
        startDate=cal.getTime();
        endDate=cal.getTime();
        caldroidFragment.setSelectedDates(startDate, endDate);
        caldroidFragment.refreshView();*/

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                /*Toast.makeText(getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();*/
                clickedDate = date;
                if (startDate != null && endDate != null) {
                    if (clickedDate.after(new Date()) || clickedDate.equals(new Date())) {
                        if(startDate.after(new Date())){
                            preferenes.saveShowToday(true);
                        }else {
                            preferenes.saveShowToday(false);
                        }
                    }
                    else if(clickedDate.before(new Date()) && endDate.after(new Date())){
                        if(clickedDate.before(startDate)){
                            //preferenes.saveShowToday(false);
                            preferenes.saveShowToday(false);
                        }else {
                            //preferenes.saveShowToday(false);
                            preferenes.saveShowToday(true);
                        }

                    }
                    else {
                        if (endDate.after(new Date())) {
                            if(startDate.after(new Date())){
                                preferenes.saveShowToday(true);
                            }else {
                                preferenes.saveShowToday(false);
                            }

                        } else {
                            preferenes.saveShowToday(true);
                        }
                    }
                    if (clickedDate.equals(startDate) && startDate.equals(endDate)) {
                        startDate = null;
                        endDate = null;
                        isFirsttimeClicked=true;
                        caldroidFragment.setSelectedDates(startDate, endDate);
                        caldroidFragment.refreshView();
                    } else if (clickedDate.before(startDate)) {
                        startDate = clickedDate;
                    } else if (clickedDate.after(endDate)) {
                        endDate = clickedDate;
                        isFirsttimeClicked=true;
                    } else if (clickedDate.after(startDate)) {
                        endDate = clickedDate;
                    } else if (clickedDate.equals(startDate) || clickedDate.equals(endDate)) {
                        startDate = clickedDate;
                        endDate = clickedDate;
                    }
                } else {
                    if (startDate != null) {
                        endDate = clickedDate;
                    } else {
                        startDate = clickedDate;
                        endDate = clickedDate;
                    }
                    Date temp = null;
                    if (startDate.after(endDate)) {
                        temp = endDate;
                        endDate = startDate;
                        startDate = temp;

                    }
                }
                caldroidFragment.setSelectedDates(startDate, endDate);
                caldroidFragment.refreshView();
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                /*Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                /*Toast.makeText(getApplicationContext(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    /*Toast.makeText(getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();*/
                }
            }

            @Override
            public void onDatePickerClicked() {
                showDatePickerDialog();
            }

            @Override
            public void onDoneClicked() {
                if (startDateLast != null && endDateLast != null) {
                    if(startDate != null && endDate != null){
                        if(startDate == startDateLast && endDate == endDateLast){
                        }
                        else {
                            doneButoonTocallService();
                        }
                    }
                } else {
                    doneButoonTocallService();
                }
            }
        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
    }

}
