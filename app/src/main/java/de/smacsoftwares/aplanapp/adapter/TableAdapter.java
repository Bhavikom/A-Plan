package de.smacsoftwares.aplanapp.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import de.smacsoftwares.aplanapp.Model.TableDataModelClass;
import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.util.GlobalClass;

import java.util.ArrayList;
import java.util.List;

// this adapter is for table fragment listview
public class TableAdapter extends BaseAdapter {
    Context context;
    LayoutInflater lInflater;
    List<TableDataModelClass> arraylistMain;
    public TableAdapter(Context context, List<TableDataModelClass> items) {
        this.context = context;
        this.arraylistMain = items;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    private class ViewHolder
    {
        TextView textviewName;
        EditText editTextwidth;
        ToggleButton toggleButton;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_list_table, parent, false);
        }
        TableDataModelClass tabledata = getTableData(position);

        ((TextView) view.findViewById(R.id.textviewtableitem)).setText(tabledata.name);
        EditText edittextWidth= (EditText)view.findViewById(R.id.edittexttableitem);
        edittextWidth.setText(tabledata.width+"");
        //((ImageView) view.findViewById(R.id.ivImage)).setImageResource(tabledata.image);
        if(GlobalClass.isEditMode){
            edittextWidth.setEnabled(true);
        }
        else {
            edittextWidth.setEnabled(false);
        }

        LinearLayout linearMain = (LinearLayout)view.findViewById(R.id.linear_table_item_main);
        GlobalClass.setupTypeface(linearMain, GlobalClass.fontLight);

        ToggleButton cbBuy = (ToggleButton) view.findViewById(R.id.switchnumber);
        cbBuy.setOnCheckedChangeListener(myCheckChangList);
        cbBuy.setTag(position);
        cbBuy.setChecked(tabledata.selected);

        if (position % 2 == 0) {

            linearMain.setBackgroundColor(context.getResources().getColor(R.color.select_filter));

        } else {

            linearMain.setBackgroundColor(context.getResources().getColor(R.color.white_color));

        }


        return view;
    }
    public TableDataModelClass getTableData(int position)
    {
        return ((TableDataModelClass) getItem(position));
    }

    /* this method returns all selected data from listview adapter */
    public ArrayList<TableDataModelClass> getSelectedData()
    {
        ArrayList<TableDataModelClass> arraylistSelected = new ArrayList<TableDataModelClass>();
        for (TableDataModelClass tableData : arraylistMain)
        {
            if (tableData.selected)
                arraylistSelected.add(tableData);
        }
        return arraylistSelected;
    }
    CompoundButton.OnCheckedChangeListener myCheckChangList = new CompoundButton.OnCheckedChangeListener()
    {
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
        {
            getTableData((Integer) buttonView.getTag()).selected = isChecked;
            getSelectedData();
        }
    };
    @Override
    public int getCount() {
        return arraylistMain.size();
    }
    @Override
    public Object getItem(int position) {
        return arraylistMain.get(position);
    }
    @Override
    public long getItemId(int position) {
        return arraylistMain.indexOf(getItem(position));
    }
}