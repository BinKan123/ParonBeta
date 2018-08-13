package com.peppypals.paronbeta.ChatSection;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.peppypals.paronbeta.R;

public class BillingFragment extends Fragment implements BillingProcessor.IBillingHandler{

    private BillingProcessor bp;
    private Button payBtn;


    public BillingFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_billing, container, false);

        //if have a google license from google console
        bp = new BillingProcessor(getActivity(), "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAy9/oNpP4rM6D0G1q3SVHl9rwKyZCI8IatoByrqDM6/hyFpSlM7kMNr0YErQ6IpWm8Wpau6uFlU9jcagVi+TsfFzjw6xIk5qvkXaTXoyXpjj4CmdnjvQLUC/JKjWxMsiPzH8tiEZg9L1h5M3NaM8gjPTLTF6IVVOZ+Nk6SBrXa8n10akrUl9QooS+st57fBSx1w8vgIwYddRxUSyCyVMdRotk0InrXm/Yt7iozTOPwwwmtCcHgioD1rp3NNov+Ju85Nleio0XqBJc+bP7sRN8MtXJ7rk0oiUWVP4gobGd0t22NYEzhiAOPpJ5h1tQAGmxf5sk356Pp3wvkhmX6fmYIwIDAQAB", this);

        //no license, just for test
        bp = new BillingProcessor(getActivity(), null,this);

        payBtn = (Button) view.findViewById(R.id.payBtn);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bp.purchase(getActivity(),"45_pedagog");
            }
        });

        final FragmentManager fragmentManager = getFragmentManager();
         Button smsBtn = (Button) view.findViewById(R.id.SMSBtn);
        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ChatSMSFragment();
                fragmentManager.beginTransaction().replace(android.R.id.content, fragment).commit();

            }
        });

        return view;
    }


    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Toast.makeText(getActivity(), "Payment successful",
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Toast.makeText(getActivity(), "something went wrong",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(!bp.handleActivityResult(requestCode,resultCode,data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }
}
