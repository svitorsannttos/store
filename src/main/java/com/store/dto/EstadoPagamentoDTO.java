package com.store.dto;

import java.io.Serializable;

public class EstadoPagamentoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer estado;
	private Integer idPedido;
	
	public EstadoPagamentoDTO() {
	}
	
	public EstadoPagamentoDTO(Integer estado, Integer idPedido) {
		super();
		this.estado = estado;
		this.idPedido = idPedido;
	}

	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public Integer getIdPedido() {
		return idPedido;
	}
	public void setIdPedido(Integer idPedido) {
		this.idPedido = idPedido;
	}
	
	
}
