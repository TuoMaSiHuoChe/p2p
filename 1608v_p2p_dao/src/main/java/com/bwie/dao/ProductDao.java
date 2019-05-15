package com.bwie.dao;

import com.bwie.pojo.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: admin
 * @Date: 2019/5/14 08:28
 * @Description:
 */
public interface ProductDao extends JpaRepository<Product,Long>,JpaSpecificationExecutor<Product> {
    Product findOneByProId(Long proId);
}
