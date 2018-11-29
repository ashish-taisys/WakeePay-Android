package com.wakeepay.taisys.wakeepay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordVC extends AppCompatActivity {
    public EditText mobile_no;
    public Button Submit;
    ApiClient apiClient = new ApiClient();
    String apiClientResult = "";
    JSONObject resultJsonObj, resultObjJsonObj;
    String status;
    String message;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String mypreference = "wakeepref";
    String mobileNoPattern = "^[0-9]{10}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_vc);
        mobile_no = (EditText) findViewById(R.id.mobileNo1);
        Submit = (Button) findViewById(R.id.btnOtp);
        sharedpreferences = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!mobile_no.getText().toString().trim().matches(mobileNoPattern)) {
                        Toast.makeText(getApplicationContext(),"Please provide a valid mobile number", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    else {
                        apiClientResult = apiClient.sendOtp(mobile_no.getText().toString());
                        if(apiClientResult.equals("Internal Server Error")) {
                            Toast.makeText(getApplicationContext(), "Error: Please try again!", Toast.LENGTH_LONG).show();
                        } else {
                            resultJsonObj = new JSONObject(apiClientResult);
                            String status = resultJsonObj.getString("status");
                            String message = resultJsonObj.getString("message");
                            if (resultJsonObj.get("resultObj").equals(null)) {
                                Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_LONG).show();
                            } else {
                                resultObjJsonObj = (JSONObject) resultJsonObj.get("resultObj");
                                String isOtpSent = resultObjJsonObj.getString("isOtpSent");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ForgotPasswordVC.this,ForgotConfirmOtpVC.class);
                                intent.putExtra("MobileNo", mobile_no.getText().toString());
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
}
    //Override the onBackPressed method
    @Override
    public void onBackPressed()
    {
        this.startActivity(new Intent(ForgotPasswordVC.this, LoginVC.class));
        finish();
    }
}
