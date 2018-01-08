package de.smacsoftwares.aplanapp.Model;

/**
 * Created by SSoft-13 on 13-04-2017.
 */

public class RibbonFilterDataSet {
    String pId;
    String uId;
    String lang;
    String key;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueLocal() {
        return valueLocal;
    }

    public void setValueLocal(String valueLocal) {
        this.valueLocal = valueLocal;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    String value;
    String valueLocal;
    String resolution;

}
