package com.example.vm.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vm.Classes.ProductClass;
import com.example.vm.Purchase_entry;
import com.example.vm.R;

import java.util.List;

public class ProductAdapter extends BaseAdapter {

    private Activity mContext;
    List<ProductClass> productClassList;

    public ProductAdapter(Activity mContext, List<ProductClass> productClassList) {
        this.mContext = mContext;
        this.productClassList = productClassList;
    }


    @Override
    public int getCount() {
        return productClassList.size();
    }

    @Override
    public Object getItem(int position) {
        return productClassList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater =mContext.getLayoutInflater();
        View view =inflater.inflate(R.layout.list_product,null,true);

        TextView sno =view.findViewById(R.id.sno);
        TextView name = view.findViewById(R.id.name);
        TextView hsn = view.findViewById(R.id.hsn);
        TextView quantity = view.findViewById(R.id.quantity);
        TextView rate = view.findViewById(R.id.rate);
        TextView sgst = view.findViewById(R.id.sgst);
        TextView cgst = view.findViewById(R.id.cgst);
        TextView amount = view.findViewById(R.id.amount);

        ProductClass productClass = productClassList.get(position);
        sno.setText(String.valueOf(position+1));
        name.setText(productClass.getpName());
        hsn.setText(productClass.getpHsn());
        quantity.setText(productClass.getQuantity());
        rate.setText(productClass.getpRate());
        sgst.setText(productClass.getpSgst());
        cgst.setText(productClass.getpCgst());
        amount.setText(productClass.getpAmount());

        return view;
    }
}
