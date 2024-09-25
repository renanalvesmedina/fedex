package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.RemetenteOTD;
import com.mercurio.lms.vendas.model.dao.RemetenteOTDDao;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.remetenteOTDService"
 */
public class RemetenteOTDService extends CrudService<RemetenteOTD, Long> {
	
	public RemetenteOTD findById(java.lang.Long id) {
		return (RemetenteOTD)super.findById(id);
	}

	public void setRemetenteOTDDao(RemetenteOTDDao dao) {
		setDao( dao );
	}

	public RemetenteOTDDao getRemetenteOTDDao() {
		return (RemetenteOTDDao) getDao();
	}

	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	// isso aqui funciona :)
	public List<RemetenteOTD> findByClienteRemetente(Long idCliente, Long idRemetente) {
		return getRemetenteOTDDao().findByIdClienteRemetente(idCliente, idRemetente);
	}
	
}