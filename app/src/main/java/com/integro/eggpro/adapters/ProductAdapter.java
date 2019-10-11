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
import com.integro.eggpro.interfaces.QuantityChangedListener;
import com.integro.eggpro.interfaces.SubscribeClickListener;
import com.integro.eggpro.utility.entity.CartItem;
import com.integro.eggpro.utility.entity.Product;
import com.integro.eggpro.utility.viewmodels.CartViewModel;
import com.integro.eggpro.utility.viewmodels.ProductsViewModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    public SubscribeClickListener clickListener;
    public QuantityChangedListener quantityChangedListener;
    private static final String TAG = "ProductAdapter";
    private ProductsViewModel productsViewModel;
    private CartViewModel cartViewModel;
    private DecimalFormat decimalFormat=new DecimalFormat("0.00");
    ArrayList<Product> productsList = new ArrayList<>();
    ArrayList<CartItem> cart = new ArrayList<>();
    private Context context;

    public ProductAdapter(Context context) {
        FragmentActivity activity = (FragmentActivity) context;
        this.context = context;

        cartViewModel = ViewModelProviders.of(activity).get(CartViewModel.class);
        productsViewModel = ViewModelProviders.of(activity).get(ProductsViewModel.class);

        productsViewModel.getProducts().observe(activity, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                productsList = (ArrayList<Product>) products;
                notifyDataSetChanged();
            }
        });

        cartViewModel.getCart().observe(activity, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                cart = (ArrayList<CartItem>) cartItems;
                Log.i(TAG, "onChanged: Cart Updated");
            }
        });
    }

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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        final Product product = productsList.get(i);

        myViewHolder.tvName.setText(product.getProdName());
        myViewHolder.tvPrice.setText("\u20B9 "+(product.getProdSellingPrice()));
        myViewHolder.tvQuantity.setText(context.getResources().getString(R.string.productQuantity,product.getProdQty()));
        myViewHolder.tvListingPrice.setText(context.getResources().getString(R.string.productListingPrice,decimalFormat.format((product.getProdListingPrice()))));
        myViewHolder.itemQty.setNumber(""+product.getItemQty());
        final Double price = Double.valueOf(product.getProdSellingPrice());

        Glide.with(context)
                .load(product.getProductImage())
                .into(myViewHolder.ivImage);

        myViewHolder.tvTotalPrice.setText(context.getResources().getString(R.string.itemPrice, decimalFormat.format((product.getProdSellingPrice() * product.getItemQty()))));

        myViewHolder.itemQty.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                boolean flag = false;
                for (CartItem item: cart) {
                    if (item.getId() == product.getId()) {
                        flag =true;
                        break;
                    }
                }
                product.setItemQty(newValue);
                productsViewModel.updateProduct(product);
                if (!flag) {
                    cartViewModel.addItem(ParseObjects.toCartItem(product));
                } else {
                    cartViewModel.updateItem(ParseObjects.toCartItem(product));
                    if (newValue==0) {
                        cartViewModel.removeItem(ParseObjects.toCartItem(product));
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ElegantNumberButton itemQty;
        TextView tvName;
        TextView tvPrice;
        TextView tvTotalPrice;
        ImageView ivImage;
        TextView tvQuantity;
        TextView tvListingPrice;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemQty = itemView.findViewById(R.id.itemQty);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvSellingPrice);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            tvListingPrice=itemView.findViewById(R.id.tvListingPrice);
        }
    }
}
