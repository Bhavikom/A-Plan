package de.smacsoftwares.aplanapp.adapter;

/**
 * Created by SSoft-13 on 17-06-2016.
 */
// this adapter is for task listview in dashboard  fragment

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.smacsoftwares.aplanapp.Model.TaskListModelClass;
import de.smacsoftwares.aplanapp.R;
import de.smacsoftwares.aplanapp.util.GlobalClass;
import de.smacsoftwares.aplanapp.util.LogApp;

import java.util.ArrayList;
import java.util.Locale;

public class TasklistAdapter extends BaseAdapter {
    String strTaskfilter = "", strProjectfilter = "";
    int filterType = 0;
    private ArrayList<TaskListModelClass> arraylistTemp;
    String strfilterType = "";
    Context context;
    ArrayList<TaskListModelClass> arraylistMain;
    ArrayList<TaskListModelClass> filterProject;
    ArrayList<TaskListModelClass> filterTask;
    private ArrayList<TaskListModelClass> arraylistFiltered;
    private ArrayList<TaskListModelClass> arraylistFilteredTemp;
    Boolean isTaskFilterd = false, isProjectFilter = false, isFiltered = false;
    Boolean isFiltered2 = false;

    public TasklistAdapter(Context context, ArrayList<TaskListModelClass> projectlist) {
        this.context = context;
        this.arraylistMain = projectlist;

        this.arraylistTemp = new ArrayList<TaskListModelClass>();
        this.arraylistTemp.addAll(arraylistMain);
        this.arraylistFiltered = new ArrayList<>();
        this.arraylistFilteredTemp = new ArrayList<>();
    }

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_list_project, null);
            /* initializing controls */
            LinearLayout linearMain = (LinearLayout) convertView.findViewById(R.id.linear_task_list_main);
            GlobalClass.setupTypeface(linearMain, GlobalClass.fontRegular);

            LinearLayout linearStatusImages = (LinearLayout) convertView.findViewById(R.id.linear_status_image);
            TextView textviewProjectName = (TextView) convertView.findViewById(R.id.textviewprojectname);
            TextView textviewTaskName = (TextView) convertView.findViewById(R.id.textviewtaskname);
            TextView textviewFinishedDate = (TextView) convertView.findViewById(R.id.textviewfinishdateitem);
            ImageView imageviewDate = (ImageView) convertView.findViewById(R.id.imgdateitem);
            ImageView imageviewTraffic = (ImageView) convertView.findViewById(R.id.imgtraficeitem);
            //ImageView imageviewStatus = (ImageView) convertView.findViewById(R.id.imgstatusitem);
            /* getting model */
            TaskListModelClass model = arraylistMain.get(position);
            /* setting values to controls */
            textviewProjectName.setText(model.getProjectName());
            textviewTaskName.setText(model.getName());

            textviewProjectName.setPaintFlags(textviewProjectName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            textviewTaskName.setPaintFlags(textviewTaskName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            //textviewFinishedDate.setPaintFlags(textviewFinishedDate.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
            /* getting end date and parsing as needed and printing on textview */
            String endDate = "";
            endDate = model.getEndDate();
            if (!TextUtils.isEmpty(endDate)) {
                textviewFinishedDate.setText(endDate);
            }
            /* getting traffic light text from model and showing traffic lights as needed */
            if (!TextUtils.isEmpty(model.getTrafficLight())) {
                if (model.getTrafficLight().equalsIgnoreCase("TrafficlightRed")) {
                    imageviewTraffic.setBackgroundResource(R.drawable.traffic_red);
                } else if (model.getTrafficLight().equalsIgnoreCase("TrafficLightYellow")) {
                    imageviewTraffic.setBackgroundResource(R.drawable.traffic_yellow);
                } else if (model.getTrafficLight().equalsIgnoreCase("TrafficLightGreen")) {
                    imageviewTraffic.setBackgroundResource(R.drawable.traffic_green);
                }
            } else {
                imageviewTraffic.setBackgroundResource(0);
            }
            /* getting Date text from model and showing date as needed */
            if (!TextUtils.isEmpty(model.getDateImage())) {
                if (model.getDateImage().equalsIgnoreCase("ShowOverdueFinished")) {
                    imageviewDate.setBackgroundResource(R.drawable.completion);
                } else if (model.getDateImage().equalsIgnoreCase("ShowFinishToday")) {
                    imageviewDate.setBackgroundResource(R.drawable.completed_today);
                } else if (model.getDateImage().equalsIgnoreCase("ShowFinishWeek")) {
                    imageviewDate.setBackgroundResource(R.drawable.completed_in_one_week);
                } else if (model.getDateImage().equalsIgnoreCase("ShowStarted")) {
                    imageviewDate.setBackgroundResource(R.drawable.started_task);
                } else if (model.getDateImage().equalsIgnoreCase("ShowPending")) {
                    imageviewDate.setBackgroundResource(R.drawable.pending_task);
                } else if (model.getDateImage().equalsIgnoreCase("ShowNoTime")) {
                    imageviewDate.setBackgroundResource(R.drawable.task_without_date);
                } else if (model.getDateImage().equalsIgnoreCase("ShowPassiv")) {
                    imageviewDate.setBackgroundResource(R.drawable.passive);
                } else if (model.getDateImage().equalsIgnoreCase("ShowCanceled")) {
                    imageviewDate.setBackgroundResource(R.drawable.canceled_task);
                }
            } else {
                imageviewDate.setBackgroundResource(0);
            }
            /* getting status text from model and showing images as needed */
            if (model.getStatusArray().size() > 0) {
                for (int i = 0; i < model.getStatusArray().size(); i++) {

                    ImageView image = new ImageView(context);
                    image.setLayoutParams(new ViewGroup.LayoutParams(70, 70));
                    image.setMaxHeight(70);
                    image.setMaxWidth(70);

                    // Adds the view to the layout
                    linearStatusImages.addView(image);

                    if (model.getStatusArray().get(i).equalsIgnoreCase("ShowArrow")) {
                        image.setBackgroundResource(R.drawable.notice_arrow);
                    } else if (model.getStatusArray().get(i).equalsIgnoreCase("ShowQuestion")) {
                        image.setBackgroundResource(R.drawable.not_clear);
                    } else if (model.getStatusArray().get(i).equalsIgnoreCase("ShowCritical")) {
                        image.setBackgroundResource(R.drawable.critical);
                    } else if (model.getStatusArray().get(i).equalsIgnoreCase("ShowReminder")) {
                        image.setBackgroundResource(R.drawable.reminder);
                    } else if (model.getStatusArray().get(i).equalsIgnoreCase("ShowFinished")) {
                        image.setBackgroundResource(R.drawable.completed_task);
                    } else if (model.getStatusArray().get(i).equalsIgnoreCase("ShowOverdueLimit")) {
                        image.setBackgroundResource(R.drawable.limit);
                    } else if (model.getStatusArray().get(i).equalsIgnoreCase("ShowCapacityTooHigh")) {
                        image.setBackgroundResource(R.drawable.capacity_too_high);
                    } else if (model.getStatusArray().get(i).equalsIgnoreCase("ShowCapacityTooLow")) {
                        image.setBackgroundResource(R.drawable.capacity_too_low);
                    }
                }
            }
            LogApp.e("", " image and status object : " + " status : " + model.getStatusObj() + " traffic : " + model.getTrafficLightObj());
        }
        return convertView;
    }

    /* filter from task name */
    public void filterTask(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arraylistMain.clear();

        if (charText.length() > 0) {
            strTaskfilter = charText;
            if (arraylistFiltered.size() > 0) {
                arraylistFilteredTemp.clear();
                arraylistFilteredTemp.addAll(arraylistFiltered);
                arraylistFiltered.clear();
                for (TaskListModelClass wp : arraylistFilteredTemp) {
                    if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        arraylistMain.add(wp);
                        arraylistFiltered.add(wp);

                    }
                }
            } else {
                arraylistFiltered.clear();
                for (TaskListModelClass wp : arraylistTemp) {
                    if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        arraylistMain.add(wp);
                        arraylistFiltered.add(wp);
                    }
                }
            }
        } else {
            strTaskfilter = "";
            if (strProjectfilter.equalsIgnoreCase("")) {
                arraylistMain.clear();
                arraylistMain.addAll(arraylistTemp);
            } else {
                arraylistFiltered.clear();
                filterProject(strProjectfilter);
            }
        }
        notifyDataSetChanged();
    }

    /* filter from project name */
    public void filterProject(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arraylistMain.clear();
        if (charText.length() > 0) {
            strProjectfilter = charText;
            if (arraylistFiltered.size() > 0) {
                arraylistFilteredTemp.clear();
                arraylistFilteredTemp.addAll(arraylistFiltered);
                arraylistFiltered.clear();
                for (TaskListModelClass wp : arraylistFilteredTemp) {
                    if (wp.getProjectName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        arraylistMain.add(wp);
                        arraylistFiltered.add(wp);

                    }
                }
            } else {
                arraylistFiltered.clear();
                for (TaskListModelClass wp : arraylistTemp) {
                    if (wp.getProjectName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        arraylistMain.add(wp);
                        arraylistFiltered.add(wp);
                    }
                }
            }
        } else {
            strProjectfilter = "";
            if (strTaskfilter.equalsIgnoreCase("")) {
                arraylistMain.clear();
                arraylistMain.addAll(arraylistTemp);
            } else {
                arraylistFiltered.clear();
                filterTask(strTaskfilter);
                //arraylistMain.clear();
                //arraylistMain.addAll( arraylistFiltered);
            }
        }
        notifyDataSetChanged();
    }

    public ArrayList<TaskListModelClass> filterListview(String task, String project, ArrayList<String> arrayListFinishDates,
                                                        ArrayList<String> arrayListTrafficlight, ArrayList<String> arraylistStatus, ArrayList<String> arraylistDate,
                                                        ArrayList<TaskListModelClass> arraylistallTask) {
        arraylistMain.clear();
        arraylistFiltered.clear();
        if (!TextUtils.isEmpty(task)) {
            for (TaskListModelClass wp : arraylistallTask) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(task)) {
                    arraylistFiltered.add(wp);
                }
            }
        } else {
            arraylistFiltered.addAll(arraylistallTask);
        }
        if (!TextUtils.isEmpty(project)) {
            arraylistFilteredTemp = new ArrayList<>();
            arraylistFilteredTemp.addAll(arraylistFiltered);
            arraylistFiltered.clear();
            for (TaskListModelClass wp : arraylistFilteredTemp) {
                if (wp.getProjectName().toLowerCase(Locale.getDefault()).contains(project)) {
                    arraylistFiltered.add(wp);
                    //arraylistMain.add(wp);
                }
            }
        }
        if (arrayListFinishDates.size() > 0) {
            arraylistFilteredTemp = new ArrayList<>();
            arraylistFilteredTemp.addAll(arraylistFiltered);
            arraylistFiltered.clear();
            for (TaskListModelClass taskModel : arraylistFilteredTemp) {
                //LogApp.e(""," comparison of traffic lights : "+wp.getTrafficLight().toLowerCase((Locale.getDefault()))+"  char text : "+charText);
                for (int i = 0; i < arrayListFinishDates.size(); i++) {
                    if (taskModel.getEndDate().toLowerCase((Locale.getDefault())).equalsIgnoreCase(arrayListFinishDates.get(i))) {
                        arraylistFiltered.add(taskModel);
                    }
                }
            }
        }
        if (arrayListTrafficlight.size() > 0) {
            arraylistFilteredTemp = new ArrayList<>();
            arraylistFilteredTemp.addAll(arraylistFiltered);
            arraylistFiltered.clear();
            for (TaskListModelClass taskModel : arraylistFilteredTemp) {
                //LogApp.e(""," comparison of traffic lights : "+wp.getTrafficLight().toLowerCase((Locale.getDefault()))+"  char text : "+charText);
                for (int i = 0; i < arrayListTrafficlight.size(); i++) {
                    if (taskModel.getTrafficLight().toLowerCase((Locale.getDefault())).equalsIgnoreCase(arrayListTrafficlight.get(i))) {
                        arraylistFiltered.add(taskModel);
                    }
                }

            }
        }
        if (arraylistStatus.size() > 0) {
            arraylistFilteredTemp = new ArrayList<>();
            arraylistFilteredTemp.addAll(arraylistFiltered);
            arraylistFiltered.clear();
            for (TaskListModelClass taskModel : arraylistFilteredTemp) {
                //LogApp.e(""," comparison of traffic lights : "+wp.getTrafficLight().toLowerCase((Locale.getDefault()))+"  char text : "+charText);
                for (int i = 0; i < arraylistStatus.size(); i++) {
                    for (int k = 0; k < taskModel.getStatusArray().size(); k++) {
                        if (taskModel.getStatusArray().get(k).toLowerCase((Locale.getDefault())).equalsIgnoreCase(arraylistStatus.get(i))) {
                            arraylistFiltered.add(taskModel);
                        }
                    }

                }
            }
        }
        if (arraylistDate.size() > 0) {
            arraylistFilteredTemp = new ArrayList<>();
            arraylistFilteredTemp.addAll(arraylistFiltered);
            arraylistFiltered.clear();
            for (TaskListModelClass taskModel : arraylistFilteredTemp) {
                //LogApp.e(""," comparison of traffic lights : "+wp.getTrafficLight().toLowerCase((Locale.getDefault()))+"  char text : "+charText);
                for (int i = 0; i < arraylistDate.size(); i++) {
                    if (taskModel.getDateImage().toLowerCase((Locale.getDefault())).equalsIgnoreCase(arraylistDate.get(i))) {
                        arraylistFiltered.add(taskModel);
                    }
                }
            }
        }
        arraylistMain.addAll(arraylistFiltered);
        arraylistFiltered.clear();
        notifyDataSetChanged();
        return arraylistMain;
    }
}
