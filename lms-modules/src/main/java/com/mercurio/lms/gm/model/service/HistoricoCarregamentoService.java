package com.mercurio.lms.gm.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.HistoricoCarregamento;
import com.mercurio.lms.gm.model.dao.HistoricoCarregamentoDAO;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.gm.embarqueService"
 */
public class HistoricoCarregamentoService extends CrudService<HistoricoCarregamento, Long>{

	private HistoricoCarregamentoDAO historicoCarregamentoDao;

	public HistoricoCarregamentoDAO getHistoricoCarregamentoDao() {
		return historicoCarregamentoDao;
	}

	public void setHistoricoCarregamentoDao(HistoricoCarregamentoDAO historicoCarregamentoDao) {
		this.historicoCarregamentoDao = historicoCarregamentoDao;
	}
	
	@Override
	public Serializable store(HistoricoCarregamento bean) {
		return super.store(bean);
	}
	

	public Serializable findById(Long id) {
		return super.findById(id);
	}
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setHistoricoCarregamentoDAO(HistoricoCarregamentoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private HistoricoCarregamentoDAO getHistoricoCarregamentoDAO() {
		return (HistoricoCarregamentoDAO) getDao();
	}

}
