package com.store.services;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.store.domain.Categoria;
import com.store.domain.Estoque;
import com.store.domain.Produto;
import com.store.dto.ProdutoDTO;
import com.store.repositories.EstoqueRepository;
import com.store.repositories.ProdutoRepository;
import com.store.services.exceptions.DataIntegrityException;
import com.store.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private EstoqueRepository repoEstoque;
	
	@Autowired
	private CategoriaService categoriaService;
	
	@Autowired
	private S3Service s3Service;
	
	public Produto find(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Produto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
	}
	
	@Transactional
	public Produto insert(ProdutoDTO objDto) {
		Categoria cat = categoriaService.find(objDto.getCategoria().getId());
		Produto prod = new Produto(null, objDto.getNome(), objDto.getPreco(), objDto.getMarca(), objDto.getModelo(), objDto.getInformacoesTecnicas(), cat, null);
		Estoque est = new Estoque(null, objDto.getQuantidade(), objDto.getTamanho(), prod);
		repo.save(prod);
		repoEstoque.save(est);
		return prod;
	}
	
	public List<Produto> findAll(){
		List<Produto> prod = new ArrayList<>();
		for (Produto p : repo.findAll()) {
			if(p.getEstoque().getQuantidade() > 0) {
				prod.add(p);
			}
		}
		return prod;
	}
	
	@Transactional
	public Produto update(Produto obj) {
		Produto prod = find(obj.getId());
		prod.setMarca(obj.getMarca());
		prod.setModelo(obj.getModelo());
		prod.setPreco(obj.getPreco());
		prod.setNome(obj.getNome());
		prod.setInformacoesTecnicas(obj.getInformacoesTecnicas());
		repoEstoque.save(prod.getEstoque());
		return repo.save(prod);
	}

	public void delete (Integer id){
		Produto prod = find(id);
		try {
			repo.delete(prod);
			repoEstoque.delete(prod.getEstoque());
		}
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir porque há entidades relacionadas!");
		}
	}
	
	public URI uploadProductPicture(MultipartFile multipartFile, Integer id) {
		URI uri = s3Service.uploadFile(multipartFile);
		Produto prod = find(id);
		prod.setImageUrl(uri.toString());
		repo.save(prod);
		return uri;
	}
	
	public Produto fromDTO (ProdutoDTO objDto) {
		Produto prod = new Produto(null, objDto.getNome(), objDto.getPreco(), objDto.getMarca(), objDto.getModelo(), 
				objDto.getInformacoesTecnicas() ,categoriaService.find(objDto.getCategoria().getId()), null);
		
		return prod;
	}
	
}
