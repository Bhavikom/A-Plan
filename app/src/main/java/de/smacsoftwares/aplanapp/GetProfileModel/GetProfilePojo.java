
package de.smacsoftwares.aplanapp.GetProfileModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProfilePojo
{
    @SerializedName("Message")
    @Expose
    private Object message;
    @SerializedName("Payload")
    @Expose
    private List<Payload> payload = null;
    @SerializedName("Status")
    @Expose
    private Integer status;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public List<Payload> getPayload() {
        return payload;
    }

    public void setPayload(List<Payload> payload) {
        this.payload = payload;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
