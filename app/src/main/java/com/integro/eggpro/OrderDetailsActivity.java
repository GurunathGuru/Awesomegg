package com.integro.eggpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.integro.eggpro.model.Items;
import com.integro.eggpro.adapters.OrderItemListAdapter;

import java.util.ArrayList;

import static com.integro.eggpro.constants.GenralConstants.ITEM_LIST;

public class OrderDetailsActivity extends AppCompatActivity {
    ArrayList<Items> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        items= (ArrayList<Items>) getIntent().getSerializableExtra(ITEM_LIST);

        RecyclerView recyclerView = findViewById(R.id.rvOrderList);
        OrderItemListAdapter adapter = new OrderItemListAdapter(items,getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
