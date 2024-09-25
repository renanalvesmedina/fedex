package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.vendas.model.MunicipioDestinoCalculo;
import com.mercurio.lms.vendas.model.dao.MunicipioDestinoCalculoDAO;

/**
 * Classe de serviço para CRUD:
 *
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.municipioDestinoService"
 */
public class MunicipioDestinoCalculoService extends CrudService<MunicipioDestinoCalculo, Long> {

	public Serializable store(MunicipioDestinoCalculo bean) {
		return super.store(bean);
	}

    public Integer getRowCount(Map criteria) {
		return getDAO().getRowCount(criteria);
    }
    
	public MunicipioDestinoCalculo findById(Long id) {
		return getDAO().findById(id);
	}

	public void setDAO(MunicipioDestinoCalculoDAO dao) {
		setDao(dao);
	}

	public MunicipioDestinoCalculoDAO getDAO() {
		return (MunicipioDestinoCalculoDAO) getDao();
	}

    public void removeById(Long id) {
        super.removeById(id);
    }

	public ResultSetPage<MunicipioDestinoCalculo> findPaginated(PaginatedQuery paginatedQuery) {
		return getDAO().findPaginated(paginatedQuery);
	}

	public List<MunicipioDestinoCalculo> find(Map criteria) {
		return getDAO().find(criteria);
	}

	public boolean validateMunicipioDestino(Map criteria) {
		return getDAO().validateMunicipioDestino(criteria);
	}

	public MunicipioDestinoCalculo findDestinoVigenteByOrigem(Long idMunicipioOrigem) {
		return getDAO().findDestinoVigenteByOrigem(idMunicipioOrigem);
	}
}
