package com.projects.booksphere.book.repository;

import com.projects.booksphere.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b JOIN b.genres g WHERE g.id IN :genreIds GROUP BY b HAVING COUNT(DISTINCT g) = :genreCount")
    List<Book> findBooksByAllGenreIds(@Param("genreIds") Set<Long> genreIds, @Param("genreCount") long genreCount);

    @Query("SELECT b FROM Book b JOIN b.tags t WHERE t.id IN :tagIds GROUP BY b HAVING COUNT(DISTINCT t) = :tagCount")
    List<Book> findBooksByAllTagIds(@Param("tagIds") Set<Long> tagIds, @Param("tagCount") long tagCount);

    List<Book> findAllByAuthorsId(Long authorId);
}