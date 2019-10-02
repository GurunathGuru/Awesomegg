package com.integro.eggpro.utility.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.integro.eggpro.utility.entity.CartItem;
import com.integro.eggpro.utility.repository.CartItemsRepository;

import java.util.List;

public class CartViewModel extends AndroidViewModel {

    private CartItemsRepository cartItemsRepository;

    private LiveData<List<CartItem>> cart;

    public CartViewModel(@NonNull Application application) {
        super(application);
        cartItemsRepository = new CartItemsRepository(application);
    }

    public LiveData<List<CartItem>> getCart() {
        return cart = cartItemsRepository.getCart();
    }

    public void addItem(CartItem item) {
        cartItemsRepository.addItem(item);
    }

    public void updateItem(CartItem item) {
        cartItemsRepository.updateItem(item);
    }

    public void removeItem(CartItem item) {
        cartItemsRepository.removeItem(item);
    }

    public void clearCart() {
        cartItemsRepository.clearCart();
    }
}
