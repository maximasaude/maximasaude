package com.saude.maxima;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saude.maxima.utils.Auth;
import com.saude.maxima.utils.Routes;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView txtCreate;

    TextView emailUser;
    TextView nameUser;

    ProgressBar progressBar;

    Activity activity;

    String url = "";
    String params = "";
    Toolbar toolbar;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = this;

        //Verifico se o usuário está logado
        if(Auth.isLogged()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtCreate = (TextView) findViewById(R.id.txtCreate);
        progressBar = (ProgressBar) findViewById(R.id.progress);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Função que verifica se há conexão com a internet
     * @return boolean
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void login(View view){
        if(isOnline()){
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            if(email.isEmpty() || password.isEmpty()){
                edtEmail.setText(R.string.field_required);
                edtPassword.setText(R.string.field_required);
                Toast.makeText(getApplicationContext(), "Preencha os campos", Toast.LENGTH_SHORT).show();
            }else {
                params = "username="+email;
                params += "&password="+password+"&grant_type=password";
                params += "&client_id=2";
                params += "&client_secret=GUmSvywQ3uUuWEnD2Uv9enqq3j8jtMjyU59Cb0qT";
                params += "&scope=";
                new getAccessTokenUser().execute(Routes.takeToken);
            }
        }else{
            Toast.makeText(getApplicationContext(), "Não há conexão com a internet", Toast.LENGTH_SHORT).show();
        }

    }

    public void create(View view){
        Intent intent = new Intent(this, CreateUserActivity.class);
        startActivity(intent);
    }

    /**
     * Implementation of AsyncTask designed to fetch data from the network.
     */
    private class getAccessTokenUser extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
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
                    Toast.makeText(getApplicationContext(), "Usuário ou Senha inválidos", Toast.LENGTH_LONG).show();

                    progressBar.setVisibility(View.GONE);
                }
            }catch (JSONException e){
                progressBar.setVisibility(View.GONE);
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

                    //Pegando os dados de retorno
                    JSONObject data = result.getJSONObject("success");

                    //Setando o email do usuário no cabeçalho do menu lateral
                    //emailUser = (TextView) findViewById(R.id.email);
                    //emailUser.setText(data.getString("name"));

                    //Setando o nome do usuário no cabeçalho do menu lateral
                    //nameUser = (TextView) findViewById(R.id.name);
                    //nameUser.setText(data.getString("email"));

                    //Adicionando os dados do usuário
                    SharedPreferences sharedPreferences = activity.getSharedPreferences("user", Context.MODE_PRIVATE);
                    sharedPreferences.edit().putString("user", data.toString()).commit();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    setResult(RESULT_OK, intent);
                    finish();

                    //Iniciando a transição para a tela home
                    //FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    //HomeFragment homeFragment = new HomeFragment();
                    //fragmentTransaction.replace(R.id.content_fragment, homeFragment);
                    //fragmentTransaction.addToBackStack(getString(R.string.addToBackStack));
                    //fragmentTransaction.commit();
                }
            }catch (JSONException e){
                progressBar.setVisibility(View.GONE);
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
