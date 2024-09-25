package com.mercurio.lms.entrega.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.entrega.model.GrauRiscoPerguntaResposta;

public class GrauRiscoPerguntaRespostaDAO extends BaseCrudDao<GrauRiscoPerguntaResposta, Long> {

	@Override
	protected Class getPersistentClass() {		
		return GrauRiscoPerguntaResposta.class;
	}
	
	public GrauRiscoPerguntaResposta store(GrauRiscoPerguntaResposta grauRiscoPerguntaResposta) {
		super.store(grauRiscoPerguntaResposta);
		getAdsmHibernateTemplate().flush();
		return grauRiscoPerguntaResposta;
	}
	
	public List<Map<String, Object>> findPerguntas(){
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT gr.ID_GRAU_RISCO_PERGUNTA as id_Pergunta, gr.DS_GRAU_RISCO_PERGUNTA as pergunta ");
		sql.append("FROM GRAU_RISCO_PERGUNTA gr ");
		Map<String, Object> values = new HashMap<String, Object>();
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), values, null);
	}

}
