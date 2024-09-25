package com.mercurio.lms.configuracoes.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.EmpresaCobranca;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EmpresaCobrancaDAO extends BaseCrudDao<EmpresaCobranca, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return EmpresaCobranca.class;
    }

    /**
     * Verifica a existencia de Empresa Cobrança com mesmo Numero e Tipo de Identificacao, exceto ela (se existir!).
     * @param map
     * @return se existe alguma empresa de cobranca
     */
    public boolean verificaExistenciaEmpresaCobranca(EmpresaCobranca empresa){

    	DetachedCriteria dc = createDetachedCriteria();

    	dc.createAlias("pessoa", "p");
    	dc.add(Restrictions.eq("p.nrIdentificacao", empresa.getPessoa().getNrIdentificacao()));
    	dc.add(Restrictions.eq("p.tpIdentificacao", empresa.getPessoa().getTpIdentificacao()));

    	if (empresa.getIdEmpresaCobranca() != null){
    		dc.add(Restrictions.ne("idEmpresaCobranca", null));	
    	}

    	dc.setProjection( Projections.count("idEmpresaCobranca") );
    	
    	List list = findByDetachedCriteria(dc);
    	int count = ((Integer) list.get(0)).intValue();

    	return (count > 0);
    	
    }
    
    /**
     * Verifica a existencia de Empresa Cobrança.
     * @param Long idEmpresaCobranca
     * @return se existe alguma empresa de cobranca
     */
    public boolean findEmpresaCobrancaById(Long idEmpresaCobranca){

    	DetachedCriteria dc = createDetachedCriteria();

    	if (idEmpresaCobranca != null){
    		dc.add(Restrictions.eq("idEmpresaCobranca", idEmpresaCobranca));	
    	}

    	dc.setProjection( Projections.count("idEmpresaCobranca") );
    	
    	List list = findByDetachedCriteria(dc);
    	int count = ((Integer) list.get(0)).intValue();

    	return (count > 0);
    	
    }
        
    public List findListByCriteria(Map criterions) {
    	List lstOrderBy = new ArrayList();
    	lstOrderBy.add("pessoa_.nmPessoa");
    	return findListByCriteria(criterions, lstOrderBy);
    }
    
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("pessoa",FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("pessoa",FetchMode.JOIN);
	}
	
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("pessoa",FetchMode.JOIN);
	}

	/**
	 * Retorna 'true' se a pessoa informada é uma empresa cobranca ativa senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isEmpresaCobranca(Long idPessoa){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("count(ec.id)");
		
		hql.addInnerJoin(EmpresaCobranca.class.getName(), "ec");
		
		hql.addCriteria("ec.id", "=", idPessoa);
		hql.addCriteria("ec.tpSituacao", "=", "A");
		
		List lstEmpresaCobranca = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		
		if (((Long)lstEmpresaCobranca.get(0)) > 0){
			return true;
		} else {
			return false;
		}
	}	
}