package com.abmtech.presswala.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.abmtech.presswala.Session.Session;
import com.abmtech.presswala.databinding.ActivityAdminHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Session session;
    private ActivityAdminHomeBinding binding;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;


        auth = FirebaseAuth.getInstance();
        session = new Session(activity);
        binding.openDrawer.setOnClickListener(view -> binding.Drawerlayout.open());

        replaceFragment(new AdminDashboardFragment());

        binding.userName.setText(session.getName());
        binding.editPrices.setOnClickListener(view -> startActivity(new Intent(activity, MyProductActivity.class)));
        binding.createDeliveryAccount.setOnClickListener(view -> startActivity(new Intent(activity, DeliveryBoyAccountListActivity.class)));
        binding.products.setOnClickListener(view -> startActivity(new Intent(activity, MyProductActivity.class)));
        binding.myProfile.setOnClickListener(view -> startActivity(new Intent(activity, UserProfileActivity.class)));

        binding.logout.setOnClickListener(view -> logout());
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

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(binding.container.getId(), fragment);
        ft.commit();
    }
}