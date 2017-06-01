package com.star.jpademo.api.business;

import com.star.jpademo.domain.User;

import java.util.List;

/**
 * Created by User on 2017/6/1.
 */
public interface UserService {

    void save(User user);

    void update(User user);

    void delete(Integer id);

    User get(Integer id);

    List<User> list();

}
