package com.saude.maxima;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saude.maxima.utils.Auth;
import com.saude.maxima.utils.ManagerSharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by junnyor on 11/29/17.
 */

public class BaseActivity extends AppCompatActivity{

    /**
     *  Frame layout: Which is going to be used as parent layout for child activity layout.
     *  This layout is protected so that child activity can access this
     *  */
    protected FrameLayout frameLayout;

    Activity activity;
    Context context;
    NavigationView navigationView = null;
    Toolbar toolbar = null;
    LinearLayout navHeader;
    LinearLayout navContentLogo;
    private Auth auth;
    private JSONObject user;

    TextView name;
    TextView email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();
        activity = this;

        //Drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        frameLayout = (FrameLayout)findViewById(R.id.content_frame);

        navHeader = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.nav_header);
        navContentLogo = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.nav_content_logo);

        name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.name);
        email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);

        //Instancia para a classe auth
        this.auth = new Auth(getApplicationContext());

        //Pegando os dados do usuário, caso esteja logado
        this.user = this.auth.getAuth();

        //Setando true para o menu Home
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.getMenu().findItem(R.id.optionUser).setVisible(false);

        //Verifico se o usuário está logado
        if (Auth.isLogged()) {
            navContentLogo.setVisibility(View.GONE);
            navHeader.setVisibility(View.VISIBLE);
            //Setando false para o menu login não ficar visível
            navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().findItem(R.id.optionUser).setVisible(true);
            try {
                name.setText(this.user.get("name").toString());
                email.setText(this.user.get("email").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    protected void clearChecked(){
        for(int i = 0; i < navigationView.getMenu().size(); i++){
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //clearChecked();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_close) {
            finish();
        }else if(id == R.id.action_logout){
            ManagerSharedPreferences managerSharedPreferences = new ManagerSharedPreferences(this);
            managerSharedPreferences.remove("user");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
