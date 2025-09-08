package com.mottu.trackyard.repository;

import com.mottu.trackyard.entity.PontosLeitura;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PontosLeituraRepository extends JpaRepository<PontosLeitura, Long> {
    List<PontosLeitura> findByPatioIdPatio(Long idPatio);
    Page<PontosLeitura> findByPatioIdPatio(Long idPatio, Pageable pageable);
    boolean existsById(Long idPonto);
}