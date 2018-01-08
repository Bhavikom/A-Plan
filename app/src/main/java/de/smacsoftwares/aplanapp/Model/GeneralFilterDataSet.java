package de.smacsoftwares.aplanapp.Model;

/**
 * Created by SSoft-13 on 11-04-2017.
 */

public class GeneralFilterDataSet {
    String profileId="";
    String key="";
    String value="";
    String type="";
    String valueLocal="";

    public String getValueLocal() {
        return valueLocal;
    }

    public void setValueLocal(String valueLocal) {
        this.valueLocal = valueLocal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
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

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    String lang="";
}
