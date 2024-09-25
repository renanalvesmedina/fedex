package com.mercurio.lms.carregamento.model.dao;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.LiberaMpc;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplica��o atrav�s do suporte
 * ao Hibernate em conjunto com o Spring. N�o inserir documenta��o ap�s ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class LiberarMpcDAO extends BaseCrudDao<LiberaMpc, Long> {

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
	protected final Class getPersistentClass() {
		return LiberaMpc.class;
	}

	/**
	 * Retorna o uma lista de Libera Mpc de acordo com a situa��o
	 */
	public List<Map> findLiberarMpcBySituacao(Map param) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" new Map(" +
				"lbMpc.idLiberaMPC as idLiberaMPC, " + 
				"lbMpc.dsLiberaMPC as dsLiberaMPC, " + 
				"lbMpc.stLibera as stLibera, " + 
				"lbMpc.tpAutorizacao as tpAutorizacao, " + 
				"lbMpc.tpLibera as tpLibera ) " );

		sql.addFrom(LiberaMpc.class.getName() + " lbMpc");

		sql.addCriteria("lbMpc.stLibera", "=", param.get("stLibera"));

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	public boolean findExigeAutirizacao(Long idLiberarMpc) {

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("count(*)");

		sql.addFrom(LiberaMpc.class.getName() + " lbMpc");

		sql.addCriteria("lbMpc.idLiberaMPC", "=", idLiberarMpc);

		Object result = getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());

		return ((Long) result) > 0 ? true : false;
	}

	/**
	 * M�todo gen�rico para buscar um LiberaMpc, passando os par�metros ele
	 * monta a query conforme os que n�o est�o vazios.
	 * 
	 * @param idLiberaMPC
	 * @param dsLiberaMPC
	 * @param stLibera
	 * @param tpLibera
	 * @param tpAutorizacao
	 * @return
	 */
	public LiberaMpc find(Long idLiberaMPC, String dsLiberaMPC, String stLibera, String tpLibera, String tpAutorizacao) {
		SqlTemplate sql = new SqlTemplate();

		sql.addFrom(LiberaMpc.class.getName() + " lbMpc");

		if (idLiberaMPC != null) {
			sql.addCriteria("lbMpc.idLiberaMPC", "=", idLiberaMPC);
		}

		if (dsLiberaMPC != null) {
			sql.addCriteria("lbMpc.dsLiberaMPC", "=", dsLiberaMPC);
		}

		if (stLibera != null) {
			sql.addCriteria("lbMpc.stLibera", "=", stLibera);
		}

		if (tpLibera != null) {
			sql.addCriteria("lbMpc.tpLibera", "=", tpLibera);
		}

		if (tpAutorizacao != null) {
			sql.addCriteria("lbMpc.tpAutorizacao", "=", tpAutorizacao);
		}

		return (LiberaMpc) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
	}
}
