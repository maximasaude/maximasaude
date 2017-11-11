package com.saude.maxima.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by junnyor on 11/5/17.
 */

public class Diary {

    private int id;
    private String availableDate;
    private String description;
    private int isActive;
    private String createdAt;
    private String updatedAt;
    private List<DiaryHour> hourList;
    private int year;
    private int month;
    private int day;

    public Diary(int id, String availableDate, String description, int isActive, String createdAt, String updatedAt, List<DiaryHour> hourList) {
        this.id = id;
        this.availableDate = availableDate;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.hourList = hourList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(String availableDate) {
        this.availableDate = availableDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<DiaryHour> getHourList() {
        return hourList;
    }

    public void setHourList(List<DiaryHour> hourList) {
        this.hourList = hourList;
    }

    public void setYear(){
        this.year = Integer.parseInt(this.availableDate.split("-")[0]);
    }

    public int getYear(){
        return this.year = Integer.parseInt(this.availableDate.split("-")[0]);
    }

    public int getMonth(){
        return this.month = Integer.parseInt(this.availableDate.split("-")[1]);
    }

    public int getDay(){
        return this.day = Integer.parseInt(this.availableDate.split("-")[2]);
    }
}
