package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.OrigemPropostaFOB;
import com.mercurio.lms.vendas.model.dao.OrigemPropostaFOBDAO;

/**
 * @spring.bean id="lms.vendas.origemPropostaFOBService"
 */
public class OrigemPropostaFOBService extends CrudService<OrigemPropostaFOB, Long> {

	/**
	 * Busca todas as origens relacionadas a proposta FOB
	 * passada por parametro
	 * 
	 * @param  idProposta
	 * @return List<OrigemPropostaFOB>
	 */
	public List<OrigemPropostaFOB> findFiliaisProposta(Long idProposta){
		return getOrigemPropostaFOBDAO().findFiliaisProposta(idProposta);
	}
	
	@Override
	protected Serializable store(OrigemPropostaFOB bean) {		
		return super.store(bean);
	}
	
	/**
	 * Remove todas as origens da proposta
	 * @param idProposta
	 */
	public void removeAll(Long idProposta){
		
		List<OrigemPropostaFOB> origens = getOrigemPropostaFOBDAO().findFiliaisProposta(idProposta);
		for (OrigemPropostaFOB origem : origens) {
			removeById(origem.getIdOrigemPropostaFOB());
		}
	}
	
	public void setOrigemPropostaFOBDAO(OrigemPropostaFOBDAO dao){
		setDao(dao);
	}
	
	private OrigemPropostaFOBDAO getOrigemPropostaFOBDAO() {
		return (OrigemPropostaFOBDAO) getDao();
	}	
}
