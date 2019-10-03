package com.integro.eggpro.utility.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.integro.eggpro.utility.entity.Product;
import com.integro.eggpro.utility.repository.ProductsRepository;

import java.util.List;

public class ProductsViewModel extends AndroidViewModel {

    private ProductsRepository productsRepository;

    private LiveData<List<Product>> products;

    public ProductsViewModel(@NonNull Application application) {
        super(application);
        productsRepository = new ProductsRepository(application);
    }

    public LiveData<List<Product>> getProducts() {
        return products = productsRepository.getProducts();
    }

    public void updateProduct(Product product) {
        productsRepository.updateProduct(product);
    }

    public void addProduct(Product product) {
        productsRepository.addProduct(product);
    }

    public void clearProducts() {
        productsRepository.clearProducts();
    }
}
