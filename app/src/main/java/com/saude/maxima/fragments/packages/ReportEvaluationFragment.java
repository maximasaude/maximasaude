package com.saude.maxima.fragments.packages;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.saude.maxima.Adapters.Package.Evaluation;
import com.saude.maxima.Adapters.Package.ReportEvaluationAdapter;
import com.saude.maxima.Adapters.Package.ReportScheduledAdapter;
import com.saude.maxima.Adapters.Package.Schedule;
import com.saude.maxima.Connection;
import com.saude.maxima.R;
import com.saude.maxima.ReportEvaluationShowActivity;
import com.saude.maxima.interfaces.RecyclerViewOnClickListenerHack;
import com.saude.maxima.utils.Auth;
import com.saude.maxima.utils.Routes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportEvaluationFragment extends Fragment implements RecyclerViewOnClickListenerHack{

    View view;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    ReportEvaluationAdapter reportEvaluationAdapter;
    List<Evaluation> evaluationList;
    ProgressBar progressBar;

    public ReportEvaluationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_report_evaluation, container, false);
        // Inflate the layout for this fragment

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getContext());
        //gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        //staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        this.onScrollRecycleView();

        Auth auth = new Auth(getContext());
        int id = 0;
        try {
            id = auth.getAuth().getInt("id");
        }catch (JSONException e){
            e.printStackTrace();
        }

        //Executando busca de todos os pacotes disponíveis na API
        String url = Routes.evaluations[1].replace("{id}", ""+id);
        new getEvaluations(null).execute(url);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void OnClickListener(View view, int position) {
        //Evaluation evaluation = this.evaluationList.get(position);
        Intent intent = new Intent(getContext(), ReportEvaluationShowActivity.class);
        JSONObject objEvaluation = this.evaluationList.get(position).getEvaluation();
        JSONObject objAnthropometry = this.evaluationList.get(position).getAnthropometry();
        JSONObject objUser = this.evaluationList.get(position).getUser();
        intent.putExtra("evaluation", objEvaluation.toString());
        intent.putExtra("anthropometry", objAnthropometry.toString());
        intent.putExtra("user", objUser.toString());
        startActivity(intent);
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

    public class getEvaluations extends AsyncTask<String, Void, JSONObject> {

        String params;
        private JSONArray schedules;

        private getEvaluations(String params){
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
            evaluationList = new ArrayList<Evaluation>();
            try{
                if(!result.has("errors")){
                    JSONArray arrEvaluation = result.getJSONArray("success");

                    for(int i = 0; i < arrEvaluation.length(); i++){
                        try{
                            JSONObject objEvaluation = arrEvaluation.getJSONObject(i);
                            JSONObject objAnthropometry = objEvaluation.getJSONObject("anthropometry");
                            JSONObject objUser = objEvaluation.getJSONObject("user");
                            evaluationList.add(new Evaluation(objEvaluation, objAnthropometry, objUser));
                            //packages.add(new Package(2, "Completo", "Pacote completo", 50.00));
                        }catch (JSONException ex){
                            ex.printStackTrace();
                        }
                    }

                    progressBar.setVisibility(View.GONE);

                }
            }catch (JSONException e){
                progressBar.setVisibility(View.GONE);
            }

            //TextView empty = (TextView) view.findViewById(R.id.empty);
            if(evaluationList.size() == 0){
                //empty.setVisibility(View.VISIBLE);
            }

            progressBar.setVisibility(View.GONE);
            reportEvaluationAdapter = new ReportEvaluationAdapter(getContext(), evaluationList);
            recyclerView.setAdapter(reportEvaluationAdapter);
            recyclerView.addOnItemTouchListener(new RecyclerViewOnTouchListener(getContext(), recyclerView, ReportEvaluationFragment.this));
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
