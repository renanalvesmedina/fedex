package com.mercurio.lms.questionamentoFaturas.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.questionamentoFaturas.model.dao.AnexoQuestionamentoFaturasDAO;
import com.mercurio.lms.questionamentofaturas.model.AnexoQuestionamentoFatura;
import com.mercurio.lms.util.AnexosQuestionamentoUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class AnexoQuestionamentoFaturasService extends CrudService<AnexoQuestionamentoFatura, Long>{

	public AnexoQuestionamentoFatura findById(Long id) {
		return getAnexoQuestionamentoFaturasDAO().findById(id);
	}

	public ResultSetPage<Map<String, Object>> findPaginated(PaginatedQuery paginatedQuery) {
		ResultSetPage result = getAnexoQuestionamentoFaturasDAO().findPaginated(paginatedQuery);

		List<AnexoQuestionamentoFatura> anexos = result.getList();
		List<Map<String, Object>> toReturn = new ArrayList<Map<String,Object>>(anexos.size());
		for (AnexoQuestionamentoFatura anexo : anexos) {
			toReturn.add(AnexosQuestionamentoUtils.convertBeanToMap(anexo));
		}
		result.setList(toReturn);
		return result;
	}

	@Override
	public void removeById(Long id) {
		if(id > 0) {
			AnexoQuestionamentoFatura anexoQuestionamentoFatura = this.findById(id);
			if(!SessionUtils.getUsuarioLogado().getIdUsuario().equals(anexoQuestionamentoFatura.getUsuario().getIdUsuario())) {
				throw new BusinessException("LMS-42029");
			}
			/** Verifica a situação do Questionamento para excluir anexo */
	    	String tpSituacao = anexoQuestionamentoFatura.getQuestionamentoFatura().getTpSituacao().getValue();
			if ("ACO".equals(tpSituacao) || "CAN".equals(tpSituacao)){
				throw new BusinessException("LMS-42022", new Object[]{anexoQuestionamentoFatura.getQuestionamentoFatura().getTpSituacao().getDescriptionAsString()});
			}
			super.removeById(id);
		/** Remove Anexos na sessão */
		} else AnexosQuestionamentoUtils.removeAnexosFromSession(id);
	}

	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		for (Long id : ids) {
			this.removeById(id);
		}
	}

	public java.io.Serializable store(AnexoQuestionamentoFatura bean) {
		return super.store(bean);
	}

	public void setAnexoQuestionamentoFaturasDAO(AnexoQuestionamentoFaturasDAO dao) {
		setDao( dao );
	}
	private AnexoQuestionamentoFaturasDAO getAnexoQuestionamentoFaturasDAO() {
		return (AnexoQuestionamentoFaturasDAO) getDao();
	}
}