package com.frontendsource.drugstore.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.frontendsource.drugstore.R;
import com.frontendsource.drugstore.api.clients.RestClient;
import com.frontendsource.drugstore.model.Bill;
import com.frontendsource.drugstore.model.Prescription;
import com.frontendsource.drugstore.util.localstorage.LocalStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Drug App
 * https://github.com/quintuslabs/GroceryStore
 * Created on 18-Feb-2019.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */
public class BillAdapter extends RecyclerView.Adapter<BillAdapter.MyViewHolder> {

    List<Bill> billList;
    Activity context;

    LocalStorage localStorage;

    public BillAdapter( List<Bill> billList, Activity context) {
        this.billList = billList;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;


        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_bill, parent, false);

        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        final Bill bill = billList.get(position);

        Picasso.get().load(RestClient.BASE_URL+bill.getBill()).into(holder.imageView);
        holder.prescription_title.setText(bill.getTitle());
        holder.name.setText(bill.getName());
        holder.email.setText(bill.getEmail());
        holder.mobile.setText(bill.getMobile());
        holder.amount.setText("Amount: "+bill.getAmount());
        holder.status.setText("status: "+bill.getPayment_status());
        holder.date.setText("Upload Date: "+bill.getCreated_date());
        if(bill.getPayment_status().equalsIgnoreCase("paid")){
            holder.view_receipt.setVisibility(View.VISIBLE);
        }else{
            holder.view_receipt.setVisibility(View.GONE);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(context,R.style.full_screen_dialog);
                dialog.setContentView(R.layout.product_image);
                ImageView imageView1 = dialog.findViewById(R.id.image_popup);
                Picasso.get().load(RestClient.BASE_URL+bill.getBill()).into(imageView1);
                dialog.show();
            }
        });
        holder.view_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(context,R.style.full_screen_dialog);
                dialog.setContentView(R.layout.product_image);
                ImageView imageView1 = dialog.findViewById(R.id.image_popup);
                Picasso.get().load(RestClient.BASE_URL+bill.getReceipt()).into(imageView1);
                dialog.show();
            }
        });

    }


    @Override
    public int getItemCount() {

        return billList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView prescription_title,name,email,mobile,amount,date,view_receipt,status;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            prescription_title = itemView.findViewById(R.id.prescription_title);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            mobile = itemView.findViewById(R.id.mobile);
            status = itemView.findViewById(R.id.status);
            date = itemView.findViewById(R.id.date_time);
            amount = itemView.findViewById(R.id.amount);
            view_receipt = itemView.findViewById(R.id.view_receipt);
            imageView = itemView.findViewById(R.id.imageView);


        }
    }
}
