package edu.eci.arsw.RoyalArena.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.arsw.RoyalArena.dto.request.DeckRequestDTO;
import edu.eci.arsw.RoyalArena.dto.response.DeckResponseDTO;
import edu.eci.arsw.RoyalArena.service.DeckService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/decks")
@RequiredArgsConstructor
public class DeckController {

    private final DeckService deckService;

    /**
     * Crea un mazo para el usuario autenticado. El userId lo saca del header
     * X-User-Id que inyecta el API Gateway después de validar el JWT.
     * Mismo patrón que aplicamos en TechCup con captainId.
     */
    @PostMapping
    public ResponseEntity<DeckResponseDTO> createDeck(
            @Valid @RequestBody DeckRequestDTO request,
            @RequestHeader("X-User-Id") Long userId) {
        log.info("POST /api/decks - user {} creating deck '{}'", userId, request.getName());
        DeckResponseDTO response = deckService.createDeck(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeckResponseDTO> getDeckById(@PathVariable Long id) {
        log.info("GET /api/decks/{}", id);
        return ResponseEntity.ok(deckService.getDeckById(id));
    }

    /**
     * Lista todos los mazos del usuario autenticado.
     */
    @GetMapping("/my")
    public ResponseEntity<List<DeckResponseDTO>> getMyDecks(
            @RequestHeader("X-User-Id") Long userId) {
        log.info("GET /api/decks/my - user {}", userId);
        return ResponseEntity.ok(deckService.getDecksByUserId(userId));
    }

    /**
     * Obtiene el mazo actualmente activo del usuario (el que usa para partidas).
     */
    @GetMapping("/my/active")
    public ResponseEntity<DeckResponseDTO> getMyActiveDeck(
            @RequestHeader("X-User-Id") Long userId) {
        log.info("GET /api/decks/my/active - user {}", userId);
        return ResponseEntity.ok(deckService.getActiveDeck(userId));
    }

    /**
     * Cambia el mazo activo. Desactiva el mazo actual y activa el especificado.
     */
    @PutMapping("/{id}/activate")
    public ResponseEntity<DeckResponseDTO> setActiveDeck(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        log.info("PUT /api/decks/{}/activate - user {}", id, userId);
        return ResponseEntity.ok(deckService.setActiveDeck(id, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeck(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        log.info("DELETE /api/decks/{} - user {}", id, userId);
        deckService.deleteDeck(id, userId);
        return ResponseEntity.noContent().build();
    }
}