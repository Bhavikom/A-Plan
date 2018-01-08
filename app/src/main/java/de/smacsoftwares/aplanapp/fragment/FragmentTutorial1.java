package de.smacsoftwares.aplanapp.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.MyApplication;
import de.smacsoftwares.aplanapp.util.PreferencesHelper;

import javax.inject.Inject;

public class FragmentTutorial1 extends Fragment {
    PreferencesHelper preferences;
    ToggleButton toggleButton;
    TextView textViewLink,textviewWhatisAplan;
    LinearLayout linearMain;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tutorial1, container, false);
        //MyApplication.component(getActivity()).inject(this);
        preferences = new PreferencesHelper(getActivity());
        GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);
        linearMain=(LinearLayout)rootView.findViewById(R.id.linear_tutorial1_main);
        GlobalClass.setupTypeface(linearMain, GlobalClass.fontLight);
        textViewLink = (TextView) rootView.findViewById(R.id.textViewLink);
        textviewWhatisAplan = (TextView)rootView.findViewById(R.id.textview_lable_whatisaplan);
        textviewWhatisAplan.setTypeface(GlobalClass.fontBold);
        SpannableString ss = new SpannableString(getString(R.string.first_screen));
        ss.setSpan(new URLSpanNoUnderline("http://www.braintool.com"), 256, 273, 0);
        ss.setSpan(new ForegroundColorSpan(Color.BLUE), 256, 273, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       /* ss.setSpan(new MyClickableSpan(), 256, 273, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 32 to 46 stack is clickable*/
        textViewLink.setText(ss);
        textViewLink.setMovementMethod(LinkMovementMethod.getInstance());
        if (preferences.isTutorialShow()) {
        } else {
        }
        String text = "<font color=#000000>Was ist</font><font color=#A51317> A-Plan?</font>";
        textviewWhatisAplan.setText(Html.fromHtml(text));

        return rootView;
    }
    public class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }
}
