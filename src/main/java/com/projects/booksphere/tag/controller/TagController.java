package com.projects.booksphere.tag.controller;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.projects.booksphere.tag.model.TagDTO;
import com.projects.booksphere.tag.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tag")
public class TagController {
    private final TagService tagService;

    @PostMapping
    public ResponseEntity<TagDTO> createTag(@RequestBody @Valid TagDTO tagDTO) {
        return ResponseEntity.ok(tagService.createTag(tagDTO));
    }

    @GetMapping
    public ResponseEntity<List<TagDTO>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TagDTO> updateTag(@PathVariable Long id, @RequestBody JsonMergePatch patch) {
        return ResponseEntity.ok(tagService.updateTag(id, patch));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTagById(@PathVariable Long id) {
        tagService.deleteTagById(id);
        return ResponseEntity.noContent().build();
    }
}