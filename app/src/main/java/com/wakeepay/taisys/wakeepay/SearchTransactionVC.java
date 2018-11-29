package com.wakeepay.taisys.wakeepay;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SearchTransactionVC extends AppCompatActivity {
    ListView list;
    Button ClearText;
    EditText TextSearch;
    Button backButton;
    TextView EmptyList;

    String[] receivedFrom;
    String[] transactionId;
    String[] comment;
    String[] amount;
    String[] date;
    String[] paymentstatus;
    private ArrayAdapter adapter;

    LinearLayout mLinearLayout;

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
        setContentView(R.layout.activity_search_transaction_vc);
        sharedpreferences = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);


        ClearText = (Button) findViewById(R.id.clearText);
        TextSearch = (EditText) findViewById(R.id.enterText);
        backButton = (Button) findViewById(R.id.backBtn);
        EmptyList = (TextView) findViewById(R.id.empty);

        EmptyList.setVisibility(View.INVISIBLE);

        list = (ListView)findViewById(R.id.searchList);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });


        ClearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextSearch.setText("");
            }
        });

        TextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                    return true;
                }
                return false;
            }
        });

        TextSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() == 0){
                    EmptyList.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void performSearch()
    {
        try  {

//            if (sharedpreferences.getString("type",null).equals("Merchant")){
//                apiClientResult = apiClient.getMerchantTransactions(sharedpreferences.getString("MerchID",null));
//            }
//            else {
            apiClientResult = apiClient.getTransactions(TextSearch.getText().toString().trim());
//            }
            if(!apiClientResult.equals("Internal Server Error")) {
                Log.i("apiClientResult", apiClientResult);
                resultJsonObj = new JSONObject(apiClientResult);
                status = resultJsonObj.getString("status");
                message = resultJsonObj.getString("message");
                if (resultJsonObj.get("resultObj").equals(null)) {
                    Toast.makeText(getApplicationContext(), "Transactions not found!", Toast.LENGTH_LONG).show();
                } else {
                    EmptyList.setVisibility(View.INVISIBLE);
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

                            list = findViewById(R.id.searchList);
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
    }

    public void Back()
    {
        this.startActivity(new Intent(SearchTransactionVC.this, TransactionListVC.class));
        finish();
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
            this.startActivity(new Intent(SearchTransactionVC.this, TransactionListVC.class));
            finish();
        }
        else{
            this.startActivity(new Intent(SearchTransactionVC.this, TransactionListVC.class));
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
