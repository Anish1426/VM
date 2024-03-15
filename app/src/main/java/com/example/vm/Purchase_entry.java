package com.example.vm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vm.Adapters.ProductAdapter;
import com.example.vm.Classes.ProductClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Purchase_entry extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    sqlconnection db ;
    Spinner cpy_name;
    TableLayout tableLayout;
    Button addRowButton,generateBill,clear;
    Spinner productName;
    String pno,partyAddress,gstIn,sGst,cGst;

    CheckBox gst;
    List<ProductClass> productClassList;
    ProductClass productClass;
    private ProductAdapter productAdapter;

    String total ,gstTotal,grandTotal, amount;



    String[] items = {"--Select--", "Item 2", "Item 3", "Item 4"};
    public int rowCounter = 1;

    DatabaseReference customerReference ,productReference;
    ArrayList<String> labels;
    ArrayAdapter<String> dataAdapter;

    List<String> product;

    TextView quantity;
    EditText sgst1,cgst1,quan,rate;

    ListView productList;

    TextView totalAmount,gstAmount,fullAmount;

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_entry);
        productName = findViewById(R.id.spinner);
        cpy_name = findViewById(R.id.cpy_name);
        sgst1 = findViewById(R.id.sgst);
        cgst1 = findViewById(R.id.cgst);
        gst = findViewById(R.id.gst);
        quantity = findViewById(R.id.quantity);
        quan = findViewById(R.id.quan);
        rate = findViewById(R.id.rate);
        clear = findViewById(R.id.clear);
        generateBill = findViewById(R.id.generateBill);
        totalAmount = findViewById(R.id.initialAmount);
        gstAmount = findViewById(R.id.gstAmount);
        fullAmount = findViewById(R.id.finalAmount);
        productList = findViewById(R.id.purchaseList);
        productClassList = new ArrayList<>();
        productAdapter =new ProductAdapter(Purchase_entry.this,productClassList);
        productList.setAdapter(productAdapter);

        addRowButton = findViewById(R.id.add_row);
        labels = new ArrayList<>();
        dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cpy_name.setAdapter(dataAdapter);

        customerReference = FirebaseDatabase.getInstance().getReference().child("addSeller");
        productReference = FirebaseDatabase.getInstance().getReference().child("product");

        cpy_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCustomer = parent.getItemAtPosition(position).toString();
                customerReference.orderByChild("partyName").equalTo(selectedCustomer).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            partyAddress= dataSnapshot.child("address").getValue(String.class);
                            gstIn = dataSnapshot.child("gstNo").getValue(String.class);
                        }
                        Log.d("FirebaseDebug", "Selected Customer: " + selectedCustomer);
                        Log.d("FirebaseDebug", "Phone Number: " + partyAddress);
                        Log.d("FirebaseDebug", "GST Number: " + gstIn);
                        System.out.println(partyAddress);
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


               productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       ProductClass productClass1 = productClassList.get(position);
                       String name = productClass1.getpName();
                       Toast.makeText(Purchase_entry.this,name,Toast.LENGTH_SHORT).show();
                   }
               });

               clear.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {

                   }
               });


        addRowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewRow();
            }
        });
        generateBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Purchase_entry.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Request the permission
                    ActivityCompat.requestPermissions(Purchase_entry.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    // Permission already granted, call your createPdf() method here
                    createPdf();
                }
            }
        });







        gst.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sgst1.setEnabled(true);
                cgst1.setEnabled(true);
            } else {
                sgst1.setEnabled(false);
                cgst1.setEnabled(false);
            }
        });
        loadCompanyData();
        loadProductData();
        productDetails();




    }



    private void addNewRow() {

        String pName= productName.getSelectedItem().toString().trim();
        String pHsn = quantity.getText().toString();
        String quantity = quan.getText().toString();
        String rate1 = rate.getText().toString();
        double sgst = Double.parseDouble(sgst1.getText().toString());
        double cgst = Double.parseDouble(cgst1.getText().toString());
        String sg = String.valueOf(sgst);
        String cg = String.valueOf(cgst);
        double row =0;
        int count =1;




        if(gst.isChecked()){


            String sno = String.valueOf(count);
            double res = Integer.parseInt(quantity) * Double.parseDouble(rate1);
            double gstRes = sgst+cgst;
            double initialAmount = res * (gstRes/100);
            double finalAmount = res+initialAmount;

            grandTotal = String.valueOf(finalAmount);
            double initTotal=0,Total=0,TotalGst=0;

            ProductClass productClass = new ProductClass(sno,pName,pHsn,quantity,rate1,cg,sg,grandTotal);
            productClassList.add(productClass);

            for (ProductClass pClass : productClassList) {
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
            Toast.makeText(Purchase_entry.this, amount, Toast.LENGTH_SHORT).show();
            totalAmount.setText(total);
            gstAmount.setText(gstTotal);
            fullAmount.setText(amount);
            productAdapter.notifyDataSetChanged();
            count++;

        }




    }

    private void createPdf() {

        try {

            String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            File file = new File(pdfPath, "sample.pdf");

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();


            PdfPTable border = new PdfPTable(1);
            border.setWidthPercentage(100);
            PdfPCell bod = new PdfPCell(new Phrase(""));
            Paragraph para = new Paragraph("INVOICE COPY", FontFactory.getFont(FontFactory.TIMES_ROMAN,14, Font.BOLD));
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(new Paragraph(" "));
            PdfPTable name = new PdfPTable(2);
            name.setWidthPercentage(100);
            PdfPCell headerCell = new PdfPCell(new Phrase(""));
            com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            headerCell.setPhrase(new Phrase(" GST: 33BNFPR9407M1ZI",headerFont));
            headerCell.setBorder(Rectangle.NO_BORDER);
            PdfPCell gst = new PdfPCell(new Phrase(""));
            gst.setPhrase(new Phrase(" CELL:7010917885",headerFont));
            gst.setHorizontalAlignment(Element.ALIGN_RIGHT);
            gst.setBorder(Rectangle.NO_BORDER);

            name.addCell(headerCell);
            name.addCell(gst);
            border.addCell(bod);
            document.add(border);
            document.add(name);

            Paragraph company = new Paragraph("VETRIMURUGAN & CO",FontFactory.getFont(FontFactory.TIMES_ROMAN,30,BaseColor.BLACK));
            company.setAlignment(Element.ALIGN_CENTER);
            Paragraph address = new Paragraph("Waste Paper, Govt Contractors, Mill Suppliers & General Merchants"
                    + "\nNo,66 Subramaniyar koil Street Panapakkam-631052 Nemili Tk,"
                    + "\nRanipet district,TamilNadu-631052.",FontFactory.getFont(FontFactory.TIMES_ROMAN,5,BaseColor.BLACK));
            address.setAlignment(Element.ALIGN_CENTER);
            document.add(new Paragraph(""));
            document.add(company);
            document.add(address);

            document.add(new Paragraph(" "));

            PdfPTable details = new PdfPTable(3);
            details.setWidthPercentage(100);
            PdfPCell det = new PdfPCell(new Phrase(""));
            det.setPhrase(new Phrase("To,",FontFactory.getFont(FontFactory.TIMES_BOLD,12,BaseColor.BLACK)));
            det.setBorder(Rectangle.NO_BORDER);

            String cname = (String)cpy_name.getSelectedItem();
            PdfPCell abi = new PdfPCell(new Phrase(""));
            abi.setPhrase(new Phrase(cname,FontFactory.getFont(FontFactory.TIMES_BOLD,14,BaseColor.BLACK)));
            abi.setBorder(Rectangle.NO_BORDER);

            PdfPCell adr = new PdfPCell(new Phrase(""));
            adr.setPhrase(new Phrase(partyAddress,FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
            adr.setBorder(Rectangle.NO_BORDER);



            PdfPCell invoice = new PdfPCell(new Phrase(""));
            invoice.setPhrase(new Phrase("",FontFactory.getFont(FontFactory.TIMES_BOLD,12,BaseColor.BLACK)));
            invoice.setBorder(Rectangle.NO_BORDER);

            PdfPCell date = new PdfPCell(new Phrase(""));
            date.setPhrase(new Phrase("",FontFactory.getFont(FontFactory.TIMES_BOLD,12,BaseColor.BLACK)));
            date.setBorder(Rectangle.NO_BORDER);

            invoice.setHorizontalAlignment(Element.ALIGN_RIGHT);
            date.setHorizontalAlignment(Element.ALIGN_RIGHT);

            PdfPCell e1 = new PdfPCell(new Phrase(""));
            e1.setPhrase(new Phrase("",FontFactory.getFont(FontFactory.TIMES_BOLD,12,BaseColor.BLACK)));
            e1.setBorder(Rectangle.NO_BORDER);
            PdfPCell e2 = new PdfPCell(new Phrase(""));
            e2.setBorder(Rectangle.NO_BORDER);
            PdfPCell invoiceNum = new PdfPCell(new Phrase("Invoice No: "+1+"/23-24",FontFactory.getFont(FontFactory.TIMES_BOLD,12,BaseColor.BLACK)));
            invoiceNum.setHorizontalAlignment(Element.ALIGN_RIGHT);
            invoiceNum.setBorder(Rectangle.NO_BORDER);



            PdfPCell d = new PdfPCell(new Phrase("Date: "+"fdate"));
            d.setBorder(Rectangle.NO_BORDER);
            d.setHorizontalAlignment(Element.ALIGN_RIGHT);


            invoice.setColspan(1);
            details.addCell(det);
            details.addCell(invoice);
            details.addCell(invoiceNum);
            details.addCell(abi);
            details.addCell(date);
            details.addCell(d);
            details.addCell(adr);
            details.addCell(e1);
            details.addCell(e2);

            document.add(details);
            document.add(new Paragraph(" "));
            document.add(new LineSeparator());

            PdfPTable tab = new PdfPTable(8);
            tab.setWidthPercentage(105);
            PdfPCell sn = new PdfPCell(new Phrase("SNO",FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
            sn.setBorder(Rectangle.NO_BORDER);
            sn.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell item = new PdfPCell(new Phrase("Product Name",FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
            item.setBorder(Rectangle.NO_BORDER);
            item.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell hsn = new PdfPCell(new Phrase("HSN/SAC",FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
            hsn.setBorder(Rectangle.NO_BORDER);
            hsn.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell qty = new PdfPCell(new Phrase("Quantity",FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
            qty.setBorder(Rectangle.NO_BORDER);
            qty.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell rate = new PdfPCell(new Phrase("Rate",FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
            rate.setBorder(Rectangle.NO_BORDER);
            rate.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell sgst = new PdfPCell(new Phrase("SGST",FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
            sgst.setBorder(Rectangle.NO_BORDER);
            sgst.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell cgst = new PdfPCell(new Phrase("CGST",FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
            cgst.setBorder(Rectangle.NO_BORDER);
            d.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell am = new PdfPCell(new Phrase("Amount",FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
            am.setBorder(Rectangle.NO_BORDER);
            am.setHorizontalAlignment(Element.ALIGN_CENTER);


            tab.addCell(sn);
            tab.addCell(item);
            tab.addCell(hsn);
            tab.addCell(qty);
            tab.addCell(rate);
            tab.addCell(sgst);
            tab.addCell(cgst);
            tab.addCell(am);


            document.add(tab);
            document.add(new LineSeparator());

            for (int i = 0; i < productClassList.size(); i++) {
                document.add(new Paragraph(""));
                ProductClass pClass = productClassList.get(i);

                String sno = pClass.getpCode();
                String pName = pClass.getpName();
                String pHsn = pClass.getpHsn();
                String pQuantity = pClass.getQuantity();
                String pRate = pClass.getpRate();
                String pcGst = pClass.getpCgst();
                String pSgst = pClass.getpSgst();
                String pAmount = pClass.getpAmount();
                PdfPTable tab2 = new PdfPTable(8);
                tab2.setWidthPercentage(105);
                PdfPCell sn1 = new PdfPCell(new Phrase(sno,FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
                sn1.setBorder(Rectangle.NO_BORDER);
                sn1.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell item1 = new PdfPCell(new Phrase(pName,FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
                item1.setBorder(Rectangle.NO_BORDER);
                item1.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell hsn1 = new PdfPCell(new Phrase(pHsn,FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
                hsn1.setBorder(Rectangle.NO_BORDER);
                hsn1.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell qty1 = new PdfPCell(new Phrase(pQuantity,FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
                qty1.setBorder(Rectangle.NO_BORDER);
                qty1.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell rate1 = new PdfPCell(new Phrase(pRate,FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
                rate1.setBorder(Rectangle.NO_BORDER);
                rate1.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell sgst1 = new PdfPCell(new Phrase(pSgst+"\n  2.5%",FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
                sgst1.setBorder(Rectangle.NO_BORDER);
                sgst1.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell cgst1 = new PdfPCell(new Phrase(pcGst+"\n 2.5%",FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
                cgst1.setBorder(Rectangle.NO_BORDER);
                d.setHorizontalAlignment(Element.ALIGN_CENTER);

                PdfPCell am1 = new PdfPCell(new Phrase(pAmount,FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
                am1.setBorder(Rectangle.NO_BORDER);

                am1.setHorizontalAlignment(Element.ALIGN_CENTER);


                tab2.addCell(sn1);
                tab2.addCell(item1);
                tab2.addCell(hsn1);
                tab2.addCell(qty1);
                tab2.addCell(rate1);
                tab2.addCell(sgst1);
                tab2.addCell(cgst1);
                tab2.addCell(am1);


                document.add(tab2);
                document.add(new Paragraph(""));
                document.add(new LineSeparator());
            }

            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));


            document.add(new LineSeparator());
            document.add(new Paragraph(" "));
            PdfPTable detail = new PdfPTable(8);

            detail.setWidthPercentage(100);
            PdfPCell bd = new PdfPCell(new Phrase("Bank Details",FontFactory.getFont(FontFactory.TIMES_BOLD,12,BaseColor.BLACK)));
            bd.setHorizontalAlignment(Element.ALIGN_LEFT);
            bd.setColspan(2);
            bd.setBorder(Rectangle.NO_BORDER);
            PdfPCell bd1 = new PdfPCell(new Phrase("BANK: CANARA BANK	\r\n"
                    + "A/c #:	1046201012060    	\r\n"
                    + "IFSC 	CNRB0001046	\r\n",FontFactory.getFont(FontFactory.TIMES_ROMAN,11,BaseColor.BLACK)));
            bd1.setHorizontalAlignment(Element.ALIGN_LEFT);
            bd1.setColspan(2);
            bd1.setBorder(Rectangle.NO_BORDER);
            PdfPCell tot = new PdfPCell(new Phrase("Total Before Tax",FontFactory.getFont(FontFactory.TIMES_BOLD,12,BaseColor.BLACK)));
            tot.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tot.setColspan(2);
            tot.setBorder(Rectangle.NO_BORDER);


            PdfPCell gst2 = new PdfPCell(new Phrase("Total Tax",FontFactory.getFont(FontFactory.TIMES_BOLD,12,BaseColor.BLACK)));
            gst2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            gst2.setColspan(2);
            gst2.setBorder(Rectangle.NO_BORDER);

            PdfPCell gtotal = new PdfPCell(new Phrase("Grand Total",FontFactory.getFont(FontFactory.TIMES_BOLD,12,BaseColor.BLACK)));
            gtotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            gtotal.setColspan(2);
            gtotal.setBorderColor(BaseColor.DARK_GRAY);
            gtotal.setBorder(Rectangle.NO_BORDER);

            PdfPCell totalval = new PdfPCell(new Phrase(total,FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
            totalval.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalval.setColspan(2);
            totalval.setBorder(Rectangle.NO_BORDER);

            PdfPCell gstval = new PdfPCell(new Phrase(gstTotal,FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
            gstval.setHorizontalAlignment(Element.ALIGN_RIGHT);
            gstval.setColspan(2);
            gstval.setBorder(Rectangle.NO_BORDER);

            PdfPCell fullval = new PdfPCell(new Phrase(amount,FontFactory.getFont(FontFactory.TIMES_ROMAN,12,BaseColor.BLACK)));
            fullval.setHorizontalAlignment(Element.ALIGN_RIGHT);
            fullval.setColspan(2);
            fullval.setBorder(Rectangle.NO_BORDER);



            detail.addCell(bd);
            detail.addCell(e1);
            detail.addCell(e1);
            detail.addCell(e1);
            detail.addCell(tot);
            detail.addCell(totalval);
            detail.addCell(bd1);

            detail.addCell(e1);
            detail.addCell(e1);
            detail.addCell(e1);
            detail.addCell(gst2);
            detail.addCell(gstval);


            detail.addCell(e1);
            detail.addCell(e1);
            detail.addCell(e1);
            detail.addCell(gtotal);
            detail.addCell(fullval);


            document.add(detail);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new LineSeparator());


            Paragraph sig = new Paragraph("Authorized Signature",FontFactory.getFont(FontFactory.TIMES_ROMAN,15,BaseColor.BLACK));
            document.add(new Paragraph(""));
            document.add(sig);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("____________________________"));

            document.close();

            toastMessage("Pdf Created Successfully.");

        } catch (Exception e) {
            Log.d("PdfView", e.getMessage());

            toastMessage(e.getMessage());
        }
    }

    private  void productDetails(){
        productName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String product  = parent.getItemAtPosition(position).toString();
                productReference.orderByChild("pName").equalTo(product).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            pno = dataSnapshot.child("pHsn").getValue(String.class);
                            sGst = dataSnapshot.child("pSgst").getValue(String.class);
                            cGst = dataSnapshot.child("pCgst").getValue(String.class);
                        }
                        quantity.setText(pno);
                        sgst1.setText(sGst);
                        cgst1.setText(cGst);

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



    private void loadCompanyData() {
        customerReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    labels.add(dataSnapshot.child("partyName").getValue().toString());
                }
                dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadProductData(){

        product = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,product);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        productName.setAdapter(adapter);






        productReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot products:snapshot.getChildren()){
                    product.add(products.child("pName").getValue().toString());

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "You selected: " + label,
                Toast.LENGTH_LONG).show();

    }



    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
    public  void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


}