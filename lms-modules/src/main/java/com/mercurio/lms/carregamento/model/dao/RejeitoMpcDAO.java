package com.mercurio.lms.carregamento.model.dao;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.RejeitoMpc;
import com.mercurio.lms.expedicao.model.Conhecimento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RejeitoMpcDAO extends BaseCrudDao<RejeitoMpc, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return RejeitoMpc.class;
	}
	
	/**
	 * Retorna o uma lista de Rejeito Mpc de acordo com a situação e a abrangência
	 */
	public List<Map> findRejeitoMpcBySituacaoAbrangencia(Map param) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" new Map(rjMpc.idRejeitoMPC as idRejeitoMPC, " +
				"rjMpc.dsRejeitoMPC as dsRejeitoMPC, " +
				"rjMpc.stRejeito as stRejeito, " +
				"rjMpc.tpAbrangencia as tpAbrangencia, " +
				"rjMpc.tpAutorizacao as tpAutorizacao, " +
				"rjMpc.tpRejeito as tpRejeito ) "
		);

		sql.addFrom(RejeitoMpc.class.getName() + " rjMpc");	
		
		sql.addCriteria("rjMpc.stRejeito", "=", param.get("stRejeito") );
		sql.addCriteria("rjMpc.tpAbrangencia", "=", param.get("tpAbrangencia") );
		
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria()); 
	}
	
	public boolean findExigeAutirizacao(Long idRejeitoMpc) {
		
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("count(*)");

		sql.addFrom(RejeitoMpc.class.getName() + " rjMpc");	
		
		sql.addCriteria("rjMpc.idRejeitoMPC", "=", idRejeitoMpc);
		sql.addCriteria("rjMpc.tpAbrangencia", "=", "V");
		sql.addCriteriaIn("rjMpc.tpAutorizacao", new Object[] { "AC", "AI" });
		
		Object result = getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
		
		return ((Long)result) > 0 ? true : false;
	}
}
