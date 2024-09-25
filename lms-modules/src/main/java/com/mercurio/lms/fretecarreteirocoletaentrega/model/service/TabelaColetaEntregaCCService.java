package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.AdiantamentoTrecho;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntregaCC;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.TabelaColetaEntregaCCDAO;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.fretecarreteirocoletaentrega.tabelaColetaEntregaCCService"
 */
public class TabelaColetaEntregaCCService extends CrudService<TabelaColetaEntregaCC, Long> {

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setFaixaPesoParcelaTabelaCEDAO(TabelaColetaEntregaCCDAO faixaPesoParcelaTabelaCEDAO) {
		setDao(faixaPesoParcelaTabelaCEDAO);
	}

	private TabelaColetaEntregaCCDAO getFaixaPesoParcelaTabelaCEDAO() {
		return (TabelaColetaEntregaCCDAO) getDao();
	}

	public List<TabelaColetaEntregaCC> findByIdContoleCarga(Long idControleCarga) {
		return getFaixaPesoParcelaTabelaCEDAO().findByIdContoleCarga(idControleCarga);
	}

	public TabelaColetaEntregaCC findOneByIdControleCarga(Long idControleCarga) {
		List<TabelaColetaEntregaCC> result = getFaixaPesoParcelaTabelaCEDAO().findByIdControleCarga(idControleCarga);
		if (CollectionUtils.isNotEmpty(result)) {
			return result.get(0);
		}
		return null;
	}
	
	public List<TabelaColetaEntregaCC> findByIdControleCarga(Long idControleCarga) {
		return getFaixaPesoParcelaTabelaCEDAO().findByIdControleCarga(idControleCarga);
	}

    public void removeByIdControleCarga(Long idControleCarga) {
    	getFaixaPesoParcelaTabelaCEDAO().removeByIdControleCarga(idControleCarga);
}

}
