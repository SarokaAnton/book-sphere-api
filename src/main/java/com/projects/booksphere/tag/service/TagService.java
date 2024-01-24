package com.projects.booksphere.tag.service;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.projects.booksphere.tag.model.TagDTO;

import java.util.List;

public interface TagService {
    TagDTO createTag(TagDTO tagDTO);

    TagDTO getTagById(Long tagId);

    List<TagDTO> getAllTags();

    TagDTO updateTag(Long tagId, JsonMergePatch patch);

    void deleteTagById(Long tagId);
}