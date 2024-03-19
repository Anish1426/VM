package com.example.vm;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.vm.Adapters.PurchaseAdapter;
import com.example.vm.Adapters.SalesAdapter;
import com.example.vm.Classes.SalesClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_sales_entry extends AppCompatActivity {

    Spinner to,productName;
    EditText quantity,rate,sgstRate,cgstRate,billNo;
    TextView hsnCode,intiAmount,gstAmount,fullAmount;
    CheckBox gst;
    ListView salesList;
    private SalesClass salesClass;
    private SalesAdapter salesAdapter;
    List<SalesClass> salesClassList;

    List<String> customerList,productList;
    String productHsn,productSgst,productCgst,grandTotal,total,amount,gstTotal;
    DatabaseReference productReference,customerReference,salesBillReference;

    Button generateBill;

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_entry);

        productReference = FirebaseDatabase.getInstance().getReference().child("product");
        customerReference = FirebaseDatabase.getInstance().getReference().child("addSeller");
        salesBillReference = FirebaseDatabase.getInstance().getReference().child("salesBill");


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
        salesAdapter = new SalesAdapter(this,salesClassList);
        salesList.setAdapter(salesAdapter);

        generateBill = findViewById(R.id.generateBill);

        generateBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(activity_sales_entry.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Request the permission
                    ActivityCompat.requestPermissions(activity_sales_entry.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    billNoChange();
                    salesBillSave();
                    createPdf();
                }
            }
        });

        billNoChange();
        loadSpinnerData();
        loadHsnCodeGst();


    }

    private void createPdf() {
    }

    private void salesBillSave() {
        try {
            String id = billNo.getText().toString();
            for (int i = 0; i < salesClassList.size(); i++) {
                salesClass = salesClassList.get(i);

                String sno = salesClass.getpCode();
                String pName = salesClass.getpName();
                String pHsn = salesClass.getpHsn();
                String pQuantity = salesClass.getQuantity();
                String pRate = salesClass.getpRate();
                String pcGst = salesClass.getpCgst();
                String pSgst = salesClass.getpSgst();
                String pAmount = salesClass.getpAmount();

                salesClass = new SalesClass(sno, pName, pHsn, pQuantity, pRate, pcGst, pSgst, pAmount);

                String productKey = salesBillReference.child(id).push().getKey();

                salesBillReference.child(id).child(productKey).setValue(salesClass)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                toastMessage("Bill Saved Successfully");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                toastMessage("Bill Saving Failed");
                            }
                        });
            }
        }
        catch (Exception e){
            toastMessage(e.getMessage());
        }
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

            salesClass = new SalesClass(sno,pName,pHsn,quan,rate1,cg,sg,grandTotal);
            salesClassList.add(salesClass);

            for (SalesClass sClass : salesClassList) {
                String name = sClass.getpName();
                double initAmount = Double.parseDouble(salesClass.getQuantity()) * Double.parseDouble(salesClass.getpRate());
                double initGst = initAmount*((Double.parseDouble(salesClass.getpSgst()) + Double.parseDouble(salesClass.getpCgst()))/100);
                double val = Double.parseDouble(salesClass.getpAmount());
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
            salesAdapter.notifyDataSetChanged();

        }
    }

    private void billNoChange() {
        salesBillReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                String billNumber = String.valueOf(count+1);
                billNo.setText(billNumber);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private  void  toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    public void billSearch(View view) {
        toastMessage("Sample");
    }
}