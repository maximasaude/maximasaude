package com.saude.maxima.utils;

import android.webkit.JavascriptInterface;

import org.json.JSONObject;

public class Payment /*extends Observable */{

    private JSONObject items;
    private JSONObject auth;

    /*public CreditCard(Observer observer) {
        addObserver(observer);
    }*/

    @JavascriptInterface
    public String getAuth() {
        return auth.toString();
    }

    public void setAuth(JSONObject auth) {
        this.auth = auth;
    }

    @JavascriptInterface
    public String getItems() {
        return items.toString();
    }

    public void setItems(JSONObject items) {
        this.items = items;
    }


}