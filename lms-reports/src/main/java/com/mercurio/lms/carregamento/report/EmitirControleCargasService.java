package com.mercurio.lms.carregamento.report;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.contratacaoveiculos.ConstantesContratacaoVeiculos;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração do Relatório de Controle de Cargas - Viagem
 * Especificação técnica 05.01.01.02
 * @author Cesar Gabbardo
 * 
 * @spring.bean id="lms.carregamento.emitirControleCargasService"
 * @spring.property name="reportName" value="com/mercurio/lms/carregamento/report/emitirControleCargas.jasper"
 */
@SuppressWarnings("deprecation")
public class EmitirControleCargasService extends ReportServiceSupport {

	private Long idControleCarga;
	private ControleCargaService controleCargaService;

    /**
     * Método responsável por gerar o relatório. 
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap)parameters;
		Long idControleCarga = tfm.getLong("controleCarga.idControleCarga");
		this.setIdControleCarga(idControleCarga);

        SqlTemplate sql = mountSql(idControleCarga);
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
              
        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
        
        parametersReport.put("sgAcao", tfm.getString("sgAcao"));
        parametersReport.put("dsAcao", tfm.getString("dsAcao"));
        parametersReport.put("sgFilialUsuarioEmissor", tfm.getString("sgFilial"));
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("siglaSimbolo", SessionUtils.getMoedaSessao().getSiglaSimbolo());
        parametersReport.put("idControleCarga", idControleCarga);
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
        
        String modeloContrato = controleCargaService.findOwnerContractModel(idControleCarga);    
        parametersReport.put("blPrintTermoResponsabilidade", ConstantesContratacaoVeiculos.CONTRATO_MOTORISTA_MODELO_1.equals(modeloContrato));
        parametersReport.put("blPrintContratoPrestacao", ConstantesContratacaoVeiculos.CONTRATO_MOTORISTA_MODELO_2.equals(modeloContrato));
        parametersReport.put("blPrintModeloDeclaracaoRet", controleCargaService.validateImprimeDeclaracaoCCViagem(modeloContrato, idControleCarga));
        
        jr.setParameters(parametersReport);
        return jr;		
	}
	
	/**
	 * Método que monta o select para a consulta.
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
    private SqlTemplate mountSql(Long idControleCarga) throws Exception {    	
    	SqlTemplate sql = createSqlTemplate();

    	sql.addProjection("CONTROLE_CARGA.ID_CONTROLE_CARGA", "ID_CONTROLE_CARGA");
    	sql.addProjection("FILIAL.SG_FILIAL", "SG_FILIAL");
    	sql.addProjection("CONTROLE_CARGA.NR_CONTROLE_CARGA", "NR_CONTROLE_CARGA");
    	sql.addProjection("CONTROLE_CARGA.TP_CONTROLE_CARGA", "TP_CONTROLE_CARGA");    	
    	sql.addProjection("FILIAL_SOLICITACAO.SG_FILIAL", "SG_FILIAL_SOLICITACAO");
    	sql.addProjection("SOLICITACAO_CONTRATACAO.NR_SOLICITACAO_CONTRATACAO", "NR_SOLICITACAO_CONTRATACAO");

    	// LMSA-6351
        sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO "
                + "WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_TIPO_CARGA_COMPARTILHADA') "
                + "AND VL_VALOR_DOMINIO = SOLICITACAO_CONTRATACAO.TP_CARGA_COMPARTILHADA)", "DS_CARGA_COMPARTILHADA");

    	sql.addProjection("CONTROLE_CARGA.TP_ROTA_VIAGEM ", "TP_ROTA_VIAGEM");
    	sql.addProjection("ROTA.ID_ROTA", "ID_ROTA");
    	sql.addProjection("ROTA.DS_ROTA", "DS_ROTA");
    	sql.addProjection("ROTA_IDA_VOLTA.ID_ROTA_IDA_VOLTA", "ID_ROTA_IDA_VOLTA");    	
    	sql.addProjection("MEIO_TRANSPORTE.NR_FROTA", "FROTA_VEICULO");
    	sql.addProjection("MEIO_TRANSPORTE.NR_IDENTIFICADOR", "IDENTIFICADOR_VEICULO");
    	sql.addProjection("SEMI_REBOQUE.NR_FROTA", "FROTA_SEMI_REBOQUE");
    	sql.addProjection("SEMI_REBOQUE.NR_IDENTIFICADOR", "IDENTIFICADOR_SEMI_REBOQUE");       
    	sql.addProjection("PROPRIETARIO_PESSOA.NM_PESSOA", "NM_PROPRIETARIO");
    	sql.addProjection("MOTORISTA_PESSOA.NM_PESSOA", "NM_MOTORISTA");
    	sql.addProjection("CONTROLE_CARGA.PS_TOTAL_FROTA", "PS_TOTAL_FROTA");
    	sql.addProjection("CONTROLE_CARGA.PS_TOTAL_AFORADO", "PS_TOTAL_AFORADO");
    	sql.addProjection("CONTROLE_CARGA.PC_OCUPACAO_CALCULADO", "PC_OCUPACAO_CALCULADO");
    	sql.addProjection("CONTROLE_CARGA.PC_OCUPACAO_AFORADO_CALCULADO", "PC_OCUPACAO_AFORADO_CALCULADO");       
    	sql.addProjection("CONTROLE_CARGA.PC_OCUPACAO_INFORMADO", "PC_OCUPACAO_INFORMADO");
    	sql.addProjection("CONTROLE_CARGA.NR_TEMPO_VIAGEM", "NR_TEMPO_VIAGEM");
    	sql.addProjection("CONTROLE_CARGA.DH_PREVISAO_SAIDA", "DH_PREVISAO_SAIDA");
    	sql.addProjection("CONTROLE_CARGA.VL_FRETE_CARRETEIRO", "VL_FRETE_CARRETEIRO");
    	sql.addProjection("CONTROLE_CARGA.BL_EXIGE_CIOT", "BL_EXIGE_CIOT");
    	sql.addProjection("CIOT.NR_CIOT", "NR_CIOT");
    	sql.addProjection("CIOT.NR_COD_VERIFICADOR", "NR_COD_VERIFICADOR");
    	sql.addProjection(queryChaveViagem(), "CHAVE_VIAGEM");

    	sql.addFrom("CONTROLE_CARGA", "CONTROLE_CARGA");
    	sql.addFrom("FILIAL", "FILIAL");
    	sql.addFrom("SOLICITACAO_CONTRATACAO", "SOLICITACAO_CONTRATACAO");
    	sql.addFrom("FILIAL", "FILIAL_SOLICITACAO");    	
    	sql.addFrom("MEIO_TRANSPORTE", "MEIO_TRANSPORTE");
    	sql.addFrom("MEIO_TRANSPORTE", "SEMI_REBOQUE");
    	sql.addFrom("PESSOA", "PROPRIETARIO_PESSOA");
    	sql.addFrom("PESSOA", "MOTORISTA_PESSOA");
    	sql.addFrom("ROTA_IDA_VOLTA", "ROTA_IDA_VOLTA");
    	sql.addFrom("ROTA", "ROTA");
    	sql.addFrom("CIOT_CONTROLE_CARGA", "CIOT_CONTROLE_CARGA");
    	sql.addFrom("CIOT", "CIOT");
      
    	sql.addJoin("CONTROLE_CARGA.ID_FILIAL_ORIGEM", "FILIAL.ID_FILIAL");
    	sql.addJoin("CONTROLE_CARGA.ID_SOLICITACAO_CONTRATACAO", "SOLICITACAO_CONTRATACAO.ID_SOLICITACAO_CONTRATACAO(+)");
    	sql.addJoin("SOLICITACAO_CONTRATACAO.ID_FILIAL", "FILIAL_SOLICITACAO.ID_FILIAL(+)");
    	sql.addJoin("CONTROLE_CARGA.ID_TRANSPORTADO", "MEIO_TRANSPORTE.ID_MEIO_TRANSPORTE(+)");
    	sql.addJoin("CONTROLE_CARGA.ID_SEMI_REBOCADO", "SEMI_REBOQUE.ID_MEIO_TRANSPORTE(+)");
    	sql.addJoin("CONTROLE_CARGA.ID_PROPRIETARIO", "PROPRIETARIO_PESSOA.ID_PESSOA(+)");
    	sql.addJoin("CONTROLE_CARGA.ID_MOTORISTA", "MOTORISTA_PESSOA.ID_PESSOA(+)");
    	sql.addJoin("CONTROLE_CARGA.ID_ROTA_IDA_VOLTA", "ROTA_IDA_VOLTA.ID_ROTA_IDA_VOLTA(+)");
    	sql.addJoin("CONTROLE_CARGA.ID_ROTA", "ROTA.ID_ROTA(+)");
    	sql.addJoin("CONTROLE_CARGA.ID_CONTROLE_CARGA", "CIOT_CONTROLE_CARGA.ID_CONTROLE_CARGA(+)");
    	sql.addJoin("CIOT_CONTROLE_CARGA.ID_CIOT", "CIOT.ID_CIOT(+)");

    	sql.addCriteria("CONTROLE_CARGA.ID_CONTROLE_CARGA", "=", idControleCarga);
        return sql;
    }

	private String queryChaveViagem() {
		String filialOrigem = " lpad(nvl(filial.cd_filial,'0'),3,'0')  ";
		String nrControle = " lpad(controle_carga.nr_controle_carga,6,'0')  ";
		String meioTransporte = " ascii(substr(meio_transporte.nr_identificador,0,1))|| ascii(substr(meio_transporte.nr_identificador,1,1))|| ascii(substr(meio_transporte.nr_identificador,2,1)) || substr(meio_transporte.nr_identificador,4) ";
		String semiReboque = " decode(semi_reboque.id_meio_transporte, null,"+ meioTransporte + ", (ascii(substr(SEMI_REBOQUE.nr_identificador,0,1))|| ascii(substr(SEMI_REBOQUE.nr_identificador,1,1))|| ascii(substr(SEMI_REBOQUE.nr_identificador,2,1)) || substr(SEMI_REBOQUE.nr_identificador,4) ) )";
		String rotaLMS = " lpad(nvl(rota_ida_volta.nr_rota,0),4,'0')  ";
		String vlrPedagio = " lpad(regexp_replace(nvl(controle_carga.vl_pedagio,0), '\\,|\\.', '') ,5,'0') ";
		
		String sqlFlagFiliaisRotasNaoImplantas = " select sum(1) from filial_rota filial_rota_inner inner join filial filial_inner on filial_inner.id_filial = filial_rota_inner.id_filial where filial_rota_inner.id_rota = ROTA.id_ROTA and (filial_inner.dt_implantacao_lms is null or filial_inner.dt_implantacao_lms > sysdate) ";
		return " case when  ("+sqlFlagFiliaisRotasNaoImplantas+") is null then null else regexp_replace("+filialOrigem + " || "+ nrControle + " || "+ meioTransporte + " || " + semiReboque + " || "+ rotaLMS + " || "+ vlrPedagio + " ,'((.){5})', '\\1.') end " ;
		//filial.dt_implantacao_lms < sysdate 
	}

	/**
	 * Método que gera o subrelatorio de Lacres. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirControleCargasLacres() throws Exception {    	
        SqlTemplate sql = createSqlTemplate();
        sql.addProjection("LACRE_CONTROLE_CARGA.NR_LACRES", "NR_LACRES");        
    	sql.addFrom("LACRE_CONTROLE_CARGA", "LACRE_CONTROLE_CARGA");        
    	sql.addCriteria("LACRE_CONTROLE_CARGA.ID_CONTROLE_CARGA", "=", this.getIdControleCarga());
    	sql.addCustomCriteria("LACRE_CONTROLE_CARGA.TP_STATUS_LACRE = 'FE'");
    	sql.addCustomCriteria("LACRE_CONTROLE_CARGA.NR_LACRES IS NOT NULL");
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
    /**
     * Método que gera o subrelatorio de Ocorrencias. 
     * @return
     * @throws Exception
     */
    public JRDataSource executeEmitirControleCargasOcorrencias() throws Exception {        
        return new JREmptyDataSource();
    }    
    
	/**
	 * Método que gera o subrelatorio de Trechos. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirControleCargasTrechos() throws Exception {    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection("CONTROLE_TRECHO.ID_CONTROLE_TRECHO", "ID_CONTROLE_TRECHO");
        sql.addProjection("FILIAL_ORIGEM.SG_FILIAL", "SG_FILIAL_ORIGEM");
        sql.addProjection("FILIAL_DESTINO.SG_FILIAL", "SG_FILIAL_DESTINO");
        sql.addProjection("CONTROLE_TRECHO.DH_PREVISAO_SAIDA", "DH_PREVISAO_SAIDA");
        sql.addProjection("CONTROLE_TRECHO.NR_TEMPO_VIAGEM", "NR_TEMPO_VIAGEM");

        sql.addFrom("CONTROLE_TRECHO", "CONTROLE_TRECHO");
        sql.addFrom("FILIAL", "FILIAL_ORIGEM");
        sql.addFrom("FILIAL", "FILIAL_DESTINO");
		      
        sql.addJoin("CONTROLE_TRECHO.ID_FILIAL_ORIGEM", "FILIAL_ORIGEM.ID_FILIAL");
        sql.addJoin("CONTROLE_TRECHO.ID_FILIAL_DESTINO", "FILIAL_DESTINO.ID_FILIAL");

        sql.addCriteria("CONTROLE_TRECHO.BL_TRECHO_DIRETO", "=", "S");
        sql.addCriteria("CONTROLE_TRECHO.ID_CONTROLE_CARGA", "=", this.getIdControleCarga());
		
        sql.addOrderBy("CONTROLE_TRECHO.DH_PREVISAO_SAIDA");
        sql.addOrderBy("CONTROLE_TRECHO.DH_PREVISAO_CHEGADA");
           	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }

	/**
	 * Método que gera o subrelatorio de Pagamento Postos de passagem. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirControleCargasPagamentoPostos() throws Exception {    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection("PAGTO_PEDAGIO_CC.ID_PAGTO_PEDAGIO_CC", "ID_PAGTO_PEDAGIO_CC");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("TIPO_PAGAM_POSTO_PASSAGEM.DS_TIPO_PAGAM_POSTO_PASSAGEM_I"), "DS_TIPO_PAGAM_POSTO_PASSAGEM");
        sql.addProjection("MOEDA.SG_MOEDA", "SG_MOEDA");
        sql.addProjection("MOEDA.DS_SIMBOLO", "DS_SIMBOLO");
        sql.addProjection("PAGTO_PEDAGIO_CC.VL_PEDAGIO", "VL_PEDAGIO");
        sql.addProjection("PESSOA.NM_PESSOA", "NM_PESSOA_OPERADORA");
        sql.addProjection("CARTAO_PEDAGIO.NR_CARTAO", "NR_CARTAO");
        
        sql.addFrom("PAGTO_PEDAGIO_CC", "PAGTO_PEDAGIO_CC");
        sql.addFrom("CARTAO_PEDAGIO", "CARTAO_PEDAGIO");
        sql.addFrom("OPERADORA_CARTAO_PEDAGIO", "OPERADORA_CARTAO_PEDAGIO");
        sql.addFrom("MOEDA", "MOEDA");
        sql.addFrom("PESSOA", "PESSOA");
        sql.addFrom("TIPO_PAGAM_POSTO_PASSAGEM", "TIPO_PAGAM_POSTO_PASSAGEM");

        sql.addJoin("PAGTO_PEDAGIO_CC.ID_CARTAO_PEDAGIO", "CARTAO_PEDAGIO.ID_CARTAO_PEDAGIO(+)");
        sql.addJoin("PAGTO_PEDAGIO_CC.ID_OPERADORA_CARTAO_PEDAGIO", "OPERADORA_CARTAO_PEDAGIO.ID_OPERADORA_CARTAO_PEDAGIO(+)");
        sql.addJoin("OPERADORA_CARTAO_PEDAGIO.ID_OPERADORA_CARTAO_PEDAGIO", "PESSOA.ID_PESSOA(+)");
        sql.addJoin("PAGTO_PEDAGIO_CC.ID_MOEDA", "MOEDA.ID_MOEDA");
        sql.addJoin("PAGTO_PEDAGIO_CC.ID_TIPO_PAGAM_POSTO_PASSAGEM", "TIPO_PAGAM_POSTO_PASSAGEM.ID_TIPO_PAGAM_POSTO_PASSAGEM");
        
        sql.addCriteria("PAGTO_PEDAGIO_CC.ID_CONTROLE_CARGA", "=", this.getIdControleCarga());
		     	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }     
    
	/**
	 * Método que gera o subrelatorio de Pontos de Parada. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirControleCargasPontosParada() throws Exception {    	
        SqlTemplate sql = createSqlTemplate();
                
        sql.addProjection("PONTO_PARADA.ID_PONTO_PARADA", "ID_PONTO_PARADA");
        sql.addProjection("PONTO_PARADA_TRECHO.ID_PONTO_PARADA_TRECHO", "ID_PONTO_PARADA_TRECHO");
        sql.addProjection("FILIAL_ORIGEM.SG_FILIAL", "SG_FILIAL_ORIGEM");
        sql.addProjection("FILIAL_DESTINO.SG_FILIAL", "SG_FILIAL_DESTINO");
        sql.addProjection("RODOVIA.SG_RODOVIA", "SG_RODOVIA");
        sql.addProjection("RODOVIA.DS_RODOVIA", "DS_RODOVIA");
        sql.addProjection("PONTO_PARADA.NR_KM", "NR_KM");
        sql.addProjection("MUNICIPIO.NM_MUNICIPIO", "NM_MUNICIPIO");
        sql.addProjection("UNIDADE_FEDERATIVA.SG_UNIDADE_FEDERATIVA", "SG_UNIDADE_FEDERATIVA");
        sql.addProjection("PONTO_PARADA_TRECHO.NR_TEMPO_PARADA", "NR_TEMPO_PARADA");

        sql.addFrom("CONTROLE_TRECHO", "CONTROLE_TRECHO");
        sql.addFrom("TRECHO_ROTA_IDA_VOLTA", "TRECHO_ROTA_IDA_VOLTA");
        sql.addFrom("FILIAL_ROTA", "FILIAL_ROTA_ORIGEM");
        sql.addFrom("FILIAL_ROTA", "FILIAL_ROTA_DESTINO");
        sql.addFrom("FILIAL", "FILIAL_ORIGEM");
        sql.addFrom("FILIAL", "FILIAL_DESTINO");
        sql.addFrom("PONTO_PARADA_TRECHO", "PONTO_PARADA_TRECHO");
        sql.addFrom("PONTO_PARADA", "PONTO_PARADA");
        sql.addFrom("RODOVIA", "RODOVIA");
        sql.addFrom("MUNICIPIO", "MUNICIPIO");
        sql.addFrom("UNIDADE_FEDERATIVA", "UNIDADE_FEDERATIVA");
      
        sql.addJoin("CONTROLE_TRECHO.ID_TRECHO_ROTA_IDA_VOLTA", "TRECHO_ROTA_IDA_VOLTA.ID_TRECHO_ROTA_IDA_VOLTA");
        sql.addJoin("TRECHO_ROTA_IDA_VOLTA.ID_FILIAL_ROTA_ORIGEM", "FILIAL_ROTA_ORIGEM.ID_FILIAL_ROTA");
        sql.addJoin("FILIAL_ROTA_ORIGEM.ID_FILIAL", "FILIAL_ORIGEM.ID_FILIAL");
        sql.addJoin("TRECHO_ROTA_IDA_VOLTA.ID_FILIAL_ROTA_DESTINO", "FILIAL_ROTA_DESTINO.ID_FILIAL_ROTA");
        sql.addJoin("FILIAL_ROTA_DESTINO.ID_FILIAL", "FILIAL_DESTINO.ID_FILIAL");
        sql.addJoin("TRECHO_ROTA_IDA_VOLTA.ID_TRECHO_ROTA_IDA_VOLTA", "PONTO_PARADA_TRECHO.ID_TRECHO_ROTA_IDA_VOLTA");
        sql.addJoin("PONTO_PARADA_TRECHO.ID_PONTO_PARADA", "PONTO_PARADA.ID_PONTO_PARADA");
        sql.addJoin("PONTO_PARADA.ID_RODOVIA", "RODOVIA.ID_RODOVIA(+)");
        sql.addJoin("PONTO_PARADA.ID_MUNICIPIO", "MUNICIPIO.ID_MUNICIPIO");
        sql.addJoin("MUNICIPIO.ID_UNIDADE_FEDERATIVA", "UNIDADE_FEDERATIVA.ID_UNIDADE_FEDERATIVA");
        
        sql.addCriteria("PONTO_PARADA_TRECHO.DT_VIGENCIA_INICIAL", "<=", JTDateTimeUtils.getDataAtual());
        sql.addCriteria("PONTO_PARADA_TRECHO.DT_VIGENCIA_FINAL", ">=", JTDateTimeUtils.getDataAtual());
        sql.addCriteria("CONTROLE_TRECHO.ID_CONTROLE_CARGA", "=", this.getIdControleCarga());
		
        sql.addOrderBy("TRECHO_ROTA_IDA_VOLTA.HR_SAIDA");
        sql.addOrderBy("TRECHO_ROTA_IDA_VOLTA.NR_TEMPO_VIAGEM");
           	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }    
    
    
	/**
	 * Método que gera o subrelatorio de Motivo de Ponto de Parada. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirControleCargasMotivos(Long idPontoParadaTrecho) throws Exception {    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection(PropertyVarcharI18nProjection.createProjection("MOTIVO_PARADA.DS_MOTIVO_PARADA_I"), "DS_MOTIVO_PARADA");

        sql.addFrom("MOTIVO_PARADA_PONTO_TRECHO", "MOTIVO_PARADA_PONTO_TRECHO");
        sql.addFrom("MOTIVO_PARADA", "MOTIVO_PARADA");
             
        sql.addJoin("MOTIVO_PARADA_PONTO_TRECHO.ID_MOTIVO_PARADA", "MOTIVO_PARADA.ID_MOTIVO_PARADA");
        
        sql.addCriteria("MOTIVO_PARADA_PONTO_TRECHO.DT_VIGENCIA_INICIAL", "<=", JTDateTimeUtils.getDataAtual());
        sql.addCriteria("MOTIVO_PARADA_PONTO_TRECHO.DT_VIGENCIA_FINAL", ">=", JTDateTimeUtils.getDataAtual());

        sql.addCriteria("MOTIVO_PARADA_PONTO_TRECHO.ID_PONTO_PARADA_TRECHO", "=", idPontoParadaTrecho);
		     	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }       
    
	/**
	 * Método que gera o subrelatorio de Manifesto. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirControleCargasManifestos() throws Exception {    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection("MANIFESTO.ID_MANIFESTO", "ID_MANIFESTO");
        sql.addProjection("FILIAL_ORIGEM.SG_FILIAL", "SG_FILIAL_ORIGEM");
        sql.addProjection("FILIAL_DESTINO.SG_FILIAL", "SG_FILIAL_DESTINO");
        sql.addProjection("MANIFESTO.TP_MANIFESTO", "TP_MANIFESTO_DESCRIPTION");
        sql.addProjection("MANIFESTO.TP_MANIFESTO", "TP_MANIFESTO");
        sql.addProjection("MANIFESTO.TP_ABRANGENCIA", "TP_ABRANGENCIA");
        sql.addProjection("MANIFESTO.TP_MANIFESTO_VIAGEM", "TP_MANIFESTO_VIAGEM");
        sql.addProjection("MANIFESTO.TP_MANIFESTO_ENTREGA", "TP_MANIFESTO_ENTREGA");
        sql.addProjection("MANIFESTO.PS_TOTAL_MANIFESTO", "PS_TOTAL_MANIFESTO");
        sql.addProjection("MANIFESTO.PS_TOTAL_AFORADO_MANIFESTO", "PS_TOTAL_AFORADO_MANIFESTO");
        sql.addProjection("MOEDA.SG_MOEDA", "SG_MOEDA");
        sql.addProjection("MOEDA.DS_SIMBOLO", "DS_SIMBOLO");
        sql.addProjection("MANIFESTO.VL_TOTAL_MANIFESTO", "VL_TOTAL_MANIFESTO");
        
        sql.addFrom("MANIFESTO", "MANIFESTO");
        sql.addFrom("MOEDA", "MOEDA");
        sql.addFrom("FILIAL", "FILIAL_ORIGEM");
        sql.addFrom("FILIAL", "FILIAL_DESTINO");

        sql.addJoin("MANIFESTO.ID_MOEDA", "MOEDA.ID_MOEDA");
        sql.addJoin("MANIFESTO.ID_FILIAL_ORIGEM", "FILIAL_ORIGEM.ID_FILIAL");
        sql.addJoin("MANIFESTO.ID_FILIAL_DESTINO", "FILIAL_DESTINO.ID_FILIAL");
        
        sql.addCustomCriteria("MANIFESTO.TP_STATUS_MANIFESTO IN ('EF', 'CC', 'ME', 'EV')");
        sql.addCriteria("MANIFESTO.ID_CONTROLE_CARGA", "=", this.getIdControleCarga());
        
        sql.addOrderBy("SG_FILIAL_ORIGEM");
       		     	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }       
    
	/**
	 * Método que gera o subrelatorio de Números Manifestos. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirControleCargasNumeroManifestos(Long idManifesto, String tpManifesto, 
    															   	String tpAbrangencia) throws Exception {    	
        SqlTemplate sql = createSqlTemplate();

        if(tpManifesto.equals("E")) {
        	sql.addProjection("NR_MANIFESTO_ENTREGA", "NR_MANIFESTO");
	        sql.addFrom("MANIFESTO_ENTREGA", "MANIFESTO_ENTREGA");
	        sql.addCriteria("MANIFESTO_ENTREGA.ID_MANIFESTO_ENTREGA", "=", idManifesto);
        } else if(tpManifesto.equals("V") && (tpAbrangencia != null && tpAbrangencia.equals("N"))) {
        	sql.addProjection("NR_MANIFESTO_ORIGEM", "NR_MANIFESTO");
	        sql.addFrom("MANIFESTO_VIAGEM_NACIONAL", "MANIFESTO_VIAGEM_NACIONAL");
	        sql.addCriteria("MANIFESTO_VIAGEM_NACIONAL.ID_MANIFESTO_VIAGEM_NACIONAL", "=", idManifesto);
        } else if(tpManifesto.equals("V") && (tpAbrangencia != null && tpAbrangencia.equals("I"))) {
        	sql.addProjection("NR_MANIFESTO_INT", "NR_MANIFESTO");
	        sql.addFrom("MANIFESTO_INTERNACIONAL", "MANIFESTO_INTERNACIONAL");
	        sql.addCriteria("MANIFESTO_INTERNACIONAL.ID_MANIFESTO_INTERNACIONAL", "=", idManifesto);
        }
        
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }    
    
	/**
	 * Método que gera o subrelatorio de Quantidade Documentos. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirControleCargasQuantidadeDocumentos(Long idManifesto, String tpManifesto, 
    																	String tpAbrangencia) throws Exception {    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection("COUNT(*)", "QUANTIDADE");
        
        if(tpManifesto.equals("E")) {
	        sql.addFrom("MANIFESTO_ENTREGA", "MANIFESTO_ENTREGA");
	        sql.addFrom("MANIFESTO_ENTREGA_DOCUMENTO", "MANIFESTO_ENTREGA_DOCUMENTO");        
	        sql.addJoin("MANIFESTO_ENTREGA.ID_MANIFESTO_ENTREGA", "MANIFESTO_ENTREGA_DOCUMENTO.ID_MANIFESTO_ENTREGA");
	        sql.addCriteria("MANIFESTO_ENTREGA.ID_MANIFESTO_ENTREGA", "=", idManifesto);
        } else if(tpManifesto.equals("V") && (tpAbrangencia != null && tpAbrangencia.equals("N"))) {
	        sql.addFrom("MANIFESTO_VIAGEM_NACIONAL", "MANIFESTO_VIAGEM_NACIONAL");
	        sql.addFrom("MANIFESTO_NACIONAL_CTO", "MANIFESTO_NACIONAL_CTO");        
	        sql.addJoin("MANIFESTO_VIAGEM_NACIONAL.ID_MANIFESTO_VIAGEM_NACIONAL", "MANIFESTO_NACIONAL_CTO.ID_MANIFESTO_VIAGEM_NACIONAL");
	        sql.addCriteria("MANIFESTO_VIAGEM_NACIONAL.ID_MANIFESTO_VIAGEM_NACIONAL", "=", idManifesto);
        } else if(tpManifesto.equals("V") && (tpAbrangencia != null && tpAbrangencia.equals("I"))) {
	        sql.addFrom("MANIFESTO_INTERNACIONAL", "MANIFESTO_INTERNACIONAL");
	        sql.addFrom("MANIFESTO_INTERNAC_CTO", "MANIFESTO_INTERNAC_CTO");        
	        sql.addJoin("MANIFESTO_INTERNACIONAL.ID_MANIFESTO_INTERNACIONAL", "MANIFESTO_INTERNAC_CTO.ID_MANIFESTO_INTERNACIONAL");
	        sql.addCriteria("MANIFESTO_INTERNACIONAL.ID_MANIFESTO_INTERNACIONAL", "=", idManifesto);
        }
        
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }  
    
	/**
	 * Método que gera o subrelatorio de MDA. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirControleCargasMDAs(Long idManifesto) throws Exception {    	
        SqlTemplate sql = createSqlTemplate();
        
        sql.addProjection("FILIAL.SG_FILIAL", "SG_FILIAL");
        sql.addProjection("DOCTO_SERVICO.NR_DOCTO_SERVICO", "NR_MDA");

        sql.addFrom("PRE_MANIFESTO_DOCUMENTO", "PRE_MANIFESTO_DOCUMENTO");
        sql.addFrom("DOCTO_SERVICO", "DOCTO_SERVICO");
        sql.addFrom("MDA", "MDA");
        sql.addFrom("FILIAL", "FILIAL");
             
        sql.addJoin("PRE_MANIFESTO_DOCUMENTO.ID_DOCTO_SERVICO", "DOCTO_SERVICO.ID_DOCTO_SERVICO");
        sql.addJoin("DOCTO_SERVICO.ID_DOCTO_SERVICO", "MDA.ID_MDA");
        sql.addJoin("DOCTO_SERVICO.ID_FILIAL_ORIGEM", "FILIAL.ID_FILIAL");
        
        sql.addCriteria("DOCTO_SERVICO.TP_DOCUMENTO_SERVICO", "=", "MDA");
        sql.addCriteria("PRE_MANIFESTO_DOCUMENTO.ID_MANIFESTO", "=", idManifesto);
		     	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }  
    
    /**
     * Método que gera o subrelatorio de Controle de Cargas - Gerenciamento de riscos. 
     * @return
     * @throws Exception
     */
    public JRDataSource executeEmitirControleCargasGerRiscos() throws Exception {
        SqlTemplate sql = createSqlTemplate();
        
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("EXIGENCIA_GER_RISCO.DS_RESUMIDA_I"), "DS_RESUMIDA");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("EXIGENCIA_GER_RISCO.DS_COMPLETA_I"), "DS_COMPLETA");        

        sql.addFrom("CONTROLE_CARGA", "CONTROLE_CARGA");
        sql.addFrom("SOLIC_MONIT_PREVENTIVO", "SOLIC_MONIT_PREVENTIVO");
        sql.addFrom("FILIAL", "FILIAL");
        sql.addFrom("EXIGENCIA_SMP", "EXIGENCIA_SMP");
        sql.addFrom("EXIGENCIA_GER_RISCO", "EXIGENCIA_GER_RISCO");

        sql.addJoin("CONTROLE_CARGA.ID_CONTROLE_CARGA", "SOLIC_MONIT_PREVENTIVO.ID_CONTROLE_CARGA");
        sql.addJoin("FILIAL.ID_FILIAL", "SOLIC_MONIT_PREVENTIVO.ID_FILIAL");
        sql.addJoin("SOLIC_MONIT_PREVENTIVO.ID_SOLIC_MONIT_PREVENTIVO", "EXIGENCIA_SMP.ID_SOLIC_MONIT_PREVENTIVO");
        sql.addJoin("EXIGENCIA_SMP.ID_EXIGENCIA_GER_RISCO", "EXIGENCIA_GER_RISCO.ID_EXIGENCIA_GER_RISCO");

        sql.addCriteria("FILIAL.ID_FILIAL", "=", SessionUtils.getFilialSessao().getIdFilial());
        sql.addCriteria("CONTROLE_CARGA.ID_CONTROLE_CARGA", "=", this.getIdControleCarga());
        sql.addCriteria("SOLIC_MONIT_PREVENTIVO.TP_STATUS_SMP", "<>", "CA");

        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
        
    /**
     * Configura Dominios
     */
	@Override
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_CONTROLE_CARGA", "DM_TIPO_CONTROLE_CARGAS");
		config.configDomainField("TP_ROTA_VIAGEM", "DM_TIPO_ROTA_VIAGEM_CC");		
		config.configDomainField("TP_MANIFESTO_DESCRIPTION", "DM_TIPO_MANIFESTO");
		config.configDomainField("TP_MANIFESTO_VIAGEM", "DM_TIPO_MANIFESTO_VIAGEM");
		config.configDomainField("TP_MANIFESTO_ENTREGA", "DM_TIPO_MANIFESTO_ENTREGA");
	}       
	
    /**
     * Método q calcula o valor total das mercadorias de um Controle de Carga, podendo filtrar apenas documentos
     * de determinado modal, de determinada abrangência e que tenham ou não seguro do cliente.
     * 
     * @param idControleCarga
     * @param blSeguroMercurio
     * @param tpModal
     * @param tpAbrangencia
     * @return
     */
    public BigDecimal generateCalculaValorTotalMercadoriaControleCarga(Long idControleCarga, Boolean blSeguroMercurio,
    														   String tpModal, String tpAbrangencia) {    	
    	
    	return getControleCargaService().generateCalculaValorTotalMercadoriaControleCarga(
    			idControleCarga, blSeguroMercurio, tpModal, tpAbrangencia, null);
    }

	public Long getIdControleCarga() {
		return idControleCarga;
	}

	public void setIdControleCarga(Long idControleCarga) {
		this.idControleCarga = idControleCarga;
	}

	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	
    public JRDataSource executeEmitirTermoResponsabilidadeTransporteCargas(Object[] parameters) throws SQLException {    	
    	SqlTemplate sql = createSqlTemplate();
    	
    	sql.addProjection("PESSOA.tp_pessoa", "tp_pessoa");
    	sql.addProjection("CONTROLE_CARGA.ID_CONTROLE_CARGA", "id_controle_carga");
        sql.addFrom("PESSOA", "PESSOA");
        sql.addFrom("CONTROLE_CARGA", "CONTROLE_CARGA");
        sql.addFrom("PROPRIETARIO", "PROPRIETARIO");
        sql.addJoin("PROPRIETARIO.id_proprietario", "PESSOA.id_pessoa");
        sql.addJoin("CONTROLE_CARGA.id_proprietario", "PESSOA.id_pessoa");
        sql.addCriteria("CONTROLE_CARGA.ID_CONTROLE_CARGA", "=", parameters[0]);
        sql.addCustomCriteria("PROPRIETARIO.tp_proprietario = 'E'");
        
        JRDataSource jrdt = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource(); 
        if (jrdt == null) {
        	jrdt = new JRMapCollectionDataSource(null);
        }
        
        return jrdt;
    }     
    
    public JRDataSource executeEmitirModeloDeclaracaoRetencaoTransporteCargas(Object[] parameters) throws SQLException {    	
    	SqlTemplate sql = createSqlTemplate();
    	
    	Calendar calendar = Calendar.getInstance();
    	int ano = calendar.get(Calendar.YEAR);
    	int dia = calendar.get(Calendar.DAY_OF_MONTH);
    	
    	Locale local = new Locale("pt","BR");
    	DateFormat dateFormat = new SimpleDateFormat("MMMM",local); 
    	String mes = dateFormat.format(calendar.getTime()).toUpperCase();
    	
    	sql.addProjection("PROPRIETARIO.nr_pis", "nr_pis");//Se retorno nulo, vazio ou 0 (zero) deixar campo em branco.
    	sql.addProjection("PROPRIETARIO.nr_dependentes", "nr_dependentes");
    	sql.addProjection("PESSOA.nm_pessoa", "nm_pessoa");//Formatar o nome em letras maiúsculas
    	sql.addProjection("PESSOA.tp_identificacao", "tp_identificacao");//Formatar a descrição em letras maiúsculas
    	sql.addProjection("PESSOA.nr_identificacao", "nr_identificacao");//Formatar de acordo com o tipo [2], conforme segue.  //“CPF” NNN.NNN.NNN-NN //“CNPJ” NN.NNN.NNN/NNNN-NN
    	sql.addProjection("MUNICIPIO.nm_municipio", "nm_municipio");
    	sql.addProjection("MUNICIPIO_F.nm_municipio", "nm_municipio_f");
    	sql.addProjection("" + ano, "ano");
    	sql.addProjection("" + dia, "dia");
    	sql.addProjection("'" + mes+"'", "mes");
    	
    	sql.addProjection("(OPERACAO_SERVICO_LOCALIZA.nr_tempo_entrega / 1440) || ' dia' ", "nr_tempo_entrega");
    	//Se retorno nulo, vazio ou 0 (zero) apresentar 1 dia. Sempre concatenar o numeral de retorno ou 
        //fixo 1 com a palavra “dia”. Exemplo: “1 dia”.
    	
    	sql.addFrom("CONTROLE_CARGA", "CONTROLE_CARGA");
        sql.addFrom("PROPRIETARIO", "PROPRIETARIO");
        sql.addFrom("PESSOA", "PESSOA");
        sql.addFrom("PESSOA", "PESSOA_FILIAL");
        sql.addFrom("PESSOA", "PESSOA_FILIAL_DESTINO");
        sql.addFrom("MUNICIPIO", "MUNICIPIO");
        sql.addFrom("MUNICIPIO", "MUNICIPIO_F");
        sql.addFrom("ROTA_INTERVALO_CEP", "ROTA_INTERVALO_CEP");
        sql.addFrom("ENDERECO_PESSOA", "ENDERECO_PESSOA");
        sql.addFrom("ENDERECO_PESSOA", "ENDERECO_PESSOA_F_D");
        sql.addFrom("MUNICIPIO_FILIAL", "MUNICIPIO_FILIAL");
        sql.addFrom("OPERACAO_SERVICO_LOCALIZA", "OPERACAO_SERVICO_LOCALIZA");
        
        sql.addJoin("PROPRIETARIO.id_proprietario", "CONTROLE_CARGA.id_proprietario");
        sql.addJoin("PESSOA.id_pessoa", "CONTROLE_CARGA.id_proprietario");
        sql.addJoin("CONTROLE_CARGA.id_filial_destino", "PESSOA_FILIAL_DESTINO.id_pessoa");
        sql.addJoin("PESSOA_FILIAL_DESTINO.id_endereco_pessoa", "ENDERECO_PESSOA_F_D.id_endereco_pessoa");
        sql.addJoin("ENDERECO_PESSOA_F_D.id_municipio", "MUNICIPIO.id_municipio");
        sql.addJoin("PESSOA_FILIAL.id_pessoa", "MUNICIPIO_FILIAL.id_filial");
        sql.addJoin("PESSOA_FILIAL.id_endereco_pessoa", "ENDERECO_PESSOA.id_endereco_pessoa");
        sql.addJoin("ENDERECO_PESSOA.id_municipio", "MUNICIPIO_FILIAL.id_municipio");
        sql.addJoin("MUNICIPIO_FILIAL.id_municipio", "MUNICIPIO_F.id_municipio");
        sql.addJoin("MUNICIPIO_FILIAL.id_municipio_filial", "OPERACAO_SERVICO_LOCALIZA.id_municipio_filial");		
        sql.addJoin("PESSOA_FILIAL.id_pessoa", "MUNICIPIO_FILIAL.id_filial");
        
        sql.addCriteria("CONTROLE_CARGA.ID_CONTROLE_CARGA", "=", parameters[0]);
        
        sql.addCriteria("PESSOA_FILIAL.id_pessoa", "=", SessionUtils.getFilialSessao().getIdFilial());
        sql.addCustomCriteria("MUNICIPIO_FILIAL.bl_padrao_mcd = 'S' ");
        sql.addCustomCriteria("OPERACAO_SERVICO_LOCALIZA.tp_operacao IN ('A','E')");
        
        sql.addCustomCriteria("ROTA_INTERVALO_CEP.dt_vigencia_final >= sysdate");
        sql.addCustomCriteria("MUNICIPIO_FILIAL.dt_vigencia_final >= sysdate");
        sql.addCustomCriteria("OPERACAO_SERVICO_LOCALIZA.dt_vigencia_final >= sysdate");
                
        sql.addCustomCriteria("ROWNUM = 1");  
     	 
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }	

}
