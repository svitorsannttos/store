package com.store.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.amazonaws.services.licensemanager.model.AuthorizationException;
import com.store.domain.ItemPedido;
import com.store.domain.PagamentoComBoleto;
import com.store.domain.Pedido;
import com.store.domain.Usuario;
import com.store.domain.enums.EstadoPagamento;
import com.store.repositories.ItemPedidoRepository;
import com.store.repositories.PagamentoRepository;
import com.store.repositories.PedidoRepository;
import com.store.security.UserSS;
import com.store.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository repoPag;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private UsuarioService UsuarioService;
	
	@Autowired
	private ItemPedidoRepository repoItemPedido;

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Pedido não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setUsuario(UsuarioService.find(obj.getUsuario().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		repoPag.save(obj.getPagamento());
		for(ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		repoItemPedido.saveAll(obj.getItens());
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		Usuario Usuario =  UsuarioService.find(user.getId());
		return repo.findByUsuario(Usuario, pageRequest);
	}
}
