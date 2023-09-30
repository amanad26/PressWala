package com.abmtech.presswala.Aapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abmtech.presswala.Activity.ChooseDeliveryBoyActivity;
import com.abmtech.presswala.Activity.DeliveryBoyHomeActivity;
import com.abmtech.presswala.Activity.HomeActivity;
import com.abmtech.presswala.Activity.MyOrdersActivity;
import com.abmtech.presswala.Activity.TrackOrderActivity;
import com.abmtech.presswala.Apis.ApiInterface;
import com.abmtech.presswala.Apis.RetrofitClient;
import com.abmtech.presswala.Models.MyOrderModel;
import com.abmtech.presswala.Models.UpdateModel;
import com.abmtech.presswala.R;
import com.abmtech.presswala.Session.Session;
import com.abmtech.presswala.Utills.ProgressDialog;
import com.abmtech.presswala.databinding.MyOrdersLayoutBinding;
import com.abmtech.presswala.databinding.RecentDataLayoutBinding;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphCardView;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewHolder> {

    Context context;
    ///List<OrderModel> models;
    Session session;
    List<MyOrderModel.MyOrderData> models;
    ApiInterface apiInterface;
    ProgressDialog pd;
    int count = 0;
    private boolean isOk = false;

    public RecentAdapter(Context context, List<MyOrderModel.MyOrderData> models, int count) {
        this.context = context;
        this.models = models;
        this.count = count;
        session = new Session(context);
        apiInterface = RetrofitClient.getClient(context);
        pd = new ProgressDialog(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.recent_data_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.orderId.setText("Order ID : " + models.get(position).getOrderId());
        holder.binding.userName.setText(models.get(position).getUserName());
        holder.binding.userMobile.setText("Mo : " + models.get(position).getUserMobile());
        holder.binding.amount.setText("Rs. " + models.get(position).getTotalPrice() + " (" + models.get(position).getItems().size() + " items )");
        holder.binding.date.setText(models.get(position).getOrderDate());
        holder.itemView.setOnClickListener(view -> showOrderDetails(models.get(position).getItems(), models.get(position).getTotalPrice(), models.get(position).getOrderId(), models.get(position).getOrderStatus(), models.get(position).getId()));

        if (models.get(position).getOrderStatus().equalsIgnoreCase("placed")) {
            holder.binding.status.setTextColor(context.getResources().getColor(com.google.android.libraries.places.R.color.quantum_yellow));
            holder.binding.status.setText("Pending");

            if (session.getType().equalsIgnoreCase("delivery_boy")) {
                holder.binding.assignMe.setVisibility(View.VISIBLE);
                holder.binding.assignMe.setOnClickListener(view -> {
                    assignDeliveryBoy(models.get(position).getId(), models.get(position).getOrderId());
                    holder.binding.assignMe.setVisibility(View.GONE);
                });
                //holder.binding.assignMe.setVisibility(View.GONE);
            }

        } else if (models.get(position).getOrderStatus().equalsIgnoreCase("assign_to_delivery_boy")) {
            holder.binding.status.setTextColor(context.getResources().getColor(com.google.android.libraries.places.R.color.quantum_yellow));
            holder.binding.status.setText("Assigned");
            holder.binding.assignMe.setVisibility(View.GONE);
        } else if (models.get(position).getOrderStatus().equalsIgnoreCase("cancle")) {
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.primary_color));
            holder.binding.status.setText("Canceled");
            holder.binding.assignMe.setVisibility(View.GONE);
        } else if (models.get(position).getOrderStatus().equalsIgnoreCase("pick_up")) {
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.textcolor));
            holder.binding.status.setText("Picked");
            holder.binding.assignMe.setVisibility(View.GONE);
        } else {
            holder.binding.status.setText("Completed");
            holder.binding.status.setTextColor(context.getResources().getColor(R.color.green));
            holder.binding.assignMe.setVisibility(View.GONE);
        }

    }

    private void assignDeliveryBoy(String id, String orderId) {
        pd.show();
        RetrofitClient.getClient(context).assignDeliveryBoy(
                id,
                session.getName(),
                session.getUserId(),
                session.getMobile()
        ).enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(@NonNull Call<UpdateModel> call, @NonNull Response<UpdateModel> response) {
                //pd.dismiss();
                if (response.code() == 200)
                    if (response.body() != null)
                        if (response.body().getResult().equalsIgnoreCase("true")) {
                            Toast.makeText(context, "You Assigned in this Order ", Toast.LENGTH_SHORT).show();
                            cancelOrder(orderId, "assign_to_delivery_boy", "", true);
                            context.startActivity(new Intent(context, DeliveryBoyHomeActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            );

                        } else {
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        }
            }

            @Override
            public void onFailure(@NonNull Call<UpdateModel> call, @NonNull Throwable t) {
                pd.dismiss();
            }
        });

    }


    @Override
    public int getItemCount() {
        return count;
    }


    private void showOrderDetails(List<MyOrderModel.MyOrderData.MyOrderItem> data, String totalPrice, String id, String status, String OId) {

        RoundedBottomSheetDialog mBottomSheetDialog = new RoundedBottomSheetDialog(context);
        View sheetView = mBottomSheetDialog.getLayoutInflater().inflate(R.layout.my_order_details, null);

        mBottomSheetDialog.setContentView(sheetView);

        NeumorphCardView cancle = mBottomSheetDialog.findViewById(R.id.cancle);
        NeumorphCardView canceled = mBottomSheetDialog.findViewById(R.id.canceled);
        NeumorphCardView track = mBottomSheetDialog.findViewById(R.id.track);
        TextView total_price = mBottomSheetDialog.findViewById(R.id.total_price);
        NeumorphCardView accept = mBottomSheetDialog.findViewById(R.id.accept);
        LinearLayout linear = mBottomSheetDialog.findViewById(R.id.linear_for_admin);
        RecyclerView recyclerView = mBottomSheetDialog.findViewById(R.id.recyclerView);
        CheckBox terms_con_checkbox = mBottomSheetDialog.findViewById(R.id.terms_con_checkbox);
        EditText edit_regioun = mBottomSheetDialog.findViewById(R.id.edit_regioun);
        LinearLayout cancele_description_linar = mBottomSheetDialog.findViewById(R.id.cancele_description_linar);
        NeumorphCardView assign_delivery_boy = mBottomSheetDialog.findViewById(R.id.assign_delivery_boy);

        if (status.equalsIgnoreCase("cancle")) {
            canceled.setVisibility(View.VISIBLE);
            cancle.setVisibility(View.GONE);
            assign_delivery_boy.setVisibility(View.GONE);
            track.setVisibility(View.GONE);
        }

        total_price.setText("Total - Rs." + totalPrice);

        if (session.getType().equalsIgnoreCase("admin")) {
            linear.setVisibility(View.VISIBLE);
            assign_delivery_boy.setVisibility(View.VISIBLE);

            assign_delivery_boy.setOnClickListener(view -> context.startActivity(new Intent(context, ChooseDeliveryBoyActivity.class)
                    .putExtra("order_id", OId)
                    .putExtra("id", id)
            ));

        } else if (session.getType().equalsIgnoreCase("delivery_boy")) {
            track.setVisibility(View.VISIBLE);
        }


        terms_con_checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> isOk = isChecked);


        cancle.setOnClickListener(v -> {

            if (isOk) {
                cancele_description_linar.setVisibility(View.VISIBLE);
                if (edit_regioun.getText().toString().equalsIgnoreCase("")) {
                    edit_regioun.setError("Enter Cancel Description");
                    edit_regioun.requestFocus();
                } else {
                    cancelOrder(id, "cancle", edit_regioun.getText().toString(), false);
                    mBottomSheetDialog.dismiss();
                }

            } else {
                cancele_description_linar.setVisibility(View.GONE);
                Toast.makeText(context, "Please Accept Terms and condition ", Toast.LENGTH_SHORT).show();
            }

        });
        track.setOnClickListener(v -> {
            context.startActivity(new Intent(context, TrackOrderActivity.class)
                    .putExtra("order_id", id));
            mBottomSheetDialog.dismiss();
        });
        accept.setOnClickListener(v -> {
            cancelOrder(id, "assign_to_delivery_boy", "", false);
            mBottomSheetDialog.dismiss();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new OrderDetailsAdapter(context, data));

        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();


    }

    private void cancelOrder(String id, String status, String desc, boolean isdel) {
        pd.show();
        apiInterface.updateStatus(id, status, desc).enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {
                pd.dismiss();
                if (response.code() == 200)
                    if (response.body() != null)
                        if (response.body().getResult().equalsIgnoreCase("true")) {
                            Toast.makeText(context, "Updated...", Toast.LENGTH_SHORT).show();

                            if (!isdel) {
                                context.startActivity(new Intent(context, HomeActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                );
                            }
                        }

            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                pd.dismiss();
                Log.e("TAG", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecentDataLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RecentDataLayoutBinding.bind(itemView);
        }
    }
}
