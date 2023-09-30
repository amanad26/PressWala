package com.abmtech.presswala.Aapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.abmtech.presswala.Models.SingleOrderModel;
import com.abmtech.presswala.R;
import com.abmtech.presswala.databinding.MyProductLayoutBinding;

import java.util.List;

public class SingleOrderDetailsAdapter extends RecyclerView.Adapter<SingleOrderDetailsAdapter.ViewHolder> {


    Context context;
    List<SingleOrderModel.SingleOrderData.SingleOrderItems> models;


    public SingleOrderDetailsAdapter(Context context, List<SingleOrderModel.SingleOrderData.SingleOrderItems> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.my_product_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.edit.setVisibility(View.GONE);
        holder.binding.productName.setText(models.get(position).getProName());
        holder.binding.productPrice.setText("Price " + models.get(position).getProPrice());
        int count = Integer.parseInt(models.get(position).getProQuantity());
        int price = Integer.parseInt(models.get(position).getProPrice());
        int total = count * price;

        holder.binding.delete.setVisibility(View.GONE);

        holder.binding.totalPrice.setText("Q. " + String.valueOf(count) + " * " + String.valueOf(price) + " = " + String.valueOf(total));

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MyProductLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = MyProductLayoutBinding.bind(itemView);
        }
    }
}
