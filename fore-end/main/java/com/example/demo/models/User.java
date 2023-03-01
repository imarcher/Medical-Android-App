package com.example.demo.models;

public class User {
    private int id;
    private String username="default";
    private String head_url="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597761770519&di=bcd71f34be7ded9e0a4ef65c69b2575e&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201412%2F11%2F20141211235149_mdiAH.jpeg";
    private String token;
    private boolean isdocter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getIsdocter() {
        return isdocter;
    }

    public void setIsdocter(boolean isdocter) {
        this.isdocter = isdocter;
    }
}
