package com.saude.maxima;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saude.maxima.utils.Auth;
import com.saude.maxima.utils.Routes;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    AutoCompleteTextView edtEmail, edtPassword;
    Button btnLogin;
    TextView txtCreate;

    TextView emailUser;
    TextView nameUser;

    LinearLayout content;

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
    public void onResume(){
        super.onResume();
        clearChecked();
        navigationView.getMenu().findItem(R.id.nav_login).setChecked(true);
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            finish();
        }else if (id == R.id.nav_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_cart) {
            Intent intent = new Intent(this, DiaryActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_report) {
            Intent intent = new Intent(this, ReportActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.nav_perfil) {
            Intent intent = new Intent(this, EditUserActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_login, frameLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_login);
        setActionBarDrawerToggle(toolbar);

        navigationView.getMenu().findItem(R.id.nav_login).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        activity = this;

        //Verifico se o usuário está logado
        if(Auth.isLogged()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this, R.string.you_are_logged, Toast.LENGTH_LONG).show();
        }

        content = (LinearLayout) findViewById(R.id.content);

        edtEmail = (AutoCompleteTextView) findViewById(R.id.edtEmail);
        edtPassword = (AutoCompleteTextView) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtCreate = (TextView) findViewById(R.id.txtCreate);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                login(textView);
                return false;

            }
        });
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
                edtEmail.setError(getString(R.string.field_required));
                edtPassword.setError(getString(R.string.field_required));
                Toast.makeText(getApplicationContext(), "Preencha os campos", Toast.LENGTH_SHORT).show();
            }else {
                params = "username="+email;
                params += "&password="+password+"&grant_type=password";
                params += "&client_id=2";
                params += "&client_secret=RkohvyMvNAjViUDlTdQjx5dvSw2dlYaUTvu5CvGq";
                params += "&scope=";

                content.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

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
                }
            }catch (JSONException e){

            }
            progressBar.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
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
                    sharedPreferences.edit().putString("user", data.toString()).apply();

                    finish();

                    //Iniciando a transição para a tela home
                    //FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    //HomeFragment homeFragment = new HomeFragment();
                    //fragmentTransaction.replace(R.id.content_fragment, homeFragment);
                    //fragmentTransaction.addToBackStack(getString(R.string.addToBackStack));
                    //fragmentTransaction.commit();
                }
            }catch (JSONException e){

            }
            progressBar.setVisibility(View.GONE);
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
