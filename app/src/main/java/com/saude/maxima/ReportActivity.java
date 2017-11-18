package com.saude.maxima;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.saude.maxima.Adapters.Package.ReportScheduledAdapter;
import com.saude.maxima.Adapters.Package.Schedule;
import com.saude.maxima.fragments.packages.ReportEvaluationFragment;
import com.saude.maxima.fragments.packages.ReportScheduledFragment;
import com.saude.maxima.interfaces.RecyclerViewOnClickListenerHack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {

    RecyclerView recyclerView;
    TabLayout tabLayout;
    ViewPager viewPager;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    List<Schedule> scheduleList;
    ReportScheduledAdapter reportSimpleAdapter;
    ProgressBar progressBar;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        viewPager.setAdapter(
                new ReportFragmentStatePagerAdapter(
                        getSupportFragmentManager(),
                        getResources().getStringArray(R.array.titles_tab_report)
                )
        );

        tabLayout.setupWithViewPager(viewPager);

        //progressBar = (ProgressBar) findViewById(R.id.progress);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                    }
        });*/

        //recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        //recyclerView.setHasFixedSize(true);

        //linearLayoutManager = new LinearLayoutManager(this);
        //gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        //staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.VERTICAL);
        //recyclerView.setLayoutManager(linearLayoutManager);

        //this.onScrollRecycleView();

        //Auth auth = new Auth(this);
        //int id = 0;
        /*try {
            id = auth.getAuth().getInt("id");
        }catch (JSONException e){
            e.printStackTrace();
        }*/

        //Executando busca de todos os pacotes disponíveis na API
        //String url = Routes.schedules[1].replace("{id}", ""+id);
        //new getSchedules(null).execute(url);
        //progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnClickListener(View view, int position) {
        Toast.makeText(this, "position: "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnLongPressClickListener(View view, int position) {

    }

    private void onScrollRecycleView(){
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                //StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                //int max = linearLayoutManager.findLastVisibleItemPosition();







                //int[] aux = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(null);
                //int max = -1;

                /*for(int i = 0; i < aux.length; i++){
                    max = aux[i] > max ? aux[1] : max;
                }

                reportSimpleAdapter = (ReportSimpleAdapter) recyclerView.getAdapter();
                *//*if(packagesList.size() == gridLayoutManager.findLastCompletelyVisibleItemPosition() + 1){

                }*//*

                if(scheduleList.size() == max){

                }*/

            }
        });
    }

    private class ReportFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

        private String[] tabTitles;
        private String dataPackage;

        private ReportFragmentStatePagerAdapter(FragmentManager fm, String[] tabTitles) {
            super(fm);
            this.tabTitles = tabTitles;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            switch (position){
                case 0:
                    ReportScheduledFragment reportScheduledFragment = new ReportScheduledFragment();

                    return reportScheduledFragment;
                case 1:
                    ReportEvaluationFragment reportEvaluationFragment = new ReportEvaluationFragment();
                    return reportEvaluationFragment;
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

    public class getSchedules extends AsyncTask<String, Void, JSONObject> {

        String params;
        private JSONArray schedules;

        private getSchedules(String params){
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

            scheduleList = new ArrayList<Schedule>();
            try{
                if(!result.has("error")){
                    JSONArray arrSchedule = result.getJSONArray("success");

                    for(int i = 0; i < arrSchedule.length(); i++){
                        try{
                            JSONObject objSchedule = arrSchedule.getJSONObject(i);
                            JSONObject objOrder = objSchedule.getJSONObject("order");
                            JSONObject objDiary = objSchedule.getJSONObject("diary");
                            JSONObject objDiaryHour = objSchedule.getJSONObject("diary_hour");
                            JSONObject objPackage = objSchedule.getJSONObject("package");
                            JSONObject objSupplier = objSchedule.getJSONObject("supplier");
                            scheduleList.add(new Schedule(objSchedule, objOrder, objDiary, objDiaryHour, objPackage, objSupplier));
                            //packages.add(new Package(2, "Completo", "Pacote completo", 50.00));
                        }catch (JSONException ex){
                            ex.printStackTrace();
                        }
                    }

                    //Toast.makeText(getContext(), result.getJSONObject("success").toString(), Toast.LENGTH_SHORT).show();

                    progressBar.setVisibility(View.GONE);

                }
            }catch (JSONException e){
                progressBar.setVisibility(View.GONE);
            }
            progressBar.setVisibility(View.GONE);
            //reportSimpleAdapter = new ReportSimpleAdapter(getApplicationContext(), scheduleList);
            //packagesAdapter.setRecyclerViewOnClickListenerHack(HomeFragment.this);
            //recyclerView.setAdapter(reportSimpleAdapter);
            //recyclerView.addOnItemTouchListener(new RecyclerViewOnTouchListener(getApplicationContext(), recyclerView, ReportActivity.this));
            //gridView.setAdapter(packagesAdapter);
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

    private class RecyclerViewOnTouchListener implements RecyclerView.OnItemTouchListener {

        private Context context;
        private GestureDetector gestureDetector;
        private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

        public RecyclerViewOnTouchListener(Context context, final RecyclerView rv, RecyclerViewOnClickListenerHack rvoclh){
            this.context = context;
            this.recyclerViewOnClickListenerHack = rvoclh;
            gestureDetector = new GestureDetector(this.context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    View cv = rv.findChildViewUnder(e.getX(), e.getY());
                    if(cv != null && recyclerViewOnClickListenerHack != null){
                        recyclerViewOnClickListenerHack.OnClickListener(cv, rv.getChildLayoutPosition(cv));
                    }
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                    View cv = rv.findChildViewUnder(e.getX(), e.getY());
                    if(cv != null && recyclerViewOnClickListenerHack != null){
                        recyclerViewOnClickListenerHack.OnLongPressClickListener(cv, rv.getChildLayoutPosition(cv));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            this.gestureDetector.onTouchEvent(e);
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
