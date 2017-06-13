# Spring Data JPA使用说明
## 简介
Spring data jpa 是由spring提供的一个用于简化JPA开发的框架.它可以极大的简化JPA的写法,可以再几乎不用写实现的情况下,实现对数据库的访问和操作,除了CRUD外,还包括如分页,排序等一些常用的功能。

## 简单示例

### 创建jpademo

jpademo是用gradle创建的 java项目

- 在项目中添加依赖的jar包

```gradle 
dependencies {
    testCompile 'junit:junit:4.12'
    compile 'org.springframework:spring-context:4.2.0.RELEASE'
    compile 'org.springframework:spring-orm:4.2.0.RELEASE'
    compile 'org.springframework:spring-test:4.2.0.RELEASE'
    compile 'org.springframework.data:spring-data-jpa:1.8.2.RELEASE'
    compile 'org.hibernate:hibernate-entitymanager:5.1.0.Final'
    compile 'mysql:mysql-connector-java:5.1.38'
}
```

- 在src/main/resources 下面创建META-INF目录，并在META-INF创建persistence.xml文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.0" xmlns="http://java.sun.com/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/persistence
            http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd">

    <persistence-unit name="myJPA" transaction-type="RESOURCE_LOCAL">
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
        <class>com.star.jpademo.domain.User</class>
        <properties>
            <!--配置Hibernate方言 -->
            <property name="hibernate.dialect" value="org.hibernate.dialect.MySQL5Dialect" />
            <!--配置数据库驱动 -->
            <property name="hibernate.connection.driver_class" value="com.mysql.jdbc.Driver" />
            <!--配置数据库用户名 -->
            <property name="hibernate.connection.username" value="root" />
            <!--配置数据库密码 -->
            <property name="hibernate.connection.password" value="root" />
            <!--配置数据库url -->
            <property name="hibernate.connection.url" value="jdbc:mysql://localhost:3306/jpa?useUnicode=true&amp;characterEncoding=UTF-8" />
            <!--设置外连接抓取树的最大深度 -->
            <property name="hibernate.max_fetch_depth" value="3" />
            <!--自动输出schema创建DDL语句 -->
            <property name="hibernate.hbm2ddl.auto" value="update" />
            <property name="hibernate.show_sql" value="true" />
            <property name="hibernate.format_sql" value="true" />
            <property name="javax.persistence.validation.mode" value="none"/>
        </properties>
    </persistence-unit>
</persistence>
```

- 在src/main/resources目录创建spring配置文件 application.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:jpa="http://www.springframework.org/schema/data/jpa"

       xsi:schemaLocation="http://www.springframework.org/schema/beans
          http://www.springframework.org/schema/beans/spring-beans-3.1.xsd
          http://www.springframework.org/schema/context
          http://www.springframework.org/schema/context/spring-context-3.1.xsd
          http://www.springframework.org/schema/tx
          http://www.springframework.org/schema/tx/spring-tx-3.1.xsd
          http://www.springframework.org/schema/data/jpa
          http://www.springframework.org/schema/data/jpa/spring-jpa.xsd">

    <context:annotation-config />

    <context:component-scan base-package="com.star.jpademo"/>

    <!-- 定义实体管理器工厂 -->
    <bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
        <property name="persistenceUnitName" value="myJPA"/>
    </bean>

    <!-- 配置事务管理器 -->
    <bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
        <property name="entityManagerFactory" ref="entityManagerFactory" />
    </bean>

    <!-- 启用 annotation事务-->
    <tx:annotation-driven transaction-manager="transactionManager"/>

    <!-- 配置Spring Data JPA扫描目录-->
    <jpa:repositories base-package="com.star.jpademo.dao"/>
    
</beans>

```

- 实体对象 User

```java 
@Entity
@Table(name="t_user")
public class User implements Serializable{

    @Id
    @GeneratedValue
    private Integer id ;

    @Column
    private String name;
    
    // get set 忽略
}
```
- 持久层对象 UserRepository

```java
public interface UserRepository extends Repository<User,Integer> {
    void save(User user);
    User findById(Integer id);
}
```

- 业务层对象 UserServiceImpl

```java
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    public User get(Integer id){
        return userRepository.findById(id);
    }
```

- 测试代码

```java
@ContextConfiguration("classpath:applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JpaTest {

    @Autowired
    UserService userService;

    @Test
    public void test(){
        User user = new User();
        user.setName("ye");
        userService.save(user);
    }

//    @Test
    public void test2(){
        System.out.println(userService.get(1).getName());
    }
    
}
```
执行测试代码后，查询数据库，可以看到数据如我们预期的一样插入的数据库中了。

- 总结

开发Spring Data JPA大致需要三个步骤。

1. 声明实体类，并配置到persistence.xml中。
```xml
 <persistence-unit name="myJPA" transaction-type="RESOURCE_LOCAL">
        <class>com.star.jpademo.domain.User</class>
 </persistence-unit>
```
>在ottplatform 中配置成了扫描domain类。

2. 声明持久层借口，该接口继承Repository,Repository 是一个标记型接口，它不包含任何方法，当然如果有需要，Spring Data 也提供了若干 Repository 子接口，其中定义了一些常用的增删改查，以及分页相关的方法。
> 在spring配置文件中配置了扫描继承了Repository或者其子接口的接口，并创建代理对象，并将代理对象注册为Spring Bean,业务层便可以通过Spring自动封装的特性来直接使用对象。
```java
 <jpa:repositories base-package="com.star.jpademo.dao"/>
```

3. 在接口中声明需要的业务方法。Spring Data 将根据给定的策略（具体策略稍后讲解）来为其生成实现代码。


## 接口说明
### Repository：

这个接口只是一个空的接口，目的是为了统一所有Repository的类型，其接口类型使用了泛型，泛型参数中T代表实体类型，ID则是实体中id的类型。
```java
public interface Repository<T, ID extends Serializable> {

}
```

### CrudRepository：

持久层接口定义的比较多,且每个接口都需要声明相似的CURD的方法,直接继承Repository就比较繁琐.这时就可以继承CrudRepository,它会自动为域对象创建CRUD的方法,供业务层直接调用.
```java
public interface CrudRepository<T, ID extends Serializable> extends Repository<T, ID> {
	<S extends T> S save(S entity);
	<S extends T> Iterable<S> save(Iterable<S> entities);
	T findOne(ID id);
	boolean exists(ID id);
	Iterable<T> findAll();
	Iterable<T> findAll(Iterable<ID> ids);
	long count();
	void delete(ID id);
	void delete(T entity);
	void delete(Iterable<? extends T> entities);
	void deleteAll();
}
```

### PagingAndSortingRepository：
分页查询和排序是持久层常用的功能，Spring Data 为此提供了 PagingAndSortingRepository 接口，它继承自 CrudRepository 接口，在 CrudRepository 基础上新增了两个与分页有关的方法。
```java
public interface PagingAndSortingRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {
	Iterable<T> findAll(Sort sort);
	Page<T> findAll(Pageable pageable);
}
```

### JpaRepository：
这个类继承自PagingAndSortingRepository，看其中的方法，可以看出里面的方法都是一些简单的操作，并未涉及到复杂的逻辑。当你在处理一些简单的数据逻辑时，便可继承此接口.
```java
public interface JpaRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {
	List<T> findAll();
	List<T> findAll(Sort sort);
	List<T> findAll(Iterable<ID> ids);
	<S extends T> List<S> save(Iterable<S> entities);
	void flush();
	<S extends T> S saveAndFlush(S entity);
	void deleteInBatch(Iterable<T> entities);
	void deleteAllInBatch();
	T getOne(ID id);
}
```
### JpaSpecificationExecutor：
其实在业务中往往会涉及到多张表的查询，以及查询时需要的各种条件,此时就需要复杂业务查询了.spring data jpa提供了JpaSpecificationExecutor.
```java
public interface JpaSpecificationExecutor<T> {
	T findOne(Specification<T> spec);
	List<T> findAll(Specification<T> spec);
	Page<T> findAll(Specification<T> spec, Pageable pageable);
	List<T> findAll(Specification<T> spec, Sort sort);
	long count(Specification<T> spec);
}
```

### Specification：
 JpaSpecificationExecutor在这个接口里面出现次数最多的类就是Specification.class，而这个类主要也就是围绕Specification来打造 的，Specification.class是Spring Data JPA提供的一个查询规范，只需围绕这个规范来设置查询条件便可.
 ```java
public interface Specification<T> {
	Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);
}
 ```

## 查询策略

### 1. 通过解析方法名创建查询

框架在进行方法名解析时，会先把方法名多余的前缀截取掉，比如 find、findBy、read、readBy、get、getBy，然后对剩下部分进行解析。并且如果方法的最后一个参数是 Sort 或者 Pageable 类型，也会提取相关的信息，以便按规则进行排序或者分页查询。

直接在接口中定义查询方法，如果是符合规范的，可以不用写实现，目前支持的关键字写法如下：

关键字 | 例子 | JPQL 片段
---|---|---|
And | findByLastnameAndFirstname | where x.lastname =?1 and x.firstname =?2
Or | findByLastnameOrFirstname | where x.lastname = ?1 or x.firstname = ?2
Between | findByStartDateBetween | where x.startData between 1? and 2?
LessThan | findByAgeLessThan | where  x.age < ?1
GreaterThan | findByAgeGreaterThan | where x.age >?1
After | findByStartDateAfter | where x.startDate > ?1
Before | findByStartDateBefor | where   x.startDate < ?1
IsNull | findByAgeIsNull | where   x.age is  null
IsNotNull,NotNull | findByAge(Is)NotNull | where   x.age is not null
Like | findByFirstnameLike | where   x.firstname like ?1
NotLike | findByFirstnameNotLike | where   x.firstname not like ?1
StartingWith | findByFirstnameStartingWith | where x.firstname like ?1(如%?)
EndingWith | findByFirstnameEndingWith | where x.firstname like ?1(如?%)
Containing | findByFirstnameContaining | where x.fistaname like ?1(如%?%)
OrderBy | findByFirstnameOrderByAgeDesc | where x.firstname  =?1 Order by x.age desc
Not | findByLastnameNot | where   x.lastname <>?1
In | findByAgeIn(collegetion<Age> ages) | where x.age in ?1
NotIn | findByAgeNotIn(collegetion<Age> ages) | where x.age  not  in  ?1
True | findByActiveTrue | where x.active = true
false | findbyActiveFalse | where   x.active =false

- 示例代码 java
```java
public interface UserRepository extends Repository<User,Integer> {
    List<User> findByName(String name);
}
```
- hibernate打印输出的sql
```sql
    select
        user0_.id as id1_0_,
        user0_.name as name2_0_ 
    from
        t_user user0_ 
    where
        user0_.name=?
1
```

特殊的参数:还可以直接在方法的参数上加入分页或排序的参数
- 示例代码：
```java
public interface UserRepository extends Repository<User,Integer> {

    List<User> findByName(String name, Pageable pageable);

    List<User> findByName(String name, Sort sort);

}
```

- hibernate打印pageable输出sql：
```sql
    select
        user0_.id as id1_0_,
        user0_.name as name2_0_ 
    from
        t_user user0_ 
    where
        user0_.name=? limit ?, ?
```
- hibernate打印sort输出sql：
```sql
    select
        user0_.id as id1_0_,
        user0_.name as name2_0_ 
    from
        t_user user0_ 
    where
        user0_.name=? 
    order by
        user0_.id desc
```

### 2. 使用@query
@Query 注解的使用非常简单，只需在声明的方法上面标注该注解，同时提供一个 JPQL 查询语句即可，如下所示：


- 示例代码
```java
public interface UserRepository extends Repository<User,Integer> {

    @Query("select u from User u where u.age = ?1")
    List<User> findByAge(Integer age);

}
```
- hibernate 打印输出sql
```sql
    select
        user0_.id as id1_0_,
        user0_.age as age2_0_,
        user0_.name as name3_0_ 
    from
        t_user user0_ 
    where
        user0_.age=?
```

### 3. 使用Specifications
Spring Data JPA支持JPA2.0的Criteria查询，相应的接口是JpaSpecificationExecutor。Criteria 查询：是一种类型安全和更面向对象的查询 。

- 示例代码
```java
public interface UserRepository extends Repository<User,Integer>,JpaSpecificationExecutor<User> {
    List<User> findAll(Specification<User> spec);

}
```

```java
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

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
```
- hibernate 打印输出sql
```sql
    select
        user0_.id as id1_0_,
        user0_.age as age2_0_,
        user0_.name as name3_0_ 
    from
        t_user user0_ 
    where
        (
            user0_.name like ?
        ) 
        and user0_.age=20 
    order by
        user0_.id desc

```
### 4. 查询的顺序

Spring Data JPA 在为接口创建代理对象时，如果发现同时存在多种上述情况可用，它该优先采用哪种策略呢？为此，<jpa:repositories> 提供了 query-lookup-strategy 属性，用以指定查找的顺序。它有如下三个取值：
create --- 通过解析方法名字来创建查询。即使有符合的命名查询，或者方法通过 @Query 指定的查询语句，都将会被忽略。
create-if-not-found --- 如果方法通过 @Query 指定了查询语句，则使用该语句实现查询；如果没有，则查找是否定义了符合条件的命名查询，如果找到，则使用该命名查询；如果两者都没有找到，则通过解析方法名字来创建查询。这是 query-lookup-strategy 属性的默认值。
use-declared-query --- 如果方法通过 @Query 指定了查询语句，则使用该语句实现查询；如果没有，则查找是否定义了符合条件的命名查询，如果找到，则使用该命名查询；如果两者都没有找到，则抛出异常。

## Spring Data JPA 对事务的支持

默认情况下，Spring Data JPA 实现的方法都是使用事务的。针对查询类型的方法，其等价于 @Transactional(readOnly=true)；增删改类型的方法，等价于 @Transactional。可以看出，除了将查询的方法设为只读事务外，其他事务属性均采用默认值。
如果用户觉得有必要，可以在接口方法上使用 @Transactional 显式指定事务属性，该值覆盖 Spring Data JPA 提供的默认值。同时，开发者也可以在业务层方法上使用 @Transactional 指定事务属性，这主要针对一个业务层方法多次调用持久层方法的情况。持久层的事务会根据设置的事务传播行为来决定是挂起业务层事务还是加入业务层的事务。






