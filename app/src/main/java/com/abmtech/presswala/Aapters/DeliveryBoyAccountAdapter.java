package com.abmtech.presswala.Aapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abmtech.presswala.Activity.UpdateDeliveryBoyProfileActivity;
import com.abmtech.presswala.Apis.OnDeliveryBoySelected;
import com.abmtech.presswala.Models.UsersModel;
import com.abmtech.presswala.R;
import com.abmtech.presswala.databinding.UsersLayouBinding;

import java.util.List;

public class DeliveryBoyAccountAdapter extends RecyclerView.Adapter<DeliveryBoyAccountAdapter.ViewHolder> {

    Context context;
    List<UsersModel> models;
    int type;
    int isOneSelected = -1;
    OnDeliveryBoySelected onDeliveryBoySelected;

    public DeliveryBoyAccountAdapter(Context context, List<UsersModel> models, int type, OnDeliveryBoySelected onDeliveryBoySelected) {
        this.context = context;
        this.models = models;
        this.type = type;
        this.onDeliveryBoySelected = onDeliveryBoySelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.users_layou, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.binding.userMobile.setText(models.get(position).getUserPhone());
        holder.binding.userName.setText(models.get(position).getUserName());

        holder.itemView.setOnClickListener(view -> {
            if (type == 1) {


                if (isOneSelected == -1) {
                    if (models.get(position).isSelected()) {
                        onDeliveryBoySelected.onRemove();
                        holder.binding.linearLayout.setBackgroundResource(R.drawable.unselected_delivery_boy_bg);
                        models.get(position).setSelected(false);
                        isOneSelected = position;
                    } else {
                        onDeliveryBoySelected.onSelected(models.get(position));
                        holder.binding.linearLayout.setBackgroundResource(R.drawable.selected_delivery_boy_bg);
                        models.get(position).setSelected(true);
                        isOneSelected = position;
                    }
                } else {
                    models.get(isOneSelected).setSelected(false);
                    holder.binding.linearLayout.setBackgroundResource(R.drawable.unselected_delivery_boy_bg);
                    onDeliveryBoySelected.onRemove();
                    isOneSelected = -1;
                }


            } else {
                holder.itemView.setOnClickListener(view1 -> context.startActivity(new Intent(context, UpdateDeliveryBoyProfileActivity.class).putExtra("u_id", models.get(position).getUserId())));
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        UsersLayouBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = UsersLayouBinding.bind(itemView);
        }
    }

}
