package com.bwie.service.impl;

import com.bwie.dao.ProductEarningRateDao;
import com.bwie.service.ProductEarningRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: admin
 * @Date: 2019/5/14 08:46
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductEarningRateServiceImpl implements ProductEarningRateService {
    @Autowired
    private ProductEarningRateDao productEarningRateDao;

}
