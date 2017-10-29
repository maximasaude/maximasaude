package com.saude.maxima;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.saude.maxima.utils.Auth;
import com.saude.maxima.utils.Routes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginFragment extends Fragment {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView txtCreate;

    TextView emailUser;
    TextView nameUser;

    private Auth auth;
    private JSONObject user;


    ProgressDialog progressDialog;

    String url = "";
    String params = "";


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        //Instancia para a classe auth
        this.auth = new Auth(getActivity().getApplicationContext());

        //Pegando os dados do usuário, caso esteja logado
        this.user = this.auth.getAuth();

        //Verifico se o usuário está logado
        if(this.auth.isLogged()){
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_fragment, new HomeFragment(), "home").commit();
        }

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        txtCreate = (TextView) view.findViewById(R.id.txtCreate);
        progressDialog = new ProgressDialog(getContext());

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
                        edtEmail.setText(R.string.field_required);
                        edtPassword.setText(R.string.field_required);
                        Toast.makeText(getContext(), "Preencha os campos", Toast.LENGTH_SHORT).show();
                    }else {
                        params = "username="+email;
                        params += "&password="+password+"&grant_type=password";
                        params += "&client_id=2";
                        params += "&client_secret=lwt2GU8ToG40P9UusKizYz2umr9rZlggfybJ502s";
                        params += "&scope=";
                        new getAccessTokenUser().execute(Routes.takeToken);
                    }
                }else{
                    Toast.makeText(getContext(), "Não há conexão com a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Função que verifica se há conexão com a internet
     * @return boolean
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Implementation of AsyncTask designed to fetch data from the network.
     */
    private class getAccessTokenUser extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.executing));
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        /**
         * Defines work to perform on the background thread.
         */
        @Override
        protected JSONObject doInBackground(String... urls) {
            return Connection.post(urls[0], params);
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            try{
                if(result.has("success")){
                    //Pegando o tipo de token
                    String tokenType = result.getJSONObject("success").getString("token_type");
                    //Pegando o token do usuário
                    String accessToken = result.getJSONObject("success").getString("access_token");

                    //Fazendo requisição para fazer login e buscar os dados do usuário
                    new getUser(tokenType, accessToken, params).execute(Routes.takeUser);
                }else{
                    Toast.makeText(getContext(), "Usuário ou Senha inválidos", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }catch (JSONException e){
                progressDialog.dismiss();
            }
        }

    }

    /**
     * Implementation of AsyncTask designed to fetch data from the network.
     */
    private class getUser extends AsyncTask<String, Void, JSONObject> {

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
        protected JSONObject doInBackground(String... urls) {
            return Connection.get(this.getType(), this.getAccessToken(), urls[0], null);
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            try{
                if(!result.has("error")){

                    //Desabilitando o progresso
                    progressDialog.dismiss();

                    //Pegando os dados de retorno
                    JSONObject data = result.getJSONObject("success");

                    //Setando o email do usuário no cabeçalho do menu lateral
                    emailUser = (TextView) getActivity().findViewById(R.id.email);
                    emailUser.setText(data.getString("name"));

                    //Setando o nome do usuário no cabeçalho do menu lateral
                    nameUser = (TextView) getActivity().findViewById(R.id.name);
                    nameUser.setText(data.getString("email"));

                    //Adicionando os dados do usuário
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("user", data.toString()).commit();

                    //Iniciando a transição para a tela home
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    HomeFragment homeFragment = new HomeFragment();
                    fragmentTransaction.replace(R.id.content_fragment, homeFragment);
                    fragmentTransaction.addToBackStack(getString(R.string.addToBackStack));
                    fragmentTransaction.commit();
                }
            }catch (JSONException e){
                progressDialog.dismiss();
            }
        }


        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(JSONObject result) {
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
