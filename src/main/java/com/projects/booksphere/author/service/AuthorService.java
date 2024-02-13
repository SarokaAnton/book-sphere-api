package com.projects.booksphere.author.service;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.projects.booksphere.author.model.AuthorDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService {
    AuthorDTO createAuthor(AuthorDTO authorDTO);

    AuthorDTO getAuthorById(Long authorId);

    Page<AuthorDTO> getAllAuthors(Pageable pageable);

    AuthorDTO updateAuthor(Long authorId, JsonMergePatch patch);

    void deleteAuthorById(Long authorId);
}