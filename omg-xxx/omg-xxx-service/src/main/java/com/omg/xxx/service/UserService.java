package com.omg.xxx.service;

import com.omg.xxx.dal.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wenjing on 2017-4-18.
 */
public interface UserService {

    User getById(Long id);

    void save(User user);

    @Transactional(readOnly = true)
    List<User> getUsers();
}
