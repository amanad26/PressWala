package com.abmtech.presswala.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

import com.abmtech.presswala.databinding.ActivityOrderPlacedBinding;

public class OrderPlacedActivity extends AppCompatActivity {

    private ActivityOrderPlacedBinding binding;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderPlacedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
    }
}