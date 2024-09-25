package com.mercurio.lms.configuracoes.model.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.CotacaoMoeda;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MoedaPaisDAO extends BaseCrudDao<MoedaPais, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MoedaPais.class;
    }
    
    
    public MoedaPais findByUniqueKey(Long idMoeda, Long idPais){
    	MoedaPais mp = null;
    	
    	List list = findByDetachedCriteria(createDetachedCriteria().add(
    			Expression.and(
    					Expression.eq("moeda.idMoeda",idMoeda),Expression.eq("pais.idPais",idPais)
						)));
    	if (!list.isEmpty()){
    		mp = (MoedaPais) list.get(0);
    	}
    	
    	return mp;
    }
    
    /**
     * 
     * @param criterias
     * @return
     */
    public List findMoedasByPaisLookup(Map map){
    	
    	DetachedCriteria dc = createDetachedCriteria();
		dc.setFetchMode("moedaPais",FetchMode.JOIN);
		dc.setFetchMode("moedaPais.moeda",FetchMode.JOIN);
		dc.createAlias("moedaPais.moeda","m");
		dc.add(Expression.like("m.dsMoeda",map.get("dsMoeda")));
    	
    	List list = findByDetachedCriteria(dc);
    	List moedas = null;
    	if (!list.isEmpty()){
    		CotacaoMoeda mc;
    		moedas = new ArrayList(5);
    		for (Iterator iter = list.iterator(); iter.hasNext();){
    			mc = (CotacaoMoeda) iter.next();
    			moedas.add(mc.getMoedaPais().getMoeda());
    		}
    	}
    	return moedas;
    	
    }
    
   public MoedaPais findByPaisAndMoeda(Long idPais, Long idMoeda) {
	   return (MoedaPais)DetachedCriteria.forClass(getPersistentClass(),"MP")
			.createAlias("MP.pais","P")
			.createAlias("MP.moeda","M")
			.add(Restrictions.eq("P.id",idPais))
			.add(Restrictions.eq("M.id",idMoeda))
			.getExecutableCriteria(getSession()).uniqueResult();
   }
  public List findMoedaByPaisSituacao(Long idPais, String tpSituacao){
	 return getAdsmHibernateTemplate()
     	.findByNamedQueryAndNamedParam(MoedaPais.FIND_MOEDA_BY_PAIS_SITUACAO,
     			new String[]{"idPais", "tpSituacaoMoedaPais", "tpSituacaoMoeda"}, 
     			new Object[]{idPais, tpSituacao, tpSituacao} );
  }
    
    /**
     * Verifica se já exist uma moeda mais utilizada nesse pais que é diferente dele.
     * @param MoedaPais
     * @return boolean true quando já existe um
     */
    public boolean existMoedaMaisUtilizada(MoedaPais mp){
    	SqlTemplate sql = new SqlTemplate();
	    
		sql.addProjection("count(mp)");
		sql.addFrom(MoedaPais.class.getName()+" mp ");		
		
		sql.addCriteria("mp.blIndicadorMaisUtilizada", "=", Boolean.TRUE);
		sql.addCriteria("mp.pais.id", "=", mp.getPais().getIdPais());
		sql.addCriteria("mp.idMoedaPais", "<>", mp.getIdMoedaPais());		
		
    	List numeroRegistro = getAdsmHibernateTemplate().find(sql.getSql(true),sql.getCriteria());
    	if (((Long)numeroRegistro.get(0)).intValue() > 0) {
    		return true;
    	}
    	return false;
    }  
    
    /**
     * Verifica se já exist uma moeda padrão nesse pais que é diferente dele.
     * @param MoedaPais
     * @return boolean true quando já existe um
     */
    public boolean existMoedaPadrao(MoedaPais mp){
    	SqlTemplate sql = new SqlTemplate();
  
		sql.addProjection("count(mp)");
		sql.addFrom(MoedaPais.class.getName()+" mp ");		
		
		sql.addCriteria("mp.blIndicadorPadrao", "=", Boolean.TRUE);
		sql.addCriteria("mp.pais.id", "=", mp.getPais().getIdPais());
		sql.addCriteria("mp.idMoedaPais", "<>", mp.getIdMoedaPais());		
		
    	List numeroRegistro = getAdsmHibernateTemplate().find(sql.getSql(true),sql.getCriteria());
    	if (((Long)numeroRegistro.get(0)).intValue() > 0) {
    		return true;
    	}
    	return false;
    }       
 	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindByIdLazyProperties(java.util.Map)
	 */
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("pais",FetchMode.JOIN);
		lazyFindById.put("moeda",FetchMode.JOIN);
	}
	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindPaginatedLazyProperties(java.util.Map)
	 */
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("pais",FetchMode.JOIN);
		lazyFindPaginated.put("moeda",FetchMode.JOIN);
	}
	
	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("moeda",FetchMode.JOIN);
	}
	
	public List findMoedaPaisBySituacao(String tpSituacao) {
	   	ProjectionList pl = Projections.projectionList()
	   				    .add(Projections.alias(Projections.property("blIndicadorMaisUtilizada"),"blIndicadorMaisUtilizada"))
	   				  	.add(Projections.alias(Projections.property("M.dsSimbolo"),"moeda_dsSimbolo"))
	   				  	.add(Projections.alias(Projections.property("M.idMoeda"),"moeda_idMoeda"))
	   					.add(Projections.alias(Projections.property("P.idPais"),"pais_idPais"));
		DetachedCriteria dc = createDetachedCriteria()
						   .setProjection(pl)
						   .createAlias("pais","P")
						   .createAlias("moeda","M")
						   .setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		if (tpSituacao != null)
				dc.add(Restrictions.eq("M.tpSituacao",tpSituacao));
	   return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	public List findMoedaPaisBySituacaoPais(String tpSituacao,Long idPais) {
	   	ProjectionList pl = Projections.projectionList()
	   				    .add(Projections.alias(Projections.property("blIndicadorMaisUtilizada"),"blIndicadorMaisUtilizada"))
	   				    .add(Projections.alias(Projections.property("idMoedaPais"),"idMoedaPais"))
	   				  	.add(Projections.alias(Projections.property("M.dsSimbolo"),"moeda_dsSimbolo"))
	   				  	.add(Projections.alias(Projections.property("M.idMoeda"),"moeda_idMoeda"))
	   					.add(Projections.alias(Projections.property("P.idPais"),"pais_idPais"));
		DetachedCriteria dc = createDetachedCriteria()
						   .setProjection(pl)
						   .createAlias("pais","P")
						   .createAlias("moeda","M")
						   .setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		if (tpSituacao != null)
				dc.add(Restrictions.eq("M.tpSituacao",tpSituacao));
		if (idPais != null)
				dc.add(Restrictions.eq("P.idPais",idPais));
	   return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	/**
     * Método que retorna as moedas do pais informado, ativo.
     * 
     * @param Long idPais
     * @param String strAtivo
     * @return List
     * */
    public List findByPais(Long idPais, String strAtivo){
    	StringBuffer sql = new StringBuffer();
    	sql.append("select mp from MoedaPais as mp");
    	sql.append(" join fetch mp.pais as p");
    	sql.append(" join fetch mp.moeda as m");
    	sql.append(" where");
    	sql.append(" p.id = ? ");
    	sql.append(" and m.tpSituacao = ?");
    	sql.append(" and mp.tpSituacao = ?");
    	sql.append(" order by m.dsSimbolo");
    	
    	Object[] values = {idPais,strAtivo,strAtivo};
    	
    	return this.getAdsmHibernateTemplate().find(sql.toString(), values);
    }
    
	/**
	 * Retorna a moeda a mais utilizada do pais informado como parámetro;
	 *
	 * @author Micka&el Jalbert
	 * @since 30/08/2006
	 *
	 * @param Long idPais
	 * @param Boolean blIndicador
	 * @return MoedaPais moedaPais
	 */
    public MoedaPais findMoedaPaisMaisUtilizada(Long idPais, Boolean blIndicador) {
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("mp");
    	
    	hql.addInnerJoin(MoedaPais.class.getName(), "mp");
    	hql.addInnerJoin("fetch mp.moeda", "m");
    	
    	hql.addCriteria("mp.pais.id", "=", idPais);
    	hql.addCriteria("mp.blIndicadorMaisUtilizada", "=", blIndicador);
    	
    	List lstMoedaPais = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    	
    	if (!lstMoedaPais.isEmpty()) {
    		return (MoedaPais)lstMoedaPais.get(0);
    	} else {
    		return null;
    	}
    }         
    
}