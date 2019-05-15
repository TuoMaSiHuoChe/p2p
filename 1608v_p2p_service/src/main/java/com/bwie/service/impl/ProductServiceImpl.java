package com.bwie.service.impl;

import com.bwie.dao.ProductDao;
import com.bwie.dao.ProductEarningRateDao;
import com.bwie.pojo.Product;
import com.bwie.pojo.ProductEarningRate;
import com.bwie.service.ProductService;
import com.bwie.until.ProductStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: admin
 * @Date: 2019/5/14 08:46
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductEarningRateDao productEarningRateDao;

    @Override
    public List<Product> findAllProduct() {
        List<Product> all = productDao.findAll();
        for (Product product : all) {
            changeStatusToChinese(product);
        }

        return all;
    }

    @Override
    public Product findProductById(Long proId) {
        Product oneByProId = productDao.findOneByProId(proId);
        changeStatusToChinese(oneByProId);
        return oneByProId;
    }

    @Override
    public List<ProductEarningRate> findRates(Integer proId) {
        return productEarningRateDao.findByProductId(proId);
    }

    @Override
    public void update(Product product) {
        List<ProductEarningRate> proEarningRate = product.getProEarningRate();
        if(proEarningRate!=null){
            productEarningRateDao.deleteByProId(Integer.parseInt(product.getProId()+""));
        }
        productEarningRateDao.save(proEarningRate);
        productDao.save(product);
    }

    @Override
    public void save(Product product) {
        Product save = productDao.save(product);
        List<ProductEarningRate> proEarningRate = product.getProEarningRate();
        if(proEarningRate!=null){
            for (ProductEarningRate productEarningRate : proEarningRate) {
                productEarningRate.setProductId(Integer.parseInt(save.getProId()+""));
            }
        }
        productEarningRateDao.save(proEarningRate);

    }

    @Override
    public void delete(Long proId) {
        productEarningRateDao.deleteByProId(Integer.parseInt(proId+""));
        productDao.delete(proId);
    }


    /**
     * 方法描述：将状态转换为中文
     *
     * @param product void
     */
    private void changeStatusToChinese(Product product) {
        if (null == product) {
            return;
        }

        int way = product.getWayToReturnMoney();
        // 每月部分回款
        if (ProductStyle.REPAYMENT_WAY_MONTH_PART.equals(String.valueOf(way))) {
            product.setWayToReturnMoneyDesc("每月部分回款");
            // 到期一次性回款
        } else if (ProductStyle.REPAYMENT_WAY_ONECE_DUE_DATE.equals(String.valueOf(way))) {
            product.setWayToReturnMoneyDesc("到期一次性回款");
        }

        // 是否复投 isReaptInvest 136：是、137：否
        // 可以复投
        if (ProductStyle.CAN_REPEAR == product.getIsRepeatInvest()) {
            product.setIsRepeatInvestDesc("是");
            // 不可复投
        } else if (ProductStyle.CAN_NOT_REPEAR == product.getIsRepeatInvest()) {
            product.setIsRepeatInvestDesc("否");
        }
        // 年利率
        if (ProductStyle.ANNUAL_RATE == product.getEarningType()) {
            product.setEarningTypeDesc("年利率");
            // 月利率 135
        } else if (ProductStyle.MONTHLY_RATE == product.getEarningType()) {
            product.setEarningTypeDesc("月利率");
        }

        if (ProductStyle.NORMAL == product.getStatus()) {
            product.setStatusDesc("正常");
        } else if (ProductStyle.STOP_USE == product.getStatus()) {
            product.setStatusDesc("停用");
        }

        // 是否可转让
        if (ProductStyle.CAN_NOT_TRNASATION == product.getIsAllowTransfer()) {
            product.setIsAllowTransferDesc("否");
        } else if (ProductStyle.CAN_TRNASATION == product.getIsAllowTransfer()) {
            product.setIsAllowTransferDesc("是");
        }
    }

}
