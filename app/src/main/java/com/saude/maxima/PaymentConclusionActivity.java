package com.saude.maxima;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saude.maxima.utils.Auth;
import com.saude.maxima.utils.ManagerSharedPreferences;
import com.saude.maxima.utils.Routes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PaymentConclusionActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView txtPackageName;
    TextView txtPackageValue;
    TextView txtScheduledDate;
    TextView txtGym;
    TextView txtCity;
    TextView txtDistrict;
    TextView txtAddress;
    TextView txtZip;
    TextView txtState;
    Button btnDashboard;
    Button btnHome;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_conclusion);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtPackageName = (TextView) findViewById(R.id.package_name);
        txtPackageValue = (TextView) findViewById(R.id.package_value);
        txtScheduledDate = (TextView) findViewById(R.id.scheduled_date);
        txtGym = (TextView) findViewById(R.id.gym);
        txtCity = (TextView) findViewById(R.id.city);
        txtAddress = (TextView) findViewById(R.id.address);
        txtDistrict = (TextView) findViewById(R.id.district);
        txtZip = (TextView) findViewById(R.id.zip);
        txtState = (TextView) findViewById(R.id.state);

        btnDashboard = (Button) findViewById(R.id.dashboard);
        btnHome = (Button) findViewById(R.id.home);

        ManagerSharedPreferences managerSharedPreferences = new ManagerSharedPreferences(this);

        String params = null;
        try {
            JSONObject objItem = managerSharedPreferences.get("order");
            JSONObject objPackage = objItem.getJSONObject("package");
            txtPackageName.setText(objPackage.getString("name"));
            txtPackageValue.setText(objPackage.getString("value"));
            txtScheduledDate.setText(objItem.getString("year"));

            params = "?available_date="+objItem.getString("year")+"-"+objItem.getString("month")+"-"+objItem.getString("day");
            params += "&available_hour="+objItem.getString("hour")+":"+objItem.getString("minute");

        }catch (JSONException e){
            e.printStackTrace();
        }

        Auth auth = new Auth(this);
        int id = 0;
        try {
            id = auth.getAuth().getInt("id");
        }catch (JSONException e){
            e.printStackTrace();
        }


        //Executando busca de todos os pacotes disponíveis na API
        String url = Routes.order[0].replace("{user_id}", ""+id);
        new getOrder(params).execute(url);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     *
     * @param view
     */
    public void dashboard(View view){
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     *
     * @param view
     */
    public void home(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public class getOrder extends AsyncTask<String, Void, JSONObject> {

        String params;
        private JSONArray schedules;

        private getOrder(String params) {
            this.setParams(params);
        }

        /**
         * Defines work to perform on the background thread.
         */
        @Override
        protected JSONObject doInBackground(String... urls) {
            return Connection.get(urls[0], this.getParams());
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            if (!result.has("error")) {
                try {
                    JSONObject objOrder = result.getJSONObject("success");
                    JSONObject objDiary = objOrder.getJSONObject("diary");
                    JSONObject objDiaryHour = objOrder.getJSONObject("diary_hour");
                    JSONObject objSupplier = objOrder.getJSONObject("supplier");
                } catch (JSONException ex) {
                    ex.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
            }

            progressBar.setVisibility(View.GONE);
        }

        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(JSONObject result) {
        }

        private String getParams() {
            return params;
        }

        private void setParams(String params) {
            this.params = params;
        }
    }
}
