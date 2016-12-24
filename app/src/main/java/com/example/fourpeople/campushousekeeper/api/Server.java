package com.example.fourpeople.campushousekeeper.api;

import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Administrator on 2016/12/10.
 */

public class Server {
    static OkHttpClient client;
    static
    {
        CookieManager cookieManager=new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client=new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();
    }
    public  static  OkHttpClient getSharedClient()
    {
        return client;
    }
    public static String serverAddress = "http://172.27.15.50:8080/membercenter/";
    public  static Request.Builder requestBuilderWithApi(String api)
    {
        return new Request.Builder()
//                .url("Http://172.27.0.36:8080/membercenter/api/"+api)
                .url(serverAddress+"api/"+api);
    }
    public  static Request.Builder requestBuilderWithPartTime(String parttime)
    {
        return new Request.Builder()
//                .url("Http://172.27.0.36:8080/membercenter/api/"+api)
                .url(serverAddress+"parttime/"+parttime);
    }

}
