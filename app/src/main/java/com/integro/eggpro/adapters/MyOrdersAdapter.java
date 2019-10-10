package com.integro.eggpro.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.integro.eggpro.OrderDetailsActivity;
import com.integro.eggpro.R;
import com.integro.eggpro.model.MyOrderList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.integro.eggpro.constants.GenralConstants.ITEM_LIST;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyViewHolder> {


    Context context;
    ArrayList<MyOrderList> orderList = new ArrayList<>();

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
        holder.orderId.setText(orderList.get(position).getId());
        holder.orderDate.setText(orderList.get(position).getStartDate());
        holder.orderType.setText("Order Type: "+orderList.get(position).getOrderType());
        holder.orderPrice.setText("Order Price: \u20B9 "+orderList.get(position).getOrderPrice());

        holder.llMyOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, OrderDetailsActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("",orderList.get(position).getItems());
                intent.putExtra(ITEM_LIST,bundle);
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

        @BindView(R.id.llMyOder)
        LinearLayout llMyOder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
