package com.epam.esm.repository.impl;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    EntityManager entityManager;

    @Override
    public List<Order> getOrders(Integer page, Integer pageSize) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root);
        int firstResult = Pagination.firstResult(page, pageSize);

        return entityManager.createQuery(criteriaQuery).setFirstResult(firstResult).setMaxResults(pageSize).getResultList();
    }

    @Override
    public Order create(Order order, Integer userId) {

        User user = entityManager.find(User.class, userId);
        order.setUser(user);
        entityManager.persist(order);
        return order;
    }

    @Override
    public Optional<Order> getOrderById(Integer id) {
        Optional<Order> result;
        try{
            result = Optional.of(entityManager.find(Order.class, id));
        }catch (NullPointerException e){
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public void deleteOrder(Integer id) {

        Order order = entityManager.find(Order.class, id);
        entityManager.remove(order);

    }
}
