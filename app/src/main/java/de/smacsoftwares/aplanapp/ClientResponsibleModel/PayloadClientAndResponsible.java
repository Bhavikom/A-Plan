
package de.smacsoftwares.aplanapp.ClientResponsibleModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PayloadClientAndResponsible {

    @SerializedName("__type")
    @Expose
    private String type;
    @SerializedName("Clients")
    @Expose
    private List<Object> clients = null;
    @SerializedName("Resources")
    @Expose
    private List<Resource> resources = null;
    @SerializedName("ResponsiblePersons")
    @Expose
    private List<Object> responsiblePersons = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Object> getClients() {
        return clients;
    }

    public void setClients(List<Object> clients) {
        this.clients = clients;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public List<Object> getResponsiblePersons() {
        return responsiblePersons;
    }

    public void setResponsiblePersons(List<Object> responsiblePersons) {
        this.responsiblePersons = responsiblePersons;
    }

}
