package com.mercurio.lms.contasreceber.action;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.contasreceber.model.service.CedenteService;
import com.mercurio.lms.contasreceber.report.EmitirBoletosRomaneiosVencidosVencerService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.emitirBoletosRomaneiosVencidosVencerAction"
 */

public class EmitirBoletosRomaneiosVencidosVencerAction extends ReportActionSupport {

	private CedenteService cedenteService;
	private ClienteService clienteService;
	private FilialService filialService;
	private ConfiguracoesFacade configuracoesFacade;
	
	public void setCedenteService(CedenteService cedenteService) {
		this.cedenteService = cedenteService;
	}
	
	public void setEmitirBoletosRomaneiosVencidosVencerService(EmitirBoletosRomaneiosVencidosVencerService emitirBoletosRomaneiosVencidosVencerService) {
		this.reportServiceSupport = emitirBoletosRomaneiosVencidosVencerService;
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}	
	
	
	public List findComboCedentes(TypedFlatMap criteria){
		return this.cedenteService.findComboValues(criteria);
	}
	
	/**
     * Busca a filial do usuario logado
     * @return Filial
     */
    public Filial findFilialUsuario(){
    	return SessionUtils.getFilialSessao();
    } 
	
    /**
     * Busca a moeda do usuario logado
     * @return Moeda
     */
    public Moeda findMoedaUsuario(){
    	return SessionUtils.getMoedaSessao();
    } 
	
	/**
	 * Retorna a Data atual e a Data atual mais 40 anos
	 *   
	 * @author Diego Umpierre
	 * @return Map com os valores das datas
	 */
	public TypedFlatMap findDatas(){
		
		YearMonthDay periodoInicial = JTDateTimeUtils.convertDataStringToYearMonthDay("01012000");
		YearMonthDay periodoFinal = JTDateTimeUtils.getDataAtual();
		
		// adicionando 40 anos a data atual
		periodoFinal = periodoFinal.plusYears(40);
		
		TypedFlatMap map = new TypedFlatMap();
		
		map.put("periodoInicial",periodoInicial);
		map.put("periodoFinal",periodoFinal);
		
		
		return map;
		
	}
	
	public List findMoedaPaisCombo() {
		Pais p = (Pais)SessionContext.get(SessionKey.PAIS_KEY);
		return configuracoesFacade.getMoeda(p.getIdPais(),Boolean.TRUE);
	}
	
	
	public List findLookupFilial(TypedFlatMap criteria) {
		return filialService.findLookup(criteria);
	}
	
	/**
	 * Busca os cliente de acordo com o n�mero de identifica��o informado
	 * @param criteria cliente.pessoa.nrIdentificacao N�mero de identifica��o do cliente
	 * @return Lista de clientes
	 */
	public List findLookupClientes(TypedFlatMap criteria){
		return clienteService.findLookupSimplificadoAbaCad(
				criteria.getString("pessoa.nrIdentificacao"),
				null,
				criteria.getString("tpSituacao"),
				null);
	}

	/**
	 * Busca a filial do usu�rio e verifica se a filial do usu�rio � a Matriz
	 * @param tfm Crit�rios de pesquisa
	 * @return TypedFlatMap com dados de filial e a informa��o se a filial � ou n�o a Matriz
	 */
	public TypedFlatMap findUserMatriz(TypedFlatMap tfm){
		tfm = new TypedFlatMap();
		// Seta no Map se a filial do usu�rio logado � Matriz
		tfm.put("filialUserIsMatriz", SessionUtils.isFilialSessaoMatriz());
		return tfm;
	}
}