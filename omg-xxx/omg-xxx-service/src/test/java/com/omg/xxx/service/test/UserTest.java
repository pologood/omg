package com.omg.xxx.service.test;

import com.omg.xxx.dal.model.User;
import com.omg.xxx.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created by wenjing on 2017-4-18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring/applicationContext-*" })
public class UserTest {

    @Autowired
    private UserService userService;

    @Test
    public void saveUserTest() {
        User user = new User();
        Date date = new Date();
        user.setName("NAME" + date.getTime());
        user.setUserId("ID" + date.getTime());
        userService.save(user);
    }

    @Test
    public void selectUser() {
        User user = userService.getById(1L);
        System.out.print(user.getName());
    }
}
