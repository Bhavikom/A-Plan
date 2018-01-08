package de.smacsoftwares.aplanapp.Model;

/**
 * Created by SSoft-13 on 23-08-2016.
 */

public class ProfileModelClass
{
    String profileId;
    String profileName;
    String expandedId;
    String language;
    String resolution;

    String colDeTail;
    String profileUserId;

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

    String key;
    String value;
    String valueLocal;
    String keyLocal;

    public String getValueLocal() {
        return valueLocal;
    }

    public void setValueLocal(String valueLocal) {
        this.valueLocal = valueLocal;
    }

    public String getKeyLocal() {
        return keyLocal;
    }

    public void setKeyLocal(String keyLocal) {
        this.keyLocal = keyLocal;
    }

    public String getResolution()
    {
        return resolution;
    }

    public void setResolution(String resolution)
    {
        this.resolution = resolution;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public String getExpandedId()
    {
        return expandedId;
    }

    public void setExpandedId(String expandedId)
    {
        this.expandedId = expandedId;
    }

    public String getProfileId()
    {
        return profileId;
    }

    public void setProfileId(String profileId)
    {
        this.profileId = profileId;
    }

    public String getProfileName()
    {
        return profileName;
    }

    public void setProfileName(String profileName)
    {
        this.profileName = profileName;
    }

    public String getColDeTail()
    {
        return colDeTail;
    }

    public void setColDeTail(String colDeTail)
    {
        this.colDeTail = colDeTail;
    }

    public String getProfileUserId()
    {
        return profileUserId;
    }

    public void setProfileUserId(String profileUserId)
    {
        this.profileUserId = profileUserId;
    }


}
