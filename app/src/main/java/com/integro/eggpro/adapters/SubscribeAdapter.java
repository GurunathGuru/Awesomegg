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

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.integro.eggpro.R;
import com.integro.eggpro.helpers.ParseObjects;
import com.integro.eggpro.utility.entity.CartItem;
import com.integro.eggpro.utility.entity.Product;
import com.integro.eggpro.utility.viewmodels.CartViewModel;
import com.integro.eggpro.utility.viewmodels.ProductsViewModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SubscribeAdapter extends RecyclerView.Adapter<SubscribeAdapter.MyViewHoder> {

    Context context;
    ArrayList<CartItem> cart;
    ArrayList<Product> productList;

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

        Picasso.with(context)
                .load(item.getProductImage())
                .resize(60, 68)
                .centerCrop()
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
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }
}
