package com.mercurio.lms.entrega.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.entrega.model.GrauRiscoPerguntaResposta;
import com.mercurio.lms.entrega.model.dao.GrauRiscoPerguntaRespostaDAO;

public class GrauRiscoPerguntaRespostaService extends CrudService<GrauRiscoPerguntaResposta,Long> {
	
	
   public java.io.Serializable store(GrauRiscoPerguntaResposta bean) {
		if (bean.getIdGrauRiscoPerguntaResposta() == null) {
			throw new BusinessException("LMS-17001");
		}
		return super.store(bean);
	}
	
	public void storeRisco(GrauRiscoPerguntaResposta grauRiscoPerguntaResposta) {
		getDao().store(grauRiscoPerguntaResposta);
	}	
	
	public void setGrauRiscoPerguntaRespostaDAO(GrauRiscoPerguntaRespostaDAO dao) {
		setDao(dao);
	}
	
	public List<Map<String, Object>> findPerguntas() {
		return getRiscoDao().findPerguntas();
	}
	
	private GrauRiscoPerguntaRespostaDAO getRiscoDao() {
		return (GrauRiscoPerguntaRespostaDAO) getDao();
	}

}
