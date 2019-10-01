package com.integro.eggpro.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.integro.eggpro.R;
import com.integro.eggpro.model.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SubscribeAdapter extends RecyclerView.Adapter<SubscribeAdapter.MyViewHoder> {

    Context context;
    ArrayList<Products> productsArrayList;

    public SubscribeAdapter(Context context, ArrayList<Products> productsArrayList) {
        this.context = context;
        this.productsArrayList = productsArrayList;
    }

    @NonNull
    @Override
    public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item2, parent, false);
        return new MyViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {

        final Products products = productsArrayList.get(position);
        holder.tvName.setText(products.getProdName());
        holder.itemQty.setNumber(String.valueOf(products.getProdQty()));
        int num= Integer.parseInt(String.valueOf(products.getProdQty()));
        holder.tvPrice.setText("\u20B9 "+(num * (Double.valueOf(products.getProdSellingPrice()*products.getProdQty()))));

        holder.itemQty.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                holder.tvPrice.setText("Rs: " + String.valueOf(products.getProdSellingPrice()*newValue*products.getProdQty()));
                Log.i(TAG, "onBindViewHolder: "+products.getProdQty());
            }
        });

        Picasso.with(context)
                .load(products.getProductImage())
                .resize(60, 68)
                .centerCrop()
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return productsArrayList.size();
    }

    public class MyViewHoder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        ElegantNumberButton itemQty;
        TextView tvName;
        TextView tvPrice;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            itemQty = itemView.findViewById(R.id.itemQty);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }
}
