package com.bwie.dao;

import com.bwie.pojo.ProductEarningRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author: admin
 * @Date: 2019/5/14 08:30
 * @Description:
 */
public interface ProductEarningRateDao extends JpaRepository<ProductEarningRate,Integer>,JpaSpecificationExecutor<ProductEarningRate> {

    List<ProductEarningRate> findByProductId(Integer proId);

    @Modifying
    @Query("delete from ProductEarningRate p where p.productId=?1")
    void deleteByProId(Integer proId);
}
