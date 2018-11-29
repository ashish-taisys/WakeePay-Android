package com.wakeepay.taisys.wakeepay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PaymentError extends AppCompatActivity {
    TextView _errorCode, _errorMessage;
    Button _btnHome;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "wakeepref";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_error);

        sharedpreferences = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        try {
            _errorCode = (TextView)findViewById(R.id.textViewErrorCodeVal);
            _errorMessage = (TextView)findViewById(R.id.textViewErrorMsgVal);
            _btnHome = (Button) findViewById(R.id.btnHome);

            _errorCode.setText(getIntent().getStringExtra("errorCode"));
            _errorMessage.setText(getIntent().getStringExtra("errorMessage"));

            _btnHome.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    Intent intent = new Intent(PaymentError.this, DashboardVC.class);
                    startActivity(intent);
                    finish();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed()
    {
        if (sharedpreferences.getString("type", null).equals("Merchant")) {
            this.startActivity(new Intent(PaymentError.this, MerchantDashVC.class));
            finish();
        }
        else{
            this.startActivity(new Intent(PaymentError.this, DashboardVC.class));
            finish();

        }
    }
}
