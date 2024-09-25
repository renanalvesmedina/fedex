package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.ClienteOperadorLogistico;
import com.mercurio.lms.vendas.model.service.ClienteOperadorLogisticoService;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * @spring.bean id="lms.vendas.manterClienteOperadorLogisticoAction"
 */
public class ManterClienteOperadorLogisticoAction extends CrudAction {
	private ClienteService clienteService;
	private ClienteOperadorLogisticoService clienteOperadorLogisticoService;
	
	public Serializable store(TypedFlatMap data){
		ClienteOperadorLogistico clienteOperadorLogistico = null;
		if (data.getBoolean("idClienteOperadorLogistico")!= null){
			clienteOperadorLogistico = (ClienteOperadorLogistico)findById(data.getLong("idClienteOperadorLogistico"));
		}else {
			clienteOperadorLogistico = new ClienteOperadorLogistico();
		}
		
		Cliente clienteOperador = clienteService.findById(data.getLong("cliente.idCliente"));
		clienteOperadorLogistico.setClienteOperador(clienteOperador);
		Cliente clienteOperado = clienteService.findById(data.getLong("clienteOperado.idCliente"));
		clienteOperadorLogistico.setClienteOperado(clienteOperado);
		return store(clienteOperadorLogistico);
	}
	
	public Boolean validateAcessoFilial(Long idFilial){
		return clienteOperadorLogisticoService.validateAcessoFilial(idFilial);
	}
	
	public ClienteOperadorLogistico findById(Long id){
		return (ClienteOperadorLogistico) super.findById(id);
	}
	
	public void removeById(Long id) {
	    super.removeById(id);
	}
	
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
	    super.removeByIds(ids);
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		/*
		 * correção no criteria pois na tela é necessario que o property usado seja 'cliente'
		 * poder interagir com a tela principal de clientes.
		 */
		criteria.put("clienteOperador", criteria.get("cliente"));
	    return super.findPaginated(criteria);
	}

	public void setClienteService(ClienteService clienteService) {
    	this.clienteService = clienteService;
    }

	public void setClienteOperadorLogisticoService(
            ClienteOperadorLogisticoService clienteOperadorLogisticoService) {
    	this.clienteOperadorLogisticoService = clienteOperadorLogisticoService;
    	this.setDefaultService(clienteOperadorLogisticoService);
    }
	
	
}
