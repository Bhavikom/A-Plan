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
import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.util.GlobalClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SSOFT4 on 11/15/2016.
 */
// this adapter is for week popupview in project fragment
public class ProjectWeekAdapter extends BaseAdapter {
    Context context;
    List<FilterModelClass> arraylist;
    public ProjectWeekAdapter(Context context, List<FilterModelClass> items)
    {
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
            view = mInflater.inflate(R.layout.item_list_project_week, parent, false);
        }
        LinearLayout linearMain =(LinearLayout) view.findViewById(R.id.linear_week_main);
        GlobalClass.setupTypeface(linearMain, GlobalClass.fontRegular);
        TextView textvewDate = (TextView) view.findViewById(R.id.textweekmitem);
        textvewDate.setText(arraylist.get(position).getName());
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
    public ArrayList<FilterModelClass> getSelectedItem()
    {
        ArrayList<FilterModelClass> arraylistSelectedFilter = new ArrayList<FilterModelClass>();
        for (FilterModelClass p : arraylist) {
            if (p.isSelected())
                arraylistSelectedFilter.add(p);
        }
        return arraylistSelectedFilter;
    }
}

