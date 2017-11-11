package com.saude.maxima;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.uol.pslibs.checkout_in_app.PSCheckout;
import br.com.uol.pslibs.checkout_in_app.wallet.util.PSCheckoutConfig;


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

        PSCheckoutConfig psCheckoutConfig = new PSCheckoutConfig();
        psCheckoutConfig.setSellerEmail(SELLER_EMAIL);
        psCheckoutConfig.setSellerToken(SELLER_TOKEN);
//Informe o fragment container
        psCheckoutConfig.setContainer(R.id.fragment_container);

//Inicializa apenas os recursos de pagamento transparente e boleto
        PSCheckout.initTransparent(getActivity(), psCheckoutConfig);

        // Inflate the layout for this fragment
        return view;
    }

}
