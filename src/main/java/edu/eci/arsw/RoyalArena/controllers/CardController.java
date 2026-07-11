package edu.eci.arsw.RoyalArena.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.arsw.RoyalArena.dto.response.CardResponseDTO;
import edu.eci.arsw.RoyalArena.model.enums.CardType;
import edu.eci.arsw.RoyalArena.model.enums.Rarity;
import edu.eci.arsw.RoyalArena.service.CardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Endpoints de consulta transversal del catálogo. Este controller NO crea cartas
 * (para eso están /api/troops, /api/spells, /api/buildings). Se usa cuando el
 * frontend quiere listar el catálogo completo, filtrar por rareza, tipo o elixir,
 * sin importar la subclase concreta.
 */
@Slf4j
@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping("/{id}")
    public ResponseEntity<CardResponseDTO> getCardById(@PathVariable Long id) {
        log.info("GET /api/cards/{}", id);
        return ResponseEntity.ok(cardService.getCardById(id));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<CardResponseDTO> getCardByName(@PathVariable String name) {
        log.info("GET /api/cards/by-name/{}", name);
        return ResponseEntity.ok(cardService.getCardByName(name));
    }

    /**
     * Lista todas las cartas del catálogo. Admite filtros opcionales via query params:
     *   /api/cards
     *   /api/cards?rarity=EPIC
     *   /api/cards?type=TROOP
     *   /api/cards?elixirCost=3
     */
    @GetMapping
    public ResponseEntity<List<CardResponseDTO>> getCards(
            @RequestParam(required = false) Rarity rarity,
            @RequestParam(required = false) CardType type,
            @RequestParam(required = false) Integer elixirCost) {

        log.info("GET /api/cards - filters: rarity={}, type={}, elixirCost={}",
                rarity, type, elixirCost);

        if (rarity != null) return ResponseEntity.ok(cardService.getCardsByRarity(rarity));
        if (type != null) return ResponseEntity.ok(cardService.getCardsByType(type));
        if (elixirCost != null) return ResponseEntity.ok(cardService.getCardsByElixirCost(elixirCost));
        return ResponseEntity.ok(cardService.getAllCards());
    }
}