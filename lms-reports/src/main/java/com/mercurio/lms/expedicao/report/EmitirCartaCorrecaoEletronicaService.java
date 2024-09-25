package com.mercurio.lms.expedicao.report;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapArrayDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.service.CartaCorrecaoEletronicaService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;

/**
 * 
 * @author CaetanoM
 * 
 */

public class EmitirCartaCorrecaoEletronicaService extends ReportServiceSupport {

	private CartaCorrecaoEletronicaService cartaCorrecaoEletronicaService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private MonitoramentoDocEletronico monitoramentoDocEletronico;
	
	@Override
	@SuppressWarnings("rawtypes")
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap params = (TypedFlatMap) parameters;
		
		MonitoramentoDocEletronico monitoramentoDocEletronico =
				monitoramentoDocEletronicoService.findById(params.getLong("idMonitoramentoDocEletronico"));
		
		List<TypedFlatMap> aprovadas =
				cartaCorrecaoEletronicaService.findAprovadasByIdDoctoServico(params.getLong("idDoctoServico"));
		
		DoctoServico doctoServico = monitoramentoDocEletronico.getDoctoServico();
		Filial filialOrigem = doctoServico.getFilialByIdFilialOrigem();
		String numCte = filialOrigem.getSgFilial() + " " + doctoServico.getNrDoctoServico();
		String cnpj = FormatUtils.formatCNPJ(filialOrigem.getPessoa().getNrIdentificacao());
		Long nrProtocolo = params.getLong("nrProtocolo");
		
		params.put("chaveCte", monitoramentoDocEletronico.getNrChave());
		params.put("numCte", numCte);
		params.put("nrProtocolo", Long.toString(nrProtocolo));
		params.put("cnpjFilial", cnpj);
		params.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
		return createReportDataObject(new JRMapArrayDataSource(aprovadas.toArray()), params);
	}

	public CartaCorrecaoEletronicaService getCartaCorrecaoEletronicaService() {
		return cartaCorrecaoEletronicaService;
	}

	public void setCartaCorrecaoEletronicaService(
			CartaCorrecaoEletronicaService cartaCorrecaoEletronicaService) {
		this.cartaCorrecaoEletronicaService = cartaCorrecaoEletronicaService;
	}

	public MonitoramentoDocEletronicoService getMonitoramentoDocEletronicoService() {
		return monitoramentoDocEletronicoService;
	}

	public void setMonitoramentoDocEletronicoService(
			MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public MonitoramentoDocEletronico getMonitoramentoDocEletronico() {
		return monitoramentoDocEletronico;
	}

	public void setMonitoramentoDocEletronico(MonitoramentoDocEletronico monitoramentoDocEletronico) {
		this.monitoramentoDocEletronico = monitoramentoDocEletronico;
	}
}
