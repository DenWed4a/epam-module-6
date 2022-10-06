package com.epam.esm.service.impl;

import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CustomNotValidArgumentException;
import com.epam.esm.exception.ServiceBlankFieldException;
import com.epam.esm.exception.ServiceSourceNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.CertificateDtoService;
import com.epam.esm.validation.UpdatedCertificateDtoValidator;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
@NoArgsConstructor
@Service

public class CertificateDtoServiceImpl implements CertificateDtoService {

    private CertificateRepository certificateRepository;
    private TagRepository tagRepository;
    private TagConverter tagConverter;
    private CertificateConverter certificateConverter;
    @Autowired
    UpdatedCertificateDtoValidator updatedCertificateDtoValidator;

    @Autowired
    public CertificateDtoServiceImpl(CertificateRepository certificateRepository, TagRepository tagRepository,
                                     TagConverter tagConverter, CertificateConverter certificateConverter){
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
        this.certificateConverter = certificateConverter;

    }

    @Override
    public CertificateDto save(CertificateDto certificate) {
        certificate.setCreateDate(LocalDateTime.now());
        certificate.setLastUpdateDate(LocalDateTime.now());
        Set<TagDto> tags = certificate.getTags();
        if(tags != null) {
            tags.forEach( tagDto -> {
                if(tagDto.getName().isBlank()){
                    throw new ServiceBlankFieldException("name");
                }
                Tag tag;
                if (tagRepository.getByName(tagDto.getName()).isEmpty()) {
                    tag = tagRepository.create(tagConverter.convertToTag(tagDto));
                } else {
                    tag = tagRepository.getByName(tagDto.getName()).get();
                }
                tagDto.setId(tag.getId());
            });
        }
        return certificateConverter.convertToCertificateDto(
                certificateRepository.save(
                        certificateConverter.convertToCertificate(certificate)));
    }

    @Override
    public CertificateDto updateCertificate(CertificateDto certificate, BindingResult bindingResult) {
        Certificate certificateFromDb = certificateRepository.getCertificate(certificate.getId())
                .orElseThrow(() -> new ServiceSourceNotFoundException(certificate.getId()));

        Certificate result = Certificate.builder()
                .id(certificate.getId())
                .name(certificate.getName() != null ? certificate.getName() : certificateFromDb.getName())
                .price(certificate.getPrice() != null ? certificate.getPrice() : certificateFromDb.getPrice())
                .description(certificate.getDescription() != null ? certificate.getDescription() : certificateFromDb.getDescription())
                .duration(certificate.getDuration() != null ? certificate.getDuration() : certificateFromDb.getDuration())
                .createDate(certificateFromDb.getCreateDate())
                .lastUpdateDate(LocalDateTime.now())
                .tags(certificateFromDb.getTags()).build();

        updatedCertificateDtoValidator.validate(
                certificateConverter.convertToCertificateDto(result), bindingResult);
        if(bindingResult.hasErrors()){
            throw new CustomNotValidArgumentException(bindingResult);
        }
        return certificateConverter.convertToCertificateDto(certificateRepository.updateCertificate(result));
    }

    @Override
    public CertificateDto updateCertificate(CertificateDto certificate) {
        Certificate certificateFromDb = certificateRepository.getCertificate(certificate.getId())
                .orElseThrow(() -> new ServiceSourceNotFoundException(certificate.getId()));
        Certificate result = Certificate.builder()
                .id(certificate.getId())
                .name(certificate.getName() != null ? certificate.getName() : certificateFromDb.getName())
                .price(certificate.getPrice() != null ? certificate.getPrice() : certificateFromDb.getPrice())
                .description(certificate.getDescription() != null ? certificate.getDescription() : certificateFromDb.getDescription())
                .duration(certificate.getDuration() != null ? certificate.getDuration() : certificateFromDb.getDuration())
                .createDate(certificateFromDb.getCreateDate())
                .lastUpdateDate(LocalDateTime.now())
                .tags(certificateFromDb.getTags()).build();
        return certificateConverter.convertToCertificateDto(certificateRepository.updateCertificate(result));
    }

    @Override
    public CertificateDto getCertificate(Integer id) {
        return  certificateConverter.convertToCertificateDto(
                certificateRepository.getCertificate(id)
                        .orElseThrow(() -> new ServiceSourceNotFoundException(id)));

    }

    @Override
    public List<CertificateDto> getCertificates(Map<String, String> params, String[] tags, Integer page, Integer pageSize) {
        return certificateConverter.convertToCertificateDtoList(certificateRepository.getCertificates(params, tags, page, pageSize));
    }

    @Override
    public void deleteCertificate(Integer id) {
        Certificate certificate = certificateRepository.getCertificate(id)
                .orElseThrow(()->new ServiceSourceNotFoundException(id));
        certificateRepository.deleteCertificate(certificate);

    }

    @Override
    public CertificateDto addTagToGiftCertificate(Integer id, TagDto tagDto) {
        Tag tag;
        if(tagRepository.getByName(tagDto.getName()).isEmpty()){
             tag = tagRepository.create(tagConverter.convertToTag(tagDto));
        }else{
             tag = tagRepository.getByName(tagDto.getName()).get();
        }
        Certificate certificate = certificateRepository.getCertificate(id).orElseThrow(() -> new ServiceSourceNotFoundException(id));
        return certificateConverter.convertToCertificateDto(certificateRepository.addTagToGiftCertificate(certificate, tag));


    }

    @Override
    public void removeTagFromGiftCertificate(Integer certificateId, Integer tagId) {
        Certificate certificate = certificateRepository.getCertificate(certificateId)
                .orElseThrow(() -> new ServiceSourceNotFoundException(certificateId));
        Set<Tag> tags = certificate.getTags();
        Tag tag = tags.stream().filter(
                tag1 -> Objects.equals(tag1.getId(), tagId))
                .findFirst().orElseThrow(() -> new ServiceSourceNotFoundException(tagId));
        certificateRepository.removeTagFromGiftCertificate(certificate, tag);
    }

}
