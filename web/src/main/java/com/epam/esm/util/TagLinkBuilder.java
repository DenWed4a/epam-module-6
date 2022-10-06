package com.epam.esm.util;

import com.epam.esm.controller.TagController;
import com.epam.esm.dto.TagDto;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Class Tag link builder
 * @author Dzianis Savastsuik
 */
@Component
public class TagLinkBuilder {
    /**
     * Creates self link for TagDto
     * @param tag the TagDto
     * @return Link
     */
    public Link createTagSelfLink(TagDto tag){
        return linkTo(methodOn(TagController.class).getTagById(tag.getId())).withSelfRel();
    }

    /**
     * Creates self link for list of TagDto
     * @param page the page number
     * @param pageSize the page size
     * @return Link
     */
    public Link createTagListSelfLink(Integer page, Integer pageSize){
        return linkTo(methodOn(TagController.class).getAll(page, pageSize)).withSelfRel();
    }
}
