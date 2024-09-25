package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.vendas.model.ClienteOperadorLogistico;
import com.mercurio.lms.vendas.model.dao.ClienteOperadorLogisticoDAO;

/**
 * @spring.bean id="lms.vendas.clienteOperadorLogisticoService"
 */
public class ClienteOperadorLogisticoService extends CrudService<ClienteOperadorLogistico, Long>{
	
	private ClienteOperadorLogisticoDAO clienteOperadorLogisticoDAO;
	private UsuarioService usuarioService;
	
	@Override
	protected Serializable store(ClienteOperadorLogistico bean) {
		if (bean.getClienteOperado().getIdCliente().equals(bean.getClienteOperador().getIdCliente())){
			throw new BusinessException("LMS-01208");
		}
	    return super.store(bean);
	}
	
	public void removeByIdClienteOperador(Long idClienteOperador){
		clienteOperadorLogisticoDAO.removeByIdClienteOperador(idClienteOperador);
	}
	
	public List<ClienteOperadorLogistico> findByIdClienteOperado(Long idClienteOperado){
		return clienteOperadorLogisticoDAO.findByIdClienteOperado(idClienteOperado);
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {
	    return super.findPaginated(criteria);
	}
	
	

	public void setClienteOperadorLogisticoDAO(
            ClienteOperadorLogisticoDAO clienteOperadorLogisticoDAO) {
    	this.clienteOperadorLogisticoDAO = clienteOperadorLogisticoDAO;
    	setDao(clienteOperadorLogisticoDAO);
    }

	
	public Boolean validateAcessoFilial(Long idFilial){
		return usuarioService.validateAcessoFilialRegionalUsuario(idFilial);
	}

	public void setUsuarioService(UsuarioService usuarioService) {
    	this.usuarioService = usuarioService;
    }
	
}
