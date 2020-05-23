package com.integro.eggpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.integro.eggpro.apis.ApiClient;
import com.integro.eggpro.apis.ApiService;
import com.integro.eggpro.firebase.MyFirebaseMessagingService;
import com.integro.eggpro.model.User;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.integro.eggpro.constants.GenralConstants.ARG_USER_DETAILS;
import static com.integro.eggpro.constants.GenralConstants.PREFERENCE;
import static com.integro.eggpro.constants.GenralConstants.PREFERENCE_PRIVATE;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";
    String fcmTag;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    MyFirebaseMessagingService messagingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //isInternetConnection();
        messagingService = new MyFirebaseMessagingService();

        SharedPreferences prefs = getSharedPreferences(PREFERENCE, PREFERENCE_PRIVATE);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                return;
            }
            fcmTag = Objects.requireNonNull(task.getResult()).getToken();
            Log.i(TAG, "setFcmTag: tag " + fcmTag);
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user == null) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ApiClient.getClient2().create(ApiService.class).isRegistered(user.getUid(), user.getPhoneNumber(), fcmTag).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(WelcomeActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Log.i(TAG, "onResponse: " + response.body());
                            if (response.body() == null) {
                                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(ARG_USER_DETAILS, response.body());
                                intent.putExtra(ARG_USER_DETAILS, bundle);
                                startActivity(intent);
                                finish();
                            }
                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                }
            }
        }, 1000);
    }

    /*public boolean isInternetConnection() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else{
            connected = false;
            Toast.makeText(getApplicationContext(), "Please turn on your internet/wifi connection", Toast.LENGTH_LONG).show();
        }
        return connected;
    }*/
}
