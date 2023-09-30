package com.abmtech.presswala.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.abmtech.presswala.Aapters.MyProductAdapter;
import com.abmtech.presswala.Models.ProductModel;
import com.abmtech.presswala.databinding.ActivityMyProductBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyProductActivity extends AppCompatActivity {
    private ActivityMyProductBinding binding;
    private MyProductActivity activity;
    private FirebaseDatabase database;
    private List<ProductModel> productModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = this;

        database = FirebaseDatabase.getInstance();

        binding.back.setOnClickListener(view -> onBackPressed());

        binding.addProduct.setOnClickListener(view -> startActivity(new Intent(activity, AddProductActivity.class)));
    }

    private void getMyProduct() {
        database.getReference().child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productModelList.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    ProductModel model = snapshot1.getValue(ProductModel.class);
                    productModelList.add(model);
                }

                binding.myProductRecycler.setLayoutManager(new LinearLayoutManager(activity));
                binding.myProductRecycler.setAdapter(new MyProductAdapter(activity, productModelList));
                binding.progress.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyProduct();
    }
}