package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.ReciboPostoPassagem;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ReciboPostoPassagemDAO extends BaseCrudDao<ReciboPostoPassagem, Long>{
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ReciboPostoPassagem.class;
    }
    
    /**
     * Retorna o ultimo ReciboPostoPassagem de uma determinada filial. Caso nenhum
     * registro seja encontrado retorna <code>null</code>. 
     * 
     * @param idFilial
     * @return
     */
    public ReciboPostoPassagem findLastReciboPostoPassagemFilial(Long idFilial) {
    	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ReciboPostoPassagem.class)
    		.setFetchMode("filial", FetchMode.JOIN)
    		.add(Restrictions.eq("filial.idFilial", idFilial))
    		.addOrder(Order.desc("nrReciboPostoPassagem"));
    	
    	List result = this.findByDetachedCriteria(detachedCriteria);
    	
    	return (ReciboPostoPassagem) result.get(0);
    	
    }
    
    public Integer getRowCountByIdControleCarga(Long idControleCarga, Boolean blMostrarCancelados){
    	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ReciboPostoPassagem.class)
			.add(Restrictions.eq("controleCarga.idControleCarga", idControleCarga))
			.addOrder(Order.asc("nrReciboPostoPassagem"));
		
    	if (blMostrarCancelados != null && blMostrarCancelados)
    		detachedCriteria.add(Restrictions.eq("tpStatusRecibo", "CA"));
    	else
    		detachedCriteria.add(Restrictions.ne("tpStatusRecibo", "CA"));

		List result = this.findByDetachedCriteria(detachedCriteria);
		return Integer.valueOf(result.size());
    }
    
    public ResultSetPage findPaginatedByIdControleCarga(Long idControleCarga, Boolean blMostrarCancelados, FindDefinition findDefinition) {
    	DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ReciboPostoPassagem.class)
			.setFetchMode("moeda", FetchMode.JOIN)
			.setFetchMode("filial", FetchMode.JOIN)
			.setFetchMode("usuario", FetchMode.JOIN)
			.add(Restrictions.eq("controleCarga.idControleCarga", idControleCarga))
			.addOrder(Order.asc("nrReciboPostoPassagem"));

    	if (blMostrarCancelados != null && blMostrarCancelados)
    		detachedCriteria.add(Restrictions.eq("tpStatusRecibo", "CA"));
    	else
    		detachedCriteria.add(Restrictions.ne("tpStatusRecibo", "CA"));

		return this.findPaginatedByDetachedCriteria(detachedCriteria, findDefinition.getCurrentPage(), findDefinition.getPageSize());
    }
    
	/**
	 * Método que busca uma list de Recibo de Posto de Passagem a partir do ID do Controle de Carga. 
	 * @param idControleCarga
	 * @return
	 */
	public List findReciboPostoPassagemByIdControleCarga(Long idControleCarga) {
    	DetachedCriteria dc = DetachedCriteria.forClass(ReciboPostoPassagem.class);
    	dc.add(Restrictions.eq("controleCarga.id", idControleCarga));
    	dc.add(Restrictions.ne("tpStatusRecibo", "CA"));
    	return  super.findByDetachedCriteria(dc);
	}

	/**
     * Altera o status para o Controle de Carga em questão.
     * 
     * @param idControleCarga
     */
    public void updateStatusReciboByIdControleCarga(Long idControleCarga) {
    	StringBuffer sql = new StringBuffer()
	    	.append("update ")
	    	.append(ReciboPostoPassagem.class.getName()).append(" as rpp ")
	    	.append(" set rpp.tpStatusRecibo = 'CA' ")
	    	.append("where rpp.controleCarga.id = ? ");

    	List param = new ArrayList();
    	param.add(idControleCarga);

    	executeHql(sql.toString(), param);
    }
    
    /**
     * Remove as instâncias do pojo, que estão associados ao controle de carga recebido por parâmetro.
     * @param idControleCarga
     */
    public void removeByIdControleCarga(Long idControleCarga) {
    	StringBuffer sql = new StringBuffer()
	    	.append("delete from ")
	    	.append(ReciboPostoPassagem.class.getName()).append(" as rpp ")
	    	.append(" where rpp.controleCarga.id = :id");

    	getAdsmHibernateTemplate().removeById(sql.toString(), idControleCarga);
    }
}