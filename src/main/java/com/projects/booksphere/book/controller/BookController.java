package com.projects.booksphere.book.controller;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.projects.booksphere.book.model.BookDTO;
import com.projects.booksphere.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/books")
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody @Valid BookDTO bookDTO) {
        return ResponseEntity.ok(bookService.createBook(bookDTO));
    }

    @GetMapping
    public ResponseEntity<Page<BookDTO>> getAllBooks(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(bookService.getAllBooks(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping("/search/by-genres")
    public ResponseEntity<Page<BookDTO>> getBooksByGenres(@RequestParam Set<Long> genreIds, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(bookService.getBooksByGenres(genreIds, pageable));
    }

    @GetMapping("/search/by-tags")
    public ResponseEntity<Page<BookDTO>> getBooksByTags(@RequestParam Set<Long> tagIds, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(bookService.getBooksByTags(tagIds, pageable));
    }

    @GetMapping("/search/by-author/{authorId}")
    public ResponseEntity<Page<BookDTO>> getBooksByAuthor(@PathVariable Long authorId, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId, pageable));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody JsonMergePatch patch) {
        return ResponseEntity.ok(bookService.updateBook(id, patch));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }
}