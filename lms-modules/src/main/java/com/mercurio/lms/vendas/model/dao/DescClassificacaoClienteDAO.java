package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.lms.vendas.model.DescClassificacaoCliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DescClassificacaoClienteDAO extends BaseCrudDao<DescClassificacaoCliente, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return DescClassificacaoCliente.class;
    }
    
    protected void initFindListLazyProperties(Map lazyFindList) {
        lazyFindList.put("tipoClassificacaoCliente",FetchMode.JOIN);        
    }

    /**
     * M�todo utilizado para ordenar a combo de classifica��es do cliente.
     */
    public List findListByCriteria(Map criterions) {
        
        ArrayList order = new ArrayList();  
        order.add("tipoClassificacaoCliente_.dsTipoClassificacaoCliente");
        order.add("dsDescClassificacaoCliente");
        
        return super.findListByCriteria(criterions, order);
    }

    /**
     * Busca somente as Classifica��es que possuem seu tipo ativo
     * @param criterios
     * @return Lista de classifica��es ativas
     */
    public List findClassificacoesAtivas(Map criterios) {
        
        DetachedCriteria dc = createDetachedCriteria();
        
        dc.createAlias("tipoClassificacaoCliente", "tc");
        
        dc.add(Restrictions.eq("tpSituacao","A"));
        dc.add(Restrictions.eq("tc.tpSituacao", "A"));
        dc.addOrder(OrderVarcharI18n.asc("tc.dsTipoClassificacaoCliente", LocaleContextHolder.getLocale()));
        dc.addOrder(OrderVarcharI18n.asc("dsDescClassificacaoCliente", LocaleContextHolder.getLocale()));                                        
        
        return findByDetachedCriteria(dc);
    }


}