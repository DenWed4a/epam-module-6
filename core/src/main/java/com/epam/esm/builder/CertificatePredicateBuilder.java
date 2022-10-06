package com.epam.esm.builder;

import com.epam.esm.entity.Certificate;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** Class CertificatePredicateBuilder builds predicates for criteria api
 * @author Dzianis Savastsiuk
 */
@Component
public class CertificatePredicateBuilder {
    private final static String NAME = "name";
    private final static String SEARCH = "search";
    private final static String DESCRIPTION = "description";
    private final static String TAGS = "tags";
    private final static String PERCENT_SIGN = "%";

    /**
     * Gets array of predicates by parameters and tags
     * @param criteriaBuilder the CriteriaBuilder from Criteria api
     * @param root the Root from Criteria api
     * @param params parameters like sort and search
     * @param tags the tags of certificate
     * @return Array of predicates
     */
    public Predicate[] getPredicate(CriteriaBuilder criteriaBuilder,
                                    Root<Certificate> root,
                                    Map<String, String> params, String[] tags){

        List<Predicate> predicates = new ArrayList<>();

        String searchParam = params.get(SEARCH);
        if(searchParam!=null) {
            Predicate predicateForFindByDescription =
                    criteriaBuilder.like(root.get(NAME), PERCENT_SIGN + searchParam + PERCENT_SIGN);

            Predicate predicateForFindByName =
                    criteriaBuilder.like(root.get(DESCRIPTION), PERCENT_SIGN + searchParam + PERCENT_SIGN);

            Predicate predicateForFindByWord =
                    criteriaBuilder.or(predicateForFindByName, predicateForFindByDescription);

            predicates.add(predicateForFindByWord);
        }

        if(tags != null) {
            Arrays.stream(tags).forEach(tag -> {
                Predicate predicate = criteriaBuilder.equal(root.join(TAGS).get(NAME), tag);

                predicates.add(predicate);
            });
        }
        return predicates.toArray(Predicate[]::new);
    }


}
