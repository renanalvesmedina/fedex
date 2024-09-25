package com.mercurio.lms.tabelaprecos.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tabelaprecos.model.TipoTabelaPreco;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoTabelaPrecoDAO extends BaseCrudDao<TipoTabelaPreco, Long>
{
	
	public List findListByCriteria(Map criterions) {
		List listaOrder = new ArrayList();
		listaOrder.add("tpTipoTabelaPreco:asc");
		listaOrder.add("nrVersao:asc");
		return super.findListByCriteria(criterions,listaOrder);
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return TipoTabelaPreco.class;
	}
	
	protected void initFindByIdLazyProperties(Map map) 
	{
		map.put("empresaByIdEmpresaCadastrada", FetchMode.JOIN);
		map.put("empresaByIdEmpresaCadastrada.pessoa", FetchMode.JOIN);
		map.put("cliente", FetchMode.JOIN);
		map.put("cliente.pessoa", FetchMode.JOIN);
		map.put("servico", FetchMode.JOIN);
	}
	
	protected void initFindPaginatedLazyProperties(Map map) 
	{
		map.put("empresaByIdEmpresaCadastrada", FetchMode.JOIN);
		map.put("empresaByIdEmpresaCadastrada.pessoa", FetchMode.JOIN);
		map.put("cliente", FetchMode.JOIN);
		map.put("cliente.pessoa", FetchMode.JOIN);
		map.put("servico", FetchMode.JOIN);
	}
	
	protected void initFindListLazyProperties(Map map) {
		map.put("empresaByIdEmpresaCadastrada", FetchMode.JOIN);
		map.put("empresaByIdEmpresaCadastrada.pessoa", FetchMode.JOIN);
	}
	
	public List findAtivos() {
		StringBuffer sb = new StringBuffer()
			.append("select new map(ttp.idTipoTabelaPreco as idTipoTabelaPreco, ")
			.append("ttp.tpTipoTabelaPreco as tpTipoTabelaPreco, ")
			.append("ttp.nrVersao as nrVersao, ")
			.append("s.idServico as servico_idServico, ")
			.append("s.dsServico as servico_dsServico, ")
			.append("s.tpModal as servico_tpModal, ")
			.append("pc.nmPessoa as cliente_pessoa_nmPessoa, ")
			.append("ec.id as empresaByIdEmpresaCadastrada_idEmpresa, ")
			.append("p.nmPessoa as empresaByIdEmpresaCadastrada_pessoa_nmPessoa) from ")
			.append(TipoTabelaPreco.class.getName()+ " as ttp ")
			.append("join ttp.empresaByIdEmpresaCadastrada as ec ")
			.append("join ec.pessoa as p ")
			.append("left join ttp.servico as s ")
			.append("left join ttp.cliente as c ")
			.append("left join c.pessoa as pc ")
			.append("where ttp.tpSituacao = :tpSituacao ")
			.append("order by ttp.tpTipoTabelaPreco, ttp.nrVersao");
		return AliasToTypedFlatMapResultTransformer
			.getInstance()
				.transformListResult(getAdsmHibernateTemplate()
										.findByNamedParam(sb.toString(), "tpSituacao", "A"));
	}
	
	/**
	 * Retorna todas as tabelas de preco que possuam a situacao fornecida e que
	 * <strong>NAO<strong> sejam do tipo fornecido.
	 * 
	 * @param tpSituacao
	 * @param tpTipoTabelaPreco
	 * @return
	 */
	public List findByTpSituacaoNotTpTipoTabelaPreco(String tpSituacao, String tpTipoTabelaPreco) {
		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("ttp.idTipoTabelaPreco"), "idTipoTabelaPreco")
		.add(Projections.property("ttp.tpTipoTabelaPreco"), "tpTipoTabelaPreco")
		.add(Projections.property("ttp.nrVersao"), "nrVersao")
		.add(Projections.property("ttp.dsIdentificacao"), "dsIdentificacao");
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ttp")
		.setProjection(pl)
		.add(Restrictions.ne("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco))
		.add(Restrictions.eq("ttp.tpSituacao", tpSituacao))
		.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	/**
	 * Retorna todas as tabelas de preco que possuam a situacao fornecida e que
	 * sejam do tipo fornecido.
	 * 
	 * @param tpSituacao
	 * @param tpTipoTabelaPreco
	 * @return
	 */
	public List findByTpSituacaoTpTipoTabelaPreco(String tpSituacao, String tpTipoTabelaPreco) {
		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("ttp.idTipoTabelaPreco"), "idTipoTabelaPreco")
		.add(Projections.property("ttp.tpTipoTabelaPreco"), "tpTipoTabelaPreco")
		.add(Projections.property("ttp.nrVersao"), "nrVersao")
		.add(Projections.property("ttp.dsIdentificacao"), "dsIdentificacao");
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ttp")
		.setProjection(pl)
		.add(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco))
		.add(Restrictions.eq("ttp.tpSituacao", tpSituacao))
		.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	public TipoTabelaPreco findByTipoTabelaBase(String tpTipoTabelaPreco, Integer nrVersao, Long idEmpresaCadastrada, Long idServico, Long idCliente) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ttp");
		if (StringUtils.isNotBlank(tpTipoTabelaPreco)) {
			dc.add(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco));
		}
		if (nrVersao != null) {
			dc.add(Restrictions.eq("ttp.nrVersao", nrVersao));
		}
		if (StringUtils.isNotBlank(tpTipoTabelaPreco)) {
			dc.add(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco));
		}
		if (idEmpresaCadastrada != null) {
			dc.add(Restrictions.eq("ttp.empresaByIdEmpresaCadastrada.id", idEmpresaCadastrada));
		}
		if (idServico != null) {
			dc.add(Restrictions.eq("ttp.servico.id", idServico));
		}
		if (idCliente != null) {
			dc.add(Restrictions.eq("ttp.cliente.id", idCliente));
		} else {
			dc.add(Restrictions.isNull("ttp.cliente"));
		}
		
		List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (result != null && result.size() > 0) {
			return (TipoTabelaPreco) result.get(0);
		}
		return null;
	}

	public Integer findByTpTipoTabelaPrecoUltimoNrVersao(String tpTipoTabelaPreco){
		StringBuilder sql = new StringBuilder().append("select (max(nrVersao) + 1) as nrVersao ")
				.append(" from TipoTabelaPreco ")
				.append(" where tpTipoTabelaPreco = ? ");

		Object obj[] = new Object[1];
		obj[0] = tpTipoTabelaPreco;
		List result = getAdsmHibernateTemplate().find(sql.toString(), obj);
		return ((Integer)result.get(0));
	}

	public boolean findNrVersaoNaoUtilizada(String tpTipoTabelaPreco, Integer nrVersao) {
		StringBuilder sql = new StringBuilder().append("select nrVersao ")
				.append(" from  TipoTabelaPreco ")
				.append(" where tpTipoTabelaPreco = ? ")
				.append(" and   nrVersao = ? ");

		Object obj[] = new Object[2];
		obj[0] = tpTipoTabelaPreco;
		obj[1] = nrVersao;
		List result = getAdsmHibernateTemplate().find(sql.toString(), obj);

		if (result != null && result.size() > 0) {
			return true;
		}
		return false;
	}
}
