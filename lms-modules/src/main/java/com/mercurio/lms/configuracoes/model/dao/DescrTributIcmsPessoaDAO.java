package com.mercurio.lms.configuracoes.model.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.DescrTributIcmsPessoa;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DescrTributIcmsPessoaDAO extends BaseCrudDao<DescrTributIcmsPessoa, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DescrTributIcmsPessoa.class;
    }

    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("inscricaoEstadual",FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
      lazyFindById.put("inscricaoEstadual",FetchMode.JOIN);
	} 

	
	/**
     * Validar se existe outro intervalo de vigência 
     * existente para a mesma Inscrição Estadual.
     * 
     * @author Alexandre Menezes
     * @param idDescricaoTributacaoIcms 
     * @param dtInicioVigencia
     * @param dtFimVigencia
     * @return true or false
     */
    public boolean validarVigencia(Long idInscricaoEstadual, Long idDescrTributIcmsPessoa, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
     		
       DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
       dc.add(Restrictions.eq("inscricaoEstadual.idInscricaoEstadual",idInscricaoEstadual));
       if (idDescrTributIcmsPessoa != null) {
    	   dc.add(Restrictions.ne("idDescrTributIcmsPessoa",idDescrTributIcmsPessoa));  
       }
       
       boolean result = true;
       
       List list = findByDetachedCriteria(dc);
       
       
       for (Iterator iter = list.iterator(); iter.hasNext();) {
    	   DescrTributIcmsPessoa descrTributIcmsPessoa = (DescrTributIcmsPessoa)iter.next();
    	   YearMonthDay dtInicial = descrTributIcmsPessoa.getDtVigenciaInicial();
    	   YearMonthDay dtFinal = descrTributIcmsPessoa.getDtVigenciaFinal();
    	   if (dtFinal == null) {
    		   dtFinal = new YearMonthDay(4000,12,30); 
    	   }
    	   
          /**
           * As datas de vigência inicial ou final não 
           * estejam dentro de outro intervalo de vigência 
           * existente para a mesma Inscrição Estadual
           */
    	   
    	  if (dtVigenciaInicial.compareTo(dtInicial)>=0 && dtVigenciaInicial.compareTo(dtFinal)<=0) {
     		 result = false;
    		 break;
    	  }
    	  
    	  if (dtVigenciaFinal != null) {
    		  if (dtVigenciaFinal.compareTo(dtInicial)>=0 && dtVigenciaFinal.compareTo(dtFinal)<=0) {
    			  result = false;
    			  break;
    		  }
    	  } else if (dtVigenciaInicial.compareTo(dtFinal)<=0 ){
			  result = false;
			  break;
    	  }
       }
       
       return result;
    }

}