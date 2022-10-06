package com.epam.esm.repository;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;

import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * The Interface certificate repository contains methods for certificates
 *
 * @author Dzianis Savastsiuk
 */
public interface CertificateRepository {
    /**
     * Adds new certificate to a data base
     * @param certificate the certificate
     * @return added certificate from the database
     */
    Certificate save(Certificate certificate);
    /**
     * Updates certificates in the data vase
     * @param certificate the certificate
     * @return GiftCertificate
     */
    Certificate updateCertificate(Certificate certificate);
    /**
     * Gets certificate by the id
     * @param id the id
     * @return Optional of certificate
     */
    Optional<Certificate> getCertificate(int id);

    /**
     * Gets certificates with pagination, sort and searching by tags
     * @param params the parameters like sort and search
     * @param tags the tags
     * @param page page number
     * @param pageSize size of page
     * @return List of certificates
     */
    List<Certificate> getCertificates(Map<String, String> params, String[] tags, Integer page, Integer pageSize);

    /**
     * Deletes certificate
     * @param certificate the certificate
     */
    void deleteCertificate(Certificate certificate);

    /**
     * Adding the tag to certificate
     * @param certificate the certificate
     * @param tag the tag
     * @return The certificate
     */
    Certificate addTagToGiftCertificate(Certificate certificate, Tag tag);

    /**
     * Removes the tag from certificate
     * @param certificate the certificate
     * @param tag the tag
     */
    void removeTagFromGiftCertificate(Certificate certificate, Tag tag);
}
