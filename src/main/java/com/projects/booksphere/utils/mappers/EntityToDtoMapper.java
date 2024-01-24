package com.projects.booksphere.utils.mappers;

import com.projects.booksphere.author.model.Author;
import com.projects.booksphere.author.model.AuthorDTO;
import com.projects.booksphere.book.model.Book;
import com.projects.booksphere.book.model.BookDTO;
import com.projects.booksphere.genre.model.Genre;
import com.projects.booksphere.genre.model.GenreDTO;
import com.projects.booksphere.tag.model.Tag;
import com.projects.booksphere.tag.model.TagDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityToDtoMapper {
    GenreDTO toGenreDTO(Genre genre);

    Genre toGenre(GenreDTO genreDTO);

    BookDTO toBookDTO(Book book);

    Book toBook(BookDTO bookDTO);

    AuthorDTO toAuthorDTO(Author author);

    Author toAuthor(AuthorDTO authorDTO);

    TagDTO toTagDTO(Tag tag);

    Tag toTag(TagDTO tagDTO);
}