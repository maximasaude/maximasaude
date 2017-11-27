package com.saude.maxima;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.saude.maxima.Adapters.Package.Package;
import com.saude.maxima.Adapters.Package.PackagesAdapter;
import com.saude.maxima.interfaces.RecyclerViewOnClickListenerHack;
import com.saude.maxima.utils.Auth;
import com.saude.maxima.utils.ManagerSharedPreferences;
import com.saude.maxima.utils.Routes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RecyclerViewOnClickListenerHack{

    private float startX;
    private float lastX;
    Activity activity;
    Context context;
    PackagesAdapter packagesAdapter;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    String params, url;
    LinearLayout content;

    List<Package> packagesList = new ArrayList<Package>();
    AutoCompleteTextView edtName;
    AutoCompleteTextView edtEmail;
    ViewFlipper viewFlipper;


    ProgressDialog progressDialog;

    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;


    private FragmentManager fm = getSupportFragmentManager();
    NavigationView navigationView = null;

    TextView name;
    TextView email;

    LinearLayout navHeader;
    LinearLayout navContentLogo;

    private Auth auth;
    private JSONObject user;
    SharedPreferences sharedPreferences;
    SharedPreferences.OnSharedPreferenceChangeListener spChange = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.i("Script alterado", key+"updated");
        }
    };

    private void refreshContent(){
        recreate();
    }

    @Override
    public void OnClickListener(View view, int position) {
        progressDialog.show();

        int package_id = packagesList.get(position).getId();

        url = Routes.packages[1].replace("{id}", ""+package_id);

        new findPackage(null).execute(url);
    }

    @Override
    public void OnLongPressClickListener(View view, int position) {
        Toast.makeText(context, "Postion: "+ position, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ListPackages.key, new ListPackages(packagesList));
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getApplicationContext();
        this.activity = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        viewFlipper = (ViewFlipper) findViewById(R.id.view_fliper);

        viewFlipper.setFlipInterval(3000);
        viewFlipper.startFlipping();
        Animation in = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
        viewFlipper.setInAnimation(in);
        viewFlipper.setOutAnimation(out);


        //viewFlipper.setOnTouchListener(onSwipeTouchListener);
        viewFlipper.addOnLayoutChangeListener(onLayoutChangeListenerViewFlipper);

        edtEmail = (AutoCompleteTextView) findViewById(R.id.edtEmail);
        edtName = (AutoCompleteTextView) findViewById(R.id.edtName);

        edtEmail.clearFocus();
        edtName.clearFocus();


        //Método executado ao tocar no viewflipper
        //Faz as trocas de imagens
        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                viewFlipper.stopFlipping();
                int action = event.getActionMasked();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        float endX = event.getX();
                        float endY = event.getY();

                        //swipe right
                        if (startX < endX) {
                            viewFlipper.showNext();
                            viewFlipper.startFlipping();
                        }
                        //swipe left
                        if (startX > endX) {
                            viewFlipper.showPrevious();
                            viewFlipper.startFlipping();
                        }

                        break;
                }
                return true;
            }
        });

        content = (LinearLayout) findViewById(R.id.content);
        content.setVisibility(View.INVISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        this.onScrollRecycleView();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        navigationView = (NavigationView) findViewById(R.id.nav_view);


        navHeader = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.nav_header);
        navContentLogo = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.nav_content_logo);

        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        //sharedPreferences.registerOnSharedPreferenceChangeListener(spChange);
        if (savedInstanceState != null) {
            ListPackages lp = (ListPackages) savedInstanceState.getSerializable(ListPackages.key);
            packagesList = lp.packagesList;
            /*HomeFragment homeFragment = new HomeFragment();
            FragmentTransaction fragmentHomeTransaction = getSupportFragmentManager().beginTransaction();
            fragmentHomeTransaction.add(R.id.content_fragment, homeFragment);
            fragmentHomeTransaction.commit();*/
        }

        if(packagesList.size() == 0 || packagesList == null){
            new getPackages(null).execute(Routes.packages[0]);
        }else{
            setShowRecyclerView();
        }





        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.name);
        email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);

        //Instancia para a classe auth
        this.auth = new Auth(getApplicationContext());

        //Pegando os dados do usuário, caso esteja logado
        this.user = this.auth.getAuth();

    /*if(sharedPreferences.contains("user")){
        navigationView.getMenu().getItem(1).setVisible(false);
    }*/

        //Setando true para o menu Home
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.getMenu().findItem(R.id.optionUser).setVisible(false);

        //Verifico se o usuário está logado
        if (Auth.isLogged()) {
            navContentLogo.setVisibility(View.GONE);
            navHeader.setVisibility(View.VISIBLE);
            //Setando false para o menu login não ficar visível
            navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().findItem(R.id.optionUser).setVisible(true);
            try {
                name.setText(this.user.get("name").toString());
                email.setText(this.user.get("email").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void setShowRecyclerView(){
        packagesAdapter = new PackagesAdapter(context, packagesList);
        recyclerView.setAdapter(packagesAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewOnTouchListener(context, recyclerView, MainActivity.this));
        content.setVisibility(View.VISIBLE);
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

    /**
     *
     */
    private void addCallBackChangeFragment(){
        getSupportFragmentManager().addOnBackStackChangedListener(
            new FragmentManager.OnBackStackChangedListener() {
                public void onBackStackChanged() {
                    if(sharedPreferences.contains("user")){
                        navigationView.getMenu().getItem(1).setVisible(false);
                    }
                    Fragment current = fm.findFragmentById(R.id.content_fragment);
                    if (current instanceof HomeFragment) {
                        navigationView.setCheckedItem(R.id.nav_home);
                    } else if(current instanceof LoginFragment){
                       navigationView.setCheckedItem(R.id.nav_login);
                    }else if(current instanceof DiaryFragment){
                        navigationView.setCheckedItem(R.id.nav_cart);
                    }
                }
            }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_close) {
            finish();
            System.exit(0);
        }else if(id == R.id.action_logout){
            ManagerSharedPreferences managerSharedPreferences = new ManagerSharedPreferences(this);
            managerSharedPreferences.remove("user");
            recreate();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            HomeFragment homeFragment = new HomeFragment();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.addToBackStack(getString(R.string.addToBackStack));
            fragmentTransaction.replace(R.id.content_fragment, homeFragment, "home").commit();

        }else if (id == R.id.nav_login) {
            //LoginFragment loginFragment = new LoginFragment();
            //FragmentTransaction fragmentTransaction = fm.beginTransaction();
            //fragmentTransaction.addToBackStack(getString(R.string.addToBackStack));
            //fragmentTransaction.replace(R.id.content_fragment, loginFragment, "login").commit();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult(intent, 2);
        }else if (id == R.id.nav_cart) {
            DiaryFragment diaryFragment = new DiaryFragment();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.addToBackStack(getString(R.string.addToBackStack));
            fragmentTransaction.replace(R.id.content_fragment, diaryFragment, "diary").commit();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 2) {
            recreate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(getString(R.string.executing));
            progressDialog.setCancelable(false);
            progressDialog.show();
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
                    progressDialog.dismiss();

                }
            }catch (JSONException e){
                progressDialog.dismiss();
            }
            progressDialog.dismiss();
            packagesAdapter = new PackagesAdapter(context, packagesList);
            //packagesAdapter.setRecyclerViewOnClickListenerHack(HomeFragment.this);
            recyclerView.setAdapter(packagesAdapter);
            recyclerView.addOnItemTouchListener(new RecyclerViewOnTouchListener(context, recyclerView, MainActivity.this));
            //gridView.setAdapter(packagesAdapter);
            content.setVisibility(View.VISIBLE);
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
                    progressDialog.dismiss();
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
