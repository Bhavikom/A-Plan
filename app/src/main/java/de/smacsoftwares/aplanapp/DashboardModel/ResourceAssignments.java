
package de.smacsoftwares.aplanapp.DashboardModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ResourceAssignments {

    @SerializedName("assignmentdata")
    @Expose
    private List<Assignmentdatum> assignmentdata = new ArrayList<Assignmentdatum>();
    @SerializedName("resources")
    @Expose
    private List<Resource> resources = new ArrayList<Resource>();

    /**
     * 
     * @return
     *     The assignmentdata
     */
    public List<Assignmentdatum> getAssignmentdata() {
        return assignmentdata;
    }

    /**
     * 
     * @param assignmentdata
     *     The assignmentdata
     */
    public void setAssignmentdata(List<Assignmentdatum> assignmentdata) {
        this.assignmentdata = assignmentdata;
    }

    /**
     * 
     * @return
     *     The resources
     */
    public List<Resource> getResources() {
        return resources;
    }

    /**
     * 
     * @param resources
     *     The resources
     */
    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

}
