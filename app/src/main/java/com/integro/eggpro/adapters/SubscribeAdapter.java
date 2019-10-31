package com.integro.eggpro.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.integro.eggpro.R;
import com.integro.eggpro.helpers.ParseObjects;
import com.integro.eggpro.utility.entity.CartItem;
import com.integro.eggpro.utility.entity.Product;
import com.integro.eggpro.utility.viewmodels.CartViewModel;
import com.integro.eggpro.utility.viewmodels.ProductsViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SubscribeAdapter extends RecyclerView.Adapter<SubscribeAdapter.MyViewHoder> {

    Context context;
    ArrayList<CartItem> cart = new ArrayList<>();
    ArrayList<Product> productList = new ArrayList<>();
    private static final String TAG = "SubscribeAdapter";

    CartViewModel cartViewModel;
    ProductsViewModel productsViewModel;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public SubscribeAdapter(Context context) {
        this.context = context;
        FragmentActivity activity = (FragmentActivity) context;
        cartViewModel = ViewModelProviders.of(activity).get(CartViewModel.class);
        productsViewModel = ViewModelProviders.of(activity).get(ProductsViewModel.class);
        cartViewModel.getCart().observe(activity, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                Log.i(TAG, "onChanged: " + cartItems);
                cart = (ArrayList<CartItem>) cartItems;
                notifyDataSetChanged();
            }
        });
        productsViewModel.getProducts().observe(activity, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                productList = (ArrayList<Product>) products;
            }
        });
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

        final CartItem item = cart.get(position);

        holder.tvName.setText(item.getProdName());
        holder.itemQty.setNumber(String.valueOf(item.getItemQty()));
        holder.itemQty.setRange(0,item.getProdStock());
        holder.tvPrice.setText(context.getResources().getString(R.string.itemPrice, decimalFormat.format((item.getProdSellingPrice() * item.getItemQty()))));
        holder.itemQty.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                item.setItemQty(newValue);
                cartViewModel.updateItem(item);
                Log.i(TAG, "onValueChange: " + item.toProduct());
                productsViewModel.updateProduct(ParseObjects.toProduct(item));
                if (newValue == 0) {
                    cartViewModel.removeItem(item);
                }
                Log.i(TAG, "onBindViewHolder: " + item.getItemQty());
            }
        });

        Glide.with(context)
                .load(item.getProductImage())
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return cart.size();
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
            tvPrice = itemView.findViewById(R.id.tvSellingPrice);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }
}
