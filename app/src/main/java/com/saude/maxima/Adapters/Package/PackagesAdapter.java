package com.saude.maxima.Adapters.Package;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Junnyor on 21/10/2017.
 */

public class PackagesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Time> list;

    public PackagesAdapter(Context context, ArrayList<Time> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null;
    }
}
