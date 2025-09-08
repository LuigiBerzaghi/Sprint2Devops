package com.mottu.trackyard.service;

import com.mottu.trackyard.dto.PatiosDTO;
import com.mottu.trackyard.dto.PontosLeituraDTO;
import com.mottu.trackyard.entity.Patios;
import com.mottu.trackyard.repository.PatiosRepository;
import com.mottu.trackyard.repository.PontosLeituraRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class PatioService {

    private final PatiosRepository patiosRepository;
    private final PontosLeituraRepository pontosLeituraRepository;

    public PatioService(PatiosRepository patiosRepository, PontosLeituraRepository pontosLeituraRepository) {
		this.patiosRepository = patiosRepository;
		this.pontosLeituraRepository = pontosLeituraRepository;
}

    // CREATE - não usa id do DTO (deixa o JPA gerar)
    public PatiosDTO createPatio(PatiosDTO dto) {
        Patios patio = new Patios();
        // NÃO setar o ID aqui!
        patio.setNome(dto.nome());
        patio.setTelefone(dto.telefone());
        patio.setEndereco(dto.endereco());

        Patios saved = patiosRepository.save(patio); // ID gerado aqui
        return new PatiosDTO(saved.getIdPatio(), saved.getNome(), saved.getTelefone(), saved.getEndereco());
    }

    // UPDATE - usa o id do path; não exige id no DTO
    public PatiosDTO updatePatio(Long idPatio, PatiosDTO dto) {
        Patios patio = patiosRepository.findById(idPatio)
                .orElseThrow(() -> new NoSuchElementException("Pátio não encontrado"));

        
        patio.setNome(dto.nome());
        patio.setTelefone(dto.telefone());
        patio.setEndereco(dto.endereco());

        Patios saved = patiosRepository.save(patio);
        return new PatiosDTO(saved.getIdPatio(), saved.getNome(), saved.getTelefone(), saved.getEndereco());
    }

    // LIST
    public Page<PatiosDTO> getAllPatios(Pageable pageable) {
        return patiosRepository.findAll(pageable)
                .map(p -> new PatiosDTO(p.getIdPatio(), p.getNome(), p.getTelefone(), p.getEndereco()));
    }

    // READ
    public PatiosDTO getPatioById(Long idPatio) {
        Patios p = patiosRepository.findById(idPatio)
                .orElseThrow(() -> new NoSuchElementException("Pátio não encontrado"));
        return new PatiosDTO(p.getIdPatio(), p.getNome(), p.getTelefone(), p.getEndereco());
    }

    // DELETE
    public void deletePatio(Long idPatio) {
        if (!patiosRepository.existsById(idPatio)) {
            throw new NoSuchElementException("Pátio não encontrado");
        }
        patiosRepository.deleteById(idPatio);
    }
    
    public Page<PontosLeituraDTO> getPontosByPatio(Long idPatio, Pageable pageable) {
        if (!patiosRepository.existsById(idPatio)) {
            throw new NoSuchElementException("Pátio não encontrado");
        }
        return pontosLeituraRepository.findByPatioIdPatio(idPatio, pageable)
                .map(p -> new PontosLeituraDTO(
                        p.getIdPonto(),
                        p.getPatio().getIdPatio(),
                        p.getNomePonto(),
                        p.getDescricao()
                ));
    }
}
