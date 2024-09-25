package com.mercurio.lms.expedicao.model.service;

import static com.mercurio.lms.sim.ConstantesSim.EVENTO_ENTREGA;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.CtoCtoCooperada;
import com.mercurio.lms.expedicao.model.dao.CtoCtoCooperadaDAO;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.dao.LMParceriaDAO;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;

/**
 * Classe de servi�o para CRUD:
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.ctoCtoCooperadaService"
 */
public class CtoCtoCooperadaService extends CrudService<CtoCtoCooperada, Long> {
	private LMParceriaDAO lmParceriaDao;
	private EventoDocumentoServicoService eventoDocumentoServicoService;

	/**
	 * Recupera uma inst�ncia de <code>CtoCtoCooperada</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public CtoCtoCooperada findById(java.lang.Long id) {
		return (CtoCtoCooperada)super.findById(id);
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
	public java.io.Serializable store(CtoCtoCooperada bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setCtoCtoCooperadaDAO(CtoCtoCooperadaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private CtoCtoCooperadaDAO getCtoCtoCooperadaDAO() {
		return (CtoCtoCooperadaDAO) getDao();
	}

	public Integer getRowCountByIdFilialNrCooperada(Long idFilial, Integer nrCtoCooperada) {
		return getCtoCtoCooperadaDAO().getRowCountByIdFilialNrCooperada(idFilial, nrCtoCooperada);
	}
	
	public CtoCtoCooperada findByIdFilialNrCooperada(Long idFilial, Integer nrCtoCooperada) {
		return getCtoCtoCooperadaDAO().findByIdFilialNrCooperada(idFilial, nrCtoCooperada);
	}

	/**
	 * verifica se existe algum conhecimento da cooperada para o documento de servico 
	 * @param idDoctoServico
	 * @return
	 */
	public boolean findCoopByIdDoctoServico(Long idDoctoServico) {
		return getCtoCtoCooperadaDAO().findCoopByIdDoctoServico(idDoctoServico);
	}

	public List findCooperadaByIdConhecimento(Long idDoctoServico) {
		List<Map> cooperadas = lmParceriaDao.findCooperadaByIdConhecimento(idDoctoServico);
		if (cooperadas != null) {
			for (Map cooperada : cooperadas) {
				List<EventoDocumentoServico> eventos = eventoDocumentoServicoService.findEventoDoctoServico(idDoctoServico, EVENTO_ENTREGA, new DomainValue("R"), Boolean.FALSE);
				if (eventos != null && !eventos.isEmpty()) {
					EventoDocumentoServico evento = eventos.get(0);
					cooperada.put("dtEntrega", evento.getDhEvento());
				}
			}
		}
		return cooperadas;
	}

	public List findPaginatedIntegrantes(Long idDoctoServico){
		return lmParceriaDao.findPaginatedIntegrantes(idDoctoServico);
	}

	public Map findDadosCalculoByIdConhecimento(Long idDoctoServico){
		return lmParceriaDao.findDadosCalculoByIdConhecimento(idDoctoServico);
	}

	public Map findOutrosByIdConhecimento(Long idDoctoServico){
		return lmParceriaDao.findOutrosByIdConhecimento(idDoctoServico);
	}

	/*
	 * GETTERS E SETTERS
	 */
	public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}
	public void setLmParceriaDao(LMParceriaDAO lmParceriaDao) {
		this.lmParceriaDao = lmParceriaDao;
	}

}