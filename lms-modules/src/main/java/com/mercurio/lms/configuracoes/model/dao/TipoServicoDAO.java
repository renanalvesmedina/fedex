package com.mercurio.lms.configuracoes.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.BaseCompareVarcharI18n;
import com.mercurio.lms.configuracoes.model.TipoServico;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoServicoDAO extends BaseCrudDao<TipoServico, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoServico.class;
    }
    
    public List findListByCriteria(Map criterions) {
    	
    	if (criterions == null) criterions = new HashMap();
    	List order = new ArrayList(1);
    	order.add("dsTipoServico");
    	
        return super.findListByCriteria(criterions, order);
    }

    /**
     * Verifica se um Tipo de Serviço possuí o campo Priorizar igual a sim.
     * 
     * @param idTipoServico O id do Tipo de Serviço.
     * @return true se nenhum Tipo se Serviço conter o campo Priorizar igual a sim ou false caso contrário.
     */
	public boolean verificarCampoPriorizar(Long idTipoServico) {

		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("blPriorizar",Boolean.TRUE));
		if (idTipoServico != null){
			dc.add(Restrictions.ne("id",idTipoServico));
		}

		return findByDetachedCriteria(dc).isEmpty();

	}
	
	/**
	 * Busca uma entidade entidade TipoServico de acordo com o campo dsTioServico
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 18/01/2007
	 *
	 * @param dsTipoServico
	 * @return
	 *
	 */
	public TipoServico findTipoServicoByDsTipoServico( String dsTipoServico ){

		if(dsTipoServico == null) return null;

		DetachedCriteria dc = this.createDetachedCriteria();
		dc.add(BaseCompareVarcharI18n.eq("dsTipoServico", dsTipoServico, LocaleContextHolder.getLocale()));

        return (TipoServico) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	 

}