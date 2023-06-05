package com.example.exampleapp.listener;

import com.example.exampleapp.model.ProductModel;

import java.util.List;

public interface IProductLoadListener {
    void onProductLoadSuccess(List<ProductModel> productModelList);
    void onProductLoadFailed(String message);
}
