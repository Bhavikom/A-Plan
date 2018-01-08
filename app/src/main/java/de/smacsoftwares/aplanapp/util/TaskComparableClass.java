package de.smacsoftwares.aplanapp.util;

import java.text.SimpleDateFormat;
import java.util.Comparator;

import de.smacsoftwares.aplanapp.Model.TaskListModelClass;

/**
 * Created by SSoft-13 on 18-07-2016.
 */
// this is comparable class to sort custom listview in dashboard
public class TaskComparableClass implements Comparator<TaskListModelClass> {
    private int sortingVia = 0;
    private int type = 0;
    private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    public TaskComparableClass(int sortingVia, int type) {
        this.sortingVia = sortingVia;
        this.type = type;
    }
    private int sortProejectName(TaskListModelClass sourceModel, TaskListModelClass compareWithModel) {
        return sourceModel.getProjectName().compareToIgnoreCase(compareWithModel.getProjectName());
    }
    private int sortTaskName(TaskListModelClass sourceModel, TaskListModelClass compareWithModel) {
        return sourceModel.getName().compareToIgnoreCase(compareWithModel.getName());
    }
    @Override
    public int compare(TaskListModelClass sourceModel, TaskListModelClass compareWithModel) {
        {
            switch (sortingVia) {
                case 1:
                    if(type == 0)
                    {
                        return sortProejectName(sourceModel, compareWithModel);
                    }
                    else
                    {
                        return sortProejectName(compareWithModel, sourceModel);
                    }
                case 2:
                    if(type == 0)
                    {
                        return sortTaskName(sourceModel, compareWithModel);
                    }
                    else
                    {
                        return sortTaskName(compareWithModel, sourceModel);
                    }

            }
            return 0;
        }
    }
}
