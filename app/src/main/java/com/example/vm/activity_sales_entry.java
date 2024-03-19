package com.example.vm;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ListAdapter;

import com.example.vm.Adapters.ProductAdapter;
import com.example.vm.Classes.ProductClass;
import com.example.vm.Classes.SalesClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class activity_sales_entry extends AppCompatActivity {

    Spinner to,productName;
    EditText quantity,rate,sgstRate,cgstRate,billNo;
    TextView hsnCode,intiAmount,gstAmount,fullAmount;
    CheckBox gst;
    ListView salesList;
    private SalesClass salesClass;
    private ProductAdapter productAdapter;
    List<ProductClass> salesClassList;

    List<String> customerList,productList;
    String productHsn,productSgst,productCgst,grandTotal,total,amount,gstTotal;
    DatabaseReference productReference,customerReference,salesBillReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_entry);

        to = findViewById(R.id.cpy_name);
        productName = findViewById(R.id.productName);
        gst = findViewById(R.id.gst);
        hsnCode = findViewById(R.id.hsn);
        intiAmount =findViewById(R.id.initialAmount);
        gstAmount= findViewById(R.id.gstAmount);
        fullAmount = findViewById(R.id.finalAmount);

        billNo = findViewById(R.id.billNo);
        quantity = findViewById(R.id.quantity);
        rate= findViewById(R.id.rate);
        sgstRate = findViewById(R.id.sgst);
        cgstRate=findViewById(R.id.cgst);

        salesList = findViewById(R.id.salesList);

        salesClassList = new ArrayList<>();
        productAdapter = new ProductAdapter(this,salesClassList);
        salesList.setAdapter(productAdapter);


        productReference = FirebaseDatabase.getInstance().getReference().child("product");
        customerReference = FirebaseDatabase.getInstance().getReference().child("addSeller");

        loadSpinnerData();
        loadHsnCodeGst();
    }

    private void loadHsnCodeGst() {
        productName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCustomer = parent.getItemAtPosition(position).toString();

                productReference.orderByChild("pName").equalTo(selectedCustomer).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            productHsn = dataSnapshot.child("pHsn").getValue(String.class);
                            productSgst = dataSnapshot.child("pSgst").getValue(String.class);
                            productCgst = dataSnapshot.child("pCgst").getValue(String.class);
                            hsnCode.setText(productHsn);
                            sgstRate.setText(productSgst);
                            cgstRate.setText(productCgst);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadSpinnerData() {
        customerList  = new ArrayList<>();
        ArrayAdapter<String> cusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,customerList);
        cusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        to.setAdapter(cusAdapter);

        productList = new ArrayList<>();
        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,productList);
        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productName.setAdapter(productAdapter);

        customerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                    customerList.add(dataSnapshot.child("partyName").getValue().toString());
                }
                cusAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        productReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    productList.add(dataSnapshot.child("pName").getValue().toString());

                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void clear(View view) {

    }

    public void addRow(View view) {
        addTableData();
    }

    private void addTableData() {

        String pName= productName.getSelectedItem().toString().trim();
        String pHsn = hsnCode.getText().toString();
        String quan = quantity.getText().toString();
        String rate1 = rate.getText().toString();
        double sgst = Double.parseDouble(cgstRate.getText().toString());
        double cgst = Double.parseDouble(sgstRate.getText().toString());
        String sg = String.valueOf(sgst);
        String cg = String.valueOf(cgst);
        int count =1;




        if(gst.isChecked()){


            String sno = String.valueOf(count);
            double res = Integer.parseInt(quan) * Double.parseDouble(rate1);
            double gstRes = sgst+cgst;
            double initialAmount = res * (gstRes/100);
            double finalAmount = res+initialAmount;

            grandTotal = String.valueOf(finalAmount);
            double initTotal=0,Total=0,TotalGst=0;

            ProductClass productClass = new ProductClass(sno,pName,pHsn,quan,rate1,cg,sg,grandTotal);
            salesClassList.add(productClass);

            for (ProductClass pClass : salesClassList) {
                String name = pClass.getpName();
                double initAmount = Double.parseDouble(productClass.getQuantity()) * Double.parseDouble(productClass.getpRate());
                double initGst = initAmount*((Double.parseDouble(productClass.getpSgst()) + Double.parseDouble(productClass.getpCgst()))/100);
                double val = Double.parseDouble(productClass.getpAmount());
                initTotal = initTotal+initAmount;
                TotalGst = TotalGst + initGst;
                Total = Total+val;
            }
            total = String.valueOf(initTotal);
            gstTotal=String.valueOf(TotalGst);
            amount = String.valueOf(Total);
            Toast.makeText(activity_sales_entry.this, amount, Toast.LENGTH_SHORT).show();
            intiAmount.setText(total);
            gstAmount.setText(gstTotal);
            fullAmount.setText(amount);
            productAdapter.notifyDataSetChanged();

        }
    }

    private  void  toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}