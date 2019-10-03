package com.integro.eggpro.utility.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.integro.eggpro.utility.dao.CartItemDao;
import com.integro.eggpro.utility.database.IbEGGX;
import com.integro.eggpro.utility.entity.CartItem;

import java.util.List;

public class CartItemsRepository {

    private CartItemDao cartItemDao;

    private LiveData<List<CartItem>> cart;

    public CartItemsRepository(Application application) {
        IbEGGX database =IbEGGX.getInstance(application);
        cartItemDao = database.cartItemDao();
        cart = cartItemDao.getCart();
    }

    public LiveData<List<CartItem>> getCart() {
        return cart;
    }

    public void addItem(CartItem item) {
        new InsertItemAsyncTask(cartItemDao).execute(item);
    }

    public void updateItem(CartItem item) {
        new UpdateItemAsyncTask(cartItemDao).execute(item);
    }

    public void removeItem(CartItem item) {
        new RemoveItemAsyncTask(cartItemDao).execute(item);
    }

    public void clearCart() {
        new ClearCartAsyncTask(cartItemDao).execute();
    }

    public LiveData<List<CartItem>> getCartData() {
        return cart;
    }

    private class InsertItemAsyncTask extends AsyncTask<CartItem, Void, Void> {

        private CartItemDao cartItemDao;

        public InsertItemAsyncTask(CartItemDao cartItemDao) {
            this.cartItemDao = cartItemDao;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            cartItemDao.insert(cartItems[0]);
            return null;
        }
    }

    private class UpdateItemAsyncTask extends AsyncTask<CartItem, Void, Void> {

        private CartItemDao cartItemDao;

        public UpdateItemAsyncTask(CartItemDao cartItemDao) {
            this.cartItemDao = cartItemDao;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            cartItemDao.update(cartItems[0]);
            return null;
        }
    }

    private class RemoveItemAsyncTask extends AsyncTask<CartItem, Void, Void> {

        private CartItemDao cartItemDao;

        public RemoveItemAsyncTask(CartItemDao cartItemDao) {
            this.cartItemDao = cartItemDao;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            cartItemDao.delete(cartItems[0]);
            return null;
        }
    }

    private class ClearCartAsyncTask extends AsyncTask<Void, Void, Void> {

        private CartItemDao cartItemDao;

        public ClearCartAsyncTask(CartItemDao cartItemDao) {
            this.cartItemDao = cartItemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cartItemDao.clearCart();
            return null;
        }
    }

}
