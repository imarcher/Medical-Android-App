package com.example.demo.models;

public class DocterOneComment {

    private int id;
    private String content;
    private String create_time;
    private int docter_id;
    private String patient_username;
    private String patient_head_url;
    private int patient_id;
    private int order_id;

    public String getPatient_username() {
        return patient_username;
    }

    public void setPatient_username(String patient_username) {
        this.patient_username = patient_username;
    }

    public String getPatient_head_url() {
        return patient_head_url;
    }

    public void setPatient_head_url(String patient_head_url) {
        this.patient_head_url = patient_head_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getDocter_id() {
        return docter_id;
    }

    public void setDocter_id(int docter_id) {
        this.docter_id = docter_id;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
