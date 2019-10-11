package com.integro.eggpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.integro.eggpro.R;
import com.integro.eggpro.model.Items;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderItemListAdapter extends RecyclerView.Adapter<OrderItemListAdapter.MyViewHolder> {
    Context context;
    ArrayList<Items> items;

    DecimalFormat format =new DecimalFormat("0.00");

    public OrderItemListAdapter(ArrayList<Items> items, Context context) {
        this.items=items;
        this.context=context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.card_item3,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvName.setText(items.get(position).getProdName());
        holder.tvSellingPrice.setText("\u20B9"+format.format(Double.valueOf(items.get(position).getProdSellingPrice())*Integer.parseInt(items.get(position).getItemQty())));
        holder.itemQty.setText(items.get(position).getItemQty());
        holder.tvTotalPrice.setText("\u20B9"+items.get(position).getProdSellingPrice());
        holder.tvQuantity.setText(items.get(position).getProdQty());
        holder.tvListingPrice.setText("\u20B9"+items.get(position).getProdListingPrice());

        Glide.with(context).
                load(items.get(position).getProductImage())
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvSellingPrice;
        TextView itemQty;
        TextView tvTotalPrice;
        ImageView ivImage;
        TextView tvQuantity;
        TextView tvListingPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemQty = itemView.findViewById(R.id.itemQty);
            tvName = itemView.findViewById(R.id.tvName);
            tvSellingPrice = itemView.findViewById(R.id.tvSellingPrice);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            tvListingPrice=itemView.findViewById(R.id.tvListingPrice);
        }
    }
}
