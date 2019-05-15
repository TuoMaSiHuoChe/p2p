package com.bwie.dao;

import com.bwie.pojo.UserAccountModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 
* @ClassName: UserAccountModel
* @Description: 用户账户实体类
 */

public interface UserAccountDao extends JpaRepository<UserAccountModel,Integer>,JpaSpecificationExecutor<UserAccountModel> {


	
}
