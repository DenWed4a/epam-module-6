package com.epam.esm.converter;


import com.epam.esm.dto.CertificateDto;
import com.epam.esm.entity.Certificate;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
/**
 * Class Certificate converter contains method for convert  Certificate to CertificateDto
 * and vise versa
 * @author Dzianis Savastsiuk
 */

@Component
@NoArgsConstructor
public class CertificateConverter {

    TagConverter tagConverter;
    @Autowired
    public CertificateConverter(TagConverter tagConverter){
        this.tagConverter = tagConverter;
    }

    /**
     * Converts CertificateDto to Certificate
     * @param certificateDto the CertificateDto
     * @return The Certificate
     */
    public Certificate convertToCertificate(CertificateDto certificateDto){
        return Certificate.builder()
                .id(certificateDto.getId())
                .name(certificateDto.getName())
                .description(certificateDto.getDescription())
                .duration(certificateDto.getDuration())
                .price(certificateDto.getPrice())
                .createDate(certificateDto.getCreateDate())
                .lastUpdateDate(certificateDto.getLastUpdateDate())
                .tags(tagConverter.convertToSetTags(certificateDto.getTags())).build();
    }

    /**
     * Converts Certificate to CertificateDto
     * @param certificate the Certificate
     * @return CertificateDto
     */
    public CertificateDto convertToCertificateDto(Certificate certificate){
        return CertificateDto.builder()
                .id(certificate.getId())
                .name(certificate.getName())
                .description(certificate.getDescription())
                .duration(certificate.getDuration())
                .price(certificate.getPrice())
                .createDate(certificate.getCreateDate())
                .lastUpdateDate(certificate.getLastUpdateDate())
                .tags(tagConverter.convertToSetTagDto(certificate.getTags())).build();
    }

    /**
     * Converts List of CertificatesDto to List of Certificates
     * @param certificateDtoList the List of CertificateDto
     * @return List of Certificates
     */
    public List<Certificate> convertToCertificateList(List<CertificateDto> certificateDtoList){
        List<Certificate> result = new ArrayList<>();
        certificateDtoList.stream().forEach(certificateDto -> result.add(convertToCertificate(certificateDto)));
        return result;
    }

    /**
     * Converts List of Certificates to List of CertificatesDto
     * @param certificates the List of Certificates
     * @return List of CertificatesDto
     */
    public List<CertificateDto> convertToCertificateDtoList(List<Certificate> certificates){
        List<CertificateDto> result = new ArrayList<>();
        certificates.stream().forEach(certificate -> result.add(convertToCertificateDto(certificate)));
        return result;
    }

}
