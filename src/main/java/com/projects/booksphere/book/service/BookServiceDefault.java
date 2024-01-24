package com.projects.booksphere.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.projects.booksphere.author.model.Author;
import com.projects.booksphere.author.model.AuthorDTO;
import com.projects.booksphere.author.service.AuthorService;
import com.projects.booksphere.book.model.Book;
import com.projects.booksphere.book.model.BookDTO;
import com.projects.booksphere.book.repository.BookRepository;
import com.projects.booksphere.genre.model.Genre;
import com.projects.booksphere.genre.model.GenreDTO;
import com.projects.booksphere.genre.service.GenreService;
import com.projects.booksphere.tag.model.Tag;
import com.projects.booksphere.tag.model.TagDTO;
import com.projects.booksphere.tag.service.TagService;
import com.projects.booksphere.utils.exceptionhandler.exceptions.ElementAlreadyExistsException;
import com.projects.booksphere.utils.exceptionhandler.exceptions.ElementNotFoundException;
import com.projects.booksphere.utils.exceptionhandler.exceptions.ElementUpdateException;
import com.projects.booksphere.utils.mappers.EntityToDtoMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import static com.projects.booksphere.utils.exceptionhandler.ExceptionMessages.*;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceDefault implements BookService {
    private final EntityToDtoMapper entityToDtoMapper;
    private final BookRepository bookRepository;
    private final GenreService genreService;
    private final TagService tagService;
    private final AuthorService authorService;

    @Override
    @Transactional
    public BookDTO createBook(BookDTO bookDTO) {
        Book book = entityToDtoMapper.toBook(bookDTO);

        book.setGenres(this.getDatabaseGenres(bookDTO.genres()));
        book.setTags(this.getDatabaseTags(bookDTO.tags()));
        book.setAuthors(this.getDatabaseAuthors(bookDTO.authors()));

        if (isBookExist(book)) {
            throw new ElementAlreadyExistsException(BOOK_ALREADY_EXIST);
        }

        return entityToDtoMapper.toBookDTO(bookRepository.save(book));
    }

    @Override
    public BookDTO getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .map(entityToDtoMapper::toBookDTO)
                .orElseThrow(() -> new ElementNotFoundException(String.format(BOOK_NOT_FOUND, bookId)));
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(entityToDtoMapper::toBookDTO)
                .toList();
    }

    @Override
    public List<BookDTO> getBooksByGenres(Set<Long> genreIds) {
        return bookRepository.findBooksByAllGenreIds(genreIds, genreIds.size())
                .stream()
                .map(entityToDtoMapper::toBookDTO)
                .toList();
    }

    @Override
    public List<BookDTO> getBooksByTags(Set<Long> tagIds) {
        return bookRepository.findBooksByAllTagIds(tagIds, tagIds.size())
                .stream()
                .map(entityToDtoMapper::toBookDTO)
                .toList();
    }

    @Override
    public List<BookDTO> getBooksByAuthor(Long authorId) {
        return bookRepository.findAllByAuthorsId(authorId)
                .stream()
                .map(entityToDtoMapper::toBookDTO)
                .toList();
    }

    @Override
    @Transactional
    public BookDTO updateBook(Long bookId, JsonMergePatch patch) {
        Book bookDB = bookRepository.findById(bookId).orElseThrow(() ->
                new ElementNotFoundException(String.format(BOOK_NOT_FOUND, bookId)));

        Book bookUpdated = getUpdatedBook(patch, bookDB);

        bookDB.setTitle(bookUpdated.getTitle());
        bookDB.setSlogan(bookUpdated.getSlogan());
        bookDB.setDescription(bookUpdated.getDescription());
        bookDB.setPublishDate(bookUpdated.getPublishDate());
        bookDB.setGenres(bookUpdated.getGenres());

        Set<GenreDTO> genresDTO = bookUpdated.getGenres()
                .stream()
                .map(entityToDtoMapper::toGenreDTO)
                .collect(Collectors.toSet());

        bookDB.setGenres(getDatabaseGenres(genresDTO));

        Set<TagDTO> tagsDTO = bookUpdated.getTags()
                .stream()
                .map(entityToDtoMapper::toTagDTO)
                .collect(Collectors.toSet());

        bookDB.setTags(getDatabaseTags(tagsDTO));

        Set<AuthorDTO> authorsDTO = bookUpdated.getAuthors()
                .stream()
                .map(entityToDtoMapper::toAuthorDTO)
                .collect(Collectors.toSet());

        bookDB.setAuthors(getDatabaseAuthors(authorsDTO));

        return entityToDtoMapper.toBookDTO(bookRepository.save(bookDB));
    }

    @Override
    public void deleteBookById(Long bookId) {
        bookRepository.findById(bookId).ifPresentOrElse(bookRepository::delete,
                () -> {
                    throw new ElementNotFoundException(String.format(BOOK_NOT_FOUND, bookId));
                });
    }

    private Book getUpdatedBook(JsonMergePatch patch, Book bookDB) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModules(new JavaTimeModule());

        try {
            JsonNode updatedBookJSON = patch.apply(objectMapper.convertValue(bookDB, JsonNode.class));
            return objectMapper.treeToValue(updatedBookJSON, Book.class);
        } catch (JsonPatchException | JsonProcessingException exception) {
            throw new ElementUpdateException(BOOK_UPDATE_EXCEPTION_MESSAGE);
        }
    }

    private Set<Genre> getDatabaseGenres(Set<GenreDTO> genres) {
        return genres.stream()
                .map(genreDTO -> genreService.getGenreById(genreDTO.id()))
                .map(entityToDtoMapper::toGenre)
                .collect(Collectors.toSet());
    }

    private Set<Tag> getDatabaseTags(Set<TagDTO> tags) {
        return tags.stream()
                .map(tagDTO -> tagService.getTagById(tagDTO.id()))
                .map(entityToDtoMapper::toTag)
                .collect(Collectors.toSet());
    }

    private Set<Author> getDatabaseAuthors(Set<AuthorDTO> authors) {
        return authors.stream()
                .map(authorDTO -> authorService.getAuthorById(authorDTO.id()))
                .map(entityToDtoMapper::toAuthor)
                .collect(Collectors.toSet());
    }

    private boolean isBookExist(Book book) {
        ExampleMatcher bookMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id", "publish_date")
                .withMatcher("title", exact().ignoreCase())
                .withMatcher("slogan", exact())
                .withMatcher("description", exact());
        return bookRepository.exists(Example.of(book, bookMatcher));
    }
}