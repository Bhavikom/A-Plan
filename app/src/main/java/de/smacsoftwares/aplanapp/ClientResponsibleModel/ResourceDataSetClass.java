package de.smacsoftwares.aplanapp.ClientResponsibleModel;

/**
 * Created by SSoft-13 on 21-12-2016.
 */

public class ResourceDataSetClass
{
    String ResourceId="";
    String ResourceName="";
    String IsDeleted="";

    public String getResourceId()
    {
        return ResourceId;
    }

    public void setResourceId(String resourceId)
    {
        ResourceId = resourceId;
    }

    public String getResourceName()
    {
        return ResourceName;
    }

    public void setResourceName(String resourceName)
    {
        ResourceName = resourceName;
    }

    public String isDeleted()
    {
        return IsDeleted;
    }

    public void setDeleted(String deleted)
    {
        IsDeleted = deleted;
    }

    public String getId()
    {
        return Id;
    }

    public void setId(String id)
    {
        Id = id;
    }

    public String getResourceType()
    {
        return ResourceType;
    }

    public void setResourceType(String resourceType)
    {
        ResourceType = resourceType;
    }

    public String getResourceUnit()
    {
        return ResourceUnit;
    }

    public void setResourceUnit(String resourceUnit)
    {
        ResourceUnit = resourceUnit;
    }

    String Id="";
    String ResourceType="";
    String ResourceUnit="";



}
