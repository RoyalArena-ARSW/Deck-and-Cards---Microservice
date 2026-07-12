package edu.eci.arsw.RoyalArena.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.arsw.RoyalArena.dto.request.BuildingRequestDTO;
import edu.eci.arsw.RoyalArena.dto.response.BuildingResponseDTO;
import edu.eci.arsw.RoyalArena.service.BuildingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/buildings")
@RequiredArgsConstructor
public class BuildingController {

    private final BuildingService buildingService;

    @PostMapping
    public ResponseEntity<BuildingResponseDTO> createBuilding(@Valid @RequestBody BuildingRequestDTO request) {
        log.info("POST /api/buildings - Create building: {}", request.getName());
        BuildingResponseDTO response = buildingService.createBuilding(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BuildingResponseDTO> getBuildingById(@PathVariable Long id) {
        log.info("GET /api/buildings/{}", id);
        return ResponseEntity.ok(buildingService.getBuildingById(id));
    }

    @GetMapping
    public ResponseEntity<List<BuildingResponseDTO>> getAllBuildings() {
        log.info("GET /api/buildings");
        return ResponseEntity.ok(buildingService.getAllBuildings());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable Long id) {
        log.info("DELETE /api/buildings/{}", id);
        buildingService.deleteBuilding(id);
        return ResponseEntity.noContent().build();
    }
}