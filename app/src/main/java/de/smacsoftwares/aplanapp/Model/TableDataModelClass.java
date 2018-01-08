package de.smacsoftwares.aplanapp.Model;

import java.io.Serializable;

/**
 * Created by SSOFT4 on 7/25/2016.
 */
public class TableDataModelClass implements Serializable
{
    public String name;
    public String width;
    public int image;
    public boolean selected;
    public String Id;

    public TableDataModelClass() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public TableDataModelClass(String _name, String _width, int _image, boolean _selected, String id)
    {
        name = _name;
        width = _width;
        image = _image;

        selected = _selected;
        Id = id;
    }
}