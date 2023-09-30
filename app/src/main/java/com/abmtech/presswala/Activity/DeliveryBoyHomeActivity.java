package com.abmtech.presswala.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.abmtech.presswala.Session.Session;
import com.abmtech.presswala.databinding.ActivityDeliveryBoyHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

public class DeliveryBoyHomeActivity extends AppCompatActivity {

    ActivityDeliveryBoyHomeBinding binding;
    DeliveryBoyHomeActivity activity;
    FirebaseAuth auth;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeliveryBoyHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        session = new Session(activity);
        auth = FirebaseAuth.getInstance();


        replaceFragment(new DeliveryBoyHomeFragment());
        binding.profile.setOnClickListener(view -> startActivity(new Intent(activity, UserProfileActivity.class)));
        binding.trackorder.setOnClickListener(view -> startActivity(new Intent(activity, TrackOrderActivity.class)));

        binding.openDrawer.setOnClickListener(view -> binding.drawer.open());
        binding.userName.setText(session.getName());
        binding.logout.setOnClickListener(view -> logout());

    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(binding.container.getId(), fragment);
        ft.commit();
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
}