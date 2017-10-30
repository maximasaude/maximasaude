package com.saude.maxima.Adapters.Package;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.saude.maxima.R;

import java.util.ArrayList;

/**
 * Created by Junnyor on 21/10/2017.
 */

public class PackagesAdapterr extends ArrayAdapter<Package> {

    private Context context;
    private ArrayList<Package> list;

    TextView value, name, description;
    ImageView img;

    public PackagesAdapterr(Context context, ArrayList<Package> list){
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Package el = this.list.get(position);

        if(convertView == null)
            convertView = LayoutInflater.from(this.context).inflate(R.layout.grid_view_home, null);

        img = (ImageView) convertView.findViewById(R.id.img_package);
        img.setImageResource(R.drawable.android_1);

        name = (TextView) convertView.findViewById(R.id.name_package);
        name.setText(el.getName());
        name.setTextSize(20);

        value = (TextView) convertView.findViewById(R.id.value_package);
        value.setText("R$ "+Double.toString(el.getValue()));
        value.setTextSize(20);


        return convertView;
    }
}
