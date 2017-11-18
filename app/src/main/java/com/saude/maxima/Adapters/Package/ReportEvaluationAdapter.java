package com.saude.maxima.Adapters.Package;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.saude.maxima.R;
import com.saude.maxima.interfaces.RecyclerViewOnClickListenerHack;

import java.util.List;

/**
 * Created by Junnyor on 15/11/2017.
 */

public class ReportEvaluationAdapter extends RecyclerView.Adapter<ReportEvaluationAdapter.ReportEvaluationViewHolder> {

    private List<Evaluation> evaluations;
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

    public ReportEvaluationAdapter(Context context, List<Evaluation> evaluations){
        this.evaluations = evaluations;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        recyclerViewOnClickListenerHack = r;
    }

    @Override
    public ReportEvaluationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.report_evaluation_adapter, parent, false);
        ReportEvaluationViewHolder reportEvaluationViewHolder = new ReportEvaluationViewHolder(view);
        return reportEvaluationViewHolder;
    }

    @Override
    public void onBindViewHolder(ReportEvaluationViewHolder holder, int position) {
        holder.txtDate.setText(evaluations.get(position).getCreatedAtFormatted());
    }

    @Override
    public int getItemCount() {
        return evaluations.size();
    }


    public class ReportEvaluationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtDate;

        private ReportEvaluationViewHolder(View itemView) {
            super(itemView);
            //img = (ImageView) itemView.findViewById(R.id.img_package);
            //txtPackageValue = (TextView) itemView.findViewById(R.id.package_name);
            //name = (TextView) itemView.findViewById(R.id.name_package);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            //description = (TextView) itemView.findViewById(R.id.description_package);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(recyclerViewOnClickListenerHack != null){
                recyclerViewOnClickListenerHack.OnClickListener(view, getPosition());
            }
        }
    }


}
