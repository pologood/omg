package com.omg.xxx.service.test;

import com.omg.xxx.dal.model.User;
import com.omg.xxx.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;

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
    public void selectUserTest() {
        User user = userService.getById(1L);
        System.out.print(user.getName());
    }

    @Test
    public void selectAllUserTest() {

        System.out.print(userService.getUsers().size());
    }

    @Test
    public void test() {
//        traverseFolder1("D:\\workspace\\omg\\omg-framework\\omg-framework-common-utils\\src\\main\\java\\com\\omg\\framework\\common");
        new Modify("D:\\workspace\\omg\\omg-framework\\omg-framework-common-utils\\src\\main\\java\\com\\omg\\framework\\common", ".eif.", ".omg.");
    }

    public void traverseFolder1(String path) {
        int fileNum = 0, folderNum = 0;
        File file = new File(path);
        if (file.exists()) {
            LinkedList<File> list = new LinkedList<File>();
            File[] files = file.listFiles();
            for (File file2 : files) {
                if (file2.isDirectory()) {
                    System.out.println("文件夹:" + file2.getAbsolutePath());
                    list.add(file2);
                    fileNum++;
                } else {
                    System.out.println("文件:" + file2.getAbsolutePath());
                    folderNum++;
                }
            }
            File temp_file;
            while (!list.isEmpty()) {
                temp_file = list.removeFirst();
                files = temp_file.listFiles();
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        System.out.println("文件夹:" + file2.getAbsolutePath());
                        list.add(file2);
                        fileNum++;
                    } else {
                        System.out.println("文件:" + file2.getAbsolutePath());
                        folderNum++;
                        new Modify(file2.getAbsolutePath(), ".eif.", ".omg.");
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
        System.out.println("文件夹共有:" + folderNum + ",文件共有:" + fileNum);

    }

}
