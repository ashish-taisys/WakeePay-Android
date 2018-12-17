package com.wakeepay.taisys.wakeepay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.sip.SipSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginVC extends Activity {
    public  EditText email,mobile,password;
    public Button Submit;
    public TextView forgotpassword;
    public Button SignUp;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String mobileNoPattern = "^[0-9]{10}$";
    ApiClient apiClient = new ApiClient();
    String apiClientResult = "";
    JSONObject resultJsonObj, resultObjJsonObj;
    String status;
    String message;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String mypreference = "wakeepref";


    public void init(){
        SignUp = (Button)findViewById(R.id.btnSignUp);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Start = new Intent(LoginVC.this,SignUpVC.class);
                startActivity(Start);
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_vc);

        sharedpreferences = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        Log.i("LoginFailed",sharedpreferences.getString("firstName",""));


        if (sharedpreferences.getString("firstName","").isEmpty())
        {

        }
        else
        {
            if (sharedpreferences.getString("type","").equals("Merchant"))
            {
            Intent Start = new Intent(LoginVC.this,MerchantDashVC.class);
            startActivity(Start);
            finish();
            }
            else {
            Intent Start = new Intent(LoginVC.this,DashboardVC.class);
            startActivity(Start);
            finish();
        }
        }
        init();
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.mobile);
        password = (EditText) findViewById(R.id.password);
        Submit = (Button) findViewById(R.id.btnlogin);
        forgotpassword = (TextView) findViewById(R.id.btnForgotPswrd);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Submit();
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginVC.this,ForgotPasswordVC.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void Submit()
    {
        try {
//        if (!email.getText().toString().trim().matches(emailPattern)) {
//            Toast.makeText(getApplicationContext(), "Please provide a valid email address", Toast.LENGTH_SHORT).show();
//        } else if (!mobile.getText().toString().trim().matches(mobileNoPattern)) {
//            Toast.makeText(getApplicationContext(), "Please enter the 10 digits mobile number", Toast.LENGTH_SHORT).show();
//            return;
//        } else

            if (password.getText().toString().matches("")) {
            Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
//            ProgressDialog progress = new ProgressDialog(this);
//            progress.setTitle("Loading");
//            progress.setMessage("Wait while loading...");
//            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
//            progress.show();
           apiClientResult = apiClient.loginCustomer(email.getText().toString(),mobile.getText().toString(),password.getText().toString());
            if(apiClientResult.equals("Internal Server Error")) {
                Toast.makeText(getApplicationContext(), "Error : Please try again!", Toast.LENGTH_LONG).show();
            } else {
                resultJsonObj = new JSONObject(apiClientResult);
                String status = resultJsonObj.getString("status");
                String message = resultJsonObj.getString("message");
                if (resultJsonObj.get("resultObj").equals(null)) {
                    Toast.makeText(getApplicationContext(), " Please enter the valid credentials!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Customer loggedin successfully", Toast.LENGTH_LONG).show();
                    resultObjJsonObj = (JSONObject) resultJsonObj.get("resultObj");
                    editor = sharedpreferences.edit();
                    editor.putString("firstName",resultObjJsonObj.getString("firstName"));
                    editor.putString("email",resultObjJsonObj.getString("email"));
                    editor.putString("MobileNo",resultObjJsonObj.getString("mobileNo"));
                    editor.putString("type",resultObjJsonObj.getString("type"));
                    editor.putString("MerchID",resultObjJsonObj.getString("MerchID"));
                    editor.putString("status",resultObjJsonObj.getString("status"));
                    editor.commit();

                    if (resultObjJsonObj.getString("type").equals("Merchant")){
                        Intent intent = new Intent(LoginVC.this,MerchantDashVC.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(LoginVC.this,DashboardVC.class);
                        startActivity(intent);
                        finish();
                    }
                    finish();
                }
            }
        }
    } catch (JSONException e) {
        e.printStackTrace();
    }
    }

}
