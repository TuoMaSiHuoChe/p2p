package com.bwie.dao;

import com.bwie.pojo.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author: admin
 * @Date: 2019/5/13 19:27
 * @Description:
 */

public interface AdminDao extends JpaRepository<AdminModel,Integer>,JpaSpecificationExecutor<AdminModel> {
    @Query("select a from AdminModel  a where a.usernaem=?1 and a.password=?2")
    AdminModel findOneByUsernaemAndPassword(String username,String password);
}
