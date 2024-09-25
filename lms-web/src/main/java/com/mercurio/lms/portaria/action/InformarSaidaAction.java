package com.mercurio.lms.portaria.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.LacreControleCargaService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.portaria.model.service.ConfiguracaoAuditoriaService;
import com.mercurio.lms.portaria.model.service.ControleEntSaidaTerceiroService;
import com.mercurio.lms.portaria.model.service.ControleQuilometragemService;
import com.mercurio.lms.portaria.model.service.InformarSaidaService;
import com.mercurio.lms.portaria.model.service.NewInformarSaidaService;
import com.mercurio.lms.portaria.model.service.OrdemSaidaService;


/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.portaria.informarSaidaAction"
 */

public class InformarSaidaAction extends CrudAction {
	
	private InformarSaidaService informarSaidaService;
	private FilialService filialService;
	private LacreControleCargaService lacreControleCargaService;
	private ControleCargaService controleCargaService;
	private MeioTransporteService meioTransporteService;
	private ControleQuilometragemService controleQuilometragemService;
	private OrdemSaidaService ordemSaidaService;
	private ControleEntSaidaTerceiroService controleEntSaidaTerceiroService;
	private ConfiguracaoAuditoriaService configuracaoAuditoriaService;
	private NewInformarSaidaService newInformarSaidaService;

	public Map findDadosSaida(TypedFlatMap parameters) {
		return newInformarSaidaService.findDados(parameters);
	}

	public String executeFindMeioTransporteAuditoria(TypedFlatMap parameters){
		return getConfiguracaoAuditoriaService().executeVerificarMeioTransporteParaAuditoria(parameters.getLong("idFilial"), parameters.getLong("idMeioTransporte"));
	}
	
	
	public List findLacreControleCarga(Long idControleCarga){		
		return getLacreControleCargaService().findByControleCarga(idControleCarga);						
	}

	public void executeConfirmaSaida(TypedFlatMap parameters) {
		newInformarSaidaService.executeSalvar(parameters);
	}

	/**
	 * @return Returns the informarSaidaService.
	 */
	public InformarSaidaService getInformarSaidaService() {
		return informarSaidaService;
	}

	/**
	 * @param informarSaidaService The informarSaidaService to set.
	 */
	public void setInformarSaidaService(InformarSaidaService informarSaidaService) {
		this.informarSaidaService = informarSaidaService;
	}

	/**
	 * @return Returns the filialService.
	 */
	public FilialService getFilialService() {
		return filialService;
	}

	/**
	 * @param filialService The filialService to set.
	 */
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	/**
	 * @return Returns the lacreControleCargaService.
	 */
	public LacreControleCargaService getLacreControleCargaService() {
		return lacreControleCargaService;
	}

	/**
	 * @param lacreControleCargaService The lacreControleCargaService to set.
	 */
	public void setLacreControleCargaService(
			LacreControleCargaService lacreControleCargaService) {
		this.lacreControleCargaService = lacreControleCargaService;
	}

	/**
	 * @return Returns the controleCargaService.
	 */
	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}

	/**
	 * @param controleCargaService The controleCargaService to set.
	 */
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	/**
	 * @return Returns the meioTransporteService.
	 */
	public MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}

	/**
	 * @param meioTransporteService The meioTransporteService to set.
	 */
	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}


	/**
	 * @return Returns the controleQuilometragemService.
	 */
	public ControleQuilometragemService getControleQuilometragemService() {
		return controleQuilometragemService;
	}


	/**
	 * @param controleQuilometragemService The controleQuilometragemService to set.
	 */
	public void setControleQuilometragemService(
			ControleQuilometragemService controleQuilometragemService) {
		this.controleQuilometragemService = controleQuilometragemService;
	}


	/**
	 * @return Returns the ordemSaidaService.
	 */
	public OrdemSaidaService getOrdemSaidaService() {
		return ordemSaidaService;
	}


	/**
	 * @param ordemSaidaService The ordemSaidaService to set.
	 */
	public void setOrdemSaidaService(OrdemSaidaService ordemSaidaService) {
		this.ordemSaidaService = ordemSaidaService;
	}


	/**
	 * @return Returns the controleEntSaidaTerceiroService.
	 */
	public ControleEntSaidaTerceiroService getControleEntSaidaTerceiroService() {
		return controleEntSaidaTerceiroService;
	}


	/**
	 * @param controleEntSaidaTerceiroService The controleEntSaidaTerceiroService to set.
	 */
	public void setControleEntSaidaTerceiroService(
			ControleEntSaidaTerceiroService controleEntSaidaTerceiroService) {
		this.controleEntSaidaTerceiroService = controleEntSaidaTerceiroService;
	}

	/**
	 * @return Returns the configuracaoAuditoriaService.
	 */
	public ConfiguracaoAuditoriaService getConfiguracaoAuditoriaService() {
		return configuracaoAuditoriaService;
	}

	/**
	 * @param configuracaoAuditoriaService The configuracaoAuditoriaService to set.
	 */
	public void setConfiguracaoAuditoriaService(
			ConfiguracaoAuditoriaService configuracaoAuditoriaService) {
		this.configuracaoAuditoriaService = configuracaoAuditoriaService;
	}

	public void setNewInformarSaidaService(NewInformarSaidaService newInformarSaidaService) {
		this.newInformarSaidaService = newInformarSaidaService;
	}

}
