package com.wakeepay.taisys.wakeepay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpVC extends Activity{
    public EditText first_name,email_id,mobile_no,pass;
    public Button merchant,others,submit;
    public TextView popupBtn;
    public CheckBox CheckBtn;
    String type = "";
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
    Boolean isOnePressed = false;
    Boolean  isSecondPlace = false;
    String Checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);


        first_name = (EditText) findViewById(R.id.first_name);
        email_id = (EditText) findViewById(R.id.email_id);
        mobile_no = (EditText) findViewById(R.id.mobile_no);
        pass = (EditText) findViewById(R.id.password);
        merchant = (Button) findViewById(R.id.merchant);
        others = (Button) findViewById(R.id.others);
        submit = (Button) findViewById(R.id.submit);
        CheckBtn = (CheckBox) findViewById(R.id.checkbox);
        popupBtn = (TextView) findViewById(R.id.termcondition);

        Checked = "false";

        CheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()){
                    Checked = "true";
                }
                else {
                    Checked = "false";
                }
            }
        });


        merchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                isOnePressed = true;
                merchant.setBackgroundResource(R.drawable.merchant_button);
                others.setBackgroundResource(R.drawable.others_button);
                type = "Merchant";
                if (isSecondPlace) {
                    others.setBackgroundResource(R.drawable.others_button);

                    isSecondPlace = false;
                }

            }
        });

        others.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                others.setBackgroundResource(R.drawable.merchant_button);
                merchant.setBackgroundResource(R.drawable.others_button);
                type = "Others";
                isSecondPlace = true;
                if (isOnePressed) {
                    merchant.setBackgroundResource(R.drawable.others_button);
                    isOnePressed = false;
                }
            }
        });

        popupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.activity_term_and_condition_vc, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, 900, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });
       submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               sendOtp();
           }
       });
       }


    public void sendOtp(){
        {
            try {
                if (first_name.getText().toString().matches("")) {
                    Toast.makeText(this, "Please enter the name", Toast.LENGTH_SHORT).show();
                    return;
                }
               else if (!email_id.getText().toString().trim().matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(),"Please provide a valid email address", Toast.LENGTH_SHORT).show();
                }
                else if (!mobile_no.getText().toString().trim().matches(mobileNoPattern)) {
                    Toast.makeText(getApplicationContext(),"Please provide a valid mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }

                else if (pass.getText().toString().matches("")) {
                    Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (type.toString() == "") {
                    Toast.makeText(this, "Please Select Merchant or Others", Toast.LENGTH_SHORT).show();
                    return;
                }

                else if (Checked == "false"){
                    Toast.makeText(this, "Please Accept Terms & Conditions", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    ProgressDialog progress = new ProgressDialog(this);
                    progress.setTitle("Loading");
                    progress.setMessage("Wait while loading...");
                    progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                    progress.show();

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
//                            editor = sharedpreferences.edit();
//                            editor.putString("mobile_number", mobile_no.getText().toString());
//                            editor.commit();

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignUpVC.this,VerifyOtpVC.class);
                            intent.putExtra("MobileNo", mobile_no.getText().toString());
                            intent.putExtra("firstName", first_name.getText().toString());
                            intent.putExtra("password", pass.getText().toString());
                            intent.putExtra("type", type.toString());
                            intent.putExtra("email", email_id.getText().toString());
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    //Override the onBackPressed method
    @Override
    public void onBackPressed()
    {
        this.startActivity(new Intent(SignUpVC.this, LoginVC.class));
        finish();
    }
}
