package edu.eci.arsw.RoyalArena.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.eci.arsw.RoyalArena.dto.response.CardResponseDTO;
import edu.eci.arsw.RoyalArena.exception.ResourceNotFoundException;
import edu.eci.arsw.RoyalArena.mappers.CardMapper;
import edu.eci.arsw.RoyalArena.model.Card;
import edu.eci.arsw.RoyalArena.model.enums.CardType;
import edu.eci.arsw.RoyalArena.model.enums.Rarity;
import edu.eci.arsw.RoyalArena.repository.CardRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Transactional(readOnly = true)
    public CardResponseDTO getCardById(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with id: " + id));
        return cardMapper.toDto(card);
    }

    @Transactional(readOnly = true)
    public CardResponseDTO getCardByName(String name) {
        Card card = cardRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Card not found with name: " + name));
        return cardMapper.toDto(card);
    }

    @Transactional(readOnly = true)
    public List<CardResponseDTO> getAllCards() {
        return cardMapper.toDtoList(cardRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<CardResponseDTO> getCardsByRarity(Rarity rarity) {
        return cardMapper.toDtoList(cardRepository.findByRarity(rarity));
    }

    @Transactional(readOnly = true)
    public List<CardResponseDTO> getCardsByType(CardType type) {
        return cardMapper.toDtoList(cardRepository.findByType(type));
    }

    @Transactional(readOnly = true)
    public List<CardResponseDTO> getCardsByElixirCost(int elixirCost) {
        return cardMapper.toDtoList(cardRepository.findByElixirCost(elixirCost));
    }
}