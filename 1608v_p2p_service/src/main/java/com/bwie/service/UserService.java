package com.bwie.service;

import com.bwie.pojo.UserModel;

/**
 * @author: admin
 * @Date: 2019/5/14 15:17
 * @Description:
 */
public interface UserService {
    UserModel validateUserName(String username);

    UserModel validatePhone(String phone);

    boolean addUser(UserModel userModel);




    UserModel login(String username, String pwd);

    UserModel findById(int userid);

    void updatePhoneStatus(String phone, int id);

    void updateRealName(String realName, String identity, int id);

    UserModel findByEmail(String email);
}
