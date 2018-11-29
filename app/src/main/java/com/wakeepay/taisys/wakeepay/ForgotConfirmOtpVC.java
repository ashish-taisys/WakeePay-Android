package com.wakeepay.taisys.wakeepay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotConfirmOtpVC extends AppCompatActivity {
    public EditText enterOtp;
    public Button Submit,resendOtp;

    ApiClient apiClient = new ApiClient();
    String apiClientResult = "";
    JSONObject resultJsonObj, resultObjJsonObj;
    String status;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forgot_confirm_otp_vc);
        enterOtp = (EditText) findViewById(R.id.enterOtp);
        Submit = (Button) findViewById(R.id.submit_otp1);
        resendOtp = (Button) findViewById(R.id.resend_otp1);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    apiClientResult = apiClient.verifyOtp(getIntent().getStringExtra("MobileNo"),
                            enterOtp.getText().toString());
                    if(apiClientResult.equals("Internal Server Error")) {
                        Toast.makeText(getApplicationContext(), "Error: Please try again!", Toast.LENGTH_LONG).show();
                    } else {
                        resultJsonObj = new JSONObject(apiClientResult);
                        status = resultJsonObj.getString("status");
                        message = resultJsonObj.getString("message");
                        if (resultJsonObj.get("resultObj").equals(null)) {

                            Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_LONG).show();

                        }
                        else {
                            resultObjJsonObj = (JSONObject) resultJsonObj.get("resultObj");
                            if(resultObjJsonObj.getString("isOtpVerified").equals("true")){
                                Log.i("VerifyOtp", message);
                                Toast.makeText(getApplicationContext(), "OTP MATCHED!", Toast.LENGTH_LONG).show();
                                Intent Start = new Intent(ForgotConfirmOtpVC.this,CreatePasswordVC.class);
                                Start.putExtra("MobileNo", getIntent().getStringExtra("MobileNo"));
                                startActivity(Start);
                                finish();

                            } else {
                                // OTP MISMATCH
                                Toast.makeText(getApplicationContext(), "OTP MISMATCH!", Toast.LENGTH_LONG).show();
                            /*Intent intent = new Intent(VerifyOtp.this, MainActivity.class);
                            startActivity(intent);*/
                            }
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error: Please try again!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });

        resendOtp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                try {
                    apiClientResult = apiClient.sendOtp(getIntent().getStringExtra("MobileNo"));
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
                            /*editor = sharedpreferences.edit();
                            editor.putString("mobile_number", getIntent().getStringExtra("MobileNo"));
                            editor.commit();*/
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error: Please try again!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    //Override the onBackPressed method
    @Override
    public void onBackPressed()
    {
        this.startActivity(new Intent(ForgotConfirmOtpVC.this, LoginVC.class));
        finish();
    }
}
