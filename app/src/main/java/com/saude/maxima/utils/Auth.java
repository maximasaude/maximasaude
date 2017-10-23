package com.saude.maxima.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Junnyor on 20/10/2017.
 */

public class Auth{

    static JSONObject auth;

    private static Context context;

    public Auth(Context context){
        auth = new JSONObject();
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        if(sharedPreferences != null){
            String user = sharedPreferences.getString("user", null);
            if(user != null) {
                try {
                    auth.put("auth", new JSONObject(user));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     *
     * @param data
     */
    public static void setAuth(JSONObject data){
        if(data != null){
            try {
                auth.put("auth", data);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else {
            try {
                auth.put("auth", "");
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public JSONObject getAuth(){
        try {
            if (auth.getJSONObject("auth") != null && auth.has("auth")) {
                return auth.getJSONObject("auth");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isLogged(){
        try {
            if(auth.getJSONObject("auth") != null && auth.has("auth")){
                return true;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return false;
    }


}
