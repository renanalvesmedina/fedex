package com.mercurio.lms.seguros.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;
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
 * @spring.bean id="lms.seguros.emitirRelatorioProcessoSinistroDetalhadoService"
 * @spring.property name="reportName" value="com/mercurio/lms/seguros/report/emitirRelatorioProcessoSinistro.jasper"
 */

public class EmitirRelatorioProcessoSinistroDetalhadoService extends EmitirRelatorioProcessoSinistroService{


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
    	sql.append("processosinistro.nr_processo_sinistro AS nrProcessoSinistro, ");
    	sql.append("filialorigemcontrolecarga.sg_filial AS sgFilialOrigemControleCarga, ");
    	sql.append("controlecarga.nr_controle_carga AS nrControleCarga, ");
    	sql.append("rota.ds_rota AS dsRota, ");
    	sql.append("(municipio.nm_municipio || ' - ' || unidadefederativa.sg_unidade_federativa) AS municipiouf, ");
    	sql.append("processosinistro.tp_local_sinistro AS tpLocalSinistro, "); 
		sql.append("pessoaFilial.nm_fantasia AS nmFantasia, ");
    	sql.append("(rodovia.ds_rodovia || ' ' || processosinistro.nr_km_sinistro) AS dsrodoviasinistro, ");
    	sql.append("pessoaaeroporto.nm_pessoa AS nmPessoaAeroporto, ");
    	sql.append("processosinistro.ds_local_sinistro AS dsLocalSinistro, ");
    	sql.append("(meiotransporte.nr_frota || '-' || meiotransporte.nr_identificador) AS veiculo, ");
    	sql.append("tiposeguro.sg_tipo AS sgTipoSeguro, ");
    	sql.append(PropertyVarcharI18nProjection.createProjection("tiposinistro.ds_tipo_i", "dsTipoSinistro")+", ");
    	sql.append("processosinistro.nr_boletim_ocorrencia AS nrBoletimOcorrencia, ");
    	sql.append("moeda.sg_moeda AS sgmoeda, ");
    	sql.append("moeda.ds_simbolo AS dsSimbolo, ");
    	sql.append("processosinistro.dh_fechamento AS dhFechamento, ");
    	sql.append("processosinistro.dh_sinistro AS dhSinistro, ");
    	sql.append("filialorigemmanifesto.sg_filial AS sgFilial, ");
    	sql.append("manifesto.nr_pre_manifesto AS nrPreManifesto, ");
    	sql.append("manifesto.tp_manifesto AS tpManifesto, ");
    	sql.append("manifesto.tp_abrangencia AS tpAbrangenciaManifesto, ");
    	sql.append("moedamanifesto.ds_simbolo AS dsSimboloManifesto, ");
    	sql.append("moedamanifesto.sg_moeda AS sgMoedaManifesto, ");
    	sql.append("manifesto.vl_total_manifesto AS vlTotalManifesto, ");
    	sql.append("manifestoentrega.nr_manifesto_entrega AS nrManifestoEntrega, ");
    	sql.append("manifestoviagemnacional.nr_manifesto_origem AS nrManifestoOrigem, ");
    	sql.append("manifestointernacional.nr_manifesto_int AS nrManifestoInt, ");
    	sql.append("doctoservico.tp_documento_servico AS tpDocumentoServico, ");
    	sql.append("doctoservico.nr_docto_servico AS nrDoctoServico, ");
    	sql.append("filialdestinodoctoservico.sg_filial AS sgFilialDestinoDoctoServico, ");
    	sql.append("filialorigemdoctoservico.sg_filial AS sgFilialOrigemDoctoServico, ");
    	sql.append("servico.tp_modal AS TPMODAL, ");
    	sql.append("servico.tp_abrangencia AS TPABRANGENCIA, ");
    	sql.append("doctoservico.qt_volumes AS qtVolumes, ");
    	sql.append("doctoservico.ps_real AS psReal, ");
    	sql.append("moedadoctoservico.ds_simbolo AS dsSimboloDoctoServico, ");
    	sql.append("moedadoctoservico.sg_moeda AS sgMoedaDoctoServico, ");
    	sql.append("doctoservico.vl_mercadoria AS vlMercadoria, ");
    	sql.append("moedasinistrodoctoservico.ds_simbolo as dsSimboloSDS, ");
    	sql.append("moedasinistrodoctoservico.sg_moeda AS sgMoedaSDS, ");
    	sql.append("sinistrodoctoservico.vl_prejuizo AS vlPrejuizoSDS, ");
    	sql.append("reciboIndenizacao.nr_recibo_indenizacao AS nrReciboIndenizacao, ");
    	sql.append("filialReciboIndenizacao.sg_filial AS sgFilialReciboIndenizacao, ");
    	sql.append("doctoservicoindenizacao.vl_indenizado AS vlIndenizacao, ");
    	sql.append("reciboIndenizacao.dt_pagamento_efetuado AS dtPagamentoEfetuado, ");
    	
    	//From...
    	sql.append("FROM processo_sinistro processosinistro ");
    	sql.append("INNER JOIN tipo_seguro tiposeguro ON (processosinistro.id_tipo_seguro = tiposeguro.id_tipo_seguro) ");
		sql.append("INNER JOIN tipo_sinistro tiposinistro ON (processosinistro.id_tipo_sinistro = tiposinistro.id_tipo_sinistro) ");
    	sql.append("INNER JOIN municipio ON (processosinistro.id_municipio = municipio.id_municipio) ");
    	sql.append("INNER JOIN unidade_federativa unidadeFederativa ON (municipio.id_unidade_federativa = unidadefederativa.id_unidade_federativa) ");
    	sql.append("LEFT JOIN moeda ON (processosinistro.id_moeda = moeda.id_moeda) ");
    	sql.append("LEFT JOIN aeroporto ON (processosinistro.id_aeroporto = aeroporto.id_aeroporto) ");
    	sql.append("LEFT JOIN pessoa pessoaAeroporto ON (aeroporto.ID_AEROPORTO = pessoaAeroporto.id_pessoa) ");
    	sql.append("LEFT JOIN filial filial ON (processosinistro.id_filial = filial.id_filial) ");
    	sql.append("LEFT JOIN pessoa pessoaFilial ON (filial.id_filial = pessoaFilial.id_pessoa) ");
    	sql.append("LEFT JOIN regional_filial regionalFilial ON (filial.id_filial = regionalFilial.id_filial AND ((regionalfilial.dt_vigencia_inicial <= CURRENT_DATE) AND (regionalfilial.dt_vigencia_final >= CURRENT_DATE))) ");
    	sql.append("LEFT JOIN rodovia ON (processosinistro.id_rodovia = rodovia.id_rodovia) ");
    	sql.append("LEFT JOIN meio_transporte meiotransporte ON (processosinistro.id_meio_transporte = meiotransporte.id_meio_transporte) ");
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
		sql.append("LEFT JOIN docto_servico_indenizacao doctoservicoindenizacao ON (doctoservico.id_docto_servico = doctoServicoindenizacao.id_docto_servico) ");
		sql.append("LEFT JOIN recibo_indenizacao reciboIndenizacao ON (doctoServicoIndenizacao.id_recibo_indenizacao = reciboIndenizacao.id_recibo_indenizacao and reciboIndenizacao.tp_status_indenizacao <> ?) ");
		List criterias = new ArrayList();
		criterias.add("C");

		sql.append("LEFT JOIN filial filialReciboIndenizacao ON (reciboIndenizacao.id_filial = filialReciboIndenizacao.ID_FILIAL) ");
		sql.append("INNER JOIN filial filialorigemdoctoservico ON (doctoservico.id_filial_origem = filialorigemdoctoservico.id_filial) ");
		//O "servico" passou a ser opcional de acordo com modificacoes em 08/05/2005.
		//Alterado de INNER JOIN para LEFT JOIN
		sql.append("LEFT JOIN servico ON (doctoservico.id_servico = servico.id_servico) ");
		sql.append("LEFT JOIN filial filialdestinodoctoservico ON (doctoservico.id_filial_destino = filialdestinodoctoservico.id_filial) ");
		sql.append("LEFT JOIN conhecimento ON (doctoservico.id_docto_servico = conhecimento.id_conhecimento) ");
		sql.append("LEFT JOIN moeda moedaDoctoServico ON (doctoservico.id_moeda = moedadoctoservico.id_moeda) ");
		
		// a boa prática diz que tem que ter um where
		sql.append("WHERE 1=1 ");
		
		Map mapCriterios = getCriterios(criteria, criterias);
		sql.append((String)mapCriterios.get("sql"));
		String parameterValues = (String)mapCriterios.get("paratersValues");
		
		//Order By....
        sql.append("ORDER BY processosinistro.id_processo_sinistro, ");
        sql.append("processosinistro.nr_processo_sinistro, ");
        sql.append("filialorigemmanifesto.sg_filial, ");
        sql.append("manifesto.id_manifesto, ");
        sql.append("manifesto.nr_pre_manifesto, ");
        sql.append("doctoservico.tp_documento_servico, ");
        sql.append("filialorigemdoctoservico.sg_filial, ");
        sql.append("doctoservico.nr_docto_servico ");
        
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
        BigDecimal value = new BigDecimal(0);
        
        if(idProcessoSinistro==null) {
        	return value;
        } else {
            SqlTemplate sql = new SqlTemplate();
            
            sql.addProjection("SUM(VL_CUSTO_ADICIONAL) AS VLCUSTOADICIONALTOTAL");
            sql.addFrom("CUSTO_ADICIONAL_SINISTRO");
            sql.addCriteria("ID_PROCESSO_SINISTRO", "=", idProcessoSinistro);
            List data = this.getJdbcTemplate().queryForList(sql.getSql(),sql.getCriteria());
            if (data!=null && data.size()>0){
            	ListOrderedMap listOrderedMap = (ListOrderedMap) data.get(0);
            	
            	if (listOrderedMap.get("VLCUSTOADICIONALTOTAL")!=null) {
            		return (BigDecimal) listOrderedMap.get("VLCUSTOADICIONALTOTAL");
            	} else { 
            		return new BigDecimal(0);
            	}
            }
            return value;
        }
    }
     
    public void configReportDomains(ReportDomainConfig config) {
    	config.configDomainField("TPMODAL","DM_MODAL");
    	config.configDomainField("TPABRANGENCIA","DM_ABRANGENCIA");
    	config.configDomainField("TPMANIFESTO","DM_TIPO_MANIFESTO");
    	config.configDomainField("TPABRANGENCIAMANIFESTO","DM_ABRANGENCIA");
	}
}