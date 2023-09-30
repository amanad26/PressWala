package com.abmtech.presswala.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import com.abmtech.presswala.Models.UsersModel;
import com.abmtech.presswala.Session.Session;
import com.abmtech.presswala.Utills.ProgressDialog;
import com.abmtech.presswala.databinding.ActivityUserProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserProfileActivity extends AppCompatActivity {

    ActivityUserProfileBinding binding;
    UserProfileActivity activity;
    FirebaseDatabase database;
    Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = this;
        database = FirebaseDatabase.getInstance();
        session = new Session(activity);

        binding.edtMobile.setText(session.getMobile());
        binding.edtEmail.setText(session.getEmail());
        binding.edtName.setText(session.getName());
        binding.address.setText(session.getAddress());

        binding.icBack.setOnClickListener(view -> onBackPressed());

        binding.cardLogin.setOnClickListener(view -> {
            ProgressDialog pd = new ProgressDialog(activity);
            pd.show();
            Map<String, Object> map = new HashMap<>();
            map.put("userName", binding.edtEmail.getText().toString());
            map.put("userPhone", binding.edtMobile.getText().toString());
            map.put("address", binding.address.getText().toString());

            database.getReference().child("users").child(session.getUserId())
                    .updateChildren(map).addOnSuccessListener(unused -> {
                        pd.dismiss();
                        Toast.makeText(activity, "Updated....", Toast.LENGTH_SHORT).show();
                    });

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyProfile();
    }

    private void getMyProfile() {

        database.getReference().child("users").child(session.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersModel usersModel = snapshot.getValue(UsersModel.class);
                binding.edtMobile.setText(usersModel.getUserPhone());
                binding.edtEmail.setText(usersModel.getUserName());
                binding.address.setText(usersModel.getAddress());

                session.setName(usersModel.getUserName());
                session.setEmail(usersModel.getUserEmail());
                session.setAddress(usersModel.getAddress());
                session.setMobile(usersModel.getUserPhone());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}