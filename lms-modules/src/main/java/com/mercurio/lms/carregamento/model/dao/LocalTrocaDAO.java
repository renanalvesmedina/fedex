package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.LocalTroca;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LocalTrocaDAO extends BaseCrudDao<LocalTroca, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return LocalTroca.class;
    }

    /**
     * Remove o relacionamento entre o localTroca e controleTrecho
     * @param idControleCarga
     * @param idFilial
     */
    public void storeAtualizacaoLocalTrocaPeloControleTrecho(Long idControleCarga, Long idFilial) {
    	StringBuffer sql = new StringBuffer()
   	    	.append("update ")
	    	.append(LocalTroca.class.getName()).append(" as lt ")
	    	.append(" set lt.controleTrecho = null ")
	    	.append("where lt.controleTrecho.id in ")
	    	.append("(select ct.id as idCt from ")
	    	.append(ControleTrecho.class.getName())
	    	.append(" as ct ")
	    	.append("where ct.controleCarga.id = ? ")
	    	.append("and (ct.filialByIdFilialOrigem.id = ? or ct.filialByIdFilialDestino.id = ?)) ");
    	
    	List param = new ArrayList();
    	param.add(idControleCarga);
    	param.add(idFilial);
    	param.add(idFilial);
    	
    	super.executeHql(sql.toString(), param);
    }

}