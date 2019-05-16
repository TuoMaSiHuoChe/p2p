package com.bwie.service.impl;

import com.bwie.dao.UserAccountDao;
import com.bwie.pojo.UserAccountModel;
import com.bwie.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: admin
 * @Date: 2019/5/14 21:42
 * @Description:
 */
@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {
    @Autowired
    private UserAccountDao userAccountDao;

    @Override
    public void addUserAccount(int userId) {
        UserAccountModel userAccountModel = new UserAccountModel();
        userAccountModel.setUserId(userId);
        userAccountDao.save(userAccountModel);
    }

    @Override
    public UserAccountModel findById(int userid) {
        return userAccountDao.findByUserId(Integer.parseInt(userid+""));
    }
}
