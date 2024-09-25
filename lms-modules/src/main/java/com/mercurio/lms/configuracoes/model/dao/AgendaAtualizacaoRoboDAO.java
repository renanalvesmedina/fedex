package com.mercurio.lms.configuracoes.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.AgendaAtualizacaoRobo;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AgendaAtualizacaoRoboDAO  extends BaseCrudDao<AgendaAtualizacaoRobo, Long> {

	public Long getUltimaVersao() {
		StringBuffer sql = new StringBuffer("");
		sql.append("select max(nrVersao) from AgendaAtualizacaoRobo ");
		
		List<Long> list = getAdsmHibernateTemplate().find(sql.toString());
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		return Long.valueOf(0);
	}
	

	public AgendaAtualizacaoRobo getUltimaAtualizacao(Long versaoAtual) {
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT atr FROM AgendaAtualizacaoRobo atr " +
				   " WHERE " +
				   " 		atr.nrVersao = (SELECT MAX(ag.nrVersao) FROM AgendaAtualizacaoRobo ag) " +
				   " 		and atr.nrVersao > :versaoAtual");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("versaoAtual", versaoAtual);
		return (AgendaAtualizacaoRobo) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), parameters);
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return AgendaAtualizacaoRobo.class;
	}
}
