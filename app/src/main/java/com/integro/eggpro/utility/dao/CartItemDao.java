package com.integro.eggpro.utility.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.integro.eggpro.utility.entity.CartItem;

import java.util.List;

@Dao
public interface CartItemDao {

    @Insert
    void insert(CartItem item);

    @Update
    void update(CartItem item);

    @Delete
    void delete(CartItem item);

    @Query("DELETE FROM cartItem")
    void clearCart();

    @Query("Select * From cartItem")
    LiveData<List<CartItem>> getCart();
}
