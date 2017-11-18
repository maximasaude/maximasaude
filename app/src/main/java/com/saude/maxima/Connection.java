package com.saude.maxima;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Junnyor on 15/10/2017.
 */

public class Connection {

    private static String accessToken = null;
    private static int client_id = 2;
    private static String client_secret = "p2NErCSjWHnT3OhqE83JaEttG0PWQxJLFuvEo1bE";
    private static String scope = "";
    private static JSONObject errors;
    private static JSONObject result;
    private static JSONObject success;


    /**
     *
     * @param tokenType
     * @param token
     * @param url_user
     * @param params
     * @return
     */
    public static JSONObject get(String tokenType, String token, String url_user, String params){
        URL url;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            url = new URL(url_user);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", tokenType+" "+token);
            urlConnection.setRequestProperty("Accept", "application/json");

            if(params != null) {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
                outputStreamWriter.write(params);
                outputStreamWriter.flush();
            }

            urlConnection.connect();

            int code = urlConnection.getResponseCode();

            BufferedReader bufferedReader;

            if(code >= 200 && code <= 299 ) {
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuffer response = new StringBuffer();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line+ "\n");
                }
                bufferedReader.close();

                //Adicionando o índice success ao retorno
                result = new JSONObject();

                try{
                    //Resposta de sucesso
                    success = new JSONObject(response.toString());
                    result.put("success", success);
                } catch (JSONException e){
                    try {
                        result.put("success", response.toString());
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }

                return result;
            }else{
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream(), "UTF-8"));
                StringBuffer response = new StringBuffer();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line+ "\n");
                }
                bufferedReader.close();

                result = new JSONObject();
                try{
                    errors = new JSONObject(response.toString());
                    result.put("errors", errors);
                }catch (JSONException e){
                    try {
                        result.put("errors", response.toString());
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                return result;
            }
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

    }


    /**
     *
     * @param url_user
     * @param params
     * @return
     */
    public static JSONObject get(String url_user, String params){
        URL url;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            url = new URL(url_user);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            //urlConnection.setRequestProperty("Accept", "application/json");

            urlConnection.connect();

            int code = urlConnection.getResponseCode();

            BufferedReader bufferedReader;

            if(code >= 200 && code <= 299 ) {
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuffer response = new StringBuffer();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line+ "\n");
                }
                bufferedReader.close();

                //Adicionando o índice success ao retorno
                result = new JSONObject();

                try{
                    //Resposta de sucesso
                    success = new JSONObject(response.toString());
                    result.put("success", success);
                } catch (JSONException e){
                    try{
                        result.put("success", new JSONArray(response.toString()));
                    }catch (JSONException ex){
                        ex.printStackTrace();
                        try{
                            result.put("success", response.toString());
                        }catch (JSONException exx){
                            e.printStackTrace();
                        }
                    }
                    /*try {
                        result.put("success", response.toString());
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }*/
                }

                return result;
            }else{
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream(), "UTF-8"));

                StringBuffer response = new StringBuffer();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line+ "\n");
                }
                bufferedReader.close();

                result = new JSONObject();
                try{
                    errors = new JSONObject(response.toString());
                    result.put("errors", errors);
                }catch (JSONException e){
                    try {
                        result.put("errors", response.toString());
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                return result;
            }
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

    }

    public static JSONObject post(String url_user, String params){
        URL url;
        HttpURLConnection connection = null;

        try {
            url = new URL(url_user);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", ""+Integer.toString(params.getBytes().length));
            connection.setRequestProperty("Content-Language", "pt-BR");

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            /*DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(params);
            dataOutputStream.flush();
            dataOutputStream.close();*/

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            outputStreamWriter.write(params);
            outputStreamWriter.flush();


            int code = connection.getResponseCode();

            BufferedReader bufferedReader;

            if(code >= 200 && code <= 299 ) {
                InputStream inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuffer response = new StringBuffer();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line+ "\n");
                }
                bufferedReader.close();

                //Adicionando o índice success ao retorno
                result = new JSONObject();

                try{
                    //Resposta de sucesso
                    success = new JSONObject(response.toString());
                    result.put("success", success);
                } catch (JSONException e){
                    result.put("success", response.toString());
                }

                return result;
            } else{
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
                StringBuffer response = new StringBuffer();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line+ "\n");
                }
                bufferedReader.close();

                result = new JSONObject();
                try{
                    errors = new JSONObject(response.toString());
                    result.put("errors", errors);
                } catch (JSONException e){
                    result.put("errors", response.toString());
                }
                return result;
            }

        }catch (Exception error){
            error.printStackTrace();
            Log.d("Error", error.getMessage());
        }finally {
            if(connection != null){
                connection.disconnect();
            }
        }
        return null;
    }
}
