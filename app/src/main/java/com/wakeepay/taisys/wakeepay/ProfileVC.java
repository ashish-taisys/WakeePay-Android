package com.wakeepay.taisys.wakeepay;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;

public class ProfileVC extends AppCompatActivity {
    public EditText name,email,mobileNo,dob,accountNo,accountType,ifsc;
    public Button Submit;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static final String mypreference = "wakeepref";
    private static final int PERMISSION_SEND_SMS = 1;
    Calendar myCalendar = Calendar.getInstance();
    ApiClient apiClient = new ApiClient();
    String apiClientResult = "";
    JSONObject resultJsonObj, resultObjJsonObj;
    String status;
    String message;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String mobileNoPattern = "^[0-9]{10}$";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_vc);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        mobileNo = (EditText) findViewById(R.id.mobile);
        dob = (EditText) findViewById(R.id.dob);
        Submit = (Button) findViewById(R.id.update);
        sharedpreferences = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        Log.i("Result Mobile No.",sharedpreferences.getString("MobileNo",null));

        try {
        apiClientResult = apiClient.getMerchant(sharedpreferences.getString("MobileNo", null));
        if(!apiClientResult.equals("Internal Server Error")) {
            resultJsonObj = new JSONObject(apiClientResult);
             message = resultJsonObj.getString("message");
            if (resultJsonObj.get("resultObj").equals(null)) {
                // Customer not found - NOT POSSIBLE
                Log.i("MyProfile", message);
            } else {
                resultObjJsonObj = (JSONObject) resultJsonObj.get("resultObj");
                Log.i("firstName", resultObjJsonObj.getString("firstName"));
                Log.i("email", resultObjJsonObj.getString("email"));
                Log.i("dob", resultObjJsonObj.getString("dob"));
                name.setText(resultObjJsonObj.getString("firstName"));
                email.setText(resultObjJsonObj.getString("email"));
                dob.setText(resultObjJsonObj.getString("dob"));
                mobileNo.setText(resultObjJsonObj.getString("mobileNo"));
            }
        } else {
            Toast.makeText(getApplicationContext(), "Error: Please try again!", Toast.LENGTH_LONG).show();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }


        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(ProfileVC.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1)
                                + "/" + String.valueOf(year);
                        dob.setText(date);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });

    Submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                try {
                    if (name.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), "Please enter the name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if (!email.getText().toString().trim().matches(emailPattern)) {
                        Toast.makeText(getApplicationContext(),"Please provide a valid email address", Toast.LENGTH_SHORT).show();
                    }
                    else if (!mobileNo.getText().toString().trim().matches(mobileNoPattern)) {
                        Toast.makeText(getApplicationContext(),"Please provide a valid mobile number", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if (dob.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(),"Please enter date of birth", Toast.LENGTH_SHORT).show();
                        return;

                    }
                    else {
                        apiClientResult = apiClient.updateUser(sharedpreferences.getString("MobileNo", null),
                                name.getText().toString().trim(), email.getText().toString().trim(),
                                dob.getText().toString().trim());
                        if(!apiClientResult.equals("Internal Server Error")) {
                            resultJsonObj = new JSONObject(apiClientResult);
                            status = resultJsonObj.getString("status");
                            message = resultJsonObj.getString("message");
                            if(status.equals("100")){
                                Toast.makeText(getApplicationContext(), "Details updated successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ProfileVC.this, DashboardVC.class);
                                intent.putExtra("MobileNo", sharedpreferences.getString("mobile_number", null));
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error occured while updating uder details. Please try again!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: Please try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void onBackPressed()
    {
        this.startActivity(new Intent(ProfileVC.this, DashboardVC.class));
        finish();
    }
}
