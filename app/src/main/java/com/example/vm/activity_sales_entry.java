package com.example.vm;



import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
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
import java.util.Arrays;
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
    private int clickedRowPosition = -1;

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

        salesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickedRowPosition = position; // Store the position of the clicked row
                salesClass = salesClassList.get(position);
                String quan = salesClass.getQuantity();
                String rates = salesClass.getpRate();

                // Set the values to corresponding EditText fields
                quantity.setText(quan);
                rate.setText(rates);
            }
        });

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
            final String id = billNo.getText().toString();

            // Check if the bill already exists in the database
            salesBillReference.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    salesBillReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (int i = 0; i < salesClassList.size(); i++) {
                                SalesClass salesClass = salesClassList.get(i);
                                String productKey = salesBillReference.child(id).push().getKey();
                                salesBillReference.child(id).child(productKey).setValue(salesClass)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                toastMessage("Bill Added Successfully");
                                                salesClassList.clear();
                                                salesAdapter.notifyDataSetChanged();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                toastMessage("Bill Adding Failed");
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    toastMessage("Data Removing Failed");
                }
            });
        } catch (Exception e) {
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
        customerList = new ArrayList<>();
        ArrayAdapter<String> cusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, customerList);
        cusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        to.setAdapter(cusAdapter);

        productList = new ArrayList<>();
        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productList);
        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productName.setAdapter(productAdapter);

        customerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
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
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Object pNameValue = dataSnapshot.child("pName").getValue();
                    if (pNameValue != null) {
                        productList.add(pNameValue.toString());
                    } else {
                        toastMessage("No data in Product");
                    }
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event if needed
            }
        });
    }

        public void clear(View view) {

        String bill = billNo.getText().toString();
        String pName = productName.getSelectedItem().toString().trim();
        String pHsn = hsnCode.getText().toString();
        String quan = quantity.getText().toString();
        String rate1 = rate.getText().toString();
        double sgst = Double.parseDouble(cgstRate.getText().toString());
        double cgst = Double.parseDouble(sgstRate.getText().toString());
        String sg = String.valueOf(sgst);
        String cg = String.valueOf(cgst);

       SalesClass salesClass1 = new SalesClass("1",pName,pHsn,quan,rate1,sg,cg,"2000");


       salesBillReference.child(bill).setValue(salesClass1).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void unused) {
               toastMessage("Updated Successfully");
           }
       })
                       .addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               toastMessage("Update failed");
                           }
                       });
               salesClassList.clear();
        salesAdapter.notifyDataSetChanged();
    }

    public void addRow(View view) {
        addTableData(clickedRowPosition); // Pass the clicked row position to the addTableData() method
        salesAdapter.notifyDataSetChanged();
    }

    private void addTableData(int clickedPosition) {
        String pName = productName.getSelectedItem().toString().trim();
        String pHsn = hsnCode.getText().toString();
        String quan = quantity.getText().toString();
        String rate1 = rate.getText().toString();
        double sgst = Double.parseDouble(cgstRate.getText().toString());
        double cgst = Double.parseDouble(sgstRate.getText().toString());
        String sg = String.valueOf(sgst);
        String cg = String.valueOf(cgst);

        // Calculate the new amounts
        double res = Integer.parseInt(quan) * Double.parseDouble(rate1);
        double gstRes = (sgst + cgst)/100;        
        double initialAmount = res * gstRes;
        double finalAmount = res + initialAmount;
        String gstTotalAmount = String.valueOf(initialAmount);
        String grandTotal = String.valueOf(finalAmount);

        // If a row was clicked, update its data
        if (clickedPosition != -1) {
            SalesClass clickedSalesClass = salesClassList.get(clickedPosition);
            clickedSalesClass.setQuantity(quan);
            clickedSalesClass.setpRate(rate1);
            clickedSalesClass.setpAmount(grandTotal);
            salesClassList.set(clickedPosition, clickedSalesClass); // Update the item in the list
        } else {
            // If no row was clicked, add a new item to the list
            String sno = String.valueOf(salesClassList.size() + 1);
            SalesClass newSalesClass = new SalesClass(sno, pName, pHsn, quan, rate1, cg, sg, grandTotal);
            salesClassList.add(newSalesClass); // Add the new item to the list
        }

        // Recalculate the total amount, GST amount, and initial amount
        double totalAmount = 0;
        double gstValue =0;
        double totalGstAmount = 0;
        double totalInitialAmount = 0;
        
        for (SalesClass salesClass : salesClassList) {

            gstValue += Double.parseDouble(salesClass.getpSgst())+Double.parseDouble(salesClass.getpCgst());
            totalInitialAmount += Double.parseDouble(salesClass.getQuantity())*Double.parseDouble(salesClass.getpRate());
            totalGstAmount += totalInitialAmount * (gstValue/100);
            totalAmount += Double.parseDouble(salesClass.getpAmount());
        }

        // Update the UI
        salesAdapter.notifyDataSetChanged(); // Notify the adapter of data change
        clearEditTextFields(); // Clear the EditText fields
        updateTotalAmountTextView(totalAmount,totalGstAmount,totalInitialAmount); // Update the total amount TextView
    }

    // Method to update the total amount TextView
    private void updateTotalAmountTextView(double totalAmount,double gstAmounts,double initialAmount) {
        String initAmount = String.valueOf(initialAmount);
        String gst = String.valueOf(gstAmounts);
        String totalAmountString = String.valueOf(totalAmount);


        gstAmount.setText(gst);
        intiAmount.setText(initAmount);
        fullAmount.setText(totalAmountString);

    }


    // Method to clear EditText fields
    private void clearEditTextFields() {
        quantity.setText("");
        rate.setText("");
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
        salesClassList.clear();
        salesAdapter.notifyDataSetChanged();
        String billNumber = billNo.getText().toString();

        salesBillReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String val = dataSnapshot.getKey();
                    if(val.equals(billNumber)){
                        salesBillReference.child(val).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                double initTotal=0,Total=0,TotalGst=0;
                                for (DataSnapshot snapshot1 : snapshot.getChildren()){

                                    SalesClass salesClass1 = snapshot1.getValue(SalesClass.class);
                                    if(salesClass1 != null){
                                        salesClassList.add(salesClass1);
                                    }

                                    for(SalesClass salesClass2 : salesClassList){
                                        double initAmount = Double.parseDouble(salesClass2.getQuantity()) * Double.parseDouble(salesClass2.getpRate());
                                        double initGst = initAmount*((Double.parseDouble(salesClass2.getpSgst()) + Double.parseDouble(salesClass2.getpCgst()))/100);
                                        double val = Double.parseDouble(salesClass2.getpAmount());
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
                                }


                                salesAdapter = new SalesAdapter(activity_sales_entry.this,salesClassList);
                                salesList.setAdapter(salesAdapter);
                                salesAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//        salesBillReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        String key = snapshot.getKey();
//
//
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("TAG", "Error fetching data", databaseError.toException());
//                toastMessage("Error fetching data");
//            }
//        });
    }


}