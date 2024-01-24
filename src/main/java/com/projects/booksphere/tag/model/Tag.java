package com.projects.booksphere.tag.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "tag")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) return false;
        Tag tag = (Tag) obj;
        return id != null && Objects.equals(id, tag.id) && name != null && Objects.equals(name, tag.name);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}