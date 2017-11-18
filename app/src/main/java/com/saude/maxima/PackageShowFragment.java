package com.saude.maxima;

import com.saude.maxima.utils.Diary;
import com.saude.maxima.utils.DiaryHour;
import com.saude.maxima.utils.ManagerSharedPreferences;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.saude.maxima.Adapters.Package.Package;
import com.saude.maxima.Adapters.Package.PackagesAdapter;
import com.saude.maxima.Adapters.Package.PackagesAdapterr;
import com.saude.maxima.fragments.packages.PackageContentFragment;
import com.saude.maxima.fragments.packages.PackageDescriptionFragment;
import com.saude.maxima.utils.Routes;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class PackageShowFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View bottomSheet;
    private float offsetY;
    private ExpandableHeightGridView gridView;
    private ProgressDialog progressDialog;
    private ArrayList<Package> packagesList;
    private PackagesAdapterr packagesAdapter;
    private String url;
    private DatePickerDialog datePickerDialog;
    private List<Diary> diaries;
    private List<DiaryHour> diaryHours;
    private ManagerSharedPreferences managerSharedPreferences;
    private Bundle args;
    private Context context;
    private JSONObject jsonPackage;

    TextView name, description;

    private int year, month, day, hour, minute;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_package_show, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);

        this.context = getActivity().getApplicationContext();

        args = getArguments();

        viewPager.setAdapter(
                new PackageFragmentStatePagerAdapter(
                        getFragmentManager(),
                        getResources().getStringArray(R.array.titles_tab),
                        args.getString("package")
                )
        );

        tabLayout.setupWithViewPager(viewPager);

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();

                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage(getString(R.string.executing));
                progressDialog.setCancelable(false);
                progressDialog.show();

                //CustomBottomSheetDialogFragment dialog = new CustomBottomSheetDialogFragment();
                //dialog.show(getFragmentManager(), getString(R.string.addToBackStack));
                initDateTimeDate();
                Calendar calendarDefault = Calendar.getInstance();
                calendarDefault.set(year, month, day);
                datePickerDialog = DatePickerDialog.newInstance(
                        PackageShowFragment.this,
                        calendarDefault.get(Calendar.YEAR),
                        calendarDefault.get(Calendar.MONTH),
                        calendarDefault.get(Calendar.DAY_OF_MONTH)
                );

                new getAvailableDates(null).execute(Routes.diaries[0]);

            }
        });


        /*offsetY = 0;
        bottomSheet = view.findViewById(R.id.modal);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if(offsetY < slideOffset){
                    fab.hide();
                }else if(offsetY > slideOffset){
                    fab.show();
                }
                offsetY = slideOffset;
            }
        });*/

        //gridView = (ExpandableHeightGridView) view.findViewById(R.id.cart_grid);
        //Setando tamanho expandido para a gridview
        //gridView = (ExpandableHeightGridView) view.findViewById(R.id.gridView);
        //gridView.setExpanded(true);
        //this.onClickGridView();

        //new getPackages(null).execute(Routes.packages[0]);

        return view;
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

    private void onClickGridView(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                progressDialog.show();

                int package_id = packagesList.get(position).getId();
                Toast.makeText(getActivity(), "position: "+package_id, Toast.LENGTH_SHORT).show();

                //url = Routes.packages[1].replace("{id}", ""+package_id);

                //new HomeFragment.findPackage(null).execute(url);

                //new findPackage("").execute(Routes.packages[1]);
                //TextView name = (TextView) view.findViewById(R.id.name_package);
                //Toast.makeText(getContext(), name.getText(), Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(getContext(), Main2Activity.class);
                Bundle params = new Bundle();
                params.putString("name", name.getText().toString());
                intent.putExtras(params);
                startActivity(intent);*/
            }
        });
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
                PackageShowFragment.this,
                timeDefault.get(Calendar.HOUR_OF_DAY),
                timeDefault.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.setSelectableTimes(listTimePoint);

        timePickerDialog.setOnCancelListener(PackageShowFragment.this);
        timePickerDialog.show(getActivity().getFragmentManager(), "timePickerDialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int min, int second) {
        /*if(hourOfDay < 8 || hourOfDay > 18){
            onDateSet(null, year, month, day);
            Toast.makeText(getContext(), "SOmente entre 8h e 18h", Toast.LENGTH_SHORT).show();
            return;
        }*/

        JSONObject order = new JSONObject();
        try {
            order.put("year", year);
            order.put("month", month < 10 ? "0"+(month+1) : (month+1));
            order.put("day", day < 10 ? "0"+day : day);
            order.put("hour", hourOfDay);
            order.put("minute", min);
            order.put("second", second);
            order.put("package", new JSONObject(args.getString("package")));
        }catch (JSONException e){
            e.printStackTrace();
        }
        managerSharedPreferences = new ManagerSharedPreferences(this.context);

        managerSharedPreferences.set("order", order.toString());

        hour = hourOfDay;
        minute = min;
        String namePackage = "";

        try {
            namePackage = new JSONObject(args.getString("package")).getString("name");
        }catch (JSONException e){
            e.printStackTrace();
        }

        Toast.makeText(this.context, "Pacote "+namePackage+" adicionado para o agendamento.", Toast.LENGTH_LONG).show();
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);


        Bundle args = getIntent().getExtras();


        viewPager.setAdapter(
                new PackageFragmentStatePagerAdapter(
                        getSupportFragmentManager(),
                        getResources().getStringArray(R.array.titles_tab),
                        args.getString("package")
                )
        );

        tabLayout.setupWithViewPager(viewPager);





        *//*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*//*
    }



    @Override
    protected void onPause() {
        super.onPause();
        *//*Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();*//*
    }*/

    private class PackageFragmentStatePagerAdapter extends FragmentStatePagerAdapter{

        private String[] tabTitles;
        private String dataPackage;

        private PackageFragmentStatePagerAdapter(FragmentManager fm, String[] tabTitles, String dataPackage) {
            super(fm);
            this.tabTitles = tabTitles;
            this.dataPackage = dataPackage;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            switch (position){
                case 0:
                    PackageContentFragment packageContentFragment = new PackageContentFragment();
                    args.putString("package", this.dataPackage);

                    packageContentFragment.setArguments(args);
                    return packageContentFragment;
                case 1:
                    PackageDescriptionFragment packageDescriptionFragment = new PackageDescriptionFragment();

                    args.putString("package", this.dataPackage);

                    packageDescriptionFragment.setArguments(args);
                    return packageDescriptionFragment;
                default: return null;
            }
        }

        @Override
        public int getCount() {
            return this.tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.tabTitles[position];
        }
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
                if(!result.has("errors")) {
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
                        datePickerDialog.setOnCancelListener(PackageShowFragment.this);
                        datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");

                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Não existe datas disponíveis", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Não existe datas disponíveis", Toast.LENGTH_LONG).show();
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

}
