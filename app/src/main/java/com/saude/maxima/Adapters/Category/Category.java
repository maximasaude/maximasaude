package com.saude.maxima.Adapters.Category;

import com.saude.maxima.Adapters.Package.Package;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by junnyor on 12/18/17.
 */

public class Category implements Serializable{

    private String name;
    private String description;
    private List<Package> packages;

    public static final String key = "categories";

    public Category(String name, String description, List<Package> packages) {
        this.name = name;
        this.description = description;
        this.packages = packages;
    }

    public List<Package> getPackages(){
        return this.packages;
    }

    public String getDescription(){
        return this.description;
    }

    public String getName(){
        return this.name;
    }

}
