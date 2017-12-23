package com.saude.maxima.Adapters.Spinner;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by junnyor on 12/21/17.
 */

public class SupplierAdapter extends BaseAdapter {
    private Context context;
    private String[] lista;

    public SupplierAdapter(Context context, String[] lista){
        this.context = context;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.length;
    }

    @Override
    public Object getItem(int i) {
        return lista[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        TextView tv = new TextView(context);
        tv.setText(lista[i]);
        tv.setTextColor(Color.RED);

        return tv;
    }
}
