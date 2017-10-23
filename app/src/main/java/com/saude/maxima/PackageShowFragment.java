package com.saude.maxima;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class PackageShowFragment extends Fragment {

    private JSONObject data_package;

    TextView name, description;

    public PackageShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_package_show, container, false);

        name = (TextView) view.findViewById(R.id.name);
        description = (TextView) view.findViewById(R.id.description);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            try{
                data_package = new JSONObject(args.getString("package"));
                name.setText(data_package.getString("name"));

                description.setText(data_package.getString("description"));
            }catch (JSONException e){
                e.printStackTrace();
            }
            //Toast.makeText(getContext(), args.toString(), Toast.LENGTH_LONG).show();
        }
    }

}
