package edu.eci.arsw.RoyalArena.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {

    List<Building> findByIsSpawner(Boolean isSpawner);
}