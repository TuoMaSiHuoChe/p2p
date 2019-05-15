package com.bwie.service.impl;

import com.bwie.dao.AdminDao;
import com.bwie.pojo.AdminModel;
import com.bwie.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: admin
 * @Date: 2019/5/13 19:43
 * @Description:
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminDao adminDao;
    @Override
    public AdminModel login(String username, String password) {
        return adminDao.findOneByUsernaemAndPassword(username,password);
    }
}
