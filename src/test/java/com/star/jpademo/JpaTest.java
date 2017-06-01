package com.star.jpademo;

import com.star.jpademo.api.business.UserService;
import com.star.jpademo.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by User on 2017/6/1.
 */

@ContextConfiguration("classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JpaTest {

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
        System.out.println(userService.get(1).getName());
    }


}
