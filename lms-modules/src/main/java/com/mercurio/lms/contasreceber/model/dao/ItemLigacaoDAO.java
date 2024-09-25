package com.mercurio.lms.contasreceber.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contasreceber.model.ItemLigacao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ItemLigacaoDAO extends BaseCrudDao<ItemLigacao, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return ItemLigacao.class;
    }

   


}