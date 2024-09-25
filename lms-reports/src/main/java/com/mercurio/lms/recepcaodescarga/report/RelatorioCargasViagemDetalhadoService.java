package com.mercurio.lms.recepcaodescarga.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Rodrigo Antunes
 * @spring.bean id="lms.recepcaodescarga.relatorioCargasViagemDetalhadoService"
 * @spring.property name="reportName" value="com/mercurio/lms/recepcaodescarga/report/consultarCargasViagemDetalhado.jasper"
 */
public class RelatorioCargasViagemDetalhadoService extends ReportServiceSupport {

	private ManifestoService manifestoService;
	private ConfiguracoesFacade configuracoesFacade;

	private ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		SqlTemplate sql = mountReportParameters(tfm);
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));

		JRReportDataObject jr = null;
		
        if (tfm.getString("tpFormatoRelatorio").equals("xls")){
        	jr = createReportDataObject(new JRMapCollectionDataSource(executeReportCargasEmViagemExcel(tfm)), parametersReport);
			setReportName("com/mercurio/lms/recepcaodescarga/report/consultarCargasViagemDetalhadoExcel.jasper");			
		}else{
			jr = createReportDataObject(new JRMapCollectionDataSource(executeReportCargasEmViagem(tfm)), parametersReport);
			setReportName("com/mercurio/lms/recepcaodescarga/report/consultarCargasViagemDetalhado.jasper");
		}

		return jr;
	}

	private List executeReportCargasEmViagem(TypedFlatMap parameters) {
		List retorno = new ArrayList();
		List list = this.getManifestoService().findReportCargasEmViagem(parameters);
		Iterator iter = list.iterator();
		
		while (iter.hasNext()) {
			TypedFlatMap tfm = (TypedFlatMap) iter.next();
			Map dados = new HashMap();
			
			dados.put("NR_MANIFESTO", 				tfm.getLong("manifesto.nrManifesto"));
			dados.put("DH_EMISSAO_MANIFESTO", 		JTFormatUtils.format(tfm.getDateTime("manifesto.dhEmissaoManifesto")));
			dados.put("CHEGADA_PROGRAMADA", 		tfm.getDateTime("chegadaProgramada")!=null?JTFormatUtils.format(tfm.getDateTime("chegadaProgramada")):null);
			dados.put("DH_SAIDA", 					tfm.getDateTime("manifesto.dhSaida")!=null?JTFormatUtils.format(tfm.getDateTime("manifesto.dhSaida")):null);
			dados.put("ATRASO", 					tfm.getString("atraso"));
			dados.put("CHEGADA_ESTIMADA", 			tfm.getDateTime("chegadaEstimada")!=null?JTFormatUtils.format(tfm.getDateTime("chegadaEstimada")):null);
			dados.put("FILIAL_DESTINO_MANIFESTO", 	tfm.getString("manifesto.filialByIdFilialDestino.sgFilial") );
			dados.put("FILIAL_ORIGEM_MANIFESTO", 	tfm.getString("manifesto.filialByIdFilialOrigem.sgFilial"));
			dados.put("ID_MANIFESTO", 				tfm.getLong("manifesto.idManifesto"));
			dados.put("FILIAL_ORIGEM_CC", 		 	tfm.getString("manifesto.controleCarga.filialByIdFilialOrigem.sgFilial"));
			dados.put("NR_CONTROLE_CARGA", 			tfm.getLong("manifesto.controleCarga.nrControleCarga"));
			dados.put("NR_FROTA_VEICULO", 			tfm.getString("manifesto.controleCarga.meioTransporteByIdTransportado.nrFrota"));			
			dados.put("NR_IDENTIFICADOR_VEICULO", 	tfm.getString("manifesto.controleCarga.meioTransporteByIdTransportado.nrIdentificador"));
			dados.put("VL_TOTAL_MANIFESTO", 		tfm.getBigDecimal("manifesto.vlTotalManifesto"));
			dados.put("NR_CAPACIDADE_KG", 			tfm.getBigDecimal("manifesto.controleCarga.meioTransporteByIdTransportado.nrCapacidadeKg"));
			dados.put("NR_FROTA_REBOQUE", 			tfm.getString("manifesto.controleCarga.meioTransporteByIdSemiRebocado.nrFrota"));
			dados.put("NR_IDENTIFICADOR_REBOQUE", 	tfm.getString("manifesto.controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador"));
			dados.put("PS_TOTAL_FROTA", 			tfm.getBigDecimal("manifesto.controleCarga.psTotalFrota"));
			dados.put("PC_OCUPACAO_CALCULADO", 		tfm.getBigDecimal("manifesto.controleCarga.pcOcupacaoCalculado"));
			dados.put("PC_OCUPACAO_AFORADO_CALCULADO", tfm.getBigDecimal("manifesto.controleCarga.pcOcupacaoAforadoCalculado"));
			dados.put("PC_OCUPACAO_INFORMADO", 		tfm.getBigDecimal("manifesto.controleCarga.pcOcupacaoInformado"));
			dados.put("PS_TOTAL_MANIFESTO", 		tfm.getBigDecimal("manifesto.psTotalManifesto"));
			dados.put("SIGLA_SIMBOLO_MOEDA", 		tfm.getString("manifesto.moeda.siglaSimbolo"));
			dados.put("TP_MANIFESTO", 				tfm.getString("manifesto.tpManifesto"));
			dados.put("QTDE_DOCTOS", 				tfm.getInteger("quantidadeDoctos"));
			
			retorno.add( dados );
		}
		
		return retorno;
	}
	
	private List executeReportCargasEmViagemExcel(TypedFlatMap parameters) {
		List retorno = new ArrayList();
		List listManifestos = this.getManifestoService().findReportCargasEmViagem(parameters);
		Iterator iterManifestos = listManifestos.iterator();
		
		while (iterManifestos.hasNext()) {
			TypedFlatMap tfmManifesto = (TypedFlatMap) iterManifestos.next();
			
			Long idManifesto = tfmManifesto.getLong("manifesto.idManifesto");
			
			List listDoctos = getManifestoService().findReportDocumentosConsultarCargasViagem(idManifesto);
			
			for (Iterator iterDoctos = listDoctos.iterator(); iterDoctos.hasNext();) {
				TypedFlatMap tfmDoctos = (TypedFlatMap) iterDoctos.next();
				Map dados = new HashMap();

				dados.put("FILIAL_ORIGEM_CC", 		 	tfmManifesto.getString("manifesto.controleCarga.filialByIdFilialOrigem.sgFilial"));
				dados.put("NR_CONTROLE_CARGA", 			tfmManifesto.getLong("manifesto.controleCarga.nrControleCarga"));
				dados.put("NR_FROTA_VEICULO", 			tfmManifesto.getString("manifesto.controleCarga.meioTransporteByIdTransportado.nrFrota"));			
				dados.put("NR_IDENTIFICADOR_VEICULO", 	tfmManifesto.getString("manifesto.controleCarga.meioTransporteByIdTransportado.nrIdentificador"));
				dados.put("NR_FROTA_REBOQUE", 			tfmManifesto.getString("manifesto.controleCarga.meioTransporteByIdSemiRebocado.nrFrota"));
				dados.put("NR_IDENTIFICADOR_REBOQUE", 	tfmManifesto.getString("manifesto.controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador"));
				dados.put("CHEGADA_PROGRAMADA", 		tfmManifesto.getDateTime("chegadaProgramada")!=null?JTFormatUtils.format(tfmManifesto.getDateTime("chegadaProgramada"), "dd/MM/yyyy HH:mm"):null);
				dados.put("CHEGADA_ESTIMADA", 			tfmManifesto.getDateTime("chegadaEstimada")!=null?JTFormatUtils.format(tfmManifesto.getDateTime("chegadaEstimada"), "dd/MM/yyyy HH:mm"):null);
				dados.put("FILIAL_ORIGEM_MANIFESTO", 	tfmManifesto.getString("manifesto.filialByIdFilialOrigem.sgFilial"));
				dados.put("NR_MANIFESTO", 				tfmManifesto.getLong("manifesto.nrManifesto"));
				dados.put("DH_EMISSAO_MANIFESTO", 		JTFormatUtils.format(tfmManifesto.getDateTime("manifesto.dhEmissaoManifesto"), "dd/MM/yyyy"));
				
				String tpDoctoServico = tfmDoctos.getString("doctoServico.tpDoctoServico")+""; 
				String nrDoctoServico = FormatUtils.formatSgFilialWithLong(tfmDoctos.getString("doctoServico.filialByIdFilialOrigem.sgFilial"), tfmDoctos.getLong("doctoServico.nrDoctoServico")); 
				dados.put("DOCTO_SERVICO", tpDoctoServico+" "+ nrDoctoServico);
				dados.put("FILIAL_DESTINO_DOCTO_SERVICO", tfmDoctos.getString("doctoServico.filialByIdFilialDestino.sgFilial"));
				dados.put("DH_EMISSAO_DOCTO_SERVICO", tfmDoctos.getDateTime("doctoServico.dhEmissao")!=null? JTFormatUtils.format(tfmDoctos.getDateTime("doctoServico.dhEmissao"), "dd/MM/yyyy"):null);
				dados.put("REMETENTE_DOCTO_SERVICO", tfmDoctos.getString("doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa")); 
				dados.put("DESTINATARIO_DOCTO_SERVICO", tfmDoctos.getString("doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa"));
				dados.put("VOLUME", tfmDoctos.getInteger("doctoServico.qtVolumes")); 
				dados.put("PESO", tfmDoctos.getBigDecimal("doctoServico.psReal"));
				dados.put("VALOR", tfmDoctos.getBigDecimal("doctoServico.vlMercadoria"));
				dados.put("DPE", tfmDoctos.getYearMonthDay("doctoServico.dtPrevEntrega")!=null?JTFormatUtils.format(tfmDoctos.getYearMonthDay("doctoServico.dtPrevEntrega")):null); 
				dados.put("NR_ROTA", tfmDoctos.getShort("doctoServico.rotaColetaEntregaByIdRotaColetaEntregaReal.nrRota"));
				dados.put("DS_ROTA", tfmDoctos.getString("doctoServico.rotaColetaEntregaByIdRotaColetaEntregaReal.dsRota"));
				dados.put("DT_AGENDAMENTO", tfmDoctos.getYearMonthDay("doctoServico.agendamentoEntrega.dtAgendamento")!=null?JTFormatUtils.format(tfmDoctos.getYearMonthDay("doctoServico.agendamentoEntrega.dtAgendamento")):null);
				dados.put("BL_PRODUTO_PERIGOSO", tfmDoctos.getString("conhecimento.blProdutoPerigoso"));
				dados.put("BL_PRODUTO_CONTROLADO", tfmDoctos.getString("conhecimento.blProdutoControlado"));
				
				
				retorno.add( dados );
			}	
		}
		
		return retorno;
	}
	
	
    private SqlTemplate mountReportParameters(TypedFlatMap parameters) {
        SqlTemplate sql = createSqlTemplate();

        sql.addFilterSummary("filialOrigem", parameters.getString("filialOrigem.sgFilial"));
        sql.addFilterSummary("filialDestino", parameters.getString("filialDestino.sgFilial"));
        
        sql.addFilterSummary("servico", parameters.getString("servico.dsServico"));
        
        String strControleCargas = FormatUtils.formatSgFilialWithLong(parameters.getString("controleCarga.filialByIdFilialOrigem.sgFilial"),  parameters.getLong("controleCarga.nrControleCarga"));
        sql.addFilterSummary("controleCargas", strControleCargas);
        
        String strMeioTransporte = parameters.getString("controleCarga.meioTransporteByIdTransportado2.nrFrota");
        if (StringUtils.isNotEmpty(strMeioTransporte)) {
        	strMeioTransporte = strMeioTransporte+" "+parameters.getString("controleCarga.meioTransporteByIdTransportado.nrIdentificador");
        }
        sql.addFilterSummary("meioTransporte",  strMeioTransporte);

        String strManifesto =FormatUtils.formatSgFilialWithLong(parameters.getString("manifesto.filialByIdFilialOrigem.sgFilial"), parameters.getLong("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
        if (StringUtils.isNotEmpty(strManifesto)) {
        	strManifesto = parameters.getString("manifesto.tpManifesto") + " " +strManifesto ;  
        } 
        sql.addFilterSummary("manifestoViagem", strManifesto);

        if ( !StringUtils.isBlank(parameters.getString("chegadaPrevistaInicial")) ) 
        	sql.addFilterSummary("horarioChegadaPrevistaInicial", JTFormatUtils.format(parameters.getDateTime("chegadaPrevistaInicial"), 2));

        if ( !StringUtils.isBlank(parameters.getString("chegadaPrevistaFinal")) )
        	sql.addFilterSummary("horarioChegadaPrevistaFinal", JTFormatUtils.format(parameters.getDateTime("chegadaPrevistaFinal"), 2));

        if ( !StringUtils.isBlank(parameters.getString("chegadaEstimadaInicial")) )
        	sql.addFilterSummary("horarioChegadaEstimadaInicial", JTFormatUtils.format(parameters.getDateTime("chegadaEstimadaInicial"), 2));

        if ( !StringUtils.isBlank(parameters.getString("chegadaEstimadaFinal")) )
        	sql.addFilterSummary("horarioChegadaEstimadaFinal", JTFormatUtils.format(parameters.getDateTime("chegadaEstimadaFinal"), 2));
        
        Boolean emAtraso = parameters.getBoolean("emAtraso");
        Boolean naData = parameters.getBoolean("naData");
        Boolean emDia = parameters.getBoolean("emDia");
        
        if (emAtraso.booleanValue())
        	sql.addFilterSummary("emAtraso", getConfiguracoesFacade().getMensagem("sim") );
        
        if (naData.booleanValue())
        	sql.addFilterSummary("naData", getConfiguracoesFacade().getMensagem("sim") );
        
        if (emDia.booleanValue())
        	sql.addFilterSummary("emDia", getConfiguracoesFacade().getMensagem("sim") );
        
        return sql;
    }
	
    public JRDataSource executeSubRelatorioServicos(Long idManifesto) {
		List l = getManifestoService().findReportDocumentosConsultarCargasViagem(idManifesto);
		List doctoServicoList = new ArrayList();
		
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			TypedFlatMap tfm = (TypedFlatMap) iter.next();
			Map dados = new HashMap();

			dados.put("SERVICO", tfm.getString("doctoServico.servico.dsServico"));
			String tpDoctoServico = tfm.getString("doctoServico.tpDoctoServico")+""; 
			String nrDoctoServico = FormatUtils.formatSgFilialWithLong(tfm.getString("doctoServico.filialByIdFilialOrigem.sgFilial"), tfm.getLong("doctoServico.nrDoctoServico")); 
			dados.put("DOCTO_SERVICO", tpDoctoServico+" "+ nrDoctoServico);
			dados.put("FILIAL_DESTINO_DOCTO_SERVICO", tfm.getString("doctoServico.filialByIdFilialDestino.sgFilial"));
			dados.put("DH_EMISSAO_DOCTO_SERVICO", tfm.getDateTime("doctoServico.dhEmissao")!=null? JTFormatUtils.format(tfm.getDateTime("doctoServico.dhEmissao")):null);
			dados.put("REMETENTE_DOCTO_SERVICO", tfm.getString("doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa")); 
			dados.put("DESTINATARIO_DOCTO_SERVICO", tfm.getString("doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa"));
			dados.put("VOLUME", tfm.getInteger("doctoServico.qtVolumes")); 
			dados.put("PESO", tfm.getBigDecimal("doctoServico.psReal"));
			dados.put("VALOR", tfm.getBigDecimal("doctoServico.vlMercadoria"));
			dados.put("SIGLA_SIMBOLO", tfm.getString("doctoServico.moeda.siglaSimbolo"));
			dados.put("VL_TOTAL_DOC_SERVICO", tfm.getBigDecimal("doctoServico.vlTotalDocServico"));
			dados.put("DPE", tfm.getYearMonthDay("doctoServico.dtPrevEntrega")!=null?JTFormatUtils.format(tfm.getYearMonthDay("doctoServico.dtPrevEntrega")):null); 
			doctoServicoList.add(dados);
		}
		
		return new JRMapCollectionDataSource(doctoServicoList);
    }
    
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}

	private ManifestoService getManifestoService() {
		return manifestoService;
	}

}
