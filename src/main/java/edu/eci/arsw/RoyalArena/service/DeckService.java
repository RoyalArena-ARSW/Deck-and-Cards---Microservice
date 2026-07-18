package edu.eci.arsw.RoyalArena.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.eci.arsw.RoyalArena.dto.request.DeckRequestDTO;
import edu.eci.arsw.RoyalArena.dto.response.DeckResponseDTO;
import edu.eci.arsw.RoyalArena.exception.InvalidDeckException;
import edu.eci.arsw.RoyalArena.exception.ResourceNotFoundException;
import edu.eci.arsw.RoyalArena.exception.UnauthorizedException;
import edu.eci.arsw.RoyalArena.mappers.DeckMapper;
import edu.eci.arsw.RoyalArena.model.Card;
import edu.eci.arsw.RoyalArena.model.Deck;
import edu.eci.arsw.RoyalArena.repository.CardRepository;
import edu.eci.arsw.RoyalArena.repository.DeckRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeckService {

    private final DeckRepository deckRepository;
    private final CardRepository cardRepository;
    private final DeckMapper deckMapper;

    /**
     * Crea un mazo para el usuario. userId lo saca el controller del header
     * X-User-Id que inyecta el Gateway (mismo patrón de TechCup).
     */
    @Transactional
    public DeckResponseDTO createDeck(DeckRequestDTO request, Long userId) {
        log.info("Creating deck '{}' for user {}", request.getName(), userId);

        // 1. Validar unicidad del nombre por usuario.
        if (deckRepository.existsByUserIdAndName(userId, request.getName())) {
            throw new IllegalArgumentException(
                "User " + userId + " already has a deck named '" + request.getName() + "'");
        }

        // 2. Validar que no haya IDs duplicados (no puedes tener 2 Knights).
        Set<Long> uniqueIds = new HashSet<>(request.getCardIds());
        if (uniqueIds.size() != request.getCardIds().size()) {
            throw new InvalidDeckException("A deck cannot contain duplicate cards");
        }

        // 3. Resolver los IDs a entidades.
        List<Card> cards = cardRepository.findAllById(request.getCardIds());
        if (cards.size() != 8) {
            throw new InvalidDeckException(
                "Some card IDs do not exist. Expected 8 cards, found " + cards.size());
        }

        // 4. Si el nuevo mazo es marcado como activo, desactivar el actual del usuario.
        Boolean isActive = request.getIsActive() != null && request.getIsActive();
        if (Boolean.TRUE.equals(isActive)) {
            deactivateCurrentActiveDeck(userId);
        }

        Deck deck = Deck.builder()
                .name(request.getName())
                .userId(userId)
                .cards(cards)
                .isActive(isActive)
                .build();

        Deck saved = deckRepository.save(deck);
        log.info("Deck {} created for user {}", saved.getId(), userId);
        return deckMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public DeckResponseDTO getDeckById(Long id) {
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found with id: " + id));
        return deckMapper.toDto(deck);
    }

    @Transactional(readOnly = true)
    public List<DeckResponseDTO> getDecksByUserId(Long userId) {
        return deckRepository.findByUserId(userId).stream()
                .map(deckMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public DeckResponseDTO getActiveDeck(Long userId) {
        Deck deck = deckRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "User " + userId + " has no active deck"));
        return deckMapper.toDto(deck);
    }

    /**
     * Cambia el mazo activo del usuario. Desactiva el actual y activa el nuevo
     * en una sola transacción para no dejar dos mazos activos si algo falla.
     */
    @Transactional
    public DeckResponseDTO setActiveDeck(Long deckId, Long userId) {
        Deck newActive = deckRepository.findById(deckId)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found with id: " + deckId));

        if (!newActive.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Deck does not belong to user " + userId);
        }

        deactivateCurrentActiveDeck(userId);
        newActive.setIsActive(true);
        Deck saved = deckRepository.save(newActive);

        log.info("Deck {} set as active for user {}", saved.getId(), userId);
        return deckMapper.toDto(saved);
    }

    /**
     * Reemplaza las cartas de un mazo. Valida propiedad, cantidad y duplicados.
     */
    @Transactional
    public DeckResponseDTO updateDeckCards(Long deckId, Long userId, List<Long> cardIds) {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new ResourceNotFoundException("Mazo no encontrado: " + deckId));

        // El mazo debe ser del usuario que lo edita
        if (!deck.getUserId().equals(userId)) {
            throw new UnauthorizedException("Ese mazo no te pertenece");
        }

        // Sin cartas repetidas
        if (cardIds.stream().distinct().count() != cardIds.size()) {
            throw new InvalidDeckException("El mazo no puede tener cartas repetidas");
        }

        // Traer las 8 cartas y verificar que todas existan
        List<Card> cards = cardRepository.findAllById(cardIds);
        if (cards.size() != cardIds.size()) {
            throw new InvalidDeckException("Alguna carta no existe en el catálogo");
        }

        deck.setCards(cards);
        Deck saved = deckRepository.save(deck);
        return deckMapper.toDto(saved);
    }

    @Transactional
    public void deleteDeck(Long id, Long userId) {
        Deck deck = deckRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found with id: " + id));

        if (!deck.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Deck does not belong to user " + userId);
        }

        deckRepository.delete(deck);
        log.info("Deck {} deleted by user {}", id, userId);
    }

    /**
     * Desactiva el mazo actualmente activo del usuario, si existe.
     */
    private void deactivateCurrentActiveDeck(Long userId) {
        deckRepository.findByUserIdAndIsActiveTrue(userId).ifPresent(current -> {
            current.setIsActive(false);
            deckRepository.save(current);
            log.debug("Deactivated deck {} for user {}", current.getId(), userId);
        });
    }

    /**
     * Mazo activo de CUALQUIER usuario. Endpoint interno para Game Engine:
     * a diferencia de getMyActiveDeck, el userId viene por path, no del header.
     * El @Transactional cubre el mapeo de las cartas (evita LazyInitializationException).
     */
    @Transactional(readOnly = true)
    public DeckResponseDTO getActiveDeckByUserId(Long userId) {
        Deck deck = deckRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No active deck found for user " + userId));
        return deckMapper.toDto(deck);
    }
}