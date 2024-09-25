package com.mercurio.lms.seguros.model.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;
import com.mercurio.lms.seguros.model.dao.RelatorioSegurosDAO;
import com.mercurio.lms.util.FormatUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.seguros.RelatorioSegurosService"
 */
public class RelatorioSegurosService {

	private RelatorioSegurosDAO relatorioSegurosDAO;
	
	private static final String REPORT_NAME = "RELATORIO_SEGUROS";
	
	public File executeReportCSV(TypedFlatMap parameters, File generateOutputDir) {
		
		List<Map<String, Object>> list = this.relatorioSegurosDAO.findDadosReport(parameters);
		
		if(list == null || list.isEmpty()) {
			throw new BusinessException("emptyReport");
		}
		
		return generateReportFile(parameters, list, generateOutputDir);
	}
	
	private File generateReportFile(TypedFlatMap parameters,
			List<Map<String, Object>> listaParaCsv, File reportOutputDir) {
		
		try {
			File reportFile = File.createTempFile("report", ".csv",
					reportOutputDir);
			FileOutputStream out = new FileOutputStream(reportFile);

			// Título
			out.write("Relatório de Seguros\n".getBytes());
			
			// Filtros Utilizados
			out.write(getStringFiltrosProcessoSinistroUtilizados(parameters).getBytes());
			out.write(getStringFiltrosRIMUtilizados(parameters).getBytes());
			out.write(getStringFiltrosDoctoServicoUtilizados(parameters).getBytes());
			out.write("\n".getBytes());
			
			// Detalhes
			if (listaParaCsv != null && !listaParaCsv.isEmpty()) {
				Map<String, Object> mapCsv = FranqueadoUtils.convertMappedListToCsv(REPORT_NAME, listaParaCsv, ";");
				out.write(mapCsv.get(REPORT_NAME).toString().getBytes());
			}

			out.flush();
			out.close();
			return reportFile;
		} catch (IOException e) {
			throw new InfrastructureException(e);
		}
	}
	
	private String getStringFiltrosProcessoSinistroUtilizados(TypedFlatMap parameters) {
		
		StringBuilder filtros = new StringBuilder();
		
		// Processo de sinistro
		if(!parameters.getString("nrProcessoSinistro").isEmpty()) {
			filtros.append("Processo de sinistro:;").append(parameters.getString("nrProcessoSinistro")).append("\n");
		}
		
		// Manifesto
		if(parameters.getLong("manifesto.idManifesto") != null) {
			filtros.append("Manifesto:;")
			.append(parameters.getString("manifesto.tpManifestoHidden")).append(" ")
			.append(parameters.getString("manifesto.filialByIdFilialOrigem.pessoa.sgFilialHidden")).append(" ")
			.append(parameters.getLong("manifesto.nrManifesto")).append("\n");
		}
		
		// Data sinistro
		
		if(parameters.getYearMonthDay("dhSinistroInicial") != null || parameters.getYearMonthDay("dhSinistroFinal") != null) {
			filtros.append("Data sinistro:;");
			if(parameters.getYearMonthDay("dhSinistroInicial") != null) {
				filtros.append(parameters.getYearMonthDay("dhSinistroInicial").toString("dd/MM/yyyy")).append(";");
			}
			if(parameters.getYearMonthDay("dhSinistroFinal") != null) {
				filtros.append("até;")
				.append(parameters.getYearMonthDay("dhSinistroFinal").toString("dd/MM/yyyy"));
			}
			filtros.append("\n");
		}
		
		// Tipo de sinistro
		if(parameters.getLong("tipoSinistro.idTipoSinistro") != null) {
			filtros.append("Tipo de sinistro:;").append(parameters.getString("dsTipoSinistro")).append("\n");
		}
		
		// Rodovia
		if(parameters.getLong("rodovia.idRodovia") != null) {
			filtros.append("Rodovia:;").append(parameters.getString("sgRodoviaHidden")).append(";")
			.append(parameters.getString("rodovia.dsRodovia")).append("\n");
		}
		
		// Filial sinistro
		if(parameters.getLong("filialSinistro.idFilial") != null) {
			filtros.append("Filial sinistro:;").append(parameters.getString("sgFilialSinisitro")).append(";")
			.append(parameters.getString("filialSinistro.pessoa.nmFantasia")).append("\n");
		}
		
		// Aeroporto
		if(parameters.getLong("aeroporto.idAeroporto") != null) {
			filtros.append("Aeroporto:;").append(parameters.getString("sgAeroportoHidden")).append(";")
			.append(parameters.getString("aeroporto.pessoa.nmPessoa")).append("\n");
		}
		
		// Situação
		if(!parameters.getString("situacaoProcessoSinistro").isEmpty()) {
			filtros.append("Situação:;").append(parameters.getString("dsSituacaoProcessoSinistro")).append("\n");
		}
		
		// Controle de carga
		if(parameters.getLong("controleCarga.idControleCarga") != null) {
			filtros.append("Controle de carga:;")
			.append(parameters.getString("controleCarga.sgFilialOrigemHidden")).append(" ")
			.append(parameters.getLong("controleCarga.nrControleCargaHidden")).append("\n");
		}
		
		// Frota/placa
		if(parameters.getLong("meioTransporteRodoviario.idMeioTransporte") != null) {
			filtros.append("Frota/placa:;").append(parameters.getString("nrFrotaHidden")).append(" ")
			.append(parameters.getString("nrIdentificadorHidden")).append("\n");
		}
		
		// Local sinistro
		if(!parameters.getString("localSinistro").isEmpty()) {
			filtros.append("Local sinistro:;").append(parameters.getString("dsLocalSinistro")).append("\n");
		}
		
		// Tipo de seguro
		if(parameters.getLong("tipoSeguro.idTipoSeguro") != null) {
			filtros.append("Tipo de seguro:;").append(parameters.getString("tipoSeguro.dsTipoSeguro")).append("\n");
		}
		
		// Município
		if(parameters.getLong("municipio.idMunicipio") != null) {
			filtros.append("Município:;").append(parameters.getString("municipio.nmMunicipioHidden")).append("\n");
		}
		
		// UF
		if(parameters.getLong("unidadeFederativa.idUnidadeFederativa") != null) {
			filtros.append("UF:;").append(parameters.getString("unidadeFederativa.sgUnidadeFederativaHidden")).append(";")
			.append(parameters.getString("unidadeFederativa.nmUnidadeFederativaHidden")).append("\n");
		}
		
		// Usuário responsável
		if(parameters.getLong("usuario.idUsuario") != null) {
			filtros.append("Usuário responsável:;").append(parameters.getString("usuarioResponsavel.nrMatriculaHidden")).append(";")
			.append(parameters.getString("usuarioResponsavel.nmUsuarioHidden")).append("\n");
		}
		
		return filtros.toString();
	}
	
	private String getStringFiltrosRIMUtilizados(TypedFlatMap parameters) {
		
		StringBuilder filtros = new StringBuilder();
		
		// Filial
		if(parameters.getLong("filial.idFilial") != null) {
			filtros.append("Filial:;").append(parameters.getString("filialRIM.sgFilial")).append(";")
			.append(parameters.getString("filialRIM.nmFilial")).append("\n");
		}
		
		// Número
		if(parameters.getInteger("nrReciboIndenizacaoInicial") != null || parameters.getInteger("nrReciboIndenizacaoFinal") != null) {
			filtros.append("Número:;");
			if(parameters.getInteger("nrReciboIndenizacaoInicial") != null) {
				filtros.append(parameters.getLong("nrReciboIndenizacaoInicial")).append(";");
			}
			if(parameters.getInteger("nrReciboIndenizacaoFinal") != null) {
				filtros.append("até;").append(parameters.getLong("nrReciboIndenizacaoFinal"));
			}
			filtros.append("\n");
		}
		
		// Tipo de indenização
		if(!parameters.getString("tpIndenizacao").isEmpty()) {
			filtros.append("Tipo de indenização:;").append(parameters.getString("dsTipoIndenizacao")).append("\n");
		}
		
		// Status
		if(!parameters.getString("tpStatusIndenizacao").isEmpty()) {
			filtros.append("Status:;").append(parameters.getString("dsTpStatusIndenizacao")).append("\n");
		}
		
		// Situação da aprovação
		if(!parameters.getString("tpSituacaoWorkFlow").isEmpty()) {
			filtros.append("Situação da aprovação:;").append(parameters.getString("dsTpSituacaoWorkFlow")).append("\n");
		}
		
		// Natureza do produto
		if(parameters.getLong("naturezaProduto.idNaturezaProduto") != null) {
			filtros.append("Natureza do produto:;").append(parameters.getString("naturezaProduto.dsNaturezaProdutoHidden")).append("\n");
		}
		
		// Forma de pagamento
		if(!parameters.getString("formaPagto").isEmpty()) {
			filtros.append("Forma de pagamento:;").append(parameters.getString("dsFormaPagto")).append("\n");
		}
		
		// Valor indenização
		if(parameters.getBigDecimal("valorIndenizacaoInicial") != null ||
				parameters.getBigDecimal("valorIndenizacaoFinal") != null) {
			filtros.append("Valor indenização:;");
			if(parameters.getBigDecimal("valorIndenizacaoInicial") != null) {
				filtros.append(formatParameterToBigDecimal(parameters, "valorIndenizacaoInicial")).append(";");
			}
			if(parameters.getBigDecimal("valorIndenizacaoFinal") != null) {
				filtros.append("até;").append(formatParameterToBigDecimal(parameters, "valorIndenizacaoFinal"));
			}
			filtros.append("\n");
		}
		
		// Filial debitada
		if(parameters.getLong("filialDebitada.idFilial") != null) {
			filtros.append("Filial debitada:;").append(parameters.getString("filialDebitada.sgFilialHidden")).append(";")
			.append(parameters.getString("filialDebitada.nmFilialHidden")).append("\n");
		}
		
		// Filial ocorrência 1
		if(parameters.getLong("filialOcorrencia1.idFilial") != null) {
			filtros.append("Filial ocorrência 1:;").append(parameters.getString("siglaFilialOcorrencia1Hidden")).append(";")
				.append(parameters.getString("nomeFilialOcorrencia1Hidden")).append("\n");
		}
		
		// Filial ocorrência 2
		if(parameters.getLong("filialOcorrencia2.idFilial") != null) {
			filtros.append("Filial ocorrência 2:;").append(parameters.getString("siglaFilialOcorrencia2Hidden")).append(";")
			.append(parameters.getString("nomeFilialOcorrencia2Hidden")).append("\n");
		}
		
		// Beneficiário
		if(parameters.getLong("beneficiario.idPessoa") != null) {
			filtros.append("Beneficiário:;").append(parameters.getString("beneficiario.nrIdentificacaoHidden")).append(";")
			.append(parameters.getString("beneficiario.nmPessoa")).append("\n");
		}
		
		// Favorecido
		if(parameters.getLong("favorecido.idPessoa") != null) {
			filtros.append("Favorecido:;").append(parameters.getString("favorecido.nrIdentificacaoHidden")).append(";")
			.append(parameters.getString("favorecido.nmPessoa")).append("\n");
		}
		
		// Data de emissão
		if(parameters.getYearMonthDay("dataInicial") != null ||
				parameters.getYearMonthDay("dataFinal") != null) {
			filtros.append("Data de emissão:;");
			if(parameters.getYearMonthDay("dataInicial") != null) {
				filtros.append(parameters.getYearMonthDay("dataInicial").toString("dd/MM/yyyy")).append(";");
			}
			if(parameters.getYearMonthDay("dataFinal") != null) {
				filtros.append("até;")
				.append(parameters.getYearMonthDay("dataFinal").toString("dd/MM/yyyy"));
			}
			filtros.append("\n");
		}
		
		// Data liberação de pgto
		if(parameters.getYearMonthDay("dataLiberacaoPgtoInicial") != null ||
				parameters.getYearMonthDay("dataLiberacaoPgtoFinal") != null) {
			filtros.append("Data liberação de pgto:;");
			if(parameters.getYearMonthDay("dataLiberacaoPgtoInicial") != null) {
				filtros.append(parameters.getYearMonthDay("dataLiberacaoPgtoInicial").toString("dd/MM/yyyy")).append(";");
			}
			if(parameters.getYearMonthDay("dataLiberacaoPgtoFinal") != null) {
				filtros.append("até;")
				.append(parameters.getYearMonthDay("dataLiberacaoPgtoFinal").toString("dd/MM/yyyy"));
			}
			filtros.append("\n");
		}
		
		// Data programada de pgto
		if(parameters.getYearMonthDay("dataProgramadaPagamentoInicial") != null ||
				parameters.getYearMonthDay("dataProgramadaPagamentoFinal") != null) {
			filtros.append("Data programada de pgto:;");
			if(parameters.getYearMonthDay("dataProgramadaPagamentoInicial") != null) {
				filtros.append(parameters.getYearMonthDay("dataProgramadaPagamentoInicial").toString("dd/MM/yyyy")).append(";");
			}
			if(parameters.getYearMonthDay("dataProgramadaPagamentoFinal") != null) {
				filtros.append("até;")
				.append(parameters.getYearMonthDay("dataProgramadaPagamentoFinal").toString("dd/MM/yyyy"));
			}
			filtros.append("\n");
		}
		
		// Data de pagamento
		if(parameters.getYearMonthDay("dataPagtoInicial") != null ||
				parameters.getYearMonthDay("dataPagtoFinal") != null) {
			filtros.append("Data de pagamento:;");
			if(parameters.getYearMonthDay("dataPagtoInicial") != null) {
				filtros.append(parameters.getYearMonthDay("dataPagtoInicial").toString("dd/MM/yyyy")).append(";");
			}
			if(parameters.getYearMonthDay("dataPagtoFinal") != null) {
				filtros.append("até;")
				.append(parameters.getYearMonthDay("dataPagtoFinal").toString("dd/MM/yyyy"));
			}
			filtros.append("\n");
		}
		
		// Data lote
		if(parameters.getYearMonthDay("dataLoteInicial") != null ||
				parameters.getYearMonthDay("dataLoteFinal") != null) {
			filtros.append("Data lote:;");
			if(parameters.getYearMonthDay("dataLoteInicial") != null) {
				filtros.append(parameters.getYearMonthDay("dataLoteInicial").toString("dd/MM/yyyy")).append(";");
			}
			if(parameters.getYearMonthDay("dataLoteFinal") != null) {
				filtros.append("até;")
				.append(parameters.getYearMonthDay("dataLoteFinal").toString("dd/MM/yyyy"));
			}
			filtros.append("\n");
		}
		
		// Número do lote
		if(parameters.getLong("nrLote") != null) {
			filtros.append("Número do lote:;").append(parameters.getLong("nrLote")).append("\n");
		}
		
		// Filial RNC
		if(parameters.getLong("filialRnc.idFilial") != null) {
			filtros.append("Filial RNC:;").append(parameters.getString("siglaFilialRncHidden")).append(";")
			.append(parameters.getString("filialRnc.pessoa.nmFantasia")).append("\n");
		}
		
		// Número RNC
		if(parameters.getInteger("numeroRncInicial") != null ||
				parameters.getInteger("numeroRncFinal") != null) {
			filtros.append("Número RNC:;");
			if(parameters.getInteger("numeroRncInicial") != null) {
				filtros.append(parameters.getInteger("numeroRncInicial")).append(";");
			}
			if(parameters.getInteger("numeroRncFinal") != null) {
				filtros.append("até;").append(parameters.getInteger("numeroRncFinal"));
			}
			filtros.append("\n");
		}
		
		// Data emissão RNC
		if(parameters.getYearMonthDay("dataEmissaoRncInicial") != null ||
				parameters.getYearMonthDay("dataEmissaoRncFinal") != null) {
			filtros.append("Data emissão RNC:;");
			if(parameters.getYearMonthDay("dataEmissaoRncInicial") != null) {
				filtros.append(parameters.getYearMonthDay("dataEmissaoRncInicial").toString("dd/MM/yyyy")).append(";");
			}
			if(parameters.getYearMonthDay("dataEmissaoRncFinal") != null) {
				filtros.append("até;")
				.append(parameters.getYearMonthDay("dataEmissaoRncFinal").toString("dd/MM/yyyy"));
			}
			filtros.append("\n");
		}
		
		// Motivo de abertura
		if(parameters.getLong("motivoAbertura.idMotivoAberturaNc") != null) {
			filtros.append("Motivo de abertura:;").append(parameters.getString("dsMotivoAberturaHidden")).append("\n");
		}
		
		// Salvados
		if(!parameters.getString("blSalvados").isEmpty()) {
			filtros.append("Salvados:;").append(parameters.getString("blSalvadosHidden")).append("\n");
		}
		
		return filtros.toString();
	}
	
	private String getStringFiltrosDoctoServicoUtilizados(TypedFlatMap parameters) {
		
		StringBuilder filtros = new StringBuilder();
		
		// Documento de serviço
		if(parameters.getLong("doctoServico.idDoctoServico") != null) {
			filtros.append("Documento de serviço:;").append(parameters.getString("doctoServico.dsTpDocumentoServico")).append(" ")
			.append(parameters.getString("doctoServico.sgFilialOrigem")).append(" ")
			.append(parameters.getString("doctoServico.nrDocto")).append("\n");
		}
		
		// Remetente
		if(parameters.getLong("doctoServico.clienteByIdClienteRemetente.idCliente") != null) {
			filtros.append("Remetente:;").append(parameters.getString("remetente.nrIdentificacaoHidden")).append(";")
			.append(parameters.getString("doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa")).append("\n");
		}
		
		// Destinatário
		if(parameters.getLong("doctoServico.clienteByIdClienteDestinatario.idCliente") != null) {
			filtros.append("Destinatário:;").append(parameters.getString("destinatario.nrIdentificacaoHidden")).append(";")
			.append(parameters.getString("doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa")).append("\n");
		}
		
		// Devedor
		if(parameters.getLong("devedor.idPessoa") != null) {
			filtros.append("Devedor:;").append(parameters.getString("devedor.nrIdentificacaoHidden")).append(";")
			.append(parameters.getString("devedor.nmPessoa")).append("\n");
		}
		
		// Modal
		if(!parameters.getString("modal").isEmpty()) {
			filtros.append("Modal:;").append(parameters.getString("modalHidden")).append("\n");
		}
		
		// Abrangência
		if(!parameters.getString("abrangencia").isEmpty()) {
			filtros.append("Abrangência:;").append(parameters.getString("abrangenciaHidden")).append("\n");
		}
		
		// Data de emissão
		if(parameters.getYearMonthDay("dataEmissaoDocInicial") != null ||
				parameters.getYearMonthDay("dataEmissaoDocFinal") != null) {
			filtros.append("Data de emissão:;");
			if(parameters.getYearMonthDay("dataEmissaoDocInicial") != null) {
				filtros.append(parameters.getYearMonthDay("dataEmissaoDocInicial").toString("dd/MM/yyyy")).append(";");
			}
			if(parameters.getYearMonthDay("dataEmissaoDocFinal") != null) {
				filtros.append("até;")
				.append(parameters.getYearMonthDay("dataEmissaoDocFinal").toString("dd/MM/yyyy"));
			}
			filtros.append("\n");
		}
		
		// Serviço
		if(parameters.getLong("servico.idServico") != null) {
			filtros.append("Serviço:;").append(parameters.getString("dsServicoHidden")).append("\n");
		}
	
		return filtros.toString();
	} 
	
	private String formatParameterToBigDecimal(TypedFlatMap parameters, String keyParameter) {
		return FormatUtils.formatBigDecimalWithPattern(
				parameters.getBigDecimal(keyParameter)
				, "###,###,###,###,##0.00");
	}
	
	public void setRelatorioSegurosDAO(RelatorioSegurosDAO relatorioSegurosDAO) {
		this.relatorioSegurosDAO = relatorioSegurosDAO;
	}
}