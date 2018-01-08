package de.smacsoftwares.aplanapp.Model;

/**
 * Created by SSoft-13 on 25-11-2016.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

// this class is to handle login response
@Generated("org.jsonschema2pojo")
public class LoginPojo
{
    @SerializedName("Message")
    @Expose
    private Object message;
    @SerializedName("Payload")
    @Expose
    private Payload payload;
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
    public Payload getPayload() {
        return payload;
    }

    /**
     *
     * @param payload
     *     The Payload
     */
    public void setPayload(Payload payload) {
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