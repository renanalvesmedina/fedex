package com.mercurio.lms.expedicao.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.dao.ObservacaoDoctoServicoDAO;
import com.mercurio.lms.sim.model.dao.LMComplementoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:
 * 
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.expedicao.observacaoDoctoServicoService"
 */
public class ObservacaoDoctoServicoService extends CrudService<ObservacaoDoctoServico, Long> {
	
	private LMComplementoDAO lmComplementoDao;

	/**
	 * Recupera uma inst�ncia de <code>ObservacaoDoctoServico</code>
	 * a partir do ID.
	 * 
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ObservacaoDoctoServico findById(java.lang.Long id) {
		return (ObservacaoDoctoServico) super.findById(id);
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
	@Override
	@ParametrizedAttribute(type = Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contr�rio.
	 * 
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ObservacaoDoctoServico bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste
	 * servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setObservacaoDoctoServicoDAO(ObservacaoDoctoServicoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia
	 * dos dados deste servi�o.
	 * 
	 * @return Inst�ncia do DAO.
	 */
	private ObservacaoDoctoServicoDAO getObservacaoDoctoServicoDAO() {
		return (ObservacaoDoctoServicoDAO) getDao();
	}

	public List<Long> findIdsByIdDoctoServico(Long idDoctoServico) {
		return getObservacaoDoctoServicoDAO().findIdsByIdDoctoServico(idDoctoServico);
	}

	public List<ObservacaoDoctoServico> findByIdDoctoServico(Long idDoctoServico) {
		Map<String, Object> m1 = new HashMap<String, Object>(1);
		Map<String, Object> m2 = new HashMap<String, Object>(1);
		m2.put("idDoctoServico", idDoctoServico);
		m1.put("doctoServico", m2);
		return find(m1);
	}

	public String findCdEmbMastersafByDocServico(Long idDoctoServico) {
		return getObservacaoDoctoServicoDAO().findCdEmbMastersafByDocServico(idDoctoServico);
	}

	public List<Map<String, Object>> findPaginatedComplObservacoes(Long idDoctoServico){
		return lmComplementoDao.findPaginatedComplObservacoes(idDoctoServico);
	}

	public void setLmComplementoDao(LMComplementoDAO lmComplementoDao) {
		this.lmComplementoDao = lmComplementoDao;
	}

	public List<ObservacaoDoctoServico> findOrderPriorityByIdDoctoServico(Long idDoctoServico) {
		return getObservacaoDoctoServicoDAO().findOrderPriorityByIdDoctoServico(idDoctoServico);
	}

	public void removeByIdDoctoServicoAndDsObservacao(Long idDoctoServico, String dsObservacao){
		getObservacaoDoctoServicoDAO().removeByIdDoctoServicoAndDsObservacao(idDoctoServico, dsObservacao);
	}
	
}