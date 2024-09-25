package com.mercurio.lms.contasreceber.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.contasreceber.model.ReciboDesconto;
import com.mercurio.lms.contasreceber.model.service.ReciboDescontoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.MoedaUtils;

/**
 * @author Hector Julian Esnaola Junior
 *
 * @spring.bean id="lms.contasreceber.emitirRecibosDescontosService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirRecibosDescontosFretes.jasper"
 */
public class EmitirRecibosDescontosService extends ReportServiceSupport {
	
	private MoedaService moedaService;
	public void setMoedaService(MoedaService moedaService){
		this.moedaService = moedaService;
	}
	
	private ReciboDescontoService reciboDescontoService;
	public void setReciboDescontoService(ReciboDescontoService reciboDescontoService){
		this.reciboDescontoService = reciboDescontoService; 
	}
	
	private DomainValueService domainValueService;
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public JRReportDataObject execute(Map parameters) throws Exception {
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		/** Instância a classe SqlTemplate, que retorna o sql para geração do relatório */
		SqlTemplate sql = getSqlTemplate(tfm);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		/** Adiciona os parâmetros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		
		/** Caso o nrRecibo não tenha sido informado, será feito update no reciboDesconto */
		if(tfm.getLong("numeroRecibo") == null){
			parametersReport.put("updateRecibo", "S");
		}else{
			parametersReport.put("updateRecibo", "N");
		}
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		
		/** Adiciona o tipo de relatório no Map */
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		
		jr.setParameters(parametersReport);
		return jr;
	}

	/**
	 * Configura variáveis do relatório, para receberem valores não abreviados do domínio 
	 * Ex: situação = I  -  vai ser configurado, e exibido no relatório como Inativo
	 */
	public void configReportDomains(ReportDomainConfig config) {		
		config.configDomainField("TIPO","DM_TIPO_DOCUMENTO_SERVICO");
		super.configReportDomains(config);
	}

	/**
	 * Monta o sql principal
	 * @param parameters
	 * @return SqlTemplate
	 */
	public SqlTemplate getSqlTemplate(Map parameters){
		
		TypedFlatMap tfm = (TypedFlatMap)parameters;
		
		SqlTemplate sql = this.createSqlTemplate();
		
		sql.addProjection("distinct rd.id_recibo_desconto as RECIBO_DESCONTO");
		sql.addProjection("filrec.sg_filial, rd.nr_recibo_desconto");
		
		sql.addFrom(this.genericFrom());
		
		sql.addCriteria("rd.id_redeco", "=", tfm.getLong("redeco.idRedeco"));
		
		sql.addCriteria("rd.id_filial", "=", tfm.getLong("filial.idFilial"));
		
		Long nrReciboDesconto = tfm.getLong("numeroRecibo");
		if(nrReciboDesconto != null){
			sql.addCriteria("rd.nr_recibo_desconto", "=", nrReciboDesconto);
		}else{
			if (tfm.getLong("redeco.idRedeco") == null){
				sql.addCriteria("rd.tp_situacao_recibo_desconto", "=", "A");
			}
			sql.addCustomCriteria("( rd.tp_situacao_aprovacao = 'A' or rd.tp_situacao_aprovacao is null )");
		}
		
		sql.addOrderBy("filrec.sg_filial, rd.nr_recibo_desconto");
		
		return sql;
	}
	
	/**
	 * Monta o sql principal
	 * @param tfm
	 * @return sql
	 * @throws Exception
	 */		
	public JRDataSource executeSubRelatorioRecibosDescontos(Object[] parameters){
		
		/** Resgata o id do ReciboDesconto que é passado por parâmetro do relatório pai */
        Long idReciboDesconto = (Long) parameters[0];
        
        /** Resgata o número do reciboDesconto que é passado por parâmetro do relatório pai */ 
        String nrReciboDesconto = null;
        nrReciboDesconto = (String) parameters[1];
		
		SqlTemplate sql = this.createSqlTemplate();
		
		/** HEADER */
		sql.addProjection("rd.id_recibo_desconto", "ID_RECIBO_DESCONTO");
		sql.addProjection("(filrec.sg_filial || ' - ' || rd.nr_recibo_desconto)", "NUMERO");
		sql.addProjection("rd.dt_emissao", "EMISSAO");
		sql.addProjection("(filcli.sg_filial || ' - ' || pesfilcli.nm_fantasia)", "FILIAL_COBRANCA");
		sql.addProjection("pcli.nm_pessoa", "CLIENTE");
		sql.addProjection("pcli.id_pessoa", "ID_PESSOA_CLIENTE");
		sql.addProjection("pcli.nr_identificacao", "NR_IDENTIFICACAO");
		sql.addProjection("pcli.tp_identificacao", "TP_IDENTIFICACAO");
		sql.addProjection("pfilrec.nm_pessoa", "PES_REC");
		sql.addProjection("pfilrec.nr_identificacao", "PES_REC_NR_IDENTIFICACAO");
		sql.addProjection("pfilrec.tp_identificacao", "PES_REC_TP_IDENTIFICACAO");
		sql.addProjection("m.nm_municipio", "MUNICIPIO");
		sql.addProjection("uf.sg_unidade_federativa", "UF");
		
		/** DETAIL */
		sql.addProjection("nvl(ds.tp_documento_servico,'')", "TIPO");
		
		sql.addProjection("fildocserv.sg_filial", "fildocservSgFilial");
		sql.addProjection("ds.nr_docto_servico", "DOCUMENTO");
		
		sql.addProjection("ddsf.vl_devido", "VALOR");
		sql.addProjection("ds.vl_base_calc_imposto", "BASE_CALCULO");
		sql.addProjection("ds.vl_imposto", "ICMS");
		sql.addProjection("d.vl_desconto", "DESCONTO");
		sql.addProjection("(((d.vl_desconto / ddsf.vl_devido) * 100))", "PORCENTAGEM_DESCONTO");
		
		/** FOOTER */
		sql.addProjection("rd.ob_recibo_desconto", "OBSERVACAO");
		sql.addProjection("sysdate", "DATA_ATUAL");
		sql.addProjection("ds.id_moeda", "ID_MOEDA");
		
		//Novos campos de retorno, usados para os dados do endereco do cliente
		sql.addProjection("epc.ds_endereco","DS_ENDERECO_CLI");
		sql.addProjection("epc.nr_endereco","NR_ENDERECO_CLI");
		sql.addProjection("epc.ds_complemento","DS_COMPLEMENTO_END_CLI");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("tlc.ds_tipo_logradouro_i"),"TP_LOGRADOURO_END_CLI");
		sql.addProjection("mc.nm_municipio","CIDADE_END_CLI");
		sql.addProjection("ufc.SG_UNIDADE_FEDERATIVA","UF_END_CLI");
		sql.addProjection("epc.nr_cep","CEP_END_CLI");
		
		sql.addProjection("( " +
								" SELECT   tec.nr_ddd " +
								" FROM     telefone_endereco tec " +
								" WHERE    tec.id_telefone_endereco = " +
										" (SELECT MIN(te.id_telefone_endereco) " +
										"  FROM telefone_endereco te " +
										"  WHERE te.id_endereco_pessoa = epc.id_endereco_pessoa" +
										" ) " +
						"  ) as DDD_END_CLI ");
		
		sql.addProjection("( " +
								" SELECT   tec.nr_ddi " +
								" FROM     telefone_endereco tec " +
								" WHERE    tec.id_telefone_endereco = " +
										" (SELECT MIN(te.id_telefone_endereco) " +
										"  FROM telefone_endereco te " +
										"  WHERE te.id_endereco_pessoa = epc.id_endereco_pessoa" +
										" ) " +
						"  ) as DDI_END_CLI ");
		
		sql.addProjection("( " +
								" SELECT   tec.nr_telefone " +
								" FROM     telefone_endereco tec " +
								" WHERE    tec.id_telefone_endereco = " +
										" (SELECT MIN(te.id_telefone_endereco) " +
										"  FROM telefone_endereco te " +
										"  WHERE te.id_endereco_pessoa = epc.id_endereco_pessoa" +
										" ) " +
						"  ) as NR_TELEFONE_END_CLI ");
		
		sql.addProjection("md.cd_motivo_desconto","MOTIVO");
		sql.addFrom(this.genericFrom());
		
		sql.addCriteria("rd.id_recibo_desconto", "=", idReciboDesconto);
		
		long hash = System.currentTimeMillis();
		sql.addCustomCriteria(hash + "=" + hash);
		
		/**
		 * Troca o TP_SITUACAO_RECIBO_DESCONTO de 'A' para 'E' caso o número do recibo tenha sido informado 
		 */
		if(nrReciboDesconto != null && nrReciboDesconto.equals("S")){
			ReciboDesconto rec = reciboDescontoService.findById(idReciboDesconto);
			rec.setTpSituacaoReciboDesconto(domainValueService.findDomainValueByValue("DM_STATUS_RECIBO_DESCONTO", "E"));
			reciboDescontoService.store(rec);		
		}
		
		sql.addOrderBy("filrec.sg_filial, rd.nr_recibo_desconto, TIPO, fildocservSgFilial, DOCUMENTO");
		
		/** Itera o ResultSet para montar o map para o relatório */
		List rel = this.iteratorResultSet(sql);
		
		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(rel);

		return jrMap;
	}
	
	private List iteratorResultSet(SqlTemplate sql){
		
		List retorno = (List)getJdbcTemplate().query(sql.getSql(), sql.getCriteria(), new ResultSetExtractor() {
			
			Double somaDesconto = new Double(0);
			Long idMoeda = null;
			List lst = new ArrayList();
			
			public Object extractData(ResultSet rs) throws SQLException {
				Map map = null;
				
				while(rs.next()){
					
					map = new HashMap();					
					
					/** HEADER */
					map.put("ID_RECIBO_DESCONTO", Long.valueOf(rs.getLong("ID_RECIBO_DESCONTO")));
					map.put("NUMERO", rs.getString("NUMERO"));
					map.put("EMISSAO", rs.getDate("EMISSAO"));
					map.put("FILIAL_COBRANCA", rs.getString("FILIAL_COBRANCA"));
					
					map.put("CLIENTE", rs.getString("CLIENTE"));
					
					String tpIdentificacao = rs.getString("TP_IDENTIFICACAO");
					String nrIdentificacao = rs.getString("NR_IDENTIFICACAO");
					
					map.put("CGC", FormatUtils.formatIdentificacao(tpIdentificacao, nrIdentificacao));
					
					tpIdentificacao = rs.getString("PES_REC_TP_IDENTIFICACAO");
					nrIdentificacao = rs.getString("PES_REC_NR_IDENTIFICACAO");
					
					map.put("IDENTIFICACAO", " " + rs.getString("PES_REC") + ", CNPJ " + FormatUtils.formatIdentificacao(tpIdentificacao, nrIdentificacao));
					 
					map.put("CIDADE_EMPRESA", rs.getString("MUNICIPIO"));
					map.put("UF_EMPRESA", rs.getString("UF"));
					
					if(StringUtils.isNotBlank(rs.getString("DS_ENDERECO_CLI"))){
						map.put("ENDERECO_CLIENTE", FormatUtils.montaEnderecoCompleto(rs.getString("TP_LOGRADOURO_END_CLI"), rs.getString("DS_ENDERECO_CLI"), rs.getString("NR_ENDERECO_CLI"), rs.getString("DS_COMPLEMENTO_END_CLI")));
						map.put("CIDADE_CLIENTE", rs.getString("CIDADE_END_CLI"));
						map.put("UF_CLIENTE", rs.getString("UF_END_CLI"));
						map.put("CEP_CLIENTE", rs.getString("CEP_END_CLI"));
						
						if (StringUtils.isNotBlank("NR_TELEFONE_END_CLI")) {
							
							String ddd = rs.getString("DDD_END_CLI");
							String ddi = rs.getString("DDI_END_CLI");
							String numero = rs.getString("NR_TELEFONE_END_CLI");
							
							map.put("FONE_CLIENTE", FormatUtils.formatTelefone(numero, ddd, ddi));
						}else{
							map.put("FONE_CLIENTE", "");
						}
						
					}else{
						map.put("ENDERECO_CLIENTE", "");
						map.put("CIDADE_CLIENTE", "");
						map.put("UF_CLIENTE", "");
						map.put("CEP_CLIENTE", "");
						map.put("FONE_CLIENTE", "");
					}
					
					/** DETAIL */				
					String tipo = null;
					String filialDocumento = null;
					Double valor = 0.0d;
					Double baseCalculo = 0.0d;
					Double icms = 0.0d;
					Double desconto = 0.0d;
					Double porcentagemDesconto = 0.0d;
					
					if( rs.getString("TIPO") != null && !rs.getString("TIPO").equalsIgnoreCase("") ){
						tipo = domainValueService.findDomainValueByValue("DM_TIPO_DOCUMENTO_RECIBO", rs.getString("TIPO")).getDescription().getValue();
						if( (rs.getString("fildocservSgFilial") != null) && (rs.getString("DOCUMENTO")!= null) ){
							filialDocumento = rs.getString("fildocservSgFilial") +" " + FormatUtils.formataNrDocumento(rs.getString("DOCUMENTO"), rs.getString("TIPO"));
						}
					}
					
					map.put("TIPO_DOCUMENTO", tipo);
					map.put("FILIAL_DOCUMENTO",filialDocumento);
					
					if( rs.getString("VALOR") != null ){
						valor = new Double(rs.getDouble("VALOR"));
					}
					
					if( rs.getString("BASE_CALCULO") != null ){
						baseCalculo = new Double(rs.getDouble("BASE_CALCULO"));
					}
					
					if( rs.getString("ICMS") != null ){
						icms = new Double(rs.getDouble("ICMS"));
					}
					
					map.put("VALOR", valor);
					map.put("BASE_CALC", baseCalculo);
					map.put("ICMS", icms);
					
					if( rs.getString("DESCONTO") != null ){
						desconto = new Double(rs.getDouble("DESCONTO"));
					}
					map.put("DESCONTO", desconto);
					
					if( rs.getString("ID_MOEDA") != null ){
						idMoeda = Long.valueOf(rs.getString("ID_MOEDA"));
					}
					
					somaDesconto = new Double(somaDesconto.doubleValue() + desconto.doubleValue());
					map.put("DESCONTO_EXTENSO", "");
					
					if( rs.getString("PORCENTAGEM_DESCONTO") != null ){
						porcentagemDesconto = new Double(rs.getDouble("PORCENTAGEM_DESCONTO"));
					}
					
					map.put("PERC_DESC", porcentagemDesconto);
					map.put("MOTIVO_DESCONTO", rs.getString("OBSERVACAO"));
					map.put("MOTIVO", rs.getString("MOTIVO"));
					lst.add(map);
					
				}
				 
				
				Moeda moeda = null;
				String porExtenso = null;			
				
				if( idMoeda != null ){
					moeda = moedaService.findById(idMoeda);
					porExtenso = MoedaUtils.formataPorExtenso(somaDesconto, moeda);
				}
				
				map.put("DESCONTO_EXTENSO", porExtenso);
				
				return lst;
			}
			
		});
		
		return retorno;
	}
	
	/**
	 * Monta o from genérico
	 * @return String from
	 */
	private String genericFrom(){
		
		String from =   "recibo_desconto rd " +
						"inner join filial filrec on rd.id_filial = filrec.id_filial " +
						"inner join pessoa pfilrec on pfilrec.id_pessoa = filrec.id_filial " +
						"inner join endereco_pessoa ep on ep.id_endereco_pessoa = pfilrec.id_endereco_pessoa " +
						"inner join municipio m on m.id_municipio = ep.id_municipio " +
						"inner join unidade_federativa uf on uf.id_unidade_federativa = m.id_unidade_federativa " +
						"inner join desconto d on d.id_recibo_desconto = rd.id_recibo_desconto " +
						"inner join motivo_desconto md on md.id_motivo_desconto = d.id_motivo_desconto " +
						"inner join cliente c on c.id_cliente = rd.id_cliente " +
						"inner join pessoa pcli on pcli.id_pessoa = c.id_cliente " +
						"inner join filial filcli on filcli.id_filial = c.id_filial_cobranca " +
						"inner join pessoa pesfilcli on pesfilcli.id_pessoa = filcli.id_filial " +
						"inner join devedor_doc_serv_fat ddsf on ddsf.id_devedor_doc_serv_fat = d.id_devedor_doc_serv_fat " +
						"inner join filial fildoc on  fildoc.id_filial = ddsf.id_filial " +
						"inner join docto_servico ds on ds.id_docto_servico = ddsf.id_docto_servico " +
						"inner join filial fildocserv on fildocserv.id_filial = ds.id_filial_origem " +
						
						"inner join endereco_pessoa epc on epc.id_endereco_pessoa = F_BUSCA_ENDERECO_PESSOA(c.id_cliente, 'COB', sysdate) " +
						"inner join municipio mc on mc.id_municipio = epc.id_municipio " +
						"inner join unidade_federativa ufc on ufc.id_unidade_federativa = mc.id_unidade_federativa " +
						"inner join tipo_logradouro tlc on epc.id_tipo_logradouro = tlc.id_tipo_logradouro ";

		return from;
	}
}
