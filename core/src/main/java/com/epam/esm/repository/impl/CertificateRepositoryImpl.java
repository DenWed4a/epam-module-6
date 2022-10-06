package com.epam.esm.repository.impl;

import com.epam.esm.builder.CertificatePredicateBuilder;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.pagination.Pagination;
import com.epam.esm.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
@Repository
@Transactional
public class CertificateRepositoryImpl implements CertificateRepository {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CertificatePredicateBuilder certificatePredicateBuilder;


    @Override
    public Certificate save(Certificate certificate) {
        entityManager.persist(certificate);
        return certificate;
    }

    @Override
    public Certificate updateCertificate(Certificate certificate) {
        entityManager.merge(certificate);
        return certificate;
    }

    @Override
    public Optional<Certificate> getCertificate(int id) {
        Optional<Certificate> result;
        try{
            result = Optional.of(entityManager.find(Certificate.class, id));
        }catch (NullPointerException e){
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public List<Certificate> getCertificates(Map<String, String> params, String[] tags, Integer page, Integer pageSize) {


        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Certificate> criteriaQuery = criteriaBuilder.createQuery(Certificate.class);
        Root<Certificate> root = criteriaQuery.from(Certificate.class);
        Predicate[] predicates = certificatePredicateBuilder.getPredicate(criteriaBuilder, root, params, tags);
        criteriaQuery.select(root).where(predicates);
        int firstResult = Pagination.firstResult(page, pageSize);

        String sort = params.get("sort");
        if(sort!=null && sort.equals("asc")) {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get("name")));
        }else if(sort!=null && sort.equals("desc")) {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("name")));
        }
        TypedQuery<Certificate> query = entityManager.createQuery(criteriaQuery).setFirstResult(firstResult).setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public void deleteCertificate(Certificate certificate) {

        entityManager.remove(certificate);

    }

    @Override
    public Certificate addTagToGiftCertificate(Certificate certificate, Tag tag) {

        Set<Tag> tags = certificate.getTags();
        tags.add(tag);
        certificate.setTags(tags);
        return certificate;
    }

    @Override
    public void removeTagFromGiftCertificate(Certificate certificate, Tag tag) {
        Set<Tag> tags = certificate.getTags();
        tags.remove(tag);
        certificate.setTags(tags);

    }

}
