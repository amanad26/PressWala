package com.abmtech.presswala.Aapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abmtech.presswala.Models.ProductModel;
import com.abmtech.presswala.R;
import com.abmtech.presswala.Utills.ProgressDialog;
import com.abmtech.presswala.databinding.MyProductLayoutBinding;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soup.neumorphism.NeumorphCardView;

public class MyProductAdapter extends RecyclerView.Adapter<MyProductAdapter.ViewHolder> {

    private final Context context;
    private final List<ProductModel> models;
    private final FirebaseDatabase database;

    public MyProductAdapter(Context context, List<ProductModel> models) {
        this.context = context;
        this.models = models;
        database = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.my_product_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = models.get(position).getProductName() + " (" + models.get(position).getProductType() + ")";

        holder.binding.productName.setText(name);
        holder.binding.productPrice.setText("₹ " + models.get(position).getProductPrice());

        holder.binding.delete.setOnClickListener(view -> {
            String id = models.get(position).getProductId();
            notifyItemRemoved(position);
            notifyItemRangeChanged(0, models.size() - 1);
            models.remove(position);
            deleteProduct(id);
        });


        holder.binding.edit.setOnClickListener(view -> editProduct(models.get(position).getProductPrice(), models.get(position).getProductId()));
    }

    private void deleteProduct(String id) {
        database.getReference().child("products").child(id).removeValue().addOnSuccessListener(unused -> Log.e("TAG", "onSuccess() called with: Deleted.. = [" + unused + "]"));


    }

    private void editProduct(String price, String id) {
        RoundedBottomSheetDialog mBottomSheetDialog = new RoundedBottomSheetDialog(context);
        View sheetView = mBottomSheetDialog.getLayoutInflater().inflate(R.layout.edit_product_layout, null);

        mBottomSheetDialog.setContentView(sheetView);

        NeumorphCardView save = mBottomSheetDialog.findViewById(R.id.card_save);
        EditText priceEdit = mBottomSheetDialog.findViewById(R.id.price_edit);

        if (priceEdit != null)
            priceEdit.setText(price);

        if (save != null)
            save.setOnClickListener(view -> {
                ProgressDialog pd = new ProgressDialog(context);
                pd.show();
                Map<String, Object> map = new HashMap<>();
                if (priceEdit != null)
                    map.put("productPrice", priceEdit.getText().toString());

                database.getReference().child("products").child(id).updateChildren(map).addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Updated..!", Toast.LENGTH_SHORT).show();
                    mBottomSheetDialog.dismiss();
                    pd.dismiss();
                });
            });

        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
    }


    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public MyProductLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.binding = MyProductLayoutBinding.bind(itemView);
        }
    }
}
