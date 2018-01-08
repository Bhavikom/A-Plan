package de.smacsoftwares.aplanapp.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.ClientResponsibleModel.ClientDataSetClass;
import de.smacsoftwares.aplanapp.ClientResponsibleModel.GroupDatasetClass;
import de.smacsoftwares.aplanapp.ClientResponsibleModel.ResourceDataSetClass;
import de.smacsoftwares.aplanapp.ClientResponsibleModel.ResponsibleDataSetClass;
import de.smacsoftwares.aplanapp.Model.GeneralFilterDataSet;
import de.smacsoftwares.aplanapp.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import de.smacsoftwares.aplanapp.activity.HomeActivity;
import de.smacsoftwares.aplanapp.adapter.ClientSpinnerAdapter;
import de.smacsoftwares.aplanapp.adapter.GroupAdapter;
import de.smacsoftwares.aplanapp.adapter.ResourceAdapter;
import de.smacsoftwares.aplanapp.adapter.ResponsibleSpinnerAdapter;
import de.smacsoftwares.aplanapp.adapter.SpinnerAdapter;
import de.smacsoftwares.aplanapp.util.CustomTypefaceSpan;
import de.smacsoftwares.aplanapp.util.DatabaseHandler;
import de.smacsoftwares.aplanapp.util.GenericHelper;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.InterfaceProjectClicked;
import de.smacsoftwares.aplanapp.util.InterfaceforSelectFilter;
import de.smacsoftwares.aplanapp.util.LogApp;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;
import okhttp3.Headers;
import retrofit2.Call;


/**
 * Created by SSOFT4 on 7/15/2016.
 */
public class FragmentFilterText extends Fragment implements View.OnClickListener,InterfaceforSelectFilter,
        InterfaceProjectClicked
{
    int pos=0;
    boolean isTablet,isFirstTime;
    String comefrom="this";
    boolean isTextChanged=false;
    boolean isTimetoChangeText=false;
    Context context;
    PreferencesHelper preferences;
    DatabaseHandler dbHandler;

    ArrayList<ResourceDataSetClass> arraylistResourceTemp;
    ArrayList<GroupDatasetClass> arrayListGroup = new ArrayList<>();
    ArrayList<ResourceDataSetClass> arrayListResource = new ArrayList<>();
    HashMap<String, ArrayList<ResourceDataSetClass>> hashMap = new HashMap<String, ArrayList<ResourceDataSetClass>>();

    TextView textviewDone,textviewGeneralFilter;
    TextView textviewAnytext,textviewPrio,textviewPrioTo,textviewLevelTo,textviewIdentno,textviewTask,textviewClient,
    textViewResponsible,textviewGroup,textviewResource,textviewTitle;
    Spinner spinnerPriorityfrm,spinnerPriorityTo,spinnerClient,spinnerResponsible,spinnerLevel, spinnerGroup, spinnerResourse;
    ImageView imgCrossPriofrm,imgCrossPrioto,imgCrossLevel,imgCrossClient,imgCrossResponsible,imgCrossGroup,imgCrossResource;
    EditText edittextAnytext,edittextIdentNo,edittextTask;
    ImageView imgDeleteFilter,imgBack;
    RelativeLayout relativeMain;
    LinearLayout linearBack;

    ArrayList<String> arraylistPriority = new ArrayList<>();
    ArrayList<String> arraylistPriorityTo = new ArrayList<>();
    ArrayList<String> arraylistLevel = new ArrayList<>();
    ArrayList<ClientDataSetClass> arraylistClient = new ArrayList<>();
    ArrayList<ResponsibleDataSetClass> arraylistResponsible = new ArrayList<>();
    ArrayList<GeneralFilterDataSet> arraylistgetedFromDatabase = new ArrayList<>();
    ArrayList<String> arraylistValue = new ArrayList<>();

    SpinnerAdapter spinnerAdapter;
    ResponsibleSpinnerAdapter responsibleAdapter;
    ClientSpinnerAdapter clientAdapter;
    GroupAdapter gruopAdapter;
    ResourceAdapter resourceAdapter;

    RelativeLayout relativeFooter;
    View rootView;
    String strAnytext="",strPriofrm="",strPrioto="",strLevel="",strIdentNo="",strTask="",strClient="",strResponsible="";
    String strAnytextfinal="",strPriofrmfinal="",strPriotofinal="",strLevelfinal="",strIdentNofinal="",strTaskfinal="",
            strClientfinal="",strResponsiblefinal="",strResourcefinal="",strGroupfinal="";
    String strSelectedClient="",strSelectedResponsible="",strSelectedGroup="",strSelectedResource="";
    String strSelectedClientId="",strSelectedResponsibleId="",strSelectedGroupId="",strSelectedResourceId="";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_filter_text, container, false);
        //MyApplication.component(getActivity()).inject(this);
        dbHandler = new DatabaseHandler(getActivity());
        preferences = new PreferencesHelper(getActivity());
        clickOutSideSpinner(rootView);
        initControl();
        return rootView;
    }
    public FragmentFilterText(){
//  An empty constructor for Android System to use, otherwise exception may occur.
    }
    //    this method intialize control and class all initial work
    public void initControl()
    {
        isTablet = getResources().getBoolean(R.bool.isTablet);
        GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);
        arrayListResource = new ArrayList<>();
        arraylistResourceTemp = new ArrayList<>();
        arraylistPriority = new ArrayList<>();
        arraylistClient = new ArrayList<>();
        arraylistPriorityTo = new ArrayList<>();
        arraylistLevel = new ArrayList<>();
        arraylistResponsible = new ArrayList<>();
        arrayListGroup = new ArrayList<>();

        imgBack=(ImageView)rootView.findViewById(R.id.imgback);
        linearBack = (LinearLayout)rootView.findViewById(R.id.linearback);
        linearBack.setOnClickListener(this);
        relativeMain = (RelativeLayout)rootView.findViewById(R.id.relative_text_main);
        GlobalClass.setupTypeface(relativeMain, GlobalClass.fontLight);
        imgDeleteFilter = (ImageView)rootView.findViewById(R.id.img_delete);
        imgDeleteFilter.setOnClickListener(this);
        textviewDone= (TextView)rootView.findViewById(R.id.textview_done);
        textviewDone.setOnClickListener(this);
        textviewDone.setVisibility(View.GONE);
        textviewGeneralFilter = (TextView)rootView.findViewById(R.id.textview_generalfilter);
        textviewGeneralFilter.setOnClickListener(this);
        textviewGeneralFilter.setOnTouchListener(new CustomTouchListener());

        textviewAnytext = (TextView)rootView.findViewById(R.id.textview_lable_anytext);
        textviewPrio = (TextView)rootView.findViewById(R.id.textview_lable_prio);
        textviewPrioTo = (TextView)rootView.findViewById(R.id.textview_lable_prioto);
        textviewLevelTo = (TextView)rootView.findViewById(R.id.textview_lable_levelto);
        textviewIdentno = (TextView)rootView.findViewById(R.id.textview_lable_identno);
        textviewTask = (TextView)rootView.findViewById(R.id.textview_lable_task);
        textviewClient = (TextView)rootView.findViewById(R.id.textview_lable_client);
        textViewResponsible = (TextView)rootView.findViewById(R.id.textview_lable_responsible);
        textviewGroup = (TextView)rootView.findViewById(R.id.textview_lable_group);
        textviewResource = (TextView)rootView.findViewById(R.id.textview_lable_source);
        textviewTitle = (TextView)rootView.findViewById(R.id.textview_lable_title);
        textviewTitle.setTypeface(GlobalClass.fontBold);

        edittextAnytext=(EditText)rootView.findViewById(R.id.edittext_anytext);
        edittextIdentNo=(EditText)rootView.findViewById(R.id.edittext_identity);
        edittextTask=(EditText)rootView.findViewById(R.id.edittext_task);


        imgCrossPriofrm = (ImageView)rootView.findViewById(R.id.img_cross_priofrm);
        imgCrossPrioto = (ImageView)rootView.findViewById(R.id.img_cross_prioto);
        imgCrossLevel = (ImageView)rootView.findViewById(R.id.img_cross_level);
        imgCrossClient = (ImageView)rootView.findViewById(R.id.img_cross_client);
        imgCrossResponsible = (ImageView)rootView.findViewById(R.id.img_cross_responsible);
        imgCrossGroup = (ImageView)rootView.findViewById(R.id.img_cross_group);
        imgCrossResource = (ImageView)rootView.findViewById(R.id.img_cross_resource);

        imgCrossPrioto.setOnClickListener(this);
        imgCrossPriofrm.setOnClickListener(this);
        imgCrossLevel.setOnClickListener(this);
        imgCrossClient.setOnClickListener(this);
        imgCrossResponsible.setOnClickListener(this);
        imgCrossGroup.setOnClickListener(this);
        imgCrossResource.setOnClickListener(this);


        spinnerPriorityfrm = (Spinner)rootView.findViewById(R.id.spinner_priority);
        spinnerPriorityTo = (Spinner)rootView.findViewById(R.id.spinner_to);
        spinnerLevel = (Spinner)rootView.findViewById(R.id.spinner_level);
        spinnerClient = (Spinner)rootView.findViewById(R.id.spinner_client);
        spinnerResponsible = (Spinner)rootView.findViewById(R.id.spinner_responsible);
        spinnerGroup = (Spinner)rootView.findViewById(R.id.spinner_group);
        spinnerResourse = (Spinner)rootView.findViewById(R.id.spinner_resource);
        addArraylistData();
        spinnerAdapter = new SpinnerAdapter(getActivity(),arraylistPriority);
        spinnerPriorityfrm.setAdapter(spinnerAdapter);

        spinnerAdapter = new SpinnerAdapter(getActivity(),arraylistPriorityTo);
        spinnerPriorityTo.setAdapter(spinnerAdapter);

        spinnerAdapter = new SpinnerAdapter(getActivity(),arraylistLevel);
        spinnerLevel.setAdapter(spinnerAdapter);

        spinnerItemSelector(spinnerPriorityfrm);
        spinnerItemSelector(spinnerPriorityTo);
        spinnerItemSelector(spinnerLevel);
        spinnerItemSelector(spinnerClient);
        spinnerItemSelector(spinnerResponsible);
        spinnerItemSelector(spinnerGroup);
        spinnerItemSelector(spinnerResourse);

        editTextTextChange(edittextAnytext);
        editTextTextChange(edittextIdentNo);
        editTextTextChange(edittextTask);
         /* calling the service */
        if(GlobalClass.isNetworkAvailable(getActivity()))
        {
            getResponsiblePersonAndClient();
        }
        else
        {
            //GlobalClass.showToastInternet(getActivity());
            GlobalClass.showCustomDialogInternet(getActivity());
        }
        setLanguage();
    }

    public void doneButtonClicked(String comefrom){
        if(comefrom.equalsIgnoreCase("this")){
            preferences.saveIsProgressShow("yes");
        }
        else {
            preferences.saveIsProgressShow("no");
        }
        dbHandler.deleteGeneralFilterData(preferences.getProfileId(),getString(R.string.TextFilter_text)
                ,preferences.getLanguage(),preferences.getUserID());
        //dbHandler.deleteTextFilter(preferences.getUserID(),preferences.getProfileId());
        if(isTextChanged){

        }
        if(edittextAnytext.getText().toString().equalsIgnoreCase("")){
            strAnytext="null";
        }
        else {
            strAnytext=edittextAnytext.getText().toString();
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_anyText),strAnytext,preferences.getLanguage(),getString(R.string.TextFilter_text),preferences.getUserID(),"");
            //dbHandler.addTextFilter(getString(R.string.key_anyText),strAnytext,preferences.getUserID(),preferences.getProfileId());
        }

        if(edittextIdentNo.getText().toString().equalsIgnoreCase("")){
            strIdentNo="null";
        }
        else {
            strIdentNo=edittextIdentNo.getText().toString();
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_indentyNo),strIdentNo,preferences.getLanguage(),getString(R.string.TextFilter_text),preferences.getUserID(),"");
            //dbHandler.addTextFilter(getString(R.string.key_indentyNo),strIdentNo,preferences.getUserID(),preferences.getProfileId());
        }

        if(edittextTask.getText().toString().equalsIgnoreCase("")){
            strTask="null";
        }
        else {
            strTask=edittextTask.getText().toString();
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_Task),strTask,preferences.getLanguage(),getString(R.string.TextFilter_text),preferences.getUserID(),"");
            //dbHandler.addTextFilter(getString(R.string.key_Task),strTask,preferences.getUserID(),preferences.getProfileId());
        }

        if(strSelectedGroupId != null && !TextUtils.isEmpty(strSelectedGroupId)){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_ResourceGroupId),strSelectedGroupId,preferences.getLanguage(),getString(R.string.TextFilter_text),preferences.getUserID(),"");
            //dbHandler.addTextFilter(getString(R.string.key_ResourceGroupId),strSelectedGroupId,preferences.getUserID(),preferences.getProfileId());
        }
        if(strSelectedResourceId != null && !TextUtils.isEmpty(strSelectedResourceId)){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_ResourceId),strSelectedResourceId,preferences.getLanguage(),getString(R.string.TextFilter_text),preferences.getUserID(),"");
            //dbHandler.addTextFilter(getString(R.string.key_ResourceId),strSelectedResourceId,preferences.getUserID(),
                    //preferences.getProfileId());
        }
        if(spinnerPriorityfrm.getSelectedItem().toString() != null &&
                !spinnerPriorityfrm.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.blank_value))){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_priFrom),
                    spinnerPriorityfrm.getSelectedItem().toString(),preferences.getLanguage(),
                    getString(R.string.TextFilter_text),preferences.getUserID(),"");
            //dbHandler.addTextFilter(getString(R.string.key_priFrom),spinnerPriorityfrm.getSelectedItem().toString(),
                    //preferences.getUserID(),preferences.getProfileId());
        }
        if(spinnerPriorityTo.getSelectedItem().toString() != null &&
                !spinnerPriorityTo.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.blank_value))){
            dbHandler.addGeneralFilter(preferences.getProfileId(),
                    getString(R.string.key_priTO),spinnerPriorityTo.getSelectedItem().toString(),
                    preferences.getLanguage(),getString(R.string.TextFilter_text),preferences.getUserID(),"");
            //dbHandler.addTextFilter(getString(R.string.key_priTO),spinnerPriorityTo.getSelectedItem().toString(),
                    //preferences.getUserID(),preferences.getProfileId());
        }
        if(spinnerLevel.getSelectedItem().toString() != null &&
                !spinnerLevel.getSelectedItem().toString().equalsIgnoreCase(getString(R.string.blank_value))){
            dbHandler.addGeneralFilter(preferences.getProfileId(),
                    getString(R.string.key_level),spinnerLevel.getSelectedItem().toString(),
                    preferences.getLanguage(),getString(R.string.TextFilter_text),preferences.getUserID(),"");
            //dbHandler.addTextFilter(getString(R.string.key_level),spinnerLevel.getSelectedItem().toString(),
                    //preferences.getUserID(),preferences.getProfileId());
        }
        if(strSelectedClient != null &&
                !TextUtils.isEmpty(strSelectedClient) &&
                   !strSelectedClient.equalsIgnoreCase(getString(R.string.blank_value))){
            dbHandler.addGeneralFilter(preferences.getProfileId(),
                    getString(R.string.key_Client),strSelectedClient,
                    preferences.getLanguage(),getString(R.string.TextFilter_text),preferences.getUserID(),"");
        }
        if(strSelectedResponsible != null && !TextUtils.isEmpty(strSelectedResponsible) &&
                !strSelectedResponsible.equalsIgnoreCase(getString(R.string.blank_value))){
            dbHandler.addGeneralFilter(preferences.getProfileId(),
                    getString(R.string.key_Responsible),strSelectedResponsible,
                    preferences.getLanguage(),getString(R.string.TextFilter_text),preferences.getUserID(),"");
        }
        /*strAnytextfinal="anytext="+strAnytext;
        strIdentNofinal="identyno="+strIdentNo;
        strTaskfinal="task="+strTask;

        strPriofrmfinal="priofrm="+spinnerPriorityfrm.getSelectedItem().toString();
        strPriotofinal="prioto="+spinnerPriorityTo.getSelectedItem().toString();
        strLevelfinal="level="+spinnerLevel.getSelectedItem().toString();

        strClientfinal="client="+strSelectedClient;
        strResponsiblefinal="responsible="+strSelectedResponsible;
        strGroupfinal = "group="+strSelectedGroup;
        strResourcefinal = "resource="+strSelectedResource;

        String finalCommaseperated = strAnytextfinal+","+strPriofrmfinal+","+strPriotofinal+","+
                strLevelfinal+","+strIdentNofinal
                +","+strTaskfinal+","+strClientfinal+","+strResponsiblefinal+","+strGroupfinal+","+strResourcefinal;
        dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.TextFilter_text));
        if(strAnytext.equalsIgnoreCase("null") && strIdentNo.equalsIgnoreCase("null") && strTask.equalsIgnoreCase("null")
                && spinnerPriorityfrm.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.blank_value))
                && spinnerPriorityTo.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.blank_value))
                && spinnerLevel.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.blank_value))
                && strSelectedClient.equalsIgnoreCase(getResources().getString(R.string.blank_value))
                && strSelectedResponsible.equalsIgnoreCase(getResources().getString(R.string.blank_value))
                && strSelectedGroup.equalsIgnoreCase(getResources().getString(R.string.blank_value))
                && strSelectedResource.equalsIgnoreCase(getResources().getString(R.string.blank_value))){
        }
        else if(preferences.getLastTextFilterString().equalsIgnoreCase("")){
            preferences.saveLastTextFilterString(finalCommaseperated);
            dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.TextFilter_text));
            //dbHandler.deleteTextFilter(preferences.getUserID(),preferences.getProfileId());
            dbHandler.addGeneralFilterInfo(getString(R.string.TextFilter_text),finalCommaseperated,"","",preferences.getUserID(),
                    preferences.getProfileId());
            // fire filter
            if(preferences.getCurrentFiredFilter().equalsIgnoreCase("1") ||
                    preferences.getCurrentFiredFilter().equalsIgnoreCase("2") ||
                    preferences.getCurrentFiredFilter().equalsIgnoreCase("3") ||
                    preferences.getCurrentFiredFilter().equalsIgnoreCase("4") ||
                    preferences.getCurrentFiredFilter().equalsIgnoreCase("")){
                preferences.saveCurrentFiredFilter("6");
            }
            else if(preferences.getCurrentFiredFilter().equalsIgnoreCase("8")){
                if(preferences.getLastFiredFilterAll().equalsIgnoreCase("dashboard")){
                    preferences.saveCurrentFiredFilter("6");
                }else {
                    preferences.saveCurrentFiredFilter("7");
                }

            }
            else {
                if(preferences.getLastFiredFilterAll().equalsIgnoreCase("dashboard")){
                    preferences.saveCurrentFiredFilter("6");
                }else {
                    preferences.saveCurrentFiredFilter("7");
                }
            }
        }
        else {
            if(preferences.getLastTextFilterString().equalsIgnoreCase(finalCommaseperated)){
                preferences.saveLastTextFilterString(finalCommaseperated);
                //dbHandler.deleteTextFilter(preferences.getUserID(),preferences.getProfileId());
                dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.TextFilter_text));
                dbHandler.addGeneralFilterInfo(getString(R.string.TextFilter_text),finalCommaseperated,"","",preferences.getUserID(),
                        preferences.getProfileId());
                // don't fire filter
            }
            else {
                // fire filter
                preferences.saveLastTextFilterString(finalCommaseperated);
                //dbHandler.deleteTextFilter(preferences.getUserID(),preferences.getProfileId());
                dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.TextFilter_text));
                dbHandler.addGeneralFilterInfo(getString(R.string.TextFilter_text),finalCommaseperated,"","",preferences.getUserID(),
                        preferences.getProfileId());
                if(preferences.getCurrentFiredFilter().equalsIgnoreCase("1") ||
                        preferences.getCurrentFiredFilter().equalsIgnoreCase("2") ||
                        preferences.getCurrentFiredFilter().equalsIgnoreCase("3") ||
                        preferences.getCurrentFiredFilter().equalsIgnoreCase("4") ||
                        preferences.getCurrentFiredFilter().equalsIgnoreCase("")){
                    preferences.saveCurrentFiredFilter("6");
                }
                else if(preferences.getCurrentFiredFilter().equalsIgnoreCase("8")){
                    if(preferences.getLastFiredFilterAll().equalsIgnoreCase("dashboard")){
                        preferences.saveCurrentFiredFilter("6");
                    }else {
                        preferences.saveCurrentFiredFilter("7");
                    }

                }
                else {
                    if(preferences.getLastFiredFilterAll().equalsIgnoreCase("dashboard")){
                        preferences.saveCurrentFiredFilter("6");
                    }else {
                        preferences.saveCurrentFiredFilter("7");
                    }
                }
            }
        }*/
        /*if(strAnytext.equalsIgnoreCase("null") && strIdentNo.equalsIgnoreCase("null") && strTask.equalsIgnoreCase("null")
                && spinnerPriorityfrm.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.blank_value))
                && spinnerPriorityTo.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.blank_value))
                && spinnerLevel.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.blank_value))
                && strSelectedClient.equalsIgnoreCase(getResources().getString(R.string.blank_value))
                && strSelectedResponsible.equalsIgnoreCase(getResources().getString(R.string.blank_value))
                && strSelectedGroup.equalsIgnoreCase(getResources().getString(R.string.blank_value))
                && strSelectedResource.equalsIgnoreCase(getResources().getString(R.string.blank_value))){
        }
        else {
            if(preferences.getLastTextFilterString().equalsIgnoreCase(finalCommaseperated)){
                preferences.saveLastTextFilterString(finalCommaseperated);
                dbHandler.addGeneralFilterInfo(getString(R.string.TextFilter_text),finalCommaseperated,"","",preferences.getUserID(),
                        preferences.getProfileId());
            }
            else {
                preferences.saveCurrentFiredFilter("6");
                preferences.saveLastTextFilterString(finalCommaseperated);
                dbHandler.addGeneralFilterInfo(getString(R.string.TextFilter_text),finalCommaseperated,"","",preferences.getUserID(),
                        preferences.getProfileId());
            }

        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview_generalfilter:
                comefrom="this";
                doneButtonClicked(comefrom);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentSelectFilter fragment = new FragmentSelectFilter();
                transaction.replace(R.id.select_filter_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

                break;
            case R.id.linearback:
                comefrom="this";
                doneButtonClicked(comefrom);

                FragmentTransaction transaction2 = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentSelectFilter fragment2 = new FragmentSelectFilter();
                transaction2.replace(R.id.select_filter_container, fragment2);
                transaction2.addToBackStack(null);
                transaction2.commit();
                break;
            case R.id.textview_done:
                comefrom="this";
                doneButtonClicked(comefrom);
                break;
            case R.id.img_delete:
                dbHandler.deleteGeneralFilterData(preferences.getProfileId(),getString(R.string.TextFilter_text)
                        ,preferences.getLanguage(),preferences.getUserID());
                //dbHandler.updateGeneralFilterToDelete(preferences.getProfileId(),getString(R.string.TextFilter_text),"",preferences.getLanguage(),preferences.getUserID());
                edittextAnytext.setText("");
                edittextIdentNo.setText("");
                edittextTask.setText("");
                spinnerClient.setSelection(0);
                spinnerLevel.setSelection(0);
                spinnerPriorityfrm.setSelection(0);
                spinnerPriorityTo.setSelection(0);
                spinnerGroup.setSelection(0);
                spinnerResourse.setSelection(0);
                spinnerResponsible.setSelection(0);
                resetAllvalue();
                break;
            case R.id.img_cross_prioto:
                spinnerPriorityTo.setSelection(0);
                break;
            case R.id.img_cross_priofrm:
                spinnerPriorityfrm.setSelection(0);
                break;
            case R.id.img_cross_level:
                spinnerLevel.setSelection(0);
                break;
            case R.id.img_cross_client:
                spinnerClient.setSelection(0);
                break;
            case R.id.img_cross_responsible:
                spinnerResponsible.setSelection(0);
                break;
            case R.id.img_cross_resource:
                spinnerResourse.setSelection(0);
                break;
            case R.id.img_cross_group:
                spinnerGroup.setSelection(0);
                break;
        }

    }
    public void resetAllvalue(){
        strSelectedClient="";
        strSelectedClientId="";
        strSelectedGroup="";
        strSelectedGroupId="";
        strSelectedResource="";
        strSelectedResourceId="";
        strSelectedResponsible="";
        strSelectedResponsibleId="";

        edittextAnytext.setText("");
        edittextIdentNo.setText("");
        edittextTask.setText("");

        spinnerClient.setSelection(0);
        spinnerLevel.setSelection(0);
        spinnerPriorityfrm.setSelection(0);
        spinnerPriorityTo.setSelection(0);
        spinnerGroup.setSelection(0);
        spinnerResourse.setSelection(0);
        spinnerResponsible.setSelection(0);
    }
//    add static data in arraylist for listview
    public void addArraylistData()
    {
        arraylistPriority.add(getResources().getString(R.string.blank_value));
        arraylistPriorityTo.add(getResources().getString(R.string.blank_value));
        arraylistLevel.add(getResources().getString(R.string.blank_value));
        for (int i = 1;i<=100;i++){
            arraylistPriority.add(""+i);
            arraylistPriorityTo.add(""+i);
            arraylistLevel.add(""+i);
            //arraylistClient.add("item"+i);
           // arraylistResponsible.add("item"+i);
        }
    }
    public String getEdittextValue(EditText edittext)
    {
        String value="";
        if(edittext.getText().toString().equalsIgnoreCase("")){
            value="null";
        }
        else {
            value=edittext.getText().toString();
        }
        return value;
    }
    public String setTextinEdittext(String text)
    {
        String value="";
        if(text.equalsIgnoreCase("null") || text.equalsIgnoreCase(""))
        {
            value="";
        }
        else
        {
            value=text;
        }
        return  value;
    }
    /* this method will call get new device token service to get refreshed device token */
    public void updateAccessToken(){
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
                            getResponsiblePersonAndClient();
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
    private void getResponsiblePersonAndClient()
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

            GenericHelper helper = new GenericHelper(getActivity());
            Call<String> call = helper.getRetrofit().getResponsibleAndClient();
            call.enqueue(new retrofit2.Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    try {
                            String responseText = response.body();
                            if(responseText.equalsIgnoreCase("") || responseText == null){
                                updateAccessToken();
                            }
                            else {
                                JSONObject objMain = (JSONObject) new JSONTokener(responseText).nextValue();
                                String status = objMain.getString("Status");
                                if (status.equalsIgnoreCase("0")) {
                                    JSONObject objPayload = objMain.getJSONObject("Payload");
                                    JSONArray jsonArrayResource = objPayload.getJSONArray("Resources");

                                    if(jsonArrayResource.length() > 0){
                                        hashMap = new HashMap<String, ArrayList<ResourceDataSetClass>>();
                                        arrayListGroup = new ArrayList<GroupDatasetClass>();
                                        for (int i=0;i<jsonArrayResource.length();i++){

                                            JSONObject objInner = jsonArrayResource.getJSONObject(i);
                                            if(i==0){
                                                addBlankGroup();
                                            }
                                            String GroupId=objInner.getString("Id");
                                            String GroupName=objInner.getString("Name");
                                            GroupDatasetClass group = new GroupDatasetClass();
                                            group.setGroupId(GroupId);
                                            group.setGroupName(GroupName);
                                            arrayListGroup.add(group);
                                            LogApp.e(" in outer looop "," Resource Name : "+GroupName);
                                            if(objInner.has("Childrens")){
                                                JSONArray jsonArrayChild = objInner.getJSONArray("Childrens");
                                                arrayListResource = new ArrayList<ResourceDataSetClass>();
                                                if(jsonArrayChild.length() > 0){
                                                    for (int k=0;k<jsonArrayChild.length();k++){
                                                        JSONObject objChild = jsonArrayChild.getJSONObject(k);
                                                        if(k==0){
                                                            //addBlankResource();
                                                        }
                                                        String ResourceName= objChild.getString("Name");
                                                        String Id= objChild.getString("Id");
                                                        String IsDeleted= objChild.getString("IsDeleted");
                                                        String ResourceId= objChild.getString("ResourceId");
                                                        String ResourceType= objChild.getString("ResourceType");
                                                        String ResourceUnit= objChild.getString("Unit");


                                                        ResourceDataSetClass resource = new ResourceDataSetClass();
                                                        resource.setResourceName(ResourceName);
                                                        resource.setResourceId(ResourceId);
                                                        resource.setId(Id);
                                                        resource.setDeleted(IsDeleted);
                                                        resource.setResourceType(ResourceType);
                                                        resource.setResourceUnit(ResourceUnit);
                                                        arrayListResource.add(resource);

                                                        LogApp.e(" in innner looop "," Resource Name : "+ResourceName);
                                                    }
                                                    hashMap.put(GroupId,arrayListResource);
                                                }
                                            }
                                        }
                                    }
                                    JSONArray jsonArrayClient = objPayload.getJSONArray("Clients");
                                    JSONArray jsonArrayResponsiblePerson = objPayload.getJSONArray("ResponsiblePersons");
                                    if(arrayListGroup.size() > 0){
                                        gruopAdapter = new GroupAdapter(getActivity(),arrayListGroup);
                                        spinnerGroup.setAdapter(gruopAdapter);
                                    }
                                    /* adding blank ---- value to both client and responsible*/
                                    //addBlankClient();
                                    //addBlankResponsible();
                                    /* parsing client array data and storing in arraylist and showing in listview */
                                    if(jsonArrayClient.length() > 0){
                                        for (int i=0;i<jsonArrayClient.length();i++) {
                                            JSONObject objInner = jsonArrayClient.getJSONObject(i);
                                            if(i==0){
                                                addBlankClient();
                                            }
                                            String contact=objInner.getString("Contact");
                                            String designation=objInner.getString("Designation");
                                            String email=objInner.getString("Email");
                                            String gendar=objInner.getString("Gender");
                                            String id=objInner.getString("Id");
                                            String name=objInner.getString("Name");
                                            String type=objInner.getString("Type");

                                            ClientDataSetClass client = new ClientDataSetClass();
                                            client.setContatct(contact);
                                            client.setDesignation(designation);
                                            client.setEmail(email);
                                            client.setGendar(gendar);
                                            client.setId(id);
                                            client.setType(type);
                                            client.setName(name);

                                            arraylistClient.add(client);

                                        }
                                    }
                                    else {
                                        addBlankClient();
                                    }
                                    /* parsing responsible PERSON data storing in arraylist and showing in listview */
                                    if(jsonArrayResponsiblePerson.length() > 0){
                                        for (int i=0;i<jsonArrayResponsiblePerson.length();i++) {
                                            JSONObject objInner = jsonArrayResponsiblePerson.getJSONObject(i);
                                            if(i==0){
                                                addBlankResponsible();
                                            }
                                            String contact=objInner.getString("Contact");
                                            String designation=objInner.getString("Designation");
                                            String email=objInner.getString("Email");
                                            String gendar=objInner.getString("Gender");
                                            String id=objInner.getString("Id");
                                            String name=objInner.getString("Name");
                                            String type=objInner.getString("Type");

                                            ResponsibleDataSetClass responsible = new ResponsibleDataSetClass();
                                            responsible.setContact(contact);
                                            responsible.setDesignation(designation);
                                            responsible.setEmail(email);
                                            responsible.setGendar(gendar);
                                            responsible.setId(id);
                                            responsible.setType(type);
                                            responsible.setName(name);

                                            arraylistResponsible.add(responsible);

                                        }
                                    }
                                    else {
                                        addBlankResponsible();
                                    }

                                    if(arraylistResponsible.size() > 0){
                                        responsibleAdapter = new ResponsibleSpinnerAdapter(getActivity(),arraylistResponsible);
                                        spinnerResponsible.setAdapter(responsibleAdapter);
                                    }
                                    if(arraylistClient.size() > 0){
                                        clientAdapter = new ClientSpinnerAdapter(getActivity(),arraylistClient);
                                        spinnerClient.setAdapter(clientAdapter);
                                    }
                            }
                        }
                    }
                    catch (Exception e){
                    }

                    if(dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(),getString(R.string.TextFilter_text),
                            preferences.getLanguage(),preferences.getUserID()) > 0){
                        arraylistgetedFromDatabase = dbHandler.getAllGeneralFilterData(preferences.getProfileId(),getString(R.string.TextFilter_text),
                                preferences.getLanguage(),preferences.getUserID());
                    }
                    arraylistValue = new ArrayList<>();
                    if(arraylistgetedFromDatabase.size()>0){

                        for (int i=0;i<arraylistgetedFromDatabase.size();i++){
                            if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_anyText))){
                                edittextAnytext.setText(setTextinEdittext(arraylistgetedFromDatabase.get(i).getValue()));
                            }
                            else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_priFrom))){
                                spinnerPriorityfrm.setSelection(getIndex(spinnerPriorityfrm, arraylistgetedFromDatabase.get(i).getValue()));
                            }
                            else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_priTO))){
                                spinnerPriorityTo.setSelection(getIndex(spinnerPriorityTo, arraylistgetedFromDatabase.get(i).getValue()));
                            }
                            else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_level))){
                                spinnerLevel.setSelection(getIndex(spinnerLevel, arraylistgetedFromDatabase.get(i).getValue()));
                            }
                            else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_indentyNo))){
                                edittextIdentNo.setText(setTextinEdittext(arraylistgetedFromDatabase.get(i).getValue()));
                            }
                            else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_Task))){
                                edittextTask.setText(setTextinEdittext(arraylistgetedFromDatabase.get(i).getValue()));
                            }
                            else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_Client))){
                                if(arraylistClient.size() > 0){
                                    for (int j=0;j<arraylistClient.size();j++){
                                        if(arraylistClient.get(j).getStrId() != null){
                                            strSelectedClient=arraylistClient.get(j).getStrId();
                                            if(arraylistClient.get(j).getStrId().equalsIgnoreCase(arraylistgetedFromDatabase.get(i).getValue())){
                                                spinnerClient.setSelection(j);
                                                break;
                                            }
                                        }

                                    }
                                }
                                //spinnerClient.setSelection(getIndex(spinnerClient, arraylistgetedFromDatabase.get(i).getValue()));
                            }
                            else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_Responsible))){
                                if(arraylistResponsible.size() > 0){
                                    for (int j=0;j<arraylistResponsible.size();j++){
                                        if(arraylistResponsible.get(j).getId() != null){
                                            if(arraylistResponsible.get(j).getId().equalsIgnoreCase(arraylistgetedFromDatabase.get(i).getValue())){
                                                strSelectedResponsible=arraylistResponsible.get(j).getId();
                                                spinnerResponsible.setSelection(j);
                                                break;
                                            }
                                        }

                                    }
                                }
                                //spinnerResponsible.setSelection(getIndex(spinnerResponsible, arraylistgetedFromDatabase.get(i).getValue()));
                            }
                            else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_ResourceId))){


                                //spinnerResourse.setSelection(getIndex(spinnerResourse, arraylistgetedFromDatabase.get(i).getValue()));
                            }
                            else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_ResourceGroupId))){
                                pos=i;
                                for (int k=0;k<arrayListGroup.size();k++){
                                    if(arrayListGroup.get(k).getGroupId().equalsIgnoreCase(arraylistgetedFromDatabase.get(i).getValue())){
                                        spinnerGroup.setSelection(k);
                                        strSelectedGroupId=arrayListGroup.get(k).getGroupId();
                                        break;
                                    }
                                }

                                    /*arraylistResourceTemp = new ArrayList<ResourceDataSetClass>();
                                    if (hashMap != null && hashMap.size() > 0)
                                    {
                                        if (hashMap.containsKey(arraylistgetedFromDatabase.get(i).getValue()))
                                        {
                                            ResourceDataSetClass resource = new ResourceDataSetClass();
                                            resource.setResourceName(getResources().getString(R.string.blank_value));
                                            resource.setResourceId("null");
                                            resource.setId("null");
                                            resource.setDeleted("null");
                                            resource.setResourceType("null");
                                            resource.setResourceUnit("null");
                                            arraylistResourceTemp.add(resource);
                                            arraylistResourceTemp.addAll(hashMap.get(arraylistgetedFromDatabase.get(i).getValue()));
                                        }
                                    }
                                    if (arraylistResourceTemp.size() > 0) {
                                        for (int k = 0; k < arraylistResourceTemp.size(); k++) {
                                            for (int a=0;a<arraylistgetedFromDatabase.size();a++){
                                                if(arraylistResourceTemp.get(k).getResourceId().equalsIgnoreCase(arraylistgetedFromDatabase.get(a).getValue())){
                                                    spinnerResourse.setSelection(k);
                                                }
                                            }
                                        }
                                    }*/
                                //spinnerGroup.setSelection(getIndex(spinnerGroup, arraylistgetedFromDatabase.get(i).getValue()));
                            }
                        }
                    }
                    getspinnerSelectItemEvent();
                    if(progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    isTimetoChangeText=true;
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if(progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    isTimetoChangeText=true;
                    if(t instanceof UnknownHostException){
                        //progressDialog.dismiss();
                        //GlobalClass.showToast(getActivity(), getString(R.string.url_not_found));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.enter_organization_url_title),getString(R.string.enter_valid_url));
                        preferences.save404Count(preferences.get404Count()+1);
                    }
                }
            });
        }
        catch (Exception e){
            LogApp.e(" while client responsible service : "," in catch : "+e.toString());
            isTimetoChangeText=true;
        }
    }
    public void addBlankClient(){
        ClientDataSetClass client = new ClientDataSetClass();
        client.setName(getResources().getString(R.string.blank_value));
        arraylistClient.add(client);
    }
    public void addBlankResponsible(){
        ResponsibleDataSetClass responsible = new ResponsibleDataSetClass();
        responsible.setName(getResources().getString(R.string.blank_value));
        arraylistResponsible.add(responsible);
    }
    public void addBlankGroup(){
        GroupDatasetClass group = new GroupDatasetClass();
        group.setGroupId("null");
        group.setGroupName(getResources().getString(R.string.blank_value));
        arrayListGroup.add(group);
    }
    public void addBlankResource(){
        ResourceDataSetClass resource = new ResourceDataSetClass();
        resource.setResourceName(getResources().getString(R.string.blank_value));
        resource.setResourceId("null");
        resource.setId("null");
        resource.setDeleted("null");
        resource.setResourceType("null");
        resource.setResourceUnit("null");
        arrayListResource.add(resource);
    }
    public void getspinnerSelectItemEvent(){
        spinnerClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(arraylistClient.size() > 0){
                    strSelectedClient = arraylistClient.get(position).getStrId();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        spinnerResponsible.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(arraylistResponsible.size() > 0){
                    strSelectedResponsible = arraylistResponsible.get(position).getId();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        spinnerResourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(arraylistResourceTemp != null){
                    if(arraylistResourceTemp.size() > 0){
                        strSelectedResource = arraylistResourceTemp.get(position).getResourceName();
                        if(position != 0){
                            strSelectedResourceId = arraylistResourceTemp.get(position).getResourceId();
                            doneButtonClicked(comefrom);
                        }
                    }
                }
                else {
                    strSelectedResource = getString(R.string.blank_value);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                    if(arrayListGroup.size() > 0){
                        strSelectedGroup = arrayListGroup.get(position).getGroupName();
                    }
                    if(position == 0){
                        arraylistResourceTemp = new ArrayList<ResourceDataSetClass>();
                        ResourceDataSetClass resource = new ResourceDataSetClass();
                        resource.setResourceName(getResources().getString(R.string.blank_value));
                        resource.setResourceId("null");
                         resource.setId("null");
                        resource.setDeleted("null");
                        resource.setResourceType("null");
                        resource.setResourceUnit("null");
                        arraylistResourceTemp.add(resource);

                        if (arraylistResourceTemp.size() > 0)
                        {
                            resourceAdapter = new ResourceAdapter(getActivity(), arraylistResourceTemp);
                            spinnerResourse.setAdapter(null);
                            spinnerResourse.setAdapter(resourceAdapter);
                        }


                    }else {
                        strSelectedGroupId = arrayListGroup.get(position).getGroupId();
                        String selectedGroupId = arrayListGroup.get(position).getGroupId();
                        if (!selectedGroupId.equalsIgnoreCase("")) {
                            arraylistResourceTemp = new ArrayList<ResourceDataSetClass>();
                            if (hashMap != null && hashMap.size() > 0) {
                                if (hashMap.containsKey(selectedGroupId)) {
                                    ResourceDataSetClass resource = new ResourceDataSetClass();
                                    resource.setResourceName(getResources().getString(R.string.blank_value));
                                    resource.setResourceId("null");
                                    resource.setId("null");
                                    resource.setDeleted("null");
                                    resource.setResourceType("null");
                                    resource.setResourceUnit("null");
                                    arraylistResourceTemp.add(resource);

                                    arraylistResourceTemp.addAll(hashMap.get(selectedGroupId));
                                }
                            }
                            if (arraylistResourceTemp.size() > 0) {
                                resourceAdapter = new ResourceAdapter(getActivity(), arraylistResourceTemp);
                                spinnerResourse.setAdapter(null);
                                spinnerResourse.setAdapter(resourceAdapter);

                                if (arraylistResourceTemp.size() > 0) {
                                    for (int k = 0; k < arraylistResourceTemp.size(); k++) {
                                        for (int a=0;a<arraylistgetedFromDatabase.size();a++){
                                            if(arraylistResourceTemp.get(k).getResourceId().equalsIgnoreCase(arraylistgetedFromDatabase.get(a).getValue())){
                                                strSelectedResourceId=arraylistResourceTemp.get(k).getResourceId();
                                                spinnerResourse.setSelection(k);

                                            }
                                        }
                                    }
                                }

                            }

                        }
                    }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }
    //private method of your class
    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
    private int getIndexClient(ArrayList<ClientDataSetClass> arrayilist, String myString)
    {
        int index = 0;

        for (int i=0;i<arrayilist.size();i++){
            if (arrayilist.get(i).getName().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
    private int getIndexResponsible(ArrayList<ResponsibleDataSetClass> arrayilist, String myString)
    {
        int index = 0;

        for (int i=0;i<arrayilist.size();i++){
            if (arrayilist.get(i).getName().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
    private int getIndexGroup(ArrayList<GroupDatasetClass> arrayilist, String myString)
    {
        int index = 0;

        for (int i=0;i<arrayilist.size();i++){
            if (arrayilist.get(i).getGroupName().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
    private int getIndexResource(ArrayList<ResourceDataSetClass> arrayilist, String myString)
    {
        int index = 0;

        for (int i=0;i<arrayilist.size();i++){
            if (arrayilist.get(i).getResourceName().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
    @Override
    public void callSelectFilterFragment() {}
    @Override
    public void callUpdateFilter() {
        isTimetoChangeText = false;
        resetAllvalue();
        comefrom = "outside";
        setLanguage();
        arraylistgetedFromDatabase = new ArrayList<>();
        /*arraylistgetedFromDatabase = new ArrayList<>();
        if (dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(), getString(R.string.TextFilter_text),preferences.getLanguage(),preferences.getUserID()) > 0) {
            arraylistgetedFromDatabase = dbHandler.getAllGeneralFilterData(preferences.getProfileId(), getString(R.string.TextFilter_text),preferences.getLanguage(),preferences.getUserID());
        }
        if(arraylistgetedFromDatabase.size()>0){
            for (int i=0;i<arraylistgetedFromDatabase.size();i++){
                if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_anyText))){
                    edittextAnytext.setText(setTextinEdittext(arraylistgetedFromDatabase.get(i).getValue()));
                }
                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_priFrom))){
                    spinnerPriorityfrm.setSelection(getIndex(spinnerPriorityfrm, arraylistgetedFromDatabase.get(i).getValue()));
                }
                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_priTO))){
                    spinnerPriorityTo.setSelection(getIndex(spinnerPriorityTo, arraylistgetedFromDatabase.get(i).getValue()));
                }
                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_level))){
                    spinnerLevel.setSelection(getIndex(spinnerLevel, arraylistgetedFromDatabase.get(i).getValue()));
                }
                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_indentyNo))){
                    edittextIdentNo.setText(setTextinEdittext(arraylistgetedFromDatabase.get(i).getValue()));
                }
                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_Task))){
                    edittextTask.setText(setTextinEdittext(arraylistgetedFromDatabase.get(i).getValue()));
                }
                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_Client))){
                    if(arraylistClient.size() > 0){
                        for (int j=0;j<arraylistClient.size();j++){
                            if(arraylistClient.get(j).getStrId() != null){
                                if(arraylistClient.get(j).getStrId().equalsIgnoreCase(arraylistgetedFromDatabase.get(i).getValue())){
                                    spinnerClient.setSelection(j);
                                }
                            }

                        }
                    }
                }
                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_Responsible))){
                    if(arraylistResponsible.size() > 0){
                        for (int j=0;j<arraylistResponsible.size();j++){
                            if(arraylistResponsible.get(j).getId() != null){
                                if(arraylistResponsible.get(j).getId().equalsIgnoreCase(arraylistgetedFromDatabase.get(i).getValue())){
                                    spinnerResponsible.setSelection(j);
                                }
                            }

                        }
                    }
                }
                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_ResourceId))){
                }
                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_ResourceGroupId))){
                    pos=i;
                    for (int k=0;k<arrayListGroup.size();k++){
                        if(arrayListGroup.get(k).getGroupId().equalsIgnoreCase(arraylistgetedFromDatabase.get(i).getValue())){
                            spinnerGroup.setSelection(k);
                        }
                    }
                }
            }
            getspinnerSelectItemEvent();
            isTimetoChangeText = true;
        }*/
        if(GlobalClass.isNetworkAvailable(getActivity()))
        {
            getResponsiblePersonAndClient();
        }
        else
        {
            //GlobalClass.showToastInternet(getActivity());
            GlobalClass.showCustomDialogInternet(getActivity());
        }
    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        context = activity;
        ((HomeActivity)context).interfaceforSelectFilter = this;
        //((HomeActivity)context).interfaceChangeLanguageTextFilter = this;
        ((HomeActivity)context).interfaceProjectClicked = this;

    }
    public void spinnerItemSelector(Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isTimetoChangeText) {
                    isTextChanged=true;
                    doneButtonClicked(comefrom);
                    //GlobalClass.showToast(getActivity(), " item changed : ");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
    public void editTextTextChange(EditText edittext){
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(isTimetoChangeText)
                {
                    isTextChanged=true;
                    doneButtonClicked(comefrom);
                    //GlobalClass.showToast(getActivity(), " text Changed : ");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    public void setLanguage(){
        textviewAnytext.setText(getString(R.string.anytextfiled));
        textviewPrio.setText(getString(R.string.prio));
        textviewPrioTo.setText(getString(R.string.to));
        textviewLevelTo.setText(getString(R.string.leaveto));
        textviewIdentno.setText(getString(R.string.identno));
        textviewTask.setText(getString(R.string.task));
        textviewClient.setText(getString(R.string.client));
        textViewResponsible.setText(getString(R.string.responsible));
        textviewGroup.setText(getString(R.string.group));
        textviewResource.setText(getString(R.string.resources));
        textviewTitle.setText(getString(R.string.text_filter_title));
        if(isTablet){
            textviewGeneralFilter.setText(getString(R.string.general_filte_title));
        }
        else {
            textviewGeneralFilter.setText(getString(R.string.back));
        }
    }
    @Override
    public void clickedProject() {
        doneButtonClicked("this");
    }
    public static void clickOutSideSpinner(View view)
    {
        // Configure touch listener for all views except edittext,Button and Spinner
        if (!(view instanceof EditText)
                &&!(view instanceof Button)
                &&!(view instanceof Spinner))
        {
            view.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    //here you close your dialog spinner

                    return false;
                }
            });
        }

        //runs through all the children views .
        if (view instanceof ViewGroup)
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                View innerView = ((ViewGroup) view).getChildAt(i);
                //closeSlidingDrawerOnTouch(innerView);
            }
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
}