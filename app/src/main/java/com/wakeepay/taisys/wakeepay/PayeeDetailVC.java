package com.wakeepay.taisys.wakeepay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class PayeeDetailVC extends Activity {

    Button btnProceed;
    EditText amount,remark;
    String amountPattern = "^[0-9]+(\\.[0-9]{1,2})?$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_payee_detail_vc);
        btnProceed = (Button) findViewById(R.id.btn_proceed);
        amount = (EditText) findViewById(R.id.amount);
        remark = (EditText) findViewById(R.id.comment);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Submit();
            }
        });
    }

    public void Submit() {
        if (!amount.getText().toString().trim().matches(amountPattern)) {
            Toast.makeText(getApplicationContext(), "Please enter the amount", Toast.LENGTH_SHORT).show();
        } else if (remark.getText().toString().trim().matches("")) {
            Toast.makeText(getApplicationContext(), "Please enter the name", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
                    Intent Start = new Intent(PayeeDetailVC.this, AmountDetailVC.class);
                    Start.putExtra("amount", amount.getText().toString());
                    Start.putExtra("remark",remark.getText().toString());
                    startActivity(Start);
            }
      }

    //Override the onBackPressed method
    @Override
    public void onBackPressed()
    {
        this.startActivity(new Intent(PayeeDetailVC.this, SelectPaymentVC.class));
        finish();
    }
    }
