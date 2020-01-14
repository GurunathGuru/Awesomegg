package com.integro.eggpro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.integro.eggpro.apis.ApiClient;
import com.integro.eggpro.apis.ApiService;
import com.integro.eggpro.model.Apartments;
import com.integro.eggpro.model.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.integro.eggpro.constants.GenralConstants.ARG_USER_DETAILS;
import static com.integro.eggpro.constants.GenralConstants.FCMTAG;
import static com.integro.eggpro.constants.GenralConstants.PREFERENCE;
import static com.integro.eggpro.constants.GenralConstants.PREFERENCE_PRIVATE;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.tvSignIn)
    TextView tvSignIn;
    @BindView(R.id.etSelectApartment)
    EditText etSelectApartment;
    @BindView(R.id.etBlockNo)
    EditText etBlockNo;
    @BindView(R.id.etFlatNo)
    EditText etFlatNo;

    ArrayList<Apartments> apartmentsArrayList;
    ArrayList<User> userArrayList;
    SpinnerDialog spinnerDialog;
    ApiService apiService;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String apartmentId;
    private String userId;
    private String name;
    private String email;
    private String emailPattern;
    private String blockNo;
    private String flatNo;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        user.getPhoneNumber();
        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        etSelectApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApartmentList();
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString().trim();
                email = etEmail.getText().toString().trim();
                apartmentId = etSelectApartment.getText().toString().trim();
                blockNo = etBlockNo.getText().toString().trim();
                flatNo = etFlatNo.getText().toString().trim();
                mobile = user.getPhoneNumber();
                userId = user.getUid();

                if (!email.matches(emailPattern)) {
                    etEmail.setError("Enter Valid Email");
                    return;
                }
                if (name.isEmpty() || email.isEmpty() || apartmentId.isEmpty() || blockNo.isEmpty() || flatNo.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please enter all valid details", Toast.LENGTH_SHORT).show();
                    return;
                }
                registerUserDetails();
            }
        });
    }

    private void getApartmentList() {
        apiService = ApiClient.getClient2().create(ApiService.class);
        Call<ArrayList<Apartments>> arrayListCall = apiService.getApartmentList();
        arrayListCall.enqueue(new Callback<ArrayList<Apartments>>() {
            @Override
            public void onResponse(Call<ArrayList<Apartments>> call, Response<ArrayList<Apartments>> response) {
                Log.i(TAG, "onResponse: " + response);
                if (!response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "response is not Successful.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body() == null) {
                    Toast.makeText(RegisterActivity.this, "response.body is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                apartmentsArrayList = new ArrayList<>(response.body());
                showSpinnerDialog();
            }

            @Override
            public void onFailure(Call<ArrayList<Apartments>> call, Throwable t) {

            }
        });
    }

    private void showSpinnerDialog() {
        ArrayList<String> apartments = new ArrayList<>();
        for (Apartments apartment : apartmentsArrayList) {
            apartments.add(apartment.getApartmentName());
            // apartments.add(apartment.getApartmentAddress());
        }
        spinnerDialog = new SpinnerDialog(this, apartments, "Apartments");
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                etSelectApartment.setText(s);
            }
        });
        spinnerDialog.showSpinerDialog();
    }

    private void registerUserDetails() {
        ApiClient.getClient2()
                .create(ApiService.class)
                .getUserDetailsList(apartmentId, userId, name, email, flatNo, mobile, blockNo)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.i(TAG, "onResponse: " + response);
                        if (!response.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "response is not Successful.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body() == null) {
                            Toast.makeText(RegisterActivity.this, "response.body is null", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (!response.body().contentEquals(user.getUid())) {
                            Toast.makeText(RegisterActivity.this, "Something Went Wrong.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        Log.i(TAG, "onClick: " + name);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
    }
}
