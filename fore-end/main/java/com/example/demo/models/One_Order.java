package com.example.demo.models;

public class One_Order {
    private int id;
    private String docter_name;
    private String patient_name;
    private String create_time;
    private boolean iscommented;
    private int docter_id;
    private int patient_id;

    public boolean isIscommented() {
        return iscommented;
    }

    public void setIscommented(boolean iscommented) {
        this.iscommented = iscommented;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDocter_name() {
        return docter_name;
    }

    public void setDocter_name(String docter_name) {
        this.docter_name = docter_name;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
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
}
