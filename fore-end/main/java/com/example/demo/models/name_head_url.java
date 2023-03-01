package com.example.demo.models;


/**
 * 登录IM临时用的
 */
public class name_head_url {

    private String name;
    private String head_url;

    public name_head_url(String name, String head_url) {
        this.name = name;
        this.head_url = head_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }
}
