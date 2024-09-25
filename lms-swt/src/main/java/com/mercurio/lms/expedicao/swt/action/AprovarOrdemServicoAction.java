package com.mercurio.lms.expedicao.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.expedicao.model.service.OrdemServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

public class AprovarOrdemServicoAction extends CrudAction {
	private ClienteService clienteService;
	private FilialService filialService;
	private PessoaService pessoaService;

	
	public TypedFlatMap findByIdClienteTomador(Long id) {
		
		TypedFlatMap typedFlatMap = new TypedFlatMap();
		Cliente cliente = clienteService.findById(id);
		typedFlatMap.put("idCliente", cliente.getIdCliente());
		if (cliente.getPessoa() != null) {
			typedFlatMap.put("nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
			typedFlatMap.put("nmPessoa", cliente.getPessoa().getNmPessoa());
		}
		return typedFlatMap;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> findFiliaisUsuario() {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Filial> filiais = SessionUtils.getFiliaisUsuarioLogado();
		if (filiais != null) {
			Filial filialLogada =  SessionUtils.getFilialSessao();
			Map<String, Object> mapFilialLogada = new HashMap<String, Object>();
			mapFilialLogada.put("sgFilial", filialLogada.getSgFilial());
			mapFilialLogada.put("idFilial", filialLogada.getIdFilial());
			Pessoa pessoa = pessoaService.findById(filialLogada.getPessoa().getIdPessoa());
			mapFilialLogada.put("nmFantasia", pessoa.getNmFantasia());
			result.put("filialLogada", mapFilialLogada);
			
			if (filiais.size() > 1) {
				List<Long> listaFiliais = new ArrayList<Long>();
				for (Filial filial : filiais) {
					listaFiliais.add(filial.getIdFilial());
				}
				result.put("listaFiliais", listaFiliais);
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findLookupFilial(TypedFlatMap criteria) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Filial> filiais = filialService.findLookup(criteria);
		if (filiais != null) {
			for (Filial filial : filiais) {
				Map<String, Object> mapFilial = new HashMap<String, Object>();
				mapFilial.put("sgFilial", filial.getSgFilial());
				mapFilial.put("idFilial", filial.getIdFilial());
				mapFilial.put("nmFantasia", filial.getPessoa().getNmFantasia());
				result.add(mapFilial);
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked" )
	public List<Map<String, Object>> findLookupCliente(Map<String, Object> criteria){
		Map<String, Object> pessoa = new HashMap<String, Object>();
		pessoa.put("nrIdentificacao", criteria.get("nrIdentificacao"));
		criteria.put("pessoa", pessoa);
		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>();
		List<Cliente> clientes = clienteService.findLookup(criteria);
		for (Cliente cliente : clientes) {
			Map<String, Object> cli = new HashMap<String, Object>(); 
			cli.put("idCliente", cliente.getIdCliente());
			if (cliente.getPessoa() != null) {
				cli.put("nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
				cli.put("nmPessoa", cliente.getPessoa().getNmPessoa());
			}
			retorno.add(cli);
		}
		return retorno;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResultSetPage<Map<String, Object>> findPaginated(Map criteria) {
		return getOrdemServicoService().findPaginatedAprovacao(new PaginatedQuery(criteria));
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		return getOrdemServicoService().getRowCountAprovacao(criteria);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResultSetPage<Map<String, Object>> findPaginatedAprovacaoOrdens(Map criteria) {
		return getOrdemServicoService().findPaginatedAprovacaoOrdens(new PaginatedQuery(criteria));
	}
	
	public Integer getRowCountAprovacaoOrdens(TypedFlatMap criteria) {
		return getOrdemServicoService().getRowCountAprovacaoOrdens(criteria);
	}
	
	private OrdemServicoService getOrdemServicoService() {
		return (OrdemServicoService)this.defaultService;
	}
	
	public void setOrdemServicoService(OrdemServicoService ordemServicoService) {
		this.defaultService = ordemServicoService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
}
