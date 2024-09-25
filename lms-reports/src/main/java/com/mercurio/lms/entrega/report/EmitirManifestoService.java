package com.mercurio.lms.entrega.report;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.service.DadosComplementoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.seguros.model.ApoliceSeguro;
import com.mercurio.lms.seguros.model.ReguladoraSeguro;
import com.mercurio.lms.seguros.model.service.ApoliceSeguroService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.SeguroCliente;
import com.mercurio.lms.vendas.util.ConstantesEventosPCE;
import net.sf.jasperreports.engine.JRDataSource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
/**
 * @author Andrêsa Vargas
 *
 * @spring.bean id="lms.entrega.emitirManifestoService"
 * @spring.property name="reportName" value="com/mercurio/lms/entrega/report/emitirManifesto.jasper"
 */
public class EmitirManifestoService extends ReportServiceSupport {

	private DadosComplementoService dadosComplementoService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private ApoliceSeguroService apoliceSeguroService;
	private ConfiguracoesFacade configuracoesFacade;
	private ManifestoService manifestoService;
	private InscricaoEstadualService inscricaoEstadualService;

	public JRReportDataObject execute(Map parameters) throws Exception {

		manifestoService.flushModeParaManual();

		TypedFlatMap tfm = (TypedFlatMap) parameters;

		SqlTemplate sql = getSqlTemplate(tfm);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

		Map parametersReport = new HashMap();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());

		Moeda moeda = SessionUtils.getMoedaSessao();
		Pais pais = SessionUtils.getPaisSessao();
		parametersReport.put("ID_MOEDA",moeda.getIdMoeda());
		parametersReport.put("ID_PAIS",pais.getIdPais());
		parametersReport.put("DS_SIMBOLO",moeda.getDsSimbolo());

		//Obs SPP
		parametersReport.put("OBS_SPP",ConstantesExpedicao.SPP_NATURA);

		//OBS APOLICE
		String obsApolice = montaMensagemSeguros(manifestoService.findById(tfm.getLong("idManifesto")));
		parametersReport.put("OBS_APOLICE", obsApolice);

		//INSCRICAO_ESTADUAL
		Filial filialLogada = SessionUtils.getFilialSessao();
		InscricaoEstadual ie = inscricaoEstadualService.findIeByIdPessoaAtivoPadrao(filialLogada.getIdFilial());
		parametersReport.put("NR_INSCRICAO_ESTADUAL", ie != null ? ie.getNrInscricaoEstadual() : "");

		jr.setParameters(parametersReport);

		manifestoService.flushModeParaAuto();

		return jr;
	}


	private SqlTemplate getSqlTemplate(TypedFlatMap map) {
		SqlTemplate mainSql = createSqlTemplate();
		SqlTemplate sql = new SqlTemplate();

		//CABEÇALHO LMS-2532
		sql.setDistinct();
		sql.addProjection("P_EMPRESA.NM_PESSOA","NM_EMPRESA");
		sql.addProjection("END_P.DS_ENDERECO","DS_ENDERECO_FILIAL");
		sql.addProjection("MUN.NM_MUNICIPIO","NM_MUNICIPIO_FILIAL");
		sql.addProjection("P_FILIAL.NR_IDENTIFICACAO","NR_CNPJ_FILIAL");

		sql.addProjection("FO.SG_FILIAL");
		sql.addProjection("RCE.NR_ROTA");
		sql.addProjection("RCE.DS_ROTA");

		//AEREO
		sql.addProjection("AW.ID_AWB", "ID_AWB");
		sql.addProjection("AW.NR_AWB", "NR_AWB");
		sql.addProjection("AW.DS_SERIE", "DS_SERIE");
		sql.addProjection("AW.DV_AWB", "DV_AWB");
		sql.addProjection("AW.TP_STATUS_AWB", "TP_STATUS_AWB");
		sql.addProjection("PESS.NM_FANTASIA", "CIA_AEREA");
		sql.addProjection("AW.TP_FRETE", "TP_FRETE");
		sql.addProjection("AORIGEM.SG_AEROPORTO", "AERO_ORIGEM");
		sql.addProjection("ADESTINO.SG_AEROPORTO", "AERO_DESTINO");
		sql.addProjection("AW.QT_VOLUMES", "QT_VOL");
		sql.addProjection("AW.PS_CUBADO", "PS_CUB");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("EMB.DS_EMBALAGEM_I"),"EMBALAGEM");

		sql.addProjection("ME.NR_MANIFESTO_ENTREGA");
		sql.addProjection("M.DH_EMISSAO_MANIFESTO");
		sql.addProjection("M.ID_MANIFESTO");	       

		// ENTREGA PARCEIRA/PARCEIRA RETIRA
		sql.addProjection("M.TP_MANIFESTO_ENTREGA","TP_MANIFESTO_ENTREGA_VALUE");
		sql.addProjection("M.TP_MANIFESTO_ENTREGA");
		sql.addProjection("P_CONSIG.NM_PESSOA","NM_CLIENTE");		

		// TRUE  POSSUA DADOS_COMPLEMENTO (SSP)
		StringBuilder sinalizaDocumento = new StringBuilder()
				.append(" CASE WHEN ( SELECT DC.DS_VALOR_CAMPO FROM DADOS_COMPLEMENTO DC ")
				.append("    			INNER JOIN CONHECIMENTO CON ON CON.ID_CONHECIMENTO = DC.ID_CONHECIMENTO ")
				.append("    			INNER JOIN INFORMACAO_DOCTO_CLIENTE IDC ON IDC.ID_INFORMACAO_DOCTO_CLIENTE = DC.ID_INFORMACAO_DOCTO_CLIENTE ")
				.append(" 			  WHERE CON.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO ")
				.append("  				    AND IDC.DS_CAMPO = 'Cód. Conf' " )
				.append(" 		    ) IS NOT NULL " )
				.append("      THEN ( ")
				.append(" 			   CASE WHEN ( SELECT DC.DS_VALOR_CAMPO ")
				.append(" 						   FROM DADOS_COMPLEMENTO DC ")
				.append(" 							 INNER JOIN CONHECIMENTO CON ON CON.ID_CONHECIMENTO = DC.ID_CONHECIMENTO ")
				.append("           				 INNER JOIN INFORMACAO_DOCTO_CLIENTE IDC ON IDC.ID_INFORMACAO_DOCTO_CLIENTE = DC.ID_INFORMACAO_DOCTO_CLIENTE  ")
				.append("        				   WHERE CON.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO ")
				.append("                                AND IDC.DS_CAMPO = 'Cód. Conf' ")
				.append("                                AND DECODE(replace(translate(DC.DS_VALOR_CAMPO,'1234567890','##########'),'#'),NULL,to_number(DC.DS_VALOR_CAMPO),-1) > 1 ")
				.append("  					     ) IS NOT NULL	")
				.append("  					THEN 'true' ")
				.append("  					ELSE 'false' ")
				.append("           	END " )
				.append("			) ")
				.append("		ELSE ")
				.append("   	  'false' " )
				.append(" END ");
		sql.addProjection(sinalizaDocumento.toString(), "SINALIZA");

		//AGENDAMENTO
		StringBuilder agendamento = new StringBuilder();
		agendamento.append(" CASE ")
				.append("			WHEN AE.DT_AGENDAMENTO IS NOT NULL THEN ")
				.append("			 	 CASE ")
				.append("					WHEN TUR.DS_TURNO IS NOT NULL THEN ")
				.append("                       (SELECT TEXTO FROM RECURSOS_MENSAGENS WHERE CHAVE = 'LMS-09123') || AE.DT_AGENDAMENTO || ' - ' || TUR.DS_TURNO ")
				.append("					WHEN AE.HR_PREFERENCIA_INICIAL IS NOT NULL THEN ")
				.append(" 					    (SELECT TEXTO FROM RECURSOS_MENSAGENS WHERE CHAVE = 'LMS-09123') || ' ' || AE.DT_AGENDAMENTO ")
				.append("							|| ' - das ' || TO_CHAR(AE.HR_PREFERENCIA_INICIAL, 'hh24:mi') || ' as ' || TO_CHAR(AE.HR_PREFERENCIA_FINAL, 'hh24:mi') ")
				.append("				 END ")
				.append("		 END ");
		sql.addProjection(agendamento.toString(),"AGENDAMENTO");

		//BL_CARTAO
		StringBuilder blCartao = new StringBuilder();
		blCartao.append(" CASE WHEN AE.BL_CARTAO = 'S' THEN ")
				.append(" 		   (SELECT TEXTO FROM RECURSOS_MENSAGENS WHERE CHAVE = 'LMS-09072') ")
				.append("     END ");
		sql.addProjection(blCartao.toString(), "BL_CARTAO");

		//BOLETOS
		StringBuilder countBoletos = new StringBuilder();
		countBoletos.append(" CASE WHEN ( SELECT Count(*) FROM MANIFESTO_ENTREGA_DOCUMENTO MED2 ")
				.append("                 WHERE MED2.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ")
				.append("                    AND MED2.ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA ")
				.append("                    AND MED2.TP_DOCUMENTO_COBRANCA = 'B') > 0 ")
				.append("		   THEN ")
				.append("             (SELECT TEXTO FROM RECURSOS_MENSAGENS WHERE CHAVE = 'LMS-09032') ")
				.append("	    END ");
		sql.addProjection(countBoletos.toString(), "COUNT_BOLETOS");

		//RECIBOS
		StringBuilder countRecibos = new StringBuilder();
		countRecibos.append(" CASE WHEN ( SELECT Count(*) FROM MANIFESTO_ENTREGA_DOCUMENTO MED2 ")
				.append("				  WHERE MED2.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ")
				.append("                     AND MED2.ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA ")
				.append("                     AND MED2.TP_DOCUMENTO_COBRANCA = 'R') > 0 ")
				.append("          THEN ")
				.append("            (SELECT TEXTO FROM RECURSOS_MENSAGENS WHERE CHAVE = 'LMS-09033') ")
				.append("       END ");
		sql.addProjection(countRecibos.toString(), "COUNT_RECIBOS" );

		//DPE
		StringBuilder dpe = new StringBuilder();
		dpe.append(" DECODE(DS.DT_PREV_ENTREGA, SYSDATE, 'Hoje', TO_CHAR(DS.DT_PREV_ENTREGA, 'dd/MM/yy')) ");
		sql.addProjection(dpe.toString(), "DT_PREV_ENTREGA" );

		//DS TIPO DOCUMENTO ENTREGA
		StringBuilder dsTipoDocumentoEntrega = new StringBuilder();
		dsTipoDocumentoEntrega.append(" ( SELECT  DISTINCT  SUBSTR(REGEXP_SUBSTR(TDE.DS_TIPO_DOCUMENTO_ENTREGA_I, 'pt_BR»[^¦]+'), ")
				.append(" 							  INSTR(REGEXP_SUBSTR(TDE.DS_TIPO_DOCUMENTO_ENTREGA_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) ")
				.append("			          FROM TIPO_DOCUMENTO_ENTREGA TDE, REGISTRO_DOCUMENTO_ENTREGA RDE ")
				.append("                     WHERE RDE.ID_TIPO_DOCUMENTO_ENTREGA = TDE.ID_TIPO_DOCUMENTO_ENTREGA ")
				.append("                           AND RDE.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ) ");
		sql.addProjection(dsTipoDocumentoEntrega.toString(), "DS_TIPO_DOCUMENTO_ENTREGA");


		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		//DESCRITIVO PCE REMETENTE
		StringBuilder pceRemetente = new StringBuilder();
		pceRemetente.append(" ( SELECT SUBSTR(REGEXP_SUBSTR(DESCRITIVO_PCE_.DS_DESCRITIVO_PCE_I, 'pt_BR»[^¦]+'), ")
				.append("                  INSTR(REGEXP_SUBSTR(DESCRITIVO_PCE_.DS_DESCRITIVO_PCE_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) ")
				.append("           FROM VERSAO_DESCRITIVO_PCE VERSAO_DESCRITIVO_PCE, DESCRITIVO_PCE DESCRITIVO_PCE_, OCORRENCIA_PCE OCORRENCIA_PCE, ")
				.append("			     EVENTO_PCE EVENTO_PCE, PROCESSO_PCE PROCESSO_PCE, VERSAO_PCE VERSAO_PCE, CLIENTE CLIENTE ")
				.append("           WHERE VERSAO_DESCRITIVO_PCE.ID_DESCRITIVO_PCE=DESCRITIVO_PCE_.ID_DESCRITIVO_PCE " )
				.append("				AND DESCRITIVO_PCE_.ID_OCORRENCIA_PCE=OCORRENCIA_PCE.ID_OCORRENCIA_PCE " )
				.append("				AND OCORRENCIA_PCE.ID_EVENTO_PCE=EVENTO_PCE.ID_EVENTO_PCE ")
				.append("				AND EVENTO_PCE.ID_PROCESSO_PCE=PROCESSO_PCE.ID_PROCESSO_PCE ")
				.append("				AND VERSAO_DESCRITIVO_PCE.ID_VERSAO_PCE=VERSAO_PCE.ID_VERSAO_PCE ")
				.append("				AND VERSAO_PCE.ID_CLIENTE=CLIENTE.ID_CLIENTE ")
				.append("				AND CLIENTE.ID_CLIENTE=DS.ID_CLIENTE_REMETENTE ")
				.append("				AND OCORRENCIA_PCE.CD_OCORRENCIA_PCE = ? ")
				.append("				AND EVENTO_PCE.CD_EVENTO_PCE = ? ")
				.append("				AND PROCESSO_PCE.CD_PROCESSO_PCE = ? ")
				.append("				AND VERSAO_PCE.DT_VIGENCIA_INICIAL <= ? AND VERSAO_PCE.DT_VIGENCIA_FINAL >= ? )");
		sql.addProjection(pceRemetente.toString(), "PCE_REMETENTE");
		mainSql.addCriteriaValue( ConstantesEventosPCE.CD_OCORRENCIA_NA_ENTREGA );
		mainSql.addCriteriaValue( ConstantesEventosPCE.CD_EVENTO_MANIFESTO_ENTREGA );
		mainSql.addCriteriaValue( ConstantesEventosPCE.CD_PROCESSO_ENTREGA );
		mainSql.addCriteriaValue(dataAtual);
		mainSql.addCriteriaValue(dataAtual);

		//DESCRITIVO PCE DESTINATARIO
		StringBuilder pceDestinatario = new StringBuilder();
		pceDestinatario.append(" ( SELECT SUBSTR(REGEXP_SUBSTR(DESCRITIVO_PCE_.DS_DESCRITIVO_PCE_I, 'pt_BR»[^¦]+'), ")
				.append("			          INSTR(REGEXP_SUBSTR(DESCRITIVO_PCE_.DS_DESCRITIVO_PCE_I, 'pt_BR»[^¦]+'),  'pt_BR»')+LENGTH('pt_BR»')) ")
				.append("	           FROM VERSAO_DESCRITIVO_PCE VERSAO_DESCRITIVO_PCE, DESCRITIVO_PCE DESCRITIVO_PCE_, OCORRENCIA_PCE OCORRENCIA_PCE, ")
				.append("		            EVENTO_PCE EVENTO_PCE, PROCESSO_PCE PROCESSO_PCE, VERSAO_PCE VERSAO_PCE, CLIENTE CLIENTE ")
				.append("  			   WHERE VERSAO_DESCRITIVO_PCE.ID_DESCRITIVO_PCE=DESCRITIVO_PCE_.ID_DESCRITIVO_PCE ")
				.append("		             AND DESCRITIVO_PCE_.ID_OCORRENCIA_PCE=OCORRENCIA_PCE.ID_OCORRENCIA_PCE ")
				.append("	                 AND OCORRENCIA_PCE.ID_EVENTO_PCE=EVENTO_PCE.ID_EVENTO_PCE ")
				.append("                    AND EVENTO_PCE.ID_PROCESSO_PCE=PROCESSO_PCE.ID_PROCESSO_PCE ")
				.append("                    AND VERSAO_DESCRITIVO_PCE.ID_VERSAO_PCE=VERSAO_PCE.ID_VERSAO_PCE ")
				.append("                    AND VERSAO_PCE.ID_CLIENTE=CLIENTE.ID_CLIENTE ")
				.append("                    AND CLIENTE.ID_CLIENTE=DS.ID_CLIENTE_DESTINATARIO ")
				.append("                    AND OCORRENCIA_PCE.CD_OCORRENCIA_PCE = ? ")
				.append("                    AND EVENTO_PCE.CD_EVENTO_PCE = ? ")
				.append("                    AND PROCESSO_PCE.CD_PROCESSO_PCE = ? ")
				.append("                    AND VERSAO_PCE.DT_VIGENCIA_INICIAL <= ? AND VERSAO_PCE.DT_VIGENCIA_FINAL >= ? ");
		sql.addProjection(pceRemetente.toString(), "PCE_DESTINATARIO");
		mainSql.addCriteriaValue( ConstantesEventosPCE.CD_OCORRENCIA_NA_ENTREGA );
		mainSql.addCriteriaValue( ConstantesEventosPCE.CD_EVENTO_MANIFESTO_ENTREGA );
		mainSql.addCriteriaValue( ConstantesEventosPCE.CD_PROCESSO_ENTREGA );
		mainSql.addCriteriaValue(dataAtual);
		mainSql.addCriteriaValue(dataAtual);


		//ENTREGA PARCEIRA/PARCEIRA RETIRA TRATAR A PROJECAO
		StringBuilder nrPlaca = new StringBuilder()
				.append(" CASE ")
				.append(" WHEN M.TP_MANIFESTO_ENTREGA <> 'EP' ")
				.append("     THEN SR.NR_PLACA")
				.append(" ELSE TRANSP.NR_IDENTIFICADOR END");
		sql.addProjection(nrPlaca.toString(),"NR_PLACA");


		StringBuilder nrFrota = new StringBuilder()
				.append(" CASE ")
				.append(" WHEN M.TP_MANIFESTO_ENTREGA <> 'EP' ")
				.append("     THEN '' ")
				.append(" ELSE TRANSP.NR_FROTA END");
		sql.addProjection(nrFrota.toString(),"NR_FROTA_SEMIR");


		StringBuilder nrPlacaSemiReboque = new StringBuilder()
				.append(" CASE ")
				.append(" WHEN M.TP_MANIFESTO_ENTREGA <> 'EP' ")
				.append("     THEN SR.NR_PLACA_SEMI_REBOQUE")
				.append(" ELSE SEMI.NR_IDENTIFICADOR END");
		sql.addProjection(nrPlacaSemiReboque.toString(),"NR_PLACA_SEMI_REBOQUE");



		// CLIENTE_RETIRA
		sql.addProjection("SR.NM_RETIRANTE");
		sql.addProjection("SR.NR_RG");
		sql.addProjection("SR.TP_IDENTIFICACAO","TP_IDENTIFICACAO_VALUE");
		sql.addProjection("SR.TP_IDENTIFICACAO");
		sql.addProjection("SR.NR_CNPJ");
		sql.addProjection("SR.NR_DDD");
		sql.addProjection("SR.NR_TELEFONE");
		// CASO CONTRÁRIO
		sql.addProjection("P_MOTO.NM_PESSOA", "NM_MOTORISTA");
		sql.addProjection("MOTO.NR_CARTEIRA_HABILITACAO", "NR_CARTEIRA_HABILITACAO");
		sql.addProjection("MARCA_TRANSP.DS_MARCA_MEIO_TRANSPORTE","DS_MARCA_MEIO_TRANSPORTE");
		sql.addProjection("MODEL.DS_MODELO_MEIO_TRANSPORTE");
		sql.addProjection("TRANSP.NR_FROTA");
		sql.addProjection("TRANSP.NR_IDENTIFICADOR","NR_IDENTIFICADOR_TRANSP");
		sql.addProjection("SEMI.NR_FROTA","NR_FROTA_SEMI_REBOQUE");
		sql.addProjection("SEMI.NR_IDENTIFICADOR","NR_IDENTIFICADOR_SEMI");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("S.DS_SETOR_I"), "DS_SETOR");
		// RODAPE
		sql.addProjection("ME.OB_MANIFESTO_ENTREGA");
		// DETALHE
		sql.addProjection("REMET.NM_PESSOA","NM_REMETENTE");
		sql.addProjection("DS.VL_MERCADORIA","VL_MERCADORIA");
		sql.addProjection("OE.CD_OCORRENCIA_ENTREGA");
		sql.addProjection("FDS.SG_FILIAL","SG_FILIAL_DOCTO_SERVICO");
		sql.addProjection("DS.ID_DOCTO_SERVICO");
		sql.addProjection("DS.NR_DOCTO_SERVICO");
		sql.addProjection("DS.TP_DOCUMENTO_SERVICO");
		sql.addProjection("DS.QT_VOLUMES");
		sql.addProjection("Greatest(Nvl(DS.PS_REAL,0),Nvl(DS.PS_AFORADO,0))","PESO");
		sql.addProjection("DEST.NM_PESSOA","NM_DESTINATARIO");
		sql.addProjection("DS.DS_ENDERECO_ENTREGA_REAL","DS_ENDERECO_ENTREGA");
		sql.addProjection("MED.OB_MANIFESTO_ENTREGA_DOCUMENTO");

		sql.addProjection("DS.ID_CLIENTE_REMETENTE");
		sql.addProjection("DS.ID_CLIENTE_DESTINATARIO");

		sql.addProjection("DS.ID_MOEDA");
		sql.addProjection("DS.ID_PAIS");

		sql.addProjection("FNC.SG_FILIAL","SG_FILIAL_NAO_CONFORMIDADE");
		sql.addProjection("NC.NR_NAO_CONFORMIDADE");

		StringBuilder vlReembolso = new StringBuilder()
				.append(" CASE ")
				.append(" WHEN RR.TP_VALOR_ATRIBUIDO_RECIBO = ? ")
				.append("     THEN RR.VL_REEMBOLSO + RR.VL_APLICADO")
				.append(" WHEN RR.TP_VALOR_ATRIBUIDO_RECIBO = ? ")
				.append("     THEN RR.VL_REEMBOLSO - RR.VL_APLICADO")
				.append(" ELSE DECODE(RR.VL_REEMBOLSO,NULL,TO_NUMBER('0'),RR.VL_REEMBOLSO) END");
		sql.addProjection(vlReembolso.toString(),"VL_REEMBOLSO");
		mainSql.addCriteriaValue("A");
		mainSql.addCriteriaValue("D");

		StringBuilder nr = new StringBuilder()
				.append(" SELECT Count(*) FROM ")
				.append(" MANIFESTO_ENTREGA_DOCUMENTO MED, ")
				.append(" MANIFESTO M2 ")
				.append(" WHERE DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO")
				.append("   AND M2.ID_MANIFESTO = MED.ID_MANIFESTO_ENTREGA")
				.append("   AND ME.ID_MANIFESTO_ENTREGA <> MED.ID_MANIFESTO_ENTREGA")
				.append("   AND M2.TP_STATUS_MANIFESTO <> ?");
		sql.addProjection("(" + nr.toString() + ")","NR");
		mainSql.addCriteriaValue("CA");

		StringBuilder nrOrdem = new StringBuilder()
				.append(" SELECT Max(PRE.NR_ORDEM) FROM ")
				.append(" PRE_MANIFESTO_DOCUMENTO PRE")
				.append(" WHERE PRE.ID_MANIFESTO = M.ID_MANIFESTO")
				.append("   AND PRE.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO");
		sql.addProjection("(" + nrOrdem.toString() + ")","NR_ORDEM");

		sql.addFrom("MANIFESTO_ENTREGA_DOCUMENTO","MED");
		sql.addFrom("MANIFESTO_ENTREGA","ME");

		sql.addFrom("MANIFESTO","M");
		sql.addFrom("FILIAL","FO");

		sql.addFrom("EMPRESA","EMP");
		sql.addFrom("PESSOA","P_EMPRESA");
		sql.addFrom("PESSOA","P_FILIAL");
		sql.addFrom("ENDERECO_PESSOA","END_P");
		sql.addFrom("MUNICIPIO","MUN");

		sql.addFrom("MOTORISTA","MOTO");
		sql.addFrom("PESSOA","P_MOTO");

		sql.addFrom("MARCA_MEIO_TRANSPORTE", "MARCA_TRANSP");

		sql.addFrom("PESSOA", "REMET");

		sql.addFrom("SOLICITACAO_RETIRADA","SR");
		sql.addFrom("CLIENTE","CONSIG");
		sql.addFrom("PESSOA","P_CONSIG");
		sql.addFrom("CONTROLE_CARGA","CC");
		sql.addFrom("MEIO_TRANSPORTE","TRANSP");
		sql.addFrom("MEIO_TRANSPORTE","SEMI");
		sql.addFrom("MODELO_MEIO_TRANSPORTE","MODEL");
		sql.addFrom("SETOR","S");
		sql.addFrom("ROTA_COLETA_ENTREGA","RCE");

		sql.addFrom("OCORRENCIA_ENTREGA","OE");
		sql.addFrom("DOCTO_SERVICO","DS");
		sql.addFrom("RECIBO_REEMBOLSO","RR");
		sql.addFrom("FILIAL","FDS");
		sql.addFrom("PESSOA","DEST");

		sql.addFrom("FILIAL","FNC");
		sql.addFrom("NAO_CONFORMIDADE","NC");


		//AEREO
		sql.addFrom("AWB", "AW");
		sql.addFrom("CIA_FILIAL_MERCURIO", "CIA");
		sql.addFrom("EMPRESA", "EMPR");
		sql.addFrom("PESSOA", "PESS");
		sql.addFrom("AEROPORTO", "AORIGEM");
		sql.addFrom("AEROPORTO", "ADESTINO");
		sql.addFrom("CTO_AWB", "CTAWB");
		sql.addFrom("AWB_EMBALAGEM", "AWB_EMB");
		sql.addFrom("EMBALAGEM", "EMB");


		sql.addFrom("(SELECT ADS2.* " +
				"FROM AGENDAMENTO_DOCTO_SERVICO ADS2, AGENDAMENTO_ENTREGA AE2 " +
				"WHERE ADS2.TP_SITUACAO           = ? " +
				"AND AE2.ID_AGENDAMENTO_ENTREGA = ADS2.ID_AGENDAMENTO_ENTREGA " +
				"AND (AE2.TP_SITUACAO_AGENDAMENTO = ? OR AE2.TP_SITUACAO_AGENDAMENTO = ?) " +
				"AND AE2.TP_AGENDAMENTO          <> ? " +
				"AND AE2.DH_CONTATO               = " +
				"  (SELECT MAX(AE3.DH_CONTATO) " +
				"  FROM AGENDAMENTO_DOCTO_SERVICO ADS3, " +
				"    AGENDAMENTO_ENTREGA AE3 " +
				"  WHERE (AE3.TP_SITUACAO_AGENDAMENTO = ? OR AE3.TP_SITUACAO_AGENDAMENTO = ?) " +
				"  AND AE3.ID_AGENDAMENTO_ENTREGA     = ADS3.ID_AGENDAMENTO_ENTREGA " +
				"  AND AE3.TP_AGENDAMENTO            <> ? " +
				"  AND ads3.id_docto_servico          = ads2.id_docto_servico " +
				"  )) ADS");
		mainSql.addCriteriaValue("A");
		mainSql.addCriteriaValue("F");
		mainSql.addCriteriaValue("A");
		mainSql.addCriteriaValue("TA");
		mainSql.addCriteriaValue("F");
		mainSql.addCriteriaValue("A");
		mainSql.addCriteriaValue("TA");

		sql.addFrom("AGENDAMENTO_ENTREGA","AE");
		sql.addFrom("TURNO TUR");

		sql.addJoin("ME.ID_MANIFESTO_ENTREGA","MED.ID_MANIFESTO_ENTREGA");
		sql.addJoin("ME.ID_MANIFESTO_ENTREGA","M.ID_MANIFESTO");
		sql.addJoin("FO.ID_FILIAL","M.ID_FILIAL_ORIGEM");

		sql.addJoin("EMP.ID_EMPRESA (+)","FO.ID_EMPRESA");
		sql.addJoin("P_EMPRESA.ID_PESSOA (+)","EMP.ID_EMPRESA");
		sql.addJoin("P_FILIAL.ID_PESSOA (+)","FO.ID_FILIAL ");
		sql.addJoin("END_P.ID_ENDERECO_PESSOA (+)","P_FILIAL.ID_ENDERECO_PESSOA");
		sql.addJoin("MUN.ID_MUNICIPIO (+)","END_P.ID_MUNICIPIO");

		sql.addJoin("SR.ID_SOLICITACAO_RETIRADA (+)","M.ID_SOLICITACAO_RETIRADA");
		sql.addJoin("CONSIG.ID_CLIENTE (+)","M.ID_CLIENTE_CONSIG");
		sql.addJoin("P_CONSIG.ID_PESSOA (+)","CONSIG.ID_CLIENTE");
		sql.addJoin("CC.ID_CONTROLE_CARGA (+)","M.ID_CONTROLE_CARGA");
		sql.addJoin("TRANSP.ID_MEIO_TRANSPORTE (+)","CC.ID_TRANSPORTADO");
		sql.addJoin("SEMI.ID_MEIO_TRANSPORTE (+)","CC.ID_SEMI_REBOCADO");

		sql.addJoin("MOTO.ID_MOTORISTA (+)", "CC.ID_MOTORISTA");
		sql.addJoin("P_MOTO.ID_PESSOA (+)", "MOTO.ID_MOTORISTA");

		sql.addJoin("REMET.ID_PESSOA (+)","DS.ID_CLIENTE_REMETENTE");

		sql.addJoin("MODEL.ID_MODELO_MEIO_TRANSPORTE (+)","TRANSP.ID_MODELO_MEIO_TRANSPORTE");

		sql.addJoin("MARCA_TRANSP.ID_MARCA_MEIO_TRANSPORTE (+)","MODEL.ID_MARCA_MEIO_TRANSPORTE");

		sql.addJoin("S.ID_SETOR (+)","ME.ID_SETOR");
		sql.addJoin("OE.ID_OCORRENCIA_ENTREGA (+)","MED.ID_OCORRENCIA_ENTREGA");

		//AEREO
		sql.addJoin("MED.ID_AWB","AW.ID_AWB (+)");
		sql.addJoin("AW.ID_AEROPORTO_ORIGEM","AORIGEM.ID_AEROPORTO (+)");
		sql.addJoin("AW.ID_AEROPORTO_DESTINO","ADESTINO.ID_AEROPORTO (+)");
		sql.addJoin("AW.ID_AWB","CTAWB.ID_AWB (+)");
		sql.addJoin("AW.ID_CIA_FILIAL_MERCURIO","CIA.ID_CIA_FILIAL_MERCURIO (+)");
		sql.addJoin("CIA.ID_EMPRESA","EMPR.ID_EMPRESA (+)");
		sql.addJoin("EMPR.ID_EMPRESA","PESS.ID_PESSOA (+)");
		sql.addJoin("AW.ID_AWB","AWB_EMB.ID_AWB (+)");
		sql.addJoin("AWB_EMB.ID_EMBALAGEM","EMB.ID_EMBALAGEM(+)");


		sql.addJoin("DS.ID_DOCTO_SERVICO","MED.ID_DOCTO_SERVICO");
		sql.addJoin("RR.ID_DOCTO_SERV_REEMBOLSADO (+)","DS.ID_DOCTO_SERVICO");
		sql.addJoin("FDS.ID_FILIAL","DS.ID_FILIAL_ORIGEM");
		sql.addJoin("DEST.ID_PESSOA (+)","DS.ID_CLIENTE_DESTINATARIO");
		sql.addJoin("RCE.ID_ROTA_COLETA_ENTREGA (+)","CC.ID_ROTA_COLETA_ENTREGA");
		sql.addJoin("ADS.ID_DOCTO_SERVICO (+)","DS.ID_DOCTO_SERVICO");
		sql.addJoin("AE.ID_AGENDAMENTO_ENTREGA (+)","ADS.ID_AGENDAMENTO_ENTREGA");
		sql.addJoin("TUR.ID_TURNO (+)","AE.ID_TURNO");
		sql.addJoin("NC.ID_DOCTO_SERVICO (+)","DS.ID_DOCTO_SERVICO");
		sql.addJoin("FNC.ID_FILIAL (+)","NC.ID_FILIAL");

		sql.addCustomCriteria("M.TP_MANIFESTO = ?");
		mainSql.addCriteriaValue("E");


		Long idManifesto = map.getLong("idManifesto");
		if (idManifesto != null) {
			sql.addCustomCriteria("M.ID_MANIFESTO = ?");
			mainSql.addCriteriaValue(idManifesto);
		}

		//AEREO
		sql.addOrderBy("AW.ID_AWB");

		String tpOrdemDoc = map.getString("filial.tpOrdemDoc");
		//Verifica qual tipo de ordenação para listagem de ctrc
		if(tpOrdemDoc != null && !"".equals(tpOrdemDoc)) {
			if("DA".equals(tpOrdemDoc)) {
				sql.addOrderBy("DS.DH_INCLUSAO", "ASC");
			} else if("AA".equals(tpOrdemDoc)){
				sql.addOrderBy("FDS.SG_FILIAL", "ASC");
				sql.addOrderBy("DS.NR_DOCTO_SERVICO", "ASC");
			}
		}

		mainSql.addProjection("ROWNUM","LINHA");
		mainSql.addProjection("T1.*");
		mainSql.addProjection("CASE WHEN T1.TP_STATUS_AWB = 'E' THEN LPAD(T1.DS_SERIE, 4, '0') || '.' || LPAD(T1.NR_AWB, 6, '0') || '-' || T1.DV_AWB ELSE TO_CHAR(T1.ID_AWB) END", "AWB");
		mainSql.addFrom("(" + sql.getSql() + ")","T1");

		return mainSql;
	}

	private SqlTemplate getSqlTemplateManifestoNotasFiscais(Long idDoctoServico) {

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("NFC.NR_NOTA_FISCAL","NR_NOTA_FISCAL_CONHECIMENTO");
		sql.addProjection("NFC.DS_SERIE","DS_SERIE");

		sql.addFrom("DOCTO_SERVICO","DS");
		sql.addFrom("CONHECIMENTO","CO");
		sql.addFrom("NOTA_FISCAL_CONHECIMENTO","NFC");

		sql.addJoin("CO.ID_CONHECIMENTO","DS.ID_DOCTO_SERVICO");
		sql.addJoin("NFC.ID_CONHECIMENTO","CO.ID_CONHECIMENTO");

		sql.addCriteria("DS.ID_DOCTO_SERVICO", "=" , idDoctoServico);

		return sql;

	}

	/**
	 * Busca as notas fiscais referente ao documento passado por parâmetro
	 * @param idDoctoServico
	 * @return
	 */
	public JRDataSource executeManifestoNotasFiscais(Long idDoctoServico){

		JRDataSource data = null;
		SqlTemplate sql = getSqlTemplateManifestoNotasFiscais(idDoctoServico);

		data = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();

		return data;

	}

	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_IDENTIFICACAO", "DM_TIPO_IDENTIFICACAO");
		config.configDomainField("TP_MANIFESTO_ENTREGA", "DM_TIPO_MANIFESTO_ENTREGA");
		config.configDomainField("TP_DOCUMENTO_SERVICO", "DM_TIPO_DOCUMENTO_SERVICO");
	}

	public JRDataSource executeComprovantes(Long idDoctoServico) throws Exception {
		SqlTemplate sql = createSqlTemplate();

		sql.addProjection(" DISTINCT "+PropertyVarcharI18nProjection.createProjection("TDE.DS_TIPO_DOCUMENTO_ENTREGA_I","DS_TIPO_DOCUMENTO_ENTREGA"));

		sql.addFrom("TIPO_DOCUMENTO_ENTREGA","TDE");
		sql.addFrom("REGISTRO_DOCUMENTO_ENTREGA","RDE");

		sql.addJoin("RDE.ID_TIPO_DOCUMENTO_ENTREGA","TDE.ID_TIPO_DOCUMENTO_ENTREGA");

		sql.addCriteria("RDE.ID_DOCTO_SERVICO","=",idDoctoServico);

		sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("TDE.DS_TIPO_DOCUMENTO_ENTREGA_I"));

		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}

	public String getLinhaNotasFiscais(Long idDoctoServico){
		StringBuilder stringBuilder = new StringBuilder();
		List<NotaFiscalConhecimento> list = notaFiscalConhecimentoService.findByConhecimento(idDoctoServico);
		if (list != null && !list.isEmpty()){
			for (NotaFiscalConhecimento nota: list){
				if (StringUtils.isNotBlank(stringBuilder.toString())){
					stringBuilder.append(", ");
				}
				stringBuilder.append(nota.getNrNotaFiscal().toString());
				if(nota.getDsSerie() != null && StringUtils.isNotBlank(nota.getDsSerie())){
					stringBuilder.append("-").append(nota.getDsSerie());
				}
			}
		}
		return stringBuilder.toString();
	}

	public String getObsManifestoConhecimento(String obManifestoEntregaDocumento, String agendamento, String blCartao, String pceRemetente,
											  String pceDestinario, String countBoleto, String countRecibo) {
		StringBuilder line = new StringBuilder();
		String[] fields = new String[] { obManifestoEntregaDocumento, agendamento, blCartao, pceRemetente, pceDestinario, countBoleto, countRecibo };

		for (String field:fields){
			if (StringUtils.isNotBlank(field)) {
				if (StringUtils.isNotBlank(line.toString())){
					line.append(" - ");
				}
				line.append(field);
			}
		}

		return line.toString();
	}

	/**
	 * Método responsável por montar a mensagem a ser usada na observação com informações dos seguros. Demanda LMS-3702
	 * @param manifesto
	 * @return String contendo as mensagens com apólices da TNT e do Cliente
	 */
	private String montaMensagemSeguros(Manifesto manifesto) {
		StringBuilder mensagens = null;
		List<ApoliceSeguro> retornaApolices = apoliceSeguroService.retornaApolices(manifesto.getTpModal().getValue(), new Date());

		if(CollectionUtils.isEmpty(retornaApolices)){
			throw new BusinessException("LMS-04507");
		}

		String[] parametrosTNT = {"", "", ""};

		//1) Buscar dados da(s) seguradora(s) TNT

		boolean variasApolices = !retornaApolices.isEmpty() && retornaApolices.size()>1;
		boolean mesmaSeguradora = true;
		ApoliceSeguro apoliceDeMaiorValor = null;

		for (ApoliceSeguro as : retornaApolices) {
			apoliceDeMaiorValor = (apoliceDeMaiorValor != null && apoliceDeMaiorValor.getVlLimiteApolice().doubleValue() > as.getVlLimiteApolice().doubleValue()) ? apoliceDeMaiorValor : as ;
			if(!apoliceDeMaiorValor.getSeguradora().getIdSeguradora().equals(as.getSeguradora().getIdSeguradora())){
				mesmaSeguradora = false;
			}
		}

		if(variasApolices){
			if(mesmaSeguradora){
				for (ApoliceSeguro as : retornaApolices){
					parametrosTNT[0] += as.getTipoSeguro().getSgTipo()+" "+as.getNrApolice()+", ";
				}
				parametrosTNT[1] += apoliceDeMaiorValor.getSeguradora().getPessoa().getNmFantasia()+" CNPJ "+FormatUtils.formatIdentificacao(apoliceDeMaiorValor.getSeguradora().getPessoa())+" ";
				parametrosTNT[2] += NumberFormat.getInstance().format(apoliceDeMaiorValor.getVlLimiteApolice().doubleValue());
			}else{
				for (ApoliceSeguro as : retornaApolices){
					parametrosTNT[0] += as.getTipoSeguro().getSgTipo()+" "+as.getNrApolice()+" "+apoliceDeMaiorValor.getSeguradora().getPessoa().getNmFantasia()+" CNPJ "+FormatUtils.formatIdentificacao(apoliceDeMaiorValor.getSeguradora().getPessoa());
				}
				parametrosTNT[2] += NumberFormat.getInstance().format(apoliceDeMaiorValor.getVlLimiteApolice().doubleValue())+".";
			}
		}else{
			for (ApoliceSeguro as : retornaApolices){
				parametrosTNT[0] += as.getTipoSeguro().getSgTipo()+" " +as.getNrApolice();
			}
			parametrosTNT[1] += apoliceDeMaiorValor.getSeguradora().getPessoa().getNmFantasia()+" CNPJ "+FormatUtils.formatIdentificacao(apoliceDeMaiorValor.getSeguradora().getPessoa())+" ";
			parametrosTNT[2] += NumberFormat.getInstance().format(apoliceDeMaiorValor.getVlLimiteApolice().doubleValue());
		}

		mensagens = new StringBuilder(getConfiguracoesFacade().getMensagem("LMS-04410", parametrosTNT ).replace("  ", " ").replace(", ,", ", "));
		mensagens.append("\n\n");

		Map<Cliente, Map<ReguladoraSeguro, List<SeguroCliente>>> segurosClientes = new HashMap<Cliente, Map<ReguladoraSeguro, List<SeguroCliente>>>();

		for(PreManifestoDocumento pmd : manifesto.getPreManifestoDocumentos()){

			DoctoServico doc = pmd.getDoctoServico();
			Cliente cli = doc.getClienteByIdClienteRemetente();

			for(Object obj : cli.getSeguroClientes()){
				SeguroCliente sc = (SeguroCliente) obj;
				if("N".equals(sc.getTpAbrangencia().getValue())//Nacional
						&& sc.getDtVigenciaInicial().toDateMidnight().toDate().before(new Date())//esta vigente?
						&& sc.getDtVigenciaFinal().toDateMidnight().toDate().after(new Date())){

					Map<ReguladoraSeguro, List<SeguroCliente>> seguros = null;
					if(!segurosClientes.containsKey(cli)){
						seguros = new HashMap<ReguladoraSeguro, List<SeguroCliente>>();
						segurosClientes.put(cli, seguros);
					}else{
						seguros = segurosClientes.get(cli);
					}

					if(!seguros.containsKey(sc.getReguladoraSeguro())){
						seguros.put(sc.getReguladoraSeguro(), new ArrayList<SeguroCliente>());
					}
					if(!seguros.get(sc.getReguladoraSeguro()).contains(sc)){
						seguros.get(sc.getReguladoraSeguro()).add(sc);
					}
				}
			}
		}

		//imprime os valores no formulario
		if(!segurosClientes.isEmpty()){

			Iterator<Cliente> iteratorClientes = segurosClientes.keySet().iterator();

			while(iteratorClientes.hasNext()){
				String[] param = {"", "", ""};
				BigDecimal valor = BigDecimal.ZERO;
				Cliente cliente = iteratorClientes.next();
				param[0] = (cliente.getPessoa().getNmFantasia()!=null?cliente.getPessoa().getNmFantasia():cliente.getPessoa().getNmPessoa())+" CNPJ "+FormatUtils.formatIdentificacao(cliente.getPessoa());

				Map<ReguladoraSeguro, List<SeguroCliente>> map = segurosClientes.get(cliente);
				Iterator<ReguladoraSeguro> iteratorSeguros = map.keySet().iterator();
				while(iteratorSeguros.hasNext()){

					ReguladoraSeguro next = iteratorSeguros.next();
					List<SeguroCliente> list = map.get(next);

					for (SeguroCliente seguroCliente : list) {
						if (!param[1].isEmpty()) {
							param[1] += ", ";
						}

						param[1] += seguroCliente.getTipoSeguro().getSgTipo() + " " + seguroCliente.getDsApolice();

						if (seguroCliente.getReguladoraSeguro() != null) {
							param[1] += ", " + seguroCliente.getReguladoraSeguro().getPessoa().getNmPessoa() + " CNPJ " + FormatUtils.formatIdentificacao(cliente.getPessoa());
						}

						valor = seguroCliente.getVlLimite().doubleValue() > valor.doubleValue() ? seguroCliente.getVlLimite() : valor;
					}

				}

				param[2] = NumberFormat.getCurrencyInstance().format(valor).replace("R$ ", "");

				//LMS-04411 "Apólice(s) própria(s): {0} apólice(s) de seguro(s) {1}, averbação de R$ {2}."
				mensagens.append(getConfiguracoesFacade().getMensagem("LMS-04411", param));
				if(iteratorClientes.hasNext()) {
					mensagens.append("\n\n");
				}
			}

		}

		return mensagens.toString();
	}

	public DadosComplementoService getDadosComplementoService() {
		return dadosComplementoService;
	}

	public void setDadosComplementoService(DadosComplementoService dadosComplementoService) {
		this.dadosComplementoService = dadosComplementoService;
	}


	public void setNotaFiscalConhecimentoService(
			NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}


	public ApoliceSeguroService getApoliceSeguroService() {
		return apoliceSeguroService;
	}


	public void setApoliceSeguroService(ApoliceSeguroService apoliceSeguroService) {
		this.apoliceSeguroService = apoliceSeguroService;
	}


	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}


	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}


	public ManifestoService getManifestoService() {
		return manifestoService;
	}


	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}


	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}

}