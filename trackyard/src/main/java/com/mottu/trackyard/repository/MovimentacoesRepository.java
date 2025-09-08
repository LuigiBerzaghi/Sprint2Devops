package com.mottu.trackyard.repository;

import com.mottu.trackyard.entity.Movimentacoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovimentacoesRepository extends JpaRepository<Movimentacoes, Long> {
    List<Movimentacoes> findByMotoIdMoto(String idMoto); 
    Page<Movimentacoes> findByMotoIdMoto(String idMoto, Pageable pageable); 
}