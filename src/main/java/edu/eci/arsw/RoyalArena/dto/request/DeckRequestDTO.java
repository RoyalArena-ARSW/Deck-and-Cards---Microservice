package edu.eci.arsw.RoyalArena.dto.request;


import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeckRequestDTO {

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    /**
     * IDs de las 8 cartas que componen el mazo. Se resuelven a entidades
     * completas en el service consultando CardRepository.
     */
    @NotNull
    @Size(min = 8, max = 8, message = "A deck must contain exactly 8 cards")
    private List<Long> cardIds;

    private Boolean isActive;

}