package com.projects.booksphere.author.service;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.projects.booksphere.author.model.AuthorDTO;

import java.util.List;

public interface AuthorService {
    AuthorDTO createAuthor(AuthorDTO authorDTO);

    AuthorDTO getAuthorById(Long authorId);

    List<AuthorDTO> getAllAuthors();

    AuthorDTO updateAuthor(Long authorId, JsonMergePatch patch);

    void deleteAuthorById(Long authorId);
}