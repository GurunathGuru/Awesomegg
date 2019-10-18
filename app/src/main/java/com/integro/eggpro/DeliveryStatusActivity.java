package com.integro.eggpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.integro.eggpro.adapters.DeliveryStatusAdapter;
import com.integro.eggpro.apis.ApiClient;
import com.integro.eggpro.apis.ApiService;
import com.integro.eggpro.model.DeliveryStatus;
import com.integro.eggpro.model.WalletStatement;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.integro.eggpro.constants.GenralConstants.ITEM_LIST;
import static com.integro.eggpro.constants.GenralConstants.ORDER_ID;

public class DeliveryStatusActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = mAuth.getCurrentUser();

    @BindView(R.id.rvDeliveryStatus)
    RecyclerView rvDeliveryStatus;

    DeliveryStatusAdapter deliveryStatusAdapter;

    String orderId;
    String uid;

    private static final String TAG = "DeliveryStatusActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_status);
        ButterKnife.bind(this);

        orderId = (String) getIntent().getSerializableExtra(ORDER_ID);
        Log.i(TAG, "onCreate: orderId "+orderId);
        uid=firebaseUser.getUid();
        Log.i(TAG, "onCreate: uid "+uid);
        deliveryStatusAdapter = new DeliveryStatusAdapter(this,uid);
        rvDeliveryStatus.setLayoutManager(new LinearLayoutManager(this));
        rvDeliveryStatus.setAdapter(deliveryStatusAdapter);
        getDeliveryStatus();

    }

    public void getDeliveryStatus() {
        ApiClient.getClient2().create(ApiService.class).getDeliveryStatus(uid,orderId).enqueue(new Callback<ArrayList<DeliveryStatus>>() {
            @Override
            public void onResponse(Call<ArrayList<DeliveryStatus>> call, Response<ArrayList<DeliveryStatus>> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "onResponse: " + response.errorBody());
                    return;
                }
                if (response.body() == null) {
                    Log.i(TAG, "onResponse: " + response.body());
                    return;
                }
                Log.i(TAG, "onResponse: " + response.body());

                deliveryStatusAdapter.setDeliveryStatus(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<DeliveryStatus>> call, Throwable t) {
                Toast.makeText(DeliveryStatusActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

