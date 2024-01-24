package com.projects.booksphere.author.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthorDTO(
        Long id,
        @Size(max = 45, message = "{firstname.size.max}")
        @NotBlank(message = "{firstname.notBlank}")
        String firstName,
        @Size(max = 45, message = "{secondname.size.max}")
        @NotBlank(message = "{secondname.notBlank}")
        String secondName,
        @Size(max = 2000, message = "{biography.size.max}")
        @NotBlank(message = "{biography.notBlank}")
        String biography) {
}