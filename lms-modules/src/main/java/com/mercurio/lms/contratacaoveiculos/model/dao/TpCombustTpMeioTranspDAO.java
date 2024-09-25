package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contratacaoveiculos.model.TpCombustTpMeioTransp;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TpCombustTpMeioTranspDAO extends BaseCrudDao<TpCombustTpMeioTransp, Long>
{

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("tipoCombustivel", FetchMode.JOIN);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TpCombustTpMeioTransp.class;
    }

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("tipoCombustivel", FetchMode.JOIN);
	}
	
	public boolean verificaTipoCombustivelMeioTransporte(Long idTipoMeioTransporte, Long idTipoCombustivel, Long idTpCombustTpMeioTransp){
		DetachedCriteria dc = createDetachedCriteria();
		if(idTpCombustTpMeioTransp != null){
			dc.add(Restrictions.ne("idTpCombustTpMeioTransp",idTpCombustTpMeioTransp));
		}
		DetachedCriteria dcTipoMeioTransporte = dc.createCriteria("tipoMeioTransporte");
		dcTipoMeioTransporte.add(Restrictions.eq("idTipoMeioTransporte",idTipoMeioTransporte));
		DetachedCriteria dcTipoCombustivel = dc.createCriteria("tipoCombustivel");
		dcTipoCombustivel.add(Restrictions.eq("idTipoCombustivel",idTipoCombustivel));
		
		return findByDetachedCriteria(dc).size()>0;
	}

   


}