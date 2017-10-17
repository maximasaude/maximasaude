package com.saude.maxima;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginFragment extends Fragment {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView txtCreate;

    String url = "";
    String params = "";
    private static String[] routes = {
        "http://10.0.0.103:8000/oauth/token",
        "http://10.0.0.103:8000/api/user"
    };

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        txtCreate = (TextView) view.findViewById(R.id.txtCreate);

        txtCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddUserFragment addUserFragment = new AddUserFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_fragment, addUserFragment, "add_user");
                fragmentTransaction.addToBackStack("add_user");
                fragmentTransaction.commit();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()){
                    String email = edtEmail.getText().toString();
                    String password = edtPassword.getText().toString();
                    if(email.isEmpty() || password.isEmpty()){
                        Toast.makeText(getContext(), "Preencha os campos", Toast.LENGTH_SHORT).show();
                    }else {
                        url = routes[0];
                        params = "username="+email;
                        params += "&password="+password+"&grant_type=password";
                        params += "&client_id=2";
                        params += "&client_secret=LGbti3QiyHEv3RURLckseKR1laX6v2zDCqpkr6LG";
                        params += "&scope=";
                        new getAccessTokenUser().execute(url);
                    }
                }else{
                    Toast.makeText(getContext(), "Não há conexão com a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void login(View view){
        //Toast.makeText(getContext(), "Teste", Toast.LENGTH_LONG).show();
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Implementation of AsyncTask designed to fetch data from the network.
     */
    private class getAccessTokenUser extends AsyncTask<String, Void, String> {

        /**
         * Defines work to perform on the background thread.
         */
        @Override
        protected String doInBackground(String... urls) {
            return Connection.post(urls[0], params);
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try{
                JSONObject jsonObject = new JSONObject(result);
                if(jsonObject.has("success")){
                    String tokenType = jsonObject.getJSONObject("success").getString("token_type");
                    String accessToken = jsonObject.getJSONObject("success").getString("access_token");
                    new getUser(tokenType, accessToken, params).execute(routes[1]);
                }else{
                    Toast.makeText(getContext(), "Usuário ou Senha inválidos", Toast.LENGTH_LONG).show();
                }
            }catch (JSONException e){
                Log.d("error1", e.getMessage());
            }

            /*if(!result.equals("401")){
                try{
                    JSONObject jsonObject = new JSONObject(result);
                    String tokenType = jsonObject.get("token_type").toString();
                    String accessToken = jsonObject.get("access_token").toString();
                    new getUser(tokenType, accessToken, params).execute(routes[1]);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(getContext(), "Usuário ou Senha inválidos", Toast.LENGTH_LONG).show();
            }*/
        }


        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(String result) {
        }
    }

    /**
     * Implementation of AsyncTask designed to fetch data from the network.
     */
    private class getUser extends AsyncTask<String, Void, String> {

        String type, accessToken, params;

        private getUser(String type, String accessToken, String params){
            this.setType(type);
            this.setAccessToken(accessToken);
            this.setParams(params);
        }

        /**
         * Defines work to perform on the background thread.
         */
        @Override
        protected String doInBackground(String... urls) {
            return Connection.get(this.getType(), this.getAccessToken(), urls[0], null);
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try{
                JSONObject jsonObject = new JSONObject(result);
                if(!jsonObject.has("error")){
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    HomeFragment homeFragment = new HomeFragment();
                    fragmentTransaction.replace(R.id.content_fragment, homeFragment);
                    fragmentTransaction.commit();
                }
            }catch (JSONException e){
                Log.d("error1", e.getMessage());
            }
        }


        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(String result) {
        }



        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }


    }



}
