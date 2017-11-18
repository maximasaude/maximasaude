package com.saude.maxima.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.saude.maxima.DashboardActivity;
import com.saude.maxima.MainActivity;
import com.saude.maxima.PaymentConclusionActivity;

import org.json.JSONObject;

public class Payment /*extends Observable */{
    private Context context;

    public Payment(Context context){
        this.context = context;
    }

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

    @JavascriptInterface
    public void redirect(){
        ((Activity) this.context).finish();
        Intent intent = new Intent(this.context, PaymentConclusionActivity.class);
        this.context.startActivity(intent);
    }


}