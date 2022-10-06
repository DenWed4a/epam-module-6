package com.epam.esm.repository.impl;

import com.epam.esm.entity.Role;
import com.epam.esm.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;
@Repository
public class RoleRepositoryImpl implements RoleRepository {
    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Role> getRoleById(Integer id) {
        Optional<Role> result;
        try{
            result = Optional.of(entityManager.find(Role.class, id));
        }catch (NoResultException e){
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public Optional<Role> getRoleByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Role> criteriaQuery = criteriaBuilder.createQuery(Role.class);
        Root<Role> root = criteriaQuery.from(Role.class);
        criteriaQuery.select(root).where(criteriaBuilder.like(root.get("name"), name));

        Optional<Role> result;
        try{
            result = Optional.of(entityManager.createQuery(criteriaQuery).getSingleResult());
        }catch (NoResultException e){
            result = Optional.empty();
        }
        return result;
    }
}
