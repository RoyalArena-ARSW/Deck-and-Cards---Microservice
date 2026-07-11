package edu.eci.arsw.RoyalArena.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.eci.arsw.RoyalArena.model.Card;
import edu.eci.arsw.RoyalArena.model.enums.CardType;
import edu.eci.arsw.RoyalArena.model.enums.Rarity;



@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByName(String name);

    List<Card> findByRarity(Rarity rarity);

    List<Card> findByType(CardType type);

    List<Card> findByElixirCost(int elixirCost);

    boolean existsByName(String name);

    List<Card> findAllByOrderByRarityAscNameAsc();

    List<Card> findByRarityOrderByNameAsc(Rarity rarity);

    List<Card> findByTypeOrderByRarityAscNameAsc(CardType type);

    List<Card> findByElixirCostOrderByRarityAscNameAsc(int elixirCost);
}