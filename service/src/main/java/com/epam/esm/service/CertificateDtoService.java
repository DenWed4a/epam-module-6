package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;
/**
 * The interface CertificateDto service contains methods for business logic with certificates
 * and their tags.
 * @author Dzianis Savastsiuk
 */

public interface CertificateDtoService {
    /**
     * Adds certificate.
     * @param certificate the certificate dto
     * @return the certificate dto
     */
    CertificateDto save(CertificateDto certificate);
    /**
     * Updates certificate dto
     * @param certificate certificate dto
     * @return the certificate dto
     */
    CertificateDto updateCertificate(CertificateDto certificate, BindingResult bindingResult);

    CertificateDto updateCertificate(CertificateDto certificate);

    /**
     * Gets certificate by id
     * @param id the id
     * @return the certificate dto
     */
    CertificateDto getCertificate(Integer id);

    /**
     * Gets certificates with pagination and by parameters
     * @param params parameters like sort and search
     * @param tags certificate tags
     * @param page page number
     * @param pageSize page size
     * @return List of CertificateDto
     */
    List<CertificateDto> getCertificates(Map<String, String> params, String[] tags, Integer page, Integer pageSize);

    /**
     * Deletes certificate by id
     * @param id the id
     */
    void deleteCertificate(Integer id);

    /**
     * Adds the Tag to Certificate
     * @param id the certificate id
     * @param tag the TagDto
     * @return CertificateDto
     */
    CertificateDto addTagToGiftCertificate(Integer id, TagDto tag);

    /**
     * Removes Tag from certificate
     * @param certificateId the certificate id
     * @param tagId the tag id
     */
    void removeTagFromGiftCertificate(Integer certificateId, Integer tagId);

}
