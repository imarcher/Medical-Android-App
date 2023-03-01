package com.example.demo.models;

import java.util.List;

public class BackHead {
    private User user;
    private Post post;
    private List<Picture> picture_urls;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public List<Picture> getPicture_urls() {
        return picture_urls;
    }

    public void setPicture_urls(List<Picture> picture_urls) {
        this.picture_urls = picture_urls;
    }
}
