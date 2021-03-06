package com.store.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.store.domain.Pedido;
import com.store.dto.EstadoPagamentoDTO;
import com.store.security.UserSS;
import com.store.services.PedidoService;
import com.store.services.UserService;
import com.store.services.UsuarioService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/pedidos")
@Api(value="API REST Pedidos")
public class PedidoResource {
	
	@Autowired
	private PedidoService service;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@ApiOperation(value = "Retorna um Pedido")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retornou um pedido."),
		    @ApiResponse(code = 401, message = "Precisa está autenticado para obter a resposta solicitada."),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção."),
		    @ApiResponse(code = 404, message = "O servidor não pode encontrar o recurso solicitado."),
		})
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	public ResponseEntity<Pedido> find(@PathVariable Integer id){
		Pedido obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@ApiOperation(value = "Atualiza o estado de um Pedido")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Atualiza o  estado de um pedido."),
		    @ApiResponse(code = 401, message = "Precisa está autenticado para obter a resposta solicitada."),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção."),
		    @ApiResponse(code = 404, message = "O servidor não pode encontrar o recurso solicitado."),
		})
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/status",method = RequestMethod.POST)
	public ResponseEntity<Pedido> updateStatus(@RequestBody EstadoPagamentoDTO objDto){
		service.alterarEstado(objDto.getEstado(), objDto.getIdPedido());
		return ResponseEntity.ok().build();
	}

	@ApiOperation(value = "Insere um Pedido")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Inseriu um pedido."),
		    @ApiResponse(code = 401, message = "Precisa está autenticado para obter a resposta solicitada."),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção."),
		    @ApiResponse(code = 404, message = "O servidor não pode encontrar o recurso solicitado."),
		})
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Pedido> insert(@Valid @RequestBody Pedido obj) {
		UserSS user = UserService.authenticated();
		obj.setUsuario(usuarioService.find(user.getId()));
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(obj);
	}
	
	@ApiOperation(value = "Retorna uma lista de Pedidos")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Retornou uma lista de pedidos."),
		    @ApiResponse(code = 401, message = "Precisa está autenticado para obter a resposta solicitada."),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção."),
		    @ApiResponse(code = 404, message = "O servidor não pode encontrar o recurso solicitado."),
		})
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Pedido>> findAll() {
		return ResponseEntity.ok().body(service.findAll());
	}
	
	@ApiOperation(value = "Retorna uma lista de Pedidos do usuario")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Retornou uma lista de pedidos do usuario."),
		    @ApiResponse(code = 401, message = "Precisa está autenticado para obter a resposta solicitada."),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção."),
		    @ApiResponse(code = 404, message = "O servidor não pode encontrar o recurso solicitado."),
		})
	@RequestMapping(value = "/user" ,method = RequestMethod.GET)
	public ResponseEntity<List<Pedido>> findAllUser() {
		UserSS user = UserService.authenticated();
		return ResponseEntity.ok().body(service.findAllUser(user.getId()));
	}
	
	@ApiOperation(value = "Retorna uma paginação de Pedidos")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retornou uma paginação de pedidos."),
		    @ApiResponse(code = 401, message = "Precisa está autenticado para obter a resposta solicitada."),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção."),
		    @ApiResponse(code = 404, message = "O servidor não pode encontrar o recurso solicitado."),
		})
	@RequestMapping(value="/page",method=RequestMethod.GET)
	public ResponseEntity<Page<Pedido>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="instante") String orderBy, 
			@RequestParam(value="direction", defaultValue="DESC") String direction) {
		Page<Pedido> list = service.findPage(page, linesPerPage, orderBy, direction);
		return ResponseEntity.ok().body(list);
	}
}
