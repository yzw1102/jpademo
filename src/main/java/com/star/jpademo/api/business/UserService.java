package com.star.jpademo.api.business;

import com.star.jpademo.domain.User;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by User on 2017/6/1.
 */
public interface UserService {

    void save(User user);

    User get(Integer id);

    List<User> findByName(String name);

    List<User> findByName(String name,Integer pageNumber,Integer pageSize);

    List<User> findByName(String name, Sort.Direction direction, String columnName);

    List<User> findByAge(Integer age);

    List<User> findAll(String name,Integer age);
}
