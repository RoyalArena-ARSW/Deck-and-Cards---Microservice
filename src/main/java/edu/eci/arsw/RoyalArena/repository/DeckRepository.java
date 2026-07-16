package edu.eci.arsw.RoyalArena.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.eci.arsw.RoyalArena.model.Deck;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long> {

    List<Deck> findByUserId(Long userId);

    Optional<Deck> findByUserIdAndIsActiveTrue(Long userId);

    boolean existsByUserIdAndName(Long userId, String name);

    Optional<Deck> findByUserIdAndActiveTrue(Long userId);
}