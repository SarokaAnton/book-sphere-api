package com.projects.booksphere.book.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projects.booksphere.author.model.AuthorDTO;
import com.projects.booksphere.genre.model.GenreDTO;
import com.projects.booksphere.tag.model.TagDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

public record BookDTO(
        Long id,
        @Size(max = 45, message = "{title.size.max}")
        @NotBlank(message = "{title.notBlank}")
        String title,
        @Size(max = 100, message = "{slogan.size.max}")
        @NotBlank(message = "{slogan.notBlank}")
        String slogan,
        @Size(max = 1000, message = "{description.size.max}")
        @NotBlank(message = "{description.notBlank}")
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull(message = "{publishdate.notNull}")
        LocalDate publishDate,
        @NotEmpty(message = "{genres.notEmpty}")
        Set<GenreDTO> genres,
        @NotEmpty(message = "{tags.notEmpty}")
        Set<TagDTO> tags,
        @NotEmpty(message = "{authors.notEmpty}")
        Set<AuthorDTO> authors) {
}