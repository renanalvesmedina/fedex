package com.mercurio.lms.contasreceber.report;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapArrayDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.contasreceber.model.MotivoOcorrencia;
import com.mercurio.lms.contasreceber.model.service.BloqueioFaturamentoService;
import com.mercurio.lms.contasreceber.model.service.MotivoOcorrenciaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

public class ExportarBloqueioFaturamentoService extends ReportServiceSupport {
	BloqueioFaturamentoService bloqueioFaturamentoService;
	FilialService filialService;
	UsuarioLMSService usuarioBloqueioService;
	UsuarioLMSService usuarioDesbloqueioService;
	MotivoOcorrenciaService motivoOcorrenciaService;
	ClienteService clienteService;



	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {

		List bloqueios = bloqueioFaturamentoService.find(parameters);

		SqlTemplate sql = this.createSqlTemplate();
		sql.addFilterSummary("documentoServico", parameters.get("idDoctoServico"));

		if (parameters.get("idCliente") != null) {
			Cliente cliente = clienteService.findById((Long)parameters.get("idCliente"));
			sql.addFilterSummary("cliente", cliente.getPessoa().getNmPessoa());
		}

		if (parameters.get("idFilialCobranca") != null) {
			Filial filial = filialService.findById((Long)parameters.get("idFilialCobranca"));
			sql.addFilterSummary("filial", filial.getSgFilial());
		}

		if (parameters.get("idUsuarioBloqueio") != null) {
			UsuarioLMS usuario = usuarioBloqueioService.findById((Long)parameters.get("idUsuarioBloqueio"));
			sql.addFilterSummary("usuarioBloqueio", usuario.getUsuarioADSM().getNmUsuario());
		}

		if (parameters.get("idUsuarioDesbloqueio") != null) {
			UsuarioLMS usuario = usuarioDesbloqueioService.findById((Long)parameters.get("idUsuarioDesbloqueio"));
			sql.addFilterSummary("usuarioDesbloqueio", usuario.getUsuarioADSM().getNmUsuario());
		}

		if (parameters.get("idMotivoOcorrencia") != null) {
			MotivoOcorrencia motivo = motivoOcorrenciaService.findById((Long)parameters.get("idMotivoOcorrencia"));
			sql.addFilterSummary("motivoBloqueio", motivo.getDsMotivoOcorrencia());
		}

		if (parameters.get("dhInicioPrevisao") != null) {
			sql.addFilterSummary("inicioPrevisao", parameters.get("dhInicioPrevisao"));
		}

		if (parameters.get("dhFimPrevisao") != null) {
			sql.addFilterSummary("fimPrevisao", parameters.get("dhFimPrevisao"));
		}

		if (parameters.get("dhInicioBloqueio") != null) {
			sql.addFilterSummary("inicioBloqueio", parameters.get("dhInicioBloqueio"));
		}

		if (parameters.get("dhFimBloqueio") != null) {
			sql.addFilterSummary("fimBloqueio", parameters.get("dhFimBloqueio"));
		}

		if (parameters.get("dhInicioDesbloqueio") != null) {
			sql.addFilterSummary("inicioDesbloqueio", parameters.get("dhInicioDesbloqueio"));
		}

		if (parameters.get("dhFimDesbloqueio") != null) {
			sql.addFilterSummary("fimDesbloqueio", parameters.get("dhFimDesbloqueio"));
		}

		parameters.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
		parameters.put("parametrosPesquisa", sql.getFilterSummary());

		return createReportDataObject(new JRMapArrayDataSource(bloqueios.toArray()), parameters);
	}


	public void setUsuarioBloqueioService(UsuarioLMSService usuarioBloqueioService) {
		this.usuarioBloqueioService = usuarioBloqueioService;
	}


	public void setBloqueioFaturamentoService(BloqueioFaturamentoService bloqueioFaturamentoService) {
		this.bloqueioFaturamentoService = bloqueioFaturamentoService;
	}


	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}


	public void setUsuarioDesbloqueioService(UsuarioLMSService usuarioDesbloqueioService) {
		this.usuarioDesbloqueioService = usuarioDesbloqueioService;
	}


	public void setMotivoOcorrenciaService(MotivoOcorrenciaService motivoOcorrenciaService) {
		this.motivoOcorrenciaService = motivoOcorrenciaService;
	}


	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
}
