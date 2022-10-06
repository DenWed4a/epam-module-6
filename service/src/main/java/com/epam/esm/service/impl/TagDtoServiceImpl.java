package com.epam.esm.service.impl;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ServiceSourceNotFoundException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagDtoService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@NoArgsConstructor
public class TagDtoServiceImpl implements TagDtoService {

    private TagRepository tagRepository;
    private TagConverter tagConverter;
    @Autowired
    public TagDtoServiceImpl(TagRepository tagRepository, TagConverter tagConverter){
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    @Override
    public TagDto create(TagDto tag) {
        Tag result;
            if(tagRepository.getByName(tag.getName()).isEmpty()) {
                   result = tagRepository.create(tagConverter.convertToTag(tag));
            }else{
                result = tagRepository.getByName(tag.getName()).get();
            }
        return tagConverter.convertToTagDto(result);
    }

    @Override
    public Set<TagDto> getAll(Integer page, Integer pageSize) {
        return tagConverter.convertToSetTagDto(tagRepository.getAll(page, pageSize));
    }

    @Override
    public TagDto getById(Integer id) {
        return tagConverter.convertToTagDto(tagRepository.getById(id)
                        .orElseThrow(() -> new ServiceSourceNotFoundException(id)));
    }

    @Override
    public void delete(Integer id) {
        tagRepository.delete(id);
    }

    @Override
    public TagDto getWidelyUserTagWithHighestOrdersCost() {
        return tagConverter.convertToTagDto(tagRepository.getWidelyUserTagWithHighestOrdersCost());
    }


}
