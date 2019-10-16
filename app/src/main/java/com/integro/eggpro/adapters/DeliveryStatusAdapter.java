package com.integro.eggpro.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.integro.eggpro.R;
import com.integro.eggpro.model.DeliveryStatus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DeliveryStatusAdapter extends RecyclerView.Adapter<DeliveryStatusAdapter.MyViewHolder> {

    ArrayList<DeliveryStatus> deliveryStatus =new ArrayList<>();
    String uid;
    Context context;

    private static final String TAG = "DeliveryStatusAdapter";
    public DeliveryStatusAdapter(Context context, String uid) {
        this.uid = uid;
        this.context = context;
    }

    public void setDeliveryStatus(ArrayList<DeliveryStatus> deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.card_delivery,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.orderId.setText("order Id: "+deliveryStatus.get(position).getId());
        holder.orderDate.setText(deliveryStatus.get(position).getServeDate());
        holder.orderName.setText(deliveryStatus.get(position).getProdName());
        holder.itemQty.setText(deliveryStatus.get(position).getItemQty());
        if (deliveryStatus.get(position).getStatus().equals("Order Delivered")){
            holder.tvStatus.setTextColor((Color.parseColor("#5BB100")));
        }else {
            holder.tvStatus.setTextColor((Color.parseColor("#000000")));
        }
        holder.tvStatus.setText(deliveryStatus.get(position).getStatus());
        Log.i(TAG, "onBindViewHolder: "+deliveryStatus.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return deliveryStatus.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.orderId)
        TextView orderId;

        @BindView(R.id.orderDate)
        TextView orderDate;

        @BindView(R.id.orderName)
        TextView orderName;

        @BindView(R.id.itemQty)
        TextView itemQty;

        @BindView(R.id.tvStatus)
        TextView tvStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
