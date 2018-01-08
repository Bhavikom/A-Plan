
package de.smacsoftwares.aplanapp.DashboardModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Assignmentdatum {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("ResouceType")
    @Expose
    private Integer resouceType;
    @SerializedName("ResourceId")
    @Expose
    private String resourceId;
    @SerializedName("TaskId")
    @Expose
    private String taskId;
    @SerializedName("Type")
    @Expose
    private Integer type;
    @SerializedName("Units")
    @Expose
    private Integer units;

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The Id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The resouceType
     */
    public Integer getResouceType() {
        return resouceType;
    }

    /**
     * 
     * @param resouceType
     *     The ResouceType
     */
    public void setResouceType(Integer resouceType) {
        this.resouceType = resouceType;
    }

    /**
     * 
     * @return
     *     The resourceId
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * 
     * @param resourceId
     *     The ResourceId
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * 
     * @return
     *     The taskId
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * 
     * @param taskId
     *     The TaskId
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * 
     * @return
     *     The type
     */
    public Integer getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The Type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The units
     */
    public Integer getUnits() {
        return units;
    }

    /**
     * 
     * @param units
     *     The Units
     */
    public void setUnits(Integer units) {
        this.units = units;
    }

}
