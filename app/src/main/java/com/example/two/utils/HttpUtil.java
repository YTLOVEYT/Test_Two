package com.example.two.utils;

import android.util.Log;

import com.example.two.testapi.JokeDataApi;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络访问
 * Created by YinTao on 2018/1/9.
 */

public class HttpUtil
{
    private static final String TAG = "HttpUtil";
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;

    private HttpUtil()
    {

    }

    private static void init()
    {
        okHttpClient = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor()
                {
                    @Override
                    public Response intercept(Chain chain) throws IOException
                    {
                        Request request = chain.request();
                        long t1 = System.nanoTime();
                        // request.body().toString(); 会被关闭流
                        Log.e(TAG, "intercept-request url=" + request.url()
                                + "\nconnection=" + chain.connection()
                                + "\nheaders=" + request.headers());
                        Response response = chain.proceed(request);
                        long t2 = System.nanoTime();
                        ResponseBody responseBody = response.peekBody(1024 * 1024);
                        Log.e(TAG, "intercept-response:url=" + response.request().url()
                                + "\nresponseBody" + responseBody.string()
                                + "\ntime=" + (t2 - t1) + "\nheader" + response.headers());
                        return response;
                    }
                })
                .build();
        retrofit = new Retrofit.Builder().client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://v.juhe.cn/joke/")
                .build();
    }

    /** 返回 JokeData */
    public static JokeDataApi getJokeDataApi()
    {
        if (retrofit == null || okHttpClient == null)
        {
            init();
        }
        return retrofit.create(JokeDataApi.class);
    }
    /** 返回 JokeData */
    public static JokeDataApi getImageApi()
    {
        if (retrofit == null || okHttpClient == null)
        {
            init();
        }
        return retrofit.create(JokeDataApi.class);
    }
}
