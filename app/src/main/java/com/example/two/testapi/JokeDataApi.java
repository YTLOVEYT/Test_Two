package com.example.two.testapi;

import com.example.two.entity.DataEntity;
import com.example.two.entity.RequestParam;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * API:http://v.juhe.cn/joke/
 * public static String KEY = "fcc736b44a9f3ed587971eb62276ff0b";//
 * param.put("key", Constant.KEY);
 * param.put("pagesize", pageSize);
 * param.put("page", currentPage);
 * param.put("type", "");
 * Created by YinTao on 2018/1/9.
 */

public interface JokeDataApi
{
    @GET("randJoke.php")
    Observable<DataEntity> getJokeData(@QueryMap RequestParam param);
}
