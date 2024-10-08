package com.mercurio.lms.workflow.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.workflow.report.EmitirTempoMedioAprovacaoAprovadorService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.workflow.emitirTempoMedioAprovacaoAprovadorAction"
 */

public class EmitirTempoMedioAprovacaoAprovadorAction extends ReportActionSupport {
	
	private UsuarioService usuarioService;
	private FilialService filialService;
	
	/**
	 * Busca a lista de filiais para a lookup de filial
	 * @param map Crit�rios de pesquisa como sgFilial
	 * @return Lista de filiais
	 */
	public List findLookupFilial(Map map){
		return filialService.findLookupBySgFilial(map);
	}
	
	/**
	 * Busca os usu�rios para a lookup de funcion�rio
	 * @param tfm Crit�rios de pesquisa como usuario.idUsuario e outros dados como usuario.nmUsuario
	 * @return Lista de funcion�rios
	 */
	public List findLookupUsuarioFuncionario(TypedFlatMap tfm){
        return usuarioService.findLookupUsuarioFuncionario(tfm.getLong("idUsuario"), tfm.getString("nrMatricula"), null, null, null, null, true);
	}
	
	public void setEmitirTempoMedioAprovacaoAprovadorService(EmitirTempoMedioAprovacaoAprovadorService service){
		this.reportServiceSupport = service;
	}
	
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
}
