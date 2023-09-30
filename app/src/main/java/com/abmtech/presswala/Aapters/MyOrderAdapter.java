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

import com.abmtech.presswala.Activity.MyOrdersActivity;
import com.abmtech.presswala.Activity.TrackOrderActivity;
import com.abmtech.presswala.Apis.ApiInterface;
import com.abmtech.presswala.Apis.RetrofitClient;
import com.abmtech.presswala.Models.MyOrderModel;
import com.abmtech.presswala.Models.OrderData;
import com.abmtech.presswala.Models.OrderModel;
import com.abmtech.presswala.Models.UpdateModel;
import com.abmtech.presswala.R;
import com.abmtech.presswala.Session.Session;
import com.abmtech.presswala.Utills.ProgressDialog;
import com.abmtech.presswala.databinding.MyOrdersLayoutBinding;
import com.deishelon.roundedbottomsheet.RoundedBottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import soup.neumorphism.NeumorphCardView;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    Context context;
    ///List<OrderModel> models;
    Session session;
    List<MyOrderModel.MyOrderData> models;
    ApiInterface apiInterface;
    ProgressDialog pd;
    int count = 0;
    boolean isOk = false;


    public MyOrderAdapter(Context context, List<MyOrderModel.MyOrderData> models, int count) {
        this.context = context;
        this.models = models;
        session = new Session(context);
        apiInterface = RetrofitClient.getClient(context);
        pd = new ProgressDialog(context);
        this.count = count;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.my_orders_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.orderId.setText(models.get(position).getOrderId());
        holder.itemView.setOnClickListener(view -> {
            showOrderDetails(models.get(position).getItems(), models.get(position).getTotalPrice(), models.get(position).getOrderId(), models.get(position).getOrderStatus());
        });


    }

    private void showOrderDetails(List<MyOrderModel.MyOrderData.MyOrderItem> data, String totalPrice, String id, String status) {


        RoundedBottomSheetDialog mBottomSheetDialog = new RoundedBottomSheetDialog(context);
        View sheetView = mBottomSheetDialog.getLayoutInflater().inflate(R.layout.my_order_details, null);

        mBottomSheetDialog.setContentView(sheetView);

        NeumorphCardView cancle = mBottomSheetDialog.findViewById(R.id.cancle);
        CheckBox terms_con_checkbox = mBottomSheetDialog.findViewById(R.id.terms_con_checkbox);
        EditText edit_regioun = mBottomSheetDialog.findViewById(R.id.edit_regioun);
        NeumorphCardView canceled = mBottomSheetDialog.findViewById(R.id.canceled);
        NeumorphCardView track = mBottomSheetDialog.findViewById(R.id.track);
        TextView total_price = mBottomSheetDialog.findViewById(R.id.total_price);
        NeumorphCardView accept = mBottomSheetDialog.findViewById(R.id.accept);
        LinearLayout linear = mBottomSheetDialog.findViewById(R.id.linear_for_admin);
        LinearLayout cancele_description_linar = mBottomSheetDialog.findViewById(R.id.cancele_description_linar);
        RecyclerView recyclerView = mBottomSheetDialog.findViewById(R.id.recyclerView);

        if (status.equalsIgnoreCase("cancle")) {
            canceled.setVisibility(View.VISIBLE);
            cancle.setVisibility(View.GONE);
        }

        terms_con_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isOk = isChecked;
            }
        });

        total_price.setText("Total - Rs." + totalPrice);

        if (session.getType().equalsIgnoreCase("admin")) {
            linear.setVisibility(View.VISIBLE);
        } else if (session.getType().equalsIgnoreCase("delivery_boy")) {
            track.setVisibility(View.VISIBLE);
        }

        cancle.setOnClickListener(v -> {

            if (isOk) {
                cancele_description_linar.setVisibility(View.VISIBLE);
                if (edit_regioun.getText().toString().equalsIgnoreCase("")) {
                    edit_regioun.setError("Enter Cancel Description");
                    edit_regioun.requestFocus();
                } else {
                    cancelOrder(id, "cancle", edit_regioun.getText().toString());
                }

            } else {
                cancele_description_linar.setVisibility(View.GONE);
                Toast.makeText(context, "Please Accept Terms and condition ", Toast.LENGTH_SHORT).show();
            }

        });

        track.setOnClickListener(v -> context.startActivity(new Intent(context, TrackOrderActivity.class)
                .putExtra("order_id",id)));

        accept.setOnClickListener(v -> cancelOrder(id, "assign_to_delivery_boy", ""));

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new OrderDetailsAdapter(context, data));

        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();

    }

    private void cancelOrder(String id, String status, String des) {
        pd.show();
        apiInterface.updateStatus(id, status, des).enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(@NonNull Call<UpdateModel> call, @NonNull Response<UpdateModel> response) {
                pd.dismiss();
                if (response.code() == 200)
                    if (response.body() != null)
                        if (response.body().getResult().equalsIgnoreCase("true")) {
                            Toast.makeText(context, "Updated...", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, MyOrdersActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            );
                            isOk = false;
                        }

            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                pd.dismiss();
                Log.e("TAG", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });

    }


    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MyOrdersLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = MyOrdersLayoutBinding.bind(itemView);
        }
    }
}
