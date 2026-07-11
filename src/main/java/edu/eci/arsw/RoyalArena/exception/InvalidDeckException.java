package edu.eci.arsw.RoyalArena.exception;

/**
 * Se lanza cuando un mazo no cumple las reglas de negocio:
 * no tiene exactamente 8 cartas, tiene cartas duplicadas, etc.
 */
public class InvalidDeckException extends RuntimeException {
    public InvalidDeckException(String message) {
        super(message);
    }
}