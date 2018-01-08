
package de.smacsoftwares.aplanapp.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// this class is to handle change password service response
public class ChangePassResponse
{

    @SerializedName("Message")
    @Expose
    private Object message;
    @SerializedName("Payload")
    @Expose
    private Object payload;
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
    public Object getPayload() {
        return payload;
    }

    /**
     * 
     * @param payload
     *     The Payload
     */
    public void setPayload(Object payload) {
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
