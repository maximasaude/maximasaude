package com.saude.maxima;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment {

    public static final String SELLER_EMAIL = "miranda.fitness.avaliacao@gmail.com";
    public static final String SELLER_TOKEN = "619A9082A5374BD1917745ABC9D471FF";

    public PaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);


        // Inflate the layout for this fragment
        return view;
    }

}
