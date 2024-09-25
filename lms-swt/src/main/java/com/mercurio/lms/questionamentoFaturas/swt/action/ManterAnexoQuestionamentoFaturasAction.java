package com.mercurio.lms.questionamentoFaturas.swt.action;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.questionamentoFaturas.model.service.AnexoQuestionamentoFaturasService;
import com.mercurio.lms.questionamentoFaturas.model.service.QuestionamentoFaturasService;
import com.mercurio.lms.questionamentofaturas.model.AnexoQuestionamentoFatura;
import com.mercurio.lms.questionamentofaturas.model.QuestionamentoFatura;
import com.mercurio.lms.util.AnexosQuestionamentoUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class ManterAnexoQuestionamentoFaturasAction {
	private AnexoQuestionamentoFaturasService anexoQuestionamentoFaturasService;
	private QuestionamentoFaturasService questionamentoFaturasService;
	private UsuarioLMSService usuarioLMSService;

    public AnexoQuestionamentoFatura findById(Long id) {
    	return anexoQuestionamentoFaturasService.findById(id);
    }

    public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		ResultSetPage<Map<String, Object>> rsp = new ResultSetPage<Map<String, Object>>(Integer.valueOf(1), false, false, Collections.EMPTY_LIST);
		if(MapUtils.getObject(criteria, "idQuestionamentoFatura") != null) {
			rsp = anexoQuestionamentoFaturasService.findPaginated(new PaginatedQuery(criteria));
		} else rsp.setList(AnexosQuestionamentoUtils.findAnexosMappedInSession());
		return rsp;
	}

    public void removeById(Long id) {
    	anexoQuestionamentoFaturasService.removeById(id);
    }

	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
    	anexoQuestionamentoFaturasService.removeByIds(ids);
	}

    public Map<String, Object> store(Map<String, Object> criteria) {
		AnexoQuestionamentoFatura anexoQuestionamentoFatura = new AnexoQuestionamentoFatura();
		anexoQuestionamentoFatura.setDsAnexo(MapUtils.getString(criteria, "dsAnexo"));
		anexoQuestionamentoFatura.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
		anexoQuestionamentoFatura.setUsuario(usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario()));
		try {
			/** Converte arquivo */
			anexoQuestionamentoFatura.setDcArquivo(Base64Util.decode(MapUtils.getString(criteria, "dcArquivo")));
		} catch (IOException e) {
			throw new InfrastructureException(e.getMessage());
		}

		/** Se ainda não existe Questionamento, grava anexos na sessão */
		Long idQuestionamentoFatura = MapUtils.getLong(criteria, "idQuestionamentoFatura");
		if(idQuestionamentoFatura != null) {
			QuestionamentoFatura questionamentoFatura = questionamentoFaturasService.findById(idQuestionamentoFatura);
			anexoQuestionamentoFatura.setQuestionamentoFatura(questionamentoFatura);

			anexoQuestionamentoFaturasService.store(anexoQuestionamentoFatura);
		} else {
			/** Grava Anexo na sessão */
			anexoQuestionamentoFatura.setDcArquivoTemp(MapUtils.getString(criteria, "dcArquivoTemp"));
			AnexosQuestionamentoUtils.addAnexoInSession(anexoQuestionamentoFatura);
		}
		Map<String, Object> mapRetorno = new HashMap<String, Object>();
		mapRetorno.put("idAnexoQuestionamentoFatura", anexoQuestionamentoFatura.getIdAnexoQuestionamentoFatura());
		return mapRetorno;
	}

	public void setAnexoQuestionamentoFaturasService(AnexoQuestionamentoFaturasService anexoQuestionamentoFaturasService) {
		this.anexoQuestionamentoFaturasService = anexoQuestionamentoFaturasService;
	}
	public void setQuestionamentoFaturasService(QuestionamentoFaturasService questionamentoFaturasService) {
		this.questionamentoFaturasService = questionamentoFaturasService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}
}