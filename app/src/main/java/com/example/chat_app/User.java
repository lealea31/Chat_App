package com.example.chat_app;

import java.io.Serializable;

public class User implements Serializable
{


    private String id;
    private String name;
    private String img_url;


    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public String getImg_url() {
        return img_url == null? "": img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

}