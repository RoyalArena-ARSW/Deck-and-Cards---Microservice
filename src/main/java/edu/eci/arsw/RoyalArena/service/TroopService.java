package edu.eci.arsw.RoyalArena.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.eci.arsw.RoyalArena.dto.request.TroopRequestDTO;
import edu.eci.arsw.RoyalArena.dto.response.TroopResponseDTO;
import edu.eci.arsw.RoyalArena.exception.ResourceNotFoundException;
import edu.eci.arsw.RoyalArena.mappers.TroopMapper;
import edu.eci.arsw.RoyalArena.model.Troop;
import edu.eci.arsw.RoyalArena.repository.CardRepository;
import edu.eci.arsw.RoyalArena.repository.TroopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TroopService {

    private final TroopRepository troopRepository;
    private final CardRepository cardRepository;
    private final TroopMapper troopMapper;

    @Transactional
    public TroopResponseDTO createTroop(TroopRequestDTO request) {
        log.info("Creating new troop: {}", request.getName());

        if (cardRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException(
                "A card with name '" + request.getName() + "' already exists");
        }

        Troop troop = troopMapper.toEntity(request);
        Troop saved = troopRepository.save(troop);

        log.info("Troop created with id {}: {}", saved.getId(), saved.getName());
        return troopMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public TroopResponseDTO getTroopById(Long id) {
        Troop troop = troopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Troop not found with id: " + id));
        return troopMapper.toDto(troop);
    }

    @Transactional(readOnly = true)
    public List<TroopResponseDTO> getAllTroops() {
        return troopRepository.findAll().stream()
                .map(troopMapper::toDto)
                .toList();
    }

    @Transactional
    public void deleteTroop(Long id) {
        if (!troopRepository.existsById(id)) {
            throw new ResourceNotFoundException("Troop not found with id: " + id);
        }
        troopRepository.deleteById(id);
        log.info("Troop deleted with id: {}", id);
    }
}