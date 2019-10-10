package com.integro.eggpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.integro.eggpro.apis.ApiClient;
import com.integro.eggpro.apis.ApiService;
import com.integro.eggpro.model.MyOrderList;
import com.integro.eggpro.adapters.MyOrdersAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyOrdersActivity extends AppCompatActivity {

    @BindView(R.id.rvMyOrders)
    RecyclerView rvMyOrder;

    private static final String TAG = "MyOrdersActivity";
    MyOrdersAdapter myOrdersAdapter;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        ButterKnife.bind(this);

        rvMyOrder.setLayoutManager(new LinearLayoutManager(MyOrdersActivity.this));
        myOrdersAdapter = new MyOrdersAdapter(MyOrdersActivity.this);
        rvMyOrder.setAdapter(myOrdersAdapter);

        getMyOrdersList();
    }

    public void getMyOrdersList() {
        ApiClient.getClient2().create(ApiService.class).getMyOrderList(firebaseUser.getUid()).enqueue(new Callback<ArrayList<MyOrderList>>() {
            @Override
            public void onResponse(Call<ArrayList<MyOrderList>> call, Response<ArrayList<MyOrderList>> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "onResponse: " + response.errorBody());
                    return;
                }
                if (response.body() == null) {
                    Log.i(TAG, "onResponse: " + response.body());
                    return;
                }
                Log.i(TAG, "onResponse: " + response.body());

                myOrdersAdapter.setOrderList(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<MyOrderList>> call, Throwable t) {
                Toast.makeText(MyOrdersActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
