package com.wakeepay.taisys.wakeepay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class CreatePasswordVC extends AppCompatActivity {
    public EditText password1,password2;
    public Button Submit;
    ApiClient apiClient = new ApiClient();
    String apiClientResult = "";
    JSONObject resultJsonObj, resultObjJsonObj;
    String status;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password_vc);
        password1 = (EditText) findViewById(R.id.newpassword1);
        password2 = (EditText) findViewById(R.id.newpassword2);
        Submit = (Button) findViewById(R.id.btnSubmit);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (password1.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), "Please enter the new password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else if (password2.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(),"Please confirm the new password", Toast.LENGTH_SHORT).show();
                    }
                    else if (!password1.getText().toString().equals(password2.getText().toString())){
                        Toast.makeText(getApplicationContext(),"Entered password didn't matched", Toast.LENGTH_SHORT).show();

                    }

                    else {
                        apiClientResult = apiClient.updatePassword(getIntent().getStringExtra("MobileNo"),
                                password1.getText().toString().trim());
                        if(!apiClientResult.equals("Internal Server Error")) {
                            resultJsonObj = new JSONObject(apiClientResult);
                            status = resultJsonObj.getString("status");
                            message = resultJsonObj.getString("message");
                            if(status.equals("100")){
                                Toast.makeText(getApplicationContext(), "Password updated successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(CreatePasswordVC.this, LoginVC.class);
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
}
