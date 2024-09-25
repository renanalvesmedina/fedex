 package com.mercurio.lms.contasreceber.model.dao;


import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contasreceber.model.Arquivo;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ArquivoDAO extends BaseCrudDao<Arquivo, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Arquivo.class;
    }

	public Arquivo findById(Long id) {
		return super.findById(id);
	}
    
       
}