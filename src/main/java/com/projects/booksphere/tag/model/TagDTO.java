package com.projects.booksphere.tag.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TagDTO(
        Long id,
        @Size(max = 45, message = "{tag.name.size.max}")
        @NotBlank(message = "{tag.name.notBlank}")
        String name) {
}