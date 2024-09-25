package com.mercurio.lms.coleta.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração do Relatório de Manifestos. Especificação
 * técnica 02.01.02.06
 * 
 * @author Ruhan
 * 
 * @spring.bean id="lms.coleta.emitirManifestosAwbService"
 * @spring.property name="reportName" value="com/mercurio/lms/coleta/report/emitirManifestosDetalheAwb.jasper"
 */
public class EmitirManifestosAwbService extends ReportServiceSupport {

	private static final String ID_SERVICO_AEREO_NAC_EMERG = "ID_SERVICO_AEREO_NAC_EMERG";
	private ParametroGeralService parametroGeralService;

	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap)parameters;		
		
		BigDecimal idServicoAereoNacEmerg = (BigDecimal)parametroGeralService.findConteudoByNomeParametro(ID_SERVICO_AEREO_NAC_EMERG, false);

        String sql = createMainQuery(tfm, idServicoAereoNacEmerg);
        
        List<Object> params = new ArrayList<Object>();
        
        if(tfm.getLong("manifestoColeta.idManifestoColeta") != null){
        	params.add(tfm.getLong("manifestoColeta.idManifestoColeta"));
        }
        if(tfm.getLong("rotaColetaEntrega.idRotaColetaEntrega") != null){
        	params.add(tfm.getLong("rotaColetaEntrega.idRotaColetaEntrega"));
        }
        
        JRReportDataObject jr = executeQuery(sql, params.toArray());
        
        // Seta os parametros de pesquisa que irão no cabeçalho da página
        Map parametersReport = new HashMap();
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        jr.setParameters(parametersReport);
        return jr;
	}

	private String createMainQuery(TypedFlatMap tfm, BigDecimal idServicoAereoNacEmerg) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT CC.ID_CONTROLE_CARGA, ");
		sb.append("  DS.ID_DOCTO_SERVICO, ");
		sb.append("  CASE WHEN DC.ID_SERVICO = ").append(idServicoAereoNacEmerg.toString()).append(" THEN 'S' ELSE 'N' END AS IS_EMERGENCIAL, ");
		sb.append("  DC.BL_ENTREGA_DIRETA                                   AS BL_ENTREGA_DIRETA, ");
		sb.append("  PEM.NM_PESSOA                                          AS NM_EMPRESA, ");
		sb.append("  EP.DS_ENDERECO                                         AS DS_ENDERECO_FILIAL, ");
		sb.append("  MUN.NM_MUNICIPIO                                       AS NM_MUNICIPIO_FILIAL, ");
		sb.append("  PFO.NR_IDENTIFICACAO                                   AS NR_CNPJ_FILIAL, ");
		sb.append("  IES.NR_INSCRICAO_ESTADUAL                              AS NR_INSCRICAO_ESTADUAL, ");
		sb.append("  MC.ID_MANIFESTO_COLETA                                 AS ID_MANIFESTO_COLETA, ");
		sb.append("  FMC.SG_FILIAL                                          AS SG_FILIAL_MANIFESTO, ");
		sb.append("  MC.NR_MANIFESTO                                        AS NR_MANIFESTO, ");
		sb.append("  NVL(MC.DH_EMISSAO, SYSDATE)                            AS DH_EMISSAO_MANIFESTO, ");
		sb.append("  RCE.NR_ROTA                                            AS NR_ROTA, ");
		sb.append("  RCE.DS_ROTA                                            AS DS_ROTA, ");
		sb.append("  PES.NM_PESSOA                                          AS NM_MOTORISTA, ");
		sb.append("  MOT.NR_CARTEIRA_HABILITACAO                            AS NR_HABILITACAO, ");
		sb.append("  MRT.DS_MARCA_MEIO_TRANSPORTE                           AS NM_MARCA, ");
		sb.append("  MT.NR_FROTA                                            AS NR_FROTA, ");
		sb.append("  MT.NR_IDENTIFICADOR                                    AS NR_PLACA, ");
		sb.append("  MMT.DS_MODELO_MEIO_TRANSPORTE                          AS NM_MODELO, ");
		sb.append("  MTREB.NR_FROTA                                         AS NR_FROTA_SEMI, ");
		sb.append("  MTREB.NR_IDENTIFICADOR                                 AS NR_PLACA_SEMI, ");
		sb.append("  PC.ID_PEDIDO_COLETA                                    AS ID_PEDIDO_COLETA, ");
		sb.append("  AWB.TP_STATUS_AWB                                      AS TP_STATUS_AWB, ");
		sb.append("  FPC.SG_FILIAL                                          AS SG_FILIAL_COLETA, ");
		sb.append("  PC.NR_COLETA                                           AS NR_COLETA, ");
		sb.append("  PC.TP_PEDIDO_COLETA                                    AS TP_PEDIDO_COLETA, ");
		sb.append("  PC.HR_LIMITE_COLETA                                    AS HR_LIMITE_COLETA, ");
		sb.append("  AWB.NR_AWB												AS NR_AWB, ");
		sb.append("  AWB.DS_SERIE											AS DS_SERIE, ");
		sb.append("  AWB.DV_AWB                                             AS DV_AWB, ");
		sb.append("  AWB.ID_AWB                                             AS ID_AWB, ");
		sb.append("  (SELECT P.NM_FANTASIA ");
		sb.append("  FROM CIA_FILIAL_MERCURIO CFM, ");
		sb.append("    PESSOA P ");
		sb.append("  WHERE CFM.ID_EMPRESA           = P.ID_PESSOA ");
		sb.append("  AND CFM.ID_CIA_FILIAL_MERCURIO = AWB.ID_CIA_FILIAL_MERCURIO ");
		sb.append("  )                                                AS NM_CIA_AEREA, ");
		
		sb.append("  (SELECT EMP.SG_EMPRESA ");
		sb.append("  FROM CIA_FILIAL_MERCURIO CFM, ");
		sb.append("    EMPRESA EMP ");
		sb.append("  WHERE CFM.ID_EMPRESA           = EMP.ID_EMPRESA ");
		sb.append("  AND CFM.ID_CIA_FILIAL_MERCURIO = AWB.ID_CIA_FILIAL_MERCURIO ");
		sb.append("  )                                                AS SG_EMPRESA, ");
		
		sb.append("  ''                                               AS SERVICO_FRETE, ");
		sb.append("  AOR.SG_AEROPORTO                                 AS DS_AERO_ORIGEM, ");
		sb.append("  ADE.SG_AEROPORTO                                 AS DS_AERO_DESTINO, ");
		sb.append("  AWB.QT_VOLUMES                                   AS AWB_QT_VOLUMES, ");
		sb.append("  AWB.PS_CUBADO                                    AS AWB_PS_CUBADO, ");
		sb.append("  " + PropertyVarcharI18nProjection.createProjection("EMB.DS_EMBALAGEM_I") + "                               AS DS_EMBALAGEM, ");
		sb.append("  " + PropertyVarcharI18nProjection.createProjection("NP.DS_NATUREZA_PRODUTO_I") + "                         AS DS_NATUREZA_PRODUTO, ");
		sb.append("  DS.TP_DOCUMENTO_SERVICO                          AS TP_DOCUMENTO_SERVICO, ");
		sb.append("  FODS.SG_FILIAL                                   AS DS_FILIAL_ORIGEM, ");
		sb.append("  DS.NR_DOCTO_SERVICO                              AS DS_NUMERO, ");
		sb.append("  DS.DT_PREV_ENTREGA                               AS ODT, ");
		sb.append("  PRE.NM_PESSOA                                    AS NM_REMETENTE, ");
		sb.append("  PDE.NM_PESSOA                                    AS NM_DESTINATARIO, ");
		sb.append("  DS.DS_ENDERECO_ENTREGA_REAL                      AS DS_ENDERECO_ENTREGA, ");
		sb.append("  CO.TP_FRETE                                      AS TP_FRETE, ");
		sb.append("  DS.PS_REAL                                       AS PS_REAL, ");
		sb.append("  DS.QT_VOLUMES                                    AS QT_VOLUME, ");
		sb.append("  M.SG_MOEDA                                       AS SG_MOEDA, ");
		sb.append("  M.DS_SIMBOLO                                     AS DS_SIMBOLO, ");
		sb.append("  DS.VL_MERCADORIA                                 AS VL_MERCADORIA, ");
		sb.append("  GREATEST(NVL(DS.PS_REAL,0),NVL(DS.PS_AFORADO,0)) AS PESO, ");
		sb.append("  (SELECT LISTAGG(NFC.NR_NOTA_FISCAL, ', ') WITHIN GROUP ( ");
		sb.append("  ORDER BY NFC.NR_NOTA_FISCAL) ");
		sb.append("  FROM NOTA_FISCAL_CONHECIMENTO NFC ");
		sb.append("  WHERE NFC.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO ");
		sb.append("  )  AS NOTAS_FISCAIS, ");
		sb.append("(SELECT NR_NAO_CONFORMIDADE ");
		sb.append("FROM NAO_CONFORMIDADE NC ");
		sb.append("WHERE NC.ID_DOCTO_SERVICO              = DS.ID_DOCTO_SERVICO ");
		sb.append("AND NC.ID_FILIAL                       = MC.ID_FILIAL_ORIGEM ");
		sb.append("AND NC.TP_STATUS_NAO_CONFORMIDADE NOT IN ('AGP', 'CAN')) ");
		sb.append("AS RNC ");
		
		
		sb.append("FROM DETALHE_COLETA DC ");
		sb.append("JOIN PEDIDO_COLETA PC ");
		sb.append("ON PC.ID_PEDIDO_COLETA = DC.ID_PEDIDO_COLETA ");
		sb.append("JOIN FILIAL FPC ");
		sb.append("ON PC.ID_FILIAL_SOLICITANTE = FPC.ID_FILIAL ");
		sb.append("JOIN MANIFESTO_COLETA MC ");
		sb.append("ON PC.ID_MANIFESTO_COLETA = MC.ID_MANIFESTO_COLETA ");
		sb.append("JOIN FILIAL FMC ");
		sb.append("ON MC.ID_FILIAL_ORIGEM = FMC.ID_FILIAL ");
		sb.append("JOIN EMPRESA EMP ");
		sb.append("ON FMC.ID_EMPRESA = EMP.ID_EMPRESA ");
		sb.append("JOIN PESSOA PEM ");
		sb.append("ON EMP.ID_EMPRESA = PEM.ID_PESSOA ");
		sb.append("JOIN PESSOA PFO ");
		sb.append("ON FMC.ID_FILIAL = PFO.ID_PESSOA ");
		sb.append("JOIN INSCRICAO_ESTADUAL IES ");
		sb.append("ON PFO.ID_PESSOA            = IES.ID_PESSOA ");
		sb.append("AND IES.BL_INDICADOR_PADRAO = 'S' ");
		sb.append("JOIN ENDERECO_PESSOA EP ");
		sb.append("ON PFO.ID_ENDERECO_PESSOA = EP.ID_ENDERECO_PESSOA ");
		sb.append("JOIN MUNICIPIO MUN ");
		sb.append("ON EP.ID_MUNICIPIO = MUN.ID_MUNICIPIO ");
		sb.append("JOIN ROTA_COLETA_ENTREGA RCE ");
		sb.append("ON MC.ID_ROTA_COLETA_ENTREGA = RCE.ID_ROTA_COLETA_ENTREGA ");
		sb.append("JOIN CONTROLE_CARGA CC ");
		sb.append("ON MC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA ");
		sb.append("LEFT JOIN MEIO_TRANSPORTE MT ");
		sb.append("ON CC.ID_TRANSPORTADO = MT.ID_MEIO_TRANSPORTE ");
		sb.append("LEFT JOIN MODELO_MEIO_TRANSPORTE MMT ");
		sb.append("ON MT.ID_MODELO_MEIO_TRANSPORTE = MMT.ID_MODELO_MEIO_TRANSPORTE ");
		sb.append("LEFT JOIN MARCA_MEIO_TRANSPORTE MRT ");
		sb.append("ON MMT.ID_MARCA_MEIO_TRANSPORTE = MRT.ID_MARCA_MEIO_TRANSPORTE ");
		sb.append("LEFT JOIN MOTORISTA MOT ");
		sb.append("ON CC.ID_MOTORISTA = MOT.ID_MOTORISTA ");
		sb.append("LEFT JOIN PESSOA PES ");
		sb.append("ON MOT.ID_MOTORISTA = PES.ID_PESSOA ");
		sb.append("LEFT JOIN MEIO_TRANSPORTE MTREB ");
		sb.append("ON CC.ID_SEMI_REBOCADO = MTREB.ID_MEIO_TRANSPORTE ");
		sb.append("LEFT JOIN MOEDA M ");
		sb.append("ON M.ID_MOEDA = DC.ID_MOEDA ");
		sb.append("LEFT JOIN AWB_COLETA AC ");
		sb.append("ON DC.ID_DETALHE_COLETA = AC.ID_DETALHE_COLETA ");
		sb.append("LEFT JOIN CTO_AWB CA_AW ");
		sb.append("ON AC.ID_AWB = CA_AW.ID_AWB ");
		sb.append("LEFT JOIN CTO_AWB CA_DS ");
		sb.append("ON DC.ID_DOCTO_SERVICO = CA_DS.ID_CONHECIMENTO ");
		sb.append("LEFT JOIN DOCTO_SERVICO DS ");
		sb.append("ON NVL(CA_DS.ID_CONHECIMENTO, CA_AW.ID_CONHECIMENTO) = DS.ID_DOCTO_SERVICO ");
		sb.append("LEFT JOIN AWB AWB ");
		sb.append("ON NVL(CA_DS.ID_AWB, CA_AW.ID_AWB) = AWB.ID_AWB ");
		sb.append("LEFT JOIN CONHECIMENTO CO ");
		sb.append("ON DS.ID_DOCTO_SERVICO = CO.ID_CONHECIMENTO ");
		sb.append("LEFT JOIN FILIAL FODS ");
		sb.append("ON DS.ID_FILIAL_ORIGEM = FODS.ID_FILIAL ");
		sb.append("LEFT JOIN PESSOA PRE ");
		sb.append("ON DS.ID_CLIENTE_REMETENTE = PRE.ID_PESSOA ");
		sb.append("LEFT JOIN PESSOA PDE ");
		sb.append("ON DS.ID_CLIENTE_DESTINATARIO = PDE.ID_PESSOA ");
		sb.append("LEFT JOIN AEROPORTO AOR ");
		sb.append("ON AWB.ID_AEROPORTO_ORIGEM = AOR.ID_AEROPORTO ");
		sb.append("LEFT JOIN AEROPORTO ADE ");
		sb.append("ON AWB.ID_AEROPORTO_DESTINO = ADE.ID_AEROPORTO ");
		sb.append("LEFT JOIN AWB_EMBALAGEM AE ");
		sb.append("ON AWB.ID_AWB = AE.ID_AWB ");
		sb.append("LEFT JOIN EMBALAGEM EMB ");
		sb.append("ON AE.ID_EMBALAGEM = EMB.ID_EMBALAGEM ");
		sb.append("LEFT JOIN NATUREZA_PRODUTO NP ");
		sb.append("ON AWB.ID_NATUREZA_PRODUTO = NP.ID_NATUREZA_PRODUTO ");
		
		sb.append("WHERE PC.TP_PEDIDO_COLETA = 'AE' ");
		if(tfm.getLong("manifestoColeta.idManifestoColeta") != null){
			sb.append("  AND MC.ID_MANIFESTO_COLETA = ? ");
        }
        if(tfm.getLong("rotaColetaEntrega.idRotaColetaEntrega") != null){
        	sb.append("  AND RCE.ID_ROTA_COLETA_ENTREGA = ? ");
        }
        sb.append("AND MC.TP_STATUS_MANIFESTO_COLETA IN ('GE', 'EM') ");
		sb.append("AND (AWB.ID_AWB = ");
		sb.append("  (SELECT MAX(a.id_awb) ");
		sb.append("  FROM cto_awb a ");
		sb.append("  JOIN awb AWB_ ");
		sb.append("  ON awb_.id_awb          = a.id_awb ");
		sb.append("  WHERE a.id_conhecimento = DC.id_docto_servico ");
		sb.append("  AND awb_.tp_status_awb <> 'C' ");
		sb.append("  ) ");
		sb.append("OR DC.id_docto_servico IS NULL)");
		sb.append(" ORDER BY 3 DESC, NR_COLETA, SG_EMPRESA, NR_AWB, DS_FILIAL_ORIGEM, DS_NUMERO ");
		
		return sb.toString();

	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

}
