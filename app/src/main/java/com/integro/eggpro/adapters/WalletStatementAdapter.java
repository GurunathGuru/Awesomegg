package com.integro.eggpro.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.integro.eggpro.R;
import com.integro.eggpro.model.WalletStatement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletStatementAdapter extends RecyclerView.Adapter<WalletStatementAdapter.MyViewHolder> {

    Context context;
    ArrayList<WalletStatement> walletStatements= new ArrayList<>();
    private static final String TAG = "WalletStatementAdapter";
    public WalletStatementAdapter(Context context) {
        this.context = context;
    }

    public void setWalletStatements(ArrayList<WalletStatement> walletStatements){
        this.walletStatements=walletStatements;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.card_wallet,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        String getDate = walletStatements.get(position).getDate();

        String[] timeStampArray = getDate.split(" ");
        String[] dateArray = timeStampArray[0].split("-");
        String[] timeArray = timeStampArray[1].split(":");

        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,Integer.parseInt(dateArray[0]));
        calendar.set(Calendar.MONTH,Integer.parseInt(dateArray[1]));
        calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dateArray[2]));

        calendar.set(Calendar.HOUR,Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE,Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND,Integer.parseInt(timeArray[2]));

        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy, hh.mm a");
        Log.i(TAG, "onBindViewHolder: "+format.format(calendar.getTime()));

        holder.tvTitle.setText(walletStatements.get(position).getDescription());
        if ((walletStatements.get(position).getCredit())!=null){
            holder.tvAmount.setText("+"+walletStatements.get(position).getCredit());
            holder.tvAmount.setTextColor((Color.parseColor("#5BB100")));
        }
        if (walletStatements.get(position).getDebit()!=null){
            holder.tvAmount.setText("-"+walletStatements.get(position).getDebit());
            holder.tvAmount.setTextColor((Color.parseColor("#000000")));
        }
        holder.tvDate.setText(format.format(calendar.getTime()));
        holder.tvClosingBalance.setText("closing Balance: \u0020\u20B9\u0020"+walletStatements.get(position).getBalance());
    }

    @Override
    public int getItemCount() {
        return walletStatements.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView( R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvAmount)
        TextView tvAmount;

        @BindView(R.id.tvDate)
        TextView tvDate;

        @BindView(R.id.tvClosingBalance)
        TextView tvClosingBalance;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
