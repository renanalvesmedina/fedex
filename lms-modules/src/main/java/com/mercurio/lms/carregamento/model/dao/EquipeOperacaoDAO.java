package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.EquipeOperacao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EquipeOperacaoDAO extends BaseCrudDao<EquipeOperacao, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return EquipeOperacao.class;
    }

    protected void initFindPaginatedLazyProperties(Map map) {
    	map.put("controleCarga", FetchMode.JOIN);
    	map.put("equipe", FetchMode.JOIN);
    }

    protected void initFindByIdLazyProperties(Map map) {
    	map.put("equipe", FetchMode.JOIN);
    }
    
    /**
     * Remove os integrantes de uma equipe de operacao
     * 
     * @param removeItems
     */
	private  void removeIntegranteEqOperacao(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
	}
	
    /**
     * Salva os integrantes de uma equipe de operacao
     * 
     * @param newOrModifiedItems
     */
    private  void storeIntegranteEqOperacao(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
	}
	
	/**
	 * Realiza a operacao de salvar d tela iniciarCarregamento.
	 * Faz uso da engine de DF2 para efetuar a operacao.
	 * 
	 * @param carregamentoDescarga
	 * @param items
	 * @return
	 */
    public EquipeOperacao storeEquipeOperacao(EquipeOperacao equipeOperacao, ItemList items) {
        super.store(equipeOperacao);

 	   	this.removeIntegranteEqOperacao(items.getRemovedItems());
 	   	this.storeIntegranteEqOperacao(items.getNewOrModifiedItems());
 	   	getAdsmHibernateTemplate().flush(); 
 	   	return equipeOperacao;
    }
    
    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
     * parametros.
     * 
     * @param idCarregamentoDescarga
     * @param idControleCarga
     * @return Integer com o numero de registos com os dados da grid.
     */
    public Integer getRowCountByIdControleCarga(Long idCarregamentoDescarga, Long idControleCarga){
    	DetachedCriteria dc = addDetachedCriteriaByfindPaginatedByIdControleCarga(idCarregamentoDescarga, idControleCarga);
    	dc.setProjection(Projections.rowCount());
    	dc.addOrder(Order.desc("dhInicioOperacao"));
        List result = super.findByDetachedCriteria(dc);
        return (Integer) result.get(0);
    }

    /**
     * Retorna uma List com os objetos a serem mostrados na grid.
     * Exige que o idCarregamentoDescarga ou idControleCarga seja informado.
     * 
     * @param Long idCarregamentoDescarga
     * @param Long idControleCarga
     * @param Boolean blOrderDescIdEquipeOperacao
     * @param Boolean blOrderDescDhFimOperacao
     * @param FindDefinition findDefinition
     * @return ResultSetPage com os dados da grid.
     */
    public ResultSetPage findPaginatedByIdControleCarga(Long idCarregamentoDescarga, Long idControleCarga, 
    													Boolean blOrderDescIdEquipeOperacao, Boolean blOrderDescDhFimOperacao,
    													FindDefinition findDefinition)
    {
    	DetachedCriteria dc = addDetachedCriteriaByfindPaginatedByIdControleCarga(idCarregamentoDescarga, idControleCarga);
    	if (blOrderDescIdEquipeOperacao != null && blOrderDescIdEquipeOperacao.booleanValue())
    		dc.addOrder(Order.desc("idEquipeOperacao"));
    	else
    	if (blOrderDescDhFimOperacao != null && blOrderDescDhFimOperacao.booleanValue())
    		dc.addOrder(Order.desc("dhFimOperacao"));

    	return super.findPaginatedByDetachedCriteria(dc, findDefinition.getCurrentPage(), findDefinition.getPageSize());
    }
    

    /**
     * 
     * @param idCarregamentoDescarga
     * @param idControleCarga
     * @return
     */
    private DetachedCriteria addDetachedCriteriaByfindPaginatedByIdControleCarga(Long idCarregamentoDescarga, Long idControleCarga) {
    	DetachedCriteria dc = DetachedCriteria.forClass(EquipeOperacao.class)
		 	.setFetchMode("carregamentoDescarga",FetchMode.JOIN)
		 	.setFetchMode("equipe",FetchMode.JOIN);

		if (idCarregamentoDescarga != null) {
			dc.add(Restrictions.eq("carregamentoDescarga.id",idCarregamentoDescarga));
		}
		if (idControleCarga != null) {
			dc.add(Restrictions.eq("controleCarga.id",idControleCarga));
		}
		return dc;
    }
    
    
    /**
     * Retorna uma Lista
     * Exige que um idCarregamentoDescarga seja informado.
     * 
     * @param Long idCarregamentoDescarga
     * @return List com os dados.
     */
    public List findEquipeByIdControleCarga(Long idCarregamentoDescarga){
    	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(EquipeOperacao.class)
		 	.setFetchMode("carregamentoDescarga",FetchMode.JOIN)
		 	.setFetchMode("equipe",FetchMode.JOIN)
		 	.add(Restrictions.eq("carregamentoDescarga.idCarregamentoDescarga",idCarregamentoDescarga))
		 	.addOrder(Order.asc("dhInicioOperacao"));
    	
    	return super.findByDetachedCriteria(detachedCriteria);	
    }


    /**
     * Retorna a última equipe operacao cadastrada de um controle de carga de coleta/entrega
     * 
     * @param idControleCarga
     * @return
     */
    public EquipeOperacao findByIdControleCargaColetaEntrega(Long idControleCarga){
    	DetachedCriteria dc = DetachedCriteria.forClass(EquipeOperacao.class, "equipeOperacao")
		 	.setFetchMode("equipe",FetchMode.JOIN)
		 	.add(Restrictions.eq("equipeOperacao.controleCarga.id", idControleCarga))
		 	.add(Restrictions.isNull("equipeOperacao.carregamentoDescarga.id"))
		 	.addOrder(Order.desc("equipeOperacao.dhInicioOperacao.value"));

    	List lista = super.findByDetachedCriteria(dc);
    	if (lista.isEmpty())
    		return null;
    	return (EquipeOperacao)lista.get(0);
    }
    
    /**
     * Obtém a última EquipeOperacao sem data de fechamento e sem 
     * CarregamentoDescarga para o ControleCarga em questão.
     * @param idControleCarga
     * @return
     */
    public EquipeOperacao findLastEquipeOperacaoWithoutFechamentoByIdControleCarga(Long idControleCarga) {
    	StringBuffer s = new StringBuffer()
    	.append("select eo from "+EquipeOperacao.class.getName()+" eo join fetch eo.equipe ")
    	.append("where eo.id = (select max(e2.id) ")
    					.append("from "+EquipeOperacao.class.getName()+" e2 ")
    					.append("where e2.carregamentoDescarga is null ")
    					.append("  and e2.dhFimOperacao.value is null")
    					.append("  and e2.controleCarga.id = ? )");
    	
    	return (EquipeOperacao)getAdsmHibernateTemplate().getSessionFactory().getCurrentSession()
    	.createQuery(s.toString()).setParameter(0, idControleCarga).uniqueResult();
    }
    
    /**
     * Busca List de EquipeOperacao que seja de um determinado CarregamentoDescarga levando em consideração o tipo
     * de operação ("C" ou "D") e se a data de fim da operação é nula ou não.
     * @param idCarregamentoDescarga
     * @param blDhFimOperacaoNulo
     * @param tpOperacao
     * @return
     */
    public List findEquipeOperacaoByIdCarregamentoDescarga(Long idCarregamentoDescarga, boolean blDhFimOperacaoNulo, String tpOperacao) {
    	List param = new ArrayList();
		StringBuffer sql = new StringBuffer()
		.append("from ")
		.append(EquipeOperacao.class.getName()).append(" as eo ")
		.append("inner join fetch eo.equipe as equipe ")
		.append("inner join fetch eo.carregamentoDescarga as cd ")
		.append("where ")
		.append("cd.id = ? ")
		.append("and cd.tpOperacao = ? ");
		
		
		if (blDhFimOperacaoNulo) {
			sql.append("and eo.dhFimOperacao.value is null ");
		}
		param.add(idCarregamentoDescarga);
		param.add(tpOperacao);
		
		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    }    
    
    
    /**
     * 
     * @param idControleCarga
     * @return
     */
    public List findEquipeOperacaoByIdControleCarga(Long idControleCarga, Boolean blDhFimOperacaoNulo) {
    	List param = new ArrayList();
		StringBuffer sql = new StringBuffer()
		.append("from ")
		.append(EquipeOperacao.class.getName()).append(" as eo ")
		.append("inner join fetch eo.equipe as equipe ")
		.append("where ")
		.append("eo.controleCarga.id = ? ");
		
		if (blDhFimOperacaoNulo != null && blDhFimOperacaoNulo) {
			sql.append("and eo.dhFimOperacao.value is null ");
		}

		param.add(idControleCarga);
		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    }


    /**
     * 
     * @param idControleCarga
     */
    public List findEquipeOperacaoByCancelamentoControleCarga(Long idControleCarga) {
    	StringBuffer sql = new StringBuffer()
    	.append("from  ")
    	.append(EquipeOperacao.class.getName()).append(" as eo ")
    	.append("where eo.dhFimOperacao.value is null and (eo.controleCarga.id = ? or eo.carregamentoDescarga.id in (")
	    	.append("select cd.id as idCarregamentoDescarga from ")
			.append(CarregamentoDescarga.class.getName()).append(" cd ")
			.append("where cd.controleCarga.id = ?)) ");

    	List param = new ArrayList();
    	param.add(idControleCarga);
    	param.add(idControleCarga);

    	return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    }
    
    
    
    
    
    /**
     * Busca a Equipe Operacao atraves do controle de carga e o carregamento descarga
     * @param idControleCarga
     * @param idCarregamentoDescarga
     * @return
     */
	public EquipeOperacao findEquipeOperacaoByIdControleCargaAndCarregamentoDescarga(Long idControleCarga, Long idCarregamentoDescarga) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(EquipeOperacao.class)
		.setFetchMode("controleCarga", FetchMode.JOIN)
		.setFetchMode("equipe", FetchMode.JOIN)
		.setFetchMode("carregamentoDescarga", FetchMode.JOIN)
		.add(Restrictions.eq("controleCarga.id", idControleCarga))
		.add(Restrictions.eq("carregamentoDescarga.id", idCarregamentoDescarga));
		
		return (EquipeOperacao) getAdsmHibernateTemplate().findUniqueResult(dc);
		
	}
	
	
	
	public EquipeOperacao findEquipeOperacaoByIdControleCargaByIdCarregamentoDescarga(
												Long idControleCarga, Long idCarregamentoDescarga) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(EquipeOperacao.class.getName());
		hql.addCriteria("controleCarga.idControleCarga", "=", idControleCarga);
		hql.addCriteria("carregamentoDescarga.idCarregamentoDescarga", "=", idCarregamentoDescarga);
	
		List result = getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		if(result!=null){
			return (EquipeOperacao)result.get(0);
		}
		return null ;
	}

	public EquipeOperacao findEquipeOperacaoByDescricao(String descricao) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(EquipeOperacao.class.getName());
		hql.addCriteria("equipe.dsEquipe", "=", descricao);
		List result = getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		if(result!=null){
			return (EquipeOperacao)result.get(0);
		}
		return null ;
	}
    
}