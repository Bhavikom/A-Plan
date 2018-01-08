package de.smacsoftwares.aplanapp.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;

import java.util.Locale;

import javax.inject.Inject;

import static android.R.id.message;
import static de.smacsoftwares.aplanapp.R.id.subject;

public class FragmentSupport extends Fragment implements View.OnClickListener
{
    private Menu menu;
    RelativeLayout relativeAbout, relativeHelp, relativeFaq, relativeContact,relativeContactAdmin;
    TextView textViewAboutus,textViewHelp,textViewVersionNo,textViewSendFeedback;
    LinearLayout linearLayoutMain;
    TextView textViewMessage;
    Button btnsend;
    View rootView;
    PreferencesHelper preferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_support, container, false);
        //MyApplication.component(getActivity()).inject(this);
        preferences = new PreferencesHelper(getActivity());
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
        initControl();
        return rootView;
    }
    //    this method intialize control and class all initial work
    public void initControl()
    {
        GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);
        linearLayoutMain=(LinearLayout)rootView.findViewById(R.id.linear_suppport_main);
        GlobalClass.setupTypeface(linearLayoutMain, GlobalClass.fontRegular);
        relativeContactAdmin=(RelativeLayout)rootView.findViewById(R.id.relative_contact_adamin);
        textViewMessage=(TextView)rootView.findViewById(R.id.textview_message);
        if(preferences.isFileMissing()){
            relativeContactAdmin.setVisibility(View.VISIBLE);
            textViewMessage.setText(getString(R.string.contact_adminstrator));
        }else {
            relativeContactAdmin.setVisibility(View.GONE);
        }

        if(preferences.isRightRevoked()){
            relativeContactAdmin.setVisibility(View.VISIBLE);
            textViewMessage.setText(getString(R.string.app_right_has_been_revoked));
        }
        else {
            relativeContactAdmin.setVisibility(View.GONE);
        }

        if(preferences.isLicenceExpire()){
            relativeContactAdmin.setVisibility(View.VISIBLE);
            textViewMessage.setText(getString(R.string.licence_expired_message));
        }
        else {
            relativeContactAdmin.setVisibility(View.GONE);
        }


        relativeAbout = (RelativeLayout) rootView.findViewById(R.id.relative_aboutus);
        relativeHelp = (RelativeLayout) rootView.findViewById(R.id.relative_help);
        relativeFaq = (RelativeLayout) rootView.findViewById(R.id.relative_faqs);
        relativeContact = (RelativeLayout) rootView.findViewById(R.id.relative_contact);

        textViewAboutus=(TextView)rootView.findViewById(R.id.textview_aboutus);
        textViewHelp=(TextView)rootView.findViewById(R.id.textview_help);
        textViewVersionNo=(TextView)rootView.findViewById(R.id.textview_version_no);
        textViewSendFeedback=(TextView)rootView.findViewById(R.id.textview_send_feedback);

        textViewVersionNo.setTypeface(GlobalClass.fontLight);
        textViewSendFeedback.setTypeface(GlobalClass.fontLight);

        setLanguage(preferences.getLanguage());
        relativeAbout.setOnClickListener(this);
        relativeHelp.setOnClickListener(this);
        relativeFaq.setOnClickListener(this);
        relativeContact.setOnClickListener(this);

        PackageInfo pInfo = null;
        try
        {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            textViewVersionNo.setText("Version "+version);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

    }
    @Override
    public void onClick(View v)
    {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        switch (v.getId()) {
            case R.id.relative_aboutus:

                fragment = new FragmentSupportAbout();
                transaction.replace(R.id.content_frame, fragment);
                transaction.addToBackStack("Support");
                transaction.commit();
                break;
            case R.id.relative_help:
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","appsupport@braintool.com", null));
                intent.putExtra(Intent.EXTRA_SUBJECT,subject);
                intent.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                break;
            case R.id.relative_contact:
                //FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragment = new FragmentSupportContact();
                transaction.replace(R.id.content_frame, fragment);
                transaction.addToBackStack("Support");
                transaction.commit();
                break;
            case R.id.relative_faqs:
                /*FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentSupportFAQs fragment = new FragmentSupportFAQs();
                transaction.replace(R.id.content_frame, fragment);
                transaction.addToBackStack("Support");
                transaction.commit();*/
                break;
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.toolbar_menu_item, menu);
        this.menu = menu;
        menu.findItem(R.id.actionEdit).setVisible(false);
        menu.findItem(R.id.actionDefault).setVisible(false);
        menu.findItem(R.id.actionSupport).setVisible(false);
        menu.findItem(R.id.actionSend).setVisible(false);
        menu.findItem(R.id.actionprofile).setVisible(false);
    }
    public void setLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getResources().updateConfiguration(config,this.getResources().getDisplayMetrics());
        textViewAboutus.setText(getString(R.string.aboutus));
        textViewHelp.setText(getString(R.string.help));
        textViewSendFeedback.setText(getString(R.string.send_us_your_feedback));
    }
}