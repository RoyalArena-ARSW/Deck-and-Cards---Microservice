package edu.eci.arsw.RoyalArena.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.eci.arsw.RoyalArena.dto.request.SpellRequestDTO;
import edu.eci.arsw.RoyalArena.dto.response.SpellResponseDTO;
import edu.eci.arsw.RoyalArena.exception.ResourceNotFoundException;
import edu.eci.arsw.RoyalArena.mappers.SpellMapper;
import edu.eci.arsw.RoyalArena.model.Spell;
import edu.eci.arsw.RoyalArena.repository.CardRepository;
import edu.eci.arsw.RoyalArena.repository.SpellRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpellService {

    private final SpellRepository spellRepository;
    private final CardRepository cardRepository;
    private final SpellMapper spellMapper;

    @Transactional
    public SpellResponseDTO createSpell(SpellRequestDTO request) {
        log.info("Creating new spell: {}", request.getName());

        if (cardRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException(
                "A card with name '" + request.getName() + "' already exists");
        }

        Spell spell = spellMapper.toEntity(request);
        Spell saved = spellRepository.save(spell);

        log.info("Spell created with id {}: {}", saved.getId(), saved.getName());
        return spellMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public SpellResponseDTO getSpellById(Long id) {
        Spell spell = spellRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spell not found with id: " + id));
        return spellMapper.toDto(spell);
    }

    @Transactional(readOnly = true)
    public List<SpellResponseDTO> getAllSpells() {
        return spellRepository.findAll().stream()
                .map(spellMapper::toDto)
                .toList();
    }

    @Transactional
    public void deleteSpell(Long id) {
        if (!spellRepository.existsById(id)) {
            throw new ResourceNotFoundException("Spell not found with id: " + id);
        }
        spellRepository.deleteById(id);
        log.info("Spell deleted with id: {}", id);
    }
}