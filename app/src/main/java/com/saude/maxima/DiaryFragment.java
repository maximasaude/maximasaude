package com.saude.maxima;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saude.maxima.utils.ManagerSharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

public class DiaryFragment extends Fragment {

    private ManagerSharedPreferences managerSharedPreferences;
    private Context context;
    TextView txtPackage;
    TextView txtValue;
    TextView txtDate;
    Button btnEdit;
    Button btnRemove;
    TextView txtNoHasPackage;
    LinearLayout llNoHasPackage;
    LinearLayout llContent;

    public DiaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        this.context = getContext();

        txtPackage = (TextView) view.findViewById(R.id.txtPackage);
        txtValue = (TextView) view.findViewById(R.id.txtValue);
        txtDate = (TextView) view.findViewById(R.id.txtDate);

        managerSharedPreferences = new ManagerSharedPreferences(this.context);

        btnRemove = (Button) view.findViewById(R.id.btnRemove);
        btnEdit = (Button) view.findViewById(R.id.btnEdit);
        this.onClickBtnRemove();
        this.onClickBtnEdit();
        if(managerSharedPreferences.has("order")){
            try {
                JSONObject objOrder = new JSONObject(managerSharedPreferences.get("order").toString());
                JSONObject objPackage = new JSONObject(objOrder.get("package").toString());
                String dateFormated = objOrder.getString("day")+ "/" +
                        objOrder.getString("month") + "/"+ objOrder.getString("year")+" "+
                        objOrder.getString("hour")+":"+objOrder.getString("minute");
                txtPackage.setText(objPackage.getString("name"));
                txtDate.setText(dateFormated);
                txtValue.setText(objPackage.getString("value"));
            }catch (JSONException e){
                e.printStackTrace();
            }

            this.onClickBtnRemove();
            this.onClickBtnEdit();
        }else{
            /*llNoHasPackage = (LinearLayout) view.findViewById(R.id.llNoHasPackage);
            llNoHasPackage.setVisibility(View.VISIBLE);
            llContent = (LinearLayout) view.findViewById(R.id.llContent);
            llContent.setVisibility(View.GONE);
            txtNoHasPackage = (TextView) view.findViewById(R.id.txtNoHasPackage);
            txtNoHasPackage.setText("Tstando");*/
        }


        // Inflate the layout for this fragment
        return view;
    }

    private void onClickBtnRemove(){
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managerSharedPreferences.remove("order");
                getActivity().recreate();
            }
        });
    }

    private void onClickBtnEdit(){
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "teste", Toast.LENGTH_LONG).show();
            }
        });
    }

}
