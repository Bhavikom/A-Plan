package de.smacsoftwares.aplanapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import de.smacsoftwares.aplanapp.Model.FolderDataset;
import de.smacsoftwares.aplanapp.Model.GeneralFilterDataSet;
import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.activity.HomeActivity;
import de.smacsoftwares.aplanapp.util.DatabaseHandler;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.InterfaceProjectClicked;
import de.smacsoftwares.aplanapp.util.InterfaceforSelectFilter;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by SSOFT4 on 7/18/2016.
 */
public class FragmentFilterUserDefined extends Fragment implements View.OnClickListener,InterfaceforSelectFilter,
        InterfaceProjectClicked {

    PreferencesHelper preferences;
    DatabaseHandler dbHandler;

    boolean isTablet;
    String comefrom="this";
    boolean isTextChanged=false;
    boolean isTimetoChangeText=false;
    Context context;
    RelativeLayout relativeMain;
    TextView textview1, textview2, textview3, textview4, textview5, textview6,
            textview7, textview8, textview9, textview10, textviewDone,textviewGeneralFilter;
    TextView textViewNumber,textViewText,textViewTitle;
    ImageView imgDeleteFilter,imgBack;
    EditText edit1, edit2, edit3, edit4, edit5, edit6, edit7, edit8, edit9, edit10;
    EditText edit1Number, edit2Number, edit3Number, edit4Number, edit5Number, edit6Number, edit7Number, edit8Number,
            edit9Number, edit10Number;
    ToggleButton btnswitch;
    View rootView;
    String strText1 = "", strText2 = "", strText3 = "", strText4 = "", strText5 = "", strText6 = "", strText7 = "",
            strText8 = "", strText9 = "", strText10 = "";
    String strNumber1 = "", strNumber2 = "", strNumber3 = "", strNumber4 = "", strNumber5 = "", strNumber6 = "", strNumber7 = "",
            strNumber8 = "", strNumber9 = "", strNumber10 = "";
    ArrayList<GeneralFilterDataSet> arraylistgetedFromDatabase = new ArrayList<>();
    ArrayList<FolderDataset> arraylistgetedFromDatabase2 = new ArrayList<>();
    ArrayList<String> arraylistValue = new ArrayList<>();
    LinearLayout linearBack;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_defined_filter, container, false);
        //MyApplication.component(getActivity()).inject(this);
        dbHandler = new DatabaseHandler(getActivity());
        preferences = new PreferencesHelper(getActivity());
        initControl();
        return rootView;
    }
    //    this method intialize control and class all initial work
    public void initControl() {
        isTablet = getResources().getBoolean(R.bool.isTablet);
        GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);

        linearBack = (LinearLayout)rootView.findViewById(R.id.linearback);
        linearBack.setOnClickListener(this);
        relativeMain = (RelativeLayout)rootView.findViewById(R.id.relative_userdefined_main);
        GlobalClass.setupTypeface(relativeMain, GlobalClass.fontLight);
        imgDeleteFilter=(ImageView) rootView.findViewById(R.id.img_delete);
        imgBack = (ImageView)rootView.findViewById(R.id.imgback);
        textview1 = (TextView) rootView.findViewById(R.id.textview1);
        textview2 = (TextView) rootView.findViewById(R.id.textview2);
        textview3 = (TextView) rootView.findViewById(R.id.textview3);
        textview4 = (TextView) rootView.findViewById(R.id.textview4);
        textview5 = (TextView) rootView.findViewById(R.id.textview5);
        textview6 = (TextView) rootView.findViewById(R.id.textview6);
        textview7 = (TextView) rootView.findViewById(R.id.textview7);
        textview8 = (TextView) rootView.findViewById(R.id.textview8);
        textview9 = (TextView) rootView.findViewById(R.id.textview9);
        textview10 = (TextView) rootView.findViewById(R.id.textview10);

        textViewNumber = (TextView) rootView.findViewById(R.id.textview_lable_number);
        textViewText = (TextView) rootView.findViewById(R.id.textview_lable_text);
        textViewTitle = (TextView) rootView.findViewById(R.id.textview_lable_title);
        textViewTitle.setTypeface(GlobalClass.fontBold);

        edit1 = (EditText) rootView.findViewById(R.id.edittext1);
        edit2 = (EditText) rootView.findViewById(R.id.edittext2);
        edit3 = (EditText) rootView.findViewById(R.id.edittext3);
        edit4 = (EditText) rootView.findViewById(R.id.edittext4);
        edit5 = (EditText) rootView.findViewById(R.id.edittext5);
        edit6 = (EditText) rootView.findViewById(R.id.edittext6);
        edit7 = (EditText) rootView.findViewById(R.id.edittext7);
        edit8 = (EditText) rootView.findViewById(R.id.edittext8);
        edit9 = (EditText) rootView.findViewById(R.id.edittext9);
        edit10 = (EditText) rootView.findViewById(R.id.edittext10);

        edit1Number = (EditText) rootView.findViewById(R.id.edittext1Number);
        edit2Number = (EditText) rootView.findViewById(R.id.edittext2Number);
        edit3Number = (EditText) rootView.findViewById(R.id.edittext3Number);
        edit4Number = (EditText) rootView.findViewById(R.id.edittext4Number);
        edit5Number = (EditText) rootView.findViewById(R.id.edittext5Number);
        edit6Number = (EditText) rootView.findViewById(R.id.edittext6Number);
        edit7Number = (EditText) rootView.findViewById(R.id.edittext7Number);
        edit8Number = (EditText) rootView.findViewById(R.id.edittext8Number);
        edit9Number = (EditText) rootView.findViewById(R.id.edittext9Number);
        edit10Number = (EditText) rootView.findViewById(R.id.edittext10Number);

        editTextTextChange(edit1);
        editTextTextChange(edit2);
        editTextTextChange(edit3);
        editTextTextChange(edit4);
        editTextTextChange(edit5);
        editTextTextChange(edit6);
        editTextTextChange(edit7);
        editTextTextChange(edit8);
        editTextTextChange(edit9);
        editTextTextChange(edit10);
        editTextTextChange(edit1Number);
        editTextTextChange(edit2Number);
        editTextTextChange(edit3Number);
        editTextTextChange(edit4Number);
        editTextTextChange(edit5Number);
        editTextTextChange(edit6Number);
        editTextTextChange(edit7Number);
        editTextTextChange(edit8Number);
        editTextTextChange(edit9Number);
        editTextTextChange(edit10Number);



        textviewDone = (TextView)rootView.findViewById(R.id.textview_done);
        textviewDone.setOnClickListener(this);
        textviewDone.setVisibility(View.GONE);
        textviewGeneralFilter = (TextView)rootView.findViewById(R.id.textview_generalfilter);
        textviewGeneralFilter.setOnTouchListener(new CustomTouchListener());
        textviewGeneralFilter.setOnClickListener(this);
        btnswitch = (ToggleButton) rootView.findViewById(R.id.switchnumber);

        if(preferences.getUserDefinedType().equalsIgnoreCase("number")){
            showNumbers();
            btnswitch.setChecked(true);
        }
        else {
            showText();
            btnswitch.setChecked(false);
        }

        imgDeleteFilter.setOnClickListener(this);
        btnswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showNumbers();
                    preferences.saveUserDefinedType("number");
                } else {
                    showText();
                    preferences.saveUserDefinedType("text");
                }

            }
        });
        getValueFromDatabaseandshowOnScreen();
        isTimetoChangeText=true;
        setLanguage();

    }
//    getting previousely store data from dataabase and showing on screen
    private void getValueFromDatabaseandshowOnScreen(){

        /*if (dbHandler.getCursorCountFilterForAllFilter(preferences.getUserID(),preferences.getProfileId()) > 0) {
            arraylistgetedFromDatabase = dbHandler.getGeneralFilterData(getString(R.string.UserDefinedFilter_text), preferences.getUserID(),preferences.getProfileId());
        }*/
        if(dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(),getString(R.string.UserDefinedFilter_text),preferences.getLanguage(),preferences.getUserID()) > 0){
            arraylistgetedFromDatabase = dbHandler.getAllGeneralFilterData(preferences.getProfileId(),getString(R.string.UserDefinedFilter_text),preferences.getLanguage(),preferences.getUserID());
        }
        if (arraylistgetedFromDatabase.size() > 0) {
            for (int i = 0; i < arraylistgetedFromDatabase.size(); i++) {
                if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_text1_string))){
                    edit1.setText(arraylistgetedFromDatabase.get(i).getValue());
                }


                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_text2_string))){
                    edit2.setText(arraylistgetedFromDatabase.get(i).getValue());
                }

                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_text3_string))){
                    edit3.setText(arraylistgetedFromDatabase.get(i).getValue());
                }

                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_text4_string))){
                    edit4.setText(arraylistgetedFromDatabase.get(i).getValue());
                }

                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_text5_string))){
                    edit5.setText(arraylistgetedFromDatabase.get(i).getValue());
                }

                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_text6_string))){
                    edit6.setText(arraylistgetedFromDatabase.get(i).getValue());
                }

                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_text7_string))){
                    edit7.setText(arraylistgetedFromDatabase.get(i).getValue());
                }

                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_text8_string))){
                    edit8.setText(arraylistgetedFromDatabase.get(i).getValue());
                }


                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_text9_string))){
                    edit9.setText(arraylistgetedFromDatabase.get(i).getValue());
                }

                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_text10_string))){
                    edit10.setText(arraylistgetedFromDatabase.get(i).getValue());
                }
            }


        }
        /////////////////////////////////////////////////////////////////////
        if(dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(),getString(R.string.UserDefindedFilterForNumber),preferences.getLanguage(),preferences.getUserID()) > 0){
            arraylistgetedFromDatabase = dbHandler.getAllGeneralFilterData(preferences.getProfileId(),getString(R.string.UserDefindedFilterForNumber),preferences.getLanguage(),preferences.getUserID());
        }
        if (arraylistgetedFromDatabase.size() > 0) {
            for (int i = 0; i < arraylistgetedFromDatabase.size(); i++) {
                if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_number1_string))){
                    edit1Number.setText(arraylistgetedFromDatabase.get(i).getValue());
                }


                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_number2_string))){
                    edit2Number.setText(arraylistgetedFromDatabase.get(i).getValue());
                }

                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_number3_string))){
                    edit3Number.setText(arraylistgetedFromDatabase.get(i).getValue());
                }

                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_number4_string))){
                    edit4Number.setText(arraylistgetedFromDatabase.get(i).getValue());
                }

                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_number5_string))){
                    edit5Number.setText(arraylistgetedFromDatabase.get(i).getValue());
                }

                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_number6_string))){
                    edit6Number.setText(arraylistgetedFromDatabase.get(i).getValue());
                }

                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_number7_string))){
                    edit7Number.setText(arraylistgetedFromDatabase.get(i).getValue());
                }

                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_number8_string))){
                    edit8Number.setText(arraylistgetedFromDatabase.get(i).getValue());
                }


                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_number9_string))){
                    edit9Number.setText(arraylistgetedFromDatabase.get(i).getValue());
                }

                else if(arraylistgetedFromDatabase.get(i).getKey().equalsIgnoreCase(getString(R.string.key_number10_string))){
                    edit10Number.setText(arraylistgetedFromDatabase.get(i).getValue());
                }
            }
        }
    }
    public void doneButtonClicked(String comeFrom){
        if(comeFrom.equalsIgnoreCase("this")){
            preferences.saveIsProgressShow("yes");
        }
        else {
            preferences.saveIsProgressShow("no");
        }
       /* InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm.isAcceptingText()) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
*/
        dbHandler.deleteGeneralFilterData(preferences.getProfileId(),getString(R.string.UserDefinedFilter_text)
                ,preferences.getLanguage(),preferences.getUserID());
        dbHandler.deleteGeneralFilterData(preferences.getProfileId(),getString(R.string.UserDefindedFilterForNumber)
                ,preferences.getLanguage(),preferences.getUserID());
        if(!getEdittextValue(edit1).equalsIgnoreCase("") && !getEdittextValue(edit1).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_text1_string),
                    getEdittextValue(edit1),preferences.getLanguage(),getString(R.string.UserDefinedFilter_text),
                    preferences.getUserID(),"");
        }
        if(!getEdittextValue(edit2).equalsIgnoreCase("") && !getEdittextValue(edit2).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_text2_string),
                    getEdittextValue(edit2),preferences.getLanguage(),getString(R.string.UserDefinedFilter_text),
                    preferences.getUserID(),"");
        }
        if(!getEdittextValue(edit3).equalsIgnoreCase("") && !getEdittextValue(edit3).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_text3_string),
                    getEdittextValue(edit3),preferences.getLanguage(),getString(R.string.UserDefinedFilter_text),
                    preferences.getUserID(),"");
        }
        if(!getEdittextValue(edit4).equalsIgnoreCase("") && !getEdittextValue(edit4).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_text4_string),
                    getEdittextValue(edit4),preferences.getLanguage(),getString(R.string.UserDefinedFilter_text),
                    preferences.getUserID(),"");
        }
        if(!getEdittextValue(edit5).equalsIgnoreCase("") && !getEdittextValue(edit5).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_text5_string),
                    getEdittextValue(edit5),preferences.getLanguage(),getString(R.string.UserDefinedFilter_text),
                    preferences.getUserID(),"");
        }
        if(!getEdittextValue(edit6).equalsIgnoreCase("") && !getEdittextValue(edit6).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_text6_string),
                    getEdittextValue(edit6),preferences.getLanguage(),getString(R.string.UserDefinedFilter_text),
                    preferences.getUserID(),"");
        }if(!getEdittextValue(edit7).equalsIgnoreCase("") && !getEdittextValue(edit7).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_text7_string),
                    getEdittextValue(edit7),preferences.getLanguage(),getString(R.string.UserDefinedFilter_text),
                    preferences.getUserID(),"");
        }if(!getEdittextValue(edit8).equalsIgnoreCase("") && !getEdittextValue(edit8).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_text8_string),
                    getEdittextValue(edit8),preferences.getLanguage(),getString(R.string.UserDefinedFilter_text),
                    preferences.getUserID(),"");
        }
        if(!getEdittextValue(edit9).equalsIgnoreCase("") && !getEdittextValue(edit9).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_text9_string),
                    getEdittextValue(edit9),preferences.getLanguage(),getString(R.string.UserDefinedFilter_text),
                    preferences.getUserID(),"");
        }
        if(!getEdittextValue(edit10).equalsIgnoreCase("") && !getEdittextValue(edit10).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_text10_string),
                    getEdittextValue(edit10),preferences.getLanguage(),getString(R.string.UserDefinedFilter_text),
                    preferences.getUserID(),"");


        }if(!getEdittextValue(edit1Number).equalsIgnoreCase("") && !getEdittextValue(edit1Number).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_number1_string),
                    getEdittextValue(edit1Number),preferences.getLanguage(),getString(R.string.UserDefindedFilterForNumber),
                    preferences.getUserID(),"");
        }if(!getEdittextValue(edit2Number).equalsIgnoreCase("") && !getEdittextValue(edit2Number).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_number2_string),
                    getEdittextValue(edit2Number),preferences.getLanguage(),getString(R.string.UserDefindedFilterForNumber),
                    preferences.getUserID(),"");
        }
        if(!getEdittextValue(edit3Number).equalsIgnoreCase("") && !getEdittextValue(edit3Number).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_number3_string),
                    getEdittextValue(edit3Number),preferences.getLanguage(),getString(R.string.UserDefindedFilterForNumber),
                    preferences.getUserID(),"");
        }
        if(!getEdittextValue(edit4Number).equalsIgnoreCase("") && !getEdittextValue(edit4Number).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_number4_string),
                    getEdittextValue(edit4Number),preferences.getLanguage(),getString(R.string.UserDefindedFilterForNumber),
                    preferences.getUserID(),"");
        }
        if(!getEdittextValue(edit5Number).equalsIgnoreCase("") && !getEdittextValue(edit5Number).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_number5_string),
                    getEdittextValue(edit5Number),preferences.getLanguage(),getString(R.string.UserDefindedFilterForNumber),
                    preferences.getUserID(),"");
        }
        if(!getEdittextValue(edit6Number).equalsIgnoreCase("") && !getEdittextValue(edit6Number).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_number6_string),
                    getEdittextValue(edit6Number),preferences.getLanguage(),getString(R.string.UserDefindedFilterForNumber),
                    preferences.getUserID(),"");
        }
        if(!getEdittextValue(edit7Number).equalsIgnoreCase("") && !getEdittextValue(edit7Number).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_number7_string),
                    getEdittextValue(edit7Number),preferences.getLanguage(),getString(R.string.UserDefindedFilterForNumber),
                    preferences.getUserID(),"");
        }
        if(!getEdittextValue(edit8Number).equalsIgnoreCase("") && !getEdittextValue(edit8Number).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_number8_string),
                    getEdittextValue(edit8Number),preferences.getLanguage(),getString(R.string.UserDefindedFilterForNumber),
                    preferences.getUserID(),"");
        }
        if(!getEdittextValue(edit9Number).equalsIgnoreCase("") && !getEdittextValue(edit9Number).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_number9_string),
                    getEdittextValue(edit9Number),preferences.getLanguage(),getString(R.string.UserDefindedFilterForNumber),
                    preferences.getUserID(),"");
        }
        if(!getEdittextValue(edit10Number).equalsIgnoreCase("") && !getEdittextValue(edit10Number).equalsIgnoreCase("null")){
            dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.key_number10_string),
                    getEdittextValue(edit10Number),preferences.getLanguage(),getString(R.string.UserDefindedFilterForNumber),
                    preferences.getUserID(),"");
        }

        strText1 = getString(R.string.key_text1_string)+"-"+  getEdittextValue(edit1);
        strText2 = getString(R.string.key_text2_string)+"-"+  getEdittextValue(edit2);
        strText3 = getString(R.string.key_text3_string)+"-" + getEdittextValue(edit3);
        strText4 = getString(R.string.key_text4_string)+"-" + getEdittextValue(edit4);
        strText5 = getString(R.string.key_text5_string)+"-" + getEdittextValue(edit5);
        strText6 = getString(R.string.key_text6_string)+"-" + getEdittextValue(edit6);
        strText7 = getString(R.string.key_text7_string)+"-" + getEdittextValue(edit7);
        strText8 = getString(R.string.key_text8_string)+"-" + getEdittextValue(edit8);
        strText9 = getString(R.string.key_text9_string)+"-" + getEdittextValue(edit9);
        strText10 = getString(R.string.key_text10_string)+"-" + getEdittextValue(edit10);
        String finalCommaseperated = strText1 + "," + strText2 + "," + strText3 + "," + strText4 + "," + strText5 + "," + strText6 + "," +
                strText7 + "," + strText8 + "," + strText9 + "," + strText10;
        //dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.UserDefinedFilter_text));
        if(getEdittextValue(edit1).equalsIgnoreCase("null") && getEdittextValue(edit2).equalsIgnoreCase("null")
                && getEdittextValue(edit3).equalsIgnoreCase("null") && getEdittextValue(edit4).equalsIgnoreCase("null")
                && getEdittextValue(edit5).equalsIgnoreCase("null") && getEdittextValue(edit6).equalsIgnoreCase("null")
                && getEdittextValue(edit7).equalsIgnoreCase("null") && getEdittextValue(edit8).equalsIgnoreCase("null")
                && getEdittextValue(edit9).equalsIgnoreCase("null") && getEdittextValue(edit10).equalsIgnoreCase("null")){

        }
        else if(preferences.getLastUserDefinedFilterString().equalsIgnoreCase("")){
            // fire filter here
            preferences.saveLastUserDefinedFilterString(finalCommaseperated);
            //dbHandler.addGeneralFilterInfo(getString(R.string.UserDefinedFilter_text), finalCommaseperated,
                    //"", "", preferences.getUserID(),preferences.getProfileId());
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
            if(preferences.getLastUserDefinedFilterString().equalsIgnoreCase(finalCommaseperated)){
                // don't fire filter here
                preferences.saveLastUserDefinedFilterString(finalCommaseperated);
                //dbHandler.addGeneralFilterInfo(getString(R.string.UserDefinedFilter_text), finalCommaseperated,
                        //"", "", preferences.getUserID(),preferences.getProfileId());
            }
            else {
                // fire filter here
                preferences.saveLastUserDefinedFilterString(finalCommaseperated);
                //dbHandler.addGeneralFilterInfo(getString(R.string.UserDefinedFilter_text), finalCommaseperated,
                        //"", "", preferences.getUserID(),preferences.getProfileId());
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
        }
        /*if(isTextChanged){
            preferences.saveCurrentFiredFilter("6");
        }

        if(getEdittextValue(edit1).equalsIgnoreCase("null") && getEdittextValue(edit2).equalsIgnoreCase("null")
                && getEdittextValue(edit3).equalsIgnoreCase("null") && getEdittextValue(edit4).equalsIgnoreCase("null")
                && getEdittextValue(edit5).equalsIgnoreCase("null") && getEdittextValue(edit6).equalsIgnoreCase("null")
                && getEdittextValue(edit7).equalsIgnoreCase("null") && getEdittextValue(edit8).equalsIgnoreCase("null")
                && getEdittextValue(edit9).equalsIgnoreCase("null") && getEdittextValue(edit10).equalsIgnoreCase("null")){

        }
        else {
            dbHandler.addGeneralFilterInfo(getString(R.string.UserDefinedFilter_text), finalCommaseperated, "", "", preferences.getUserID(),preferences.getProfileId());
        }*/
        strNumber1 = getString(R.string.key_number1_string) +"-"+ getEdittextValue(edit1Number);
        strNumber2 = getString(R.string.key_number2_string) +"-"+ getEdittextValue(edit2Number);
        strNumber3 = getString(R.string.key_number3_string) +"-"+ getEdittextValue(edit3Number);
        strNumber4 = getString(R.string.key_number4_string) +"-"+ getEdittextValue(edit4Number);
        strNumber5 = getString(R.string.key_number5_string) +"-"+ getEdittextValue(edit5Number);
        strNumber6 = getString(R.string.key_number6_string) +"-"+ getEdittextValue(edit6Number);
        strNumber7 = getString(R.string.key_number7_string) +"-"+ getEdittextValue(edit7Number);
        strNumber8 = getString(R.string.key_number8_string) +"-"+ getEdittextValue(edit8Number);
        strNumber9 = getString(R.string.key_number9_string) +"-"+ getEdittextValue(edit9Number);
        strNumber10 = getString(R.string.key_number10_string) +"-"+ getEdittextValue(edit10Number);
        String finalCommaseperated2 = strNumber1 + "," + strNumber2 + "," + strNumber3 + "," + strNumber4 + "," + strNumber5 + "," + strNumber6 + "," +
                strNumber7 + "," + strNumber8 + "," + strNumber9 + "," + strNumber10;
        //dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.UserDefindedFilterForNumber));

        if(getEdittextValue(edit1Number).equalsIgnoreCase("null") && getEdittextValue(edit2Number).equalsIgnoreCase("null")
                && getEdittextValue(edit3Number).equalsIgnoreCase("null") && getEdittextValue(edit4Number).equalsIgnoreCase("null")
                && getEdittextValue(edit5Number).equalsIgnoreCase("null") && getEdittextValue(edit6Number).equalsIgnoreCase("null")
                && getEdittextValue(edit7Number).equalsIgnoreCase("null") && getEdittextValue(edit8Number).equalsIgnoreCase("null")
                && getEdittextValue(edit9Number).equalsIgnoreCase("null") && getEdittextValue(edit10Number).equalsIgnoreCase("null")){

        }
        else if(preferences.getLastUserDefinedFilterString2().equalsIgnoreCase("")){
            // fire filter here
            preferences.saveLastUserDefinedFilterString2(finalCommaseperated2);
            //dbHandler.addGeneralFilterInfo(getString(R.string.UserDefindedFilterForNumber), finalCommaseperated2,
                    //"", "", preferences.getUserID(),preferences.getProfileId());
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
            if(preferences.getLastUserDefinedFilterString2().equalsIgnoreCase(finalCommaseperated2)){
                // don't fire filter here
                preferences.saveLastUserDefinedFilterString(finalCommaseperated2);
                //dbHandler.addGeneralFilterInfo(getString(R.string.UserDefindedFilterForNumber), finalCommaseperated2,
                        //"", "", preferences.getUserID(),preferences.getProfileId());
            }
            else {
                // fire filter here
                preferences.saveLastUserDefinedFilterString(finalCommaseperated2);
                //dbHandler.addGeneralFilterInfo(getString(R.string.UserDefindedFilterForNumber), finalCommaseperated2,
                        //"", "", preferences.getUserID(),preferences.getProfileId());
                if(preferences.getCurrentFiredFilter().equalsIgnoreCase("1") ||
                        preferences.getCurrentFiredFilter().equalsIgnoreCase("2") ||
                        preferences.getCurrentFiredFilter().equalsIgnoreCase("3") ||
                        preferences.getCurrentFiredFilter().equalsIgnoreCase("4") ||
                        preferences.getCurrentFiredFilter().equalsIgnoreCase("") ||
                        preferences.getCurrentFiredFilter().equalsIgnoreCase("6")){
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
        }

        /*if(getEdittextValue(edit1Number).equalsIgnoreCase("null") && getEdittextValue(edit2Number).equalsIgnoreCase("null")
                && getEdittextValue(edit3Number).equalsIgnoreCase("null") && getEdittextValue(edit4Number).equalsIgnoreCase("null")
                && getEdittextValue(edit5Number).equalsIgnoreCase("null") && getEdittextValue(edit6Number).equalsIgnoreCase("null")
                && getEdittextValue(edit7Number).equalsIgnoreCase("null") && getEdittextValue(edit8Number).equalsIgnoreCase("null")
                && getEdittextValue(edit9Number).equalsIgnoreCase("null") && getEdittextValue(edit10Number).equalsIgnoreCase("null")){
        }
        else {

            dbHandler.addGeneralFilterInfo(getString(R.string.UserDefindedFilterForNumber), finalCommaseperated2, "", "", preferences.getUserID(),preferences.getProfileId());
        }*/
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview_generalfilter:
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
            case R.id.linearback:
                comefrom="this";
                doneButtonClicked(comefrom);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentSelectFilter fragment = new FragmentSelectFilter();
                transaction.replace(R.id.select_filter_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.img_delete:
                dbHandler.updateGeneralFilterToDelete(preferences.getProfileId(),getString(R.string.UserDefinedFilter_text),"",preferences.getLanguage(),preferences.getUserID());
                dbHandler.updateGeneralFilterToDelete(preferences.getProfileId(),getString(R.string.UserDefindedFilterForNumber),"",preferences.getLanguage(),preferences.getUserID());
                preferences.saveCurrentFiredFilter("6");
                resetEdittext();
                break;
        }
    }
//    reset all the edittext
    public void resetEdittext(){
        edit1.setText("");
        edit2.setText("");
        edit3.setText("");
        edit4.setText("");
        edit5.setText("");
        edit6.setText("");
        edit7.setText("");
        edit8.setText("");
        edit9.setText("");
        edit10.setText("");

        edit1Number.setText("");
        edit2Number.setText("");
        edit3Number.setText("");
        edit4Number.setText("");
        edit5Number.setText("");
        edit6Number.setText("");
        edit7Number.setText("");
        edit8Number.setText("");
        edit9Number.setText("");
        edit10Number.setText("");
    }
//  get edittext value dynamically
    public String getEdittextValue(EditText edittext) {
        String value = "";
        if (edittext.getText().toString().equalsIgnoreCase("")) {
            value = "null";
        } else {
            value = edittext.getText().toString();
        }
        return value;
    }
//  set text in edittext dynamically
    public String setTextinEdittext(String text) {
        String value = "";
        if (text.equalsIgnoreCase("null") || text.equalsIgnoreCase("")) {
            value = "";
        } else {
            value = text;
        }
        return value;
    }
//    hide edittext based on condition
    public void showNumbers()
    {
        textview1.setText(getString(R.string.key_number1));
        textview2.setText(getString(R.string.key_number2));
        textview3.setText(getString(R.string.key_number3));
        textview4.setText(getString(R.string.key_number4));
        textview5.setText(getString(R.string.key_number5));
        textview6.setText(getString(R.string.key_number6));
        textview7.setText(getString(R.string.key_number7));
        textview8.setText(getString(R.string.key_number8));
        textview9.setText(getString(R.string.key_number9));
        textview10.setText(getString(R.string.key_number10));

        edit1.setVisibility(View.GONE);
        edit2.setVisibility(View.GONE);
        edit3.setVisibility(View.GONE);
        edit4.setVisibility(View.GONE);
        edit5.setVisibility(View.GONE);
        edit6.setVisibility(View.GONE);
        edit7.setVisibility(View.GONE);
        edit8.setVisibility(View.GONE);
        edit9.setVisibility(View.GONE);
        edit10.setVisibility(View.GONE);

        edit1Number.setVisibility(View.VISIBLE);
        edit2Number.setVisibility(View.VISIBLE);
        edit3Number.setVisibility(View.VISIBLE);
        edit4Number.setVisibility(View.VISIBLE);
        edit5Number.setVisibility(View.VISIBLE);
        edit6Number.setVisibility(View.VISIBLE);
        edit7Number.setVisibility(View.VISIBLE);
        edit8Number.setVisibility(View.VISIBLE);
        edit9Number.setVisibility(View.VISIBLE);
        edit10Number.setVisibility(View.VISIBLE);
    }
//    hide number textview based on condition
    public void showText()
    {
        textview1.setText(getString(R.string.key_text1));
        textview2.setText(getString(R.string.key_text2));
        textview3.setText(getString(R.string.key_text3));
        textview4.setText(getString(R.string.key_text4));
        textview5.setText(getString(R.string.key_text5));
        textview6.setText(getString(R.string.key_text6));
        textview7.setText(getString(R.string.key_text7));
        textview8.setText(getString(R.string.key_text8));
        textview9.setText(getString(R.string.key_text9));
        textview10.setText(getString(R.string.key_text10));

        edit1.setVisibility(View.VISIBLE);
        edit2.setVisibility(View.VISIBLE);
        edit3.setVisibility(View.VISIBLE);
        edit4.setVisibility(View.VISIBLE);
        edit5.setVisibility(View.VISIBLE);
        edit6.setVisibility(View.VISIBLE);
        edit7.setVisibility(View.VISIBLE);
        edit8.setVisibility(View.VISIBLE);
        edit9.setVisibility(View.VISIBLE);
        edit10.setVisibility(View.VISIBLE);

        edit1Number.setVisibility(View.GONE);
        edit2Number.setVisibility(View.GONE);
        edit3Number.setVisibility(View.GONE);
        edit4Number.setVisibility(View.GONE);
        edit5Number.setVisibility(View.GONE);
        edit6Number.setVisibility(View.GONE);
        edit7Number.setVisibility(View.GONE);
        edit8Number.setVisibility(View.GONE);
        edit9Number.setVisibility(View.GONE);
        edit10Number.setVisibility(View.GONE);
    }
    @Override
    public void callSelectFilterFragment() {
    }
    @Override
    public void callUpdateFilter()
    {
        isTimetoChangeText=false;
        resetEdittext();
        //initControl();
        setLanguage();
        isTimetoChangeText=false;
        arraylistgetedFromDatabase = new ArrayList<>();
        getValueFromDatabaseandshowOnScreen();
        isTimetoChangeText=true;
    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        context = activity;
        ((HomeActivity)context).interfaceforSelectFilter = this;
        ((HomeActivity)context).interfaceProjectClicked = this;
        //((HomeActivity)context).interfaceChangeLanguageUserDefinedFilter = this;

    }
//    edittext change evernt dynamically
    public void editTextTextChange(EditText edittext){
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(isTimetoChangeText){
                    isTextChanged=true;
                    doneButtonClicked(comefrom);
                    //GlobalClass.showToast(getActivity()," text Changed : ");
                }
            }
        });
    }
    public void setLanguage(){
        textViewNumber.setText(getString(R.string.number));
        textViewText.setText(getString(R.string.text_user_defined));
        textViewTitle.setText(getString(R.string.user_defined_filter_title));
        if(isTablet){
            textviewGeneralFilter.setText(getString(R.string.general_filte_title));
        }
        else {
            textviewGeneralFilter.setText(getString(R.string.back));
        }
        if(preferences.getUserDefinedType().equalsIgnoreCase("number")){
            showNumbers();
            btnswitch.setChecked(true);
        }
        else {
            showText();
            btnswitch.setChecked(false);
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
    @Override
    public void clickedProject() {
        doneButtonClicked("this");
    }
}