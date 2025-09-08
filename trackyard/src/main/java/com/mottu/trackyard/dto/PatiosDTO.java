package com.mottu.trackyard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PatiosDTO(
    Long idPatio,

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    String nome,

    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    String telefone,

    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    String endereco
) {}