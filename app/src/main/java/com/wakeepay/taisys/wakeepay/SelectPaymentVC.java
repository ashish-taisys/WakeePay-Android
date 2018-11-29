package com.wakeepay.taisys.wakeepay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectPaymentVC extends Activity {
 Button ScanQR,Pay;

    EditText dropdownmenu;
    String itemvalue;

    ApiClient apiClient = new ApiClient();
    String apiClientResult = "";
    JSONObject resultJsonObj, resultObjJsonObj, transactionJsonObj;
    JSONArray jsonArrayIhvTransactionsList;
    String status;
    String message;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "wakeepref";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_payment_vc);

        sharedpreferences = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        ScanQR = (Button) findViewById(R.id.scan_qr);
        dropdownmenu = (EditText) findViewById(R.id.merchantId);
        Pay = (Button) findViewById(R.id.pay);

        ScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Start = new Intent(SelectPaymentVC.this,BarCodeVC.class);
                startActivity(Start);            }
        });

        Pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dropdownmenu.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Please enter merchant Id", Toast.LENGTH_SHORT).show();
                    return;
                }
               else {
                    try {
                        apiClientResult = apiClient.verifyMerchant(dropdownmenu.getText().toString().trim());
                        if(!apiClientResult.equals("Internal Server Error")) {
                            resultJsonObj = new JSONObject(apiClientResult);
                            message = resultJsonObj.getString("message");
                            if (message.equals("Success: Merchant is Active")) {
                                // Customer not found - NOT POSSIBLE
                                resultObjJsonObj = (JSONObject) resultJsonObj.get("resultObj");
                                Intent Start = new Intent(SelectPaymentVC.this, PayeeDetailVC.class);
                                startActivity(Start);
                            } else {
                                Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: Please try again!", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }


    //Override the onBackPressed method
    @Override
    public void onBackPressed()
    {
        if (sharedpreferences.getString("type", null).equals("Merchant")) {
            this.startActivity(new Intent(SelectPaymentVC.this, MerchantDashVC.class));
            finish();
        }
        else{
            this.startActivity(new Intent(SelectPaymentVC.this, DashboardVC.class));
            finish();

        }
    }
}
