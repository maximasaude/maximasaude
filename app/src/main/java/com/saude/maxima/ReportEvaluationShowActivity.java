package com.saude.maxima;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ReportEvaluationShowActivity extends AppCompatActivity {

    Toolbar toolbar;
    AutoCompleteTextView edtWeight;
    AutoCompleteTextView edtHeight;
    AutoCompleteTextView edtRightArm;
    AutoCompleteTextView edtLeftArm;
    AutoCompleteTextView edtTummy;
    AutoCompleteTextView edtHip;
    AutoCompleteTextView edtCoxaProximal;
    AutoCompleteTextView edtCoxaMedial;
    AutoCompleteTextView edtCoxaDistal;
    AutoCompleteTextView edtRightLeg;
    AutoCompleteTextView edtLeftLeg;
    AutoCompleteTextView edtChest;
    AutoCompleteTextView edtWaist;
    AutoCompleteTextView edtForearm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_evaluation_show);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        edtWeight = (AutoCompleteTextView) findViewById(R.id.edtWeight);
        edtHeight = (AutoCompleteTextView) findViewById(R.id.edtHeight);
        edtRightArm = (AutoCompleteTextView) findViewById(R.id.edtRightArm);
        edtLeftArm = (AutoCompleteTextView) findViewById(R.id.edtLeftArm);
        edtTummy = (AutoCompleteTextView) findViewById(R.id.edtTummy);
        edtHip = (AutoCompleteTextView) findViewById(R.id.edtHip);
        edtCoxaProximal = (AutoCompleteTextView) findViewById(R.id.edtCoxaProximal);
        edtCoxaMedial = (AutoCompleteTextView) findViewById(R.id.edtCoxaMedial);
        edtCoxaDistal = (AutoCompleteTextView) findViewById(R.id.edtCoxaDistal);
        edtRightLeg = (AutoCompleteTextView) findViewById(R.id.edtRightLeg);
        edtLeftLeg = (AutoCompleteTextView) findViewById(R.id.edtLeftLeg);
        edtChest = (AutoCompleteTextView) findViewById(R.id.edtChest);
        edtWaist = (AutoCompleteTextView) findViewById(R.id.edtWaist);
        edtForearm = (AutoCompleteTextView) findViewById(R.id.edtForearm);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            try{
                JSONObject objEvaluation = new JSONObject(bundle.getString("evaluation"));
                JSONObject objAnthropometry = new JSONObject(bundle.getString("anthropometry"));
                JSONObject objUser = new JSONObject(bundle.getString("user"));

                edtHeight.setText(!objAnthropometry.getString("height").equals("null") ? objAnthropometry.getString("height")+"CM" : "Não informado");
                edtWeight.setText(!objAnthropometry.getString("weight").equals("null") ? objAnthropometry.getString("weight")+"CM" : "Não informado");
                edtRightArm.setText(!objAnthropometry.getString("right_arm").equals("null") ? objAnthropometry.getString("right_arm")+"CM" : "Não informado");
                edtLeftArm.setText(!objAnthropometry.getString("left_arm").equals("null") ? objAnthropometry.getString("left_arm")+"CM" : "Não informado");
                edtTummy.setText(!objAnthropometry.getString("tummy").equals("null") ? objAnthropometry.getString("tummy")+"CM" : "Não informado");
                edtHip.setText(!objAnthropometry.getString("hip").equals("null") ? objAnthropometry.getString("hip")+"CM" : "Não informado");
                edtCoxaProximal.setText(!objAnthropometry.getString("coxa_proximal").equals("null") ? objAnthropometry.getString("coxa_proximal")+"CM" : "Não informado");
                edtCoxaMedial.setText(!objAnthropometry.getString("coxa_medial").equals("null") ? objAnthropometry.getString("coxa_medial")+"CM" : "Não informado");
                edtCoxaDistal.setText(!objAnthropometry.getString("coxa_distal").equals("null") ? objAnthropometry.getString("coxa_distal")+"CM" : "Não informado");
                edtRightLeg.setText(!objAnthropometry.getString("right_leg").equals("null") ? objAnthropometry.getString("right_leg")+"CM" : "Não informado");
                edtLeftLeg.setText(!objAnthropometry.getString("left_leg").equals("null") ? objAnthropometry.getString("left_leg")+"CM" : "Não informado");
                edtForearm.setText(!objAnthropometry.getString("forearm").equals("null") ? objAnthropometry.getString("forearm")+"CM" : "Não informado");
                edtChest.setText(!objAnthropometry.getString("chest").equals("null") ? objAnthropometry.getString("chest")+"CM" : "Não informado");
                edtWaist.setText(!objAnthropometry.getString("waist").equals("null") ? objAnthropometry.getString("waist")+"CM" : "Não informado");

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
