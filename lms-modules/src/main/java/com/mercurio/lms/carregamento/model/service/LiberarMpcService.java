package com.mercurio.lms.carregamento.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.LiberaMpc;
import com.mercurio.lms.carregamento.model.dao.LiberarMpcDAO;

/**
 * Classe de servi�o para o Liberar do MPC. Atende a demanda LMS-2791
 * 
 * @author mxavier@voiza.com.br
 * @spring.bean id="lms.carregamento.liberarMPCService"
 */
public class LiberarMpcService extends CrudService<LiberaMpc, Long> {

	public Serializable findById(Long id) {
		return super.findById(id);
	}

	@Override
	public List find(Map criteria) {
		return super.find(criteria);
	}
	
	/**
	 * Apaga a entidade atrav�s do Id
	 * 
	 * @param id
	 * 			indica a entidade que dever� ser removida
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}
	
	/**
	 * Apaga v�rias entidades atrav�s do Id
	 * 
	 * @param ids
	 * 			lista com as entidades que dever� ser removidas
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Servi�o de busca gen�rica para LiberaMPC.
	 *  
	 * @param idLiberaMPC
	 * @param dsLiberaMPC
	 * @param stLibera
	 * @param tpLibera
	 * @param tpAutorizacao
	 * @return
	 */
	public LiberaMpc find(Long idLiberaMPC, String dsLiberaMPC, String stLibera, String tpLibera, String tpAutorizacao) {
		return getLiberarMpcDAO().find(idLiberaMPC, dsLiberaMPC, stLibera, tpLibera, tpAutorizacao);
	}

	/**
	 * Retorna o uma lista de Libera Mpc de acordo com a situa��o
	 * 
	 * @param param
	 * @return
	 */
	public List findLiberarMpcBySituacao(Map param) {
		List<Map> listLiberarMpc = getLiberarMpcDAO().findLiberarMpcBySituacao(param);

		for (Map mapLiberaMpc : listLiberarMpc) {
			mapLiberaMpc.put("stLibera", ((DomainValue) mapLiberaMpc.get("stLibera")).getValue());
			mapLiberaMpc.put("tpAutorizacao", ((DomainValue) mapLiberaMpc.get("tpAutorizacao")).getValue());
			mapLiberaMpc.put("tpLibera", ((DomainValue) mapLiberaMpc.get("tpLibera")).getValue());
		}

		return listLiberarMpc;
	}

	public boolean findExigeAutirizacao(Long idRejeitoMpc) {
		return getLiberarMpcDAO().findExigeAutirizacao(idRejeitoMpc);
	}

	@Override
	public void storeAll(List<LiberaMpc> list) {
		super.storeAll(list);
	}

	@Override
	public Serializable store(LiberaMpc bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste
	 * servi�o.
	 * 
	 * @param Inst�ncia
	 *            do DAO.
	 */
	public void setLiberarMpcDAO(LiberarMpcDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia
	 * dos dados deste servi�o.
	 * 
	 * @return Inst�ncia do DAO.
	 */
	private LiberarMpcDAO getLiberarMpcDAO() {
		return (LiberarMpcDAO) getDao();
	}
	

	

}
