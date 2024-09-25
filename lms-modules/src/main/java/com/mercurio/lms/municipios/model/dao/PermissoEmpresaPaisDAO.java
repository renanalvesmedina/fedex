package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.PermissoEmpresaPais;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PermissoEmpresaPaisDAO extends BaseCrudDao<PermissoEmpresaPais, Long>
{

	
	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("empresa", FetchMode.JOIN);
		fetchModes.put("empresa.pessoa", FetchMode.JOIN);
		fetchModes.put("paisByIdPaisOrigem",FetchMode.JOIN);
		fetchModes.put("paisByIdPaisDestino",FetchMode.JOIN);
	}
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return PermissoEmpresaPais.class;
    }

    protected void initFindByIdLazyProperties(Map fetchModes) {
    	fetchModes.put("empresa",FetchMode.JOIN);
    	fetchModes.put("empresa.pessoa",FetchMode.JOIN);
    	fetchModes.put("paisByIdPaisOrigem",FetchMode.JOIN);
    	fetchModes.put("paisByIdPaisDestino",FetchMode.JOIN);
	}
    
    public boolean verificaPermissoEmpresaPaises(Long idEmpresa, Long idPaisOrigem, Long idPaisDestino, Long idPermissoEmpresaPais, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.setProjection(Projections.rowCount());
    	if(idPermissoEmpresaPais != null){
    		dc.add(Restrictions.ne("idPermissoEmpresaPais",idPermissoEmpresaPais));
    	}
 	   
 	   if (dtVigenciaFinal != null) {
 		  
 		   dc.add( Restrictions.not(Restrictions.or(
		   										Restrictions.lt("dtVigenciaFinal", dtVigenciaInicial),
						                        Restrictions.gt("dtVigenciaInicial", dtVigenciaFinal))));
 		} else {			
 			dc.add(Restrictions.not(Restrictions.lt("dtVigenciaFinal", dtVigenciaInicial)));
     		
 			
 		}
 	   	dc.add(Restrictions.eq("empresa.idEmpresa",idEmpresa));
    	dc.add(Restrictions.eq("paisByIdPaisOrigem.idPais", idPaisOrigem));
    	dc.add(Restrictions.eq("paisByIdPaisDestino.idPais",idPaisDestino));
    	Integer result = (Integer) findByDetachedCriteria(dc).get(0);
    	return result.intValue()>0;
    }
    
    public PermissoEmpresaPais findByFilialOrigemDestino(Long idPaisOrigem, Long idPaisDestino){

    	ProjectionList pl = Projections.projectionList()
    	.add(Projections.property("pep.idPermissoEmpresaPais"), "idPermissoEmpresaPais")
    	.add(Projections.property("pep.nrPermisso"), "nrPermisso")
    	;

    	YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();

    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pep")
    	.createAlias("pep.paisByIdPaisDestino", "pepd")
    	.createAlias("pep.paisByIdPaisOrigem", "pepo")

    	.add(Restrictions.eq("pepo.id", idPaisOrigem))
    	.add(Restrictions.eq("pepd.id", idPaisDestino))
    	.add(Restrictions.le("pep.dtVigenciaInicial", dataAtual))
    	.add(Restrictions.ge("pep.dtVigenciaFinal", dataAtual))

    	.setProjection(pl)
    	.addOrder(Order.desc("pep.dtVigenciaFinal"))
    	.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()))
    	;

    	List l = findByDetachedCriteria(dc);
		if(l != null && !l.isEmpty()) return (PermissoEmpresaPais)l.get(0);

    	return null;
    }
}