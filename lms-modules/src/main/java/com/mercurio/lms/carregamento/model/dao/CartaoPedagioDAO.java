package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.CartaoPedagio;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CartaoPedagioDAO extends BaseCrudDao<CartaoPedagio, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return CartaoPedagio.class;
    }

    protected void initFindByIdLazyProperties(Map map) {
        map.put("operadoraCartaoPedagio", FetchMode.JOIN);
        map.put("operadoraCartaoPedagio.pessoa", FetchMode.JOIN);
    }

    protected void initFindPaginatedLazyProperties(Map map) {
        map.put("operadoraCartaoPedagio", FetchMode.JOIN);
        map.put("operadoraCartaoPedagio.pessoa", FetchMode.JOIN);
    }


    /**
     * 
     * @param idCartaoPedagio
     * @param idOperadoraCartaoPedagio
     * @param nrCartao
     * @param blVerificarDtValidade
     * @return
     */
    public List findCartaoPedagioByOperadora(Long idCartaoPedagio, Long idOperadoraCartaoPedagio, Long nrCartao, Boolean blVerificarDtValidade){
    	StringBuffer sql = new StringBuffer()
    		.append("from ")
    		.append(CartaoPedagio.class.getName()).append(" as cp ")
    		.append("where 1=1 ");
    	
    	List param = new ArrayList();
    	
    	if (idCartaoPedagio != null) {
    		sql.append("and cp.id = ? ");
    		param.add(idCartaoPedagio);
    	}
    	if (idOperadoraCartaoPedagio != null) {
    		sql.append("and cp.operadoraCartaoPedagio.id = ? ");
    		param.add(idOperadoraCartaoPedagio);
    	}
    	if (nrCartao != null) {
    		sql.append("and cp.nrCartao = ? ");
    		param.add(nrCartao);
    	}
    	if (blVerificarDtValidade != null && blVerificarDtValidade) {
    		sql.append("and cp.dtValidade >= ? ");
    		param.add(JTDateTimeUtils.getDataAtual());
    	}

    	List result = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    	return result;
    }
}