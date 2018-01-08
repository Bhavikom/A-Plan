package de.smacsoftwares.aplanapp.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import de.smacsoftwares.aplanapp.Model.ChangePassResponse;
import de.smacsoftwares.aplanapp.R;

import javax.inject.Inject;

import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.InterfaceServiceClass;
import de.smacsoftwares.aplanapp.util.LogApp;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FragmentChangePassword extends Fragment {
    PreferencesHelper preferences;

    EditText editTextOldPass,editTextNewPass;
    Button btnSubmit;
    FragmentSetting activity;
    private  Menu menu;
    private OnFragmentInteractionListener mListener;
    public FragmentChangePassword() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View content = inflater.inflate(R.layout.fragment_change_password, container, false);
        //MyApplication.component(getActivity()).inject(this);
        preferences = new PreferencesHelper(getActivity());
        GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);
        editTextOldPass=(EditText) content.findViewById(R.id.edittextoldpsss);
        editTextNewPass=(EditText) content.findViewById(R.id.edittextnewpsss);
        btnSubmit=(Button) content.findViewById(R.id.btnsubmit);
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
        btnSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(GlobalClass.isNetworkAvailable(getActivity())){
                    if(checkValidation()) {
                        /// call login service here
                        //callChangePasswordWebService();
                    }
                }
                else {
                    //GlobalClass.showToastInternet(getActivity());
                    GlobalClass.showCustomDialogInternet(getActivity());
                }
            }
        });
        getActivity().setTitle(getResources().getString(R.string.changepassword));
        return content;
    }
//    this method checks validation and return true or false
    public boolean checkValidation()
    {
        boolean flag = true;
        if(TextUtils.isEmpty(editTextNewPass.getText().toString()) || TextUtils.isEmpty(editTextOldPass.getText().toString()))
        {
            //GlobalClass.showToastField(getActivity());
            GlobalClass.showCustomDialogFieldRequired(getActivity());
            flag=false;
        }
        else {
            flag=true;
        }
        return flag;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.toolbar_menu_item, menu);
        this.menu = menu;
        menu.findItem(R.id.actionEdit).setVisible(false);
        menu.findItem(R.id.actionDefault).setVisible(false);
        menu.findItem(R.id.actionSend).setVisible(false);
        menu.findItem(R.id.actionSupport).setVisible(false);
        menu.findItem(R.id.actionprofile).setVisible(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionprofile:
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStack();
        }
        return true;
    }
    /* this method is for calling change password service */
    /*private void callChangePasswordWebService()
    {
        try
        {
            final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),getString(R.string.changepassword),
                    getString(R.string.please_wait),false,false);
            //Creating a rest adapterRecyclerview
            RestAdapter adapter = new RestAdapter.Builder().setEndpoint(preferences.getServerUrlUser()).build();
            //Creating an object of our api interface
            InterfaceServiceClass interfaceObject = adapter.create(InterfaceServiceClass.class);
            interfaceObject.callChangePass(preferences.getUserID().toString(), editTextOldPass.getText().toString(),
                    editTextNewPass.getText().toString(),
                    new Callback<ChangePassResponse>()
            {
                @Override
                public void success(ChangePassResponse changePassResponse, Response response)
                {
                    LogApp.e(" change pass success "," response from service : "+changePassResponse.toString());
                    try
                    {
                        if(response != null)
                        {
                            if (changePassResponse.getStatus() == 0)
                            {
                                progressDialog.dismiss();
                                //GlobalClass.showToast(getActivity(),getString(R.string.password_change_successfully));
                            }
                            else if(changePassResponse.getStatus() == 1){
                                progressDialog.dismiss();
                                //GlobalClass.showToast(getActivity(),getString(R.string.password_does_not_change));
                            }
                        }
                    }
                    catch (Exception e){
                        progressDialog.dismiss();
                    }
                }
                @Override
                public void failure(RetrofitError error)
                {
                    LogApp.e(" change pass failed "," response from service : "+error.toString());

                    progressDialog.dismiss();
                }
            });
        }
        catch (Exception e){
            LogApp.e(" while change pass service : "," in catch : "+e.toString());
        }
    }*/
}