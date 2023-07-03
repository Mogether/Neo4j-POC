package com.neo4jpoc.bootifulmusuc.service.impl;

import com.neo4jpoc.bootifulmusuc.domain.Genre;
import com.neo4jpoc.bootifulmusuc.repository.GenreRepository;
import com.neo4jpoc.bootifulmusuc.service.GenreService;
import com.neo4jpoc.bootifulmusuc.service.dto.GenreDTO;
import com.neo4jpoc.bootifulmusuc.service.mapper.GenreMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Genre}.
 */
@Service
public class GenreServiceImpl implements GenreService {

    private final Logger log = LoggerFactory.getLogger(GenreServiceImpl.class);

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    public GenreServiceImpl(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    @Override
    public GenreDTO save(GenreDTO genreDTO) {
        log.debug("Request to save Genre : {}", genreDTO);
        Genre genre = genreMapper.toEntity(genreDTO);
        genre = genreRepository.save(genre);
        return genreMapper.toDto(genre);
    }

    @Override
    public GenreDTO update(GenreDTO genreDTO) {
        log.debug("Request to update Genre : {}", genreDTO);
        Genre genre = genreMapper.toEntity(genreDTO);
        genre = genreRepository.save(genre);
        return genreMapper.toDto(genre);
    }

    @Override
    public Optional<GenreDTO> partialUpdate(GenreDTO genreDTO) {
        log.debug("Request to partially update Genre : {}", genreDTO);

        return genreRepository
            .findById(genreDTO.getId())
            .map(existingGenre -> {
                genreMapper.partialUpdate(existingGenre, genreDTO);

                return existingGenre;
            })
            .map(genreRepository::save)
            .map(genreMapper::toDto);
    }

    @Override
    public Page<GenreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Genres");
        return genreRepository.findAll(pageable).map(genreMapper::toDto);
    }

    @Override
    public Optional<GenreDTO> findOne(String id) {
        log.debug("Request to get Genre : {}", id);
        return genreRepository.findById(id).map(genreMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Genre : {}", id);
        genreRepository.deleteById(id);
    }
}
