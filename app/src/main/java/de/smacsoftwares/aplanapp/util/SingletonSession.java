package de.smacsoftwares.aplanapp.util;

/**
 * Created by SSoft-13 on 11-03-2017.
 */

public class SingletonSession {

    private static SingletonSession instance;
    private String deviceToken="";
    boolean flag=false;
    //no outer class can initialize this class's object
    private SingletonSession() {}

    public static SingletonSession Instance()
    {
        //if no instance is initialized yet then create new instance
        //else return stored instance
        if (instance == null)
        {
            instance = new SingletonSession();
        }
        return instance;
    }

    public String getDiviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String username) {
        this.deviceToken = username;
    }
    public boolean isAppDisable() {
        return flag;
    }

    public void setAppDisable(boolean flag1) {
        this.flag = flag1;
    }
}
