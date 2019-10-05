package com.integro.eggpro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.integro.eggpro.adapters.ProductAdapter;
import com.integro.eggpro.apis.ApiClient;
import com.integro.eggpro.apis.ApiService;
import com.integro.eggpro.interfaces.QuantityChangedListener;
import com.integro.eggpro.model.Products;
import com.integro.eggpro.model.User;
import com.integro.eggpro.utility.entity.Product;
import com.integro.eggpro.utility.viewmodels.CartViewModel;
import com.integro.eggpro.utility.viewmodels.ProductsViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static com.integro.eggpro.constants.GenralConstants.ARG_USER_DETAILS;


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

    private ApiService apiService;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = mAuth.getCurrentUser();
    private ArrayList<Products> productsArrayList = new ArrayList<>();
    private Call<Products> productsCall;
    private ProductAdapter productAdapter;

    private double total = 0.0;
    private User user;

    private ProductsViewModel productsViewModel;
    private CartViewModel cartViewModel;

    //Listeners
    private QuantityChangedListener quantityChangedListener = new QuantityChangedListener() {
        @Override
        public void onItermQuantityChanged(int oldValue, int newValue, Product product) {
            /*int flag = -1;
            for (int i = 0; i < productsArrayList.size(); i++) {
                if (productsArrayList.get(i).getId() == Integer.parseInt(String.valueOf(product.getId()))) {
                    flag = i;
                    break;
                }
            }
            if (flag >= 0) {
                if (newValue > 0) {
                    productsArrayList.get(flag).setProdQty(newValue);
                } else {
                    productsArrayList.remove(flag);
                }
            } else {
                productsArrayList.add(new Products(product.getId(), product.getProdSellingPrice(), newValue, product.getProdName(), product.getProductImage()));
            }
            total = 0.0;
            for (int i = 0; i < productsArrayList.size(); i++) {
                total += (productsArrayList.get(i).getProdSellingPrice() * 6 * productsArrayList.get(i).getProdQty());
            }*/
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productsViewModel = ViewModelProviders.of(this).get(ProductsViewModel.class);
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if (getIntent().hasExtra(ARG_USER_DETAILS)) {
            Bundle bundle = getIntent().getBundleExtra(ARG_USER_DETAILS);
            user = (User) bundle.getSerializable(ARG_USER_DETAILS);

            if (bundle != null) {
                tvName.setText(user.getName().toUpperCase());
                tvEmail.setText(user.getEmail());
                tvPhone.setText(user.getMobile());
                tvAddress.setText(user.getFloorNo() + "," + user.getFlatNo() + ",\n" + user.getApartmentId());
            }
        }
        apiService = ApiClient.getClient2().create(ApiService.class);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(this);
        rvItems.setAdapter(productAdapter);

        initProducts();

        if (firebaseUser == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
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
                //Product(int id, String productImage, Double prodSellingPrice, String prodName, String prodDescription, int prodQty, int itemQty, Double prodListingPrice, int additionalDiscount, int prodStock)
                for (int i =0; i<response.body().size(); i++) {
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

    @OnClick(R.id.tvSubscribe)
    public void subscribe() {
        Intent subscribeIntent = new Intent(MainActivity.this, SubscribeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("CARTLIST", productsArrayList);
        subscribeIntent.putExtra("CARTLIST", bundle);
        Log.i(TAG, "subscribe: " + productsArrayList);
        startActivity(subscribeIntent);
    }

    public void getProducts() {
        productsViewModel.getProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                Log.i(TAG, "onChanged: " + products.size());
            }
        });
        /*ApiClient.getClient2().create(ApiService.class).getAddItemList().enqueue(new Callback<ArrayList<Products>>() {
            @Override
            public void onResponse(Call<ArrayList<Products>> call, Order<ArrayList<Products>> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "NoT Successful Request");
                    return;
                }
                if (response.body()== null) {
                    Log.i(TAG, "onResponse: " + response.body());
                    return;
                }

                productAdapter.setAddItemArrayList(response.body());
                Log.i(TAG, "onResponse: " + response.body());
                productAdapter.setQuantityChangedListener(quantityChangedListener);
            }

            @Override
            public void onFailure(Call<ArrayList<Products>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error\n" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/
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
}