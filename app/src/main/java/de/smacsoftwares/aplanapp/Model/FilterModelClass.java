package de.smacsoftwares.aplanapp.Model;

/**
 * Created by SSoft-13 on 11-07-2016.
 */
// this is model class for filter data
public class FilterModelClass
{
    int drawable;
    String name;
    boolean selected;
    String key;
    String strTosendControl;

    public String getStrTosendControl() {
        return strTosendControl;
    }

    public void setStrTosendControl(String strTosendControl) {
        this.strTosendControl = strTosendControl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public int getDrawable()
    {
        return drawable;
    }

    public void setDrawable(int drawable)
    {
        this.drawable = drawable;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String  getStrOriginalValue()
    {
        return strOriginalValue;
    }

    public void setStrOriginalValue(String strOriginalValue)
    {
        this.strOriginalValue = strOriginalValue;
    }

    String strOriginalValue;
}
