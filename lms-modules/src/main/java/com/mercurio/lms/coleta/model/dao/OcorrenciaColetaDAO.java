package com.mercurio.lms.coleta.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class OcorrenciaColetaDAO extends BaseCrudDao<OcorrenciaColeta, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return OcorrenciaColeta.class;
    }

    /**
     * Retorna as ocorrencias de coleta para um certo tipo de evento de coleta
     * passado por par�metro ("CA"=cancelada, "SO"=solicita��o, ...).
     * @param strTpEventoColeta String que representa o evento de coleta ("CA"=cancelada, "SO"=solicita��o, ...).
     * @return List
     */
    public List findOcorrenciaColetaByTpEventoColeta(String strTpEventoColeta){
        DetachedCriteria dc = DetachedCriteria.forClass(OcorrenciaColeta.class)
        	.add(Restrictions.eq("tpEventoColeta", strTpEventoColeta));
        return super.findByDetachedCriteria(dc);
    }
    
    public List<OcorrenciaColeta> findOcorrenciaColetaByCodigo(Short codigo){
        DetachedCriteria dc = DetachedCriteria.forClass(OcorrenciaColeta.class)
        	.add(Restrictions.eq("codigo", codigo));
        return super.findByDetachedCriteria(dc);
    }       
    
    
}