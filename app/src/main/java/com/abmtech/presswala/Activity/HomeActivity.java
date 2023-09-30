package com.abmtech.presswala.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.abmtech.presswala.Aapters.MyOrderAdapter;
import com.abmtech.presswala.Aapters.RecentAdapter;
import com.abmtech.presswala.Apis.ApiInterface;
import com.abmtech.presswala.Apis.RetrofitClient;
import com.abmtech.presswala.Models.MyOrderModel;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.abmtech.presswala.Session.Session;
import com.abmtech.presswala.databinding.ActivityHomeBinding;
import com.abmtech.presswala.databinding.BottomsheetDrycleanPressBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Activity activity;
    private RoundedBottomSheetDialog mBottomSheetDialog;
    private BottomsheetDrycleanPressBinding bottomsheetDrycleanPressBinding;
    FirebaseAuth auth;
    Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        auth = FirebaseAuth.getInstance();
        session = new Session(activity);

        binding.userName.setText(session.getName());
        Log.d("TAG", "onCreate() called with: session.getName() = [" + session.getName() + "]");

        binding.makeOrderLayout.setOnClickListener(view -> option());
        binding.trackOrderLayout.setOnClickListener(view -> startActivity(new Intent(activity, TrackOrderActivity.class)
                .putExtra("order_id","")));
        binding.home.setOnClickListener(view -> startActivity(new Intent(activity, HomeActivity.class)));
        binding.logout.setOnClickListener(view -> logout());
        binding.myorder.setOnClickListener(view -> startActivity(new Intent(activity, MyOrdersActivity.class)));
        binding.profile.setOnClickListener(view -> startActivity(new Intent(activity, UserProfileActivity.class)));
        binding.privacy.setOnClickListener(view -> Toast.makeText(activity, "Privacy Policy", Toast.LENGTH_SHORT).show());

        binding.openDrawer.setOnClickListener(view -> binding.Drawerlayout.open());
    }

    private void logout() {
        auth.signOut();
        session.logOut();
        startActivity(new Intent(activity, LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        );
        finish();
    }

    private void option() {
        bottomsheetDrycleanPressBinding = BottomsheetDrycleanPressBinding.inflate(LayoutInflater.from(activity));
        mBottomSheetDialog = new RoundedBottomSheetDialog(activity);
        // View sheetView = mBottomSheetDialog.getLayoutInflater().inflate(binding.getRoot(), null);
        mBottomSheetDialog.setContentView(bottomsheetDrycleanPressBinding.getRoot());

        bottomsheetDrycleanPressBinding.dryclean.setOnClickListener(view -> startActivity(new Intent(activity, AddQuantitiesDryCleanActivity.class)));

        bottomsheetDrycleanPressBinding.press.setOnClickListener(view -> startActivity(new Intent(activity, AddQuantitiesPresssActivity.class)));

        bottomsheetDrycleanPressBinding.back.setOnClickListener(view -> mBottomSheetDialog.dismiss());

        mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mBottomSheetDialog.setCancelable(false);

        mBottomSheetDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyOrders();
    }

    private void getMyOrders() {
        ApiInterface apiInterface = RetrofitClient.getClient(activity);
        apiInterface.getMyOrders(session.getUserId()).enqueue(new Callback<MyOrderModel>() {
            @Override
            public void onResponse(@NonNull Call<MyOrderModel> call, @NonNull Response<MyOrderModel> response) {
                binding.progress.setVisibility(View.GONE);
                if (response.code() == 200)
                    if (response.body() != null)
                        if (response.body().getResult().equalsIgnoreCase("true")) {
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                            List<MyOrderModel.MyOrderData> list = new ArrayList<MyOrderModel.MyOrderData>();
                            list.clear();
                            list.addAll(response.body().getData());
                            Collections.reverse(list);
                            binding.recyclerView.setAdapter(new RecentAdapter(activity, list, list.size()));

                        } else {
                            binding.textEmpty.setVisibility(View.VISIBLE);
                        }
            }

            @Override
            public void onFailure(@NonNull Call<MyOrderModel> call, @NonNull Throwable t) {
                Log.e("TAG", "onFailure() called with: call = [" + call + "], t = [" + t.getLocalizedMessage() + "]");
                binding.progress.setVisibility(View.GONE);
                binding.textEmpty.setVisibility(View.VISIBLE);
            }
        });


    }


}