package com.projects.booksphere.book.service;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.projects.booksphere.book.model.BookDTO;

import java.util.List;
import java.util.Set;

public interface BookService {
    BookDTO createBook(BookDTO bookDTO);

    BookDTO getBookById(Long bookId);

    List<BookDTO> getAllBooks();

    List<BookDTO> getBooksByGenres(Set<Long> genreIds);

    List<BookDTO> getBooksByTags(Set<Long> tagIds);

    List<BookDTO> getBooksByAuthor(Long authorId);

    BookDTO updateBook(Long bookId, JsonMergePatch patch);

    void deleteBookById(Long bookId);
}