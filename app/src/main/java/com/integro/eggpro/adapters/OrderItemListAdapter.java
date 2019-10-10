package com.integro.eggpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.integro.eggpro.R;
import com.integro.eggpro.model.Items;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderItemListAdapter extends RecyclerView.Adapter<OrderItemListAdapter.MyViewHolder> {
    ArrayList<Items> arrayList =new ArrayList<>();
    Context context;

    DecimalFormat format =new DecimalFormat("0.00");

    public OrderItemListAdapter(ArrayList<Items> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.card_item3,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvName.setText(arrayList.get(position).getProdName());
        holder.tvPrice.setText(format.format(Double.valueOf(arrayList.get(position).getItemPrice())*Integer.parseInt(arrayList.get(position).getItemQty())));
        holder.itemQty.setText(arrayList.get(position).getItemQty());
        holder.tvTotalPrice.setText(arrayList.get(position).getItemPrice());
        holder.tvQuantity.setText(arrayList.get(position).getProdQty());
        holder.tvListingPrice.setText(arrayList.get(position).getProdListingPrice());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvPrice;
        TextView itemQty;
        TextView tvTotalPrice;
        ImageView ivImage;
        TextView tvQuantity;
        TextView tvListingPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemQty = itemView.findViewById(R.id.itemQty);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            tvListingPrice=itemView.findViewById(R.id.tvListingPrice);
        }
    }
}
