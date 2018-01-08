package de.smacsoftwares.aplanapp.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.Model.GeneralFilterDataSet;
import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.activity.HomeActivity;
import de.smacsoftwares.aplanapp.adapter.DatesFilterScreenAdapter;
import de.smacsoftwares.aplanapp.adapter.SpinnerAdapter;
import de.smacsoftwares.aplanapp.util.DatabaseHandler;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.InterfaceProjectClicked;
import de.smacsoftwares.aplanapp.util.InterfaceforSelectFilter;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * Created by SSOFT4 on 7/18/2016.
 */
public class FragmentFilterDate extends Fragment implements View.OnClickListener,InterfaceforSelectFilter,
        InterfaceProjectClicked
{

    PreferencesHelper prefrences;
    DatabaseHandler dbHandler;

    boolean isTablet;
    String comefrom="this";
    boolean flagEdittext1 = false,flagEdittext2 = false,flagEdittext3 = false,flagEdittext4 = false;
    SpinnerAdapter spinnerAdapter;
    boolean isTextChanged=false;
    boolean isTimetoChangeText=false;
    Context context;
    boolean isClickableEdittext=true,isClickableEdittext2=true;
    static Date selectedDate1=null;
    static Date selectedDate2=null;
    static Date selectedDate3=null;
    static Date selectedDate4=null;
    View rootView;
    private static DatePickerDialog datePicker;
    //String strDate1="",strDate2="",strDate3="",strDate4="";


    TextView textviewDone,textviewGeneralFilter;
    TextView textViewCriteria1,textViewCriteria2,textViewTo1,textViewTo2,textViewTitle;
    Activity activtiy;
    RelativeLayout relativeFooter,relativeMain;
    LinearLayout linearBack;
    //LinearLayout linearCriteria1,linearCriteria2;
    EditText edittextCriteria1,edittextCriteria2,edittextSelectedCriteria1,edittextSelectedCriteria2;
    TextView textviewBetween1,textviewBetween2;
    EditText edittextDate1, edittextDate2, edittextDate3, edittextDate4;
    EditText edittextTimePeriod1, edittextTimePeriod2;
    ImageView imgDeleteFilter,imgBack;
    ImageButton imgCalendar1,imgCalendar2,imgCalendar3,imgCalendar4;
    ImageButton imgCross1,imgCross2,imgCross3,imgCross4;

    ArrayList<String> arraylistCriteria = new ArrayList<>();
    ArrayList<String> arraylistDuration = new ArrayList<>();
    ArrayList<String> arraylistTimePeriod = new ArrayList<>();

    ArrayList<String> arraylistCriteriaComparision = new ArrayList<>();
    ArrayList<String> arraylistDurationComparision = new ArrayList<>();
    ArrayList<String> arraylistTimePeriodComparision = new ArrayList<>();
    ArrayList<String> arraylistTimeRange = new ArrayList<>();

    String strSelectedCriteria1="",strSelectedCriteria2="";
    String strSelectedDuration1="",strSelectedDuration2="";
    String strSelectedTimePeriod1="",strSelectedTimePeriod2="",strSelectedTimePeriod1toSend="",strSelectedTimePeriod2toSend="";
    String strCriteria1="",strSelectedCriterial1="",strDate1="",strDate2="",strTimeUnit1="",strCriteria2="",
            strSelectedCriterial2="",
    strDate3="",strDate4="",strTimeUnit2="";
    ArrayList<GeneralFilterDataSet> arraylistgetedFromDatabase = new ArrayList<>();
    ArrayList<String> arraylistValue = new ArrayList<>();
    PopupWindow popupWindowTimeRange;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_filter_date, container,false);
        //MyApplication.component(getActivity()).inject(this);
        prefrences = new PreferencesHelper(getActivity());
        dbHandler = new DatabaseHandler(getActivity());
        initControl();
        return rootView;
    }
    //    this method intialize control and class all initial work
    public void initControl()
    {
        isTablet = getResources().getBoolean(R.bool.isTablet);
        imgBack = (ImageView)rootView.findViewById(R.id.imgback);
        linearBack = (LinearLayout)rootView.findViewById(R.id.linearback);
        linearBack.setOnClickListener(this);
        relativeMain = (RelativeLayout)rootView.findViewById(R.id.relative_date_main);
        GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);
        GlobalClass.setupTypeface(relativeMain, GlobalClass.fontLight);

        imgDeleteFilter=(ImageView)rootView.findViewById(R.id.img_delete);
        imgDeleteFilter.setOnClickListener(this);
        textviewDone = (TextView)rootView.findViewById(R.id.textview_done);
        textviewDone.setOnClickListener(this);
        textviewDone.setVisibility(View.GONE);

        textViewTitle = (TextView)rootView.findViewById(R.id.textview_lable_title);
        textViewTitle.setTypeface(GlobalClass.fontBold);
        textViewCriteria1 = (TextView)rootView.findViewById(R.id.textview_lable_criteria1);
        textViewCriteria2 = (TextView)rootView.findViewById(R.id.textview_lable_criteria2);

        imgCalendar1=(ImageButton)rootView.findViewById(R.id.imgcalendar1);
        imgCalendar2=(ImageButton)rootView.findViewById(R.id.imgcalendar2);
        imgCalendar3=(ImageButton)rootView.findViewById(R.id.imgcalendar3);
        imgCalendar4=(ImageButton)rootView.findViewById(R.id.imgcalendar4);
        imgCross1=(ImageButton)rootView.findViewById(R.id.imgcross1);
        imgCross2=(ImageButton)rootView.findViewById(R.id.imgcross2);
        imgCross3=(ImageButton)rootView.findViewById(R.id.imgcross3);
        imgCross4=(ImageButton)rootView.findViewById(R.id.imgcross4);

        imgCalendar1.setOnClickListener(this);
        imgCalendar2.setOnClickListener(this);
        imgCalendar3.setOnClickListener(this);
        imgCalendar4.setOnClickListener(this);
        imgCross1.setOnClickListener(this);
        imgCross2.setOnClickListener(this);
        imgCross3.setOnClickListener(this);
        imgCross4.setOnClickListener(this);

        textviewGeneralFilter = (TextView)rootView.findViewById(R.id.textview_generalfilter);
        textviewGeneralFilter.setOnClickListener(this);
        textviewGeneralFilter.setOnTouchListener(new CustomTouchListener());

        edittextCriteria1=(EditText)rootView.findViewById(R.id.edittext_criteria1);
        edittextCriteria2=(EditText)rootView.findViewById(R.id.edittext_criteria2);
        edittextCriteria1.setText(getString(R.string.empty));
        edittextCriteria2.setText(getString(R.string.empty));
        edittextSelectedCriteria1=(EditText)rootView.findViewById(R.id.edittext_selected_criteria1);
        edittextSelectedCriteria2=(EditText)rootView.findViewById(R.id.edittext_selected_criteria2);

        textviewBetween1=(TextView)rootView.findViewById(R.id.textview_between1);
        edittextDate1 =(EditText)rootView.findViewById(R.id.textview_date1);
        edittextDate2 =(EditText)rootView.findViewById(R.id.textview_date2);
        edittextTimePeriod1 =(EditText)rootView.findViewById(R.id.textview_day_unit1);
        edittextTimePeriod1.setOnClickListener(this);

        textviewBetween2=(TextView)rootView.findViewById(R.id.textview_between2);
        edittextDate3 =(EditText)rootView.findViewById(R.id.textview_date3);
        edittextDate4 =(EditText)rootView.findViewById(R.id.textview_date4);
        edittextTimePeriod2 =(EditText)rootView.findViewById(R.id.textview_day_unit2);
        edittextTimePeriod2.setOnClickListener(this);
        edittextTimePeriod1.setKeyListener(null);
        edittextTimePeriod2.setKeyListener(null);

        edittextDate1.setOnClickListener(this);
        edittextDate2.setOnClickListener(this);
        edittextDate3.setOnClickListener(this);
        edittextDate4.setOnClickListener(this);

        editTextTextChange(edittextDate1);
        editTextTextChange(edittextDate2);
        editTextTextChange(edittextDate3);
        editTextTextChange(edittextDate4);

        editableFalse(edittextDate1,3);
        editableFalse(edittextDate2,3);
        editableFalse(edittextDate3,3);
        editableFalse(edittextDate4,3);

        edittextCriteria1.setOnClickListener(this);
        edittextCriteria2.setOnClickListener(this);
        editableFalse(edittextCriteria1,3);
        editableFalse(edittextCriteria2,3);

        edittextSelectedCriteria1.setOnClickListener(this);
        edittextSelectedCriteria2.setOnClickListener(this);
        edittextSelectedCriteria1.setKeyListener(null);
        edittextSelectedCriteria2.setKeyListener(null);

        fillArraylists();
        visibleCriteria1(false);
        visibleCriteria2(false);

        arraylistgetedFromDatabase = new ArrayList<>();
        ///////////// for date filter to selection of date
        //ArrayList<TextFilterKeyValueDataset> arraylistValue = new ArrayList<>();
        if(dbHandler.getCursorCountFilterForGeneralFilter(prefrences.getProfileId(),getString(R.string.DateFilter_text),prefrences.getLanguage(),prefrences.getUserID()) > 0){
            arraylistgetedFromDatabase = dbHandler.getAllGeneralFilterData(prefrences.getProfileId(),getString(R.string.DateFilter_text),prefrences.getLanguage(),prefrences.getUserID());
        }
        //arraylistValue = dbHandler.getDateFilterData(prefrences.getUserID(),prefrences.getProfileId(),"database");
        if(arraylistgetedFromDatabase.size() > 0){
            JSONObject obj1 = new JSONObject();
            for (int i=0;i<arraylistgetedFromDatabase.size();i++){
//              this is for criteria
                if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_criterion1_string))){
                    if(Locale.getDefault().getLanguage().equalsIgnoreCase(getString(R.string.language_germany))){
                        String strCriteria="";
                        if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.empty_string))){
                            strCriteria=getString(R.string.empty);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.planned_time_to_be_spent_on_task_string))){
                            strCriteria=getString(R.string.planned_time_to_be_spent_on_task);
                            strSelectedCriteria1=getString(R.string.planned_time_to_be_spent_on_task_string);

                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.begin_pid_string))){
                            strCriteria=getString(R.string.begin_pid);
                            strSelectedCriteria1=getString(R.string.begin_pid_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.end_pid_string))){
                            strCriteria=getString(R.string.end_pid);
                            strSelectedCriteria1=getString(R.string.end_pid_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.actual_time_to_be_spent_on_task_string))){
                            strCriteria=getString(R.string.actual_time_to_be_spent_on_task);
                            strSelectedCriteria1=getString(R.string.actual_time_to_be_spent_on_task_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.begin_act_string))){
                            strCriteria=getString(R.string.begin_act);
                            strSelectedCriteria1=getString(R.string.begin_act_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.end_act_string))){
                            strCriteria=getString(R.string.end_act);
                            strSelectedCriteria1=getString(R.string.end_act_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.end_progn_string))){
                            strCriteria=getString(R.string.end_progn);
                            strSelectedCriteria1=getString(R.string.end_progn_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.entry_date_string))){
                            strCriteria=getString(R.string.entry_date);
                            strSelectedCriteria1=getString(R.string.entry_date_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.reshow_date_string))){
                            strCriteria=getString(R.string.reshow_date);
                            strSelectedCriteria1=getString(R.string.reshow_date_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.complete_on_string))){
                            strCriteria=getString(R.string.complete_on);
                            strSelectedCriteria1=getString(R.string.complete_on_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.last_change_string))){
                            strCriteria=getString(R.string.last_change);
                            strSelectedCriteria1=getString(R.string.last_change_string);
                        }
                        setEdittextValue(edittextCriteria1,strCriteria);
                        //strSelectedCriteria1=edittextCriteria1.getText().toString();
                    }
                    else {
                        setEdittextValue(edittextCriteria1,arraylistgetedFromDatabase.get(i).getValue());
                        strSelectedCriteria1=edittextCriteria1.getText().toString();
                    }

                }
//                this is for criteria type
                if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_criterion1Type_string))){
                    if(Locale.getDefault().getLanguage().equalsIgnoreCase(getString(R.string.language_germany))){
                        String strCriteriaType="";
                        if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.before_string))){
                            strCriteriaType=getString(R.string.before);
                            strSelectedDuration1=getString(R.string.before_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.on_string))){
                            strCriteriaType=getString(R.string.on);
                            strSelectedDuration1=getString(R.string.on_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.after_string))){
                            strCriteriaType=getString(R.string.after);
                            strSelectedDuration1=getString(R.string.after_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.between_string))){
                            strCriteriaType=getString(R.string.between);
                            strSelectedDuration1=getString(R.string.between_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.time_range_string))){
                            strCriteriaType=getString(R.string.time_range);
                            strSelectedDuration1=getString(R.string.time_range_string);
                        }
                        setEdittextValue(edittextSelectedCriteria1,strCriteriaType);
                        //strSelectedDuration1=edittextSelectedCriteria1.getText().toString();
                    }
                    else {
                        setEdittextValue(edittextSelectedCriteria1,arraylistgetedFromDatabase.get(i).getValue());
                        strSelectedDuration1=edittextSelectedCriteria1.getText().toString();
                    }

                }
                if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_criterion1TimeFrom_string))){
                    setEdittextValue(edittextDate1,arraylistgetedFromDatabase.get(i).getValue());
                    //edittextDate1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar, 0);
                    editableTrue2(edittextDate1,false);
                    setVisibilityCross(1);
                    flagEdittext1=true;
                }
                if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_criterion1TimeTo_string))){
                    setEdittextValue(edittextDate2,arraylistgetedFromDatabase.get(i).getValue());
                    //edittextDate2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar, 0);
                    editableTrue2(edittextDate2,false);
                    flagEdittext2=true;
                    setVisibilityCross(1);
                    if(!TextUtils.isEmpty(getEdittextValue(edittextDate1)) &&
                            !TextUtils.isEmpty(getEdittextValue(edittextDate2))){
                        textviewBetween1.setVisibility(View.VISIBLE);
                        textviewBetween1.setText(getString(R.string.to));
                    }
                }
                if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_criterion1TimeUnit_string))){
                    if(Locale.getDefault().getLanguage().equalsIgnoreCase(getString(R.string.language_germany))){
                        String strTimeUnit="";
                        if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.days_string))){
                            strTimeUnit=getString(R.string.days);
                            strSelectedTimePeriod1toSend=getString(R.string.days_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.weeks_string))){
                            strTimeUnit=getString(R.string.weeks);
                            strSelectedTimePeriod1toSend=getString(R.string.weeks_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.months_string))){
                            strTimeUnit=getString(R.string.months);
                            strSelectedTimePeriod1toSend=getString(R.string.months_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.years_string))){
                            strTimeUnit=getString(R.string.years);
                            strSelectedTimePeriod1toSend=getString(R.string.years_string);
                        }

                        setEdittextValue(edittextTimePeriod1,strTimeUnit);
                        //strSelectedTimePeriod1toSend = edittextTimePeriod1.getText().toString();
                    }
                    else {
                        setEdittextValue(edittextTimePeriod1, arraylistgetedFromDatabase.get(i).getValue());
                        strSelectedTimePeriod1toSend = edittextTimePeriod1.getText().toString();
                    }
                }
                if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_criterion1FromDate_string))){
                    Date dateSelected = GlobalClass.convertStringToDate2(arraylistgetedFromDatabase.get(i).getValue());
                    String dateStr = GlobalClass.dateFormateChanged(dateSelected);
                    setEdittextValue(edittextDate1,dateStr);
                    //edittextDate1.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar, 0);
                    editableTrue2(edittextDate1,true);
                    setVisibilityCalendar(1);
                    flagEdittext1=false;
                }
                if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_criterion1ToDate_string))){
                    if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_criterion1FromDate_string))){
                        Date dateSelected = GlobalClass.convertStringToDate2(arraylistgetedFromDatabase.get(i).getValue());
                        String dateStr = GlobalClass.dateFormateChanged(dateSelected);

                        setEdittextValue(edittextDate2,dateStr);
                        //edittextDate2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar, 0);
                        editableTrue2(edittextDate2,true);
                        flagEdittext2=false;
                        setVisibilityCalendar(5);
                        if(!TextUtils.isEmpty(getEdittextValue(edittextDate1)) &&
                                !TextUtils.isEmpty(getEdittextValue(edittextDate2))){
                            textviewBetween1.setVisibility(View.VISIBLE);
                            textviewBetween1.setText(getString(R.string.and));
                        }
                    }else {
                        Date dateSelected = GlobalClass.convertStringToDate2(arraylistgetedFromDatabase.get(i).getValue());
                        String dateStr = GlobalClass.dateFormateChanged(dateSelected);

                        setEdittextValue(edittextDate2,dateStr);
                        //edittextDate2.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar, 0);
                        editableTrue2(edittextDate2,true);
                        flagEdittext2=false;
                        setVisibilityCalendar(2);
                        if(!TextUtils.isEmpty(getEdittextValue(edittextDate1)) &&
                                !TextUtils.isEmpty(getEdittextValue(edittextDate2))){
                            textviewBetween1.setVisibility(View.VISIBLE);
                            textviewBetween1.setText(getString(R.string.and));
                        }
                    }
                }
                ////////////////////////////// this is for criteria 22222
                if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_criterion2_string))){
                    if(Locale.getDefault().getLanguage().equalsIgnoreCase(getString(R.string.language_germany))){
                        String strCriteria2="";
                        if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.empty_string))){
                            strCriteria2=getString(R.string.empty);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.planned_time_to_be_spent_on_task_string))){
                            strCriteria2=getString(R.string.planned_time_to_be_spent_on_task);
                            strSelectedCriteria2=getString(R.string.planned_time_to_be_spent_on_task_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.begin_pid_string))){
                            strCriteria2=getString(R.string.begin_pid);
                            strSelectedCriteria2=getString(R.string.begin_pid_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.end_pid_string))){
                            strCriteria2=getString(R.string.end_pid);
                            strSelectedCriteria2=getString(R.string.end_pid_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.actual_time_to_be_spent_on_task_string))){
                            strCriteria2=getString(R.string.actual_time_to_be_spent_on_task);
                            strSelectedCriteria2=getString(R.string.actual_time_to_be_spent_on_task_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.begin_act_string))){
                            strCriteria2=getString(R.string.begin_act);
                            strSelectedCriteria2=getString(R.string.begin_act_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.end_act_string))){
                            strCriteria2=getString(R.string.end_act);
                            strSelectedCriteria2=getString(R.string.end_act_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.end_progn_string))){
                            strCriteria2=getString(R.string.end_progn);
                            strSelectedCriteria2=getString(R.string.end_progn_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.entry_date_string))){
                            strCriteria2=getString(R.string.entry_date);
                            strSelectedCriteria2=getString(R.string.entry_date_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.reshow_date_string))){
                            strCriteria2=getString(R.string.reshow_date);
                            strSelectedCriteria2=getString(R.string.reshow_date_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.complete_on_string))){
                            strCriteria2=getString(R.string.complete_on);
                            strSelectedCriteria2=getString(R.string.complete_on_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.last_change_string))){
                            strCriteria2=getString(R.string.last_change);
                            strSelectedCriteria2=getString(R.string.last_change_string);
                        }
                        setEdittextValue(edittextCriteria2,strCriteria2);
                        //strSelectedCriteria2=edittextCriteria2.getText().toString();
                    }
                    else {
                        setEdittextValue(edittextCriteria2,arraylistgetedFromDatabase.get(i).getValue());
                        strSelectedCriteria2=edittextCriteria2.getText().toString();
                    }

                }
                if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_criterion2Type_string))){
                    if(Locale.getDefault().getLanguage().equalsIgnoreCase(getString(R.string.language_germany))){
                        String strCriteriaType2="";
                        if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.before_string))){
                            strCriteriaType2=getString(R.string.before);
                            strSelectedDuration2=getString(R.string.before_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.on_string))){
                            strCriteriaType2=getString(R.string.on);
                            strSelectedDuration2=getString(R.string.on_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.after_string))){
                            strCriteriaType2=getString(R.string.after);
                            strSelectedDuration2=getString(R.string.after_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.between_string))){
                            strCriteriaType2=getString(R.string.between);
                            strSelectedDuration2=getString(R.string.between_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.time_range_string))){
                            strCriteriaType2=getString(R.string.time_range);
                            strSelectedDuration2=getString(R.string.time_range_string);
                        }
                        setEdittextValue(edittextSelectedCriteria2,strCriteriaType2);
                        //strSelectedDuration2 = edittextSelectedCriteria2.getText().toString();
                    }
                    else {
                        setEdittextValue(edittextSelectedCriteria2, arraylistgetedFromDatabase.get(i).getValue());
                        strSelectedDuration2 = edittextSelectedCriteria2.getText().toString();
                    }
                }
                if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_criterion2TimeFrom_string))){
                    setEdittextValue(edittextDate3,arraylistgetedFromDatabase.get(i).getValue());
                    //edittextDate3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar, 0);
                    editableTrue2(edittextDate3,false);
                    setVisibilityCross(2);
                    flagEdittext3=true;
                }
                if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_criterion2TimeTo_string))){
                    setEdittextValue(edittextDate4,arraylistgetedFromDatabase.get(i).getValue());
                    //edittextDate4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar, 0);
                    editableTrue2(edittextDate4,false);
                    flagEdittext4=true;
                    setVisibilityCross(2);
                    if(!TextUtils.isEmpty(getEdittextValue(edittextDate3)) &&
                            !TextUtils.isEmpty(getEdittextValue(edittextDate4))){
                        textviewBetween2.setVisibility(View.VISIBLE);
                        textviewBetween2.setText(getString(R.string.to));
                    }
                }
                if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_criterion2TimeUnit_string))){
                    if(Locale.getDefault().getLanguage().equalsIgnoreCase(getString(R.string.language_germany))){
                        String strTimeUnit2="";
                        if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.days_string))){
                            strTimeUnit2=getString(R.string.days);
                            strSelectedTimePeriod2toSend=getString(R.string.days_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.weeks_string))){
                            strTimeUnit2=getString(R.string.weeks);
                            strSelectedTimePeriod2toSend=getString(R.string.weeks_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.months_string))){
                            strTimeUnit2=getString(R.string.months);
                            strSelectedTimePeriod2toSend=getString(R.string.months_string);
                        }
                        else if(arraylistgetedFromDatabase.get(i).getValue().equalsIgnoreCase(getString(R.string.years_string))){
                            strTimeUnit2=getString(R.string.years);
                            strSelectedTimePeriod2toSend=getString(R.string.years_string);
                        }

                        setEdittextValue(edittextTimePeriod2,strTimeUnit2);
                        //strSelectedTimePeriod2toSend = edittextTimePeriod2.getText().toString();
                    }
                    else {
                        setEdittextValue(edittextTimePeriod2, arraylistgetedFromDatabase.get(i).getValue());
                        strSelectedTimePeriod2toSend = edittextTimePeriod2.getText().toString();
                    }
                }
                if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_criterion2FromDate_string))){
                    Date dateSelected = GlobalClass.convertStringToDate2(arraylistgetedFromDatabase.get(i).getValue());
                    String dateStr = GlobalClass.dateFormateChanged(dateSelected);

                    setEdittextValue(edittextDate3,dateStr);
                    //edittextDate3.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar, 0);
                    editableTrue2(edittextDate3,true);
                    flagEdittext3=false;
                    setVisibilityCalendar(3);
                }
                if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_criterion2ToDate_string))){
                    if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_criterion2FromDate_string))){
                        Date dateSelected = GlobalClass.convertStringToDate2(arraylistgetedFromDatabase.get(i).getValue());
                        String dateStr = GlobalClass.dateFormateChanged(dateSelected);
                        setEdittextValue(edittextDate4,dateStr);
                        // edittextDate4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar, 0);
                        editableTrue2(edittextDate4,true);
                        flagEdittext4=false;
                        setVisibilityCalendar(6);
                        if(!TextUtils.isEmpty(getEdittextValue(edittextDate3)) &&
                                !TextUtils.isEmpty(getEdittextValue(edittextDate4))){
                            textviewBetween2.setVisibility(View.VISIBLE);
                            textviewBetween2.setText(getString(R.string.and));
                        }
                    }
                    else {
                        Date dateSelected = GlobalClass.convertStringToDate2(arraylistgetedFromDatabase.get(i).getValue());
                        String dateStr = GlobalClass.dateFormateChanged(dateSelected);
                        setEdittextValue(edittextDate4,dateStr);
                        // edittextDate4.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.calendar, 0);
                        editableTrue2(edittextDate4,true);
                        flagEdittext4=false;
                        setVisibilityCalendar(4);
                        if(!TextUtils.isEmpty(getEdittextValue(edittextDate3)) &&
                                !TextUtils.isEmpty(getEdittextValue(edittextDate4))){
                            textviewBetween2.setVisibility(View.VISIBLE);
                            textviewBetween2.setText(getString(R.string.and));
                            setVisibilityCalendar(6);
                        }
                    }

                }
            }
            String strFolder = obj1.toString();
        }
        isTimetoChangeText=true;
        setLanguage();
    }
    public void doneButtonClicked(String comeFrom){
        if(comeFrom.equalsIgnoreCase("this")){
            prefrences.saveIsProgressShow("yes");
        }
        else {
            prefrences.saveIsProgressShow("no");
        }
        dbHandler.deleteGeneralFilterData(prefrences.getProfileId(),getString(R.string.DateFilter_text)
                ,prefrences.getLanguage(),prefrences.getUserID());

        //dbHandler.deleteDateFilter(prefrences.getUserID(),prefrences.getProfileId());
        //dbHandler.deleteFilter(prefrences.getUserID(),prefrences.getProfileId(),getString(R.string.DateFilter_text));


        if(!getEdittextValue(edittextDate1).equalsIgnoreCase("") ||
                !getEdittextValue(edittextDate2).equalsIgnoreCase("")){
            prefrences.saveIsProgressShow("yes");

            //dbHandler.addGeneralFilterInfo(getString(R.string.DateFilter_text),"","","",prefrences.getUserID(),prefrences.getProfileId());
            /// storing criteria 1
            if(!TextUtils.isEmpty(getEdittextValue(edittextCriteria1))){
                dbHandler.addGeneralFilter(prefrences.getProfileId(),getString(R.string.key_criterion1_string),strSelectedCriteria1,prefrences.getLanguage(),getString(R.string.DateFilter_text),prefrences.getUserID(),"");
                /*dbHandler.addDateFilter(getString(R.string.key_criterion1_string),strSelectedCriteria1,prefrences.getUserID(),
                        prefrences.getProfileId(),"control");
                dbHandler.addDateFilter(getString(R.string.key_criterion1_string),getEdittextValue(edittextCriteria1),
                        prefrences.getUserID(),prefrences.getProfileId(),"database");*/
            }
            //// stroing criteria type
            if(!TextUtils.isEmpty(getEdittextValue(edittextSelectedCriteria1))){
                dbHandler.addGeneralFilter(prefrences.getProfileId(),getString(R.string.key_criterion1Type_string),strSelectedDuration1,prefrences.getLanguage(),getString(R.string.DateFilter_text),prefrences.getUserID(),"");
                /*dbHandler.addDateFilter(getString(R.string.key_criterion1Type_string),strSelectedDuration1,prefrences.getUserID(),
                        prefrences.getProfileId(),"control");
                dbHandler.addDateFilter(getString(R.string.key_criterion1Type_string),getEdittextValue(edittextSelectedCriteria1),
                        prefrences.getUserID(),prefrences.getProfileId(),"database");*/
            }
            /// checking if time range is selected or date range
            if(strSelectedDuration1.equalsIgnoreCase(getString(R.string.time_range_string))){
                // for time range
                if(!TextUtils.isEmpty(getEdittextValue(edittextDate1))){
                    if(isTextChanged){
                        if(prefrences.getLastFiredFilterAll().equalsIgnoreCase("dashboard")){
                            prefrences.saveCurrentFiredFilter("6");
                        }else {
                            prefrences.saveCurrentFiredFilter("7");
                        }
                    }
                    dbHandler.addGeneralFilter(prefrences.getProfileId(),getString(R.string.key_criterion1TimeFrom),
                            getEdittextValue(edittextDate1),prefrences.getLanguage(),getString(R.string.DateFilter_text),prefrences.getUserID(),"");
                    /*dbHandler.addDateFilter(getString(R.string.key_criterion1TimeFrom),getEdittextValue(edittextDate1),
                            prefrences.getUserID(),prefrences.getProfileId(),"control");
                    dbHandler.addDateFilter(getString(R.string.key_criterion1TimeFrom),getEdittextValue(edittextDate1),
                            prefrences.getUserID(),prefrences.getProfileId(),"database");*/
                }
                if(!TextUtils.isEmpty(getEdittextValue(edittextDate2))){
                    if(isTextChanged){
                        if(prefrences.getLastFiredFilterAll().equalsIgnoreCase("dashboard")){
                            prefrences.saveCurrentFiredFilter("6");
                        }else {
                            prefrences.saveCurrentFiredFilter("7");
                        }
                    }
                    dbHandler.addGeneralFilter(prefrences.getProfileId(),getString(R.string.key_criterion1TimeTo),
                            getEdittextValue(edittextDate2),prefrences.getLanguage(),getString(R.string.DateFilter_text),prefrences.getUserID(),"");

                    /*dbHandler.addDateFilter(getString(R.string.key_criterion1TimeTo),getEdittextValue(edittextDate2),
                            prefrences.getUserID(),prefrences.getProfileId(),"control");
                    dbHandler.addDateFilter(getString(R.string.key_criterion1TimeTo),getEdittextValue(edittextDate2),
                            prefrences.getUserID(),prefrences.getProfileId(),"database");*/
                }
                    /* fot time period */
                if(!TextUtils.isEmpty(getEdittextValue(edittextTimePeriod1))){
                    if(isTextChanged){
                        if(prefrences.getLastFiredFilterAll().equalsIgnoreCase("dashboard")){
                            prefrences.saveCurrentFiredFilter("6");
                        }else {
                            prefrences.saveCurrentFiredFilter("7");
                        }
                    }
                    dbHandler.addGeneralFilter(prefrences.getProfileId(),getString(R.string.key_criterion1TimeUnit_string),
                            strSelectedTimePeriod1toSend,prefrences.getLanguage(),getString(R.string.DateFilter_text),prefrences.getUserID(),"");

                    /*dbHandler.addDateFilter(getString(R.string.key_criterion1TimeUnit_string),strSelectedTimePeriod1toSend,
                            prefrences.getUserID(),prefrences.getProfileId(),"control");
                    dbHandler.addDateFilter(getString(R.string.key_criterion1TimeUnit_string),getEdittextValue(edittextTimePeriod1),
                            prefrences.getUserID(),prefrences.getProfileId(),"database");*/
                }
            }
            else {
                // for date range
                if(!TextUtils.isEmpty(getEdittextValue(edittextDate1))){
                    if(isTextChanged){
                        if(prefrences.getLastFiredFilterAll().equalsIgnoreCase("dashboard")){
                            prefrences.saveCurrentFiredFilter("6");
                        }else {
                            prefrences.saveCurrentFiredFilter("7");
                        }
                    }
                    Date dateSelected = GlobalClass.convertStringToDate(getEdittextValue(edittextDate1));
                    String dateStr = GlobalClass.dateFormateChanged2(dateSelected);

                    dbHandler.addGeneralFilter(prefrences.getProfileId(),getString(R.string.key_criterion1FromDate_string),
                            dateStr,prefrences.getLanguage(),getString(R.string.DateFilter_text),prefrences.getUserID(),"");
                    /*dbHandler.addDateFilter(getString(R.string.key_criterion1FromDate_string),dateStr,
                            prefrences.getUserID(),prefrences.getProfileId(),"control");
                    dbHandler.addDateFilter(getString(R.string.key_criterion1FromDate_string),getEdittextValue(edittextDate1),
                            prefrences.getUserID(),prefrences.getProfileId(),"database");*/
                }
                if(!TextUtils.isEmpty(getEdittextValue(edittextDate2))){
                    if(isTextChanged){
                        if(prefrences.getLastFiredFilterAll().equalsIgnoreCase("dashboard")){
                            prefrences.saveCurrentFiredFilter("6");
                        }else {
                            prefrences.saveCurrentFiredFilter("7");
                        }
                    }
                    Date dateSelected = GlobalClass.convertStringToDate(getEdittextValue(edittextDate2));
                    String dateStr = GlobalClass.dateFormateChanged2(dateSelected);

                    dbHandler.addGeneralFilter(prefrences.getProfileId(),getString(R.string.key_criterion1ToDate_string),
                            dateStr,prefrences.getLanguage(),getString(R.string.DateFilter_text),prefrences.getUserID(),"");

                    /*dbHandler.addDateFilter(getString(R.string.key_criterion1ToDate_string),dateStr,
                            prefrences.getUserID(),prefrences.getProfileId(),"control");
                    dbHandler.addDateFilter(getString(R.string.key_criterion1ToDate_string),getEdittextValue(edittextDate2),
                            prefrences.getUserID(),prefrences.getProfileId(),"database");*/
                }
            }
        }

        if(!getEdittextValue(edittextDate3).equalsIgnoreCase("") ||
                !getEdittextValue(edittextDate4).equalsIgnoreCase("")){
            prefrences.saveIsProgressShow("yes");
            //dbHandler.addGeneralFilterInfo(getString(R.string.DateFilter_text),"","","",prefrences.getUserID(),prefrences.getProfileId());
            /// storing criteria 1
            if(!TextUtils.isEmpty(getEdittextValue(edittextCriteria2))){
                dbHandler.addGeneralFilter(prefrences.getProfileId(),getString(R.string.key_criterion2_string),
                        strSelectedCriteria2,prefrences.getLanguage(),getString(R.string.DateFilter_text),prefrences.getUserID(),"");

                /*dbHandler.addDateFilter(getString(R.string.key_criterion2_string),strSelectedCriteria2,prefrences.getUserID(),
                        prefrences.getProfileId(),"control");
                dbHandler.addDateFilter(getString(R.string.key_criterion2_string),getEdittextValue(edittextCriteria2),
                        prefrences.getUserID(),prefrences.getProfileId(),"database");*/
            }
            //// stroing criteria type
            if(!TextUtils.isEmpty(getEdittextValue(edittextSelectedCriteria2))){
                dbHandler.addGeneralFilter(prefrences.getProfileId(),getString(R.string.key_criterion2Type_string),
                        strSelectedDuration2,prefrences.getLanguage(),getString(R.string.DateFilter_text),prefrences.getUserID(),"");

                /*dbHandler.addDateFilter(getString(R.string.key_criterion2Type_string),strSelectedDuration2,prefrences.getUserID(),
                        prefrences.getProfileId(),"control");
                dbHandler.addDateFilter(getString(R.string.key_criterion2Type_string),getEdittextValue(edittextSelectedCriteria2),
                        prefrences.getUserID(),prefrences.getProfileId(),"database");*/
            }
            /// checking if time range is selected or date range
            if(strSelectedDuration2.equalsIgnoreCase(getString(R.string.time_range_string))){
                // for time range
                if(!TextUtils.isEmpty(getEdittextValue(edittextDate3))){
                    if(isTextChanged){
                        if(prefrences.getLastFiredFilterAll().equalsIgnoreCase("dashboard")){
                            prefrences.saveCurrentFiredFilter("6");
                        }else {
                            prefrences.saveCurrentFiredFilter("7");
                        }
                    }
                    dbHandler.addGeneralFilter(prefrences.getProfileId(),getString(R.string.key_criterion2TimeFrom),
                            getEdittextValue(edittextDate3),prefrences.getLanguage(),getString(R.string.DateFilter_text),prefrences.getUserID(),"");

                    /*dbHandler.addDateFilter(getString(R.string.key_criterion2TimeFrom),getEdittextValue(edittextDate3),
                            prefrences.getUserID(),prefrences.getProfileId(),"control");
                    dbHandler.addDateFilter(getString(R.string.key_criterion2TimeFrom),getEdittextValue(edittextDate3),
                            prefrences.getUserID(),prefrences.getProfileId(),"database");*/
                }
                if(!TextUtils.isEmpty(getEdittextValue(edittextDate4))){
                    if(isTextChanged){
                        prefrences.saveCurrentFiredFilter("7");
                    }
                    dbHandler.addGeneralFilter(prefrences.getProfileId(),getString(R.string.key_criterion2TimeTo),
                            getEdittextValue(edittextDate4),prefrences.getLanguage(),getString(R.string.DateFilter_text),prefrences.getUserID(),"");
                    /*dbHandler.addDateFilter(getString(R.string.key_criterion2TimeTo),getEdittextValue(edittextDate4),
                            prefrences.getUserID(),prefrences.getProfileId(),"control");
                    dbHandler.addDateFilter(getString(R.string.key_criterion2TimeTo),getEdittextValue(edittextDate4),
                            prefrences.getUserID(),prefrences.getProfileId(),"database");*/
                }
                if(!TextUtils.isEmpty(getEdittextValue(edittextTimePeriod2))){
                    if(isTextChanged){
                        if(prefrences.getLastFiredFilterAll().equalsIgnoreCase("dashboard")){
                            prefrences.saveCurrentFiredFilter("6");
                        }else {
                            prefrences.saveCurrentFiredFilter("7");
                        }
                    }
                    dbHandler.addGeneralFilter(prefrences.getProfileId(),getString(R.string.key_criterion2TimeUnit_string),
                            strSelectedTimePeriod2toSend,prefrences.getLanguage(),getString(R.string.DateFilter_text),prefrences.getUserID(),"");

                    /*dbHandler.addDateFilter(getString(R.string.key_criterion2TimeUnit_string),strSelectedTimePeriod2toSend,
                            prefrences.getUserID(),prefrences.getProfileId(),"control");
                    dbHandler.addDateFilter(getString(R.string.key_criterion2TimeUnit_string),getEdittextValue(edittextTimePeriod2),
                            prefrences.getUserID(),prefrences.getProfileId(),"database");*/
                }
            }
            else {
                // for date range
                if(!TextUtils.isEmpty(getEdittextValue(edittextDate3))){
                    if(isTextChanged){
                        if(prefrences.getLastFiredFilterAll().equalsIgnoreCase("dashboard")){
                            prefrences.saveCurrentFiredFilter("6");
                        }else {
                            prefrences.saveCurrentFiredFilter("7");
                        }
                    }
                    Date dateSelected = GlobalClass.convertStringToDate(getEdittextValue(edittextDate3));
                    String dateStr = GlobalClass.dateFormateChanged2(dateSelected);

                    dbHandler.addGeneralFilter(prefrences.getProfileId(),getString(R.string.key_criterion2FromDate_string),
                            dateStr,prefrences.getLanguage(),getString(R.string.DateFilter_text),prefrences.getUserID(),"");

                    /*dbHandler.addDateFilter(getString(R.string.key_criterion2FromDate_string),dateStr,
                            prefrences.getUserID(),prefrences.getProfileId(),"control");
                    dbHandler.addDateFilter(getString(R.string.key_criterion2FromDate_string),getEdittextValue(edittextDate3),
                            prefrences.getUserID(),prefrences.getProfileId(),"database");*/
                }
                if(!TextUtils.isEmpty(getEdittextValue(edittextDate4))){
                    if(isTextChanged){
                        if(prefrences.getLastFiredFilterAll().equalsIgnoreCase("dashboard")){
                            prefrences.saveCurrentFiredFilter("6");
                        }else {
                            prefrences.saveCurrentFiredFilter("7");
                        }
                    }
                    Date dateSelected = GlobalClass.convertStringToDate(getEdittextValue(edittextDate4));
                    String dateStr = GlobalClass.dateFormateChanged2(dateSelected);

                    dbHandler.addGeneralFilter(prefrences.getProfileId(),getString(R.string.key_criterion2ToDate_string),
                            dateStr,prefrences.getLanguage(),getString(R.string.DateFilter_text),prefrences.getUserID(),"");

                    /*dbHandler.addDateFilter(getString(R.string.key_criterion2ToDate_string),dateStr,
                            prefrences.getUserID(),prefrences.getProfileId(),"control");
                    dbHandler.addDateFilter(getString(R.string.key_criterion2ToDate_string),getEdittextValue(edittextDate4),
                            prefrences.getUserID(),prefrences.getProfileId(),"database");*/
                }
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.textview_done:
                comefrom="this";
                doneButtonClicked(comefrom);
                break;
            case R.id.edittext_criteria1:
                showDialogToselectCriteria("criteria1");
                break;
            case R.id.edittext_criteria2:
                showDialogToselectCriteria("criteria2");
                break;
            case R.id.edittext_selected_criteria1:
                showDialogDuration("criteria1");
                break;
            case R.id.edittext_selected_criteria2:
                showDialogDuration("criteria2");
                break;
            case R.id.textview_date1:
                if(flagEdittext1){
                /*if(popupWindowTimeRange.isShowing()){
                    popupWindowTimeRange.dismiss();
                }
                else {*/
                    showPopupWindow(1);
                    //}

                }else {
                    if(isClickableEdittext){
                        showDatePickerDialog(1);
                    }
                }
                break;
            case R.id.textview_date2:
                if(flagEdittext2){
                /*if(popupWindowTimeRange.isShowing()){
                    popupWindowTimeRange.dismiss();
                }
                else {*/
                    showPopupWindow(2);
                    //}

                }
                else {
                    if(isClickableEdittext){
                        showDatePickerDialog(2);
                    }
                }
                break;
            case R.id.textview_date3:
                if(flagEdittext3){
                /*if(popupWindowTimeRange.isShowing()){
                    popupWindowTimeRange.dismiss();
                }
                else {*/
                    showPopupWindow(3);
                    //}

                }
                else {
                    if(isClickableEdittext2){
                        showDatePickerDialog(3);
                    }
                }
                break;
            case R.id.textview_date4:
                if(flagEdittext4){
                /*if(popupWindowTimeRange.isShowing()){
                    popupWindowTimeRange.dismiss();
                }
                else {*/
                    showPopupWindow(4);
                    //}

                }
                else {
                    if(isClickableEdittext2){
                        showDatePickerDialog(4);
                    }
                }
                break;
            case R.id.textview_day_unit1:
                showDialogTimePeriod("criteria1");
                break;
            case R.id.textview_day_unit2:
                showDialogTimePeriod("criteria2");
                break;
            case R.id.textview_generalfilter:
                comefrom="this";
                doneButtonClicked(comefrom);
                /*InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm.isAcceptingText()) {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }*/

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentSelectFilter fragment = new FragmentSelectFilter();
                transaction.replace(R.id.select_filter_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            case R.id.linearback:
                comefrom="this";
                doneButtonClicked(comefrom);
                /*InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm.isAcceptingText()) {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }*/

                FragmentTransaction transaction2 = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentSelectFilter fragment2 = new FragmentSelectFilter();
                transaction2.replace(R.id.select_filter_container, fragment2);
                transaction2.addToBackStack(null);
                transaction2.commit();
                break;
            case R.id.img_delete:
                dbHandler.deleteGeneralFilterData(prefrences.getProfileId(),getString(R.string.DateFilter_text)
                        ,prefrences.getLanguage(),prefrences.getUserID());

                dbHandler.deleteDateFilter(prefrences.getUserID(),prefrences.getProfileId());
                //dbHandler.updateGeneralFilterToDelete(prefrences.getProfileId(),getString(R.string.DateFilter_text),"",prefrences.getLanguage(),prefrences.getUserID());
                visibleCriteria1(false);
                visibleCriteria2(false);
                edittextCriteria1.setText(getString(R.string.empty));
                edittextCriteria2.setText(getString(R.string.empty));
                //if(dbHandler.getCursorCountforDateFilter() > 0){
                    if(prefrences.getLastFiredFilterAll().equalsIgnoreCase("dashboard")){
                        prefrences.saveCurrentFiredFilter("6");
                    }else {
                        prefrences.saveCurrentFiredFilter("7");
                    }
                    dbHandler.deleteDateFilter(prefrences.getUserID(),prefrences.getProfileId());
                    //dbHandler.deleteFilter(prefrences.getUserID(),prefrences.getProfileId(),getString(R.string.DateFilter_text));

                    edittextCriteria1.setText("");
                    edittextCriteria2.setText("");
                    edittextTimePeriod1.setText("");
                    edittextTimePeriod2.setText("");
                    edittextSelectedCriteria1.setText("");
                    edittextSelectedCriteria2.setText("");
                    edittextDate1.setText("");
                    edittextDate2.setText("");
                    edittextDate3.setText("");
                    edittextDate4.setText("");
                    imgCalendar1.setVisibility(View.GONE);
                    imgCalendar2.setVisibility(View.GONE);
                    imgCalendar3.setVisibility(View.GONE);
                    imgCalendar4.setVisibility(View.GONE);
                    imgCross1.setVisibility(View.GONE);
                    imgCross2.setVisibility(View.GONE);
                    imgCross3.setVisibility(View.GONE);
                    imgCross4.setVisibility(View.GONE);

                    initControl();
                //}
                break;
            case R.id.imgcross1:
                edittextDate1.setText("");
                break;
            case R.id.imgcross2:
                edittextDate2.setText("");
                break;
            case R.id.imgcross3:
                edittextDate3.setText("");
                break;
            case R.id.imgcross4:
                edittextDate4.setText("");
                break;
        }
    }
//    set edittext value dynamically
    public void setEdittextValue(EditText edittext,String value){
       edittext.setVisibility(View.VISIBLE);
       edittext.setText(value);
    }
//    show hide layout based on condition
    public void visibleCriteria1(boolean flag)
    {
        if(flag){
            textviewBetween1.setVisibility(View.VISIBLE);
            edittextDate1.setVisibility(View.VISIBLE);
            edittextDate2.setVisibility(View.VISIBLE);
            edittextTimePeriod1.setVisibility(View.VISIBLE);
            edittextSelectedCriteria1.setVisibility(View.VISIBLE);

        }
        else {
            textviewBetween1.setVisibility(View.GONE);
            edittextDate1.setVisibility(View.GONE);
            edittextDate2.setVisibility(View.GONE);
            edittextTimePeriod1.setVisibility(View.GONE);
            edittextSelectedCriteria1.setVisibility(View.GONE);
            imgCross1.setVisibility(View.GONE);
            imgCross2.setVisibility(View.GONE);
            imgCalendar1.setVisibility(View.GONE);
            imgCalendar2.setVisibility(View.GONE);
        }
    }
    //    show hide layout based on condition
    public void visibleCriteria2(boolean flag)
    {
        if(flag){
            textviewBetween2.setVisibility(View.VISIBLE);
            edittextDate3.setVisibility(View.VISIBLE);
            edittextDate4.setVisibility(View.VISIBLE);
            edittextTimePeriod2.setVisibility(View.VISIBLE);
            edittextSelectedCriteria2.setVisibility(View.VISIBLE);
        }
        else {
            textviewBetween2.setVisibility(View.GONE);
            edittextDate3.setVisibility(View.GONE);
            edittextDate4.setVisibility(View.GONE);
            edittextTimePeriod2.setVisibility(View.GONE);
            edittextSelectedCriteria2.setVisibility(View.GONE);
            imgCross3.setVisibility(View.GONE);
            imgCross4.setVisibility(View.GONE);
            imgCalendar3.setVisibility(View.GONE);
            imgCalendar4.setVisibility(View.GONE);
        }
    }
//    clearing criteria of above
    public void clearCriteria1()
    {
        edittextDate1.setText("");
        edittextDate2.setText("");
        edittextTimePeriod1.setText("");
        textviewBetween1.setText("");
        //edittextSelectedCriteria1.setText("");

        edittextDate1.setVisibility(View.GONE);
        edittextDate2.setVisibility(View.GONE);
        edittextTimePeriod1.setVisibility(View.GONE);
        textviewBetween1.setVisibility(View.GONE);

        imgCalendar1.setVisibility(View.GONE);
        imgCalendar2.setVisibility(View.GONE);
        imgCross1.setVisibility(View.GONE);
        imgCross2.setVisibility(View.GONE);

    }
//    clearing criteria of below
    public void clearCriteria2()
    {
        edittextDate3.setText("");
        edittextDate4.setText("");
        edittextTimePeriod2.setText("");
        textviewBetween2.setText("");
        //edittextSelectedCriteria2.setText("");

        edittextDate3.setVisibility(View.GONE);
        edittextDate4.setVisibility(View.GONE);
        edittextTimePeriod2.setVisibility(View.GONE);
        textviewBetween2.setVisibility(View.GONE);

        imgCalendar3.setVisibility(View.GONE);
        imgCalendar4.setVisibility(View.GONE);
        imgCross3.setVisibility(View.GONE);
        imgCross4.setVisibility(View.GONE);
    }
    public void showDialogToselectCriteria(final String criteria){
        // Create custom dialog object
        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_select_criteria);

        // Set dialog title
        dialog.setTitle("");
        dialog.show();

        final ListView listviewCriteria=(ListView)dialog.findViewById(R.id.listviewcriteria);
        listviewCriteria.setSelector(R.drawable.list_selector);

        DatesFilterScreenAdapter adapter = new DatesFilterScreenAdapter(getActivity(),arraylistCriteria);
        listviewCriteria.setAdapter(adapter);

        listviewCriteria.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id)
            {
                if(isTimetoChangeText)
                {
                    isTextChanged=true;
                    //doneButtonClicked(comefrom);
                    //GlobalClass.showToast(getActivity(), " item selected : ");
                }
                if(criteria.equalsIgnoreCase("criteria1")){
                    strSelectedCriteria1=arraylistCriteriaComparision.get(position);

                    if(!TextUtils.isEmpty(strSelectedCriteria1)){
                        if(!strSelectedCriteria1.equalsIgnoreCase("Empty")){
                            edittextSelectedCriteria1.setVisibility(View.VISIBLE);
                            edittextCriteria1.setText(arraylistCriteria.get(position));
                            edittextSelectedCriteria1.setText(arraylistDuration.get(0));
                            strSelectedDuration1=arraylistDurationComparision.get(0);

                            clearCriteria1();

                            flagEdittext1=false;
                            flagEdittext2=false;
                            showUiforOthers(1);
                            setVisibilityCalendar(1);


                        }
                        else {
                            edittextSelectedCriteria1.setVisibility(View.GONE);
                            edittextCriteria1.setText(arraylistCriteria.get(position));
                            clearCriteria1();

                        }
                    }
                }
                else if(criteria.equalsIgnoreCase("criteria2"))
                {
                    strSelectedCriteria2=arraylistCriteriaComparision.get(position);
                    if(!TextUtils.isEmpty(strSelectedCriteria2)){
                        if(!strSelectedCriteria2.equalsIgnoreCase("Empty")){
                            edittextCriteria2.setText(arraylistCriteria.get(position));
                            edittextSelectedCriteria2.setVisibility(View.VISIBLE);
                            edittextSelectedCriteria2.setText(arraylistDuration.get(0));
                            strSelectedDuration2=arraylistDurationComparision.get(0);
                            clearCriteria2();

                            flagEdittext3=false;
                            flagEdittext4=false;
                            showUiforOthers(2);
                            setVisibilityCalendar(3);

                        }
                        else {
                            edittextSelectedCriteria2.setVisibility(View.GONE);
                            edittextCriteria2.setText(arraylistCriteria.get(position));
                            clearCriteria2();
                        }
                    }
                }
                if(dialog != null) {
                    dialog.dismiss();
                }
            }
        });

    }
//    this method show duration dialog
    public void showDialogDuration(final String criteria){
        // Create custom dialog object
        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_select_criteria);

        // Set dialog title
        dialog.setTitle("");
        dialog.show();

        final ListView listviewDuration=(ListView)dialog.findViewById(R.id.listviewcriteria);
        listviewDuration.setSelector(R.drawable.list_selector);

        DatesFilterScreenAdapter adapter = new DatesFilterScreenAdapter(getActivity(),arraylistDuration);
        listviewDuration.setAdapter(adapter);

        listviewDuration.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id)
            {

                if(isTimetoChangeText)
                {
                    isTextChanged=true;
                    //doneButtonClicked(comefrom);
                    //GlobalClass.showToast(getActivity(), " item selected : ");
                }
                if(criteria.equalsIgnoreCase("criteria1")){
                    strSelectedDuration1=arraylistDurationComparision.get(position);

                    if(!TextUtils.isEmpty(strSelectedDuration1)){
                        edittextSelectedCriteria1.setText(arraylistDuration.get(position));
                        edittextDate1.setText("");
                        edittextDate2.setText("");
                        edittextTimePeriod1.setText("");
                        textviewBetween1.setText("");
                        if(strSelectedDuration1.equalsIgnoreCase("Time range")){
                            flagEdittext1=true;
                            flagEdittext2=true;
                            showUiforRange(1);
                            edittextTimePeriod1.setText(arraylistTimePeriod.get(0));
                            strSelectedTimePeriod1toSend=edittextTimePeriod1.getText().toString();
                            setVisibilityCross(1);
                        }
                        else if(strSelectedDuration1.equalsIgnoreCase("Between")){
                            flagEdittext1=false;
                            flagEdittext2=false;
                            showUiforBetween(1);
                            setVisibilityCalendar(5);
                        }
                        else if(strSelectedDuration1.equalsIgnoreCase("On") || strSelectedDuration1.equalsIgnoreCase("After") ||
                                strSelectedDuration1.equalsIgnoreCase("Before")){
                            flagEdittext1=false;
                            flagEdittext2=false;
                           showUiforOthers(1);
                            setVisibilityCalendar(1);
                        }



                    }
                }
                else if(criteria.equalsIgnoreCase("criteria2"))
                {
                    strSelectedDuration2=arraylistDurationComparision.get(position);
                    if(!TextUtils.isEmpty(strSelectedDuration2)){
                        edittextSelectedCriteria2.setText(arraylistDuration.get(position));

                        edittextDate3.setText("");
                        edittextDate4.setText("");
                        edittextTimePeriod2.setText("");
                        textviewBetween2.setText("");

                        if(strSelectedDuration2.equalsIgnoreCase("Time range")){
                            flagEdittext3=true;
                            flagEdittext4=true;
                            showUiforRange(2);
                            edittextTimePeriod2.setText(arraylistTimePeriod.get(0));
                            strSelectedTimePeriod2toSend=edittextTimePeriod2.getText().toString();
                            setVisibilityCross(2);
                        }
                        else if(strSelectedDuration2.equalsIgnoreCase("Between")){
                            flagEdittext3=false;
                            flagEdittext4=false;
                            showUiforBetween(2);
                            setVisibilityCalendar(6);
                        }

                        else if(strSelectedDuration2.equalsIgnoreCase("On") || strSelectedDuration2.equalsIgnoreCase("After") ||
                                strSelectedDuration2.equalsIgnoreCase("Before")){
                            flagEdittext3=false;
                            flagEdittext4=false;
                            showUiforOthers(2);
                            setVisibilityCalendar(3);
                        }
                    }
                }
                if(dialog != null) {
                    dialog.dismiss();
                }
            }
        });

    }
//    this method shows time period dialog
    public void showDialogTimePeriod(final String criteria)
    {
        // Create custom dialog object
        final Dialog dialog = new Dialog(getActivity());
        // Include dialog.xml file
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_select_criteria);

        // Set dialog title
        dialog.setTitle("");
        dialog.show();

        final ListView listviewTimePeriod=(ListView)dialog.findViewById(R.id.listviewcriteria);
        listviewTimePeriod.setSelector(R.drawable.list_selector);

        DatesFilterScreenAdapter adapter = new DatesFilterScreenAdapter(getActivity(),arraylistTimePeriod);
        listviewTimePeriod.setAdapter(adapter);

        listviewTimePeriod.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id)
            {

                if(isTimetoChangeText)
                {
                    isTextChanged=true;
                    doneButtonClicked(comefrom);
                    //GlobalClass.showToast(getActivity(), " item selected : ");
                }
                if(criteria.equalsIgnoreCase("criteria1")){
                    strSelectedTimePeriod1=arraylistTimePeriod.get(position);
                    strSelectedTimePeriod1toSend=arraylistTimePeriodComparision.get(position);

                    if(!TextUtils.isEmpty(strSelectedTimePeriod1)){
                        edittextTimePeriod1.setText(strSelectedTimePeriod1);

                    }
                }
                else if(criteria.equalsIgnoreCase("criteria2"))
                {
                    strSelectedTimePeriod2=arraylistTimePeriod.get(position);
                    strSelectedTimePeriod2toSend=arraylistTimePeriodComparision.get(position);
                    if(!TextUtils.isEmpty(strSelectedTimePeriod2)){
                        edittextTimePeriod2.setText(strSelectedTimePeriod2);

                    }
                }
                if(dialog != null) {
                    dialog.dismiss();
                }
            }
        });

    }
//    this method shows default date picker dialog
    public void showDatePickerDialog(final int pos)
    {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        String selectedDate = GlobalClass.pad(dayOfMonth)+"."+ GlobalClass.pad((monthOfYear+1))+"."+year;
                        try
                        {
                            if(pos==1){
                                selectedDate1= GlobalClass.dateFormater.parse(selectedDate);
                                strDate1=selectedDate;
                                edittextDate1.setText(strDate1);
                                doneButtonClicked(comefrom);
                            }
                            if(pos==2){
                                selectedDate2= GlobalClass.dateFormater.parse(selectedDate);
                                strDate2=selectedDate;
                                edittextDate2.setText(strDate2);
                                doneButtonClicked(comefrom);
                            }
                            if(pos==3){
                                selectedDate3= GlobalClass.dateFormater.parse(selectedDate);
                                strDate3=selectedDate;
                                edittextDate3.setText(strDate3);
                                doneButtonClicked(comefrom);
                            }

                            if(pos==4){
                                selectedDate4= GlobalClass.dateFormater.parse(selectedDate);
                                strDate4=selectedDate;
                                edittextDate4.setText(strDate4);
                                doneButtonClicked(comefrom);
                            }

                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
//    fiiling static arraylist to show in listview
    public void fillArraylists(){
        arraylistCriteria = new ArrayList<>();

        arraylistCriteria.add(getString(R.string.empty));
        arraylistCriteria.add(getString(R.string.planned_time_to_be_spent_on_task));
        arraylistCriteria.add(getString(R.string.begin_pid));
        arraylistCriteria.add(getString(R.string.end_pid));
        arraylistCriteria.add(getString(R.string.actual_time_to_be_spent_on_task));
        arraylistCriteria.add(getString(R.string.begin_act));
        arraylistCriteria.add(getString(R.string.end_act));
        arraylistCriteria.add(getString(R.string.end_progn));
        arraylistCriteria.add(getString(R.string.entry_date));
        arraylistCriteria.add(getString(R.string.reshow_date));
        arraylistCriteria.add(getString(R.string.complete_on));
        arraylistCriteria.add(getString(R.string.last_change));

        arraylistCriteriaComparision = new ArrayList<>();
        arraylistCriteriaComparision.add(getString(R.string.empty_string));
        arraylistCriteriaComparision.add(getString(R.string.planned_time_to_be_spent_on_task_string));
        arraylistCriteriaComparision.add(getString(R.string.begin_pid_string));
        arraylistCriteriaComparision.add(getString(R.string.end_pid_string));
        arraylistCriteriaComparision.add(getString(R.string.actual_time_to_be_spent_on_task_string));
        arraylistCriteriaComparision.add(getString(R.string.begin_act_string));
        arraylistCriteriaComparision.add(getString(R.string.end_act_string));
        arraylistCriteriaComparision.add(getString(R.string.end_progn_string));
        arraylistCriteriaComparision.add(getString(R.string.entry_date_string));
        arraylistCriteriaComparision.add(getString(R.string.reshow_date_string));
        arraylistCriteriaComparision.add(getString(R.string.complete_on_string));
        arraylistCriteriaComparision.add(getString(R.string.last_change_string));

        arraylistDuration = new ArrayList<>();
        arraylistDuration.add(getString(R.string.before));
        arraylistDuration.add(getString(R.string.on));
        arraylistDuration.add(getString(R.string.after));
        arraylistDuration.add(getString(R.string.between));
        arraylistDuration.add(getString(R.string.time_range));

        arraylistDurationComparision = new ArrayList<>();
        arraylistDurationComparision.add(getString(R.string.before_string));
        arraylistDurationComparision.add(getString(R.string.on_string));
        arraylistDurationComparision.add(getString(R.string.after_string));
        arraylistDurationComparision.add(getString(R.string.between_string));
        arraylistDurationComparision.add(getString(R.string.time_range_string));

        arraylistTimePeriod = new ArrayList<>();
        arraylistTimePeriod.add(getString(R.string.days));
        arraylistTimePeriod.add(getString(R.string.weeks));
        arraylistTimePeriod.add(getString(R.string.months));
        arraylistTimePeriod.add(getString(R.string.years));

        arraylistTimePeriodComparision = new ArrayList<>();
        arraylistTimePeriodComparision.add(getString(R.string.days_string));
        arraylistTimePeriodComparision.add(getString(R.string.weeks_string));
        arraylistTimePeriodComparision.add(getString(R.string.months_string));
        arraylistTimePeriodComparision.add(getString(R.string.years_string));

        String[] array = {"-14","-13","-12","-11","-10","-9","-8","-7","-6","-5","-4","-3",
                "-2","-1","0","1","2","3","4","5","6","7","8",
                "9","10","11","12","13","14"};
        arraylistTimeRange = new ArrayList<>();
        for (int i=0;i<array.length;i++)
        {
            arraylistTimeRange.add(array[i]);
        }
    }
//    do edittext editable false
    public void editableFalse(EditText edittext,int pos)
    {
        edittext.setFocusable(false);
        edittext.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        edittext.setClickable(false);
        if(pos==1){
            isClickableEdittext=true;
        }
        else if(pos==2){
            isClickableEdittext2=true;
        }
    }
//    do edittext editable true
    public void editableTrue2(EditText edittext,boolean flag){
        if(flag){
            edittext.setFocusable(false);
            edittext.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            edittext.setClickable(false);
            edittext.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0 , 0);
        }
        else {
            edittext.setFocusable(false);
            edittext.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
            edittext.setClickable(false);
            edittext.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0 , 0);
        }
    }
    //    do edittext editable true
    public void editableTrue(EditText edittext,int pos){
        edittext.setFocusable(false);
        edittext.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        edittext.setClickable(false);
        if(pos==1){
            isClickableEdittext=false;
        }
        else if(pos==2){
            isClickableEdittext2=false;
        }
    }
//    showing UI dynamically
    public void showUiforRange(int pos){
        if(pos==1){
            edittextTimePeriod1.setVisibility(View.VISIBLE);
            textviewBetween1.setVisibility(View.VISIBLE);
            textviewBetween1.setText(getString(R.string.to));
            edittextDate1.setVisibility(View.VISIBLE);
            edittextDate2.setVisibility(View.VISIBLE);
            editableTrue(edittextDate1,pos);
            editableTrue(edittextDate2,pos);
            edittextDate1.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            edittextDate2.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            edittextDate1.setText("");
            edittextDate2.setText("");
        }
        else {
            edittextTimePeriod2.setVisibility(View.VISIBLE);
            textviewBetween2.setVisibility(View.VISIBLE);
            textviewBetween2.setText(getString(R.string.to));
            edittextDate4.setVisibility(View.VISIBLE);
            edittextDate3.setVisibility(View.VISIBLE);
            editableTrue(edittextDate3,pos);
            editableTrue(edittextDate4,pos);
            edittextDate4.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            edittextDate3.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            edittextDate3.setText("");
            edittextDate4.setText("");
        }

    }
    public void showUiforBetween(int pos){
        if(pos==1){
            textviewBetween1.setVisibility(View.VISIBLE);
            textviewBetween1.setText(getString(R.string.and));
            edittextDate1.setVisibility(View.VISIBLE);
            edittextDate2.setVisibility(View.VISIBLE);
            edittextTimePeriod1.setVisibility(View.GONE);
            editableFalse(edittextDate1,pos);
            editableFalse(edittextDate2,pos);
            edittextDate1.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            edittextDate2.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            edittextDate1.setText("");
            edittextDate2.setText("");
        }
        else {
            textviewBetween2.setVisibility(View.VISIBLE);
            textviewBetween2.setText(getString(R.string.and));
            edittextDate4.setVisibility(View.VISIBLE);
            edittextDate3.setVisibility(View.VISIBLE);
            edittextTimePeriod2.setVisibility(View.GONE);
            editableFalse(edittextDate3,pos);
            editableFalse(edittextDate4,pos);
            edittextDate3.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            edittextDate4.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            edittextDate3.setText("");
            edittextDate4.setText("");
        }
    }
    public void showUiforOthers(int pos){
        if(pos==1){
            edittextTimePeriod1.setVisibility(View.GONE);
            textviewBetween1.setVisibility(View.GONE);
            edittextDate1.setVisibility(View.VISIBLE);
            edittextDate2.setVisibility(View.GONE);
            editableFalse(edittextDate1,pos);
            editableFalse(edittextDate2,pos);
            edittextDate1.setCompoundDrawablesWithIntrinsicBounds(0,0,0, 0);
            edittextDate1.setText("");


        }
        else {
            edittextTimePeriod2.setVisibility(View.GONE);
            textviewBetween2.setVisibility(View.GONE);
            edittextDate4.setVisibility(View.GONE);
            edittextDate3.setVisibility(View.VISIBLE);
            editableFalse(edittextDate3,pos);
            editableFalse(edittextDate4,pos);
            edittextDate3.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            edittextDate3.setText("");
        }
    }
//    getting edittext value dynamically
    public String getEdittextValue(EditText edittext)
    {
        String value="";
        if(edittext.getText().toString().equalsIgnoreCase("")){
            value="";
        }
        else {
            value=edittext.getText().toString();
        }
        return value;
    }
    @Override
    public void callSelectFilterFragment() {
    }
    @Override
    public void callUpdateFilter()
    {
        initControl();
    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        context = activity;
        ((HomeActivity)context).interfaceforSelectFilter = this;
        //((HomeActivity)context).interfaceChangeLanguageDateFilter = this;
        ((HomeActivity)context).interfaceProjectClicked = this;
    }
//    edittext changed event for passed edittext
    public void editTextTextChange(EditText edittext){
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isTimetoChangeText){
                    isTextChanged=true;
                    //GlobalClass.showToast(getActivity()," text Changed : ");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    public void setVisibilityCross(int pos){
        if(pos ==1 ){
            imgCalendar1.setVisibility(View.GONE);
            imgCalendar2.setVisibility(View.GONE);
            imgCross1.setVisibility(View.VISIBLE);
            imgCross2.setVisibility(View.VISIBLE);
        }
        else {
            imgCalendar3.setVisibility(View.GONE);
            imgCalendar4.setVisibility(View.GONE);
            imgCross3.setVisibility(View.VISIBLE);
            imgCross4.setVisibility(View.VISIBLE);
        }
    }
    public void setVisibilityCalendar(int pos){
        if(pos ==1 ){
            imgCalendar1.setVisibility(View.VISIBLE);
            imgCalendar2.setVisibility(View.GONE);
            imgCross1.setVisibility(View.GONE);
            imgCross2.setVisibility(View.GONE);
        }
        else if(pos ==2 ){
            imgCalendar1.setVisibility(View.VISIBLE);
            imgCalendar2.setVisibility(View.VISIBLE);
            imgCross1.setVisibility(View.GONE);
            imgCross2.setVisibility(View.GONE);
        }
        else if(pos ==3 ){
            imgCalendar3.setVisibility(View.VISIBLE);
            imgCalendar4.setVisibility(View.GONE);
            imgCross3.setVisibility(View.GONE);
            imgCross4.setVisibility(View.GONE);
        }
        else if(pos ==4 ){
            imgCalendar3.setVisibility(View.GONE);
            imgCalendar4.setVisibility(View.VISIBLE);
            imgCross1.setVisibility(View.GONE);
            imgCross2.setVisibility(View.GONE);
        }
        else if(pos ==5 ){
            imgCalendar1.setVisibility(View.VISIBLE);
            imgCalendar2.setVisibility(View.VISIBLE);
            imgCross1.setVisibility(View.GONE);
            imgCross2.setVisibility(View.GONE);
        }
        else if(pos ==6 ){
            imgCalendar3.setVisibility(View.VISIBLE);
            imgCalendar4.setVisibility(View.VISIBLE);
            imgCross3.setVisibility(View.GONE);
            imgCross4.setVisibility(View.GONE);
        }
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void showPopupWindow(final int popupNo)
    {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            final View popupView = layoutInflater.inflate(R.layout.popupview_timerange, null);
            popupWindowTimeRange = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindowTimeRange.setWidth(edittextDate1.getWidth());
            popupWindowTimeRange.setHeight((GlobalClass.screenHeight(getContext()) * 35) / 100);
            popupWindowTimeRange.setOutsideTouchable(true);
            if(popupNo == 1){
                popupWindowTimeRange.showAsDropDown(edittextDate1,0, 0, Gravity.CENTER | Gravity.BOTTOM);
            }
            if(popupNo == 2){
                popupWindowTimeRange.showAsDropDown(edittextDate2,0, 0, Gravity.CENTER | Gravity.BOTTOM);
            }
            if(popupNo == 3){
                popupWindowTimeRange.showAsDropDown(edittextDate3,0, 0, Gravity.CENTER | Gravity.BOTTOM);
            }
            if(popupNo == 4){
                popupWindowTimeRange.showAsDropDown(edittextDate4,0, 0, Gravity.CENTER | Gravity.BOTTOM);
            }
            ListView listviewTimeRange = (ListView) popupView.findViewById(R.id.listview_timerange);

            spinnerAdapter = new SpinnerAdapter(getActivity(),arraylistTimeRange);
            listviewTimeRange.setAdapter(spinnerAdapter);

            listviewTimeRange.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    if(popupNo == 1){
                        edittextDate1.setText(arraylistTimeRange.get(position));
                        if(popupWindowTimeRange != null) {
                            popupWindowTimeRange.dismiss();
                        }
                    }
                    if(popupNo == 2){
                        popupWindowTimeRange.showAsDropDown(edittextDate2,0, 0, Gravity.CENTER | Gravity.BOTTOM);
                        edittextDate2.setText(arraylistTimeRange.get(position));
                        if(popupWindowTimeRange != null) {
                            popupWindowTimeRange.dismiss();
                        }
                    }
                    if(popupNo == 3){
                        popupWindowTimeRange.showAsDropDown(edittextDate3,0, 0, Gravity.CENTER | Gravity.BOTTOM);
                        edittextDate3.setText(arraylistTimeRange.get(position));
                        if(popupWindowTimeRange != null) {
                            popupWindowTimeRange.dismiss();
                        }
                    }
                    if(popupNo == 4){
                        popupWindowTimeRange.showAsDropDown(edittextDate4,0, 0, Gravity.CENTER | Gravity.BOTTOM);
                        edittextDate4.setText(arraylistTimeRange.get(position));
                        if(popupWindowTimeRange != null) {
                            popupWindowTimeRange.dismiss();
                        }
                    }
                }
            });
        }
    public void setLanguage(){
        textViewTitle.setText(getString(R.string.date_filter_title));
        textViewCriteria1.setText(getString(R.string.criteria1));
        textViewCriteria2.setText(getString(R.string.criteria2));
        textviewBetween1.setText(getString(R.string.to));
        textviewBetween2.setText(getString(R.string.to));
        if(isTablet){
            textviewGeneralFilter.setText(getString(R.string.general_filte_title));
        }
        else {
            textviewGeneralFilter.setText(getString(R.string.back));
        }
        //fillArraylists();
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
    @Override
    public void clickedProject() {
        doneButtonClicked("this");
    }
}
