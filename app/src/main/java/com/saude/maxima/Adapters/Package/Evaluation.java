package com.saude.maxima.Adapters.Package;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

/**
 * Created by Junnyor on 16/11/2017.
 */

public class Evaluation {

    private JSONObject anthropometry;
    private JSONObject evaluation;
    private JSONObject user;

    public Evaluation(JSONObject evaluation, JSONObject anthropometry, JSONObject user){
        this.evaluation = evaluation;
        this.anthropometry = anthropometry;
        this.user = user;
    }

    public String getObjective(){
        String objective = "";
        try{
            objective = this.evaluation.getString("objective");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return objective;
    }

    public JSONObject getAnthropometry(){
        return this.anthropometry;
    }

    public JSONObject getUser(){
        return this.user;
    }

    public JSONObject getEvaluation(){
        return this.evaluation;
    }

    public String getCreatedAtFormatted(){
        String createdAtFormatted = "";
        try{
            String[] date = this.evaluation.getString("created_at").split("-");
            createdAtFormatted = date[2].split(" ")[0]+"/"+date[1]+"/"+date[0];
        }catch (JSONException e){
            e.printStackTrace();
        }
        return createdAtFormatted;
    }

    public String getCreatedAt(){
        String createdAt = "";
        try{
            createdAt = this.evaluation.getString("created_at");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return createdAt;
    }

    public String getWeight(){
        String weight = "";
        try{
            weight = this.evaluation.getString("weight");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return weight;
    }

    public String getRightArm(){
        String rightArm = "";
        try{
            rightArm = this.anthropometry.getString("right_arm");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return rightArm;
    }

    public String getLeftArm(){
        String leftArm = "";
        try{
            leftArm = this.anthropometry.getString("left_arm");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return leftArm;
    }

    public String getTummy(){
        String tummy = "";
        try{
            tummy = this.anthropometry.getString("tummy");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return tummy;
    }

    public String getHip(){
        String hip = "";
        try{
            hip = this.anthropometry.getString("hip");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return hip;
    }

    public String getCoxaProximal(){
        String coxaProximal = "";
        try{
            coxaProximal = this.anthropometry.getString("coxa_proximal");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return coxaProximal;
    }

    public String getCoxaMedial(){
        String coxaMedial = "";
        try{
            coxaMedial = this.anthropometry.getString("coxa_medial");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return coxaMedial;
    }

    public String getCoxaDistal(){
        String coxaDistal = "";
        try{
            coxaDistal = this.anthropometry.getString("coxa_distal");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return coxaDistal;
    }

    public String getRightLeg(){
        String coxaRightLeg = "";
        try{
            coxaRightLeg = this.anthropometry.getString("right_leg");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return coxaRightLeg;
    }

    public String getLeftLeg(){
        String coxaLeftLeg = "";
        try{
            coxaLeftLeg = this.anthropometry.getString("left_leg");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return coxaLeftLeg;
    }

    public String getForearm(){
        String forearm = "";
        try{
            forearm = this.anthropometry.getString("forearm");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return forearm;
    }

    public String getChest(){
        String chest = "";
        try{
            chest = this.anthropometry.getString("chest");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return chest;
    }

    public String getWaist(){
        String waist = "";
        try{
            waist = this.anthropometry.getString("waist");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return waist;
    }

    public String getHeight(){
        String height = "";
        try{
            height = this.anthropometry.getString("height");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return height;
    }

    public String getFinalConsideration(){
        String final_consideration = "";
        try{
            final_consideration = this.evaluation.getString("final_consideration");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return final_consideration;
    }

    public String getValidity(){
        String validity = "";
        try{
            validity = this.evaluation.getString("validity");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return validity;
    }

    public String getUserName(){
        String userName = "";
        try{
            userName = this.user.getString("name");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return userName;
    }

    public String getUserEmail(){
        String userEmail = "";
        try{
            userEmail = this.user.getString("email");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return userEmail;
    }

    public String getUserBirthDate(){
        String userBirthDate = "";
        try{
            userBirthDate = this.user.getString("birth_date");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return userBirthDate;
    }

}
