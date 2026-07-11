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

import edu.eci.arsw.RoyalArena.dto.request.TroopRequestDTO;
import edu.eci.arsw.RoyalArena.dto.response.TroopResponseDTO;
import edu.eci.arsw.RoyalArena.service.TroopService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/troops")
@RequiredArgsConstructor
public class TroopController {

    private final TroopService troopService;

    @PostMapping
    public ResponseEntity<TroopResponseDTO> createTroop(@Valid @RequestBody TroopRequestDTO request) {
        log.info("POST /api/troops - Create troop: {}", request.getName());
        TroopResponseDTO response = troopService.createTroop(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TroopResponseDTO> getTroopById(@PathVariable Long id) {
        log.info("GET /api/troops/{}", id);
        return ResponseEntity.ok(troopService.getTroopById(id));
    }

    @GetMapping
    public ResponseEntity<List<TroopResponseDTO>> getAllTroops() {
        log.info("GET /api/troops");
        return ResponseEntity.ok(troopService.getAllTroops());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTroop(@PathVariable Long id) {
        log.info("DELETE /api/troops/{}", id);
        troopService.deleteTroop(id);
        return ResponseEntity.noContent().build();
    }
}