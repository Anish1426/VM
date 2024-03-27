package com.example.vm.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vm.Classes.SalesReportClass;
import com.example.vm.R;

import java.util.List;

public class SalesReportAdapter extends BaseAdapter {
    TextView invoiceDate,invoiceNumber,partyName,customerGst,productName,hsnCode,
    quantity,rate,sgstPercentage,cgstPercentage,sGst,cGst,amount;

    private Activity mContext;

    List<SalesReportClass> salesReportClassList;

    public SalesReportAdapter(Activity mContext, List<SalesReportClass> salesReportClassList) {
        this.mContext = mContext;
        this.salesReportClassList = salesReportClassList;
    }

    @Override
    public int getCount() {
        return salesReportClassList.size();
    }

    @Override
    public Object getItem(int position) {
        return salesReportClassList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View view = inflater.inflate(R.layout.list_sales_report,null,true);

        invoiceDate = view.findViewById(R.id.invoiceDate);
        invoiceNumber= view.findViewById(R.id.invoiceNumber);
        partyName=view.findViewById(R.id.partyName);
        customerGst=view.findViewById(R.id.customerGst);
        productName=view.findViewById(R.id.productName);
        hsnCode=view.findViewById(R.id.hsnCode);
        quantity=view.findViewById(R.id.quantity);
        rate=view.findViewById(R.id.rate);
        sgstPercentage=view.findViewById(R.id.sgstPercentage);
        cgstPercentage=view.findViewById(R.id.cgstPercentage);
        sGst=view.findViewById(R.id.sGst);
        cGst=view.findViewById(R.id.cGst);
        amount=view.findViewById(R.id.amount);

        SalesReportClass salesReportClass = salesReportClassList.get(position);

        invoiceDate.setText(salesReportClass.getInvoiceDate());
        invoiceNumber.setText(salesReportClass.getInvoiceNumber());
        partyName.setText(salesReportClass.getPartyName());
        customerGst.setText(salesReportClass.getCustomerGst());
        productName.setText(salesReportClass.getProductName());
        hsnCode.setText(salesReportClass.getHsnCode());
        quantity.setText(salesReportClass.getQuantity());
        rate.setText(salesReportClass.getRate());
        sgstPercentage.setText(salesReportClass.getSgstPercentage());
        cgstPercentage.setText(salesReportClass.getCgstPercentage());
        sGst.setText(salesReportClass.getsGst());
        cGst.setText(salesReportClass.getcGst());
        amount.setText(salesReportClass.getAmount());
        return view;
    }
}
