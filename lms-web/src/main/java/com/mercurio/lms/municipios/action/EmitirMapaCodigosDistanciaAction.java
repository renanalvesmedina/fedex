package com.mercurio.lms.municipios.action;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.municipios.report.EmitirMCDService;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * servi�o.
 * 
 * @spring.bean id="lms.municipios.emitirMapaCodigosDistanciaAction"
 */
public class EmitirMapaCodigosDistanciaAction extends ReportActionSupport {
	private EmitirMCDService emitirMCDService;
	
	/**
	 * Caso seja uma exporta��o para excel, chama o arquivo com o mesmo + o sufixo Excel
	 * Ex: emitirRelatorioIndenizacoesExcel.jasper
	 */
	public File execute(TypedFlatMap parameters) throws Exception {

		String reportName = "com/mercurio/lms/municipios/report/emitirMapaCodigosDistancia.jasper";
		
		if (parameters.get("tpFormatoRelatorio").equals("xls") && reportName.lastIndexOf("Excel.jasper") == -1) { // Se nao tiver c sufixo Excel, e For xsl, coloca o sufixo
			reportName = reportName.substring(0, reportName.lastIndexOf('.')) + "Excel.jasper";						
		} else if (!parameters.get("tpFormatoRelatorio").equals("xls") && reportName.lastIndexOf("Excel.jasper") != -1) { // Se tiver c sufixo Excel, e N�o for xsl, retira o sufixo
			reportName = reportName.substring(0, reportName.lastIndexOf("Excel.jasper")) + ".jasper";
	} 
		getReportServiceSupport().setReportName(reportName);

		return super.execute(parameters);
	}
	
	public EmitirMCDService getEmitirMCDService() {
		return emitirMCDService;
	} 

	public void setEmitirMCDService(EmitirMCDService emitirMCDService) {
		this.reportServiceSupport = emitirMCDService;
	}
	
	private FilialService filialService;
	private ServicoService servicoService;
	private MunicipioFilialService municipioFilialService;
	private UnidadeFederativaService unidadeFederativaService;
	private RegionalService regionalService;
	private List<Regional> regionais;

	public List findComboServico(TypedFlatMap criteria) {
		return servicoService.find(criteria);
	}
	
	public List findLookupFilial(Map criteria) {
		return filialService.findLookup(criteria);
	}
	
	public List findLookupMunicipio(Map criteria) {
		return municipioFilialService.findLookup(criteria);
	}
	
	public List findLookupUf(Map criteria) {
		return unidadeFederativaService.findLookup(criteria);
	}
	
	public List<Regional> findRegionaisValidas() {
		if (regionais == null) {
			regionais = regionalService.findRegionaisValidas();
		}
		return regionais;
	}
	
	public TypedFlatMap getDataAtual() {
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("dtVigencia", JTDateTimeUtils.getDataAtual());
		return retorno;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public MunicipioFilialService getMunicipioFilialService() {
		return municipioFilialService;
	}

	public ServicoService getServicoService() {
		return servicoService;
	}

	public UnidadeFederativaService getUnidadeFederativaService() {
		return unidadeFederativaService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}

	public void setRegionalService(RegionalService regionalFilialService) {
		this.regionalService = regionalFilialService;
}

	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}

	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
}
