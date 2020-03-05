package com.integro.eggpro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.integro.eggpro.model.CustomCalender;
import com.integro.eggpro.model.CustomDate;
import com.integro.eggpro.model.RechargeResponse;
import com.integro.eggpro.model.User;
import com.integro.eggpro.utility.entity.CartItem;
import com.integro.eggpro.utility.viewmodels.CartViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.integro.eggpro.constants.GenralConstants.ORDER_ID;
import static com.integro.eggpro.constants.GenralConstants.RESULT_FAILED;

public class SubscribeActivity extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = "SubscribeActivity";
    @BindViews({R.id.sevenDays, R.id.fourteenDays})
    List<RadioButton> radioButtons;

    @BindView(R.id.tvAddItem)
    TextView tvAddItem;

    @BindView(R.id.tvGrandTotal)
    TextView tvGrandTotal;
    @BindView(R.id.tvDiscountPrice)
    TextView tvDiscountPrice;
    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;
    @BindView(R.id.tvTotalPrice2)
    TextView tvTotalPrice2;
    @BindView(R.id.tvSavedPrice)
    TextView tvSavedPrice;
    @BindView(R.id.monthName)
    TextView monthView;

    @BindView(R.id.rvSubscribe)
    RecyclerView rvSubscribe;

    @BindView(R.id.rvCalender)
    RecyclerView rvCalender;

    int radioId;
    RadioButton radioButton;
    int frequncy = 4;
    private RadioGroup radioGroup;
    private MaterialCalendarView calendarView;
    private EditText tvDate;
    private Calendar primaryCalendar = Calendar.getInstance();

    private SubscribeAdapter adapter;

    private CustomCalenderAdapter customCalenderAdapter = new CustomCalenderAdapter();
    private Double total = null;
    private Double discountTotal = 0.00;
    private Double savedPrice = null;
    private Double finalPrice = null;
    private CartViewModel cartViewModel;
    private Map<String, String> params;
    private RechargeResponse response;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private User user;

    private ArrayList<CartItem> cart = new ArrayList<>();
    private CreateOrder createOrderCallbackListner = new CreateOrder() {
        @Override
        public void onOrderCreated(RechargeResponse localResponse) {
            if (localResponse == null) {
                Toast.makeText(SubscribeActivity.this, "Something Not Right", Toast.LENGTH_SHORT).show();
                return;
            }
            response = localResponse;
            procedeWithPayment();
        }
    };
    private int size;
    private OnDateSelected onDateSelected = new OnDateSelected() {
        @Override
        public void onDateSelected(CustomDate date) {

            Log.i(TAG, "onDateSelected: ");

            monthView.setText(new SimpleDateFormat("MMM").format(date.getCalendar().getTime()));

            radioId = radioGroup.getCheckedRadioButtonId();
            if (radioId < 0) {
                Toast.makeText(SubscribeActivity.this, "Select any option", Toast.LENGTH_SHORT).show();
                return;
            }
            radioButton = findViewById(radioId);
            if (radioButton.getText().toString().contentEquals(getString(R.string.evr_fourteen))) {

                primaryCalendar.set(Calendar.YEAR, date.getCalendar().get(Calendar.YEAR));
                primaryCalendar.set(Calendar.MONTH, date.getCalendar().get(Calendar.MONTH));
                primaryCalendar.set(Calendar.DAY_OF_MONTH, date.getCalendar().get(Calendar.DAY_OF_MONTH));
                everyFourteenDays();
            } else {
                primaryCalendar.set(Calendar.YEAR, date.getCalendar().get(Calendar.YEAR));
                primaryCalendar.set(Calendar.MONTH, date.getCalendar().get(Calendar.MONTH));
                primaryCalendar.set(Calendar.DAY_OF_MONTH, date.getCalendar().get(Calendar.DAY_OF_MONTH));
                everySevenDays();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);
        ButterKnife.bind(this);
        Checkout.preload(getApplicationContext());

        if (getIntent().hasExtra(ORDER_ID)){
            tvAddItem.setVisibility(View.GONE);
            tvAddItem.setEnabled(false);
            Log.i(TAG, "onCreate: "+getIntent().getIntExtra(ORDER_ID,-1));
        }

        radioButtons.get(0).setChecked(true);
        tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        rvSubscribe.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SubscribeAdapter(this);
        rvSubscribe.setAdapter(adapter);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

        rvCalender.setLayoutManager(linearLayoutManager);
        rvCalender.setAdapter(customCalenderAdapter);
        customCalenderAdapter.setOnDateSelected(onDateSelected);


        loadDates();

        calendarView = findViewById(R.id.calendarView);
        tvDate = findViewById(R.id.tvDate);

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        cartViewModel.getCart().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                cart = new ArrayList<>(cartItems);
                calculateTotal();
            }
        });

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i(TAG, "onCheckedChanged: ");
                RadioButton rb = group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    if (rb.getText().toString().contentEquals(getString(R.string.evr_fourteen))) {
                        frequncy = 2;
                        Log.i(TAG, "onCheckedChanged: " + frequncy);
                        everyFourteenDays();
                    } else {
                        frequncy = 4;
                        Log.i(TAG, "onCheckedChanged: " + frequncy);
                        everySevenDays();
                    }
                }
            }
        });
    }

    private void calculateTotal() {
        total = 0.0;
        discountTotal = 0.00;
        finalPrice = 0.00;
        savedPrice = 0.00;
        size = cart.size();
        params = new HashMap<>();

        for (int i = 0; i < size; i++) {
            CartItem item = cart.get(i);
            total += item.getItemQty() * item.getProdSellingPrice();
            discountTotal += item.getProdSellingPrice() * item.getAdditionalDiscount() / 100 * item.getItemQty();
            double discount = item.getProdSellingPrice() * item.getAdditionalDiscount() / 100 * item.getItemQty();
            savedPrice = item.getProdListingPrice() - (item.getProdSellingPrice() - discount);
            finalPrice = (total - discountTotal) * frequncy;
            params.put("productId[" + i + "]", String.valueOf(item.getId()));
            params.put("itemQty[" + i + "]", String.valueOf(item.getItemQty()));
            params.put("itemPrice[" + i + "]", decimalFormat.format(item.getProdSellingPrice() - (item.getProdSellingPrice() * item.getAdditionalDiscount() / 100)));
        }
        setTotalView();
    }

    @SuppressLint("StringFormatMatches")
    private void setTotalView() {
        tvGrandTotal.setText(getString(R.string.total, decimalFormat.format(total)));
        tvDiscountPrice.setText(getString(R.string.cardDiscountsTotal, decimalFormat.format(discountTotal))+ "/ord");
        tvSavedPrice.setText( getString(R.string.cardSavedTotal, decimalFormat.format(savedPrice))+ "/ord");
        tvTotalPrice.setText(getString(R.string.net, decimalFormat.format(total - discountTotal) + "/ord"));
        tvTotalPrice2.setText(getString(R.string.total, decimalFormat.format(finalPrice) + "/mnt"));
    }

    @SuppressLint("WrongConstant")
    private Calendar initializeCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -365);
        Log.i(TAG, "initializeCalendar: ");
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE); // Removes onClick functionality
        for (int i = 0; i < 1095; i++) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            calendarView.setDateSelected(CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)), false);
        }
        cal = (Calendar) primaryCalendar.clone();
        tvDate.setText(getString(R.string.monthPlaceholder, cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)));
        cal = (Calendar) primaryCalendar.clone();
        calendarView.setDateSelected(CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)), true);
        Log.i(TAG, "initializeCalendar: " + cal.getTime());
        return cal;
    }

    public void everyFourteenDays() {
        Log.i(TAG, "everyFourteenDays: ");
        Calendar cal = initializeCalendar();
        for (int i = 0; i < 26; i++) {
            cal.add(Calendar.DAY_OF_MONTH, 14);
            calendarView.setDateSelected(CalendarDay.from(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)), true);
        }
        calculateTotal();
    }

    public void everySevenDays() {
        Calendar cal = initializeCalendar();
        Log.i(TAG, "everySevenDays: ");
        for (int i = 0; i < 52; i++) {
            cal.add(Calendar.DAY_OF_MONTH, 7);
            calendarView.setDateSelected(CalendarDay.from(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)), true);
            Log.i(TAG, "everySevenDays: " + cal.get(Calendar.YEAR));
        }
        calculateTotal();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void loadDates() {
        ApiClient.getClient2()
                .create(ApiService.class)
                .getCalender("Prestige Royale Gardens")
                .enqueue(new Callback<ArrayList<CustomCalender>>() {
                    @Override
                    public void onResponse(Call<ArrayList<CustomCalender>> call, Response<ArrayList<CustomCalender>> response) {
                        Log.i(TAG, "onResponse: " + response.body());
                        if (!response.isSuccessful()) {
                            Log.i(TAG, "onResponse: " + response.body());
                            Toast.makeText(SubscribeActivity.this, "Something not right", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (response.body() == null) {
                            Toast.makeText(SubscribeActivity.this, "Something not right", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ArrayList<CustomDate> customDates = new ArrayList<>();
                        for (int i = 0; i < response.body().size(); i++) {
                            int month = response.body().get(i).getMonth();
                            for (int j = 0; j < response.body().get(i).getDates().length; j++) {
                                CustomDate customDate =new CustomDate(month, response.body().get(i).getDates()[j]);
                                if (customDate.getCalendar().get(Calendar.DAY_OF_MONTH)==Calendar.getInstance().get(Calendar.DAY_OF_MONTH)&&
                                        customDate.getCalendar().get(Calendar.MONTH)==Calendar.getInstance().get(Calendar.MONTH)&&
                                        customDate.getCalendar().get(Calendar.YEAR)==Calendar.getInstance().get(Calendar.YEAR)){
                                    continue;
                                }
                                customDates.add(customDate);

                            }
                        }
                        Log.i(TAG, "onResponse: " + response.body());

                        customCalenderAdapter.setCustomDates(customDates);
                        customCalenderAdapter.initialize();

                        if (customCalenderAdapter.getFirstDate() != null) {
                            Log.i(TAG, "onResponse: " + customCalenderAdapter.getFirstDate());
                            primaryCalendar = (Calendar) customCalenderAdapter.getFirstDate().getCalendar().clone();
                            Log.i(TAG, "PRIMARY Calendar onResponse: " + primaryCalendar.getTime());
                            initializeCalendar();
                        } else {
                            primaryCalendar = Calendar.getInstance();
                            initializeCalendar();
                        }
                        monthView.setText(new SimpleDateFormat("MMM").format(primaryCalendar.getTime()));
                    }

                    @Override
                    public void onFailure(Call<ArrayList<CustomCalender>> call, Throwable t) {
                        Log.i(TAG, "onFailure: " + t.toString());
                        Toast.makeText(SubscribeActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getDate(View view) {
        Toast.makeText(this, "" + customCalenderAdapter.getSelectedDate().getCalendar().getTime(), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.tvProceedToPay)
    public void makePayment() {
        if (radioButtons.get(0).isChecked() || radioButtons.get(1).isChecked()) {
            if (getIntent().hasExtra(ORDER_ID)) {
                rechargeOrder(createOrderCallbackListner);
                Log.i(TAG, "makePayment: recharge order");
            } else {
                Log.i(TAG, "makePayment: new order");
                getResponseList(createOrderCallbackListner);
            }
        } else {
            Toast.makeText(this, "Not selected any option", Toast.LENGTH_SHORT).show();
        }
    }
    private void rechargeOrder(CreateOrder callback) {
        if (response != null) {
            procedeWithPayment();
            return;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        Log.i(TAG, "getResponseList: " + finalPrice);
        int period = 1;
        int freq = (frequncy==4) ? 7 : 14;
        Double startDate = (primaryCalendar.getTimeInMillis() / 1000.00);
        int startDateTimeStamp = startDate.intValue();
        String orderType = "Subscriprion";
        Double orderPrice = Double.valueOf(decimalFormat.format(finalPrice)) ;

        ApiClient.getClient2().create(ApiService.class).rechargeOrder(
                getIntent().getIntExtra(ORDER_ID,-1),
                period,
                freq,
                startDateTimeStamp,
                orderPrice,
                uid
        ).enqueue(new Callback<RechargeResponse>() {
            @Override
            public void onResponse(Call<RechargeResponse> call, Response<RechargeResponse> response) {
                Log.i(TAG, "onResponse: " + response.body());

                if (!response.isSuccessful()) {
                    Toast.makeText(SubscribeActivity.this, "Something Not rignt is success", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body() == null) {
                    Toast.makeText(SubscribeActivity.this, "Something Not rignt body null", Toast.LENGTH_SHORT).show();
                    callback.onOrderCreated(null);
                    return;
                }
                callback.onOrderCreated(response.body());
            }

            @Override
            public void onFailure(Call<RechargeResponse> call, Throwable t) {
                Toast.makeText(SubscribeActivity.this, "Something Not rignt" + t.getMessage(), Toast.LENGTH_SHORT).show();
                //callback.onOrderCreated(null);
            }
        });
    }

    @Override
    public void onPaymentSuccess(String paymentId) {
        Toast.makeText(this, "Payment Success " + paymentId, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onPaymentSuccess: "+paymentId);
        finalPrice = finalPrice / 100;
        paymentComplete(paymentId);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "" + i + " Payment Fail " + s, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onPaymentError: " + s + ", code: " + i);
        setResult(RESULT_FAILED);
        finish();

    }

    private void getResponseList(CreateOrder callback) {
        if (response != null) {
            procedeWithPayment();
            return;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        Log.i(TAG, "getResponseList: " + finalPrice);
        int period = 1;
        int freq = frequncy==4? 7 :14;
        Double startDate = (primaryCalendar.getTimeInMillis() / 1000.00);
        Log.i(TAG, "getResponseList: "+startDate);
        int startDateTimeStamp = startDate.intValue();
        String orderType = "Subscription";
        Double orderPrice = finalPrice;

        ApiClient.getClient2().create(ApiService.class).createOrder(
                uid,
                period,
                freq,
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
                    Toast.makeText(SubscribeActivity.this, "Something Not right is success", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body() == null) {
                    Toast.makeText(SubscribeActivity.this, "Something Not right body null", Toast.LENGTH_SHORT).show();
                    callback.onOrderCreated(null);
                    return;
                }
                callback.onOrderCreated(response.body());
            }

            @Override
            public void onFailure(Call<RechargeResponse> call, Throwable t) {
                Toast.makeText(SubscribeActivity.this, "Something Not right" + t.getMessage(), Toast.LENGTH_SHORT).show();
                callback.onOrderCreated(null);
            }
        });
    }

    public void procedeWithPayment() {
        Checkout checkout = new Checkout();
        final Activity activity = SubscribeActivity.this;
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
                            Toast.makeText(SubscribeActivity.this, "response is not Successful.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body() == null) {
                            Toast.makeText(SubscribeActivity.this, "response.body is null", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(SubscribeActivity.this, "Order Placed", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {

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
        int period = 1;
        int frequecy = 7;
        Double startDate = (primaryCalendar.getTimeInMillis() / 1000.00);
        Log.i(TAG, "getResponseList: "+startDate);
        int startDateTimeStamp = startDate.intValue();
        String orderType = "Cash on Delivery Subscription";
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
                    Toast.makeText(SubscribeActivity.this, "Something Not right is success", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body() == null) {
                    Toast.makeText(SubscribeActivity.this, "Something Not right body null", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i(TAG, "onResponse: "+response.body());
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(SubscribeActivity.this, "Something Not right" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}