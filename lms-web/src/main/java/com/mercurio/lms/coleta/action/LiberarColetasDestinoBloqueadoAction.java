package com.mercurio.lms.coleta.action;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.coleta.model.service.OcorrenciaColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/** 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.coleta.liberarColetasDestinoBloqueadoAction"
 */
public class LiberarColetasDestinoBloqueadoAction extends CrudAction {
	private UsuarioService usuarioService;
	private OcorrenciaColetaService ocorrenciaColetaService;	
	
	public void setService(PedidoColetaService pedidoColetaService) {
		this.defaultService = pedidoColetaService;		
	}

    public List findLookupUsuarioFuncionario(TypedFlatMap tfm){
    	String nrMatricula = tfm.getString("nrMatricula");
    	if (nrMatricula.length() < 9) {
    		nrMatricula = FormatUtils.fillNumberWithZero(nrMatricula, 9);
		}  
    	
    	return this.getUsuarioService().findLookupUsuarioFuncionario(tfm.getLong("idUsuario"), 
																	 nrMatricula, 
																	 tfm.getLong("filialSessao.idFilial"), 
																	 null, null, null, true);
    }
	
	public List findOcorrenciaColeta(Map criteria) {
		List listOcorrenciaColeta = this.getOcorrenciaColetaService().findOcorrenciaColetaByTpEventoColeta("LI");
    	Collections.sort(listOcorrenciaColeta, new Comparator() {
			public int compare(Object obj1, Object obj2) {
				OcorrenciaColeta ocorrenciaColeta1 = (OcorrenciaColeta)obj1;
				OcorrenciaColeta ocorrenciaColeta2 = (OcorrenciaColeta)obj2;
        		return ocorrenciaColeta1.getDsDescricaoCompleta().toString().compareTo(ocorrenciaColeta2.getDsDescricaoCompleta().toString());  		
			}    		
    	});

		return listOcorrenciaColeta;
	}	
	
	/**
	 * Pega o usuario logado na sess�o com sua moeda e sua respectiva filial. 
	 */
	public TypedFlatMap getDadosSessao() {
		TypedFlatMap map = new TypedFlatMap();
	
		Filial filial = SessionUtils.getFilialSessao();		
		map.put("idFilialSessao", filial.getIdFilial());
		map.put("sgFilialSessao", filial.getSgFilial());
		map.put("nmFilialSessao", filial.getPessoa().getNmFantasia());
				
		return map;
	}	
	
	
	public OcorrenciaColetaService getOcorrenciaColetaService() {
		return ocorrenciaColetaService;
	}
	public void setOcorrenciaColetaService(OcorrenciaColetaService ocorrenciaColetaService) {
		this.ocorrenciaColetaService = ocorrenciaColetaService;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
}
