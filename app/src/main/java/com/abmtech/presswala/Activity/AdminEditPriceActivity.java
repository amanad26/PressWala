package com.abmtech.presswala.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

import com.abmtech.presswala.databinding.ActivityAdminEditPriceBinding;

public class AdminEditPriceActivity extends AppCompatActivity {
    private Activity activity;
    private ActivityAdminEditPriceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminEditPriceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
    }
}