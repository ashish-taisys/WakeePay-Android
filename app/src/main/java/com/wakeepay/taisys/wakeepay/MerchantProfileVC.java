package com.wakeepay.taisys.wakeepay;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class MerchantProfileVC extends AppCompatActivity {
    public EditText name,email,mobileNo,dob,accountNo,accountType,ifsc,institutionName,gstNo;
    public Button update;
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
    String accountNoPattern = "[0-9]{9,18}";
    String ifscPattern = "^[A-Za-z]{4}0[A-Z0-9a-z]{6}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_profile_vc);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        mobileNo = (EditText) findViewById(R.id.mobile);
        dob = (EditText) findViewById(R.id.dob);
        accountType = (EditText) findViewById(R.id.accounttype);
        accountNo = (EditText) findViewById(R.id.accountno);
        ifsc = (EditText) findViewById(R.id.ifsc);
        institutionName = (EditText) findViewById(R.id.institution);
        gstNo = (EditText) findViewById(R.id.gstNo);
        update = (Button) findViewById(R.id.update);

        sharedpreferences = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
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
                    Log.i("accountNo", resultObjJsonObj.getString("accountNo"));
                    Log.i("accountType", resultObjJsonObj.getString("accountType"));
                    Log.i("ifsc", resultObjJsonObj.getString("ifsc"));
                    Log.i("institutionName", resultObjJsonObj.getString("institutionName"));

                    name.setText(resultObjJsonObj.getString("firstName"));
                    email.setText(resultObjJsonObj.getString("email"));
                    dob.setText(resultObjJsonObj.getString("dob"));
                    mobileNo.setText(resultObjJsonObj.getString("mobileNo"));
                    accountType.setText(resultObjJsonObj.getString("accountType"));
                    accountNo.setText(resultObjJsonObj.getString("accountNo"));
                    ifsc.setText(resultObjJsonObj.getString("ifsc"));
                    institutionName.setText(resultObjJsonObj.getString("institutionName"));
                    gstNo.setText(resultObjJsonObj.getString("gstNo"));
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
                DatePickerDialog datePicker = new DatePickerDialog(MerchantProfileVC.this, new DatePickerDialog.OnDateSetListener() {
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


        accountType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(MerchantProfileVC.this);
                b.setTitle("Select Account Type");
                String[] types = {"Saving", "Current"};
                b.setItems(types, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch(which){
                            case 0:
                                accountType.setText("Saving");
                                break;
                            case 1:
                                accountType.setText("Current");
                                break;
                        }
                    }

                });
                b.show();
            }
        });


        update.setOnClickListener(new View.OnClickListener(){
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
                    else if (!accountNo.getText().toString().trim().matches(accountNoPattern)) {
                        Toast.makeText(getApplicationContext(),"Please enter valid Account No.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if (!ifsc.getText().toString().trim().matches(ifscPattern)) {
                        Toast.makeText(getApplicationContext(),"Please enter bank IFSC code", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if (accountType.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(),"Please enter Account Type", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if (institutionName.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), "Please enter the Institution Name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        apiClientResult = apiClient.updateCustomer(sharedpreferences.getString("MobileNo", null),
                                name.getText().toString().trim(), email.getText().toString().trim(),
                                dob.getText().toString().trim(),accountNo.getText().toString().trim(),accountType.getText().toString().trim(),ifsc.getText().toString().trim(),institutionName.getText().toString().trim(),gstNo.getText().toString().trim());
                        if(!apiClientResult.equals("Internal Server Error")) {
                            resultJsonObj = new JSONObject(apiClientResult);
                            status = resultJsonObj.getString("status");
                            message = resultJsonObj.getString("message");
                            if(status.equals("100")){
                                Toast.makeText(getApplicationContext(), "Details updated successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MerchantProfileVC.this, MerchantDashVC.class);
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
        this.startActivity(new Intent(MerchantProfileVC.this, MerchantDashVC.class));
        finish();
    }
}
