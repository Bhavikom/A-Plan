package de.smacsoftwares.aplanapp.Model;

/**
 * Created by SSoft-13 on 17-06-2016.
 */
// this is for agreegate listview in dashboard fragment
public class AgreegateDataModelClass
{
    public String getStrNo()
    {
        return strNo;
    }

    public void setStrNo(String strNo)
    {
        this.strNo = strNo;
    }

    public String getStrName()
    {
        return strName;
    }

    public void setStrName(String strName)
    {
        this.strName = strName;
    }

    String strNo,strName;
    int drawable;

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getJsonString()
    {
        return jsonString;
    }

    public void setJsonString(String jsonString)
    {
        this.jsonString = jsonString;
    }

    String jsonString;

    public String getStrGroupId()
    {
        return strGroupId;
    }

    public void setStrGroupId(String strGroupId)
    {
        this.strGroupId = strGroupId;
    }

    String strGroupId="";

    public boolean isClickable()
    {
        return isClickable;
    }

    public void setClickable(boolean clickable)
    {
        isClickable = clickable;
    }

    boolean isClickable;

}
