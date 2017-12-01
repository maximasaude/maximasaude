package com.saude.maxima;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saude.maxima.utils.Auth;
import com.saude.maxima.utils.Diary;
import com.saude.maxima.utils.DiaryHour;
import com.saude.maxima.utils.ManagerSharedPreferences;
import com.saude.maxima.utils.Routes;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class DiaryActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener {

    private ManagerSharedPreferences managerSharedPreferences;

    TextView txtPackage;
    TextView txtValue;
    TextView txtDate;
    Button btnEdit;
    Button btnRemove;
    ImageView imgPackage;
    TextView txtNoHasPackage;
    LinearLayout llNoHasPackage;
    LinearLayout llContent;
    ProgressDialog progressDialog;
    List<Diary> diaries;
    List<DiaryHour> diaryHours;
    DatePickerDialog datePickerDialog;
    FloatingActionButton btnPayment;
    int year, month, day, hour, minute;
    EditText cardNumber;
    WebView webView;
    ProgressBar progressBar;

    View view;

    SwipeRefreshLayout swipeRefreshLayout;

    String params = null;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void refreshContent(){
        recreate();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
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

        getLayoutInflater().inflate(R.layout.activity_diary, frameLayout);
        getSupportActionBar().setTitle(R.string.cart);
        navigationView.setNavigationItemSelectedListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
        webView = (WebView) findViewById(R.id.webView);

        txtPackage = (TextView) findViewById(R.id.txtPackage);
        txtValue = (TextView) findViewById(R.id.txtValue);
        txtDate = (TextView) findViewById(R.id.txtDate);
        imgPackage = (ImageView) findViewById(R.id.img_package);

        managerSharedPreferences = new ManagerSharedPreferences(this.context);
        btnPayment = (FloatingActionButton) findViewById(R.id.btnPayment);

        if (managerSharedPreferences.has("order")) {
            btnRemove = (Button) findViewById(R.id.btnRemove);
            btnEdit = (Button) findViewById(R.id.btnEdit);
            try {
                JSONObject objOrder = new JSONObject(managerSharedPreferences.get("order").toString());
                JSONObject objPackage = new JSONObject(objOrder.get("package").toString());
                String dateFormated = objOrder.getString("day") + "/" +
                        objOrder.getString("month") + "/" + objOrder.getString("year") + " " +
                        objOrder.getString("hour") + ":" + objOrder.getString("minute");
                txtPackage.setText(objPackage.getString("name"));
                txtDate.setText(dateFormated);

                if(objPackage.getString("name").equals("Simples")){
                    imgPackage.setImageResource(R.drawable.simples);
                }else if(objPackage.getString("name").equals("Completo")){
                    imgPackage.setImageResource(R.drawable.completo);
                }else{
                    imgPackage.setImageResource(R.drawable.premium);
                }

                params = "available_date=" + objOrder.getString("year") + "-" + objOrder.getString("month") + "-" + objOrder.getString("day");
                params += "&available_hour=" + objOrder.getString("hour") + ":" + objOrder.getString("minute");

                NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                double value;
                value = Double.parseDouble(objPackage.getString("value"));

                txtValue.setText(numberFormat.format(value));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //new getPagseguroSessionId(null).execute(Routes.pagSeguro[0]);

            btnPayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!Auth.isLogged()) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                    }else {
                        //Se há conexão com a internet
                        if (isOnline()) {
                            String url = Routes.verifyDateAndHour[0] + "?" + params;
                            progressBar.setVisibility(View.VISIBLE);
                            new DiaryActivity.getDateAndHourAvailable(null).execute(url);
                        } else {
                            Toast.makeText(context, "Não há conexão com a internet", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

        /*btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MDDialog.Builder(context)
                        .setTitle("Pagamento")
                        .setNegativeButton("Cancelar", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setPositiveButton("Finalizar", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                View root = view.getRootView();
                                CreditCard creditCard = new CreditCard();
                                creditCard.setPagseguroSessionId(context);
                                creditCard.setCardNumber(getViewContent(root, R.id.card_number));
                                creditCard.setName(getViewContent(root, R.id.name));
                                creditCard.setMonth(getViewContent(root, R.id.month));
                                creditCard.setYear(getViewContent(root, R.id.year));
                                creditCard.setCvv(getViewContent(root, R.id.cvv));
                                creditCard.setParcels(Integer.parseInt(getViewContent(root, R.id.parcels)));
                                getPaymentToken(creditCard);
                            }
                        })
                        .setContentViewOperator(new MDDialog.ContentViewOperator() {
                            @Override
                            public void operate(View contentView) {
                                setOnCardNumberClickListener(contentView);
                            }
                        })
                        .setContentView(R.layout.credit_card)
                        .create()
                        .show();
            }
        });*/
        } else {
            llNoHasPackage = (LinearLayout) findViewById(R.id.llNoHasPackage);
            llNoHasPackage.setVisibility(View.VISIBLE);
            llContent = (LinearLayout) findViewById(R.id.llContent);
            llContent.setVisibility(View.GONE);
            txtNoHasPackage = (TextView) findViewById(R.id.txtNoHasPackage);
            txtNoHasPackage.setText("Não há nenhum pacote selecionado");
            Toast.makeText(context, "Não há nenhum pacote selecionado", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        progressBar.setVisibility(View.GONE);
        clearChecked();
        navigationView.getMenu().findItem(R.id.nav_cart).setChecked(true);
    }

    public void remove(View view){
        managerSharedPreferences.remove("order");
        recreate();
    }

    private void initDateTimeDate(){
        if(year == 0){
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        year = month = day = hour = minute = 0;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int y, int monthOfYear, int dayOfMonth) {
        Timepoint[] listTimePoint;
        List<Timepoint> auxTimePoint = new LinkedList<>();

        for(int i = 0; i < diaries.size(); i++){
            for(int j = 0; j < diaries.get(i).getHourList().size(); j++){
                int hour = diaries.get(i).getHourList().get(j).getHour();
                int minute = diaries.get(i).getHourList().get(j).getMinute();
                int second = diaries.get(i).getHourList().get(j).getSecond();
                Timepoint timepoint = new Timepoint(hour, minute, second);
                auxTimePoint.add(timepoint);
            }
        }

        listTimePoint = new Timepoint[auxTimePoint.size()];

        for(int i = 0; i < listTimePoint.length; i++){
            listTimePoint[i] = auxTimePoint.get(i);
        }

        Calendar timeDefault = Calendar.getInstance();

        timeDefault.set(year, month, day, hour, minute);

        year = y;
        month = monthOfYear;
        day = dayOfMonth;

        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                DiaryActivity.this,
                timeDefault.get(Calendar.HOUR_OF_DAY),
                timeDefault.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.setSelectableTimes(listTimePoint);

        timePickerDialog.setOnCancelListener(DiaryActivity.this);
        timePickerDialog.show(activity.getFragmentManager(), "timePickerDialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int min, int second) {

        managerSharedPreferences = new ManagerSharedPreferences(this.context);
        JSONObject order = new JSONObject();
        try {
            order.put("year", year);
            order.put("month", month < 10 ? "0"+(month+1) : (month+1));
            order.put("day", day < 10 ? "0"+day : day);
            order.put("hour", hourOfDay);
            order.put("minute", min);
            order.put("second", second);
            order.put("package", new JSONObject(managerSharedPreferences.get("order").getString("package")));
        }catch (JSONException e){
            e.printStackTrace();
        }

        managerSharedPreferences.set("order", order.toString());

        hour = hourOfDay;
        minute = min;

        Toast.makeText(this.context, "Data de agendamento alterada.", Toast.LENGTH_LONG).show();
        recreate();
    }

    private class getAvailableDates extends AsyncTask<String, Void, JSONObject> {

        String params;
        private JSONArray packages;
        private Calendar[] daysArray;

        private getAvailableDates(String params){
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

            try{
                if(!result.has("error")) {
                    JSONArray arrPackage = result.getJSONArray("success");

                    diaries = new LinkedList<>();
                    diaryHours = new LinkedList<>();

                    List<Calendar> dayList = new LinkedList<>();

                    for (int i = 0; i < arrPackage.length(); i++) {
                        try {
                            JSONObject objDiary = arrPackage.getJSONObject(i);

                            JSONArray hours = objDiary.getJSONArray("hours");
                            for (int h = 0; h < hours.length(); h++) {
                                JSONObject objHour = hours.getJSONObject(h);
                                int id = objHour.getInt("id");
                                String availableHour = objHour.getString("available_hour");
                                String createdAt = objHour.getString("created_at");
                                String updatedAt = objHour.getString("updated_at");
                                //Nova instancia de DiaryHour
                                diaryHours.add(new DiaryHour(id, availableHour, 1, createdAt, updatedAt));
                            }


                            int id = objDiary.getInt("id");
                            String availableDate = objDiary.getString("available_date");
                            String description = objDiary.getString("description");
                            int isActive = objDiary.getInt("is_active");
                            String createdAt = objDiary.getString("created_at");
                            String updatedAt = objDiary.getString("updated_at");
                            diaries.add(new Diary(id, availableDate, description, isActive, createdAt, updatedAt, diaryHours));

                            //Ano, Mês e Dia
                            int y, m, d;
                            String[] available_date = diaries.get(i).getAvailableDate().split("-");
                            y = Integer.parseInt(available_date[0]);
                            m = Integer.parseInt(available_date[1]) - 1;
                            d = Integer.parseInt(available_date[2]);

                            Calendar cAux = Calendar.getInstance();
                            cAux.set(y, m, d);

                            dayList.add(cAux);
                            /*
                            while(cAux.getTimeInMillis() <= cMax.getTimeInMillis()){
                                if(cAux.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && cAux.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY){
                                    Calendar c = Calendar.getInstance();
                                    c.setTimeInMillis(cAux.getTimeInMillis());
                                    dayList.add(c);
                                }
                                cAux.setTimeInMillis(cAux.getTimeInMillis() + (24 * 60*  60 * 1000));
                            }*/


                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }

                    if (dayList.size() > 0) {

                        Calendar cMin = Calendar.getInstance();
                        Calendar cMax = Calendar.getInstance();
                        cMax.set(cMax.get(Calendar.YEAR), 11, 31);

                        datePickerDialog.setMinDate(cMin);
                        datePickerDialog.setMaxDate(cMax);

                        daysArray = new Calendar[dayList.size()];

                        for (int j = 0; j < daysArray.length; j++) {
                            daysArray[j] = dayList.get(j);
                        }

                        //Toast.makeText(getContext(), result.getJSONObject("success").toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        //Toast.makeText(getContext(), ""+daysArray[0].get(Calendar.DAY_OF_WEEK), Toast.LENGTH_SHORT).show();


                        datePickerDialog.setSelectableDays(daysArray);
                        datePickerDialog.setOnCancelListener(DiaryActivity.this);
                        datePickerDialog.show(activity.getFragmentManager(), "Datepickerdialog");

                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(context, "Não existe datas disponíveis", Toast.LENGTH_LONG).show();
                    }
                }
            }catch (JSONException e){
                progressDialog.dismiss();
            }
            progressDialog.dismiss();

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

    private class getPagseguroSessionId extends AsyncTask<String, Void, JSONObject> {

        String params;
        private JSONArray packages;
        private Calendar[] daysArray;

        private getPagseguroSessionId(String params){
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
            if(result.has("success")){
                ManagerSharedPreferences managerSharedPreferences = new ManagerSharedPreferences(context);
                try{
                    managerSharedPreferences.set("pagseguro_session_id",
                            result.getJSONObject("success").toString()
                    );
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
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

    private class getCardBrand extends AsyncTask<String, Void, JSONObject> {

        String params;
        private JSONArray packages;
        private Calendar[] daysArray;

        private getCardBrand(String params){
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
            if(result.has("success")){

            }
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

    public class getDateAndHourAvailable extends AsyncTask<String, Void, JSONObject> {

        String params;
        private JSONArray schedules;

        private getDateAndHourAvailable(String params) {
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

            if (!result.has("errors")) {
                try {
                    JSONObject objOrder = result.getJSONObject("success");
                    Intent intent = new Intent(context, PaymentActivity.class);
                    startActivity(intent);

                    progressBar.setVisibility(View.GONE);

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }else{
                try {
                    if (!result.getString("errors").equals("false")) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, result.getJSONObject("errors").getString("response"), Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
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


    public void edit(View view){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getString(R.string.executing));
        progressDialog.setCancelable(false);
        progressDialog.show();

        //CustomBottomSheetDialogFragment dialog = new CustomBottomSheetDialogFragment();
        //dialog.show(getFragmentManager(), getString(R.string.addToBackStack));
        initDateTimeDate();
        Calendar calendarDefault = Calendar.getInstance();
        calendarDefault.set(year, month, day);
        datePickerDialog = DatePickerDialog.newInstance(
                DiaryActivity.this,
                calendarDefault.get(Calendar.YEAR),
                calendarDefault.get(Calendar.MONTH),
                calendarDefault.get(Calendar.DAY_OF_MONTH)
        );

        new DiaryActivity.getAvailableDates(null).execute(Routes.diaries[0]);

    }

    /**
     * Função que verifica se há conexão com a internet
     * @return boolean
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
