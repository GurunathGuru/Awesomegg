package com.integro.eggpro;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.integro.eggpro.adapters.OrderItemListAdapter;
import com.integro.eggpro.model.Items;

import java.util.ArrayList;

import static com.integro.eggpro.constants.GenralConstants.ITEM_LIST;

public class OrderDetailsActivity extends AppCompatActivity {
    ArrayList<Items> items;
    private static final String TAG = "OrderDetailsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Log.i(TAG, "onCreate: "+getIntent().getSerializableExtra(ITEM_LIST));


        items= (ArrayList<Items>) getIntent().getSerializableExtra(ITEM_LIST);
        Log.i(TAG, "onCreate, items: "+items);

        RecyclerView recyclerView = findViewById(R.id.rvOrderList);
        OrderItemListAdapter adapter = new OrderItemListAdapter(items,getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}
