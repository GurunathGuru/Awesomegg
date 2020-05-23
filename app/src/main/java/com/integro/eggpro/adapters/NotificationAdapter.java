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
import com.integro.eggpro.model.Notifications;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Notifications> notificationsArrayList;

    public NotificationAdapter(Context context, ArrayList<Notifications> notificationsArrayList) {
        this.context = context;
        this.notificationsArrayList = notificationsArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.card_recipes,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Notifications notifications=notificationsArrayList.get(position);

        Glide.with(context).load(notifications.getImg()).into(holder.ivImage);

        holder.tvTitle.setText(notifications.getTitle());
        holder.tvDescription.setText(notifications.getMessage());
    }

    @Override
    public int getItemCount() {
        return notificationsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvTitle;
        TextView tvDescription;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage=itemView.findViewById(R.id.ivImage);
            tvTitle=itemView.findViewById(R.id.tvTitle);
            tvDescription=itemView.findViewById(R.id.tvDescription);
        }
    }
}
