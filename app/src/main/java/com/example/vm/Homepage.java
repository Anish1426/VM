package com.example.vm;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vm.Classes.AddCustomers;
import com.example.vm.Classes.ProductClass;
import com.example.vm.Classes.PurchaseClass;
import com.example.vm.Classes.TransportClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Homepage extends AppCompatActivity {
    Button transport,product,pcancel,psubmit,sell_cancel,sell_submit,addSeller,addCustomer,pur_down,purchase,
            cusCancel,cusSubmit;
    Dialog dialog;


    EditText tpcode,tpname,tpvechile,tpcontact,pcode,pname,phsn,prate,pcgst,psgst
            ,sell_gst,sell_name,sell_address,sell_state,sell_contact
            ,cusGst,cusName,cusAddress,cusState,cusContact;

    DatabaseReference reference,productReference,addSellerReference,addCustomerReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        transport = findViewById(R.id.transport);
        product=findViewById(R.id.addpro);
        addSeller=findViewById(R.id.addSeller);
        addCustomer=findViewById(R.id.addCustomer);
        pur_down= findViewById(R.id.pur_down);
        purchase = findViewById(R.id.purchase_entry);
        reference = FirebaseDatabase.getInstance().getReference().child("transport");

        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Homepage.this);
                View view = getLayoutInflater().inflate(R.layout.add_customer,null);

                addCustomerReference = FirebaseDatabase.getInstance().getReference().child("addCustomer");

                cusGst= view.findViewById(R.id.cusGst);
                cusName=view.findViewById(R.id.cusName);
                cusAddress=view.findViewById(R.id.cusAddress);
                cusState=view.findViewById(R.id.cusState);
                cusContact=view.findViewById(R.id.cusContact);
                cusCancel = view.findViewById(R.id.cusCancel);
                cusSubmit=view.findViewById(R.id.cusSubmit);

                cusCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                cusSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String gst = cusGst.getText().toString();
                        String name = cusName.getText().toString();
                        String address = cusAddress.getText().toString();
                        String state = cusAddress.getText().toString();
                        String contact = cusContact.getText().toString();



                        addCustomerReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                AddCustomers addCustomers = new AddCustomers(gst,name,address,state,contact);
                                long count = snapshot.getChildrenCount();
                                String id = String.valueOf(count+1);
                                addCustomerReference.child(id).setValue(addCustomers)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                toastMessage("customer Added");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                toastMessage("customer Adding Failed");
                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

                builder.setView(view);
                dialog = builder.create();
                dialog.show();

            }
        });
        transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Homepage.this);

                View view = getLayoutInflater().inflate(R.layout.activity_add_transport, null);
                tpcode = view.findViewById(R.id.tpcode);
                tpname = view.findViewById(R.id.tpname);
                tpvechile = view.findViewById(R.id.tpvehicle);
                tpcontact = view.findViewById(R.id.tpcontact);
                Button btnsubmit = view.findViewById(R.id.transsubmit);
                Button btncancel = view.findViewById(R.id.cancel);

                sqlconnection db = new sqlconnection(Homepage.this);

                btnsubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String trid = tpcode.getText().toString();
                        String tname = tpname.getText().toString();
                        String tnum = tpvechile.getText().toString();
                        String tcon = tpcontact.getText().toString();
                        if(trid.equals("") || tname.equals("") || tnum.equals("") || tcon.equals("")){
                            Toast.makeText(Homepage.this,"Error!! Enter the details..",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Boolean insert = db.insertData(trid, tname, tnum, tcon);
                            TransportClass transportClass = new TransportClass(trid,tname,tnum,tcon);
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long count =snapshot.getChildrenCount();
                                    String id =String.valueOf(count+1);
                                    reference.child(id).setValue(transportClass)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(Homepage.this, "Success", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Homepage.this, "Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            {
                                if (insert == true) {
                                    Toast.makeText(Homepage.this, "Success", Toast.LENGTH_SHORT).show();
                                    tpcode.setText("");
                                    tpname.setText("");
                                    tpvechile.setText("");
                                    tpcontact.setText("");
                                } else {
                                    Toast.makeText(Homepage.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });

                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                builder.setView(view);
                dialog = builder.create();
                dialog.show();
            }
        });
        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Homepage.this);
                View view = getLayoutInflater().inflate(R.layout.add_products, null);

                pcode = view.findViewById(R.id.pcode);
                pname = view.findViewById(R.id.pname);
                phsn = view.findViewById(R.id.hsn);
                prate = view.findViewById(R.id.prate);
                pcgst=view.findViewById(R.id.pcgst);
                psgst = view.findViewById(R.id.psgst);
                pcancel= view.findViewById(R.id.pcancel);
                psubmit = view.findViewById(R.id.psubmit);
                sqlconnection db = new sqlconnection(Homepage.this);
                productReference = FirebaseDatabase.getInstance().getReference().child("product");

                pcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                psubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String proid=pcode.getText().toString();
                        String prname=pname.getText().toString();
                        String prhsn=phsn.getText().toString();
                        String prrate=prate.getText().toString();
                        String prcgst=pcgst.getText().toString();
                        String prsgst=psgst.getText().toString();
                        if(proid.equals("") || prname.equals("") || prhsn.equals("") || prrate.equals("") || prcgst.equals("") || prsgst.equals("")){
                            Toast.makeText(Homepage.this,"Error!! Enter the detail.!!",Toast.LENGTH_SHORT).show();
                        }else {
                            ProductClass productClass = new ProductClass(proid,prname,prhsn,prrate,prcgst,prsgst);
                            productReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long count = snapshot.getChildrenCount();
                                    String id = String.valueOf(count+1);
                                    productReference.child(id).setValue(productClass)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(Homepage.this, "Inserted", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Homepage.this, "Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            Boolean insert = db.insertp_data(proid, prname, prhsn, prrate, prcgst, prsgst);
                            if (insert == true) {
                                Toast.makeText(Homepage.this, "Inserted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Homepage.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.setView(view);
                dialog = builder.create();
                dialog.show();

            }
        });
        addSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Homepage.this);

                View view = getLayoutInflater().inflate(R.layout.add_seller,null);

                addSellerReference = FirebaseDatabase.getInstance().getReference().child("addSeller");
                sell_gst = view.findViewById(R.id.sell_gst);
                sell_name=view.findViewById(R.id.sell_name);
                sell_address=view.findViewById(R.id.sell_address);
                sell_state  = view.findViewById(R.id.sell_state);
                sell_contact=view.findViewById(R.id.sell_contact);

                sell_cancel=view.findViewById(R.id.sell_cancel);
                sell_submit = view.findViewById(R.id.sell_submit);
                sqlconnection db = new sqlconnection(Homepage.this);

                sell_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                sell_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String gst = sell_gst.getText().toString();
                        String name = sell_name.getText().toString();
                        String address = sell_address.getText().toString();
                        String state = sell_state.getText().toString();
                        String contact =sell_contact.getText().toString();

                        if(gst.equals("")||address.equals("")||state.equals("")||name.equals("")||contact.equals("")){
                            Toast.makeText(Homepage.this,"Error!! Enter the details..",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            addSellerReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    long count = snapshot.getChildrenCount();
                                    String id = String.valueOf(count+1);
                                    PurchaseClass sellerClass = new PurchaseClass(gst,name,address,state,contact);
                                    addSellerReference.child(id).setValue(sellerClass)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(Homepage.this,"Seller Added Successfully",Toast.LENGTH_SHORT).show();

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(Homepage.this,"Seller Added Failed",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }
                });

                builder.setView(view);
                dialog =builder.create();
                dialog.show();
            }
        });

        pur_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Homepage.this);

                View view = getLayoutInflater().inflate(R.layout.purchase_download,null);


                builder.setView(view);
                dialog = builder.create();
                dialog.show();

            }
        });
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this,Purchase_entry.class);
                startActivity(intent);
            }
        });

    }

    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


    public void generateBill(View view) {
        Intent intent = new Intent(Homepage.this,activity_sales_entry.class);
        startActivity(intent);
    }
}
