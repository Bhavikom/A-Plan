package de.smacsoftwares.aplanapp.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import de.smacsoftwares.aplanapp.Model.FolderDataset;
import de.smacsoftwares.aplanapp.Model.GeneralFilterDataSet;
import de.smacsoftwares.aplanapp.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import de.smacsoftwares.aplanapp.activity.HomeActivity;
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

public class FragmentFilterFolder extends Fragment implements View.OnClickListener,
        InterfaceforSelectFilter,InterfaceProjectClicked {

    PreferencesHelper preferences;
    DatabaseHandler dbHandler;

    boolean isTablet;
    String comefrom="this";
    public static boolean isTextChanged=false;
    public static boolean isTimetoChangeText=false;
    Context context;
    TextView textviewDone,textviewGeneralFilter,textViewTitle;
    ListView listviewFolder;
    ArrayList<FolderDataset> arraylistFolder = new ArrayList<>();
    FilterFolderAdapter adpterFolder;
    ArrayList<FolderDataset> arraylistSelectedFolders = new ArrayList<>();
    ArrayList<GeneralFilterDataSet> arraylistgetedFromDatabase = new ArrayList<>();
    List<String> arraygetedIdFromdb;
    View rootView;
    ImageView imgDeleteFilter,imgBack;
    RelativeLayout relativeMain;
    LinearLayout linearBack;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_filter_folder, container,false);
        //MyApplication.component(getActivity()).inject(this);
        preferences = new PreferencesHelper(getActivity());
        dbHandler = new DatabaseHandler(getActivity());
        initControl();
        return rootView;
    }
    //    this method intialize control and class all initial work
    public void initControl() {
        isTablet = getResources().getBoolean(R.bool.isTablet);
        GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);
        arraylistSelectedFolders = new ArrayList<>();
        arraylistgetedFromDatabase = new ArrayList<>();
        arraylistFolder = new ArrayList<>();

        imgBack = (ImageView)rootView.findViewById(R.id.imgback);
        linearBack = (LinearLayout)rootView.findViewById(R.id.linearback);
        linearBack.setOnClickListener(this);
        relativeMain = (RelativeLayout)rootView.findViewById(R.id.relative_folder_main);
        GlobalClass.setupTypeface(relativeMain, GlobalClass.fontRegular);
        imgDeleteFilter = (ImageView)rootView.findViewById(R.id.img_delete);
        imgDeleteFilter.setOnClickListener(this);
        textViewTitle = (TextView)rootView.findViewById(R.id.textview_lable_title);
        textViewTitle.setTypeface(GlobalClass.fontBold);
        textviewDone = (TextView)rootView.findViewById(R.id.textview_done);
        textviewDone.setOnClickListener(this);
        textviewDone.setVisibility(View.GONE);
        textviewGeneralFilter=(TextView)rootView.findViewById(R.id.textview_generalfilter);
        textviewGeneralFilter.setTypeface(GlobalClass.fontLight);
        textviewGeneralFilter.setOnClickListener(this);
        textviewGeneralFilter.setOnTouchListener(new CustomTouchListener());
        listviewFolder = (ListView)rootView.findViewById(R.id.listviewFolder);

        arraygetedIdFromdb = new ArrayList<>();
        if(dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(),getString(R.string.FolderFilter_text),preferences.getLanguage(),preferences.getUserID()) > 0){
            arraylistgetedFromDatabase = dbHandler.getAllGeneralFilterData(preferences.getProfileId(),getString(R.string.FolderFilter_text),preferences.getLanguage(),preferences.getUserID());

            arraygetedIdFromdb = new ArrayList<>();
            String[] split = arraylistgetedFromDatabase.get(0).getValueLocal().split(",");
            String[] splitlocal = arraylistgetedFromDatabase.get(0).getValueLocal().split(",");
            String[] splitserver = arraylistgetedFromDatabase.get(0).getValue().split(",");
            String[] combined = new String[0];
            if(splitlocal.length > 0){
                if(splitserver.length > 0){
                    Set<String> set = new HashSet<>(Arrays.asList(splitlocal));
                    set.addAll(Arrays.asList(splitserver));
                    combined = set.toArray(new String[0]);
                }
                else {
                    combined = splitlocal;
                }

            }
            else if(splitserver.length > 0){
                combined = splitserver;
            }
            if(combined.length > 0){
                for (int i = 0; i < combined.length; i++) {
                    arraygetedIdFromdb.add(combined[i]);
                }
            }
        }
        /*if (dbHandler.getCursorCountFilterForAllFilter(preferences.getUserID(),preferences.getProfileId()) > 0) {
            arraylistgetedFromDatabase = dbHandler.getGeneralFilterData(getString(R.string.FolderFilter_text), preferences.getUserID(),
                    preferences.getProfileId());
        }*/

        /* calling the service */
        if (GlobalClass.isNetworkAvailable(getActivity())) {
            getGroupList();
        } else {
            //GlobalClass.showToastInternet(getActivity());
            GlobalClass.showCustomDialogInternet(getActivity());
        }
        setLangauge();
    }
//    calling folder list service
    private void getGroupList() {
        try {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            //progressDialog.create();

            SpannableString ss=  new SpannableString(getString(R.string.please_wait));
            ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new CustomTypefaceSpan("", GlobalClass.fontRegular), 0, ss.length(), 0);

            progressDialog.setMessage(ss);
            progressDialog.show();

            GenericHelper helper = new GenericHelper(getActivity());
            Call<String> call = helper.getRetrofit().getGroupList(/*GlobalClass.strAccessToken+GlobalClass.getEpochTime()*/);
            call.enqueue(new retrofit2.Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    String finalResponseString = new Gson().toJson(response);
                    try {
                        String strResponse = response.body();
                        JSONArray jsonArray = (JSONArray) new JSONTokener(strResponse).nextValue();
                        //String body = obj.getString("body");
                        if(jsonArray.length() <= 0){
                            /* response is null that's why call get Access token service hare update access token */
                            Headers header = response.headers();
                            try {
                                if (header.get(getResources().getString(R.string.x_status)) != null &&
                                        Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 0)
                                {
                                }
                                else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                        Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1009) {
                                /* whenever access token is expire get 1009
                                from service and call here getNewdeviceToken servcie and replace onld one with new device token */
                                    updateAccessToken();
                                }
                                else {
                                    if(progressDialog != null) {
                                        progressDialog.dismiss();
                                    }
                                    //GlobalClass.showToast(activity,getString(R.string.something_is_wrong));
                                }
                            }
                            catch (Exception e){
                                if(progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                //GlobalClass.showToast(activity,getString(R.string.something_is_wrong));
                            }
                        }
                        else {
                            /* response is not null thats why parse and show data in listview*/
                            if(jsonArray.length() > 0 ){
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    FolderDataset folder = new FolderDataset();
                                    folder.setId(object.getString("Id"));
                                    folder.setName(object.getString("Name"));
                                    arraylistFolder.add(folder);
                                }
                            }
                            for (int i = 0; i < arraylistFolder.size(); i++) {
                                if(arraygetedIdFromdb != null){
                                    if (arraygetedIdFromdb.size() > 0) {
                                        for (int j = 0; j < arraygetedIdFromdb.size(); j++) {
                                            if (arraylistFolder.get(i).getId().equalsIgnoreCase(arraygetedIdFromdb.get(j))) {
                                                arraylistFolder.get(i).setSelected(true);
                                            }

                                        }
                                    }
                                }

                            }
                            listviewFolder.setAdapter(null);
                            adpterFolder = new FilterFolderAdapter(getActivity(), arraylistFolder);
                            listviewFolder.setAdapter(adpterFolder);
                        }
                        if(progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        isTimetoChangeText=true;

                    }
                    catch (Exception e){
                        LogApp.e(" ###### ", " exception response from folder : " + e.toString());
                    }
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
        } catch (Exception e) {
            isTimetoChangeText=true;
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
                            getGroupList();
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
    public void doneButtonClicked(String comeFrom){
        if(comeFrom.equalsIgnoreCase("this")){
            if(preferences != null) {
                preferences.saveIsProgressShow("yes");/// crash here
            }
        }
        else {
            if(preferences != null) {
                preferences.saveIsProgressShow("no");
            }
        }

            /* on clicking of done button getting selected value from adapter and storing in database table for retain selected */
        arraylistSelectedFolders.clear();
        if(adpterFolder != null){
            arraylistSelectedFolders = adpterFolder.getSelectedData();
        }
        if (arraylistSelectedFolders.size() > 0) {

            //dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.FolderFilter_text));
            ArrayList<String> arrayStringName = new ArrayList<>();
            ArrayList<String> arrayStringId = new ArrayList<>();
            for (int i = 0; i < arraylistSelectedFolders.size(); i++) {
                arrayStringName.add(arraylistSelectedFolders.get(i).getName());
                arrayStringId.add(arraylistSelectedFolders.get(i).getId());
            }
            String commaSeperated = "";
            String commaSeperatedID = "";
            commaSeperated = TextUtils.join(",", arrayStringName);
            commaSeperatedID = TextUtils.join(",", arrayStringId);
            if(commaSeperatedID.equalsIgnoreCase(preferences.getLastFolderfilterString())){
                preferences.saveLastFolderfilterString(commaSeperatedID);
                /*dbHandler.addGeneralFilterInfo(getString(R.string.FolderFilter_text), commaSeperated,
                        arraylistSelectedFolders.get(0).getName(),commaSeperatedID, preferences.getUserID(),
                        preferences.getProfileId());*/
                if (dbHandler.getCursorCountForFolderFilter(preferences.getProfileId(),
                        getString(R.string.FolderFilter_text), preferences.getLanguage(), preferences.getUserID()) > 0) {
                    dbHandler.updateGeneralFilter(preferences.getProfileId(),
                            getString(R.string.folder_group_id),getString(R.string.FolderFilter_text),commaSeperatedID,preferences.getLanguage(),preferences.getUserID());
                }
                else {
                    dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.folder_group_id),"",preferences.getLanguage(),getString(R.string.FolderFilter_text),preferences.getUserID(),commaSeperatedID);
                }

            }
            else {
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

                preferences.saveLastFolderfilterString(commaSeperatedID);
                /*dbHandler.addGeneralFilterInfo(getString(R.string.FolderFilter_text), commaSeperated,
                        arraylistSelectedFolders.get(0).getName(),commaSeperatedID, preferences.getUserID(),
                        preferences.getProfileId());*/
                if (dbHandler.getCursorCountForFolderFilter(preferences.getProfileId(),
                        getString(R.string.FolderFilter_text), preferences.getLanguage(), preferences.getUserID()) > 0) {
                    dbHandler.updateGeneralFilter(preferences.getProfileId(),
                            getString(R.string.folder_group_id),getString(R.string.FolderFilter_text),commaSeperatedID,preferences.getLanguage(),preferences.getUserID());
                }
                else {
                    dbHandler.addGeneralFilter(preferences.getProfileId(),getString(R.string.folder_group_id),"",preferences.getLanguage(),getString(R.string.FolderFilter_text),preferences.getUserID(),commaSeperatedID);
                }
            }
        }
        else {
            if(preferences.getLastFolderfilterString().equalsIgnoreCase("")) {
            }
            else {
                preferences.saveLastFolderfilterString("");
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
                if(dbHandler.getServerStringFromFolderFilter(preferences.getProfileId(),getString(R.string.FolderFilter_text),preferences.getLanguage(),preferences.getUserID()).equalsIgnoreCase("")){
                    dbHandler.deleteGeneralFilterDataWhileRefresh(preferences.getProfileId(),getString(R.string.FolderFilter_text),preferences.getLanguage(),preferences.getUserID());
                }
                else {
                    dbHandler.updateGeneralFilter(preferences.getProfileId(),
                            getString(R.string.folder_group_id),getString(R.string.FolderFilter_text),"",preferences.getLanguage(),preferences.getUserID());
                }

                //dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.FolderFilter_text));
            }

        }
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.textview_generalfilter:
                comefrom="this";
                doneButtonClicked(comefrom);
                FragmentTransaction transaction2 = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentSelectFilter fragment2 = new FragmentSelectFilter();
                transaction2.replace(R.id.select_filter_container, fragment2);
                transaction2.addToBackStack(null);
                transaction2.commit();
            case R.id.linearback:
                comefrom="this";
                doneButtonClicked(comefrom);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentSelectFilter fragment = new FragmentSelectFilter();
                transaction.replace(R.id.select_filter_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.textview_done:
                comefrom="this";
                doneButtonClicked(comefrom);
                break;

            case R.id.img_delete:
                dbHandler.updateGeneralFilterToDelete(preferences.getProfileId(),getString(R.string.FolderFilter_text),"",preferences.getLanguage(),
                        preferences.getUserID());
                //dbHandler.deleteFilter(preferences.getUserID(),preferences.getProfileId(),getString(R.string.FolderFilter_text));
                if(arraylistFolder.size() > 0){
                    for (int i = 0; i < arraylistFolder.size(); i++) {
                        if(arraygetedIdFromdb != null){
                            arraylistFolder.get(i).setSelected(false);
                        }
                    }
                }
                adpterFolder.notifyDataSetChanged();
                //initControl();
                break;
        }
    }
    @Override
    public void callSelectFilterFragment() {}
    @Override
    public void callUpdateFilter()
    {
        isTimetoChangeText=false;
        initControl();
    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        context = activity;
        ((HomeActivity)context).interfaceforSelectFilter = this;
        //((HomeActivity)context).interfaceChangeLanguageFolderFilter = this;
        ((HomeActivity)context).interfaceProjectClicked = this;


    }
    public class FilterFolderAdapter extends BaseAdapter {
        Context context;
        ArrayList<FolderDataset> arraylist;
        boolean isChangedAdapter=false;
        public FilterFolderAdapter(Context context, ArrayList<FolderDataset> items) {
            this.context = context;
            this.arraylist = items;
        }
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
            {
                convertView = mInflater.inflate(R.layout.item_list_folerfilter, null);
                holder = new ViewHolder();
                holder.textviewfolderName = (TextView) convertView.findViewById(R.id.textviewfolderitem);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.linearMain =(LinearLayout)convertView.findViewById(R.id.linear_folder_item_main);
            GlobalClass.setupTypeface(holder.linearMain, GlobalClass.fontRegular);

            holder.textviewfolderName.setText(arraylist.get(position).getName());
            CheckBox cbBuy = (CheckBox) convertView.findViewById(R.id.checkboxfilter);
            cbBuy.setOnCheckedChangeListener(myCheckChangList);
            cbBuy.setTag(position);
            cbBuy.setChecked(arraylist.get(position).isSelected());

            if(arraylist.get(position).isSelected()){
                holder.textviewfolderName.setTextColor(getResources().getColor(R.color.indigo_color));
            }
            else {
                holder.textviewfolderName.setTextColor(getResources().getColor(R.color.black_color));
            }

            convertView.setTag(holder);
            convertView.setTag(R.id.title, holder.textviewfolderName);
            convertView.setTag(R.id.checkbox, cbBuy);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (arraylist.get(position).isSelected()==true) {
                        arraylist.get(position).setSelected(false);
                    }else{
                        arraylist.get(position).setSelected(true);
                    }
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }
        CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
            {
                if(isTimetoChangeText){
                    isChangedAdapter=true;
                }
                getTableData((Integer) buttonView.getTag()).setSelected(isChecked);
                if(isTimetoChangeText){
                    doneButtonClicked(comefrom);
                }


            }
        };
        public FolderDataset getTableData(int position)
        {
            return ((FolderDataset) getItem(position));
        }
        public ArrayList<FolderDataset> getSelectedData()
        {
            ArrayList<FolderDataset> arraylistSelected = new ArrayList<FolderDataset>();
            for (FolderDataset tableData : arraylist)
            {
                if (tableData.isSelected())
                    arraylistSelected.add(tableData);
            }
            return arraylistSelected;
        }
        @Override
        public int getCount() {
            return arraylist.size();
        }
        @Override
        public Object getItem(int position) {
            return arraylist.get(position);
        }
        @Override
        public long getItemId(int position) {
            return arraylist.indexOf(getItem(position));
        }
    }
    /*private view holder class*/
    private class ViewHolder {
        TextView textviewfolderName;
        LinearLayout linearMain;

    }
    @Override
    public void clickedProject() {
        doneButtonClicked("this");
    }
    public void setLangauge(){
        if(isTablet){
            textviewGeneralFilter.setText(getString(R.string.general_filte_title));
        }
        else {
            textviewGeneralFilter.setText(getString(R.string.back));
        }

        textViewTitle.setText(getString(R.string.filterfolder_title));
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