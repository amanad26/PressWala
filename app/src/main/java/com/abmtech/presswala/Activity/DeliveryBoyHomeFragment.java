package com.abmtech.presswala.Activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abmtech.presswala.Aapters.MyOrderAdapter;
import com.abmtech.presswala.Aapters.RecentAdapter;
import com.abmtech.presswala.Apis.ApiInterface;
import com.abmtech.presswala.Apis.RetrofitClient;
import com.abmtech.presswala.Models.MyOrderModel;
import com.abmtech.presswala.R;
import com.abmtech.presswala.Session.Session;
import com.abmtech.presswala.Utills.ProgressDialog;
import com.abmtech.presswala.databinding.FragmentDeliveryBoyHomeBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DeliveryBoyHomeFragment extends Fragment {

    Activity activity;
    FragmentDeliveryBoyHomeBinding binding;
    Session session;
    ProgressDialog pd;

    public DeliveryBoyHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDeliveryBoyHomeBinding.inflate(getLayoutInflater());

        activity = requireActivity();
        session = new Session(activity);
        pd = new ProgressDialog(activity);

        binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyOrders();
            }
        });
        return binding.getRoot();

    }

    @Override
    public void onResume() {
        super.onResume();
        getMyOrders();
    }

    private void getMyOrders() {

        pd.show();
        ApiInterface apiInterface = RetrofitClient.getClient(activity);
        apiInterface.getAllOrders().enqueue(new Callback<MyOrderModel>() {
            @Override
            public void onResponse(@NonNull Call<MyOrderModel> call, @NonNull Response<MyOrderModel> response) {
                pd.dismiss();
                binding.refresh.setRefreshing(false);
                if (response.code() == 200)
                    if (response.body() != null)
                        if (response.body().getResult().equalsIgnoreCase("true")) {
                            binding.progress.setVisibility(View.GONE);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                            List<MyOrderModel.MyOrderData> list = response.body().getData();
//                            for (int i = 0; i < response.body().getData().size(); i++) {
//                                if (response.body().getData().get(i).getOrderStatus().equalsIgnoreCase("assign_to_delivery_boy") || response.body().getData().get(i).getOrderStatus().equalsIgnoreCase("pick_up"))
//                                    if (response.body().getData().get(i).getDev_boy_id().equalsIgnoreCase(session.getUserId()))
//                                        list.add(response.body().getData().get(i));
//                            }

                            if (list.size() == 0)
                                Toast.makeText(activity, "No Orders Found....", Toast.LENGTH_SHORT).show();

                            Collections.reverse(list);
                            binding.recyclerView.setAdapter(new RecentAdapter(activity, list, list.size()));
                        }
            }

            @Override
            public void onFailure(@NonNull Call<MyOrderModel> call, @NonNull Throwable t) {
                pd.dismiss();
                binding.refresh.setRefreshing(false);
                Log.d("TAG", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });


    }

}