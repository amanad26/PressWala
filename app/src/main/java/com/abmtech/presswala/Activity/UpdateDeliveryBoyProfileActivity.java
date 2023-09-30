package com.abmtech.presswala.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.abmtech.presswala.Models.UsersModel;
import com.abmtech.presswala.R;
import com.abmtech.presswala.Session.Session;
import com.abmtech.presswala.Utills.ProgressDialog;
import com.abmtech.presswala.databinding.ActivityUpdateDeliveryBoyProfileBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UpdateDeliveryBoyProfileActivity extends AppCompatActivity {

    ActivityUpdateDeliveryBoyProfileBinding binding;
    UpdateDeliveryBoyProfileActivity activity;
    FirebaseDatabase database;
    String user_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateDeliveryBoyProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;

        database = FirebaseDatabase.getInstance();

        user_id = getIntent().getStringExtra("u_id");

        binding.cardLogin.setOnClickListener(view -> {
            ProgressDialog pd = new ProgressDialog(activity);
            pd.show();
            Map<String, Object> map = new HashMap<>();
            map.put("userName", binding.edtName.getText().toString());
            map.put("userPhone", binding.edtMobile.getText().toString());

            database.getReference().child("users").child(user_id)
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

        database.getReference().child("users").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersModel usersModel = snapshot.getValue(UsersModel.class);
                binding.edtMobile.setText(usersModel.getUserPhone());
                binding.edtEmail.setText(usersModel.getUserEmail());
                binding.edtName.setText(usersModel.getUserName());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}