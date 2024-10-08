package com.mercurio.lms.entrega.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.entrega.report.EmitirEficienciaEntregaDataService;
import com.mercurio.lms.entrega.report.EmitirEficienciaEntregaMeioTransporteService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.entrega.emitirEficienciaEntregasAction"
 */

public class EmitirEficienciaEntregasAction extends ReportActionSupport {
	
	private MoedaPaisService moedaPaisService;
	private FilialService filialService;
	private MeioTransporteService meioTransporteService;
	
	private EmitirEficienciaEntregaDataService eficienciaEntregaDataService;
	private EmitirEficienciaEntregaMeioTransporteService emitirEficienciaEntregaMeioTransporteService;
	
	@Override
	public java.io.File execute(TypedFlatMap parameters) throws Exception {
		String tpOpcao = parameters.getString("tpOpcao");
		if (tpOpcao.equals("D")) {
			this.reportServiceSupport = eficienciaEntregaDataService;
		} else if (tpOpcao.equals("M")) {
			this.reportServiceSupport = emitirEficienciaEntregaMeioTransporteService;
		}
		return super.execute(parameters);
	}
	
	/**
	 * M�todo respons�vel por requisitar informa��es do usu�rio logado.
	 * @return
	 */
	public TypedFlatMap findInfoUsuarioLogado() {
		Filial f = SessionUtils.getFilialSessao();
		
		TypedFlatMap retorno = new TypedFlatMap();

		retorno.put("filial.idFilial",f.getIdFilial());
		retorno.put("filial.sgFilial",f.getSgFilial());
		retorno.put("filial.pessoa.nmFantasia",f.getPessoa().getNmFantasia());
		
		return retorno;
	}
	
	/**
	 * Find da combo de moeda a partir do pa�s do usu�rio logado.
	 * @return
	 */
	public List findComboMoedaPais() {
		Pais p = SessionUtils.getPaisSessao();
		List moedaPaises = moedaPaisService.findByPais(p.getIdPais(),Boolean.TRUE);
		
		List retorno = new ArrayList(moedaPaises.size()); 
		
		Iterator iMoedaPaises = moedaPaises.iterator();
		while (iMoedaPaises.hasNext()) {
			MoedaPais mp = (MoedaPais)iMoedaPaises.next();
			Moeda m = mp.getMoeda();
			TypedFlatMap map = new TypedFlatMap();
			map.put("idMoedaPais",mp.getIdMoedaPais());
			map.put("moeda.siglaSimbolo",m.getSiglaSimbolo());
			map.put("moeda.dsSimbolo",m.getDsSimbolo());
			map.put("moeda.idMoeda",m.getIdMoeda());
			map.put("pais.idPais",mp.getPais().getIdPais());
			retorno.add(map);
		}
		
		return retorno;
	}
	
	/**
     * findLookup de filiais.
     * @param criteria
     * @return
     */
    public List findLookupFilial(Map criteria) {
		return filialService.findLookupFilial(criteria);
	}
    	
	/**
     * findLookup de Meio de transporte.
     * @param criteria
     * @return
     */
    public List findLookupMeioTransporte(Map criteria) {
    	return meioTransporteService.findLookup(criteria);
    }
    

	
	public void setEficienciaEntregaDataService(
			EmitirEficienciaEntregaDataService eficienciaEntregaDataService) {
		this.eficienciaEntregaDataService = eficienciaEntregaDataService;
	}

	public void setEmitirEficienciaEntregaMeioTransporteService(
			EmitirEficienciaEntregaMeioTransporteService emitirEficienciaEntregaMeioTransporteService) {
		this.emitirEficienciaEntregaMeioTransporteService = emitirEficienciaEntregaMeioTransporteService;
	}


	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}


	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}


	public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
	}
	
}
