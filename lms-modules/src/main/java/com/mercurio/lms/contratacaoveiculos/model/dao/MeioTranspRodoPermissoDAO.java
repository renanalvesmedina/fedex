package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspRodoPermisso;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MeioTranspRodoPermissoDAO extends BaseCrudDao<MeioTranspRodoPermisso, Long>
{

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("meioTransporteRodoviario", FetchMode.JOIN);
		fetchModes.put("meioTransporteRodoviario.meioTransporte", FetchMode.JOIN);
		fetchModes.put("pais", FetchMode.JOIN);
		
	}
	
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("meioTransporteRodoviario", FetchMode.JOIN);
		fetchModes.put("meioTransporteRodoviario.meioTransporte", FetchMode.JOIN);
		fetchModes.put("pais", FetchMode.JOIN);
	}
    
	public boolean findMeioTranspRodoByVigencia(MeioTranspRodoPermisso meioTranspRodoPermisso){
		DetachedCriteria dc = createDetachedCriteria();
		if(meioTranspRodoPermisso.getIdMeioTranspRodoPermisso() != null){
			dc.add(Restrictions.ne("idMeioTranspRodoPermisso",meioTranspRodoPermisso.getIdMeioTranspRodoPermisso()));
		}

		dc = JTVigenciaUtils.getDetachedVigencia(dc,meioTranspRodoPermisso.getDtVigenciaInicial(),meioTranspRodoPermisso.getDtVigenciaFinal());
				
		dc.add(Restrictions.eq("pais.idPais",meioTranspRodoPermisso.getPais().getIdPais()));
		
		dc.add(Restrictions.eq("meioTransporteRodoviario.idMeioTransporte",meioTranspRodoPermisso.getMeioTransporteRodoviario().getIdMeioTransporte()));
		
		return findByDetachedCriteria(dc).size()>0;
	}
    
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MeioTranspRodoPermisso.class;
    }
    
	
   


}