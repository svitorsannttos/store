package com.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.store.domain.Pagamento;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer>{
	
	@Modifying()
	@Query("UPDATE Pagamento SET estado = :estado where id = :idpedido")
	void alterarEstado (@Param("estado") Integer estado, @Param("idpedido") Integer idpedido);

}
