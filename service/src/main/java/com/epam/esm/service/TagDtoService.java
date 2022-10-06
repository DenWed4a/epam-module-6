package com.epam.esm.service;

import com.epam.esm.dto.TagDto;

import java.util.Set;

/**
 * The interface TagDto service contains methods for business logic with tags.
 * @author Dzianis Savastsiuk
 */
public interface TagDtoService {
    /**
     * Saves tag
     * @param tag the TagDto
     * @return TagDto
     */
    TagDto create(TagDto tag);

    /**
     * Gets Set of TagDto with pagination
     * @param page the page number
     * @param pageSize the page size
     * @return Set of TagDto
     */
    Set<TagDto> getAll(Integer page, Integer pageSize);

    /**
     * Gets TagDto by id
     * @param id the id
     * @return TagDto
     */
    TagDto getById(Integer id);

    /**
     * Deletes TagDto by id
     * @param id the id
     */
    void delete(Integer id);

    /**
     * Get the most widely used tag of a user with the highest cost of all orders
     * @return TagDto
     */
    TagDto getWidelyUserTagWithHighestOrdersCost();

}
