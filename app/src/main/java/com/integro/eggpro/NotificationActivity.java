package com.integro.eggpro;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.integro.eggpro.adapters.NotificationAdapter;
import com.integro.eggpro.apis.ApiClient;
import com.integro.eggpro.apis.ApiService;
import com.integro.eggpro.model.Notifications;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = "NotificationActivity";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = mAuth.getCurrentUser();
    String uid;

    private RecyclerView rvNotifications;
    private ArrayList<Notifications> notificationsArrayList;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        uid=firebaseUser.getUid();

        rvNotifications = findViewById(R.id.rvNotifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));
        getNotification();
    }

    public void getNotification() {
        Log.i(TAG, "onCreate: uid "+uid);
        Call<ArrayList<Notifications>> arrayListCall = ApiClient.getClient2().create(ApiService.class).getNotification(uid);
        arrayListCall.enqueue(new Callback<ArrayList<Notifications>>() {
            @Override
            public void onResponse(Call<ArrayList<Notifications>> call, Response<ArrayList<Notifications>> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "onResponse: fail");
                    return;
                }
                if (response.body() == null) {
                    Log.i(TAG, "onResponse: null");
                    return;
                }
                notificationsArrayList=response.body();
                adapter=new NotificationAdapter(getApplicationContext(),notificationsArrayList);
                rvNotifications.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Notifications>> call, Throwable t) {

            }
        });
    }
}
