/**
 * 
 */
package com.mercurio.lms.recepcaodescarga.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.lang.StringUtils;

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
 * @spring.bean id="lms.recepcaodescarga.relatorioCargasViagemResumidoService"
 * @spring.property name="reportName" value="com/mercurio/lms/recepcaodescarga/report/consultarCargasViagemResumido.jasper"
 */
public class RelatorioCargasViagemResumidoService extends ReportServiceSupport {
	private ManifestoService manifestoService;
	private ConfiguracoesFacade configuracoesFacade;

	private ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public JRReportDataObject execute(Map parameters) throws Exception {
    	TypedFlatMap tfm = (TypedFlatMap)parameters;
        SqlTemplate sql = mountReportParameters(tfm);
        
        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
        
        parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));

        JRReportDataObject jr = createReportDataObject(new JRMapCollectionDataSource(executeReportCargasEmViagem(tfm)), parametersReport);

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


	private ManifestoService getManifestoService() {
		return this.manifestoService;
	}

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}

}
