package com.integro.eggpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.integro.eggpro.apis.ApiClient;
import com.integro.eggpro.apis.ApiService;
import com.integro.eggpro.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.integro.eggpro.constants.GenralConstants.ARG_USER_DETAILS;

public class WelcomeActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user==null){
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    ApiClient.getClient2().create(ApiService.class).isRegistered(user.getUid(),user.getPhoneNumber()).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(WelcomeActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Log.i(TAG, "onResponse: "+response.body());
                            if (response.body()==null) {
                                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                                startActivity(intent);
                                finish();
                            }else {

                                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putSerializable(ARG_USER_DETAILS,response.body());
                                intent.putExtra(ARG_USER_DETAILS,bundle);
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
        },3000);
        }
    }
