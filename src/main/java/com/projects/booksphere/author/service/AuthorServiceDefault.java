package com.projects.booksphere.author.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.projects.booksphere.author.model.Author;
import com.projects.booksphere.author.model.AuthorDTO;
import com.projects.booksphere.author.repository.AuthorRepository;
import com.projects.booksphere.utils.exceptionhandler.exceptions.ElementAlreadyExistsException;
import com.projects.booksphere.utils.exceptionhandler.exceptions.ElementNotFoundException;
import com.projects.booksphere.utils.exceptionhandler.exceptions.ElementUpdateException;
import com.projects.booksphere.utils.mappers.EntityToDtoMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.projects.booksphere.utils.exceptionhandler.ExceptionMessages.*;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Service
@RequiredArgsConstructor
public class AuthorServiceDefault implements AuthorService {
    private final EntityToDtoMapper entityToDtoMapper;
    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = entityToDtoMapper.toAuthor(authorDTO);

        if (isAuthorExist(author)) {
            throw new ElementAlreadyExistsException(AUTHOR_ALREADY_EXIST);
        }

        return entityToDtoMapper.toAuthorDTO(authorRepository.save(author));
    }

    @Override
    public AuthorDTO getAuthorById(Long authorId) {
        return authorRepository.findById(authorId)
                .map(entityToDtoMapper::toAuthorDTO)
                .orElseThrow(() -> new ElementNotFoundException(String.format(AUTHOR_NOT_FOUND, authorId)));
    }

    @Override
    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(entityToDtoMapper::toAuthorDTO)
                .toList();
    }

    @Override
    @Transactional
    public AuthorDTO updateAuthor(Long authorId, JsonMergePatch patch) {
        Author authorDB = authorRepository.findById(authorId).orElseThrow(() ->
                new ElementNotFoundException(String.format(AUTHOR_NOT_FOUND, authorId)));

        Author authorUpdated = getUpdatedAuthor(patch, authorDB);

        if (isAuthorExist(authorUpdated)) {
            throw new ElementAlreadyExistsException(AUTHOR_ALREADY_EXIST);
        }

        authorDB.setFirstName(authorUpdated.getFirstName());
        authorDB.setSecondName(authorUpdated.getSecondName());
        authorDB.setBiography(authorUpdated.getBiography());

        return entityToDtoMapper.toAuthorDTO(authorRepository.save(authorDB));
    }

    @Override
    public void deleteAuthorById(Long authorId) {
        authorRepository.findById(authorId).ifPresentOrElse(authorRepository::delete,
                () -> {
                    throw new ElementNotFoundException(String.format(AUTHOR_NOT_FOUND, authorId));
                });
    }

    private Author getUpdatedAuthor(JsonMergePatch patch, Author authorDB) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode updatedAuthorJSON = patch.apply(objectMapper.convertValue(authorDB, JsonNode.class));
            return objectMapper.treeToValue(updatedAuthorJSON, Author.class);
        } catch (JsonPatchException | JsonProcessingException exception) {
            throw new ElementUpdateException(AUTHOR_UPDATE_EXCEPTION_MESSAGE);
        }
    }

    private boolean isAuthorExist(Author author) {
        ExampleMatcher authorMatcher = ExampleMatcher.matching()
                .withMatcher("first_name", exact())
                .withMatcher("second_name", exact())
                .withMatcher("biography", exact());
        return authorRepository.exists(Example.of(author, authorMatcher));
    }
}