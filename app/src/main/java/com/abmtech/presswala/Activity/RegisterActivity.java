package com.abmtech.presswala.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.abmtech.presswala.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;

        binding.tologin.setOnClickListener(view -> startActivity(new Intent(activity, LoginActivity.class)));

        binding.tosignup.setOnClickListener(view -> startActivity(new Intent(activity, SignupActivity.class)));

    }
}