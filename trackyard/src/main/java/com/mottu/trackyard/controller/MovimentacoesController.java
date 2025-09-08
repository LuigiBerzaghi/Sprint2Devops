package com.mottu.trackyard.controller;

import com.mottu.trackyard.dto.MovimentacoesDTO;
import com.mottu.trackyard.service.MovimentacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movimentacoes")
@Validated
public class MovimentacoesController {

    private final MovimentacaoService movimentacaoService;

    public MovimentacoesController(MovimentacaoService movimentacaoService) {
        this.movimentacaoService = movimentacaoService;
    }

    //Método POST para criar uma entidade moto
    @PostMapping
    public ResponseEntity<Void> registrarMovimentacao(@Valid @RequestBody MovimentacoesDTO dto) {
        movimentacaoService.registrarMovimentacao(dto);
        return ResponseEntity.status(201).build();
    }

    //Método GET para exibir uma movimentação através do ID
    @GetMapping("/{idMovimentacao}")
    public ResponseEntity<MovimentacoesDTO> getMovimentacao(@PathVariable Long idMovimentacao) {
        MovimentacoesDTO movimentacao = movimentacaoService.getMovimentacaoById(idMovimentacao);
        return ResponseEntity.ok(movimentacao);
    }
}