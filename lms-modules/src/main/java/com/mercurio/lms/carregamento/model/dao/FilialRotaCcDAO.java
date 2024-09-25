package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.carregamento.model.FilialRotaCc;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FilialRotaCcDAO extends BaseCrudDao<FilialRotaCc, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return FilialRotaCc.class;
    }
    
    protected void initFindListLazyProperties(Map map) {
    	map.put("filial", FetchMode.JOIN);
    	map.put("filial.pessoa", FetchMode.JOIN);
    }

    
    public void storeAll(List list) {
    	getAdsmHibernateTemplate().saveOrUpdateAll(list);
    }


    public List findFilialPosteriorByIdFilialRotaCc(Long idFilialRotaCc) {
    	StringBuffer sb = getSqlFindFilialAnteriorPosterior(true);
    
    	List param = new ArrayList();
    	param.add(idFilialRotaCc);
    	param.add(idFilialRotaCc);
    	
    	return getAdsmHibernateTemplate().find(sb.toString(), param.toArray());
    }

	private StringBuffer getSqlFindFilialAnteriorPosterior(boolean posterior) {
    	StringBuffer sb = new StringBuffer()
    	.append("Select new map(")
    	.append("frCc.idFilialRotaCc as idFilialRotaCc, ")
    	.append("frCc.nrOrdem as nrOrdem, ")
    	.append("filial.idFilial as filial_idFilial, ")
    	.append("filial.sgFilial as filial_sgFilial, ")
    	.append("pessoa.nmFantasia as filial_pessoa_nmFantasia ")
    	.append(") ")
    	.append("from ")
    	.append(FilialRotaCc.class.getName()).append(" as frCc ")
    	.append("inner join frCc.filial as filial ")
    	.append("inner join filial.pessoa as pessoa ")
    	.append("where ")

    	.append("frCc.controleCarga.id = (")
    	.append("select frCc1.controleCarga.id from ")
    	.append(FilialRotaCc.class.getName()).append(" as frCc1 ")
    	.append("where frCc1.id = ?) ")

    	.append("and frCc.nrOrdem = (")
    	.append("select frCc1.nrOrdem ").append(posterior ? "+ 1" : "-1").append(" from ")
    	.append(FilialRotaCc.class.getName()).append(" as frCc1 ")
    	.append("where frCc1.id = ?) ");
		return sb;
	}
    
    public List findFilialAnteriorByIdFilialRotaCc(Long idFilialRotaCc) {
    	StringBuffer sb = getSqlFindFilialAnteriorPosterior(false);

    	List param = new ArrayList();
    	param.add(idFilialRotaCc);
    	param.add(idFilialRotaCc);
    	
    	return getAdsmHibernateTemplate().find(sb.toString(), param.toArray());
    }
    
    
    /**
     * 
     * @param idControleCarga
     * @param nrOrdemFilialAnterior
     * @param blMenor
     * @param blMaior
     * @return
     */
    public List findFilialRotaCcWithNrOrdem(Long idControleCarga, Integer nrOrdem, Boolean blMenor, Boolean blMaior) {
    	StringBuffer sb = new StringBuffer()
    	.append("Select new map(")
    	.append("frCc.idFilialRotaCc as idFilialRotaCc, ")
    	.append("frCc.nrOrdem as nrOrdem, ")
    	.append("frCc.blInseridoManualmente as blInseridoManualmente, ")
    	.append("cc.idControleCarga as controleCarga_idControleCarga, ")
    	.append("filial.idFilial as filial_idFilial, ")
    	.append("filial.sgFilial as filial_sgFilial ")
    	.append(") ")
    	.append("from ")
    	.append(FilialRotaCc.class.getName()).append(" as frCc ")
    	.append("inner join frCc.filial as filial ")
    	.append("inner join frCc.controleCarga as cc ")
    	.append("where ")
    	.append("cc.id = ? ");

    	List param = new ArrayList();
    	param.add(idControleCarga);
    	
    	if (blMenor != null && blMenor.booleanValue()) {
    		sb.append("and frCc.nrOrdem < ? ");
    		param.add( Byte.valueOf(nrOrdem.toString()) );
    	}
    	if (blMaior != null && blMaior.booleanValue()) {
    		sb.append("and frCc.nrOrdem > ? ");
    		param.add( Byte.valueOf(nrOrdem.toString()) );
    	}
    	sb.append("order by frCc.nrOrdem ");
    
    	return getAdsmHibernateTemplate().find(sb.toString(), param.toArray());
    }
    
    /**
     * Solicitação CQPRO00006183 da Integração.
     * Método que retorna uma instancia da classe FilialRotaCC sendo esta 
     * uma filial anterior à filial recebida por parametro.
     * @param idControleCarga
     * @param idFilial
     * @return
     */
    public FilialRotaCc findFilialRotaCCAnterior(Long idControleCarga, Long idFilial){
        Long nrOrdem = getNrOrdemFilialRotaCc(idControleCarga, idFilial);
        if(nrOrdem == null) return null;
        
        StringBuilder hql = new StringBuilder();
        hql.append("select frcc from FilialRotaCc frcc ");
        hql.append("where frcc.controleCarga.id = :idControleCarga ");
        hql.append("and frcc.nrOrdem = :numeroOrdem ");

        Map paramMap = new HashMap();
        paramMap.put("idControleCarga", idControleCarga);
        paramMap.put("numeroOrdem", nrOrdem);
        return (FilialRotaCc)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), paramMap);
      }

    /**
     * Método que retorna ordem da FilialRotaCc segundo subconsulta da solicitação CQPRO00006183 da Integração
     * 
     * @param idControleCarga
     * @param idFilial
     * @return
     */
    private Long getNrOrdemFilialRotaCc(Long idControleCarga, Long idFilial){
    	if(idControleCarga==null || idFilial == null) return null;
    	StringBuilder hql = new StringBuilder();
    	hql.append("select frcc2.nrOrdem ");
    	hql.append("from FilialRotaCc frcc2 ");
    	hql.append("where frcc2.controleCarga.id = :idControleCarga and frcc2.filial.id = :idFilial");
      
    	Map paramMap = new HashMap();
    	paramMap.put("idControleCarga", idControleCarga);
    	paramMap.put("idFilial", idFilial);
 
    	Byte nrOrdem = (Byte)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), paramMap);
      
    	if(nrOrdem == null){
    		return null;
    	}
   		return Long.valueOf(nrOrdem.intValue()-1);
	}
    
    /**
     * Busca todas as FilialRotaCcs associadas ao controle de carga informado.
     * 
     * @param idControleCarga
     * @return
     */
    public List<FilialRotaCc> findByControleCarga(Long idControleCarga) {
    	StringBuilder hql = new StringBuilder()
    	.append("select frcc \n")
    	.append("from ").append(getPersistentClass().getName()).append(" frcc \n")
    	.append("join fetch frcc.filial f \n")
    	.append("join fetch f.pessoa p \n")
    	.append("where frcc.controleCarga.id = ? \n")
    	.append("order by frcc.nrOrdem asc");
    	return getAdsmHibernateTemplate().find(hql.toString(), idControleCarga);
    }
    
    /**
     * 
     * @param newOrModifiedItems
     */
    private void storeFilialRotaCc(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
		getAdsmHibernateTemplate().flush();
	}

    /**
     * 
     * @param removeItems
     */
	private void removeFilialRotaCc(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
		getAdsmHibernateTemplate().flush();
	}
    
	/**
	 * Realiza a operacao de salvar.
	 * Faz uso da engine de DF2 para efetuar a operacao.
	 * 
	 * @param items
	 * @return
	 */
    public void storeFilialRotaCc(ItemList items) {
    	removeFilialRotaCc(items.getRemovedItems());
 	   	storeFilialRotaCc(items.getNewOrModifiedItems());
    }
}