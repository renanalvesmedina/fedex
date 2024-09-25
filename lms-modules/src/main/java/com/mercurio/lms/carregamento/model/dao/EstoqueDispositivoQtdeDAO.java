package com.mercurio.lms.carregamento.model.dao;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.EstoqueDispositivoQtde;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class EstoqueDispositivoQtdeDAO extends BaseCrudDao<EstoqueDispositivoQtde, Long> {

    /**
     * Nome da classe que o DAO é responsável por persistir.
     */
    protected final Class getPersistentClass() {
        return EstoqueDispositivoQtde.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("empresa", FetchMode.JOIN);
    	lazyFindById.put("empresa.pessoa", FetchMode.JOIN);
    	lazyFindById.put("filial", FetchMode.JOIN);
    	lazyFindById.put("filial.pessoa", FetchMode.JOIN);
    	lazyFindById.put("tipoDispositivoUnitizacao", FetchMode.JOIN);

        super.initFindByIdLazyProperties(lazyFindById);
    }

    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
        lazyFindPaginated.put("empresa", FetchMode.JOIN);
        lazyFindPaginated.put("empresa.pessoa", FetchMode.JOIN);
        lazyFindPaginated.put("filial", FetchMode.JOIN);
        lazyFindPaginated.put("filial.pessoa", FetchMode.JOIN);
        lazyFindPaginated.put("tipoDispositivoUnitizacao", FetchMode.JOIN);
        super.initFindPaginatedLazyProperties(lazyFindPaginated);
    }


    /**
     * Faz a pesquisa padrão da tela, agora sem restrição de data. 
     * @param criteria
     * @param findDef
     * @return ResultSetPage
     */
    public ResultSetPage findPaginatedWithDateRestriction(Map criteria,FindDefinition findDef) {
        return super.findPaginated(criteria, findDef);
    }
    

    /**
     * Método especializado que retorna o total de linhas baseado na 
     * restrição da pesquisa 
     * @param criteria
     * @param findDef
     * @return
     */
    public Integer getRowCountWithDateRestriction(Map criteria) {
        return super.getRowCount(criteria);
    }    
    
    /**
     * Retorna um POJO de EstoqueDispositivoQtde
     *
     * @param idTipoDispositivoUnitizacao
     * @param idEmpresa
     * @param idControleCarga
     * @param idFilial
     * @return
     */
    public EstoqueDispositivoQtde findEstoqueDispositivoQtde(Long idTipoDispositivoUnitizacao, Long idEmpresa, Long idControleCarga, Long idFilial) {
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(EstoqueDispositivoQtde.class);
    	dc.add(Restrictions.eq("tipoDispositivoUnitizacao.id", idTipoDispositivoUnitizacao));
    	dc.add(Restrictions.eq("empresa.id", idEmpresa));
    	if (idControleCarga != null) {
    		dc.add(Restrictions.eq("controleCarga.id", idControleCarga));
		}
    	if (idFilial != null) {
    		dc.add(Restrictions.eq("filial.id", idFilial));
		}
    	
        Criteria criteria = dc.getExecutableCriteria(getAdsmHibernateTemplate().getSessionFactory().getCurrentSession());
        
        return (EstoqueDispositivoQtde)criteria.uniqueResult();
    }

}