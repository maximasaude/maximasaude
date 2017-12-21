package com.saude.maxima;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements RecyclerViewOnClickListenerHack{

    View view;
    TextView name;
    int keySequence;
    RecyclerView recyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    PackagesAdapter packagesAdapter;
    List<Package> packagesList = new ArrayList<>();
    Context context;
    String url;

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void OnClickListener(View view, int position) {
        int package_id = packagesList.get(position).getId();

        url = Routes.packages[1].replace("{id}", ""+package_id);

        new CategoryFragment.findPackage(null).execute(url);
    }

    @Override
    public void OnLongPressClickListener(View view, int position) {
        Toast.makeText(context, "Postion: "+ position, Toast.LENGTH_SHORT).show();
    }

    /**
     * Função que verifica se há conexão com a internet
     * @return boolean
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        Bundle bundle = getArguments();
        this.keySequence = bundle.getInt("keySequence");

        Category category = (Category) bundle.getSerializable(Category.key+""+this.keySequence);

        if(category != null){
            packagesList = category.getPackages();
        }

        view = inflater.inflate(R.layout.fragment_category, container, false);

        if(this.isOnline()){
            recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
            recyclerView.setHasFixedSize(true);
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            this.onScrollRecycleView();


            packagesAdapter = new PackagesAdapter(context, packagesList);
            recyclerView.setAdapter(packagesAdapter);
            recyclerView.addOnItemTouchListener(new CategoryFragment.RecyclerViewOnTouchListener(context, recyclerView, CategoryFragment.this));

        }


        // Inflate the layout for this fragment
        return view;
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
     * Classe responsável por executar busca de um determinado pacote, com o id como parâmetro
     */
    private class findPackages extends AsyncTask<String, Void, JSONObject> {

        String params;
        private JSONArray packages;

        private findPackages(String params){
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

                    Package package_obj = new Package(
                            objPackage.getInt("id"),
                            objPackage.getString("name"),
                            objPackage.getString("description"),
                            objPackage.getDouble("value")
                    );

                    Bundle args = new Bundle();
                    args.putSerializable("package_obj", package_obj);
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

}
