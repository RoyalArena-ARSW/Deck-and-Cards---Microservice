package edu.eci.arsw.RoyalArena.model.enums;

/**
 * Define dónde puede desplegarse una carta en el tablero.
 * OWN_SIDE: solo en la mitad propia del jugador (tropas, edificios).
 * ANYWHERE: en cualquier parte del mapa (hechizos como Fireball, Arrows).
 */
public enum DeploymentType {
    OWN_SIDE,
    ANYWHERE
}