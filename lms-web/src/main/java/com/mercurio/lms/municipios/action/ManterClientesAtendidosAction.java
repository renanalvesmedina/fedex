package com.mercurio.lms.municipios.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.MunicipioFilialCliOrigem;
import com.mercurio.lms.municipios.model.service.MunicipioFilialCliOrigemService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.vendas.model.Cliente;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.manterClientesAtendidosAction"
 */

public class ManterClientesAtendidosAction extends CrudAction {
	public void setMunicipioFilialCliOrigem(MunicipioFilialCliOrigemService municipioFilialCliOrigemService) {
		this.defaultService = municipioFilialCliOrigemService;
	}
    public void removeById(java.lang.Long id) {
        ((MunicipioFilialCliOrigemService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((MunicipioFilialCliOrigemService)defaultService).removeByIds(ids);
    }
    
    
    public Map findById(java.lang.Long id) {
    	MunicipioFilialCliOrigem m = ((MunicipioFilialCliOrigemService)defaultService).findById(id);
    	TypedFlatMap retorno = bean2map(m);
    	Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(m);
    	retorno.put("acaoVigenciaAtual",acaoVigencia);

    	return retorno;
    }
    
    private TypedFlatMap bean2map(MunicipioFilialCliOrigem bean){
    	TypedFlatMap map = new TypedFlatMap();
	    map.put("idMunicipioFilialCliOrigem", bean.getIdMunicipioFilialCliOrigem());
	    map.put("dtVigenciaInicial", bean.getDtVigenciaInicial());
	    map.put("dtVigenciaFinal", bean.getDtVigenciaFinal());
	    	 	    
	    Cliente cliente = bean.getCliente();
	    map.put("cliente.pessoa.tpIdentificacao.description", cliente.getPessoa().getTpIdentificacao().getDescription()); 
	    map.put("cliente.idCliente", cliente.getIdCliente());
	    map.put("cliente.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao(cliente.getPessoa().getTpIdentificacao().getValue(), cliente.getPessoa().getNrIdentificacao()));
	    map.put("cliente.pessoa.nmPessoa", cliente.getPessoa().getNmPessoa());
	    
	    return map;
    }
}
