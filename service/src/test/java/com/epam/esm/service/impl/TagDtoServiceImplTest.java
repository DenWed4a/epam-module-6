package com.epam.esm.service.impl;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagDtoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TagRepository.class)
class TagDtoServiceImplTest {


    private final TagRepository tagRepository = Mockito.mock(TagRepository.class);
    private TagConverter tagConverter;
    private TagDtoService tagDtoService;


    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        this.tagConverter = new TagConverter();
        this.tagDtoService = new TagDtoServiceImpl(tagRepository, tagConverter);

    }


    @Test
    void Should_CallMethodGetNameFromRepository_When_Creating() {
        Mockito.when(tagRepository.create(Mockito.any(Tag.class)))
                .thenReturn(Tag.builder().id(1).name("tag").build());
        Mockito.when(tagRepository.getByName(Mockito.anyString())).thenReturn(Optional.empty());
        TagDto tagDto = new TagDto();
        tagDto.setId(1);
        tagDto.setName("tag");
        tagDtoService.create(tagDto);
        Mockito.verify(tagRepository, Mockito.times(1))
                .getByName(Mockito.anyString());
        Mockito.verify(tagRepository).create(Mockito.any(Tag.class));

    }

    @Test
    void Should_CallMethodGetTagsFromDao_When_GettingAllTags() {
        Mockito.when(tagRepository.getAll(1, 10))
                .thenReturn(Set.of(Tag.builder().id(1).name("first").build(),
                         Tag.builder().id(1).name("second").build()));
        tagDtoService.getAll(1,10);
        Mockito.verify(tagRepository).getAll(1,10);
    }

    @Test
    void Should_CallMethodGetTagFromDao_When_GettingTagById() {
        Mockito.when(tagRepository.getById(Mockito.anyInt()))
                .thenReturn(Optional.of(Tag.builder().id(1).name("first").build()));
        Tag actual = tagConverter.convertToTag(tagDtoService.getById(1));
        assertNotNull(actual);
        Mockito.verify(tagRepository).getById(Mockito.anyInt());
    }

    @Test
    void Should_CallMethodDeleteFromRepository_When_Deleting() {
        tagDtoService.delete(Mockito.anyInt());
        Mockito.verify(tagRepository).delete(Mockito.anyInt());

    }

    @Test
    void Should_CallMethodGetWidelyUserTag_When_CallingMethod() {
        Mockito.when(tagRepository.getWidelyUserTagWithHighestOrdersCost())
                .thenReturn(Tag.builder().id(1).name("tag").build());
        tagDtoService.getWidelyUserTagWithHighestOrdersCost();
        Mockito.verify(tagRepository).getWidelyUserTagWithHighestOrdersCost();

    }
}