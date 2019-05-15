package com.bwie.service.impl;

import com.bwie.dao.UserDao;
import com.bwie.pojo.UserModel;
import com.bwie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: admin
 * @Date: 2019/5/14 15:17
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public UserModel validateUserName(String username) {
        return userDao.findOneByUsername(username);
    }

    @Override
    public UserModel validatePhone(String phone) {
        return userDao.findOneByPhone(phone);
    }

    @Override
    public boolean addUser(UserModel userModel) {
        try {
            userDao.save(userModel);
            return true;
        } catch (Exception e) {
            return false;
        }

    }



    @Override
    public UserModel login(String username, String pwd) {
        return userDao.findByUsernameAndPassword(username,pwd);
    }

    @Override
    public UserModel findById(int userid) {
        return userDao.findById(userid);
    }


}
