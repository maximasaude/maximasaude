package com.saude.maxima;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.saude.maxima.utils.ManagerSharedPreferences;
import com.saude.maxima.utils.Routes;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddUserFragment extends Fragment {

    EditText edtName, edtPassword, edtEmail, edtConfirmPassword;
    Button btnCancel, btnRegister;
    RadioGroup radioGroup;
    RadioButton gender;


    ProgressDialog progressDialog;


    TextView emailUser;
    TextView nameUser;

    String url = "";
    String params = "";

    ManagerSharedPreferences managerSharedPreferences;

    private static String[] routes = {
            "http://10.0.0.103:8000/oauth/token",
            "http://10.0.0.103:8000/api/user"
    };

    public AddUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);

        managerSharedPreferences = new ManagerSharedPreferences(getContext());


        edtName = (EditText) view.findViewById(R.id.edtName);
        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);
        edtConfirmPassword = (EditText) view.findViewById(R.id.edtConfirmPassword);
        radioGroup = (RadioGroup) view.findViewById(R.id.radGender);
        btnRegister = (Button) view.findViewById(R.id.btnRegister);
        gender = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
        progressDialog = new ProgressDialog(getContext());

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_fragment, new HomeFragment(), "home");
                fragmentTransaction.addToBackStack("home");
                fragmentTransaction.commit();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()){
                    String name = edtName.getText().toString();
                    String email = edtEmail.getText().toString();
                    String password = edtPassword.getText().toString();
                    String confirmPassword = edtConfirmPassword.getText().toString();
                    int sex = gender.getText().equals(R.string.man) ? 0 : 1;
                    if(name.isEmpty() || email.isEmpty() || password.isEmpty()){
                        Toast.makeText(getContext(), "Preencha os campos", Toast.LENGTH_SHORT).show();
                    }else {
                        params = "name="+name;
                        params += "&email="+email;
                        params += "&password="+password;
                        params += "&confirm_password="+confirmPassword;
                        params += "&gender="+sex;
                        new create().execute(Routes.createUser);
                    }
                }else{
                    Toast.makeText(getContext(), "Não há conexão com a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Implementation of AsyncTask designed to fetch data from the network.
     */
    private class create extends AsyncTask<String, Void, JSONObject> {

        /**
         * Defines work to perform on the background thread.
         */
        @Override
        protected JSONObject doInBackground(String... urls) {
            return Connection.post(urls[0], params);
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            try {
                if (result.has("success")) {
                    String tokenType = result.getJSONObject("success").get("token").toString();
                    Log.i("route", Routes.takeUser);
                    new getUser("Bearer", tokenType, params).execute(Routes.takeUser);
                } else {
                    Toast.makeText(getContext(), "Ocorreu um erro ao cadastrar, tente novamente", Toast.LENGTH_LONG).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.executing));
            progressDialog.show();
        }


        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(JSONObject result) {
        }
    }

    /**
     * Implementation of AsyncTask designed to fetch data from the network.
     */
    private class getUser extends AsyncTask<String, Void, JSONObject> {

        String type, accessToken, params;

        private getUser(String type, String accessToken, String params){
            this.setType(type);
            this.setAccessToken(accessToken);
            this.setParams(params);
        }

        /**
         * Defines work to perform on the background thread.
         */
        @Override
        protected JSONObject doInBackground(String... urls) {
            return Connection.get(this.getType(), this.getAccessToken(), urls[0], null);
        }

        /**
         * Updates the DownloadCallback with the result.
         */
        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);

            try {
                if (result.has("success")) {
                    //Pegando os dados de retorno
                    JSONObject data = result.getJSONObject("success");

                    //Setando o email do usuário no cabeçalho do menu lateral
                    emailUser = (TextView) getActivity().findViewById(R.id.email);
                    emailUser.setText(data.getString("email"));

                    //Setando o nome do usuário no cabeçalho do menu lateral
                    nameUser = (TextView) getActivity().findViewById(R.id.name);
                    nameUser.setText(data.getString("email"));

                    progressDialog.dismiss();

                    //Adicionando os dados do usuário
                    //SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                    //sharedPreferences.edit().putString("user", data.toString()).commit();

                    managerSharedPreferences.set("user", data.toString());

                    //Iniciando a transição para a tela home
                    HomeFragment homeFragment = new HomeFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_fragment, homeFragment, "home");
                    fragmentTransaction.addToBackStack(getString(R.string.addToBackStack));
                    fragmentTransaction.commit();
                    Toast.makeText(getContext(), "Cadastrado com Sucesso", Toast.LENGTH_LONG).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
                progressDialog.dismiss();
            }
        }


        /**
         * Override to add special behavior for cancelled AsyncTask.
         */
        @Override
        protected void onCancelled(JSONObject result) {
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }


    }

}
