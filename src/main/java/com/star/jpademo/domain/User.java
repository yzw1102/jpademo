package com.star.jpademo.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by User on 2017/6/1.
 */
@Entity
@Table(name="t_user")
public class User implements Serializable{

    @Id
    @GeneratedValue
    private Integer id ;

    @Column
    private String name;

    @Column
    private Integer age;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
