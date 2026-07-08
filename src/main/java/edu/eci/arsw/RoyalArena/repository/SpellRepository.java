package edu.eci.arsw.RoyalArena.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.royalarena.cards.model.Spell;
import com.royalarena.cards.model.enums.EffectType;

@Repository
public interface SpellRepository extends JpaRepository<Spell, Long> {

    List<Spell> findByEffectType(EffectType effectType);
}