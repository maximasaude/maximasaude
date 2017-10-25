package com.saude.maxima.fragments.packages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saude.maxima.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by junnyor on 10/23/17.
 */

public class PackageDescriptionFragment extends Fragment {

    JSONObject data_package;
    TextView name, description;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_package_description, container, false);

        Bundle args = getArguments();

        description = (TextView) view.findViewById(R.id.description);
        if (args != null) {
            try{
                data_package = new JSONObject(args.getString("package"));

                description.setText(data_package.getString("description"));
            }catch (JSONException e){
                e.printStackTrace();
            }
            //Toast.makeText(getContext(), args.toString(), Toast.LENGTH_LONG).show();
        }

        return view;
    }
}
