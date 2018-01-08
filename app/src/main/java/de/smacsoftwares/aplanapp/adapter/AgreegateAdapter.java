package de.smacsoftwares.aplanapp.adapter;

/**
 * Created by SSoft-13 on 17-06-2016.
 */
// this Adapter is for setting adapter in dashboard agreegate listview

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.Model.AgreegateDataModelClass;
import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.util.GlobalClass;

import java.util.List;

public class AgreegateAdapter extends BaseAdapter {
    Context context;
    List<AgreegateDataModelClass> arraylist;
    public AgreegateAdapter(Context context, List<AgreegateDataModelClass> items) {
        this.context = context;
        this.arraylist = items;
    }
    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView textviewNo;
        TextView textvewName;
        LinearLayout linearMain;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_list_agreegate, null);
            holder = new ViewHolder();
            holder.textvewName = (TextView) convertView.findViewById(R.id.textviewname);
            holder.textviewNo = (TextView) convertView.findViewById(R.id.textviewno);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img_agreegate);
            holder.linearMain =(LinearLayout)convertView.findViewById(R.id.linear_agreegate_main);
            GlobalClass.fontLight = Typeface.createFromAsset(context.getAssets(), GlobalClass.fontPathLight);
            GlobalClass.setupTypeface(holder.linearMain, GlobalClass.fontLight);
            convertView.setTag(holder);

            AgreegateDataModelClass rowItem = (AgreegateDataModelClass) getItem(position);
            holder.textvewName.setText(rowItem.getStrName());
            holder.textviewNo.setText(rowItem.getStrNo());
            holder.imageView.setBackgroundResource(rowItem.getDrawable());
            if(rowItem.isClickable())
            {
                holder.textvewName.setPaintFlags(holder.textvewName.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                holder.textviewNo.setPaintFlags(holder.textviewNo.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                holder.textvewName.setTextColor(context.getResources().getColor(R.color.indigo_color));
                holder.textviewNo.setTextColor(context.getResources().getColor(R.color.indigo_color));
            }
            else {
                holder.textvewName.setTypeface(GlobalClass.fontLight, Typeface.NORMAL);
                holder.textviewNo.setTypeface(GlobalClass.fontLight, Typeface.NORMAL);
                holder.textvewName.setTextColor(context.getResources().getColor(R.color.black_color));
                holder.textviewNo.setTextColor(context.getResources().getColor(R.color.black_color));
            }
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

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
