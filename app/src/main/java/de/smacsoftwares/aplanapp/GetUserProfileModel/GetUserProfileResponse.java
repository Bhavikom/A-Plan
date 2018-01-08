
package de.smacsoftwares.aplanapp.GetUserProfileModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class GetUserProfileResponse
{
    @SerializedName("Message")
    @Expose
    private Object message;
    @SerializedName("Payload")
    @Expose
    private List<Payload> payload = new ArrayList<Payload>();
    @SerializedName("Status")
    @Expose
    private Integer status;
    /**
     *
     * @return
     *     The message
     */
    public Object getMessage() {
        return message;
    }

    /**
     *
     * @param message
     *     The Message
     */
    public void setMessage(Object message) {
        this.message = message;
    }

    /**
     *
     * @return
     *     The payload
     */
    public List<Payload> getPayload() {
        return payload;
    }

    /**
     *
     * @param payload
     *     The Payload
     */
    public void setPayload(List<Payload> payload) {
        this.payload = payload;
    }

    /**
     * 
     * @return
     *     The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The Status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

}
