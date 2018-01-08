package de.smacsoftwares.aplanapp.util;


import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by SSoft-13 on 29-03-2017.
 */

public class RequestInterceptor implements Interceptor
{
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException
    {
        Request originalRequest = chain.request();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        Request newRequest = originalRequest.newBuilder()
                .header("Authorization", GlobalClass.strAccessToken+ GlobalClass.getEpochTime())
                .build();

        return chain.proceed(newRequest);
    }
}
