package com.abmtech.presswala.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.abmtech.presswala.Aapters.DeliveryBoyAccountAdapter;
import com.abmtech.presswala.Apis.OnDeliveryBoySelected;
import com.abmtech.presswala.Apis.RetrofitClient;
import com.abmtech.presswala.Models.UpdateModel;
import com.abmtech.presswala.Models.UsersModel;
import com.abmtech.presswala.R;
import com.abmtech.presswala.Utills.ProgressDialog;
import com.abmtech.presswala.databinding.ActivityChooseDeliveryBoyBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseDeliveryBoyActivity extends AppCompatActivity implements OnDeliveryBoySelected {

    ActivityChooseDeliveryBoyBinding binding;
    ChooseDeliveryBoyActivity activity;
    FirebaseDatabase database;
    List<UsersModel> deliveryBoyAccountList = new ArrayList<>();
    public static UsersModel userData = null;
    String order_id = "", id = "";
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseDeliveryBoyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = this;
        database = FirebaseDatabase.getInstance();
        pd = new ProgressDialog(activity);

        order_id = getIntent().getStringExtra("order_id");
        id = getIntent().getStringExtra("id");


        binding.doneBtn.setOnClickListener(view -> {
            if (userData != null) {
                Log.e("TAG", "onClick: data " + userData.toString());
                assignDeliveryBoy();
            } else {
                Toast.makeText(activity, "Select Delivery Boy ", Toast.LENGTH_SHORT).show();
            }
        });

        binding.backBtn.setOnClickListener(view -> onBackPressed());

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
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UsersModel usersModel = snapshot1.getValue(UsersModel.class);
                    assert usersModel != null;
                    if (usersModel.getUserType().equalsIgnoreCase("delivery_boy")) {
                        deliveryBoyAccountList.add(usersModel);
                    }

                }

                binding.progress.setVisibility(View.GONE);
                binding.recycler.setLayoutManager(new LinearLayoutManager(activity));
                binding.recycler.setAdapter(new DeliveryBoyAccountAdapter(activity, deliveryBoyAccountList, 1, ChooseDeliveryBoyActivity.this));

                Log.e("TAG", "onDataChange: Data Size " + deliveryBoyAccountList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void assignDeliveryBoy() {

        pd.show();
        RetrofitClient.getClient(activity).assignDeliveryBoy(
                order_id,
                userData.getUserName(),
                userData.getUserId(),
                userData.getUserPhone()
        ).enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(@NonNull Call<UpdateModel> call, @NonNull Response<UpdateModel> response) {
                //pd.dismiss();
                if (response.code() == 200)
                    if (response.body() != null)
                        if (response.body().getResult().equalsIgnoreCase("true")) {
                            Toast.makeText(activity, "Delivery Boy Assigned..", Toast.LENGTH_SHORT).show();
                            cancelOrder(id, "assign_to_delivery_boy", "");
                        } else {
                            Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                        }
            }

            @Override
            public void onFailure(@NonNull Call<UpdateModel> call, @NonNull Throwable t) {
                pd.dismiss();
            }
        });

    }

    private void cancelOrder(String id, String status, String desc) {
        pd.show();
        RetrofitClient.getClient(activity).updateStatus(id, status, desc).enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(@NonNull Call<UpdateModel> call, @NonNull Response<UpdateModel> response) {
                pd.dismiss();
                if (response.code() == 200)
                    if (response.body() != null)
                        if (response.body().getResult().equalsIgnoreCase("true")) {
                            startActivity(new Intent(activity, AdminHomeActivity.class));
                            finish();
                        }

            }

            @Override
            public void onFailure(@NonNull Call<UpdateModel> call, @NonNull Throwable t) {
                pd.dismiss();
                Log.e("TAG", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });

    }

    @Override
    public void onSelected(UsersModel data) {
        userData = data;
        binding.doneBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRemove() {
        userData = null;
        binding.doneBtn.setVisibility(View.GONE);
    }
}