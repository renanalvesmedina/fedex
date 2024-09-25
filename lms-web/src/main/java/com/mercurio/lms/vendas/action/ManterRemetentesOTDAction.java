package com.mercurio.lms.vendas.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.RemetenteOTD;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.RemetenteOTDService;
import com.mercurio.lms.vendas.model.service.TipoAgrupamentoService;

public class ManterRemetentesOTDAction extends CrudAction {

	private ClienteService clienteService;
	private RemetenteOTDService remetenteOTDService;
	
	public List findLookupCliente(Map criteria) {
		return clienteService.findLookup(criteria);
	}

	public ResultSetPage findPaginatedManterRemetentesOTD(TypedFlatMap criteria) {

		// teste bagaceiro para ver se o dao com criteria funciona.
		// List<RemetenteOTD> findByZembrzuskiCriteria = remetenteOTDService.findByZembrzuskiCriteria();

		ResultSetPage results = remetenteOTDService.findPaginated(criteria);
		
		return results;
    }

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public RemetenteOTDService getRemetenteOTDService() {
		return remetenteOTDService;
	}

	public void setRemetenteOTDService(RemetenteOTDService remetenteOTDService) {
		this.remetenteOTDService = remetenteOTDService;
	}

	public void setRemetenteOTD(RemetenteOTDService remetenteOTDService) {
		this.defaultService = remetenteOTDService;
	}

	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		remetenteOTDService.removeByIds(ids);
	}

	public TypedFlatMap store(TypedFlatMap flatBean) {
		
		System.out.println("ae");
		
		Long clienteId = flatBean.getLong("cliente.idCliente");
		Long remetenteId = flatBean.getLong("remetente.idCliente");

		if (remetenteId == null) {
			throw new BusinessException("Não pôde salvar pq remetente tem de estar preenchido");
		}
		
		
		
		Cliente cliente = clienteService.findById(clienteId);
		Cliente remetente = clienteService.findById(remetenteId);
		
		RemetenteOTD remetenteOTD = new RemetenteOTD(cliente, remetente);

		List<RemetenteOTD> list = new ArrayList<RemetenteOTD>();
		list.add(remetenteOTD);
		
		remetenteOTDService.storeAll(list);
		
		
		System.out.println("ae");
		
		return null;
	}
	
}
