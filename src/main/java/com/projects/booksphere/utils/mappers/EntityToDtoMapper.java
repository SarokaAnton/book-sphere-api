package com.projects.booksphere.utils.mappers;

import com.projects.booksphere.genre.model.Genre;
import com.projects.booksphere.genre.model.GenreDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityToDtoMapper {
    GenreDTO toGenreDTO(Genre genre);

    Genre toGenre(GenreDTO genreDTO);
}