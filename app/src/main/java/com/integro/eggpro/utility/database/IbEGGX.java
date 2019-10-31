package com.integro.eggpro.utility.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.integro.eggpro.utility.dao.CartItemDao;
import com.integro.eggpro.utility.dao.ProductItemDao;
import com.integro.eggpro.utility.entity.CartItem;
import com.integro.eggpro.utility.entity.Product;

@Database(
        entities = {CartItem.class, Product.class},
        version = 1
)
public abstract class IbEGGX extends RoomDatabase {

    private static IbEGGX instance;

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsyncTask(instance).execute();
            new PopulateProductsAsyncTask(instance).execute();
        }
    };

    public static synchronized IbEGGX getInstance(Context context) {
        if (instance ==null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), IbEGGX.class,"IbEGGX")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }

    public abstract CartItemDao cartItemDao();
    public abstract ProductItemDao productItemDao();

    private static class PopulateAsyncTask extends AsyncTask<Void, Void, Void> {

        private CartItemDao cartItemDao;

        private PopulateAsyncTask(IbEGGX db) {
            cartItemDao = db.cartItemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

    private static class PopulateProductsAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProductItemDao productItemDao;

        private static final String TAG = "PopulateProductsAsyncTa";

        public PopulateProductsAsyncTask(IbEGGX db) {
            productItemDao = db.productItemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
