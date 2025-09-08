package com.mottu.trackyard.controller;

import com.mottu.trackyard.dto.PontosLeituraDTO;

import com.mottu.trackyard.service.PontosLeituraService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/pontos-leitura")
@Validated
public class PontoLeituraController {
    private final PontosLeituraService pontosLeituraService;

    public PontoLeituraController(PontosLeituraService pontosLeituraService) {
        this.pontosLeituraService = pontosLeituraService;
    }

    //Método POST para criar uma entidade ponto de leitura
    @PostMapping
    public ResponseEntity<PontosLeituraDTO> createPontoLeitura(@Valid @RequestBody PontosLeituraDTO dto) {
        return ResponseEntity.status(201).body(pontosLeituraService.createPontoLeitura(dto));
    }
    //Método GET para paginação dos pontos de leitura
    @GetMapping
    public ResponseEntity<Page<PontosLeituraDTO>> getAllPontosLeitura(Pageable pageable) {
        return ResponseEntity.ok(pontosLeituraService.getAllPontosLeitura(pageable));
    }

    //Método GET para exibir um único ponto de leitura através do ID
    @GetMapping("/{idPonto}")
    public ResponseEntity<PontosLeituraDTO> getPontoLeituraById(@PathVariable Long idPonto) {
        return ResponseEntity.ok(pontosLeituraService.getPontoLeituraById(idPonto));
    }

    @PutMapping("/{idPonto}")
    public ResponseEntity<PontosLeituraDTO> updatePontoLeitura(@PathVariable Long idPonto, @Valid @RequestBody PontosLeituraDTO dto) {
        return ResponseEntity.ok(pontosLeituraService.updatePontoLeitura(idPonto, dto));
    }
    
    //Atualiza um ponto de leitura através do ID
    @DeleteMapping("/{idPonto}")
    public ResponseEntity<String> deletePontoLeitura(@PathVariable Long idPonto) {
        pontosLeituraService.deletePontoLeitura(idPonto);
        return ResponseEntity.ok("Ponto de leitura com ID " + idPonto + " foi deletado com sucesso!");
    }
}