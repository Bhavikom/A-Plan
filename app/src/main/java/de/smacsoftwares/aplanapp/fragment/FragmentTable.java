package de.smacsoftwares.aplanapp.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.Model.ProfileModelClass;
import de.smacsoftwares.aplanapp.Model.TableDataModelClass;
import de.smacsoftwares.aplanapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import de.smacsoftwares.aplanapp.adapter.RecyclerListAdapter;
import de.smacsoftwares.aplanapp.adapter.TableAdapter;
import de.smacsoftwares.aplanapp.helper.OnStartDragListener;
import de.smacsoftwares.aplanapp.helper.SimpleItemTouchHelperCallback;
import de.smacsoftwares.aplanapp.util.CustomTypefaceSpan;
import de.smacsoftwares.aplanapp.util.DatabaseHandler;
import de.smacsoftwares.aplanapp.util.GenericHelper;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.LogApp;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;
import okhttp3.Headers;
import retrofit2.Call;


public class FragmentTable extends Fragment implements AdapterView.OnItemClickListener, OnStartDragListener {
    String profileId = "";
    String columnValueToSend = "";
    String expandedIdToSend = "";
    String profileNameToSend = "";
    PreferencesHelper preferences;
    DatabaseHandler dbHandler;

    RecyclerListAdapter adapterRecyclerview;
    TableAdapter adapterSimple;
    private ItemTouchHelper mItemTouchHelper;
    private RecyclerView recyclerView;
    private Menu menu;
    FragmentActivity activity;
    FragmentTable context;
    Toolbar toolbar;
    ArrayList<TableDataModelClass> arraylistTable;
    ArrayList<ProfileModelClass> arrayListUserPfofile;
    ArrayList<ProfileModelClass> arrayListUserPfofileTemp;
    TextView textviewLableName, textViewLableWidth, textViewLableShow;
    RelativeLayout relativeMain;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_table, container, false);
        //MyApplication.component(getActivity()).inject(this);
        preferences = new PreferencesHelper(getActivity());
        dbHandler = new DatabaseHandler(getActivity());
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
        initControl();
        return view;
    }

    //    this method intialize control and class all initial work
    public void initControl() {
        GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);
        /* activity = (SettingActivity) getActivity();*/
        arraylistTable = new ArrayList<TableDataModelClass>();
        GlobalClass.isEditMode = false;
        relativeMain = (RelativeLayout) view.findViewById(R.id.reletive_table_main);
        GlobalClass.setupTypeface(relativeMain, GlobalClass.fontRegular);
        //dbHandler.deleteProfile(preferences.getUserID());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        context = this;

        textviewLableName = (TextView) view.findViewById(R.id.textview_lable_name);
        textViewLableShow = (TextView) view.findViewById(R.id.textview_lable_show);
        textViewLableWidth = (TextView) view.findViewById(R.id.textview_lable_width);
        setLanguage(preferences.getLanguage());
        callingServiceAndShowdata();
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
    }

    void callingServiceAndShowdata() {
        /* first time getting data from database of login time*/
        arrayListUserPfofile = dbHandler.getSingleUserProfileForTableScreen(preferences.getUserID(),
                preferences.getProfileId(), preferences.getLanguage());
        if (arrayListUserPfofile.size() > 0) {
            /* calling service here from pfofile id */
            profileId = arrayListUserPfofile.get(0).getProfileId();
            expandedIdToSend = arrayListUserPfofile.get(0).getExpandedId();
            profileNameToSend = arrayListUserPfofile.get(0).getProfileName();
            //new getUserProfilefromId().execute();
            getProfile();
        }
    }

    public void getUpdateDatafromListview() {
        if (adapterRecyclerview.getSelectedData().size() > 0) {
            JSONArray jsonArray = new JSONArray();
            for (TableDataModelClass taskModel : adapterRecyclerview.getSelectedData()) {
                String isshow = "";
                if (taskModel.selected) {
                    isshow = "1";
                } else {
                    isshow = "0";
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Name", taskModel.name);
                    jsonObject.put("Width", taskModel.width);
                    jsonObject.put("Show", isshow);
                    jsonObject.put("Id", taskModel.Id);
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //dbHandler.addProfileInfo(taskModel.name,isshow,taskModel.width,preferences.getUserID(),"DefaultProfile");
                if (taskModel.selected) {
                    LogApp.e(" ###### ", " get selected result : " + taskModel.name);
                }
            }
            columnValueToSend = jsonArray.toString();
            //new updateUserProfile().execute();
            updateProfile();
        }

    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu_item, menu);
        this.menu = menu;

        menu.findItem(R.id.actionEdit).setVisible(true);
        menu.findItem(R.id.actionDefault).setVisible(false);
        menu.findItem(R.id.actionSupport).setVisible(false);
        menu.findItem(R.id.actionSend).setVisible(false);
        menu.findItem(R.id.actionprofile).setVisible(false);

        menu.findItem(R.id.actionDefault).setTitle(R.string.default_string);
        menu.findItem(R.id.actionEdit).setTitle(R.string.edit);
        menu.findItem(R.id.actionSend).setTitle(R.string.send);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case R.id.actionEdit:

                if(preferences.isDemoUser()){
                    GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.access_denied_title),
                            getString(R.string.profile_update_disable));
                }else {
                    if (!GlobalClass.isEditMode) {
                        GlobalClass.isEditMode = true;
                        menu.findItem(R.id.actionEdit).setTitle(getResources().getString(R.string.ok));
                        if (adapterRecyclerview != null) {
                            adapterRecyclerview.notifyDataSetChanged();
                        }


                    } else {
                        //dbHandler.deleteProfile(preferences.getUserID());
                        if (arraylistTable.size() > 0) {

                            getUpdateDatafromListview();
                        }
                        GlobalClass.isEditMode = false;
                        menu.findItem(R.id.actionEdit).setTitle(getResources().getString(R.string.edit));
                        if (adapterRecyclerview != null) {
                            adapterRecyclerview.notifyDataSetChanged();
                        }

                    }
                }

                return true;
            case R.id.actionDefault:

                return true;
            default:
                return super.onOptionsItemSelected(item);
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
                                getProfile();
                            } else if (pos == 2) {
                                updateProfile();
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

    private void getProfile() {
        try {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            //progressDialog.create();

            SpannableString ss = new SpannableString(getString(R.string.fetching_profile));
            ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new CustomTypefaceSpan("", GlobalClass.fontRegular), 0, ss.length(), 0);

            progressDialog.setMessage(ss);
            progressDialog.show();

            GenericHelper helper = new GenericHelper(getActivity());
            Call<String> call = helper.getRetrofit().callGetProfileFormID(profileId);
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
                                preferences.saveCurrentFiredFilter("6");
                                preferences.saveIsProgressShow("yes");
                                if(progressDialog != null){
                                    progressDialog.dismiss();
                                }

                                String ColumnsDetail = "";
                                JSONObject obj = (JSONObject) new JSONTokener(body).nextValue();
                                String status = obj.getString("Status");
                                String Name = "";
                                String Id = "";
                                String UserId = "";
                                if (status.equalsIgnoreCase("0")) {
                                    JSONObject jsonObjPayload = obj.getJSONObject("Payload");
                                    Name = jsonObjPayload.getString("Name");
                                    Id = jsonObjPayload.getString("Id");
                                    UserId = jsonObjPayload.getString("UserId");
                                    JSONArray jsonArrayAttribute = jsonObjPayload.getJSONArray("Attributes");
                                    if (jsonArrayAttribute.length() > 0) {
                                        for (int i = 0; i < jsonArrayAttribute.length(); i++) {

                                            JSONObject objAttribute = jsonArrayAttribute.getJSONObject(i);
                                            String AttributeName = objAttribute.getString("AttributeName");
                                            if (AttributeName.equalsIgnoreCase("Columns")) {
                                                ColumnsDetail = objAttribute.getString("AttributeValue");
                                            }

                                        }
                                    }
                                    ProfileModelClass userProfile = new ProfileModelClass();
                                    userProfile.setProfileName(Name);
                                    userProfile.setColDeTail(ColumnsDetail);
                                    userProfile.setProfileId(Id);
                                    userProfile.setProfileUserId(UserId);

                                    arrayListUserPfofileTemp = new ArrayList<>();
                                    arrayListUserPfofileTemp.add(userProfile);

                                    if(progressDialog != null){
                                        progressDialog.dismiss();
                                    }
                                    if (arrayListUserPfofileTemp != null) {
                                        if (arrayListUserPfofileTemp.size() > 0) {
                                            try {
                                                arraylistTable.clear();
                                                JSONArray jsonArray = new JSONArray(arrayListUserPfofileTemp.get(0).getColDeTail().toString());
                                                LogApp.e("", " json array ; " + jsonArray);
                                                if (jsonArray.length() > 0) {
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject jsonobjectInner = jsonArray.getJSONObject(i);
                                                        String name = "";
                                                        String width = "";
                                                        String show = "";
                                                        String id = "";
                                                        boolean isShow = false;
                                                        if (jsonobjectInner.has("Name") && jsonobjectInner.has("Id") &&
                                                                jsonobjectInner.has("Width") && jsonobjectInner.has("Show")) {
                                                            name = jsonobjectInner.getString("Name");
                                                            width = jsonobjectInner.getString("Width");
                                                            show = jsonobjectInner.getString("Show");
                                                            id = jsonobjectInner.getString("Id");

                                                            if (show.equalsIgnoreCase("1")) {
                                                                isShow = true;
                                                            } else {
                                                                isShow = false;
                                                            }
                                                        }
                                                        if(!TextUtils.isEmpty(name)){
                                                            arraylistTable.add(new TableDataModelClass(name, width, 0, isShow, id));
                                                        }
                                                    }
                                                }
                                                if (arraylistTable.size() > 0) {
                                                    adapterRecyclerview = new RecyclerListAdapter(getActivity(), context, arraylistTable);
                                                    recyclerView.setAdapter(adapterRecyclerview);
                                                    ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapterRecyclerview);
                                                    mItemTouchHelper = new ItemTouchHelper(callback);
                                                    mItemTouchHelper.attachToRecyclerView(recyclerView);
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                        }
                    } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                            Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1009) {
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
                        //dialogCreateProfile.dismiss();
                        //GlobalClass.showToast(getActivity(), getString(R.string.no_response_from_server));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.error_title),getString(R.string.something_is_wrong));
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }
                    if(t instanceof UnknownHostException){
                        //progressDialog.dismiss();
                       // GlobalClass.showToast(getActivity(), getString(R.string.url_not_found));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.enter_organization_url_title),getString(R.string.enter_valid_url));
                        preferences.save404Count(preferences.get404Count()+1);
                    }
                    else {
                        //GlobalClass.showToast(getActivity(), getString(R.string.problem_while_deleting_profile));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.error_title),getString(R.string.no_response_from_server));
                    }

                }
            });
        } catch (Exception e) {
            LogApp.e(" while login service : ", " in catch : " + e.toString());
        }
    }

    private void updateProfile() {
        try {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            //progressDialog.create();

            SpannableString ss = new SpannableString(getString(R.string.please_wait));
            ss.setSpan(new RelativeSizeSpan(1.0f), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new CustomTypefaceSpan("", GlobalClass.fontRegular), 0, ss.length(), 0);

            progressDialog.setMessage(ss);
            progressDialog.show();

            GenericHelper helper = new GenericHelper(getActivity());
            Call<String> call = helper.getRetrofit().updateProfile(profileId, columnValueToSend);
            call.enqueue(new retrofit2.Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    String body = response.body();
                    if (body == null || body.equalsIgnoreCase("")) {
                        /* body is null that's why call new Access token service here */
                        updateAccessToken(2);
                    } else if (body != null || !body.equalsIgnoreCase("")) {
                        Headers header = response.headers();
                        try {
                            if (header.get(getResources().getString(R.string.x_status)) != null &&
                                    Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 0) {
                                if(progressDialog != null){
                                    progressDialog.dismiss();
                                }
                                getProfile();

                            } else if (header.get(getResources().getString(R.string.x_status)) != null &&
                                    Integer.parseInt(header.get(getResources().getString(R.string.x_status))) == 1009) {
                                /* whenever access token is expire get 1009
                                from service and call here getNewdeviceToken servcie and replace onld one with new device token */
                                updateAccessToken(2);
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
                                //dialogCreateProfile.dismiss();
                                //GlobalClass.showToast(getActivity(), getString(R.string.no_response_from_server));
                                GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.error_title),getString(R.string.no_response_from_server));
                            }
                        } catch (Exception e) {
                            if(progressDialog != null){
                                progressDialog.dismiss();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if(progressDialog != null){
                        progressDialog.dismiss();
                    }
                    if(t instanceof UnknownHostException){
                        //progressDialog.dismiss();
                        //GlobalClass.showToast(getActivity(), getString(R.string.url_not_found));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.enter_organization_url_title),getString(R.string.enter_valid_url));
                        preferences.save404Count(preferences.get404Count()+1);
                    }
                    else {
                        //GlobalClass.showToast(getActivity(), getString(R.string.problem_while_deleting_profile));
                        GlobalClass.showCustomMessageDialog(getActivity(),getString(R.string.error_title),getString(R.string.no_response_from_server));
                    }

                }
            });
        } catch (Exception e) {
            LogApp.e(" while login service : ", " in catch : " + e.toString());
        }
    }

    public void setLanguage(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
        textviewLableName.setText(getString(R.string.name));
        textViewLableWidth.setText(getString(R.string.width));
        textViewLableShow.setText(getString(R.string.show));
    }
}
