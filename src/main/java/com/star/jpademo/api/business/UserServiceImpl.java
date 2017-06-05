package com.star.jpademo.api.business;

import com.star.jpademo.dao.UserRepository;
import com.star.jpademo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
    public User get(Integer id){
        return userRepository.findById(id);
    }

    @Override
    public List<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public List<User> findByName(String name,Integer pageNumber,Integer pageSize) {
        Pageable pageable = new PageRequest(pageNumber,pageSize);
        return userRepository.findByName(name,pageable);
    }

    @Override
    public List<User> findByName(String name, Sort.Direction direction,String columnName) {
        Sort sort = new Sort(direction,columnName);
        return userRepository.findByName(name,sort);
    }

    @Override
    public List<User> findByAge(Integer age) {
        return userRepository.findByAge(age);
    }

    @Override
    public List<User> findAll(String name, Integer age) {
       return userRepository.findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate p1 = cb.like(root.get("name"),"%" + name +"%" );
                Predicate p2 = cb.equal(root.get("age"),age);
                query.where(cb.and(p1,p2));
                query.orderBy(cb.desc(root.get("id")));
                return query.getRestriction();
            }
        });
    }
}
