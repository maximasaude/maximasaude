package com.saude.maxima.utils;

/**
 * Created by junnyor on 11/5/17.
 */

public class DiaryHour {

    private int id;
    private String availableHour;
    private int isActive;
    private String createdAt;
    private String updatedAt;
    private int hour;
    private int minute;
    private int second;


    public DiaryHour(int id, String availableHour, int isActive, String createdAt, String updatedAt) {
        this.id = id;
        this.availableHour = availableHour;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvailableHour() {
        return availableHour;
    }

    public void setAvailableHour(String availableHour) {
        this.availableHour = availableHour;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getHour(){
        return Integer.parseInt(this.availableHour.split(":")[0]);
    }

    public int getMinute(){
        return Integer.parseInt(this.availableHour.split(":")[1]);
    }

    public int getSecond(){
        return Integer.parseInt(this.availableHour.split(":")[2]);
    }
}
