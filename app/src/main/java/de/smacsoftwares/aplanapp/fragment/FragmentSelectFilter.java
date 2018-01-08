package de.smacsoftwares.aplanapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.activity.HomeActivity;
import de.smacsoftwares.aplanapp.util.DatabaseHandler;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.InterfaceChangeLanguageSelectFilter;
import de.smacsoftwares.aplanapp.util.InterfaceforSelectFilter;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;

import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by SSOFT4 on 10/25/2016.
 */

public class FragmentSelectFilter extends Fragment implements View.OnClickListener,InterfaceforSelectFilter,
        InterfaceChangeLanguageSelectFilter {
    boolean isVisible=false;
    RelativeLayout relativeFolder,relativeText,relativeDate,relativeUserDefined,relativeFooter;
    RelativeLayout relativeFolderSub,relativeTextSub,relativeDateSub,relativeUserDefinedSub;
    View rootView;
    TextView txtlink;
    PreferencesHelper preferences;
    DatabaseHandler dbHandler;

    FrameLayout frameLayoutMain;
    TextView textviewFolderfilter,textviewDatefilter,textviewTextfilter,textviewUserdefinedfilter,textViewTitle;
    Context context;
    LinearLayout.LayoutParams param;
    LinearLayout.LayoutParams paramFooter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_select_filter, container,false);
        //MyApplication.component(getActivity()).inject(this);
        preferences = new PreferencesHelper(getActivity());
        dbHandler = new DatabaseHandler(getActivity());
        initControl();
        return rootView;
    }
    //    this method intialize control and class all initial work
    public void initControl()
    {
        getActivity();
        GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);
        GlobalClass.fontBold = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPathBold);
        frameLayoutMain = (FrameLayout)rootView.findViewById(R.id.select_filter_container);
        GlobalClass.setupTypeface(frameLayoutMain, GlobalClass.fontLight);
        relativeFolder= (RelativeLayout)rootView.findViewById(R.id.relative_folder);
        relativeText= (RelativeLayout)rootView.findViewById(R.id.relative_text);
        relativeDate= (RelativeLayout)rootView.findViewById(R.id.relative_date);
        relativeUserDefined= (RelativeLayout)rootView.findViewById(R.id.relative_userdefined);
        relativeFooter = (RelativeLayout)rootView.findViewById(R.id.relativefooter);

        relativeFolderSub= (RelativeLayout)rootView.findViewById(R.id.relative_folder_sub);
        relativeTextSub= (RelativeLayout)rootView.findViewById(R.id.relative_text_sub);
        relativeDateSub= (RelativeLayout)rootView.findViewById(R.id.relative_date_sub);
        relativeUserDefinedSub= (RelativeLayout)rootView.findViewById(R.id.relative_userdefined_sub);

        //relativeFolderSub.setVisibility(View.GONE);
        //relativeTextSub.setVisibility(View.GONE);
        //relativeDateSub.setVisibility(View.GONE);
        //relativeUserDefinedSub.setVisibility(View.GONE);

        textViewTitle=(TextView)rootView.findViewById(R.id.textview_title_select_filter);
        textViewTitle.setTypeface(GlobalClass.fontBold);
        textviewFolderfilter=(TextView)rootView.findViewById(R.id.textview_folderfilter);
        textviewTextfilter=(TextView)rootView.findViewById(R.id.textview_generalfilter);
        textviewDatefilter=(TextView)rootView.findViewById(R.id.textview_datefilter);
        textviewUserdefinedfilter=(TextView)rootView.findViewById(R.id.textview_userdefindedfilter);



        relativeFolder.setOnClickListener(this);
        relativeText.setOnClickListener(this);
        relativeDate.setOnClickListener(this);
        relativeUserDefined.setOnClickListener(this);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            if(GlobalClass.getScreenOrientation(getActivity()) ==1){
                // for portrait
                param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0, 0.09f);
                paramFooter = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0, 0.44f);
            }
            else {
                /// for landscape
                param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0, 0.14f);
                paramFooter = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0, 0.24f);
            }
        } else {
            if(GlobalClass.getScreenOrientation(getActivity()) ==1){
                // for portrait
                param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0, 0.12f);
                paramFooter = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0, 0.30f);
            }
            else {
                /// for landscape
                param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0, 0.14f);
                paramFooter = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0, 0.24f);
            }
        }
        /*if(getScreenOrientation() ==1){
            // for portrait
            param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 0.09f);
            paramFooter = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 0.44f);
        }
        else {
            /// for landscape
            param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 0.14f);
            paramFooter = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 0.24f);
        }*/
        relativeFolder.setLayoutParams(param);
        relativeText.setLayoutParams(param);
        relativeDate.setLayoutParams(param);
        relativeUserDefined.setLayoutParams(param);
        relativeFooter.setLayoutParams(paramFooter);

        txtlink= (TextView) rootView.findViewById(R.id.txt_ssoft_link);

        txtlink.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString ss = new SpannableString(getString(R.string.my_smsc_link));
        ss.setSpan(new URLSpanNoUnderline("http://www.smacsoftwares.de/"), 0, 36, 0);
        ss.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 36, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtlink.setText(ss);

        if(dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(),getString(R.string.FolderFilter_text),preferences.getLanguage(),preferences.getUserID()) > 0){
            relativeFolderSub.setBackgroundResource(R.drawable.select_filter_background_selected);
        }
        else {
            relativeFolderSub.setBackgroundResource(R.drawable.select_filter_background);
        }

        if(dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(),getString(R.string.DateFilter_text),preferences.getLanguage(),preferences.getUserID()) > 0){
            relativeDateSub.setBackgroundResource(R.drawable.select_filter_background_selected);
        }
        else {
            relativeDateSub.setBackgroundResource(R.drawable.select_filter_background);
        }

        if(dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(),getString(R.string.UserDefinedFilter_text),preferences.getLanguage(),preferences.getUserID()) > 0
                || dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(),getString(R.string.UserDefindedFilterForNumber),preferences.getLanguage(),preferences.getUserID()) > 0) {
            relativeUserDefinedSub.setBackgroundResource(R.drawable.select_filter_background_selected);
        }
        else {
            relativeUserDefinedSub.setBackgroundResource(R.drawable.select_filter_background);
        }

        if(dbHandler.getCursorCountFilterForGeneralFilter(preferences.getProfileId(),getString(R.string.TextFilter_text),preferences.getLanguage(),preferences.getUserID()) > 0){
            relativeTextSub.setBackgroundResource(R.drawable.select_filter_background_selected);
        }
        else {
            relativeTextSub.setBackgroundResource(R.drawable.select_filter_background);
        }
        setLanguage(preferences.getLanguage());
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen for landscape and portrait and set portrait mode always
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            /// for landscape
            param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 0.14f);
            paramFooter = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 0.24f);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            // for portrait
            param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 0.09f);
            paramFooter = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0, 0.44f);

        }
        relativeFolder.setLayoutParams(param);
        relativeText.setLayoutParams(param);
        relativeDate.setLayoutParams(param);
        relativeUserDefined.setLayoutParams(param);
        relativeFooter.setLayoutParams(paramFooter);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        /*if (isVisibleToUser) {

            isVisible=true;
            //GlobalClass.showToast(getActivity()," visible to user");
        }
        else {
            if(isVisible){

                FragmentFilterText filter = new FragmentFilterText();
                filter.doneButtonClicked();
            }
        }*/
    }
    @Override
    public void onClick(View v)
    {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        switch (v.getId()) {

            case R.id.relative_text:

                fragment = new FragmentFilterText();
                transaction.replace(R.id.select_filter_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.relative_date:
                //FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragment = new FragmentFilterDate();
                transaction.replace(R.id.select_filter_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();

                break;
            case R.id.relative_folder:
                //FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragment = new FragmentFilterFolder();
                transaction.replace(R.id.select_filter_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.relative_userdefined:
                //FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragment = new FragmentFilterUserDefined();
                transaction.replace(R.id.select_filter_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }
    public class URLSpanNoUnderline extends URLSpan
    {
        public URLSpanNoUnderline(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(true);
        }
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        clearReferences();
    }
    @Override
    public void onPause() {
        super.onPause();
/*        clearReferences();*/
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    private void clearReferences() {
    }
    @Override
    public void callSelectFilterFragment() {
            initControl();
            //GlobalClass.showToast(getActivity()," interface callled : ");
    }
    @Override
    public void callUpdateFilter() {
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        ((HomeActivity)context).interfaceforSelectFilter = this;
        ((HomeActivity)context).interfaceChangeLanguageSelectFilter = this;
    }
    @Override
    public void changeLanguageSelectFilter()
    {
        setLanguage(preferences.getLanguage());
    }
    public void setLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config,this.getResources().getDisplayMetrics());

        textviewDatefilter.setText(getString(R.string.general_filter_date_text));
        textviewTextfilter.setText(getString(R.string.general_filter_text_text));
        textviewUserdefinedfilter.setText(getString(R.string.general_filter_user_defined_text));
        textviewFolderfilter.setText(getString(R.string.general_filter_folder_text));
        textViewTitle.setText(getString(R.string.general_filte_title));

    }

}
