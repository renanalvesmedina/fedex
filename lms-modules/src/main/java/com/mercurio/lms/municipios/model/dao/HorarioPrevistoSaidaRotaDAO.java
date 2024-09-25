package com.mercurio.lms.municipios.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.HorarioPrevistoSaidaRota;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class HorarioPrevistoSaidaRotaDAO extends BaseCrudDao<HorarioPrevistoSaidaRota, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return HorarioPrevistoSaidaRota.class;
    }

   


}