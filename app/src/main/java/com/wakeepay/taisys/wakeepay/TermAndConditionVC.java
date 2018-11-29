package com.wakeepay.taisys.wakeepay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class TermAndConditionVC extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_condition_vc);

    }

    //Override the onBackPressed method
    @Override
    public void onBackPressed()
    {
        this.startActivity(new Intent(TermAndConditionVC.this, SignUpVC.class));
        finish();
    }
}
