package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.HistoricoFilial;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class HistoricoFilialDAO extends BaseCrudDao<HistoricoFilial, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return HistoricoFilial.class;
	}

	/**
	 * Retorna o ultimo Histórico da Filial.
	 * @return 
	 */
	public HistoricoFilial findUltimoHistoricoFilial(Long idFilial) {
		HistoricoFilial hf = null;
		List rs = getHibernateTemplate().find("select max(hf.id) from "+HistoricoFilial.class.getName()+" as hf where hf.filial.idFilial = ?",idFilial);
		if (rs.size() > 0 && rs.get(0) != null) {
			Long idHistoricoFilial = (Long) rs.get(0);
				hf = (HistoricoFilial) findById(idHistoricoFilial);
		}
		return hf;
	}

	public HistoricoFilial getPenultimoHistoricoFilial(Long idFilial) {
		HistoricoFilial hf = null;
		List rs = getHibernateTemplate().findByNamedParam("select max(hf.id) from " + HistoricoFilial.class.getName() + " as hf where hf.filial.idFilial = :idFilial " +
				" and hf.id < (select max(hf2.id) from " + HistoricoFilial.class.getName() + " as hf2 where hf2.filial.idFilial = :idFilial) ",new String[] {"idFilial"},new Object[] {idFilial});
		if (rs.size() > 0 && rs.get(0) != null) {
			Long idHistoricoFilial = (Long) rs.get(0);
				hf = (HistoricoFilial) findById(idHistoricoFilial);
		}
		return hf;
	}

	public Filial findFilialMatriz(Long idEmpresa) {
		StringBuffer sb = new StringBuffer()
			.append("select filial from ").append(Filial.class.getName()).append(" as filial ")
			.append("inner join filial.pessoa as pessoa ")
			.append("inner join filial.empresa as empresa ")
			.append("inner join filial.historicoFiliais as lastHistoricoFilial ")
			.append("where lastHistoricoFilial.id = ")
			.append("	(select max(hf2.idHistoricoFilial) from ").append(HistoricoFilial.class.getName()).append(" as hf2 ")
			.append("    where hf2.filial = lastHistoricoFilial.filial) ")
			.append(" and empresa.idEmpresa = ? and lastHistoricoFilial.tpFilial = ?");

		Object[] parameterValues = new Object[]{idEmpresa, "MA"};

		return (Filial) getAdsmHibernateTemplate().findUniqueResult(sb.toString(), parameterValues);
	} 

	/**
	 * Consulta o hitórico vigente da filial informada por parâmetro.
	 * @param idFilial
	 * @return bean entidade HistoricoFilial
	 */
	public HistoricoFilial findHistoricoFilialVigente(Long idFilial) {
		Validate.notNull(idFilial, "idFilial cannot be null");
		YearMonthDay dtToday = JTDateTimeUtils.getDataAtual();

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "hf");
		dc.add(Restrictions.eq("hf.filial.id", idFilial));
		dc.add(Restrictions.le("hf.dtRealOperacaoInicial", dtToday));
		dc.add(Restrictions.or(Restrictions.gt("hf.dtRealOperacaoFinal", dtToday), Restrictions.isNull("hf.dtRealOperacaoFinal")));

		return (HistoricoFilial) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public boolean verificaExistenciaHistoricoFilial(Long idFilial,
			YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
//TODO
		StringBuilder hql = new StringBuilder();

		
		hql.append(" select 1 from (select min(coalesce(dt_real_operacao_inicial, dt_previsao_operacao_inicial, :min_date)) dtiniciooperacao, ");
		hql.append(" max(coalesce(dt_real_operacao_final, dt_previsao_operacao_final, :max_date)) dtFimOperacao ");
		hql.append(" from historico_filial ");
		hql.append(" where historico_filial.id_filial   =:idFilial ) t ");
		hql.append(" where t.dtiniciooperacao <= :dtInicial ");
		hql.append(" and (t.dtfimoperacao >= :dtInicial or t.dtfimoperacao is null) ");
		if (dtVigenciaFinal != null){
			hql.append(" and (t.dtFimOperacao >= :dtFinal or  t.dtfimoperacao is null )  ");
		}
		
		Map parameters = new HashMap();
		parameters.put("idFilial", idFilial);
		parameters.put("dtInicial", dtVigenciaInicial);
		parameters.put("dtFinal", dtVigenciaFinal);
		parameters.put("min_date", JTDateTimeUtils.MIN_YEARMONTHDAY);
		parameters.put("max_date", JTDateTimeUtils.MAX_YEARMONTHDAY);

		return this.getAdsmHibernateTemplate().getRowCountBySql(hql.toString(), parameters) > 0;
	}
	
	//recebe duas datas de vigencia: inicial e final e verifica se as datas estão no intervalo de vigencia das datas reais em "Historico Filial" para aquela filial.
	public boolean verificaVigenciasEmHistoricoFilial(Long idFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		StringBuffer hql = new StringBuffer();

		hql.append("from HistoricoFilial as f join fetch f.filial ");
		hql.append("where ");
		hql.append("( ");
		hql.append("	nvl(f.dtRealOperacaoInicial,f.dtPrevisaoOperacaoInicial) <= :dtInicial ");
		hql.append("	and ");
		hql.append("	(f.dtRealOperacaoFinal is null or f.dtRealOperacaoFinal >= :dtInicial) ");

		if (dtVigenciaFinal != null) {
			hql.append("    and ");
			hql.append("    (	 ");
			hql.append("	 	(nvl(f.dtRealOperacaoInicial, f.dtPrevisaoOperacaoInicial) < :dtFinal ");
			hql.append("	 	and ");
			hql.append("	 	f.dtRealOperacaoFinal is null or f.dtRealOperacaoFinal >= :dtFinal) ");
			hql.append("	) ");
		}

	 	hql.append(") ");

		hql.append("and f.filial.idFilial = :idFilial ");

		ArrayList campos = new ArrayList();
		campos.add("dtInicial");
		if (dtVigenciaFinal != null)
			campos.add("dtFinal");
		campos.add("idFilial");
		String[] camposStr = new String[campos.size()]; 
		camposStr = (String[]) campos.toArray(camposStr);

		ArrayList values = new ArrayList();
		values.add(dtVigenciaInicial);
		if (dtVigenciaFinal != null)
			values.add(dtVigenciaFinal);
		values.add(idFilial);

		List resultList = this.getAdsmHibernateTemplate().findByNamedParam(hql.toString(),camposStr, values.toArray());

		return resultList.size() > 0;
	} 

	/**
	 * Valida se a filial (filial do usuário) é MTZ (matriz)
	 * @param idFilial Identificador da filial do usuário
	 * @return <code>true</code> se a filial do usuário for MTZ, </code>false</code> caso contrário
	 */
	public boolean validateFilialUsuarioMatriz(Long idFilial){
		SqlTemplate sql = getQueryHqlHistoricoFilial(idFilial);

		sql.addCustomCriteria("hf.tpFilial = ?");
		sql.addCustomCriteria("(    hf.dtRealOperacaoInicial is not null " +
							" and hf.dtRealOperacaoFinal is not null " +
							" and (? between hf.dtRealOperacaoInicial and hf.dtRealOperacaoFinal) )");
		sql.addCriteriaValue("MA");
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());

		Integer size = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
		return size.compareTo(Integer.valueOf(0)) == 1;
	}

	/**
	 * Query padrão para Historico Filial
	 * @param idFilial Identificador da Filial
	 * @return SqlTemplate com a query de pesquisa de Historico Filial
	 */
	public SqlTemplate getQueryHqlHistoricoFilial(Long idFilial){
		SqlTemplate sql = new SqlTemplate();

		sql.addInnerJoin(HistoricoFilial.class.getName(), "hf");
		sql.addInnerJoin("hf.filial","f");

		sql.addCriteria("f.id","=",idFilial);

		return sql;
	}

	/**
	 * @author José Rodrigo Moraes
	 * @since 12/07/2006
	 * 
	 * Valida se a filial informada é do tipo informado.
	 * Verifica na tabela HISTORICO_FILIAL se a filial em questão é, por exemplo, Matriz, sucursal, etc.... 
	 * 
	 * @param idFilial Identificador da filial
	 * @param tpFilial Tipo da filial - DM_TIPO_FILIAL
	 * @return <code>true</code> Se a filial informada for do tipo informado e <code>false</code> caso contrário
	 * 
	 */
	public boolean validateFilialByTpFilial(Long idFilial, String tpFilial) {
		SqlTemplate sql = getQueryHqlHistoricoFilial(idFilial);

		sql.addProjection("count(hf.id)");

		sql.addCriteria("hf.tpFilial","=", tpFilial);
		sql.addCustomCriteria("(    hf.dtRealOperacaoInicial is not null " +
							" and hf.dtRealOperacaoFinal is not null " +
							" and (? between hf.dtRealOperacaoInicial and hf.dtRealOperacaoFinal) )");
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());

		List listFilial = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		if( listFilial != null && !listFilial.isEmpty() ){
			if ( Integer.parseInt(listFilial.get(0).toString()) != 0 ){ 
				return true;
			} else { 
				return false;
			}
		} else {
			return false;
		}
	}

	public List<HistoricoFilial> findHistoricosFilialByEmpresa(Long idEmpresa, String tpEmpresa) {
		StringBuilder sql = new StringBuilder()
		.append("select hf \n")
		.append("from ").append(getPersistentClass().getName()).append(" hf \n")
		.append("where hf.tpFilial = ? \n")
		.append(" and hf.filial.empresa.id = ? \n")
		.append(" and hf.dtRealOperacaoInicial <= ? \n")
		.append(" and hf.dtRealOperacaoFinal >= ? \n");
		return getAdsmHibernateTemplate().find(sql.toString(), new Object[]{tpEmpresa,idEmpresa,JTDateTimeUtils.getDataAtual(),JTDateTimeUtils.getDataAtual()});		
	}

	
	/**
	 * Verifica o tipo da filial em determinada data
	 * 
	 * @param idFilial
	 * @param dataPgto
	 * @param tpFilial
	 * @return 
	 */
	public Boolean findTpFilialVigenteData(Long idFilial, YearMonthDay dataPgto, String tpFilial) {
		StringBuilder sql = new StringBuilder()
		.append("select hf \n")
		.append("from ").append(getPersistentClass().getName()).append(" hf \n")
		.append("where hf.tpFilial = ? \n")
		.append(" and hf.filial.id = ? \n")
		.append(" and hf.dtRealOperacaoInicial <= ? \n")
		.append(" and hf.dtRealOperacaoFinal >= ? \n");
		
		HistoricoFilial historicoFilial = (HistoricoFilial) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[] { tpFilial, idFilial, dataPgto, dataPgto });
		if (null != historicoFilial) {
			return true;
		}

		return false;

	}	
	
}