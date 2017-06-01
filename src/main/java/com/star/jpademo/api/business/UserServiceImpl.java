package com.star.jpademo.api.business;

import com.star.jpademo.dao.UserRepository;
import com.star.jpademo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/6/1.
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(Integer id) {
        userRepository.delete(id);
    }

    @Override
    public User get(Integer id) {
       return userRepository.findOne(id);
    }

    @Override
    public List<User> list() {
        List<User> list = new ArrayList<>();
        userRepository.findAll().forEach( (u) -> list.add(u) );
        return list;
    }

}
