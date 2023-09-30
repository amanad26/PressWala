package com.abmtech.presswala.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abmtech.presswala.Aapters.MyOrderAdapter;
import com.abmtech.presswala.Aapters.RecentAdapter;
import com.abmtech.presswala.Apis.ApiInterface;
import com.abmtech.presswala.Apis.RetrofitClient;
import com.abmtech.presswala.Models.MyOrderModel;
import com.abmtech.presswala.Models.OrderData;
import com.abmtech.presswala.Models.OrderModel;
import com.abmtech.presswala.R;
import com.abmtech.presswala.Session.Session;
import com.abmtech.presswala.databinding.FragmentAdminDashboardBinding;
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

public class AdminDashboardFragment extends Fragment {
    private Activity activity;
    private FragmentAdminDashboardBinding binding;
    FirebaseDatabase database;
    Session session;
    List<OrderModel> orderList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminDashboardBinding.inflate(inflater, container, false);
        activity = requireActivity();
        session = new Session(activity);
        database = FirebaseDatabase.getInstance();

        binding.mainLinear.setOnClickListener(view -> showDialog());
//        binding.change.setOnClickListener(view -> startActivity(new Intent(activity, AdminEditPriceActivity.class)));
//        binding.login.setOnClickListener(view -> startActivity(new Intent(activity, DeliveryBoySignupActivity.class)));
//        binding.addProduct.setOnClickListener(view -> startActivity(new Intent(activity, MyProductActivity.class)));


        binding.refresh.setOnRefreshListener(() -> getMyOrders());

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyOrders();
    }

    private void getMyOrders() {
        ApiInterface apiInterface = RetrofitClient.getClient(activity);
        apiInterface.getAllOrders().enqueue(new Callback<MyOrderModel>() {
            @Override
            public void onResponse(@NonNull Call<MyOrderModel> call, @NonNull Response<MyOrderModel> response) {
                binding.refresh.setRefreshing(false);
                if (response.code() == 200)
                    if (response.body() != null)
                        if (response.body().getResult().equalsIgnoreCase("true")) {
                            binding.progres.setVisibility(View.GONE);
                            binding.recentOrderRecycler.setLayoutManager(new LinearLayoutManager(activity));
                            List<MyOrderModel.MyOrderData> list = new ArrayList<MyOrderModel.MyOrderData>();
                            list.clear();
                            list.addAll(response.body().getData());
                            Collections.reverse(list);
                            binding.recentOrderRecycler.setAdapter(new RecentAdapter(activity, list, list.size()));
                        }
            }

            @Override
            public void onFailure(@NonNull Call<MyOrderModel> call, @NonNull Throwable t) {
                binding.refresh.setRefreshing(false);
                Log.d("TAG", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });


    }


    private void showDialog() {

        final Dialog dialog = new Dialog(activity);

        dialog.setContentView(R.layout.select_option_layout_admin);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        TextView btn_track = dialog.findViewById(R.id.btn_track);
        TextView btn_cancel = dialog.findViewById(R.id.btn_cancel);
        TextView btn_accept = dialog.findViewById(R.id.btn_accept);

        //btn_track.setOnClickListener(view -> startActivity(new Intent(activity, TrackOrderActivity.class)));

        dialog.show();

    }

    private void cancelOrder() {

    }

}