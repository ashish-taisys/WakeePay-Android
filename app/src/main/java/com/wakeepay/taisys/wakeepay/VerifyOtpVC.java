package com.wakeepay.taisys.wakeepay;

import android.app.Activity;
import android.app.ProgressDialog;
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

public class VerifyOtpVC extends Activity {
   public EditText enterOtp;
   public Button Submit,resendOtp;

    ApiClient apiClient = new ApiClient();
    String apiClientResult = "";
    JSONObject resultJsonObj, resultObjJsonObj;
    String status;
    String message;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String mypreference = "wakeepref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_verify_otp_vc);
        enterOtp = (EditText) findViewById(R.id.enterOtp);
        Submit = (Button) findViewById(R.id.submit_otp);
        resendOtp = (Button) findViewById(R.id.resend_otp);
        sharedpreferences = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);

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
                            // Should either be true or false
                            Toast.makeText(getApplicationContext(), "Please try again!", Toast.LENGTH_LONG).show();
                        }
                        else {
                            resultObjJsonObj = (JSONObject) resultJsonObj.get("resultObj");
                            if(resultObjJsonObj.getString("isOtpVerified").equals("true")){
                                Log.i("VerifyOtp", message);
                                Toast.makeText(getApplicationContext(), "OTP MATCHED!", Toast.LENGTH_LONG).show();

                                    UpdateDetails();
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


    public void UpdateDetails(){

        try {
//                ProgressDialog progress = new ProgressDialog(this);
//                progress.setTitle("Loading");
//                progress.setMessage("Wait while loading...");
//                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
//                progress.show();

                apiClientResult = apiClient.updateCustomer(getIntent().getStringExtra("firstName"),getIntent().getStringExtra("email"),getIntent().getStringExtra("MobileNo"),getIntent().getStringExtra("password"), getIntent().getStringExtra("type"));
                if(!apiClientResult.equals("Internal Server Error")) {
                    resultJsonObj = new JSONObject(apiClientResult);
                    status = resultJsonObj.getString("status");
                    message = resultJsonObj.getString("message");
                    if(status.equals("100")){
//                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Customer Registered successfully", Toast.LENGTH_LONG).show();
                        Intent Start = new Intent(VerifyOtpVC.this,LoginVC.class);
                        startActivity(Start);
                        finish();
                    } else {
//                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        Intent Start = new Intent(VerifyOtpVC.this,SignUpVC.class);
                        startActivity(Start);
                        finish();
                    }
                } else {
//                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error: Please try again!", Toast.LENGTH_LONG).show();
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Override the onBackPressed method
    @Override
    public void onBackPressed()
    {
        this.startActivity(new Intent(VerifyOtpVC.this, LoginVC.class));
        finish();
    }
}
