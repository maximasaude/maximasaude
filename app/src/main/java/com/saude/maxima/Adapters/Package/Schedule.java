package com.saude.maxima.Adapters.Package;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

/**
 * Created by Junnyor on 16/11/2017.
 */

public class Schedule {

    private JSONObject packages;
    private JSONObject supplier;
    private JSONObject diary;
    private JSONObject schedule;
    private JSONObject diaryHour;
    private JSONObject order;

    public Schedule(JSONObject schedule, JSONObject order, JSONObject diary, JSONObject diaryHour, JSONObject packages, JSONObject supplier){
        this.packages = packages;
        this.schedule = schedule;
        this.supplier = supplier;
        this.diary = diary;
        this.diaryHour = diaryHour;
        this.order = order;
    }

    public String getPackageName(){
        String packageName = "";
        try{
            packageName = this.packages.getString("name");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return packageName;
    }

    public String getPackageDescription(){
        String packageDescription = "";
        try{
            packageDescription = this.packages.getString("description");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return packageDescription;
    }

    public String getPackageValueFormated(){
        String packageValue = "";
        try{
            packageValue = this.packages.getString("value");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
            double value;
            value = Double.parseDouble(packageValue);
            packageValue = numberFormat.format(value);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return packageValue;
    }

    public double getPackageValue(){
        double packageValue = 0;
        try{
            packageValue = this.packages.getDouble("value");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return packageValue;
    }

    public String getAddress(){
        String address = "";
        try{
            address = this.supplier.getString("address");
        }catch (JSONException e){
            e.printStackTrace();
        }

        return address;
    }

    public String getCity(){
        String city = "";
        try{
            city = this.supplier.getString("city");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return city;
    }

    public String getState(){
        String state = "";
        try{
            state = this.supplier.getString("state");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return state;
    }

    public String getNumber(){
        String number = "";
        try{
            number = this.supplier.getString("number");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return number;
    }

    public String getZip(){
        String zip = "";
        try{
            zip = this.supplier.getString("zip");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return zip;
    }

    public String getOperation(){
        String operation = "";
        try{
            operation = this.supplier.getString("operation");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return operation;
    }

    public String getPhone(){
        String phone = "";
        try{
            phone = this.supplier.getString("phone");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return phone;
    }

    public String getCellPhone(){
        String cellPhone = "";
        try{
            cellPhone = this.supplier.getString("cell_phone");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return cellPhone;
    }

    public String getDistrict(){
        String district = "";
        try{
            district = this.supplier.getString("district");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return district;
    }

    public String getCountry(){
        String country = "";
        try{
            country = this.supplier.getString("country");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return country;
    }

    public String getAddressComplement(){
        String addressComplement = "";
        try{
            addressComplement = this.supplier.getString("address_complement");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return addressComplement;
    }

    public int getId() {
        int id = 0;
        try{
             id = this.schedule.getInt("id");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return id;
    }

    public int getStatus(){
        int status = 0;
        try{
            status = this.order.getInt("status_payment_id");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return status;
    }

    public String getDay(){
        String day = "";
        try{
            day = this.diary.getString("available_date").split("-")[2];
        }catch (JSONException e){
            e.printStackTrace();
        }
        return day;
    }


    public String getMonth() {
        String month = "";
        try{
            month = this.diary.getString("available_date").split("-")[1];
        }catch (JSONException e){
            e.printStackTrace();
        }
        return month;
    }


    public String getYear() {
        String year = "";
        try{
            year = this.diary.getString("available_date").split("-")[0];
        }catch (JSONException e){
            e.printStackTrace();
        }
        return year;
    }


    public String getHour() {
        String hour = "";
        try{
            hour = this.diaryHour.getString("available_hour").split(":")[0];
        }catch (JSONException e){
            e.printStackTrace();
        }
        return hour;
    }


    public String getMinute() {
        String minute = "";
        try{
            minute = this.diaryHour.getString("available_hour").split(":")[1];
        }catch (JSONException e){
            e.printStackTrace();
        }
        return minute;
    }

    public String getSecond() {
        String second = "";
        try{
            second = this.diaryHour.getString("available_hour").split(":")[2];
        }catch (JSONException e){
            e.printStackTrace();
        }
        return second;
    }

}
