package edu.eci.arsw.RoyalArena.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.eci.arsw.RoyalArena.model.Spell;
import edu.eci.arsw.RoyalArena.model.enums.EffectType;

@Repository
public interface SpellRepository extends JpaRepository<Spell, Long> {

    List<Spell> findByEffectType(EffectType effectType);
}