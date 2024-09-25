package com.mercurio.lms.vendas.model.dao;

import static org.hibernate.sql.JoinFragment.LEFT_OUTER_JOIN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.PipelineCliente;
import com.mercurio.lms.vendas.model.Proposta;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.util.SimulacaoUtils;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class SimulacaoDAO extends BaseCrudDao<Simulacao, Long> {

	private static final String SITUACAO_APROVACAO_EM_EFETIVACAO = "M";
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Simulacao.class;
	}

	@Override
	public void removeById(Long id) {
		Simulacao simulacao = (Simulacao)findById(id);
		List<ParametroCliente> parametrosCliente = simulacao.getParametroClientes();
		if (parametrosCliente != null && !parametrosCliente.isEmpty()) {
			for (ParametroCliente parametroCliente : parametrosCliente) {
				parametroCliente.setSimulacao(null);
			}
			parametrosCliente.clear();
		}

		List<ServicoAdicionalCliente> servicosAdicionais = simulacao.getServicoAdicionalClientes();
		if(servicosAdicionais != null && !servicosAdicionais.isEmpty()) {
			for (ServicoAdicionalCliente servicoAdicionalCliente : servicosAdicionais) {
				servicoAdicionalCliente.setSimulacao(null);
			}
			servicosAdicionais.clear();
		}
		getAdsmHibernateTemplate().delete(simulacao);
	}

	public ResultSetPage findPaginatedProc(TypedFlatMap criteria, FindDefinition def) {
	    
	    Projection subqueryProjection = Projections.sqlProjection("(select max(e.dh_Solicitacao) from historico_efetivacao e where e.id_simulacao = {alias}.id_simulacao) as historico_dhSolicitacao ", 
	            new String[]{"historico_dhSolicitacao"}, new Type[]{Hibernate.TIMESTAMP});
	    
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("s.idSimulacao"), "idSimulacao")
			.add(Projections.property("s.dtSimulacao"), "dtSimulacao")
			.add(Projections.property("s.nrSimulacao"), "nrSimulacao")
			.add(Projections.property("s.blEfetivada"), "blEfetivada")
			.add(Projections.property("p.nmPessoa"), "clienteByIdCliente_pessoa_nmPessoa")
			.add(Projections.property("ttp.tpTipoTabelaPreco"), "tipoTabelaPreco_tpTipoTabelaPreco")
			.add(Projections.property("ttp.nrVersao"), "tipoTabelaPreco_nrVersao")
			.add(Projections.property("stp.tpSubtipoTabelaPreco"), "subtipoTabelaPreco_tpSubtipoTabelaPreco")
			.add(Projections.property("ttpf.tpTipoTabelaPreco"), "tabelaPrecoFob_tipoTabelaPreco_tpTipoTabelaPreco")
			.add(Projections.property("ttpf.nrVersao"), "tabelaPrecoFob_tipoTabelaPreco_nrVersao")
			.add(Projections.property("stpf.tpSubtipoTabelaPreco"), "tabelaPrecoFob_subtipoTabelaPreco_tpSubtipoTabelaPreco")
			.add(Projections.property("tpSituacaoAprovacao"), "tpSituacaoAprovacao")
			.add(Projections.property("tpGeracaoProposta"), "tpGeracaoProposta")
			.add(subqueryProjection,"historico_dhSolicitacao")
			.add(Projections.property("f.sgFilial"), "filial_sgFilial");

		DetachedCriteria dc = createCriteriaPaginatedProc(criteria);
		dc.setProjection(pl);
		
		if (SITUACAO_APROVACAO_EM_EFETIVACAO.equals(criteria.getString("tpSituacaoAprovacao"))){
		    dc.addOrder(Order.asc("historico_dhSolicitacao"));
		}else{
		    dc.addOrder(Order.asc("dtSimulacao"));
		}
		dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		

		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, def.getCurrentPage(), def.getPageSize());
	}
	
	public Integer getRowCountProc(TypedFlatMap criteria) {
		DetachedCriteria dc = createCriteriaPaginatedProc(criteria);
		dc.setProjection(Projections.rowCount());
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition def) {
		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("idSimulacao"), "idSimulacao")
		.add(Projections.property("dtSimulacao"), "dtSimulacao")
		.add(Projections.property("nrSimulacao"), "nrSimulacao")
		.add(Projections.property("p.nmPessoa"), "clienteByIdCliente_pessoa_nmPessoa")
		.add(Projections.property("ttp.tpTipoTabelaPreco"), "tipoTabelaPreco_tpTipoTabelaPreco")
		.add(Projections.property("ttp.nrVersao"), "tipoTabelaPreco_nrVersao")
		.add(Projections.property("stp.tpSubtipoTabelaPreco"), "subtipoTabelaPreco_tpSubtipoTabelaPreco")
		.add(Projections.property("tpSituacaoAprovacao"), "tpSituacaoAprovacao");

		DetachedCriteria dc = createCriteriaPaginated(criteria);
		dc.setProjection(pl);
		dc.addOrder(Order.asc("nrSimulacao"));
		dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());

		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, def.getCurrentPage(), def.getPageSize());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		DetachedCriteria dc = createCriteriaPaginated(criteria);
		dc.setProjection(Projections.rowCount());
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public TypedFlatMap findDadosById(Long idSimulacao) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select new map (s.idSimulacao as idSimulacao, \n");
		hql.append("        s.nrSimulacao as nrSimulacao, \n");
		hql.append("        s.dtTabelaVigenciaInicial as simulacao_dtTabelaVigenciaInicial, \n");
		hql.append("        s.blEfetivada as blEfetivada, \n");
		hql.append("        s.blPagaFreteTonelada as blPagaFreteTonelada, \n");
		hql.append("        s.obProposta as obProposta, \n");
		hql.append("        f.idFilial as filial_idFilial, \n");
		hql.append("        f.sgFilial as filial_sgFilial, \n");
		hql.append("        c.idCliente as cliente_idCliente, \n");
		hql.append("        p.nrIdentificacao as cliente_pessoa_nrIdentificacao, \n");
		hql.append("        p.tpIdentificacao as cliente_pessoa_tpIdentificacao, \n");
		hql.append("        p.nmPessoa as cliente_pessoa_nmPessoa, \n");
		hql.append("        d.idDivisaoCliente as divisaoCliente_idDivisaoCliente, \n");
		hql.append("        ttp.tpTipoTabelaPreco as tabelaPreco_tipoTabelaPreco_tpTipoTabelaPreco, \n");
		hql.append("        ttp.nrVersao as tabelaPreco_tipoTabelaPreco_nrVersao, \n");
		hql.append("        stp.tpSubtipoTabelaPreco as tabelaPreco_subtipoTabelaPreco_tpSubtipoTabelaPreco, \n");
		hql.append("        t.dsDescricao as tabelaPreco_dsDescricao, \n");
		hql.append("        t.idTabelaPreco as tabelaPreco_idTabelaPreco, \n");
		hql.append("        m.dsSimbolo as tabelaPreco_moeda_dsSimbolo, \n");
		hql.append("        m.sgMoeda as tabelaPreco_moeda_sgMoeda, \n");
		hql.append("        ss.idServico as servico_idServico, \n");
		hql.append("        pc.idParametroCliente as parametroCliente_idParametroCliente, \n");
		hql.append("        zo.idZona as zonaByIdZonaOrigem_idZona, \n");
		hql.append("        zd.idZona as zonaByIdZonaDestino_idZona, \n");
		hql.append("        po.idPais as paisByIdPaisOrigem_idPais, \n");
		hql.append("        pd.idPais as paisByIdPaisDestino_idPais, \n");
		hql.append("        po.nmPais as paisByIdPaisOrigem_nmPais, \n");
		hql.append("        pd.nmPais as paisByIdPaisDestino_nmPais, \n");
		hql.append("        ufo.idUnidadeFederativa as unidadeFederativaByIdUfOrigem_idUnidadeFederativa, \n");
		hql.append("        ufd.idUnidadeFederativa as unidadeFederativaByIdUfDestino_idUnidadeFederativa, \n");
		hql.append("        fo.idFilial as filialByIdFilialOrigem_idFilial, \n");
		hql.append("        fd.idFilial as filialByIdFilialDestino_idFilial, \n");
		hql.append("        fo.sgFilial as filialByIdFilialOrigem_sgFilial, \n");
		hql.append("        fd.sgFilial as filialByIdFilialDestino_sgFilial, \n");
		hql.append("        fop.nmFantasia as filialByIdFilialOrigem_pessoa_nmFantasia, \n");
		hql.append("        fdp.nmFantasia as filialByIdFilialDestino_pessoa_nmFantasia, \n");
		hql.append("        mo.idMunicipio as municipioByIdMunicipioOrigem_idMunicipio, \n");
		hql.append("        md.idMunicipio as municipioByIdMunicipioDestino_idMunicipio, \n");
		hql.append("        mo.nmMunicipio as municipioByIdMunicipioOrigem_nmMunicipio, \n");
		hql.append("        md.nmMunicipio as municipioByIdMunicipioDestino_nmMunicipio, \n");
		hql.append("        ao.idAeroporto as aeroportoByIdAeroportoOrigem_idAeroporto, \n");
		hql.append("        ad.idAeroporto as aeroportoByIdAeroportoDestino_idAeroporto, \n");
		hql.append("        ao.sgAeroporto as aeroportoByIdAeroportoOrigem_sgAeroporto, \n");
		hql.append("        ad.sgAeroporto as aeroportoByIdAeroportoDestino_sgAeroporto, \n");
		hql.append("        aop.nmPessoa as aeroportoByIdAeroportoOrigem_pessoa_nmPessoa, \n");
		hql.append("        adp.nmPessoa as aeroportoByIdAeroportoDestino_pessoa_nmPessoa, \n");
		hql.append("        to.idTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem_idTipoLocalizacaoMunicipio, \n");
		hql.append("        td.idTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino_idTipoLocalizacaoMunicipio) \n");
		hql.append(" from " + getPersistentClass().getName() + " s \n");
		hql.append(" join s.clienteByIdCliente c \n");
		hql.append(" join s.filial f \n");
		hql.append(" join c.pessoa p \n");
		hql.append(" left join s.divisaoCliente d \n");
		hql.append(" join s.tabelaPreco t \n");
		hql.append(" join t.moeda m \n");
		hql.append(" join t.tipoTabelaPreco ttp \n");
		hql.append(" join t.subtipoTabelaPreco stp \n");
		hql.append(" join s.servico ss \n");
		hql.append(" left outer join s.parametroClientes pc \n");
		hql.append(" left outer join pc.zonaByIdZonaOrigem zo \n");
		hql.append(" left outer join pc.zonaByIdZonaDestino zd \n");
		hql.append(" left outer join pc.paisByIdPaisOrigem po \n");
		hql.append(" left outer join pc.paisByIdPaisDestino pd \n");
		hql.append(" left outer join pc.unidadeFederativaByIdUfOrigem ufo \n");
		hql.append(" left outer join pc.unidadeFederativaByIdUfDestino ufd \n");
		hql.append(" left outer join pc.filialByIdFilialOrigem fo \n");
		hql.append(" left outer join pc.filialByIdFilialDestino fd \n");
		hql.append(" left outer join fo.pessoa fop \n");
		hql.append(" left outer join fd.pessoa fdp \n");
		hql.append(" left outer join pc.municipioByIdMunicipioOrigem mo \n");
		hql.append(" left outer join pc.municipioByIdMunicipioDestino md \n");
		hql.append(" left outer join pc.aeroportoByIdAeroportoOrigem ao \n");
		hql.append(" left outer join pc.aeroportoByIdAeroportoDestino ad \n");
		hql.append(" left outer join ao.pessoa aop \n");
		hql.append(" left outer join ad.pessoa adp \n");
		hql.append(" left outer join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem to \n");
		hql.append(" left outer join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino td \n");
		hql.append(" where (pc.tpSituacaoParametro = 'P' or pc is null) \n");
		hql.append(" and s.id = ?");

		Map result = (Map) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idSimulacao});
		return AliasToTypedFlatMapResultTransformer.getInstance().transformeTupleMap(result);
	}

	public TypedFlatMap findDadosByIdParametroCliente(Long idParametroCliente) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select new map (s.idSimulacao as idSimulacao, \n");
		hql.append("        s.nrSimulacao as nrSimulacao, \n");
		hql.append("        s.dtTabelaVigenciaInicial as simulacao_dtTabelaVigenciaInicial, \n");
		hql.append("        s.blEfetivada as blEfetivada, \n");
		hql.append("        f.idFilial as filial_idFilial, \n");
		hql.append("        c.idCliente as cliente_idCliente, \n");
		hql.append("        p.tpIdentificacao as cliente_pessoa_tpIdentificacao, \n");
		hql.append("        p.nrIdentificacao as cliente_pessoa_nrIdentificacao, \n");
		hql.append("        p.nmPessoa as cliente_pessoa_nmPessoa, \n");
		hql.append("        d.idDivisaoCliente as divisaoCliente_idDivisaoCliente, \n");
		hql.append("        ttp.tpTipoTabelaPreco as tabelaPreco_tipoTabelaPreco_tpTipoTabelaPreco, \n");
		hql.append("        ttp.nrVersao as tabelaPreco_tipoTabelaPreco_nrVersao, \n");
		hql.append("        stp.tpSubtipoTabelaPreco as tabelaPreco_subtipoTabelaPreco_tpSubtipoTabelaPreco, \n");
		hql.append("        stp.idSubtipoTabelaPreco as tabelaPreco_subtipoTabelaPreco_idSubtipoTabelaPreco, \n");
		hql.append("        t.dsDescricao as tabelaPreco_dsDescricao, \n");
		hql.append("        t.idTabelaPreco as tabelaPreco_idTabelaPreco, \n");
		hql.append("        m.dsSimbolo as tabelaPreco_moeda_dsSimbolo, \n");
		hql.append("        m.sgMoeda as tabelaPreco_moeda_sgMoeda, \n");
		hql.append("        ss.idServico as servico_idServico, \n");
		hql.append("        pc.idParametroCliente as parametroCliente_idParametroCliente, \n");
		hql.append("        pc.idParametroCliente as idParametroCliente, \n");
		hql.append("        zo.idZona as zonaByIdZonaOrigem_idZona, \n");
		hql.append("        zd.idZona as zonaByIdZonaDestino_idZona, \n");
		hql.append("        po.idPais as paisByIdPaisOrigem_idPais, \n");
		hql.append("        pd.idPais as paisByIdPaisDestino_idPais, \n");
		hql.append("        po.nmPais as paisByIdPaisOrigem_nmPais, \n");
		hql.append("        pd.nmPais as paisByIdPaisDestino_nmPais, \n");
		hql.append("        ufo.idUnidadeFederativa as unidadeFederativaByIdUfOrigem_idUnidadeFederativa, \n");
		hql.append("        ufd.idUnidadeFederativa as unidadeFederativaByIdUfDestino_idUnidadeFederativa, \n");
		hql.append("        go.idGrupoRegiao as grupoRegiaoOrigem_idGrupoRegiao, \n");
		hql.append("        gd.idGrupoRegiao as grupoRegiaoDestino_idGrupoRegiao, \n");
		hql.append("        fo.idFilial as filialByIdFilialOrigem_idFilial, \n");
		hql.append("        fd.idFilial as filialByIdFilialDestino_idFilial, \n");
		hql.append("        fo.sgFilial as filialByIdFilialOrigem_sgFilial, \n");
		hql.append("        fd.sgFilial as filialByIdFilialDestino_sgFilial, \n");
		hql.append("        fop.nmFantasia as filialByIdFilialOrigem_pessoa_nmFantasia, \n");
		hql.append("        fdp.nmFantasia as filialByIdFilialDestino_pessoa_nmFantasia, \n");
		hql.append("        mo.idMunicipio as municipioByIdMunicipioOrigem_idMunicipio, \n");
		hql.append("        md.idMunicipio as municipioByIdMunicipioDestino_idMunicipio, \n");
		hql.append("        mo.nmMunicipio as municipioByIdMunicipioOrigem_nmMunicipio, \n");
		hql.append("        md.nmMunicipio as municipioByIdMunicipioDestino_nmMunicipio, \n");
		hql.append("        ao.idAeroporto as aeroportoByIdAeroportoOrigem_idAeroporto, \n");
		hql.append("        ad.idAeroporto as aeroportoByIdAeroportoDestino_idAeroporto, \n");
		hql.append("        ao.sgAeroporto as aeroportoByIdAeroportoOrigem_sgAeroporto, \n");
		hql.append("        ad.sgAeroporto as aeroportoByIdAeroportoDestino_sgAeroporto, \n");
		hql.append("        aop.nmPessoa as aeroportoByIdAeroportoOrigem_pessoa_nmPessoa, \n");
		hql.append("        adp.nmPessoa as aeroportoByIdAeroportoDestino_pessoa_nmPessoa, \n");
		hql.append("        to.idTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem_idTipoLocalizacaoMunicipio, \n");
		hql.append("        td.idTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino_idTipoLocalizacaoMunicipio) \n");
		/** Valida se tpGeracaoProposta == Capital */
		DomainValue tpGeracaoProposta = SimulacaoUtils.getSimulacaoInSession().getTpGeracaoProposta();
		if(tpGeracaoProposta != null && ("C".equals(tpGeracaoProposta.getValue()) || "P".equals(tpGeracaoProposta.getValue()))) {
				hql.append(" FROM ").append(Proposta.class.getName()).append(" p \n")
					.append(" 	JOIN p.simulacao s \n")
					.append(" 	JOIN p.parametroCliente pc \n");
		} else {
			hql.append(" FROM ").append(getPersistentClass().getName()).append(" s \n")
				.append(" 	JOIN s.parametroClientes pc \n");
		}
		hql.append(" join s.clienteByIdCliente c \n");
		hql.append(" join s.filial f \n");
		hql.append(" join c.pessoa p \n");
		hql.append(" left join s.divisaoCliente d \n");
		hql.append(" join s.tabelaPreco t \n");
		hql.append(" join t.moeda m \n");
		hql.append(" join t.tipoTabelaPreco ttp \n");
		hql.append(" join t.subtipoTabelaPreco stp \n");
		hql.append(" join s.servico ss \n");
		hql.append(" left outer join pc.zonaByIdZonaOrigem zo \n");
		hql.append(" left outer join pc.zonaByIdZonaDestino zd \n");
		hql.append(" left outer join pc.paisByIdPaisOrigem po \n");
		hql.append(" left outer join pc.paisByIdPaisDestino pd \n");
		hql.append(" left outer join pc.unidadeFederativaByIdUfOrigem ufo \n");
		hql.append(" left outer join pc.unidadeFederativaByIdUfDestino ufd \n");
		hql.append(" left outer join pc.filialByIdFilialOrigem fo \n");
		hql.append(" left outer join pc.filialByIdFilialDestino fd \n");
		hql.append(" left outer join fo.pessoa fop \n");
		hql.append(" left outer join fd.pessoa fdp \n");
		hql.append(" left outer join pc.municipioByIdMunicipioOrigem mo \n");
		hql.append(" left outer join pc.municipioByIdMunicipioDestino md \n");
		hql.append(" left outer join pc.aeroportoByIdAeroportoOrigem ao \n");
		hql.append(" left outer join pc.aeroportoByIdAeroportoDestino ad \n");
		hql.append(" left outer join ao.pessoa aop \n");
		hql.append(" left outer join ad.pessoa adp \n");
		hql.append(" left outer join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem to \n");
		hql.append(" left outer join pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino td \n");
		hql.append(" left outer join pc.grupoRegiaoOrigem go \n");
		hql.append(" left outer join pc.grupoRegiaoDestino gd \n");
		hql.append(" where pc.id = ?");

		Map result = (Map) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idParametroCliente});
		return AliasToTypedFlatMapResultTransformer.getInstance().transformeTupleMap(result);
	}

	public ResultSetPage findPaginatedParametros(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder()
			.append(" SELECT new map( \n")
			.append("        pc.idParametroCliente as idParametroCliente, \n")
			.append("        pc.idParametroCliente as parametroCliente_idParametroCliente, \n")
			.append("        zo.dsZona as zonaByIdZonaOrigem_dsZona, \n")
			.append("        po.nmPais as paisByIdPaisOrigem_nmPais, \n")
			.append("        ufo.sgUnidadeFederativa as unidadeFederativaByIdUfOrigem_sgUnidadeFederativa, \n")
			.append("        fo.sgFilial as filialByIdFilialOrigem_sgFilial, \n")
			.append("        mo.nmMunicipio as municipioByIdMunicipioOrigem_nmMunicipio, \n")
			.append("        ao.sgAeroporto as aeroportoByIdAeroportoOrigem_sgAeroporto, \n")
			.append("        to.dsTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem_dsTipoLocalizacaoMunicipio, \n")
			.append("        go.dsGrupoRegiao as grupoRegiaoOrigem_dsGrupoRegiao, \n")
			.append("        zd.dsZona as zonaByIdZonaDestino_dsZona, \n")
			.append("        pd.nmPais as paisByIdPaisDestino_nmPais, \n")
			.append("        ufd.sgUnidadeFederativa as unidadeFederativaByIdUfDestino_sgUnidadeFederativa, \n")
			.append("        fd.sgFilial as filialByIdFilialDestino_sgFilial, \n")
			.append("        md.nmMunicipio as municipioByIdMunicipioDestino_nmMunicipio, \n")
			.append("        ad.sgAeroporto as aeroportoByIdAeroportoDestino_sgAeroporto, \n")
			.append("        td.dsTipoLocalizacaoMunicipio as tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino_dsTipoLocalizacaoMunicipio, \n")
			.append("        gd.dsGrupoRegiao as grupoRegiaoDestino_dsGrupoRegiao \n")
			.append(" ) \n");

		hql.append(createQueryParametros(criteria));

		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		hql.append(" order by zo.dsZona ");
		hql.append("        ,po.nmPais");
		hql.append("        ,ufo.sgUnidadeFederativa");
		hql.append("        ,fo.sgFilial");
		hql.append("        ,mo.nmMunicipio");
		hql.append("        ,ao.sgAeroporto");
		hql.append("        ,to.dsTipoLocalizacaoMunicipio");
		hql.append("        ,go.dsGrupoRegiao");
		hql.append("        ,zd.dsZona");
		hql.append("        ,pd.nmPais");
		hql.append("        ,ufd.sgUnidadeFederativa");
		hql.append("        ,fd.sgFilial");
		hql.append("        ,md.nmMunicipio");
		hql.append("        ,ad.sgAeroporto");
		hql.append("        ,td.dsTipoLocalizacaoMunicipio");
		hql.append("        ,gd.dsGrupoRegiao ");
		hql.append("        asc ");
		
		
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(hql.toString(), findDef.getCurrentPage(), findDef.getPageSize(), Collections.EMPTY_MAP);
		return AliasToTypedFlatMapResultTransformer.getInstance().transformResultSetPage(rsp);
	}

	public Integer getRowCountParametros(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder();
		hql.append(createQueryParametros(criteria));
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString());
	}
	
	private StringBuilder createQueryParametros(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder();
		/*LMS-891: Correção para propostas percentuais que não estavam exibindo na tela de listagem os parametros gerados.*/
		String tpGeracaoProposta = criteria.getString("tpGeracaoProposta");
		if("C".equals(tpGeracaoProposta) || "P".equals(tpGeracaoProposta) || "O".equals(tpGeracaoProposta)
				|| "M".equals(tpGeracaoProposta) || "I".equals(tpGeracaoProposta) || "V".equals(tpGeracaoProposta)) {
			hql.append(" FROM ").append(Proposta.class.getName()).append(" p \n")
				.append(" 	JOIN p.simulacao s \n")
				.append(" 	JOIN p.parametroCliente pc \n");
		} else {
			hql.append(" FROM ").append(getPersistentClass().getName()).append(" s \n")
				.append(" 	JOIN s.parametroClientes pc \n");
		}
		hql.append(" 	JOIN s.clienteByIdCliente c \n")
			.append(" 	JOIN s.tabelaPreco t \n")
			.append(" 	LEFT OUTER JOIN pc.zonaByIdZonaOrigem zo \n")
			.append(" 	LEFT OUTER JOIN pc.paisByIdPaisOrigem po \n")
			.append(" 	LEFT OUTER JOIN pc.unidadeFederativaByIdUfOrigem ufo \n")
			.append(" 	LEFT OUTER JOIN pc.filialByIdFilialOrigem fo \n")
			.append(" 	LEFT OUTER JOIN pc.municipioByIdMunicipioOrigem mo \n")
			.append(" 	LEFT OUTER JOIN pc.aeroportoByIdAeroportoOrigem ao \n")
			.append(" 	LEFT OUTER JOIN pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem to \n")			
			.append(" 	LEFT OUTER JOIN pc.zonaByIdZonaDestino zd \n")
			.append(" 	LEFT OUTER JOIN pc.paisByIdPaisDestino pd \n")
			.append(" 	LEFT OUTER JOIN pc.unidadeFederativaByIdUfDestino ufd \n")
			.append(" 	LEFT OUTER JOIN pc.filialByIdFilialDestino fd \n")
			.append(" 	LEFT OUTER JOIN pc.municipioByIdMunicipioDestino md \n")
			.append(" 	LEFT OUTER JOIN pc.aeroportoByIdAeroportoDestino ad \n")
			.append(" 	LEFT OUTER JOIN pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino td \n")
			.append("   LEFT OUTER JOIN pc.grupoRegiaoOrigem go  \n")
			.append("   LEFT OUTER JOIN pc.grupoRegiaoDestino gd  \n")
			.append(" WHERE pc.tpSituacaoParametro = 'P' \n")
			.append(" 	AND s.idSimulacao = ").append(criteria.getLong("simulacao.idSimulacao"));
		return hql;
	}

	private DetachedCriteria createCriteriaPaginated(TypedFlatMap criteria) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "s");
		dc.createAlias("s.tabelaPreco", "tp");
		dc.createAlias("tp.tipoTabelaPreco", "ttp");
		dc.createAlias("tp.subtipoTabelaPreco", "stp");
		dc.createAlias("s.parametroClientes", "pcs");
		dc.createAlias("s.clienteByIdCliente", "c");
		dc.createAlias("c.pessoa", "p");

		dc.add(Restrictions.eq("pcs.tpSituacaoParametro", "P"));
		
		Long idTabelaPreco = criteria.getLong("tabelaPreco.idTabelaPreco");
		if (idTabelaPreco != null) {
			dc.add(Restrictions.eq("tp.idTabelaPreco", idTabelaPreco));
		}
		
		Long idCliente = criteria.getLong("cliente.idCliente");
		if (idCliente != null) {
			dc.add(Restrictions.eq("c.idCliente", idCliente));
		}
		
		Long idDivisaoCliente = criteria.getLong("divisaoCliente.idDivisaoCliente");
		if (idDivisaoCliente != null) {
			dc.add(Restrictions.eq("s.divisaoCliente.id", idDivisaoCliente));
		}
		
		Long idServico = criteria.getLong("servico.idServico");
		if (idServico != null) {
			dc.add(Restrictions.eq("s.servico.id", idServico));
		}

		return dc;
	}
	
	private DetachedCriteria createCriteriaPaginatedProc(TypedFlatMap criteria) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "s")
			.createAlias("s.clienteByIdCliente", "c")
			.createAlias("c.pessoa", "p")
			.createAlias("s.tabelaPreco", "tp", LEFT_OUTER_JOIN)
			.createAlias("tp.tipoTabelaPreco", "ttp", LEFT_OUTER_JOIN)
			.createAlias("tp.subtipoTabelaPreco", "stp", LEFT_OUTER_JOIN)
			.createAlias("s.tabelaPrecoFob", "tpf", LEFT_OUTER_JOIN)
			.createAlias("tpf.tipoTabelaPreco", "ttpf", LEFT_OUTER_JOIN)
			.createAlias("tpf.subtipoTabelaPreco", "stpf", LEFT_OUTER_JOIN)
			.createAlias("s.filial", "f")
			.add(Restrictions.eq("s.tpRegistro", "P"));
		
		Long nrSimulacao = criteria.getLong("nrSimulacao");
		if (nrSimulacao != null) {
			dc.add(Restrictions.eq("s.nrSimulacao", nrSimulacao));
		}
		
		Long idTabelaPreco = criteria.getLong("tabelaPreco.idTabelaPreco");
		if (idTabelaPreco != null) {
			dc.add(Restrictions.eq("tp.idTabelaPreco", idTabelaPreco));
		}
		
		Long idTabelaPrecoFob = criteria.getLong("tabelaPrecoFob.idTabelaPreco");
		if (idTabelaPrecoFob != null) {
			dc.add(Restrictions.eq("tpf.idTabelaPreco", idTabelaPrecoFob));
		}
		
		Long idCliente = criteria.getLong("cliente.idCliente");
		if (idCliente != null) {
			dc.add(Restrictions.eq("c.idCliente", idCliente));
		}
		
		Long idDivisaoCliente = criteria.getLong("divisaoCliente.idDivisaoCliente");
		if (idDivisaoCliente != null) {
			dc.add(Restrictions.eq("s.divisaoCliente.id", idDivisaoCliente));
		}
		
		Long idServico = criteria.getLong("servico.idServico");
		if (idServico != null) {
			dc.add(Restrictions.eq("s.servico.id", idServico));
		}
		
		Long idFilial = criteria.getLong("filial.idFilial");
		if (idFilial != null) {
			dc.add(Restrictions.eq("f.idFilial", idFilial));
		}
		
		
		String tpSituacao = criteria.getString("tpSituacaoAprovacao");
		if (!StringUtils.isEmpty(tpSituacao)) {
			dc.add(Restrictions.eq("s.tpSituacaoAprovacao", tpSituacao));
		}
		
		String tpGeracaoProposta = criteria.getString("tpGeracaoProposta");
		if (!StringUtils.isEmpty(tpGeracaoProposta)) {
			dc.add(Restrictions.eq("s.tpGeracaoProposta", tpGeracaoProposta));
		}
		
		Boolean blNovaUI = criteria.getBoolean("blNovaUI");
		if (blNovaUI!=null) {
			dc.add(Restrictions.eq("s.blNovaUI", blNovaUI));
		}
		
		return dc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindByIdLazyProperties(java.util.Map)
	 */
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("tabelaPreco", FetchMode.SELECT);
		lazyFindById.put("tabelaPreco.subtipoTabelaPreco", FetchMode.SELECT);
		lazyFindById.put("tabelaPreco.moeda", FetchMode.SELECT);
		lazyFindById.put("tabelaPrecoFob", FetchMode.SELECT);
		lazyFindById.put("usuarioByIdUsuarioAprovou", FetchMode.SELECT);
		lazyFindById.put("usuarioByIdUsuarioEfetivou", FetchMode.SELECT);
		lazyFindById.put("usuarioByIdUsuario", FetchMode.SELECT);
		lazyFindById.put("clienteByIdCliente", FetchMode.SELECT);
		lazyFindById.put("servico", FetchMode.SELECT);
		lazyFindById.put("parametroClientes", FetchMode.SELECT);
		lazyFindById.put("pendenciaAprovacao", FetchMode.SELECT);
		lazyFindById.put("pendenciaAprovacao.ocorrencia", FetchMode.SELECT);
		lazyFindById.put("filial", FetchMode.SELECT);
		lazyFindById.put("produtoEspecifico", FetchMode.SELECT);
	}
	
	public List findSimulacoesInPipelineClienteSimulacaoByPipelineCliente(PipelineCliente pipelineCliente) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		hql.append("select simu ");
		hql.append("from PipelineClienteSimulacao pics ");
		hql.append("join pics.pipelineCliente as picl ");
		hql.append("join pics.simulacao simu ");
		hql.append("where picl = ? ");
		hql.append("   and simu.blEfetivada = 'S' ");
		hql.append("order by simu.dtSimulacao ");
		params.add(pipelineCliente);

		return getHibernateTemplate().find(hql.toString(), params.toArray());
	}

	public Simulacao findSimulacaoEfetivadaByIdPipeLineCliente(PipelineCliente pipelineCliente) {
		List result = findSimulacoesInPipelineClienteSimulacaoByPipelineCliente(pipelineCliente);
		if (CollectionUtils.isNotEmpty(result)) {
			return (Simulacao) result.get(0);
		}
		return null;
	}

	public Simulacao findByNrPropostaAndClienteAndIdFilial(Long nrProposta, Long idCliente, Long idFilial) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		hql.append("select simu ");
		hql.append("from Simulacao simu ");
		hql.append("where simu.nrSimulacao = ? ");
		hql.append("and simu.filial.idFilial = ? ");
		hql.append("and simu.clienteByIdCliente.idCliente = ? ");
		
		params.add(nrProposta);
		params.add(idFilial);
		params.add(idCliente);
		
		return (Simulacao) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params.toArray());
	}
	
	/**
	 * Executa query referente a ET 01.07.02.23 para emissão do relatório em formato CSV
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> findRelatorioEfetivacaoProposta(Map<String, Object> parameters) {		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ")
		.append("    R.SG_REGIONAL as \"Regional\", ")
		.append("    P.NR_IDENTIFICACAO as \"CNPJ/CPF\", ")
		.append("    P.NM_PESSOA as \"Razão Social\", ")
		.append("    P.NM_FANTASIA as \"Nome Fantasia\", ")
		.append("    F.SG_FILIAL || ' ' || S.NR_SIMULACAO as \"Proposta\", ")
		.append("    to_char(H.DH_SOLICITACAO, 'YYYY-MM-DD HH24:MI:SS') as \"Data/hora de Recebimento\", ")
		.append("    to_char(S.DT_EFETIVACAO, 'YYYY-MM-DD') as \"Data de efetivação\", ")
		.append("    (SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO VD, DOMINIO D WHERE VD.ID_DOMINIO = D.ID_DOMINIO AND D.NM_DOMINIO = 'DM_SITUACAO_SIMULACAO' AND VD.Vl_VALOR_DOMINIO = S.TP_SITUACAO_APROVACAO) as \"Situação\", ")
		.append("    U.NM_USUARIO as \"Nome do Executivo\", ")
		.append("    M.DS_MOTIVO_REPROVACAO as \"Descrição da Reprovação\" ")
		.append("FROM ")
		.append("    SIMULACAO S, ")
		.append("    PESSOA P, ")
		.append("    FILIAL F, ")
		.append("    REGIONAL R, ")
		.append("    REGIONAL_FILIAL RF, ")
		.append("    HISTORICO_EFETIVACAO H, ")
		.append("    USUARIO U, ")
		.append("    MOTIVO_REPROVACAO M ")
		.append("WHERE ")
		.append("    S.ID_FILIAL = F.ID_FILIAL  ")
		.append("    AND S.ID_CLIENTE = P.ID_PESSOA  ")
		.append("    AND S.ID_FILIAL = RF.ID_FILIAL   ")
		.append("    AND RF.ID_REGIONAL = R.ID_REGIONAL  ")
		.append("    AND U.NR_MATRICULA = S.NR_MATRICULA_PROMOTOR ")
		.append("    AND (R.DT_VIGENCIA_FINAL >= SYSDATE AND R.DT_VIGENCIA_INICIAL <= SYSDATE) ")
		.append("    AND (RF.DT_VIGENCIA_FINAL >= SYSDATE AND RF.DT_VIGENCIA_INICIAL <= SYSDATE) ")
		.append("    AND H.ID_SIMULACAO(+) = S.ID_SIMULACAO  ")
		.append("    AND M.ID_MOTIVO_REPROVACAO(+) = H.ID_MOTIVO_REPROVACAO ")
		.append("    AND (H.ID_HISTORICO_EFETIVACAO IN (SELECT MAX(HT.ID_HISTORICO_EFETIVACAO) FROM HISTORICO_EFETIVACAO HT GROUP BY HT.ID_SIMULACAO) ")
		.append("    	  OR H.ID_HISTORICO_EFETIVACAO IS NULL) ");
		
		if((parameters.containsKey("dtDataInicial") && parameters.get("dtDataInicial") != null) &&
			(parameters.containsKey("dtDataFinal") && parameters.get("dtDataFinal") != null)){
			sql.append("    AND S.DT_SIMULACAO >= :dtDataInicial AND S.DT_SIMULACAO <= :dtDataFinal ");
		}
		
		if(parameters.containsKey("idUsuario") && LongUtils.hasValue(MapUtils.getLong(parameters,"idUsuario"))){
			sql.append("    AND U.ID_USUARIO = :idUsuario ");
		}
		
		if(parameters.containsKey("tpSituacao") && StringUtils.isNotEmpty(MapUtils.getString(parameters,"tpSituacao"))){
			sql.append("    AND S.TP_SITUACAO_APROVACAO = :tpSituacao ");
		}
		
		if(parameters.containsKey("idFilial") && LongUtils.hasValue(MapUtils.getLong(parameters,"idFilial"))){
			sql.append("    AND S.ID_FILIAL = :idFilial ");
		}
		
		if(parameters.containsKey("idCliente") && LongUtils.hasValue(MapUtils.getLong(parameters,"idCliente"))){
			sql.append("    AND S.ID_CLIENTE = :idCliente ");
		}
		
		if(parameters.containsKey("idRegional") && LongUtils.hasValue(MapUtils.getLong(parameters,"idRegional"))){
			sql.append("    AND R.ID_REGIONAL = :idRegional ");
		}
		
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("Regional",Hibernate.STRING);
				sqlQuery.addScalar("CNPJ/CPF");
				sqlQuery.addScalar("Razão Social",Hibernate.STRING);
				sqlQuery.addScalar("Nome Fantasia",Hibernate.STRING);
				sqlQuery.addScalar("Proposta",Hibernate.STRING);
				sqlQuery.addScalar("Data/hora de Recebimento",Hibernate.STRING);
				sqlQuery.addScalar("Data de efetivação",Hibernate.STRING);
				sqlQuery.addScalar("Situação",Hibernate.STRING);
				sqlQuery.addScalar("Nome do Executivo",Hibernate.STRING);
				sqlQuery.addScalar("Descrição da Reprovação",Hibernate.STRING);
			}
		};
		
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), 
																	parameters, 
																	csq);
	}

    public List<Map<String, Object>> findParcelasMarkupProposta(Long idTabelaPreco, Long idProposta, boolean buscarMarkup) {
        
        StringBuilder sql = new StringBuilder();
        sql.append(getSqlMarkupFaixaProgressiva(buscarMarkup))
            .append(" union ")
            .append(getSqlMarkupProdutoEspecifico(buscarMarkup))
            .append(" union ")
            .append(getSqlMarkupPrecoFrete(buscarMarkup))
            .append(" union ")
            .append(getSqlMarkupParcelas(buscarMarkup));

        final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("ID_TABELA_PRECO", Hibernate.LONG);
                sqlQuery.addScalar("VL_PARCELA", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("VL_MARKUP", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("VL_MARKUP_CALCULADO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("ID_PARCELA_PRECO", Hibernate.LONG);
                sqlQuery.addScalar("ID_ROTA_PRECO", Hibernate.LONG);
                sqlQuery.addScalar("VL_FAIXA_PROGRESSIVA", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("ID_PRODUTO_ESPECIFICO", Hibernate.LONG);
                sqlQuery.addScalar("CD_PARCELA_PRECO", Hibernate.STRING);
                sqlQuery.addScalar("DS_PARCELA", Hibernate.STRING);
                
            }
        };
        
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("idTabelaPreco", idTabelaPreco);
        parameters.put("idProposta", idProposta);
        return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), 
                parameters, 
                csq);
    }

    private String getSqlMarkupFaixaProgressiva(boolean buscarMarkup){
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT tpp.id_tabela_preco , \n");
        sql.append("  vfp.VL_FIXO                                          AS VL_PARCELA , \n");
        if (buscarMarkup){
            sql.append("  markup_vfp.VL_FIXO                                   AS VL_MARKUP , \n");
            sql.append("  vfp.VL_FIXO *(markup_vfp.VL_FIXO /100) + vfp.VL_FIXO AS VL_MARKUP_CALCULADO , \n");
        }else{
            sql.append(" TO_NUMBER(pg.DS_CONTEUDO)     AS VL_MARKUP, \n");
            sql.append("  vfp.VL_FIXO *(TO_NUMBER(pg.DS_CONTEUDO) /100) + vfp.VL_FIXO AS VL_MARKUP_CALCULADO , \n");
        }
        sql.append("  PP.ID_PARCELA_PRECO , \n");
        sql.append("  vfp.ID_ROTA_PRECO , \n");
        sql.append("  fp.VL_FAIXA_PROGRESSIVA , \n");
        sql.append("  NULL AS ID_PRODUTO_ESPECIFICO , \n");
        sql.append("  PP.CD_PARCELA_PRECO , \n");
        sql.append("  VI18N(PP.NM_PARCELA_PRECO_I) \n");
        sql.append("  || ':' \n");
        sql.append("  || fp.VL_FAIXA_PROGRESSIVA \n");
        sql.append("  ||'Kg' AS ds_parcela \n");
        sql.append("FROM tabela_preco_parcela tpp , \n");
        sql.append("  parcela_preco pp , \n");
        sql.append("  faixa_progressiva fp , \n");
        sql.append("  valor_faixa_progressiva vfp , \n");
        if (buscarMarkup){
            sql.append("  MARKUP_FX_PROGRESSIVA markup_fp , \n");
            sql.append("  VL_MARKUP_FAIXA_PROGRESSIVA markup_vfp \n");
        }else{
            sql.append(" parametro_geral pg \n");
        }
        sql.append("WHERE tpp.id_parcela_preco              = pp.id_parcela_preco \n");
        sql.append("AND tpp.id_tabela_preco                 = :idTabelaPreco \n");
        sql.append("AND fp.id_tabela_preco_parcela          = tpp.id_tabela_preco_parcela \n");
        sql.append("AND fp.id_produto_especifico           IS NULL \n");
        sql.append("AND vfp.id_faixa_progressiva            = fp.ID_FAIXA_PROGRESSIVA \n");
        sql.append("and exists (select 1 from destino_proposta where id_rota_preco = vfp.id_rota_preco and id_proposta = :idProposta) \n");
        
        
        if (buscarMarkup){
            sql.append("AND markup_fp.id_rota_preco             = vfp.id_rota_preco \n");
            sql.append("AND markup_fp.DT_VIGENCIA_INI          <= TRUNC(SYSDATE) \n");
            sql.append("AND markup_fp.DT_VIGENCIA_FIM          >= TRUNC(SYSDATE) \n");
            sql.append("AND markup_vfp.ID_FAIXA_PROGRESSIVA     = fp.ID_FAIXA_PROGRESSIVA \n");
            sql.append("AND markup_vfp.ID_MARKUP_FX_PROGRESSIVA = markup_fp.ID_MARKUP_FX_PROGRESSIVA \n");
        }else{
            sql.append("and pg.nm_parametro_geral = 'PC_MARKUP_GERAL' \n");
        }
        
        if (buscarMarkup){
            
            sql.append(" UNION \n");
            sql.append("SELECT tpp.id_tabela_preco , \n");
            sql.append("  vfp.VL_FIXO                                        AS VL_PARCELA , \n");
            sql.append("  markup.VL_MARKUP                                   AS VL_MARKUP , \n");
            sql.append("  vfp.VL_FIXO *(markup.VL_MARKUP /100) + vfp.VL_FIXO AS VL_MARKUP_CALCULADO , \n");
            sql.append("  PP.ID_PARCELA_PRECO , \n");
            sql.append("  vfp.ID_ROTA_PRECO , \n");
            sql.append("  fp.VL_FAIXA_PROGRESSIVA , \n");
            sql.append("  NULL AS ID_PRODUTO_ESPECIFICO , \n");
            sql.append("  PP.CD_PARCELA_PRECO , \n");
            sql.append("  VI18N(PP.NM_PARCELA_PRECO_I) \n");
            sql.append("  || ':' \n");
            sql.append("  || fp.VL_FAIXA_PROGRESSIVA \n");
            sql.append("  ||'Kg' AS ds_parcela \n");
            sql.append("FROM tabela_preco_parcela tpp , \n");
            sql.append("  parcela_preco pp , \n");
            sql.append("  faixa_progressiva fp , \n");
            sql.append("  valor_faixa_progressiva vfp , \n");
            sql.append("  markup \n");
            sql.append("WHERE tpp.id_parcela_preco      = pp.id_parcela_preco \n");
            sql.append("AND tpp.id_tabela_preco         = :idTabelaPreco \n");
            sql.append("AND fp.id_tabela_preco_parcela  = tpp.id_tabela_preco_parcela \n");
            sql.append("AND fp.id_produto_especifico   IS NULL \n");
            sql.append("AND vfp.id_faixa_progressiva    = fp.ID_FAIXA_PROGRESSIVA \n");
            sql.append("and exists (select 1 from destino_proposta where id_rota_preco = vfp.id_rota_preco and id_proposta = :idProposta) \n");
            sql.append("AND markup.id_tabela_preco      = tpp.id_tabela_preco \n");
            sql.append("AND NOT EXISTS \n");
            sql.append("  (SELECT * \n");
            sql.append("  FROM MARKUP_FX_PROGRESSIVA markup_fp \n");
            sql.append("  , VL_MARKUP_FAIXA_PROGRESSIVA markup_vfp \n");
            sql.append("  WHERE markup_fp.id_rota_preco  = vfp.id_rota_preco \n");
            sql.append("  AND markup_fp.DT_VIGENCIA_INI <= TRUNC(SYSDATE) \n");
            sql.append("  AND markup_fp.DT_VIGENCIA_FIM >= TRUNC(SYSDATE) \n");
            sql.append("  and markup_vfp.ID_FAIXA_PROGRESSIVA = fp.ID_FAIXA_PROGRESSIVA \n");
            sql.append("  and markup_vfp.ID_MARKUP_FX_PROGRESSIVA = markup_fp.ID_MARKUP_FX_PROGRESSIVA \n");
            sql.append("  )");
        }
        
        return sql.toString();
    }
    

    private String getSqlMarkupPrecoFrete(boolean buscarMarkup){
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT tpp.id_tabela_preco , \n");
        sql.append("  PF.VL_PRECO_FRETE                                               AS VL_PARCELA , \n");
        if (buscarMarkup){
            sql.append("  markup_pf.VL_FIXO                                               AS VL_MARKUP , \n");
            sql.append("  PF.VL_PRECO_FRETE *(markup_pf.VL_FIXO /100) + PF.VL_PRECO_FRETE AS VL_MARKUP_CALCULADO , \n");
        }else{
            sql.append(" TO_NUMBER(pg.DS_CONTEUDO)     AS VL_MARKUP, \n");
            sql.append("  PF.VL_PRECO_FRETE *(TO_NUMBER(pg.DS_CONTEUDO) /100) + PF.VL_PRECO_FRETE AS VL_MARKUP_CALCULADO , \n");
        }
        sql.append("  PP.ID_PARCELA_PRECO , \n");
        sql.append("  PF.ID_ROTA_PRECO , \n");
        sql.append("  NULL AS VL_FAIXA_PROGRESSIVA , \n");
        sql.append("  NULL AS ID_PRODUTO_ESPECIFICO , \n");
        sql.append("  PP.CD_PARCELA_PRECO , \n");
        sql.append("  VI18N(PP.NM_PARCELA_PRECO_I) AS DS_PARCELA \n");
        sql.append("FROM tabela_preco_parcela tpp , \n");
        sql.append("  parcela_preco pp , \n");
        sql.append("  preco_frete pf , \n");
        
        if (buscarMarkup){
            sql.append("  vl_markup_preco_frete markup_pf \n");
        }else{
            sql.append(" parametro_geral pg \n");
        }
        
        sql.append("WHERE tpp.id_parcela_preco      = pp.id_parcela_preco \n");
        sql.append("AND tpp.id_tabela_preco         = :idTabelaPreco \n");
        sql.append("AND pf.id_tabela_preco_parcela  = tpp.id_tabela_preco_parcela \n");
        sql.append("and exists (select 1 from destino_proposta where id_rota_preco = pf.id_rota_preco and id_proposta = :idProposta) \n");
        if (buscarMarkup){
            sql.append("AND markup_pf.id_rota_preco     = pf.id_rota_preco \n");
            sql.append("AND markup_pf.id_preco_frete    = pf.id_preco_frete \n");
            sql.append("AND markup_pf.DT_VIGENCIA_INI  <= TRUNC(SYSDATE) \n");
            sql.append("AND (markup_pf.DT_VIGENCIA_FIM >= TRUNC(SYSDATE) \n");
            sql.append("OR markup_pf.DT_VIGENCIA_FIM   IS NULL) \n");
        }else{
            sql.append("and pg.nm_parametro_geral = 'PC_MARKUP_GERAL' \n");
        }
        
        if (buscarMarkup){
            sql.append("UNION \n");
            sql.append("SELECT tpp.id_tabela_preco , \n");
            sql.append("  PF.VL_PRECO_FRETE                                              AS VL_PARCELA , \n");
            sql.append("  markup.VL_MARKUP                                               AS VL_MARKUP , \n");
            sql.append("  PF.VL_PRECO_FRETE *(markup.VL_MARKUP /100) + PF.VL_PRECO_FRETE AS VL_MARKUP_CALCULADO , \n");
            sql.append("  PP.ID_PARCELA_PRECO , \n");
            sql.append("  PF.ID_ROTA_PRECO , \n");
            sql.append("  NULL AS VL_FAIXA_PROGRESSIVA , \n");
            sql.append("  NULL AS ID_PRODUTO_ESPECIFICO , \n");
            sql.append("  PP.CD_PARCELA_PRECO , \n");
            sql.append("  VI18N(PP.NM_PARCELA_PRECO_I) AS DS_PARCELA \n");
            sql.append("FROM tabela_preco_parcela tpp , \n");
            sql.append("  parcela_preco pp , \n");
            sql.append("  preco_frete pf , \n");
            sql.append("  markup \n");
            sql.append("WHERE tpp.id_parcela_preco      = pp.id_parcela_preco \n");
            sql.append("AND pf.id_tabela_preco_parcela  = tpp.id_tabela_preco_parcela \n");
            sql.append("AND pf.ID_TABELA_PRECO_PARCELA  = tpp.ID_TABELA_PRECO_PARCELA \n");
            sql.append("and exists (select 1 from destino_proposta where id_rota_preco = pf.id_rota_preco and id_proposta = :idProposta) \n");
            sql.append("and not exists (select 1 from vl_markup_preco_frete where ID_ROTA_PRECO =  pf.id_rota_preco and ID_PRECO_FRETE = pf.id_preco_frete ) \n");
            sql.append("AND tpp.id_tabela_preco         = :idTabelaPreco \n");
            sql.append("AND markup.ID_TABELA_PRECO      = tpp.ID_TABELA_PRECO \n");
            sql.append("AND markup.id_parcela_preco    IS NULL \n");
        }
        
        
        return sql.toString();
    }
    
    
    private String getSqlMarkupParcelas(boolean buscarMarkup){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT tpp.id_tabela_preco , \n");
        sql.append("  parcela.VL_PARCELA                                               AS VL_PARCELA , \n");
        if (buscarMarkup){
            sql.append("  markup.VL_MARKUP                                                 AS VL_MARKUP , \n");
            sql.append("  parcela.VL_PARCELA *(markup.VL_MARKUP /100) + parcela.VL_PARCELA AS VL_MARKUP_CALCULADO , \n");
        }else{
            sql.append(" TO_NUMBER(pg.DS_CONTEUDO)     AS VL_MARKUP, \n");
            sql.append("  parcela.VL_PARCELA *(TO_NUMBER(pg.DS_CONTEUDO)/100) + parcela.VL_PARCELA AS VL_MARKUP_CALCULADO , \n");
        }
        
        sql.append("  PP.ID_PARCELA_PRECO , \n");
        sql.append("  NULL AS ID_ROTA_PRECO , \n");
        sql.append("  NULL AS VL_FAIXA_PROGRESSIVA , \n");
        sql.append("  NULL AS ID_PRODUTO_ESPECIFICO , \n");
        sql.append("  pp.CD_PARCELA_PRECO , \n");
        sql.append("  vI18N(pp.NM_PARCELA_PRECO_I) AS DS_PARCELA \n");
        sql.append("FROM tabela_preco_parcela tpp , \n");
        sql.append("  parcela_preco pp , \n");
        if (buscarMarkup){
            sql.append("  markup , \n");
        }else{
            sql.append(" parametro_geral pg, \n");
        }
        sql.append("  (SELECT id_generalidade AS id_parcela , \n");
        sql.append("    vl_generalidade       AS vl_parcela \n");
        sql.append("  FROM generalidade \n");
        sql.append("  UNION \n");
        sql.append("  SELECT id_valor_taxa AS id_parcela , vl_taxa AS vl_parcela FROM valor_taxa \n");
        sql.append("  UNION \n");
        sql.append("  SELECT id_valor_servico_adicional AS id_parcela , \n");
        sql.append("    vl_servico                      AS vl_parcela \n");
        sql.append("  FROM valor_servico_adicional \n");
        sql.append("  ) parcela \n");
        sql.append("WHERE tpp.id_parcela_preco = pp.id_parcela_preco \n");
        sql.append("AND tpp.id_tabela_preco    = :idTabelaPreco \n");
        sql.append("AND parcela.id_parcela     = tpp.id_tabela_preco_parcela \n");
        sql.append("AND NOT EXISTS \n");
        sql.append("  (SELECT * \n");
        sql.append("  FROM faixa_progressiva fp \n");
        sql.append("  WHERE fp.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela \n");
        sql.append("  ) \n");
        sql.append("AND NOT EXISTS \n");
        sql.append("  (SELECT * \n");
        sql.append("  FROM preco_frete pf \n");
        sql.append("  WHERE pf.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela \n");
        sql.append("  ) \n");
        if (buscarMarkup){
            sql.append("AND pp.id_parcela_preco    = markup.id_parcela_preco \n");
            sql.append("AND markup.id_tabela_preco = tpp.id_tabela_preco \n");
            sql.append("AND markup.DT_VIGENCIA_INICIAL <= TRUNC(SYSDATE) \n");
            sql.append("AND (markup.DT_VIGENCIA_FINAL  >= TRUNC(SYSDATE) \n");
            sql.append("OR markup.DT_VIGENCIA_FINAL    IS NULL) \n");
        }else{
            sql.append("and pg.nm_parametro_geral = 'PC_MARKUP_GERAL' \n");
        }

        if (buscarMarkup){
            sql.append("UNION \n");
            sql.append("SELECT tpp.id_tabela_preco , \n");
            sql.append("  parcela.VL_PARCELA                                               AS VL_PARCELA , \n");
            sql.append("  markup.VL_MARKUP                                                 AS VL_MARKUP , \n");
            sql.append("  parcela.VL_PARCELA *(markup.VL_MARKUP /100) + parcela.VL_PARCELA AS VL_MARKUP_CALCULADO , \n");
            sql.append("  PP.ID_PARCELA_PRECO , \n");
            sql.append("  NULL AS ID_ROTA_PRECO , \n");
            sql.append("  NULL AS VL_FAIXA_PROGRESSIVA , \n");
            sql.append("  NULL AS ID_PRODUTO_ESPECIFICO , \n");
            sql.append("  pp.CD_PARCELA_PRECO , \n");
            sql.append("  vI18N(pp.NM_PARCELA_PRECO_I) AS DS_PARCELA \n");
            sql.append("FROM tabela_preco_parcela tpp , \n");
            sql.append("  parcela_preco pp , \n");
            sql.append("  markup , \n");
            sql.append("  (SELECT id_generalidade AS id_parcela , \n");
            sql.append("    vl_generalidade       AS vl_parcela \n");
            sql.append("  FROM generalidade \n");
            sql.append("  UNION \n");
            sql.append("  SELECT id_valor_taxa AS id_parcela , vl_taxa AS vl_parcela FROM valor_taxa \n");
            sql.append("  UNION \n");
            sql.append("  SELECT id_valor_servico_adicional AS id_parcela , \n");
            sql.append("    vl_servico                      AS vl_parcela \n");
            sql.append("  FROM valor_servico_adicional \n");
            sql.append("  ) parcela \n");
            sql.append("WHERE tpp.id_parcela_preco   = pp.id_parcela_preco \n");
            sql.append("AND tpp.id_tabela_preco      = :idTabelaPreco \n");
            sql.append("AND markup.id_tabela_preco   = tpp.id_tabela_preco \n");
            sql.append("AND markup.id_parcela_preco IS NULL \n");
            sql.append("AND parcela.id_parcela       = tpp.id_tabela_preco_parcela \n");
            sql.append("AND NOT EXISTS \n");
            sql.append("  (SELECT * \n");
            sql.append("  FROM faixa_progressiva fp \n");
            sql.append("  WHERE fp.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela \n");
            sql.append("  ) \n");
            sql.append("AND NOT EXISTS \n");
            sql.append("  (SELECT * \n");
            sql.append("  FROM preco_frete pf \n");
            sql.append("  WHERE pf.id_tabela_preco_parcela = tpp.id_tabela_preco_parcela \n");
            sql.append("  ) \n");
        }
        
        return sql.toString();
    }
    
    
    private String getSqlMarkupProdutoEspecifico(boolean buscarMarkup){
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT tpp.id_tabela_preco , \n");
        sql.append("  vfp.VL_FIXO                                          AS VL_PARCELA , \n");
        if (buscarMarkup){
            sql.append("  markup_vfp.VL_FIXO                                   AS VL_MARKUP , \n");
            sql.append("  vfp.VL_FIXO *(markup_vfp.VL_FIXO /100) + vfp.VL_FIXO AS VL_MARKUP_CALCULADO , \n");
        }else{
            sql.append(" TO_NUMBER(pg.DS_CONTEUDO)     AS VL_MARKUP, \n");
            sql.append("  vfp.VL_FIXO *(TO_NUMBER(pg.DS_CONTEUDO) /100) + vfp.VL_FIXO AS VL_MARKUP_CALCULADO , \n");
        }
        
        
        sql.append("  PP.ID_PARCELA_PRECO , \n");
        sql.append("  vfp.ID_ROTA_PRECO , \n");
        sql.append("  NULL AS VL_FAIXA_PROGRESSIVA , \n");
        sql.append("  fp.ID_PRODUTO_ESPECIFICO , \n");
        sql.append("  pp.CD_PARCELA_PRECO , \n");
        sql.append("  'TE' \n");
        sql.append("  ||TO_CHAR(pe.NR_TARIFA_ESPECIFICA, '000') AS ds_parcela \n");
        sql.append("FROM tabela_preco_parcela tpp , \n");
        sql.append("  parcela_preco pp , \n");
        sql.append("  faixa_progressiva fp , \n");
        sql.append("  produto_especifico pe , \n");
        sql.append("  valor_faixa_progressiva vfp , \n");
        if (buscarMarkup){
            sql.append("  MARKUP_FX_PROGRESSIVA markup_fp , \n");
            sql.append("  VL_MARKUP_FAIXA_PROGRESSIVA markup_vfp \n");
        }else{
            sql.append(" parametro_geral pg \n");
        }
        sql.append("WHERE tpp.id_parcela_preco              = pp.id_parcela_preco \n");
        sql.append("AND tpp.id_tabela_preco                 = :idTabelaPreco \n");
        sql.append("AND fp.id_tabela_preco_parcela          = tpp.id_tabela_preco_parcela \n");
        sql.append("AND fp.id_produto_especifico            = pe.id_produto_especifico \n");
        sql.append("AND vfp.id_faixa_progressiva            = fp.ID_FAIXA_PROGRESSIVA \n");
        sql.append("and exists (select 1 from destino_proposta where id_rota_preco = vfp.id_rota_preco and id_proposta = :idProposta) \n");
        if (buscarMarkup){
            sql.append("AND markup_fp.id_rota_preco             = vfp.id_rota_preco \n");
            sql.append("AND markup_fp.DT_VIGENCIA_INI          <= TRUNC(SYSDATE) \n");
            sql.append("AND markup_fp.DT_VIGENCIA_FIM          >= TRUNC(SYSDATE) \n");
            sql.append("AND markup_vfp.ID_FAIXA_PROGRESSIVA     = fp.ID_FAIXA_PROGRESSIVA \n");
            sql.append("AND markup_vfp.ID_MARKUP_FX_PROGRESSIVA = markup_fp.ID_MARKUP_FX_PROGRESSIVA \n");
        }else{
            sql.append("and pg.nm_parametro_geral = 'PC_MARKUP_GERAL' \n");
        }
        
        if (buscarMarkup){
            sql.append("UNION \n");
            sql.append("SELECT tpp.id_tabela_preco , \n");
            sql.append("  vfp.VL_FIXO                                        AS VL_PARCELA , \n");
            sql.append("  markup.VL_MARKUP                                   AS VL_MARKUP , \n");
            sql.append("  vfp.VL_FIXO *(markup.VL_MARKUP /100) + vfp.VL_FIXO AS VL_MARKUP_CALCULADO , \n");
            sql.append("  PP.ID_PARCELA_PRECO , \n");
            sql.append("  vfp.ID_ROTA_PRECO , \n");
            sql.append("  NULL AS VL_FAIXA_PROGRESSIVA , \n");
            sql.append("  fp.ID_PRODUTO_ESPECIFICO , \n");
            sql.append("  pp.CD_PARCELA_PRECO , \n");
            sql.append("  'TE' \n");
            sql.append("  ||TO_CHAR(pe.NR_TARIFA_ESPECIFICA, '000') AS ds_parcela \n");
            sql.append("FROM tabela_preco_parcela tpp , \n");
            sql.append("  parcela_preco pp , \n");
            sql.append("  faixa_progressiva fp , \n");
            sql.append("  produto_especifico pe , \n");
            sql.append("  valor_faixa_progressiva vfp , \n");
            sql.append("  markup \n");
            sql.append("WHERE tpp.id_parcela_preco      = pp.id_parcela_preco \n");
            sql.append("AND tpp.id_tabela_preco         = :idTabelaPreco \n");
            sql.append("AND fp.id_tabela_preco_parcela  = tpp.id_tabela_preco_parcela \n");
            sql.append("AND pe.id_produto_especifico    = fp.id_produto_especifico \n");
            sql.append("AND vfp.id_faixa_progressiva    = fp.ID_FAIXA_PROGRESSIVA \n");
            sql.append("and exists (select 1 from destino_proposta where id_rota_preco = vfp.id_rota_preco and id_proposta = :idProposta) \n");
            sql.append("AND markup.id_tabela_preco      = tpp.id_tabela_preco \n");
            sql.append("AND NOT EXISTS \n");
            sql.append("  (SELECT * \n");
            sql.append("  FROM MARKUP_FX_PROGRESSIVA markup_fp \n");
            sql.append("  , VL_MARKUP_FAIXA_PROGRESSIVA markup_vfp \n");
            sql.append("  WHERE markup_fp.id_rota_preco  = vfp.id_rota_preco \n");
            sql.append("  AND markup_fp.DT_VIGENCIA_INI <= TRUNC(SYSDATE) \n");
            sql.append("  AND markup_fp.DT_VIGENCIA_FIM >= TRUNC(SYSDATE) \n");
            sql.append("  and markup_vfp.ID_FAIXA_PROGRESSIVA = fp.ID_FAIXA_PROGRESSIVA \n");
            sql.append("  and markup_vfp.ID_MARKUP_FX_PROGRESSIVA = markup_fp.ID_MARKUP_FX_PROGRESSIVA \n");
            sql.append("  )");
        }
        
        return sql.toString();
    }
	
}