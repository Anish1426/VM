package com.example.vm.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vm.Classes.ProductClass;
import com.example.vm.Classes.SalesClass;
import com.example.vm.R;

import java.util.List;

public class SalesAdapter extends BaseAdapter {

    private Activity mContext;
    List<SalesClass> salesClassList;

    public SalesAdapter(Activity mContext, List<SalesClass> salesClassList) {
        this.mContext = mContext;
        this.salesClassList = salesClassList;
    }

    @Override
    public int getCount() {
        return salesClassList.size();
    }

    @Override
    public Object getItem(int position) {
        return salesClassList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =mContext.getLayoutInflater();
        View view =inflater.inflate(R.layout.list_sales,null,true);

        TextView sno =view.findViewById(R.id.sno);
        TextView name = view.findViewById(R.id.name);
        TextView hsn = view.findViewById(R.id.hsn);
        TextView quantity = view.findViewById(R.id.quantity);
        TextView rate = view.findViewById(R.id.rate);
        TextView sgst = view.findViewById(R.id.sgst);
        TextView cgst = view.findViewById(R.id.cgst);
        TextView amount = view.findViewById(R.id.amount);

        SalesClass salesClass = salesClassList.get(position);
        sno.setText(String.valueOf(position+1));
        name.setText(salesClass.getpName());
        hsn.setText(salesClass.getpHsn());
        quantity.setText(salesClass.getQuantity());
        rate.setText(salesClass.getpRate());
        sgst.setText(salesClass.getpSgst());
        cgst.setText(salesClass.getpCgst());
        amount.setText(salesClass.getpAmount());

        return view;
    }
}
