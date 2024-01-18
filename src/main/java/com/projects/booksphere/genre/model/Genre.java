package com.projects.booksphere.genre.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "genre")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) return false;
        Genre genre = (Genre) obj;
        return id != null && Objects.equals(id, genre.id) && name != null && Objects.equals(name, genre.name);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}