package com.wakeepay.taisys.wakeepay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.telephony.SmsManager;
import android.widget.ImageButton;
import android.widget.TextView;


public class DashboardVC extends Activity {
    public Button BtnTransaction,BtnProfile,BtnPayment;
    public ImageButton BtnLogout;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String mypreference = "wakeepref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dashboard_vc);
        sharedpreferences = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        BtnTransaction = (Button) findViewById(R.id.transaction);
        BtnProfile = (Button) findViewById(R.id.profile);
        BtnPayment = (Button) findViewById(R.id.payment);
        BtnLogout = (ImageButton) findViewById(R.id.logout);

        BtnTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Start = new Intent(DashboardVC.this,TransactionListVC.class);
                startActivity(Start);
                finish();
            }
        });

        BtnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Start = new Intent( DashboardVC.this,ProfileVC.class);
                startActivity(Start);
                finish();
            }
        });

        BtnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Start = new Intent(DashboardVC.this,SelectPaymentVC.class);
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
                Intent Start = new Intent(DashboardVC.this, LoginVC.class);
                startActivity(Start);
                finish();
            }
        });

    }

}
