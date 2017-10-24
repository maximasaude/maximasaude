package com.saude.maxima;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.saude.maxima.utils.ManagerSharedPreferences;

public class SettingsActivity extends AppCompatActivity {

    ManagerSharedPreferences managerSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        managerSharedPreferences = new ManagerSharedPreferences(this);
        setContentView(R.layout.activity_settings);
    }

    /**
     *
     * @param view
     */
    public void logout(View view){
        managerSharedPreferences.remove("user");
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }

    /**
     *
     * @param view
     */
    public void terms(View view){
        Intent main = new Intent(this, PackageShowActivity.class);
        startActivity(main);
    }
}
