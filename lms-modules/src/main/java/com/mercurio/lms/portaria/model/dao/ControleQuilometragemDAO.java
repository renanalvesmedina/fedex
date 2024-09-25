package com.mercurio.lms.portaria.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.portaria.model.ControleQuilometragem;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ControleQuilometragemDAO extends BaseCrudDao<ControleQuilometragem, Long> {
	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
	protected final Class getPersistentClass() {
		return ControleQuilometragem.class;
	}

	public Map findByIdTela(Long idControleQuilometragem) {
		StringBuffer hql = new StringBuffer()
			.append(" select new Map( ")
			.append("	Q.idControleQuilometragem as idControleQuilometragem, ")
			.append("	F.idFilial as filial_idFilial, ")
			.append("	F.sgFilial as filial_sgFilial ,")
			.append("	FP.nmFantasia as filial_pessoa_nmFantasia, ")
			.append("	MTR.idMeioTransporte as meioTransporteRodoviario_idMeioTransporte, ")
			.append("	MT.nrFrota as meioTransporteRodoviario_meioTransporte_nrFrota, ")
			.append("	MT.nrIdentificador as meioTransporteRodoviario_meioTransporte_nrIdentificador, ")
			.append("	MT.tpVinculo as meioTransporteRodoviario_meioTransporte_tpVinculo, ")
			.append("	CC.idControleCarga as controleCarga_idControleCarga, ")
			.append("	FCC.sgFilial as controleCarga_filialByIdFilialOrigem_sgFilial, ")
			.append("	CC.nrControleCarga as controleCarga_nrControleCarga, ")
			.append("	Q.dhMedicao as dhMedicao, ")
			.append("	UU.nrMatricula as usuarioByIdUsuario_nrMatricula, ")
			.append("	UU.nmUsuario as usuarioByIdUsuario_nmUsuario, ")
			.append("	UU.idUsuario as usuarioByIdUsuario_idUsuario, ")
			.append("	Q.dhCorrecao as dhCorrecao, ")
			.append("	UC.nrMatricula as usuarioByIdUsuarioCorrecao_nrMatricula, ")
			.append("	UC.nmUsuario as usuarioByIdUsuarioCorrecao_nmUsuario, ")
			.append("	UC.idUsuario as usuarioByIdUsuarioCorrecao_idUsuario, ")
			.append("	Q.blSaida as blSaida, ")
			.append("	Q.blVirouHodometro as blVirouHodometro, ")
			.append("	Q.nrQuilometragem as nrQuilometragem, ")
			.append("	Q.obControleQuilometragem as obControleQuilometragem, ")
			.append("   Q.tpSituacaoPendencia as controleQuilometragem_tpSituacaoPendencia ")
			.append(" ) from " + getPersistentClass().getName() + " Q ")

			.append(" inner join Q.filial as F ")
			.append(" inner join F.pessoa as FP ")
			.append(" inner join Q.meioTransporteRodoviario as MTR ")
			.append(" inner join MTR.meioTransporte as MT ")
			.append(" left join Q.controleCarga as CC ")
			.append(" left join CC.filialByIdFilialOrigem as FCC ")
			.append(" inner join Q.usuarioByIdUsuario as UU ")
			.append(" left join Q.usuarioByIdUsuarioCorrecao as UC ")


			.append(" where Q.idControleQuilometragem = ? ");

		List l = getAdsmHibernateTemplate().find(hql.toString(),idControleQuilometragem);

		return l.isEmpty() ? new HashMap() : (Map)l.get(0); 
	}

	public ResultSetPage findPaginatedTela(TypedFlatMap criteria, FindDefinition fDef) {
		SqlTemplate sql = getSQLTemplatetoFindPaginated(criteria);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(true),fDef.getCurrentPage(),fDef.getPageSize(),sql.getCriteria());
	}

	public Integer getRowCountTela(TypedFlatMap criteria) {
		SqlTemplate sql = getSQLTemplatetoFindPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
	}

	private SqlTemplate getSQLTemplatetoFindPaginated(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();

		StringBuffer hqlProjection = new StringBuffer()
			.append("new Map(")
			.append("	Q.idControleQuilometragem as idControleQuilometragem, ")
			.append("	MT.nrFrota as nrFrota, ")
			.append("	MT.nrIdentificador as nrIdentificador, ")
			.append("	CC.nrControleCarga as nrControleCarga, ")
			.append("	FCC.sgFilial as sgFilialControleCarga, ")
			.append("	Q.dhMedicao as dhMedicao, ")
			.append("	Q.blSaida as blSaida, ")
			.append("	F.sgFilial as sgFilial, ")
			.append("	P.nmFantasia as nmFantasia, ")
			.append("	Q.nrQuilometragem as nrQuilometragem )");
		sql.addProjection(hqlProjection.toString());

		StringBuffer hqlFrom = new StringBuffer()
			.append(getPersistentClass().getName())
			.append(" Q ")
			.append(" inner join Q.filial as F ")
			.append(" inner join F.pessoa as P ")
			.append(" inner join Q.meioTransporteRodoviario as MTR ")
			.append(" inner join MTR.meioTransporte as MT ")
			.append(" inner join Q.controleCarga as CC ")
			.append(" left join CC.filialByIdFilialOrigem as FCC ");
		sql.addFrom(hqlFrom.toString());

		sql.addCriteria("F.idFilial","=",criteria.getLong("filial.idFilial"));
		sql.addCriteria("MTR.idMeioTransporte","=",criteria.getLong("meioTransporteRodoviario.idMeioTransporte"));
		sql.addCriteria("CC.idControleCarga","=",criteria.getLong("controleCarga.idControleCarga"));
		sql.addCriteria("Q.blSaida", "=", criteria.getBoolean("blSaida"));
		
		List statusCC = new ArrayList();
		statusCC.add("CA");
		statusCC.add("GE");
		
		sql.addCriteriaNotIn("CC.tpStatusControleCarga", statusCC);
		if (criteria.getYearMonthDay("dtMedicaoInicial") != null) {
			sql.addCriteria("trunc(cast(Q.dhMedicao.value as date))",">=", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("dtMedicaoInicial")));
		}
		if (criteria.getYearMonthDay("dtMedicaoFinal") != null) {
			sql.addCriteria("trunc(cast(Q.dhMedicao.value as date))","<=", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("dtMedicaoFinal")));
		}

		sql.addOrderBy("F.sgFilial, MT.nrFrota, Q.dhMedicao.value desc");

		return sql;
	}


	/**
	 * Consulta pr�xima quilometragem a partir de um meio de transporte.
	 * 
	 * @param idMeioTransporte id do meio de Transporte
	 * @param idControleQuilometragem id do Controle a desconsiderar.
	 * @return Integer com o n�mero da Quilometragem encontrada.
	 */
	public ControleQuilometragem findQuilometragemReferenciaMeioTransporte(Long idMeioTransporte, Long idControleCarga) {
		StringBuffer sb = new StringBuffer()
		.append(" select q ")
		.append(" from " + getPersistentClass().getName() +" q ")
		.append(" where q.controleCarga.id = ? ")
		.append(" and q.meioTransporteRodoviario.id = ? ")
		.append(" and q.blSaida = 'S' ");
		
		List params = new ArrayList(2);
		params.add(idControleCarga);
		params.add(idMeioTransporte);
		
		sb.append(" order by q.dhMedicao.value desc q.id desc ");
		
		List l = getAdsmHibernateTemplate().find(sb.toString(),params.toArray());
		
		return l.isEmpty() ? null : (ControleQuilometragem)l.get(0);
	}

	public ControleQuilometragem findUltimaQuilometragemByMeioTransporte(Long idMeioTransporte, DateTime dhMedicao, boolean anterior) {
		StringBuffer sb = new StringBuffer()
			.append(" select q ")
			.append(" from " + getPersistentClass().getName() +" q ")
			.append(" where ")
			.append(" q.meioTransporteRodoviario.id = ? ");
		if( anterior ){
			sb.append(" and q.dhMedicao.value < ? ");
		}else{
			sb.append(" and q.dhMedicao.value > ? ");
		}

		List params = new ArrayList(2);
		params.add(idMeioTransporte);
		params.add(dhMedicao);

		sb.append(" order by q.dhMedicao.value desc ");

		List l = getAdsmHibernateTemplate().find(sb.toString(),params.toArray());

		return l.isEmpty() ? null : (ControleQuilometragem)l.get(0);
	}

	/**
	 * M�todo que retorna um ControleQuilometragem a partir do ID do Controle de Carga, 
	 * do ID da Filial e do blSaida. No caso de um Controle de Carga de Coleta/Entrega, o
	 * idFilial � a filial de origem deste Controle de Carga.
	 * @param idControleCarga
	 * @param idFilial
	 * @return
	 */
	public ControleQuilometragem findControleQuilometragemByIdControleCargaByIdFilial(Long idControleCarga, Long idFilial, Boolean blSaida) {
		StringBuffer hql = new StringBuffer();
		List parameters = new ArrayList();

		// Se houver necessidade de joins, colocar no FROM tomando cuidado com os campos nullable.
		hql.append(" from ").append(ControleQuilometragem.class.getName()).append(" cq ");
		hql.append(" where cq.controleCarga.id = ? ");
		hql.append(" and cq.filial.id = ? ");
		hql.append(" and cq.blSaida = ? ");
		parameters.add(idControleCarga);
		parameters.add(idFilial);
		parameters.add(blSaida);
		List lista = getAdsmHibernateTemplate().find(hql.toString(), parameters.toArray());
		return lista.isEmpty() ? null : (ControleQuilometragem)lista.get(0);
	}

	/**
	 * Busca um controle de quilometragem a partir dos par�metros informados.
	 * 
	 * @param idMeioTransporte Identificador do meio de transporte.
	 * @param dhMedicao Data/hora de medi��o.
	 * @param blSaida Indicador de Saida na portaria.
	 * @return uma inst�ncia de ControleQuilometragem caso encontrado. Sen�o, retora null.
	 */
	public ControleQuilometragem findControleQuilometragem(Long idMeioTransporte,
			DateTime dhMedicao, Boolean blSaida) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"CQ");
		dc.add(Restrictions.eq("CQ.meioTransporteRodoviario.id",idMeioTransporte));
		dc.add(Restrictions.eq("CQ.dhMedicao.value",dhMedicao));
		dc.add(Restrictions.eq("CQ.blSaida",blSaida));
		
		return (ControleQuilometragem) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	
	public ControleQuilometragem getControleQuilometragem(Long idMeioTransporte,
			DateTime dhMedicao, Boolean blSaida) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(getPersistentClass().getName() + " CQ ");
		hql.addCriteria("CQ.meioTransporteRodoviario.id", "=", idMeioTransporte);
		hql.addCriteria("cast(CQ.dhMedicao.value as date)", "=", dhMedicao);
		hql.addCriteria("CQ.blSaida", "=", blSaida);
		List result = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if(result.isEmpty()){
			return null;
		}
		return (ControleQuilometragem) result.get(0);
	}

	public ControleQuilometragem findUltimoControleQuilometragemByIdControleCarga(Long idControleCarga, Boolean blSaida) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		hql.append(" select coqu "); 
		hql.append(" from ControleQuilometragem coqu ");
		hql.append(" where coqu.idControleQuilometragem = ( ");
		hql.append(" 	select max(coquMax.idControleQuilometragem) ");
		hql.append(" 	from ControleQuilometragem coquMax ");
		hql.append(" 	where coquMax.controleCarga.idControleCarga = ? ");
		hql.append(" 	and coquMax.blSaida = ? ) ");
		
		params.add(idControleCarga);
		params.add(blSaida);
		
		return (ControleQuilometragem) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params.toArray());
		
	}
	
	public List<Map<String,Object>> findQuilometragemExcedenteRota(Map<String, Object> parameters){
		StringBuilder sql = new StringBuilder();				
		
		sql.append("SELECT f.sg_filial AS \"Filial da Rota\",");
		sql.append("  rce.nr_rota AS \"N�mero da Rota\",");
		sql.append("  rce.ds_rota AS \"Descri��o da Rota\",");
		sql.append("  (f.sg_filial || ' ' || cc.nr_controle_carga) AS \"Controle de Carga\",");
		sql.append("  mt.nr_frota || '/' || mt.nr_identificador AS \"Frota/Ve�culo\",");
		sql.append("  rce.nr_km AS \"KM Rota\",");
		sql.append("  c1.nr_quilometragem AS \"KM Sa�da\",");
		sql.append("  CASE c1.bl_virou_hodometro WHEN 'S' THEN 'Sim' ELSE 'N�o' END AS \"Virou Hod�metro Sa�da\",");		
		sql.append("  c2.nr_quilometragem AS \"KM Retorno\",");
		sql.append("  CASE c2.bl_virou_hodometro WHEN 'S' THEN 'Sim' ELSE 'N�o' END AS \"Virou Hod�metro Retorno\",");		
		sql.append("  (CASE c2.bl_virou_hodometro");
		sql.append("    WHEN 'N' THEN ((c2.nr_quilometragem - c1.nr_quilometragem))");
		sql.append("    WHEN 'S' THEN (((c2.nr_quilometragem - c1.nr_quilometragem) + 1000000))");
		sql.append("  END) AS \"KM Percorrido\",");
		sql.append("  (CASE c2.bl_virou_hodometro");
		sql.append("    WHEN 'N' THEN ((c2.nr_quilometragem - c1.nr_quilometragem) - rce.nr_km)");
		sql.append("    WHEN 'S' THEN (((c2.nr_quilometragem - c1.nr_quilometragem) + 1000000) - rce.nr_km)");
		sql.append("  END) AS \"KM Excedente\",");
		sql.append("  (CAST(c2.dh_medicao AS TIMESTAMP) - CAST(c1.dh_medicao AS TIMESTAMP)) AS \"Tempo entre Medi��es\",");
		sql.append("  TO_CHAR(c1.dh_medicao,'DD/MM/YYYY HH24:MI') AS \"Data/Hora Medi��o Sa�da\",");
		sql.append("  ua1.nm_usuario AS \"Usu�rio Medi��o Sa�da\",");
		sql.append("  TO_CHAR(c2.dh_medicao,'DD/MM/YYYY HH24:MI') AS \"Data/Hora Medi��o Retorno\",");
		sql.append("  ua2.nm_usuario AS \"Usu�rio Medi��o Retorno\"");
		sql.append(" FROM controle_quilometragem c1");
		sql.append(" LEFT JOIN controle_quilometragem c2");
		sql.append("  ON c1.id_controle_carga = c2.id_controle_carga");
		sql.append(" LEFT JOIN controle_carga cc");
		sql.append("  ON cc.id_controle_carga = c1.id_controle_carga");
		sql.append(" LEFT JOIN rota_coleta_entrega rce");
		sql.append("  ON rce.id_rota_coleta_entrega = cc.id_rota_coleta_entrega");
		sql.append(" LEFT JOIN meio_transporte mt");
		sql.append("  ON mt.id_meio_transporte = cc.id_transportado");
		sql.append(" LEFT JOIN usuario_adsm ua1");
		sql.append("  ON ua1.id_usuario = c1.id_usuario");
		sql.append(" LEFT JOIN usuario_adsm ua2");
		sql.append("  ON ua2.id_usuario = c2.id_usuario");
		sql.append(" LEFT JOIN filial f");
		sql.append("  ON f.id_filial = c1.id_filial");
		sql.append(" WHERE (c1.bl_saida = 'S' AND c2.bl_saida = 'N')");
		
		if(parameters.containsKey("idFilial")){
			sql.append(" AND f.id_filial = :idFilial");
		}
		
		if(parameters.containsKey("dhPeriodoMedicaoInicial") && parameters.containsKey("dhPeriodoMedicaoFinal")){
			sql.append(" AND (c2.dh_medicao >= :dhPeriodoMedicaoInicial AND c2.dh_medicao <= :dhPeriodoMedicaoFinal)");
		}
		
		if(parameters.containsKey("idRotaColetaEntrega")){
			sql.append(" AND rce.id_rota_coleta_entrega = :idRotaColetaEntrega");
		}
		
		if(parameters.containsKey("idControleCarga")){
			sql.append(" AND c1.id_controle_carga = :idControleCarga");
		}
		
		if(parameters.containsKey("idMeioTransporte")){
			sql.append(" AND mt.id_meio_transporte = :idMeioTransporte");
		}
		
		sql.append(" ORDER BY rce.nr_rota ASC, mt.nr_identificador ASC, c2.dh_medicao DESC");
				
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("Filial da Rota", Hibernate.STRING);
				sqlQuery.addScalar("N�mero da Rota");
				sqlQuery.addScalar("Descri��o da Rota");
				sqlQuery.addScalar("Controle de Carga");
				sqlQuery.addScalar("Frota/Ve�culo", Hibernate.STRING);
				sqlQuery.addScalar("KM Rota");
				sqlQuery.addScalar("KM Sa�da");
				sqlQuery.addScalar("Virou Hod�metro Sa�da", Hibernate.STRING);
				sqlQuery.addScalar("KM Retorno");
				sqlQuery.addScalar("Virou Hod�metro Retorno", Hibernate.STRING);
				sqlQuery.addScalar("KM Percorrido", Hibernate.LONG);
				sqlQuery.addScalar("KM Excedente", Hibernate.LONG);
				sqlQuery.addScalar("Tempo entre Medi��es", Hibernate.STRING);
				sqlQuery.addScalar("Data/Hora Medi��o Sa�da");
				sqlQuery.addScalar("Usu�rio Medi��o Sa�da");
				sqlQuery.addScalar("Data/Hora Medi��o Retorno");
				sqlQuery.addScalar("Usu�rio Medi��o Retorno");		
			}
		};
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), parameters, csq);
	}

}