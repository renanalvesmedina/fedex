package com.mercurio.lms.configuracoes.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.ImpressoraFormulario;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ImpressoraFormularioService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.session.SessionUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.manterVinculoFormulariosImpressorasAction"
 */

public class ManterVinculoFormulariosImpressorasAction extends CrudAction {
	
	public void setImpressoraFormulario(ImpressoraFormularioService impressoraFormularioService) {
		this.defaultService = impressoraFormularioService;
	}
	
    public void removeById(java.lang.Long id) {
        ((ImpressoraFormularioService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((ImpressoraFormularioService)defaultService).removeByIds(ids);
    }

    /**
     * 
     *
     * @author Hector Julian Esnaola Junior
     * @since 22/11/2006
     *
     * @param id
     * @return
     *
     */
    public ImpressoraFormulario findById(java.lang.Long id) {
    	return ((ImpressoraFormularioService)defaultService).findById(id);
    }
    
    /**
     * 
     *
     * @author Hector Julian Esnaola Junior
     * @since 22/11/2006
     *
     * @param criteria
     * @return
     *
     */
    public ResultSetPage findPaginated(Map criteria) {
		return super.findPaginated(criteria);
	}

    /**
     * 
     *
     * @author Hector Julian Esnaola Junior
     * @since 22/11/2006
     *
     * @param criteria
     * @return
     *
     */
	public Integer getRowCount(Map criteria) {
		return super.getRowCount(criteria);
	}

	/**
     * Busca a filial do usuario logado
     * @return Filial
     */
    public Filial findFilialUsuario(){
    	Filial filialSessao  =  SessionUtils.getFilialSessao();
    	Filial filialRetorno = new Filial();
    	
    	filialRetorno.setSgFilial(filialSessao.getSgFilial());
    	filialRetorno.setIdFilial(filialSessao.getIdFilial());
    	
    	Pessoa pessoa = new Pessoa();
    	pessoa.setNmFantasia(filialSessao.getPessoa().getNmFantasia());
    	
    	filialRetorno.setPessoa(pessoa);
    	
    	return filialRetorno;
    } 

}
