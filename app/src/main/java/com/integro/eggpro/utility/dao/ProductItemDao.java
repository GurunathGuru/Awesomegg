package com.integro.eggpro.utility.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.integro.eggpro.utility.entity.Product;

import java.util.List;

@Dao
public interface ProductItemDao {

    @Insert
    void insert(Product product);

    @Update
    void update(Product product);

    @Query("Delete From products")
    void clearProducts();

    @Query("Select * From products")
    LiveData<List<Product>> getProducts();

}
