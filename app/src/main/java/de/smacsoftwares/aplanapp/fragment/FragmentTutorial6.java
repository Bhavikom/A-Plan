package de.smacsoftwares.aplanapp.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.util.GlobalClass;

/**
 * Created by SSOFT4 on 11/18/2016.
 */


public class FragmentTutorial6 extends Fragment
{
    LinearLayout linearMain;
    TextView textViewTitle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_tutorial6, container,false);
        GlobalClass.fontRegular = Typeface.createFromAsset(getActivity().getAssets(), GlobalClass.fontPath);
        linearMain=(LinearLayout)rootView.findViewById(R.id.linear_tutorial6_main);
        GlobalClass.setupTypeface(linearMain, GlobalClass.fontLight);
        textViewTitle = (TextView)rootView.findViewById(R.id.lable_title6);
        textViewTitle.setTypeface(GlobalClass.fontBold);
        return rootView;
    }
}
