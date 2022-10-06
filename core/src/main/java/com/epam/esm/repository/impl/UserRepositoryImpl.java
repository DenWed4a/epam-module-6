package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.repository.RoleRepository;
import com.epam.esm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private RoleRepository roleRepository;


    @Override
    public Optional<User> getUserById(Integer id) {
        Optional<User> result;
        try{
            result = Optional.of(entityManager.find(User.class, id));
        }catch (NullPointerException e){
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public Optional<User> getUserByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root).where(criteriaBuilder.like(root.get("login"), name));

        Optional<User> result;
        try{
            result = Optional.of(entityManager.createQuery(criteriaQuery).getSingleResult());
        }catch (NoResultException e){
            result = Optional.empty();
        }

        return result;
    }

    @Override
    public Optional<User> register(User user) {
        if(getUserByName(user.getLogin()).isEmpty()) {
            entityManager.persist(user);
            return Optional.of(user);
        }else {
            return Optional.empty();
        }

    }

    @Override
    public List<User> getAllUsers(Integer page, Integer pageSize) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);
        int firstResult = Pagination.firstResult(page, pageSize);
        return entityManager.createQuery(criteriaQuery).setFirstResult(firstResult).setMaxResults(pageSize).getResultList();
    }

    @Override
    public List<Order> getUserOrders(Integer id) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        Predicate predicate = criteriaBuilder.equal(root.get("user").get("id"), id);
        criteriaQuery.select(root).where(predicate);
        Query query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }



}
