package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contratacaoveiculos.model.EixosTipoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.dao.EixosTipoMeioTransporteDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contratacaoveiculos.eixosTipoMeioTransporteService"
 */
public class EixosTipoMeioTransporteService extends CrudService<EixosTipoMeioTransporte, Long> {

	/**
	 * Recupera uma inst�ncia de <code>EixosTipoMeioTransporte</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public EixosTipoMeioTransporte findById(java.lang.Long id) {
		return (EixosTipoMeioTransporte)super.findById(id);
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
	public java.io.Serializable store(EixosTipoMeioTransporte bean) {
		return super.store(bean);
	}

	/**
	 * Consulta todos os eixos de um tipo de meio de transporte.
	 * Muito utilizado em combos de eixos do tipo do meio de transporte.
	 * @param idTipoMeioTransporte
	 * @return Lista de pojos de EixosTipoMeioTransporte
	 */
	public List findEixosByTpMeioTransp(Long idTipoMeioTransporte) {
		return getEixosTipoMeioTransporteDAO().findEixosByTpMeioTransp(idTipoMeioTransporte);
	}

	/**
	 * Consulta a soma dos eixos de um tipo de meio de transporte e seu composto.
	 * 
	 * @param idTipoMeioTransporte
	 * @return Lista de Integer com a quantidade de Eixos do TipoMeioTransporte
	 */
	public List findSumEixosByTpMeioTransp(Long idTipoMeioTransporte) {
		return getEixosTipoMeioTransporteDAO().findSumEixosByTpMeioTransp(idTipoMeioTransporte);
	}

	public List findByTpMeioTranpAndNrEixos(Long idTipoMeioTransporte, Integer nrEixos) {
		return getEixosTipoMeioTransporteDAO().findByTpMeioTranpAndNrEixos(idTipoMeioTransporte,nrEixos);
	}

	public EixosTipoMeioTransporte findUniqueByTpMeioTranpAndNrEixos(Long idTipoMeioTransporte, Integer nrEixos) {
		List rs = getEixosTipoMeioTransporteDAO().findByTpMeioTranpAndNrEixos(idTipoMeioTransporte,nrEixos);
		if (rs.size() == 0)
			return null;
		return (EixosTipoMeioTransporte)rs.get(0);
	}

	public List findSumEixosAllMeioTransportes() {
		return getEixosTipoMeioTransporteDAO().findSumEixosAllMeioTransportes();
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private EixosTipoMeioTransporteDAO getEixosTipoMeioTransporteDAO() {
		return (EixosTipoMeioTransporteDAO) getDao();
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setEixosTipoMeioTransporteDAO(EixosTipoMeioTransporteDAO dao) {
		setDao( dao );
	}

	public EixosTipoMeioTransporte findByIdMeioTransporte(Long idMeioTransporte){
		return getEixosTipoMeioTransporteDAO().findByByIdMeioTransporte(idMeioTransporte);
	}
}
