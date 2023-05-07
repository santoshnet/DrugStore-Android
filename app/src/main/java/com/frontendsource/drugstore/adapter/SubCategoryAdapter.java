package com.frontendsource.drugstore.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.frontendsource.drugstore.R;
import com.frontendsource.drugstore.activity.ProductActivity;
import com.frontendsource.drugstore.api.clients.RestClient;
import com.frontendsource.drugstore.interfaces.CategorySelectCallBacks;
import com.frontendsource.drugstore.model.Category;
import com.frontendsource.drugstore.model.SubCategory;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Drug App
 * https://github.com/quintuslabs/GroceryStore
 * Created on 18-Feb-2019.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */
public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

    List<SubCategory> categoryList;
    Activity context;
    int selectedPosition = 0;

    public SubCategoryAdapter(List<SubCategory> categoryList, Activity context ) {
        this.categoryList = categoryList;
        this.context = context;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_sub_category, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final SubCategory category = categoryList.get(position);
        holder.title.setText(category.getSub_category_title());

        if(category.getSub_category_img()!=null){
            Picasso.get()
                    .load(RestClient.BASE_URL+ category.getSub_category_img())
                    .into(holder.imageView);
        }




        holder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("category", "subCategory");
                intent.putExtra("title", category.getSub_category_title());
                intent.putExtra("category_id", category.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        LinearLayout ll;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.category_image);
            title = itemView.findViewById(R.id.category_title);
            ll = itemView.findViewById(R.id.category_item_ll);
        }
    }
}
