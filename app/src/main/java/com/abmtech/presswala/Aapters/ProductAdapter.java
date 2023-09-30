package com.abmtech.presswala.Aapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abmtech.presswala.Models.ProductModel;
import com.abmtech.presswala.R;
import com.abmtech.presswala.Utills.ProductSelect;
import com.abmtech.presswala.databinding.AllProductLayoutBinding;
import com.abmtech.presswala.databinding.MyProductLayoutBinding;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private List<ProductModel> models;
    private FirebaseDatabase database;
    private ProductSelect select;

    public ProductAdapter(Context context, List<ProductModel> models, ProductSelect select) {
        this.context = context;
        this.models = models;
        database = FirebaseDatabase.getInstance();
        this.select = select;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.all_product_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.binding.productName.setText(models.get(position).getProductName());
        holder.binding.productPrice.setText("₹ " + models.get(position).getProductPrice());
        holder.binding.count.setText(models.get(position).getCount());

//        holder.binding.plus.setOnClickListener(view -> increaseCount(holder.binding.count.getText().toString(), holder.binding.count));
        holder.binding.plus.setOnClickListener(view -> select.increase(holder.getAdapterPosition()));

        holder.binding.minus.setOnClickListener(view -> select.decrease(holder.getAdapterPosition()));


    }

    private void increaseCount(String count, TextView textView) {
        int c = Integer.parseInt(count);
        c = c + 1;
        textView.setText(String.valueOf(c));
    }


    private void decreaseCount(String count, TextView textView) {
        int c = Integer.parseInt(count);
        if (c > 0) {
            c = c - 1;
            textView.setText(String.valueOf(c));
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        AllProductLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = AllProductLayoutBinding.bind(itemView);
        }
    }

}
