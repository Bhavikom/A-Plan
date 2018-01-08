package de.smacsoftwares.aplanapp.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.util.DatabaseHandler;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;


import javax.inject.Inject;

public class FragmentSupportAbout extends Fragment {

    PreferencesHelper preferences;
    DatabaseHandler dbHandler;

    FragmentProject activity;
    private Menu menu;
    TextView textViewVerision,textViewSQlVersion,textViewLicence,textViewWebViewer,textViewLicenceTo;
    LinearLayout linearMain;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_support_about, container,false);
        //MyApplication.component(getActivity()).inject(this);
        dbHandler = new DatabaseHandler(getActivity());
        preferences = new PreferencesHelper(getActivity());
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);

        GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);
        GlobalClass.fontBold = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPathBold);
        GlobalClass.fontLight = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPathLight);
        GlobalClass.fontMedium = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPathMedium);
        linearMain=(LinearLayout)rootView.findViewById(R.id.linear_about_main);
        GlobalClass.setupTypeface(linearMain, GlobalClass.fontRegular);
        textViewVerision=(TextView)rootView.findViewById(R.id.textview_version_id);
        textViewSQlVersion=(TextView)rootView.findViewById(R.id.textview_sql_version);
        textViewLicence=(TextView)rootView.findViewById(R.id.textview_licence);
        textViewWebViewer=(TextView)rootView.findViewById(R.id.textview_web_viewer);
        textViewLicenceTo=(TextView)rootView.findViewById(R.id.txt_demouser);

        textViewVerision.setTypeface(GlobalClass.fontLight);
        textViewSQlVersion.setTypeface(GlobalClass.fontLight);
        textViewLicence.setTypeface(GlobalClass.fontLight);
        textViewWebViewer.setTypeface(GlobalClass.fontLight);
        textViewLicenceTo.setTypeface(GlobalClass.fontLight);

        textViewVerision.setText("Version "+GlobalClass.getAppversion(getActivity()));
        textViewLicenceTo.setText(preferences.getOrganizationName());

        return rootView;
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.toolbar_menu_item, menu);
        this.menu = menu;
        menu.findItem(R.id.actionEdit).setVisible(false);
        menu.findItem(R.id.actionDefault).setVisible(false);
        menu.findItem(R.id.actionSend).setVisible(false);
        menu.findItem(R.id.actionSupport).setVisible(true);
        menu.findItem(R.id.actionprofile).setVisible(false);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSupport:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
        }
        return true;
    }
}