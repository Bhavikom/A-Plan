package de.smacsoftwares.aplanapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.Model.FilterModelClass;
import de.smacsoftwares.aplanapp.Model.ProfileModelClass;
import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.util.GlobalClass;

import java.util.List;

/**
 * Created by SSoft-13 on 15-12-2016.
 */
// this adapter is for profile popupview in project fragment
public class ProfileAdapter extends BaseAdapter
{
    Context context;
    List<ProfileModelClass> arraylist;
    public ProfileAdapter(Context context, List<ProfileModelClass> items) {
        this.context = context;
        this.arraylist = items;
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
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_list_profile, parent, false);
        }
        TextView textviewDate = (TextView) view.findViewById(R.id.textview_profilename);
        textviewDate.setText(arraylist.get(position).getProfileName());
        LinearLayout linearMain=(LinearLayout)view.findViewById(R.id.linear_profile_item_main);
        GlobalClass.setupTypeface(linearMain, GlobalClass.fontRegular);
        return view;
    }
    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            getFilterItem((Integer) buttonView.getTag()).setSelected(isChecked);
        }
    };
    FilterModelClass getFilterItem(int position) {
        return ((FilterModelClass) getItem(position));
    }
}


