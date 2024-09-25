package com.mercurio.lms.entrega.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.TipoDocumentoEntrega;
import com.mercurio.lms.entrega.model.service.TipoDocumentoEntregaService;



/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.entrega.manterTiposDocumentosConfirmadosEntregaAction"
 */

public class ManterTiposDocumentosConfirmadosEntregaAction extends CrudAction {
	public void setService(TipoDocumentoEntregaService tipoDocumentoEntregaService) {
		this.defaultService = tipoDocumentoEntregaService;
	}
    public void removeById(java.lang.Long id) {
        ((TipoDocumentoEntregaService)defaultService).removeById(id);
    }

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((TipoDocumentoEntregaService)defaultService).removeByIds(ids);
    }

    public TipoDocumentoEntrega findById(Long id){
    	return ((TipoDocumentoEntregaService) defaultService).findById(id);
    }
    
    
    public Serializable store(TypedFlatMap parameters) {
    	
    	/*
    	 * -	N�o dever� existir mais de um documento com o mesmo tipo de documento de cobran�a associado, 
    	 * 		caso isso ocorrer, exibir a mensagem LMS-09034 e abortar o processo
    	 * */
    	// variaveis 
    	TypedFlatMap mapa = new TypedFlatMap();
    	mapa.put("tpDocumentoCobranca", parameters.getDomainValue("tpDocumentoCobranca").getValue());
    	List lista = new ArrayList();
    	boolean erro = false;
    	int registros = 0;
    	// Pega o id do tipo documento entrega
    	long id = 0;
    	if (parameters.getLong("idTipoDocumentoEntrega") != null){
    		id = parameters.getLong("idTipoDocumentoEntrega").longValue();
    	}

    	
    	
    	// Se nao tiver tpDocumento grava.
    	if (!parameters.getDomainValue("tpDocumentoCobranca").getValue().equalsIgnoreCase("")){

    		// Se estiver editando um registro
    		if ((id != 0)){
    			// Busca todods tipos de documentos com o tpDocumentoCobranca da tela
		    	lista = ((TipoDocumentoEntregaService)defaultService).find(mapa);
		    	registros = lista.size();
	    		// se for edicao, mas o id encontrado for diferente do encontrado da erro, porque nesse caso esta salvando outro registro igual
	    		if (registros > 0 ){
		    		int i = 0;
	    			/* este while e caso de algum erro no banco e acabe retornando mais registros, como um update errado, 
		    		entao ele procura em todos regsitros se algum � difernente. um sendo diferente da erro */
		    		while ((i < registros) && (!erro)) {
		    			TipoDocumentoEntrega tipoDocumentoEntrega = (TipoDocumentoEntrega)lista.get(i);
			    		if ( tipoDocumentoEntrega.getIdTipoDocumentoEntrega().longValue() != id ){
			    			erro = true;
			    		}
			    		i++;
		    		}
	    		}
	    	} else {
	    	// Se for um registro novo, deve sempre verificar se o tpDocumentoCobranca ja existe em algum registro
		    	lista = ((TipoDocumentoEntregaService)defaultService).find(mapa);
		    	if (lista.size() > 0) 
		    		erro = true;
	    	}
    	}


    	// Se der erro
    	if (erro){
    		throw new BusinessException("LMS-09034");
    	}
    	
    	
    	/*
    	 * -	Caso n�o tenha um documento com o mesmo tipo grava o registro
    	 * */
    	TipoDocumentoEntrega bean = new TipoDocumentoEntrega();
    							bean.setIdTipoDocumentoEntrega(parameters.getLong("idTipoDocumentoEntrega"));
    							bean.setDsTipoDocumentoEntrega(parameters.getVarcharI18n("dsTipoDocumentoEntrega"));
    							bean.setTpSituacao(parameters.getDomainValue("tpSituacao"));
    							bean.setTpDocumentoCobranca(parameters.getDomainValue("tpDocumentoCobranca"));
    	return ((TipoDocumentoEntregaService)defaultService).store(bean);
    }
}
