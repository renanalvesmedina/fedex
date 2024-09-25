package com.mercurio.lms.entrega.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.entrega.model.service.GerarReciboReembolsoService;
import com.mercurio.lms.expedicao.model.service.ServAdicionalDocServService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.MoedaUtils;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
/**
 * @author Andrêsa Vargas
 * 
 * @spring.bean id="lms.entrega.emitirReciboReembolsoService"
 * @spring.property name="reportName" value="com/mercurio/lms/entrega/report/emitirReciboReembolso.jasper"
 */
public class EmitirReciboReembolsoService extends ReportServiceSupport {

	private GerarReciboReembolsoService gerarReciboReembolsoService;
	private TelefoneEnderecoService telefoneEnderecoService;
	private EnderecoPessoaService enderecoPessoaService;
	private ServAdicionalDocServService servAdicionalDocServService;
	private DomainValueService domainValueService;
	
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public JRReportDataObject execute(Map parameters) throws Exception {

	    TypedFlatMap tfm = (TypedFlatMap)parameters;
		
	    Long idManifestoViagem = tfm.getLong("idManisfestoViagemNacional");
	    Long idManifestoEntrega = tfm.getLong("idManifestoEntrega");
	    Long idConhecimento = tfm.getLong("idConhecimento");
	    Long idReciboReembolso = tfm.getLong("idReciboReembolso");
	    final boolean isReemitir = tfm.getBoolean("isReemitir").booleanValue();
	    
	    //Se nao for uma reemissao, executa a rotina de geracao de recibos de reembolso
	    if (!isReemitir)
	    	gerarReciboReembolsoService.generateReciboReembolso(idManifestoEntrega, idManifestoViagem,idConhecimento);
	    Map parametersReport = new HashMap();
	    parametersReport.put("isReemitir", new Boolean(isReemitir));
	    
	    //Consulta os dados do relatorio (os recibos recem-criados ou reemitidos)
	    SqlTemplate sql = createSqlTemplate(idReciboReembolso, idManifestoViagem, idManifestoEntrega, idConhecimento, isReemitir);
	    		 
		//Se nao for reemissao, atualiza a data de emissao dos documentos de servico criados e a situacao dos recibos
		if (!isReemitir)
			gerarReciboReembolsoService.updateDoctoServico(idManifestoEntrega, idManifestoViagem,idConhecimento);
		
		final List dados = new LinkedList();
		
		getJdbcTemplate().query(sql.getSql(true), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()), new ResultSetExtractor() {
			
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
										
				while(rs.next()){
					Map linha = new HashMap();
					
					linha.put("ID_RECIBO_REEMBOLSO", Long.valueOf(rs.getLong("ID_RECIBO_REEMBOLSO")));
					
					if (rs.getString("NR_RECIBO_REEMBOLSO") != null) {
						linha.put("NR_RECIBO_REEMBOLSO", FormatUtils.formatLongWithZeros(Long.valueOf(rs.getLong("NR_RECIBO_REEMBOLSO")), "00000000"));
					}
					
					if (rs.getObject("DH_EMISSAO") != null) {
						linha.put("DH_EMISSAO", JTFormatUtils.format(JodaTimeUtils.tstzToString(rs,rs.getObject("DH_EMISSAO"))));
					} else if (!isReemitir){
						linha.put("DH_EMISSAO", JTFormatUtils.format(JTDateTimeUtils.getDataAtual()));
					}
					
					linha.put("VL_REEMBOLSO", rs.getBigDecimal("VL_REEMBOLSO"));
					linha.put("VL_APLICADO", rs.getBigDecimal("VL_APLICADO"));
					linha.put("TP_VALOR_ATRIBUIDO_RECIBO", rs.getString("TP_VALOR_ATRIBUIDO_RECIBO"));
					linha.put("OB_MOTIVO_VALOR_APLICADO", rs.getString("OB_MOTIVO_VALOR_APLICADO"));
					linha.put("SG_FILIAL", rs.getString("SG_FILIAL"));
					linha.put("ID_PESSOA", Long.valueOf(rs.getLong("ID_PESSOA")));
					linha.put("TP_IDENTIFICACAO", rs.getString("TP_IDENTIFICACAO"));
					linha.put("NR_IDENTIFICACAO", rs.getString("NR_IDENTIFICACAO"));	
					
					if (rs.getString("NR_CONHECIMENTO") != null){
						linha.put("NR_CONHECIMENTO", FormatUtils.formatLongWithZeros(Long.valueOf(rs.getLong("NR_CONHECIMENTO")), "00000000"));
					}
					
					linha.put("DV_CONHECIMENTO", rs.getString("DV_CONHECIMENTO"));
					linha.put("TP_DOC_SERV_REEMBOLSADO", domainValueService.findDomainValueDescription("DM_TIPO_DOCUMENTO_SERVICO",rs.getString("TP_DOC_SERV_REEMBOLSADO")));
					linha.put("SG_FILIAL_CONHECIMENTO", rs.getString("SG_FILIAL_CONHECIMENTO"));
					linha.put("NM_PESSOA_REMETENTE", rs.getString("NM_PESSOA_REMETENTE"));
					linha.put("TP_IDENTIFICACAO_REMETENTE", rs.getString("TP_IDENTIFICACAO_REMETENTE"));
					linha.put("NR_IDENTIFICACAO_REMETENTE", rs.getString("NR_IDENTIFICACAO_REMETENTE"));
					linha.put("ID_PESSOA_REMETENTE", Long.valueOf(rs.getLong("ID_PESSOA_REMETENTE")));
					linha.put("NM_PESSOA_DESTINATARIO", rs.getString("NM_PESSOA_DESTINATARIO"));
					linha.put("TP_IDENTIFICACAO_DESTINATARIO", rs.getString("TP_IDENTIFICACAO_DESTINATARIO"));
					linha.put("NR_IDENTIFICACAO_DESTINATARIO", rs.getString("NR_IDENTIFICACAO_DESTINATARIO"));
					linha.put("ID_PESSOA_DESTINATARIO", Long.valueOf(rs.getLong("ID_PESSOA_DESTINATARIO")));
					linha.put("ROTA_COLETA_ENTREGA", rs.getString("ROTA_COLETA_ENTREGA"));
					
					
					if (rs.getString("NR_NOTA_FISCAL") != null){
						linha.put("NR_NOTA_FISCAL", FormatUtils.formatLongWithZeros(Long.valueOf(rs.getLong("NR_NOTA_FISCAL")), "00000000"));
					}						
					
					if (rs.getString("NR_MANIFESTO_ORIGEM") != null){
						linha.put("NR_MANIFESTO_ORIGEM", FormatUtils.formatLongWithZeros(Long.valueOf(rs.getLong("NR_MANIFESTO_ORIGEM")), "00000000"));
					}
					
					if (rs.getString("NR_MANIFESTO_ENTREGA") != null){
						linha.put("NR_MANIFESTO_ENTREGA", FormatUtils.formatLongWithZeros(Long.valueOf(rs.getLong("NR_MANIFESTO_ENTREGA")), "00000000"));
					}
					
					if (rs.getString("TP_MANIFESTO_E") != null){
						linha.put("TP_MANIFESTO", domainValueService.findDomainValueDescription("DM_TIPO_MANIFESTO",rs.getString("TP_MANIFESTO_E")));
					}
					
					if (rs.getString("TP_MANIFESTO_V") != null){
						linha.put("TP_MANIFESTO", domainValueService.findDomainValueDescription("DM_TIPO_MANIFESTO",rs.getString("TP_MANIFESTO_V")));
					}
					
					linha.put("VL_LIQUIDO", rs.getBigDecimal("VL_LIQUIDO"));
					linha.put("SG_FILIAL_MVN", rs.getString("SG_FILIAL_MVN"));
					linha.put("SG_FILIAL_ME", rs.getString("SG_FILIAL_ME"));
					linha.put("ID_DOCTO_SERVICO", Long.valueOf(rs.getLong("ID_DOCTO_SERVICO")));	
					linha.put("ID_DOCTO_SERVICO_ORIGINAL", Long.valueOf(rs.getLong("ID_DOCTO_SERVICO_ORIGINAL")));
					
					linha.put("vl_reembolso_extenso", findValorPorExtenso(rs.getBigDecimal("VL_REEMBOLSO"), Long.valueOf(rs.getLong("ID_MOEDA")), String.valueOf(rs.getString("DS_VALOR_EXTENSO"))));
					linha.put("ds_endereco_remetente", findEnderecoPessoa(rs.getLong("ID_PESSOA_REMETENTE")));
					linha.put("ds_endereco_destinatario", findEnderecoPessoa(rs.getLong("ID_PESSOA_DESTINATARIO")));
					linha.put("ds_telefone_pessoa", findTelefonePessoa(rs.getLong("ID_PESSOA")));
					linha.put("ds_endereco_pessoa", findEnderecoPessoa(rs.getLong("ID_PESSOA")));
										
					dados.add(linha);
					
					Map linha2 = new HashMap();
					linha2.putAll(linha);
					linha2.put("ID_PAGINA", Integer.valueOf(2));
					dados.add(linha2);
					
					Map linha3 = new HashMap();
					linha3.putAll(linha);
					linha3.put("ID_PAGINA", Integer.valueOf(3));
					dados.add(linha3);
					
					linha.put("ID_PAGINA", Integer.valueOf(1));
									
				}
				
				return null;
			}}
		);
				
		JRMapCollectionDataSource ds = new JRMapCollectionDataSource(dados);
		return createReportDataObject(ds, parametersReport);	  
	}
	
	private SqlTemplate createSqlTemplate(Long idReciboReembolso, Long idManifestoViagemNacional, Long idManifestoEntrega, Long idConhecimento, boolean isReemisao) {
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("RR.ID_RECIBO_REEMBOLSO");
		sql.addProjection("RR.NR_RECIBO_REEMBOLSO"); 
		sql.addProjection("DS.DH_EMISSAO");
		sql.addProjection("NVL(RR.VL_REEMBOLSO, 0)", "VL_REEMBOLSO");
		sql.addProjection("RR.VL_APLICADO", "VL_APLICADO");
		sql.addProjection("RR.TP_VALOR_ATRIBUIDO_RECIBO");
		sql.addProjection("RR.OB_MOTIVO_VALOR_APLICADO");
		sql.addProjection("F.SG_FILIAL");
		sql.addProjection("P.ID_PESSOA");
		sql.addProjection("P.TP_IDENTIFICACAO");
		sql.addProjection("P.NR_IDENTIFICACAO");		
		sql.addProjection("C.NR_CONHECIMENTO");
		sql.addProjection("DS_ORIG.TP_DOCUMENTO_SERVICO","TP_DOC_SERV_REEMBOLSADO");
		
		sql.addProjection("C.DV_CONHECIMENTO");
		sql.addProjection("FIL_C.SG_FILIAL", "SG_FILIAL_CONHECIMENTO");
		sql.addProjection("PES_C.NM_PESSOA", "NM_PESSOA_REMETENTE");
		sql.addProjection("PES_C.TP_IDENTIFICACAO", "TP_IDENTIFICACAO_REMETENTE");
		sql.addProjection("PES_C.NR_IDENTIFICACAO", "NR_IDENTIFICACAO_REMETENTE");
		sql.addProjection("PES_C.ID_PESSOA", "ID_PESSOA_REMETENTE");
		sql.addProjection("PES_D.NM_PESSOA", "NM_PESSOA_DESTINATARIO");
		sql.addProjection("PES_D.TP_IDENTIFICACAO", "TP_IDENTIFICACAO_DESTINATARIO");
		sql.addProjection("PES_D.NR_IDENTIFICACAO", "NR_IDENTIFICACAO_DESTINATARIO");
		sql.addProjection("PES_D.ID_PESSOA", "ID_PESSOA_DESTINATARIO");
		sql.addProjection("(SELECT MIN(NFC.NR_NOTA_FISCAL) FROM NOTA_FISCAL_CONHECIMENTO NFC WHERE NFC.ID_CONHECIMENTO = C.ID_CONHECIMENTO)", "NR_NOTA_FISCAL");
		sql.addProjection("MVN.NR_MANIFESTO_ORIGEM");
		sql.addProjection("ME.NR_MANIFESTO_ENTREGA");
		sql.addProjection("MANV.TP_MANIFESTO","TP_MANIFESTO_V");
		sql.addProjection("MANE.TP_MANIFESTO","TP_MANIFESTO_E");
		sql.addProjection("DS_ORIG.ID_DOCTO_SERVICO", "ID_DOCTO_SERVICO_ORIGINAL");
		
		sql.addProjection(new StringBuffer()
							.append("NVL(CASE WHEN RR.TP_VALOR_ATRIBUIDO_RECIBO = 'A' THEN RR.VL_REEMBOLSO + RR.VL_APLICADO \n")
							.append("		 WHEN RR.TP_VALOR_ATRIBUIDO_RECIBO = 'D' THEN RR.VL_REEMBOLSO - RR.VL_APLICADO \n")
							.append("		 ELSE RR.VL_REEMBOLSO END, 0) \n").toString()
							, "VL_LIQUIDO");
		sql.addProjection("FIL_MVN.SG_FILIAL", "SG_FILIAL_MVN");
		sql.addProjection("FIL_ME.SG_FILIAL", "SG_FILIAL_ME");
		sql.addProjection("DS.ID_DOCTO_SERVICO", "ID_DOCTO_SERVICO");
		sql.addProjection("DS.ID_MOEDA", "ID_MOEDA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("MO.DS_VALOR_EXTENSO_I", "DS_VALOR_EXTENSO"));
		sql.addProjection("(CASE WHEN RCE_REAL.NR_ROTA IS NOT NULL THEN RCE_REAL.NR_ROTA|| ' - ' ||RCE_REAL.DS_ROTA ELSE (nvl2(RCE_SUGERIDA.NR_ROTA,RCE_SUGERIDA.NR_ROTA|| ' - ' ||RCE_SUGERIDA.DS_ROTA,'')) END)","ROTA_COLETA_ENTREGA");
		 
		sql.addFrom("RECIBO_REEMBOLSO", "RR");
		sql.addFrom("FILIAL", "F");
		sql.addFrom("PESSOA", "P");
		sql.addFrom("CONHECIMENTO", "C");
		sql.addFrom("CONHECIMENTO", "C_DS");
		sql.addFrom("FILIAL", "FIL_C");
		sql.addFrom("FILIAL", "FIL_MVN");
		sql.addFrom("FILIAL", "FIL_ME");
		sql.addFrom("DOCTO_SERVICO", "DS_ORIG");
		sql.addFrom("DOCTO_SERVICO", "DS"); 
		sql.addFrom("PESSOA", "PES_C");
		sql.addFrom("PESSOA", "PES_D");
		sql.addFrom("MANIFESTO_VIAGEM_NACIONAL", "MVN");
		sql.addFrom("MANIFESTO_ENTREGA", "ME");
		sql.addFrom("MANIFESTO", "MANE");
		sql.addFrom("MANIFESTO", "MANV");
		sql.addFrom("MOEDA", "MO");
		sql.addFrom("ROTA_COLETA_ENTREGA","RCE_REAL");
		sql.addFrom("ROTA_COLETA_ENTREGA","RCE_SUGERIDA");
		
			    
		sql.addJoin("MVN.ID_MANIFESTO_VIAGEM_NACIONAL", "MANV.ID_MANIFESTO(+)");
		sql.addJoin("ME.ID_MANIFESTO_ENTREGA", "MANE.ID_MANIFESTO(+)");
		sql.addJoin("RR.ID_FILIAL", "F.ID_FILIAL");
		sql.addJoin("RR.ID_DOCTO_SERV_REEMBOLSADO", "C.ID_CONHECIMENTO");
		sql.addJoin("C.ID_FILIAL_ORIGEM", "FIL_C.ID_FILIAL");
		sql.addJoin("F.ID_FILIAL", "P.ID_PESSOA");
		sql.addJoin("RR.ID_DOCTO_SERV_REEMBOLSADO", "DS_ORIG.ID_DOCTO_SERVICO");
		sql.addJoin("DS.ID_CLIENTE_REMETENTE", "PES_C.ID_PESSOA");
		sql.addJoin("DS.ID_CLIENTE_DESTINATARIO", "PES_D.ID_PESSOA");		
		sql.addJoin("DS.ID_MOEDA","MO.ID_MOEDA");
		sql.addJoin("RR.ID_MANIFESTO_VIAGEM_NACIONAL", "MVN.ID_MANIFESTO_VIAGEM_NACIONAL (+)");
		sql.addJoin("MVN.ID_FILIAL", "FIL_MVN.ID_FILIAL (+)");
		sql.addJoin("RR.ID_MANIFESTO_ENTREGA", "ME.ID_MANIFESTO_ENTREGA (+)");    
		sql.addJoin("ME.ID_FILIAL", "FIL_ME.ID_FILIAL (+)");
		sql.addJoin("RR.ID_RECIBO_REEMBOLSO", "DS.ID_DOCTO_SERVICO");
		sql.addJoin("DS.ID_ROTA_COLETA_ENTREGA_REAL", "RCE_REAL.ID_ROTA_COLETA_ENTREGA(+)");
		sql.addJoin("DS.ID_ROTA_COLETA_ENTREGA_SUGERID", "RCE_SUGERIDA.ID_ROTA_COLETA_ENTREGA(+)");
		sql.addJoin("DS.ID_DOCTO_SERVICO", "C_DS.ID_CONHECIMENTO (+)");

		if (isReemisao)
			sql.addCustomCriteria("DS.DH_EMISSAO IS NOT NULL");
		else
			sql.addCustomCriteria("DS.DH_EMISSAO IS NULL");
		
		sql.addCriteria("RR.ID_RECIBO_REEMBOLSO", "=", idReciboReembolso);
		sql.addCriteria("RR.ID_MANIFESTO_VIAGEM_NACIONAL", "=", idManifestoViagemNacional);
		sql.addCriteria("RR.ID_MANIFESTO_ENTREGA", "=", idManifestoEntrega);
		sql.addCriteria("C_DS.ID_CONHECIMENTO", "=", idConhecimento);
		
		return sql;
	}

	/**
	 * Consulta o endereco comercial padrao
	 * @param idPessoa
	 * @return
	 */
	public String findEnderecoPessoa(Long idPessoa){
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idPessoa);
		
		if (enderecoPessoa != null ) {
			String dsTipoLogradouro = String.valueOf("");
			
			if (enderecoPessoa.getTipoLogradouro() != null && enderecoPessoa.getTipoLogradouro().getDsTipoLogradouro() != null){
				dsTipoLogradouro = enderecoPessoa.getTipoLogradouro().getDsTipoLogradouro().getValue();
			}			
			
			String endereco = FormatUtils.formatEnderecoPessoa(dsTipoLogradouro, enderecoPessoa.getDsEndereco(), enderecoPessoa.getNrEndereco(), enderecoPessoa.getDsComplemento());
			if(enderecoPessoa.getMunicipio()!= null){
				endereco = endereco.concat(" - ".concat(enderecoPessoa.getMunicipio().getNmMunicipio()));
				if(enderecoPessoa.getMunicipio().getUnidadeFederativa()!= null)
					endereco = endereco.concat("/".concat(enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));
			}
			return endereco;
		}
		
		return "";
	}
	
	/**
	 * Consulta o telefone padrao
	 * @param idPessoa
	 * @return
	 */
	public String findTelefonePessoa(Long idPessoa){
		Map retorno = telefoneEnderecoService.findTelefoneEnderecoByPessoaTelefoneEnderecoPessoa(idPessoa, "C", null);
		
		if (retorno != null){
			return FormatUtils.formatTelefone((String)retorno.get("nrTelefone"), (String)retorno.get("nrDdd"), null);
		}
		
		return "";
	} 
	
	
	public String findValorPorExtenso(Number valor, Long idMoeda, String dsValorExtenso){
		Moeda moeda = new Moeda();
		moeda.setIdMoeda(idMoeda);
		moeda.setDsValorExtenso(new VarcharI18n(dsValorExtenso));
		return MoedaUtils.formataPorExtenso(valor, moeda);
	}
	
	/**
	 * Sub-relatorio cheques de reembolso
	 * @param idReciboReembolso
	 * @param idDoctoServico
	 * @return
	 */
	public JRDataSource executeChequesReembolso(Long idReciboReembolso, Long idDoctoServico){
		SqlTemplate sql = montaSqlChequeReembolso(idReciboReembolso);
				
		final List dados = new LinkedList();		
		final Long auxIdDoctoServico = idDoctoServico;
		getJdbcTemplate().query(sql.getSql(true), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()), new ResultSetExtractor() {
			
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {				
				
				List cheques = new ArrayList();
				//carrega todos os cheques encontrados
				while (rs.next()) {
				
					Map linha = new HashMap();		
		 			
					linha.put("NR_CHEQUE", rs.getString("NR_CHEQUE"));	
					linha.put("DT_VENC", rs.getDate("DT_CHEQUE"));	
					linha.put("NR_BANCO", rs.getString("NR_BANCO"));					
					linha.put("VL_CHEQUE", FormatUtils.formatDecimal("#,##0.00", rs.getBigDecimal("VL_CHEQUE")));
					
					cheques.add(linha);					
				}				
				//se nao encontrou cheques, consulta a qtd informada no servico adicional do docto de servico
				int totalCheques = 0;
				if (cheques.size() <= 0){
					Integer total = servAdicionalDocServService.findQtChequesReembolsoByDoctoServico(auxIdDoctoServico);
					totalCheques =  total != null ? total.intValue() : 0;
				} else {
					totalCheques = cheques.size();
				}				
				
				//calcula o numero de linhas necessario para distribuir os cheques em duas colunas
				int numLinhas = 0;				
				if (totalCheques > 1 && (totalCheques % 2) == 0) {
					numLinhas = totalCheques / 2;
				} else {
					numLinhas = (totalCheques + 1) / 2; 
				}
				//cria as linhas que serao utilizadas no relatorio, distribuindo os dados em duas colunas
				//de forma que a primeira metade dos dados fique na primeira coluna e o restante na segunda coluna 
				for (int i=0; i <= numLinhas-1 ;i++ ){
					Map novaLinha = new HashMap();				
					//primeira coluna
					novaLinha.put("ORD_1", Integer.valueOf(i+1));
					if (cheques.size() > 0)  {
						Map linha = (Map) cheques.get(i);
						novaLinha.put("NR_CHEQUE_1", linha.get("NR_CHEQUE"));				
						novaLinha.put("NR_BANCO_1", linha.get("NR_BANCO"));
						novaLinha.put("VL_CHEQUE_1", linha.get("VL_CHEQUE"));
						novaLinha.put("DT_VENC_1", linha.get("DT_VENC"));
					}
					//segunda coluna (deve ficar com um espaco em branco quando o total de cheques for impar)
					if (i+(numLinhas) <= totalCheques-1) {
						novaLinha.put("ORD_2", Integer.valueOf(i+numLinhas+1));
						if (cheques.size() > 0)  {
							Map linha = (Map) cheques.get(i+(numLinhas));							
							novaLinha.put("NR_CHEQUE_2", linha.get("NR_CHEQUE"));				
							novaLinha.put("NR_BANCO_2", linha.get("NR_BANCO"));
							novaLinha.put("VL_CHEQUE_2", linha.get("VL_CHEQUE"));
							novaLinha.put("DT_VENC_2", linha.get("DT_VENC"));
						}
					}
					
				dados.add(novaLinha);
				}
							
				
				return null;
			}}
		);
		
			
		return new JRMapCollectionDataSource(dados);			
					
		
	}
	
	private SqlTemplate montaSqlChequeReembolso(Long idReciboReembolso){
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("CR.NR_CHEQUE");
		sql.addProjection("CR.NR_BANCO");
		sql.addProjection("CR.VL_CHEQUE");
		sql.addProjection("CR.DT_CHEQUE");
		sql.addFrom("CHEQUE_REEMBOLSO", "CR");      
		sql.addCriteria("CR.ID_RECIBO_REEMBOLSO" ,"=", idReciboReembolso);
		
		return sql;
	}
	
	
	
	public GerarReciboReembolsoService getGerarReciboReembolsoService() {
		return gerarReciboReembolsoService;
	}

	public void setGerarReciboReembolsoService(
			GerarReciboReembolsoService gerarReciboReembolsoService) {
		this.gerarReciboReembolsoService = gerarReciboReembolsoService;
	}

	/**
	 * @param telefoneEnderecoService The telefoneEnderecoService to set.
	 */
	public void setTelefoneEnderecoService(
			TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}

	/**
	 * @param enderecoPessoaService The enderecoPessoaService to set.
	 */
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	/**
	 * @param servAdicionalDocServService The servAdicionalDocServService to set.
	 */
	public void setServAdicionalDocServService(
			ServAdicionalDocServService servAdicionalDocServService) {
		this.servAdicionalDocServService = servAdicionalDocServService;
	}
}
