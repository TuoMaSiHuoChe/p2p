package com.bwie.service;

import com.bwie.pojo.Product;
import com.bwie.pojo.ProductEarningRate;

import java.util.List;

/**
 * @author: admin
 * @Date: 2019/5/14 08:46
 * @Description:
 */
public interface ProductService {
    List<Product> findAllProduct();

    Product findProductById(Long proId);

    List<ProductEarningRate> findRates(Integer proId);

    void update(Product product);

    void save(Product product);

    void delete(Long proId);
}
