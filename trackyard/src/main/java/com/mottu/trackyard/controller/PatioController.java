package com.mottu.trackyard.controller;

import com.mottu.trackyard.dto.PatiosDTO;
import com.mottu.trackyard.service.PatioService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.*;

@RestController
@RequestMapping("/api/patios")
@Validated
public class PatioController {
    private final PatioService patioService;

    public PatioController(PatioService patioService) {
        this.patioService = patioService;
    }

    //Método POST para criar uma entidade Patio
    @PostMapping
    public ResponseEntity<PatiosDTO> createPatio(@Valid @RequestBody PatiosDTO dto) {
        return ResponseEntity.status(201).body(patioService.createPatio(dto));
    }
    //Atualiza um pátio através do ID
    @PutMapping("/{idPatio}")
    public ResponseEntity<PatiosDTO> updatePatio(@PathVariable Long idPatio, @Valid @RequestBody PatiosDTO dto) {
        return ResponseEntity.ok(patioService.updatePatio(idPatio, dto));
    }

    //Método GET para paginação dos pátios
    @GetMapping
    public ResponseEntity<Page<PatiosDTO>> getAllPatios(Pageable pageable) {
        return ResponseEntity.ok(patioService.getAllPatios(pageable));
    }
    //Método GET para exibir um único pátio através do ID
    @GetMapping("/{idPatio}")
    public ResponseEntity<PatiosDTO> getPatioById(@PathVariable Long idPatio) {
        return ResponseEntity.ok(patioService.getPatioById(idPatio));
    }

    //Deletar um pátio por ID
    @DeleteMapping("/{idPatio}")
    public ResponseEntity<String> deletePatio(@PathVariable Long idPatio) {
        patioService.deletePatio(idPatio);
        return ResponseEntity.ok("Pátio com ID " + idPatio + " foi deletado com sucesso!");
    }
}