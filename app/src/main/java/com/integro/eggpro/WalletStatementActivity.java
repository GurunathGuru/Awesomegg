package com.integro.eggpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.integro.eggpro.adapters.MyOrdersAdapter;
import com.integro.eggpro.adapters.WalletStatementAdapter;
import com.integro.eggpro.apis.ApiClient;
import com.integro.eggpro.apis.ApiService;
import com.integro.eggpro.model.MyOrderList;
import com.integro.eggpro.model.WalletStatement;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletStatementActivity extends AppCompatActivity {

    @BindView(R.id.rvWallet)
    RecyclerView rvWallet;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = mAuth.getCurrentUser();
    private static final String TAG = "MyOrdersActivity";
    WalletStatementAdapter walletStatementAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_statement);
        ButterKnife.bind(this);

        rvWallet.setLayoutManager(new LinearLayoutManager(WalletStatementActivity.this));
        walletStatementAdapter = new WalletStatementAdapter(WalletStatementActivity.this);
        rvWallet.setAdapter(walletStatementAdapter);

        getWalletStatement();
    }

    public void getWalletStatement() {
        ApiClient.getClient2().create(ApiService.class).getWalletStatementList(firebaseUser.getUid()).enqueue(new Callback<ArrayList<WalletStatement>>() {
            @Override
            public void onResponse(Call<ArrayList<WalletStatement>> call, Response<ArrayList<WalletStatement>> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "onResponse: " + response.errorBody());
                    return;
                }
                if (response.body() == null) {
                    Log.i(TAG, "onResponse: " + response.body());
                    return;
                }
                Log.i(TAG, "onResponse: " + response.body());

                walletStatementAdapter.setWalletStatements(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<WalletStatement>> call, Throwable t) {
                Toast.makeText(WalletStatementActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
