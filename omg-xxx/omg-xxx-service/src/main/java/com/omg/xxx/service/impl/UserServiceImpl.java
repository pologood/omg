package com.omg.xxx.service.impl;

import com.omg.xxx.dal.model.User;
import com.omg.xxx.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wenjing on 2017-4-18.
 */
@Service
public class UserServiceImpl implements UserService {

    @Transactional
    public void save(User user) {

    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return null;
    }
}
