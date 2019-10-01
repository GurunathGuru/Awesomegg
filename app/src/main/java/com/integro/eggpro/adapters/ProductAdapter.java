package com.integro.eggpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.integro.eggpro.R;
import com.integro.eggpro.interfaces.QuantityChangedListener;
import com.integro.eggpro.interfaces.SubscribeClickListener;
import com.integro.eggpro.model.Products;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    public SubscribeClickListener clickListener;
    public QuantityChangedListener quantityChangedListener;

    private DecimalFormat decimalFormat=new DecimalFormat("0.00");

    ArrayList<Products> addItemArrayList = new ArrayList<>();
    private Context context;

    public SubscribeClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(SubscribeClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public QuantityChangedListener getQuantityChangedListener() {
        return quantityChangedListener;
    }

    public void setQuantityChangedListener(QuantityChangedListener quantityChangedListener) {
        this.quantityChangedListener = quantityChangedListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        final Products products = addItemArrayList.get(i);

        myViewHolder.tvName.setText(products.getProdName());
        myViewHolder.tvPrice.setText("\u20B9 "+(products.getProdSellingPrice()*products.getProdQty()));
        myViewHolder.tvQuantity.setText(context.getResources().getString(R.string.productQuantity,products.getProdQty()));
        myViewHolder.tvListingPrice.setText(context.getResources().getString(R.string.productListingPrice,decimalFormat.format((products.getProdListingPrice()*products.getProdQty()))));
        final Double price = Double.valueOf(products.getProdSellingPrice());

        Picasso.with(context)
                .load(products.getProductImage())
                .resize(60, 68)
                .centerCrop()
                .into(myViewHolder.ivImage);

        myViewHolder.itemQty.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                quantityChangedListener.onItermQuantityChanged(oldValue, newValue ,products);
                myViewHolder.tvTotalPrice.setText("\u20B9 "+(newValue * (Double.valueOf(products.getProdSellingPrice()*products.getProdQty()))));
            }
        });

        myViewHolder.tvSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClickListener(products);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addItemArrayList.size();
    }

    public void setAddItemArrayList(ArrayList<Products> addItemArrayList) {
        this.addItemArrayList = addItemArrayList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ElegantNumberButton itemQty;
        //TextView tvTotalCount1;
        TextView tvOrderNow;
        TextView tvSubscribe;
        TextView tvName;
        TextView tvPrice;
        TextView tvTotalPrice;
        ImageView ivImage;
        TextView tvQuantity;
        TextView tvListingPrice;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemQty = itemView.findViewById(R.id.itemQty);
            //tvTotalCount1 = itemView.findViewById(R.id.tvTotalCount1);
            tvOrderNow = itemView.findViewById(R.id.tvOrderNow);
            tvSubscribe = itemView.findViewById(R.id.tvSubscribe);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            tvListingPrice=itemView.findViewById(R.id.tvListingPrice);
        }
    }
}
