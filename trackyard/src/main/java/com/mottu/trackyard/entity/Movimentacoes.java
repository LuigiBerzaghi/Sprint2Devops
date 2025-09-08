package com.mottu.trackyard.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "Movimentacoes")
@Data
public class Movimentacoes {
	
	//Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimentacao")
    private Long idMovimentacao;

    @ManyToOne
    @JoinColumn(name = "id_moto")
    private Motos moto;

    @ManyToOne
    @JoinColumn(name = "id_ponto")
    private PontosLeitura pontoLeitura;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;
    
    //Getters e settes gerados manualmente pois o lombok não funcionou
	public Long getIdMovimentacao() {
		return idMovimentacao;
	}

	public void setIdMovimentacao(Long idMovimentacao) {
		this.idMovimentacao = idMovimentacao;
	}

	public Motos getMoto() {
		return moto;
	}

	public void setMoto(Motos moto) {
		this.moto = moto;
	}

	public PontosLeitura getPontoLeitura() {
		return pontoLeitura;
	}

	public void setPontoLeitura(PontosLeitura pontoLeitura) {
		this.pontoLeitura = pontoLeitura;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public void setDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}
    
    
}