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

import com.store.domain.Usuario;
import com.store.dto.NewUsuarioDTO;
import com.store.dto.UsuarioUpdateDTO;
import com.store.security.UserSS;
import com.store.services.UserService;
import com.store.services.UsuarioService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioResource {
	
	@Autowired
	private UsuarioService service;
	
	@ApiOperation(value = "Retorna um Usuario")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retornou um Usuario."),
		    @ApiResponse(code = 401, message = "Precisa está autenticado para obter a resposta solicitada."),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção."),
		    @ApiResponse(code = 404, message = "O servidor não pode encontrar o recurso solicitado."),
		})
	@RequestMapping(value="/find",method=RequestMethod.GET)
	public ResponseEntity<Usuario> find(){
		UserSS user = UserService.authenticated();
		Usuario obj = service.find(user.getId());
		return ResponseEntity.ok().body(obj);
	}

	@ApiOperation(value = "Deleta um Usuario")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Deletou um Usuario."),
		    @ApiResponse(code = 401, message = "Precisa está autenticado para obter a resposta solicitada."),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção."),
		    @ApiResponse(code = 404, message = "O servidor não pode encontrar o recurso solicitado."),
		})
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

	@ApiOperation(value = "Retorna uma lista de Usuarios")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Retornou uma lista de Usuarios."),
		    @ApiResponse(code = 401, message = "Precisa está autenticado para obter a resposta solicitada."),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção."),
		    @ApiResponse(code = 404, message = "O servidor não pode encontrar o recurso solicitado."),
		})
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<Usuario>> findAll(){
		List<Usuario> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@ApiOperation(value = "Insere um Usuario")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Inseriu um usuario."),
		    @ApiResponse(code = 401, message = "Precisa está autenticado para obter a resposta solicitada."),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção."),
		    @ApiResponse(code = 404, message = "O servidor não pode encontrar o recurso solicitado."),
		})
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody NewUsuarioDTO objDto) {
		Usuario obj = service.fromDTO(objDto);
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@ApiOperation(value = "Atualiza um Usuario")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Atualizou um usuario."),
		    @ApiResponse(code = 401, message = "Precisa está autenticado para obter a resposta solicitada."),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção."),
		    @ApiResponse(code = 404, message = "O servidor não pode encontrar o recurso solicitado."),
		})
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody UsuarioUpdateDTO objDto) {
		UserSS user = UserService.authenticated();
		Usuario obj = service.fromUpdateDTO(objDto);
		obj.setId(user.getId());
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Insere uma foto de Perfil para um Usuario")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Inseriu uma foto de perfil para um Usuario."),
		    @ApiResponse(code = 401, message = "Precisa está autenticado para obter a resposta solicitada."),
		    @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso."),
		    @ApiResponse(code = 500, message = "Foi gerada uma exceção."),
		    @ApiResponse(code = 404, message = "O servidor não pode encontrar o recurso solicitado."),
		})
	@RequestMapping(value="/picture", method=RequestMethod.POST)
	public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name="file") MultipartFile file) {
		URI uri = service.uploadProfilePicture(file);
		return ResponseEntity.created(uri).build();
	}
}

