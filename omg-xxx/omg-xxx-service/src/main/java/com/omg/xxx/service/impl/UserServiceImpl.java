package com.omg.xxx.service.impl;

import com.omg.framework.data.mybatis.pagination.pagehelper.PageHelper;
import com.omg.xxx.dal.dao.UserMapper;
import com.omg.xxx.dal.model.User;
import com.omg.xxx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wenjing on 2017-4-18.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void save(User user) {
        userMapper.insert(user);
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers() {

        PageHelper.startPage(1, 2);
        return userMapper.selectAll();
    }

}
