package com.mercurio.lms.vendas.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.util.TipoLocalizacaoMunicipioEnum;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import com.mercurio.lms.vendas.util.ParametroClienteUtils;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ParametroClienteDAO extends BaseCrudDao<ParametroCliente, Long> {
	
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected final Class getPersistentClass() {
		return ParametroCliente.class;
	}

	/**
	 * Método utilizado pela Integração - CQPRO00008642
	 * @author Andre Valadas
	 * 
	 * @param idsTabelaDivisaoCliente
	 */
	public Integer updateByIdTabelaDivisaoCliente(final List<Long> idsTabelaDivisaoCliente ) {
		return (Integer)getAdsmHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				StringBuilder hql = new StringBuilder();
				hql.append(" UPDATE ").append(getPersistentClass().getName());
				hql.append(" SET dtVigenciaFinal = :dtVigenciaFinal ");
				hql.append("    ,tpSituacaoParametro = :tpSituacaoParametro ");
				hql.append(" WHERE tabelaDivisaoCliente.id IN (:ids)");

				Query query = session.createQuery(hql.toString());
				query.setParameter("dtVigenciaFinal", JTDateTimeUtils.getDataAtual());
				query.setParameter("tpSituacaoParametro", ConstantesVendas.SITUACAO_INATIVO);
                query.setParameterList("ids", idsTabelaDivisaoCliente);

   				int updateEntities = query.executeUpdate();
				return Integer.valueOf(updateEntities);
			}
		}, true);
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param idTabelaDivisaoCliente
	 * @param dsEspecificacaoRota
	 * @return <ParametroCliente> 
	 */
	public ParametroCliente findParametroCliente(Long idTabelaDivisaoCliente, String dsEspecificacaoRota) {
		DetachedCriteria dc = createDetachedCriteria()
			.add(Restrictions.eq("tabelaDivisaoCliente.id", idTabelaDivisaoCliente))
			.add(Restrictions.eq("dsEspecificacaoRota", dsEspecificacaoRota));
		return (ParametroCliente) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	 * Método utilizado pela Integração
	 * @author Claiton Grings
	 * 
	 * @param idTabelaDivisaoCliente
	 * @param restricaoRotaOrigem
	 * @param restricaoRotaDestino
	 * @return <ParametroCliente> 
	 */
	public ParametroCliente findParametroCliente(Long idTabelaDivisaoCliente, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pc");
		dc.add(Restrictions.eq("pc.tabelaDivisaoCliente.id", idTabelaDivisaoCliente));

		ParametroClienteUtils.addParametroClienteRestricaoRotaWithoutOrder(dc, restricaoRotaOrigem, restricaoRotaDestino, Boolean.TRUE);

		return (ParametroCliente) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public List findParametroClienteById(Long idParametroCliente){
		StringBuilder hql = createProjection();

		hql.append("from ").append(ParametroCliente.class.getName()).append(" as pc ");
		hql.append("left join pc.tabelaDivisaoCliente as tdc ");
		hql.append("left join tdc.tabelaPreco as tp ");
		hql.append("left join pc.grupoRegiaoOrigem as gro ");
		hql.append("left join pc.grupoRegiaoDestino as grd ");
		hql.append("left join tp.moeda as m ");
		hql.append("left join tp.tipoTabelaPreco as ttp "); 
		hql.append("left join tp.subtipoTabelaPreco as sttp ");
		hql.append("left join tdc.servico as s ");
		hql.append("left join tdc.divisaoCliente as dc ");
		hql.append("left join dc.cliente as c ");
		hql.append("left join c.pessoa as p ");
		hql.append("left join pc.unidadeFederativaByIdUfOrigem as ufo ");
		hql.append("left join pc.filialByIdFilialOrigem as fo "); 
		hql.append("left join fo.pessoa as fop ");
		hql.append("left join pc.municipioByIdMunicipioOrigem as mo ");
		hql.append("left join pc.aeroportoByIdAeroportoOrigem as ao "); 
		hql.append("left join ao.pessoa as aop ");
		hql.append("left join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem as tlo "); 
		hql.append("left join pc.unidadeFederativaByIdUfDestino as ufd "); 
		hql.append("left join pc.filialByIdFilialDestino as fd "); 
		hql.append("left join fd.pessoa as fdp ");
		hql.append("left join pc.municipioByIdMunicipioDestino as md ");
		hql.append("left join pc.aeroportoByIdAeroportoDestino as ad "); 
		hql.append("left join ad.pessoa as adp ");
		hql.append("left join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino as tld "); 
		hql.append("left join pc.zonaByIdZonaDestino as zd ");
		hql.append("left join pc.zonaByIdZonaOrigem as zo ");
		hql.append("left join pc.paisByIdPaisDestino as paisd ");
		hql.append("left join pc.paisByIdPaisOrigem as paiso ");
		hql.append("left join pc.clienteByIdClienteRedespacho as cr ");
		hql.append("left join cr.pessoa as cpr ");
		hql.append("left join pc.filialByIdFilialMercurioRedespacho as fr ");
		hql.append("left join fr.pessoa as fpr ");

		hql.append("where pc.id = :idParametroCliente ");

		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "idParametroCliente", idParametroCliente);
	}

	public List findHistoricoNegociacoes(Long idParametroCliente){
		StringBuilder hql = createProjection();

		hql.append("from ").append(ParametroCliente.class.getName()).append(" as pc ");
		hql.append("left join pc.tabelaDivisaoCliente as tdc ");
		hql.append("left join pc.tabelaPreco as tp ");
		hql.append("left join pc.grupoRegiaoOrigem as gro ");
		hql.append("left join pc.grupoRegiaoDestino as grd ");
		hql.append("left join tp.moeda as m ");
		hql.append("left join tp.tipoTabelaPreco as ttp "); 
		hql.append("left join tp.subtipoTabelaPreco as sttp ");
		hql.append("left join tdc.servico as s ");
		hql.append("left join tdc.divisaoCliente as dc ");
		hql.append("left join dc.cliente as c ");
		hql.append("left join c.pessoa as p ");
		hql.append("left join pc.unidadeFederativaByIdUfOrigem as ufo ");
		hql.append("left join pc.filialByIdFilialOrigem as fo "); 
		hql.append("left join fo.pessoa as fop ");
		hql.append("left join pc.municipioByIdMunicipioOrigem as mo ");
		hql.append("left join pc.aeroportoByIdAeroportoOrigem as ao "); 
		hql.append("left join ao.pessoa as aop ");
		hql.append("left join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem as tlo "); 
		hql.append("left join pc.unidadeFederativaByIdUfDestino as ufd "); 
		hql.append("left join pc.filialByIdFilialDestino as fd "); 
		hql.append("left join fd.pessoa as fdp ");
		hql.append("left join pc.municipioByIdMunicipioDestino as md ");
		hql.append("left join pc.aeroportoByIdAeroportoDestino as ad "); 
		hql.append("left join ad.pessoa as adp ");
		hql.append("left join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino as tld "); 
		hql.append("left join pc.zonaByIdZonaDestino as zd ");
		hql.append("left join pc.zonaByIdZonaOrigem as zo ");
		hql.append("left join pc.paisByIdPaisDestino as paisd ");
		hql.append("left join pc.paisByIdPaisOrigem as paiso ");
		hql.append("left join pc.clienteByIdClienteRedespacho as cr ");
		hql.append("left join cr.pessoa as cpr ");
		hql.append("left join pc.filialByIdFilialMercurioRedespacho as fr ");
		hql.append("left join fr.pessoa as fpr ");

		hql.append("where pc.id = :idParametroCliente ");

		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "idParametroCliente", idParametroCliente);
	}

	private StringBuilder createProjection() {
		StringBuilder hql = new StringBuilder();
		hql.append("select new map(");
		hql.append("pc.dtVigenciaInicial as dtVigenciaInicial, ");
		hql.append("pc.tpIndicadorPercentualGris as tpIndicadorPercentualGris, ");
		hql.append("pc.vlPercentualGris as vlPercentualGris, ");
		hql.append("pc.tpIndicadorMinimoGris as tpIndicadorMinimoGris, ");
		hql.append("pc.vlMinimoGris as vlMinimoGris, ");
		hql.append("pc.tpIndicadorPercentualTrt as tpIndicadorPercentualTrt, ");
		hql.append("pc.vlPercentualTrt as vlPercentualTrt, ");
		hql.append("pc.tpIndicadorMinimoTrt as tpIndicadorMinimoTrt, ");
		hql.append("pc.vlMinimoTrt as vlMinimoTrt, ");
		hql.append("pc.tpIndicadorPercentualTde as tpIndicadorPercentualTde, ");
		hql.append("pc.vlPercentualTde as vlPercentualTde, ");
		hql.append("pc.tpIndicadorMinimoTde as tpIndicadorMinimoTde, ");
		hql.append("pc.vlMinimoTde as vlMinimoTde, ");
		hql.append("pc.tpIndicadorPedagio as tpIndicadorPedagio, ");
		hql.append("pc.vlPedagio as vlPedagio, ");
		hql.append("pc.tpIndicadorMinFretePeso as tpIndicadorMinFretePeso, ");
		hql.append("pc.vlMinFretePeso as vlMinFretePeso, ");
		hql.append("pc.tpIndicadorPercMinimoProgr as tpIndicadorPercMinimoProgr, ");
		hql.append("pc.vlPercMinimoProgr as vlPercMinimoProgr, ");
		hql.append("pc.tpIndicadorFretePeso as tpIndicadorFretePeso, ");
		hql.append("pc.vlFretePeso as vlFretePeso, ");
		hql.append("pc.tpIndicadorAdvalorem as tpIndicadorAdvalorem, ");
		hql.append("pc.vlAdvalorem as vlAdvalorem, ");
		hql.append("pc.tpIndicadorAdvalorem2 as tpIndicadorAdvalorem2, ");
		hql.append("pc.vlAdvalorem2 as vlAdvalorem2, ");
		hql.append("pc.tpIndicadorValorReferencia as tpIndicadorValorReferencia, ");
		hql.append("pc.vlValorReferencia as vlValorReferencia, ");
		hql.append("pc.vlMinimoFreteQuilo as vlMinimoFreteQuilo, ");
		hql.append("pc.pcFretePercentual as pcFretePercentual, ");
		hql.append("pc.vlMinimoFretePercentual as vlMinimoFretePercentual, ");
		hql.append("pc.vlToneladaFretePercentual as vlToneladaFretePercentual, ");
		hql.append("pc.psFretePercentual as psFretePercentual, ");
		hql.append("pc.pcDescontoFreteTotal as pcDescontoFreteTotal, ");
		hql.append("pc.tpIndicVlrTblEspecifica as tpIndicVlrTblEspecifica, ");
		hql.append("pc.vlTblEspecifica as vlTblEspecifica, ");
		hql.append("pc.vlFreteVolume as vlFreteVolume, ");
		hql.append("pc.blPagaCubagem as blPagaCubagem, ");
		hql.append("pc.pcPagaCubagem as pcPagaCubagem, ");
		hql.append("pc.blPagaPesoExcedente as blPagaPesoExcedente, ");
		hql.append("pc.tpTarifaMinima as tpTarifaMinima, ");
		hql.append("pc.vlTarifaMinima as vlTarifaMinima, ");
		hql.append("pc.pcCobrancaReentrega as pcCobrancaReentrega, ");
		hql.append("pc.pcCobrancaDevolucoes as pcCobrancaDevolucoes, ");
		hql.append("pc.tpSituacaoParametro as tpSituacaoParametro, ");
		hql.append("pc.pcReajFretePeso as pcReajFretePeso, ");
		hql.append("pc.pcReajVlMinimoFreteQuilo as pcReajVlMinimoFreteQuilo, ");
		hql.append("pc.pcReajVlFreteVolume as pcReajVlFreteVolume, ");
		hql.append("pc.pcReajTarifaMinima as pcReajTarifaMinima, ");
		hql.append("pc.pcReajVlTarifaEspecifica as pcReajVlTarifaEspecifica, ");
		hql.append("pc.pcReajAdvalorem as pcReajAdvalorem, ");
		hql.append("pc.pcReajAdvalorem2 as pcReajAdvalorem2, ");
		hql.append("pc.pcReajVlMinimoFretePercen as pcReajVlMinimoFretePercen, ");
		hql.append("pc.pcReajVlToneladaFretePerc as pcReajVlToneladaFretePerc, ");
		hql.append("pc.pcReajMinimoGris as pcReajMinimoGris, ");
		hql.append("pc.pcReajMinimoTrt as pcReajMinimoTrt, ");
		hql.append("pc.pcReajPedagio as pcReajPedagio, ");
		hql.append("pc.dtVigenciaFinal as dtVigenciaFinal, ");
		hql.append("pc.dsEspecificacaoRota as dsEspecificacaoRota, ");
		hql.append("pc.idParametroCliente as idParametroCliente , ");
		hql.append("gro.idGrupoRegiao as grupoRegiaoOrigem_idGrupoRegiao, ");
		hql.append("grd.idGrupoRegiao as grupoRegiaoDestino_idGrupoRegiao, ");		
		hql.append("ufo.idUnidadeFederativa as unidadeFederativaByIdUfOrigem_idUnidadeFederativa, "); 
		hql.append("paiso.idPais as paisByIdPaisOrigem_idPais, ");
		hql.append("paiso.nmPais as paisByIdPaisOrigem_nmPais, ");
		hql.append("paisd.idPais as paisByIdPaisDestino_idPais, ");
		hql.append("paisd.nmPais as paisByIdPaisDestino_nmPais, ");
		hql.append("zd.idZona as zonaByIdZonaDestino_idZona, ");
		hql.append("zo.idZona as zonaByIdZonaOrigem_idZona, ");
		hql.append("ufd.idUnidadeFederativa as unidadeFederativaByIdUfDestino_idUnidadeFederativa, "); 
		hql.append("fo.idFilial as filialByIdFilialOrigem_idFilial, ");
		hql.append("fo.sgFilial as filialByIdFilialOrigem_sgFilial, ");
		hql.append("fop.nmFantasia as filialByIdFilialOrigem_pessoa_nmFantasia, "); 
		hql.append("fd.idFilial as filialByIdFilialDestino_idFilial, ");
		hql.append("fd.sgFilial as filialByIdFilialDestino_sgFilial, ");
		hql.append("fdp.nmFantasia as filialByIdFilialDestino_pessoa_nmFantasia, ");
		hql.append("mo.idMunicipio as municipioByIdMunicipioOrigem_idMunicipio, ");
		hql.append("mo.nmMunicipio as municipioByIdMunicipioOrigem_municipio_nmMunicipio, ");
		hql.append("md.idMunicipio as municipioByIdMunicipioDestino_idMunicipio, ");
		hql.append("md.nmMunicipio as municipioByIdMunicipioDestino_municipio_nmMunicipio, ");
		hql.append("ao.idAeroporto as aeroportoByIdAeroportoOrigem_idAeroporto, ");
		hql.append("ao.sgAeroporto as aeroportoByIdAeroportoOrigem_sgAeroporto, ");
		hql.append("aop.nmPessoa as aeroportoByIdAeroportoOrigem_pessoa_nmPessoa, ");
		hql.append("ad.idAeroporto as aeroportoByIdAeroportoDestino_idAeroporto, ");
		hql.append("ad.sgAeroporto as aeroportoByIdAeroportoDestino_sgAeroporto, ");
		hql.append("adp.nmPessoa as aeroportoByIdAeroportoDestino_pessoa_nmPessoa, "); 
		hql.append("tlo.idTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem_idTipoLocalizacaoMunicipio, ");
		hql.append("tld.idTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino_idTipoLocalizacaoMunicipio, ");
		hql.append("p.nrIdentificacao as tabelaDivisaoCliente_divisaoCliente_cliente_pessoa_nrIdentificacao, ");
		hql.append("p.tpIdentificacao as tabelaDivisaoCliente_divisaoCliente_cliente_pessoa_tpIdentificacao, ");
		hql.append("p.nmPessoa as tabelaDivisaoCliente_divisaoCliente_cliente_pessoa_nmPessoa, ");
		hql.append("dc.idDivisaoCliente as tabelaDivisaoCliente_divisaoCliente_idDivisaoCliente, ");
		hql.append("tdc.idTabelaDivisaoCliente as tabelaDivisaoCliente_idTabelaDivisaoCliente, ");
		hql.append("s.idServico as tabelaDivisaoCliente_servico_idServico, ");
		hql.append("ttp.tpTipoTabelaPreco as tabelaDivisaoCliente_tabelaPreco_tipoTabelaPreco_tpTipoTabelaPreco, ");
		hql.append("ttp.nrVersao as tabelaDivisaoCliente_tabelaPreco_tipoTabelaPreco_nrVersao, ");
		hql.append("sttp.tpSubtipoTabelaPreco as tabelaDivisaoCliente_tabelaPreco_subtipoTabelaPreco_tpSubtipoTabelaPreco, ");
		hql.append("s.dsServico as tabelaDivisaoCliente_servico_dsServico, ");

		hql.append("zo.dsZona as zonaByIdZonaOrigem_dsZona, ");
		hql.append("ufo.sgUnidadeFederativa as unidadeFederativaByIdUfOrigem_sgUnidadeFederativa, ");
		hql.append("ufo.nmUnidadeFederativa as unidadeFederativaByIdUfOrigem_nmUnidadeFederativa, ");
		hql.append("tlo.dsTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem_dsTipoLocalizacaoMunicipio, ");

		hql.append("zd.dsZona as zonaByIdZonaDestino_dsZona, ");
		hql.append("ufd.sgUnidadeFederativa as unidadeFederativaByIdUfDestino_sgUnidadeFederativa, ");
		hql.append("ufd.nmUnidadeFederativa as unidadeFederativaByIdUfDestino_nmUnidadeFederativa, ");
		hql.append("tld.dsTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino_dsTipoLocalizacaoMunicipio, ");				

		hql.append("dc.dsDivisaoCliente as tabelaDivisaoCliente_divisaoCliente_dsDivisaoCliente, ");
		hql.append("m.sgMoeda as tabelaDivisaoCliente_tabelaPreco_moeda_sgMoeda, ");
		hql.append("m.sgMoeda || ' ' || m.dsSimbolo as tabelaDivisaoCliente_tabelaPreco_moeda_dsSimbolo, ");

		hql.append("sttp.idSubtipoTabelaPreco as tabelaDivisaoCliente_tabelaPreco_subtipoTabelaPreco_idSubtipoTabelaPreco, ");
		hql.append("tp.dsDescricao as tabelaDivisaoCliente_tabelaPreco_dsDescricao, ");
		hql.append("tp.idTabelaPreco as tabelaDivisaoCliente_tabelaPreco_idTabelaPreco, ");

		hql.append("c.idCliente as tabelaDivisaoCliente_divisaoCliente_cliente_idCliente, ");

		hql.append("cr.idCliente as clienteByIdClienteRedespacho_idCliente, ");
		hql.append("cpr.nrIdentificacao as clienteByIdClienteRedespacho_pessoa_nrIdentificacao, ");
		hql.append("cpr.tpIdentificacao as clienteByIdClienteRedespacho_pessoa_tpIdentificacao, ");
		hql.append("cpr.nmPessoa as clienteByIdClienteRedespacho_pessoa_nmPessoa, ");

		hql.append("fr.idFilial as filialByIdFilialMercurioRedespacho_idFilial, ");
		hql.append("fr.sgFilial as filialByIdFilialMercurioRedespacho_sgFilial, ");
		hql.append("fpr.nmFantasia as filialByIdFilialMercurioRedespacho_pessoa_nmFantasia "); 

		hql.append(") ");
		return hql;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria,FindDefinition def) {
		SqlTemplate sql = getSqlTemplateFindPaginated(criteria);

		StringBuffer hql = new StringBuffer();

		hql.append("new map( ");
		hql.append("pc.idParametroCliente as idParametroCliente, ");
		// LMS-7551
		hql.append("nvl(sim_fil.sgFilial || ' ' || sim.nrSimulacao, sim_prop_fil.sgFilial || '' || sim_prop.nrSimulacao) as proposta_idProposta, ");
		
		hql.append("zo.dsZona as zonaByIdZonaOrigem_dsZona, "); 
		hql.append("paiso.nmPais as paisByIdPaisOrigem_nmPais, ");
		hql.append("ufo.sgUnidadeFederativa as unidadeFederativaByIdUfOrigem_sgUnidadeFederativa, ");
		hql.append("fo.sgFilial as filialByIdFilialOrigem_sgFilial, ");
		hql.append("mo.nmMunicipio as municipioByIdMunicipioOrigem_nmMunicipio, ");
		hql.append("ao.sgAeroporto as aeroportoByIdAeroportoOrigem_sgAeroporto, ");
		hql.append("tlo.dsTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem_dsTipoLocalizacaoMunicipio, ");
		hql.append("gro.dsGrupoRegiao as grupoRegiaoOrigem_dsGrupoRegiao, ");
		hql.append("zo.dsZona as zonaByIdZonaDestino_dsZona, "); 
		hql.append("paiso.nmPais as paisByIdPaisDestino_nmPais, ");
		hql.append("ufd.sgUnidadeFederativa as unidadeFederativaByIdUfDestino_sgUnidadeFederativa, ");
		hql.append("fd.sgFilial as filialByIdFilialDestino_sgFilial, ");
		hql.append("md.nmMunicipio as municipioByIdMunicipioDestino_nmMunicipio, ");
		hql.append("ad.sgAeroporto as aeroportoByIdAeroportoDestino_sgAeroporto, ");
		hql.append("grd.dsGrupoRegiao as grupoRegiaoDestino_dsGrupoRegiao, ");
		hql.append("tld.dsTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino_dsTipoLocalizacaoMunicipio)");

		sql.addProjection(hql.toString());
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),def.getCurrentPage(),def.getPageSize(),sql.getCriteria());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate sql = getSqlTemplateFindPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
	}

	public SqlTemplate getSqlTemplateFindPaginated(TypedFlatMap criteria) {
		
		Validate.notEmpty(criteria.getString("tabelaDivisaoCliente.divisaoCliente.cliente.idCliente"), "idCliente é obrigatório!");
		
		SqlTemplate sql = new SqlTemplate();

		StringBuffer hql = new StringBuffer();
		hql.append(ParametroCliente.class.getName()).append(" pc ");
		hql.append("inner join pc.tabelaDivisaoCliente as tdc ");
		hql.append("inner join tdc.tabelaPreco as tp ");
		
		hql.append("inner join tp.tipoTabelaPreco as ttp ");
		hql.append("inner join tp.subtipoTabelaPreco as stp ");
		hql.append("inner join tdc.divisaoCliente as dc ");
		hql.append("inner join dc.cliente as c ");
		hql.append("inner join tdc.servico as s ");
		hql.append("inner join pc.zonaByIdZonaOrigem as zo ");
		// LMS-7551
		hql.append("left join pc.simulacao as sim ");
		hql.append("left join sim.filial as sim_fil ");
		hql.append("left join pc.proposta as prop ");
		hql.append("left join prop.simulacao as sim_prop ");
		hql.append("left join sim_prop.filial as sim_prop_fil ");
		
		hql.append("left join pc.paisByIdPaisOrigem as paiso ");
		hql.append("left join pc.unidadeFederativaByIdUfOrigem as ufo ");
		hql.append("left join pc.filialByIdFilialOrigem as fo ");
		hql.append("left join pc.municipioByIdMunicipioOrigem as mo ");
		hql.append("left join pc.aeroportoByIdAeroportoOrigem as ao ");
		hql.append("left join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem as tlo ");
		hql.append("inner join pc.zonaByIdZonaDestino as zd ");
		hql.append("left join pc.paisByIdPaisDestino as paisd ");
		hql.append("left join pc.unidadeFederativaByIdUfDestino as ufd ");
		hql.append("left join pc.filialByIdFilialDestino as fd ");
		hql.append("left join pc.municipioByIdMunicipioDestino as md ");
		hql.append("left join pc.aeroportoByIdAeroportoDestino as ad ");
		hql.append("left join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino as tld ");
		hql.append("left join pc.grupoRegiaoOrigem as gro ");
		hql.append("left join pc.grupoRegiaoDestino as grd ");

		sql.addFrom(hql.toString());

		this.createSharedOrders(sql);

		//*** Filtros utilizados apenas na tela de Parametro do Cliente.
		sql.addCriteria("pc.dtVigenciaFinal", ">=", JTDateTimeUtils.getDataAtual(), YearMonthDay.class);
		sql.addCriteria("tdc.idTabelaDivisaoCliente","=",criteria.getLong("tabelaDivisaoCliente.idTabelaDivisaoCliente"));
		sql.addCustomCriteria("pc.tpSituacaoParametro in ('A','I')");
		sql.addCriteria("stp.tpSubtipoTabelaPreco","<>","F");

		this.createSharedCriterias(criteria, sql);

		return sql;
	}
	
	public ResultSetPage findPaginatedHistoricoNegociacoes(TypedFlatMap criteria,FindDefinition def) {
		SqlTemplate sql = getSqlTemplateHistoricoNegociacoes(criteria);

		StringBuilder builder = new StringBuilder();
		builder.append("new map(");
		builder.append(" pc.idParametroCliente as idParametroCliente,");
		builder.append(" ttp.tpTipoTabelaPreco as tpTipoTabelaPreco,");
		builder.append(" ttp.nrVersao as nrVersao,");
		builder.append(" stp.tpSubtipoTabelaPreco as tpSubtipoTabelaPreco,");
		builder.append(" dc.dsDivisaoCliente as dsDivisaoCliente,");
		builder.append(" s.dsServico as dsServico,");
		builder.append(" pc.dtVigenciaInicial as dtVigenciaInicial,");
		builder.append(" pc.dtVigenciaFinal as dtVigenciaFinal,");
		builder.append(" tdc.blAtualizacaoAutomatica as blAtualizacaoAutomatica,");

		builder.append(" zo.dsZona as zonaByIdZonaOrigem_dsZona,");
		builder.append(" paiso.nmPais as paisByIdPaisOrigem_nmPais,");
		builder.append(" ufo.sgUnidadeFederativa as unidadeFederativaByIdUfOrigem_sgUnidadeFederativa,");
		builder.append(" fo.sgFilial as filialByIdFilialOrigem_sgFilial,");
		builder.append(" mo.nmMunicipio as municipioByIdMunicipioOrigem_nmMunicipio,");
		builder.append(" ao.sgAeroporto as aeroportoByIdAeroportoOrigem_sgAeroporto,");
		builder.append(" tlo.dsTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem_dsTipoLocalizacaoMunicipio,");

		builder.append(" zo.dsZona as zonaByIdZonaDestino_dsZona,");
		builder.append(" paiso.nmPais as paisByIdPaisDestino_nmPais,");
		builder.append(" ufd.sgUnidadeFederativa as unidadeFederativaByIdUfDestino_sgUnidadeFederativa,");
		builder.append(" fd.sgFilial as filialByIdFilialDestino_sgFilial,");
		builder.append(" md.nmMunicipio as municipioByIdMunicipioDestino_nmMunicipio,");
		builder.append(" ad.sgAeroporto as aeroportoByIdAeroportoDestino_sgAeroporto,");
		builder.append(" tld.dsTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino_dsTipoLocalizacaoMunicipio");
		builder.append(") ");

		sql.addProjection(builder.toString());

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),def.getCurrentPage(),def.getPageSize(),sql.getCriteria());
	}

	public Integer getRowCountHistoricoNegociacoes(TypedFlatMap criteria) {
		SqlTemplate sql = getSqlTemplateHistoricoNegociacoes(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
	}

	private SqlTemplate getSqlTemplateHistoricoNegociacoes(TypedFlatMap criteria) {
		StringBuilder builder = new StringBuilder()
			.append(ParametroCliente.class.getName()).append(" pc ")
			.append("inner join pc.tabelaDivisaoCliente tdc ")
			.append("inner join tdc.divisaoCliente dc ")
			.append("inner join dc.cliente c ")
			.append("inner join tdc.servico s ")
			.append("inner join tdc.tabelaPreco tp ")
			.append("inner join pc.grupoRegiaoOrigem as gro ")
			.append("inner join pc.grupoRegiaoDestino as grd ")
			.append("inner join tp.tipoTabelaPreco ttp ")
			.append("inner join tp.subtipoTabelaPreco stp ")
			.append("inner join pc.zonaByIdZonaOrigem zo ")
			.append("inner join pc.zonaByIdZonaDestino zd ")
			.append("left join pc.paisByIdPaisOrigem paiso ")
			.append("left join pc.paisByIdPaisDestino paisd ")
			.append("left join pc.unidadeFederativaByIdUfOrigem ufo ")
			.append("left join pc.unidadeFederativaByIdUfDestino ufd ")
			.append("left join pc.municipioByIdMunicipioOrigem mo ")
			.append("left join pc.municipioByIdMunicipioDestino md ")
			.append("left join pc.filialByIdFilialOrigem fo ")
			.append("left join pc.filialByIdFilialDestino fd ")
			.append("left join pc.aeroportoByIdAeroportoOrigem ao ")
			.append("left join pc.aeroportoByIdAeroportoDestino ad ")
			.append("left join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem tlo ")
			.append("left join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino tld ");

		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(builder.toString());

		this.createSharedCriterias(criteria, sql);
		sql.addCriteria("pc.dtVigenciaFinal", "<=", JTDateTimeUtils.getDataAtual(), YearMonthDay.class);

		sql.addOrderBy("ttp.tpTipoTabelaPreco");
		sql.addOrderBy("ttp.nrVersao");
		sql.addOrderBy("stp.tpSubtipoTabelaPreco");

		sql.addOrderBy("dc.dsDivisaoCliente");
		
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("s.dsServico", LocaleContextHolder.getLocale()));

		this.createSharedOrders(sql);

		sql.addOrderBy("pc.dtVigenciaInicial");

		return sql;
	}

	private void createSharedCriterias(TypedFlatMap criteria, SqlTemplate sql) {
		String value = null;

		value = criteria.getString("tabelaDivisaoCliente.divisaoCliente.cliente.idCliente");
		sql.addCriteria("c.idCliente","=",value, Long.class);
		value = criteria.getString("tabelaDivisaoCliente.divisaoCliente.idDivisaoCliente");
		sql.addCriteria("dc.idDivisaoCliente","=",value, Long.class);
		value = criteria.getString("tabelaPreco.idTabelaPreco");
		sql.addCriteria("tp.idTabelaPreco","=",value, Long.class);

		// zona
		value = criteria.getString("zonaByIdZonaOrigem.idZona");
		sql.addCriteria("zo.idZona","=",value, Long.class);
		// pais
		value = criteria.getString("paisByIdPaisOrigem.idPais");
		sql.addCriteria("paiso.idPais","=",value, Long.class);
		// uf
		value = criteria.getString("unidadeFederativaByIdUfOrigem.idUnidadeFederativa");
		sql.addCriteria("ufo.idUnidadeFederativa","=",value, Long.class);
		// filial
		value = criteria.getString("filialByIdFilialOrigem.idFilial");
		sql.addCriteria("fo.idFilial","=",value, Long.class);
		// municipio
		value = criteria.getString("municipioByIdMunicipioOrigem.idMunicipio");
		sql.addCriteria("mo.idMunicipio","=",value, Long.class);
		// aeroporto
		value = criteria.getString("aeroportoByIdAeroportoOrigem.idAeroporto");
		sql.addCriteria("ao.idAeroporto","=",value, Long.class);
		// tipo de localização
		value = criteria.getString("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio");
		sql.addCriteria("tlo.idTipoLocalizacaoMunicipio","=",value, Long.class);
		// grupo região
		value = criteria.getString("grupoRegiaoOrigem.idGrupoRegiao");
		sql.addCriteria("gro.idGrupoRegiao","=",value, Long.class);

		// Destino

		// zona
		value = criteria.getString("zonaByIdZonaDestino.idZona");
		sql.addCriteria("zd.idZona","=",value, Long.class);
		// pais
		value = criteria.getString("paisByIdPaisDestino.idPais");
		sql.addCriteria("paisd.idPais","=",value, Long.class);
		// uf
		value = criteria.getString("unidadeFederativaByIdUfDestino.idUnidadeFederativa");
		sql.addCriteria("ufd.idUnidadeFederativa","=",value, Long.class);
		// filial
		value = criteria.getString("filialByIdFilialDestino.idFilial");
		sql.addCriteria("fd.idFilial","=",value, Long.class);
		// municipio
		value = criteria.getString("municipioByIdMunicipioDestino.idMunicipio");
		sql.addCriteria("md.idMunicipio","=",value, Long.class);
		// aeroporto
		value = criteria.getString("aeroportoByIdAeroportoDestino.idAeroporto");
		sql.addCriteria("ad.idAeroporto","=",value, Long.class);
		// tipo de localização
		value = criteria.getString("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio");
		sql.addCriteria("tld.idTipoLocalizacaoMunicipio","=",value, Long.class);
		// grupo região
		value = criteria.getString("grupoRegiaoDestino.idGrupoRegiao");
		sql.addCriteria("grd.idGrupoRegiao","=",value, Long.class);
	}

	private void createSharedOrders(SqlTemplate sql) {
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("zo.dsZona", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("paiso.nmPais", LocaleContextHolder.getLocale()));
		sql.addOrderBy("ufo.sgUnidadeFederativa");
		sql.addOrderBy("fo.sgFilial");
		sql.addOrderBy("mo.nmMunicipio");
		sql.addOrderBy("ao.sgAeroporto");

		sql.addOrderBy(OrderVarcharI18n.hqlOrder("zd.dsZona", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("paisd.nmPais", LocaleContextHolder.getLocale()));
		sql.addOrderBy("ufd.sgUnidadeFederativa");
		sql.addOrderBy("fd.sgFilial");
		sql.addOrderBy("md.nmMunicipio");
		sql.addOrderBy("ao.sgAeroporto");
	}

	public Integer getRowCountByIdCotacao(Long idCotacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "d")
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq("d.cotacao.id", idCotacao));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public TypedFlatMap findByIdCotacao(Long idCotacao) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("idParametroCliente"), "idParametroCliente")
			.add(Projections.property("tpIndicadorPercentualGris"), "tpIndicadorPercentualGris")
			.add(Projections.property("vlPercentualGris"), "vlPercentualGris")
			.add(Projections.property("tpIndicadorMinimoGris"), "tpIndicadorMinimoGris")
			.add(Projections.property("vlMinimoGris"), "vlMinimoGris")
			.add(Projections.property("tpIndicadorPercentualTrt"), "tpIndicadorPercentualTrt")
			.add(Projections.property("vlPercentualTrt"), "vlPercentualTrt")
			.add(Projections.property("tpIndicadorMinimoTrt"), "tpIndicadorMinimoTrt")
			.add(Projections.property("vlMinimoTrt"), "vlMinimoTrt")
			.add(Projections.property("tpIndicadorPedagio"), "tpIndicadorPedagio")
			.add(Projections.property("vlPedagio"), "vlPedagio")
			.add(Projections.property("tpIndicadorPercentualTde"), "tpIndicadorPercentualTde")
			.add(Projections.property("vlPercentualTde"), "vlPercentualTde")
			.add(Projections.property("tpIndicadorMinimoTde"), "tpIndicadorMinimoTde")
			.add(Projections.property("vlMinimoTde"), "vlMinimoTde")
			.add(Projections.property("tpIndicadorMinFretePeso"), "tpIndicadorMinFretePeso")
			.add(Projections.property("vlMinFretePeso"), "vlMinFretePeso")
			.add(Projections.property("tpIndicadorPercMinimoProgr"), "tpIndicadorPercMinimoProgr")
			.add(Projections.property("vlPercMinimoProgr"), "vlPercMinimoProgr")
			.add(Projections.property("tpIndicadorFretePeso"), "tpIndicadorFretePeso")
			.add(Projections.property("vlFretePeso"), "vlFretePeso")
			.add(Projections.property("tpIndicadorAdvalorem"), "tpIndicadorAdvalorem")
			.add(Projections.property("vlMinimoFreteQuilo"), "vlMinimoFreteQuilo")
			.add(Projections.property("vlValorReferencia"), "vlValorReferencia")
			.add(Projections.property("tpIndicadorValorReferencia"), "tpIndicadorValorReferencia")
			.add(Projections.property("vlAdvalorem2"), "vlAdvalorem2")
			.add(Projections.property("tpIndicadorAdvalorem2"), "tpIndicadorAdvalorem2")
			.add(Projections.property("vlAdvalorem"), "vlAdvalorem")
			.add(Projections.property("tpIndicVlrTblEspecifica"), "tpIndicVlrTblEspecifica")
			.add(Projections.property("pcDescontoFreteTotal"), "pcDescontoFreteTotal")
			.add(Projections.property("pcCobrancaReentrega"), "pcCobrancaReentrega")
			.add(Projections.property("pcCobrancaDevolucoes"), "pcCobrancaDevolucoes")
			.add(Projections.property("psFretePercentual"), "psFretePercentual")
			.add(Projections.property("vlToneladaFretePercentual"), "vlToneladaFretePercentual")
			.add(Projections.property("vlMinimoFretePercentual"), "vlMinimoFretePercentual")
			.add(Projections.property("pcFretePercentual"), "pcFretePercentual")
			.add(Projections.property("blPagaCubagem"), "blPagaCubagem")
			.add(Projections.property("vlFreteVolume"), "vlFreteVolume")
			.add(Projections.property("vlTblEspecifica"), "vlTblEspecifica")
			.add(Projections.property("vlTarifaMinima"), "vlTarifaMinima")
			.add(Projections.property("tpTarifaMinima"), "tpTarifaMinima")
			.add(Projections.property("blPagaPesoExcedente"), "blPagaPesoExcedente")
			.add(Projections.property("pcPagaCubagem"), "pcPagaCubagem")
			.add(Projections.property("ttp.tpTipoTabelaPreco"), "tpTipoTabelaPreco")
			.add(Projections.property("ttp.nrVersao"), "nrVersao")
			.add(Projections.property("m.sgMoeda"), "sgMoeda")
			.add(Projections.property("m.dsSimbolo"), "dsSimbolo")
			.add(Projections.property("stp.tpSubtipoTabelaPreco"), "tpSubtipoTabelaPreco")
			.add(Projections.property("tp.dsDescricao"), "dsDescricao");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass())
			.createAlias("cotacao", "c")
			.createAlias("c.tabelaPreco", "tp")
			.createAlias("tp.subtipoTabelaPreco", "stp")
			.createAlias("tp.moeda", "m")
			.createAlias("tp.tipoTabelaPreco", "ttp")
			.setProjection(pl)
			.add(Restrictions.eq("c.id", idCotacao))
			.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		List l = findByDetachedCriteria(dc);
		if(!l.isEmpty())
			return (TypedFlatMap)l.get(0);
		return null;
	}

	public Integer getRowCountParametrizadoRedespacho(Long idCliente) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pc")
			.setProjection(Projections.count("pc.id"))
			.createAlias("pc.tabelaDivisaoCliente", "tdc")
			.createAlias("tdc.divisaoCliente", "dc")
			.add(Restrictions.eq("dc.cliente.id", idCliente))
			.add(Restrictions.isNotNull("pc.clienteByIdClienteRedespacho.id"));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	/** Métodos relacionados a Simulacao e Proposta */
	public Boolean existParametroByIdSimulacao(Long idSimulacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pc")
		.setProjection(Projections.count("pc.id"))
		.createAlias("pc.simulacao", "s")
		.add(Restrictions.eq("s.idSimulacao", idSimulacao))
		.add(Restrictions.eq("pc.tpSituacaoParametro", "P"));

		return CompareUtils.gt(getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc), IntegerUtils.ZERO);
	}
	public Boolean existParametroByIdProposta(Long idProposta) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pc")
		.setProjection(Projections.count("pc.id"))
		.createAlias("pc.proposta", "p")
		.add(Restrictions.eq("p.idProposta", idProposta))
		.add(Restrictions.eq("pc.tpSituacaoParametro", "P"));

		return CompareUtils.gt(getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc), IntegerUtils.ZERO);
	}
	public List<Long> findIdsParametrosByIdSimulacao(Long idSimulacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pc")
		.setProjection(Projections.property("pc.idParametroCliente"))
		.createAlias("pc.simulacao", "s")
		.add(Restrictions.eq("s.idSimulacao", idSimulacao))
		.add(Restrictions.eq("pc.tpSituacaoParametro", "P"));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	public List<ParametroCliente> findParametroByIdSimulacao(Long idSimulacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pc")
		.createAlias("pc.simulacao", "s")
		.add(Restrictions.eq("s.idSimulacao", idSimulacao))
		.add(Restrictions.eq("pc.tpSituacaoParametro", "P"));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	public List<ParametroCliente> findParametroByIdProposta(Long idProposta) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pc")
		.createAlias("pc.proposta", "p")
		.add(Restrictions.eq("p.idProposta", idProposta))
		.add(Restrictions.eq("pc.tpSituacaoParametro", "P"));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	public List<ParametroCliente> findParametroByIdCotacao(Long idCotacao) {
    	StringBuffer sql = new StringBuffer()
    	.append("from ").append(getPersistentClass().getName()).append(" as p ")
    	.append(" inner join fetch p.cotacao c ")
    	.append(" left join fetch p.tabelaDivisaoCliente as tdc")
    	.append(" left join fetch tdc.tabelaPreco tp ")
    	.append(" left join fetch tp.subtipoTabelaPreco stp ")
		.append(" left join fetch tp.moeda m ")
		.append(" left join fetch tp.tipoTabelaPreco ttp ")
    	.append(" where ")
    	.append(" c.id = ? ");
		return getAdsmHibernateTemplate().find(sql.toString(),idCotacao);	
	}
	
	public ParametroCliente findParametroById(Long idParametro) {
    	String sql = new StringBuilder()
    						.append(" from ").append(getPersistentClass().getName()).append(" as p ")
    						.append(" where p.idParametroCliente = ? ").toString();
		return (ParametroCliente) getAdsmHibernateTemplate().findUniqueResult(sql, new Object[]{idParametro});	
	}
	
	public boolean validateParametroCliente(ParametroCliente parametroCliente) {
		SqlTemplate sql = getCommonFilter(parametroCliente);

		sql.addProjection("pc.idParametroCliente","idParametroCliente");

 		String from = getPersistentClass().getName() + " as pc "+
 					"join pc.tabelaDivisaoCliente as tdc "+
 					"join pc.zonaByIdZonaOrigem as zo "+
 					"join pc.zonaByIdZonaDestino as zd "+
					"left outer join pc.paisByIdPaisDestino as paisd "+
					"left outer join pc.paisByIdPaisOrigem as paiso "+
					"left outer join pc.unidadeFederativaByIdUfOrigem as ufo "+
					"left outer join pc.unidadeFederativaByIdUfDestino as ufd "+
					"left outer join pc.filialByIdFilialOrigem as fo " +
					"left outer join pc.filialByIdFilialDestino as fd " + 
					"left outer join pc.municipioByIdMunicipioOrigem as mo "+
					"left outer join pc.municipioByIdMunicipioDestino as md "+
					"left outer join pc.aeroportoByIdAeroportoOrigem as ao "+ 
					"left outer join pc.aeroportoByIdAeroportoDestino as ad "+
					"left outer join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem as tlo " + 
					"left outer join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino as tld " +
					"left outer join pc.grupoRegiaoOrigem as gro " + 
 					"left outer join pc.grupoRegiaoDestino as grd ";

		sql.addFrom(from);
		
		sql.addCriteria("pc.tpSituacaoParametro", "=", "A");
		sql.addCriteria("trunc(pc.dtVigenciaInicial)","<=",parametroCliente.getDtVigenciaFinal(),YearMonthDay.class);
		sql.addCriteria("trunc(pc.dtVigenciaFinal)",">=",parametroCliente.getDtVigenciaInicial(),YearMonthDay.class);
		sql.addCriteria("pc.idParametroCliente","<>",parametroCliente.getIdParametroCliente());
		
		List lista = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		if(lista != null && !lista.isEmpty())
			return true;
		return false;
	}
	
	
	
	public Long findParametrizacaoAtiva(ParametroCliente parametroCliente){
		SqlTemplate sql = getCommonFilter(parametroCliente);

		sql.addProjection("pc.idParametroCliente","idParametroCliente");

 		String from = getPersistentClass().getName() + " as pc "+
 					"join pc.tabelaDivisaoCliente as tdc "+
 					"join pc.zonaByIdZonaOrigem as zo "+
 					"join pc.zonaByIdZonaDestino as zd "+
					"left outer join pc.paisByIdPaisDestino as paisd "+
					"left outer join pc.paisByIdPaisOrigem as paiso "+
					"left outer join pc.unidadeFederativaByIdUfOrigem as ufo "+
					"left outer join pc.unidadeFederativaByIdUfDestino as ufd "+
					"left outer join pc.filialByIdFilialOrigem as fo " +
					"left outer join pc.filialByIdFilialDestino as fd " + 
					"left outer join pc.municipioByIdMunicipioOrigem as mo "+
					"left outer join pc.municipioByIdMunicipioDestino as md "+
					"left outer join pc.aeroportoByIdAeroportoOrigem as ao "+ 
					"left outer join pc.aeroportoByIdAeroportoDestino as ad "+
					"left outer join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem as tlo " + 
					"left outer join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino as tld " +
					"left outer join pc.grupoRegiaoOrigem as gro " + 
 					"left outer join pc.grupoRegiaoDestino as grd ";

		sql.addFrom(from);

		sql.addCriteria("pc.tpSituacaoParametro", "=", "A");
		sql.addCriteria("trunc(pc.dtVigenciaInicial)",">=",parametroCliente.getDtVigenciaInicial(),YearMonthDay.class);
		sql.addCriteria("pc.idParametroCliente","<>",parametroCliente.getIdParametroCliente());

		List lista = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		if(lista != null && !lista.isEmpty()) {
			return (Long)lista.get(0);
		}
		return null;
	}
	

	/**
	 * Popula os filtros em comun para as pesquisas de parametros
	 * @param parametroCliente
	 * @return SqlTemplate
	 */
	private SqlTemplate getCommonFilter(ParametroCliente parametroCliente) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addCriteria("tdc.idTabelaDivisaoCliente", "=", parametroCliente.getTabelaDivisaoCliente().getIdTabelaDivisaoCliente());
		
		if(parametroCliente.getZonaByIdZonaOrigem() != null) {
			sql.addCriteria("zo.idZona", "=", parametroCliente.getZonaByIdZonaOrigem().getIdZona());
		}else{
			sql.addCustomCriteria("zo.idZona is null");
		}

		if(parametroCliente.getZonaByIdZonaDestino() != null) {
			sql.addCriteria("zd.idZona", "=", parametroCliente.getZonaByIdZonaDestino().getIdZona());
		}else{
			sql.addCustomCriteria("zd.idZona is null");
		}

		if(parametroCliente.getPaisByIdPaisOrigem() != null) {
			sql.addCriteria("paiso.idPais", "=", parametroCliente.getPaisByIdPaisOrigem().getIdPais());
		}else{
			sql.addCustomCriteria("paiso.idPais is null");
		}

		if(parametroCliente.getPaisByIdPaisDestino() != null) {
			sql.addCriteria("paisd.idPais", "=", parametroCliente.getPaisByIdPaisDestino().getIdPais());
		}else{
			sql.addCustomCriteria("paisd.idPais is null");
		}

		if(parametroCliente.getUnidadeFederativaByIdUfOrigem() != null) {
			sql.addCriteria("ufo.idUnidadeFederativa", "=", parametroCliente.getUnidadeFederativaByIdUfOrigem().getIdUnidadeFederativa());
		}else{
			sql.addCustomCriteria("ufo.idUnidadeFederativa is null");
		}

		if(parametroCliente.getUnidadeFederativaByIdUfDestino() != null) {
			sql.addCriteria("ufd.idUnidadeFederativa", "=", parametroCliente.getUnidadeFederativaByIdUfDestino().getIdUnidadeFederativa());
		}else{
			sql.addCustomCriteria("ufd.idUnidadeFederativa is null");
		}

		if(parametroCliente.getFilialByIdFilialOrigem() != null) {
			sql.addCriteria("fo.idFilial", "=", parametroCliente.getFilialByIdFilialOrigem().getIdFilial());
		}else{
			sql.addCustomCriteria("fo.idFilial is null");
		}

		if(parametroCliente.getFilialByIdFilialDestino() != null) {
			sql.addCriteria("fd.idFilial", "=", parametroCliente.getFilialByIdFilialDestino().getIdFilial());
		}else{
			sql.addCustomCriteria("fd.idFilial is null");
		}

		if(parametroCliente.getMunicipioByIdMunicipioOrigem() != null) {
			sql.addCriteria("mo.idMunicipio", "=", parametroCliente.getMunicipioByIdMunicipioOrigem().getIdMunicipio());
		}else{
			sql.addCustomCriteria("mo.idMunicipio is null");
		}

		if(parametroCliente.getMunicipioByIdMunicipioDestino() != null) {
			sql.addCriteria("md.idMunicipio", "=", parametroCliente.getMunicipioByIdMunicipioDestino().getIdMunicipio());
		}else{
			sql.addCustomCriteria("md.idMunicipio is null");
		}

		if(parametroCliente.getAeroportoByIdAeroportoOrigem() != null) {
			sql.addCriteria("ao.idAeroporto", "=", parametroCliente.getAeroportoByIdAeroportoOrigem().getIdAeroporto());
		}else{
			sql.addCustomCriteria("ao.idAeroporto is null");
		}

		if(parametroCliente.getAeroportoByIdAeroportoDestino() != null) {
			sql.addCriteria("ad.idAeroporto", "=", parametroCliente.getAeroportoByIdAeroportoDestino().getIdAeroporto());
		}else{
			sql.addCustomCriteria("ad.idAeroporto is null");
		}

		if(parametroCliente.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem() != null) {
			sql.addCriteria("tlo.idTipoLocalizacaoMunicipio", "=", parametroCliente.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem().getIdTipoLocalizacaoMunicipio());
		}else{
			sql.addCustomCriteria("tlo.idTipoLocalizacaoMunicipio is null");
		}

		if(parametroCliente.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino() != null) {
			sql.addCriteria("tld.idTipoLocalizacaoMunicipio", "=", parametroCliente.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino().getIdTipoLocalizacaoMunicipio());
		}else{
			sql.addCustomCriteria("tld.idTipoLocalizacaoMunicipio is null");
		}

		if(parametroCliente.getGrupoRegiaoOrigem() != null) {
			sql.addCriteria("gro.idGrupoRegiao", "=", parametroCliente.getGrupoRegiaoOrigem().getIdGrupoRegiao());
		}else{
			sql.addCustomCriteria("gro.idGrupoRegiao is null");
		}

		if(parametroCliente.getGrupoRegiaoDestino() != null) {
			sql.addCriteria("grd.idGrupoRegiao", "=", parametroCliente.getGrupoRegiaoDestino().getIdGrupoRegiao());
		}else{
			sql.addCustomCriteria("grd.idGrupoRegiao is null");
		}
		
		return sql;
		}
	
	
	public Long findParametrizacaoByTpSituacaoParametro(ParametroCliente parametroCliente, String tpSituacaoParametro){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("pc.idParametroCliente","idParametroCliente");

 		String from = getPersistentClass().getName() + " as pc "+
 					"join pc.zonaByIdZonaOrigem as zo "+
 					"join pc.zonaByIdZonaDestino as zd "+
					"left outer join pc.paisByIdPaisDestino as paisd "+
					"left outer join pc.paisByIdPaisOrigem as paiso "+
					"left outer join pc.unidadeFederativaByIdUfOrigem as ufo "+
					"left outer join pc.unidadeFederativaByIdUfDestino as ufd "+
					"left outer join pc.filialByIdFilialOrigem as fo " +
					"left outer join pc.filialByIdFilialDestino as fd " + 
					"left outer join pc.municipioByIdMunicipioOrigem as mo "+
					"left outer join pc.municipioByIdMunicipioDestino as md "+
					"left outer join pc.aeroportoByIdAeroportoOrigem as ao "+ 
					"left outer join pc.aeroportoByIdAeroportoDestino as ad "+
					"left outer join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem as tlo " + 
					"left outer join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino as tld " +
					"left outer join pc.grupoRegiaoOrigem as gro " + 
 					"left outer join pc.grupoRegiaoDestino as grd "; 
 		
		sql.addFrom(from);
		
		sql.addCriteria("pc.tpSituacaoParametro", "=", tpSituacaoParametro);
		if(parametroCliente.getSimulacao() != null) {
			sql.addCriteria("pc.simulacao.id", "=", parametroCliente.getSimulacao().getIdSimulacao());
		} else if(parametroCliente.getProposta() != null) {
			sql.addCriteria("pc.proposta.id", "=", parametroCliente.getProposta().getIdProposta());
		}

		if(parametroCliente.getZonaByIdZonaOrigem() != null) {
			sql.addCriteria("zo.idZona", "=", parametroCliente.getZonaByIdZonaOrigem().getIdZona());
		}else{
			sql.addCustomCriteria("zo.idZona is null");
		}
		
		if(parametroCliente.getZonaByIdZonaDestino() != null) {
			sql.addCriteria("zd.idZona", "=", parametroCliente.getZonaByIdZonaDestino().getIdZona());
		}else{
			sql.addCustomCriteria("zd.idZona is null");
		}
		
		if(parametroCliente.getPaisByIdPaisOrigem() != null) {
			sql.addCriteria("paiso.idPais", "=", parametroCliente.getPaisByIdPaisOrigem().getIdPais());
		}else{
			sql.addCustomCriteria("paiso.idPais is null");
		}

		if(parametroCliente.getPaisByIdPaisDestino() != null) {
			sql.addCriteria("paisd.idPais", "=", parametroCliente.getPaisByIdPaisDestino().getIdPais());
		}else{
			sql.addCustomCriteria("paisd.idPais is null");
		}

		if(parametroCliente.getUnidadeFederativaByIdUfOrigem() != null) {
			sql.addCriteria("ufo.idUnidadeFederativa", "=", parametroCliente.getUnidadeFederativaByIdUfOrigem().getIdUnidadeFederativa());
		}else{
			sql.addCustomCriteria("ufo.idUnidadeFederativa is null");
		}

		if(parametroCliente.getUnidadeFederativaByIdUfDestino() != null) {
			sql.addCriteria("ufd.idUnidadeFederativa", "=", parametroCliente.getUnidadeFederativaByIdUfDestino().getIdUnidadeFederativa());
		}else{
			sql.addCustomCriteria("ufd.idUnidadeFederativa is null");
		}
		
		if(parametroCliente.getFilialByIdFilialOrigem() != null) {
			sql.addCriteria("fo.idFilial", "=", parametroCliente.getFilialByIdFilialOrigem().getIdFilial());
		}else{
			sql.addCustomCriteria("fo.idFilial is null");
		}
		
		if(parametroCliente.getFilialByIdFilialDestino() != null) {
			sql.addCriteria("fd.idFilial", "=", parametroCliente.getFilialByIdFilialDestino().getIdFilial());
		}else{
			sql.addCustomCriteria("fd.idFilial is null");
		}
		
		if(parametroCliente.getMunicipioByIdMunicipioOrigem() != null) {
			sql.addCriteria("mo.idMunicipio", "=", parametroCliente.getMunicipioByIdMunicipioOrigem().getIdMunicipio());
		}else{
			sql.addCustomCriteria("mo.idMunicipio is null");
		}
		
		if(parametroCliente.getMunicipioByIdMunicipioDestino() != null) {
			sql.addCriteria("md.idMunicipio", "=", parametroCliente.getMunicipioByIdMunicipioDestino().getIdMunicipio());
		}else{
			sql.addCustomCriteria("md.idMunicipio is null");
		}

		if(parametroCliente.getAeroportoByIdAeroportoOrigem() != null) {
			sql.addCriteria("ao.idAeroporto", "=", parametroCliente.getAeroportoByIdAeroportoOrigem().getIdAeroporto());
		}else{
			sql.addCustomCriteria("ao.idAeroporto is null");
		}

		if(parametroCliente.getAeroportoByIdAeroportoDestino() != null) {
			sql.addCriteria("ad.idAeroporto", "=", parametroCliente.getAeroportoByIdAeroportoDestino().getIdAeroporto());
		}else{
			sql.addCustomCriteria("ad.idAeroporto is null");
		}

		if(parametroCliente.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem() != null) {
			sql.addCriteria("tlo.idTipoLocalizacaoMunicipio", "=", parametroCliente.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem().getIdTipoLocalizacaoMunicipio());
		}else{
			sql.addCustomCriteria("tlo.idTipoLocalizacaoMunicipio is null");
		}

		if(parametroCliente.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino() != null) {
			sql.addCriteria("tld.idTipoLocalizacaoMunicipio", "=", parametroCliente.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino().getIdTipoLocalizacaoMunicipio());
		}else{
			sql.addCustomCriteria("tld.idTipoLocalizacaoMunicipio is null");
		}
		
		if (parametroCliente.getIdParametroCliente() != null) {
			sql.addCriteria("pc.idParametroCliente","<>",parametroCliente.getIdParametroCliente());
		}

		if(parametroCliente.getGrupoRegiaoOrigem() != null) {
			sql.addCriteria("gro.idGrupoRegiao", "=", parametroCliente.getGrupoRegiaoOrigem().getIdGrupoRegiao());
		}else{
			sql.addCustomCriteria("gro.idGrupoRegiao is null");
		}

		if(parametroCliente.getGrupoRegiaoDestino() != null) {
			sql.addCriteria("grd.idGrupoRegiao", "=", parametroCliente.getGrupoRegiaoDestino().getIdGrupoRegiao());
		}else{
			sql.addCustomCriteria("grd.idGrupoRegiao is null");
		}		
		
		List lista = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		if(lista != null && !lista.isEmpty()) {
			return (Long)lista.get(0);
		}
		return null;
	}
	

	
	/**
	 * Busca Parametro de uma determinada ROTA
	 * @param parametroClienteMap
	 * @param idTabelaDivisaoCliente
	 * @param dtInicioVigencia
	 * @return
	 */
	public Long findParametrizacaoAtivaByDtVigencia(Map parametroClienteMap, Long idTabelaDivisaoCliente, YearMonthDay dtInicioVigencia){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("pc.idParametroCliente","idParametroCliente");

		StringBuilder strFrom = new StringBuilder();
 		strFrom.append(getPersistentClass().getName()).append(" as pc ");
 		strFrom.append("join pc.tabelaDivisaoCliente as tdc ");
 		strFrom.append("join pc.zonaByIdZonaOrigem as zo ");
 		strFrom.append("join pc.zonaByIdZonaDestino as zd ");
 		strFrom.append("left outer join pc.paisByIdPaisDestino as paisd ");
 		strFrom.append("left outer join pc.paisByIdPaisOrigem as paiso ");
 		strFrom.append("left outer join pc.unidadeFederativaByIdUfOrigem as ufo ");
 		strFrom.append("left outer join pc.unidadeFederativaByIdUfDestino as ufd ");
 		strFrom.append("left outer join pc.filialByIdFilialOrigem as fo " );
 		strFrom.append("left outer join pc.filialByIdFilialDestino as fd " ); 
 		strFrom.append("left outer join pc.municipioByIdMunicipioOrigem as mo ");
 		strFrom.append("left outer join pc.municipioByIdMunicipioDestino as md ");
 		strFrom.append("left outer join pc.aeroportoByIdAeroportoOrigem as ao "); 
 		strFrom.append("left outer join pc.aeroportoByIdAeroportoDestino as ad ");
 		strFrom.append("left outer join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem as tlo " ); 
 		strFrom.append("left outer join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino as tld "); 
 		strFrom.append("left outer join pc.grupoRegiaoOrigem as gro " ); 
 		strFrom.append("left outer join pc.grupoRegiaoDestino as grd ");
		sql.addFrom(strFrom.toString());

		sql.addCriteria("pc.tpSituacaoParametro", "=", "A");
		sql.addCriteria("tdc.idTabelaDivisaoCliente", "=", idTabelaDivisaoCliente);
		sql.addCriteria("trunc(pc.dtVigenciaInicial)","<=",dtInicioVigencia,YearMonthDay.class);
		sql.addCriteria("trunc(pc.dtVigenciaFinal)",">=",dtInicioVigencia,YearMonthDay.class);
		
		if(parametroClienteMap.get("zonaByIdZonaOrigem.idZona") != null) {
			sql.addCriteria("zo.idZona", "=", parametroClienteMap.get("zonaByIdZonaOrigem.idZona"));
		}else{
			sql.addCustomCriteria("zo.idZona is null");
		}
		
		if(parametroClienteMap.get("zonaByIdZonaDestino.idZona") != null) {
			sql.addCriteria("zd.idZona", "=", parametroClienteMap.get("zonaByIdZonaDestino.idZona"));
		}else{
			sql.addCustomCriteria("zd.idZona is null");
		}
		
		if(parametroClienteMap.get("grupoRegiaoOrigem.idGrupoRegiao") != null) {
			sql.addCriteria("gro.idGrupoRegiao", "=", parametroClienteMap.get("grupoRegiaoOrigem.idGrupoRegiao"));
		}else{
			sql.addCustomCriteria("gro.idGrupoRegiao is null");
		}
		
		if(parametroClienteMap.get("grupoRegiaoDestino.idGrupoRegiao") != null) {
			sql.addCriteria("grd.idGrupoRegiao", "=", parametroClienteMap.get("grupoRegiaoDestino.idGrupoRegiao"));
		}else{
			sql.addCustomCriteria("grd.idGrupoRegiao is null");
		}
		
		if(parametroClienteMap.get("paisByIdPaisOrigem.idPais") != null) {
			sql.addCriteria("paiso.idPais", "=", parametroClienteMap.get("paisByIdPaisOrigem.idPais"));
		}else{
			sql.addCustomCriteria("paiso.idPais is null");
		}

		if(parametroClienteMap.get("paisByIdPaisDestino.idPais") != null) {
			sql.addCriteria("paisd.idPais", "=", parametroClienteMap.get("paisByIdPaisDestino.idPais"));
		}else{
			sql.addCustomCriteria("paisd.idPais is null");
		}

		if(parametroClienteMap.get("unidadeFederativaByIdUfOrigem.idUnidadeFederativa") != null) {
			sql.addCriteria("ufo.idUnidadeFederativa", "=", parametroClienteMap.get("unidadeFederativaByIdUfOrigem.idUnidadeFederativa"));
		}else{
			sql.addCustomCriteria("ufo.idUnidadeFederativa is null");
		}

		if(parametroClienteMap.get("unidadeFederativaByIdUfDestino.idUnidadeFederativa") != null) {
			sql.addCriteria("ufd.idUnidadeFederativa", "=", parametroClienteMap.get("unidadeFederativaByIdUfDestino.idUnidadeFederativa"));
		}else{
			sql.addCustomCriteria("ufd.idUnidadeFederativa is null");
		}
		
		if(parametroClienteMap.get("filialByIdFilialOrigem.idFilial") != null) {
			sql.addCriteria("fo.idFilial", "=", parametroClienteMap.get("filialByIdFilialOrigem.idFilial"));
		}else{
			sql.addCustomCriteria("fo.idFilial is null");
		}
		
		if(parametroClienteMap.get("filialByIdFilialDestino.idFilial") != null) {
			sql.addCriteria("fd.idFilial", "=", parametroClienteMap.get("filialByIdFilialDestino.idFilial"));
		}else{
			sql.addCustomCriteria("fd.idFilial is null");
		}
		
		if(parametroClienteMap.get("municipioByIdMunicipioOrigem.idMunicipio") != null) {
			sql.addCriteria("mo.idMunicipio", "=", parametroClienteMap.get("municipioByIdMunicipioOrigem.idMunicipio"));
		}else{
			sql.addCustomCriteria("mo.idMunicipio is null");
		}
		
		if(parametroClienteMap.get("municipioByIdMunicipioDestino.idMunicipio") != null) {
			sql.addCriteria("md.idMunicipio", "=", parametroClienteMap.get("municipioByIdMunicipioDestino.idMunicipio"));
		}else{
			sql.addCustomCriteria("md.idMunicipio is null");
		}

		if(parametroClienteMap.get("aeroportoByIdAeroportoOrigem.idAeroporto") != null) {
			sql.addCriteria("ao.idAeroporto", "=", parametroClienteMap.get("aeroportoByIdAeroportoOrigem.idAeroporto"));
		}else{
			sql.addCustomCriteria("ao.idAeroporto is null");
		}

		if(parametroClienteMap.get("aeroportoByIdAeroportoDestino.idAeroporto") != null) {
			sql.addCriteria("ad.idAeroporto", "=", parametroClienteMap.get("aeroportoByIdAeroportoDestino.idAeroporto"));
		}else{
			sql.addCustomCriteria("ad.idAeroporto is null");
		}

		
		List<Long> localizacoes = new ArrayList<>();
		localizacoes.add(TipoLocalizacaoMunicipioEnum.CAPITAL.getValue());
		localizacoes.add(TipoLocalizacaoMunicipioEnum.GRANDE_CAPITAL.getValue());
		
		if(parametroClienteMap.get("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio") != null) {
			if (localizacoes.contains(parametroClienteMap.get("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio"))) {
				sql.addCriteriaIn("tlo.idTipoLocalizacaoMunicipio", localizacoes.toArray());
			} else if (parametroClienteMap.get("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio").equals(TipoLocalizacaoMunicipioEnum.INTERIOR.getValue())) {
				sql.addCriteria("tlo.idTipoLocalizacaoMunicipio", "=", Long.valueOf(3));
			}
		}else{
			sql.addCustomCriteria("tlo.idTipoLocalizacaoMunicipio is null");
		}

		if(parametroClienteMap.get("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio") != null) {
			if (localizacoes.contains(parametroClienteMap.get("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio"))) {
				sql.addCriteriaIn("tld.idTipoLocalizacaoMunicipio", localizacoes.toArray());
			} else if (parametroClienteMap.get("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio").equals(TipoLocalizacaoMunicipioEnum.INTERIOR.getValue())) {
				sql.addCriteria("tld.idTipoLocalizacaoMunicipio", "=", Long.valueOf(3));
			}
		}else{
			sql.addCustomCriteria("tld.idTipoLocalizacaoMunicipio is null");
		}

		List lista = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		if(lista != null && !lista.isEmpty()) {
			return (Long)lista.get(0);
		}
		return null;
	}

	/**
	 * Busca Parametros Ativos referente a TabelaDivisaoCliente passada
	 * @param idTabelaDivisaoCliente
	 * @param dtInicioVigencia
	 * @return
	 */
	public List<ParametroCliente> findParametrizacaoAtiva(Long idTabelaDivisaoCliente, YearMonthDay dtInicioVigencia){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("pc");

		StringBuilder strFrom = new StringBuilder();
 		strFrom.append(getPersistentClass().getName()).append(" as pc ");
 		strFrom.append("join pc.tabelaDivisaoCliente as tdc ");
 		strFrom.append("join pc.zonaByIdZonaOrigem as zo ");
 		strFrom.append("join pc.zonaByIdZonaDestino as zd ");
 		strFrom.append("left outer join pc.paisByIdPaisDestino as paisd ");
 		strFrom.append("left outer join pc.paisByIdPaisOrigem as paiso ");
 		strFrom.append("left outer join pc.unidadeFederativaByIdUfOrigem as ufo ");
 		strFrom.append("left outer join pc.unidadeFederativaByIdUfDestino as ufd ");
 		strFrom.append("left outer join pc.filialByIdFilialOrigem as fo " );
 		strFrom.append("left outer join pc.filialByIdFilialDestino as fd " ); 
 		strFrom.append("left outer join pc.municipioByIdMunicipioOrigem as mo ");
 		strFrom.append("left outer join pc.municipioByIdMunicipioDestino as md ");
 		strFrom.append("left outer join pc.aeroportoByIdAeroportoOrigem as ao "); 
 		strFrom.append("left outer join pc.aeroportoByIdAeroportoDestino as ad ");
 		strFrom.append("left outer join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem as tlo " ); 
 		strFrom.append("left outer join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino as tld "); 
 		strFrom.append("left outer join pc.grupoRegiaoOrigem as gro " ); 
 		strFrom.append("left outer join pc.grupoRegiaoDestino as grd ");
		sql.addFrom(strFrom.toString());

		sql.addCriteria("pc.tpSituacaoParametro", "=", "A");
		sql.addCriteria("tdc.idTabelaDivisaoCliente", "=", idTabelaDivisaoCliente);
		sql.addCriteria("trunc(pc.dtVigenciaInicial)","<",dtInicioVigencia,YearMonthDay.class);
		sql.addCriteria("trunc(pc.dtVigenciaFinal)",">=",dtInicioVigencia,YearMonthDay.class);

		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}

	/**
	 * 
	 * @param tpSituacaoParametro
	 * @param dtVigenciaFinal
	 * @param idCliente
	 * @param idDivisaoCliente
	 * @param idTabelaDivisaoCliente
	 * @return
	 */
	public List findByIdClienteTpSituacao(DomainValue tpSituacaoParametro, YearMonthDay dtVigenciaFinal, Long idCliente, Long idDivisaoCliente, Long idTabelaDivisaoCliente) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pc");
		dc.createAlias("pc.tabelaDivisaoCliente","tdc");
		dc.createAlias("tdc.divisaoCliente","dc");
		dc.createAlias("dc.cliente","c");

		dc.add(Restrictions.eq("c.idCliente", idCliente));
		dc.add(Restrictions.eq("pc.tpSituacaoParametro", tpSituacaoParametro.getValue()));
		dc.add(Restrictions.ge("pc.dtVigenciaFinal", dtVigenciaFinal));

		if(idDivisaoCliente != null) {
			dc.add(Restrictions.eq("dc.idDivisaoCliente", idDivisaoCliente));
		}
		if(idTabelaDivisaoCliente != null) {
			dc.add(Restrictions.eq("tdc.idTabelaDivisaoCliente", idTabelaDivisaoCliente));
		}
		dc.addOrder(Order.asc("pc.idParametroCliente"));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	/**
	 * @param idTabelaDivisaoCliente
	 * @param tpSituacaoParametro
	 * @param dtVigenciaInicialTabela (deve ser < dtVigenciaFinal dos Parametros)
	 * @return
	 */
	public List findByIdTabelaDivisaoCliente(Long idTabelaDivisaoCliente, DomainValue tpSituacaoParametro, YearMonthDay dtVigenciaInicialTabela) {
		StringBuilder from = new StringBuilder()
			.append(getPersistentClass().getName()).append(" as pc")
			.append(" join fetch pc.tabelaDivisaoCliente as tdc");

		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(from.toString());

		/** Filters */
		if (idTabelaDivisaoCliente != null) {
			hql.addCriteria("tdc.id","=",idTabelaDivisaoCliente);
		}
		if (tpSituacaoParametro != null) {
			hql.addCriteria("pc.tpSituacaoParametro","=",tpSituacaoParametro.getValue());
		}
		//*** Filtra Parametros que estejam no Periodo da Tabela
		if (dtVigenciaInicialTabela != null) {
			hql.addCustomCriteria("(pc.dtVigenciaFinal IS NULL OR pc.dtVigenciaFinal > ?)");
			hql.addCriteriaValue(dtVigenciaInicialTabela);
		}
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	/**
	 * Atualiza a data de vigência final em todos os parametros relacionados com
	 * a tabela divisao cliente recebida.
	 * 
	 * @param dtVigenciaFinal
	 *            data de vigência final para ser atualizada
	 * @param idTabelaDivisaoCliente
	 *            identificador da tabela divisão cliente
	 * @return numero de entidades atualizadas
	 */
	public Integer updateVigenciaFinalByIdTabelaDivisaoCliente(YearMonthDay dtVigenciaFinal, Long idTabelaDivisaoCliente) {
		StringBuilder hql = new StringBuilder()
		.append("update ").append(getPersistentClass().getName()).append(" \n")
		.append("set dtVigenciaFinal = ? \n")
		.append("where tabelaDivisaoCliente.id = ?");
		
		List<Object> params = new ArrayList<>();
		params.add(dtVigenciaFinal);
		params.add(idTabelaDivisaoCliente);
		
		return executeHql(hql.toString(), params);
	}

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindByIdLazyProperties(java.util.Map)
	 */
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("zonaByIdZonaOrigem", FetchMode.JOIN);
		lazyFindById.put("zonaByIdZonaDestino", FetchMode.JOIN);
						  
		lazyFindById.put("unidadeFederativaByIdUfOrigem", FetchMode.JOIN);
		lazyFindById.put("unidadeFederativaByIdUfDestino", FetchMode.JOIN);
		
		lazyFindById.put("municipioByIdMunicipioOrigem", FetchMode.JOIN);
		lazyFindById.put("municipioByIdMunicipioDestino", FetchMode.JOIN);
		
		lazyFindById.put("aeroportoByIdAeroportoOrigem", FetchMode.JOIN);
		lazyFindById.put("aeroportoByIdAeroportoOrigem.pessoa", FetchMode.JOIN);
		lazyFindById.put("aeroportoByIdAeroportoDestino", FetchMode.JOIN);
		lazyFindById.put("aeroportoByIdAeroportoDestino.pessoa", FetchMode.JOIN);
		
		lazyFindById.put("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem", FetchMode.JOIN);
		lazyFindById.put("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino", FetchMode.JOIN);
		
		lazyFindById.put("grupoRegiaoOrigem", FetchMode.JOIN);
		lazyFindById.put("grupoRegiaoDestino", FetchMode.JOIN);
		
		lazyFindById.put("paisByIdPaisDestino", FetchMode.JOIN);
		lazyFindById.put("paisByIdPaisOrigem", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialOrigem", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialOrigem.pessoa", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialDestino", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialDestino.pessoa", FetchMode.JOIN);
		
		lazyFindById.put("simulacao", FetchMode.JOIN);
		lazyFindById.put("proposta", FetchMode.JOIN);
		lazyFindById.put("proposta.simulacao", FetchMode.JOIN);
		
		lazyFindById.put("tabelaPreco", FetchMode.JOIN);
		lazyFindById.put("tabelaPreco.tipoTabelaPreco", FetchMode.JOIN);
		lazyFindById.put("tabelaPreco.tipoTabelaPreco.servico", FetchMode.JOIN);
		lazyFindById.put("tabelaPreco.subtipoTabelaPreco", FetchMode.JOIN);
		lazyFindById.put("tabelaPreco.moeda", FetchMode.JOIN);

		super.initFindByIdLazyProperties(lazyFindById);
	}
	
	public List<Map> findListParametrosByRotaFilial(Long idFilial) {
		
		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("c.id"), "idCliente")		
		.add(Projections.property("pc.id"), "idParametroCliente")		
		.add(Projections.property("pc.filialByIdFilialOrigem.id"), "idFilialOrigem")		
		.add(Projections.property("pc.filialByIdFilialDestino.id"), "idFilialDestino");		
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pc");
		dc.createAlias("pc.tabelaDivisaoCliente","tdc");
		dc.createAlias("tdc.divisaoCliente","dc");
		dc.createAlias("dc.cliente","c");
		dc.setProjection(pl);
		dc.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		
		dc.add(Restrictions.or(Restrictions.eq("pc.filialByIdFilialOrigem.id", idFilial), Restrictions.eq("pc.filialByIdFilialDestino.id", idFilial)));
		dc.add(Restrictions.ge("pc.dtVigenciaInicial", JTDateTimeUtils.getDataAtual() ));
						
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);		

	}
	
	public List<ParametroCliente> findParametroClienteVigenteByRota(Long idCliente, Long idParamOrigem, Long idParamDestino) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pc");
		dc.createAlias("pc.tabelaDivisaoCliente","tdc");
		dc.createAlias("tdc.divisaoCliente","dc");
		dc.createAlias("dc.cliente","c");
		
		dc.add(Restrictions.ge("c.id", idCliente ));
		dc.add(Restrictions.ge("pc.filialByIdFilialOrigem.id", idParamOrigem ));
		dc.add(Restrictions.ge("pc.filialByIdFilialDestino.id", idParamDestino ));
		dc.add(Restrictions.ge("pc.dtVigenciaInicial", JTDateTimeUtils.getDataAtual()));
		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);		
		
	}
	
	
	public Integer existPercentualMenorDoAcordado(Long idReajusteCliente) {
		String sql = new StringBuilder()
				.append(" select count(1)  ")
				.append(" from  reajuste_cliente rc  ")
				.append(" where rc.ID_REAJUSTE_CLIENTE = ? ")
				.append(" and   rc.BL_EFETIVADO = 'N'  ")
				.append(" and   ( exists( select pc.id_parametro_cliente   ")
				.append("  				  from parametro_cliente pc	 ")
				.append("   			  where pc.ID_TABELA_DIVISAO_CLIENTE = rc.ID_TABELA_DIVISAO_CLIENTE	 ")
				.append(" 				  and   pc.DT_VIGENCIA_INICIAL = rc.DT_INICIO_VIGENCIA  ")
				.append("  				  and   pc.TP_SITUACAO_PARAMETRO = 'R' ")
				.append(" 				  and  (  pc.pc_reaj_frete_peso < rc.PC_REAJUSTE_SUGERIDO or pc.PC_REAJ_VL_MINIMO_FRETE_QUILO < rc.PC_REAJUSTE_SUGERIDO or pc.PC_REAJ_VL_FRETE_VOLUME < rc.PC_REAJUSTE_SUGERIDO or  ")
				.append("                         pc.pc_reaj_tarifa_minima < rc.PC_REAJUSTE_SUGERIDO or pc.PC_REAJ_VL_TARIFA_ESPECIFICA < rc.PC_REAJUSTE_SUGERIDO or pc_reaj_advalorem < rc.PC_REAJUSTE_SUGERIDO or   ")
				.append("                         pc.PC_REAJ_ADVALOREM_2 < rc.PC_REAJUSTE_SUGERIDO or pc.PC_REAJ_VL_MINIMO_FRETE_PERCEN < rc.PC_REAJUSTE_SUGERIDO or pc.PC_REAJ_VL_TONELADA_FRETE_PERC < rc.PC_REAJUSTE_SUGERIDO or  ")
				.append("                         pc.PC_REAJ_MINIMO_GRIS < rc.PC_REAJUSTE_SUGERIDO or pc.PC_REAJ_PEDAGIO < rc.PC_REAJUSTE_SUGERIDO or pc.PC_REAJ_MINIMO_TDE < rc.PC_REAJUSTE_SUGERIDO or ")
				.append("  						  pc.PC_REAJUSTE_MINIMO_TRT < rc.PC_REAJUSTE_SUGERIDO or pc.PC_REAJ_PERCENTUAL_TDE < rc.PC_REAJUSTE_SUGERIDO or pc.PC_REAJ_PERCENTUAL_GRIS < rc.PC_REAJUSTE_SUGERIDO or ")
				.append("  						  pc.PC_REAJ_PERCENTUAL_TRT < rc.PC_REAJUSTE_SUGERIDO or pc.PC_REAJ_VL_MINIMO_FRETE_PESO < rc.PC_REAJUSTE_SUGERIDO)) or ")
				.append("  		 exists ( select gc.id_generalidade_cliente ")
				.append("                 from parametro_cliente pc1, generalidade_cliente gc ")
				.append("                 where pc1.ID_TABELA_DIVISAO_CLIENTE = rc.ID_TABELA_DIVISAO_CLIENTE   ")
				.append("                 and   pc1.DT_VIGENCIA_INICIAL = rc.DT_INICIO_VIGENCIA ")
				.append("                 and   pc1.TP_SITUACAO_PARAMETRO = 'R' ")
				.append("                 and   gc.ID_PARAMETRO_CLIENTE = pc1.ID_PARAMETRO_CLIENTE ")
				.append("                 and   (gc.PC_REAJ_GENERALIDADE < rc.PC_REAJUSTE_SUGERIDO or gc.PC_REAJUSTE_MINIMO < rc.PC_REAJUSTE_SUGERIDO)) or ")
				.append("       exists (  select tc.id_taxa_cliente ")
				.append("                 from parametro_cliente pc1, taxa_cliente tc ")
				.append("                 where pc1.ID_TABELA_DIVISAO_CLIENTE = rc.ID_TABELA_DIVISAO_CLIENTE   ")
				.append("                 and   pc1.DT_VIGENCIA_INICIAL = rc.DT_INICIO_VIGENCIA ")
				.append("                 and   pc1.TP_SITUACAO_PARAMETRO = 'R' ")
				.append("                 and   tc.ID_PARAMETRO_CLIENTE = pc1.ID_PARAMETRO_CLIENTE ")
				.append("                 and   (tc.PC_REAJ_TAXA < rc.PC_REAJUSTE_SUGERIDO or tc.PC_REAJ_VL_EXCEDENTE < rc.PC_REAJUSTE_SUGERIDO)) or  ")
				.append("      rc.PC_REAJUSTE_ACORDADO < rc.PC_REAJUSTE_SUGERIDO )  ")
				.toString();
		
		return getAdsmHibernateTemplate().getRowCountBySql(sql, new Object[]{idReajusteCliente});	
	}

	public void removeParamClienteAndAllDependecies(Long idParamCliente){
		Map<String,Object> params = new HashMap<>();
		params.put("idParamCliente",idParamCliente);
	
		String sqlTaxaCliente 		  = new StringBuilder("DELETE FROM TAXA_CLIENTE WHERE ID_PARAMETRO_CLIENTE = :idParamCliente").toString();
		String sqlGeneralidadeCliente = new StringBuilder("DELETE FROM GENERALIDADE_CLIENTE WHERE ID_PARAMETRO_CLIENTE = :idParamCliente").toString();
		String sqlParamCliente 		  = new StringBuilder("DELETE FROM PARAMETRO_CLIENTE WHERE ID_PARAMETRO_CLIENTE = :idParamCliente").toString();
		
		getAdsmHibernateTemplate().executeUpdateBySql(sqlTaxaCliente, params);
		getAdsmHibernateTemplate().executeUpdateBySql(sqlGeneralidadeCliente, params);
		getAdsmHibernateTemplate().executeUpdateBySql(sqlParamCliente, params);
	}

	public void removeParametroClienteDataSuperiorReajuste(Long idTabelaDivisaoCliente){
		Map<String,Object> params = new HashMap<>();
		params.put("idTabelaDivisaoCliente",idTabelaDivisaoCliente);
		String sqlRemoveParametroCliente = new StringBuilder("DELETE FROM PARAMETRO_CLIENTE WHERE ID_TABELA_DIVISAO_CLIENTE = :idTabelaDivisaoCliente AND DT_VIGENCIA_INICIAL > TRUNC(SYSDATE)").toString();

		getAdsmHibernateTemplate().executeUpdateBySql(sqlRemoveParametroCliente, params);
	}
	
	public List findParamClienteByIdTabDivisaoClienteAndSituacaoAndVigInicial(Long idTabelaDivisaoCliente, DomainValue tpSituacaoParametro, YearMonthDay dtVigenciaInicialTabela) {
		String from = new StringBuilder(getPersistentClass().getName()).append(" as pc join fetch pc.tabelaDivisaoCliente as tdc").toString();

		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(from);

		if (idTabelaDivisaoCliente != null) {
			hql.addCriteria("tdc.idTabelaDivisaoCliente","=",idTabelaDivisaoCliente);
		}
		if (tpSituacaoParametro != null) {
			hql.addCriteria("pc.tpSituacaoParametro","=",tpSituacaoParametro.getValue());
		}
		if (dtVigenciaInicialTabela != null) {
			hql.addCriteria("trunc(pc.dtVigenciaInicial)","=",dtVigenciaInicialTabela,YearMonthDay.class);
		}
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	public Simulacao findSimulacaoByIdParametroCliente(Long id) {
		
		String sql = "select p.proposta.simulacao from "+ParametroCliente.class.getSimpleName()+ " p "+
				" where p.id = ?";
		
		return (Simulacao) getAdsmHibernateTemplate().findUniqueResult(sql,new Object[]{id});
	}

	/*
	 * Erro calculo de frete que nao considera o ultimo dia como sendo valido, entao encerramento e inicio estão pro mesmo dia;
	 * Quando consertado isso, setar na vigencia final: dataVigenciaEncerramento.minusDays(1);
	 */
	public void storeEncerrandoParametro(ParametroCliente parametroCliente, YearMonthDay dataVigenciaEncerramento){
		parametroCliente.setDtVigenciaFinal(dataVigenciaEncerramento.minusDays(1));
		store(parametroCliente, true);
	}
	
	
	public List<Map<String,Object>> listFretePeso(Long idTabDivisaoCliente, YearMonthDay vigenciaInicial){
		String query = new StringBuilder()
				.append(" select     ") 
				.append(" 	SUBSTR(REGEXP_SUBSTR( p_origem.nm_pais_i, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  p_origem.nm_pais_i, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || UF_ORIGEM.SG_UNIDADE_FEDERATIVA || ' ' || F_ORIGEM.SG_FILIAL || ' ' || M_ORIGEM.nm_municipio || ' ' || SUBSTR(REGEXP_SUBSTR( TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || GR_ORIGEM.DS_GRUPO_REGIAO || ' ' || A_ORIGEM.SG_AEROPORTO as ORIGEM,  ")
				.append("   SUBSTR(REGEXP_SUBSTR( p_destino.nm_pais_i, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  p_destino.nm_pais_i, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || UF_DESTINO.SG_UNIDADE_FEDERATIVA || ' ' || F_DESTINO.SG_FILIAL || ' ' || M_DESTINO.nm_municipio || ' ' || SUBSTR(REGEXP_SUBSTR( TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || GR_DESTINO.DS_GRUPO_REGIAO || ' ' || A_DESTINO.SG_AEROPORTO as DESTINO,    ")
				.append("   pcn.id_parametro_cliente as ID_PARAM_CLIENTE,  	 	 ")
				.append("   pcn.pc_reaj_advalorem as ADVALOREN_PERCENTUAL,  	 ")
				.append("   pcn.pc_reaj_advalorem_2 as ADVALOREN2_PERCENTUAL,  	 ")
				.append("   pcn.pc_reaj_vl_tonelada_frete_perc as TONELADA_FRETE_PERCENTUAL,	 ")
				.append("   pcn.pc_reaj_frete_peso as FRETE_PESO_PERCENTUAL,  	 ")
				.append("   pcn.pc_reaj_vl_frete_volume as FRETE_VOLUME_PERCENTUAL,  	 ")
				.append("   pcn.pc_reaj_vl_tarifa_especifica as TARIFA_ESPECIFICA_PERCENTUAL,   ")
				.append("   pcn.pc_reaj_tarifa_minima as TARIFA_MINIMA_PERCENTUAL,  	 ")
				.append("   pcn.pc_reaj_vl_minimo_frete_percen as MINIMO_FRETE_PERCENTUAL,  	 ")
				.append("   pcn.pc_reaj_vl_minimo_frete_quilo as MINIMO_FRETE_QUILO_PERCENTUAL,	 ")
				.append("   pcn.PC_REAJ_VL_MINIMO_FRETE_PESO as MINIMO_FRETE_PESO_PERCENTUAL,   ")
				.append("   pcn.PC_REAJ_VL_MINIMO_PROG as MINIMO_PROGRAMADO_PERCENTUAL,   ")
				.append("   pcn.PC_REAJ_REFERENCIA as REFERENCIA_PERCENTUAL,   	   ")

				.append("   round(pcn.vl_advalorem - (pcn.vl_advalorem * pcn.pc_reaj_advalorem / (100 + pcn.pc_reaj_advalorem)),5 ) as ADVALOREN_ORIGINAL,   						")
				.append("   round(pcn.vl_advalorem_2 - (pcn.vl_advalorem_2 * pcn.pc_reaj_advalorem_2 / (100 + pcn.pc_reaj_advalorem_2)),2 ) as ADVALOREN2_ORIGINAL,   					")
				.append("   round(pcn.vl_tonelada_frete_percentual - (pcn.vl_tonelada_frete_percentual * pcn.pc_reaj_vl_tonelada_frete_perc / (100 + pcn.pc_reaj_vl_tonelada_frete_perc)),2 ) as TONELADA_FRETE_ORIGINAL,   	")
				.append("   round(pcn.vl_frete_peso - (pcn.vl_frete_peso * pcn.pc_reaj_frete_peso / (100 + pcn.pc_reaj_frete_peso)),5 ) as FRETE_PESO_ORIGINAL,   					")
				.append("   round(pcn.vl_frete_volume - (pcn.vl_frete_volume * pcn.pc_reaj_vl_frete_volume / (100 + pcn.pc_reaj_vl_frete_volume)),2 ) as FRETE_VOLUME_ORIGINAL,   				")
				.append("   round(pcn.vl_tbl_especifica - (pcn.vl_tbl_especifica * pcn.pc_reaj_vl_tarifa_especifica / (100 + pcn.pc_reaj_vl_tarifa_especifica)),2 ) as TARIFA_ESPECIFICA_ORIGINAL	,   		")
				.append("   round(pcn.vl_tarifa_minima - (pcn.vl_tarifa_minima * pcn.pc_reaj_tarifa_minima / (100 + pcn.pc_reaj_tarifa_minima)),2 ) as TARIFA_MINIMA_ORIGINAL, 				")
				.append("   round(pcn.vl_minimo_frete_percentual - (pcn.vl_minimo_frete_percentual * pcn.pc_reaj_vl_minimo_frete_percen / (100 + pcn.pc_reaj_vl_minimo_frete_percen)),2 ) as  MINIMO_FRETE_ORIGINAL,   		")
				.append("   round(pcn.vl_minimo_frete_quilo - (pcn.vl_minimo_frete_quilo * pcn.pc_reaj_vl_minimo_frete_quilo / (100 + pcn.pc_reaj_vl_minimo_frete_quilo)),2 ) as MINIMO_FRETE_QUILO_ORIGINAL,  		")
				.append("   round(pcn.vl_min_frete_peso - (pcn.vl_min_frete_peso * pcn.PC_REAJ_VL_MINIMO_FRETE_PESO / (100 + pcn.PC_REAJ_VL_MINIMO_FRETE_PESO)),2 ) as MINIMO_FRETE_PESO_ORIGINAL,   			")
				.append("   round(pcn.vl_perc_minimo_progr - (pcn.vl_perc_minimo_progr * pcn.PC_REAJ_VL_MINIMO_PROG / (100 + pcn.PC_REAJ_VL_MINIMO_PROG)),2 ) as  MINIMO_PROGRAMADO_ORIGINAL,			")
				.append("   round(pcn.vl_valor_referencia - (pcn.vl_valor_referencia * pcn.PC_REAJ_REFERENCIA / (100 + pcn.PC_REAJ_REFERENCIA)),2 ) as REFERENCIA_ORIGINAL  , ")
				
				.append("   pcn.vl_advalorem  as ADVALOREN_CALCULADO , ")
				.append("   pcn.vl_advalorem_2  as ADVALOREN2_CALCULADO , ")
				.append("   pcn.vl_tonelada_frete_percentual  as TONELADA_FRETE_CALCULADO , ")
				.append("   pcn.vl_frete_peso   as FRETE_PESO_CALCULADO , ")
				.append("   pcn.vl_frete_volume  as FRETE_VOLUME_CALCULADO , ")
				.append("   pcn.vl_tbl_especifica  as TARIFA_ESPECIFICA_CALCULADO , ")
				.append("   pcn.vl_tarifa_minima  as TARIFA_MINIMA_CALCULADO , ")
				.append("   pcn.vl_minimo_frete_percentual  as MINIMO_FRETE_CALCULADO , ")
				.append("   pcn.vl_minimo_frete_quilo  as MINIMO_FRETE_QUILO_CALCULADO , ")
				.append("   pcn.vl_min_frete_peso  as MINIMO_FRETE_PESO_CALCULADO , ")
				.append("   pcn.vl_perc_minimo_progr  as MINIMO_PROGRAMADO_CALCULADO , ")
				.append("   pcn.vl_valor_referencia  as REFERENCIA_CALCULADO  ")
				
				.append(" from parametro_cliente pcn, unidade_federativa UF_ORIGEM, municipio M_ORIGEM,  filial  F_ORIGEM, tipo_localizacao_municipio TLM_ORIGEM, grupo_regiao GR_ORIGEM, ")  
				.append("      aeroporto A_ORIGEM, unidade_federativa UF_DESTINO, municipio M_DESTINO, filial F_DESTINO, tipo_localizacao_municipio TLM_DESTINO, grupo_regiao GR_DESTINO, aeroporto A_DESTINO, pais p_origem, pais p_destino ")
				.append(" where   pcn.ID_TABELA_DIVISAO_CLIENTE = ?   ")
				.append("   and   (pcn.tp_situacao_parametro = 'R' OR pcn.tp_situacao_parametro = 'A')    ")
				.append("   and   pcn.dt_vigencia_inicial = ?    ")
				.append("   and   pcn.dt_vigencia_final > = trunc(sysdate)    ")
				.append("   and   pcn.id_pais_origem = p_origem.ID_PAIS(+)    ")
				.append("   and   pcn.id_uf_origem = UF_ORIGEM.id_unidade_federativa(+)    ")
				.append("   and   pcn.id_municipio_origem = M_ORIGEM.id_municipio(+)     ")
				.append("   and   pcn.id_filial_origem = F_ORIGEM.id_filial(+)     ")
				.append("   and   pcn.id_tipo_loc_municipio_origem = TLM_ORIGEM.id_tipo_localizacao_municipio(+)     ")
				.append("   and   pcn.id_grupo_regiao_origem = GR_ORIGEM.id_grupo_regiao(+)      ")
				.append("   and   pcn.id_aeroporto_origem = A_ORIGEM.id_aeroporto(+)      ")
				.append("   and   pcn.id_pais_destino = p_destino.ID_PAIS(+)    ")
				.append("   and   pcn.id_uf_destino = UF_DESTINO.id_unidade_federativa(+)     ")
				.append("   and   pcn.id_municipio_destino = M_DESTINO.id_municipio(+)     ")
				.append("   and   pcn.id_filial_destino = F_DESTINO.id_filial(+)        ")
				.append("   and   pcn.id_tipo_loc_municipio_destino = TLM_DESTINO.id_tipo_localizacao_municipio(+)    ")
				.append("   and   pcn.id_grupo_regiao_destino = GR_DESTINO.id_grupo_regiao(+)     ")
				.append("   and   pcn.id_aeroporto_destino = A_DESTINO.id_aeroporto(+)    ")
				.toString();
		
		RowMapper rowMapper = new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<>();
				map.put("ORIGEM", resultSet.getString(1));
				map.put("DESTINO", resultSet.getString(2));
				map.put("ID_PARAM_CLIENTE", 	resultSet.getLong(3));
				
				map.put("ADVALOREN_PERCENTUAL", 		resultSet.getBigDecimal(4));
				map.put("ADVALOREN2_PERCENTUAL", 		resultSet.getBigDecimal(5));
				map.put("TONELADA_FRETE_PERCENTUAL",	resultSet.getBigDecimal(6));
				map.put("FRETE_PESO_PERCENTUAL", 		resultSet.getBigDecimal(7));
				map.put("FRETE_VOLUME_PERCENTUAL", 		resultSet.getBigDecimal(8));
				map.put("TARIFA_ESPECIFICA_PERCENTUAL", resultSet.getBigDecimal(9));
				map.put("TARIFA_MINIMA_PERCENTUAL", 	resultSet.getBigDecimal(10));
				map.put("MINIMO_FRETE_PERCENTUAL", 		resultSet.getBigDecimal(11));
				map.put("MINIMO_FRETE_QUILO_PERCENTUAL", resultSet.getBigDecimal(12));
				map.put("MINIMO_FRETE_PESO_PERCENTUAL", resultSet.getBigDecimal(13));
				map.put("MINIMO_PROGRAMADO_PERCENTUAL", resultSet.getBigDecimal(14));
				map.put("REFERENCIA_PERCENTUAL",		resultSet.getBigDecimal(15));
			
				map.put("ADVALOREN_ORIGINAL", 			resultSet.getBigDecimal(16));
				map.put("ADVALOREN2_ORIGINAL", 			resultSet.getBigDecimal(17));
				map.put("TONELADA_FRETE_ORIGINAL", 		resultSet.getBigDecimal(18));
				map.put("FRETE_PESO_ORIGINAL", 			resultSet.getBigDecimal(19));
				map.put("FRETE_VOLUME_ORIGINAL", 		resultSet.getBigDecimal(20));
				map.put("TARIFA_ESPECIFICA_ORIGINAL", 	resultSet.getBigDecimal(21));
				map.put("TARIFA_MINIMA_ORIGINAL", 		resultSet.getBigDecimal(22));
				map.put("MINIMO_FRETE_ORIGINAL", 		resultSet.getBigDecimal(23));
				map.put("MINIMO_FRETE_QUILO_ORIGINAL", 	resultSet.getBigDecimal(24));
				map.put("MINIMO_FRETE_PESO_ORIGINAL", 	resultSet.getBigDecimal(25));
				map.put("MINIMO_PROGRAMADO_ORIGINAL", 	resultSet.getBigDecimal(26));
				map.put("REFERENCIA_ORIGINAL", 			resultSet.getBigDecimal(27));
				
				map.put("ADVALOREN_CALCULADO", 			resultSet.getBigDecimal(28));
				map.put("ADVALOREN2_CALCULADO", 		resultSet.getBigDecimal(29));
				map.put("TONELADA_FRETE_CALCULADO", 	resultSet.getBigDecimal(30));
				map.put("FRETE_PESO_CALCULADO", 		resultSet.getBigDecimal(31));
				map.put("FRETE_VOLUME_CALCULADO", 		resultSet.getBigDecimal(32));
				map.put("TARIFA_ESPECIFICA_CALCULADO", 	resultSet.getBigDecimal(33));
				map.put("TARIFA_MINIMA_CALCULADO", 		resultSet.getBigDecimal(34));
				map.put("MINIMO_FRETE_CALCULADO", 		resultSet.getBigDecimal(35));
				map.put("MINIMO_FRETE_QUILO_CALCULADO", resultSet.getBigDecimal(36));
				map.put("MINIMO_FRETE_PESO_CALCULADO", 	resultSet.getBigDecimal(37));
				map.put("MINIMO_PROGRAMADO_CALCULADO", 	resultSet.getBigDecimal(38));
				map.put("REFERENCIA_CALCULADO", 		resultSet.getBigDecimal(39));
				
				return map;
			}
		};
		
		Object[] params = new Object[]{ idTabDivisaoCliente , vigenciaInicial };
		return jdbcTemplate.query(query, JodaTimeUtils.jdbcPureParamConverter(jdbcTemplate, params) , rowMapper);
	}
	
	public List<Map<String,Object>> listGeneralidades(Long idTabDivisaoCliente, YearMonthDay vigenciaInicial){
		String query = new StringBuilder()
				.append(" select     ") 
				.append(" 	SUBSTR(REGEXP_SUBSTR( p_origem.nm_pais_i, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  p_origem.nm_pais_i, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || UF_ORIGEM.SG_UNIDADE_FEDERATIVA || ' ' || F_ORIGEM.SG_FILIAL || ' ' || M_ORIGEM.nm_municipio || ' ' || SUBSTR(REGEXP_SUBSTR( TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || GR_ORIGEM.DS_GRUPO_REGIAO || ' ' || A_ORIGEM.SG_AEROPORTO as ORIGEM,  ")
				.append("   SUBSTR(REGEXP_SUBSTR( p_destino.nm_pais_i, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  p_destino.nm_pais_i, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || UF_DESTINO.SG_UNIDADE_FEDERATIVA || ' ' || F_DESTINO.SG_FILIAL || ' ' || M_DESTINO.nm_municipio || ' ' || SUBSTR(REGEXP_SUBSTR( TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || GR_DESTINO.DS_GRUPO_REGIAO || ' ' || A_DESTINO.SG_AEROPORTO as DESTINO,    ")
				.append("   pcn.id_parametro_cliente as ID_PARAM_CLIENTE,  	 	")
				.append("   gcn.id_generalidade_cliente as ID_GENERALIDADE_CLIENTE,  	")
				.append("   gcn.pc_reaj_generalidade as PERCENTUAL,  	 			")
				.append("   gcn.pc_reajuste_minimo as MINIMO_PERCENTUAL,  	 	")
				.append("   round(gcn.vl_generalidade - (gcn.vl_generalidade * gcn.pc_reaj_generalidade / (100 + gcn.pc_reaj_generalidade)),2 ) as VALOR, ")
				.append("   round(gcn.vl_minimo - (gcn.vl_minimo * gcn.pc_reajuste_minimo / (100 + gcn.pc_reajuste_minimo)),2 ) as  MINIMO_VALOR, ")
				.append("   SUBSTR(REGEXP_SUBSTR( pp.nm_parcela_preco_i, 'pt_BR»[^¦]+'), INSTR(REGEXP_SUBSTR( pp.nm_parcela_preco_i, 'pt_BR»[^¦]+'), 'pt_BR»')+LENGTH('pt_BR»')) as GENERALIDADE, ")
				.append("   gcn.vl_generalidade as CALCULADO , ")
				.append("   gcn.vl_minimo as MINIMO_CALCULADO  ")
				.append(" from parametro_cliente pcn, generalidade_cliente gcn, unidade_federativa UF_ORIGEM, municipio M_ORIGEM, filial F_ORIGEM, tipo_localizacao_municipio TLM_ORIGEM, grupo_regiao GR_ORIGEM,    ")  
				.append("      aeroporto A_ORIGEM, unidade_federativa UF_DESTINO, municipio M_DESTINO, filial F_DESTINO, tipo_localizacao_municipio TLM_DESTINO, grupo_regiao GR_DESTINO, aeroporto A_DESTINO, pais p_origem, pais p_destino, parcela_preco pp  ")
				.append(" where   pcn.ID_TABELA_DIVISAO_CLIENTE = ?     ")
				.append("   and   pp.id_parcela_preco = gcn.id_parcela_preco    ")
				.append("   and   pcn.id_parametro_cliente = gcn.id_parametro_cliente    ")
				.append("   and   (pcn.tp_situacao_parametro = 'R' OR pcn.tp_situacao_parametro = 'A')    ")
				.append("   and   pcn.dt_vigencia_inicial = ?    ")
				.append("   and   pcn.dt_vigencia_final > = trunc(sysdate)    ")
				.append("   and   pcn.id_pais_origem = p_origem.ID_PAIS(+)    ")
				.append("   and   pcn.id_uf_origem = UF_ORIGEM.id_unidade_federativa(+)     ")
				.append("   and   pcn.id_municipio_origem = M_ORIGEM.id_municipio(+)     ")
				.append("   and   pcn.id_filial_origem = F_ORIGEM.id_filial(+)       ")
				.append("   and   pcn.id_tipo_loc_municipio_origem = TLM_ORIGEM.id_tipo_localizacao_municipio(+)    ")
				.append("   and   pcn.id_grupo_regiao_origem = GR_ORIGEM.id_grupo_regiao(+)      ")
				.append("   and   pcn.id_aeroporto_origem = A_ORIGEM.id_aeroporto(+)     ")
				.append("   and   pcn.id_pais_destino = p_destino.ID_PAIS(+)    ")
				.append("   and   pcn.id_uf_destino = UF_DESTINO.id_unidade_federativa(+)    ")
				.append("   and   pcn.id_municipio_destino = M_DESTINO.id_municipio(+)     ")
				.append("   and   pcn.id_filial_destino = F_DESTINO.id_filial(+)     ")
				.append("   and   pcn.id_tipo_loc_municipio_destino = TLM_DESTINO.id_tipo_localizacao_municipio(+)      ")
				.append("   and   pcn.id_grupo_regiao_destino = GR_DESTINO.id_grupo_regiao(+)     ")
				.append("   and   pcn.id_aeroporto_destino = A_DESTINO.id_aeroporto(+)    ")
				.append("  order by GENERALIDADE ") 
				.toString();
		
		RowMapper rowMapper = new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<>();
				map.put("ORIGEM", 					resultSet.getString(1));
				map.put("DESTINO", 					resultSet.getString(2));
				map.put("ID_PARAM_CLIENTE", 		resultSet.getLong(3));
				map.put("ID_GENERALIDADE_CLIENTE", 	resultSet.getLong(4));
				map.put("PERCENTUAL", 				resultSet.getBigDecimal(5));
				map.put("MINIMO_PERCENTUAL",		resultSet.getBigDecimal(6));
				map.put("VALOR", 					resultSet.getBigDecimal(7));
				map.put("MINIMO_VALOR", 			resultSet.getBigDecimal(8));
				map.put("GENERALIDADE", 			resultSet.getString(9));
				map.put("CALCULADO", 				resultSet.getBigDecimal(10));
				map.put("MINIMO_CALCULADO", 		resultSet.getBigDecimal(11));
				return map;
			}
		};
		
		Object[] params = new Object[]{ idTabDivisaoCliente , vigenciaInicial };
		return jdbcTemplate.query(query, JodaTimeUtils.jdbcPureParamConverter(jdbcTemplate, params) , rowMapper);
	}
				
	public List<Map<String,Object>> listGeneralidadesObrigatorias(Long idTabDivisaoCliente, YearMonthDay vigenciaInicial){
		String query = new StringBuilder()
				.append(" select     ") 
				.append(" 	SUBSTR(REGEXP_SUBSTR( p_origem.nm_pais_i, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  p_origem.nm_pais_i, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || UF_ORIGEM.SG_UNIDADE_FEDERATIVA || ' ' || F_ORIGEM.SG_FILIAL || ' ' || M_ORIGEM.nm_municipio || ' ' || SUBSTR(REGEXP_SUBSTR( TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || GR_ORIGEM.DS_GRUPO_REGIAO || ' ' || A_ORIGEM.SG_AEROPORTO as ORIGEM,  ")
				.append("   SUBSTR(REGEXP_SUBSTR( p_destino.nm_pais_i, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  p_destino.nm_pais_i, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || UF_DESTINO.SG_UNIDADE_FEDERATIVA || ' ' || F_DESTINO.SG_FILIAL || ' ' || M_DESTINO.nm_municipio || ' ' || SUBSTR(REGEXP_SUBSTR( TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || GR_DESTINO.DS_GRUPO_REGIAO || ' ' || A_DESTINO.SG_AEROPORTO as DESTINO,    ")
				.append("   pcn.id_parametro_cliente as ID_PARAM_CLIENTE,  	 ")
				.append("   pcn.pc_reaj_pedagio as PEDAGIO_PERCENTUAL , 	 ")
				.append("   pcn.pc_reaj_percentual_gris as GRIS_PERCENTUAL ,  	 ")
				.append("   pcn.pc_reaj_minimo_gris as GRIS_MIN_PERCENTUAL , 	 ")
				.append("   pcn.pc_reaj_percentual_trt as TRT_PERCENTUAL ,  	 	 ")
				.append("   pcn.pc_reajuste_minimo_trt as TRT_MIN_PERCENTUAL ,    ")
				.append("   pcn.pc_reaj_percentual_tde as TDE_PERCENTUAL ,  	 	 ")
				.append("   pcn.pc_reaj_minimo_tde as TDE_MIN_PERCENTUAL ,    ")
				.append("   round(pcn.vl_pedagio - (pcn.vl_pedagio * pcn.pc_reaj_pedagio / (100 + pcn.pc_reaj_pedagio)),5 ) as PEDAGIO_ORIGINAL ,       ")
				.append("   round(pcn.vl_percentual_gris - (pcn.vl_percentual_gris * pcn.pc_reaj_percentual_gris / (100 + pcn.pc_reaj_percentual_gris)),2 ) as GRIS_ORIGINAL ,  ")
				.append("   round(pcn.vl_minimo_gris - (pcn.vl_minimo_gris * pcn.pc_reaj_minimo_gris / (100 + pcn.pc_reaj_minimo_gris)),2 ) as GRIS_MIN_ORIGINAL ,  ")
				.append("   round(pcn.vl_percentual_trt - (pcn.vl_percentual_trt * pcn.pc_reaj_percentual_trt / (100 + pcn.pc_reaj_percentual_trt)),2 ) as TRT_ORIGINAL   ,  ")
				.append("   round(pcn.vl_minimo_trt - (pcn.vl_minimo_trt * pcn.pc_reajuste_minimo_trt / (100 + pcn.pc_reajuste_minimo_trt)),2 ) as TRT_MIN_ORIGINAL   ,  ")
				.append("   round(pcn.vl_percentual_tde - (pcn.vl_percentual_tde * pcn.pc_reaj_percentual_tde / (100 + pcn.pc_reaj_percentual_tde)),2 ) as TDE_ORIGINAL   ,  ")
				.append("   round(pcn.vl_minimo_tde - (pcn.vl_minimo_tde * pcn.pc_reaj_minimo_tde / (100 + pcn.pc_reaj_minimo_tde)),2 ) as TDE_MIN_ORIGINAL ,     ")
				.append("   pcn.vl_pedagio  as PEDAGIO_CALCULADO , ")
				.append("   pcn.vl_percentual_gris  as GRIS_CALCULADO , ")
				.append("   pcn.vl_minimo_gris  as GRIS_MIN_CALCULADO , ")
				.append("   pcn.vl_percentual_trt  as TRT_CALCULADO ,  ")
				.append("   pcn.vl_minimo_trt  as TRT_MIN_CALCULADO ,  ")
				.append("   pcn.vl_percentual_tde  as TDE_CALCULADO ,  ")
				.append("   pcn.vl_minimo_tde  as TDE_MIN_CALCULADO  ")
				.append(" from parametro_cliente pcn, unidade_federativa UF_ORIGEM, municipio M_ORIGEM, filial F_ORIGEM, tipo_localizacao_municipio TLM_ORIGEM, grupo_regiao GR_ORIGEM,    ")
				.append("      aeroporto A_ORIGEM, unidade_federativa UF_DESTINO, municipio M_DESTINO, filial F_DESTINO, tipo_localizacao_municipio TLM_DESTINO, grupo_regiao GR_DESTINO, aeroporto A_DESTINO, pais p_origem, pais p_destino ")
				.append(" where   pcn.ID_TABELA_DIVISAO_CLIENTE =  ?   ")
				.append("   and   (pcn.tp_situacao_parametro = 'R' OR pcn.tp_situacao_parametro = 'A')  ")
				.append("   and   pcn.dt_vigencia_inicial = ?    ")
				.append("   and   pcn.dt_vigencia_final > = trunc(sysdate)    ")
				.append("   and   pcn.id_pais_origem = p_origem.ID_PAIS(+)    ")
				.append("   and   pcn.id_uf_origem = UF_ORIGEM.id_unidade_federativa(+)      ")
				.append("   and   pcn.id_municipio_origem = M_ORIGEM.id_municipio(+)     ")
				.append("   and   pcn.id_filial_origem = F_ORIGEM.id_filial(+)       ")
				.append("   and   pcn.id_tipo_loc_municipio_origem = TLM_ORIGEM.id_tipo_localizacao_municipio(+)     ")
				.append("   and   pcn.id_grupo_regiao_origem = GR_ORIGEM.id_grupo_regiao(+)     ")
				.append("   and   pcn.id_aeroporto_origem = A_ORIGEM.id_aeroporto(+)     ")
				.append("   and   pcn.id_pais_destino = p_destino.ID_PAIS(+)    ")
				.append("   and   pcn.id_uf_destino = UF_DESTINO.id_unidade_federativa(+)     ")
				.append("   and   pcn.id_municipio_destino = M_DESTINO.id_municipio(+)       ")
				.append("   and   pcn.id_filial_destino = F_DESTINO.id_filial(+)     ")
				.append("   and   pcn.id_tipo_loc_municipio_destino = TLM_DESTINO.id_tipo_localizacao_municipio(+)     ")
				.append("   and   pcn.id_grupo_regiao_destino = GR_DESTINO.id_grupo_regiao(+)       ")
				.append("   and   pcn.id_aeroporto_destino = A_DESTINO.id_aeroporto(+)    ")
				.toString();
		
		RowMapper rowMapper = new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<>();
				map.put("ORIGEM", resultSet.getString(1));
				map.put("DESTINO", resultSet.getString(2));
				map.put("ID_PARAM_CLIENTE", 	resultSet.getLong(3));
				map.put("PEDAGIO_PERCENTUAL", 	resultSet.getBigDecimal(4));
				map.put("GRIS_PERCENTUAL", 		resultSet.getBigDecimal(5));
				map.put("GRIS_MIN_PERCENTUAL",	resultSet.getBigDecimal(6));
				map.put("TRT_PERCENTUAL", 		resultSet.getBigDecimal(7));
				map.put("TRT_MIN_PERCENTUAL", 	resultSet.getBigDecimal(8));
				map.put("TDE_PERCENTUAL", 		resultSet.getBigDecimal(9));
				map.put("TDE_MIN_PERCENTUAL", 	resultSet.getBigDecimal(10));
				map.put("PEDAGIO_ORIGINAL", 	resultSet.getBigDecimal(11));
				map.put("GRIS_ORIGINAL", 		resultSet.getBigDecimal(12));
				map.put("GRIS_MIN_ORIGINAL", 	resultSet.getBigDecimal(13));
				map.put("TRT_ORIGINAL", 		resultSet.getBigDecimal(14));
				map.put("TRT_MIN_ORIGINAL",		resultSet.getBigDecimal(15));
				map.put("TDE_ORIGINAL", 		resultSet.getBigDecimal(16));
				map.put("TDE_MIN_ORIGINAL", 	resultSet.getBigDecimal(17));
				map.put("PEDAGIO_CALCULADO", 	resultSet.getBigDecimal(18));
				map.put("GRIS_CALCULADO", 		resultSet.getBigDecimal(19));
				map.put("GRIS_MIN_CALCULADO", 	resultSet.getBigDecimal(20));
				map.put("TRT_CALCULADO", 		resultSet.getBigDecimal(21));
				map.put("TRT_MIN_CALCULADO", 	resultSet.getBigDecimal(22));
				map.put("TDE_CALCULADO", 		resultSet.getBigDecimal(23));
				map.put("TDE_MIN_CALCULADO", 	resultSet.getBigDecimal(24));
				return map;
			}
		};
		
		Object[] params = new Object[]{ idTabDivisaoCliente , vigenciaInicial };
		return jdbcTemplate.query(query, JodaTimeUtils.jdbcPureParamConverter(jdbcTemplate, params) , rowMapper);
	}
	
	public List<Map<String,Object>> listTaxaCliente(Long idTabDivisaoCliente, YearMonthDay vigenciaInicial){
		String query = new StringBuilder()
				.append(" select     ") 
				.append(" 	SUBSTR(REGEXP_SUBSTR( p_origem.nm_pais_i, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  p_origem.nm_pais_i, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || UF_ORIGEM.SG_UNIDADE_FEDERATIVA || ' ' || F_ORIGEM.SG_FILIAL || ' ' || M_ORIGEM.nm_municipio || ' ' || SUBSTR(REGEXP_SUBSTR( TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  TLM_ORIGEM.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || GR_ORIGEM.DS_GRUPO_REGIAO || ' ' || A_ORIGEM.SG_AEROPORTO as  ORIGEM,  ")
				.append("   SUBSTR(REGEXP_SUBSTR( p_destino.nm_pais_i, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  p_destino.nm_pais_i, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || UF_DESTINO.SG_UNIDADE_FEDERATIVA || ' ' || F_DESTINO.SG_FILIAL || ' ' || M_DESTINO.nm_municipio || ' ' || SUBSTR(REGEXP_SUBSTR( TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  INSTR(REGEXP_SUBSTR(  TLM_DESTINO.DS_TIPO_LOCAL_MUNICIPIO_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) || ' ' || GR_DESTINO.DS_GRUPO_REGIAO || ' ' || A_DESTINO.SG_AEROPORTO as DESTINO,    ")
				.append("   pcn.id_parametro_cliente as ID_PARAM_CLIENTE,  	")
				.append("   tc.id_taxa_cliente as  ID_TAXA_CLIENTE,  	 	")
				.append("   tc.pc_reaj_taxa as TAXA_PERCENTUAL,  	 	")
				.append("   tc.pc_reaj_vl_excedente as EXCEDENTE_PERCENTUAL, 	")
				.append("   round(tc.vl_taxa - (tc.vl_taxa * tc.pc_reaj_taxa / (100 + tc.pc_reaj_taxa)),2 ) as  TAXA_ORIGINAL ,    ")
				.append("   round(tc.vl_excedente - (tc.vl_excedente * tc.pc_reaj_vl_excedente / (100 + tc.pc_reaj_vl_excedente)),2 ) as EXCEDENTE_ORIGINAL ,   ")
				.append("   SUBSTR(REGEXP_SUBSTR(pp.nm_parcela_preco_i, 'pt_BR»[^¦]+'), INSTR(REGEXP_SUBSTR( pp.nm_parcela_preco_i, 'pt_BR»[^¦]+'), 'pt_BR»')+LENGTH('pt_BR»')) as TAXA  , ")
				.append("   tc.vl_taxa as TAXA_CALCULADO ,   ")
				.append("   tc.vl_excedente as EXCEDENTE_CALCULADO  ")
				.append(" from parametro_cliente pcn, taxa_cliente tc, unidade_federativa UF_ORIGEM, municipio M_ORIGEM, filial F_ORIGEM, tipo_localizacao_municipio TLM_ORIGEM, grupo_regiao GR_ORIGEM,   ")
				.append("      aeroporto A_ORIGEM, unidade_federativa UF_DESTINO, municipio M_DESTINO, filial F_DESTINO, tipo_localizacao_municipio TLM_DESTINO, grupo_regiao GR_DESTINO, aeroporto A_DESTINO, parcela_preco pp, pais p_origem, pais p_destino ")
				.append(" where   pcn.ID_TABELA_DIVISAO_CLIENTE = ?     ")
				.append("   and   (pcn.tp_situacao_parametro = 'R' OR pcn.tp_situacao_parametro = 'A')   ")
				.append("   and   pcn.dt_vigencia_inicial = ? 	")
				.append("   and   pcn.dt_vigencia_final >= trunc(sysdate) ")
				.append("   and   pcn.id_parametro_cliente = tc.id_parametro_cliente      ")
				.append("   and   tc.id_parcela_preco = pp.id_parcela_preco      ")
				.append("   and   pcn.id_uf_origem = UF_ORIGEM.id_unidade_federativa(+)      ")
				.append("   and   pcn.id_municipio_origem = M_ORIGEM.id_municipio(+)      ")
				.append("   and   pcn.id_filial_origem = F_ORIGEM.id_filial(+)      ")
				.append("   and   pcn.id_tipo_loc_municipio_origem = TLM_ORIGEM.id_tipo_localizacao_municipio(+)      ")
				.append("   and   pcn.id_grupo_regiao_origem = GR_ORIGEM.id_grupo_regiao(+)      ")
				.append("   and   pcn.id_aeroporto_origem = A_ORIGEM.id_aeroporto(+)      ")
				.append("   and   pcn.id_uf_destino = UF_DESTINO.id_unidade_federativa(+)      ")
				.append("   and   pcn.id_municipio_destino = M_DESTINO.id_municipio(+)        ")
				.append("   and   pcn.id_filial_destino = F_DESTINO.id_filial(+)      ")
				.append("   and   pcn.id_tipo_loc_municipio_destino = TLM_DESTINO.id_tipo_localizacao_municipio(+)      ")
				.append("   and   pcn.id_grupo_regiao_destino = GR_DESTINO.id_grupo_regiao(+)      ")
				.append("   and   pcn.id_aeroporto_destino = A_DESTINO.id_aeroporto(+)      ")
				.append("   and   UF_DESTINO.id_pais = p_destino.id_pais(+)   ")
				.append("   and   UF_ORIGEM.id_pais = p_origem.id_pais(+)   ")
				.append(" order by TAXA ")
				.toString();
		
		RowMapper rowMapper = new RowMapper(){
			@Override
			public Object mapRow(ResultSet resultSet, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<>();
				map.put("ORIGEM", resultSet.getString(1));
				map.put("DESTINO", resultSet.getString(2));
				map.put("ID_PARAM_CLIENTE", 	resultSet.getLong(3));
				map.put("ID_TAXA_CLIENTE", 		resultSet.getLong(4));
				map.put("TAXA_PERCENTUAL", 		resultSet.getBigDecimal(5));
				map.put("EXCEDENTE_PERCENTUAL",	resultSet.getBigDecimal(6));
				map.put("TAXA_ORIGINAL", 		resultSet.getBigDecimal(7));
				map.put("EXCEDENTE_ORIGINAL", 	resultSet.getBigDecimal(8));
				map.put("TAXA", 				resultSet.getString(9));
				map.put("TAXA_CALCULADO", 		resultSet.getBigDecimal(10));
				map.put("EXCEDENTE_CALCULADO", 	resultSet.getBigDecimal(11));
				return map;
			}
		};
		
		Object[] params = new Object[]{ idTabDivisaoCliente , vigenciaInicial };
		return jdbcTemplate.query(query, JodaTimeUtils.jdbcPureParamConverter(jdbcTemplate, params) , rowMapper);
	}
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
