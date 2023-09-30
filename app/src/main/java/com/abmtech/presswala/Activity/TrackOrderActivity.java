package com.abmtech.presswala.Activity;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.abmtech.presswala.Aapters.SingleOrderDetailsAdapter;
import com.abmtech.presswala.Apis.ApiInterface;
import com.abmtech.presswala.Apis.RetrofitClient;
import com.abmtech.presswala.Models.SingleOrderModel;
import com.abmtech.presswala.Models.UpdateModel;
import com.abmtech.presswala.Models.UsersModel;
import com.abmtech.presswala.R;
import com.abmtech.presswala.Session.Session;
import com.abmtech.presswala.Utills.ProgressDialog;
import com.abmtech.presswala.databinding.ActivityTrackOrderBinding;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphCardView;

public class TrackOrderActivity extends AppCompatActivity {
    ActivityTrackOrderBinding binding;
    Activity activity;
    ProgressDialog pd;
    ApiInterface apiInterface;
    Session session;
    String order_id = "";
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrackOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;

        session = new Session(activity);
        pd = new ProgressDialog(activity);
        database = FirebaseDatabase.getInstance();
        apiInterface = RetrofitClient.getClient(activity);

        order_id = getIntent().getStringExtra("order_id");

        Log.e("TAG", "onCreate: " + session.getType());

        if (!order_id.equalsIgnoreCase("")) {
            trackOrder(order_id);
        }

        binding.login.setOnClickListener(v -> {
            if (binding.orderEdit.getText().toString().trim().equalsIgnoreCase("")) {
                binding.orderEdit.setError("Enter Order Id");
                binding.orderEdit.requestFocus();
            } else {
                trackOrder(binding.orderEdit.getText().toString().trim());
            }
        });

    }


    private void cancelOrder(String id, String status) {
        pd.show();
        apiInterface.updateStatus(id, status, "").enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(@NonNull Call<UpdateModel> call, @NonNull Response<UpdateModel> response) {
                pd.dismiss();
                if (response.code() == 200)
                    if (response.body() != null)
                        if (response.body().getResult().equalsIgnoreCase("true")) {
                            Toast.makeText(activity, "Updated...", Toast.LENGTH_SHORT).show();
                            trackOrder(id);
                        }

            }

            @Override
            public void onFailure(@NonNull Call<UpdateModel> call, Throwable t) {
                pd.dismiss();
                Log.e("TAG", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });

    }

    private void trackOrder(String orderId) {
        pd.show();
        apiInterface.getOrderById(orderId).enqueue(new Callback<SingleOrderModel>() {
            @Override
            public void onResponse(@NonNull Call<SingleOrderModel> call, @NonNull Response<SingleOrderModel> response) {
                pd.dismiss();
                if (response.code() == 200)
                    if (response.body() != null)
                        if (response.body().getResult().equalsIgnoreCase("true")) {

                            SingleOrderModel.SingleOrderData data = response.body().getData();
                            binding.orderId.setText(data.getOrderId());
                            binding.userName.setText(data.getUserName());
                            binding.userMobile.setText(data.getUserMobile());
                            binding.orderDate.setText(data.getOrderDate());
                            binding.orderTime.setText(data.getTime());
                            binding.orderTotalPrice.setText(data.getTotalPrice());
                            binding.orderStatus.setText(data.getOrderStatus());
                            binding.delveryOtp.setText(data.getDeliveryBoyOtp());
                            binding.customerOtp.setText(data.getCustomerOtp());
                            binding.totlaItems.setText("Total Items (" + String.valueOf(data.getItems().size()) + ")");

                            getMyProfile(data.getUserId());
                            binding.recycler.setLayoutManager(new LinearLayoutManager(activity));
                            binding.recycler.setAdapter(new SingleOrderDetailsAdapter(activity, data.getItems()));

                            binding.trackLinear.setVisibility(View.GONE);
                            binding.trackLinearResult.setVisibility(View.VISIBLE);

                            if (data.getDev_boy_id().equalsIgnoreCase("0")) {
                                binding.devMobile.setText("Not Assigned");
                                binding.devName.setText("Not Assigned");
                            } else {
                                binding.devMobile.setText(data.getDev_boy_mobile());
                                binding.devName.setText(data.getDelivery_boy_name());

                            }

                            if (session.getType().equalsIgnoreCase("delivery_boy")) {
                                binding.changeStatus.setVisibility(View.VISIBLE);
                                binding.deliveryOtpLinear.setVisibility(View.VISIBLE);
                            }

                            if (session.getType().equalsIgnoreCase("user")) {
                                binding.customerOtpLinear.setVisibility(View.VISIBLE);
                                binding.verifyDeliveryBoyLinear.setVisibility(View.VISIBLE);
                            }

                            binding.verifyDeliveryBoy.setOnClickListener(view -> {
                                showDialog(data.getOrderId(), data.getCustomerOtp(), data.getDeliveryBoyOtp(), false);
                                // cancelOrder(data.getOrderId(), "complete");
                            });

                            binding.changeStatus.setOnClickListener(view -> {
                                PopupMenu popupMenu = new PopupMenu(activity, binding.changeStatus);

                                // Inflating popup menu from popup_menu.xml file
                                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        // Toast message on menu item clicked
                                        if (menuItem.getItemId() == R.id.pick_up_menu) {
                                            if (data.getOrderStatus().equalsIgnoreCase("assign_to_delivery_boy")) {
                                                //cancelOrder(data.getOrderId(), "pick_up");
                                                showDialog(data.getOrderId(), data.getCustomerOtp(), data.getDeliveryBoyOtp(), true);
                                            }
                                        } else {
                                            Toast.makeText(activity, "Please Verify Customer Otp", Toast.LENGTH_SHORT).show();
                                            // cancelOrder(data.getOrderId(), "complete");
                                            //  showDialog(data.getOrderId(), data.getCustomerOtp(), data.getDeliveryBoyOtp());
                                        }
                                        return true;
                                    }
                                });
                                // Showing the popup menu
                                popupMenu.show();
                            });
                        } else {
                            Toast.makeText(activity, "No Order Found..!", Toast.LENGTH_SHORT).show();
                        }
            }

            @Override
            public void onFailure(@NonNull Call<SingleOrderModel> call, @NonNull Throwable t) {
                Log.e("TAG", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });


    }

    private void getMyProfile(String userId) {

        database.getReference().child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsersModel usersModel = snapshot.getValue(UsersModel.class);
                binding.address.setText(usersModel.getAddress());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showDialog(String orderId, String customerOtp, String deliveryBoyOtp, boolean isDel) {

        RoundedBottomSheetDialog mBottomSheetDialog = new RoundedBottomSheetDialog(activity);
        View sheetView = mBottomSheetDialog.getLayoutInflater().inflate(R.layout.verify_otp_layout, null);

        mBottomSheetDialog.setContentView(sheetView);

        EditText edt_otp = mBottomSheetDialog.findViewById(R.id.edt_otp);
        NeumorphCardView card_login = mBottomSheetDialog.findViewById(R.id.card_login);

        card_login.setOnClickListener(view -> {
            if (edt_otp.getText().toString().equalsIgnoreCase("")) {
                edt_otp.setError("Enter OTP");
                edt_otp.requestFocus();
            } else {
                verifyOtp(orderId, edt_otp.getText().toString(), isDel);
            }
            mBottomSheetDialog.dismiss();
        });


//      mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.ANSPARENT));
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();

    }

    private void verifyOtp(String orderId, String customerOtp, boolean isDel) {

        ProgressDialog pd = new ProgressDialog(activity);
        pd.show();

        ApiInterface apiInterface = RetrofitClient.getClient(activity);

        Call<UpdateModel> call;

        if (session.getType().equalsIgnoreCase("user")) {
            call = apiInterface.verifyDeliveryOtp(orderId, customerOtp);
            Log.e("TAG", "verifyOtp: User");
        } else if (session.getType().equalsIgnoreCase("delivery_boy")) {
            Log.e("TAG", "verifyOtp: delivery_boy");
            call = apiInterface.verifyCustomerOtp(orderId, customerOtp);
        } else {
            Log.e("TAG", "verifyOtp: Admin");
            call = apiInterface.verifyCustomerOtp(orderId, customerOtp);
        }

        call.enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(@NonNull Call<UpdateModel> call, @NonNull Response<UpdateModel> response) {
                pd.dismiss();
                if (response.code() == 200)
                    if (response.body() != null)
                        if (response.body().getResult().equalsIgnoreCase("true")) {
                            Toast.makeText(TrackOrderActivity.this, "OTP Verified..!", Toast.LENGTH_SHORT).show();
                            if (!isDel) cancelOrder(orderId, "complete");
                            else cancelOrder(orderId, "pick_up");
                        } else {
                            Toast.makeText(TrackOrderActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
            }

            @Override
            public void onFailure(@NonNull Call<UpdateModel> call, Throwable t) {
                pd.dismiss();
            }
        });


    }
}