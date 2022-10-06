package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.TagDtoService;
import com.epam.esm.util.TagLinkBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Set;

/**
 * The type Tag controller
 * @author Dzianis Savastsiuk
 */
@Validated
@RestController
@RequestMapping("/rest/tags")
public class TagController {
    @Autowired
    TagDtoService tagDtoService;
    @Autowired
    TagLinkBuilder tagLinkBuilder;

    /**
     * Gets collection of TagDto with pagination
     * @param page the page number
     * @param pageSize the page size
     * @return CollectionModel of TagDto
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public CollectionModel<TagDto> getAll(@RequestParam(value = "page", defaultValue = "1", required = false)
                                              @Min(value = 1, message = "local.positive.field") Integer page,
                                          @RequestParam(value = "pageSize", defaultValue = "5", required = false)
                                          @Min(value = 1, message = "local.positive.field")Integer pageSize){
        Set<TagDto> tags = tagDtoService.getAll(page, pageSize);
        tags.forEach(tagDto ->  tagDto.add(tagLinkBuilder.createTagSelfLink(tagDto)));
        Link selfLink = tagLinkBuilder.createTagListSelfLink(page, pageSize);

        return CollectionModel.of(tags, selfLink);
    }

    /**
     * Gets TagDto by id
     * @param id the id
     * @return TagDto
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public TagDto getTagById(@PathVariable("id") @Min(value = 1, message = "local.positive.field") Integer id){

        TagDto tagDto = tagDtoService.getById(id);
        tagDto.add(tagLinkBuilder.createTagSelfLink(tagDto));

        return tagDto;
    }

    /**
     * Deletes TagDto by id
     * @param id the id
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @Min(value = 1, message = "local.positive.field") Integer id){
        tagDtoService.delete(id);
    }

    /**
     * Creates TagDto
     * @param tagDto the TagDto
     * @return ResponseEntity of TagDto
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<TagDto> create(@Valid @RequestBody  TagDto tagDto){
        return new ResponseEntity<>(tagDtoService.create(tagDto), HttpStatus.CREATED);
    }

    /**
     * Gets the most widely used tag of a user with the highest cost of all orders
     * @return TagDto
     */
    @GetMapping("/most_popular")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public TagDto getMostPopular(){
        return tagDtoService.getWidelyUserTagWithHighestOrdersCost();
    }


}
