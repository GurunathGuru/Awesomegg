package com.integro.eggpro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.integro.eggpro.apis.ApiClient;
import com.integro.eggpro.apis.ApiService;
import com.integro.eggpro.model.User;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.integro.eggpro.constants.GenralConstants.ARG_USER_DETAILS;
import static com.integro.eggpro.constants.GenralConstants.FCMTAG;
import static com.integro.eggpro.constants.GenralConstants.PREFERENCE;
import static com.integro.eggpro.constants.GenralConstants.PREFERENCE_PRIVATE;

public class OtpActivity extends AppCompatActivity {

    private static final String TAG = "OtpActivity";
    private String mVerificationId;
    private PinView pinView;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                pinView.setText(code);
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mAuth = FirebaseAuth.getInstance();
        pinView = findViewById(R.id.pinView);
        Intent intent = getIntent();
        String mobile = intent.getStringExtra("mobile");
        sendVerificationCode(mobile);

        findViewById(R.id.tvSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = pinView.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    pinView.setError("Enter valid code");
                    pinView.requestFocus();
                    return;
                }
                verifyVerificationCode(code);
            }
        });
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(OtpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    checkIfRegistered();
                } else {
                    String message = "Somthing is wrong, we will fix it soon...";
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        message = "Invalid code entered...";
                    }
                    Toast.makeText(OtpActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkIfRegistered() {
        SharedPreferences prefs = getSharedPreferences(PREFERENCE, PREFERENCE_PRIVATE);
        String token = prefs.getString(FCMTAG,"");
        Log.i(TAG, "onCreate: setFcmTag: gurunath token "+token);


        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        ApiClient.getClient2().create(ApiService.class).isRegistered(user.getUid(),user.getPhoneNumber(),token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(OtpActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body()==null){
                    Intent intent = new Intent(OtpActivity.this, RegisterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(OtpActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
