package com.projects.booksphere.book.repository;

import com.projects.booksphere.book.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b JOIN b.genres g WHERE g.id IN :genreIds GROUP BY b HAVING COUNT(DISTINCT g) = :genreCount")
    Page<Book> findBooksByAllGenreIds(@Param("genreIds") Set<Long> genreIds, @Param("genreCount") long genreCount, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.tags t WHERE t.id IN :tagIds GROUP BY b HAVING COUNT(DISTINCT t) = :tagCount")
    Page<Book> findBooksByAllTagIds(@Param("tagIds") Set<Long> tagIds, @Param("tagCount") long tagCount, Pageable pageable);

    Page<Book> findAllByAuthorsId(Long authorId, Pageable pageable);
}