package com.abmtech.presswala.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.abmtech.presswala.Apis.RetrofitClient;
import com.abmtech.presswala.Models.AppBlockModel;
import com.abmtech.presswala.Session.Session;
import com.abmtech.presswala.databinding.ActivitySplashBinding;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    Activity activity;
    int SPLASH_SCREEN_TIME_OUT = 2000;
    Session session;
    private String token_fcm_id = "";
    String appId = "7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        session = new Session(activity);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String fcm_id = instanceIdResult.getToken();
            // send it to server
            token_fcm_id = fcm_id;
            Log.e("refresh_tokentoken", fcm_id);
        });

        checkAppBlock();

    }

    private void checkAppBlock() {

        RetrofitClient.appBlockClient(activity).checkAppBlock(appId).enqueue(new Callback<AppBlockModel>() {
            @Override
            public void onResponse(@NonNull Call<AppBlockModel> call, @NonNull Response<AppBlockModel> response) {
                if (response.code() == 200)
                    if (response.body() != null)
                        if (response.body().getResult().equalsIgnoreCase("true")) {
                            if (session.getUserId().equalsIgnoreCase("")) {
                                Intent i = new Intent(SplashActivity.this, RegisterActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                if (session.getType().equalsIgnoreCase("user")) {
                                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    finish();
                                } else if (session.getType().equalsIgnoreCase("delivery_boy")) {
                                    startActivity(new Intent(activity, DeliveryBoyHomeActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();
                                } else {
                                    Intent i = new Intent(SplashActivity.this, AdminHomeActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    finish();
                                }

                                updateFcm();
                            }
                        } else {
                            Toast.makeText(activity, "Service Not Available...!", Toast.LENGTH_SHORT).show();
                        }
            }

            @Override
            public void onFailure(@NonNull Call<AppBlockModel> call, @NonNull Throwable t) {

            }
        });


    }

    private void updateFcm() {
        Map<String, Object> map = new HashMap<>();
        map.put("fcm", token_fcm_id);

        FirebaseDatabase.getInstance().getReference().child("users").child(session.getUserId())
                .updateChildren(map).addOnSuccessListener(unused -> {
                    Log.d("TAG", "updateFcm() called Updated");
                    // Toast.makeText(activity, "Updated....", Toast.LENGTH_SHORT).show();
                });
    }
}