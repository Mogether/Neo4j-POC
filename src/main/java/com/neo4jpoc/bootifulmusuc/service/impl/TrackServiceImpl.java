package com.neo4jpoc.bootifulmusuc.service.impl;

import com.neo4jpoc.bootifulmusuc.domain.Track;
import com.neo4jpoc.bootifulmusuc.repository.TrackRepository;
import com.neo4jpoc.bootifulmusuc.service.TrackService;
import com.neo4jpoc.bootifulmusuc.service.dto.TrackDTO;
import com.neo4jpoc.bootifulmusuc.service.mapper.TrackMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Track}.
 */
@Service
public class TrackServiceImpl implements TrackService {

    private final Logger log = LoggerFactory.getLogger(TrackServiceImpl.class);

    private final TrackRepository trackRepository;

    private final TrackMapper trackMapper;

    public TrackServiceImpl(TrackRepository trackRepository, TrackMapper trackMapper) {
        this.trackRepository = trackRepository;
        this.trackMapper = trackMapper;
    }

    @Override
    public TrackDTO save(TrackDTO trackDTO) {
        log.debug("Request to save Track : {}", trackDTO);
        Track track = trackMapper.toEntity(trackDTO);
        track = trackRepository.save(track);
        return trackMapper.toDto(track);
    }

    @Override
    public TrackDTO update(TrackDTO trackDTO) {
        log.debug("Request to update Track : {}", trackDTO);
        Track track = trackMapper.toEntity(trackDTO);
        track = trackRepository.save(track);
        return trackMapper.toDto(track);
    }

    @Override
    public Optional<TrackDTO> partialUpdate(TrackDTO trackDTO) {
        log.debug("Request to partially update Track : {}", trackDTO);

        return trackRepository
            .findById(trackDTO.getId())
            .map(existingTrack -> {
                trackMapper.partialUpdate(existingTrack, trackDTO);

                return existingTrack;
            })
            .map(trackRepository::save)
            .map(trackMapper::toDto);
    }

    @Override
    public Page<TrackDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Tracks");
        return trackRepository.findAll(pageable).map(trackMapper::toDto);
    }

    @Override
    public Optional<TrackDTO> findOne(String id) {
        log.debug("Request to get Track : {}", id);
        return trackRepository.findById(id).map(trackMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Track : {}", id);
        trackRepository.deleteById(id);
    }
}
