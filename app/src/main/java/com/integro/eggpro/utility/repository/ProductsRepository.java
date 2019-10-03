package com.integro.eggpro.utility.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.integro.eggpro.utility.dao.ProductItemDao;
import com.integro.eggpro.utility.database.IbEGGX;
import com.integro.eggpro.utility.entity.Product;

import java.util.List;

public class ProductsRepository {

    private ProductItemDao productItemDao;

    private LiveData<List<Product>> products;

    public ProductsRepository(Application application) {
        IbEGGX database = IbEGGX.getInstance(application);
        productItemDao = database.productItemDao();
        products = productItemDao.getProducts();
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    public void updateProduct(Product product) {
        new UpdateAsyncTask(productItemDao).execute(product);
    }

    public void addProduct(Product product) {
        new AddProductAsyncTask(productItemDao).execute(product);
    }

    public void clearProducts() {
        new ClearProductsAsyncTask(productItemDao).execute();
    }

    private class UpdateAsyncTask extends AsyncTask<Product,Void,Void> {
        private ProductItemDao productItemDao;
        public UpdateAsyncTask(ProductItemDao productItemDao) {
            this.productItemDao = productItemDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productItemDao.update(products[0]);
            return null;
        }
    }

    private class AddProductAsyncTask extends AsyncTask<Product, Void, Void> {

        private ProductItemDao productItemDao;

        public AddProductAsyncTask(ProductItemDao productItemDao) {
            this.productItemDao = productItemDao;
        }

        @Override
        protected Void doInBackground(Product... products) {
            productItemDao.insert(products[0]);
            return null;
        }
    }

    private class ClearProductsAsyncTask extends AsyncTask<Void,Void,Void>{

        private ProductItemDao productItemDao;

        public ClearProductsAsyncTask(ProductItemDao productItemDao) {
            this.productItemDao = productItemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            productItemDao.clearProducts();
            return null;
        }
    }
}
