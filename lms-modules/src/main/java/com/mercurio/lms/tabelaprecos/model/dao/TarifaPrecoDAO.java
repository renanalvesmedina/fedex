package com.mercurio.lms.tabelaprecos.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPrecoRota;
import com.mercurio.lms.tabelaprecos.util.RotaPrecoUtils;
import com.mercurio.lms.util.IntegerUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TarifaPrecoDAO extends BaseCrudDao<TarifaPreco, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected final Class getPersistentClass() {
		return TarifaPreco.class;
	}
	/**
	 * Através do cdTarifaPreco passada por parametro
	 * 
	 * @param cdTarifaPreco
	 * @return <TarifaPreco>
	 */
	public TarifaPreco findByCdTarifaPreco(String cdTarifaPreco) {
    	StringBuffer sql = new StringBuffer()
    	.append("from ").append(getPersistentClass().getName()).append(" as tp ")
    	.append(" where ")
    	.append(" tp.cdTarifaPreco = ? ");
		return (TarifaPreco)getAdsmHibernateTemplate().findUniqueResult(sql.toString(),new Object[]{cdTarifaPreco});	
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param idEmpresa
	 * @param cdTarifaPreco
	 * @return <TarifaPreco> 
	 */
	public TarifaPreco findTarifaPreco(Long idEmpresa, String cdTarifaPreco) {
		DetachedCriteria dc = createDetachedCriteria()
			.add(Restrictions.eq("empresa.id", idEmpresa))
			.add(Restrictions.eq("cdTarifaPreco", cdTarifaPreco));
		return (TarifaPreco) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	 * Busca Tarifa das Rotas.
	 * 
	 * @param idTabelaPreco
	 * @param restricaoRotaOrigem
	 * @param restricaoRotaDestino
	 * @return <TarifaPreco>
	 */
	public TarifaPreco findTarifaPreco(Long idTabelaPreco, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino) {
		TarifaPreco tarifaPreco = null;

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("tarp.idTarifaPreco"), "idTarifaPreco");

		DetachedCriteria dc = DetachedCriteria.forClass(TarifaPrecoRota.class, "tpr");
		dc.setProjection(projectionList);

		dc.createAlias("tpr.rotaPreco","rp");
		dc.createAlias("tpr.tarifaPreco","tarp");
		dc.createAlias("tpr.tabelaPreco","tabp");

		dc.add(Restrictions.eq("tabp.idTabelaPreco", idTabelaPreco));

		RotaPrecoUtils.addRotaPrecoRestricaoRota(dc, restricaoRotaOrigem, restricaoRotaDestino);

		dc.setResultTransformer(new AliasToBeanResultTransformer(TarifaPreco.class));

		List<TarifaPreco> result = findPaginatedByDetachedCriteria(dc, IntegerUtils.ONE, IntegerUtils.ONE).getList();
		if(result.size() == 1) {
			tarifaPreco = result.get(0);
		}
		return tarifaPreco;
	}

	public Long findByNrKm(Long nrKm) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("tp.idTarifaPreco");
		sql.addFrom("TarifaPreco", "tp");
		sql.addCriteria("tp.tpSituacao", "=", "A");
		sql.addCriteria("tp.nrKmInicial", "<=", nrKm);
		sql.addCriteria("tp.nrKmFinal", ">=", nrKm);

		List<Long> result = getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());

		if (!result.isEmpty())
			return result.get(0);
		else
			return null;
	}

	public TarifaPreco findTarifaPrecoAtual(Long nrKm) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(TarifaPreco.class, "tp")
		.add(Restrictions.le("tp.tpSituacao", "A"))
		.add(Restrictions.le("tp.nrKmInicialAtual", nrKm))
		.add(Restrictions.ge("tp.nrKmFinalAtual", nrKm));

		List<TarifaPreco> list = findByDetachedCriteria(dc);
		if(list != null && !list.isEmpty()){
			return list.get(0); 
		}		
		return null;
	}
	
	public TarifaPreco findTarifaPrecoAntiga(Long nrKm) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(TarifaPreco.class, "tp")
		.add(Restrictions.le("tp.tpSituacao", "A"))
		.add(Restrictions.le("tp.nrKmInicial", nrKm))
		.add(Restrictions.ge("tp.nrKmFinal", nrKm));

		List<TarifaPreco> list = findByDetachedCriteria(dc);
		if(list != null && !list.isEmpty()){
			return list.get(0); 
		}		
		return null;
	}	
	
	@SuppressWarnings("unchecked")
	public List<TarifaPreco> findTarifaPrecoParaMarkup(String cdTarifaPreco, Long idTabelaPreco, boolean minimoProgressivo) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> criteria = new HashMap<String, Object>();

		sql.append(" select distinct tfp ");
		sql.append(" from TarifaPreco as tfp ");
		sql.append(" where 1 = 1 ");
		
		if(minimoProgressivo){
			sql.append(" and exists ( ");
			sql.append(" 				select 1 from ValorFaixaProgressiva as v ");
			sql.append(" 					join v.faixaProgressiva as f ");
			sql.append(" 					join f.tabelaPrecoParcela as tpp ");
			sql.append(" 					join tpp.tabelaPreco as tp ");
			sql.append(" 				where tp.idTabelaPreco = :idTabelaPreco ");
			sql.append(" 				and v.tarifaPreco.idTarifaPreco = tfp.idTarifaPreco ");
			sql.append(" ) ");
		}else{
			sql.append(" and exists ( ");
			sql.append(" 				select 1 from PrecoFrete as pf ");
			sql.append(" 					join pf.tabelaPrecoParcela as tpp ");
			sql.append(" 					join tpp.tabelaPreco as tp ");
			sql.append(" 				where tp.idTabelaPreco = :idTabelaPreco ");
			sql.append(" 				and pf.tarifaPreco.idTarifaPreco = tfp.idTarifaPreco ");
			sql.append(" ) ");
		}
		
		if(StringUtils.isNotEmpty(cdTarifaPreco)){
			sql.append(" and tfp.cdTarifaPreco like :cdTarifaPreco ");
			criteria.put("cdTarifaPreco", "%"+cdTarifaPreco+"%");
		}
		
		sql.append(" order by tfp.cdTarifaPreco ");

		criteria.put("idTabelaPreco", idTabelaPreco);

		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), criteria);
	}

}