package com.mercurio.lms.contasreceber.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * DAO pattern: Carregar Arquivo Recebido de Pr�-Faturas.
 * Interface de carga das pr�-faturas com o layout "Proceda"
 * 
 * @author Rafael Andrade de Oliveira
 * @since 28/04/2006
 *
 * Esta classe fornece acesso a camada de dados da aplica��o atrav�s do suporte
 * ao Hibernate em conjunto com o Spring. N�o inserir documenta��o ap�s ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class CarregarArquivoRecebidoDAO extends BaseCrudDao<Cliente, Long> {

	protected Class getPersistentClass() {
		return Cliente.class;
	}
	
}