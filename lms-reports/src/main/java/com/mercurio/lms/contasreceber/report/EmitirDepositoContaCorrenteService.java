package com.mercurio.lms.contasreceber.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.contasreceber.model.ItemDepositoCcorrente;
import com.mercurio.lms.contasreceber.model.service.DepositoCcorrenteService;



/**
* @author Hector Julian Esnaola Junior
*
* @spring.bean id="lms.contasreceber.emitirDepositoContaCorrenteService"
* @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirDepositosContaCorrente.jasper"
*/
public class EmitirDepositoContaCorrenteService extends ReportServiceSupport {
	
	private DepositoCcorrenteService depositoCcorrenteService;
	public void setDepositoCcorrenteService(DepositoCcorrenteService depositoCcorrenteService){
		this.depositoCcorrenteService = depositoCcorrenteService;
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
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		
		/** Seta o parâmetro de tipo de arquivo a ser gerado */
       parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));		
		
		jr.setParameters(parametersReport);
		return jr;
	}
	
	/**
	 * Monta o sql principal
	 * @param tfm
	 * @return sql
	 * @throws Exception
	 */		
	private SqlTemplate getSqlTemplate(TypedFlatMap tfm) throws Exception{
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("dc.id_deposito_ccorrente as PROTOCOLO, " +
						  "p.nr_identificacao as NR_IDENTIFICACAO, " +
						  "p.tp_identificacao as TP_IDENTIFICACAO, " +
						  "p.nm_pessoa as NM_PESSOA, " +
						  "b.nr_banco as BANCO, " + 
						  "ab.nr_agencia_bancaria as AGENCIA, " +
						  "ced.nr_conta_corrente as CONTA_CORRENTE, " +
						  "dc.dt_deposito as DATA_DEPOSITO, " +
						  "dc.vl_deposito as VALOR_DEPOSITO, " +
						  "dc.ob_deposito_ccorrente as OBSERVAÇÃO " );
		
		sql.addFrom("deposito_ccorrente dc " +
					"inner join cliente c " +
					"  on c.id_cliente = dc.id_cliente " +
					"inner join pessoa p " + 
					"  on p.id_pessoa = c.id_cliente " +
					"inner join cedente ced " +
					"  on ced.id_cedente = dc.id_cedente " +
					"inner join agencia_bancaria ab " +
					"  on ab.id_agencia_bancaria = ced.id_agencia_bancaria " +
					"inner join banco b " +
					"  on ab.id_banco = b.id_banco ");
							
		sql.addCriteria("dc.id_deposito_ccorrente","=", tfm.getLong("idDepositoCcorrente"));
		
		// Busca os itemDebitoCCorrente
        List idcs = depositoCcorrenteService.findById(tfm.getLong("idDepositoCcorrente")).getItemDepositoCcorrentes();
       
        // Seta a moeda no filter summary
        if(!idcs.isEmpty()){
        	
        	ItemDepositoCcorrente idc = (ItemDepositoCcorrente)idcs.get(0);
        	
        	Moeda moeda;
        	
        	// Caso o item tenha uma fatura
        	if(idc.getFatura() != null){
        		moeda = idc.getFatura().getMoeda();
        		sql.addFilterSummary("moeda", moeda.getSiglaSimbolo());
        	// Caso o item tenha um devedorDocServFat
        	}else{
        		moeda = idc.getDevedorDocServFat().getDoctoServico().getMoeda();
        		sql.addFilterSummary("moeda", moeda.getSiglaSimbolo());
        	}
        	
        }
		
        // Ordenação
		sql.addOrderBy("dc.id_deposito_ccorrente");
				       
		return sql;

	}
	
	
	public void findFaturas(SqlTemplate sql){
		
		sql.addProjection("'Fatura' as TIPO_DOCUMENTO, " +
						  "fil.sg_filial as SG_FILIAL, " +
						  "f.nr_fatura as DOCUMENTO, " +
						  "f.dt_emissao as DATA_EMISSAO, " +
						  "f.vl_total as VALOR," +
						  "nvl(f.vl_desconto, 0) as VALOR_DESCONTO");
		
		sql.addFrom(" item_deposito_ccorrente idc " +
					  "inner join deposito_ccorrente dc " +
					  "  on dc.id_deposito_ccorrente = idc.id_deposito_ccorrente " +
					  "inner join  fatura f " +
					  "  on f.id_fatura = idc.id_fatura " +
					  "inner join filial fil " +
					  "  on f.id_filial = fil.id_filial ");
		
		
	}
	
	public void findDevedorDocServFat(SqlTemplate sql){
		
		sql.addProjection("	(CASE WHEN ds.tp_documento_servico = 'CTR' " +
				"				THEN 'CTRC'" +
				"			 ELSE ds.tp_documento_servico END) as TIPO_DOCUMENTO, " +
						  "fil.sg_filial as SG_FILIAL, " +
						  "ds.nr_docto_servico as DOCUMENTO, " +
						  "cast(ds.dh_emissao as date) as DATA_EMISSAO, " +
						  "ddsf.vl_devido as VALOR, " +
						  "CASE WHEN (D.TP_SITUACAO_APROVACAO = 'A' OR D.TP_SITUACAO_APROVACAO  IS NULL) THEN nvl(d.vl_desconto, 0) ELSE 0 END as VALOR_DESCONTO ");
		
		sql.addFrom(" item_deposito_ccorrente idc " +
					  "inner join deposito_ccorrente dc " +
					  "  on dc.id_deposito_ccorrente = idc.id_deposito_ccorrente " +
					  "inner join  devedor_doc_serv_fat ddsf " + 
					  "  on ddsf.id_devedor_doc_serv_fat = idc.id_devedor_doc_serv_fat " +
					  "inner join filial fil " +
					  "  on ddsf.id_filial = fil.id_filial " +
					  "inner join docto_servico ds " +
					  "  on ds.id_docto_servico = ddsf.id_docto_servico " +
					  "left join desconto d " +
					  "  on d.id_devedor_doc_serv_fat = ddsf.id_devedor_doc_serv_fat ");
		
		//a ordenacao vai ser por tipo de documento, filial e nr
		sql.addOrderBy("1,2,3"); 
		
		
	}
	
	
	/** Subrelatório - aliquotasIssServico */ 
	public JRDataSource executeSubRelatorioItemDebitoCCorrente(Object[] parameters) throws Exception {
        
		/** Resgata o id do DebitoCCorrente que é passado por parâmetro do relatório pai */
        Long idDebitoCCorrente = Long.valueOf((String)parameters[0]);
        
        SqlTemplate sqlFatura = createSqlTemplate();
        SqlTemplate sqlDocumento = createSqlTemplate();
       		
        findFaturas(sqlFatura);
        
        findDevedorDocServFat(sqlDocumento);
        		
        	
        
        
        sqlFatura.addCustomCriteria("dc.id_deposito_ccorrente=?");
        sqlDocumento.addCustomCriteria("dc.id_deposito_ccorrente=?");

        //usado para que se use um criteria no union
		sqlFatura.addCriteriaValue(idDebitoCCorrente);
		sqlFatura.addCriteriaValue(idDebitoCCorrente);
        
        return executeQuery(sqlFatura.getSql()+" UNION "+ sqlDocumento.getSql(), sqlFatura.getCriteria()  ).getDataSource();
    }
	

}

