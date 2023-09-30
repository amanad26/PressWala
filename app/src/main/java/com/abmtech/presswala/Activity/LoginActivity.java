package com.abmtech.presswala.Activity;

import static com.abmtech.presswala.Utills.Constants.isValidEmailId;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.abmtech.presswala.Models.UsersModel;
import com.abmtech.presswala.Session.Session;
import com.abmtech.presswala.Utills.ProgressDialog;
import com.abmtech.presswala.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private Activity activity;
    private FirebaseAuth auth;
    private ProgressDialog pd;
    private Session session;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;

        session = new Session(activity);
        auth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(activity);
        database = FirebaseDatabase.getInstance();

        binding.cardLogin.setOnClickListener(view -> {
            if (isValidated()) loginUser();
        });

        binding.gosignup.setOnClickListener(view -> {
            startActivity(new Intent(activity, SignupActivity.class));
            finish();
        });


        binding.back.setOnClickListener(view -> onBackPressed());
    }

    private void loginUser() {
        pd.show();
        auth.signInWithEmailAndPassword(binding.edtEmail.getText().toString(), binding.edtPassword.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        session.setUser_id(auth.getUid());

                        if (auth.getUid() != null)
                            database.getReference().child("users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    UsersModel usersModel = snapshot.getValue(UsersModel.class);

                                    if (usersModel != null) {
                                        session.setMobile(usersModel.getUserPhone());
                                        session.setName(usersModel.getUserName());
                                        session.setEmail(usersModel.getUserEmail());
                                        session.setType(usersModel.getUserType());

                                        if (usersModel.getUserType().equalsIgnoreCase("user")) {
                                            Toast.makeText(activity, "Login Success..", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(activity, HomeActivity.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                            finish();
                                        } else if (usersModel.getUserType().equalsIgnoreCase("delivery_boy")) {
                                            startActivity(new Intent(activity, DeliveryBoyHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                        } else {
                                            startActivity(new Intent(activity, AdminHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                        }
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    pd.dismiss();
                                    Toast.makeText(activity, error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        pd.dismiss();
                    } else {
                        pd.dismiss();
                        Toast.makeText(activity, "Check Email And Password", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isValidated() {
        if (binding.edtEmail.getText().toString().equalsIgnoreCase("")) {
            binding.edtEmail.setError("Enter Your Email...!");
            binding.edtEmail.requestFocus();
            return false;
        } else if (binding.edtPassword.getText().toString().equalsIgnoreCase("") || binding.edtPassword.getText().toString().length() < 6) {
            binding.edtPassword.setError("Enter Your 6 Digit Password...!");
            binding.edtPassword.requestFocus();
            return false;
        } else if (!isValidEmailId(binding.edtEmail.getText().toString())) {
            binding.edtEmail.setError("Invalid email...!");
            binding.edtEmail.requestFocus();
            return false;
        } else {
            return true;
        }
    }
}