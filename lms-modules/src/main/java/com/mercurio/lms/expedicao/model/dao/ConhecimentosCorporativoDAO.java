package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.mercurio.adsm.framework.util.SqlTemplate;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ConhecimentosCorporativoDAO extends JdbcDaoSupport {

	/**
	 * Busca as datas da tabela v_conhecimentos_corporativo. 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 01/08/2006
	 *
	 * @param nrConhecimento
	 * @return
	 *
	 */
	public List findDadosConhecimento(Long nrConhecimento, String sgFilial){
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom("V_CONHECIMENTOS_CORPORATIVO", "VCC");
		
		sql.addProjection("VCC.DT_PAGAMENTO", "DT_PAGAMENTO");
		sql.addProjection("VCC.DT_ENT_COB_JUR", "DT_ENT_COB_JUR");
		sql.addProjection("VCC.SG_FILIAL_RELACAO_COBRANCA", "SG_FILIAL_RELACAO_COBRANCA");
		sql.addProjection("VCC.NR_RELACAO_COBRANCA", "NR_RELACAO_COBRANCA");
		
		sql.addCriteria("VCC.NR_CONHECIMENTO", "=", nrConhecimento);
		sql.addCriteria("VCC.SG_FILIAL", "=", sgFilial);
		
		return getJdbcTemplate().queryForList(sql.getSql(), sql.getCriteria());
	}
	
}
