package com.projects.booksphere.tag.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.projects.booksphere.tag.model.Tag;
import com.projects.booksphere.tag.model.TagDTO;
import com.projects.booksphere.tag.repository.TagRepository;
import com.projects.booksphere.utils.exceptionhandler.exceptions.ElementAlreadyExistsException;
import com.projects.booksphere.utils.exceptionhandler.exceptions.ElementNotFoundException;
import com.projects.booksphere.utils.exceptionhandler.exceptions.ElementUpdateException;
import com.projects.booksphere.utils.mappers.EntityToDtoMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.projects.booksphere.utils.exceptionhandler.ExceptionMessages.*;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Service
@RequiredArgsConstructor
public class TagServiceDefault implements TagService {
    private final EntityToDtoMapper entityToDtoMapper;
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public TagDTO createTag(TagDTO tagDTO) {
        Tag tag = entityToDtoMapper.toTag(tagDTO);

        if (isTagExist(tag)) {
            throw new ElementAlreadyExistsException(String.format(TAG_ALREADY_EXIST, tagDTO.name()));
        }

        return entityToDtoMapper.toTagDTO(tagRepository.save(tag));
    }

    @Override
    public TagDTO getTagById(Long tagId) {
        return tagRepository.findById(tagId)
                .map(entityToDtoMapper::toTagDTO)
                .orElseThrow(() -> new ElementNotFoundException(String.format(TAG_NOT_FOUND, tagId)));
    }

    @Override
    public List<TagDTO> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(entityToDtoMapper::toTagDTO)
                .toList();
    }

    @Override
    @Transactional
    public TagDTO updateTag(Long tagId, JsonMergePatch patch) {
        Tag tagDB = tagRepository.findById(tagId).orElseThrow(() ->
                new ElementNotFoundException(String.format(TAG_NOT_FOUND, tagId)));

        Tag tagUpdated = getUpdatedTag(patch, tagDB);

        if (isTagExist(tagUpdated)) {
            throw new ElementAlreadyExistsException(String.format(TAG_ALREADY_EXIST, tagUpdated.getName()));
        }

        tagDB.setName(tagUpdated.getName());

        return entityToDtoMapper.toTagDTO(tagRepository.save(tagDB));
    }

    private Tag getUpdatedTag(JsonMergePatch patch, Tag tagDB) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode updatedTagJSON = patch.apply(objectMapper.convertValue(tagDB, JsonNode.class));
            return objectMapper.treeToValue(updatedTagJSON, Tag.class);
        } catch (JsonPatchException | JsonProcessingException exception) {
            throw new ElementUpdateException(TAG_UPDATE_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public void deleteTagById(Long tagId) {
        tagRepository.findById(tagId).ifPresentOrElse(tagRepository::delete,
                () -> {
                    throw new ElementNotFoundException(String.format(TAG_NOT_FOUND, tagId));
                });
    }

    private boolean isTagExist(Tag tag) {
        ExampleMatcher tagMatcher = ExampleMatcher.matching()
                .withMatcher("name", exact().ignoreCase());
        return tagRepository.exists(Example.of(tag, tagMatcher));
    }
}