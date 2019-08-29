package com.example.paymentgateway;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Payment_Details extends AppCompatActivity {


    TextView id,desc,amt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment__details);

        id = findViewById(R.id.PaymentId);
        desc = findViewById(R.id.PaymentDesc);
        amt = findViewById(R.id.PaymentAmt);

        String PayDesc = getIntent().getStringExtra("description");
        String PayAmt = getIntent().getStringExtra("amount");





    }
}
