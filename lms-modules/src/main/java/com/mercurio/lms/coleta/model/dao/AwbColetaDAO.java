package com.mercurio.lms.coleta.model.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.coleta.model.AwbColeta;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AwbColetaDAO extends BaseCrudDao<AwbColeta, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return AwbColeta.class;
    }
 
	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("awb",FetchMode.JOIN);
		map.put("awb.ciaFilialMercurio",FetchMode.JOIN);
		map.put("awb.ciaFilialMercurio.empresa",FetchMode.JOIN);
		map.put("awb.ciaFilialMercurio.empresa.pessoa",FetchMode.JOIN);
	}

	/**
     * Find que retorna uma lista de AWBs Coleta usando ID de Detalhe Coleta 
	 * @param idDetalheColeta
	 * @return
	 */    
    public List findAwbColetaByIdDetalheColeta(Long idDetalheColeta) {
    	DetachedCriteria dc = DetachedCriteria.forClass(AwbColeta.class);
    	
    	dc.createCriteria("detalheColeta")
			.add(Restrictions.eq("idDetalheColeta", idDetalheColeta));    	
    	dc.setFetchMode("awb", FetchMode.JOIN);    	
    	
    	return super.findByDetachedCriteria(dc);
    }    
    
    public void removeByIdDetalheColeta(Serializable idDetalheColeta) {
        String sql = "delete from " + AwbColeta.class.getName() + " as ac " +
		 			 " where " +
		 			 "ac.detalheColeta.id = :id";

        getAdsmHibernateTemplate().removeById(sql, idDetalheColeta);
    }       
}