package com.saude.maxima;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
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

public class ReportActivity extends BaseActivity implements RecyclerViewOnClickListenerHack, NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    TabLayout tabLayout;
    ViewPager viewPager;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    List<Schedule> scheduleList;
    ReportScheduledAdapter reportSimpleAdapter;
    ProgressBar progressBar;
    Toolbar toolbar;

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
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
        getLayoutInflater().inflate(R.layout.activity_report, frameLayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.report);
        setActionBarDrawerToggle(toolbar);

        navigationView.setNavigationItemSelectedListener(this);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        viewPager.setAdapter(
                new ReportFragmentStatePagerAdapter(
                        getSupportFragmentManager(),
                        getResources().getStringArray(R.array.titles_tab_report)
                )
        );

        tabLayout.setupWithViewPager(viewPager);
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
    public void onResume(){
        super.onResume();
        clearChecked();
        navigationView.getMenu().findItem(R.id.nav_report).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
