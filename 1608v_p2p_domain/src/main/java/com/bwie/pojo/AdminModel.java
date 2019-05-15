package com.bwie.pojo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: admin
 * @Date: 2019/5/13 19:23
 * @Description:
 */

@Entity
@Table(name = "t_admin")
public class AdminModel implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "t_username",length = 20)
    private String usernaem;
    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsernaem() {
        return usernaem;
    }

    public void setUsernaem(String usernaem) {
        this.usernaem = usernaem;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
