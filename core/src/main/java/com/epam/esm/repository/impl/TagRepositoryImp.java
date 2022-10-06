package com.epam.esm.repository.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.repository.TagRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@NoArgsConstructor
@Repository
@Transactional
public class TagRepositoryImp implements TagRepository {
    private final static String QUERY =
            "select count(tag.id) as count, tag.id, tag.name, tag.operation, tag.timestamp " +
            "from tag " +
            "join gift_certificates_has_tag on tag.id = gift_certificates_has_tag.tag_id " +
            "join orders on orders.certificate_id = gift_certificates_has_tag.gift_certificates_id " +
            "join users on orders.users_id = users.id " +
            "where users.id = " +
                "(Select users.id from users " +
                "join orders  on users.id = orders.users_id " +
                    "group by users_id having sum(total) = " +
                        "(Select  max(totally_price) as max_total " +
                        "from (select users_id, sum(total) as totally_price " +
                                "from orders group by users_id) as t ))" +
            " group by tag.id order by count desc";

    @Autowired
    private EntityManager entityManager;

    @Override

    public Tag create(Tag tag) {
        entityManager.persist(tag);
        return tag;
    }
    @Override
    public Set<Tag> getAll(Integer page, Integer pageSize) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> tagRoot = criteriaQuery.from(Tag.class);
        criteriaQuery.select(tagRoot);
        int firstResult = Pagination.firstResult(page, pageSize);
        Set<Tag> result = new HashSet<>(
                entityManager.createQuery(criteriaQuery).setFirstResult(firstResult).setMaxResults(pageSize).getResultList());
        entityManager.close();
        return result;
    }

    @Override
    public Optional<Tag> getById(Integer id) {
        Optional<Tag> result;
        try{
            result = Optional.of(entityManager.find(Tag.class, id));
        }catch (NullPointerException e){
            result = Optional.empty();
        }
       return result;
    }

    @Override
    public Optional<Tag> getByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> tagRoot = criteriaQuery.from(Tag.class);
        criteriaQuery.select(tagRoot).where(criteriaBuilder.like(tagRoot.get("name"), name));
        Optional<Tag> result;
        try{
            result = Optional.of(entityManager.createQuery(criteriaQuery).getSingleResult());
        }catch (NoResultException e){
            result = Optional.empty();
        }
        return result;
    }


    @Override
    public void delete(Integer id) {

        Tag tag = entityManager.find(Tag.class, id);
        entityManager.remove(tag);

    }
    @Override
    public Tag getWidelyUserTagWithHighestOrdersCost(){
        Query query = entityManager.createNativeQuery(QUERY, Tag.class);
        return (Tag) query.getResultList().get(0);
    }
}
