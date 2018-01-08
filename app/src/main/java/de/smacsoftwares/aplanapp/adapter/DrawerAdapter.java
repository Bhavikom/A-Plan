package de.smacsoftwares.aplanapp.adapter;

/**
 * Created by Credencys on 10/02/2016.
 */
// this adapter is for navigation drawer item adapter

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.Model.DrawerModelClass;
import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.util.GlobalClass;

import java.util.ArrayList;

public class DrawerAdapter extends BaseAdapter
{
    private Activity activity;
    private LayoutInflater minflater;
    int mSelectedItem;
    int disalbeItem;
    /**
     * LayoutInflater instance for inflating the requested layout in the list view
     */
    private LayoutInflater mInflater;

    private ArrayList<DrawerModelClass> mDataSet;
    /**
     * Default constructor
     */
    public DrawerAdapter(Context context, ArrayList<DrawerModelClass> dataSet) {
        mInflater = LayoutInflater.from(context);
        mDataSet = dataSet;
    }
    public void setInflater(LayoutInflater mInflater, Activity act) {
        this.minflater = mInflater;
        activity = act;
    }
    public void setSelectedItem(int selectedItem) {
        this.mSelectedItem = selectedItem;
    }
    public void setDisableItem(int disableItem){
        this.disalbeItem=disableItem;
    }
    public int getCount()
    {
        return mDataSet.size();
    }
    public Object getItem(int index)
    {
        return mDataSet.get(index);
    }
    public long getItemId(int index)
    {
        return index;
    }
    public View getView(int position, View recycledView, ViewGroup parent) {
        ViewHolder holder;
        if (recycledView == null) {
            holder = new ViewHolder();
            recycledView = mInflater.inflate(R.layout.item_list_drawer, parent, false);
            holder.title = (TextView) recycledView.findViewById(R.id.title);
            holder.icon = (ImageView) recycledView.findViewById(R.id.imgicon);
            holder.relativeMain = (RelativeLayout)recycledView.findViewById(R.id.relative_drawer_main);
            GlobalClass.setupTypeface(holder.relativeMain, GlobalClass.fontLight);



            recycledView.setTag(holder);

        }
        else {
            holder = (ViewHolder) recycledView.getTag();
        }
        holder.title.setText(mDataSet.get(position).getTitle());
        holder.icon.setBackgroundResource(mDataSet.get(position).getDrawable());
        if (position == mSelectedItem) {
            holder.relativeMain.setBackgroundColor(activity.getResources().getColor(R.color.grey_dashboard));
        } else {
            if (position % 2 == 0) {

                holder.relativeMain.setBackgroundColor(activity.getResources().getColor(R.color.select_filter));

            } else {

                holder.relativeMain.setBackgroundColor(activity.getResources().getColor(R.color.white_color));

            }
        }
        if(position==disalbeItem){
            //holder.title.setTextColor(activity.getResources().getColor(R.color.grey_light));
        }else {
            holder.title.setTextColor(activity.getResources().getColor(R.color.black_color));
        }
        if(position == 5 || position ==6){
            holder.relativeMain.setBackgroundColor(activity.getResources().getColor(R.color.white_color));
        }
        return recycledView;
    }
    private static class ViewHolder {
        TextView title;
        ImageView icon;
        RelativeLayout relativeMain;
    }
}