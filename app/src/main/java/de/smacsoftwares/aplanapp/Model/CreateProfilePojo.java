
package de.smacsoftwares.aplanapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateProfilePojo
{


    @SerializedName("Message")
    @Expose
    private Object message;
    @SerializedName("Payload")
    @Expose
    private PayloadCreateProfile payload;
    @SerializedName("Status")
    @Expose
    private Integer status;

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public PayloadCreateProfile getPayloadCreateProfile() {
        return payload;
    }

    public void setPayload(PayloadCreateProfile payload) {
        this.payload = payload;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}