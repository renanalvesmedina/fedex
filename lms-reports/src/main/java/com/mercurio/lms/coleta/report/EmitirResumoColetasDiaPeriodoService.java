package com.mercurio.lms.coleta.report;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.joda.time.YearMonthDay;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração do Relatório de Resumo de Coletas do Dia-Periodo.
 * Especificação técnica 02.03.01.02
 * @author Cesar Gabbardo
 * 
 * @spring.bean id="lms.coleta.emitirResumoColetasDiaPeriodoService"
 * @spring.property name="reportName" value="com/mercurio/lms/coleta/report/emitirResumoColetasDiaPeriodo.jasper"
 */
public class EmitirResumoColetasDiaPeriodoService extends ReportServiceSupport {
	private Long idFilial;
	private Long idServico;
	private String tpModoPedidoColeta;
	private String tpPedidoColeta;
	private YearMonthDay dataInicial;
	private YearMonthDay dataFinal;
	private HistoricoFilialService historicoFilialService;
	private ServicoService servicoService;
	private ConversaoMoedaService conversaoMoedaService;
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap param = (TypedFlatMap)parameters;

		this.setIdFilial(param.getLong("filial.idFilial"));
		this.setIdServico(param.getLong("servico.idServico"));

		if (param.getString("tpModoPedidoColeta") != null && !param.getString("tpModoPedidoColeta").equals("")) {
			this.setTpModoPedidoColeta(param.getString("tpModoPedidoColeta"));
		} else {
			this.setTpModoPedidoColeta(null);
		}
		if (param.getString("tpPedidoColeta") != null && !param.getString("tpPedidoColeta").equals("")) {
			this.setTpPedidoColeta(param.getString("tpPedidoColeta"));
		} else {
			this.setTpPedidoColeta(null);
		}		
		this.setDataInicial(param.getYearMonthDay("dataInicial"));
		this.setDataFinal(param.getYearMonthDay("dataFinal"));
		
        List result = (List)getJdbcTemplate().query(mountSql(), new ResultSetExtractor() {
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				List rows = new ArrayList();
				YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
				Long idPaisDestino = SessionUtils.getPaisSessao().getIdPais();
				Long idMoedaDestino = SessionUtils.getMoedaSessao().getIdMoeda();
				Long idFilialResponsavel = null;
				String sgFilialResponsavel = null;
				//SOLICITADAS
				Long totalColetasSolicitadas = Long.valueOf(0);
				BigDecimal totalPesoSolicitadas = new BigDecimal(0);
				Long totalVolumesSolicitadas = Long.valueOf(0);
				BigDecimal totalValorSolicitadas = new BigDecimal(0);
				//EXECUTADAS
				Long totalColetasExecutadas = Long.valueOf(0);
				BigDecimal totalPesoExecutadas = new BigDecimal(0);
				Long totalVolumesExecutadas = Long.valueOf(0);
				BigDecimal totalValorExecutadas = new BigDecimal(0);
				//EM EXECUCAO
				Long totalColetasEmExec = Long.valueOf(0);
				BigDecimal totalPesoEmExec = new BigDecimal(0);
				Long totalVolumesEmExec = Long.valueOf(0);
				BigDecimal totalValorEmExec = new BigDecimal(0);
				//CANCELADAS
				Long totalColetasCanc = Long.valueOf(0);
				BigDecimal totalPesoCanc = new BigDecimal(0);
				Long totalVolumesCanc = Long.valueOf(0);
				BigDecimal totalValorCanc = new BigDecimal(0);			
				while(rs.next()) {
					idFilialResponsavel = rs.getLong("ID_FILIAL_RESPONSAVEL");
					sgFilialResponsavel = rs.getString("SG_FILIAL_RESPONSAVEL");
					Long idPaisOrigem = rs.getLong("ID_PAIS");
					Long idMoedaOrigem = rs.getLong("ID_MOEDA");
					
					totalValorSolicitadas = totalValorSolicitadas.add(getConversaoMoedaService().findConversaoMoeda(idPaisOrigem, idMoedaOrigem, idPaisDestino, idMoedaDestino, dataAtual, rs.getBigDecimal("TOTAL_VALOR_SOLICITADAS")));
					totalValorExecutadas = totalValorExecutadas.add(getConversaoMoedaService().findConversaoMoeda(idPaisOrigem, idMoedaOrigem, idPaisDestino, idMoedaDestino, dataAtual, rs.getBigDecimal("TOTAL_VALOR_EXECUTADAS")));
					totalValorEmExec = totalValorEmExec.add(getConversaoMoedaService().findConversaoMoeda(idPaisOrigem, idMoedaOrigem, idPaisDestino, idMoedaDestino, dataAtual, rs.getBigDecimal("TOTAL_VALOR_EM_EXEC")));
					totalValorCanc = totalValorCanc.add(getConversaoMoedaService().findConversaoMoeda(idPaisOrigem, idMoedaOrigem, idPaisDestino, idMoedaDestino, dataAtual, rs.getBigDecimal("TOTAL_VALOR_CANC")));
					
					totalColetasSolicitadas = LongUtils.add(totalColetasSolicitadas, rs.getLong("TOTAL_COLETAS_SOLICITADAS")); 
					totalColetasExecutadas = LongUtils.add(totalColetasExecutadas, rs.getLong("TOTAL_COLETAS_EXECUTADAS"));
					totalColetasEmExec = LongUtils.add(totalColetasEmExec, rs.getLong("TOTAL_COLETAS_EM_EXEC"));
					totalColetasCanc = LongUtils.add(totalColetasCanc, rs.getLong("TOTAL_COLETAS_CANC"));
					
					totalPesoSolicitadas = totalPesoSolicitadas.add(rs.getBigDecimal("TOTAL_PESO_SOLICITADAS"));
					totalPesoExecutadas = totalPesoExecutadas.add(rs.getBigDecimal("TOTAL_PESO_EXECUTADAS"));
					totalPesoEmExec = totalPesoEmExec.add(rs.getBigDecimal("TOTAL_PESO_EM_EXEC"));
					totalPesoCanc = totalPesoCanc.add(rs.getBigDecimal("TOTAL_PESO_CANC"));
					
					totalVolumesSolicitadas = LongUtils.add(totalVolumesSolicitadas, rs.getLong("TOTAL_VOLUMES_SOLICITADAS"));
					totalVolumesExecutadas = LongUtils.add(totalVolumesExecutadas, rs.getLong("TOTAL_VOLUMES_EXECUTADAS"));
					totalVolumesEmExec = LongUtils.add(totalVolumesEmExec, rs.getLong("TOTAL_VOLUMES_EM_EXEC"));
					totalVolumesCanc = LongUtils.add(totalVolumesCanc, rs.getLong("TOTAL_VOLUMES_CANC"));
				}
				if (idFilialResponsavel!=null){ //Caso tenha retornado dados.
					TypedFlatMap values = new TypedFlatMap();
					values.put("ID_FILIAL_RESPONSAVEL", idFilialResponsavel);
					values.put("SG_FILIAL_RESPONSAVEL", sgFilialResponsavel);
					values.put("TOTAL_COLETAS_SOLICITADAS", totalColetasSolicitadas);          
					values.put("TOTAL_PESO_SOLICITADAS", totalPesoSolicitadas);
					values.put("TOTAL_VOLUMES_SOLICITADAS", totalVolumesSolicitadas);
					values.put("TOTAL_VALOR_SOLICITADAS", totalValorSolicitadas);
					values.put("TOTAL_COLETAS_EXECUTADAS", totalColetasExecutadas);
					values.put("TOTAL_PESO_EXECUTADAS", totalPesoExecutadas);
					values.put("TOTAL_VOLUMES_EXECUTADAS", totalVolumesExecutadas);
					values.put("TOTAL_VALOR_EXECUTADAS", totalValorExecutadas);
					values.put("TOTAL_COLETAS_EM_EXEC", totalColetasEmExec);
					values.put("TOTAL_PESO_EM_EXEC", totalPesoEmExec);
					values.put("TOTAL_VOLUMES_EM_EXEC", totalVolumesEmExec);
					values.put("TOTAL_VALOR_EM_EXEC", totalValorEmExec);        
					values.put("TOTAL_COLETAS_CANC", totalColetasCanc);
					values.put("TOTAL_PESO_CANC", totalPesoCanc);
					values.put("TOTAL_VOLUMES_CANC", totalVolumesCanc);
					values.put("TOTAL_VALOR_CANC", totalValorCanc);
					rows.add(values);
				}
				return rows;
			}
        });
		
        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
                
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("parametrosPesquisa", getFilterSummary(param));
        parametersReport.put("moeda", SessionUtils.getMoedaSessao().getSiglaSimbolo());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
        return createReportDataObject(new JRMapCollectionDataSource(result),parametersReport);

	}
	
   
	private String mountSql(){

    	StringBuffer str = new StringBuffer();
    	str.append(" SELECT");
    	str.append(" TOTAIS.ID_FILIAL_RESPONSAVEL,"); 
    	str.append(" F.SG_FILIAL SG_FILIAL_RESPONSAVEL,");
    	str.append(" TOTAIS.ID_MOEDA ID_MOEDA,");
    	str.append(" TOTAIS.ID_PAIS ID_PAIS,");    	
    	str.append(" SUM(TOTAL_COLETAS_SOLICITADAS) TOTAL_COLETAS_SOLICITADAS,");          
    	str.append(" SUM(TOTAL_PESO_SOLICITADAS) TOTAL_PESO_SOLICITADAS,");
    	str.append(" SUM(TOTAL_VOLUMES_SOLICITADAS) TOTAL_VOLUMES_SOLICITADAS,");
    	str.append(" SUM(TOTAL_VALOR_SOLICITADAS) TOTAL_VALOR_SOLICITADAS,");
    	str.append(" SUM(TOTAL_COLETAS_EXECUTADAS) TOTAL_COLETAS_EXECUTADAS,");
    	str.append(" SUM(TOTAL_PESO_EXECUTADAS) TOTAL_PESO_EXECUTADAS,");
    	str.append(" SUM(TOTAL_VOLUMES_EXECUTADAS) TOTAL_VOLUMES_EXECUTADAS,");
    	str.append(" SUM(TOTAL_VALOR_EXECUTADAS) TOTAL_VALOR_EXECUTADAS,");
    	str.append(" SUM(TOTAL_COLETAS_EM_EXEC) TOTAL_COLETAS_EM_EXEC,");
    	str.append(" SUM(TOTAL_PESO_EM_EXEC) TOTAL_PESO_EM_EXEC,");
    	str.append(" SUM(TOTAL_VOLUMES_EM_EXEC) TOTAL_VOLUMES_EM_EXEC,");
    	str.append(" SUM(TOTAL_VALOR_EM_EXEC) TOTAL_VALOR_EM_EXEC,");        
    	str.append(" SUM(TOTAL_COLETAS_CANC) TOTAL_COLETAS_CANC,");
    	str.append(" SUM(TOTAL_PESO_CANC) TOTAL_PESO_CANC,");
    	str.append(" SUM(TOTAL_VOLUMES_CANC) TOTAL_VOLUMES_CANC,");
    	str.append(" SUM(TOTAL_VALOR_CANC) TOTAL_VALOR_CANC");
    	str.append(" FROM (");
    	str.append(mountColetasSolicitadas());
    	str.append(" UNION");
    	str.append(mountColetasExecutadas()); 
    	str.append(" UNION");
    	str.append(mountColetasEmExecucao());
    	str.append(" UNION");
    	str.append(mountColetasCanceladas());
    	str.append(" ) TOTAIS, FILIAL F");
    	str.append(" WHERE TOTAIS.ID_FILIAL_RESPONSAVEL = F.ID_FILIAL");
    	str.append(" GROUP BY");
    	str.append(" TOTAIS.ID_FILIAL_RESPONSAVEL, F.SG_FILIAL, TOTAIS.ID_MOEDA, TOTAIS.ID_PAIS");  
    	return str.toString();
    }


	/**
	 * @param idFilial
	 * @param str
	 */
	private StringBuffer mountColetasSolicitadas() {
		//-------------------------
    	//-------SOLICITADAS-------
    	//-------------------------
		StringBuffer str = new StringBuffer();
    	str.append(" SELECT"); 
    	str.append(" DETALHE_COLETA.ID_SERVICO,");
    	str.append(" PEDIDO_COLETA.ID_FILIAL_RESPONSAVEL,");
    	str.append(" PEDIDO_COLETA.TP_MODO_PEDIDO_COLETA,");
    	str.append(" PEDIDO_COLETA.ID_MOEDA,");
    	str.append(" UNIDADE_FEDERATIVA.ID_PAIS,");
    	str.append(" COUNT(*) AS TOTAL_COLETAS_SOLICITADAS,");
    	str.append(" SUM(PEDIDO_COLETA.PS_TOTAL_VERIFICADO) AS TOTAL_PESO_SOLICITADAS,");
    	str.append(" SUM(PEDIDO_COLETA.QT_TOTAL_VOLUMES_VERIFICADO) AS TOTAL_VOLUMES_SOLICITADAS,");
    	str.append(" SUM(PEDIDO_COLETA.VL_TOTAL_VERIFICADO) AS TOTAL_VALOR_SOLICITADAS,");
    	str.append(" 0 AS TOTAL_COLETAS_EXECUTADAS,");
    	str.append(" 0 AS TOTAL_PESO_EXECUTADAS,");
    	str.append(" 0 AS TOTAL_VOLUMES_EXECUTADAS,");
    	str.append(" 0 AS TOTAL_VALOR_EXECUTADAS,");
    	str.append(" 0 AS TOTAL_COLETAS_EM_EXEC,");
    	str.append(" 0 AS TOTAL_PESO_EM_EXEC,");
    	str.append(" 0 AS TOTAL_VOLUMES_EM_EXEC,");
    	str.append(" 0 AS TOTAL_VALOR_EM_EXEC,"); 
    	str.append(" 0 AS TOTAL_COLETAS_CANC,");
    	str.append(" 0 AS TOTAL_PESO_CANC,");
    	str.append(" 0 AS TOTAL_VOLUMES_CANC,");
    	str.append(" 0 AS TOTAL_VALOR_CANC");
    	str.append(" FROM PEDIDO_COLETA PEDIDO_COLETA,");
    	str.append(" ENDERECO_PESSOA ENDERECO_PESSOA,");
    	str.append(" MUNICIPIO MUNICIPIO,");
    	str.append(" UNIDADE_FEDERATIVA UNIDADE_FEDERATIVA,");
    	str.append(" DETALHE_COLETA DETALHE_COLETA");
   		str.append(" WHERE PEDIDO_COLETA.ID_PEDIDO_COLETA = DETALHE_COLETA.ID_PEDIDO_COLETA");
   		str.append(" AND PEDIDO_COLETA.ID_ENDERECO_PESSOA = ENDERECO_PESSOA.ID_ENDERECO_PESSOA");
   		str.append(" AND ENDERECO_PESSOA.ID_MUNICIPIO = MUNICIPIO.ID_MUNICIPIO");
   		str.append(" AND MUNICIPIO.ID_UNIDADE_FEDERATIVA = UNIDADE_FEDERATIVA.ID_UNIDADE_FEDERATIVA");
   		if (this.getIdServico() != null) {
   			str.append(" AND DETALHE_COLETA.ID_SERVICO = "+this.getIdServico());
   		}
    	if (this.getIdFilial()!=null){
    		str.append(" AND PEDIDO_COLETA.ID_FILIAL_RESPONSAVEL = "+this.getIdFilial()); 
    	}
    	if (this.getTpModoPedidoColeta() != null && !this.getTpModoPedidoColeta().equals("")) {
    		str.append(" AND PEDIDO_COLETA.TP_MODO_PEDIDO_COLETA = '"+this.getTpModoPedidoColeta()+"'");
    	}
    	if (this.getTpPedidoColeta() != null && !this.getTpPedidoColeta().equals("")) {
    		str.append(" AND PEDIDO_COLETA.TP_PEDIDO_COLETA = '"+this.getTpPedidoColeta()+"'");
    	}
    	
    	str.append(" AND TRUNC(CAST(PEDIDO_COLETA.DH_PEDIDO_COLETA AS DATE)) >= TO_DATE('"+this.getDataInicial()+"', 'yyyy-mm-dd') "); 
    	str.append(" AND TRUNC(CAST(PEDIDO_COLETA.DH_PEDIDO_COLETA AS DATE)) <= TO_DATE('"+this.getDataFinal()+"', 'yyyy-mm-dd') ");
    	str.append(" GROUP BY ");
   		str.append(" DETALHE_COLETA.ID_SERVICO,");
    	str.append(" PEDIDO_COLETA.ID_FILIAL_RESPONSAVEL,");
   		str.append(" PEDIDO_COLETA.TP_MODO_PEDIDO_COLETA,");
    	str.append(" PEDIDO_COLETA.ID_MOEDA,");
    	str.append(" UNIDADE_FEDERATIVA.ID_PAIS");
    	return str;
	}


	/**
	 * @param idFilial
	 * @param str
	 */
	private StringBuffer mountColetasExecutadas() {
		//------------------------
    	//-------EXECUTADAS-------
    	//------------------------
		StringBuffer str = new StringBuffer();
    	str.append(" SELECT");
    	str.append(" DETALHE_COLETA.ID_SERVICO,");
    	str.append(" PEDIDO_COLETA.ID_FILIAL_RESPONSAVEL,"); 
    	str.append(" PEDIDO_COLETA.TP_MODO_PEDIDO_COLETA,");
    	str.append(" PEDIDO_COLETA.ID_MOEDA,");
    	str.append(" UNIDADE_FEDERATIVA.ID_PAIS,");
    	str.append(" 0 AS TOTAL_COLETAS_SOLICITADAS,");         
    	str.append(" 0 AS TOTAL_PESO_SOLICITADAS,");
    	str.append(" 0 AS TOTAL_VOLUMES_SOLICITADAS,");
    	str.append(" 0 AS TOTAL_VALOR_SOLICITADAS,");
    	str.append(" COUNT(*) AS TOTAL_COLETAS_EXECUTADAS,");
    	str.append(" SUM(PEDIDO_COLETA.PS_TOTAL_VERIFICADO) AS TOTAL_PESO_EXECUTADAS,");
    	str.append(" SUM(PEDIDO_COLETA.QT_TOTAL_VOLUMES_VERIFICADO) AS TOTAL_VOLUMES_EXECUTADAS,");
    	str.append(" SUM(PEDIDO_COLETA.VL_TOTAL_VERIFICADO) AS TOTAL_VALOR_EXECUTADAS,");              
    	str.append(" 0 AS TOTAL_COLETAS_EM_EXEC,");
    	str.append(" 0 AS TOTAL_PESO_EM_EXEC,");
    	str.append(" 0 AS TOTAL_VOLUMES_EM_EXEC,");
    	str.append(" 0 AS TOTAL_VALOR_EM_EXEC,");       
    	str.append(" 0 AS TOTAL_COLETAS_CANC,");
    	str.append(" 0 AS TOTAL_PESO_CANC,");
    	str.append(" 0 AS TOTAL_VOLUMES_CANC,");
    	str.append(" 0 AS TOTAL_VALOR_CANC");
    	str.append(" FROM PEDIDO_COLETA PEDIDO_COLETA,");
    	str.append(" ENDERECO_PESSOA ENDERECO_PESSOA,");
    	str.append(" MUNICIPIO MUNICIPIO,");
    	str.append(" UNIDADE_FEDERATIVA UNIDADE_FEDERATIVA,");
    	str.append(" EVENTO_COLETA EVENTO_COLETA,");
    	str.append(" DETALHE_COLETA DETALHE_COLETA");
    	str.append(" WHERE PEDIDO_COLETA.ID_PEDIDO_COLETA = EVENTO_COLETA.ID_PEDIDO_COLETA");
   		str.append(" AND PEDIDO_COLETA.ID_ENDERECO_PESSOA = ENDERECO_PESSOA.ID_ENDERECO_PESSOA");
   		str.append(" AND ENDERECO_PESSOA.ID_MUNICIPIO = MUNICIPIO.ID_MUNICIPIO");
   		str.append(" AND MUNICIPIO.ID_UNIDADE_FEDERATIVA = UNIDADE_FEDERATIVA.ID_UNIDADE_FEDERATIVA");
    	str.append(" AND PEDIDO_COLETA.ID_PEDIDO_COLETA = DETALHE_COLETA.ID_PEDIDO_COLETA");
    	str.append(" AND EVENTO_COLETA.TP_EVENTO_COLETA = 'EX'");
    	if (this.getIdServico() != null) {
    		str.append(" AND DETALHE_COLETA.ID_SERVICO = "+this.getIdServico());
    	}
    	if (this.getIdFilial() != null){
    		str.append(" AND PEDIDO_COLETA.ID_FILIAL_RESPONSAVEL = "+this.getIdFilial());
    	}
    	if (this.getTpModoPedidoColeta() != null && !this.getTpModoPedidoColeta().equals("")) {
    		str.append(" AND PEDIDO_COLETA.TP_MODO_PEDIDO_COLETA = '"+this.getTpModoPedidoColeta()+"'");
    	}
    	if (this.getTpPedidoColeta() != null && !this.getTpPedidoColeta().equals("")) {
    		str.append(" AND PEDIDO_COLETA.TP_PEDIDO_COLETA = '"+this.getTpPedidoColeta()+"'");
    	}	
    	str.append(" AND TRUNC(CAST(EVENTO_COLETA.DH_EVENTO AS DATE)) >= TO_DATE('"+this.getDataInicial()+"', 'yyyy-mm-dd') "); 
    	str.append(" AND TRUNC(CAST(EVENTO_COLETA.DH_EVENTO AS DATE)) <= TO_DATE('"+this.getDataFinal()+"', 'yyyy-mm-dd') ");
    	str.append(" GROUP BY"); 
   		str.append(" DETALHE_COLETA.ID_SERVICO,");
    	str.append(" PEDIDO_COLETA.ID_FILIAL_RESPONSAVEL,");
   		str.append(" PEDIDO_COLETA.TP_MODO_PEDIDO_COLETA,");
    	str.append(" PEDIDO_COLETA.ID_MOEDA,");
    	str.append(" UNIDADE_FEDERATIVA.ID_PAIS");   		
    	return str;
	}


	/**
	 * @param idFilial
	 * @param str
	 */
	private StringBuffer mountColetasEmExecucao() {
		//-------------------------
    	//-------EM EXECUÇÃO-------
    	//-------------------------
		StringBuffer str = new StringBuffer();
    	str.append(" SELECT");
    	str.append(" DETALHE_COLETA.ID_SERVICO,");
    	str.append(" PEDIDO_COLETA.ID_FILIAL_RESPONSAVEL,"); 
    	str.append(" PEDIDO_COLETA.TP_MODO_PEDIDO_COLETA,");
    	str.append(" PEDIDO_COLETA.ID_MOEDA,");
    	str.append(" UNIDADE_FEDERATIVA.ID_PAIS,");    	
    	str.append(" 0 AS TOTAL_COLETAS_SOLICITADAS,");         
    	str.append(" 0 AS TOTAL_PESO_SOLICITADAS,");
    	str.append(" 0 AS TOTAL_VOLUMES_SOLICITADAS,");
    	str.append(" 0 AS TOTAL_VALOR_SOLICITADAS,");
    	str.append(" 0 AS TOTAL_COLETAS_EXECUTADAS,");
    	str.append(" 0 AS TOTAL_PESO_EXECUTADAS,");
    	str.append(" 0 AS TOTAL_VOLUMES_EXECUTADAS,");
    	str.append(" 0 AS TOTAL_VALOR_EXECUTADAS,");          
    	str.append(" COUNT(*) AS TOTAL_COLETAS_EM_EXEC,");
    	str.append(" SUM(PEDIDO_COLETA.PS_TOTAL_VERIFICADO) AS TOTAL_PESO_EM_EXEC,");
    	str.append(" SUM(PEDIDO_COLETA.QT_TOTAL_VOLUMES_VERIFICADO) AS TOTAL_VOLUMES_EM_EXEC,");
    	str.append(" SUM(PEDIDO_COLETA.VL_TOTAL_VERIFICADO) AS TOTAL_VALOR_EM_EXEC,");
    	str.append(" 0 AS TOTAL_COLETAS_CANC,");
    	str.append(" 0 AS TOTAL_PESO_CANC,");
    	str.append(" 0 AS TOTAL_VOLUMES_CANC,");
    	str.append(" 0 AS TOTAL_VALOR_CANC");           
    	str.append(" FROM PEDIDO_COLETA PEDIDO_COLETA,");
    	str.append(" ENDERECO_PESSOA ENDERECO_PESSOA,");
    	str.append(" MUNICIPIO MUNICIPIO,");
    	str.append(" UNIDADE_FEDERATIVA UNIDADE_FEDERATIVA,");
    	str.append(" EVENTO_COLETA EVENTO_COLETA,");
    	str.append(" DETALHE_COLETA DETALHE_COLETA");
    	str.append(" WHERE PEDIDO_COLETA.ID_PEDIDO_COLETA = EVENTO_COLETA.ID_PEDIDO_COLETA");
   		str.append(" AND PEDIDO_COLETA.ID_ENDERECO_PESSOA = ENDERECO_PESSOA.ID_ENDERECO_PESSOA");
   		str.append(" AND ENDERECO_PESSOA.ID_MUNICIPIO = MUNICIPIO.ID_MUNICIPIO");
   		str.append(" AND MUNICIPIO.ID_UNIDADE_FEDERATIVA = UNIDADE_FEDERATIVA.ID_UNIDADE_FEDERATIVA");
    	str.append(" AND PEDIDO_COLETA.ID_PEDIDO_COLETA = DETALHE_COLETA.ID_PEDIDO_COLETA");
    	if (this.getIdServico() != null) {
    		str.append(" AND DETALHE_COLETA.ID_SERVICO = "+this.getIdServico());
    	}
    	if (this.getIdFilial() != null){
    		str.append(" AND PEDIDO_COLETA.ID_FILIAL_RESPONSAVEL = "+this.getIdFilial()); 
    	}
    	if (this.getTpModoPedidoColeta() != null && !this.getTpModoPedidoColeta().equals("")) {
    		str.append(" AND PEDIDO_COLETA.TP_MODO_PEDIDO_COLETA = '"+this.getTpModoPedidoColeta()+"'");
    	}
    	if (this.getTpPedidoColeta() != null && !this.getTpPedidoColeta().equals("")) {
    		str.append(" AND PEDIDO_COLETA.TP_PEDIDO_COLETA = '"+this.getTpPedidoColeta()+"'");
    	}   	
    	str.append(" AND EVENTO_COLETA.TP_EVENTO_COLETA IN ('TR','MA')"); 
    	str.append(" AND TRUNC(CAST(EVENTO_COLETA.DH_EVENTO AS DATE)) >= TO_DATE('"+this.getDataInicial()+"', 'yyyy-mm-dd') "); 
    	str.append(" AND TRUNC(CAST(EVENTO_COLETA.DH_EVENTO AS DATE)) <= TO_DATE('"+this.getDataFinal()+"', 'yyyy-mm-dd') "); 
    	str.append(" GROUP BY"); 
   		str.append(" DETALHE_COLETA.ID_SERVICO,");
    	str.append(" PEDIDO_COLETA.ID_FILIAL_RESPONSAVEL,");
   		str.append(" PEDIDO_COLETA.TP_MODO_PEDIDO_COLETA,");
    	str.append(" PEDIDO_COLETA.ID_MOEDA,");
    	str.append(" UNIDADE_FEDERATIVA.ID_PAIS");
    	return str;
	}


	/**
	 * @param idFilial
	 * @param str
	 */
	private StringBuffer mountColetasCanceladas() {
		//-------------------------
    	//-------CANCELADAS--------
    	//-------------------------
		StringBuffer str = new StringBuffer();
    	str.append(" SELECT");  
    	str.append(" DETALHE_COLETA.ID_SERVICO,");
    	str.append(" PEDIDO_COLETA.ID_FILIAL_RESPONSAVEL,"); 
    	str.append(" PEDIDO_COLETA.TP_MODO_PEDIDO_COLETA,");
    	str.append(" PEDIDO_COLETA.ID_MOEDA,");
    	str.append(" UNIDADE_FEDERATIVA.ID_PAIS,");    	
    	str.append(" 0 AS TOTAL_COLETAS_SOLICITADAS,");         
    	str.append(" 0 AS TOTAL_PESO_SOLICITADAS,");
    	str.append(" 0 AS TOTAL_VOLUMES_SOLICITADAS,");
    	str.append(" 0 AS TOTAL_VALOR_SOLICITADAS,");
    	str.append(" 0 AS TOTAL_COLETAS_EXECUTADAS,");
    	str.append(" 0 AS TOTAL_PESO_EXECUTADAS,");
    	str.append(" 0 AS TOTAL_VOLUMES_EXECUTADAS,");
    	str.append(" 0 AS TOTAL_VALOR_EXECUTADAS,");                
    	str.append(" 0 AS TOTAL_COLETAS_EM_EXEC,");
    	str.append(" 0 AS TOTAL_PESO_EM_EXEC,");
    	str.append(" 0 AS TOTAL_VOLUMES_EM_EXEC,");
    	str.append(" 0 AS TOTAL_VALOR_EM_EXEC,");
    	str.append(" COUNT(*) AS TOTAL_COLETAS_CANC,");
    	str.append(" SUM(PEDIDO_COLETA.PS_TOTAL_VERIFICADO) AS TOTAL_PESO_CANC,");
    	str.append(" SUM(PEDIDO_COLETA.QT_TOTAL_VOLUMES_VERIFICADO) AS TOTAL_VOLUMES_CANC,");
    	str.append(" SUM(PEDIDO_COLETA.VL_TOTAL_VERIFICADO) AS TOTAL_VALOR_CANC");
    	str.append(" FROM PEDIDO_COLETA PEDIDO_COLETA,");
    	str.append(" ENDERECO_PESSOA ENDERECO_PESSOA,");
    	str.append(" MUNICIPIO MUNICIPIO,");
    	str.append(" UNIDADE_FEDERATIVA UNIDADE_FEDERATIVA,");
    	str.append(" EVENTO_COLETA EVENTO_COLETA,");
    	str.append(" DETALHE_COLETA DETALHE_COLETA");
    	str.append(" WHERE PEDIDO_COLETA.ID_PEDIDO_COLETA = EVENTO_COLETA.ID_PEDIDO_COLETA");
   		str.append(" AND PEDIDO_COLETA.ID_ENDERECO_PESSOA = ENDERECO_PESSOA.ID_ENDERECO_PESSOA");
   		str.append(" AND ENDERECO_PESSOA.ID_MUNICIPIO = MUNICIPIO.ID_MUNICIPIO");
   		str.append(" AND MUNICIPIO.ID_UNIDADE_FEDERATIVA = UNIDADE_FEDERATIVA.ID_UNIDADE_FEDERATIVA");
    	str.append(" AND PEDIDO_COLETA.ID_PEDIDO_COLETA = DETALHE_COLETA.ID_PEDIDO_COLETA");
    	if (this.getIdServico() != null) {
    		str.append(" AND DETALHE_COLETA.ID_SERVICO = "+this.getIdServico());
    	}
    	if (this.getIdFilial() != null){
    		str.append(" AND PEDIDO_COLETA.ID_FILIAL_RESPONSAVEL = "+this.getIdFilial()); 
    	}
    	if (this.getTpModoPedidoColeta() != null && !this.getTpModoPedidoColeta().equals("")) {
    		str.append(" AND PEDIDO_COLETA.TP_MODO_PEDIDO_COLETA = '"+this.getTpModoPedidoColeta()+"'");
    	}
    	if (this.getTpPedidoColeta() != null && !this.getTpPedidoColeta().equals("")) {
    		str.append(" AND PEDIDO_COLETA.TP_PEDIDO_COLETA = '"+this.getTpPedidoColeta()+"'");
    	} 	
    	str.append(" AND EVENTO_COLETA.TP_EVENTO_COLETA = 'CA'"); 
    	str.append(" AND TRUNC(CAST(EVENTO_COLETA.DH_EVENTO AS DATE)) >= TO_DATE('"+this.getDataInicial()+"', 'yyyy-mm-dd') ");
    	str.append(" AND TRUNC(CAST(EVENTO_COLETA.DH_EVENTO AS DATE)) <= TO_DATE('"+this.getDataFinal()+"', 'yyyy-mm-dd') ");
    	str.append(" GROUP BY"); 
    	str.append(" DETALHE_COLETA.ID_SERVICO,");
    	str.append(" PEDIDO_COLETA.ID_FILIAL_RESPONSAVEL,");
    	str.append(" PEDIDO_COLETA.TP_MODO_PEDIDO_COLETA,");
    	str.append(" PEDIDO_COLETA.ID_MOEDA,");
    	str.append(" UNIDADE_FEDERATIVA.ID_PAIS");    	
    	return str;
	}
    
	/**
	 * Obtem os parametros comuns a ambas as consultas de coleta e entrega
	 * @param param
	 * @return
	 */
	private String getFilterSummary(TypedFlatMap param) {
		SqlTemplate sqlTemp = createSqlTemplate();
		
    	if (param.getLong("filial.idFilial") != null) {
    		sqlTemp.addFilterSummary("filial", param.getString("filial.sgFilial"));	
    	}

		sqlTemp.addFilterSummary("periodoInicial", JTFormatUtils.format(param.getYearMonthDay("dataInicial")));
		sqlTemp.addFilterSummary("periodoFinal", JTFormatUtils.format(param.getYearMonthDay("dataFinal")));
    	
    	if (param.getLong("servico.idServico") != null) {
			sqlTemp.addFilterSummary("servico", getServicoService().findById(param.getLong("servico.idServico")).getDsServico());
		}

    	if (!param.getString("tpModoPedidoColeta").equals("")) {
			sqlTemp.addFilterSummary("modoColeta", this.getDomainValueService().
					findDomainValueDescription("DM_MODO_PEDIDO_COLETA", param.getString("tpModoPedidoColeta")));
		}
    	
    	if (!param.getString("tpPedidoColeta").equals("")) {
			sqlTemp.addFilterSummary("tipoColeta", this.getDomainValueService().
					findDomainValueDescription("DM_TIPO_PEDIDO_COLETA", param.getString("tpPedidoColeta")));
		}
    	
		return sqlTemp.getFilterSummary();
	}

	public Long getIdServico() {
		return idServico;
	}

	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}
	
	public Long getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}

	public String getTpModoPedidoColeta() {
		return tpModoPedidoColeta;
	}
	
	public void setTpModoPedidoColeta(String tpModoPedidoColeta) {
		this.tpModoPedidoColeta = tpModoPedidoColeta;
	}
	
	public String getTpPedidoColeta() {
		return tpPedidoColeta;
	}
	
	public void setTpPedidoColeta(String tpPedidoColeta) {
		this.tpPedidoColeta = tpPedidoColeta;
	}
	
	public YearMonthDay getDataFinal() {
		return dataFinal;
	}
	
	public void setDataFinal(YearMonthDay dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	public YearMonthDay getDataInicial() {
		return dataInicial;
	}
	
	public void setDataInicial(YearMonthDay dataInicial) {
		this.dataInicial = dataInicial;
	}	
	
	public HistoricoFilialService getHistoricoFilialService() {
		return historicoFilialService;
	}
	
	public void setHistoricoFilialService(
			HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}
	
	public ServicoService getServicoService() {
		return servicoService;
	}
	
	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
	
	public ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}

	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}

}
