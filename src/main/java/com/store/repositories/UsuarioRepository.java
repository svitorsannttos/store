package com.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.store.domain.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{

	@org.springframework.transaction.annotation.Transactional(readOnly = true)
	Usuario findByEmail(String email);
	
}
