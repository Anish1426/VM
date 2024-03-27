package com.example.vm;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vm.Adapters.SalesReportAdapter;
import com.example.vm.Classes.SalesReportClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SalesReport extends AppCompatActivity {

    DatabaseReference salesReference;

    List<SalesReportClass> salesReportClassList;

    SalesReportAdapter salesReportAdapter;
    ListView salesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sales_report);
        salesList = findViewById(R.id.salesList);

        salesReportClassList = new ArrayList<>();


        salesReference = FirebaseDatabase.getInstance().getReference("salesBill");

        salesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    SalesReportClass salesReportClass = dataSnapshot.getValue(SalesReportClass.class);
                    salesReportClassList.add(salesReportClass);
                }
                salesReportAdapter =new SalesReportAdapter(SalesReport.this,salesReportClassList);
                salesList.setAdapter(salesReportAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}