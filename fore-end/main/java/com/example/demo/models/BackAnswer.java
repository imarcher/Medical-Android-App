package com.example.demo.models;

import java.util.List;

public class BackAnswer {
    private User user;
    private Answer answer;
    private List<Picture> picture_urls;
    private List<BackComment> comments;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public List<Picture> getPicture_urls() {
        return picture_urls;
    }

    public void setPicture_urls(List<Picture> picture_urls) {
        this.picture_urls = picture_urls;
    }

    public List<BackComment> getComments() {
        return comments;
    }

    public void setComments(List<BackComment> comments) {
        this.comments = comments;
    }
}
