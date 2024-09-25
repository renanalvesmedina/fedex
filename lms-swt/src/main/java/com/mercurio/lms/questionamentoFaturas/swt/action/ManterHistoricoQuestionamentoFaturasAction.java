package com.mercurio.lms.questionamentoFaturas.swt.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.questionamentoFaturas.model.service.HistoricoQuestionamentoFaturasService;
import com.mercurio.lms.questionamentofaturas.model.HistoricoQuestionamentoFatura;
import com.mercurio.lms.questionamentofaturas.model.QuestionamentoFatura;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class ManterHistoricoQuestionamentoFaturasAction {
	private HistoricoQuestionamentoFaturasService historicoQuestionamentoFaturasService;

    public HistoricoQuestionamentoFatura findById(Long id) {
    	return historicoQuestionamentoFaturasService.findById(id);
    }

    public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		PaginatedQuery paginatedQuery = new PaginatedQuery(criteria);
		return historicoQuestionamentoFaturasService.findPaginated(paginatedQuery);
	}

    public void removeById(Long id) {
    	historicoQuestionamentoFaturasService.removeById(id);
    }

	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		historicoQuestionamentoFaturasService.removeByIds(ids);
	}
    
    public Map store(Map<String, Object> criteria) {
		Long idHistoricoQuestionamentoFatura = (Long)criteria.get("idHistoricoQuestionamentoFatura");
		Long idQuestionamentoFatura = MapUtils.getLong(criteria, "idQuestionamentoFatura");
		QuestionamentoFatura questionamentoFatura = new QuestionamentoFatura();
		questionamentoFatura.setIdQuestionamentoFatura(idQuestionamentoFatura);

		HistoricoQuestionamentoFatura historicoQuestionamentoFatura = new HistoricoQuestionamentoFatura();
		historicoQuestionamentoFatura.setIdHistoricoQuestionamentoFatura(idHistoricoQuestionamentoFatura);
		historicoQuestionamentoFatura.setQuestionamentoFatura(questionamentoFatura);
		UsuarioLMS usuarioLMS = new UsuarioLMS();;
		usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		historicoQuestionamentoFatura.setUsuario(usuarioLMS);
		historicoQuestionamentoFatura.setDhHistorico(JTDateTimeUtils.getDataHoraAtual());
		historicoQuestionamentoFatura.setTpHistorico(new DomainValue(MapUtils.getString(criteria, "tpHistorico")));
		historicoQuestionamentoFatura.setObHistorico(MapUtils.getString(criteria, "obHistorico"));

		historicoQuestionamentoFaturasService.store(historicoQuestionamentoFatura);

		Map<String, Object> mapRetorno = new HashMap<String, Object>();
		mapRetorno.put("idHistoricoQuestionamentoFatura", historicoQuestionamentoFatura.getIdHistoricoQuestionamentoFatura());
		return mapRetorno;
	}

	public void setHistoricoQuestionamentoFaturasService(HistoricoQuestionamentoFaturasService historicoQuestionamentoFaturasService) {
		this.historicoQuestionamentoFaturasService = historicoQuestionamentoFaturasService;
	}
}