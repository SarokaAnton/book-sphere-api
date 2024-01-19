package com.projects.booksphere.genre.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GenreDTO(
        Long id,
        @Size(max = 45, message = "{name.size.max}")
        @NotBlank(message = "{name.notBlank}")
        String name) {}