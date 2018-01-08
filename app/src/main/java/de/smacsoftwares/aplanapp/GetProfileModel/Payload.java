
package de.smacsoftwares.aplanapp.GetProfileModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColsDetail() {
        return colsDetail;
    }

    public void setColsDetail(String colsDetail) {
        this.colsDetail = colsDetail;
    }

    public String getExpandedIds() {
        return expandedIds;
    }

    public void setExpandedIds(String expandedIds) {
        this.expandedIds = expandedIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
