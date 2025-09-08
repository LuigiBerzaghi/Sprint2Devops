package com.mottu.trackyard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MotosDTO(
 // @NotBlank(message = "ID da moto é obrigatório")
    @Size(max = 50, message = "ID da moto deve ter no máximo 50 caracteres")
    String idMoto,

    @NotBlank(message = "Modelo é obrigatório")
    @Size(max = 100, message = "Modelo deve ter no máximo 100 caracteres")
    String modelo,

    @NotBlank(message = "Placa é obrigatória")
    @Size(max = 10, message = "Placa deve ter no máximo 10 caracteres")
    String placa
) {}