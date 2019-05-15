package com.bwie.dao;

import com.bwie.pojo.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 
* @ClassName: UserAccountModel
* @Description: 用户账户实体类
 */

public interface UserDao extends JpaRepository<UserModel,Integer>,JpaSpecificationExecutor<UserModel> {

    UserModel findOneByUsername(String uername);
    UserModel findOneByPhone(String phone);
    UserModel findByUsernameAndPassword(String username,String password);

    UserModel findById(int userid);
}
