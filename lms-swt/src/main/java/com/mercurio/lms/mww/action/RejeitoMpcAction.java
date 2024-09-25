package com.mercurio.lms.mww.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.lms.carregamento.model.CabecalhoCarregamento;
import com.mercurio.lms.carregamento.model.Carregamento;
import com.mercurio.lms.carregamento.model.HistoricoCarregamento;
import com.mercurio.lms.carregamento.model.HistoricoMpc;
import com.mercurio.lms.carregamento.model.RejeitoMpc;
import com.mercurio.lms.carregamento.model.Volume;
import com.mercurio.lms.carregamento.model.service.HistoricoMpcService;
import com.mercurio.lms.carregamento.model.service.RejeitoMpcService;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.gm.model.service.CarregamentoService;
import com.mercurio.lms.gm.model.service.HistoricoCarregamentoService;
import com.mercurio.lms.gm.model.service.VolumeService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.mww.rejeitoMpcAction"
 */
public class RejeitoMpcAction extends CrudAction {

	private RejeitoMpcService rejeitoMpcService;
	
	public RejeitoMpc findRejeitoMpcById(Long idRejeitoMpc) {
		return (RejeitoMpc) rejeitoMpcService.findById(idRejeitoMpc);
	}
	
	/**
	 * Retorna o uma lista de Rejeito Mpc de acordo com a situa��o e a abrang�ncia
	 */
	public Map<String, Object> findListaRejeitoMpcBySituacaoAbrangencia(Map param){
		Map<String,Object> mapRoot = new HashMap<String, Object>();
		List<Map<String,Object>> rejeitoMpc = rejeitoMpcService.findRejeitoMpcBySituacaoAbrangencia(param);
		
		mapRoot.put("listaRejeitoMpc", rejeitoMpc);
	
		return mapRoot;
	}
	
	/**
	 * M�todo que deve ser chamado para iniciar o armazenado das informa��es de rejei��o de mpc 
	 *   quando REJEITO_MPC.tp_abrangencia = "M", ou seja "MAPA".
	 * Referente a demanda LMS-2788
	 * 
	 * @param map vindo da tela de rejeito mpc
	 */
	public Map<String, Object> executeAbrangenciaMapa(Map<String, Object> map) {		
		return rejeitoMpcService.executeAbrangenciaMapa(map);
	}
	
	/**
	 * M�todo que deve ser chamado para iniciar o armazenado das informa��es de rejei��o de mpc 
	 *   quando REJEITO_MPC.tp_abrangencia = "V", ou seja "VOLUME".
	 * Referente a demanda LMS-2788
	 * 
	 * @param map vindo da tela de rejeito mpc
	 */
	public Map<String,Object> executeAbrangenciaVolume(Map<String, Object> map) {
		return rejeitoMpcService.executeAbrangenciaVolume(map);		
	}
	
	public Map<String, Object> findExigeAutirizacao(Long idRejeitoMpc) {
		Map<String,Object> map = new HashMap<String, Object>();
		
		boolean exigeAutorizacao = rejeitoMpcService.findExigeAutirizacao(idRejeitoMpc);
		
		map.put("ExigeAutorizacao", exigeAutorizacao);
		
		return map;
	}
	
	/// SETTER ///
	public void setRejeitoMpcService(RejeitoMpcService rejeitoMpcService) {
		this.rejeitoMpcService = rejeitoMpcService;
	}

	public RejeitoMpcService getRejeitoMpcService() {
		return rejeitoMpcService;
	}	
}
