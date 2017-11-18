package com.saude.maxima.Adapters.Package;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

public class ReportScheduledAdapter extends RecyclerView.Adapter<ReportScheduledAdapter.ReportSimpleViewHolder> {

    private List<Schedule> schedules;
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

    public ReportScheduledAdapter(Context context, List<Schedule> schedules){
        this.schedules = schedules;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        recyclerViewOnClickListenerHack = r;
    }

    @Override
    public ReportSimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.report_scheduled_adapter, parent, false);
        ReportSimpleViewHolder reportSimpleViewHolder = new ReportSimpleViewHolder(view);
        return reportSimpleViewHolder;
    }

    @Override
    public void onBindViewHolder(ReportSimpleViewHolder holder, int position) {
        holder.txtScheduledDate.setText(schedules.get(position).getDay()+"/"+schedules.get(position).getMonth()+"/"+schedules.get(position).getYear()+" "+schedules.get(position).getHour()+":"+schedules.get(position).getMinute());
        holder.txtPackageName.setText(schedules.get(position).getPackageName());
        holder.txtPackageValue.setText(schedules.get(position).getPackageValueFormated());
        holder.txtCity.setText(schedules.get(position).getCity());
        holder.txtAddress.setText(schedules.get(position).getAddress());
        String status = this.getStatus(schedules.get(position).getStatus());
        holder.txtStatus.setText(status);
        holder.txtDistrict.setText(schedules.get(position).getDistrict());
        holder.txtZip.setText(schedules.get(position).getZip());
        holder.txtState.setText(schedules.get(position).getState());
    }

    private String getStatus(int status){
        String value;
        switch (status){
            case 1: {
                value = "Aguardando pagamento";
                break;
            }
            case 2: {
                value = "Em análise";
                break;
            }
            case 3: {
                value = "Paga";
                break;
            }
            case 4: {
                value = "Disponível";
                break;
            }
            case 5: {
                value = "Em disputa";
                break;
            }
            case 6: {
                value = "Devolvida";
                break;
            }
            default:
                value = "Cancelada";
                break;
        }
        return value;
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }


    public class ReportSimpleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtPackageValue;
        private TextView txtPackageName;
        private TextView txtAddress;
        private TextView txtCity;
        private TextView txtStatus;
        private TextView txtScheduledDate;
        private TextView txtDistrict;
        private TextView txtZip;
        private TextView txtState;

        private ReportSimpleViewHolder(View itemView) {
            super(itemView);
            txtScheduledDate = (TextView) itemView.findViewById(R.id.txtScheduledDate);
            txtPackageName = (TextView) itemView.findViewById(R.id.txtPackageName);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
            txtCity = (TextView) itemView.findViewById(R.id.txtCity);
            txtPackageValue = (TextView) itemView.findViewById(R.id.txtPackageValue);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            txtDistrict = (TextView) itemView.findViewById(R.id.txtDistrict);
            txtZip = (TextView) itemView.findViewById(R.id.txtZip);
            txtState = (TextView) itemView.findViewById(R.id.txtState);
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
