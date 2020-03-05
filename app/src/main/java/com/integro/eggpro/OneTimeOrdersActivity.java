package com.integro.eggpro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.integro.eggpro.adapters.CustomCalenderAdapter;
import com.integro.eggpro.adapters.SubscribeAdapter;
import com.integro.eggpro.apis.ApiClient;
import com.integro.eggpro.apis.ApiService;
import com.integro.eggpro.interfaces.CreateOrder;
import com.integro.eggpro.interfaces.OnDateSelected;
import com.integro.eggpro.model.CustomDate;
import com.integro.eggpro.model.RechargeResponse;
import com.integro.eggpro.utility.entity.CartItem;
import com.integro.eggpro.utility.viewmodels.CartViewModel;
import com.razorpay.Checkout;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OneTimeOrdersActivity extends AppCompatActivity {

    private static final String TAG = "OneTimeOrdersActivity";
    private TextView tvGrandTotal;
    private TextView tvDiscountPrice;
    private TextView tvTotalPrice;
    private TextView tvSavedPrice;
    private Calendar primaryCalendar = Calendar.getInstance();
    private Double total = null;
    private Double savedPrice = null;
    private Double finalPrice = null;
    private RechargeResponse response;

    private CustomCalenderAdapter customCalenderAdapter = new CustomCalenderAdapter();
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private OnDateSelected onDateSelected = new OnDateSelected() {
        @SuppressLint("SimpleDateFormat")
        @Override
        public void onDateSelected(CustomDate date) {
            primaryCalendar = (Calendar) date.getCalendar().clone();
        }
    };
    private CreateOrder createOrderCallbackListner = new CreateOrder() {
        @Override
        public void onOrderCreated(RechargeResponse localResponse) {
            if (localResponse == null) {
                Toast.makeText(OneTimeOrdersActivity.this, "Something Not Right", Toast.LENGTH_SHORT).show();
                return;
            }
            response = localResponse;
            procedeWithPayment();
        }
    };

    private ArrayList<Integer> productId;
    private ArrayList<Double> itemPrice;
    private ArrayList<Integer> quantity;
    private int size;
    private Map<String, String> params;
    private Calendar deliveryDate;

    public OneTimeOrdersActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time_orders);
        ButterKnife.bind(this);

        tvDiscountPrice = findViewById(R.id.tvDiscountPrice);
        tvSavedPrice = findViewById(R.id.tvSavedPrice);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvGrandTotal = findViewById(R.id.tvGrandTotal);
        TextView tvAddItem = findViewById(R.id.tvAddItem);

        tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deliveryDate = Calendar.getInstance();
        deliveryDate.add(Calendar.DAY_OF_MONTH, 1);

        EditText tvDate = findViewById(R.id.tvDate);
        tvDate.setText(getString(R.string.monthPlaceholder, deliveryDate.get(Calendar.DAY_OF_MONTH),
                deliveryDate.get(Calendar.MONTH) + 1, deliveryDate.get(Calendar.YEAR)));

        SubscribeAdapter adapter = new SubscribeAdapter(this);
        CartViewModel cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        cartViewModel.getCart().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                total = 0.0;
                finalPrice = 0.00;
                savedPrice = 0.00;
                size = cartItems.size();
                productId = new ArrayList<>();
                itemPrice = new ArrayList<>();
                quantity = new ArrayList<>();
                params = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    CartItem item = cartItems.get(i);
                    total += item.getItemQty() * item.getProdSellingPrice();
                    savedPrice = item.getProdListingPrice() - item.getProdSellingPrice();
                    finalPrice = total;
                    params.put("productId[" + i + "]", String.valueOf(item.getId()));
                    params.put("itemQty[" + i + "]", String.valueOf(item.getItemQty()));
                    params.put("itemPrice[" + i + "]", decimalFormat.format(item.getProdSellingPrice()));
                }
                setTotalView();
            }
        });
    }

    @SuppressLint("StringFormatMatches")
    private void setTotalView() {
        tvDiscountPrice.setText("Subscription \nSavings \u0020\u0020\u0020 \u20B9 0.00/ord");
        tvSavedPrice.setText(getString(R.string.cardSavedTotal, decimalFormat.format(savedPrice)) + "/ord");
        tvTotalPrice.setText(getString(R.string.net, decimalFormat.format(finalPrice)) + "/ord");
        tvGrandTotal.setText(getString(R.string.net, decimalFormat.format(total)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void getDate(View view) {
        Toast.makeText(this, "" + customCalenderAdapter.getSelectedDate().getCalendar().getTime(), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.tvProceedToPay)
    public void makePayment() {
        getResponseList(createOrderCallbackListner);
    }

    public void onPaymentSuccess(String paymentId) {
        Toast.makeText(this, "Payment Success " + paymentId, Toast.LENGTH_SHORT).show();
        finalPrice = finalPrice / 100;
        paymentComplete(paymentId);
    }

    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "" + i + " Payment Fail " + s, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onPaymentError: " + s + " " + i);
    }

    private void getResponseList(CreateOrder callback) {


        if (response != null) {
            procedeWithPayment();
            return;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        Log.i(TAG, "getResponseList: " + finalPrice);
        int period = 0;
        int frequecy = 1;

        Double startDate = (deliveryDate.getTimeInMillis() / 1000.00);
        int startDateTimeStamp = startDate.intValue();
        String orderType = "One Time Trial";
        Double orderPrice = finalPrice;
        ApiClient.getClient2().create(ApiService.class).createOrder(
                uid,
                period,
                frequecy,
                startDateTimeStamp,
                orderType,
                orderPrice,
                size,
                params
        ).enqueue(new Callback<RechargeResponse>() {
            @Override
            public void onResponse(Call<RechargeResponse> call, Response<RechargeResponse> response) {
                Log.i(TAG, "onResponse: " + response.body());

                if (!response.isSuccessful()) {
                    Toast.makeText(OneTimeOrdersActivity.this, "Something Not right is success", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body() == null) {
                    Toast.makeText(OneTimeOrdersActivity.this, "Something Not right body null", Toast.LENGTH_SHORT).show();
                    callback.onOrderCreated(null);
                    return;
                }
                callback.onOrderCreated(response.body());
            }

            @Override
            public void onFailure(Call<RechargeResponse> call, Throwable t) {
                Toast.makeText(OneTimeOrdersActivity.this, "Something Not right" + t.getMessage(), Toast.LENGTH_SHORT).show();
                callback.onOrderCreated(null);
            }
        });
    }

    private void getResponseList2() {
        if (response != null) {
            procedeWithPayment();
            return;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        Log.i(TAG, "getResponseList: " + finalPrice);
        int period = 0;
        int frequecy = 1;
        Double startDate = (primaryCalendar.getTimeInMillis() / 1000.00);
        Log.i(TAG, "getResponseList: " + startDate);
        int startDateTimeStamp = startDate.intValue();
        String orderType = "One Time Cash on Delivery";
        Double orderPrice = finalPrice;

        ApiClient.getClient2().create(ApiService.class).cashOnDelivery(
                uid,
                period,
                frequecy,
                startDateTimeStamp,
                orderType,
                orderPrice,
                size,
                params
        ).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.i(TAG, "onResponse: " + response.body());

                if (!response.isSuccessful()) {
                    Toast.makeText(OneTimeOrdersActivity.this, "Something Not right is success", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body() == null) {
                    Toast.makeText(OneTimeOrdersActivity.this, "Something Not right body null", Toast.LENGTH_SHORT).show();
                    return;
                }
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(OneTimeOrdersActivity.this, "Something Not right" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void procedeWithPayment() {
        Checkout checkout = new Checkout();
        checkout.setImage(R.mipmap.ic_launcher);
        final Activity activity = OneTimeOrdersActivity.this;
        finalPrice = finalPrice * 100;
        int totalinpaisa = finalPrice.intValue();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "AWESOMEGG");
            options.put("description", "Reference No. " + response.getId());
            options.put("order_id", response.getIpgOrderId());
            options.put("currency", "INR");
            options.put("amount", totalinpaisa);
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    private void paymentComplete(String paymentId) {
        ApiClient.getClient2()
                .create(ApiService.class)
                .paymentComplete(response.getId(), paymentId)
                .enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        Log.i(TAG, "onResponse: " + response);
                        if (!response.isSuccessful()) {
                            Toast.makeText(OneTimeOrdersActivity.this, "response is not Successful.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body() == null) {
                            Toast.makeText(OneTimeOrdersActivity.this, "response.body is null", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(OneTimeOrdersActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(OneTimeOrdersActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onFailure: " + t.toString());
                    }
                });
    }
}
