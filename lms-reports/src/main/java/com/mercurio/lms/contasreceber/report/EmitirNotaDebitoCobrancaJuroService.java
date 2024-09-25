package com.mercurio.lms.contasreceber.report;


import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.NotaDebitoNacional;
import com.mercurio.lms.contasreceber.model.service.CalcularJurosDiarioService;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.contasreceber.model.service.NotaDebitoNacionalService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.MoedaUtils;







/**
*
* @spring.bean id="lms.contasreceber.emitirNotaDebitoCobrancaJuroService"
* @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirNotaDebitoCobrancaJuros.jasper"
*/
public class EmitirNotaDebitoCobrancaJuroService extends ReportServiceSupport {
	
	EnderecoPessoaService enderecoPessoaService;
	
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	private MoedaService moedaService;
	public void setMoedaService(MoedaService moedaService){
		this.moedaService = moedaService;
	}

	private FaturaService faturaService;
	public void setFaturaService(FaturaService faturaService){
		this.faturaService = faturaService;
	}	
	
	private NotaDebitoNacionalService notaDebitoNacionalService;
	public void setNotaDebitoNacionalService(NotaDebitoNacionalService notaDebitoNacionalService){
		this.notaDebitoNacionalService = notaDebitoNacionalService;
	}	
	
	private CalcularJurosDiarioService calcularJurosDiarioService;
	public void setCalcularJurosDiarioService(CalcularJurosDiarioService calcularJurosDiarioService){
		this.calcularJurosDiarioService = calcularJurosDiarioService;
	}
	
	
	
	public JRReportDataObject execute(Map criteria) throws Exception {
		
		TypedFlatMap map = (TypedFlatMap)criteria;		
		
		SqlTemplate sql = mountSql(map);
		
		//lista com o retorno da consulta
		List retorno = (List)getJdbcTemplate().query(sql.getSql(), sql.getCriteria(), new ResultSetExtractor() {
	
			
			double somaJuros = 0.0;
			long idMoeda = 0;
			long idFatura = -1;
			long idDoctoServAnt = 0;
			private DomainValue tpSituacaoCancelamento = null;
			private DomainValue tpSituacaoNotaDebitoNacional = null;
			private DomainValue tpSituacaoDesconto = null;
			
			//tratamento para cada tupla da consulta
			public Object extractData(ResultSet rs) throws SQLException {
				Map mapRet = null;
				List lst = new ArrayList();
				
				while(rs.next()){					
				 	
					if( StringUtils.isNotBlank(rs.getString("TP_SITUACAO_DESCONTO")) ){
						tpSituacaoDesconto = new DomainValue(rs.getString("TP_SITUACAO_DESCONTO"));
					}
					
					if( StringUtils.isNotBlank(rs.getString("TP_SITUACAO_CANCELAMENTO")) ){
						tpSituacaoCancelamento = new DomainValue(rs.getString("TP_SITUACAO_CANCELAMENTO"));
					}
					
					if( StringUtils.isNotBlank(rs.getString("TP_SITUACAO_NOTA_DEBITO_NAC")) ){
						tpSituacaoNotaDebitoNacional = new DomainValue(rs.getString("TP_SITUACAO_NOTA_DEBITO_NAC"));
					}
					
					notaDebitoNacionalService.validaAlteracaoExclusaoNotaDebito(tpSituacaoDesconto,tpSituacaoCancelamento,tpSituacaoNotaDebitoNacional);	
					
				//map que sera retornado
				 mapRet = new HashMap();
				 
				 //quando mudar o id zera o somaJuros
				 if ( idDoctoServAnt != rs.getLong("ID_NOT_DEB_NAC") ){
					 
						idFatura = rs.getLong("ID_FATURA") ;
						idDoctoServAnt = rs.getLong("ID_NOT_DEB_NAC");
						somaJuros = 0.0;						
					 
				 }
				
				//pegando o endereco padrao FILIAL
				EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao( Long.valueOf( rs.getLong("ID_FILIAL")) );
				
				mapRet.put("ENDERECO",  enderecoPessoaService.formatEnderecoPessoaComplemento( enderecoPessoa.getIdEnderecoPessoa() )  );
				mapRet.put("BAIRRO",enderecoPessoa.getDsBairro());
				mapRet.put("CIDADE",enderecoPessoa.getMunicipio().getNmMunicipio() );
				mapRet.put("UF",enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa() );
				mapRet.put("CEP",FormatUtils.formatCep("BRA",enderecoPessoa.getNrCep()  ));
			
				if (enderecoPessoa != null && !enderecoPessoa.getTelefoneEnderecos().isEmpty()) {
					TelefoneEndereco telefoneEndereco = (TelefoneEndereco) enderecoPessoa.getTelefoneEnderecos().get(0);
					mapRet.put("TELEFONE", FormatUtils.formatTelefone(telefoneEndereco.getNrTelefone(), telefoneEndereco.getNrDdd(), telefoneEndereco.getNrDdi()));
				} else {
					mapRet.put("TELEFONE", "");
				}
				
				mapRet.put("FILIAL", rs.getString("FILIAL"));
				mapRet.put("CNPJ",	 FormatUtils.formatCNPJ( rs.getString("CNPJ") ));
				mapRet.put("IE",	 rs.getString("IE"));
				
				
				//pegando o endereco de cobranca CLIENTE
				EnderecoPessoa enderecoCobranca = enderecoPessoaService.findEnderecoPessoaCobranca( Long.valueOf( rs.getLong("ID_CLIENTE")),JTDateTimeUtils.getDataAtual() );
				
				mapRet.put("ENDERECO_CLIENTE",  enderecoPessoaService.formatEnderecoPessoaComplemento( enderecoCobranca.getIdEnderecoPessoa() )  );
				mapRet.put("BAIRRO_CLIENTE",enderecoCobranca.getDsBairro());
				mapRet.put("CIDADE_CLIENTE",enderecoCobranca.getMunicipio().getNmMunicipio() );
				mapRet.put("UF_CLIENTE",enderecoCobranca.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa() );
				mapRet.put("CEP_CLIENTE",FormatUtils.formatCep("BRA",enderecoCobranca.getNrCep()  ));
				
				if (enderecoCobranca != null && !enderecoCobranca.getTelefoneEnderecos().isEmpty()) {
					TelefoneEndereco telefoneEnderecoCob = (TelefoneEndereco) enderecoCobranca.getTelefoneEnderecos().get(0);
					mapRet.put("TELEFONE_CLIENTE", FormatUtils.formatTelefone(telefoneEnderecoCob.getNrTelefone(), telefoneEnderecoCob.getNrDdd(), telefoneEnderecoCob.getNrDdi()));
				} else {
					mapRet.put("TELEFONE_CLIENTE", "");
				}				
				
				mapRet.put("CLIENTE",		rs.getString("CLIENTE"));
				mapRet.put("IDENT_CLIENTE",	FormatUtils.formatIdentificacao(rs.getString("TP_IDENTIFICACAO"),rs.getString("IDENT_CLIENTE")));
				mapRet.put("IE_CLIENTE",	rs.getString("IE_CLIENTE"));

				mapRet.put("ID_NOTA_DEBITO_NACIONAL",		rs.getLong("ID_NOT_DEB_NAC"));
				
				
				//DETALHE
				mapRet.put("ROMANEIO", rs.getString("SG_FILIAL_FATURA") +" " +	FormatUtils.completaDados(rs.getString("NR_FATURA"),"0",10,0,true) );
				mapRet.put("VALOR_ORIGINAL", new Double( rs.getDouble("VALOR_ORIGINAL")));
				mapRet.put("VENCIMENTO",	 rs.getDate("VENCIMENTO"));
				mapRet.put("DEPOSITO",		 rs.getDate("DEPOSITO"));
				mapRet.put("VALOR_JURO",	 new Double( rs.getDouble("VALOR_JURO")));
				
				//CABECALHO
				mapRet.put("NUMERO",		 rs.getString("NUMERO"));
				
				//RODAPE
				mapRet.put("OBSERVACAO",		 rs.getString("OBSERVACAO"));
				
				//somando o valor do juros
				somaJuros += rs.getDouble("VALOR_JURO") ;
				idMoeda = rs.getLong("ID_MOEDA") ;
				
//				atualizando o TP_SITUACAO_NOTA_DEBITO_NAC PARA E , regra 2.2 ET
				NotaDebitoNacional notaDebitoNacional = notaDebitoNacionalService.findById(Long.valueOf(rs.getLong("ID_NOT_DEB_NAC") ));
				notaDebitoNacional.setTpSituacaoNotaDebitoNac(new DomainValue("E"));
				notaDebitoNacionalService.store(notaDebitoNacional);
				
				
//				calcular os dias de atraso
				int dif = 0; 
				YearMonthDay dtDeposito 	= JTFormatUtils.buildYmd( rs.getDate("DEPOSITO") );
				YearMonthDay dtVencimento 	= JTFormatUtils.buildYmd( rs.getDate("VENCIMENTO") );
				dif = JTDateTimeUtils.getIntervalInDays(dtVencimento,dtDeposito);				
				
//				colocando a dif em dias
				mapRet.put("DIAS_ATRASO",new Double(dif));			

				
				//RODAPE
				
				
				Moeda moeda = moedaService.findById(Long.valueOf(idMoeda) );
				
				if ( somaJuros != 0.0d ){
					mapRet.put("VALOR_EXTENSO",MoedaUtils.formataPorExtenso( new Double(somaJuros), moeda));
				}else{
					mapRet.put("VALOR_EXTENSO"," "+MoedaUtils.formataPorExtenso( new Double(somaJuros), moeda) );
				}
								
				
				Fatura fatura = faturaService.findById( Long.valueOf(idFatura) );
				
				BigDecimal txJurMen = calcularJurosDiarioService.calcularPercentualJuroDiario( fatura );
				
				txJurMen = txJurMen.multiply( new BigDecimal(30) );
				
				mapRet.put("TAXA_JURO_MENSAL", txJurMen );
				
				
				
				lst.add(mapRet);
				
				}
			

				
				
				
				return lst;
			}
			
			});		
		
		

		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(retorno);

		JRReportDataObject jr = createReportDataObject(jrMap, criteria);
		
		Map parametersReport = new HashMap();
		
		/* Tipo do relatório */
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, criteria.get("tpFormatoRelatorio"));
		

		jr.setParameters(parametersReport);

		return jr;

		
	}

	
	
	private SqlTemplate mountSql(TypedFlatMap parametros ){
		
		SqlTemplate sql = createSqlTemplate();
	
		
		//montando a projection
		
		//FILIAL
		sql.addProjection("filFat.SG_FILIAL	|| ' - ' || pesCob.NM_FANTASIA","FILIAL");
		sql.addProjection("pesFat.NR_IDENTIFICACAO","CNPJ");
		sql.addProjection("filFat.ID_FILIAL","ID_FILIAL");
		sql.addProjection("pesFatInscEst.NR_INSCRICAO_ESTADUAL","IE");

		//CLIENTE
		sql.addProjection("pesCli.NM_PESSOA","CLIENTE");
		sql.addProjection("pesCli.NR_IDENTIFICACAO","IDENT_CLIENTE");
		sql.addProjection("pesCli.TP_IDENTIFICACAO","TP_IDENTIFICACAO");
		sql.addProjection("pesCliInscEst.NR_INSCRICAO_ESTADUAL","IE_CLIENTE");
		sql.addProjection("cliente.ID_CLIENTE","ID_CLIENTE");
		
		//UPDATE
		sql.addProjection("notDebNac.TP_SITUACAO_NOTA_DEBITO_NAC","TPSITUACAO");
		sql.addProjection("notDebNac.ID_NOTA_DEBITO_NACIONAL","ID_NOT_DEB_NAC");
		
		//VALIDAÇÕES
		sql.addProjection("notDebNac.TP_SITUACAO_NOTA_DEBITO_NAC","TP_SITUACAO_NOTA_DEBITO_NAC");
		sql.addProjection("notDebNac.TP_SITUACAO_CANCELAMENTO","TP_SITUACAO_CANCELAMENTO");
		sql.addProjection("notDebNac.TP_SITUACAO_DESCONTO","TP_SITUACAO_DESCONTO");
		
		
		//DETALHE
		sql.addProjection("fatura.NR_FATURA","NR_FATURA");
		sql.addProjection("filFatura.SG_FILIAL","SG_FILIAL_FATURA");
		sql.addProjection("fatura.VL_TOTAL","VALOR_ORIGINAL");
		sql.addProjection("fatura.DT_VENCIMENTO","VENCIMENTO");
		sql.addProjection("NVL(fatura.DT_LIQUIDACAO,red.DT_RECEBIMENTO)","DEPOSITO");
		sql.addProjection("itNotDebNac.VL_JURO_RECEBER","VALOR_JURO");
		
		//RODAPE
		sql.addProjection("notDebNac.OB_NOTA_DEBITO_NAC","OBSERVACAO");
		sql.addProjection("fatura.ID_MOEDA","ID_MOEDA");
		sql.addProjection("fatura.ID_FATURA","ID_FATURA");
		
		//CABECALHO
		
		sql.addProjection("notDebNac.NR_NOTA_DEBITO_NAC","NUMERO");
		
		
		
		//from 
		sql.addFrom(" DOCTO_SERVICO ds " +
				" INNER JOIN NOTA_DEBITO_NACIONAL 		notDebNac		ON ds.ID_DOCTO_SERVICO = notDebNac.ID_NOTA_DEBITO_NACIONAL	" +
				" INNER JOIN FILIAL 					filFat 			ON filFat.ID_FILIAL = ds.ID_FILIAL_ORIGEM " +
				" INNER JOIN PESSOA 					pesFat		  	ON filFat.ID_FILIAL = pesFat.ID_PESSOA" +
				" LEFT OUTER JOIN  INSCRICAO_ESTADUAL 		pesFatInscEst	ON pesFatInscEst.ID_PESSOA = pesFat.ID_PESSOA" +
			
				" INNER JOIN DEVEDOR_DOC_SERV_FAT  	 	devDocServFat	ON devDocServFat.ID_DOCTO_SERVICO = ds.ID_DOCTO_SERVICO " +
				" INNER JOIN FILIAL  					filCob     		ON devDocServFat.ID_FILIAL = filCob.ID_FILIAL" +
				" INNER JOIN PESSOA 					pesCob		  	ON filCob.ID_FILIAL = pesCob.ID_PESSOA" +
				" INNER JOIN CLIENTE			  		cliente   		ON notDebNac.ID_CLIENTE = cliente.ID_CLIENTE "+
				" INNER JOIN PESSOA			  			pesCli   		ON pesCli.ID_PESSOA = cliente.ID_CLIENTE "+
				" LEFT OUTER JOIN INSCRICAO_ESTADUAL 		pesCliInscEst	ON pesCliInscEst.ID_PESSOA = pesCli.ID_PESSOA" +
				
				" INNER JOIN ITEM_NOTA_DEBITO_NACIONAL	itNotDebNac		ON notDebNac.ID_NOTA_DEBITO_NACIONAL = itNotDebNac.ID_NOTA_DEBITO_NACIONAL "+
				" INNER JOIN FATURA						fatura			ON itNotDebNac.ID_FATURA = fatura.ID_FATURA "+
				" INNER JOIN FILIAL  					filFatura    	ON fatura.ID_FILIAL = filFatura.ID_FILIAL " +
				" LEFT OUTER JOIN REDECO 				red 			ON red.ID_REDECO = fatura.ID_REDECO");
		
		//os criteria
		
		sql.addCustomCriteria("notDebNac.TP_SITUACAO_NOTA_DEBITO_NAC != 'C'");
		
		//filial de cobranca
		sql.addCriteria("devDocServFat.id_filial","=",parametros.getLong("filial.idFilial"));
		
		//id da nota de débito
		sql.addCriteria("notDebNac.ID_NOTA_DEBITO_NACIONAL","=",parametros.getLong("idDoctoServico"));

		//numero da nota
		//ver regra 2.2 da ET
		if (StringUtils.isNotBlank(parametros.getString("numeroNota"))){
			sql.addCriteria("notDebNac.NR_NOTA_DEBITO_NAC","=",parametros.getLong("numeroNota"));
		}else{
			sql.addCriteria("notDebNac.TP_SITUACAO_NOTA_DEBITO_NAC","=","D");
			sql.addCustomCriteria( " (notDebNac.TP_SITUACAO_DESCONTO = 'A' OR notDebNac.TP_SITUACAO_DESCONTO is null) " );
		}
		
		
		//para buscar a inscricao estadual FILIAL
		sql.addCustomCriteria(" ( pesFatInscEst.BL_INDICADOR_PADRAO = 'S' OR pesFatInscEst.BL_INDICADOR_PADRAO is null ) ");
		sql.addCustomCriteria(" ( pesFatInscEst.TP_SITUACAO = 'A' OR pesFatInscEst.TP_SITUACAO is null ) ");

		
		
		//para buscar a inscricao estadual CLIENTE
		sql.addCustomCriteria(" ( pesCliInscEst.BL_INDICADOR_PADRAO = 'S' OR pesCliInscEst.BL_INDICADOR_PADRAO is null ) ");
		sql.addCustomCriteria(" ( pesCliInscEst.TP_SITUACAO = 'A' OR pesCliInscEst.TP_SITUACAO is null ) ");
		
		//order by
		sql.addOrderBy("filFat.SG_FILIAL, notDebNac.NR_NOTA_DEBITO_NAC, filFatura.SG_FILIAL, fatura.NR_FATURA ");
		
		
		
		
	
	    return sql;
	}
	
	
}
