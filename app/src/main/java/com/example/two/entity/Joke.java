package com.example.two.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 单个实例
 * Created by YinTao on 2018/1/9.
 */

public class Joke implements Serializable
{
    @SerializedName("content")
    private String content;
    @SerializedName("hashId")
    private String hashId;
    @SerializedName("unixtime")
    private String unixtime;
    @SerializedName("url")
    private String url;

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getHashId()
    {
        return hashId;
    }

    public void setHashId(String hashId)
    {
        this.hashId = hashId;
    }

    public String getUnixtime()
    {
        return unixtime;
    }

    public void setUnixtime(String unixtime)
    {
        this.unixtime = unixtime;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "Joke{" +
                "content='" + content + '\'' +
                ", hashId='" + hashId + '\'' +
                ", unixtime='" + unixtime + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
