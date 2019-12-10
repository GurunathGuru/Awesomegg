package com.integro.eggpro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.shuhart.stepview.StepView;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.integro.eggpro.constants.GenralConstants.ARG_USER_DETAILS;
import static com.integro.eggpro.constants.GenralConstants.FCMTAG;
import static com.integro.eggpro.constants.GenralConstants.PREFERENCE;
import static com.integro.eggpro.constants.GenralConstants.PREFERENCE_PRIVATE;

public class LoginActivity extends AppCompatActivity {

   /* private EditText etMobilNumber;
    private TextView tvContinue;*/

    private static final String TAG = "FirebasePhoneNumAuth";
    LinearLayout layout1, layout2, layout3;
    StepView stepView;
    private int currentStep = 0;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private Button btnNext;
    private Button btnContinue;
    private Button btnContinue2;
    private EditText etPhoneNum;
    private PinView pinView;
    private TextView tvphoneNumberText;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        layout1 = (LinearLayout) findViewById(R.id.layout1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);

        btnNext = (Button) findViewById(R.id.btnNext);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnContinue2 = (Button) findViewById(R.id.btnContinue2);

        etPhoneNum = (EditText) findViewById(R.id.etPhoneNum);
        pinView = (PinView) findViewById(R.id.pinView);
        tvphoneNumberText = (TextView) findViewById(R.id.tvphoneNumberText);

        stepView = findViewById(R.id.step_view);
        stepView.setStepsNumber(3);
        stepView.go(0, true);
        layout1.setVisibility(View.VISIBLE);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNumber = etPhoneNum.getText().toString();
                tvphoneNumberText.setText(phoneNumber);

                if (TextUtils.isEmpty(phoneNumber)) {
                    etPhoneNum.setError("Enter a Phone Number");
                    etPhoneNum.requestFocus();
                } else if (phoneNumber.length() < 10) {
                    etPhoneNum.setError("Please enter a valid phone");
                    etPhoneNum.requestFocus();
                } else {

                    if (currentStep < stepView.getStepCount() - 1) {
                        currentStep++;
                        stepView.go(currentStep, true);
                    } else {
                        stepView.done(true);
                    }
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91"+phoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            LoginActivity.this,               // Activity (for callback binding)
                            mCallbacks);        // OnVerificationStateChangedCallbacks
                }
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
                Log.i(TAG, "onVerificationCompleted: "+phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                // ...
            }
        };

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verificationCode = pinView.getText().toString();
                Log.i(TAG, "onClick: "+verificationCode);
                if (verificationCode.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter verification code", Toast.LENGTH_SHORT).show();
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                    //pinView.setText(verificationCode);
                    signInWithPhoneAuthCredential(credential);
                    Log.i(TAG, "onClick: credential : "+credential);
                }
            }
        });

        btnContinue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentStep < stepView.getStepCount() - 1) {
                    currentStep++;
                    stepView.go(currentStep, true);
                } else {
                    stepView.done(true);
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }, 3000);
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Log.i(TAG, "signInWithPhoneAuthCredential: "+credential);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            if (currentStep < stepView.getStepCount() - 1) {
                                currentStep++;
                                stepView.go(currentStep, true);
                            } else {
                                stepView.done(true);
                            }
                            checkIfRegistered();
                            layout1.setVisibility(View.GONE);
                            layout2.setVisibility(View.GONE);
                            layout3.setVisibility(View.VISIBLE);
                            // ...
                        } else {
                            Toast.makeText(LoginActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                            }
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
                    Toast.makeText(LoginActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body()==null){
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
                Log.i(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(LoginActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
