package com.saude.maxima.Adapters.Package;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by junnyor on 12/18/17.
 */

public class Category implements Serializable{

    private JSONObject category;

    public Category(JSONObject category) {
        this.category = category;
    }

    public String getName(){
        String name = "";
        try {
            name = this.getCategory().getString("name");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return name;
    }

    public JSONObject getCategory() {
        return category;
    }

    public void setCategorie(JSONObject category) {
        this.category = category;
    }
}
