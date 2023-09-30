package com.abmtech.presswala.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.abmtech.presswala.Aapters.CalculationAdapter;
import com.abmtech.presswala.Apis.ApiInterface;
import com.abmtech.presswala.Apis.RetrofitClient;
import com.abmtech.presswala.Models.AddOrderModel;
import com.abmtech.presswala.Models.UpdateModel;
import com.abmtech.presswala.R;
import com.abmtech.presswala.Session.Session;
import com.abmtech.presswala.Utills.ProgressDialog;
import com.abmtech.presswala.databinding.ActivityCalculationFinalBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphCardView;

public class CalculationFinalActivity extends AppCompatActivity {
    private ActivityCalculationFinalBinding binding;
    private Activity activity;
    private String total;
    private DatabaseReference reference;
    private FirebaseDatabase database;
    int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalculationFinalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        Session session = new Session(activity);

//        binding.trackOrderBtn.setOnClickListener(view -> {
//            showDialog();
//            /// startThread();
//        })

        total = getIntent().getStringExtra("total");
        String random = getIntent().getStringExtra("random");

        database = FirebaseDatabase.getInstance();
        //// int random = new Random().nextInt(1000000);

        reference = database.getReference().child("orders").child(session.getUserId()).child(String.valueOf(random));


        ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) getIntent().getSerializableExtra("productList");

        binding.recyclerView.setAdapter(new CalculationAdapter(activity, data, total));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        String userId = String.valueOf(data.get(0).get("userId"));
        String userName = String.valueOf(data.get(0).get("userName"));
        String userMobile = String.valueOf(data.get(0).get("userMobile"));
        String orderDate = String.valueOf(data.get(0).get("orderDate"));
        String time = String.valueOf(data.get(0).get("time"));
        String count = String.valueOf(data.get(0).get("count"));
        String orderStatus = String.valueOf(data.get(0).get("orderStatus"));
        // String totalPrice = String.valueOf(data.get(0).get("totalPrice"));
        String orderId = String.valueOf(data.get(0).get("orderId"));


        Log.e("TAG", "onCreate() called with: userId = [" + userId + "]");
        Log.e("TAG", "onCreate() called with: userName = [" + userName + "]");
        Log.e("TAG", "onCreate() called with: userMobile = [" + userMobile + "]");
        Log.e("TAG", "onCreate() called with: orderDate = [" + orderDate + "]");
        Log.e("TAG", "onCreate() called with: userId = [" + userId + "]");
        Log.e("TAG", "onCreate() called with: time = [" + time + "]");
        Log.e("TAG", "onCreate() called with: orderStatus = [" + orderStatus + "]");
        Log.e("TAG", "onCreate() called with: orderId = [" + orderId + "]");

        List<AddOrderModel.OrderData.Item> itemsList = new ArrayList<>();


        for (int i = 0; i < data.size(); i++) {
            itemsList.add(new AddOrderModel.OrderData.Item(String.valueOf(data.get(i).get("productName")), String.valueOf(data.get(i).get("productPrice")), String.valueOf(data.get(i).get("count")), String.valueOf(data.get(i).get("orderId"))));
            totalPrice = totalPrice + Integer.parseInt(String.valueOf(data.get(i).get("totalPrice")));
        }


        Log.e("TAG", "onCreate() called with: totalPrice = [" + totalPrice + "]");


        binding.cancelOrder.setOnClickListener(view -> finish());

        binding.placeOrderBtn.setOnClickListener(view -> {

            ProgressDialog pd = new ProgressDialog(activity);
            pd.show();
            String text = "[";

            for (int i = 0; i < itemsList.size(); i++) {


                text = text + "{";

                text = text + "\"" + "pro_name" + "\"" + ":" + "\"" + itemsList.get(i).getProName() + "\"" + ",";
                text = text + "\"" + "pro_price" + "\"" + ":" + "\"" + itemsList.get(i).getProPrice() + "\"" + ",";
                text = text + "\"" + "pro_quantity" + "\"" + ":" + "\"" + itemsList.get(i).getProQuantity() + "\"" + ",";
                text = text + "\"" + "orderId" + "\"" + ":" + "\"" + orderId + "\"" + "}";

                if (i != data.size() - 1)
                    text = text + ",";
            }

            text = text + "]";

            Log.e("TAG", "onCreate() called with: text = [" + text + "]");


            ApiInterface apiInterface = RetrofitClient.getClient(activity);
            apiInterface.addOrder(
                    text,
                    userId,
                    userName,
                    userMobile,
                    orderDate,
                    time,
                    orderId,
                    String.valueOf(totalPrice)
            ).enqueue(new Callback<UpdateModel>() {
                @Override
                public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {
                    pd.dismiss();
                    if (response.code() == 200)
                        if (response.body() != null)
                            if (response.body().getResult().equalsIgnoreCase("true")) {
                                Toast.makeText(activity, "Order Placed..!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(activity, HomeActivity.class));
                                finish();
                            }
                }

                @Override
                public void onFailure(Call<UpdateModel> call, Throwable t) {
                    pd.dismiss();
                    Log.e("TAG", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                }
            });


        });

        ///   startActivity(new Intent(activity, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(activity);

        dialog.setContentView(R.layout.dialog_order_placed);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

        NeumorphCardView homeBtn = dialog.findViewById(R.id.home);
        ImageView cancel_btn = dialog.findViewById(R.id.cancel_btn);


        homeBtn.setOnClickListener(view -> {
            startActivity(new Intent(activity, HomeActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            );
            finish();
        });

        cancel_btn.setOnClickListener(view -> {
            startActivity(new Intent(activity, HomeActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            );
            finish();
        });

        dialog.show();
    }
}