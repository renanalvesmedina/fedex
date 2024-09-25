package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.SimulacaoAnexo;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @author FabianoP
 * 
 * @spring.bean
 */
public class SimulacaoAnexoDAO extends BaseCrudDao<SimulacaoAnexo, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected Class<SimulacaoAnexo> getPersistentClass() {
		return SimulacaoAnexo.class;
	}

	/**
	 * LMS-6172 - Busca instância de <tt>SimulacaoAnexo</tt> para determinado
	 * id.
	 * 
	 * @param idSimulacaoAnexo
	 *            id da <tt>SimulacaoAnexo</tt>
	 * @return instância de <tt>SimulacaoAnexo</tt>
	 */
	public SimulacaoAnexo findSimulacaoAnexo(Long idSimulacaoAnexo) {
		return findById(idSimulacaoAnexo);
	}

	/**
	 * LMS-6172 - Busca lista de <tt>SimulacaoAnexo</tt> pelo id da
	 * <tt>Simulacao</tt> a que estão relacionadas, correspondente atributo
	 * <tt>simulacao.idSimulacao</tt> mapeado no critério.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>simulacao.idSimulacao</tt>
	 * @return lista de <tt>SimulacaoAnexo</tt>
	 */
	@SuppressWarnings("unchecked")
	public ResultSetPage<SimulacaoAnexo> findSimulacaoAnexoList(TypedFlatMap criteria) {
		FindDefinition findDefinition = FindDefinition.createFindDefinition(criteria);

		return findPaginated(criteria, findDefinition);
	}

	/**
	 * LMS-6172 - Busca quantidade de <tt>SimulacaoAnexo</tt> relacionadas a uma
	 * <tt>Simulacao</tt> de determinado id.
	 * 
	 * @param idSimulacao
	 *            id da <tt>Simulacao</tt>
	 * @return quantidade de <tt>SimulacaoAnexo</tt>
	 */
	public Integer findSimulacaoAnexoRowCount(Long idSimulacao) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("simulacao.idSimulacao", idSimulacao);
		return getRowCount(criteria);
	}

	/**
	 * LMS-6172 - Inclui ou atualiza instância de <tt>SimulacaoAnexo</tt> no
	 * banco de dados. Deve obrigatoriamente estar relacionada a uma
	 * <tt>Simulacao</tt>.
	 * 
	 * @param simulacaoAnexo
	 *            instância de <tt>SimulacaoAnexo</tt>
	 * @return a mesma instância após persistência
	 */
	public SimulacaoAnexo storeSimulacaoAnexo(SimulacaoAnexo simulacaoAnexo) {
		getAdsmHibernateTemplate().saveOrUpdate(simulacaoAnexo);
		return simulacaoAnexo;
	}

}
