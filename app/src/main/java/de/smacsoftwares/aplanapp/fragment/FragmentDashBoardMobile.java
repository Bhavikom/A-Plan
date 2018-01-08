package de.smacsoftwares.aplanapp.fragment;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
 * Created by SSOFT4 on 3/7/2017.
 */

public class FragmentDashBoardMobile extends Fragment implements
        View.OnClickListener,FragmentProfile.InterfaceProfileChanged, InterfaceChangeLanguageDashboard {

    PreferencesHelper preferenes;
    DatabaseHandler dbHandler;

    Bundle bundle;
    private CaldroidFragment caldroidFragment;
    Date startDate = null, endDate = null, clickedDate = null,startDateLast=null,endDateLast=null;
    Date startDateLastSelected = null, endDateLastSelected = null;
    public boolean isDateDialogFirsttime = true;

    String strLoadmore1 = "", strLoadmore2 = "";
    ImageView imgrefresh;
    boolean flag1 = false;
    ArrayList<String> arrayListSelectedDate = new ArrayList<>();
    ArrayList<String> arrayListSelectedStatus = new ArrayList<>();
    ArrayList<String> arrayListSelectedTraffic = new ArrayList<>();
    ArrayList<String> arrayListSelectedFinishDates = new ArrayList<>();

    public static ArrayList<Date> selectedDatesStartEnd;
    public static ArrayList<Date> arraylistStartEndDate = new ArrayList<>();
    static Calendar nextYear;
    static Calendar lastYear;
    // all arraylist
    static List<Date> allDatesBetweenDates;
    public Context context;
    String strNodedata = "";
    TaskclickedDashboardMobile taskclicked;
    String currentVisibleView = "listview";

    Activity activity;
    SearchView searchViewProject, searchViewTask;
    TextView textViewHeader, textViewProjecName, textViewTaskName;
    TextView textViewCounter;
    TextView textviewStartDateLabel, textviewEndDateLabel, textviewSortingLabel;
    ImageView imgFilterDate, imgFilterStatus, imgFilterTrafficelights;
    ImageView imgSortProject, imgSortTask, imgRefresh;
    ImageView imgPlusDate, imgPlusTraffic, imgPlusStatus;
    TextView textviewPleseSelectDateLabel;
    LinearLayout linearCalendar, linearListview, linearDatepanel, linearMain;
    LinearLayout linearAgreegate;
    LinearLayout linearTabLayout;
    View viewBlank;
    private Button footerButtonforListview;
    LinearLayout.LayoutParams paramListview;
    LinearLayout.LayoutParams paramCalendar;
    LinearLayout.LayoutParams paramHeader;
    LinearLayout.LayoutParams paramDatepanel;
    RelativeLayout relativeCalendar, relativeSorting, relativeListLayout, relativeCalendarInner, relativeMain;
    FrameLayout frameLayoutmain;
    LinearLayout.LayoutParams paramCalendarWidth;
    LinearLayout.LayoutParams paramAgreegateWidth;
    LinearLayout.LayoutParams paramViewBlank;
    LinearLayout linearStartDate, linearEndDate, linearSorting;
    TaskListModelClass tasklistModelTemp;
    View rootView;
    // all adapter
    AgreegateAdapter adapterAgreegate;
    RelativeLayout reletive2;
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
    private ArrayList<String> arraylistFinishDates;
    ArrayList<TaskListModelClass> arrayTasklistAllDataPermenent = new ArrayList<>();
    ArrayList<TaskListModelClass> arrayTasklistafterFiltered = new ArrayList<>();
    ArrayList<TaskListModelClass> arrayTasklistLastTimeLoaded = new ArrayList<>();
    ArrayList<TaskListModelClass> arrayTasklistLastTimeLoaded2 = new ArrayList<>();
    ArrayList<JSONObject> arraylistJsonObjChildren = new ArrayList<JSONObject>();
    List<Date> arraylistLastSelectedDates = new ArrayList<>();

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
    boolean flagStartDate = false, flagEndDate = false, flagSorting = false;
    private View footerViewforListview;
    private TextView textViewDone, textviewStartDate, textviewSelectedDates, textviewEndDate;
    //private ListView listviewAgreegateList;
    ImageView imgDatePicker;
    ListView listviewTasklist;
    TextView textviewTitle;
    PopupWindow popupWindowcounter, popupWindowProfile;
    // private ArrayList<String> arraylistFinishDates;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //MyApplication.component(getActivity()).inject(this);
        preferenes = new PreferencesHelper(getActivity());
        dbHandler = new DatabaseHandler(getActivity());
        bundle = savedInstanceState;
        GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);
        GlobalClass.fontBold = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPathBold);
        GlobalClass.fontLight = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPathLight);
        GlobalClass.fontMedium = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPathMedium);
        preferenes.saveIsfirstTime(true);
        preferenes.saveShowToday(true);
        preferenes.saveIsFirsttimeToLoadControl("yes");
        initControl();
        setCaldroidFragment(savedInstanceState);
        return rootView;
    }

    public void initControl() {


        footerButtonforListview = (Button) rootView.findViewById(R.id.buttonLoadMore);
        textViewCounter = (TextView) rootView.findViewById(R.id.txt_counter);
        textViewHeader = (TextView) rootView.findViewById(R.id.txt_header);
        textViewHeader.setTypeface(GlobalClass.fontBold);
        textviewStartDateLabel = (TextView) rootView.findViewById(R.id.textview_startdate_label);
        textviewEndDateLabel = (TextView) rootView.findViewById(R.id.textview_enddate_label);
        textviewSortingLabel = (TextView) rootView.findViewById(R.id.textview_sorting_label);
        textviewPleseSelectDateLabel = (TextView) rootView.findViewById(R.id.textview_lable_please_selecte_date);
        linearStartDate = (LinearLayout) rootView.findViewById(R.id.linear_start_date);
        linearEndDate = (LinearLayout) rootView.findViewById(R.id.linear_enddate);
        linearSorting = (LinearLayout) rootView.findViewById(R.id.linear_sorting);
        relativeCalendar = (RelativeLayout) rootView.findViewById(R.id.relativeCalendar);
        relativeCalendarInner = (RelativeLayout) rootView.findViewById(R.id.relativeCalendarInner);
        relativeMain = (RelativeLayout) rootView.findViewById(R.id.relative_main);
        GlobalClass.setupTypeface(relativeMain, GlobalClass.fontBold);
        relativeSorting = (RelativeLayout) rootView.findViewById(R.id.reletivesorting);
        relativeListLayout = (RelativeLayout) rootView.findViewById(R.id.reletive_list);
        imgRefresh = (ImageView) rootView.findViewById(R.id.imgrefresh);
        textViewDone = (TextView) rootView.findViewById(R.id.textviewDone);
        //setTouchListner(textViewDone);
        textViewDone = (TextView) rootView.findViewById(R.id.textviewDone);
        imgPlusDate = (ImageView) rootView.findViewById(R.id.imgplus_date);
        imgPlusStatus = (ImageView) rootView.findViewById(R.id.imgplus_status);
        imgPlusTraffic = (ImageView) rootView.findViewById(R.id.imgplus_traffic);
//        textViewLableFinishDate = (TextView) rootView.findViewById(R.id.txt_lbl_Finishdate);
        imgPlusDate.setVisibility(View.INVISIBLE);
        imgPlusStatus.setVisibility(View.INVISIBLE);
        imgPlusTraffic.setVisibility(View.INVISIBLE);
        //textviewCurrentMonth = (TextView) rootView.findViewById(R.id.textviewcurrentmonth);
        //textviewCurrentMonth.setOnClickListener(this);
        textviewStartDate = (TextView) rootView.findViewById(R.id.textviewcurrentdate);
        textviewStartDate.setTypeface(GlobalClass.fontMedium);
        textviewEndDate = (TextView) rootView.findViewById(R.id.textviewcurrentdate2);
        textviewEndDate.setTypeface(GlobalClass.fontMedium);
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
        textviewSelectedDates = (TextView) rootView.findViewById(R.id.textviewSelectedDates);
        textViewDone = (TextView) rootView.findViewById(R.id.textviewDone);
        textViewDone.setOnClickListener(this);
        textviewSelectedDates.setOnClickListener(this);
        //textviewCurrentMonth = (TextView) rootView.findViewById(R.id.textviewcurrentmonth);
        //textviewCurrentMonth.setOnClickListener(this);
        textviewStartDate = (TextView) rootView.findViewById(R.id.textviewcurrentdate);
        imgSortProject.setOnClickListener(this);
        imgSortTask.setOnClickListener(this);
        imgFilterDate.setOnClickListener(this);
        imgFilterTrafficelights.setOnClickListener(this);
        imgFilterStatus.setOnClickListener(this);
        textViewDone.setOnClickListener(this);
        imgRefresh.setOnClickListener(this);
        linearStartDate.setOnClickListener(this);
        linearEndDate.setOnClickListener(this);
        linearSorting.setOnClickListener(this);
        textViewCounter.setOnClickListener(this);
        footerButtonforListview.setOnClickListener(this);
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
                //taskclicked.ontaskClicked(strNodedata, clickedTaskId, "task",position);
                taskclicked.ontaskClickedMobile(strNodedata, clickedTaskId, "task", position);
            }
        });
        RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams) relativeCalendar.getLayoutParams();
        //relativeParams.setMargins(0,0, 0,(GlobalClass.screenHeight(getActivity())*20)/100);  // left, top, right, bottom
        relativeParams.height = (GlobalClass.screenHeight(getActivity()) * 55) / 100;
        relativeCalendar.setLayoutParams(relativeParams);

        textviewStartDate.setText(GlobalClass.dateFormateChanged(new Date()));
        textviewEndDate.setText(GlobalClass.dateFormateChanged(new Date()));
        nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 50);

        lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -50);

        /*calendarPicker = (CalendarPickerView) rootView.findViewById(R.id.calendar_view);
        calendarPicker.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.RANGE) //
                .withSelectedDate(new Date());
        calendarPickerSelectUnslectEvent();*/

        gettingTodayDateForService();
        /* calling dashboard service firtst time */
        if (GlobalClass.isNetworkAvailable(getActivity())) {
            getDashboardData();
        } else {
            //listviewTasklist.addFooterView(footerViewforListview);
            footerButtonforListview.setText(getString(R.string.no_more_data_to_load_0_0));
            addOfflineDatatoAgreegate();
            //GlobalClass.showToastInternet(getActivity());
            GlobalClass.showCustomDialogInternet(getActivity());
        }
        linearMain = (LinearLayout) rootView.findViewById(R.id.linearMain);
        GlobalClass.setupTypeface(linearMain, GlobalClass.fontRegular);
        textViewCounter.setTypeface(GlobalClass.fontMedium);
        textViewHeader.setTypeface(GlobalClass.fontBold);
        linearTabLayout = (LinearLayout) rootView.findViewById(R.id.linear_tab);
        GlobalClass.setupTypeface(linearTabLayout, GlobalClass.fontLight);

        setLanguage(preferenes.getLanguage());
    }

    public boolean isTwoArrayListsWithSameValues(List<Date> list1, List<Date> list2) {
        //null checking
        if (list1 == null && list2 == null)
            return true;
        if ((list1 == null && list2 != null) || (list1 != null && list2 == null))
            return false;

        if (list1.size() != list2.size())
            return false;
        for (Object itemList1 : list1) {
            if (!list2.contains(itemList1))
                return false;
        }

        return true;
    }

    public void doneButoonTocallService() {

        if (startDate != null || endDate != null) {
            resetAllvariable();
            if (startDate != null && endDate != null) {
                startDateLast=startDate;
                endDateLast=endDate;
               /* textviewStartDate.setText(GlobalClass.dateFormateChanged(startDate)
                        + " To " + GlobalClass.dateFormateChanged(endDate));
                textviewEndDate.setText(GlobalClass.dateFormateChanged(startDate)
                        + " To " + GlobalClass.dateFormateChanged(endDate));*/
                textviewStartDate.setText(GlobalClass.dateFormateChanged(startDate));
                textviewEndDate.setText(GlobalClass.dateFormateChanged(endDate));
            }
            arraylistStringDates.clear();
            arraylistStringDates.add(GlobalClass.dateFormateChanged2(startDate));
            arraylistStringDates.add(GlobalClass.dateFormateChanged2(endDate));

            dateArray = new String[arraylistStringDates.size()];
            dateArray = arraylistStringDates.toArray(dateArray);
            if (GlobalClass.isNetworkAvailable(getActivity())) {
                getDashboardData();
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
                            getDashboardData();
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

    public void getDashboardData() {
        try {
            refreshArraylst();
            progressDialog = new ProgressDialog(getActivity());
            //progressDialog.create();

            SpannableString ss = new SpannableString(getString(R.string.fetching_data));
            ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new CustomTypefaceSpan("", GlobalClass.fontRegular), 0, ss.length(), 0);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(ss);
            progressDialog.show();

            String jsonDateString = JsonString(dateArray);
            RequestBody userId = RequestBody.create(MediaType.parse("text/plain"),preferenes.getUserID());
            RequestBody dateRange = RequestBody.create(MediaType.parse("text/plain"), jsonDateString);
            RequestBody projectSearch = RequestBody.create(MediaType.parse("text/plain"), "");
            RequestBody taskSearch = RequestBody.create(MediaType.parse("text/plain"), "");
            RequestBody projectOrder = RequestBody.create(MediaType.parse("text/plain"), "1");
            RequestBody projectLimit = RequestBody.create(MediaType.parse("text/plain"), "12");
            RequestBody taskOrder = RequestBody.create(MediaType.parse("text/plain"), "1");


            GenericHelper helper = new GenericHelper(getActivity());
            Call<String> call = helper.getRetrofit().getDashboard(dateRange, projectSearch, taskSearch,
                    projectOrder, projectLimit, taskOrder);

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
                                    /*if (arraylistAgreegate.size() > 0) {
                                        adapterAgreegate = new AgreegateAdapter(getActivity(), arraylistAgreegate);
                                      *//*  listviewAgreegateList.setAdapter(adapterAgreegate);*//*
                                    }*/
                                }

                            } catch (JSONException e) {
                                LogApp.e(" json exception ", " exception while parsing : " + e.toString());
                                e.printStackTrace();
                                arraylistTasklistforAdapter.clear();
                                arrayTasklistLastTimeLoaded.clear();
                                  /*  arraylistFinishDates.clear();*/
                                listviewTasklist.setAdapter(null);
                            }
                            if(progressDialog != null) {
                                progressDialog.dismiss();
                            }
                        } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1009) {
                                /* whenever access token is expire get 1009
                                from service and call here getNewdeviceToken servcie and replace onld one with new device token */
                            updateAccessToken();
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                        }
                        else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == Integer.parseInt(getString(R.string.LICENSE_EXPIRED))) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                            //GlobalClass.showToast(activity, getString(R.string.something_is_wrong));
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
                            //GlobalClass.showToast(activity, getString(R.string.something_is_wrong));
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
                        if(progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    LogApp.e(" dashboard failed ", " response from service : " + t.toString());
                    resetAlllistandAdapter();
                    if(progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    addOfflineDatatoAgreegate();
                    strLoadmore2 = getString(R.string.no_more_data_to_load_0_0_static);
                    footerButtonforListview.setText(getString(R.string.no_more_data_to_load_0_0));
                    if(t instanceof UnknownHostException){
                        //progressDialog.dismiss();
                        //GlobalClass.showToast(getActivity(), getString(R.string.url_not_found));
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

    public String JsonString(String[] dates) {
        //String[] data = {"2016/07/06", "2016/07/07","2016/07/08","2016/07/09"};
        String[] date = dates;
        JSONArray json = new JSONArray(Arrays.asList(date));
        return json.toString();
    }

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
       /* if (arraylistAgreegate.size() > 0) {
            adapterAgreegate = new AgreegateAdapter(getActivity(), arraylistAgreegate);
            listviewAgreegateList.setAdapter(adapterAgreegate);
        }*/

    }

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
           /* textviewSelectedDates.setTextColor(getResources().getColor(R.color.black_color));*/
        } else {
            /*textviewSelectedDates.setTextColor(getResources().getColor(R.color.textview_color_grey));*/
        }
    }

    public void resetAlllistandAdapter() {
        arraylistTaskAssignedTree.clear();
        arraylistTaskCompleteNextFiveDayTree.clear();
        arraylistTaskCompleteTodayTree.clear();
        arraylistTaskCompleteTree.clear();
        arraylistTaskPendingTree.clear();
        /*listviewAgreegateList.setAdapter(null);*/
        arraylistTasklistforAdapter.clear();
        arrayTasklistLastTimeLoaded.clear();
        /*  arraylistFinishDates.clear();*/
        listviewTasklist.setAdapter(null);
        strLoadmore2 = (getString(R.string.no_more_data_to_load_static));
        footerButtonforListview.setText(getString(R.string.no_more_data_to_load));
    }

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
                adapterTasklist.notifyDataSetChanged();
                //adapterTasklist = new TasklistAdapter(getActivity(), arraylistTasklistforAdapter);
                //listviewTasklist.setAdapter(adapterTasklist);
                strLoadmore2 = getString(R.string.no_more_data_to_load_static);
                footerButtonforListview.setText("" + arraylistTasklistforAdapter.size() + "/" + "" +
                        arraylistTasklistforAdapter.size() + " " + getString(R.string.no_more_data_to_load));
            }
        } else {
            strLoadmore2 = getString(R.string.no_more_data_to_load_0_0_static);
            footerButtonforListview.setText(getString(R.string.no_more_data_to_load_0_0));
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

    public void dismissPopup(PopupWindow popup) {
        if (popup != null && popup.isShowing()) {
            popup.dismiss();
        }
    }

    public void resetDatePanelColor() {
        linearStartDate.setBackgroundColor(getResources().getColor(R.color.white));
        linearEndDate.setBackgroundColor(getResources().getColor(R.color.white));
        linearSorting.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void setColorforDatePanel(int pos, LinearLayout linearStart, LinearLayout linearEnd, LinearLayout linearSort) {
        if (pos == 1) {
            linearStart.setBackgroundColor(getResources().getColor(R.color.sky_blue_color));
            linearEnd.setBackgroundColor(getResources().getColor(R.color.white));
            linearSort.setBackgroundColor(getResources().getColor(R.color.white));
        } else if (pos == 2) {
            linearEndDate.setBackgroundColor(getResources().getColor(R.color.orange_color_light));
            linearStartDate.setBackgroundColor(getResources().getColor(R.color.white));
            linearSorting.setBackgroundColor(getResources().getColor(R.color.white));
        } else if (pos == 3) {
            linearSorting.setBackgroundColor(getResources().getColor(R.color.darkorrange));
            linearEndDate.setBackgroundColor(getResources().getColor(R.color.white));
            linearStartDate.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonLoadMore:
                paginationForListview();
                break;

            case R.id.linear_start_date:
                if (currentVisibleView.equalsIgnoreCase("listview")) {
                    setTouchListner(textviewStartDateLabel);
                    flagStartDate = true;
                    relativeCalendarInner.setVisibility(View.VISIBLE);
                    relativeSorting.setVisibility(View.GONE);
                    slideDown();
                    /*slideDownAnimation(relativeListLayout);*/
                } else {

                    fadeIndAnimation(relativeCalendarInner, relativeSorting);
                }
                if (startDate != null && endDate != null) {

                    isFirsttimeClicked = true;
                    preferenes.saveShowToday(true);
                    /*arraylistStartEndDate = new ArrayList<>();
                    arraylistStartEndDate.add(calendarPicker.getSelectedDates().get(0));
                    arraylistStartEndDate.add(calendarPicker.getSelectedDates().get(calendarPicker.getSelectedDates().size()-1));


                    ArrayList<Date> temp = new ArrayList<>();
                    temp.add(calendarPicker.getSelectedDates().get(0));
                    calendarPicker.init(lastYear.getTime(), nextYear.getTime()) //
                            .inMode(CalendarPickerView.SelectionMode.RANGE) //
                            .withSelectedDates(temp);*/
                    endDateLastSelected = endDate;
                    endDate = startDate;
                    //Date startDateTemp=startDate;&&&&&
                    caldroidFragment.setSelectedDates(startDate, endDate);
                    caldroidFragment.refreshView();
                    textviewStartDate.setText(GlobalClass.dateFormateChanged(startDate));
                    //textviewEndDate.setText(GlobalClass.dateFormateChanged(temp.get(0)));
                    textviewPleseSelectDateLabel.setText(strSelecteddates());
                }

                currentVisibleView = "startdate";
                textviewPleseSelectDateLabel.setVisibility(View.VISIBLE);
                setColorforDatePanel(1, linearStartDate, linearEndDate, linearSorting);
                break;
            case R.id.linear_enddate:
                if (currentVisibleView.equalsIgnoreCase("listview")) {
                    setTouchListner(textviewStartDateLabel);
                    flagStartDate = true;
                    relativeCalendarInner.setVisibility(View.VISIBLE);
                    relativeSorting.setVisibility(View.GONE);
                    slideDown();
                    /*slideDownAnimation(relativeListLayout);*/
                } else {

                    fadeIndAnimation(relativeCalendarInner, relativeSorting);
                }
                currentVisibleView = "enddate";
                textviewPleseSelectDateLabel.setVisibility(View.VISIBLE);
                setColorforDatePanel(2, linearStartDate, linearEndDate, linearSorting);
                if (endDateLastSelected != null) {
                    endDate = endDateLastSelected;
                    endDateLastSelected = null;
                }
                if (endDate != null && endDate.after(new Date())) {
                    if(startDate != null && startDate.after(new Date())){
                        preferenes.saveShowToday(true);
                    }
                    else {
                        preferenes.saveShowToday(false);
                    }

                }
                else {
                    preferenes.saveShowToday(true);
                }
                if (startDate != null && endDate != null) {
                    //ArrayList<Date> temp = new ArrayList<>();
                    //temp.add(calendarPicker.getSelectedDates().get(0));
                    caldroidFragment.setSelectedDates(startDate, endDate);
                    caldroidFragment.refreshView();
                    //textviewStartDate.setText(GlobalClass.dateFormateChanged(temp.get(0)));
                    //textviewEndDate.setText(GlobalClass.dateFormateChanged(temp.get(0)));

                    textviewPleseSelectDateLabel.setText(strSelecteddates());
                }
                break;
            case R.id.linear_sorting:
                if (currentVisibleView.equalsIgnoreCase("listview")) {
                    slideDown();
                    /*slideDownAnimation(relativeListLayout);*/
                    relativeCalendarInner.setVisibility(view.GONE);
                    relativeSorting.setVisibility(view.VISIBLE);
                    flagSorting = true;
                } else if (currentVisibleView.equalsIgnoreCase("startdate") ||
                        currentVisibleView.equalsIgnoreCase("enddate")) {
                    fadeIndAnimation(relativeSorting, relativeCalendarInner);
                }
                currentVisibleView = "filter";
                textviewPleseSelectDateLabel.setVisibility(View.INVISIBLE);
                setColorforDatePanel(3, linearStartDate, linearEndDate, linearSorting);
                break;
            case R.id.txt_counter:
                showCounterPopup();
                break;
            case R.id.img_date_picker:
                break;
            case R.id.textviewDone:
                linearEndDate.setBackgroundColor(getResources().getColor(R.color.white));
                linearStartDate.setBackgroundColor(getResources().getColor(R.color.white));
                linearSorting.setBackgroundColor(getResources().getColor(R.color.white));
                if (currentVisibleView.equalsIgnoreCase("startdate") || currentVisibleView.equalsIgnoreCase("enddate")) {
                    if (startDateLast != null && endDateLast != null) {
                        if(startDate != null && endDate != null){
                            if(startDate == startDateLast && endDate == endDateLast){

                            }
                            else {
                                doneButoonTocallService();
                            }
                        }
                        /*if (calendarPicker.getSelectedDates().size() > 0) {
                            if(isTwoArrayListsWithSameValues(arraylistLastSelectedDates,calendarPicker.getSelectedDates())){
                            }
                            else {
                                doneButoonTocallService();
                            }
                        }*/
                    } else {
                        doneButoonTocallService();
                    }

                    slideup();
                    currentVisibleView = "listview";
                } else {
                    currentVisibleView = "listview";
                    slideup();
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
                textviewPleseSelectDateLabel.setText(getResources().getString(R.string.please_enter_your_dates));
                resetDatePanelColor();
                break;
        }


    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void slideDownAnimation(final View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, relativeCalendar.getHeight());
        animate.setDuration(800);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // clear animation to prevent flicker
                view.clearAnimation();
                // set new "real" position of wrapper
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                lp.addRule(RelativeLayout.BELOW, R.id.relativeCalendar);
                view.setLayoutParams(lp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        /*ObjectAnimator posterSlider = ObjectAnimator.ofFloat(view, "translationY", relativeCalendar.getHeight());
        posterSlider.setDuration(800); // duration 5 seconds
        posterSlider.start();
        flagStartDate = true;


        ViewGroup.LayoutParams params = relativeListLayout.getLayoutParams();
        params.height = 600;
        relativeListLayout.setLayoutParams(params);*/

        /*AnimationSet set = new AnimationSet(true);
        // slideDown Animation
        Animation animation = new TranslateAnimation(0,0,0,relativeCalendar.getHeight());
        animation.setDuration(800);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(final Animation anim) {
            };
            @Override
            public void onAnimationRepeat(final Animation anim) {
            };
            @Override
            public void onAnimationEnd(final Animation anim) {
                *//*ViewGroup.LayoutParams params = relativeListLayout.getLayoutParams();
                params.height = 600;
                relativeListLayout.setLayoutParams(params);*//*

                // clear animation to prevent flicker
                view.clearAnimation();
                // set new "real" position of wrapper
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                lp.addRule(RelativeLayout.BELOW, R.id.relativeCalendar);
                view.setLayoutParams(lp);
            }
        });
        set.addAnimation(animation);
        // set and start animation
        view.startAnimation(animation);*/
    }

    public void slideUpAnimation(final View view) {
       /* ObjectAnimator posterSlider = ObjectAnimator.ofFloat(view, "translationY", relativeCalendar.getX());
        posterSlider.setDuration(800); // duration 5 seconds
        posterSlider.start();//s
        flagStartDate = false;*/
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, -relativeCalendar.getHeight());
        animate.setDuration(800);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                // set new "real" position of wrapper
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                //lp.addRule(RelativeLayout.BELOW, R.id.relativeCalendar);
                view.setLayoutParams(lp);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void slideup() {
        ObjectAnimator posterSlider = ObjectAnimator.ofFloat(relativeListLayout, "translationY", relativeListLayout.getX());
        posterSlider.setDuration(800);
        posterSlider.start();//s
        flag1 = false;

    }

    public void slideDown() {
        ObjectAnimator posterSlider = ObjectAnimator.ofFloat(relativeListLayout, "translationY", relativeCalendar.getHeight());
        posterSlider.setDuration(800);
        posterSlider.start();

        flag1 = true;

    }
        /*AnimationSet set = new AnimationSet(true);
        // slideDown Animation
        Animation animation = new TranslateAnimation(0,0,0,relativeCalendar.getX());
        animation.setDuration(800);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(final Animation anim) {
            };
            @Override
            public void onAnimationRepeat(final Animation anim) {
            };
            @Override
            public void onAnimationEnd(final Animation anim) {
                *//*ViewGroup.LayoutParams params = relativeListLayout.getLayoutParams();
                params.height = 600;
                relativeListLayout.setLayoutParams(params);*//*

                // clear animation to prevent flicker
                view.clearAnimation();
                // set new "real" position of wrapper
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                //lp.addRule(RelativeLayout.BELOW, R.id.relativeCalendar);
                view.setLayoutParams(lp);
            }
        });
        set.addAnimation(animation);
        // set and start animation
        view.startAnimation(animation);*/


    public void fadeIndAnimation(View view, View view2) {
        view2.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);
        view.setAnimation(fadeIn);
    }

    private void scrollMyListViewToBottom() {
        listviewTasklist.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listviewTasklist.setSelection(adapterTasklist.getCount() - 1);
            }
        });
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
            ArrayList<String> selectedString = new ArrayList<>();
            /*if (!selectedItem.equalsIgnoreCase("")) {
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
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
                    popupWindowDate.setWidth((GlobalClass.screenWidth(getContext()) * 100) / 100);
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
                //setTouchListner(textViewDone);
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
                    popupWindowStatus.setWidth((GlobalClass.screenWidth(getContext()) * 100) / 100);
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
                    popupWindowTraffic.setWidth((GlobalClass.screenWidth(getContext()) * 100) / 100);
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
                /*textviewLableTraffic.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                imgFilterTrafficelights.setBackgroundResource(R.drawable.traffic_lights);
                imgPlusTraffic.setVisibility(View.VISIBLE);
            } else if (arrayList.size() == 1) {
                imgFilterTrafficelights.setBackgroundResource(arrayDrawable.get(0));
                /*textviewLableTraffic.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                imgPlusTraffic.setVisibility(View.INVISIBLE);
            } else {
                imgFilterTrafficelights.setBackgroundResource(R.drawable.traffic_lights);
                /*textviewLableTraffic.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);*/
                imgPlusTraffic.setVisibility(View.INVISIBLE);
            }
        } else {
            imgFilterTrafficelights.setBackgroundResource(R.drawable.traffic_lights);
            imgPlusTraffic.setVisibility(View.INVISIBLE);
            /*textviewLableTraffic.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);*/
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
              /*  textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                imgFilterStatus.setBackgroundResource(R.drawable.status_icon);
                imgPlusStatus.setVisibility(View.VISIBLE);
            } else if (arrayList.size() == 1) {
                imgFilterStatus.setBackgroundResource(arrayDrawable.get(0));
                /*textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                imgPlusStatus.setVisibility(View.INVISIBLE);
            } else {
                imgFilterStatus.setBackgroundResource(R.drawable.status_icon);
                /*textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);*/
                imgPlusStatus.setVisibility(View.INVISIBLE);
            }

        } else {
            imgFilterStatus.setBackgroundResource(R.drawable.status_icon);
            imgPlusStatus.setVisibility(View.INVISIBLE);
            /*textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);*/
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
              /*  textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                imgFilterDate.setBackgroundResource(R.drawable.date_icon);
                imgPlusDate.setVisibility(View.VISIBLE);
            } else if (arrayListSelected.size() == 1) {
                imgFilterDate.setBackgroundResource(arrayDrawable.get(0));
                /*textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                imgPlusDate.setVisibility(View.GONE);
            } else {
                imgFilterDate.setBackgroundResource(R.drawable.date_icon);
                /*textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);*/
                imgPlusDate.setVisibility(View.INVISIBLE);
            }
        } else {
            imgFilterDate.setBackgroundResource(R.drawable.date_icon);
            imgPlusDate.setVisibility(View.INVISIBLE);
           /* textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);*/
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
        /*if (!selectedItemDate.equalsIgnoreCase("")) {

            String[] split = selectedItemDate.split(",");
            for (int i = 0; i < split.length; i++) {
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
        /*if (!selectedItemStatus.equalsIgnoreCase("")) {
            String[] split = selectedItemStatus.split(",");
            for (int i = 0; i < split.length; i++) {
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
        /*if (!selectedItemTraffic.equalsIgnoreCase("")) {
            String[] split = selectedItemTraffic.split(",");
            for (int i = 0; i < split.length; i++) {
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
        /*if (!selectedItemFinishdate.equalsIgnoreCase("")) {

            String[] split = selectedItemFinishdate.split(",");

            for (int i = 0; i < split.length; i++) {
                arrayFinishDate.add(split[i]);
            }
        }*/
    }

    public void showSelectedDates() {
        //String selectedItem = dbHandler.getSelectedItems("1");
        ArrayList<String> selectedString = new ArrayList<>();
        /*if (!selectedItem.equalsIgnoreCase("")) {

            String[] split = selectedItem.split(",");

            for (int i = 0; i < split.length; i++) {
                selectedString.add(split[i]);
            }
        }*/
        if (selectedString.size() > 0) {
            if (selectedString.size() > 1) {
               /* textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                imgFilterDate.setBackgroundResource(R.drawable.date_plus);
            } else if (selectedString.size() == 1) {
                if (selectedString.get(0).equalsIgnoreCase("Completion overrun")) {
                    /*textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterDate.setBackgroundResource(R.drawable.completion);
                } else if (selectedString.get(0).equalsIgnoreCase("Complete today")) {
                    /*textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterDate.setBackgroundResource(R.drawable.completed_today);
                } else if (selectedString.get(0).equalsIgnoreCase("Completed in 1 week")) {
                    /*textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterDate.setBackgroundResource(R.drawable.completed_in_one_week);
                } else if (selectedString.get(0).equalsIgnoreCase("Started tasks")) {
                    /*textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterDate.setBackgroundResource(R.drawable.started_task);
                } else if (selectedString.get(0).equalsIgnoreCase("Pending tasks")) {
                    /*textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterDate.setBackgroundResource(R.drawable.pending_task);
                } else if (selectedString.get(0).equalsIgnoreCase("Task without date")) {
                    /*textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterDate.setBackgroundResource(R.drawable.task_without_date);
                } else if (selectedString.get(0).equalsIgnoreCase("Passive tasks")) {
                    /*textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterDate.setBackgroundResource(R.drawable.reminders);
                } else if (selectedString.get(0).equalsIgnoreCase("Canceled tasks")) {
                    /*textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterDate.setBackgroundResource(R.drawable.canceled_task);
                }
            }
        }
    }

    public void showSelectedStatus() {
        //String selectedItem = dbHandler.getSelectedItems("2");
        ArrayList<String> selectedString = new ArrayList<>();
        /*if (!selectedItem.equalsIgnoreCase("")) {

            String[] split = selectedItem.split(",");

            for (int i = 0; i < split.length; i++) {
                selectedString.add(split[i]);
            }
        }*/
        if (selectedString.size() > 0) {
            if (selectedString.size() > 1) {
                /*textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                imgFilterStatus.setBackgroundResource(R.drawable.status_plus);
            } else if (selectedString.size() == 1) {
                if (selectedString.get(0).equalsIgnoreCase("Notice Arrow")) {
                    /*textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterStatus.setBackgroundResource(R.drawable.notice_arrow);
                } else if (selectedString.get(0).equalsIgnoreCase("Not Clear")) {
                    /*textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterStatus.setBackgroundResource(R.drawable.not_clear);
                } else if (selectedString.get(0).equalsIgnoreCase("Critical")) {
                    /*textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterStatus.setBackgroundResource(R.drawable.critical);
                } else if (selectedString.get(0).equalsIgnoreCase("Remindar")) {
                    /*textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterStatus.setBackgroundResource(R.drawable.reminders);
                } else if (selectedString.get(0).equalsIgnoreCase("Completed tasks")) {
                    /*textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterStatus.setBackgroundResource(R.drawable.completed_task);
                } else if (selectedString.get(0).equalsIgnoreCase("Limit")) {
                    /*textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterStatus.setBackgroundResource(R.drawable.limit);
                } else if (selectedString.get(0).equalsIgnoreCase("CapacityTooHigh")) {
                    /*textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterStatus.setBackgroundResource(R.drawable.capacity_too_high);
                } else if (selectedString.get(0).equalsIgnoreCase("CapacityTooLow")) {
                 /*   textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterStatus.setBackgroundResource(R.drawable.capacity_too_low);
                }
            }
        }
    }

    public void showSelectedTrafficLight() {
        //String selectedItem = dbHandler.getSelectedItems("3");
        ArrayList<String> selectedString = new ArrayList<>();
        /*if (!selectedItem.equalsIgnoreCase("")) {

            String[] split = selectedItem.split(",");

            for (int i = 0; i < split.length; i++) {
                selectedString.add(split[i]);
            }
        }*/
        if (selectedString.size() > 0) {
            if (selectedString.size() > 1) {
                /*textviewLableTraffic.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                imgFilterTrafficelights.setBackgroundResource(R.drawable.traffic_plus);
            } else if (selectedString.size() == 1) {
                if (selectedString.get(0).equalsIgnoreCase("Green")) {
                  /*  textviewLableTraffic.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterTrafficelights.setBackgroundResource(R.drawable.traffic_green);
                } else if (selectedString.get(0).equalsIgnoreCase("Yellow")) {
                    /*textviewLableTraffic.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterTrafficelights.setBackgroundResource(R.drawable.traffic_yellow);
                } else if (selectedString.get(0).equalsIgnoreCase("Red")) {
                    /*textviewLableTraffic.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
                    imgFilterTrafficelights.setBackgroundResource(R.drawable.traffic_red);
                }
            }
        }
    }

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
                popupWindowFinishDates.setWidth((GlobalClass.screenWidth(getContext()) * 100) / 100);
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
                    /*    textViewLableFinishDate.setTypeface(GlobalClass.fontRegular, Typeface.BOLD);*/
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
                        /*textViewLableFinishDate.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);*/
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
            GlobalClass.showCustomMessageDialog(activity,getString(R.string.select_date_title),getString(R.string.please_select_date_first));
        }
    }
    public void refreshActivityAndCallService() {

        preferenes.saveShowToday(true);
        textviewStartDate.setText(GlobalClass.dateFormateChanged(new Date()));
        textviewEndDate.setText(GlobalClass.dateFormateChanged(new Date()));
        startDate = null;
        endDate = null;
        if (currentVisibleView.equalsIgnoreCase("startdate") || currentVisibleView.equalsIgnoreCase("enddate")
                || currentVisibleView.equalsIgnoreCase("filter")) {
            slideup();
            /*slideUpAnimation(relativeListLayout);*/
        }
        currentVisibleView = "listview";

        preferenes.saveIsfirstTime(true);
        arraylistLastSelectedDates.clear();
        startDateLastSelected = null;
        endDateLastSelected = null;
        startDateLast=null;
        endDateLast=null;
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
            getDashboardData();
        } else {
            //listviewTasklist.addFooterView(footerViewforListview);
            footerButtonforListview.setText(getString(R.string.no_more_data_to_load_0_0));
            addOfflineDatatoAgreegate();
            //GlobalClass.showToastInternet(getActivity());
            GlobalClass.showCustomDialogInternet(getActivity());
        }
    }

    //    this is recursion method to get child data from service

    public void resetAllvariable() {
        totRecord = 10;
        startRecord = 0;
        endRecord = 0;

        preferenes.clearClickedAgreegatePosition();
        preferenes.clearClickedTaskPosition();
        preferenes.clearCurrentFiredFilter();
        preferenes.clearTaskLastFiredFilter();
        /*textViewLableFinishDate.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);*/
        /*textviewLabledate.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);
        textviewLableTraffic.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);
        textviewLableStatus.setTypeface(GlobalClass.fontRegular, Typeface.NORMAL);

        */
      /*  textviewSelectedDates.setTextColor(getResources().getColor(R.color.textview_color_grey));*/
/*        textviewSelectedDates.setText(getString(R.string.selected_date));*/
        preferenes.clearAgreegateLastFiredFilter();


        imgFilterDate.setBackgroundResource(R.drawable.date_icon);
        imgFilterStatus.setBackgroundResource(R.drawable.status_icon);
        imgFilterTrafficelights.setBackgroundResource(R.drawable.traffic_lights);

        refreshArraylst();
        isFilterFired = false;
        isFirsttimeClicked = true;
        //dbHandler.deleteAllSelectedITem();
        preferenes.saveIsfirstTime(false);
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
    public void showCounterPopup() {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.custom_dialog_counter, null);
        popupWindowcounter = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindowcounter.setWidth((GlobalClass.screenWidth(getContext()) * 90) / 100);
        popupWindowcounter.setHeight((GlobalClass.screenHeight(getContext()) * 38) / 100);
        popupWindowcounter.setOutsideTouchable(true);
        //popupWindowcounter.setFocusable(true);
        popupWindowcounter.showAsDropDown(textViewCounter,0, 20, Gravity.CENTER | Gravity.BOTTOM);
        ListView listviewAgreegte = (ListView) popupView.findViewById(R.id.listviewAgreegate);
        adapterAgreegate = new AgreegateAdapter(getActivity(), arraylistAgreegate);
        listviewAgreegte.setAdapter(adapterAgreegate);
        listviewAgreegte.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (popupWindowcounter != null) {
                    if (!arraylistAgreegate.get(position).getStrNo().equals("0"))
                        popupWindowcounter.dismiss();
                }
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
                    //taskclicked.onAgreegateClicked(str, GroupId, "agreegate",position);
                    taskclicked.onAgreegateClickedMobile(str, GroupId, "agreegate", position);
                } else {
                }
            }
        });
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        activity = context;
        ((HomeActivity) context).interfaceChangeLanguage = this;
        taskclicked = (TaskclickedDashboardMobile) context;
    }

    private String strSelecteddates() {
        String str = "";
        str = getResources().getString(R.string.you_have_selected) + " " +
                daysBetween(startDate, endDate) + " " + getResources().getString(R.string.date_s);
        return str;
    }

    public long daysBetween(Date startDate, Date endDate) {
        long daysBetween = 0;
        if (startDate != null && endDate != null) {
            Calendar sDate = Calendar.getInstance();
            sDate.setTime(startDate);


            Calendar eDate = Calendar.getInstance();
            eDate.setTime(endDate);


            while (sDate.before(eDate)) {
                sDate.add(Calendar.DAY_OF_MONTH, 1);
                daysBetween++;
            }
            if (daysBetween == 0) {
                daysBetween = 1;
            } else {
                daysBetween = daysBetween + 1;
            }

        }
        return daysBetween;
    }

    /*private void calendarPickerSelectUnslectEvent(){
        calendarPicker.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                textviewPleseSelectDateLabel.setText(strSelecteddates());
                        if(calendarPicker.getSelectedDates().size() > 0){
                            setColorforDatePanel(2,linearStartDate,linearEndDate,linearSorting);
                        }
                        if(calendarPicker.getSelectedDates().size() > 0){
                            if (calendarPicker.getSelectedDates().size() > 1) {
                                textviewStartDate.setText(GlobalClass.dateFormateChanged(calendarPicker.getSelectedDates().get(0)));

                                textviewEndDate.setText(GlobalClass.dateFormateChanged(calendarPicker.getSelectedDates().get
                                        (calendarPicker.getSelectedDates().size() - 1)));
                            }
                            else {
                                textviewStartDate.setText(GlobalClass.dateFormateChanged(calendarPicker.getSelectedDates().get(0)));
                                textviewEndDate.setText(GlobalClass.dateFormateChanged(calendarPicker.getSelectedDates().get(0)));
                            }
                        }
                        else {
                            textviewStartDate.setText(GlobalClass.dateFormateChanged(new Date()));
                            textviewEndDate.setText(GlobalClass.dateFormateChanged(new Date()));
                        }

            }
            @Override
            public void onDateUnselected(Date date) {
                textviewPleseSelectDateLabel.setText(strSelecteddates());
                if(calendarPicker.getSelectedDates().size() > 0){
                    if (calendarPicker.getSelectedDates().size() > 1) {
                        textviewStartDate.setText(GlobalClass.dateFormateChanged(calendarPicker.getSelectedDates().get(0)));

                        textviewEndDate.setText(GlobalClass.dateFormateChanged(calendarPicker.getSelectedDates().get
                                (calendarPicker.getSelectedDates().size() - 1)));
                    }
                    else {
                        textviewStartDate.setText(GlobalClass.dateFormateChanged(calendarPicker.getSelectedDates().get(0)));
                        textviewEndDate.setText(GlobalClass.dateFormateChanged(calendarPicker.getSelectedDates().get(0)));
                    }
                }
                else {
                    textviewStartDate.setText(GlobalClass.dateFormateChanged(new Date()));
                    textviewEndDate.setText(GlobalClass.dateFormateChanged(new Date()));
                }
            }
        });
    }*/
    public void setLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
        textViewHeader.setText(getString(R.string.dashboard_header));
//        textViewProjecName.setText(getString(R.string.project_footer));
        //      textViewTaskName.setText(getString(R.string.task));
        textViewCounter.setText(getString(R.string.counter));
        textviewStartDateLabel.setText(getString(R.string.startDate));
        textviewEndDateLabel.setText(getString(R.string.endDate));
        textviewSortingLabel.setText(getString(R.string.sorting));
        textViewDone.setText(R.string.load);
        searchViewProject.setQueryHint(getResources().getString(R.string.project_search));
        searchViewTask.setQueryHint(getResources().getString(R.string.task_search));
        textviewSelectedDates.setText(R.string.selected_date);
        if (startDate != null & endDate != null) {
            textviewPleseSelectDateLabel.setText(strSelecteddates());
        } else {
            textviewPleseSelectDateLabel.setText(getResources().getString(R.string.please_enter_your_dates));
        }

    }

    public interface TaskclickedDashboardMobile {
        void ontaskClickedMobile(String strFinalResponsesStringTemp, String clickedTaskId, String task, int pos);

        void onAgreegateClickedMobile(String strjson, String taskId, String agreegate, int pos);
    }

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
        if (adapterAgreegate != null) {
            adapterAgreegate.notifyDataSetChanged();
        }
    }

    @Override
    public void profileChanged() {

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

        //caldroidFragment.visibleDateIcon(false);
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
                caldroidFragment.clearBackgroundDrawableForDate(new Date());
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
                if (isFirsttimeClicked) {
                    isFirsttimeClicked = false;
                    //textviewPleseSelectDateLabel.setText(strSelecteddates());
                    if (startDate != null) {
                        setColorforDatePanel(2, linearStartDate, linearEndDate, linearSorting);
                    }
                }
                textviewPleseSelectDateLabel.setText(strSelecteddates());
                if (startDate != null) {
                    if (startDate != null && endDate != null) {
                        showStartandEndDate(startDate, endDate);
                    } else {
                        showStartandEndDate(startDate, startDate);
                    }
                } else {
                    showStartandEndDate(new Date(), new Date());
                }
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
                //showDatePickerDialog();
            }

            @Override
            public void onDoneClicked() {
                doneButoonTocallService();
            }
        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);

    }

    public void showStartandEndDate(Date start, Date end) {
        textviewStartDate.setText(GlobalClass.dateFormateChanged(start));
        textviewEndDate.setText(GlobalClass.dateFormateChanged(end));
    }
}


