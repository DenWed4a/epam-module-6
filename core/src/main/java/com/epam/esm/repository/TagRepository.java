package com.epam.esm.repository;

import com.epam.esm.entity.Tag;

import java.util.Optional;
import java.util.Set;

/**
 * The interface Tag repository contain method for tags
 * @author Dzianis Savastsiuk
 */

public interface TagRepository {
    /**
     * Add tag to database without id.
     * @param tag the tag
     * @return added tag from db with id
     */
    Tag create(Tag tag);

    /**
     * get all tags with pagination
     * @param page the number of page
     * @param pageSize the number of page size
     * @return Set of tags
     */
    Set<Tag> getAll(Integer page, Integer pageSize);
    /**
     * get tag from data base by  id
     * @param id the id
     * @return Optional of tag
     */
    Optional<Tag> getById(Integer id);
    /**
     * get tag from data base by name
     * @param name the name
     * @return Optional of tag
     */
    Optional<Tag> getByName(String name);
    /**
     * delete tag from the data base
     * @param id the tag id
     */
    void delete(Integer id);

    /**
     * Get the most widely used tag of a user with the highest cost of all orders
     * @return the tag
     */
    Tag getWidelyUserTagWithHighestOrdersCost();
}
