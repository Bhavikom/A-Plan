package de.smacsoftwares.aplanapp.Model;

/**
 * Created by SSoft-13 on 19-07-2016.
 */
// this is model class for navigation drawer adapter listview
public class DrawerModelClass
{
    String title="";
    int Drawable;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getDrawable()
    {
        return Drawable;
    }

    public void setDrawable(int drawable)
    {
        Drawable = drawable;
    }
}
