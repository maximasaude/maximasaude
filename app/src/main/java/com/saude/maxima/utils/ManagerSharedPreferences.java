package com.saude.maxima.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Junnyor on 21/10/2017.
 */

public class ManagerSharedPreferences {

    private Context context;

    public ManagerSharedPreferences(Context context){
        this.context = context;
    }

    public boolean remove(String name){
        SharedPreferences contentSharedPreferences = this.context.getSharedPreferences(name, Context.MODE_PRIVATE);
        if(contentSharedPreferences != null){
            if(contentSharedPreferences.contains(name)){
                if(contentSharedPreferences.edit().remove(name).commit()){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean has(String name){
        SharedPreferences contentSharedPreferences = this.context.getSharedPreferences(name, Context.MODE_PRIVATE);
        if(contentSharedPreferences.contains(name)){
            return true;
        }
        return false;
    }

    public JSONObject get(String name){
        try{
            SharedPreferences contentSharedPreferences = this.context.getSharedPreferences(name, Context.MODE_PRIVATE);
            JSONObject obj = new JSONObject(contentSharedPreferences.getString(name, null));
            return obj;
        }catch (JSONException e){
            Log.i("token", e.getMessage());
        }
        return null;
    }

    public void set(String index, String content){
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(index, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(index, content).apply();
    }

    public void set(String index, int content){
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(index, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(index, content).apply();
    }

    public void set(String index, float content){
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(index, Context.MODE_PRIVATE);
        sharedPreferences.edit().putFloat(index, content).apply();
    }
}
