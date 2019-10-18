package com.integro.eggpro.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.integro.eggpro.DeliveryStatusActivity;
import com.integro.eggpro.OrderDetailsActivity;
import com.integro.eggpro.R;
import com.integro.eggpro.SubscribeActivity;
import com.integro.eggpro.helpers.ParseObjects;
import com.integro.eggpro.model.Items;
import com.integro.eggpro.model.MyOrderList;
import com.integro.eggpro.utility.entity.Product;
import com.integro.eggpro.utility.viewmodels.CartViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.integro.eggpro.constants.GenralConstants.ITEM_LIST;
import static com.integro.eggpro.constants.GenralConstants.ORDER_ID;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyViewHolder> {

    private static final String TAG = "MyOrdersAdapter";
    Context context;
    ArrayList<MyOrderList> orderList = new ArrayList<>();
    ArrayList<Items> items = new ArrayList<>();
    int id;
    String productImage;
    Double prodSellingPrice;
    String prodName;
    String prodDescription;
    int prodQty;
    Double prodListingPrice;
    int additionalDiscount;
    int prodStock;
    int itemQty;

    public MyOrdersAdapter(Context context) {
        this.context = context;
    }

    public void setOrderList(ArrayList<MyOrderList> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_order, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String getDate = orderList.get(position).getStartDate();

        String[] timeStampArray = getDate.split(" ");
        String[] dateArray = timeStampArray[0].split("-");
        String[] timeArray = timeStampArray[1].split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]));
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]));

        calendar.set(Calendar.HOUR, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, Integer.parseInt(timeArray[2]));

        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy, hh.mm a");
        Log.i(TAG, "onBindViewHolder: " + format.format(calendar.getTime()));

        holder.orderId.setText(orderList.get(position).getId());
        holder.orderDate.setText(format.format(calendar.getTime()));
        holder.orderType.setText(orderList.get(position).getOrderType());
        holder.orderPrice.setText("\u20B9" + orderList.get(position).getOrderPrice());

        holder.tvOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra(ITEM_LIST, orderList.get(position).getItems());
                context.startActivity(intent);
            }
        });

        holder.tvDeliveryStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DeliveryStatusActivity.class);
                intent.putExtra(ORDER_ID, orderList.get(position).getId());
                context.startActivity(intent);
            }
        });

        holder.tvRecharge.setOnClickListener(new View.OnClickListener() {
            CartViewModel cartViewModel;
            @Override
            public void onClick(View v) {
                items = orderList.get(position).getItems();
                for (int i = 0; i < items.size(); i++) {
                    id = Integer.parseInt(items.get(i).getId());
                    productImage = items.get(i).getProductImage();
                    prodSellingPrice = Double.valueOf(items.get(i).getProdSellingPrice());
                    prodName = items.get(i).getProdName();
                    prodDescription = items.get(i).getId();
                    prodQty = Integer.parseInt(items.get(i).getProdQty());
                    prodListingPrice = Double.valueOf(items.get(i).getProdListingPrice());
                    additionalDiscount = Integer.parseInt(items.get(i).getAdditionalDiscount());
                    prodStock = Integer.parseInt(items.get(i).getProdStock());
                    itemQty = Integer.parseInt(items.get(i).getItemQty());

                    Product product = new Product(id, productImage, prodSellingPrice, prodName, prodDescription, prodQty, prodListingPrice, additionalDiscount, prodStock, itemQty);
                    cartViewModel = ViewModelProviders.of((FragmentActivity) context).get(CartViewModel.class);
                    cartViewModel.addItem(ParseObjects.toCartItem(product));
                }
                Intent intent = new Intent(context, SubscribeActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.orderId)
        TextView orderId;

        @BindView(R.id.orderDate)
        TextView orderDate;

        @BindView(R.id.orderType)
        TextView orderType;

        @BindView(R.id.orderPrice)
        TextView orderPrice;

        @BindView(R.id.tvOrderDetails)
        TextView tvOrderDetails;

        @BindView(R.id.tvDeliveryStatus)
        TextView tvDeliveryStatus;

        @BindView(R.id.tvRecharge)
        TextView tvRecharge;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
