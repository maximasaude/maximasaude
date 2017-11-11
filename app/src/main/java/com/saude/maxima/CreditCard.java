package com.saude.maxima;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.saude.maxima.utils.ManagerSharedPreferences;

import org.json.JSONException;

import java.util.Observable;
import java.util.Observer;

public class CreditCard /*extends Observable */{

    private String cardNumber;
    private String name;
    private String month;
    private String year;
    private String cvv;
    private int parcels;
    private String error;
    private String token;
    private String pagseguroSessionId;

    /*public CreditCard(Observer observer) {
        addObserver(observer);
    }*/

    @JavascriptInterface
    public String getCardNumber() {
        Log.i("cardNumber: ", cardNumber);
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @JavascriptInterface
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JavascriptInterface
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    @JavascriptInterface
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @JavascriptInterface
    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public int getParcels() {
        return parcels;
    }

    public void setParcels(int parcels) {
        this.parcels = parcels;
    }

    public String getError() {
        return error;
    }

    @JavascriptInterface
    public void setError(String errors) {
        /*for (String e : errors) {
            if (e.equalsIgnoreCase("card_number")) {
                error += "Número do cartão, inválido; ";
            }
        }*/
        Log.i("log", "error:" + errors);

        //setChanged();
        //notifyObservers();
    }

    public void setPagseguroSessionId(Context context){
        ManagerSharedPreferences managerSharedPreferences = new ManagerSharedPreferences(context);
        try {
            this.pagseguroSessionId = managerSharedPreferences.get("pagseguro_session_id").get("id").toString();
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public String getPagseguroSessionId(){
        return this.pagseguroSessionId;
    }

    @JavascriptInterface
    public void teste(String teste){

    }

    public String getToken() {
        return token;
    }

    @JavascriptInterface
    public void setToken(String token) {

        Log.i("setToken", "Token: " + token);
        //this.token = token;

        //setChanged();
        //notifyObservers();
    }
}