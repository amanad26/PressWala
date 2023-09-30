package com.abmtech.presswala.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.abmtech.presswala.Aapters.MyOrderAdapter;
import com.abmtech.presswala.Apis.ApiInterface;
import com.abmtech.presswala.Apis.RetrofitClient;
import com.abmtech.presswala.Models.MyOrderModel;
import com.abmtech.presswala.Models.OrderData;
import com.abmtech.presswala.Models.OrderModel;
import com.abmtech.presswala.Session.Session;
import com.abmtech.presswala.databinding.ActivityMyOrdersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrdersActivity extends AppCompatActivity {
    ActivityMyOrdersBinding binding;
    Activity activity;
    FirebaseDatabase database;
    Session session;
    List<OrderModel> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = this;
        session = new Session(activity);
        database = FirebaseDatabase.getInstance();

        /// binding.mainLinear.setOnClickListener(view -> showDialog());

    }

    private void getMyOrders() {
        ApiInterface apiInterface = RetrofitClient.getClient(activity);
        apiInterface.getMyOrders(session.getUserId()).enqueue(new Callback<MyOrderModel>() {
            @Override
            public void onResponse(Call<MyOrderModel> call, Response<MyOrderModel> response) {
                if (response.code() == 200)
                    if (response.body() != null)
                        if (response.body().getResult().equalsIgnoreCase("true")) {
                            binding.progres.setVisibility(View.GONE);
                            binding.myOrderRecycler.setLayoutManager(new LinearLayoutManager(activity));
                            List<MyOrderModel.MyOrderData> list = new ArrayList<MyOrderModel.MyOrderData>();
                            list.clear();
                            list.addAll(response.body().getData());
                            Collections.reverse(list);
                            binding.myOrderRecycler.setAdapter(new MyOrderAdapter(activity, list, list.size()));
                        }
            }

            @Override
            public void onFailure(Call<MyOrderModel> call, Throwable t) {
                Log.d("TAG", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyOrders();
    }
}