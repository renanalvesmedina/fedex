package com.mercurio.lms.edi.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edi.model.ClienteEDIFilialEmbarcadora;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;

public class ClienteEDIFilialEmbarcadoraDAO extends BaseCrudDao<ClienteEDIFilialEmbarcadora, Long>{

	@Override
	protected Class getPersistentClass() {
		return ClienteEDIFilialEmbarcadora.class;
	}
	
	public List<ClienteEDIFilialEmbarcadora> findFiliaisEmbarcadoraById(Long id) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(ClienteEDIFilialEmbarcadora.class)	
		.add(Restrictions.eq("idClienteEDIFilialEmbarcadora", id));
		
		return findByDetachedCriteria(dc);
	}		
	
	public List findByNrIdentificacaoByIdFilial(String nrIdentificacao, Long idFilial) {
		StringBuilder query = new StringBuilder()
		.append(" select cefe from ClienteEDIFilialEmbarcadora as cefe, ")
		.append(" Cliente as clie, ")
		.append(" Pessoa as pess ")
		.append(" where cefe.clienteEmbarcador.idCliente = clie.idCliente ")
		.append(" and clie.idCliente = pess.idPessoa ")
		.append(" and cefe.filial.idFilial = ? ")
		.append(" and pess.nrIdentificacao = ? ")
		.append(" and pess.tpIdentificacao in (?, ?) ");
		return getAdsmHibernateTemplate().find(query.toString(), new Object [] {idFilial, nrIdentificacao, "CNPJ", "CPF"});
	}
	
	public void executeRemoveById(Long id){
		
		StringBuilder hql = new StringBuilder();
		hql.append("delete from ClienteEDIFilialEmbarcadora where idClienteEDIFilialEmbarcadora = ").append(id);
		
		executeHql(hql.toString(), new ArrayList());
	}

	@SuppressWarnings("unchecked")
	public ResultSetPage<ClienteEDIFilialEmbarcadora> findPaginated(PaginatedQuery paginatedQuery) {
		
		StringBuilder query = new StringBuilder()
			.append("from ClienteEDIFilialEmbarcadora as embarcadora ")
			.append(" join fetch embarcadora.cliente as cli ")
			.append(" join fetch embarcadora.clienteEmbarcador as cliem ")
			.append(" join fetch cliem.pessoa as pes ")
			.append(" join fetch embarcadora.filial as fil ")
			.append(" join fetch fil.pessoa as pesfil ")
			.append("where 1=1 ");
		
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if(MapUtils.getObject(criteria, "idClienteEDI") != null) {
			query.append("  and cli.id =:idClienteEDI ");
		}
		if(MapUtils.getObject(criteria, "idCliente") != null) {
			query.append("  and cliem.id =:idCliente ");
		}
		if(MapUtils.getObject(criteria, "idFilial") != null) {
			query.append("  and fil.id =:idFilial ");
		}		
		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
	}

	public ResultSetPage findPaginatedLookup(Map criteria, FindDefinition findDef) {
		SqlTemplate sql = getSqlFindPaginated((TypedFlatMap) criteria);

		StringBuilder projection = new StringBuilder();
		projection.append("new Map(clEmb.idClienteEDIFilialEmbarcadora as idClienteEDIFilialEmbarcadora, \n");
		projection.append("cl.idCliente as idCliente, \n");
		projection.append("fl.sgFilial as filial_sgFilial, \n");
		projection.append("pe.nmPessoa as pessoa_nmPessoa, \n");
		projection.append("pe.tpPessoa as pessoa_tpPessoa, \n");
		projection.append("pe.nrIdentificacao as pessoa_nrIdentificacao, \n");
		projection.append("pe.tpIdentificacao as pessoa_tpIdentificacao, \n");
		projection.append("pe.nmFantasia as pessoa_nmFantasia, \n");
		projection.append("ep.dsEndereco as enderecoPessoa_dsEndereco, \n");
		projection.append("ep.nrEndereco as enderecoPessoa_nrEndereco, \n");
		projection.append("ep.dsComplemento as enderecoPessoa_dsComplemento, \n");
		projection.append("tl.dsTipoLogradouro as enderecoPessoa_tipoLogradouro_dsTipoLogradouro, \n");
		projection.append("m.nmMunicipio as enderecoPessoa_municipio_nmMunicipio, \n");
		projection.append("uf.sgUnidadeFederativa as enderecoPessoa_municipio_unidadeFederativa_sgUnidadeFederativa, \n");
		projection.append("cl.nrConta as nrConta, \n");
		projection.append("cl.tpCliente as tpCliente, \n");
		projection.append("cl.tpSituacao as tpSituacao, \n");
		projection.append("filc.id as filialByIdFilialCobranca_idFilial, \n");
		projection.append("filc.sgFilial as filialByIdFilialCobranca_sgFilial, \n");
		projection.append("pesfilc.nmFantasia as filialByIdFilialCobranca_pessoa_nmFantasia)");

		sql.addProjection(projection.toString());

		StringBuffer joins = new StringBuffer();		
		joins.append(" join clEmb.clienteEmbarcador as cl ");
		joins.append(" join clEmb.filial as fl ");
		joins.append(" join clEmb.filial.empresa as emp ");
		joins.append(" join cl.pessoa as pe ");
		joins.append(" join cl.filialByIdFilialCobranca as filc ");
		joins.append(" join filc.pessoa as pesfilc ");
		joins.append(" left outer join pe.enderecoPessoa as ep ");
		joins.append(" left outer join ep.tipoLogradouro as tl ");
		joins.append(" left outer join ep.municipio as m ");
		joins.append(" left outer join m.unidadeFederativa as uf ");


		sql.addFrom(ClienteEDIFilialEmbarcadora.class.getName()+" clEmb " + joins.toString());
		sql.addOrderBy("pe.nmPessoa");
		sql.addOrderBy("pe.tpIdentificacao");
		sql.addOrderBy("pe.nrIdentificacao");

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria()); 
		List list = rsp.getList();
		rsp.setList(AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(list));
		return rsp;
	}
	
	private SqlTemplate getSqlFindPaginated(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(DomainValue.class.getName() + " dv join dv.domain do");		
		sql.addJoin("nvl(pe.tpIdentificacao, 'CNPJ')","dv.value"); 
		sql.addCustomCriteria("do.name = 'DM_TIPO_IDENTIFICACAO'");

		String nrIdentificacao = criteria.getString("pessoa.nrIdentificacao");
		if(StringUtils.isNotBlank(nrIdentificacao)) {
			sql.addCriteria("lower(pe.nrIdentificacao)", "like", nrIdentificacao.toLowerCase());
		}
		sql.addCriteria("pe.tpIdentificacao", "=", criteria.getString("pessoa.tpIdentificacao"));
		String nmPessoa = criteria.getString("pessoa.nmPessoa");
		if (StringUtils.isNotBlank(nmPessoa)) {
			sql.addCriteria("lower(pe.nmPessoa)", "like", nmPessoa.toLowerCase());
		}
		sql.addCriteria("pe.tpPessoa", "like", criteria.getString("pessoa.tpPessoa"));
		String nmFantasia = criteria.getString("nmFantasia");
		if (StringUtils.isNotBlank(nmFantasia)) {
			sql.addCriteria("lower(pe.nmFantasia)", "like", nmFantasia.toLowerCase());
		}
		sql.addCriteria("cl.nrConta", "=", criteria.getLong("nrConta"));
		sql.addCriteria("cl.tpCliente", "like", criteria.getString("tpCliente"));
		sql.addCriteria("cl.tpSituacao", "like", criteria.getString("tpSituacao"));
		sql.addCriteria("fl.idFilial", "=", criteria.getLong("idFilial"));
		sql.addCriteria("emp.idEmpresa", "=", criteria.getLong("idEmpresa"));
		
		return sql;
	} 
	public Integer getRowCountLookup(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = this.getSqlFindPaginated(criteria);
		sql.addProjection("count(clEmb.idClienteEDIFilialEmbarcadora)");

		StringBuilder joins = new StringBuilder();
		joins.append(ClienteEDIFilialEmbarcadora.class.getName()).append(" clEmb");
		joins.append(" join clEmb.clienteEmbarcador as cl ");
		joins.append(" join clEmb.filial as fl ");
		joins.append(" join clEmb.filial.empresa as emp ");
		joins.append(" join cl.pessoa as pe ");

		sql.addFrom(joins.toString());
		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return result.intValue();
		
	}
	
	public List<ClienteEDIFilialEmbarcadora> findLookupEmbarcadora(Long idCliente, String identificacao) {
		
		DetachedCriteria dc = createDetachedCriteria()
		.setFetchMode("clienteEmbarcador.pessoa", FetchMode.JOIN)
		.add(Restrictions.eq("cliente.id", idCliente))
		.add(Restrictions.eq("clienteEmbarcador.id", idCliente));
				
		return getAdsmHibernateTemplate().findByCriteria(dc);
	}

	public ClienteEDIFilialEmbarcadora findDadosEmbarcadora(Long idClienteEDIFilialEmbarcadora) {
		
	   ProjectionList pl = Projections.projectionList()
			.add(Projections.property("idClienteEDIFilialEmbarcadora"), "idClienteEDIFilialEmbarcadora")
			.add(Projections.property("pes.nrIdentificacao"), "clienteEmbarcador.pessoa.nrIdentificacao")		
	   		.add(Projections.property("pes.nmPessoa"), "clienteEmbarcador.pessoa.nmPessoa");		
		
		DetachedCriteria dc = createDetachedCriteria()
		.setProjection(pl)
		.createAlias("clienteEmbarcador", "emb")
		.createAlias("emb.pessoa", "pes")
		.add(Restrictions.eq("id", idClienteEDIFilialEmbarcadora));
		
		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(ClienteEDIFilialEmbarcadora.class));
		
		return (ClienteEDIFilialEmbarcadora)getAdsmHibernateTemplate().findUniqueResult(dc);		
	}
	
	public List findLookupClienteEmbarc(String nrIndentificacao) {
		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("cb.idClienteEDIFilialEmbarcadora"), "idClienteEDIFilialEmbarcadora")
		.add(Projections.property("c.idCliente"), "idCliente")
		.add(Projections.property("c.tpCliente"), "tpCliente")
		.add(Projections.property("p.nmPessoa"), "pessoa_nmPessoa")
		.add(Projections.property("p.nrIdentificacao"), "pessoa_nrIdentificacao")
		.add(Projections.property("p.tpIdentificacao"), "pessoa_tpIdentificacao")
		.add(Projections.property("p.tpPessoa"), "pessoa_tpPessoa")
		.add(Projections.property("pep.dsEndereco"), "pessoa_endereco_dsEndereco")
		.add(Projections.property("pep.nrEndereco"), "pessoa_endereco_nrEndereco")
		.add(Projections.property("pep.dsComplemento"), "pessoa_endereco_dsComplemento")
		.add(Projections.property("pep.dsBairro"), "pessoa_endereco_dsBairro")
		.add(Projections.property("pep.nrCep"), "pessoa_endereco_nrCep")
		.add(Projections.property("pepm.idMunicipio"), "pessoa_endereco_idMunicipio")
		.add(Projections.property("pepm.nmMunicipio"), "pessoa_endereco_nmMunicipio")
		.add(Projections.property("pepmuf.idUnidadeFederativa"), "pessoa_endereco_idUnidadeFederativa")
		.add(Projections.property("pepmuf.sgUnidadeFederativa"), "pessoa_endereco_sgUnidadeFederativa")
		.add(Projections.property("pepmp.nmPais"), "pessoa_endereco_nmPais")
		.add(Projections.property("peptl.dsTipoLogradouro"), "pessoa_endereco_dsTipoLogradouro");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "cb")
		.setProjection(pl)
		.createAlias("cb.clienteEmbarcador", "c")
		.createAlias("c.pessoa", "p")
		.createAlias("p.enderecoPessoa", "pep")
		.createAlias("pep.tipoLogradouro", "peptl")
		.createAlias("pep.municipio", "pepm")
		.createAlias("pepm.unidadeFederativa", "pepmuf")
		.createAlias("pepmuf.pais", "pepmp");

		if(StringUtils.isNotBlank(nrIndentificacao)) {
			dc.add(Restrictions.eq("p.nrIdentificacao", nrIndentificacao));
		}
		dc.add(Restrictions.eq("c.tpSituacao", "A"));

		dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());

		return findByDetachedCriteria(dc);
	}
	
}
