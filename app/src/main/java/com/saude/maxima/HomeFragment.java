package com.saude.maxima;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
public class HomeFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    private float startX;
    private float lastX;

    TextView data;
    ExpandableHeightGridView gridView;
    Context context;
    PackagesAdapter packagesAdapter;

    RecyclerView recyclerView;

    SwipeRefreshLayout swipeRefreshLayout;
    String params, url;

    LinearLayout content;

    List<Package> packagesList;

    Button prev, next;

    ViewFlipper viewFlipper;

    AutoCompleteTextView edtName;
    AutoCompleteTextView edtEmail;


    ProgressDialog progressDialog;


    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;


    public HomeFragment() {
        // Required empty public constructor
    }


    private void refreshContent(){
        ((Activity) context).recreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = view.getContext();

        //Swipe Refresh Layout
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });

        viewFlipper = (ViewFlipper) view.findViewById(R.id.view_fliper);

        viewFlipper.setFlipInterval(3000);
        viewFlipper.startFlipping();
        Animation in = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right);
        viewFlipper.setInAnimation(in);
        viewFlipper.setOutAnimation(out);


        //viewFlipper.setOnTouchListener(onSwipeTouchListener);
        viewFlipper.addOnLayoutChangeListener(onLayoutChangeListenerViewFlipper);

        edtEmail = (AutoCompleteTextView) view.findViewById(R.id.edtEmail);
        edtName = (AutoCompleteTextView) view.findViewById(R.id.edtName);

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

        content = (LinearLayout) view.findViewById(R.id.content);
        content.setVisibility(View.INVISIBLE);

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage(getString(R.string.executing));
        progressDialog.setCancelable(false);
        progressDialog.show();


        //Setando tamanho expandido para a gridview
        //gridView = (ExpandableHeightGridView) view.findViewById(R.id.gridView);
        //gridView.setExpanded(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);

        //linearLayoutManager = new LinearLayoutManager(getActivity());
        //gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        this.onScrollRecycleView();

        //Executando busca de todos os pacotes disponíveis na API
        new getPackages(null).execute(Routes.packages[0]);

        //data = (TextView) view.findViewById(R.id.data);



        //Implementa o listener do gridview
        //this.onClickGridView();

        //gridView.setAdapter(packages);
        //SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        //data.setText(sharedPreferences.getString("user", "null"));

        // Inflate the layout for this fragment
        return view;
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


    private void onClickGridView(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                progressDialog.show();

                int package_id = packagesList.get(position).getId();

                url = Routes.packages[1].replace("{id}", ""+package_id);

                new findPackage(null).execute(url);

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
    public void OnClickListener(View view, int position) {
        progressDialog.show();

        int package_id = packagesList.get(position).getId();

        url = Routes.packages[1].replace("{id}", ""+package_id);

        new findPackage(null).execute(url);
    }

    @Override
    public void OnLongPressClickListener(View view, int position) {
        Toast.makeText(getActivity(), "Postion: "+ position, Toast.LENGTH_SHORT).show();
    }

    public class getPackages extends AsyncTask<String, Void, JSONObject>{

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

            packagesList = new ArrayList<Package>();
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
            packagesAdapter = new PackagesAdapter(getActivity(), packagesList);
            //packagesAdapter.setRecyclerViewOnClickListenerHack(HomeFragment.this);
            recyclerView.setAdapter(packagesAdapter);
            recyclerView.addOnItemTouchListener(new RecyclerViewOnTouchListener(getActivity(), recyclerView, HomeFragment.this));
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

            packagesList = new ArrayList<Package>();
            try{
                if(!result.has("error")){
                    progressDialog.dismiss();
                    JSONObject objPackage = result.getJSONObject("success");

                    Bundle args = new Bundle();
                    args.putString("package", objPackage.toString());

                    /*Intent intent = new Intent(getContext(), PackageShowActivity.class);
                    intent.putExtras(args);
                    startActivity(intent);*/

                    PackageShowFragment packageShowFragment =  new PackageShowFragment();
                    packageShowFragment.setArguments(args);

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(getString(R.string.addToBackStack));
                    fragmentTransaction.replace(R.id.content_fragment, packageShowFragment).commit();

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

}
