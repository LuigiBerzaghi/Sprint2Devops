package com.mottu.trackyard.entity;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "Motos")
@Data
public class Motos {
	
	//Atributos
    @Id
  //@NotBlank(message = "ID da moto é obrigatório")
    @Size(max = 50, message = "ID da moto deve ter no máximo 50 caracteres")
    @Column(name = "id_moto", length = 50)
    private String idMoto;

    @NotBlank(message = "Modelo é obrigatório")
    @Size(max = 100, message = "Modelo deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String modelo;

    @NotBlank(message = "Placa é obrigatória")
    @Size(max = 10, message = "Placa deve ter no máximo 10 caracteres")
    @Column(nullable = false, length = 10, unique = true)
    private String placa;

    @OneToMany(mappedBy = "moto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movimentacoes> movimentacoes;

  //Getters e settes gerados manualmente pois o lombok não funcionou
  	public String getIdMoto() {
  		return idMoto;
  	}

  	public void setIdMoto(String idMoto) {
  		this.idMoto = idMoto;
  	}

  	public String getModelo() {
  		return modelo;
  	}

  	public void setModelo(String modelo) {
  		this.modelo = modelo;
  	}

  	public String getPlaca() {
  		return placa;
  	}

  	public void setPlaca(String placa) {
  		this.placa = placa;
  	}
}