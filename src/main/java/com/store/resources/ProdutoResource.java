package com.store.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.store.domain.Estoque;
import com.store.domain.Produto;
import com.store.dto.ProdutoDTO;
import com.store.services.ProdutoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/produtos")
@Api(value="API REST Produtos")
public class ProdutoResource {
	
	@Autowired
	private ProdutoService service;
	
	@ApiOperation(value = "Retorna uma Produto")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retornou uma produto."),
		    @ApiResponse(code = 401, message = "Precisa está autenticado para obter a resposta solicitada."),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção."),
		    @ApiResponse(code = 404, message = "O servidor não pode encontrar o recurso solicitado."),
		})
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ResponseEntity<Produto> find(@PathVariable Integer id){
		Produto obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@ApiOperation(value = "Insere uma Produto")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Inseriu uma produto."),
		    @ApiResponse(code = 401, message = "Precisa está autenticado para obter a resposta solicitada."),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção."),
		    @ApiResponse(code = 404, message = "O servidor não pode encontrar o recurso solicitado."),
		})
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Produto> insert(@Valid @RequestBody ProdutoDTO objDto) {
		Produto obj = service.insert(objDto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}
	
	@ApiOperation(value = "Atualiza um Produto")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Atualizou um produto."),
		    @ApiResponse(code = 401, message = "Precisa está autenticado para obter a resposta solicitada."),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção."),
		    @ApiResponse(code = 404, message = "O servidor não pode encontrar o recurso solicitado."),
		})
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ProdutoDTO objDto, @PathVariable Integer id) {
		Produto obj = service.fromDTO(objDto);
		obj.setId(id);
		Estoque est = new Estoque(service.find(obj.getId()).getEstoque().getId(), 
				objDto.getQuantidade(), objDto.getTamanho(), obj);
		obj.setEstoque(est);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Deleta um Produto")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Deletou um produto."),
		    @ApiResponse(code = 401, message = "Precisa está autenticado para obter a resposta solicitada."),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção."),
		    @ApiResponse(code = 404, message = "O servidor não pode encontrar o recurso solicitado."),
		})
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Retorna uma lista de Produtos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retornou uma lista de Produtos."),
		    @ApiResponse(code = 401, message = "Precisa está autenticado para obter a resposta solicitada."),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção."),
		    @ApiResponse(code = 404, message = "O servidor não pode encontrar o recurso solicitado."),
		})
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<Produto>> findAll(){
		List<Produto> obj = service.findAll();
		return ResponseEntity.ok().body(obj);
	}
	
	@ApiOperation(value = "Insere uma foto do Produto")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Inseriu uma foto do produto."),
		    @ApiResponse(code = 401, message = "Precisa está autenticado para obter a resposta solicitada."),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção."),
		    @ApiResponse(code = 404, message = "O servidor não pode encontrar o recurso solicitado."),
		})
	@RequestMapping(value="/picture/{id}", method=RequestMethod.POST)
	public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name="file") MultipartFile file, @PathVariable Integer id) {
		URI uri = service.uploadProductPicture(file, id);
		return ResponseEntity.created(uri).build();
	}
}
