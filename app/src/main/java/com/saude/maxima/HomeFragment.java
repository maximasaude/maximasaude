package com.saude.maxima;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saude.maxima.Adapters.Package.Package;
import com.saude.maxima.Adapters.Package.PackagesAdapter;
import com.saude.maxima.utils.Routes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    TextView data;
    ExpandableHeightGridView gridView;
    Context context;
    ArrayAdapter<Package> packagesAdapter;

    String params, url;

    LinearLayout content;

    ArrayList<Package> packagesList;

    ProgressDialog progressDialog;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = view.getContext();

        content = (LinearLayout) view.findViewById(R.id.content);
        content.setVisibility(View.INVISIBLE);

        progressDialog = new ProgressDialog(getContext());

        progressDialog.setMessage(getString(R.string.executing));
        progressDialog.show();


        gridView = (ExpandableHeightGridView) view.findViewById(R.id.gridView);
        gridView.setExpanded(true);


        new getPackages(null).execute(Routes.packages[0]);

        //data = (TextView) view.findViewById(R.id.data);



        this.onClickGridView();

        //gridView.setAdapter(packages);
        //SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        //data.setText(sharedPreferences.getString("user", "null"));

        // Inflate the layout for this fragment
        return view;
    }

    private void onClickGridView(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                progressDialog.show();

                int package_id = packagesList.get(position).getId();

                url = Routes.packages[1].replace("{id}", ""+package_id);

                new findPackage(null).execute(url);

                //new findPackage("").execute(Routes.packages[1]);
                //TextView name = (TextView) view.findViewById(R.id.name_package);
                //Toast.makeText(getContext(), name.getText(), Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent(getContext(), Main2Activity.class);
                Bundle params = new Bundle();
                params.putString("name", name.getText().toString());
                intent.putExtras(params);
                startActivity(intent);*/
            }
        });
    }

    public class getPackages extends AsyncTask<String, Void, JSONObject>{

        String params;
        private JSONArray packages;

        private getPackages(String params){
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

            packagesList = new ArrayList<Package>();
            try{
                if(!result.has("error")){
                    JSONArray arrPackage = result.getJSONArray("success");

                    for(int i = 0; i < arrPackage.length(); i++){
                        try{
                            JSONObject objPackage = arrPackage.getJSONObject(i);
                            packagesList.add(new Package(objPackage.getInt("id"), objPackage.getString("name"), objPackage.getString("description"), objPackage.getDouble("value")));
                            //packages.add(new Package(2, "Completo", "Pacote completo", 50.00));
                        }catch (JSONException ex){
                            ex.printStackTrace();
                        }
                    }

                    //Toast.makeText(getContext(), result.getJSONObject("success").toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }catch (JSONException e){
                progressDialog.dismiss();
            }
            progressDialog.dismiss();
            packagesAdapter = new PackagesAdapter(getContext(), packagesList);
            gridView.setAdapter(packagesAdapter);
            content.setVisibility(View.VISIBLE);
        }


        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(JSONObject result) {
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }
    }

    public class findPackage extends AsyncTask<String, Void, JSONObject> {

        String params;
        private JSONArray packages;

        private findPackage(String params){
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

            packagesList = new ArrayList<Package>();
            try{
                if(!result.has("error")){
                    progressDialog.dismiss();
                    JSONObject objPackage = result.getJSONObject("success");

                    Bundle args = new Bundle();
                    args.putString("package", objPackage.toString());

                    PackageShowFragment packageShowFragment =  new PackageShowFragment();
                    packageShowFragment.setArguments(args);

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(getString(R.string.addToBackStack));
                    fragmentTransaction.replace(R.id.content_fragment, packageShowFragment).commit();

                    //Toast.makeText(getContext(), result.getJSONObject("success").toString(), Toast.LENGTH_SHORT).show();
                    //progressDialog.dismiss();

                }
            }catch (JSONException e){
                progressDialog.dismiss();
            }
            progressDialog.dismiss();
            packagesAdapter = new PackagesAdapter(getContext(), packagesList);
            gridView.setAdapter(packagesAdapter);
            content.setVisibility(View.VISIBLE);
        }


        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(JSONObject result) {
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }

    }


    }
