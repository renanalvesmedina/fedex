package com.mercurio.lms.expedicao.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.CtoAwbService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Ruhan
 * 
 * @spring.bean id="lms.expedicao.emitirEspelhoAwbService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/emitirEspelhoAwb.jasper"
 */
public class EmitirEspelhoAwbService extends ReportServiceSupport {

	private static final long FIRST_ROW_DOC_FL_AUX = 25L;
	
	private CtoAwbService ctoAwbService;
	private AwbService awbService;
	
	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap)parameters;

        String sql = createMainQuery();
        
        List<Object> params = new ArrayList<Object>();
        
        if(tfm.getLong("awb.idAwb") != null){
        	params.add(tfm.getLong("awb.idAwb"));
        }
        
        JRReportDataObject jr = executeQuery(sql, params.toArray());
        
        // Seta os parametros de pesquisa que irão no cabeçalho da página
        Map parametersReport = new HashMap();
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("dtEmissao", new SimpleDateFormat("dd/MM/yyyy - HH:mm").format(new Date(JTDateTimeUtils.getDataHoraAtual().getMillis())));
        parametersReport.put("dsDoc1", new JRBeanCollectionDataSource(ctoAwbService.findDoctosForReport(tfm.getLong("awb.idAwb"), 1, FIRST_ROW_DOC_FL_AUX)));
        parametersReport.put("dsDoc2", new JRBeanCollectionDataSource(ctoAwbService.findDoctosForReport(tfm.getLong("awb.idAwb"), 0, FIRST_ROW_DOC_FL_AUX)));
        
        List<Map<String, Object>> dsDoc3 = ctoAwbService.findDoctosForReport(tfm.getLong("awb.idAwb"), null, FIRST_ROW_DOC_FL_AUX); 
        parametersReport.put("dsDoc3", dsDoc3 != null && !dsDoc3.isEmpty() ? new JRBeanCollectionDataSource(dsDoc3) : null);
        
        parametersReport.put("dsParcelas1", new JRBeanCollectionDataSource(awbService.findParcelasForReport(tfm.getLong("awb.idAwb"), 1)));
        parametersReport.put("dsParcelas2", new JRBeanCollectionDataSource(awbService.findParcelasForReport(tfm.getLong("awb.idAwb"), 2)));
        parametersReport.put("dsParcelas3", new JRBeanCollectionDataSource(awbService.findParcelasForReport(tfm.getLong("awb.idAwb"), 3)));
        parametersReport.put("dsParcelas4", new JRBeanCollectionDataSource(awbService.findParcelasForReport(tfm.getLong("awb.idAwb"), 0)));
        
        jr.setParameters(parametersReport);
        return jr;
	}

	private String createMainQuery() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT AWB.NR_CHAVE, ");
		sb.append("  AWB.ID_AWB                                       AS ID_AWB, ");
		sb.append("  AWB.NR_OPE_CONH_AEREO                            AS NR_OPERACIONAL, ");
		sb.append("  CIA.CD_IATA									  AS CD_IATA,	");	
		sb.append("  AWB.DS_MODELO_DACTE                              AS NR_MODELO, ");
		sb.append("  AWB.NR_SERIE_DACTE                               AS NR_SERIE, ");
		sb.append("  AWB.NR_DACTE                                     AS NR_NUMERO, ");
		sb.append("  NULL                                             AS NR_INSCR_SUFRAMA, ");
		sb.append("  PCIA.NM_PESSOA                                   AS NM_CIA, ");
		sb.append("  EPCIA.DS_ENDERECO                                AS ENDERECO_CIA, ");
		sb.append("  EPCIA.DS_BAIRRO                                  AS BAIRRO_CIA, ");
		sb.append("  MCIA.NM_MUNICIPIO                                AS MUNICIPIO_CIA, ");
		sb.append("  UFCIA.SG_UNIDADE_FEDERATIVA                      AS UF_CIA, ");
		sb.append("  EPCIA.NR_CEP                                     AS CEP_CIA, ");
		sb.append("  PCIA.NR_IDENTIFICACAO                            AS CPFCNPJ_CIA, ");
		sb.append("  PCIA.TP_IDENTIFICACAO                            AS TP_ID_CIA, ");
		sb.append("  IECIA.NR_INSCRICAO_ESTADUAL                      AS IE_CIA, ");
		sb.append("  AWB.TP_CTE                                       AS TP_CTE, ");
		sb.append("  'Pago'                                           AS DS_FORMA_PAGTO, ");
		sb.append("  AWB.TP_SERVICO                                   AS TP_SERVICO, ");
		sb.append("  AWB.DS_NATUREZA_PRESTACAO                        AS DS_NATUREZA_PRESTACAO, ");
		sb.append("  AWB.DS_AUTORIZACAO_USO                           AS DS_AUTORIZACAO_USO, ");
		sb.append("  UFAO.SG_UNIDADE_FEDERATIVA                       AS UF_AEROPORTO_ORIGEM, ");
		sb.append("  MAO.NM_MUNICIPIO                                 AS MUNICIPIO_AEROPORTO_ORIGEM, ");
		sb.append("  UFAD.SG_UNIDADE_FEDERATIVA                       AS UF_AEROPORTO_DESTINO, ");
		sb.append("  MAD.NM_MUNICIPIO                                 AS MUNICIPIO_AEROPORTO_DESTINO, ");
		sb.append("  PCD.NM_PESSOA                                    AS NM_DESTINATARIO, ");
		sb.append("  EPCD.DS_ENDERECO                                 AS ENDERECO_DESTINATARIO, ");
		sb.append("  EPCD.DS_BAIRRO                                   AS BAIRRO_DESTINATARIO, ");
		sb.append("  MCD.NM_MUNICIPIO                                 AS MUNICIPIO_DESTINATARIO, ");
		sb.append("  EPCD.NR_CEP                                      AS CEP_DESTINATARIO, ");
		sb.append("  PCD.NR_IDENTIFICACAO                             AS CPFCNPJ_DESTINATARIO, ");
		sb.append("  PCD.TP_IDENTIFICACAO                             AS TP_ID_DESTINATARIO, ");
		sb.append("  UFCD.SG_UNIDADE_FEDERATIVA                       AS UF_DESTINATARIO, ");
		sb.append("  " + PropertyVarcharI18nProjection.createProjection("PSCD.NM_PAIS_I") + " AS PAIS_DESTINATARIO, ");
		sb.append("  (SELECT TO_CHAR(WM_CONCAT(TECD.NR_DDD || ' ' || TECD.NR_TELEFONE)) ");
		sb.append("  FROM TELEFONE_ENDERECO TECD ");
		sb.append("  WHERE TECD.ID_ENDERECO_PESSOA = EPCD.ID_ENDERECO_PESSOA ");
		sb.append("  )                          AS FONE_DESTINATARIO, ");
		sb.append("  IECD.NR_INSCRICAO_ESTADUAL AS IE_DESTINATARIO, ");
		sb.append("  PCE.NM_PESSOA              AS NM_EXPEDIDOR, ");
		sb.append("  EPCE.DS_ENDERECO           AS ENDERECO_EXPEDIDOR, ");
		sb.append("  EPCE.DS_BAIRRO             AS BAIRRO_EXPEDIDOR, ");
		sb.append("  MCE.NM_MUNICIPIO           AS MUNICIPIO_EXPEDIDOR, ");
		sb.append("  EPCE.NR_CEP                AS CEP_EXPEDIDOR, ");
		sb.append("  PCE.NR_IDENTIFICACAO       AS CPFCNPJ_EXPEDIDOR, ");
		sb.append("  PCE.TP_IDENTIFICACAO       AS TP_ID_EXPEDIDOR, ");
		sb.append("  UFCE.SG_UNIDADE_FEDERATIVA AS UF_EXPEDIDOR, ");
		sb.append("  " + PropertyVarcharI18nProjection.createProjection("PSCE.NM_PAIS_I") + " AS PAIS_EXPEDIDOR, ");
		sb.append("  (SELECT TO_CHAR(WM_CONCAT(TECE.NR_DDD || ' ' || TECE.NR_TELEFONE)) ");
		sb.append("  FROM TELEFONE_ENDERECO TECE ");
		sb.append("  WHERE TECE.ID_ENDERECO_PESSOA = EPCE.ID_ENDERECO_PESSOA ");
		sb.append("  )                          AS FONE_EXPEDIDOR, ");
		sb.append("  IECE.NR_INSCRICAO_ESTADUAL AS IE_EXPEDIDOR, ");
		sb.append("  PCT.NM_PESSOA              AS NM_TOMADOR, ");
		sb.append("  EPCT.DS_ENDERECO           AS ENDERECO_TOMADOR, ");
		sb.append("  EPCT.DS_BAIRRO             AS BAIRRO_TOMADOR, ");
		sb.append("  MCT.NM_MUNICIPIO           AS MUNICIPIO_TOMADOR, ");
		sb.append("  EPCT.NR_CEP                AS CEP_TOMADOR, ");
		sb.append("  PCT.NR_IDENTIFICACAO       AS CPFCNPJ_TOMADOR, ");
		sb.append("  PCT.TP_IDENTIFICACAO       AS TP_ID_TOMADOR, ");
		sb.append("  UFCT.SG_UNIDADE_FEDERATIVA AS UF_TOMADOR, ");
		sb.append("  " + PropertyVarcharI18nProjection.createProjection("PSCT.NM_PAIS_I") + " AS PAIS_TOMADOR, ");
		sb.append("  (SELECT TO_CHAR(WM_CONCAT(TECT.NR_DDD || ' ' || TECT.NR_TELEFONE)) ");
		sb.append("  FROM TELEFONE_ENDERECO TECT ");
		sb.append("  WHERE TECT.ID_ENDERECO_PESSOA = EPCT.ID_ENDERECO_PESSOA ");
		sb.append("  )                          AS FONE_TOMADOR, ");
		sb.append("  IECT.NR_INSCRICAO_ESTADUAL AS IE_TOMADOR, ");
		sb.append("  " + PropertyVarcharI18nProjection.createProjection("NP.DS_NATUREZA_PRODUTO_I") + " AS DS_NATUREZA_PRODUTO, ");
		sb.append("  AWB.QT_VOLUMES                                                            AS QT_VOLUMES, ");
		sb.append("  AWB.PS_TOTAL                                                              AS PS_BRUTO, ");
		sb.append("  AWB.PS_CUBADO                                                             AS PS_CUBADO, ");
		sb.append("  AWB.NR_APOLICE_SEGURO                                                     AS NR_APOLICE,");
		sb.append("  SEG.NM_PESSOA                                                             AS NM_SEGURADORA, ");
		sb.append("  AWB.VL_ICMS                                                               AS VL_ICMS, ");
		sb.append("  AWB.PC_ALIQUOTA_ICMS AS                                                   PC_ALIQUOTA_ICMS, ");		
		sb.append("  AWB.DS_PROD_PREDOMINANTE                                                  AS PROD_PREDOMINANTE, ");
		sb.append("  AWB.DS_OUT_CARACTERISTICAS                                                AS OUTRAS_CARACTERISTICAS, ");
		sb.append("  AWB.VL_TOTAL_MERCADORIA                                                   AS VALOR_TOTAL, ");
		sb.append("  AWB.PS_BASE_CALC                                                          AS PS_BASE_CALC, ");
		sb.append("  AWB.VL_CUBAGEM                                                            AS CUBAGEM, ");
		sb.append("  AWB.NM_RESPONSAVEL_APOLICE                                                AS RESPONSAVEL, ");
		sb.append("  NULL                                                                      AS NR_AVERBACAO, ");
		sb.append("  AWB.VL_TOTAL_SERVICO                                                      AS VL_TOTAL_SERVICO, ");
		sb.append("  AWB.VL_RECEBER                                                            AS VL_RECEBER, ");
		sb.append("  AWB.DS_SITUACAO_TRIBUTARIA                                                AS SITUACAO_TRIBUTARIA, ");
		sb.append("  AWB.PC_RED_BC_CALC                                                        AS BASE_CALCULO, ");
		sb.append("  AWB.PC_ALIQUOTA_ICMS                                                      AS VL_ALIQUOTA_ICMS, ");
		sb.append("  AWB.VL_BASE_CALC_IMPOSTO                                                  AS VL_RED_BC_CALC, ");
		sb.append("  AWB.VL_ICMS_ST                                                            AS VL_ICMS_ST, ");
		sb.append("  AWB.OB_AWB                                                                AS OBSERVACOES, ");
		sb.append("  AWB.DS_INF_MANUSEIO                                                       AS INF_MANUSEIO, ");
		sb.append("  AWB.CD_CARGA_ESPECIAL                                                     AS COD_CARGA_ESPECIAL, ");
		sb.append("  AWB.DS_CARAC_ADIC_SERV                                                    AS CARAC_ADIC_SERV, ");
		sb.append("  AWB.NR_OPE_CONH_AEREO                                                     AS NUM_OPE_CONH_AEREO, ");
		sb.append("  TO_CHAR(AWB.DT_PREVISTA_ENTREGA, 'DD/MM/YYYY')                            AS DT_PREVISTA_ENTREGA, ");
		sb.append("  AOR.SG_AEROPORTO                                                          AS AEROPORTO_ORIGEM, ");
		sb.append("  AES.SG_AEROPORTO                                                          AS AEROPORTO_ESCALA, ");
		sb.append("  ADE.SG_AEROPORTO                                                          AS AEROPORTO_DESTINO, ");
		sb.append("  AWB.DS_CLASSE                                                             AS CLASSE, ");
		sb.append("  AWB.CD_TARIIFA                                                            AS COD_TARIIFA, ");
		sb.append("  AWB.VL_TARIFA                                                             AS VL_TARIFA, ");
		sb.append("  AWB.NR_MINUTA                                                             AS NR_MINUTA, ");
		sb.append("  AWB.DS_DADOS_RETIRADA                                                     AS DADOS_RETIRADA, ");
		sb.append("  AWB.DS_IDENTIFICACAO_TOMADOR                                              AS IDENTIFICACAO_TOMADOR, ");
		sb.append("  AWB.DS_IDENTIFICACAO_EMISSOR                                              AS IDENTIFICACAO_EMISSOR, ");
		sb.append("  AWB.BL_RETIRA                                                             AS BL_RETIRA, ");
		sb.append("  AWB.DS_USO_EMISSOR                                                        AS USO_EMISSOR ");
		sb.append("FROM AWB AWB ");
		sb.append("LEFT JOIN NATUREZA_PRODUTO NP ");
		sb.append("ON AWB.ID_NATUREZA_PRODUTO = NP.ID_NATUREZA_PRODUTO ");
		sb.append("LEFT JOIN CIA_FILIAL_MERCURIO CFM ");
		sb.append("ON AWB.ID_CIA_FILIAL_MERCURIO = CFM.ID_CIA_FILIAL_MERCURIO ");
		sb.append("LEFT JOIN EMPRESA CIA ");
		sb.append("ON CFM.ID_EMPRESA = CIA.ID_EMPRESA ");
		sb.append("LEFT JOIN PESSOA PCIA ");
		sb.append("ON CIA.ID_EMPRESA = PCIA.ID_PESSOA ");
		sb.append("LEFT JOIN ENDERECO_PESSOA EPCIA ");
		sb.append("ON PCIA.ID_PESSOA = EPCIA.ID_PESSOA ");
		sb.append("AND SYSDATE BETWEEN EPCIA.DT_VIGENCIA_INICIAL AND EPCIA.DT_VIGENCIA_FINAL ");
		sb.append("LEFT JOIN MUNICIPIO MCIA ");
		sb.append("ON EPCIA.ID_MUNICIPIO = MCIA.ID_MUNICIPIO ");
		sb.append("LEFT JOIN UNIDADE_FEDERATIVA UFCIA ");
		sb.append("ON MCIA.ID_UNIDADE_FEDERATIVA = UFCIA.ID_UNIDADE_FEDERATIVA ");
		sb.append("LEFT JOIN PAIS PSCIA ");
		sb.append("ON UFCIA.ID_PAIS = PSCIA.ID_PAIS ");
		sb.append("LEFT JOIN INSCRICAO_ESTADUAL IECIA ");
		sb.append("ON PCIA.ID_PESSOA             = IECIA.ID_PESSOA ");
		sb.append("AND IECIA.BL_INDICADOR_PADRAO = 'S' ");
		sb.append("AND IECIA.TP_SITUACAO         = 'A' ");
		sb.append("LEFT JOIN CLIENTE CLD ");
		sb.append("ON AWB.ID_CLIENTE_DESTINATARIO = CLD.ID_CLIENTE ");
		sb.append("LEFT JOIN EMPRESA ECD ");
		sb.append("ON CLD.ID_CLIENTE = ECD.ID_EMPRESA ");
		sb.append("LEFT JOIN PESSOA PCD ");
		sb.append("ON CLD.ID_CLIENTE = PCD.ID_PESSOA ");
		sb.append("LEFT JOIN ENDERECO_PESSOA EPCD ");
		sb.append("ON PCD.ID_PESSOA = EPCD.ID_PESSOA ");
		sb.append("AND SYSDATE BETWEEN EPCD.DT_VIGENCIA_INICIAL AND EPCD.DT_VIGENCIA_FINAL ");
		sb.append("LEFT JOIN MUNICIPIO MCD ");
		sb.append("ON EPCD.ID_MUNICIPIO = MCD.ID_MUNICIPIO ");
		sb.append("LEFT JOIN UNIDADE_FEDERATIVA UFCD ");
		sb.append("ON MCD.ID_UNIDADE_FEDERATIVA = UFCD.ID_UNIDADE_FEDERATIVA ");
		sb.append("LEFT JOIN PAIS PSCD ");
		sb.append("ON UFCD.ID_PAIS = PSCD.ID_PAIS ");
		sb.append("LEFT JOIN INSCRICAO_ESTADUAL IECD ");
		sb.append("ON PCD.ID_PESSOA             = IECD.ID_PESSOA ");
		sb.append("AND IECD.BL_INDICADOR_PADRAO = 'S' ");
		sb.append("AND IECD.TP_SITUACAO         = 'A' ");
		sb.append("LEFT JOIN CLIENTE CLE ");
		sb.append("ON AWB.ID_CLIENTE_EXPEDIDOR = CLE.ID_CLIENTE ");
		sb.append("LEFT JOIN EMPRESA ECE ");
		sb.append("ON CLE.ID_CLIENTE = ECE.ID_EMPRESA ");
		sb.append("LEFT JOIN PESSOA PCE ");
		sb.append("ON CLE.ID_CLIENTE = PCE.ID_PESSOA ");
		sb.append("LEFT JOIN ENDERECO_PESSOA EPCE ");
		sb.append("ON PCE.ID_PESSOA = EPCE.ID_PESSOA ");
		sb.append("AND SYSDATE BETWEEN EPCE.DT_VIGENCIA_INICIAL AND EPCE.DT_VIGENCIA_FINAL ");
		sb.append("LEFT JOIN MUNICIPIO MCE ");
		sb.append("ON EPCE.ID_MUNICIPIO = MCE.ID_MUNICIPIO ");
		sb.append("LEFT JOIN UNIDADE_FEDERATIVA UFCE ");
		sb.append("ON MCE.ID_UNIDADE_FEDERATIVA = UFCE.ID_UNIDADE_FEDERATIVA ");
		sb.append("LEFT JOIN PAIS PSCE ");
		sb.append("ON UFCE.ID_PAIS = PSCE.ID_PAIS ");
		sb.append("LEFT JOIN INSCRICAO_ESTADUAL IECE ");
		sb.append("ON PCE.ID_PESSOA             = IECE.ID_PESSOA ");
		sb.append("AND IECE.BL_INDICADOR_PADRAO = 'S' ");
		sb.append("AND IECE.TP_SITUACAO         = 'A' ");
		sb.append("LEFT JOIN CLIENTE CLT ");
		sb.append("ON AWB.ID_CLIENTE_TOMADOR = CLT.ID_CLIENTE ");
		sb.append("LEFT JOIN EMPRESA ECT ");
		sb.append("ON CLT.ID_CLIENTE = ECT.ID_EMPRESA ");
		sb.append("LEFT JOIN PESSOA PCT ");
		sb.append("ON CLT.ID_CLIENTE = PCT.ID_PESSOA ");
		sb.append("LEFT JOIN ENDERECO_PESSOA EPCT ");
		sb.append("ON PCT.ID_PESSOA = EPCT.ID_PESSOA ");
		sb.append("AND SYSDATE BETWEEN EPCT.DT_VIGENCIA_INICIAL AND EPCT.DT_VIGENCIA_FINAL ");
		sb.append("LEFT JOIN MUNICIPIO MCT ");
		sb.append("ON EPCT.ID_MUNICIPIO = MCT.ID_MUNICIPIO ");
		sb.append("LEFT JOIN UNIDADE_FEDERATIVA UFCT ");
		sb.append("ON MCT.ID_UNIDADE_FEDERATIVA = UFCT.ID_UNIDADE_FEDERATIVA ");
		sb.append("LEFT JOIN PAIS PSCT ");
		sb.append("ON UFCT.ID_PAIS = PSCT.ID_PAIS ");
		sb.append("LEFT JOIN INSCRICAO_ESTADUAL IECT ");
		sb.append("ON PCT.ID_PESSOA             = IECT.ID_PESSOA ");
		sb.append("AND IECT.BL_INDICADOR_PADRAO = 'S' ");
		sb.append("AND IECT.TP_SITUACAO         = 'A' ");
		sb.append("LEFT JOIN AEROPORTO AOR ");
		sb.append("ON AWB.ID_AEROPORTO_ORIGEM = AOR.ID_AEROPORTO ");
		sb.append("LEFT JOIN PESSOA PAO ");
		sb.append("ON AOR.ID_AEROPORTO = PAO.ID_PESSOA ");
		sb.append("LEFT JOIN ENDERECO_PESSOA EPAO ");
		sb.append("ON PAO.ID_PESSOA = EPAO.ID_PESSOA AND SYSDATE BETWEEN EPAO.DT_VIGENCIA_INICIAL AND EPAO.DT_VIGENCIA_FINAL ");
		sb.append("LEFT JOIN MUNICIPIO MAO ");
		sb.append("ON EPAO.ID_MUNICIPIO = MAO.ID_MUNICIPIO ");
		sb.append("LEFT JOIN UNIDADE_FEDERATIVA UFAO ");
		sb.append("ON MAO.ID_UNIDADE_FEDERATIVA = UFAO.ID_UNIDADE_FEDERATIVA ");
		sb.append("LEFT JOIN AEROPORTO AES ");
		sb.append("ON AWB.ID_AEROPORTO_ESCALA = AES.ID_AEROPORTO ");
		sb.append("LEFT JOIN AEROPORTO ADE ");
		sb.append("ON AWB.ID_AEROPORTO_DESTINO = ADE.ID_AEROPORTO ");
		sb.append("LEFT JOIN PESSOA PAD ");
		sb.append("ON ADE.ID_AEROPORTO = PAD.ID_PESSOA ");
		sb.append("LEFT JOIN ENDERECO_PESSOA EPAD ");
		sb.append("ON PAD.ID_PESSOA = EPAD.ID_PESSOA AND SYSDATE BETWEEN EPAD.DT_VIGENCIA_INICIAL AND EPAD.DT_VIGENCIA_FINAL ");
		sb.append("LEFT JOIN MUNICIPIO MAD ");
		sb.append("ON EPAO.ID_MUNICIPIO = MAD.ID_MUNICIPIO ");
		sb.append("LEFT JOIN UNIDADE_FEDERATIVA UFAD ");
		sb.append("ON MAD.ID_UNIDADE_FEDERATIVA = UFAD.ID_UNIDADE_FEDERATIVA ");
		sb.append("LEFT JOIN APOLICE_SEGURO APS ");
		sb.append("ON AWB.ID_APOLICE_SEGURO = APS.ID_APOLICE_SEGURO ");
		sb.append("AND SYSDATE BETWEEN APS.DT_VIGENCIA_INICIAL AND APS.DT_VIGENCIA_FINAL ");
		sb.append("LEFT JOIN PESSOA SEG ");
		sb.append("ON APS.ID_SEGURADORA = SEG.ID_PESSOA ");
		sb.append("WHERE ID_AWB = ?");
		
		return sb.toString();

	}

	public void setCtoAwbService(CtoAwbService ctoAwbService) {
		this.ctoAwbService = ctoAwbService;
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}
}
