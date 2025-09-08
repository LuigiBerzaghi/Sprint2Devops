package com.mottu.trackyard.controller;

import com.mottu.trackyard.dto.MotosDTO;
import com.mottu.trackyard.dto.MovimentacoesDTO;
import com.mottu.trackyard.service.MotoService;
import com.mottu.trackyard.service.MovimentacaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/motos")
@Validated
public class MotoController {
    private final MotoService motoService;
    private final MovimentacaoService movimentacaoService;

    public MotoController(MotoService motoService, MovimentacaoService movimentacaoService) {
        this.motoService = motoService;
        this.movimentacaoService = movimentacaoService;
    }

    //Método POST para criar uma entidade moto
    @PostMapping
    public ResponseEntity<MotosDTO> createMoto(@Valid @RequestBody MotosDTO dto) {
        MotosDTO created = motoService.createMoto(dto);
        return ResponseEntity.status(201).body(created);
    }

    //Método GET para paginação das motos
    @GetMapping
    public ResponseEntity<Page<MotosDTO>> getAllMotos(Pageable pageable) {
        return ResponseEntity.ok(motoService.getAllMotos(pageable));
    }

    //Método GET para exibir uma única moto através do ID
    @GetMapping("/{idMoto}")
    public ResponseEntity<MotosDTO> getMoto(@PathVariable String idMoto) {
        return ResponseEntity.ok(motoService.getMotoById(idMoto));
    }

    //Atualiza uma moto através do ID
    @PutMapping("/{idMoto}")
    public ResponseEntity<MotosDTO> updateMoto(@PathVariable String idMoto, @Valid @RequestBody MotosDTO dto) {
        return ResponseEntity.ok(motoService.updateMoto(idMoto, dto));
    }

    //Deletar uma moto por ID
    @DeleteMapping("/{idMoto}")
    public ResponseEntity<String> deleteMoto(@PathVariable String idMoto) {
        motoService.deleteMoto(idMoto);
        return ResponseEntity.ok("Moto de ID: " + idMoto + " foi deletada com sucesso!");
    }

    //Método GET para exibir uma única moto através da placa
    @GetMapping("/placa/{placa}")
    public ResponseEntity<MotosDTO> findByPlaca(@PathVariable String placa) {
        return ResponseEntity.ok(motoService.findByPlaca(placa));
    }

    //Método GET para exibir o histórico de movimentações de uma determinada moto através do ID
    @GetMapping("/{idMoto}/historico")
    public ResponseEntity<Page<MovimentacoesDTO>> listarHistorico(@PathVariable String idMoto, Pageable pageable) {
        return ResponseEntity.ok(movimentacaoService.getHistoricoMoto(idMoto, pageable));
    }
}