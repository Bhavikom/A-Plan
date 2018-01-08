
package de.smacsoftwares.aplanapp.DashboardModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class DashboardAggregateData {

    @SerializedName("TaskAssigned")
    @Expose
    private Integer taskAssigned;
    @SerializedName("TaskComplete")
    @Expose
    private Integer taskComplete;
    @SerializedName("TaskCompleteNextFiveDay")
    @Expose
    private Integer taskCompleteNextFiveDay;
    @SerializedName("TaskCompleteToday")
    @Expose
    private Integer taskCompleteToday;
    @SerializedName("TaskPending")
    @Expose
    private Integer taskPending;

    /**
     * 
     * @return
     *     The taskAssigned
     */
    public Integer getTaskAssigned() {
        return taskAssigned;
    }

    /**
     * 
     * @param taskAssigned
     *     The TaskAssigned
     */
    public void setTaskAssigned(Integer taskAssigned) {
        this.taskAssigned = taskAssigned;
    }

    /**
     * 
     * @return
     *     The taskComplete
     */
    public Integer getTaskComplete() {
        return taskComplete;
    }

    /**
     * 
     * @param taskComplete
     *     The TaskComplete
     */
    public void setTaskComplete(Integer taskComplete) {
        this.taskComplete = taskComplete;
    }

    /**
     * 
     * @return
     *     The taskCompleteNextFiveDay
     */
    public Integer getTaskCompleteNextFiveDay() {
        return taskCompleteNextFiveDay;
    }

    /**
     * 
     * @param taskCompleteNextFiveDay
     *     The TaskCompleteNextFiveDay
     */
    public void setTaskCompleteNextFiveDay(Integer taskCompleteNextFiveDay) {
        this.taskCompleteNextFiveDay = taskCompleteNextFiveDay;
    }

    /**
     * 
     * @return
     *     The taskCompleteToday
     */
    public Integer getTaskCompleteToday() {
        return taskCompleteToday;
    }

    /**
     * 
     * @param taskCompleteToday
     *     The TaskCompleteToday
     */
    public void setTaskCompleteToday(Integer taskCompleteToday) {
        this.taskCompleteToday = taskCompleteToday;
    }

    /**
     * 
     * @return
     *     The taskPending
     */
    public Integer getTaskPending() {
        return taskPending;
    }

    /**
     * 
     * @param taskPending
     *     The TaskPending
     */
    public void setTaskPending(Integer taskPending) {
        this.taskPending = taskPending;
    }

}
