package edu.eci.arsw.RoyalArena.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.eci.arsw.RoyalArena.dto.request.BuildingRequestDTO;
import edu.eci.arsw.RoyalArena.dto.response.BuildingResponseDTO;
import edu.eci.arsw.RoyalArena.exception.ResourceNotFoundException;
import edu.eci.arsw.RoyalArena.mappers.BuildingMapper;
import edu.eci.arsw.RoyalArena.model.Building;
import edu.eci.arsw.RoyalArena.repository.BuildingRepository;
import edu.eci.arsw.RoyalArena.repository.CardRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final CardRepository cardRepository;
    private final BuildingMapper buildingMapper;

    @Transactional
    public BuildingResponseDTO createBuilding(BuildingRequestDTO request) {
        log.info("Creating new building: {}", request.getName());

        if (cardRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException(
                "A card with name '" + request.getName() + "' already exists");
        }

        // Si es spawner, debe tener los campos de spawn definidos.
        if (Boolean.TRUE.equals(request.getIsSpawner())) {
            if (request.getSpawnedUnit() == null || request.getSpawnedUnit().isBlank()
                || request.getSpawnIntervalSeconds() == null
                || request.getSpawnCount() == null) {
                throw new IllegalArgumentException(
                    "A spawner building must define spawnedUnit, spawnIntervalSeconds and spawnCount");
            }
        }

        Building building = buildingMapper.toEntity(request);
        Building saved = buildingRepository.save(building);

        log.info("Building created with id {}: {}", saved.getId(), saved.getName());
        return buildingMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public BuildingResponseDTO getBuildingById(Long id) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Building not found with id: " + id));
        return buildingMapper.toDto(building);
    }

    @Transactional(readOnly = true)
    public List<BuildingResponseDTO> getAllBuildings() {
        return buildingRepository.findAll().stream()
                .map(buildingMapper::toDto)
                .toList();
    }

    @Transactional
    public void deleteBuilding(Long id) {
        if (!buildingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Building not found with id: " + id);
        }
        buildingRepository.deleteById(id);
        log.info("Building deleted with id: {}", id);
    }
}