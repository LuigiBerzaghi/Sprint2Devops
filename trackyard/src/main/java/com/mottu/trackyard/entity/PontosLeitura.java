package com.mottu.trackyard.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Pontos_Leitura")
@Data
public class PontosLeitura {
	
	//Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ponto")
    private Long idPonto;

    @ManyToOne
    @JoinColumn(name = "id_patio", nullable = false)
    private Patios patio;

    @NotBlank(message = "Nome do ponto é obrigatório")
    @Size(max = 50, message = "Nome do ponto deve ter no máximo 50 caracteres")
    @Column(name = "nome_ponto", nullable = false, length = 50)
    private String nomePonto;

    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
    @Column(length = 200)
    private String descricao;

    @OneToMany(mappedBy = "pontoLeitura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movimentacoes> movimentacoes;

    //Getters e settes gerados manualmente pois o lombok não funcionou
	public Long getIdPonto() {
		return idPonto;
	}

	public void setIdPonto(Long idPonto) {
		this.idPonto = idPonto;
	}

	public Patios getPatio() {
		return patio;
	}

	public void setPatio(Patios patio) {
		this.patio = patio;
	}

	public String getNomePonto() {
		return nomePonto;
	}

	public void setNomePonto(String nomePonto) {
		this.nomePonto = nomePonto;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
    
    
}