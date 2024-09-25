package com.mercurio.lms.expedicao.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.expedicao.model.Dimensao;
import com.mercurio.lms.expedicao.model.dao.DimensaoDAO;
import com.mercurio.lms.vendas.util.VendasUtils;

/**
 * Classe de servi�o para CRUD:
 * 
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.expedicao.dimensaoService"
 */
public class DimensaoService extends CrudService<Dimensao, Long> {

	/**
	 * Recupera uma inst�ncia de <code>Dimensao</code> a partir do ID.
	 * 
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public Dimensao findById(java.lang.Long id) {
		return (Dimensao) super.findById(id);
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contr�rio.
	 * 
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(Dimensao bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos
	 * dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setDimensaoDAO(DimensaoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia
	 * dos dados deste servi�o.
	 * 
	 * @return Inst�ncia do DAO.
	 */
	private DimensaoDAO getDimensaoDAO() {
		return (DimensaoDAO) getDao();
	}

	public List findIdsByIdConhecimento(Long idConhecimento) {
		return getDimensaoDAO().findIdsByIdConhecimento(idConhecimento);
	}

	public List findByIdAwb(Long idAwb) {
		return getDimensaoDAO().findByIdAwb(idAwb);
	}

	public Integer getRowCountByIdCotacao(Long idCotacao) {
		return getDimensaoDAO().getRowCountByIdCotacao(idCotacao);
	}

	public ResultSetPage findPaginatedByIdCotacao(Long idCotacao, Map criteria) {
		FindDefinition def = FindDefinition.createFindDefinition(criteria);
		return getDimensaoDAO().findPaginatedByIdCotacao(idCotacao, def);
	}

	public ResultSetPage findPaginatedByCotacaoSession() {
		List dim = findByCotacaoSession();
		return new ResultSetPage(1,dim);
	}

	public List findByIdCtoInternacional(Long idCtoInternacional){
		return getDimensaoDAO().findByIdCtoInternacional(idCtoInternacional);
	}

	public ResultSetPage findPaginatedByIdCtoInternacional(Long idCtoInternacional, Map criteria) {
		FindDefinition def = FindDefinition.createFindDefinition(criteria);
		return getDimensaoDAO().findPaginatedByIdCtoInternacional(idCtoInternacional, def);
	}

	public Integer getRowCountByIdCtoInternacional(Long idCtoInternacional) {
		return getDimensaoDAO().getRowCountByIdCtoInternacional(idCtoInternacional);
	}

	public Integer getRowCountByIdAwb(Long idAwb) {
		return getDimensaoDAO().getRowCountByIdAwb(idAwb);
	}

	public List findPaginatedByIdAwb(Long idAwb) {
		return getDimensaoDAO().findPaginatedByIdAwb(idAwb);
	}

	public void removeByIdAwb(Long idAwb) {
		getDimensaoDAO().removeByIdAwb(idAwb);
	}

	public List findByIdCotacao(Long id){
		return getDimensaoDAO().findByIdCotacao(id);
	}

	public List findByCotacaoSession(){
		return VendasUtils.getCotacaoInSession().getDimensoes();
	}
}