package com.bwie.service;

import com.bwie.pojo.UserAccountModel;

/**
 * @author: admin
 * @Date: 2019/5/14 21:42
 * @Description:
 */
public interface UserAccountService {
    void addUserAccount(int userId);

    UserAccountModel findById(int userid);
}
