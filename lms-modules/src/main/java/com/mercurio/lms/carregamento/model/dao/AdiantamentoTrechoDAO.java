package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.carregamento.model.AdiantamentoTrecho;
import com.mercurio.lms.contratacaoveiculos.model.PostoConveniado;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AdiantamentoTrechoDAO extends BaseCrudDao<AdiantamentoTrecho, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return AdiantamentoTrecho.class;
    }


	/**
	 * Realiza a operacao de salvar.
	 * Faz uso da engine de DF2 para efetuar a operacao.
	 * 
	 * @param items
	 * @return
	 */
    public void storeAdiantamentoTrecho(ItemList items) {
    	getAdsmHibernateTemplate().deleteAll(items.getRemovedItems());
    	getAdsmHibernateTemplate().flush();
    	getAdsmHibernateTemplate().saveOrUpdateAll(items.getNewOrModifiedItems());
    	getAdsmHibernateTemplate().flush();
    }

    
    public List findByIdControleCarga(Long idControleCarga, Long idFilialOrigem, Long idFilialDestino) {
    	StringBuffer sql = new StringBuffer()
    	.append("from ").append(AdiantamentoTrecho.class.getName()).append(" as at ")
    	.append("inner join fetch at.filialByIdFilialOrigem filialOrigem ")
    	.append("inner join fetch at.filialByIdFilialDestino filialDestino ")
    	.append("where ")
    	.append("at.controleCarga.id = ? ");

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
     * Remove as instâncias do pojo de acordo com os parâmetros recebidos.
     * 
     * @param idControleCarga
     */
    public void removeByIdControleCarga(Long idControleCarga) {
    	StringBuffer sql = new StringBuffer()
	    	.append("delete from ")
	    	.append(AdiantamentoTrecho.class.getName()).append(" as at ")
	    	.append(" where at.controleCarga.id = ? ");

    	List param = new ArrayList();
    	param.add(idControleCarga);

    	super.executeHql(sql.toString(), param);
    }


	public PostoConveniado findPostoConveniadoByIdReciboFreteCarreteiro(Long idReciboFreteCarreteiro) {
		StringBuffer sql = new StringBuffer()
	    	.append("from ").append(PostoConveniado.class.getName()).append(" postoConveniado ")
	    	.append("inner join fetch  postoConveniado.pessoa pessoa ")
	    	.append("where ")
	    	.append(" exists ( from ").append(AdiantamentoTrecho.class.getName()).append(" at where at.reciboFreteCarreteiro.id = ? and at.postoConveniado = postoConveniado )");

    	List param = new ArrayList();
    	param.add(idReciboFreteCarreteiro);
    	
    	return (PostoConveniado) super.getAdsmHibernateTemplate().findUniqueResult(sql.toString(), param.toArray());
	}
}