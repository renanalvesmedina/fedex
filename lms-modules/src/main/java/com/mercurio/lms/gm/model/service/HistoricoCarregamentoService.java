package com.mercurio.lms.gm.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.HistoricoCarregamento;
import com.mercurio.lms.gm.model.dao.HistoricoCarregamentoDAO;

/**
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
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
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setHistoricoCarregamentoDAO(HistoricoCarregamentoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private HistoricoCarregamentoDAO getHistoricoCarregamentoDAO() {
		return (HistoricoCarregamentoDAO) getDao();
	}

}
