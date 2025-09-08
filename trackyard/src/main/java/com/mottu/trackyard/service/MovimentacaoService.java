package com.mottu.trackyard.service;

import com.mottu.trackyard.dto.MovimentacoesDTO;
import com.mottu.trackyard.entity.Motos;
import com.mottu.trackyard.entity.Movimentacoes;
import com.mottu.trackyard.entity.PontosLeitura;
import com.mottu.trackyard.repository.MotosRepository;
import com.mottu.trackyard.repository.MovimentacoesRepository;
import com.mottu.trackyard.repository.PontosLeituraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimentacaoService {

    private final MotosRepository motosRepository;
    private final MovimentacoesRepository movimentacoesRepository;
    private final PontosLeituraRepository pontosLeituraRepository;

    public MovimentacaoService(MotosRepository motosRepository, 
                               MovimentacoesRepository movimentacoesRepository, 
                               PontosLeituraRepository pontosLeituraRepository) {
        this.motosRepository = motosRepository;
        this.movimentacoesRepository = movimentacoesRepository;
        this.pontosLeituraRepository = pontosLeituraRepository;
    }

    //Registra uma movimentação
    public void registrarMovimentacao(MovimentacoesDTO dto) {
        Motos moto = motosRepository.findById(dto.idMoto())
            .orElseThrow(() -> new RuntimeException("Moto não encontrada"));
        PontosLeitura ponto = pontosLeituraRepository.findById(dto.idPonto())
            .orElseThrow(() -> new RuntimeException("Ponto de leitura não encontrado"));

        Movimentacoes movimentacao = new Movimentacoes();
        movimentacao.setMoto(moto);
        movimentacao.setPontoLeitura(ponto);
        movimentacao.setDataHora(dto.dataHora());
        movimentacoesRepository.save(movimentacao);
    }

    //Pagina movimentações por moto
    public Page<MovimentacoesDTO> getHistoricoMoto(String idMoto, Pageable pageable) {
        if (!motosRepository.existsById(idMoto)) {
            throw new RuntimeException("Moto não encontrada");
        }
        Page<Movimentacoes> movimentacoes = movimentacoesRepository.findByMotoIdMoto(idMoto, pageable);
        List<MovimentacoesDTO> dtos = movimentacoes.getContent().stream()
            .map(m -> new MovimentacoesDTO(
                m.getIdMovimentacao(),
                m.getMoto().getIdMoto(),
                m.getPontoLeitura().getIdPonto(),
                m.getDataHora()
            ))
            .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, movimentacoes.getTotalElements());
    }

    //Exibe uma determinada movimentação
    public MovimentacoesDTO getMovimentacaoById(Long idMovimentacao) {
        Movimentacoes movimentacao = movimentacoesRepository.findById(idMovimentacao)
            .orElseThrow(() -> new RuntimeException("Movimentação não encontrada"));
        return new MovimentacoesDTO(
            movimentacao.getIdMovimentacao(),
            movimentacao.getMoto().getIdMoto(),
            movimentacao.getPontoLeitura().getIdPonto(),
            movimentacao.getDataHora()
        );
    }
}