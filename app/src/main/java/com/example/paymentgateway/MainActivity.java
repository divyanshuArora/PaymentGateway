package com.example.paymentgateway;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.paymentgateway.Config.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {
    EditText amount;
    Button pay;
    String Paying_Amt = null;
    int PayPalRequestCode = 11117;


    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    private static PayPalConfiguration payPalConfiguration = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.Paypal_Client_Id);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfiguration);
        startService(intent);


        amount = findViewById(R.id.amount);
        pay = findViewById(R.id.pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentProcess();
            }
        });



    }

    private void paymentProcess()
    {
        Paying_Amt  = amount.getText().toString();

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(Paying_Amt),"USD","Pay to Merchant",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (paymentConfirmation != null)
                {
                    try
                    {
                        String description = paymentConfirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this,Payment_Details.class)
                        .putExtra("description",description)
                        .putExtra("amount",Paying_Amt)
                        );
                    }
                    catch (Exception e)
                    {
                            e.printStackTrace();
                    }
                }
            }
            else if (resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();

            }
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
        {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }
    }
}
