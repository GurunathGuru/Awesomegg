package com.integro.eggpro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.integro.eggpro.adapters.ProductAdapter;
import com.integro.eggpro.apis.ApiClient;
import com.integro.eggpro.apis.ApiService;
import com.integro.eggpro.model.Products;
import com.integro.eggpro.model.User;
import com.integro.eggpro.utility.entity.Product;
import com.integro.eggpro.utility.viewmodels.CartViewModel;
import com.integro.eggpro.utility.viewmodels.ProductsViewModel;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CALL_PHONE;
import static com.integro.eggpro.constants.GenralConstants.ARG_USER_DETAILS;
import static com.integro.eggpro.constants.GenralConstants.REQUEST_CODE;
import static com.integro.eggpro.constants.GenralConstants.RESULT_FAILED;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvEmail)
    TextView tvEmail;

    @BindView(R.id.tvPhone)
    TextView tvPhone;

    @BindView(R.id.tvAddress)
    TextView tvAddress;

    @BindView(R.id.rvItems)
    RecyclerView rvItems;

    @BindView(R.id.tvLogOut)
    TextView tvLogOut;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tvWallet)
    TextView tvWallet;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = mAuth.getCurrentUser();
    private ProductAdapter productAdapter;
    private User user;
    private ProductsViewModel productsViewModel;
    private CartViewModel cartViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //onCoachMark();

        if (Utils.isFirstTimeNews(MainActivity.this)) {
            onCoachMark();
        }
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        FirebaseMessaging.getInstance().subscribeToTopic("com.integro.eggpro.update");
        FirebaseMessaging.getInstance().subscribeToTopic("com.integro.eggpro.offers");
        FirebaseMessaging.getInstance().subscribeToTopic("com.integro.eggpro.others");

        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);

        if (getIntent().hasExtra(ARG_USER_DETAILS)) {
            Bundle bundle = getIntent().getBundleExtra(ARG_USER_DETAILS);
            user = (User) bundle.getSerializable(ARG_USER_DETAILS);
            if (bundle != null) {
                tvName.setText(user.getName().toUpperCase());
                tvEmail.setText(user.getEmail());
                tvPhone.setText(user.getMobile());
                tvAddress.setText(user.getFloorNo() + "," + user.getFlatNo() + ",\n" + user.getApartmentId());
                String appartmentName = user.getApartmentId();
                String[] subtopicArr = appartmentName.split(" ");
                String topic = "com.integro.eggpro." + TextUtils.join("_", Arrays.asList(subtopicArr));

                FirebaseMessaging.getInstance().subscribeToTopic(topic);

            }
        }
        ApiClient.getClient2().create(ApiService.class);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(this);
        rvItems.setAdapter(productAdapter);

        initProducts();

        if (firebaseUser == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        getCurrentBalance();
    }

    private void initProducts() {
        productsViewModel.clearProducts();
        cartViewModel.clearCart();
        ApiClient.getClient2().create(ApiService.class).getAddItemList().enqueue(new retrofit2.Callback<ArrayList<Products>>() {
            @Override
            public void onResponse(Call<ArrayList<Products>> call, Response<ArrayList<Products>> response) {
                if (!response.isSuccessful()) {
                    Log.i(TAG, "onResponse: Something Went Wrong response.isSuccessful()");
                }
                if (response.body() == null) {
                    Log.i(TAG, "onResponse: something went Wrong response.body");
                }
                for (int i = 0; i < response.body().size(); i++) {
                    productsViewModel.addProduct(new Product(
                            response.body().get(i).getId(),
                            response.body().get(i).getProductImage(),
                            response.body().get(i).getProdSellingPrice(),
                            response.body().get(i).getProdName(),
                            response.body().get(i).getProdDescription(),
                            response.body().get(i).getProdQty(),
                            response.body().get(i).getProdListingPrice(),
                            response.body().get(i).getAdditionalDiscount(),
                            response.body().get(i).getProdStock(),
                            0
                    ));
                    Log.i(TAG, "onResponse: " + response.body().get(i));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Products>> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    public void getCurrentBalance() {
        ApiClient.getClient2().create(ApiService.class).getCurrentBalance(firebaseUser.getUid()).enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.body() == null) {
                    return;
                }
                Log.i(TAG, "onResponse: " + response.body());
                tvWallet.setText(String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.tvSubscribe)
    public void subscribe() {
        Intent subscribeIntent = new Intent(MainActivity.this, SubscribeActivity.class);
        startActivityForResult(subscribeIntent, REQUEST_CODE);
    }

    @OnClick(R.id.tvOrderNow)
    public void oneTimeOrder() {
        Intent oneTimeOrderIntent = new Intent(MainActivity.this, OneTimeOrdersActivity.class);
        startActivityForResult(oneTimeOrderIntent, REQUEST_CODE);
    }

    @OnClick(R.id.tvMyOrders)
    public void getMyOders() {
        Intent intent = new Intent(getApplicationContext(), MyOrdersActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ivSupport)
    public void support() {
        makeACall();
    }

    @OnClick(R.id.ivNotification)
    public void notification() {
        Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.llWallet)
    public void getWalletStatement() {
        Intent intent = new Intent(getApplicationContext(), WalletStatementActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tvContactUs)
    public void contactUs() {
        makeACall();
    }

    @OnClick(R.id.tvAboutUs)
    public void AboutUs() {
        String url = "http://www.awesomegg.com/";
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorBackground));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    @OnClick(R.id.tvLogOut)
    public void logOut() {
        confirmLogOut();
    }

    public void confirmLogOut() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure,you wanted to Log Out");
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void makeACall() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure,you wanted to Call");
        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                try {
                    Intent my_callIntent = new Intent(Intent.ACTION_CALL);
                    my_callIntent.setData(Uri.parse("tel:+91 91480 50344"));
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(my_callIntent);
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{CALL_PHONE}, 1);
                        }
                    }
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Error in your phone call" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE) && resultCode == RESULT_OK) {
            initProducts();
            getCurrentBalance();
            Log.i(TAG, "onActivityResult: " + RESULT_OK);
            Intent intent = new Intent(getApplicationContext(), MyOrdersActivity.class);
            startActivity(intent);
        } else if ((requestCode == REQUEST_CODE) && (resultCode == RESULT_FAILED)) {
            initProducts();
            getCurrentBalance();
        }
    }

    public void onCoachMark() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.coach_mark_master_view);
        dialog.setCanceledOnTouchOutside(false);
        //for dismissing anywhere you touch
        Button masterView = dialog.findViewById(R.id.button);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        masterView.startAnimation(myAnim);
        masterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("WELCOME", MODE_PRIVATE);
                boolean ranBefore = preferences.getBoolean("WELCOME", false);
                if (!ranBefore) {
                    // first time
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("WELCOME", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    class MyBounceInterpolator implements android.view.animation.Interpolator {
        private double mAmplitude = 1;
        private double mFrequency = 10;

        MyBounceInterpolator(double amplitude, double frequency) {
            mAmplitude = amplitude;
            mFrequency = frequency;
        }

        public float getInterpolation(float time) {
            return (float) (-1 * Math.pow(Math.E, -time / mAmplitude) *
                    Math.cos(mFrequency * time) + 1);
        }
    }
}