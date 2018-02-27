package com.example.two.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 网络请求返回数据
 * Created by YinTao on 2018/1/9.
 */

public class DataEntity implements Serializable
{
    @SerializedName("reason")
    private String reason;
    @SerializedName("error_code")
    private int error_code;
    @SerializedName("result")
    private List<Joke> jokes;

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public int getError_code()
    {
        return error_code;
    }

    public void setError_code(int error_code)
    {
        this.error_code = error_code;
    }

    public List<Joke> getJokes()
    {
        return jokes;
    }

    public void setJokes(List<Joke> jokes)
    {
        this.jokes = jokes;
    }

    @Override
    public String toString()
    {
        return "DataEntity{" +
                "reason='" + reason + '\'' +
                ", error_code=" + error_code +
                ", jokes=" + jokes +
                '}';
    }
}
