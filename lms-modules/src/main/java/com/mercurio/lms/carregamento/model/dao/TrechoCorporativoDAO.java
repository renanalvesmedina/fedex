package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.carregamento.model.TrechoCorporativo;
import com.mercurio.lms.util.IntegerUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TrechoCorporativoDAO extends BaseCrudDao<TrechoCorporativo, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TrechoCorporativo.class;
    }

	/**
	 * Realiza a operacao de salvar.
	 * Faz uso da engine de DF2 para efetuar a operacao.
	 * 
	 * @param items
	 * @return
	 */
    public void storeTrechoCorporativo(ItemList items) {
    	getAdsmHibernateTemplate().deleteAll(items.getRemovedItems());
    	getAdsmHibernateTemplate().flush();
    	getAdsmHibernateTemplate().saveOrUpdateAll(items.getNewOrModifiedItems());
    	getAdsmHibernateTemplate().flush();
    }
    
    
    public List findByIdControleCarga(Long idControleCarga, Long idFilialOrigem, Long idFilialDestino) {
    	StringBuffer sql = new StringBuffer()
    	.append("from ").append(TrechoCorporativo.class.getName()).append(" as tc ")
    	.append("inner join fetch tc.filialByIdFilialOrigem filialOrigem ")
    	.append("inner join fetch tc.filialByIdFilialDestino filialDestino ")
    	.append("where ")
    	.append("tc.controleCarga.id = ? ");

    	List param = new ArrayList();
    	param.add(idControleCarga);
    	
    	if (idFilialOrigem != null) {
    		sql.append("and filialOrigem.id = ? ");
    		param.add(idFilialOrigem);
    	}
    	if (idFilialDestino != null) {
    		sql.append("and filialDestino.id = ? ");
    		param.add(idFilialDestino);
    	}
    	return super.getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    }

    /**
     * Verifica se existe Trecho corporativo
     * @author André Valadas
     * @param idControleCarga
     */
    public Boolean verifyTrechoCoporativoByIdControleCarga(final Long idControleCarga) {
    	StringBuilder hql = new StringBuilder();
    	hql.append("from ").append(TrechoCorporativo.class.getName()).append(" as tc ");
    	hql.append("where tc.controleCarga.id = ? ");
    
    	final List param = new ArrayList();
    	param.add(idControleCarga);
    	final Integer rowCount = (Integer)getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), param.toArray());
		return IntegerUtils.hasValue(rowCount);
    }

    /**
     * Remove as instâncias do pojo de acordo com os parâmetros recebidos.
     * 
     * @param idControleCarga
     */
    public void removeByIdControleCarga(Long idControleCarga) {
    	StringBuffer sql = new StringBuffer()
	    	.append("delete from ")
	    	.append(TrechoCorporativo.class.getName()).append(" as tc ")
	    	.append(" where tc.controleCarga.id = ? ");

    	List param = new ArrayList();
    	param.add(idControleCarga);

    	super.executeHql(sql.toString(), param);
    }
}