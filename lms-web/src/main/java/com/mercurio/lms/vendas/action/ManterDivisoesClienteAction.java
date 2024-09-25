package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.PrazoVencimento;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.manterDivisoesClienteAction"
 */

public class ManterDivisoesClienteAction extends CrudAction {
	
	private UsuarioService usuarioService;
	private DivisaoClienteService divisaoClienteService;
	
	public void setService(DivisaoClienteService serviceService) {
		this.defaultService = serviceService;
	}

	/**
	 * Valida as permissoes do usu�rio
	 * @param idFilial
	 * @return
	 */
	public Boolean validatePermissao(Long idFilial) {
    	return getUsuarioService().validateAcessoFilialRegionalUsuario(idFilial);
    }
	
	public void removeById(java.lang.Long id) {
        ((DivisaoClienteService)defaultService).removeById(id);
    }

    public Serializable store(DivisaoCliente bean) {
    	validaDivisao(bean);
    	return ((DivisaoClienteService)defaultService).store(bean);
    }

    /**
     * Valida se o nome da divis�o alterada n�o � igual a alguma divis�o ativa do mesmo cliente
     * Caso tenha, dispara uma business exception 
     * @param bean
     */
    private void validaDivisao(DivisaoCliente bean){
    	//Se possuir id � porque est� alterando
    	boolean alteracao = (bean.getIdDivisaoCliente() != null);
    	
    	//Busca todas as divis�es do cliente
    	List divisoes = divisaoClienteService.findByIdCliente(bean.getCliente().getIdCliente());  
    	Iterator i = divisoes.iterator();
    	
    	if(!alteracao){
    	DivisaoCliente divisoesToValidate = divisaoClienteService.findByIdClienteCdDivisao(bean.getCliente().getIdCliente(), bean.getCdDivisaoCliente());
		if(divisoesToValidate != null){
			throw new BusinessException("LMS-01218");
		}
    	}
		
    	//Para cada uma verifica se o nome � o mesmo
    	while (i.hasNext()){
    		HashMap divisao = (HashMap)i.next();
    		String dsDivisao = (String)divisao.get("dsDivisaoCliente");
    		Long idDivisao = (Long)divisao.get("idDivisaoCliente");
 
 
    		if (//Caso estiver incluindo e j� tiver a divis�o com o mesmo nome
				(dsDivisao.equalsIgnoreCase(bean.getDsDivisaoCliente()) && !alteracao) ||
				// OU 
				//Caso estiver alterando e for outra divisao com o mesmo nome  
				(alteracao && !idDivisao.equals(bean.getIdDivisaoCliente()) && 
				dsDivisao.equalsIgnoreCase(bean.getDsDivisaoCliente()))
    		){  
    			throw new BusinessException("LMS-01032");
    		}
    	}
    }
    
    /**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((DivisaoClienteService)defaultService).removeByIds(ids);
    }

    public DivisaoCliente findById(java.lang.Long id) {
    	return ((DivisaoClienteService)defaultService).findById(id);
    }

    public PrazoVencimento findPrazoVencimentoByIdDivisao(Long idDivisaoCliente) {
		return ((DivisaoClienteService)defaultService).findPrazoVencimentoByIdDivisao(idDivisaoCliente);
	}
    
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public DivisaoClienteService getDivisaoClienteService() {
		return divisaoClienteService;
	}

	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}
}
