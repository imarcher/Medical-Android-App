package com.example.demo.models;

import java.util.List;

public class BackPost {
    private BackHead head;
    private List<BackAnswer> answers;

    public BackHead getHead() {
        return head;
    }

    public void setHead(BackHead head) {
        this.head = head;
    }

    public List<BackAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<BackAnswer> answers) {
        this.answers = answers;
    }
}
