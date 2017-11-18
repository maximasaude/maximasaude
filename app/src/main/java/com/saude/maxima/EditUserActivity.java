package com.saude.maxima;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;

public class EditUserActivity extends AppCompatActivity {

    AutoCompleteTextView edtName;
    AutoCompleteTextView edtAddress;
    AutoCompleteTextView edtAddressComplement;
    AutoCompleteTextView edtDistrict;
    AutoCompleteTextView edtZip;
    AutoCompleteTextView edtPhone;
    AutoCompleteTextView edtCellPhone;
    AutoCompleteTextView edtState;
    AutoCompleteTextView edtCity;
    AutoCompleteTextView edtCPF;
    AutoCompleteTextView edtBirthDate;
    AutoCompleteTextView edtRG;
    AutoCompleteTextView edtNumber;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        edtName = (AutoCompleteTextView) findViewById(R.id.edtName);
        edtName.setText("Gilson de Melo");

        edtAddress = (AutoCompleteTextView) findViewById(R.id.edtAddress);
        edtAddress.setText("Rua Bahia");

        edtAddressComplement = (AutoCompleteTextView) findViewById(R.id.edtAddressComplement);
        edtAddressComplement.setText("Não informado");

        edtDistrict = (AutoCompleteTextView) findViewById(R.id.edtDistrict);
        edtDistrict.setText("Nova Candeias");

        edtZip = (AutoCompleteTextView) findViewById(R.id.edtZip);
        edtZip.setText("43815-200");

        edtPhone = (AutoCompleteTextView) findViewById(R.id.edtPhone);
        edtPhone.setText("(71) 3601-4189");

        edtCellPhone = (AutoCompleteTextView) findViewById(R.id.edtCellPhone);
        edtCellPhone.setText("(71) 99714-3703");

        edtState = (AutoCompleteTextView) findViewById(R.id.edtState);
        edtState.setText("BA");

        edtCity = (AutoCompleteTextView) findViewById(R.id.edtCity);
        edtCity.setText("Candeias");

        edtNumber = (AutoCompleteTextView) findViewById(R.id.edtNumber);
        edtNumber.setText("99");

        edtCPF = (AutoCompleteTextView) findViewById(R.id.edtCPF);
        edtCPF.setText("999.999.999-99");

        edtBirthDate = (AutoCompleteTextView) findViewById(R.id.edtBirthDate);
        edtBirthDate.setText("31/12/1994");

        edtRG = (AutoCompleteTextView) findViewById(R.id.edtRG);
        edtRG.setText("31/12/1994");

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
