package com.saude.maxima;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.saude.maxima.Adapters.Category.Category;
import com.saude.maxima.Adapters.Package.Package;
import com.saude.maxima.Adapters.Package.PackagesAdapter;
import com.saude.maxima.interfaces.RecyclerViewOnClickListenerHack;
import com.saude.maxima.utils.Routes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements RecyclerViewOnClickListenerHack, NavigationView.OnNavigationItemSelectedListener{

    private float startX;
    private float lastX;
    PackagesAdapter packagesAdapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    String params, url;

    List<Package> packagesList = new ArrayList<>();
    List<Category> categoryList = new ArrayList<>();
    AutoCompleteTextView edtName;
    AutoCompleteTextView edtEmail;
    ViewFlipper viewFlipper;
    ProgressBar progress;

    ViewPager viewPager;
    TabLayout tabLayout;
    RelativeLayout content;
    Toolbar toolbar;

    ProgressDialog progressDialog;

    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

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
        }else if (id == R.id.nav_cart) {
            Intent intent = new Intent(this, DiaryActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_report) {
            Intent intent = new Intent(this, ReportActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_perfil) {
            Intent intent = new Intent(this, EditUserActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private FragmentManager fm = getSupportFragmentManager();

    SharedPreferences sharedPreferences;
    SharedPreferences.OnSharedPreferenceChangeListener spChange = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.i("Script alterado", key+"updated");
        }
    };

    private void refreshContent(){
        if(isOnline()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void OnClickListener(View view, int position) {
        int package_id = packagesList.get(position).getId();

        url = Routes.packages[1].replace("{id}", ""+package_id);

        new MainActivity.findPackage(null).execute(url);
    }

    @Override
    public void OnLongPressClickListener(View view, int position) {
        Toast.makeText(context, "Postion: "+ position, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putSerializable(ListPackages.key, new ListPackages(packagesList));
        outState.putSerializable(ListCategory.key, new ListCategory(categoryList));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Função que verifica se há conexão com a internet
     * @return boolean
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Layout filho
        getLayoutInflater().inflate(R.layout.content_main, frameLayout);

        progress = (ProgressBar) findViewById(R.id.progress);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setActionBarDrawerToggle(toolbar);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        if(isOnline()) {

            tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            viewPager = (ViewPager) findViewById(R.id.view_pager);
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabLayout.setHorizontalScrollBarEnabled(true);


            /*recyclerView = (RecyclerView) findViewById(R.id.recycleView);
            recyclerView.setHasFixedSize(true);

            staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);

            this.onScrollRecycleView();

            sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);*/

            //sharedPreferences.registerOnSharedPreferenceChangeListener(spChange);
            if (savedInstanceState != null) {
                ListCategory lc = (ListCategory) savedInstanceState.getSerializable(ListCategory.key);
                //ListPackages lp = (ListPackages) savedInstanceState.getSerializable(ListPackages.key);
                categoryList = (lc != null) ? lc.categoryList : categoryList;
                //packagesList = (lp != null) ? lp.packagesList : packagesList ;
            }

            /*Bundle bundle = getIntent().getExtras();

            if(bundle != null){
                ListCategory lc = bundle.getSerializable(ListCategory.key) != null ? (ListCategory) bundle.getSerializable(ListCategory.key): null;
                ListPackages lp = bundle.getSerializable(ListPackages.key) != null ? (ListPackages) bundle.getSerializable(ListPackages.key): null;
                categoryList = lc != null ? lc.categoryList : categoryList;
                packagesList = lp != null ? lp.packagesList : packagesList;
            }*/

            if (categoryList.size() == 0 || categoryList == null) {
                new MainActivity.getPackages(null).execute(Routes.packages[0]);
            } else {
                setShowRecyclerView();
            }


        }else{
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();
        }

    }

    private void setShowRecyclerView(){
        viewPager.setAdapter(
                new CategoryFragmentStatePagerAdapter(
                        getSupportFragmentManager(),
                        categoryList
                )
        );
        progress.setVisibility(View.GONE);

        //packagesAdapter = new PackagesAdapter(context, packagesList);
        //recyclerView.setAdapter(packagesAdapter);
        //recyclerView.addOnItemTouchListener(new MainActivity.RecyclerViewOnTouchListener(context, recyclerView, MainActivity.this));
    }

    private class CategoryFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

        private String[] tabTitles;
        private List<Category> categoryList;

        private CategoryFragmentStatePagerAdapter(FragmentManager fm, List<Category> categoryList) {
            super(fm);
            this.categoryList = categoryList;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putInt("keySequence", position);
            Category category = this.categoryList.get(position);
            args.putSerializable(Category.key+""+position, category);
            CategoryFragment categoryFragment = new CategoryFragment();
            categoryFragment.setArguments(args);
            return categoryFragment;
        }

        @Override
        public int getCount() {
            return this.categoryList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.categoryList.get(position).getName();
        }
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
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                int[] aux = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(null);
                int max = -1;

                for(int i = 0; i < aux.length; i++){
                    max = aux[i] > max ? aux[1] : max;
                }

                packagesAdapter = (PackagesAdapter) recyclerView.getAdapter();
                /*if(packagesList.size() == gridLayoutManager.findLastCompletelyVisibleItemPosition() + 1){

                }*/

                if(packagesList.size() == max){

                }

            }
        });
    }

    /**
     * Método executado ao trocar de layout no viewflipper
     */
    View.OnLayoutChangeListener onLayoutChangeListenerViewFlipper = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            /*ImageView img = (ImageView) v.findViewById(R.id.slide2);
            img.setImageResource(R.drawable.slide1);*/
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            //finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }

    /* @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(spChange);
        sharedPreferences.edit().clear().commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(spChange);
        sharedPreferences.edit().clear().commit();
    }*/

    public class getPackages extends AsyncTask<String, Void, JSONObject> {

        String params;
        private JSONArray packages;

        private getPackages(String params){
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
            packagesList.clear();
            try{
                if(!result.has("error")){
                    JSONArray arrPackage = result.getJSONArray("success");

                    for(int i = 0; i < arrPackage.length(); i++){
                        try{
                            JSONObject objPackage = arrPackage.getJSONObject(i);
                            packagesList.add(new Package(objPackage.getInt("id"), objPackage.getString("name"), objPackage.getString("description"), objPackage.getDouble("value")));
                            //packages.add(new Package(2, "Completo", "Pacote completo", 50.00));
                        }catch (JSONException ex){
                            ex.printStackTrace();
                        }
                    }

                    //Toast.makeText(getContext(), result.getJSONObject("success").toString(), Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){

            }

            categoryList.add(new Category(
                    "Todos",
                    "todos",
                    packagesList
            ));


            new MainActivity.getCategories(null).execute(Routes.categories[0]);

            //packagesAdapter = new PackagesAdapter(context, packagesList);
            //recyclerView.setAdapter(packagesAdapter);
            //recyclerView.addOnItemTouchListener(new MainActivity.RecyclerViewOnTouchListener(context, recyclerView, MainActivity.this));

            //packagesAdapter.setRecyclerViewOnClickListenerHack(HomeFragment.this);

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

    /**
     * Classe responsável por executar busca de um determinado pacote, com o id como parâmetro
     */
    private class findPackage extends AsyncTask<String, Void, JSONObject> {

        String params;
        private JSONArray packages;

        private findPackage(String params){
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
                if(!result.has("error")){
                    JSONObject objPackage = result.getJSONObject("success");

                    Bundle args = new Bundle();
                    args.putString("package", objPackage.toString());

                    Intent intent = new Intent(context, PackageShowActivity.class);
                    intent.putExtras(args);
                    startActivity(intent);


                    //Toast.makeText(getContext(), result.getJSONObject("success").toString(), Toast.LENGTH_SHORT).show();
                    //progressDialog.dismiss();

                }
            }catch (JSONException e){
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

    public class getCategories extends AsyncTask<String, Void, JSONObject> {

        String params;

        private getCategories(String params){
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
                if(!result.has("error")){
                    JSONArray arrCategory = result.getJSONArray("success");

                    for(int i = 0; i < arrCategory.length(); i++){
                        try{
                            JSONObject objCategory = arrCategory.getJSONObject(i);
                            JSONArray arrPackages = objCategory.getJSONArray("packages");

                            List<Package> allPackages = new ArrayList<>();
                            for(int j = 0; j < arrPackages.length(); j++){
                                allPackages.add(
                                        new Package(
                                                arrPackages.getJSONObject(j).getInt("id"),
                                                arrPackages.getJSONObject(j).getString("name"),
                                                arrPackages.getJSONObject(j).getString("description"),
                                                arrPackages.getJSONObject(j).getDouble("value")
                                        )
                                );
                            }

                            categoryList.add(
                                    new Category(
                                            objCategory.getString("name"),
                                            objCategory.getString("slug"),
                                            allPackages
                                    )
                            );

                        }catch (JSONException ex){
                            ex.printStackTrace();
                        }
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            viewPager.setAdapter(
                    new CategoryFragmentStatePagerAdapter(
                            getSupportFragmentManager(),
                            categoryList
                    )
            );
            progress.setVisibility(View.GONE);
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
