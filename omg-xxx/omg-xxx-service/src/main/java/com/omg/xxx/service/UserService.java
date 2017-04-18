package com.omg.xxx.service;

import com.omg.xxx.dal.model.User;

/**
 * Created by wenjing on 2017-4-18.
 */
public interface UserService {

    User getById(Long id);

    void save(User user);
}
