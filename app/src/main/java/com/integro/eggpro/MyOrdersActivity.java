package com.integro.eggpro;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.integro.eggpro.adapters.MyOrdersAdapter;
import com.integro.eggpro.apis.ApiClient;
import com.integro.eggpro.apis.ApiService;
import com.integro.eggpro.interfaces.BalanceCallBack;
import com.integro.eggpro.model.MyOrderList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrdersActivity extends AppCompatActivity {

    private static final String TAG = "MyOrdersActivity";
    @BindView(R.id.rvMyOrders)
    RecyclerView rvMyOrder;
    @BindView(R.id.tvNoOrders)
    TextView tvNoOrders;

    MyOrdersAdapter myOrdersAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = mAuth.getCurrentUser();
    private BalanceCallBack balanceCallBack = new BalanceCallBack() {
        @Override
        public void onBalanceRetrieved(Double balance) {
            getMyOrdersList(balance);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        ButterKnife.bind(this);

        rvMyOrder.setLayoutManager(new LinearLayoutManager(MyOrdersActivity.this));
        myOrdersAdapter = new MyOrdersAdapter(MyOrdersActivity.this);
        rvMyOrder.setAdapter(myOrdersAdapter);
        getCurrentBalance(balanceCallBack);
    }


    public void getCurrentBalance(BalanceCallBack callBack) {
        ApiClient.getClient2().create(ApiService.class).getCurrentBalance(firebaseUser.getUid()).enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.body() == null) {
                    return;
                }
                callBack.onBalanceRetrieved(response.body());
                Log.i(TAG, "onResponse: 1 " + response.body());
            }
            @Override
            public void onFailure(Call<Double> call, Throwable t) {

            }
        });
    }

    public void getMyOrdersList(Double balance) {
        ApiClient.getClient2().create(ApiService.class).getMyOrderList(firebaseUser.getUid()).enqueue(new Callback<ArrayList<MyOrderList>>() {
            @Override
            public void onResponse(Call<ArrayList<MyOrderList>> call, Response<ArrayList<MyOrderList>> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "onResponse: 2 " + response.errorBody());
                    return;
                }
                if (response.body() == null) {
                    Log.i(TAG, "onResponse: 3 " + response.body());
                    return;
                }
                if (response.body().size() == 0) {
                    Log.i(TAG, "onResponse: 3 " + response.body());
                    rvMyOrder.setVisibility(View.GONE);
                    tvNoOrders.setVisibility(View.VISIBLE);
                }
                Log.i(TAG, "onResponse:4 " + response.body().size());
                myOrdersAdapter.setBalance(balance);
                myOrdersAdapter.setOrderList(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<MyOrderList>> call, Throwable t) {
                Toast.makeText(MyOrdersActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
