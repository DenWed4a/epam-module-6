package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.CertificateDtoService;
import com.epam.esm.util.CertificateLinkBuilder;
import com.epam.esm.util.TagLinkBuilder;
import com.epam.esm.validation.UpdatedCertificateDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * The type Controller is a rest controller which operates requests from clients and
 * generates response in representational forms. Information exchanging are in a JSON
 * forms.
 * @author Dzianis Savastsiuk
 */
@RestController
@RequestMapping("/rest/certificates")
@Validated
public class CertificateController {
    @Autowired
    private CertificateDtoService certificateDtoService;
    @Autowired
    private CertificateLinkBuilder certificateLinkBuilder;
    @Autowired
    private TagLinkBuilder tagLinkBuilder;
    @Autowired
    private UpdatedCertificateDtoValidator updatedCertificateDtoValidator;


    /**
     * Gets collection of certificateDto
     * @param searchWord for search certificate by word or part of word
     * @param sort sorting parameter, can be asc or desc
     * @param tags certificates tags, for finding certificate by tags
     * @param page page number
     * @param pageSize page size
     * @return CollectionModel of CertificateDto
     */
    @GetMapping
    public CollectionModel<CertificateDto> getCertificates(
            @RequestParam(value = "search", defaultValue = "",required = false) String searchWord,
            @RequestParam(value = "sort", defaultValue = "asc", required = false) @Pattern(regexp = "(\\basc\\b)?(\\bdesc\\b)?") String sort,
            @RequestParam(value = "tag", defaultValue = "", required = false) String[] tags,
            @RequestParam(value = "page", defaultValue = "1", required = false) @Min(value = 1, message = "local.positive.field") Integer page,
            @RequestParam(value = "pageSize", defaultValue = "5", required = false) @Min(value = 1, message = "local.positive.field") Integer pageSize){

        Map<String, String> params = new HashMap<>();
        params.put("search", searchWord);
        params.put("sort", sort);

        List<CertificateDto> certificates = certificateDtoService.getCertificates(params, tags,
                page, pageSize);

        certificates.forEach(certificateDto -> {
                    certificateDto.add(certificateLinkBuilder.createCertificateSelfLink(certificateDto));
                    certificateDto.getTags().forEach(
                            tagDto -> tagDto.add(tagLinkBuilder.createTagSelfLink(tagDto)));
                });

        Link selfLink = certificateLinkBuilder.createCertificateListSelfLink(searchWord, sort, tags, page, pageSize);

        return CollectionModel.of(certificates, selfLink);
    }

    /**
     * Gets certificate by id
     * @param id the id
     * @return CertificateDto
     */
    @GetMapping("/{id}")
    public CertificateDto getCertificateById(@PathVariable @Min(value = 1, message = "local.positive.field") Integer id){
        CertificateDto certificate = certificateDtoService.getCertificate(id);
        certificate.add(certificateLinkBuilder.createCertificateSelfLink(certificate));
        return certificate;
    }

    /**
     * Creates certificateDto
     * @param certificateDto the CertificateDto
     * @return ResponseEntity of CertificateDto
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<CertificateDto> create(@Valid @RequestBody CertificateDto certificateDto){
        return new ResponseEntity<>(certificateDtoService.save(certificateDto), HttpStatus.CREATED);
    }

    /**
     * Updates CertificateDto
     * @param id the id
     * @param certificateDto to the CertificateDto
     * @return ResponseEntity of CertificateDto
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<CertificateDto> update(@PathVariable @Min(value = 1, message = "local.positive.field") Integer id,
                                                 @RequestBody CertificateDto certificateDto,
                                                 BindingResult bindingResult){
        certificateDto.setId(id);
        return new ResponseEntity<>(
                certificateDtoService.updateCertificate(certificateDto, bindingResult), HttpStatus.OK);
    }

    /**
     * Updates only price in CertificateDto
     * @param id the id
     * @param price the price
     * @return ResponseEntity of CertificateDto
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<CertificateDto> updatePrice(@PathVariable @Min(value = 1, message = "local.positive.field") Integer id,
                                                      @RequestBody Double price){
        CertificateDto certificateDto = certificateDtoService.getCertificate(id);
        certificateDto.setPrice(price);
        certificateDtoService.updateCertificate(certificateDto);
        return new ResponseEntity<>(certificateDtoService.updateCertificate(certificateDto), HttpStatus.OK);
    }

    /**
     * Adds TagDto to CertificateDto
     * @param id the certificate id
     * @param tagDto the TagDto
     * @return ResponseEntity of CertificateDto
     */
    @PutMapping("/{id}/tags")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<CertificateDto> addTagToCertificate(@PathVariable @Min(value = 1, message = "local.positive.field") Integer id,
                                                              @Valid @RequestBody TagDto tagDto){
        return new ResponseEntity<>(certificateDtoService.addTagToGiftCertificate(id,tagDto), HttpStatus.OK);
    }

    /**
     * Deletes TagDto from CertificateDto
     * @param id the certificate id
     * @param tagId the tag id
     */
    @DeleteMapping("/{id}/tags/{tagId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deleteTagFromCertificate(@PathVariable @Min(value = 1, message = "local.positive.field") Integer id,
                                         @PathVariable @Min(value = 1, message = "local.positive.field") Integer tagId){
        certificateDtoService.removeTagFromGiftCertificate(id, tagId);
    }

    /**
     * Deletes CertificateDto by id
     * @param id the id
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCertificate(@PathVariable  @Min(value = 1, message = "local.positive.field") Integer id){
        certificateDtoService.deleteCertificate(id);
    }


}
