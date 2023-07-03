package com.neo4jpoc.bootifulmusuc.service.impl;

import com.neo4jpoc.bootifulmusuc.domain.Album;
import com.neo4jpoc.bootifulmusuc.repository.AlbumRepository;
import com.neo4jpoc.bootifulmusuc.service.AlbumService;
import com.neo4jpoc.bootifulmusuc.service.dto.AlbumDTO;
import com.neo4jpoc.bootifulmusuc.service.mapper.AlbumMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Album}.
 */
@Service
public class AlbumServiceImpl implements AlbumService {

    private final Logger log = LoggerFactory.getLogger(AlbumServiceImpl.class);

    private final AlbumRepository albumRepository;

    private final AlbumMapper albumMapper;

    public AlbumServiceImpl(AlbumRepository albumRepository, AlbumMapper albumMapper) {
        this.albumRepository = albumRepository;
        this.albumMapper = albumMapper;
    }

    @Override
    public AlbumDTO save(AlbumDTO albumDTO) {
        log.debug("Request to save Album : {}", albumDTO);
        Album album = albumMapper.toEntity(albumDTO);
        album = albumRepository.save(album);
        return albumMapper.toDto(album);
    }

    @Override
    public AlbumDTO update(AlbumDTO albumDTO) {
        log.debug("Request to update Album : {}", albumDTO);
        Album album = albumMapper.toEntity(albumDTO);
        album = albumRepository.save(album);
        return albumMapper.toDto(album);
    }

    @Override
    public Optional<AlbumDTO> partialUpdate(AlbumDTO albumDTO) {
        log.debug("Request to partially update Album : {}", albumDTO);

        return albumRepository
            .findById(albumDTO.getId())
            .map(existingAlbum -> {
                albumMapper.partialUpdate(existingAlbum, albumDTO);

                return existingAlbum;
            })
            .map(albumRepository::save)
            .map(albumMapper::toDto);
    }

    @Override
    public Page<AlbumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Albums");
        return albumRepository.findAll(pageable).map(albumMapper::toDto);
    }

    @Override
    public Optional<AlbumDTO> findOne(String id) {
        log.debug("Request to get Album : {}", id);
        return albumRepository.findById(id).map(albumMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Album : {}", id);
        albumRepository.deleteById(id);
    }
}
