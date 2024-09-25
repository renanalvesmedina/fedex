package com.mercurio.lms.portaria.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.portaria.model.OrdemSaida;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class OrdemSaidaDAO extends BaseCrudDao<OrdemSaida, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return OrdemSaida.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("usuario", FetchMode.JOIN);
		lazyFindById.put("usuario.vfuncionario", FetchMode.JOIN);
		lazyFindById.put("meioTransporteRodoviarioByIdSemiReboque",FetchMode.JOIN);
		lazyFindById.put("meioTransporteRodoviarioByIdMeioTransporte",FetchMode.JOIN);
		lazyFindById.put("meioTransporteRodoviarioByIdSemiReboque.meioTransporte",FetchMode.JOIN);
		lazyFindById.put("meioTransporteRodoviarioByIdMeioTransporte.meioTransporte",FetchMode.JOIN);
		lazyFindById.put("motorista", FetchMode.JOIN);
		lazyFindById.put("motorista.pessoa", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialOrigem", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialOrigem.pessoa", FetchMode.JOIN);
	}

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria, FindDefinition fDef) {
		SqlTemplate hql = this.getHqlFindPaginated(criteria);
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),fDef.getCurrentPage(),fDef.getPageSize(),hql.getCriteria());
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		SqlTemplate hql = this.getHqlFindPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
	}

	public SqlTemplate getHqlFindPaginated(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("new Map(OS.idOrdemSaida","idOrdemSaida");
		hql.addProjection("MT.nrFrota","meioTransporteRodoviarioByIdMeioTransporte_meioTransporte_nrFrota");
		hql.addProjection("MT.nrIdentificador","meioTransporteRodoviarioByIdMeioTransporte_meioTransporte_nrIdentificador");

		hql.addProjection("SR.nrFrota","meioTransporteRodoviarioByIdSemiReboque_meioTransporte_nrFrota");
		hql.addProjection("SR.nrIdentificador","meioTransporteRodoviarioByIdSemiReboque_meioTransporte_nrIdentificador");

		hql.addProjection("MP.tpIdentificacao","motorista_pessoa_tpIdentificacao");
		hql.addProjection("MP.nrIdentificacao","motorista_pessoa_nrIdentificacao");
		hql.addProjection("MP.nmPessoa","motorista_pessoa_nmPessoa");
		hql.addProjection("FUNC.nmFuncionario","usuario_vfuncionario_nmFuncionario");
		hql.addProjection("OS.dhRegistro","dhRegistro)");

		StringBuilder hqlfrom = new StringBuilder()
				.append(OrdemSaida.class.getName()).append(" OS ")
				.append(" inner join OS.usuario as U ")
				.append("  left join U.vfuncionario as FUNC ")
				.append(" inner join OS.meioTransporteRodoviarioByIdMeioTransporte as MTR ")
				.append(" inner join MTR.meioTransporte as MT ")
				.append("  left join OS.meioTransporteRodoviarioByIdSemiReboque as SRR ")
				.append("  left join SRR.meioTransporte as SR ")
				.append(" inner join OS.motorista as M ")
				.append(" inner join M.pessoa as MP ")
				.append(" inner join OS.filialByIdFilialOrigem as F ");

		hql.addFrom(hqlfrom.toString());

		hql.addCriteria("F.id","=", criteria.getLong("filialByIdFilialOrigem.idFilial"));
		hql.addCriteria("MTR.id","=", criteria.getLong("meioTransporteRodoviarioByIdMeioTransporte.idMeioTransporte"));
		hql.addCriteria("SRR.id","=", criteria.getLong("meioTransporteRodoviarioByIdSemiReboque.idMeioTransporte"));
		hql.addCriteria("M.id","=", criteria.getLong("motorista.idMotorista"));
		hql.addCriteria("U.id","=", criteria.getLong("usuario.idUsuario"));

		YearMonthDay dtRegistroInicial = criteria.getYearMonthDay("dtRegistroInicial");
		if (dtRegistroInicial != null) {
			hql.addCriteria("OS.dhRegistro.value",">=",JTDateTimeUtils.yearMonthDayToDateTime(dtRegistroInicial));
		}

		YearMonthDay dtRegistroFinal = criteria.getYearMonthDay("dtRegistroFinal");
		if (dtRegistroFinal != null) {
			hql.addCriteria("OS.dhRegistro.value","<",JTDateTimeUtils.yearMonthDayToDateTime(dtRegistroFinal.plusDays(1)));
		}

		hql.addOrderBy("F.sgFilial");
		hql.addOrderBy("MT.nrFrota");
		hql.addOrderBy("MP.nmPessoa");
		hql.addOrderBy("OS.dhRegistro.value");

		return hql;
	}

	/**
	 * Verifica se existe alguma ordem de saida impedindo a criacao de uma ordem nova para o meio de transporte,
	 * de acordo com a regra 3.1 da especificacao
	 * @param idOrdemSaida
	 * @param idMeioTransporte
	 * @return TRUE se nao existe registro impedindo, FALSE caso contrario
	 */
	public boolean verificaOrdemSaidaByMeioTransporte(Long idOrdemSaida, Long idMeioTransporte){
		DetachedCriteria dc = createDetachedCriteria();
		dc.setProjection(Projections.rowCount());

		if (idOrdemSaida != null)
			dc.add(Restrictions.ne("idOrdemSaida", idOrdemSaida));
		dc.add(Restrictions.eq("meioTransporteRodoviarioByIdMeioTransporte.idMeioTransporte", idMeioTransporte));
		dc.add(
			Restrictions.or(
				Restrictions.and(
						Restrictions.eq("blSemRetorno", Boolean.TRUE),
						Restrictions.isNull("dhSaida")
				),
				Restrictions.and(
						Restrictions.eq("blSemRetorno", Boolean.FALSE),
						Restrictions.isNull("dhChegada")
				)
			)
		);
		return !(((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0);
	}

	/**
	 * Consulta os dados relativos a ordem de saida que sao utilizados nas telas de chegada e saida da portaria
	 * @param idOrdemSaida
	 * @return
	 */
	public List findDadosChegadaSaida(Long idOrdemSaida) {
		SqlTemplate sql = new SqlTemplate();

		StringBuilder hqlProjection = new StringBuilder();
		hqlProjection.append("new Map(os.idOrdemSaida as idOrdemSaida,");
		hqlProjection.append("  mt.idMeioTransporte as idMeioTransporte,");
		hqlProjection.append("  mt.nrIdentificador as nrIdentificadorTransportado,");
		hqlProjection.append("  mt.nrFrota as nrFrotaTransportado,");
		hqlProjection.append("  tmt.dsTipoMeioTransporte as dsTipoMeioTransporte,");
		hqlProjection.append("  reb.nrIdentificador as nrIdentificadorReboque,");
		hqlProjection.append("  reb.nrFrota as nrFrotaReboque,");
		hqlProjection.append("  reb.idMeioTransporte as idReboque,");
		hqlProjection.append("  mot.idMotorista as idMotorista,");
		hqlProjection.append("  pes.nmPessoa as nmPessoa,");
		hqlProjection.append("  pes.nrIdentificacao as nrIdentificacao,");
		hqlProjection.append("  pes.tpIdentificacao as tpIdentificacao");
		hqlProjection.append(")");
		sql.addProjection(hqlProjection.toString());

		StringBuilder hqlFrom = new StringBuilder();
		hqlFrom.append(getPersistentClass().getName()).append(" os");
		hqlFrom.append(" inner join os.meioTransporteRodoviarioByIdMeioTransporte mtr inner join mtr.meioTransporte mt");
		hqlFrom.append(" inner join mt.modeloMeioTransporte mdl inner join mdl.tipoMeioTransporte tmt");
		hqlFrom.append(" inner join os.motorista mot inner join mot.pessoa pes");
		hqlFrom.append(" left  join os.meioTransporteRodoviarioByIdSemiReboque rebr left join rebr.meioTransporte reb");
		sql.addFrom(hqlFrom.toString());

		sql.addCriteria("os.idOrdemSaida", "=", idOrdemSaida);

		List retorno = getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
		return retorno;
	}

	/**
	 * Verifica se existe alguma ordem de saída aberta para o meio de transporte informado.
	 * 
	 * @param map
	 * @return TRUE se existe alguma ordem de saída aberta para o meio de transporte informado, FALSE caso contrário
	 */
	public List<OrdemSaida> findByMeioTransporteInOrdemSaida(Map map) {
		StringBuffer hql = new StringBuffer()
		.append("Select os ")
		.append("from ")
		.append(OrdemSaida.class.getName()).append(" as os ")
		.append("where 1=1 ");

		List param = new ArrayList();
		if (map.get("meioTransporteRodoviarioByIdMeioTransporte") != null) {
			Long idMeioTransporte = (Long)((Map)map.get("meioTransporteRodoviarioByIdMeioTransporte")).get("idMeioTransporte");
			hql.append("and os.meioTransporteRodoviarioByIdMeioTransporte.id = ? ");
			param.add(idMeioTransporte);
		}

		hql.append("and ( (os.blSemRetorno = ? and os.dhSaida.value is null) or (os.blSemRetorno = ? and os.dhChegada.value is null) ) ");
		param.add(Boolean.TRUE);
		param.add(Boolean.FALSE);

		return getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
	}

	/**
	 * Busca última ordem de saída do meio de transporte enviado por parâmetro com data
	 * maior que a data do controle de carga.
	 * @return
	 */
	public OrdemSaida findUltimaOrdemSaida(TypedFlatMap map){
		boolean isSemiReboque =  (Boolean) map.get("semiReboque");
		Long idMeioTransporte = (Long)map.get("idMeioTransporte");
		OrdemSaida ordemSaida = null;

		if (idMeioTransporte != null){
			SqlTemplate hql = new SqlTemplate();
			
			hql.addProjection(" os ");
			
			StringBuilder hqlfrom = new StringBuilder()
				.append(OrdemSaida.class.getName()).append(" os ")
				.append(" inner join os.filialByIdFilialOrigem fo ");
			
			if (isSemiReboque){
				hqlfrom.append(" left join os.meioTransporteRodoviarioByIdSemiReboque mtrs ");
				hqlfrom.append(" left join os.meioTransporteRodoviarioByIdMeioTransporte mtr ");
				hql.addCustomCriteria(" (mtrs.idMeioTransporte = " + idMeioTransporte + " OR mtr.idMeioTransporte = " +  idMeioTransporte + ")");
				
		} else {
				hqlfrom.append(" inner join os.meioTransporteRodoviarioByIdMeioTransporte mtr ");
				hql.addCriteria("mtr.idMeioTransporte"," = ", idMeioTransporte);
			}
				
			hql.addFrom(hqlfrom.toString());
			
			DateTime dhGeracao = new DateTime(map.get("dhGeracao"));

			if(dhGeracao != null){
				hql.addCriteria("os.dhRegistro.value", ">", dhGeracao);
		}
 
			
			hql.addOrderBy("os.dhRegistro.value", "desc");
			
			List<Map> retorno = getAdsmHibernateTemplate().find(hql.getSql(true), hql.getCriteria());
			
			if (retorno != null && !retorno.isEmpty()){
				ordemSaida = (OrdemSaida) retorno.get(0);
		}
			
	}

		return ordemSaida;	
	}

}