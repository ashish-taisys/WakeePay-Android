package com.wakeepay.taisys.wakeepay;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class AmountDetailVC extends Activity implements PaymentResultListener {
    private static final String TAG = AmountDetailVC.class.getSimpleName();

    TextView _amount,_fee, _gst, _totalAmount;
    Button btnPay;
    double amount, conFee, gstFee, total;
    double netAmount;
    ApiClient apiClient = new ApiClient();
    String apiClientResult = "";
    String transactionId = "";
    JSONObject resultJsonObj, resultObjJsonObj;
    String status = "", message = "", prefillEmail = "", prefillContact = "";
    SharedPreferences sharedpreferences;
    public static final String mypreference = "wakeepref";
    public static final DecimalFormat decimalFormat = new DecimalFormat("##.00");
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
    LinearLayout mLinearLayout;
    LinearLayout mLinearLayoutHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amount_detail_vc);
        sharedpreferences = getApplicationContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        _amount = (TextView) findViewById(R.id.amount);
        _fee = (TextView) findViewById(R.id.fee);
        _gst = (TextView) findViewById(R.id.gst);
        _totalAmount = (TextView) findViewById(R.id.totalAmount);
        btnPay = (Button) findViewById(R.id.btnPay);

        mLinearLayout = (LinearLayout) findViewById(R.id.expandable);
        //set visibility to GONE
        mLinearLayout.setVisibility(View.GONE);
        mLinearLayoutHeader = (LinearLayout) findViewById(R.id.header);

        mLinearLayoutHeader.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mLinearLayout.getVisibility()==View.GONE){
                    expand();
                }else{
                    collapse();
                }
            }
        });

        try {
            amount = Double.parseDouble(getIntent().getStringExtra("amount"));
            amount = Double.parseDouble(decimalFormat.format(amount));
            conFee = amount * 0.038;
            conFee = Double.parseDouble(decimalFormat.format(conFee));
            gstFee = conFee * 0.18;
            gstFee = Double.parseDouble(decimalFormat.format(gstFee));
            total = amount + conFee + gstFee;
            total = Double.parseDouble(decimalFormat.format(total));
            netAmount = total * 100;
            netAmount = Double.parseDouble(decimalFormat.format(netAmount));

            _amount.setText("₹ " + String.valueOf(amount));
            _fee.setText("₹ " + String.valueOf(conFee));
            _gst.setText("₹ " + String.valueOf(gstFee));
            _totalAmount.setText("₹ " + String.valueOf(total));



            Log.i("wwwwww", sharedpreferences.getString("MobileNo", "aaaaa"));


//            if (sharedpreferences.contains("MobileNo")) {
//                if (sharedpreferences.contains(sharedpreferences.getString("MobileNo", null))) {
//                    apiClientResult = apiClient.getMerchant(sharedpreferences.getString("MobileNo", null));
//                }
//            }

            apiClientResult = apiClient.getMerchant(sharedpreferences.getString("MobileNo", null));
            if (apiClientResult.equals("Internal Server Error")) {
                Toast.makeText(getApplicationContext(), "Error: Please try again!", Toast.LENGTH_LONG).show();
            } else {
                resultJsonObj = new JSONObject(apiClientResult);
                String status = resultJsonObj.getString("status");
                String message = resultJsonObj.getString("message");
                if (resultJsonObj.get("resultObj").equals(null)) {
                    Toast.makeText(getApplicationContext(), " Please enter the valid credentials!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Customer Found successfully", Toast.LENGTH_LONG).show();
                    resultObjJsonObj = (JSONObject) resultJsonObj.get("resultObj");
                    prefillEmail = resultObjJsonObj.getString("email");
                    prefillContact = resultObjJsonObj.getString("mobileNo");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        btnPay.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                try {
                    transactionId = generateTransactionID("IHVRZ");
                    apiClientResult = apiClient.createTransaction(transactionId, prefillContact, String.valueOf(amount),
                            String.valueOf(conFee), String.valueOf(gstFee), String.valueOf(total),getIntent().getStringExtra("remark"));
                    if(apiClientResult.equals("Internal Server Error")) {
                        Toast.makeText(getApplicationContext(), "Error creating transaction: Please try again!", Toast.LENGTH_LONG).show();
                    } else {
                        startPayment();
                        Toast.makeText(getApplicationContext(), "TRANSACTION INITIATED!", Toast.LENGTH_LONG).show();
                        resultJsonObj = new JSONObject(apiClientResult);
                        status = resultJsonObj.getString("status");
                        message = resultJsonObj.getString("message");
                        Log.i("TransactionCreation", message);
                        //finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void startPayment() {
//        res = getResources();
        //      drawable  = res.getDrawable(R.drawable.new_logo);
        //We need to pass current activity in order to let Razorpay create CheckoutActivity
        final Activity activity = this;
        final Checkout co = new Checkout();
//        co.setImage(R.drawable.icon_application);
        try {
            JSONObject preFill = new JSONObject();
            preFill.put("email", prefillEmail);
            preFill.put("contact", prefillContact);
            JSONObject options = new JSONObject();
            options.put("name", "WakeePay");
            options.put("description", "Transaction Amount");
            // options.put("image", "https://i.imgur.com/SvxmlK3.png");
            options.put("currency", "INR");
            options.put("amount", netAmount);
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            apiClientResult = apiClient.updateTransaction(prefillContact, transactionId, "SUCCESS", razorpayPaymentID,sharedpreferences.getString("MerchantId", null));
            //Toast.makeText(this, "RazorPay Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            if(apiClientResult.equals("Internal Server Error")) {
                Toast.makeText(getApplicationContext(), "Error updating transaction: Please try again!", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(AmountDetailVC.this, PaymentSuccess.class);
                intent.putExtra("razorpayPaymentID", razorpayPaymentID.toString());
                intent.putExtra("amountPaid", String.valueOf(total));
                intent.putExtra("transactionDate", String.valueOf(dateFormat.format(new Date())));
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            Log.e("", "Exception in onPaymentSuccess", e);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            apiClientResult = apiClient.updateTransaction(prefillContact, transactionId,
                    "FAILED", "",sharedpreferences.getString("MerchantId", null));
            //Toast.makeText(this, "RazorPay Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            if(apiClientResult.equals("Internal Server Error")) {
                Toast.makeText(getApplicationContext(), "Error updating transaction: Please try again!", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(AmountDetailVC.this, PaymentError.class);
                intent.putExtra("errorCode", String.valueOf(code));
                intent.putExtra("errorMessage", response);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            Log.e("", "Exception in onPaymentError", e);
        }
    }

    public String generateTransactionID(String prefix) throws Exception {
        int maxPrefixLen = 5;
        int maxTimeKeyLen = 14;
        String idKeyDateFormat = "ddMMyyyyHHmmssSSS";
        return createId(prefix, maxPrefixLen, maxTimeKeyLen, idKeyDateFormat);
    }

    private String createId(String prefix, int maxPrefixLen, int maxTimeKeyLen, String idKeyDateFormat) throws Exception {
        SimpleDateFormat keyDateFormat = new SimpleDateFormat(idKeyDateFormat);
        if (prefix != null && prefix.length() > maxPrefixLen) {
            throw new Exception("Prefix length can not be longer than " + maxPrefixLen + ". Prefix : " + prefix);
        }
        String s = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        Random random = new Random();
        StringBuilder sb = new StringBuilder(keyDateFormat.format(new Date()));
        //logger.info("sb.toString() : " + sb.toString());
        int otherLen = maxTimeKeyLen - sb.length();
        for (int i = 0; i < otherLen; i++) {
            sb.append(s.charAt(random.nextInt(s.length())));
        }
        sb.insert(0, (prefix == null ? "" : prefix));
        return sb.toString();
    }

    private void expand() {
        //set Visible
        mLinearLayout.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mLinearLayout.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight());
        mAnimator.start();
    }


    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mLinearLayout.getLayoutParams();
                layoutParams.height = value;
                mLinearLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    private void collapse() {
        int finalHeight = mLinearLayout.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                mLinearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }
}
