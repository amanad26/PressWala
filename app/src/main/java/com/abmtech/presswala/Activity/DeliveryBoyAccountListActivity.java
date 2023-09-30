package com.abmtech.presswala.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.abmtech.presswala.Aapters.DeliveryBoyAccountAdapter;
import com.abmtech.presswala.Apis.OnDeliveryBoySelected;
import com.abmtech.presswala.Models.UsersModel;
import com.abmtech.presswala.R;
import com.abmtech.presswala.databinding.ActivityDeliveryBoyAccountListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveryBoyAccountListActivity extends AppCompatActivity implements OnDeliveryBoySelected {

    DeliveryBoyAccountListActivity activity;
    ActivityDeliveryBoyAccountListBinding binding;
    FirebaseDatabase database;
    List<UsersModel> deliveryBoyAccountList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliveryBoyAccountListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;

        database = FirebaseDatabase.getInstance();
        binding.addBtn.setOnClickListener(v -> startActivity(new Intent(activity, DeliveryBoySignupActivity.class)));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        binding.header.setOnClickListener(view -> onBackPressed());

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAccountsList();
    }

    private void getAccountsList() {

        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                deliveryBoyAccountList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UsersModel usersModel = snapshot1.getValue(UsersModel.class);
                    assert usersModel != null;
                    if (usersModel.getUserType().equalsIgnoreCase("delivery_boy")) {
                        deliveryBoyAccountList.add(usersModel);
                    }
                }

                binding.progressBar.setVisibility(View.GONE);
                binding.recyclerView.setAdapter(new DeliveryBoyAccountAdapter(activity, deliveryBoyAccountList, 0, DeliveryBoyAccountListActivity.this));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onSelected(UsersModel data) {

    }

    @Override
    public void onRemove() {

    }
}