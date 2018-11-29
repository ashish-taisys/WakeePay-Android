package com.wakeepay.taisys.wakeepay;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class TransactionListVC extends AppCompatActivity {

    ImageButton searchBtn;
    ListView list;
    String[] receivedFrom;
    String[] transactionId;
    String[] comment;
    String[] amount;
    String[] date;
    String[] paymentstatus;
    private  ArrayAdapter adapter;


    ApiClient apiClient = new ApiClient();
    String apiClientResult = "";
    JSONObject resultJsonObj, resultObjJsonObj, transactionJsonObj;
    JSONArray jsonArrayIhvTransactionsList;
    String status;
    String message;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "wakeepref";
    final ArrayList<String> items = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list_vc);
        sharedpreferences = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        searchBtn = (ImageButton) findViewById(R.id.btnSearch);
        list = (ListView)findViewById(R.id.list1);

        list.setTextFilterEnabled(true);

        try {

            if (sharedpreferences.getString("type",null).equals("Merchant")){
                apiClientResult = apiClient.getMerchantTransactions(sharedpreferences.getString("MerchID",null));
            }
            else {
                apiClientResult = apiClient.getTransactions(sharedpreferences.getString("MobileNo",null));
            }
            if(!apiClientResult.equals("Internal Server Error")) {
                Log.i("apiClientResult", apiClientResult);
                resultJsonObj = new JSONObject(apiClientResult);
                status = resultJsonObj.getString("status");
                message = resultJsonObj.getString("message");
                if (resultJsonObj.get("resultObj").equals(null)) {
                    // Transactions not found
                    searchBtn.setVisibility(View.INVISIBLE);

                    Toast.makeText(getApplicationContext(), "Transactions not found!", Toast.LENGTH_LONG).show();
                } else {

                    if (sharedpreferences.getString("type",null).equals("Merchant")){
                        searchBtn.setVisibility(View.VISIBLE);
                    }
                    else {
                        searchBtn.setVisibility(View.INVISIBLE);
                    }

                    resultObjJsonObj = (JSONObject) resultJsonObj.get("resultObj");
                    jsonArrayIhvTransactionsList = (JSONArray) resultObjJsonObj.get("ihvTransactionsList");

                    receivedFrom = new String[jsonArrayIhvTransactionsList.length()];
                    transactionId = new String[jsonArrayIhvTransactionsList.length()];
                    comment = new String[jsonArrayIhvTransactionsList.length()];
                    amount = new String[jsonArrayIhvTransactionsList.length()];
                    date = new String[jsonArrayIhvTransactionsList.length()];
                    paymentstatus = new String[jsonArrayIhvTransactionsList.length()];

                    for(int i=0;i<jsonArrayIhvTransactionsList.length();i++){
                        try {
                            transactionJsonObj = (JSONObject) jsonArrayIhvTransactionsList.get(i);

                            if (sharedpreferences.getString("type",null).equals("Merchant")){
                                receivedFrom[i] = transactionJsonObj.getString("mobileNo");
                            }
                            else {
                                receivedFrom[i] = transactionJsonObj.getString("merchantId");
                            }
                            transactionId[i] =  transactionJsonObj.getString("transID");
                            comment[i] = transactionJsonObj.getString("remark");
                            amount[i] = "₹" + transactionJsonObj.getString("totalAmount");
                            date[i] = getDate(transactionJsonObj.getString("initiatedOn"));
                            paymentstatus[i] = "Status:-" + transactionJsonObj.getString("transactionStatus");

                            list = findViewById(R.id.list1);
                            adapter = new MyAdapter(this, receivedFrom,transactionId,comment,amount,date,paymentstatus);
                            list.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error: Please try again!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionListVC.this,SearchTransactionVC.class);
                startActivity(intent);
                finish();
            }
        });


//        searchBtn.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                (TransactionListVC.this).adapter.getFilter().filter(s);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//
//            public void afterTextChanged(Editable s) {
//
//            }
//        });



    }

    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        String transReceived[];
        String transId[];
        String transComment[];
        String transAmount[];
        String transDate[];
        String transStatus[];

        MyAdapter(Context c,String[] receivedFrom,String[] transactionId, String[] comment, String[] amount, String[] date, String[] paymentstatus){
            super(c,R.layout.customtransactionlist,R.id.receivedFrom,receivedFrom);
            this.context = c;
            this.transReceived = receivedFrom;
            this.transId = transactionId;
            this.transComment = comment;
            this.transAmount = amount;
            this.transDate = date;
            this.transStatus = paymentstatus;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.customtransactionlist,parent,false);
            TextView transactionId1 = (TextView) row.findViewById(R.id.transactionId);
            TextView received = (TextView) row.findViewById(R.id.receivedFrom);
            TextView comment1 = (TextView) row.findViewById(R.id.comment);
            TextView setamount = (TextView) row.findViewById(R.id.amount);
            TextView date1 = (TextView) row.findViewById(R.id.date);
            TextView paymentstatus1 = (TextView) row.findViewById(R.id.paymentstatus);
            TextView maintitle = (TextView) row.findViewById(R.id.texta);

            if (sharedpreferences.getString("type",null).equals("Merchant")){
                maintitle.setText("Received From:-");
            }

            received.setText(receivedFrom[position]);
            transactionId1.setText(transactionId[position]);
            comment1.setText(comment[position]);
            setamount.setText(amount[position]);
            date1.setText(date[position]);
            paymentstatus1.setText(paymentstatus[position]);


            if (paymentstatus[position].equals("Status:-FAILED") || paymentstatus[position].equals("Status:-ERROR")){
                paymentstatus1.setTextColor(Color.RED);
            }

            return row;
        }
    }


    @Override
    public void onBackPressed() {

        if (sharedpreferences.getString("type", null).equals("Merchant")) {
            this.startActivity(new Intent(TransactionListVC.this, MerchantDashVC.class));
            finish();
        }
        else{
            this.startActivity(new Intent(TransactionListVC.this, DashboardVC.class));
            finish();

        }
    }


    private String getDate(String milliSecs)
    {
        // Create a DateFormatter object for displaying date in specified format.
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa");
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        long milliSeconds = Long.parseLong(milliSecs);
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


}
