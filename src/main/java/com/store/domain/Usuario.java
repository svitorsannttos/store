package com.store.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.store.domain.enums.Perfil;
import com.store.domain.enums.TipoUsuario;

@Entity
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotBlank(message = "Campo obrigatório!")
	private String nome;
	@NotBlank(message = "Campo obrigatório!")
	@Column(unique = true)
	private String email;
	@NotBlank(message = "Campo obrigatório!")
	@JsonIgnore
	private String senha;
	@NotBlank(message = "Campo obrigatório!")
	@Column(unique = true)
	private String cpfOuCnpj;
	@NotNull
	private Integer tipo;
	@NotBlank(message = "Campo obrigatório!")
	private String logradouro;
	@NotBlank(message = "Campo obrigatório!")
	private String numero;
	private String complemento;
	@NotBlank(message = "Campo obrigatório!")
	private String bairro;
	@NotBlank(message = "Campo obrigatório!")
	private String cep;
	@NotBlank(message = "Campo obrigatório!")
	private String estado;
	@NotBlank(message = "Campo obrigatório!")
	private String cidade;
	
	private String imageUrl;

	@ElementCollection
	@CollectionTable(name = "TELEFONE")
	private Set<String> telefones = new HashSet<>();

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "PERFIS")
	private Set<Integer> perfis = new HashSet<>();

	@JsonIgnore
	@OneToMany(mappedBy = "usuario")
	private List<Pedido> pedidos = new ArrayList<>();

	public Usuario() {
		addPerfil(Perfil.CLIENTE);
		this.imageUrl = "https://bethstore.s3.sa-east-1.amazonaws.com/avatar-blank.png";
	}
	
	public Usuario(Integer id, String nome, String email,
			String senha, String cpfOuCnpj, TipoUsuario tipo, 
			String logradouro, String numero, String complemento, 
			String bairro, String cep, String estado, String cidade) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.cpfOuCnpj = cpfOuCnpj;
		this.logradouro = logradouro;
		this.numero = numero;
		this.complemento = complemento;
		this.bairro = bairro;
		this.cep = cep;
		this.estado = estado;
		this.cidade = cidade;
		this.tipo = (tipo == null) ? null : tipo.getCod();
		addPerfil(Perfil.CLIENTE);
		this.imageUrl = "https://bethstore.s3.sa-east-1.amazonaws.com/avatar-blank.png";
	}

	public void addPerfil(Perfil perfil) {
		perfis.add(perfil.getCod());
	}

	public Integer getId() {
		return id;
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

	public void setPerfis(Set<Integer> perfis) {
		this.perfis = perfis;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
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

	public Set<String> getTelefones() {
		return telefones;
	}

	public void setTelefones(Set<String> telefones) {
		this.telefones = telefones;
	}

	public Set<Perfil> getPerfis() {
		return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
}
