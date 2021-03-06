package com.store.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.amazonaws.services.licensemanager.model.AuthorizationException;
import com.store.domain.ItemPedido;
import com.store.domain.PagamentoComBoleto;
import com.store.domain.Pedido;
import com.store.domain.Produto;
import com.store.domain.Usuario;
import com.store.domain.enums.EstadoPagamento;
import com.store.repositories.EstoqueRepository;
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
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository repoPag;
	
	@Autowired
	private EstoqueRepository estoqueRepository;
	
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
	
	@Transactional
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
		for(ItemPedido ip : obj.getItens()) {
			Produto prod = produtoService.find(ip.getProduto().getId());
			if(ip.getQuantidade() <= prod.getEstoque().getQuantidade()) {
				estoqueRepository.subtraiQuantidadeEstoqqeProduto(ip.getProduto().getId(), ip.getQuantidade());
				ip.setDesconto(0.0);
				ip.setProduto(produtoService.find(ip.getProduto().getId()));
				ip.setPreco(ip.getProduto().getPreco());
				ip.setPedido(obj);
			}else {
				throw new ObjectNotFoundException(
						"Não possui quantidade suficiente em estoque! Id do Produto: " + ip.getProduto().getId() + ", Tipo: " + Pedido.class.getName());
			}
			
		}
		obj = repo.save(obj);
		repoPag.save(obj.getPagamento());
		repoItemPedido.saveAll(obj.getItens());
		return obj;
	}
	
	public List<Pedido> findAll(){
		return repo.findAll();
	}
	
	public List<Pedido> findAllUser(Integer id){
		List<Pedido> pedidosUser = repo.findAllByUsuario(UsuarioService.find(id));
		return pedidosUser;
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
	
	@Transactional
	public void alterarEstado(Integer estado, Integer idPedido) {
		pagamentoRepository.alterarEstado(estado, idPedido);
	}
}
