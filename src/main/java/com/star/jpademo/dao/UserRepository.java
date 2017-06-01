package com.star.jpademo.dao;

import com.star.jpademo.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by User on 2017/6/1.
 */

@Repository
public interface UserRepository extends CrudRepository<User,Integer> {

}
