package com.projects.booksphere.genre.service;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.projects.booksphere.genre.model.GenreDTO;

import java.util.List;

public interface GenreService {
    GenreDTO createGenre(GenreDTO genreDTO);
    GenreDTO getGenreById(Long genreId);
    List<GenreDTO> getAllGenres();
    GenreDTO updateGenre(Long genreId, JsonMergePatch patch);
    void deleteGenreById(Long genreId);
}