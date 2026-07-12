package edu.eci.arsw.RoyalArena.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.eci.arsw.RoyalArena.model.Troop;
import edu.eci.arsw.RoyalArena.model.enums.Target;


@Repository
public interface TroopRepository extends JpaRepository<Troop, Long> {

    List<Troop> findByIsAerial(Boolean isAerial);

    List<Troop> findByTarget(Target target);
}