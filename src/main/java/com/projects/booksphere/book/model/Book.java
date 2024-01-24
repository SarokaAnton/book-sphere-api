package com.projects.booksphere.book.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projects.booksphere.author.model.Author;
import com.projects.booksphere.genre.model.Genre;
import com.projects.booksphere.tag.model.Tag;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "book")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String slogan;
    private String description;
    @Column(name = "publish_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishDate;
    @ManyToMany
    @ToString.Exclude
    @JoinTable(name = "book_has_genre", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;
    @ManyToMany
    @ToString.Exclude
    @JoinTable(name = "book_has_tag", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;
    @ManyToMany
    @ToString.Exclude
    @JoinTable(name = "book_has_author", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) return false;
        Book book = (Book) obj;
        return id != null && Objects.equals(id, book.id) && title != null && Objects.equals(title, book.title)
                && slogan != null && Objects.equals(slogan, book.slogan)
                && description != null && Objects.equals(description, book.description)
                && publishDate != null && Objects.equals(publishDate, book.publishDate)
                && genres != null && Objects.equals(genres, book.genres)
                && tags != null && Objects.equals(tags, book.tags)
                && authors != null && Objects.equals(authors, book.authors);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}