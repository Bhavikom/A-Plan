package de.smacsoftwares.aplanapp.adapter;

/**
 * Created by SSoft-13 on 17-06-2016.
 */
// this Adapter is for setting adapter in dashboard agreegate listview

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.Model.TermsConditionModel;
import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.util.GlobalClass;

import java.util.List;

public class TermsConditionAdapter extends BaseAdapter {
    Context context;
    List<TermsConditionModel> arraylist;
    public TermsConditionAdapter(Context context, List<TermsConditionModel> items) {
        this.context = context;
        this.arraylist = items;
    }
    /*private view holder class*/
    private class ViewHolder {
        TextView textviewTitle;
        TextView textvewMessage;
        LinearLayout linearLayout;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.terms_item, null);
            holder = new ViewHolder();
            holder.textviewTitle = (TextView) convertView.findViewById(R.id.item_title);
            holder.textvewMessage = (TextView) convertView.findViewById(R.id.item_message);
            holder.linearLayout = (LinearLayout)convertView.findViewById(R.id.main);

            GlobalClass.fontLight = Typeface.createFromAsset(context.getAssets(), GlobalClass.fontPathLight);
            GlobalClass.fontBold = Typeface.createFromAsset(context.getAssets(), GlobalClass.fontPathBold);

            GlobalClass.setupTypeface(holder.linearLayout, GlobalClass.fontLight);
            convertView.setTag(holder);



        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position == 0){
            holder.textvewMessage.setTextColor((context.getResources().getColor(R.color.red)));
            holder.textvewMessage.setTypeface(GlobalClass.fontBold);
            holder.textviewTitle.setTypeface(GlobalClass.fontBold);
            holder.textviewTitle.setGravity(Gravity.CENTER);
        }else {
            holder.textvewMessage.setTextColor((context.getResources().getColor(R.color.black_color)));
            holder.textvewMessage.setTypeface(GlobalClass.fontLight);
            holder.textviewTitle.setTypeface(GlobalClass.fontLight);
            holder.textviewTitle.setGravity(Gravity.LEFT|Gravity.CENTER);
        }
        holder.textviewTitle.setText(arraylist.get(position).getStrTitle());
        holder.textvewMessage.setText(arraylist.get(position).getStrMessage());
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
