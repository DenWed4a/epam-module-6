package com.epam.esm.util;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.dto.CertificateDto;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Class Certificate link builder
 * @author Dzianis Savastsiuk
 */
@Component
public class CertificateLinkBuilder {
    /**
     * Create self link for CertificateDto
     * @param certificateDto the CertificateDto
     * @return Link
     */
    public Link createCertificateSelfLink(CertificateDto certificateDto){
        return linkTo(CertificateController.class).slash(certificateDto.getId()).withSelfRel();
    }

    /**
     * Create self link for list of CertificateDto
     * @param searchWord for search certificate by word or part of word
     * @param sort sorting parameter, can be asc or desc
     * @param tags certificates tags, for finding certificate by tags
     * @param page page number
     * @param pageSize page size
     * @return Link
     */
    public Link createCertificateListSelfLink(String searchWord, String sort, String[] tags,  Integer page, Integer pageSize){
        return linkTo(methodOn(CertificateController.class)
                .getCertificates(searchWord, sort, tags, page, pageSize)).withSelfRel();
    }
}
