package com.wakeepay.taisys.wakeepay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MerchantDashVC extends Activity {
    public Button BtnPayment,BtnTransaction, BtnProfile, BtnQR;
    public ImageButton BtnLogout;
    public TextView MerchantStatus;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String mypreference = "wakeepref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_merchant_dash_vc);
        sharedpreferences = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        BtnTransaction = (Button) findViewById(R.id.transaction);
        BtnProfile = (Button) findViewById(R.id.profile);
        BtnQR = (Button) findViewById(R.id.qr);
        MerchantStatus = (TextView) findViewById(R.id.Merchantstatus);
        BtnLogout = (ImageButton) findViewById(R.id.logout);
        BtnPayment = (Button) findViewById(R.id.payment);

        MerchantStatus.setText(sharedpreferences.getString("status", ""));


        BtnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Start = new Intent(MerchantDashVC.this,SelectPaymentVC.class);
                startActivity(Start);
                finish();
            }
        });

        BtnTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Start = new Intent(MerchantDashVC.this, TransactionListVC.class);
                startActivity(Start);
                finish();
            }
        });

        BtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Start = new Intent(MerchantDashVC.this, MerchantProfileVC.class);
                startActivity(Start);
                finish();
            }
        });

        BtnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Start = new Intent(MerchantDashVC.this, MerchantQrVC.class);
                startActivity(Start);
                finish();
            }
        });

        BtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor = sharedpreferences.edit();
                editor.clear();
                editor.commit();
                Intent Start = new Intent(MerchantDashVC.this, LoginVC.class);
                startActivity(Start);
                finish();

            }
        });
    }

}