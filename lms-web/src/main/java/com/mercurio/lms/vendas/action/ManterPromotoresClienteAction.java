package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.PromotorCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.PromotorClienteService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.manterPromotoresClienteAction"
 */

public class ManterPromotoresClienteAction extends CrudAction {
    private UsuarioService usuarioService;

    /**
     * Remove o promotor cliente correspondente ao id passado por par�metro
     * @param id Identificador do Promotor cliente a ser removido
     */
    public void removeById(java.lang.Long id) {
        ((PromotorClienteService)defaultService).removeById(id);
    }

	/**
     * Remove os promotores cliente correspondentes aos ids passados por par�metro
     * @param ids Identificadores dos Promotores cliente a serem removidos
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	getPromotorClienteService().removeByIds(ids);
    }

    /**
     * Busca um promotor cliente pelo identificador
     * @param id Identificador do promotor cliente a ser pesquisado
     * @return Promotor Cliente encontrado
     */
    public PromotorCliente findById(java.lang.Long id) {
    	return getPromotorClienteService().findById(id);
    }

    /**
     * Salva o promotor cliente passado por par�metro
     * @param bean Promotor Cliente a ser salvo na base
     * @return Promotor Cliente salvo
     */
    public Serializable store(PromotorCliente bean) {
        return getPromotorClienteService().store(bean);
    }

    /**
     * M�todo de pesquisa da tela de listagem
     * Busca a listagem de Promotores cliente
     * @param criteria Crit�rios de pesquisa
     * @return ResultSetPage contendo a lista de Promotores cliente resultantes da pesquisa e informa��es de pagina��o
     */
    public ResultSetPage findPaginatedOtimizado(TypedFlatMap criteria) {
        return getPromotorClienteService().findPaginatedOtimizado(criteria);
    }

    public Integer getRowCountOtimizado(TypedFlatMap criteria) {
    	return getPromotorClienteService().getRowCountOtimizado(criteria);
    }

    /**
     * M�todo invocado na lookup de usu�rio
     * @param tfm
     * @return List de usu�rios
     */
    public List findLookupFuncionarioPromotor(TypedFlatMap tfm){
    	return usuarioService.findLookupFuncionarioPromotor(tfm.getString("nrMatricula"));
    }

    /**
     * M�todo identico ao findLookupFuncionarioPromotor, mas esse � chamado
     * no find da listagem da lookup de Pesquisa de funcionarios promotores.
     * @param map Crit�rios 
     * @return List Lista de funcion�rios promotores
     */
    public ResultSetPage findPaginatedPromotores(TypedFlatMap tfm){        
    	return usuarioService.findPaginatedPromotor(tfm.getLong("funcionario.codFilial.idFilial")
    			, tfm.getString("nrMatricula")
    			, tfm.getString("funcionario.codPessoa.nome")
    			, tfm.getString("funcionario.codFuncao.cargo.codigo")
    			, tfm.getString("funcionario.codFuncao.codigo")
    			, tfm.getString("tpSituacaoFuncionario")
    			, FindDefinition.createFindDefinition(tfm));
    }

    /**
     * M�todo de contagem dos Promotores retornados pela pesquisa
     * @param criterios Crit�rios 
     * @return Integer Quantidade de registros encontrados.
     */
    public Integer getRowCountPromotores(TypedFlatMap tfm){
    	return usuarioService.getRowCountPromotor(tfm.getLong("funcionario.codFilial.idFilial")
    			, tfm.getString("nrMatricula")
    			, tfm.getString("funcionario.codPessoa.nome")
    			, tfm.getString("funcionario.codFuncao.cargo.codigo")
    			, tfm.getString("funcionario.codFuncao.codigo")
    			, tfm.getString("tpSituacaoFuncionario"));
    }

    public Map<String, Object> findDefaultData() {
    	Map<String, Object> result = new HashMap<String, Object>();
    	result.put("pcRateioComissao", 100);
    	result.put("dtInclusao", JTDateTimeUtils.getDataAtual());
    	return result;
    }

    public void setClienteService(ClienteService clienteService) {
    }

    public PromotorClienteService getPromotorClienteService() {
    	return ((PromotorClienteService)defaultService);
    }
    public void setPromotorClienteService(PromotorClienteService promotorClienteService) {
		this.defaultService = promotorClienteService;
	}

    public void setUsuarioService(UsuarioService usuarioService){
    	this.usuarioService = usuarioService;
    }
}