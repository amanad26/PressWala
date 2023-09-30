package com.abmtech.presswala.Aapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abmtech.presswala.databinding.ItemCalculationLayBinding;

import java.util.ArrayList;
import java.util.Map;

public class CalculationAdapter extends RecyclerView.Adapter<CalculationAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<Map<String, Object>> data;
    private final String total;

    public CalculationAdapter(Context context, ArrayList<Map<String, Object>> data, String total) {
        this.context = context;
        this.data = data;
        this.total = total;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemCalculationLayBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position > data.size() - 1) {
            holder.binding.textName.setText("\t\tTotal");
            holder.binding.textCalculate.setText("₹ " + total + "\t\t");
        } else {
            Map<String, Object> current = data.get(holder.getAdapterPosition());

            holder.binding.textName.setText(String.valueOf(current.get("productName")));

            String calculation = current.get("count") + " X " + "₹ " + current.get("productPrice") + " - " + "₹ " + current.get("totalPrice");

            holder.binding.textCalculate.setText(calculation);
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemCalculationLayBinding binding;

        public ViewHolder(@NonNull ItemCalculationLayBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
