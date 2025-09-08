package com.mottu.trackyard.repository;

import com.mottu.trackyard.entity.Motos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MotosRepository extends JpaRepository<Motos, String> {
    Motos findByPlaca(String placa);
    boolean existsByPlaca(String placa);
}