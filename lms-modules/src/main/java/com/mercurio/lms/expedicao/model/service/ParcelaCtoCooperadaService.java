package com.mercurio.lms.expedicao.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.ParcelaCtoCooperada;
import com.mercurio.lms.expedicao.model.dao.ParcelaCtoCooperadaDAO;
import com.mercurio.lms.sim.model.dao.LMParceriaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.parcelaCtoCooperadaService"
 */
public class ParcelaCtoCooperadaService extends CrudService<ParcelaCtoCooperada, Long> {
	private LMParceriaDAO lmParceriaDao;

	public void setLmParceriaDao(LMParceriaDAO lmParceriaDao) {
		this.lmParceriaDao = lmParceriaDao;
	}

	/**
	 * Recupera uma inst�ncia de <code>ParcelaCtoCooperada</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ParcelaCtoCooperada findById(java.lang.Long id) {
		return (ParcelaCtoCooperada)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ParcelaCtoCooperada bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setParcelaCtoCooperadaDAO(ParcelaCtoCooperadaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ParcelaCtoCooperadaDAO getParcelaCtoCooperadaDAO() {
		return (ParcelaCtoCooperadaDAO) getDao();
	}

	public List findPaginatedDadosFrete(Long idDoctoServico){
		return lmParceriaDao.findPaginatedDadosFrete(idDoctoServico);
	}

	public Map findVlFreteDadosFrete(Long idDoctoServico){
		return lmParceriaDao.findVlFreteDadosFrete(idDoctoServico);
	}
}