package com.epam.esm.converter;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Class Tag converter contains methods for converting tag to tag dto and vise versa.
 * @author Dzianis Savastsiuk
 */
@Component
public class TagConverter {
    /**
     * Converts TagDto to Tag
     * @param tagDto the TagDto
     * @return Tag
     */
    public Tag convertToTag(TagDto tagDto){
        return Tag.builder().id(tagDto.getId()).name(tagDto.getName()).build();
    }

    /**
     * Converts Tag to TagDto
     * @param tag the Tag
     * @return TagDto
     */
    public TagDto convertToTagDto(Tag tag){
        return TagDto.builder().id(tag.getId()).name(tag.getName()).build();
    }

    /**
     * Converts Set of TagDto to Set of Tags
     * @param tagDtoSet the Set of TagDto
     * @return Set of Tags
     */
    public Set<Tag> convertToSetTags(Set<TagDto> tagDtoSet){
        Set<Tag> tags = new HashSet<>();
        if(tagDtoSet != null) {
            tagDtoSet.stream().forEach(tagDto -> tags.add(convertToTag(tagDto)));
        }
        return tags;
    }

    /**
     * Converts Set of Tags to Set of TagDto
     * @param tagSet Set of Tags
     * @return Set of TagDto
     */
    public Set<TagDto> convertToSetTagDto(Set<Tag> tagSet){
        Set<TagDto> tagDtoSet = new HashSet<>();
        tagSet.stream().forEach(tag -> tagDtoSet.add(convertToTagDto(tag)));
        return tagDtoSet;
    }
}
