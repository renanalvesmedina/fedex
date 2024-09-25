package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.EtapaVisita;
import com.mercurio.lms.vendas.model.dao.EtapaVisitaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.etapaVisitaService"
 */
public class EtapaVisitaService extends CrudService<EtapaVisita, Long> {
	
	/**
	 * Recupera uma Lista de EtapaVisita a partir do ID_VISITA
	 * @param idVisita
	 * @return
	 */
	public List findEtapaVisitasByVisita(Long idVisita){
		return getEtapaVisitaDAO().findEtapaVisitasByVisita(idVisita);
	}
	
	public Integer getRowCountEtapaVisitasByVisita(Long idVisita){
		return getEtapaVisitaDAO().getRowCountEtapaVisitasByVisita(idVisita);
	}

	/**
	 * Recupera uma inst�ncia de <code>EtapaVisita</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public EtapaVisita findById(java.lang.Long id) {
		return (EtapaVisita)super.findById(id);
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
	 * Apaga v�rias entidades atrav�s do IdVisita
	 */
	public void removeByIdVisita(Long idVisita){
		getEtapaVisitaDAO().removeByIdVisita(idVisita);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(EtapaVisita bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setEtapaVisitaDAO(EtapaVisitaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private EtapaVisitaDAO getEtapaVisitaDAO() {
		return (EtapaVisitaDAO) getDao();
	}

}
