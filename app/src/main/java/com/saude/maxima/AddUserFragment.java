package com.saude.maxima;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddUserFragment extends Fragment {

    EditText edtName, edtPassword, edtEmail, edtConfirmPassword;
    Button btnCancel, btnRegister;
    RadioGroup radioGroup;
    RadioButton gender;

    String url = "";
    String params = "";

    private static String[] routes = {
            "http://10.0.0.103:8000/oauth/token",
            "http://10.0.0.103:8000/api/user"
    };

    public AddUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);


        edtName = (EditText) view.findViewById(R.id.edtName);
        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);
        edtConfirmPassword = (EditText) view.findViewById(R.id.edtConfirmPassword);
        radioGroup = (RadioGroup) view.findViewById(R.id.radGender);
        btnRegister = (Button) view.findViewById(R.id.btnRegister);
        gender = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_fragment, new HomeFragment(), "home");
                fragmentTransaction.addToBackStack("home");
                fragmentTransaction.commit();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()){
                    String name = edtName.getText().toString();
                    String email = edtEmail.getText().toString();
                    String password = edtPassword.getText().toString();
                    String confirmPassword = edtConfirmPassword.getText().toString();
                    int sex = gender.getText().equals(R.string.man) ? 0 : 1;
                    if(name.isEmpty() || email.isEmpty() || password.isEmpty()){
                        Toast.makeText(getContext(), "Preencha os campos", Toast.LENGTH_SHORT).show();
                    }else {
                        url = "http://10.0.0.103:8000/api/users";
                        params = "name="+name;
                        params += "&email="+email;
                        params += "&password="+password;
                        params += "&confirm_password="+confirmPassword;
                        params += "&gender="+sex;
                        new create().execute(url);
                    }
                }else{
                    Toast.makeText(getContext(), "Não há conexão com a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
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
    private class create extends AsyncTask<String, Void, String> {

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
                JSONObject response = new JSONObject(result);
                if(response.has("success")){
                    String tokenType = response.getJSONObject("success").get("token").toString();
                    new getUser("Bearer", tokenType, params).execute(routes[1]);
                }else{
                    Toast.makeText(getContext(), "Ocorreu um erro ao cadastrar, tente novamente", Toast.LENGTH_LONG).show();
                }
            }catch (JSONException e){
                Log.d("response", e.getMessage());
            }
            /*if(!result.equals("401") || !result.equals("false")){
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_fragment, homeFragment, "home");
                fragmentTransaction.addToBackStack("home");
                fragmentTransaction.commit();

                Toast.makeText(getContext(), "Cadastrado com Sucesso", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getContext(), "Ocorreu um erro ao cadastrar", Toast.LENGTH_LONG).show();
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
                if(!jsonObject.has("errors")){
                    HomeFragment homeFragment = new HomeFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_fragment, homeFragment, "home");
                    fragmentTransaction.addToBackStack("home");
                    fragmentTransaction.commit();
                    Toast.makeText(getContext(), "Cadastrado com Sucesso", Toast.LENGTH_LONG).show();
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
