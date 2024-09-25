package com.mercurio.lms.questionamentoFaturas.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.questionamentoFaturas.model.dao.HistoricoQuestionamentoFaturasDAO;
import com.mercurio.lms.questionamentofaturas.model.HistoricoQuestionamentoFatura;
import com.mercurio.lms.questionamentofaturas.model.QuestionamentoFatura;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class HistoricoQuestionamentoFaturasService extends CrudService<HistoricoQuestionamentoFatura, Long>{

	public HistoricoQuestionamentoFatura findById(Long id) {
		return getHistoricoQuestionamentoFaturasDAO().findById(id);
	}

	public ResultSetPage<Map<String, Object>> findPaginated(PaginatedQuery paginatedQuery) {
		ResultSetPage result = getHistoricoQuestionamentoFaturasDAO().findPaginated(paginatedQuery);

		List<HistoricoQuestionamentoFatura> historicos = result.getList();
		List<Map<String, Object>> toReturn = new ArrayList<Map<String,Object>>(historicos.size());
		for (HistoricoQuestionamentoFatura historico : historicos) {
			Map<String, Object> anexoMapped = new HashMap<String, Object>();
			anexoMapped.put("idHistoricoQuestionamentoFatura", historico.getIdHistoricoQuestionamentoFatura());
			anexoMapped.put("idQuestionamentoFatura", historico.getQuestionamentoFatura().getIdQuestionamentoFatura());
			anexoMapped.put("nrMatricula", historico.getUsuario().getUsuarioADSM().getNrMatricula());
			anexoMapped.put("nmUsuario", historico.getUsuario().getUsuarioADSM().getNmUsuario());
			anexoMapped.put("dhHistorico", historico.getDhHistorico());
			anexoMapped.put("tpHistorico", historico.getTpHistorico());
			anexoMapped.put("obHistorico", historico.getObHistorico());
			toReturn.add(anexoMapped);
		}
		result.setList(toReturn);
		return result;
	}

	/**
	 * Método utilizado na tela ConsultarHistoricoQuestionamentoFaturasList.jsp
	 * @param idQuestionamentoFatura
	 * @param findDefinition
	 * @return
	 */
	public ResultSetPage findPaginatedHistoricoQuestionamentoFaturas(Long idQuestionamentoFatura, FindDefinition findDefinition) {
    	if (idQuestionamentoFatura == null) throw new IllegalArgumentException("Parâmetro 'idQuestionamentoFatura' não pode ser null!");
		ResultSetPage result = getHistoricoQuestionamentoFaturasDAO().findPaginatedHistoricoQuestionamentoFaturas(idQuestionamentoFatura, findDefinition);		
		return result;
	}

	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}

	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	public java.io.Serializable store(HistoricoQuestionamentoFatura bean) {
		return super.store(bean);
	}

	/**
	 * Rorina para geração simplificada de Historicos 
	 * @param questionamentoFatura
	 * @param tpHistorico
	 * @param obHistorico
	 */
	public void generateHistoricoQuestionamento(QuestionamentoFatura questionamentoFatura, DomainValue tpHistorico, String obHistorico) {
		/** Gera historico de questionamento */
		HistoricoQuestionamentoFatura historico = new HistoricoQuestionamentoFatura();
		historico.setQuestionamentoFatura(questionamentoFatura);
		historico.setDhHistorico(JTDateTimeUtils.getDataHoraAtual());
		UsuarioLMS usuarioLMS = new UsuarioLMS();
		usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		historico.setUsuario(usuarioLMS);
		historico.setTpHistorico(tpHistorico);
		historico.setObHistorico(obHistorico);
		this.store(historico);
	}

	public void setHistoricoQuestionamentoFaturasDAO(HistoricoQuestionamentoFaturasDAO dao) {
		setDao( dao );
	}
	private HistoricoQuestionamentoFaturasDAO getHistoricoQuestionamentoFaturasDAO() {
		return (HistoricoQuestionamentoFaturasDAO) getDao();
	}
}