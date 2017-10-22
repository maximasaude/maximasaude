package com.saude.maxima;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.saude.maxima.Adapters.Package.Package;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    TextView data;
    GridView gridView;
    Context context;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = view.getContext();

        ArrayList<Package> packages = new ArrayList<Package>();
        packages.add(new Package());
        packages.add(new Package());

        data = (TextView) view.findViewById(R.id.data);
        gridView = (GridView) view.findViewById(R.id.gridView);
        //SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        //data.setText(sharedPreferences.getString("user", "null"));

        // Inflate the layout for this fragment
        return view;
    }

}
