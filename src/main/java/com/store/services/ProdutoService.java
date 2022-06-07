package com.store.services;

import java.net.URI;
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
		Produto prod = new Produto(null, objDto.getNome(), objDto.getPreco(), objDto.getMarca(), objDto.getModelo(), cat);
		Estoque est = new Estoque(null, objDto.getQuantidade(), objDto.getTamanho(), prod);
		repo.save(prod);
		repoEstoque.save(est);
		return prod;
	}
	
	public List<Produto> findAll(){
		return repo.findAll();
	}
	
	public Produto update(Produto obj) {
		Produto prod = find(obj.getId());
		prod.setMarca(obj.getMarca());
		prod.setModelo(obj.getModelo());
		prod.setPreco(obj.getPreco());
		prod.setNome(obj.getNome());
		repoEstoque.save(obj.getEstoque());
		return repo.save(prod);
	}

	public void delete (Integer id){
		find(id);
		try {
			repo.deleteById(id);
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
				categoriaService.find(objDto.getCategoria().getId()));
		
		return prod;
	}
	
}
