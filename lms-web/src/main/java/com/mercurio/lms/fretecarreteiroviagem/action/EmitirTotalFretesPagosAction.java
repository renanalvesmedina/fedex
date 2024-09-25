package com.mercurio.lms.fretecarreteiroviagem.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.fretecarreteiroviagem.report.EmitirTotalFretesPagosService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.fretecarreteiroviagem.emitirTotalFretesPagosAction"
 */

public class EmitirTotalFretesPagosAction extends ReportActionSupport {
	private EmitirTotalFretesPagosService emitirTotalFretesPagosService;
	private ConfiguracoesFacade configuracoesFacade;
	private MoedaPaisService moedaPaisService;
	
	public Map findDadosUsuario(){
		Filial f = SessionUtils.getFilialSessao();
		Map m = new HashMap();
		m.put("idFilial", f.getIdFilial());
		m.put("sgFilial", f.getSgFilial());
		m.put("nmFantasia", f.getPessoa().getNmFantasia());
		m.putAll(findMoedaUsuario());
		
		return m;
	}	
	
	public Long findIdPaisSessao(TypedFlatMap map){
		Pais p = SessionUtils.getPaisSessao();
		return p.getIdPais();
	}
	
	
	/**
	 * Retorna valores da combo de moeda a partir do pa�s do usu�rio logado.
	 * @return
	 */
	public List findMoedaByPais() {		
		Pais p = SessionUtils.getPaisSessao();
		return configuracoesFacade.getMoedasPais(p.getIdPais(), Boolean.TRUE);
	}
	
	public Map findMoedaUsuario() {		
		Pais p = SessionUtils.getPaisSessao();
		MoedaPais mp = moedaPaisService.findMoedaPaisMaisUtilizada(p.getIdPais());	
		Map m = new HashMap();
		m.put("idMoedaPais",mp.getIdMoedaPais());
		m.put("idMoeda",mp.getMoeda().getIdMoeda());
		m.put("dsSimbolo", mp.getMoeda().getDsSimbolo());
		m.put("siglaSimbolo", mp.getMoeda().getSiglaSimbolo());
		return m;
	}
	
	
	
	public void setEmitirTotalFretesPagosService(EmitirTotalFretesPagosService emitirTotalFretesPagosService) {
		this.reportServiceSupport = emitirTotalFretesPagosService;
	}

	public EmitirTotalFretesPagosService getEmitirTotalFretesPagosService() {
		return emitirTotalFretesPagosService;
	}


	/**
	 * @param configuracoesFacade The configuracoesFacade to set.
	 */
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	/**
	 * @param moedaPaisService The moedaPaisService to set.
	 */
	public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
	}


}
