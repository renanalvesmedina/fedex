package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.BaseCompareVarcharI18n;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.PostoPassagem;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.TarifaPostoPassagem;
import com.mercurio.lms.municipios.model.ValorTarifaPostoPassagem;
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
public class PostoPassagemDAO extends BaseCrudDao<PostoPassagem, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return PostoPassagem.class;
	}

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("municipio",FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa",FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa.pais",FetchMode.JOIN);
		fetchModes.put("rodovia",FetchMode.JOIN);
		fetchModes.put("concessionaria",FetchMode.JOIN);
		fetchModes.put("concessionaria.pessoa",FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("municipio",FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa",FetchMode.JOIN);
		fetchModes.put("municipio.unidadeFederativa.pais",FetchMode.JOIN);
		fetchModes.put("rodovia",FetchMode.JOIN);
		fetchModes.put("concessionaria",FetchMode.JOIN);
		fetchModes.put("concessionaria.pessoa",FetchMode.JOIN);
	}

	public java.util.List findLookupByFormaCobranca(Map criteria) {
		StringBuffer sb = new StringBuffer(150);
		sb.append("FROM ").append(PostoPassagem.class.getName()).append(" AS pp ")
			.append("inner join fetch pp.municipio ")
			.append("inner join fetch pp.rodovia ")
			.append("WHERE pp.tpPostoPassagem in(SELECT dv.value FROM ").append(DomainValue.class.getName()).append(" AS dv WHERE ")
			.append(BaseCompareVarcharI18n.hqlLike("dv.description",LocaleContextHolder.getLocale(),true)).append(" AND ")
			.append("dv.domain.name = '").append(ReflectionUtils.getNestedBeanPropertyValue(criteria,"domain.name"))
			.append("')");
		return getAdsmHibernateTemplate().find(sb.toString(),ReflectionUtils.getNestedBeanPropertyValue(criteria,"tpPostoPassagem.description"));
	}

	//verifica se existe as mesmas informaçoes vigentes(municipio,rodovia,nrKm)
	public boolean findPostoPassagemVigente(PostoPassagem postoPassagem){
		DetachedCriteria dc = createDetachedCriteria();
		if(postoPassagem.getIdPostoPassagem()!= null){
			dc.add(Restrictions.ne("idPostoPassagem",postoPassagem.getIdPostoPassagem()));
		}
		dc.add(Restrictions.eq("municipio.idMunicipio", postoPassagem.getMunicipio().getIdMunicipio()));
		dc.add(Restrictions.eq("tpPostoPassagem",postoPassagem.getTpPostoPassagem().getValue()));

		if (postoPassagem.getRodovia() != null)
			dc.add(Restrictions.eq("rodovia.idRodovia",postoPassagem.getRodovia().getIdRodovia()));
		if (postoPassagem.getNrKm() != null)
			dc.add(Restrictions.eq("nrKm",postoPassagem.getNrKm()));
		dc = JTVigenciaUtils.getDetachedVigencia(dc,postoPassagem.getDtVigenciaInicial(),postoPassagem.getDtVigenciaFinal());
		return findByDetachedCriteria(dc).size()>0;
	}

	public boolean findPostoPassagemByVigencias(Long idPostoPassagem, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("idPostoPassagem",idPostoPassagem));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigenciaInicial));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigenciaFinal)));
		return findByDetachedCriteria(dc).size()==0;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		// TODO Auto-generated method stub
		SqlTemplate sql = createSqlTemplateToFindPaginated(criteria);
		sql.addProjection("new map(PP.tpPostoPassagem AS tpPostoPassagem");
		sql.addProjection("MU.nmMunicipio AS municipio_nmMunicipio");
		sql.addProjection("MU.idMunicipio AS municipio_idMunicipio");
		sql.addProjection("UF.sgUnidadeFederativa AS municipio_unidadeFederativa_sgUnidadeFederativa");
		sql.addProjection("UF.nmUnidadeFederativa AS municipio_unidadeFederativa_nmUnidadeFederativa");
		sql.addProjection("UF.idUnidadeFederativa AS municipio_unidadeFederativa_idUnidadeFederativa");
		sql.addProjection("PA.nmPais AS municipio_unidadeFederativa_pais_nmPais");
		sql.addProjection("PA.idPais AS municipio_unidadeFederativa_pais_idPais");
		sql.addProjection("RO.sgRodovia AS rodovia_sgRodovia");
		sql.addProjection("RO.idRodovia AS rodovia_igRodovia");
		sql.addProjection("PP.nrKm AS nrKm");
		sql.addProjection("PP.tpSentidoCobranca AS tpSentidoCobranca");
		sql.addProjection("PP.idPostoPassagem AS idPostoPassagem");
		sql.addProjection("PP.dtVigenciaInicial AS dtVigenciaInicial");
		sql.addProjection("PP.dtVigenciaFinal AS dtVigenciaFinal");

		sql.addProjection("PS.nmPessoa AS concessionaria_pessoa_nmPessoa");
		sql.addProjection("PS.tpIdentificacao AS concessionaria_pessoa_tpIdentificacao");
		sql.addProjection("PS.nrIdentificacao AS concessionaria_pessoa_nrIdentificacao)");

		//Ordder
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("PA.nmPais",LocaleContextHolder.getLocale()));
		sql.addOrderBy("UF.sgUnidadeFederativa");
		sql.addOrderBy("MU.nmMunicipio");
		sql.addOrderBy("RO.sgRodovia");
		sql.addOrderBy("PP.nrKm");
		sql.addOrderBy("PP.dtVigenciaInicial");

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate sql = createSqlTemplateToFindPaginated(criteria);
		sql.addProjection("count(*)");

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
		return result.intValue();
	}

	private SqlTemplate createSqlTemplateToFindPaginated(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();

		sql.addFrom((new StringBuffer(PostoPassagem.class.getName())).append(" AS PP ")
							.append("INNER JOIN PP.municipio AS MU ")
							.append("INNER JOIN MU.unidadeFederativa AS UF ")
							.append("INNER JOIN UF.pais AS PA ")
							.append("LEFT JOIN PP.rodovia AS RO ")
							.append("INNER JOIN PP.concessionaria AS CO ")
							.append("INNER JOIN CO.pessoa AS PS ").toString());

		if (StringUtils.isNotBlank(criteria.getString("tpPostoPassagem")))
			sql.addCriteria("PP.tpPostoPassagem","=",criteria.getString("tpPostoPassagem"));

		if (StringUtils.isNotBlank(criteria.getString("tpSentidoCobranca")))
			sql.addCriteria("PP.tpSentidoCobranca","=",criteria.getString("tpSentidoCobranca"));

		if (StringUtils.isNotBlank(criteria.getString("tpFormaCobranca"))) {
			sql.addCustomCriteria(new StringBuffer("EXISTS (SELECT TPP.id FROM ").append(TarifaPostoPassagem.class.getName()).append(" AS TPP ")
				.append("WHERE TPP.tpFormaCobranca = ? AND TPP.dtVigenciaInicial <= ? AND TPP.dtVigenciaFinal >= ? ")
				.append("AND TPP.postoPassagem.id = PP.id)").toString());
			sql.addCriteriaValue(criteria.getString("tpFormaCobranca"));
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		}

		sql.addCriteria("MU.idMunicipio","=",criteria.getLong("municipio.idMunicipio"));
		sql.addCriteria("UF.idUnidadeFederativa","=",criteria.getLong("municipio.unidadeFederativa.idUnidadeFederativa"));
		sql.addCriteria("PA.idPais","=",criteria.getLong("municipio.unidadeFederativa.pais.idPais"));
		sql.addCriteria("RO.idRodovia","=",criteria.getLong("rodovia.idRodovia"));
		sql.addCriteria("CO.idConcessionaria","=",criteria.getLong("concessionaria.idConcessionaria"));
		sql.addCriteria("PP.dtVigenciaInicial",">=",criteria.getYearMonthDay("dtVigenciaInicial"));
		sql.addCriteria("PP.dtVigenciaFinal","<=",criteria.getYearMonthDay("dtVigenciaFinal"));

		return sql;
	}

	/**
	 * 
	 * @param idRotaColetaEntrega
	 * @return
	 */
	public List findPostoPassagemByControleCarga (Long idRotaColetaEntrega) {
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();

		StringBuffer from = new StringBuffer()
			.append(RotaColetaEntrega.class.getName()).append(" AS rce ")
			.append("inner join rce.postoPassagemRotaColEnts as pprce ")
			.append("inner join pprce.postoPassagem as pp ")
			.append("inner join pp.tipoPagamentoPostos as tpp ")
			.append("inner join tpp.tipoPagamPostoPassagem as tppp ")
			.append("inner join pp.municipio as municipio ")
			.append("left join pp.rodovia as rodovia ");

		StringBuffer projecao = new StringBuffer()
			.append("new map(")
			.append("pp.idPostoPassagem as idPostoPassagem, ")
			.append("pp.nrKm as nrKm, ")
			.append("pp.tpPostoPassagem as tpPostoPassagem, ")
			.append("municipio.nmMunicipio as nmMunicipio, ")
			.append("rodovia.sgRodovia as sgRodovia, ")
			.append("tpp.idTipoPagamentoPosto as idTipoPagamentoPosto, ")
			.append("tpp.nrPrioridadeUso as nrPrioridadeUso, ")
			.append("tppp.idTipoPagamPostoPassagem as idTipoPagamPostoPassagem, ")
			.append("tppp.dsTipoPagamPostoPassagem as dsTipoPagamPostoPassagem, ")
			.append("tppp.blCartaoPedagio as blCartaoPedagio) ");

		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(projecao.toString());
		sql.addFrom(from.toString());
		sql.addOrderBy("pp.idPostoPassagem, tpp.nrPrioridadeUso");

		sql.addCustomCriteria("? between rce.dtVigenciaInicial and rce.dtVigenciaFinal", dataAtual);
		sql.addCustomCriteria("? between pprce.dtVigenciaInicial and pprce.dtVigenciaFinal", dataAtual);
		sql.addCustomCriteria("? between pp.dtVigenciaInicial and pp.dtVigenciaFinal", dataAtual);
		sql.addCustomCriteria("? between tpp.dtVigenciaInicial and tpp.dtVigenciaFinal", dataAtual);
		sql.addCustomCriteria("rce.id = ?", idRotaColetaEntrega);

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	/**
	 * 
	 * @param idPostoPassagem
	 * @param idTipoMeioTransp
	 * @param qtEixosMeioTransp
	 * @param idTipoMeioTranspComposto
	 * @param qtEixosMeioTranpComposto
	 * @param dtConsulta
	 * @return
	 */
	public List findTarifasByTpMeioTranp(Long idPostoPassagem, Long idTipoMeioTransp, 
		Integer qtEixos, YearMonthDay dtConsulta) {

		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(new StringBuffer(TarifaPostoPassagem.class.getName()).append(" AS TPP ")
				.append("LEFT JOIN FETCH TPP.valorTarifaPostoPassagems AS VTPP ")
				.append("LEFT JOIN FETCH VTPP.moedaPais AS MP ")
				.append("LEFT  JOIN FETCH VTPP.tipoMeioTransporte AS TMT ")
				.toString());

		hql.addCriteria("TPP.dtVigenciaInicial","<=",dtConsulta);
		hql.addCriteria("TPP.dtVigenciaFinal",">=",dtConsulta);

		hql.addCriteria("TPP.postoPassagem.id","=",idPostoPassagem);
		hql.addCriteria("TMT.id","=",idTipoMeioTransp);
		hql.addCriteria("VTPP.qtEixos","=",qtEixos);

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public ValorTarifaPostoPassagem findValorByTpMeioTranp(Long idPostoPassagem, Long idTipoMeioTransp, 
		Integer qtEixos, YearMonthDay dtConsulta) {

		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(new StringBuffer(ValorTarifaPostoPassagem.class.getName()).append(" AS VTPP ")
				.append("LEFT JOIN FETCH VTPP.tarifaPostoPassagem AS TPP ")
				.append("LEFT JOIN FETCH VTPP.moedaPais AS MP ")
				.append("LEFT  JOIN FETCH VTPP.tipoMeioTransporte AS TMT ")
				.toString());

		hql.addCriteria("TPP.dtVigenciaInicial","<=",dtConsulta);
		hql.addCriteria("TPP.dtVigenciaFinal",">=",dtConsulta);

		hql.addCriteria("TPP.postoPassagem.id","=",idPostoPassagem);
		hql.addCriteria("TMT.id","=",idTipoMeioTransp);
		hql.addCriteria("VTPP.qtEixos","=",qtEixos);

		return (ValorTarifaPostoPassagem)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}
}