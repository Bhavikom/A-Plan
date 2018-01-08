
package de.smacsoftwares.aplanapp.DashboardModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Payload {

    @SerializedName("__type")
    @Expose
    private String type;
    @SerializedName("DashboardAggregateData")
    @Expose
    private DashboardAggregateData dashboardAggregateData;
    @SerializedName("NodeData")
    @Expose
    private List<NodeDatum> nodeData = new ArrayList<NodeDatum>();
    @SerializedName("ResourceAssignments")
    @Expose
    private ResourceAssignments resourceAssignments;
    @SerializedName("TaskAssignedTree")
    @Expose
    private List<TaskAssignedTree> taskAssignedTree = new ArrayList<TaskAssignedTree>();
    @SerializedName("TaskCompleteNextFiveDayTree")
    @Expose
    private List<TaskCompleteNextFiveDayTree> taskCompleteNextFiveDayTree = new ArrayList<TaskCompleteNextFiveDayTree>();
    @SerializedName("TaskCompleteTodayTree")
    @Expose
    private List<Object> taskCompleteTodayTree = new ArrayList<Object>();
    @SerializedName("TaskCompleteTree")
    @Expose
    private List<Object> taskCompleteTree = new ArrayList<Object>();
    @SerializedName("TaskPendingTree")
    @Expose
    private List<Object> taskPendingTree = new ArrayList<Object>();

    /**
     *
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     *     The __type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     *     The dashboardAggregateData
     */
    public DashboardAggregateData getDashboardAggregateData() {
        return dashboardAggregateData;
    }

    /**
     *
     * @param dashboardAggregateData
     *     The DashboardAggregateData
     */
    public void setDashboardAggregateData(DashboardAggregateData dashboardAggregateData) {
        this.dashboardAggregateData = dashboardAggregateData;
    }

    /**
     *
     * @return
     *     The nodeData
     */
    public List<NodeDatum> getNodeData() {
        return nodeData;
    }

    /**
     *
     * @param nodeData
     *     The NodeData
     */
    public void setNodeData(List<NodeDatum> nodeData) {
        this.nodeData = nodeData;
    }

    /**
     *
     * @return
     *     The resourceAssignments
     */
    public ResourceAssignments getResourceAssignments() {
        return resourceAssignments;
    }

    /**
     *
     * @param resourceAssignments
     *     The ResourceAssignments
     */
    public void setResourceAssignments(ResourceAssignments resourceAssignments) {
        this.resourceAssignments = resourceAssignments;
    }

    /**
     *
     * @return
     *     The taskAssignedTree
     */
    public List<TaskAssignedTree> getTaskAssignedTree() {
        return taskAssignedTree;
    }

    /**
     *
     * @param taskAssignedTree
     *     The TaskAssignedTree
     */
    public void setTaskAssignedTree(List<TaskAssignedTree> taskAssignedTree) {
        this.taskAssignedTree = taskAssignedTree;
    }

    /**
     *
     * @return
     *     The taskCompleteNextFiveDayTree
     */
    public List<TaskCompleteNextFiveDayTree> getTaskCompleteNextFiveDayTree() {
        return taskCompleteNextFiveDayTree;
    }

    /**
     * 
     * @param taskCompleteNextFiveDayTree
     *     The TaskCompleteNextFiveDayTree
     */
    public void setTaskCompleteNextFiveDayTree(List<TaskCompleteNextFiveDayTree> taskCompleteNextFiveDayTree) {
        this.taskCompleteNextFiveDayTree = taskCompleteNextFiveDayTree;
    }

    /**
     * 
     * @return
     *     The taskCompleteTodayTree
     */
    public List<Object> getTaskCompleteTodayTree() {
        return taskCompleteTodayTree;
    }

    /**
     * 
     * @param taskCompleteTodayTree
     *     The TaskCompleteTodayTree
     */
    public void setTaskCompleteTodayTree(List<Object> taskCompleteTodayTree) {
        this.taskCompleteTodayTree = taskCompleteTodayTree;
    }

    /**
     * 
     * @return
     *     The taskCompleteTree
     */
    public List<Object> getTaskCompleteTree() {
        return taskCompleteTree;
    }

    /**
     * 
     * @param taskCompleteTree
     *     The TaskCompleteTree
     */
    public void setTaskCompleteTree(List<Object> taskCompleteTree) {
        this.taskCompleteTree = taskCompleteTree;
    }

    /**
     * 
     * @return
     *     The taskPendingTree
     */
    public List<Object> getTaskPendingTree() {
        return taskPendingTree;
    }

    /**
     * 
     * @param taskPendingTree
     *     The TaskPendingTree
     */
    public void setTaskPendingTree(List<Object> taskPendingTree) {
        this.taskPendingTree = taskPendingTree;
    }

}
