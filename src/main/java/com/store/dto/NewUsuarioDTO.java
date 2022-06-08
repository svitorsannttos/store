package com.store.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.store.services.validation.UsuarioInsert;

@UsuarioInsert
public class NewUsuarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	
	@NotBlank(message="Preenchimento obrigatorio")
	@Length(min=5, max=80, message="O tamanho deve ser entre 5 e 80 caracteres")
	private String nome;
	
	@NotBlank(message="Preenchimento obrigatorio")
	@Email(message = "E-mail inválido")
	private String email;
	
	@NotBlank(message="Preenchimento obrigatorio")
	private String cpfOuCnpj;
	
	@NotNull
	private Integer tipo;
	
	@NotBlank(message="Preenchimento obrigatorio")
	private String senha;

	@NotBlank(message="Preenchimento obrigatorio")
	private String logradouro;
	
	@NotBlank(message="Preenchimento obrigatorio")
	private String numero;
	
	private String complemento;
	
	@NotBlank(message="Preenchimento obrigatorio")
	private String bairro;
	
	@NotBlank(message="Preenchimento obrigatorio")
	private String cep;
	
	@NotBlank(message="Preenchimento obrigatorio")
	private String estado;
	
	@NotBlank(message="Preenchimento obrigatorio")
	private String cidade;

	@NotBlank(message="Preenchimento obrigatorio")
	private String telefone1;
	private String telefone2;
	private String telefone3;

	public String getTelefone1() {
		return telefone1;
	}

	public void setTelefone1(String telefone1) {
		this.telefone1 = telefone1;
	}

	public String getTelefone2() {
		return telefone2;
	}

	public void setTelefone2(String telefone2) {
		this.telefone2 = telefone2;
	}

	public String getTelefone3() {
		return telefone3;
	}

	public void setTelefone3(String telefone3) {
		this.telefone3 = telefone3;
	}

	public NewUsuarioDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpfOuCnpj() {
		return cpfOuCnpj;
	}

	public void setCpfOuCnpj(String cpfOuCnpj) {
		this.cpfOuCnpj = cpfOuCnpj;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}