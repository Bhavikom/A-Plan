package de.smacsoftwares.aplanapp.util;

import android.util.Log;

// this class show logs for applicaiton
public class LogApp
{
    private static  boolean flag=true;
    public static void i(final String tag, final String msg) {
    if(flag==true)
    {
            Log.i(tag, msg);
        }
    }
    static void d(final String tag, final String msg)
    {
        if(flag==true)
        {
            Log.d(tag, msg);
        }
    }

    public static void e(final String tag, final String msg)
    {
        if(flag==true)
        {
            Log.e(tag, msg);
        }
    }

    public static void v(final String tag, final String msg)
    {

        if(flag==true)
        {
            Log.v(tag, msg);
        }
    }

    public static void w(final String tag, final String msg)
    {
        if(flag==true)
        {
            Log.w(tag, msg);
        }

    }
}