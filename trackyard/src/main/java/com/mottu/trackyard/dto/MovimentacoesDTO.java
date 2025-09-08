package com.mottu.trackyard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record MovimentacoesDTO(
    Long idMovimentacao,

    @NotBlank(message = "ID da moto é obrigatório")
    String idMoto,

    @NotNull(message = "ID do ponto é obrigatório")
    Long idPonto,

    LocalDateTime dataHora
) {}