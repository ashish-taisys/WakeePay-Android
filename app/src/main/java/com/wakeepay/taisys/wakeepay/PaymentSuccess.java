package com.wakeepay.taisys.wakeepay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PaymentSuccess extends AppCompatActivity {

    TextView _paymentRefId, _amountPaid, _transactionDate;
    Button _btnHome;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "wakeepref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        sharedpreferences = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        try {
            _paymentRefId = (TextView)findViewById(R.id.textViewTransIdVal);
            _amountPaid = (TextView)findViewById(R.id.textViewAmtVal);
            _transactionDate = (TextView)findViewById(R.id.textViewTransDateVal);
            _btnHome = (Button) findViewById(R.id.btnHome);

            _paymentRefId.setText(getIntent().getStringExtra("razorpayPaymentID"));
            _amountPaid.setText("₹ " + getIntent().getStringExtra("amountPaid"));
            _transactionDate.setText(getIntent().getStringExtra("transactionDate"));

            _btnHome.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(PaymentSuccess.this, DashboardVC.class);
                    startActivity(intent);
                    finish();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        if (sharedpreferences.getString("type", null).equals("Merchant")) {
            this.startActivity(new Intent(PaymentSuccess.this, MerchantDashVC.class));
            finish();
        }
        else{
            this.startActivity(new Intent(PaymentSuccess.this, DashboardVC.class));
            finish();

        }
    }
}
