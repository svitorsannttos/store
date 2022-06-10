package com.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.store.domain.Estoque;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Integer>{

	@Modifying()
	@Query("UPDATE Estoque SET quantidade = (quantidade - :quantidade) WHERE id_produto = :idProduto")
	void subtraiQuantidadeEstoqqeProduto(@Param("idProduto") Integer idProduto, @Param("quantidade") Integer quantidade);

}
