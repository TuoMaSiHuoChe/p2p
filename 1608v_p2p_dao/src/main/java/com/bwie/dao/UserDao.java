package com.bwie.dao;

import com.bwie.pojo.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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

    @Modifying
    @Query("update UserModel u set u.phone=?1,u.phoneStatus=1 where u.id=?2")
    void updatePhoneStatus(String phone, Integer id);
    @Modifying
    @Query("update UserModel u set u.realName=?1,u.identity=?2,u.realNameStatus=1 where u.id=?3")
    void updateRealName(String realName, String identity, Integer i);

    UserModel findByEmail(String email);
}
