package com.abmtech.presswala.Activity;

import static com.abmtech.presswala.Utills.Constants.isValidEmailId;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.abmtech.presswala.Session.Session;
import com.abmtech.presswala.Utills.ProgressDialog;
import com.abmtech.presswala.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private Activity activity;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private Session session;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        session = new Session(activity);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        pd = new ProgressDialog(activity);

        binding.cardSignup.setOnClickListener(view -> {
            if (isValidated()) signUpUser();
        });


    }


    private boolean isValidated() {

        if (binding.nameEdit.getText().toString().equalsIgnoreCase("")) {
            binding.nameEdit.setError("Enter Your Name...!");
            binding.nameEdit.requestFocus();
            return false;
        } else if (binding.emailEdit.getText().toString().equalsIgnoreCase("")) {
            binding.emailEdit.setError("Enter Your Email...!");
            binding.emailEdit.requestFocus();
            return false;
        } else if (binding.passwordEdit.getText().toString().equalsIgnoreCase("") && binding.passwordEdit.getText().toString().length() < 6) {
            binding.passwordEdit.setError("Enter Your 6 Digit Password...!");
            binding.passwordEdit.requestFocus();
            return false;
        } else if (!isValidEmailId(binding.emailEdit.getText().toString())) {
            binding.emailEdit.setError("Invalid email...!");
            binding.emailEdit.requestFocus();
            return false;
        } else if (binding.phoneEdit.getText().toString().equalsIgnoreCase("") && binding.passwordEdit.getText().toString().length() < 6) {
            binding.phoneEdit.setError("Enter Your Mobile  Number...!");
            binding.phoneEdit.requestFocus();
            return false;
        } else {
            return true;
        }


    }


    private void signUpUser() {
        pd = new ProgressDialog(activity);
        pd.show();
        auth.createUserWithEmailAndPassword(binding.emailEdit.getText().toString(), binding.passwordEdit.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.e("TAG", "signUpUser() called");

                        Map<String, Object> map = new HashMap<>();

                        map.put("userId", auth.getUid());
                        map.put("userName", binding.nameEdit.getText().toString());
                        map.put("userEmail", binding.emailEdit.getText().toString());
                        map.put("userPassword", binding.passwordEdit.getText().toString());
                        map.put("userPhone", binding.phoneEdit.getText().toString());
                        map.put("userType", "user");
                        map.put("address", "");
                        map.put("fcm", "");

                        database.getReference().child("users").child(auth.getUid()).setValue(map).addOnSuccessListener(unused -> {

                            session.setEmail(binding.emailEdit.getText().toString());
                            session.setUser_id(auth.getUid());
                            session.setMobile(binding.phoneEdit.getText().toString());
                            session.setPassword(binding.passwordEdit.getText().toString());
                            session.setName(binding.nameEdit.getText().toString());
                            session.setType("user");
                            Toast.makeText(activity, "Your Account has been created...!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(activity, HomeActivity.class).
                                    setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                            pd.dismiss();
                        }).addOnFailureListener(e -> {
                            pd.dismiss();
                            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "onFailure() called with: e = [" + e + "]");
                        });

                    }
                });

    }
}