package com.mercurio.lms.contratacaoveiculos.model.service;
 
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contratacaoveiculos.model.ItChecklistTpMeioTransp;
import com.mercurio.lms.contratacaoveiculos.model.dao.ItChecklistTpMeioTranspDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contratacaoveiculos.itChecklistTpMeioTranspService"
 */
public class ItChecklistTpMeioTranspService extends CrudService<ItChecklistTpMeioTransp, Long> {
	private VigenciaService vigenciaService;

	/**
	 * Recupera uma inst�ncia de <code>ItChecklistTpMeioTransp</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ItChecklistTpMeioTransp findById(java.lang.Long id) {
		return (ItChecklistTpMeioTransp)super.findById(id);
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

	protected void beforeRemoveByIds(List ids) {
		ItChecklistTpMeioTransp bean = null;
		for(int x = 0; x < ids.size(); x++) {
			Long id = (Long)ids.get(x);
			bean =(ItChecklistTpMeioTransp)getItChecklistTpMeioTranspDAO().getAdsmHibernateTemplate().get(ItChecklistTpMeioTransp.class,id);
			if (bean != null)
				JTVigenciaUtils.validaVigenciaRemocao(bean);
		}
		super.beforeRemoveByIds(ids);
	}

	protected void beforeRemoveById(Long id) {
		ItChecklistTpMeioTransp bean =(ItChecklistTpMeioTransp)getItChecklistTpMeioTranspDAO().getAdsmHibernateTemplate().get(ItChecklistTpMeioTransp.class,id);
		if (bean != null)
			JTVigenciaUtils.validaVigenciaRemocao(bean);
		super.beforeRemoveById(id);
	}
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public ItChecklistTpMeioTransp beforeStore(ItChecklistTpMeioTransp bean) {
		if (getItChecklistTpMeioTranspDAO().findBeforeStore((ItChecklistTpMeioTransp)bean))
			throw new BusinessException("LMS-00003");
		return super.beforeStore(bean);
	}

	public java.io.Serializable store(ItChecklistTpMeioTransp bean) {
		vigenciaService.validaVigenciaBeforeStore(bean);
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setItChecklistTpMeioTranspDAO(ItChecklistTpMeioTranspDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ItChecklistTpMeioTranspDAO getItChecklistTpMeioTranspDAO() {
		return (ItChecklistTpMeioTranspDAO) getDao();
	}

	public List findItChecklistByIdTipoMeioTransporteSolicitacao(Long idTipoMeioTransporte, Long idChecklistMeioTransporte){
		List itens = null;
		List respostas = getItChecklistTpMeioTranspDAO().findRespostasCheckList(idTipoMeioTransporte, idChecklistMeioTransporte);
		if (!respostas.isEmpty())
			return respostas;
		else
			itens = getItChecklistTpMeioTranspDAO().findItChecklistByIdTipoMeioTransporteSolicitacao(idTipoMeioTransporte, idChecklistMeioTransporte);
		
		return itens;
	}

	public List findItChecklistMotByIdTipoMeioTransporteSolicitacao(Long idTipoMeioTransporte,Long idChecklist,String idMotorista){
		List itens = null;
		List respostas = getItChecklistTpMeioTranspDAO().findRespostasMotorista(idTipoMeioTransporte, idChecklist , idMotorista);
		if (!respostas.isEmpty())
			return respostas;
		else
			itens = getItChecklistTpMeioTranspDAO().findItChecklistMotByIdTipoMeioTransporteSolicitacao(idTipoMeioTransporte, idMotorista);
		return itens;
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

}