package com.abmtech.presswala.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.abmtech.presswala.Aapters.ProductAdapter;
import com.abmtech.presswala.Models.ProductModel;
import com.abmtech.presswala.Session.Session;
import com.abmtech.presswala.Utills.ProductSelect;
import com.abmtech.presswala.databinding.ActivityAddQuantitiesDryClearBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

public class AddQuantitiesDryCleanActivity extends AppCompatActivity implements ProductSelect {
    private ActivityAddQuantitiesDryClearBinding binding;
    private Activity activity;
    private FirebaseDatabase database;
    private ArrayList<ProductModel> productModelList = new ArrayList<>();
    private ProductAdapter adapter;
    private DatabaseReference reference;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddQuantitiesDryClearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;

        session = new Session(activity);

        database = FirebaseDatabase.getInstance();

        binding.makeOrderBtn.setOnClickListener(view -> startActivity(new Intent(activity, CalculationFinalActivity.class)));

        binding.makeOrderBtn.setOnClickListener(view -> {
            ArrayList<Map<String, Object>> data = new ArrayList<>();

            int random = new Random().nextInt(1000000);

            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
            Date d = new Date();
            String time = formatter2.format(d.getTime());

            Calendar cal = Calendar.getInstance();
            int yearT = cal.get(Calendar.YEAR);
            int monthT = cal.get(Calendar.MONTH);
            int dayT = cal.get(Calendar.DAY_OF_MONTH);

            String da = String.valueOf(dayT);
            if (da.length() == 1) da = "0" + da;

            String date = da + " " + getMonth(monthT) + " " + yearT;


            Log.e("TAG", "onCreate() called with: date = [" + date + "]");

                int total = 0;
                for (int i = 0; i < productModelList.size(); i++) {
                    int p = Integer.parseInt(productModelList.get(i).getProductPrice());
                    int c = Integer.parseInt(productModelList.get(i).getCount());

                    if (c > 0) {
                        c = c * p;
                        String key = database.getReference().push().getKey();
                        Map<String, Object> map = new HashMap<>();
                        map.put("userId", session.getUserId());
                        map.put("userName", session.getName());
                        map.put("userMobile", session.getMobile());
                        map.put("orderDate", date);
                        map.put("time", time);
                        map.put("count", productModelList.get(i).getCount());
                        map.put("orderStatus", "Placed");
                        map.put("productName", productModelList.get(i).getProductName());
                        map.put("productPrice", productModelList.get(i).getProductPrice());
                        map.put("totalPrice", String.valueOf(c));
                        map.put("productOrderId", key);
                        map.put("orderId", String.valueOf(random));

                        total = total + c;

                        data.add(map);
//                    reference.child(key).setValue(map).addOnSuccessListener(unused -> Toast.makeText(activity, "Order Placed...", Toast.LENGTH_SHORT).show());
                    }
                }
                if(data.size() != 0 ){
                    startActivity(new Intent(activity, CalculationFinalActivity.class)
                            .putExtra("productList", data)
                            .putExtra("random", String.valueOf(random))
                            .putExtra("total", String.valueOf(total)));
                    Log.e("TAG", "onCreate() called with: make Order  = [" + productModelList.toString() + "]");
                }
             else {
                Toast.makeText(activity, "Select Any Product ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    static String getMonth(int m) {
        String month = "";

        switch (m){
            case 0:  month = "January";    break;
            case 1:  month = "February";   break;
            case 2:  month = "March";      break;
            case 3:  month = "April";      break;
            case 4:  month = "May";        break;
            case 5:  month = "June";       break;
            case 6:  month = "July";       break;
            case 7:  month = "August";     break;
            case 8:  month = "September";  break;
            case 9:  month = "October";    break;
            case 10: month = "November";   break;
            case 11: month = "December";   break;

            default: month = "NAN";
        }

        return month;
    }

    private void getProduct() {
        database.getReference().child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productModelList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    ProductModel model = snapshot1.getValue(ProductModel.class);
                    if (model.getProductType().equalsIgnoreCase("Dry Clean"))
                        productModelList.add(model);
                }
                adapter = new ProductAdapter(activity, productModelList, AddQuantitiesDryCleanActivity.this);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                binding.recyclerView.setAdapter(adapter);
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
        getProduct();
    }

    @Override
    public void increase(int position) {
        int count = Integer.parseInt(productModelList.get(position).getCount());
        count++;
        Log.e("TAG", "increase() called with: position = [" + position + "] and count is " + productModelList.get(position).getCount());
        productModelList.get(position).setCount(String.valueOf(count));
        binding.recyclerView.setAdapter(adapter);
        Log.e("TAG", "increase() called with: position after increase = [" + position + "] and count is " + productModelList.get(position).getCount());
    }

    @Override
    public void decrease(int position) {
        int count = Integer.parseInt(productModelList.get(position).getCount());
        if (count > 0) count--;
        productModelList.get(position).setCount(String.valueOf(count));
        binding.recyclerView.setAdapter(adapter);
    }
}