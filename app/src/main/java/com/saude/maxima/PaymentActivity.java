package com.saude.maxima;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.saude.maxima.utils.Auth;
import com.saude.maxima.utils.ManagerSharedPreferences;
import com.saude.maxima.utils.Payment;

public class PaymentActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;

    ManagerSharedPreferences managerSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        webView = (WebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        managerSharedPreferences = new ManagerSharedPreferences(this);


        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setSupportZoom(false);

        //Pegando os dados do usuário, caso esteja logado

        Auth auth = new Auth(this);

        Payment payment = new Payment();
        payment.setAuth(auth.getAuth());
        payment.setItems(managerSharedPreferences.get("order"));

        webView.addJavascriptInterface(payment, "Payment");

        webView.loadUrl("http://10.0.0.104:8000/pagseguro/get_view");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.INVISIBLE);
                webView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
