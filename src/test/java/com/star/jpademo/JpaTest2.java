package com.star.jpademo;

import com.star.jpademo.api.business.UserService;
import com.star.jpademo.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by User on 2017/6/1.
 */

@ContextConfiguration("classpath:applicationContext2.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JpaTest2 {

    @Autowired
    UserService userService;

//    @Test
    public void test(){
        User user = new User();
        user.setName("ye");
        userService.save(user);
    }

    @Test
    public void test2(){
//        System.out.println(userService.get(2).getName());
        List<User> userList = userService.findByName("ye");
        System.out.println(userList.size());
    }

//    @Test
    public void testPageable(){
        List<User> userList = userService.findByName("ye",1,2);
        System.out.println(userList.size());
    }

//    @Test
    public void testSort(){
        List<User> userList = userService.findByName("ye", Sort.Direction.DESC,"id");
        System.out.println(userList.size());
    }

//    @Test
    public void testQuery(){
        List<User> userList = userService.findByAge(20);
        System.out.println(userList.size());
    }

//    @Test
    public void testSpecification(){
        List<User> userList = userService.findAll("ye",20);
        System.out.println(userList.size());
    }


}
