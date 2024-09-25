package com.mercurio.lms.vendas.model.service;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;
import com.mercurio.lms.vendas.model.VersaoPce;
import com.mercurio.lms.vendas.model.dao.VersaoPceDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.versaoPceService"
 */
public class VersaoPceService extends CrudService<VersaoPce, Long> {

	private VigenciaService vigenciaService;

	/**
	 * Recupera uma inst�ncia de <code>VersaoPce</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public VersaoPce findById(java.lang.Long id) {
		return (VersaoPce)super.findById(id);
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

	public VersaoPce beforeInsert(VersaoPce bean) {
		List lTest = getVersaoPceDAO().nextValNrVersaoByCliente(((VersaoPce)bean).getCliente().getIdCliente());
		if (lTest.get(0) == null)
			bean.setNrVersaoPce(Integer.valueOf(1));
		else
			bean.setNrVersaoPce(Integer.valueOf(((Integer)lTest.get(0)).intValue() + 1));

		return super.beforeInsert(bean);
	}

	protected VersaoPce beforeStore(VersaoPce bean) {
		if (getVersaoPceDAO().findVersaoPceVigente(bean).size() != 0)
			throw new BusinessException("LMS-00003");
		return super.beforeStore(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(VersaoPce bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setVersaoPceDAO(VersaoPceDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private VersaoPceDAO getVersaoPceDAO() {
		return (VersaoPceDAO) getDao();
	}

	public List findVersaoDescritivoPceByIdVersaoPce(Long masterId) {
		return getVersaoPceDAO().findVersaoDescritivoPceByIdVersaoPce(masterId);
	}

	public Integer getRowCountVersaoDescritivoPceByIdVersaoPce(Long masterId) {
		return getVersaoPceDAO().getRowCountVersaoDescritivoPceByIdVersaoPce(masterId);
	}
	
	
	public java.io.Serializable store(VersaoPce bean, ItemList items) {
		vigenciaService.validaVigenciaBeforeStore(bean);
		if (!items.hasItems())
			throw new BusinessException("LMS-29101");
		Long masterId = null;
		boolean rollbackMasterId = (bean.getIdVersaoPce() == null);
		try {
			beforeStore(bean);
			VersaoPce versaoPce = getVersaoPceDAO().store(bean,items);
			masterId = versaoPce.getIdVersaoPce();
			
		} catch (HibernateOptimisticLockingFailureException e) {
			throw new BusinessException("LMS-00012");
		} catch (RuntimeException e) {
			bean.setIdVersaoPce(null);
			bean.setVersao(null);
			this.rollbackMasterState(bean,rollbackMasterId,e);
			items.rollbackItemsState();
			throw e;
		}
		return masterId;
	}
	
	protected void beforeRemoveByIds(List ids) {
		for(int x = 0; x < ids.size(); x++)
			beforeRemoveById((Long)ids.get(x));
		super.beforeRemoveByIds(ids);
	}
	
	public int getRowCountEnderecoPessoaByContato(Long idContato) {
		return getVersaoPceDAO().getRowCountEnderecoPessoaByContato(idContato);
	}
	
	protected void beforeRemoveById(Long id) {
		VersaoPce vp = findById((Long)id);
		JTVigenciaUtils.validaVigenciaRemocao(vp);
		super.beforeRemoveById(id);
	}

	public List findTelefoneContatoByTpUso(Long idContato,String[] tpUso) {
		return getVersaoPceDAO().findTelefoneContatoByTpUso(idContato,tpUso);
	}

	public List findTelefoneContatoByTpTelefone(Long idContato,String[] tpTelefone) {
		return getVersaoPceDAO().findTelefoneContatoByTpTelefone(idContato,tpTelefone);
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

}

	