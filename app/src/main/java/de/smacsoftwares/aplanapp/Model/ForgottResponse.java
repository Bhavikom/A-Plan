
package de.smacsoftwares.aplanapp.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// this class is to handle forgott password response
public class ForgottResponse
{

    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Payload")
    @Expose
    private Object payload;
    @SerializedName("Status")
    @Expose
    private int status;

    /**
     * 
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     *     The Message
     */
    public void setMessage(String message) {
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
    public int getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The Status
     */
    public void setStatus(int status) {
        this.status = status;
    }

}
