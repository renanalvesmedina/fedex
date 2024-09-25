package com.mercurio.lms.tabelaprecos.model.dao;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;
import com.mercurio.lms.tabelaprecos.model.ValorFaixaProgressiva;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.LongUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ValorFaixaProgressivaDAO extends BaseCrudDao<ValorFaixaProgressiva, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ValorFaixaProgressiva.class;
	}

	public TypedFlatMap findByIdCustom(Long id) {
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("idValorFaixaProgressiva", id);

		SqlTemplate hql = getQueryCustom(criteria);
		hql.addProjection(getProjectionCustom());

		return AliasToTypedFlatMapResultTransformer.getInstance().transformeTupleMap((Map) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(true), hql.getCriteria()));
	}
	
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		SqlTemplate hql = getQueryCustom(criteria);
		hql.addProjection(getProjectionCustom());

		FindDefinition findDefinition = FindDefinition.createFindDefinition(criteria);
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(true),findDefinition.getCurrentPage(),findDefinition.getPageSize(),hql.getCriteria());
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		SqlTemplate hql = getQueryCustom(criteria);
		hql.addProjection("count(*)");

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(true), hql.getCriteria());
		return result.intValue();
	}

	private String getProjectionCustom() {
		StringBuilder projection = new StringBuilder()
			.append("new map(vfp.idValorFaixaProgressiva as idValorFaixaProgressiva, ")
			.append("vfp.versao as versao, ")
			.append("vfp.nrFatorMultiplicacao as nrFatorMultiplicacao, ")
			.append("vfp.vlFixo as vlFixo, ")
			.append("vfp.pcTaxa as pcTaxa, ")
			.append("vfp.pcDesconto as pcDesconto, ")
			.append("vfp.vlAcrescimo as vlAcrescimo, ")
			.append("vfp.blPromocional as blPromocional, ")
			.append("vfp.dtVigenciaPromocaoInicial as dtVigenciaPromocaoInicial, ")
			.append("vfp.dtVigenciaPromocaoFinal as dtVigenciaPromocaoFinal, ")
			.append("vfp.vlTaxaFixa as vlTaxaFixa, ")
			.append("vfp.vlKmExtra as vlKmExtra, ")
			.append("ttp.tpTipoTabelaPreco as tpTipoTabelaPreco, ")
			.append("tp.idTarifaPreco as tarifaPreco_idTarifaPreco, ")
			.append("tp.cdTarifaPreco as tarifaPreco_cdTarifaPreco, ")
			.append("rp.idRotaPreco as rotaPreco_idRotaPreco, ")
			.append("zo.dsZona as zonaByIdZonaOrigem_dsZona, ")
			.append("zd.dsZona as zonaByIdZonaDestino_dsZona, ")
			.append("paiso.nmPais as paisByIdPaisOrigem_nmPais, ")
			.append("paisd.nmPais as paisByIdPaisDestino_nmPais, ")
			.append("ufo.sgUnidadeFederativa as unidadeFederativaByIdUfOrigem_sgUnidadeFederativa, ")
			.append("ufd.sgUnidadeFederativa as unidadeFederativaByIdUfDestino_sgUnidadeFederativa, ")
			.append("fo.sgFilial as filialByIdFilialOrigem_sgFilial, ")
			.append("fd.sgFilial as filialByIdFilialDestino_sgFilial, ")
			.append("mo.nmMunicipio as municipioByIdMunicipioOrigem_nmMunicipio, ")
			.append("md.nmMunicipio as municipioByIdMunicipioDestino_nmMunicipio, ")
			.append("ao.sgAeroporto as aeroportoByIdAeroportoOrigem_sgAeroporto, ")
			.append("ad.sgAeroporto as aeroportoByIdAeroportoDestino_sgAeroporto, ")
			.append("tlo.dsTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem_dsTipoLocalizacaoMunicipio, ")
			.append("tld.dsTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino_dsTipoLocalizacaoMunicipio)");
		return projection.toString();
	}

	private SqlTemplate getQueryCustom(TypedFlatMap criteria) {
		StringBuilder from = new StringBuilder();
		from.append(ValorFaixaProgressiva.class.getName()).append(" as vfp ");
		from.append("left join vfp.faixaProgressiva as fp ");
		
		from.append("left join fp.tabelaPrecoParcela as tpp ");
		from.append("left join tpp.tabelaPreco as tabelaPreco ");
		from.append("left join tabelaPreco.tipoTabelaPreco as ttp ");
		
		from.append("left join vfp.tarifaPreco as tp ");
		from.append("left join vfp.rotaPreco as rp ");
		from.append("left join rp.unidadeFederativaByIdUfOrigem as ufo ");
		from.append("left join rp.filialByIdFilialOrigem as fo ");
		from.append("left join rp.municipioByIdMunicipioOrigem as mo ");
		from.append("left join rp.aeroportoByIdAeroportoOrigem as ao ");
		from.append("left join rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem as tlo ");
		from.append("left join rp.unidadeFederativaByIdUfDestino as ufd ");
		from.append("left join rp.filialByIdFilialDestino as fd ");
		from.append("left join rp.municipioByIdMunicipioDestino as md ");
		from.append("left join rp.aeroportoByIdAeroportoDestino as ad ");
		from.append("left join rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino as tld ");
		from.append("left join rp.zonaByIdZonaDestino as zd ");
		from.append("left join rp.zonaByIdZonaOrigem as zo ");
		from.append("left join rp.paisByIdPaisDestino as paisd ");
		from.append("left join rp.paisByIdPaisOrigem as paiso ");

		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(from.toString());

		hql.addCriteria("vfp.id","=", criteria.getLong("idValorFaixaProgressiva"));
		hql.addCriteria("fp.id","=", criteria.getLong("idFaixaProgressiva"));
		
		hql.addCriteria("tpp.id","=", criteria.getLong("tabelaPrecoParcela.idTabelaPrecoParcela"));
		hql.addCriteria("tabelaPreco.id","=", criteria.getLong("tabelaPreco.idTabelaPreco"));
		hql.addCriteria("ttp.id","=", criteria.getLong("tipoTabelaPreco.idTipoTabelaPreco"));
		
		hql.addCriteria("tp.id","=", criteria.getLong("tarifaPreco.idTarifaPreco"));
		hql.addCriteria("zo.id","=", criteria.getLong("zonaByIdZonaOrigem.idZona"));
		hql.addCriteria("paiso.id", "=", criteria.getLong("paisByIdPaisOrigem.idPais"));
		hql.addCriteria("ufo.id", "=", criteria.getLong("unidadeFederativaByIdUfOrigem.idUnidadeFederativa"));
		hql.addCriteria("fo.id", "=", criteria.getLong("filialByIdFilialOrigem.idFilial"));
		hql.addCriteria("mo.id", "=", criteria.getLong("municipioByIdMunicipioOrigem.idMunicipio"));
		hql.addCriteria("ao.id", "=", criteria.getLong("aeroportoByIdAeroportoOrigem.idAeroporto"));
		hql.addCriteria("tlo.id", "=", criteria.getLong("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio"));
		hql.addCriteria("zd.id", "=", criteria.getLong("zonaByIdZonaDestino.idZona"));
		hql.addCriteria("paisd.id", "=", criteria.getLong("paisByIdPaisDestino.idPais"));
		hql.addCriteria("ufd.id", "=", criteria.getLong("unidadeFederativaByIdUfDestino.idUnidadeFederativa"));
		hql.addCriteria("fd.id", "=", criteria.getLong("filialByIdFilialDestino.idFilial"));
		hql.addCriteria("md.id", "=", criteria.getLong("municipioByIdMunicipioDestino.idMunicipio"));
		hql.addCriteria("ad.id", "=", criteria.getLong("aeroportoByIdAeroportoDestino.idAeroporto"));
		hql.addCriteria("tld.id", "=", criteria.getLong("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio"));
		hql.addCriteria("rp.tpSituacao","=", criteria.getString("tpSituacao"));

		hql.addOrderBy(OrderVarcharI18n.hqlOrder("zo.dsZona", LocaleContextHolder.getLocale()));
		hql.addOrderBy(OrderVarcharI18n.hqlOrder("paiso.nmPais", LocaleContextHolder.getLocale()));
		hql.addOrderBy("ufo.sgUnidadeFederativa");
		hql.addOrderBy("fo.sgFilial");
		hql.addOrderBy("mo.nmMunicipio");
		hql.addOrderBy("ao.sgAeroporto");
		hql.addOrderBy(OrderVarcharI18n.hqlOrder("tlo.dsTipoLocalizacaoMunicipio", LocaleContextHolder.getLocale()));

		hql.addOrderBy(OrderVarcharI18n.hqlOrder("zd.dsZona", LocaleContextHolder.getLocale()));
		hql.addOrderBy(OrderVarcharI18n.hqlOrder("paisd.nmPais", LocaleContextHolder.getLocale()));
		hql.addOrderBy("ufd.sgUnidadeFederativa");
		hql.addOrderBy("fd.sgFilial");
		hql.addOrderBy("md.nmMunicipio");
		hql.addOrderBy("ad.sgAeroporto");
		hql.addOrderBy(OrderVarcharI18n.hqlOrder("tld.dsTipoLocalizacaoMunicipio", LocaleContextHolder.getLocale()));

		return hql;
	}

	public boolean validateVigenciasTarifaRota(ValorFaixaProgressiva valorFaixaProgressiva) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "vfp");
		dc.setProjection(Projections.rowCount());

		/** Valor Faixa Progressiva */
		Long idValorFaixaProgressiva = valorFaixaProgressiva.getIdValorFaixaProgressiva();
		if(LongUtils.hasValue(idValorFaixaProgressiva)) {
			dc.add(Restrictions.ne("vfp.id", idValorFaixaProgressiva));
		}

		/** Faixa Progressiva */
		dc.add(Restrictions.eq("vfp.faixaProgressiva.id", valorFaixaProgressiva.getFaixaProgressiva().getIdFaixaProgressiva()));
		if( valorFaixaProgressiva.getBlPromocional() ){
		dc.add(Restrictions.eq("vfp.blPromocional", Boolean.TRUE));
		}

		/** Valida vigencia promocional */
		JTVigenciaUtils.setDetachedVigencia(
			dc,
			"vfp.dtVigenciaPromocaoInicial",
			"vfp.dtVigenciaPromocaoFinal",
			valorFaixaProgressiva.getDtVigenciaPromocaoInicial(),
			valorFaixaProgressiva.getDtVigenciaPromocaoFinal());

		TarifaPreco tarifaPreco = valorFaixaProgressiva.getTarifaPreco();
		if(tarifaPreco != null) {
			dc.add(Restrictions.eq("vfp.tarifaPreco.id", tarifaPreco.getIdTarifaPreco()));
		}
		RotaPreco rotaPreco = valorFaixaProgressiva.getRotaPreco();
		if(rotaPreco != null) {
			dc.add(Restrictions.eq("vfp.rotaPreco.id", rotaPreco.getIdRotaPreco()));
		}

		Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
		return (result.intValue() > 0);
	}

	public ValorFaixaProgressiva findByIdTabelaPrecoParaMarkup(Long idTabelaPreco) {
		StringBuilder query = new StringBuilder()
		.append(" SELECT vfp ")
		.append(" FROM " + ValorFaixaProgressiva.class.getName() + " vfp ")
		.append(" left join fetch vfp.rotaPreco rp")
		.append(" left join fetch vfp.tarifaPreco tp")
		.append(" left join fetch vfp.faixaProgressiva fp")
		.append(" left join fetch fp.tabelaPrecoParcela tpp")
		.append(" left join fetch tpp.parcelaPreco pp")
		.append(" WHERE ")
		.append(" tpp.tabelaPreco.idTabelaPreco = :idTabelaPreco ")
		.append(" AND rownum = 1 ");
	
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idTabelaPreco", idTabelaPreco);
	
		return (ValorFaixaProgressiva) getAdsmHibernateTemplate().findUniqueResult(query.toString(), params);
	}
	
	
	public ValorFaixaProgressiva findMenorFaixaProgressivaByTabelaPrecoRotaPreco(Long idTabelaPreco, Long idRotaPreco) {
		StringBuilder query = new StringBuilder()
		.append(" SELECT vfp ")
		.append(" FROM " + ValorFaixaProgressiva.class.getName() + " vfp ")
		.append(" inner join fetch vfp.rotaPreco rp")
		.append(" left join fetch vfp.tarifaPreco tp")
		.append(" inner join fetch vfp.faixaProgressiva fp")
		.append(" left join fetch fp.tabelaPrecoParcela tpp")
		.append(" left join fetch tpp.parcelaPreco pp")
		.append(" WHERE ")
		.append(" tpp.tabelaPreco.idTabelaPreco = :idTabelaPreco ")
		.append(" AND rp.idRotaPreco = :idRotaPreco ")
		.append(" AND rownum <= 1 ")
		.append(" ORDER BY fp.vlFaixaProgressiva");
	
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idTabelaPreco", idTabelaPreco);
		params.put("idRotaPreco", idRotaPreco);
	
		return (ValorFaixaProgressiva) getAdsmHibernateTemplate().findUniqueResult(query.toString(), params);
	}
	
}
