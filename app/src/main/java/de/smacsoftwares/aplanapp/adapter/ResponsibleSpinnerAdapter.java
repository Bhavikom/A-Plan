package de.smacsoftwares.aplanapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.ClientResponsibleModel.ResponsibleDataSetClass;
import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.util.GlobalClass;

import java.util.ArrayList;

/**
 * Created by SSoft-13 on 03-08-2016.
 */
// this adapter is for responsible spinner in text filter screen
public class ResponsibleSpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<ResponsibleDataSetClass> arraylist;
    public ResponsibleSpinnerAdapter(Context context, ArrayList<ResponsibleDataSetClass> items) {
        this.context = context;
        this.arraylist = items;
    }
    /*private view holder class*/
    private class ViewHolder {
        TextView textview;
        RelativeLayout relativeMain;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.item_list_spinner2, null);
        holder = new ViewHolder();
        holder.textview = (TextView) convertView.findViewById(R.id.textviewname);
        holder.textview.setText(arraylist.get(position).getName());
        holder.relativeMain = (RelativeLayout)convertView.findViewById(R.id.relative_client_spinner_main);
        GlobalClass.setupTypeface(holder.relativeMain, GlobalClass.fontRegular);
        convertView.setTag(holder);
        return convertView;
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

