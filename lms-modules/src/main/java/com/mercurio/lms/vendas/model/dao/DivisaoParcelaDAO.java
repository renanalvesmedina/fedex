package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.vendas.model.DivisaoParcela;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DivisaoParcelaDAO extends BaseCrudDao<DivisaoParcela, Long> {

	@Override
	protected Class getPersistentClass() {
		return DivisaoParcela.class;
	}

	@Override
	public DivisaoParcela findById(Long id) {
		return findByIdByCriteria(createCriteria(), "idDivisaoParcela", id);
	}
	
	public List<DivisaoParcela> find(Long idTabelaDivisaoCliente, Long idParcelaPreco, Long idParcelaPrecoCobranca) {
		return findByCriterion(createCriterions(idTabelaDivisaoCliente, idParcelaPreco, idParcelaPrecoCobranca), null);
	}
	
	private List<Criterion> createCriterions(
			Long idTabelaDivisaoCliente, 
			Long idParcelaPreco, 
			Long idParcelaPrecoCobranca 
		) {

		List<Criterion> criterionList = new ArrayList<Criterion>();
		if (idTabelaDivisaoCliente != null) {
			criterionList.add(Restrictions.eq("tabelaDivisaoCliente.idTabelaDivisaoCliente", idTabelaDivisaoCliente));
		}
		if (idParcelaPreco != null) {
			criterionList.add(Restrictions.eq("parcelaPreco.idParcelaPreco", idParcelaPreco));
		}
		if (idParcelaPrecoCobranca != null) {
			criterionList.add(Restrictions.ge("parcelaPrecoCobranca.idParcelaPreco", idParcelaPrecoCobranca));
		}
		return criterionList;
	}

	public List<Map<String, Object>> findByDivisaoCliente(Long idDivisaoCliente, String tabelaPreco, Long idTabelaDivisaoCliente) {

		StringBuilder sb = new StringBuilder(
				"  select DP.ID_TABELA_DIVISAO_CLIENTE as ID_TABELA_DIVISAO_CLIENTE ")
		.append("       , DP.ID_PARCELA_PRECO as ID_PARCELA_PRECO, PP.CD_PARCELA_PRECO as CD_PARCELA_PRECO ") 
	    .append("       , DP.ID_PARCELA_PRECO_COBRANCA as ID_PARCELA_PRECO_COBRANCA, PPC.CD_PARCELA_PRECO as CD_PARCELA_PRECO_COBRANCA ")
	    .append("       , TDC.ID_DIVISAO_CLIENTE as ID_DIVISAO_CLIENTE ")  
	    .append("       , DP.ID_DIVISAO_PARCELA as ID_DIVISAO_PARCELA ")
	    .append("    from DIVISAO_PARCELA DP ")
	    .append("       , PARCELA_PRECO PP ")
	    .append("       , PARCELA_PRECO PPC ")
	    .append("       , TABELA_DIVISAO_CLIENTE TDC ")
	    .append("   where DP.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO ")
	    .append("     and DP.ID_PARCELA_PRECO_COBRANCA = PPC.ID_PARCELA_PRECO ")
	    .append("     and DP.TP_SITUACAO = 'A' ")
	    .append("     and DP.ID_TABELA_DIVISAO_CLIENTE = TDC.ID_TABELA_DIVISAO_CLIENTE ");
		
		if (idDivisaoCliente != null) {
			sb.append("     and TDC.ID_DIVISAO_CLIENTE = ").append(idDivisaoCliente).append(" ");
		}
		
		if (tabelaPreco != null) {
			sb.append("     and PP.CD_PARCELA_PRECO = '").append(tabelaPreco).append("' ");
		}
	    
	    if (idTabelaDivisaoCliente != null) {
	    	sb.append("     and DP.ID_TABELA_DIVISAO_CLIENTE = ").append(idTabelaDivisaoCliente).append(" ");
	    }
	    
	    sb.append("order by DP.ID_TABELA_DIVISAO_CLIENTE, DP.ID_PARCELA_PRECO, DP.ID_PARCELA_PRECO_COBRANCA "); 
		
		Map<String, Object> criterions = new HashMap<String, Object>();
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_TABELA_DIVISAO_CLIENTE", Hibernate.LONG);
				sqlQuery.addScalar("ID_PARCELA_PRECO", Hibernate.LONG);
				sqlQuery.addScalar("CD_PARCELA_PRECO", Hibernate.STRING);
				sqlQuery.addScalar("ID_PARCELA_PRECO_COBRANCA", Hibernate.LONG);
				sqlQuery.addScalar("CD_PARCELA_PRECO_COBRANCA", Hibernate.STRING);
				sqlQuery.addScalar("ID_DIVISAO_PARCELA", Hibernate.LONG);
			}
		};

		return getAdsmHibernateTemplate().findBySqlToMappedResult(sb.toString(), criterions, csq);
	}
	
}
