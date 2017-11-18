package com.saude.maxima.Adapters.Package;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.saude.maxima.R;
import com.saude.maxima.interfaces.RecyclerViewOnClickListenerHack;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junnyor on 21/10/2017.
 */

public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.PackagesViewHolder> {

    private List<Package> packages;
    private LayoutInflater layoutInflater;
    private RecyclerViewOnClickListenerHack recyclerViewOnClickListenerHack;

    public PackagesAdapter(Context context, List<Package> packages){
        this.packages = packages;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        recyclerViewOnClickListenerHack = r;
    }

    @Override
    public PackagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.grid_view_home, parent, false);
        PackagesViewHolder packagesViewHolder = new PackagesViewHolder(view);
        return packagesViewHolder;
    }

    @Override
    public void onBindViewHolder(PackagesViewHolder holder, int position) {

        if(packages.get(position).getName().equals("Simples")){
            holder.img.setImageResource(R.drawable.simples);
        }else if(packages.get(position).getName().equals("Completo")){
            holder.img.setImageResource(R.drawable.completo);
        }else{
            holder.img.setImageResource(R.drawable.premium);
        }


        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        double value;
        value = packages.get(position).getValue();

        holder.value.setText(numberFormat.format(value));

        holder.name.setText(packages.get(position).getName());
        holder.description.setText(packages.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return packages.size();
    }

    public class PackagesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView img;
        public TextView value;
        public TextView name;
        public TextView description;

        public PackagesViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img_package);
            value = (TextView) itemView.findViewById(R.id.value_package);
            name = (TextView) itemView.findViewById(R.id.name_package);
            description = (TextView) itemView.findViewById(R.id.description_package);
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
