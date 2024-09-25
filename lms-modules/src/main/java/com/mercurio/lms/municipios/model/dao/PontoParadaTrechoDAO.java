package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.PontoParadaTrecho;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PontoParadaTrechoDAO extends BaseCrudDao<PontoParadaTrecho, Long> {

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("pontoParada",FetchMode.JOIN);
		lazyFindById.put("pontoParada.municipio",FetchMode.JOIN);
		lazyFindById.put("pontoParada.municipio.unidadeFederativa",FetchMode.JOIN);
		lazyFindById.put("pontoParada.municipio.unidadeFederativa.pais",FetchMode.JOIN);
		lazyFindById.put("pontoParada.rodovia",FetchMode.JOIN);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return PontoParadaTrecho.class;
	}

	public Map findByIdExtra(Long id) {
		StringBuffer hql = new StringBuffer()
		.append(" select new Map( ")
		.append(" PP.idPontoParada as pontoParada_idPontoParada, ")
		.append(" PP.nmPontoParada as pontoParada_nmPontoParada, ")
		.append(" M.nmMunicipio as pontoParada_municipio_nmMunicipio, ")
		.append(" UF.sgUnidadeFederativa as pontoParada_municipio_unidadeFederativa_sgUnidadeFederativa, ")
		.append(" P.nmPais as pontoParada_municipio_unidadeFederativa_pais_nmPais, ")
		.append(" R.sgRodovia as pontoParada_rodovia_sgRodovia, ")
		.append(" R.dsRodovia as pontoParada_rodovia_dsRodovia, ")
		.append(" PP.nrKm as pontoParada_nrKm ) from ")
		.append(PontoParadaTrecho.class.getName() + " as PPT ")
		.append("left join PPT.pontoParada as PP ")
		.append("left join PPT.trechoRotaIdaVolta TRIV ")
		.append("left join PP.municipio as M ")
		.append("left join M.unidadeFederativa UF ")
		.append("left join UF.pais as P ")
		.append("left join PP.rodovia as R ")
		.append("where PPT.idPontoParadaTrecho = ? ");

		return (Map)getAdsmHibernateTemplate().find(hql.toString(),id).get(0);
	}

	public ResultSetPage findPaginatedWithLocalInformation(TypedFlatMap criteria,FindDefinition findDef) {
		SqlTemplate sql = this.getSqlTemplate(criteria);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),
				findDef.getPageSize(),sql.getCriteria());
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		SqlTemplate sql = this.getSqlTemplate(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(PontoParadaTrecho.class.getName() + " as PPT " +
				"inner join fetch PPT.pontoParada as PP " +
				"inner join fetch PPT.trechoRotaIdaVolta TRIV " +
				"inner join fetch PP.municipio as M " +
				"inner join fetch M.unidadeFederativa UF " +
				"inner join fetch UF.pais as P " +
				" left join fetch PP.rodovia as R ");

		hql.addOrderBy("PP.nmPontoParada");
		hql.addOrderBy("PPT.dtVigenciaInicial");

		hql.addCriteria("TRIV.idTrechoRotaIdaVolta","=",criteria.getLong("trechoRotaIdaVolta.idTrechoRotaIdaVolta"));
		hql.addCriteria("PP.idPontoParada","=",criteria.getLong("pontoParada.idPontoParada"));
		hql.addCriteria("PPT.dtVigenciaInicial",">=",criteria.getYearMonthDay("dtVigenciaInicial"));
		hql.addCriteria("PPT.dtVigenciaFinal","<=",criteria.getYearMonthDay("dtVigenciaFinal"));

		return hql;
	}

	public boolean validateDuplicatedPonto(Long id, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal,
			Long idPontoParada, Long idTrechoRotaIdaVolta) {
		DetachedCriteria dc = JTVigenciaUtils.getDetachedVigencia(getPersistentClass(),id,dtVigenciaInicial,dtVigenciaFinal);

		dc.setProjection(Projections.property("idPontoParadaTrecho"));

		dc.createAlias("pontoParada","p");
		dc.createAlias("trechoRotaIdaVolta","t");
		dc.add(Restrictions.eq("p.idPontoParada",idPontoParada));
		dc.add(Restrictions.eq("t.idTrechoRotaIdaVolta",idTrechoRotaIdaVolta));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc).size() > 0;
	}

	public List findToConsultarRotas(Long idTrechoRotaIdaVolta) {
		StringBuffer hql = new StringBuffer()
			.append(" select new Map( ")
			.append("   PPT.idPontoParadaTrecho as idPontoParadaTrecho, ")
			.append("   PP.nmPontoParada as dsLocalidade, ")
			.append("   MU.nmMunicipio as nmMunicipio, ")
			.append("   UF.sgUnidadeFederativa as sgUnidadeFederativa, ")
			.append("   PA.nmPais as nmPais, ")
			.append("	RO.sgRodovia as rodovia_sgRodovia, ")
			.append("   RO.dsRodovia as rodovia_dsRodovia, ")
			.append("   PP.nrKm as pontoParada_nrKm, ")
			.append("   PPT.nrTempoParada as nrTempoParada, ")
			.append("   PPT.nrOrdem as nrOrdem, ")
			.append("   PPT.dtVigenciaInicial as dtVigenciaInicial, ")
			.append("   PPT.dtVigenciaFinal as dtVigenciaFinal ")
			.append(" ) ")
			.append(" from " + PontoParadaTrecho.class.getName() + " PPT ")
			.append(" left join PPT.pontoParada as PP ")
			.append(" left join PPT.trechoRotaIdaVolta as TRIV ")
			.append(" left join PP.municipio as MU ")
			.append(" left join MU.unidadeFederativa as UF ")
			.append(" left join UF.pais as PA ")
			.append(" left join PP.rodovia as RO ")

			.append(" where TRIV.idTrechoRotaIdaVolta = ? ")
			.append(" order by PPT.nrOrdem ");

		return getAdsmHibernateTemplate().find(hql.toString(),idTrechoRotaIdaVolta);
	}

	/**
	 * 
	 * @param idRotaIdaVolta
	 * @return
	 */
	public List findToGerarControleCarga(Long idRotaIdaVolta) {
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		StringBuffer sql = new StringBuffer()
		.append("select new Map( ")
		.append("filialOrigem.sgFilial as sgFilialOrigem, ")
		.append("filialDestino.sgFilial as sgFilialDestino, ")
		.append("rodovia.sgRodovia as sgRodovia, ")
		.append("rodovia.dsRodovia as dsRodovia, ")
		.append("pp.nrKm as nrKm, ")
		.append("municipio.nmMunicipio as nmMunicipio, ")
		.append("uf.sgUnidadeFederativa as sgUf, ")
		.append("ppt.nrTempoParada as nrTempoParada, ")
		.append("enderecoPessoa.nrLatitude as nrLatitude, ")
		.append("enderecoPessoa.nrLongitude as nrLongitude, ")
		.append("ppt.idPontoParadaTrecho as idPontoParadaTrecho ")
		.append(") ")

		.append("from ").append(PontoParadaTrecho.class.getName()).append(" ppt ")
		.append("left join ppt.pontoParada as pp ")
		.append("left join ppt.trechoRotaIdaVolta as triv ")
		.append("left join triv.filialRotaByIdFilialRotaOrigem as filialRotaOrigem ")
		.append("left join filialRotaOrigem.filial as filialOrigem ")
		.append("left join triv.filialRotaByIdFilialRotaDestino as filialRotaDestino ")
		.append("left join filialRotaDestino.filial as filialDestino ")
		.append("left join pp.rodovia as rodovia ")
		.append("left join pp.municipio as municipio ")
		.append("left join municipio.unidadeFederativa as uf ")
		.append("left join pp.pessoa as pessoa ")
		.append("left join pessoa.enderecoPessoa as enderecoPessoa ")

		.append("where triv.rotaIdaVolta.id = ? ")
		.append("and ? between ppt.dtVigenciaInicial and ppt.dtVigenciaFinal ")
		.append("order by ppt.nrOrdem ");

		List param = new ArrayList();
		param.add(idRotaIdaVolta);
		param.add(dataAtual);
		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}

	public Integer getRowCountByTrechoRotaIdaVolta(Long idTrechoRotaIdaVolta) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ppt");
		dc.setProjection(Projections.rowCount());
		dc.add(Restrictions.eq("ppt.trechoRotaIdaVolta.id", idTrechoRotaIdaVolta));

		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

}