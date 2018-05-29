package com.ssltest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sslcommerz.library.payment.Classes.PayUsingSSLCommerz;
import com.sslcommerz.library.payment.Listener.OnPaymentResultListener;
import com.sslcommerz.library.payment.Util.ConstantData.CurrencyType;
import com.sslcommerz.library.payment.Util.ConstantData.ErrorKeys;
import com.sslcommerz.library.payment.Util.ConstantData.SdkCategory;
import com.sslcommerz.library.payment.Util.ConstantData.SdkType;
import com.sslcommerz.library.payment.Util.JsonModel.TransactionInfo;
import com.sslcommerz.library.payment.Util.Model.MandatoryFieldModel;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    String TAG = "PAY";
    TextView tv;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.textView);

        et = (EditText) findViewById(R.id.editText);

        ((Button) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    float amount = Float.parseFloat(et.getText().toString());
                    if (amount > 0.0) {
                        doPay(amount);
                    } else {
                        Toast.makeText(MainActivity.this, "Enter Amount!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }


    private void doPay(Float f) {
        /*Mandatory Field For Specific Bank Page*/
        /*MandatoryFieldModel mandatoryFieldModel = new MandatoryFieldModel("","","150",
                "1012", CurrencyType.BDT, SdkType.TESTBOX, SdkCategory.BANK_PAGE, BankName.BKASH);*/


        int rand = new Random().nextInt();

        /*Mandatory Field*/
        MandatoryFieldModel mandatoryFieldModel = new MandatoryFieldModel("hungr5ad2e860ca56d", "hungr5ad2e860ca56d@ssl", f.toString(),
                "trans_" + rand, CurrencyType.BDT, SdkType.TESTBOX, SdkCategory.BANK_LIST);

        /*Call for the payment*/
        PayUsingSSLCommerz.getInstance().setData(this, mandatoryFieldModel, new OnPaymentResultListener() {


            @Override
            public void transactionSuccess(TransactionInfo transactionInfo) {

                // If payment is success and risk label is 0.
                if (transactionInfo.getRiskLevel().equals("0")) {
                    Log.d(TAG, "Transaction Successfully completed");
                    Log.d(TAG, transactionInfo.getTranId());
                    Log.d(TAG, transactionInfo.getBankTranId());
                    Log.d(TAG, transactionInfo.getAmount());
                    Log.d(TAG, transactionInfo.getStoreAmount());
                    Log.d(TAG, transactionInfo.getTranDate());
                    Log.d(TAG, transactionInfo.getStatus());
                    tv.setText("Transaction Successfully completed");
                    et.setText(null);
                }
                // Payment is success but payment is not complete yet. Card on hold now.
                else {
                    Log.d(TAG, "Transaction in risk. Risk Title : " + transactionInfo.getRiskTitle().toString());
                    tv.setText("Transaction in risk.");
                    et.setText(null);
                }

            }

            @Override
            public void transactionFail(TransactionInfo transactionInfo) {
                // Transaction failed
                Log.e(TAG, "Transaction Fail");
                tv.setText("Transaction Fail");
                et.setText(null);
            }

            @Override
            public void error(int errorCode) {
                switch (errorCode) {
                    // Your provides information is not valid.
                    case ErrorKeys.USER_INPUT_ERROR:
                        Log.e(TAG, "User Input Error");
                        tv.setText("User Input Error");
                        break;
                    // Internet is not connected.
                    case ErrorKeys.INTERNET_CONNECTION_ERROR:
                        Log.e(TAG, "Internet Connection Error");
                        tv.setText("Internet Connection Error");
                        break;
                    // Server is not giving valid data.
                    case ErrorKeys.DATA_PARSING_ERROR:
                        Log.e(TAG, "Data Parsing Error");
                        tv.setText("Data Parsing Error");
                        break;
                    // User press back button or canceled the transaction.
                    case ErrorKeys.CANCEL_TRANSACTION_ERROR:
                        Log.e(TAG, "User Cancel The Transaction");
                        tv.setText("User Cancel The Transaction");
                        break;
                    // Server is not responding.
                    case ErrorKeys.SERVER_ERROR:
                        Log.e(TAG, "Server Error");
                        tv.setText("Server Error");
                        break;
                    // For some reason network is not responding
                    case ErrorKeys.NETWORK_ERROR:
                        Log.e(TAG, "Network Error");
                        tv.setText("Network Error");
                        break;
                }
                et.setText(null);
            }


        });
    }

}
