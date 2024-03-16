package com.example.vm;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_sales_entry extends AppCompatActivity {

    Spinner to,productName;
    EditText quantity,rate,sgstRate,cgstRate;
    TextView hsnCode,intiAmount,gstAmount,finalAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_entry);

        to = findViewById(R.id.cpy_name);
        productName = findViewById(R.id.productName);

        hsnCode = findViewById(R.id.hsn);
        intiAmount =findViewById(R.id.initialAmount);
        gstAmount= findViewById(R.id.gstAmount);
        finalAmount = findViewById(R.id.finalAmount);

        quantity = findViewById(R.id.quantity);
        rate= findViewById(R.id.rate);
        sgstRate = findViewById(R.id.sgst);
        cgstRate=findViewById(R.id.cgst);



    }

    public void clear(View view) {

    }

    public void addRow(View view) {

    }
}