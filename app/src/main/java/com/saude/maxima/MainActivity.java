package com.saude.maxima;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.saude.maxima.utils.Auth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fm = getSupportFragmentManager();
    NavigationView navigationView = null;

    TextView name;
    TextView email;

    private Auth auth;
    private JSONObject user;

    SharedPreferences sharedPreferences;
    SharedPreferences.OnSharedPreferenceChangeListener spChange = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.i("Script alterado", key+"updated");
        }
    };

    /**
     * Função que verifica se há conexão com a internet
     * @return boolean
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        if(!isOnline()){

            Toast.makeText(this, "Não há conexão com a internet", Toast.LENGTH_SHORT).show();

        }else {


            drawer.setDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);

            sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            //sharedPreferences.registerOnSharedPreferenceChangeListener(spChange);

            if (savedInstanceState == null) {
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.add(R.id.content_fragment, homeFragment);
                fragmentTransaction.commit();
            }


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

            name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.name);
            email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);

            //Instancia para a classe auth
            this.auth = new Auth(getApplicationContext());

            //Pegando os dados do usuário, caso esteja logado
            this.user = this.auth.getAuth();

        /*if(sharedPreferences.contains("user")){
            navigationView.getMenu().getItem(1).setVisible(false);
        }*/

            //Setando true para o menu Home
            navigationView.getMenu().getItem(0).setChecked(true);

            this.addCallBackChangeFragment();

            //Verifico se o usuário está logado
            if (this.auth.isLogged()) {
                //Setando false para o menu login não ficar visível
                navigationView.getMenu().getItem(1).setVisible(false);
                try {
                    name.setText(this.user.get("name").toString());
                    email.setText(this.user.get("email").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     *
     */
    private void addCallBackChangeFragment(){
        getSupportFragmentManager().addOnBackStackChangedListener(
            new FragmentManager.OnBackStackChangedListener() {
                public void onBackStackChanged() {
                    if(sharedPreferences.contains("user")){
                        navigationView.getMenu().getItem(1).setVisible(false);
                    }
                    Fragment current = fm.findFragmentById(R.id.content_fragment);
                    if (current instanceof HomeFragment) {
                        navigationView.setCheckedItem(R.id.nav_home);
                    } else if(current instanceof LoginFragment){
                       navigationView.setCheckedItem(R.id.nav_login);

                    }
                }
            }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settings);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            HomeFragment homeFragment = new HomeFragment();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.addToBackStack(getString(R.string.addToBackStack));
            fragmentTransaction.replace(R.id.content_fragment, homeFragment, "home").commit();

        }else if (id == R.id.nav_login) {
            LoginFragment loginFragment = new LoginFragment();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.addToBackStack(getString(R.string.addToBackStack));
            fragmentTransaction.replace(R.id.content_fragment, loginFragment, "login").commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(spChange);
        sharedPreferences.edit().clear().commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(spChange);
        sharedPreferences.edit().clear().commit();
    }*/
}
