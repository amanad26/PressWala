package com.abmtech.presswala.Activity;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.abmtech.presswala.R;
import com.abmtech.presswala.Utills.ProgressDialog;
import com.abmtech.presswala.databinding.ActivityAddProductBinding;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {
    private ActivityAddProductBinding binding;
    private AddProductActivity activity;
    private FirebaseDatabase database;
    private ProgressDialog pd;
    private String type = "Dry Clean";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = this;
        database = FirebaseDatabase.getInstance();
        pd = new ProgressDialog(activity);

        binding.backBtn.setOnClickListener(view -> onBackPressed());

        binding.cardAdd.setOnClickListener(view -> {
            if (isValidated()) addProduct();
        });

        binding.dryClean.setOnClickListener(view -> {
            binding.dryClean.setBackgroundResource(R.drawable.selected_product);
            binding.dryClean.setTextColor(getResources().getColor(R.color.white));

            binding.press.setBackgroundResource(R.drawable.unseleted_product);
            binding.press.setTextColor(getResources().getColor(R.color.black));
            type = "Dry Clean";
        });

        binding.press.setOnClickListener(view -> {
            binding.dryClean.setBackgroundResource(R.drawable.unseleted_product);
            binding.dryClean.setTextColor(getResources().getColor(R.color.black));

            binding.press.setBackgroundResource(R.drawable.selected_product);
            binding.press.setTextColor(getResources().getColor(R.color.white));
            type = "Press";
        });
    }

    private void addProduct() {
        pd.show();
        String id = database.getReference().push().getKey();
        Map<String, Object> map = new HashMap<>();
        map.put("productName", binding.pNameEdit.getText().toString());
        map.put("productPrice", binding.priceEdit.getText().toString());
        map.put("productId", id);
        map.put("productType", type);

        database.getReference().child("products").child(id).setValue(map)
                .addOnSuccessListener(unused -> {
                    pd.dismiss();
                    Toast.makeText(activity, "Product Added..", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private boolean isValidated() {
        if (binding.pNameEdit.getText().toString().equalsIgnoreCase("")) {
            binding.pNameEdit.setError("Enter Product...!");
            binding.pNameEdit.requestFocus();
            return false;
        } else if (binding.priceEdit.getText().toString().equalsIgnoreCase("")) {
            binding.priceEdit.setError("Enter Product Price...!");
            binding.priceEdit.requestFocus();
            return false;
        } else {
            return true;
        }
    }
}