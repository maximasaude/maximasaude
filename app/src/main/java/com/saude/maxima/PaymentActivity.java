package com.saude.maxima;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import br.com.uol.pslibs.checkout_in_app.PSCheckout;
import br.com.uol.pslibs.checkout_in_app.wallet.util.PSCheckoutConfig;

public class PaymentActivity extends AppCompatActivity {

    public static final String SELLER_EMAIL = "miranda.fitness.avaliacao@gmail.com";
    public static final String SELLER_TOKEN = "619A9082A5374BD1917745ABC9D471FF";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PSCheckout.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
}
