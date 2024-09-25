package com.mercurio.lms.seguros.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe responsável pela geração do relatorio de processo sinistro
 * 
 * @author Christian Brunkow
 * 
 * @spring.bean id="lms.seguros.emitirRelatorioProcessoSinistroResumidoService"
 * @spring.property name="reportName" value="com/mercurio/lms/seguros/report/emitirRelatorioProcessoSinistroResumido.jasper"
 */

public class EmitirRelatorioProcessoSinistroResumidoService extends EmitirRelatorioProcessoSinistroService{

	/**
     * método responsável por gerar o relatório. 
     */
    public JRReportDataObject execute(Map parameters) throws Exception {
    	   	TypedFlatMap criteria = (TypedFlatMap) parameters;
    	
    	Map sql = getSql(criteria);
    	
    	List criterias = (List) sql.get("criterias");
        
        JRReportDataObject jrReportDataObject = executeQuery(sql.get("sql").toString(), criterias.toArray()); 
            	        
        Map parametersReport = new HashMap();
        parametersReport.put("parametrosPesquisa", sql.get("paratersValues"));
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, criteria.getString("tpFormatoRelatorio"));
        jrReportDataObject.setParameters(parametersReport);
        
        return jrReportDataObject;
    }
    
    /**
     * Método que monta o select para a consulta.
     * 
     * @param map
     * @return SqlTemplate
     */
    private Map getSql(TypedFlatMap criteria) { 
    	
    	StringBuffer sql = new StringBuffer();
    	
    	//Projecao
    	sql.append("SELECT ");
    	sql.append("processoSinistro.id_processo_sinistro AS idProcessoSinistro, ");
    	sql.append("conhecimento.id_conhecimento AS idConhecimento, ");
    	sql.append("manifesto.id_manifesto AS idManifesto, ");
    	sql.append("sinistrodoctoservico.id_sinistro_docto_servico AS idSinistroDoctoServico, ");
    	sql.append("doctoServico.id_docto_servico AS idDoctoServico, ");
    	sql.append("processosinistro.nr_processo_sinistro AS processo, ");
    	sql.append("filialorigemcontrolecarga.sg_filial AS sgFilialOrigemControleCarga, ");
    	sql.append("controlecarga.nr_controle_carga AS nrControleCarga, ");
    	sql.append("rota.ds_rota AS dsRota, ");
    	sql.append("tiposeguro.sg_tipo AS sgTipoSeguro, ");
    	sql.append(PropertyVarcharI18nProjection.createProjection("tiposinistro.ds_tipo_i", "dsTipoSinistro")+", ");
    	sql.append("moeda.sg_moeda AS sgmoeda, ");
    	sql.append("moeda.ds_simbolo AS dsSimbolo, ");
    	sql.append("processosinistro.dh_fechamento AS dhFechamento, ");
    	sql.append("processosinistro.dh_sinistro AS dhSinistro, ");
    	sql.append("reciboIndenizacao.dt_pagamento_efetuado AS dtPagamentoEfetuado, ");
    	
    	//From...
    	sql.append("FROM processo_sinistro processosinistro ");
    	sql.append("INNER JOIN tipo_seguro tiposeguro ON (processosinistro.id_tipo_seguro = tiposeguro.id_tipo_seguro) ");
		sql.append("INNER JOIN tipo_sinistro tiposinistro ON (processosinistro.id_tipo_sinistro = tiposinistro.id_tipo_sinistro) ");
    	sql.append("INNER JOIN municipio ON (processosinistro.id_municipio = municipio.id_municipio) ");
    	sql.append("INNER JOIN unidade_federativa unidadeFederativa ON (municipio.id_unidade_federativa = unidadefederativa.id_unidade_federativa) ");
    	sql.append("LEFT JOIN moeda ON (processosinistro.id_moeda = moeda.id_moeda) ");
    	sql.append("LEFT JOIN filial filial ON (processosinistro.id_filial = filial.id_filial) ");
    	sql.append("LEFT JOIN regional_filial regionalFilial ON (filial.id_filial = regionalFilial.id_filial AND ((regionalfilial.dt_vigencia_inicial <= CURRENT_DATE) AND (regionalfilial.dt_vigencia_final >= CURRENT_DATE))) ");
    	sql.append("LEFT JOIN manifesto_entrega manifestoentrega ON (manifesto.id_manifesto = manifestoentrega.id_manifesto_entrega) ");
		sql.append("LEFT JOIN manifesto_viagem_nacional manifestoviagemnacional ON (manifesto.id_manifesto = manifestoviagemnacional.id_manifesto_viagem_nacional) ");
		sql.append("LEFT JOIN manifesto_internacional manifestointernacional ON (manifesto.id_manifesto = manifestointernacional.id_manifesto_internacional) ");
		sql.append("LEFT JOIN moeda moedamanifesto ON (manifesto.id_moeda = moedamanifesto.id_moeda) ");
		sql.append("INNER JOIN filial filialOrigemManifesto ON (manifesto.id_filial_origem = filialOrigemManifesto.id_filial) ");
		sql.append("LEFT JOIN moeda moedasinistrodoctoservico ON (sinistrodoctoservico.id_moeda = moedasinistrodoctoservico.id_moeda) ");
		sql.append("INNER JOIN controle_carga controlecarga ON (processosinistro.id_controle_carga = controlecarga.id_controle_carga) ");
		sql.append("INNER JOIN filial filialOrigemControleCarga ON (controlecarga.id_filial_origem = filialOrigemControleCarga.id_filial) ");
		sql.append("LEFT JOIN rota ON (controlecarga.id_rota = rota.id_rota) ");
		sql.append("INNER JOIN docto_servico doctoservico ON (sinistroDoctoServico.id_docto_servico = doctoServico.id_docto_servico) ");
		sql.append("LEFT JOIN cliente clienteRemetente ON (doctoservico.id_cliente_remetente = clienteRemetente.id_cliente) ");
		sql.append("LEFT JOIN cliente clienteDestinatario ON (doctoservico.id_cliente_destinatario = clienteDestinatario.id_cliente) ");
		sql.append("LEFT JOIN docto_servico_indenizacao doctoservicoindenizacao ON (doctoservico.id_docto_servico = doctoServicoindenizacao.id_docto_servico_indenizacao) ");
		sql.append("LEFT JOIN recibo_indenizacao reciboIndenizacao ON (doctoServicoIndenizacao.id_recibo_indenizacao = reciboIndenizacao.id_recibo_indenizacao and reciboIndenizacao.tp_status_indenizacao != ?) ");

		List criterias = new ArrayList();
		criterias.add("C");
		
		sql.append("LEFT JOIN filial filialReciboIndenizacao ON (reciboIndenizacao.id_filial = filialReciboIndenizacao.ID_FILIAL) ");
		sql.append("INNER JOIN filial filialorigemdoctoservico ON (doctoservico.id_filial_origem = filialorigemdoctoservico.id_filial) ");
		//O "servico" passou a ser opcional de acordo com modificacoes em 08/05/2005.
		//Alterado de INNER JOIN para LEFT JOIN
		sql.append("LEFT JOIN servico ON (doctoservico.id_servico = servico.id_servico) ");
		sql.append("LEFT JOIN filial filialdestinodoctoservico ON (doctoservico.id_filial_destino = filialdestinodoctoservico.id_filial) ");
		sql.append("LEFT JOIN conhecimento ON (doctoservico.id_docto_servico = conhecimento.id_conhecimento) ");
		
		//Where...
		sql.append("WHERE 1=1 ");

		Map mapCriterios = getCriterios(criteria, criterias);
		sql.append((String)mapCriterios.get("sql"));
		String parameterValues = (String)mapCriterios.get("paratersValues");

		//Order By....
        sql.append("ORDER BY processosinistro.id_processo_sinistro, ");
        sql.append("processosinistro.nr_processo_sinistro ");
        
        Map sqlStuffs = new HashMap();
        sqlStuffs.put("sql", sql.toString());
        sqlStuffs.put("criterias", criterias);
        sqlStuffs.put("paratersValues", parameterValues);
        
        return sqlStuffs;         
    }
    
    /**
     * Método para calcular a diferença em dias entre dois DateTime.
     * 
     * @param smallerDate
     * @param biggerDate
     * @return Uma string formatada hh:mmm
     */
    public static String getTempoProcesso(String dataSinistro, String dataFechamento) {

    	DateTime dtSinistro = JTFormatUtils.stringToDateTime(dataSinistro);
    	DateTime dtFechamento = new DateTime();
    	
    	if (dataFechamento!=null) {
    		dtFechamento = JTFormatUtils.stringToDateTime(dataFechamento);
    	} else {
    		dtFechamento = JTDateTimeUtils.getDataHoraAtual();
    	}
    	
    	String retorno = null;
		Duration duration = new Duration(dtSinistro.getMillis(), dtFechamento.getMillis());
		if (dtFechamento!=null && dtSinistro!=null) {
			// divisao para gerar segundos
			long seconds = duration.getMillis()/1000;
			// pois neste caso, são apenas horas positivas
			if (seconds > 0) {
				retorno = JTFormatUtils.formatTime(seconds, JTFormatUtils.DAYS, JTFormatUtils.DAYS);	
			}
		}
		return retorno;
    }
    
    
    /**
     * Sub relatorio de Manifestos. 
     * 
     * @param idsDoctoServico
     * @return
     */
    public BigDecimal findValorCustoAdicionalTotal(Long idProcessoSinistro) {
        BigDecimal value = new BigDecimal(0);;
        
        if(idProcessoSinistro==null) {
        	return value;
        } else {
            SqlTemplate sql = new SqlTemplate();
            
            sql.addProjection("SUM(VL_CUSTO_ADICIONAL) AS VLCUSTOADICIONALTOTAL");
            sql.addFrom("CUSTO_ADICIONAL_SINISTRO");
            sql.addCriteria("ID_PROCESSO_SINISTRO", "=", idProcessoSinistro);
            List data = this.getJdbcTemplate().queryForList(sql.getSql(),sql.getCriteria());
            if (data!=null && data.size()>0){
            	if (((Map)data.get(0)).get("VLCUSTOADICIONALTOTAL")!=null) {
            		return (BigDecimal)((Map)data.get(0)).get("VLCUSTOADICIONALTOTAL");
            	}
            }
            return value;
        }
    }
     
    public void configReportDomains(ReportDomainConfig config) {
    	config.configDomainField("TPMODAL","DM_MODAL");
    	config.configDomainField("TPABRANGENCIA","DM_ABRANGENCIA");
    	config.configDomainField("TPMANIFESTO","DM_TIPO_MANIFESTO");
	}
}
