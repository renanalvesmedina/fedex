package com.mercurio.lms.expedicao.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class ObservacaoDoctoServicoDAO extends BaseCrudDao<ObservacaoDoctoServico, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class<ObservacaoDoctoServico> getPersistentClass() {
		return ObservacaoDoctoServico.class;
	}

	public List<Long> findIdsByIdDoctoServico(Long idDoctoServico) {
		String sql = "select pojo.idObservacaoDoctoServico " + "from "
				+ ObservacaoDoctoServico.class.getName() + " as  pojo "
				+ "join pojo.doctoServico as ds "
				+ "where ds.idDoctoServico = :idDoctoServico ";
		return getAdsmHibernateTemplate().findByNamedParam(sql, "idDoctoServico", idDoctoServico);
	}

	public String findCdEmbMastersafByDocServico(Long idDoctoServico) {
		StringBuilder sql = new StringBuilder()
		.append("   select new map(min(ods.id) as id, ods.cdEmbLegalMastersaf as cdEmbLegalMastersaf)")
        .append("     from ObservacaoDoctoServico ods")
	    .append("    where ods.cdEmbLegalMastersaf is not null")
	    .append("      and ods.doctoServico.id = :idDoctoServico")
		.append(" group by cdEmbLegalMastersaf");

		List result = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), "idDoctoServico", idDoctoServico);
		
		if(result != null && result.size() > 0) {
			Map map = (Map) result.get(0);
			return (String) map.get("cdEmbLegalMastersaf");
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<ObservacaoDoctoServico> findOrderPriorityByIdDoctoServico(Long idDoctoServico) {
		String sql = "select new  com.mercurio.lms.expedicao.model.ObservacaoDoctoServico(pojo.dsObservacaoDoctoServico)"
				+ "from " + ObservacaoDoctoServico.class.getName() + " as  pojo "
				+ "join pojo.doctoServico as ds "
				+ "where ds.idDoctoServico = :idDoctoServico "
				+ "order by pojo.blPrioridade desc,  pojo.idObservacaoDoctoServico asc ";
		return getAdsmHibernateTemplate().findByNamedParam(sql, "idDoctoServico", idDoctoServico);
	}
	
	@SuppressWarnings("unchecked")
	public void removeByIdDoctoServicoAndDsObservacao(Long idDoctoServico, String dsObservacao) {
		StringBuilder query = new StringBuilder("DELETE FROM OBSERVACAO_DOCTO_SERVICO ODS ")
										.append("WHERE ODS.ID_DOCTO_SERVICO = :idDoctoServico ")
										.append("AND ODS.DS_OBSERVACAO_DOCTO_SERVICO LIKE :dsObservacao ");
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idDoctoServico", idDoctoServico);
		parametros.put("dsObservacao", dsObservacao);
		
		getAdsmHibernateTemplate().executeUpdateBySql(query.toString(), parametros);
	}

}