package edu.eci.arsw.RoyalArena.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDeckCardsRequestDTO {

    @NotNull(message = "cardIds es obligatorio")
    @Size(min = 8, max = 8, message = "El mazo debe tener exactamente 8 cartas")
    private List<Long> cardIds;
}