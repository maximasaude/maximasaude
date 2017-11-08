package com.saude.maxima;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.saude.maxima.Adapters.Package.Package;
import com.saude.maxima.Adapters.Package.PackagesAdapterr;
import com.saude.maxima.utils.Routes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by junnyor on 11/2/17.
 */

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment{

    private ExpandableHeightGridView gridView;
    private ProgressDialog progressDialog;
    private ArrayList<Package> packagesList;
    private PackagesAdapterr packagesAdapter;
    private String url;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_cart, container, false);

        this.context = getActivity();

        //gridView = (ExpandableHeightGridView) view.findViewById(R.id.cart_grid);

        //gridView.setExpanded(true);
        //this.onClickGridView();
        //progressDialog = new ProgressDialog(this.context);

        //progressDialog.setMessage(context.getResources().getString(R.string.executing));
        //progressDialog.setCancelable(false);
        //progressDialog.show();
        //new getPackages(null).execute(Routes.packages[0]);

        return view;

    }

    private void onClickGridView(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //progressDialog.show();

                int package_id = packagesList.get(position).getId();
                Toast.makeText(context, "position: "+package_id, Toast.LENGTH_SHORT).show();

                //url = Routes.packages[1].replace("{id}", ""+package_id);

                //new HomeFragment.findPackage(null).execute(url);

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

    private class getPackages extends AsyncTask<String, Void, JSONObject> {

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
            packagesAdapter = new PackagesAdapterr(context, packagesList);
            gridView.setAdapter(packagesAdapter);
        }


        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(JSONObject result) {
        }

        private String getParams() {
            return params;
        }

        private void setParams(String params) {
            this.params = params;
        }
    }
}
