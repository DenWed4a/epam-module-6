package com.epam.esm.service.impl;

import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceSourceNotFoundException;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.CertificateDtoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CertificateRepository.class)
class CertificateDtoServiceImplTest {
    private CertificateDtoService certificateDtoService;
    private final TagConverter tagConverter = Mockito.mock(TagConverter.class);
    private final CertificateConverter certificateConverter = Mockito.mock(CertificateConverter.class);
    private final TagRepository tagRepository = Mockito.mock(TagRepository.class);
    private final CertificateRepository certificateRepository = Mockito.mock(CertificateRepository.class);


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.certificateDtoService = new CertificateDtoServiceImpl(
                certificateRepository, tagRepository, tagConverter, certificateConverter);
    }

    @Test
    void Should_CallMethodSaveFromRepository_When_Creating() {
        Mockito.when(certificateRepository.save(Mockito.any()))
                .thenReturn(Certificate.builder().id(1).name("some").build());
        certificateDtoService.save(
                 CertificateDto.builder().id(1).name("some").build());
        verify(certificateRepository).save(Mockito.any());

    }

    @Test
    void Should_ThrowServiceSourceNotFoundException_When_Updating() throws ServiceSourceNotFoundException{
        Mockito.when(certificateRepository.getCertificate(1)).thenReturn(Optional.empty());
        try{
            certificateDtoService.updateCertificate(CertificateDto.builder().id(1).build());
        }catch (ServiceSourceNotFoundException e){
            assertEquals(1, e.getSourceId());
        }


    }

    @Test
    void Should_CallMethodGetCertificateFromRepository_When_GettingCertificate() {
        Certificate certificate = Certificate.builder().id(1).name("some").build();
        Mockito.when(certificateRepository.getCertificate(1))
                .thenReturn(Optional.of(certificate));
        Mockito.when(certificateConverter.convertToCertificateDto(new Certificate())).thenReturn(Mockito.any());
        certificateDtoService.getCertificate(1);
        Mockito.verify(certificateRepository).getCertificate(1);
    }

    @Test
    void Should_CallMethodGetCertificatesFromRepository_When_GettingCertificates() {
        Certificate certificate =
                Certificate.builder()
                        .id(1).name("some").description("someDescription")
                        .price(40.5).duration(15)
                        .createDate(LocalDateTime.of(2022, 3, 5, 10, 11, 12, 11))
                        .lastUpdateDate(LocalDateTime.of(2022, 3, 5, 10, 11, 12, 11))
                        .tags(Set.of(Tag.builder().name("tag").id(2).build())).build();

        CertificateDto certificateDto =
                CertificateDto.builder().id(1).name("some").description("someDescription")
                        .price(40.5).duration(15)
                        .createDate(LocalDateTime.of(2022, 3, 5, 10, 11, 12, 11))
                        .lastUpdateDate(LocalDateTime.of(2022, 3, 5, 10, 11, 12, 11))
                        .tags(Set.of(TagDto.builder().name("tag").id(2).build())).build();
        Mockito.when(certificateRepository.getCertificates(
                new HashMap<>(), new String[0],1,0)).thenReturn(List.of(certificate));
        Mockito.when(certificateConverter.convertToCertificateDtoList(List.of(certificate)))
                .thenReturn(List.of(certificateDto));
        List<CertificateDto> actual = certificateDtoService.getCertificates(
                new HashMap<>(), new String[0],1,0);
        List<CertificateDto> expected = List.of(certificateDto);
        assertEquals(expected, actual);
        Mockito.verify(certificateRepository).getCertificates(new HashMap<>(), new String[0],1,0);

    }

    @Test
    void deleteCertificate() {

        Mockito.when(certificateRepository.getCertificate(Mockito.anyInt()))
                        .thenReturn(Optional.of(Certificate.builder().build()));
        certificateDtoService.deleteCertificate(Mockito.anyInt());
        Mockito.verify(certificateRepository).deleteCertificate(Mockito.any());

    }

}