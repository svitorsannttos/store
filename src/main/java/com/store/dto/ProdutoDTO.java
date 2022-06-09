package com.store.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.store.domain.Categoria;

public class ProdutoDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	@NotBlank(message="Preenchimento obrigatorio")
	@Length(min=2, max=50, message="O tamanho deve ser entre 2 e 50 caracteres")
	private String nome;
	@NotBlank(message="Preenchimento obrigatorio")
	@Length(min=2, max=50, message="O tamanho deve ser entre 2 e 50 caracteres")
	private String marca;
	@NotBlank(message="Preenchimento obrigatorio")
	@Length(min=2, max=50, message="O tamanho deve ser entre 2 e 50 caracteres")
	private String modelo;
	@NotNull
	private Double preco;
	@NotBlank(message="Preenchimento obrigatorio")
	private String tamanho;
	@NotNull
	private Integer quantidade;
	
	private String informacoesTecnicas;

	private Categoria categoria;
	
	public ProdutoDTO() {
	}

	public ProdutoDTO(Integer id, String nome, String marca, String modelo, Double preco, String tamanho, Integer quantidade, 
			String informacoesTecnicas, Categoria categoria) {
		super();
		this.id = id;
		this.nome = nome;
		this.marca = marca;
		this.modelo = modelo;
		this.preco = preco;
		this.tamanho = tamanho;
		this.quantidade = quantidade;
		this.informacoesTecnicas = informacoesTecnicas;
		this.categoria = categoria;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public String getTamanho() {
		return tamanho;
	}

	public void setTamanho(String tamanho) {
		this.tamanho = tamanho;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public String getInformacoesTecnicas() {
		return informacoesTecnicas;
	}

	public void setInformacoesTecnicas(String informacoesTecnicas) {
		this.informacoesTecnicas = informacoesTecnicas;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
