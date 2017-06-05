package com.star.jpademo.dao;

import com.star.jpademo.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

/**
 * Created by User on 2017/6/1.
 */

public interface UserRepository extends Repository<User,Integer>,JpaSpecificationExecutor<User> {

    void save(User user);

    User findById(Integer id);

    List<User> findByName(String name);

    List<User> findByName(String name, Pageable pageable);

    List<User> findByName(String name, Sort sort);

    @Query("select u from User u where u.age = ?1")
    List<User> findByAge(Integer age);

    List<User> findAll(Specification<User> spec);

}
