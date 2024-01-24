package com.projects.booksphere.author.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "author")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "second_name")
    private String secondName;
    private String biography;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) return false;
        Author author = (Author) obj;
        return id != null && Objects.equals(id, author.id)
                && firstName != null && Objects.equals(firstName, author.firstName)
                && secondName != null && Objects.equals(secondName, author.secondName)
                && biography != null && Objects.equals(biography, author.biography);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}