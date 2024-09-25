package com.mercurio.lms.configuracoes.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TipoEnderecoPessoa;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class InscricaoEstadualDAO extends BaseCrudDao<InscricaoEstadual, Long>
{
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return InscricaoEstadual.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("tipoTributacaoIcms", FetchMode.JOIN);
		lazyFindById.put("unidadeFederativa", FetchMode.JOIN);
		lazyFindById.put("pessoa", FetchMode.JOIN);		
		lazyFindById.put("tiposTributacaoIe", FetchMode.SELECT);
		super.initFindByIdLazyProperties(lazyFindById);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("tipoTributacaoIcms", FetchMode.JOIN);
		lazyFindPaginated.put("unidadeFederativa", FetchMode.JOIN);
		super.initFindPaginatedLazyProperties(lazyFindPaginated);
	}

	/**
     * Verifica se já existe uma inscrição estadual com
     * inscricao padrao 
     * @param bean InscricaoEstadual
     * @return 
     */
	public boolean validarBlIndicadorPadrao(Long idInscricaoEstadual, Long idPessoa, boolean blAtivo) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("blIndicadorPadrao",Boolean.TRUE));
		dc.add(Restrictions.eq("pessoa.idPessoa",idPessoa));
		if (blAtivo == true){
			dc.add(Restrictions.eq("tpSituacao","A"));
		}
		if (idInscricaoEstadual != null){
			dc.add(Restrictions.ne("idInscricaoEstadual",idInscricaoEstadual));
		}
		return findByDetachedCriteria(dc).isEmpty();

	}
	/**
	 * @author Samuel Herrmann
	 * Função responsavel por buscar a ultima inscricao estadual que esteja ativa
	 * @param idPessoa
	 * @return retorna a ultima incrição estadual cadastrada da pessoa informada, null em se não encontrar 
	 */
	public InscricaoEstadual getLastInscricaoEstadualByPessoa(Long idPessoa) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("unidadeFederativa", "uf");
		dc.add(Restrictions.eq("tpSituacao","A"));
		dc.add(Restrictions.eq("pessoa.idPessoa", idPessoa));
		dc.addOrder(Order.desc("idInscricaoEstadual"));
		List list = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (list.size() == 0)
			return null;
		return (InscricaoEstadual)list.get(0);
	}
	
	/**
	 * Função responsavel por buscar as inscricões estaduais ativas
	 * 
	 * @author Samuel Alves
	 * @param String identificacao
	 * @return retorna a ultima incrição estadual cadastrada da pessoa informada, null em se não encontrar 
	 */
	public List<InscricaoEstadual> findLastInscricaoEstadualByIdentificacaoPessoa(String identificacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(InscricaoEstadual.class,"ie");
		
		dc.createAlias("ie.pessoa","pes");

		dc.add(Restrictions.eq("ie.tpSituacao","A"));
		dc.add(Restrictions.eq("pes.nrIdentificacao", identificacao.toString()));
		dc.addOrder(Order.desc("ie.blIndicadorPadrao"));
		List list = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (list.size() == 0)
			return null;
		return list;
	}
	
	/**
	 * Função responsável por buscar a inscrição estadual padrão e ativa
	 * 
	 * @param Long idPessoa 
	 * @return retorna a incrição estadual cadastrada da pessoa informada ou null se não encontrar 
	 */
	public InscricaoEstadual findIeByIdPessoaAtivoPadrao(Long idPessoa) {
		DetachedCriteria dc = DetachedCriteria.forClass(
				InscricaoEstadual.class, "ie");

		dc.createAlias("ie.pessoa", "pes");

		dc.add(Restrictions.eq("ie.tpSituacao", "A"));
		dc.add(Restrictions.eq("ie.blIndicadorPadrao", Boolean.TRUE));
		dc.add(Restrictions.eq("pes.idPessoa", idPessoa));
		dc.addOrder(Order.desc("ie.id"));
		
		@SuppressWarnings("unchecked")
		List<InscricaoEstadual> list = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (list.size() > 0)
			return list.get(0);
		else
			return null;

	}

	/**
	 * Busca o id da Unidade Federativa associada a pessoa que
	 * possua um endereço comercial. Utilizada para preencher a
	 * com de UF caso seja inserido um novo registro para a pessoa
	 * @author Alexandre Menezes
	 * @param idPessoa
	 * @return
	 */
	public Map findUnidadeFederativa(Long idPessoa) {
		
		Map result = new HashMap();
		
        String tpEnderecos = "COM";
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(TipoEnderecoPessoa.class,"tep");
    	
    	dc.createAlias("tep.enderecoPessoa","ep");
    	dc.createAlias("ep.pessoa","pes");
    	dc.createAlias("ep.municipio","mun");
    	dc.createAlias("mun.unidadeFederativa","uf");
    	
    	ProjectionList pj = Projections.projectionList();
    	
    	pj.add(Projections.property("uf.idUnidadeFederativa"));
    	pj.add(Projections.property("uf.sgUnidadeFederativa"));
    	pj.add(Projections.property("uf.nmUnidadeFederativa"));    	
    	
    	dc.setProjection(pj);
    		
    	dc.add(Restrictions.eq("pes.idPessoa", idPessoa));
    	dc.add(Restrictions.eq("tep.tpEndereco",tpEnderecos));
    	
    	List registros = findByDetachedCriteria(dc);
    	
    	if (!registros.isEmpty()) {
    		Object[] obj = (Object[])registros.get(0);
    		result.put("idUnidadeFederativa",(Long)obj[0]);
    		result.put("sgUnidadeFederativa",(String)obj[1]);
    		result.put("nmUnidadeFederativa",(String)obj[2]);
    	}
	    return result;
    	
	}
	
	/**
	 * Verifica se a InscricaoEstadual é Isento
	 *@author Robson Edemar Gehl
	 * @param id
	 * @return true se InscricaoEstadual for ISENTO; false, caso contrário.
	 */
	public boolean validateIsento(Long id){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("id", id));
		dc.add(Restrictions.eq("nrInscricaoEstadual", "ISENTO"));
		dc.setProjection(Projections.count("id"));
		Integer count = (Integer) findByDetachedCriteria(dc).get(0);
		return (count.intValue() > 0);
	}

	public InscricaoEstadual findByPessoaIndicadorPadrao(Long idPessoa, Boolean indicadorPadrao) {
		InscricaoEstadual ie = null;
		DetachedCriteria dc = createDetachedCriteria()
			.add(Restrictions.eq("pessoa.id", idPessoa))
			.add(Restrictions.eq("blIndicadorPadrao",indicadorPadrao));
		List registros = findByDetachedCriteria(dc);
    	
    	if (!registros.isEmpty()) {
    	   ie = (InscricaoEstadual)registros.get(0);	
    	}
	    return ie;
	}

	public List findByPessoa(Long idPessoa) {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("ie.idInscricaoEstadual"), "idInscricaoEstadual");
		projectionList.add(Projections.property("ie.nrInscricaoEstadual"), "nrInscricaoEstadual");
		projectionList.add(Projections.property("ie.blIndicadorPadrao"), "blIndicadorPadrao");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ie")
			.setProjection(projectionList)	
			.add(Restrictions.eq("ie.pessoa.id", idPessoa))
			.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}
	
	public List findInscricaoByPessoa(Long idPessoa) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ie")
			.add(Restrictions.eq("ie.pessoa.id", idPessoa));
		return findByDetachedCriteria(dc);
	}
	
	
	
	
	/**
	 * Busca as incricaoEstadual ativas da pessoa
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 28/09/2006
	 *
	 * @param idPessoa
	 * @param idInscricaoEstadual
	 * @return
	 *
	 */
	public List findInscricaoEstadualActiveByPessoa(Long idPessoa, Long idInscricaoEstadual) {
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("ie.idInscricaoEstadual"), "idInscricaoEstadual");
		projectionList.add(Projections.property("ie.nrInscricaoEstadual"), "nrInscricaoEstadual");
		projectionList.add(Projections.property("ie.blIndicadorPadrao"), "blIndicadorPadrao");
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ie")
			.setProjection(projectionList)	
			.add(Restrictions.eq("ie.tpSituacao","A"))
			.add(Restrictions.ne("ie.idInscricaoEstadual",idInscricaoEstadual))
			.add(Restrictions.eq("pessoa.idPessoa", idPessoa));
		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	public List<InscricaoEstadual> findInscricaoEstadualAtivaByPessoa(Long idPessoa) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ie")
			.add(Restrictions.eq("ie.tpSituacao","A"))
			.add(Restrictions.eq("pessoa.idPessoa", idPessoa));
		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	/**
	 * Carrega o InscricaoEstadual de acordo com os filtros
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 28/12/2006
	 *
	 * @param idPessoa
	 * @param nrInscricaoEstadual
	 * @return
	 *
	 */
	public InscricaoEstadual findByNrInscricaoEstadual( Long idPessoa, String nrInscricaoEstadual ){
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection(" ie ");
		
		hql.addInnerJoin( getPersistentClass().getName() + " ie ");
		hql.addInnerJoin("ie.pessoa p ");
		
		hql.addCriteria("p.idPessoa", "=", idPessoa);
		hql.addCriteria("ie.nrInscricaoEstadual", "=", nrInscricaoEstadual);
		
		return (InscricaoEstadual) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

    public List<InscricaoEstadual> findOutraInscricaoEstadual(Long idInscricaoEstadual, Pessoa pessoa, String nrInscricaoEstadual) {
        StringBuilder hql = new StringBuilder();
        hql.append(" select ie ");
        hql.append(" from InscricaoEstadual ie ");
        hql.append(" where ie.pessoa.id = ? ");
        hql.append("   and ie.nrInscricaoEstadual = ? ");
        if (idInscricaoEstadual != null) {
            hql.append("   and ie.idInscricaoEstadual <> ? ");
            return getAdsmHibernateTemplate().find(hql.toString(), new Object[] {pessoa.getIdPessoa(), nrInscricaoEstadual,idInscricaoEstadual});
        }
        return getAdsmHibernateTemplate().find(hql.toString(), new Object[] { pessoa.getIdPessoa(), nrInscricaoEstadual});
    }

    @SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = this.getHqlForFindPaginated(criteria);
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(),hql.getCriteria());
	}
	
	public Integer getRowCountCustom(TypedFlatMap criteria) {
		// Prevents fullscan on table.
		if(criteria.getLong("idPessoa") == null){
			return 0;
		}
		
		SqlTemplate hql = this.getHqlForFindPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false), hql.getCriteria());
	}
	
	private SqlTemplate getHqlForFindPaginated(TypedFlatMap criteria) {		
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(inscricaoEstadual.idInscricaoEstadual", "idInscricaoEstadual");
		hql.addProjection("inscricaoEstadual.nrInscricaoEstadual", "nrInscricaoEstadual");
		hql.addProjection("inscricaoEstadual.blIndicadorPadrao", "blIndicadorPadrao");
		hql.addProjection("inscricaoEstadual.tpSituacao", "tpSituacao");
		hql.addProjection("unidadeFederativa.sgUnidadeFederativa", "sgUnidadeFederativa)");

		StringBuilder hqlFrom = new StringBuilder();
		hqlFrom.append(InscricaoEstadual.class.getName()).append(" AS inscricaoEstadual");
		hqlFrom.append(" INNER JOIN inscricaoEstadual.unidadeFederativa AS unidadeFederativa");

		hql.addFrom(hqlFrom.toString());

		hql.addCriteria("inscricaoEstadual.pessoa.idPessoa","=", criteria.getLong("idPessoa"));		
		
		hql.addOrderBy("inscricaoEstadual.idInscricaoEstadual DESC");

		return hql;
	}
}