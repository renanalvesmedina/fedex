package com.mercurio.lms.sgr.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração do Relatório de Consulta de Solicitações de Sinal
 * Especificação técnica 11.03.01.12
 * @author Cesar Gabbardo
 * 
 * @spring.bean id="lms.sgr.consultarSolicitacoesSinalService"
 * @spring.property name="reportName" value="com/mercurio/lms/sgr/report/consultarSolicitacoesSinal.jasper"
 */
public class ConsultarSolicitacoesSinalService extends ReportServiceSupport {
	private TelefoneEnderecoService telefoneEnderecoService;
	
    /**
     * Método responsável por gerar o relatório. 
     */
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap)parameters;
			
        SqlTemplate sql = mountSql(tfm);
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
              
        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
        
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
        
        jr.setParameters(parametersReport);
        return jr;
	}
	
	/**
	 * Método que monta o select para a consulta.
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
    private SqlTemplate mountSql(TypedFlatMap parameters) throws Exception {    	
    	SqlTemplate sql = createSqlTemplate();
    	    	
    	sql.addProjection("FILIAL.SG_FILIAL", "SG_FILIAL");
    	sql.addProjection("SOLICITACAO_SINAL.NR_SOLICITACAO_SINAL", "NR_SOLICITACAO_SINAL");
    	sql.addProjection("SOLICITACAO_SINAL.DH_GERACAO", "DH_GERACAO");
    	sql.addProjection("SOLICITACAO_SINAL.BL_PERT_PROJ_CAMINHONEIRO", "BL_PERT_PROJ_CAMINHONEIRO");
    	sql.addProjection("OPERADORA_MCT.NM_PESSOA", "NM_PESSOA_OPERADORA_MCT");
    	sql.addProjection("SOLICITACAO_SINAL.NR_RASTREADOR", "NR_RASTREADOR");
    	sql.addProjection("MEIO_TRANSPORTE.NR_FROTA", "NR_FROTA_MEIO_TRANSPORTE");
    	sql.addProjection("MEIO_TRANSPORTE.NR_IDENTIFICADOR", "NR_IDENT_MEIO_TRANSPORTE");
    	sql.addProjection("SEMI_REBOQUE.NR_FROTA", "NR_FROTA_SEMI_REBOQUE");
    	sql.addProjection("SEMI_REBOQUE.NR_IDENTIFICADOR", "NR_IDENT_SEMI_REBOQUE");
    	sql.addProjection("MOTORISTA.ID_PESSOA", "ID_PESSOA_MOTORISTA");
    	sql.addProjection("MOTORISTA.NM_PESSOA", "NM_PESSOA_MOTORISTA");
    	sql.addProjection("MOTORISTA.TP_IDENTIFICACAO", "TP_IDENT_MOTORISTA");
    	sql.addProjection("MOTORISTA.NR_IDENTIFICACAO", "NR_IDENT_MOTORISTA");
    	sql.addProjection("PROPRIETARIO.ID_PESSOA", "ID_PESSOA_PROPRIETARIO");
    	sql.addProjection("PROPRIETARIO.NM_PESSOA", "NM_PESSOA_PROPRIETARIO");
    	sql.addProjection("PROPRIETARIO.TP_IDENTIFICACAO", "TP_IDENT_PROPRIETARIO");
    	sql.addProjection("PROPRIETARIO.NR_IDENTIFICACAO", "NR_IDENT_PROPRIETARIO");
    	sql.addProjection("SOLICITACAO_SINAL.NM_EMPRESA_ANTERIOR", "NM_EMPRESA_ANTERIOR");
    	sql.addProjection("SOLICITACAO_SINAL.NR_TELEFONE_EMPRESA", "NR_TELEFONE_EMPRESA");
    	sql.addProjection("SOLICITACAO_SINAL.NM_RESPONSAVEL_EMPRESA", "NM_RESPONSAVEL_EMPRESA");
        
    	sql.addFrom("SOLICITACAO_SINAL", "SOLICITACAO_SINAL");
    	sql.addFrom("FILIAL", "FILIAL");
    	sql.addFrom("PESSOA", "OPERADORA_MCT");
    	sql.addFrom("MEIO_TRANSPORTE", "MEIO_TRANSPORTE");
    	sql.addFrom("MEIO_TRANSPORTE", "SEMI_REBOQUE");
    	sql.addFrom("PESSOA", "MOTORISTA");
    	sql.addFrom("PESSOA", "PROPRIETARIO");
            	
    	sql.addJoin("SOLICITACAO_SINAL.ID_FILIAL", "FILIAL.ID_FILIAL");
    	sql.addJoin("SOLICITACAO_SINAL.ID_OPERADORA_MCT", "OPERADORA_MCT.ID_PESSOA");
    	sql.addJoin("SOLICITACAO_SINAL.ID_MEIO_TRANSPORTE", "MEIO_TRANSPORTE.ID_MEIO_TRANSPORTE");
    	sql.addJoin("SOLICITACAO_SINAL.ID_SEMI_REBOQUE", "SEMI_REBOQUE.ID_MEIO_TRANSPORTE(+)");
    	sql.addJoin("SOLICITACAO_SINAL.ID_MOTORISTA", "MOTORISTA.ID_PESSOA");
    	sql.addJoin("SOLICITACAO_SINAL.ID_PROPRIETARIO", "PROPRIETARIO.ID_PESSOA");

    	sql.addCriteria("SOLICITACAO_SINAL.ID_SOLICITACAO_SINAL", "=", parameters.getLong("idSolicitacaoSinal"));
     
        return sql;
    }


    /**
     * Configura Dominios
     */
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("BL_PERT_PROJ_CAMINHONEIRO", "DM_SIM_NAO");
		config.configDomainField("TP_IDENTIFICACAO_MOTORISTA", "DM_TIPO_IDENTIFICACAO_PESSOA");
		config.configDomainField("TP_IDENTIFICACAO_PROPRIETARIO", "DM_TIPO_IDENTIFICACAO_PESSOA");
	}    
    
	
	/**
	 * Pega o map de TelefoneEndereco com nrTelefone e nrDdd e concatena retornando uma string
	 * @param idPessoa
	 * @param tpTelefone
	 * @return
	 */
	public String findTelefoneEnderecoByPessoaTelefoneEnderecoPessoa(Long idPessoa, String tpTelefone) {
		Map mapTelefoneEndereco = this.getTelefoneEnderecoService().findTelefoneEnderecoByPessoaTelefoneEnderecoPessoa(idPessoa, tpTelefone, null);
		String telefone = "";
		if(mapTelefoneEndereco.get("nrDdd") != null) {
			telefone += mapTelefoneEndereco.get("nrDdd") + " ";
		}
		if(mapTelefoneEndereco.get("nrTelefone") != null) {
			telefone += mapTelefoneEndereco.get("nrTelefone");
		}
		return telefone;
	}
	

	public TelefoneEnderecoService getTelefoneEnderecoService() {
		return telefoneEnderecoService;
	}
	public void setTelefoneEnderecoService(
			TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}	

}
