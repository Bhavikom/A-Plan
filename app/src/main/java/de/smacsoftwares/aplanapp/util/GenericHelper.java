package de.smacsoftwares.aplanapp.util;

import android.content.Context;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by SSoft-13 on 29-03-2017.
 */

public class GenericHelper{
    Context con;
    PreferencesHelper preferences;
    public GenericHelper(Context con){
        this.con = con;
        preferences = new PreferencesHelper(con);
        //MyApplication.component(con).inject(this);
    }
    public RestApi getRetrofitGetUrl()
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                //.connectTimeout(600, TimeUnit.SECONDS)
                //.addInterceptor(new RequestInterceptor())
                .build();

        Retrofit service = new Retrofit.Builder().baseUrl(GlobalClass.GET_URL).
                addConverterFactory(ScalarsConverterFactory.create()).
                addConverterFactory(GsonConverterFactory.create()).
                client(okHttpClient).
                build();

        return service.create(RestApi.class);
    }
    public RestApi getRetrofit()
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                //.connectTimeout(600, TimeUnit.SECONDS)
                .addInterceptor(new RequestInterceptor())
                .build();

        Retrofit service = new Retrofit.Builder().baseUrl(preferences.getDomain()).
                addConverterFactory(ScalarsConverterFactory.create()).
                addConverterFactory(GsonConverterFactory.create()).
                client(okHttpClient).
                build();

        return service.create(RestApi.class);
    }
    public RestApi getRetrofitUserDetail()
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                //.connectTimeout(600, TimeUnit.SECONDS)
                //.addInterceptor(new RequestInterceptor())
                .build();

        Retrofit service = new Retrofit.Builder().baseUrl(GlobalClass.GET_USERT_DETAIL_URL).
                addConverterFactory(ScalarsConverterFactory.create()).
                addConverterFactory(GsonConverterFactory.create()).
                client(okHttpClient).
                build();

        return service.create(RestApi.class);
    }
    /*public  RestApi getRetrofitAccessToken()
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                //.connectTimeout(600, TimeUnit.SECONDS)
                .addInterceptor(new RequestInterceptor())
                .build();

        Retrofit service = new Retrofit.Builder().baseUrl(preferences.getDomain()).
                addConverterFactory(ScalarsConverterFactory.create()).
                addConverterFactory(GsonConverterFactory.create()).
                client(okHttpClient).
                build();

        return service.create(RestApi.class);
    }*/
    /*public  RestApi getRetrofitGroupList()
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                //.connectTimeout(600, TimeUnit.SECONDS)
                //.addInterceptor(new RequestInterceptor())
                .build();

        Retrofit service = new Retrofit.Builder().baseUrl(preferences.getDomain()).
                addConverterFactory(ScalarsConverterFactory.create()).
                addConverterFactory(GsonConverterFactory.create()).
                client(okHttpClient).
                build();
        return service.create(RestApi.class);
    }*/
    /*public  RestApi getRetrofitCreateProfile()
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                //.connectTimeout(600, TimeUnit.SECONDS)
                .addInterceptor(new RequestInterceptor())
                .build();

        Retrofit service = new Retrofit.Builder().baseUrl(preferences.getDomain()).
                addConverterFactory(ScalarsConverterFactory.create()).
                addConverterFactory(GsonConverterFactory.create()).
                client(okHttpClient).
                build();
        return service.create(RestApi.class);
    }
    public  RestApi getRetrofitDeleteProfile()
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                //.connectTimeout(600, TimeUnit.SECONDS)
                .addInterceptor(new RequestInterceptor())
                .build();

        Retrofit service = new Retrofit.Builder().baseUrl(preferences.getDomain()).
                addConverterFactory(ScalarsConverterFactory.create()).
                addConverterFactory(GsonConverterFactory.create()).
                client(okHttpClient).
                build();
        return service.create(RestApi.class);
    }
    public  RestApi getRetrofitUpdateProfile()
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                //.connectTimeout(600, TimeUnit.SECONDS)
                .addInterceptor(new RequestInterceptor())
                .build();

        Retrofit service = new Retrofit.Builder().baseUrl(preferences.getDomain()).
                addConverterFactory(ScalarsConverterFactory.create()).
                addConverterFactory(GsonConverterFactory.create()).
                client(okHttpClient).
                build();

        return service.create(RestApi.class);
    }
    public  RestApi getRetrofitSignOut()
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                //.connectTimeout(600, TimeUnit.SECONDS)
                .addInterceptor(new RequestInterceptor())
                .build();

        Retrofit service = new Retrofit.Builder().baseUrl(preferences.getDomain()).
                addConverterFactory(ScalarsConverterFactory.create()).
                addConverterFactory(GsonConverterFactory.create()).
                client(okHttpClient).
                build();
        return service.create(RestApi.class);
    }*/
}
