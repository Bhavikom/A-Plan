package de.smacsoftwares.aplanapp.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.Model.GeneralFilterDataSet;
import de.smacsoftwares.aplanapp.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;

import de.smacsoftwares.aplanapp.util.CustomTypefaceSpan;
import de.smacsoftwares.aplanapp.util.DatabaseHandler;
import de.smacsoftwares.aplanapp.util.GenericHelper;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.LogApp;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.OnBackPressListener;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;
import okhttp3.Headers;
import retrofit2.Call;

public class FragmentProfile extends Fragment implements View.OnClickListener
{
    Dialog dialog;
    Activity activity;
    InterfaceProfileChanged interfaceProfileChanged;
    InterfaceProfileChanged2 interfaceProfileChanged2;
    InterfaceChangeLanguageHome interfaceChangeLanguageHome;
    private Menu menu;
    PreferencesHelper preferences;
    DatabaseHandler dbHandler;
    TextView textviewName,textviewEmail,textviewMobile,textviewDepartment,textviewChangePass,textViewLoginasLicence;
    RelativeLayout relativeMain;
    private View rootView;
    private OnFragmentInteractionListener mListener;
    ImageView imgEnglish,imgGermany;
    String strLanguage="";
    public FragmentProfile() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            //MyApplication.component(getActivity()).inject(this);
            getActivity().invalidateOptionsMenu();
            setHasOptionsMenu(true);
            //super.initializeFragment(rootView);
            preferences = new PreferencesHelper(getActivity());
            dbHandler = new DatabaseHandler(getActivity());

            GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);

            strLanguage=preferences.getLanguage();
            relativeMain = (RelativeLayout)rootView.findViewById(R.id.relative_profile_main);
            GlobalClass.setupTypeface(relativeMain, GlobalClass.fontLight);
            imgEnglish=(ImageView)rootView.findViewById(R.id.img_english);
            imgGermany=(ImageView)rootView.findViewById(R.id.img_germany);
            imgEnglish.setOnClickListener(this);
            imgGermany.setOnClickListener(this);
            textViewLoginasLicence=(TextView)rootView.findViewById(R.id.textview_login_as_licence);
            if(preferences.hasLicence()){
                textViewLoginasLicence.setVisibility(View.GONE);
            }else {
                textViewLoginasLicence.setVisibility(View.GONE);
            }
            textViewLoginasLicence.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //singOutDialog();
                }
            });
            textviewName=(TextView)rootView.findViewById(R.id.textviewname);
            textviewEmail=(TextView)rootView.findViewById(R.id.textviewemail);
            textviewMobile=(TextView)rootView.findViewById(R.id.textviewmobile);
            textviewDepartment=(TextView)rootView.findViewById(R.id.textviewdepartment);
            textviewChangePass=(TextView)rootView.findViewById(R.id.textviewchangepass);
            textviewChangePass.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    FragmentChangePassword fragment = new FragmentChangePassword();
                    //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    //fragment.setArguments(args);
                    transaction.replace(R.id.content_frame, fragment);
                    transaction.addToBackStack("Profile");
                    transaction.commit();

                    //FragmentManager fragmentManager = getFragmentManager();
                    //fragmentManager.beginTransaction().replace(R.id.container, new FragmentChangePassword()).addToBackStack("abc").commit();
                }
            });
            textviewName.setText(preferences.getUserName());
            //textviewName.setText("User");
            textviewEmail.setText(preferences.getEmail());
            setImageFromLanguageSeted();
            setLanguage(preferences.getLanguage());
            //textviewMobile.setText(preferences.getDomain());
            //textviewDepartment.setText(preferences.getUserName());
        //View content = inflater.inflate(R.layout.fragment_profile, container, false);
        return rootView;
    }
    @Override
    public void onClick(View v) {
        if(v==imgEnglish){
            if(preferences.getLanguage().equalsIgnoreCase(getString(R.string.language_germany))){
                /* you want to change language to english that's why call get profile service here */
                strLanguage=getString(R.string.language_english);
                preferences.saveLanguage(getString(R.string.language_english));
                if (GlobalClass.isNetworkAvailable(getActivity())) {
                    //getProfiles();
                    getProfiles();
                } else {
                    //GlobalClass.showToastInternet(getActivity());
                    GlobalClass.showCustomDialogInternet(getActivity());
                }

            }
        }
        else if(v==imgGermany){
            if(preferences.getLanguage().equalsIgnoreCase(getString(R.string.language_english))){
                /* you want to change language to germany that's why call get profile service here */
                strLanguage=getString(R.string.language_germany);
                preferences.saveLanguage(getString(R.string.language_germany));
                if (GlobalClass.isNetworkAvailable(getActivity())) {
                   // getProfiles();
                    getProfiles();
                } else {
                    //GlobalClass.showToastInternet(getActivity());
                    GlobalClass.showCustomDialogInternet(getActivity());
                }
            }
        }
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
                            //getProfiles();
                            getProfiles();
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
    public void getProfiles(){
        try {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());

            SpannableString ss=  new SpannableString(getString(R.string.please_wait));
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
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 0)
                        {
                            String attributName="";
                            //String ColumnsDetail="";
                            //GlobalClass.showToast(LoginMobileActivity.this," 333333 success ");
                            String strResponse = response.body();
                            JSONObject obj = (JSONObject) new JSONTokener(strResponse).nextValue();
                            String status = obj.getString("Status");
                            if (status.equalsIgnoreCase("0"))
                            {
                                JSONArray jsonArrayPayload = obj.getJSONArray("Payload");
                                if(jsonArrayPayload.length() > 0){
                                    dbHandler.deleteAlluserProfile();
                                    //dbHandler.deleteGeneralFilterDataWhileProfileChange(preferences.getProfileId(),getString(R.string.TextFilter_text),preferences.getLanguage(),preferences.getUserID());
                                    //dbHandler.deleteGeneralFilterDataWhileProfileChange(preferences.getProfileId(),getString(R.string.DateFilter_text),preferences.getLanguage(),preferences.getUserID());
                                    //dbHandler.deleteGeneralFilterDataWhileProfileChange(preferences.getProfileId(),getString(R.string.UserDefindedFilterForNumber),preferences.getLanguage(),preferences.getUserID());
                                    //dbHandler.deleteGeneralFilterDataWhileProfileChange(preferences.getProfileId(),getString(R.string.UserDefinedFilter_text),preferences.getLanguage(),preferences.getUserID());
                                    for (int i=0;i<jsonArrayPayload.length();i++){
                                        JSONObject objPayload = jsonArrayPayload.getJSONObject(i);

                                        //String ColumnsDetail = objSub.getString("ColsDetail");
                                        String ProfileId = objPayload.getString("Id");
                                        String Name = objPayload.getString("Name");
                                        String UserId = objPayload.getString("UserId");
                                        String ExpandedId="";
                                        //String ExpandedId = objSub.getString("ExpandedIds");

                                        JSONArray jsonArrayAttribute=objPayload.getJSONArray("Attributes");

                                        ArrayList<String> arraylistKey = new ArrayList<String>();
                                        ArrayList<String> arraylistValue = new ArrayList<String>();
                                        String attributeValue="";
                                        if(jsonArrayAttribute.length() > 0){
                                            for(int j=0;j<jsonArrayAttribute.length();j++){
                                                JSONObject objAttribute = jsonArrayAttribute.getJSONObject(j);
                                                attributName = objAttribute.getString("AttributeName");
                                                if(attributName.equalsIgnoreCase("RibbonFilter")) {
                                                    attributeValue = objAttribute.getString("AttributeValue");
                                                    arraylistKey.add(attributName);
                                                    arraylistValue.add(attributeValue);
                                                }
                                                else if(attributName.equalsIgnoreCase("GeneralFilter")){
                                                    attributeValue=objAttribute.getString("AttributeValue");
                                                    if(!attributeValue.equalsIgnoreCase("")){
                                                            JSONObject objGeneralFilter = (JSONObject) new JSONTokener(attributeValue).nextValue();
                                                            Iterator keys = objGeneralFilter.keys();
                                                            String key="";
                                                            String value="";
                                                            String type="";

                                                            while(keys.hasNext()){
                                                                key = (String) keys.next();
                                                                value=objGeneralFilter.getString(key);
                                                                if(key.equalsIgnoreCase(getString(R.string.folder_group_id))){
                                                                    type=getString(R.string.FolderFilter_text);
                                                                }
                                                                else if(key.equalsIgnoreCase(getString(R.string.key_ResourceId))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_ResourceGroupId))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_anyText))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_priFrom))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_priTO))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_level))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_indentyNo))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_Task))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_Client))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_Responsible))){
                                                                    type=getString(R.string.TextFilter_text);

                                                                }else if(key.equalsIgnoreCase(getString(R.string.key_text1_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_text2_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_text3_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_text4_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_text5_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_text6_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_text7_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_text8_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_text9_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_text10_string))){
                                                                    type=getString(R.string.UserDefinedFilter_text);

                                                                }
                                                                else if(key.equalsIgnoreCase(getString(R.string.key_number1_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_number2_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_number3_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_number4_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_number5_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_number6_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_number7_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_number8_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_number9_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_number10_string))){
                                                                    type=getString(R.string.UserDefindedFilterForNumber);

                                                                }
                                                                else if(key.equalsIgnoreCase(getString(R.string.key_criterion1_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_criterion1Type_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_criterion1FromDate_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_criterion1ToDate_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_criterion1TimeFrom_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_criterion1TimeTo_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_criterion1TimeUnit_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_criterion2_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_criterion2FromDate_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_criterion2ToDate_string)) ||
                                                                        key.equalsIgnoreCase(getString(R.string.key_criterion2Type_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_criterion2TimeFrom_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_criterion2TimeTo_string))||
                                                                        key.equalsIgnoreCase(getString(R.string.key_criterion2TimeUnit_string))){
                                                                    type=getString(R.string.DateFilter_text);

                                                                }
                                                                if(key.equalsIgnoreCase(getString(R.string.folder_group_id))){
                                                                    if (dbHandler.getCursorCountForFolderFilter(ProfileId, getString(R.string.FolderFilter_text), preferences.getLanguage(), preferences.getUserID()) > 0) {
                                                                        /* there is already folder filter is fired in local that's why just update here*/
                                                                        ArrayList<GeneralFilterDataSet> arraylistFolderlist = dbHandler.getAllGeneralFilterData(preferences.getProfileId(), getString(R.string.FolderFilter_text), preferences.getLanguage(),
                                                                                preferences.getUserID());
                                                                        if (arraylistFolderlist.size() > 0) {
                                                                            List<String> list = GlobalClass.commaSeparetedtoArraylist(arraylistFolderlist.get(0).getValueLocal());
                                                                            List<String> listServer = GlobalClass.commaSeparetedtoArraylist(value);
                                                                            Set<String> newSet = null;
                                                                            if(list.size() > 0){
                                                                                newSet = new HashSet<String>(list);
                                                                            }
                                                                            if(listServer.size() > 0){
                                                                                if(newSet != null){
                                                                                    newSet.addAll(listServer);
                                                                                }
                                                                            }
                                                                            List<String> newList = null;
                                                                            if(newSet != null) {
                                                                                newList = new ArrayList<String>(newSet);
                                                                            }
                                                                            String joined = TextUtils.join(",",newList);
                                                                                dbHandler.updateGeneralFilter(ProfileId,getString(R.string.folder_group_id),getString(R.string.FolderFilter_text),joined,preferences.getLanguage(),preferences.getUserID());


                                                                        }
                                                                    }
                                                                    else {
                                                                      /* no folder filter is fired that's why add new record here */
                                                                        dbHandler.addGeneralFilter(ProfileId,key,GlobalClass.removeCommaAttheEnd(value),preferences.getLanguage(),type,preferences.getUserID(),GlobalClass.removeCommaAttheEnd(value));
                                                                    }
                                                                }else {
                                                                    if(dbHandler.getCursorCountForGeneralFilterforProfileChange(ProfileId,preferences.getUserID(),preferences.getLanguage(),key) > 0){
                                                                         dbHandler.updateGeneralFilter(ProfileId,key,type,GlobalClass.removeCommaAttheEnd(value),preferences.getLanguage(),preferences.getUserID());
                                                                    }
                                                                    else {
                                                                        dbHandler.addGeneralFilter(ProfileId,key,value,preferences.getLanguage(),type,preferences.getUserID(),value);
                                                                    }

                                                                }

                                                            }
                                                        }
                                                    }
                                            }
                                        }
                                        String mAttributeKey= "";
                                        String mAttributeValue= "";
                                        if(arraylistValue.size() > 0){
                                            /*Gson gson = new Gson();
                                            mAttributeKey= gson.toJson(arraylistKey);
                                            mAttributeValue= gson.toJson(arraylistValue);*/
                                            mAttributeKey= TextUtils.join(",",arraylistKey);
                                            mAttributeValue= TextUtils.join(",",arraylistValue);
                                        }

                                        /* adding all profile to database */
                                        dbHandler.addAllProfileToDB(ProfileId, Name, attributName, preferences.getUserID(),
                                                ExpandedId, preferences.getUserIdTemp(), preferences.getLanguage(), getResources().getString(R.string.weekm),mAttributeKey,mAttributeValue,mAttributeKey,mAttributeValue);
                                        if(dbHandler.getCursorCountForRibbonFilter(ProfileId,preferences.getUserID(),preferences.getLanguage()) > 0){
                                            dbHandler.updateRibbonFilterFromServerSide(ProfileId,preferences.getUserID(),preferences.getLanguage(),mAttributeValue);
                                        }
                                        else {
                                            dbHandler.addRibbonFilter(ProfileId,preferences.getUserID(),preferences.getLanguage(),mAttributeKey
                                                    ,mAttributeValue,"",getResources().getString(R.string.weekm));
                                        }

                                        preferences.saveIsProgressShow("no");
                                        preferences.clearLastFolderfilterString();
                                        preferences.clearLastTextFilterString();
                                        preferences.clearLastUserDefinedFilterString();

                                        preferences.clearLastStatusilterString();
                                        preferences.clearLastTrafficFilterString();
                                        preferences.clearLastDateFilterString();
                                        preferences.clearUserDefinedType();

                                        preferences.clearResourcefilter();
                                        preferences.clearFireResourcefilter();
                                        preferences.clearLastFiredFilterAll();

                                        setLanguage(preferences.getLanguage());
                                        setImageFromLanguageSeted();

                                    }
                                }
                                interfaceChangeLanguageHome.changeLanguageHome();
                                interfaceProfileChanged.profileChanged();
                                interfaceProfileChanged2.profileChanged2();
                            }
                            else {
                                LogApp.e(" no response from service :  ", " response from service : ");
                                //progressDialog.dismiss();
                                //GlobalClass.showToast(activity,getString(R.string.something_is_wrong));
                                GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.error_title),getString(R.string.something_is_wrong));
                            }
                        }
                        else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1009) {
                                /* whenever access token is expire get 1009
                                from service and call here getNewdeviceToken servcie and replace onld one with new device token */
                            updateAccessToken();
                        }
                        else {
                            //progressDialog.dismiss();
                            //GlobalClass.showToast(activity,getString(R.string.something_is_wrong));
                        }
                    }
                    catch (Exception e){
                        LogApp.e("","exception in while change proifle : "+e.toString());
                        //progressDialog.dismiss();
                        //GlobalClass.showToast(activity,getString(R.string.something_is_wrong));
                    }
                }
                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    //progressDialog.dismiss();
                    if (t instanceof IOException) {
                        //GlobalClass.showToast(getActivity(),getString(R.string.server_timeout_exception));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.server_timeout_exception),getString(R.string.server_timeout_exception));
                    }
                    //GlobalClass.showToast(activity,getString(R.string.something_is_wrong));
                }
            });
        }
        catch (Exception e){
            LogApp.e(" while get proifle service : "," in catch : "+e.toString());
        }
    }
    public interface InterfaceChangeLanguageHome{
        void changeLanguageHome();
    }

    public interface InterfaceProfileChanged{
        void profileChanged();
    }
    public interface InterfaceProfileChanged2{
        void profileChanged2();
    }
    public interface InterfaceProfileChangedForSetting{
        void profileChangedSetting();
    }
    public interface IntefaceChangepass{
        public void changeTitle();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public void onDetach()
    {
        super.onDetach();
    }
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public boolean onBackPressed() {
        // currently visible tab Fragment
        OnBackPressListener currentFragment = (OnBackPressListener)getActivity();

        if (currentFragment != null) {
            // lets see if the currentFragment or any of its childFragment can handle onBackPressed
            return currentFragment.onBackPressed();
        }

        // this Fragment couldn't handle the onBackPressed call
        return false;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu_item, menu);
        this.menu = menu;
        menu.findItem(R.id.actionEdit).setVisible(false);
        menu.findItem(R.id.actionDefault).setVisible(false);
        menu.findItem(R.id.actionSupport).setVisible(false);
        menu.findItem(R.id.actionSend).setVisible(false);
        menu.findItem(R.id.actionprofile).setVisible(false);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.actionEdit:
                //Toast.makeText(getActivity()," option clicked : ",Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        activity = context;
        interfaceProfileChanged =(InterfaceProfileChanged) context;
        interfaceProfileChanged2 = (InterfaceProfileChanged2) context;
        interfaceChangeLanguageHome =(InterfaceChangeLanguageHome) context;
    }
// set language dynamically
    public void setLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config,this.getResources().getDisplayMetrics());

        textviewChangePass.setText(getString(R.string.changepassword));

    }
//    setting image src to imageview based on condition
    public void setImageFromLanguageSeted(){
        if(preferences.getLanguage().equalsIgnoreCase(getString(R.string.language_english)))
        {
            imgEnglish.setImageResource(R.drawable.img_england_select);
            imgGermany.setImageResource(R.drawable.img_germany_unselect);
        }
        else {
            imgEnglish.setImageResource(R.drawable.img_england_unselect);
            imgGermany.setImageResource(R.drawable.img_germany_select);
        }
    }

}
