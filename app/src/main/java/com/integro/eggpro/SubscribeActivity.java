package com.integro.eggpro;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import com.integro.eggpro.model.Order;
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
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscribeActivity extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = "SubscribeActivity";
    ArrayList<Order> placeOrderListArrayList;
    private DatePickerDialog picker;
    private RadioGroup radioGroup;
    private MaterialCalendarView calendarView;
    private EditText tvDate;
    private Calendar primaryCalendar = Calendar.getInstance();
    private TextView tvGrandTotal, tvGrandTotal2, tvGrandTotal3, tvGrandTotal4, tvAddItem;
    private RecyclerView rvSubscribe;
    private SubscribeAdapter adapter;
    private RecyclerView recyclerView;
    private TextView monthView;
    private CustomCalenderAdapter customCalenderAdapter = new CustomCalenderAdapter();
    private Double total = null;
    private Double discountTotal = null;
    private Double savedPrice = null;
    private Double finalPrice = null;
    private ApiService apiService;
    private ArrayList<CartItem> cartItems = new ArrayList<>();
    private CartViewModel cartViewModel;

    private Order order;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private OnDateSelected onDateSelected = new OnDateSelected() {
        @Override
        public void onDateSelected(CustomDate date) {
            monthView.setText(new SimpleDateFormat("MMM").format(date.getCalendar().getTime()));
            int radioId = radioGroup.getCheckedRadioButtonId();
            if (radioId < 0) {
                Toast.makeText(SubscribeActivity.this, "Select any option", Toast.LENGTH_SHORT).show();
                return;
            }
            RadioButton radioButton = findViewById(radioId);
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

        tvAddItem = findViewById(R.id.tvAddItem);
        tvGrandTotal = findViewById(R.id.tvGrandTotal);
        tvGrandTotal2 = findViewById(R.id.tvGrandTotal2);
        tvGrandTotal3 = findViewById(R.id.tvGrandTotal3);
        tvGrandTotal4 = findViewById(R.id.tvGrandTotal4);

        tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rvSubscribe = findViewById(R.id.rvSubscribe);
        adapter = new SubscribeAdapter(this);
        rvSubscribe.setLayoutManager(new LinearLayoutManager(this));
        rvSubscribe.setAdapter(adapter);
        recyclerView = findViewById(R.id.recyclerView);
        monthView = findViewById(R.id.monthName);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(customCalenderAdapter);
        customCalenderAdapter.setOnDateSelected(onDateSelected);
        loadDates();

        calendarView = findViewById(R.id.calendarView);
        tvDate = findViewById(R.id.tvDate);

        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);

                if (null != rb && checkedId > -1) {
                    if (rb.getText().toString().contentEquals(getString(R.string.evr_fourteen))) {
                        everyFourteenDays();
                    } else {
                        everySevenDays();
                    }
                }
            }
        });

        cartViewModel.getCart().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                total = 0.0;
                discountTotal = 0.00;

                finalPrice = 0.00;
                for (CartItem item : cartItems) {
                    total += item.getItemQty() * item.getProdSellingPrice();
                    discountTotal += item.getProdSellingPrice() * item.getAdditionalDiscount() / 100 * item.getItemQty();
                    double discount = item.getProdSellingPrice() * item.getAdditionalDiscount() / 100 * item.getItemQty();
                    savedPrice = item.getProdListingPrice() - (item.getProdSellingPrice() - discount);
                    finalPrice = total - discountTotal;
                }
                setTotalView();
            }
        });
    }

    private void setTotalView() {
        tvGrandTotal.setText(getString(R.string.cardTotal, decimalFormat.format(total)));
        tvGrandTotal2.setText(getString(R.string.cardDiscountsTotal, decimalFormat.format(discountTotal)));
        tvGrandTotal3.setText(getString(R.string.cardTotal, decimalFormat.format(finalPrice)));
        tvGrandTotal4.setText(getString(R.string.cardSavedTotal, decimalFormat.format(savedPrice)));
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
        tvDate.setText(getString(R.string.monthPlaceholder, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)));
        cal = (Calendar) primaryCalendar.clone();

        calendarView.setDateSelected(CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)), true);
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
                                customDates.add(new CustomDate(month, response.body().get(i).getDates()[j]));
                            }
                        }
                        Log.i(TAG, "onResponse: " + response.body());

                        customCalenderAdapter.setCustomDates(customDates);
                        customCalenderAdapter.initialize();

                        if (customCalenderAdapter.getFirstDate() != null) {
                            primaryCalendar = (Calendar) customCalenderAdapter.getFirstDate().getCalendar().clone();
                            initializeCalendar();
                        } else {
                            primaryCalendar = Calendar.getInstance();
                            initializeCalendar();
                        }
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

    @OnClick(R.id.payment)
    public void makePayment() {

        getResponseList(createOrderCallbackListner);

    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Success " + s, Toast.LENGTH_SHORT).show();
        finalPrice = finalPrice / 100;
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "" + i + " Payment Fail " + s, Toast.LENGTH_SHORT).show();
    }

    private void getResponseList(CreateOrder callback) {
        if (order != null) {
            procedeWithPayment();
            return;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        Log.i(TAG, "getResponseList: "+ finalPrice);
        int period = 1;
        int frequecy = 7;
        Double startDate = (primaryCalendar.getTimeInMillis()/1000.00);
        int startDateTimeStamp = startDate.intValue();
        String orderType = "Subscriprion";
        Double orderPrice = finalPrice;

        ApiClient.getClient2().create(ApiService.class).createOrder(
                uid,
                period,
                frequecy,
                startDateTimeStamp,
                orderType,
                orderPrice
        ).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Log.i(TAG, "onResponse: " + response.body());
                if (!response.isSuccessful()) {
                    Toast.makeText(SubscribeActivity.this, "Something Not rignt", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body() == null) {
                    Toast.makeText(SubscribeActivity.this, "Something Not rignt", Toast.LENGTH_SHORT).show();
                    callback.onOrderCreadted(null);
                    return;
                }
                callback.onOrderCreadted(response.body());
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(SubscribeActivity.this, "Something Not rignt" + t.getMessage(), Toast.LENGTH_SHORT).show();
                callback.onOrderCreadted(null);
            }
        });
    }

    private CreateOrder createOrderCallbackListner = new CreateOrder() {
        @Override
        public void onOrderCreadted(Order localOrder) {
            if (localOrder == null) {
                Toast.makeText(SubscribeActivity.this, "Something Not Right", Toast.LENGTH_SHORT).show();
                return;
            }
            order = localOrder;
            procedeWithPayment();
        }
    };

    public void procedeWithPayment() {
        Checkout checkout = new Checkout();
        checkout.setImage(R.mipmap.ic_launcher);
        final Activity activity = SubscribeActivity.this;
        finalPrice = finalPrice * 100;
        int totalinpaisa = finalPrice.intValue();
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Integro Infotech");
            options.put("description", "Reference No. #123456");
            options.put("order_id", order.getOrderId());
            options.put("currency", "INR");
            options.put("amount", totalinpaisa);
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }
}
