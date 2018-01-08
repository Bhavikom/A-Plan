
package de.smacsoftwares.aplanapp.GetUserProfileModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Payload {

    @SerializedName("__type")
    @Expose
    private String type;
    @SerializedName("ColsDetail")
    @Expose
    private String colsDetail;
    @SerializedName("ExpandedIds")
    @Expose
    private String expandedIds;
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("UserId")
    @Expose
    private Integer userId;

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
     *     The colsDetail
     */
    public String getColsDetail() {
        return colsDetail;
    }

    /**
     * 
     * @param colsDetail
     *     The ColsDetail
     */
    public void setColsDetail(String colsDetail) {
        this.colsDetail = colsDetail;
    }

    /**
     * 
     * @return
     *     The expandedIds
     */
    public String getExpandedIds() {
        return expandedIds;
    }

    /**
     * 
     * @param expandedIds
     *     The ExpandedIds
     */
    public void setExpandedIds(String expandedIds) {
        this.expandedIds = expandedIds;
    }

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
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The UserId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
