package com.mercurio.lms.vendas.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.CampanhaMarketing;
import com.mercurio.lms.vendas.model.dao.CampanhaMarketingDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.campanhaMarketingService"
 */
public class CampanhaMarketingService extends CrudService<CampanhaMarketing, Long> {

	/**
	 * Recupera uma inst�ncia de <code>CampanhaMarketing</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public CampanhaMarketing findById(java.lang.Long id) {
		return (CampanhaMarketing)super.findById(id);
	}

	/**
	 * Recupera lista de campanhas de marketing ordenado pela descricao
	 * 
	 * @param parameters
	 * @return
	 */
	public List findCampanhaMarketingOrderByDsCampanhaMarketing(Map parameters) {
		List campoOrdenacao = new ArrayList();
		campoOrdenacao.add("dsCampanhaMarketing");

		if(parameters == null)
			parameters = new HashMap();

		List campanhas = getDao().findListByCriteria(parameters, campoOrdenacao);
		return campanhas;
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
	public java.io.Serializable store(CampanhaMarketing bean) {
		bean.setEmpresa(SessionUtils.getEmpresaSessao());
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setCampanhaMarketingDAO(CampanhaMarketingDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private CampanhaMarketingDAO getCampanhaMarketingDAO() {
		return (CampanhaMarketingDAO) getDao();
	}

	public ResultSetPage findPaginated(Map criteria) {		
		return getCampanhaMarketingDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
	}
	
	
	public Integer getRowCount(Map criteria) {		
		return getCampanhaMarketingDAO().getRowCount(criteria);
	}

}
