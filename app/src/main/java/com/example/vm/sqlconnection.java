package com.example.vm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class sqlconnection extends SQLiteOpenHelper {
    public sqlconnection(@Nullable Context context) {
        super(context, "sqlite.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table add_transport(trans_id int,trans_name varchar(20), trans_num varchar(20),trans_con varchar(29) )");
        db.execSQL("create table add_product(proid int,pname varchar(20),phsn int,prate int,pcgst int ,psgst int)");
        db.execSQL("create table add_sell(sell_gst varchar(20),sell_name varchar(20),sell_address varchar(20),sell_state varchar(20),sell_contact varchar(20))");
        db.execSQL("create table add_customer(sell_gst varchar(20),sell_name varchar(20),sell_address varchar(20),sell_state varchar(20),sell_contact varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists add_transport");
        db.execSQL("drop table if exists add_product");
        db.execSQL("drop table if exists add_sell");
        db.execSQL("drop table if exists add_customer");
    }

    public Boolean insertData(String trans_id ,String trans_name,String trans_num,String trans_con ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("trans_id",trans_id);
        contentValues.put("trans_name",trans_name);
        contentValues.put("trans_num",trans_num);
        contentValues.put("trans_con",trans_con);
        long result = db.insert("add_transport",null,contentValues);
        if(result==-1){
            return false;
        }
        else{
            return true;
        }
    }

    public  Boolean insertp_data(String proid,String pname,String phsn,String prate,String pcgst, String psgst){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("proid",proid);
        contentValues.put("pname",pname);
        contentValues.put("phsn",phsn);
        contentValues.put("prate",prate);
        contentValues.put("pcgst",pcgst);
        contentValues.put("psgst",psgst);
        long result = db.insert("add_product",null,contentValues);
        if(result==-1){
            return false;
        }
        else{
            return true;
        }
    }

    public  Boolean insert_sell(String sell_gst, String sell_name, String sell_address,String sell_state,String sell_contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sell_gst",sell_gst);
        contentValues.put("sell_name",sell_name);
        contentValues.put("sell_address",sell_address);
        contentValues.put("sell_state",sell_state);
        contentValues.put("sell_contact",sell_contact);
        long result = db.insert("add_sell",null,contentValues);
        if(result == -1){
            return false;
        }
        else{
            return  true;
        }


    }

    public  Boolean insert_customer(String sell_gst, String sell_name, String sell_address,String sell_state,String sell_contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sell_gst",sell_gst);
        contentValues.put("sell_name",sell_name);
        contentValues.put("sell_address",sell_address);
        contentValues.put("sell_state",sell_state);
        contentValues.put("sell_contact",sell_contact);
        long result = db.insert("add_customer",null,contentValues);
        if(result == -1){
            return false;
        }
        else{
            return  true;
        }


    }

    public List<String> getAllLabels(){
        List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM  add_sell";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }

    public List<String> getAllProduct(){
        List<String> list = new ArrayList<>();

        String query= "SELECT * FROM add_product";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do {
                list.add(cursor.getString(1));
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}