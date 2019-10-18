package com.integro.eggpro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.integro.eggpro.R;
import com.integro.eggpro.interfaces.OnDateSelected;
import com.integro.eggpro.model.CustomDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CustomCalenderAdapter extends RecyclerView.Adapter<CustomCalenderAdapter.CustomCalendarViewHolder>  {

    private ArrayList<CustomDate> customDates = new ArrayList<>();
    private Context context;
    private int selected = -1;
    private OnDateSelected onDateSelected;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE");

    public OnDateSelected getOnDateSelected() {
        return onDateSelected;
    }

    public void initialize(){
        if (customDates.size()==0){
            return;
        }
        customDates.get(0).setSelected(true);
        selected=0;
        notifyDataSetChanged();
    }

    public CustomDate getFirstDate() {
        if (customDates.size()>0) {
            return customDates.get(0);
        }
        else
            return null;
    }

    public void setOnDateSelected(OnDateSelected onDateSelected) {
        this.onDateSelected = onDateSelected;
    }

    public void setCustomDates(ArrayList<CustomDate> customDates) {
        this.customDates = customDates;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomCalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_date, parent, false);
        return new CustomCalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomCalendarViewHolder holder, int position) {
        final CustomDate date = customDates.get(position);
        holder.date.setText(String.valueOf(date.getDate()));
        if (date.isSelected()) {
            holder.date.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.circle));
        } else {
            holder.date.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        }
        holder.day.setText(simpleDateFormat.format(date.getCalendar().getTime()));

        holder.llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected == -1) {
                    selected = position;
                    return;
                }
                refreshState(position);
                onDateSelected.onDateSelected(date);
            }
        });
    }

    private void refreshState(int position) {
        customDates.get(selected).setSelected(false);
        customDates.get(position).setSelected(true);
        selected = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return customDates.size();
    }

    public CustomDate getSelectedDate() {
        if (selected == -1) {
            return customDates.get(0);
        }
        return customDates.get(selected);
    }

    public class CustomCalendarViewHolder extends RecyclerView.ViewHolder {
        private TextView date,day;
        private LinearLayout llDate;
        public CustomCalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            day = itemView.findViewById(R.id.day);
            llDate = itemView.findViewById(R.id.llDate);
        }
    }


}
