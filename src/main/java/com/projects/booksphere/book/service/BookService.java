package com.projects.booksphere.book.service;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.projects.booksphere.book.model.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface BookService {
    BookDTO createBook(BookDTO bookDTO);

    BookDTO getBookById(Long bookId);

    Page<BookDTO> getAllBooks(Pageable pageable);

    Page<BookDTO> getBooksByGenres(Set<Long> genreIds, Pageable pageable);

    Page<BookDTO> getBooksByTags(Set<Long> tagIds, Pageable pageable);

    Page<BookDTO> getBooksByAuthor(Long authorId, Pageable pageable);

    BookDTO updateBook(Long bookId, JsonMergePatch patch);

    void deleteBookById(Long bookId);
}