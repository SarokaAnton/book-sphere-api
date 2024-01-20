package com.projects.booksphere.genre.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.projects.booksphere.genre.model.Genre;
import com.projects.booksphere.genre.model.GenreDTO;
import com.projects.booksphere.genre.repository.GenreRepository;
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
public class GenreServiceDefault implements GenreService {
    private final EntityToDtoMapper entityToDtoMapper;
    private final GenreRepository genreRepository;

    @Override
    @Transactional
    public GenreDTO createGenre(GenreDTO genreDTO) {
        Genre genre = entityToDtoMapper.toGenre(genreDTO);
        if (isGenreExist(genre)) {
            throw new ElementAlreadyExistsException(String.format(GENRE_ALREADY_EXIST, genreDTO.name()));
        }
        return entityToDtoMapper.toGenreDTO(genreRepository.save(genre));
    }

    @Override
    public GenreDTO getGenreById(Long genreId) {
        return genreRepository.findById(genreId)
                .map(entityToDtoMapper::toGenreDTO)
                .orElseThrow(() -> new ElementNotFoundException(String.format(GENRE_NOT_FOUND, genreId)));
    }

    @Override
    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll()
                .stream()
                .map(entityToDtoMapper::toGenreDTO)
                .toList();
    }

    @Override
    @Transactional
    public GenreDTO updateGenre(Long genreId, JsonMergePatch patch) {
        Genre genreDB = genreRepository.findById(genreId).orElseThrow(() ->
                new ElementNotFoundException(String.format(GENRE_NOT_FOUND, genreId)));

        Genre genreUpdated = getUpdatedGenre(patch, genreDB);

        if (isGenreExist(genreUpdated)) {
            throw new ElementAlreadyExistsException(String.format(GENRE_ALREADY_EXIST, genreUpdated.getName()));
        }

        genreDB.setName(genreUpdated.getName());

        return entityToDtoMapper.toGenreDTO(genreRepository.save(genreDB));
    }

    private Genre getUpdatedGenre(JsonMergePatch patch, Genre genreDB) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode updatedGenreJSON = patch.apply(objectMapper.convertValue(genreDB, JsonNode.class));
            return objectMapper.treeToValue(updatedGenreJSON, Genre.class);
        } catch (JsonPatchException | JsonProcessingException exception) {
            throw new ElementUpdateException(GENRE_UPDATE_EXCEPTION_MESSAGE);
        }
    }

    @Override
    public void deleteGenreById(Long genreId) {
        genreRepository.findById(genreId).ifPresentOrElse(genreRepository::delete,
                () -> {
                    throw new ElementNotFoundException(String.format(GENRE_NOT_FOUND, genreId));
                });
    }

    private boolean isGenreExist(Genre genre) {
        ExampleMatcher genreMatcher = ExampleMatcher.matching()
                .withMatcher("name", exact().ignoreCase());
        return genreRepository.exists(Example.of(genre, genreMatcher));
    }
}