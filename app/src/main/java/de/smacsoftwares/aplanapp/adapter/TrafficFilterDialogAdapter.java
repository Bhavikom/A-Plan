package de.smacsoftwares.aplanapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.Model.FilterModelClass;
import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.util.GlobalClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SSoft-13 on 11-07-2016.
 */
// this adapter is for traffic dialog adapter in project screen
public class TrafficFilterDialogAdapter extends BaseAdapter {
    Context context;
    List<FilterModelClass> arraylist;
    public TrafficFilterDialogAdapter(Context context, List<FilterModelClass> items) {
        this.context = context;
        this.arraylist = items;
    }
    /*private view holder class*/
    private class ViewHolder {

        TextView textview;
        ImageView imageview;
    }
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_list_spinner, parent, false);
        }
        FilterModelClass filterModel = getFilterItem(position);

        RelativeLayout relativeMain = (RelativeLayout)view.findViewById(R.id.relative_spinner_main);
        GlobalClass.setupTypeface(relativeMain, GlobalClass.fontRegular);

        ImageView imageview = (ImageView) view.findViewById(R.id.imageviewfilterItem);
        TextView textview = (TextView) view.findViewById(R.id.textviewname);

        textview.setText(arraylist.get(position).getName());
        imageview.setBackgroundResource(arraylist.get(position).getDrawable());

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.cbBox);
        checkBox.setOnCheckedChangeListener(myCheckChangList);
        checkBox.setTag(position);
        checkBox.setChecked(filterModel.isSelected());

        view.setTag(R.id.title,textview);
        view.setTag(R.id.checkbox,checkBox);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arraylist.get(position).isSelected()==true) {
                    arraylist.get(position).setSelected(false);
                }else{
                    arraylist.get(position).setSelected(true);
                }
                notifyDataSetChanged();
            }
        });

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
    public ArrayList<FilterModelClass> getSelectedItem() {
        ArrayList<FilterModelClass> arraylistSelectedFilter = new ArrayList<FilterModelClass>();
        for (FilterModelClass p : arraylist) {
            if (p.isSelected())
                arraylistSelectedFilter.add(p);
        }
        return arraylistSelectedFilter;
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
