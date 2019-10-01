package com.integro.eggpro;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.integro.eggpro.adapters.CustomCalenderAdapter;
import com.integro.eggpro.adapters.ProductAdapter;
import com.integro.eggpro.adapters.SubscribeAdapter;
import com.integro.eggpro.apis.ApiClient;
import com.integro.eggpro.apis.ApiService;
import com.integro.eggpro.interfaces.OnDateSelected;
import com.integro.eggpro.model.CustomCalender;
import com.integro.eggpro.model.CustomDate;
import com.integro.eggpro.model.Products;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscribeActivity extends AppCompatActivity {
    private static final String TAG = "SubscribeActivity";
    private DatePickerDialog picker;
    private RadioGroup radioGroup;
    private MaterialCalendarView calendarView;
    private EditText tvDate;
    private Calendar primaryCalendar = Calendar.getInstance();
    private TextView tvGrandTotal,tvGrandTotal2,tvAddItem;
    private RecyclerView rvSubscribe;
    private SubscribeAdapter adapter;

    private RecyclerView recyclerView;
    private TextView monthView;
    private CustomCalenderAdapter customCalenderAdapter = new CustomCalenderAdapter();
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
            if (radioButton.getText().toString().contentEquals(getString(R.string.evr_three))) {
                primaryCalendar.set(Calendar.YEAR, date.getCalendar().get(Calendar.YEAR));
                primaryCalendar.set(Calendar.MONTH, date.getCalendar().get(Calendar.MONTH));
                primaryCalendar.set(Calendar.DAY_OF_MONTH, date.getCalendar().get(Calendar.DAY_OF_MONTH));
                everyThreeDays();
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

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("CARTLIST");

        Log.i(TAG, "onCreate: "+bundle);
        ArrayList<Products> products = (ArrayList<Products>) bundle.getSerializable("CARTLIST");

        tvAddItem=findViewById(R.id.tvAddItem);
        tvGrandTotal=findViewById(R.id.tvGrandTotal);
        tvGrandTotal2=findViewById(R.id.tvGrandTotal2);

        tvAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent1);
            }
        });

        rvSubscribe = findViewById(R.id.rvSubscribe);
        adapter = new SubscribeAdapter(SubscribeActivity.this, products);
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

      /* tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(SubscribeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            }
                        }, year, month, day);
                picker.show();
                picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            }
        })*/;

        initializeCalendar();
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);

                if (null != rb && checkedId > -1) {
                    if (rb.getText().toString().contentEquals(getString(R.string.evr_three))) {
                        everyThreeDays();
                    } else {
                        everySevenDays();
                    }
                }
            }
        });
    }

    @SuppressLint("WrongConstant")
    private Calendar initializeCalendar() {
        Calendar cal = Calendar.getInstance();
        Log.i(TAG, "initializeCalendar: ");
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE); // Removes onClick functionality
        for (int i = 0; i < 365; i++) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            calendarView.setDateSelected(CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)), false);
        }
        cal = (Calendar) primaryCalendar.clone();
        tvDate.setText(getString(R.string.monthPlaceholder, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR)));
        cal = (Calendar) primaryCalendar.clone();

        calendarView.setDateSelected(CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)), true);
        return cal;
    }

    public void everyThreeDays() {
        Log.i(TAG, "everyThreeDays: ");
        Calendar cal = initializeCalendar();
        for (int i = 0; i < 10; i++) {
            cal.add(Calendar.DAY_OF_MONTH, 3);
            calendarView.setDateSelected(CalendarDay.from(cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)), true);
        }
    }

    public void everySevenDays() {
        Calendar cal = initializeCalendar();
        Log.i(TAG, "everySevenDays: ");
        for (int i = 0; i < 4; i++) {
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
}
