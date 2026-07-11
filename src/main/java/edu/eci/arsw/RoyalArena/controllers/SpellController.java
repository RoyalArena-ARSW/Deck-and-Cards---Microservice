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

import edu.eci.arsw.RoyalArena.dto.request.SpellRequestDTO;
import edu.eci.arsw.RoyalArena.dto.response.SpellResponseDTO;
import edu.eci.arsw.RoyalArena.service.SpellService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/spells")
@RequiredArgsConstructor
public class SpellController {

    private final SpellService spellService;

    @PostMapping
    public ResponseEntity<SpellResponseDTO> createSpell(@Valid @RequestBody SpellRequestDTO request) {
        log.info("POST /api/spells - Create spell: {}", request.getName());
        SpellResponseDTO response = spellService.createSpell(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpellResponseDTO> getSpellById(@PathVariable Long id) {
        log.info("GET /api/spells/{}", id);
        return ResponseEntity.ok(spellService.getSpellById(id));
    }

    @GetMapping
    public ResponseEntity<List<SpellResponseDTO>> getAllSpells() {
        log.info("GET /api/spells");
        return ResponseEntity.ok(spellService.getAllSpells());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpell(@PathVariable Long id) {
        log.info("DELETE /api/spells/{}", id);
        spellService.deleteSpell(id);
        return ResponseEntity.noContent().build();
    }
}