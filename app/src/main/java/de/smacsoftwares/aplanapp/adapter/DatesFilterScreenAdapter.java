package de.smacsoftwares.aplanapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.util.GlobalClass;

import java.util.List;

/**
 * Created by SSoft-13 on 28-09-2016.
 */

public class DatesFilterScreenAdapter extends BaseAdapter {
    Context context;
    List<String> arraylist;
    public DatesFilterScreenAdapter(Context context, List<String> items) {
        this.context = context;
        this.arraylist = items;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_list_dates2, parent, false);
        }
        ///FilterModelClass filterModel = getFilterItem(position);
        LinearLayout linearMain =(LinearLayout)view.findViewById(R.id.linear_date_filter_main);
        GlobalClass.setupTypeface(linearMain, GlobalClass.fontRegular);
        TextView textvewDate = (TextView) view.findViewById(R.id.textdateitem);
        textvewDate.setText(arraylist.get(position));
        return view;
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

