package de.smacsoftwares.aplanapp.util;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.Model.FilterModelClass;
import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.adapter.DatesSelectedDialogAdapter;

import java.util.ArrayList;

/**
 * Created by SSoft-13 on 01-07-2016.
 */
// this class is for showing selected date in popup in dashboard filter
public class CustomDialogSelectedDates {
    DatabaseHandler dbHandler;
    private IntefaceDialog interfaceListener;
    String selectedDates = null;
    ArrayList<FilterModelClass> arraylistFilterDates = new ArrayList<>();
    public CustomDialogSelectedDates(){
        this.interfaceListener=null;
    }
    public void setCustomObjectListener(IntefaceDialog listener) {
        this.interfaceListener = listener;
    }
    public void showDialog(final Activity activity, final ArrayList<FilterModelClass> arrayList)
    {
        // Create custom dialog object
        final Dialog dialog = new Dialog(activity);
        dbHandler = new DatabaseHandler(activity);
        // Include dialog.xml file
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_selected_dates);

        // Set dialog title
        dialog.setTitle("Custom Dialog");
        dialog.show();

        TextView textviewOk=(TextView)dialog.findViewById(R.id.ok);
        ListView listviewDates=(ListView)dialog.findViewById(R.id.listviewdates);
        listviewDates.setSelector(R.drawable.list_selector);
        final DatesSelectedDialogAdapter adapter = new DatesSelectedDialogAdapter(activity,arrayList);
        listviewDates.setAdapter(adapter);
        listviewDates.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                //dialog.dismiss();
            }
        });
        textviewOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ArrayList<FilterModelClass> arrayList = adapter.getSelectedItem();
                ArrayList<String> arrayStringName = new ArrayList<>();
                if(arrayList.size()> 0) {
                    //dbHandler.deleteSelectedITem("4");
                    for (int i = 0; i < arrayList.size(); i++) {
                        arrayStringName.add(arrayList.get(i).getName());
                    }
                    String commaSeperated="";
                    String commaSeperatedID="";
                    commaSeperated= TextUtils.join(",",arrayStringName);
                    //dbHandler.addSelectedItem("4",commaSeperated);
                    if(dialog != null){
                        dialog.dismiss();
                    }

                    IntefaceDialog intefaceDialog = (IntefaceDialog)activity;
                    intefaceDialog.clickedOkButton(arrayStringName);
                }else{
                    //dbHandler.deleteSelectedITem("4");
                    if(dialog != null){
                        dialog.dismiss();
                    }
                    IntefaceDialog intefaceDialog = (IntefaceDialog)activity;
                    intefaceDialog.clickedOkButton(arrayStringName);

                }
            }
        });
    }
    public interface  IntefaceDialog
    {
        public void clickedOkButton(ArrayList<String> dates);
    }
}
