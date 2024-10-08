package com.mercurio.lms.carregamento.swt.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.carregamento.model.service.DispositivoUnitizacaoService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.portaria.model.service.MacroZonaService;
import com.mercurio.lms.util.FormatUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.carregamento.swt.emitirCodeBarraIdentificadorAction"
 */

public class EmitirCodeBarraIdentificadorAction extends CrudAction {

	private DispositivoUnitizacaoService dispositivoUnitizacaoService;
	private MeioTransporteService meioTransporteService;
	private UsuarioService usuarioService;
	private MacroZonaService macroZonaService;
	
	public void setDispositivoUnitizacaoService(DispositivoUnitizacaoService dispositivoUnitizacaoService) {
		this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
	}	
	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public void setMacroZonaService(MacroZonaService macroZonaService) {
		this.macroZonaService = macroZonaService;
	}

	public List findLookupDispositivoUnitizacao(Map criteria){
		criteria.put("nrIdentificacao", criteria.get("nrIdentificacaoEmpresa"));

		return dispositivoUnitizacaoService.findLookup(criteria);
	}
	
    public List findLookupMeioTransporte(Map criteria) {
    	return meioTransporteService.findLookup(criteria);
    }
    
    public void executeVerificarIsCavaloTrator(Map criteria) {
    	Long idMeioTransporte = Long.parseLong(criteria.get("idMeioTransporte").toString());
    	if(meioTransporteService.findMeioTransporteIsCavaloTrator(idMeioTransporte))
    		throw new BusinessException("LMS-26107");
    }
    
	public List findLookupUsuarioFuncionario(Map criteria){
		return usuarioService.findLookupUsuarioFuncionario(
			null,
			FormatUtils.fillNumberWithZero(((Integer)criteria.get("nrMatricula")).toString(), 9),
			(Long)criteria.get("idFilial"),
			null,
			null,
			null,
			true
		);
	}

	public List findLookupMacroZona(Map criteria){
		criteria.put("nrCodigoBarras", criteria.get("nrCodigoBarrasMacroZona"));
		return macroZonaService.findLookup(criteria);
	}
}