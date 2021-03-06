package com.store.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.store.domain.Pedido;
import com.store.domain.Usuario;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer>{
	
	@Transactional(readOnly=true)
	Page<Pedido> findByUsuario(Usuario usuario, Pageable pageRequest);
	
	@Transactional(readOnly=true)
	List<Pedido> findByUsuario(Usuario usuario);

	@Transactional(readOnly=true)
	List<Pedido> findAllByUsuario(Usuario usuario);

}
