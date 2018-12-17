package com.wakeepay.taisys.wakeepay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ApiClient {
    //String baseUrl = "http://192.168.1.100:8888";
     String baseUrl = "http://122.176.40.215:8888";
//    String baseUrl = "http://192.168.1.13:9000";
    public String updateCustomer(String firstName, String email, String mobileNo,String password, String type) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Log.i("Inside ", " rest api call - updateCustomer");
            URL url = new URL(baseUrl + "/Ihv/mic");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                DataOutputStream dataStreamToServer = new DataOutputStream(conn.getOutputStream());
                dataStreamToServer.writeBytes("mobileNo=" + URLEncoder.encode(mobileNo, "UTF-8")
                        + "&firstName=" + URLEncoder.encode(firstName, "UTF-8")
                        + "&email=" + URLEncoder.encode(email, "UTF-8")
                        + "&password=" + URLEncoder.encode(password, "UTF-8")
                        + "&type=" + URLEncoder.encode(type, "UTF-8"));
                dataStreamToServer.flush();
                dataStreamToServer.close();
                BufferedReader br = null;
                if (conn.getResponseCode() < 400) {
                    br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                } else {
                    br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
                }
                StringBuffer output = new StringBuffer();
                String nextLine = new String();
                while ((nextLine = br.readLine()) != null) {
                    output.append(nextLine);
                }
                br.close();
                Log.i("output.toString() ", output.toString());
                return output.toString();
            } else {
                return "A connection to the remote server could not be stablished.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Internal Server Error";
    }

    public String sendOtp(String mobileNo) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Log.i("Inside ", " rest api call - sendOtp");
            URL url = new URL(baseUrl + "/Ihv/os");
            Log.i("URL",baseUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                DataOutputStream dataStreamToServer = new DataOutputStream(conn.getOutputStream());
                dataStreamToServer.writeBytes("mobileNo=" + URLEncoder.encode(mobileNo, "UTF-8"));
                dataStreamToServer.flush();
                dataStreamToServer.close();
                BufferedReader br = null;
                Log.i("URL",baseUrl+"/Ihv/os");
                if (conn.getResponseCode() < 400) {
                    br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    Log.i("URL",baseUrl+"/Ihv/os1");
                } else {
                    br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
                    Log.i("URL",baseUrl+"/Ihv/os2");
                }
                StringBuffer output = new StringBuffer();
                String nextLine = new String();
                while ((nextLine = br.readLine()) != null) {
                    output.append(nextLine);
                }
                br.close();
                Log.i("output.toString() ", output.toString());
                return output.toString();
            } else {
                return "A connection to the remote server could not be stablished.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            //return e.getMessage();
        }
        return "Internal Server Error";
    }

    public String verifyOtp(String mobileNo, String otp) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Log.i("Inside ", " rest api call - verifyOtp");
            URL url = new URL(baseUrl + "/Ihv/ov");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                DataOutputStream dataStreamToServer = new DataOutputStream(conn.getOutputStream());
                dataStreamToServer.writeBytes("mobileNo=" + URLEncoder.encode(mobileNo, "UTF-8") + "&otp="
                        + URLEncoder.encode(otp, "UTF-8"));
                dataStreamToServer.flush();
                dataStreamToServer.close();
                BufferedReader br = null;
                if (conn.getResponseCode() < 400) {
                    br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                } else {
                    br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
                }
                StringBuffer output = new StringBuffer();
                String nextLine = new String();
                while ((nextLine = br.readLine()) != null) {
                    output.append(nextLine);
                }
                br.close();
                Log.i("output.toString() ", output.toString());
                return output.toString();
            } else {
                return "A connection to the remote server could not be established.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Internal Server Error";
    }

    public String loginCustomer(String email, String mobileNo, String password) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Log.i("Inside ", " rest api call - createCustomer");
            URL url = new URL(baseUrl + "/Ihv/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                DataOutputStream dataStreamToServer = new DataOutputStream(conn.getOutputStream());
                dataStreamToServer.writeBytes("mobileNo=" + URLEncoder.encode(mobileNo, "UTF-8")
                        + "&email=" + URLEncoder.encode(email, "UTF-8")
                        + "&password=" + URLEncoder.encode(password, "UTF-8"));
                dataStreamToServer.flush();
                dataStreamToServer.close();
                BufferedReader br = null;
                if (conn.getResponseCode() < 400) {
                    br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                } else {
                    br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
                }
                StringBuffer output = new StringBuffer();
                String nextLine = new String();
                while ((nextLine = br.readLine()) != null) {
                    output.append(nextLine);
                }
                br.close();
                Log.i("output.toString() ", output.toString());
                return output.toString();
            }
            else {
                return "A connection to the remote server could not be stablished.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Internal Server Error";
    }


    public String getMerchant(String mobileNo) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Log.i("Inside ", " rest api call - getMerchant");
            URL url = new URL(baseUrl + "/Ihv/mig");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                DataOutputStream dataStreamToServer = new DataOutputStream(conn.getOutputStream());
                dataStreamToServer.writeBytes("mobileNo=" + URLEncoder.encode(mobileNo, "UTF-8"));
                dataStreamToServer.flush();
                dataStreamToServer.close();
                BufferedReader br = null;
                if (conn.getResponseCode() < 400) {
                    br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                } else {
                    br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
                }
                StringBuffer output = new StringBuffer();
                String nextLine = new String();
                while ((nextLine = br.readLine()) != null) {
                    output.append(nextLine);
                }
                br.close();
                Log.i("output.toString() ", output.toString());
                return output.toString();
            } else {
                return "A connection to the remote server could not be stablished.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Internal Server Error";
    }

//
//    public String createCustomer(String mobileNo) {
//        try {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            Log.i("Inside ", " rest api call - createCustomer");
//            URL url = new URL(baseUrl + "/Ihv/cic");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            if (conn != null) {
//                conn.setRequestMethod("POST");
//                conn.setDoInput(true);
//                conn.setDoOutput(true);
//                conn.setUseCaches(false);
//                DataOutputStream dataStreamToServer = new DataOutputStream(conn.getOutputStream());
//                dataStreamToServer.writeBytes("mobileNo=" + URLEncoder.encode(mobileNo, "UTF-8"));
//                dataStreamToServer.flush();
//                dataStreamToServer.close();
//                BufferedReader br = null;
//                if (conn.getResponseCode() < 400) {
//                    br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
//                } else {
//                    br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
//                }
//                StringBuffer output = new StringBuffer();
//                String nextLine = new String();
//                while ((nextLine = br.readLine()) != null) {
//                    output.append(nextLine);
//                }
//                br.close();
//                Log.i("output.toString() ", output.toString());
//                return output.toString();
//            } else {
//                return "A connection to the remote server could not be stablished.";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "Internal Server Error";
//    }
//
    public String updateCustomer(String mobileNo, String fullName, String email, String dob, String accountNo, String accountType, String ifsc,String institutionName,String gstNo) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Log.i("Inside ", " rest api call - updateCustomer");
            URL url = new URL(baseUrl + "/Ihv/ciu");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                DataOutputStream dataStreamToServer = new DataOutputStream(conn.getOutputStream());
                dataStreamToServer.writeBytes("mobileNo=" + URLEncoder.encode(mobileNo, "UTF-8")
                        + "&fullName=" + URLEncoder.encode(fullName, "UTF-8")
                        + "&email=" + URLEncoder.encode(email, "UTF-8")
                        + "&dob=" + URLEncoder.encode(dob, "UTF-8")
                        + "&accountNo=" + URLEncoder.encode(accountNo, "UTF-8")
                        + "&accountType=" + URLEncoder.encode(accountType, "UTF-8")
                        + "&ifsc=" + URLEncoder.encode(ifsc, "UTF-8")
                        + "&intitutionName=" + URLEncoder.encode(institutionName, "UTF-8")
                        + "&gstNo=" + URLEncoder.encode(gstNo, "UTF-8")
                );
                dataStreamToServer.flush();
                dataStreamToServer.close();
                BufferedReader br = null;
                if (conn.getResponseCode() < 400) {
                    br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                } else {
                    br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
                }
                StringBuffer output = new StringBuffer();
                String nextLine = new String();
                while ((nextLine = br.readLine()) != null) {
                    output.append(nextLine);
                }
                br.close();
                Log.i("output.toString() ", output.toString());
                return output.toString();
            } else {
                return "A connection to the remote server could not be stablished.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Internal Server Error";
    }


    public String updateUser(String mobileNo, String fullName, String email, String dob) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Log.i("Inside ", " rest api call - updateUser");
            URL url = new URL(baseUrl + "/Ihv/uiu");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                DataOutputStream dataStreamToServer = new DataOutputStream(conn.getOutputStream());
                dataStreamToServer.writeBytes("mobileNo=" + URLEncoder.encode(mobileNo, "UTF-8")
                        + "&fullName=" + URLEncoder.encode(fullName, "UTF-8")
                        + "&email=" + URLEncoder.encode(email, "UTF-8")
                        + "&dob=" + URLEncoder.encode(dob, "UTF-8"));
                dataStreamToServer.flush();
                dataStreamToServer.close();
                BufferedReader br = null;
                if (conn.getResponseCode() < 400) {
                    br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                } else {
                    br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
                }
                StringBuffer output = new StringBuffer();
                String nextLine = new String();
                while ((nextLine = br.readLine()) != null) {
                    output.append(nextLine);
                }
                br.close();
                Log.i("output.toString() ", output.toString());
                return output.toString();
            } else {
                return "A connection to the remote server could not be stablished.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Internal Server Error";
    }

    public String createTransaction(String transId, String mobileNo, String amount, String conFee, String gstFee, String total, String remark) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Log.i("Inside ", " rest api call - createTransaction");
            URL url = new URL(baseUrl + "/Ihv/tic");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                DataOutputStream dataStreamToServer = new DataOutputStream(conn.getOutputStream());
                dataStreamToServer.writeBytes("transId=" + URLEncoder.encode(transId, "UTF-8") + "&mobileNo=" + URLEncoder.encode(mobileNo, "UTF-8")
                        + "&amount=" + URLEncoder.encode(amount, "UTF-8") + "&convFee=" + URLEncoder.encode(conFee, "UTF-8")
                        + "&gstFee=" + URLEncoder.encode(gstFee, "UTF-8") + "&totalAmount=" + URLEncoder.encode(total, "UTF-8")
                        + "&remark=" + URLEncoder.encode(remark, "UTF-8"));
                dataStreamToServer.flush();
                dataStreamToServer.close();
                BufferedReader br = null;
                if (conn.getResponseCode() < 400) {
                    br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                } else {
                    br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
                }
                StringBuffer output = new StringBuffer();
                String nextLine = new String();
                while ((nextLine = br.readLine()) != null) {
                    output.append(nextLine);
                }
                br.close();
                Log.i("output.toString() ", output.toString());
                return output.toString();
            } else {
                return "A connection to the remote server could not be stablished.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Internal Server Error";
    }

    public String updateTransaction(String mobileNo, String transId, String status, String paymentRefId, String MerchID) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Log.i("Inside ", " rest api call - updateTransaction");
            URL url = new URL(baseUrl + "/Ihv/tiu");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                DataOutputStream dataStreamToServer = new DataOutputStream(conn.getOutputStream());
                dataStreamToServer.writeBytes("mobileNo=" + URLEncoder.encode(mobileNo, "UTF-8")
                        + "&transId=" + URLEncoder.encode(transId, "UTF-8")
                        + "&transaction_status=" + URLEncoder.encode(status, "UTF-8")
                        + "&paymentRefId=" + URLEncoder.encode(paymentRefId, "UTF-8")
                        + "&MerchID=" + URLEncoder.encode(MerchID, "UTF-8"));
                dataStreamToServer.flush();
                dataStreamToServer.close();
                BufferedReader br = null;
                if (conn.getResponseCode() < 400) {
                    br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                } else {
                    br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
                }
                StringBuffer output = new StringBuffer();
                String nextLine = new String();
                while ((nextLine = br.readLine()) != null) {
                    output.append(nextLine);
                }
                br.close();
                Log.i("output.toString() ", output.toString());
                return output.toString();
            } else {
                return "A connection to the remote server could not be stablished.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Internal Server Error";
    }

    public String getTransactions(String mobileNo) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Log.i("Inside ", " rest api call - getTransactions");
            URL url = new URL(baseUrl + "/Ihv/tig");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                DataOutputStream dataStreamToServer = new DataOutputStream(conn.getOutputStream());
                dataStreamToServer.writeBytes("mobileNo=" + URLEncoder.encode(mobileNo, "UTF-8"));
                dataStreamToServer.flush();
                dataStreamToServer.close();
                BufferedReader br = null;
                if (conn.getResponseCode() < 400) {
                    br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                } else {
                    br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
                }
                StringBuffer output = new StringBuffer();
                String nextLine = new String();
                while ((nextLine = br.readLine()) != null) {
                    output.append(nextLine);
                }
                br.close();
                Log.i("output.toString() ", output.toString());
                return output.toString();
            } else {
                return "A connection to the remote server could not be stablished.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Internal Server Error";
    }

    public String getMerchantTransactions(String merchId) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Log.i("Inside ", " rest api call - getTransactions");
            URL url = new URL(baseUrl + "/Ihv/tmg");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                DataOutputStream dataStreamToServer = new DataOutputStream(conn.getOutputStream());
                dataStreamToServer.writeBytes("merchId=" + URLEncoder.encode(merchId, "UTF-8"));
                dataStreamToServer.flush();
                dataStreamToServer.close();
                BufferedReader br = null;
                if (conn.getResponseCode() < 400) {
                    br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                } else {
                    br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
                }
                StringBuffer output = new StringBuffer();
                String nextLine = new String();
                while ((nextLine = br.readLine()) != null) {
                    output.append(nextLine);
                }
                br.close();
                Log.i("output.toString() ", output.toString());
                return output.toString();
            } else {
                return "A connection to the remote server could not be stablished.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Internal Server Error";
    }

    public String verifyMerchant(String merchID) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Log.i("Inside ", " rest api call - VerifyMerchant");
            URL url = new URL(baseUrl + "/Ihv/pm");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                DataOutputStream dataStreamToServer = new DataOutputStream(conn.getOutputStream());
                dataStreamToServer.writeBytes("merchID=" + URLEncoder.encode(merchID, "UTF-8"));
                dataStreamToServer.flush();
                dataStreamToServer.close();
                BufferedReader br = null;
                if (conn.getResponseCode() < 400) {
                    br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                } else {
                    br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
                }
                StringBuffer output = new StringBuffer();
                String nextLine = new String();
                while ((nextLine = br.readLine()) != null) {
                    output.append(nextLine);
                }
                br.close();
                Log.i("output.toString() ", output.toString());
                return output.toString();
            } else {
                return "A connection to the remote server could not be stablished.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Internal Server Error";
    }

    public String updatePassword(String mobileNo,String password) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Log.i("Inside ", " rest api call - updatePassword");
            URL url = new URL(baseUrl + "/Ihv/piu");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                DataOutputStream dataStreamToServer = new DataOutputStream(conn.getOutputStream());
                dataStreamToServer.writeBytes("mobileNo=" + URLEncoder.encode(mobileNo, "UTF-8")
                        + "&password=" + URLEncoder.encode(password, "UTF-8"));
                dataStreamToServer.flush();
                dataStreamToServer.close();
                BufferedReader br = null;
                if (conn.getResponseCode() < 400) {
                    br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                } else {
                    br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));
                }
                StringBuffer output = new StringBuffer();
                String nextLine = new String();
                while ((nextLine = br.readLine()) != null) {
                    output.append(nextLine);
                }
                br.close();
                Log.i("output.toString() ", output.toString());
                return output.toString();
            } else {
                return "A connection to the remote server could not be stablished.";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Internal Server Error";
    }

}
