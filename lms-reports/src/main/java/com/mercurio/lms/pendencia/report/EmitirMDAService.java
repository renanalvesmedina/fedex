package com.mercurio.lms.pendencia.report;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração do Relatório de MDA
 * Especificação técnica 17.04.01.04
 * @author Cesar Gabbardo
 * 
 * @spring.bean id="lms.pendencia.emitirMDAService"
 * @spring.property name="reportName" value="com/mercurio/lms/pendencia/report/emitirMDA.jasper"
 */
public class EmitirMDAService extends ReportServiceSupport {
	private Long idDoctoServico;

    /**
     * Método responsável por gerar o relatório na quantidade informada. 
     */
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap)parameters;
		
		this.setIdDoctoServico(tfm.getLong("mda.idDoctoServico"));
		      
		SqlTemplate sql = mountSql();
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		
        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
                
        parametersReport.put("sgFilialUsuarioEmissor", tfm.getString("sgFilial"));
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
        
        jr.setParameters(parametersReport);
        return jr;	
	}
	
	
	/**
	 * Método que gera o relatório de MDA. 
	 * @return
	 * @throws Exception
	 */
    public SqlTemplate mountSql() throws Exception { 
    	SqlTemplate sql = createSqlTemplate();
    	
    	/**
    	 * SELECT
    	 */
    	// Docto_Servico, MDA e Filial
    	sql.addProjection("DOCTO_SERVICO.ID_DOCTO_SERVICO", "ID_DOCTO_SERVICO");
    	sql.addProjection("FILIAL_ORIGEM.SG_FILIAL", "SG_FILIAL_ORIGEM");
    	sql.addProjection("DOCTO_SERVICO.NR_DOCTO_SERVICO", "NR_DOCTO_SERVICO");
    	sql.addProjection("DOCTO_SERVICO.DH_EMISSAO", "DH_EMISSAO");
    	sql.addProjection("FILIAL_ORIGEM_PESSOA.TP_IDENTIFICACAO", "TP_IDENT_FILIAL_ORIGEM");
    	sql.addProjection("FILIAL_ORIGEM_PESSOA.NR_IDENTIFICACAO", "NR_IDENT_FILIAL_ORIGEM");
    	sql.addProjection("FILIAL_ORIGEM_PESSOA.NM_FANTASIA", "NM_FILIAL_ORIGEM");
    	sql.addProjection("FILIAL_DESTINO.SG_FILIAL", "SG_FILIAL_DESTINO");
    	sql.addProjection("FILIAL_DESTINO_PESSOA.TP_IDENTIFICACAO", "TP_IDENT_FILIAL_DESTINO");
    	sql.addProjection("FILIAL_DESTINO_PESSOA.NR_IDENTIFICACAO", "NR_IDENT_FILIAL_DESTINO");
    	sql.addProjection("FILIAL_DESTINO_PESSOA.NM_FANTASIA", "NM_FILIAL_DESTINO");        
    	sql.addProjection("MDA.OB_MDA", "OB_MDA");        
    	// Cliente Remetente, Destinatario e Consignatario
    	sql.addProjection("CLIENTE_REMETENTE.TP_IDENTIFICACAO", "TP_IDENTIFICACAO_REMETENTE");
    	sql.addProjection("CLIENTE_REMETENTE.NR_IDENTIFICACAO", "NR_IDENTIFICACAO_REMETENTE");
    	sql.addProjection("CLIENTE_REMETENTE.NM_PESSOA", "NM_REMETENTE");
    	sql.addProjection("CLIENTE_DESTINATARIO.TP_IDENTIFICACAO", "TP_IDENTIFICACAO_DESTINATARIO");
    	sql.addProjection("CLIENTE_DESTINATARIO.NR_IDENTIFICACAO", "NR_IDENTIFICACAO_DESTINATARIO");
    	sql.addProjection("CLIENTE_DESTINATARIO.NM_PESSOA", "NM_DESTINATARIO");
    	sql.addProjection("CLIENTE_CONSIGNATARIO.TP_IDENTIFICACAO", "TP_IDENTIFICACAO_CONSIGNATARIO");
    	sql.addProjection("CLIENTE_CONSIGNATARIO.NR_IDENTIFICACAO", "NR_IDENTIFICACAO_CONSIGNATARIO");
    	sql.addProjection("CLIENTE_CONSIGNATARIO.NM_PESSOA", "NM_CONSIGNATARIO");    	
    	sql.addProjection("USUARIO_DESTINO.NM_USUARIO", "NM_USUARIO_DESTINATARIO");
    	sql.addProjection("USUARIO_DESTINO.NR_MATRICULA", "NR_MATRICULA_USUARIO_DEST");
    	// Endereço Remetente
    	sql.addProjection(PropertyVarcharI18nProjection.createProjection("TIPO_LOGRADOURO_REMETENTE.DS_TIPO_LOGRADOURO_I"), "DS_LOGRADOURO_REMETENTE");
    	sql.addProjection("ENDERECO_PESSOA_REMETENTE.DS_ENDERECO", "DS_ENDERECO_REMETENTE");
    	sql.addProjection("ENDERECO_PESSOA_REMETENTE.NR_ENDERECO", "NR_ENDERECO_REMETENTE");
    	sql.addProjection("ENDERECO_PESSOA_REMETENTE.DS_COMPLEMENTO", "DS_COMPLEMENTO_REMETENTE");
    	sql.addProjection("ENDERECO_PESSOA_REMETENTE.DS_BAIRRO", "DS_BAIRRO_REMETENTE");
    	sql.addProjection("ENDERECO_PESSOA_REMETENTE.NR_CEP", "NR_CEP_REMETENTE");
    	sql.addProjection("MUNICIPIO_REMETENTE.NM_MUNICIPIO", "NM_MUNICIPIO_REMETENTE");
    	sql.addProjection("UF_REMETENTE.SG_UNIDADE_FEDERATIVA", "SG_UF_REMETENTE");    	
    	sql.addProjection("PAIS_REMETENTE.SG_PAIS", "SG_PAIS_REMETENTE");
    	// Endereço Destinatario
    	sql.addProjection(PropertyVarcharI18nProjection.createProjection("TIPO_LOGRADOURO_DESTINATARIO.DS_TIPO_LOGRADOURO_I"), "DS_LOGRADOURO_DESTINATARIO");
    	sql.addProjection("ENDERECO_PESSOA_DESTINATARIO.DS_ENDERECO", "DS_ENDERECO_DESTINATARIO");
    	sql.addProjection("ENDERECO_PESSOA_DESTINATARIO.NR_ENDERECO", "NR_ENDERECO_DESTINATARIO");
    	sql.addProjection("ENDERECO_PESSOA_DESTINATARIO.DS_COMPLEMENTO", "DS_COMPLEMENTO_DESTINATARIO");
    	sql.addProjection("ENDERECO_PESSOA_DESTINATARIO.DS_BAIRRO", "DS_BAIRRO_DESTINATARIO");
    	sql.addProjection("ENDERECO_PESSOA_DESTINATARIO.NR_CEP", "NR_CEP_DESTINATARIO");
    	sql.addProjection("MUNICIPIO_DESTINATARIO.NM_MUNICIPIO", "NM_MUNICIPIO_DESTINATARIO");
    	sql.addProjection("UF_DESTINATARIO.SG_UNIDADE_FEDERATIVA", "SG_UF_DESTINATARIO");
    	sql.addProjection("PAIS_DESTINATARIO.SG_PAIS", "SG_PAIS_DESTINATARIO");
    	// Endereço Consignatario
    	sql.addProjection(PropertyVarcharI18nProjection.createProjection("TIPO_LOGRADOURO_CONSIGNATARIO.DS_TIPO_LOGRADOURO_I"), "DS_LOGRADOURO_CONSIGNATARIO");
    	sql.addProjection("ENDERECO_PESSOA_CONSIGNATARIO.DS_ENDERECO", "DS_ENDERECO_CONSIGNATARIO");
    	sql.addProjection("ENDERECO_PESSOA_CONSIGNATARIO.NR_ENDERECO", "NR_ENDERECO_CONSIGNATARIO");
    	sql.addProjection("ENDERECO_PESSOA_CONSIGNATARIO.DS_COMPLEMENTO", "DS_COMPLEMENTO_CONSIGNATARIO");
    	sql.addProjection("ENDERECO_PESSOA_CONSIGNATARIO.DS_BAIRRO", "DS_BAIRRO_CONSIGNATARIO");
    	sql.addProjection("ENDERECO_PESSOA_CONSIGNATARIO.NR_CEP", "NR_CEP_CONSIGNATARIO");
    	sql.addProjection("MUNICIPIO_CONSIGNATARIO.NM_MUNICIPIO", "NM_MUNICIPIO_CONSIGNATARIO");
    	sql.addProjection("UF_CONSIGNATARIO.SG_UNIDADE_FEDERATIVA", "SG_UF_CONSIGNATARIO");
    	sql.addProjection("PAIS_CONSIGNATARIO.SG_PAIS", "SG_PAIS_CONSIGNATARIO");
        
    	/**
    	 * FROM
    	 */ 
    	// Docto_Servico, MDA e Filial
    	sql.addFrom("MDA", "MDA");
    	sql.addFrom("USUARIO", "USUARIO_DESTINO"); 
    	sql.addFrom("DOCTO_SERVICO", "DOCTO_SERVICO");
    	sql.addFrom("FILIAL", "FILIAL_ORIGEM");
    	sql.addFrom("PESSOA", "FILIAL_ORIGEM_PESSOA");
    	sql.addFrom("FILIAL", "FILIAL_DESTINO");
    	sql.addFrom("PESSOA", "FILIAL_DESTINO_PESSOA");        
    	// Cliente Remetente, Destinatario e Consignatario
    	sql.addFrom("PESSOA", "CLIENTE_REMETENTE");
    	sql.addFrom("PESSOA", "CLIENTE_DESTINATARIO");
    	sql.addFrom("PESSOA", "CLIENTE_CONSIGNATARIO");
    	// Endereço Remetente
    	sql.addFrom("ENDERECO_PESSOA", "ENDERECO_PESSOA_REMETENTE");
    	sql.addFrom("TIPO_LOGRADOURO", "TIPO_LOGRADOURO_REMETENTE");
    	sql.addFrom("MUNICIPIO", "MUNICIPIO_REMETENTE");
    	sql.addFrom("UNIDADE_FEDERATIVA", "UF_REMETENTE");        
    	sql.addFrom("PAIS", "PAIS_REMETENTE");
    	// Endereço Destinatario
    	sql.addFrom("ENDERECO_PESSOA", "ENDERECO_PESSOA_DESTINATARIO");
    	sql.addFrom("TIPO_LOGRADOURO", "TIPO_LOGRADOURO_DESTINATARIO");
    	sql.addFrom("MUNICIPIO", "MUNICIPIO_DESTINATARIO");
    	sql.addFrom("UNIDADE_FEDERATIVA", "UF_DESTINATARIO");
    	sql.addFrom("PAIS", "PAIS_DESTINATARIO");
    	// Endereço Consignatario
    	sql.addFrom("ENDERECO_PESSOA", "ENDERECO_PESSOA_CONSIGNATARIO");
    	sql.addFrom("TIPO_LOGRADOURO", "TIPO_LOGRADOURO_CONSIGNATARIO");
    	sql.addFrom("MUNICIPIO", "MUNICIPIO_CONSIGNATARIO");
    	sql.addFrom("UNIDADE_FEDERATIVA", "UF_CONSIGNATARIO");
    	sql.addFrom("PAIS", "PAIS_CONSIGNATARIO");
        
        /**
         * JOIN
         */
    	// Docto_Servico, MDA e Filial
    	sql.addJoin("MDA.ID_MDA", "DOCTO_SERVICO.ID_DOCTO_SERVICO");
    	sql.addJoin("DOCTO_SERVICO.ID_FILIAL_ORIGEM", "FILIAL_ORIGEM.ID_FILIAL");
    	sql.addJoin("FILIAL_ORIGEM.ID_FILIAL", "FILIAL_ORIGEM_PESSOA.ID_PESSOA");
    	sql.addJoin("DOCTO_SERVICO.ID_FILIAL_DESTINO", "FILIAL_DESTINO.ID_FILIAL(+)");
    	sql.addJoin("FILIAL_DESTINO.ID_FILIAL", "FILIAL_DESTINO_PESSOA.ID_PESSOA(+)");
    	// Cliente Remetente, Destinatario e Consignatario
    	sql.addJoin("DOCTO_SERVICO.ID_CLIENTE_REMETENTE", "CLIENTE_REMETENTE.ID_PESSOA(+)");
    	sql.addJoin("DOCTO_SERVICO.ID_CLIENTE_DESTINATARIO", "CLIENTE_DESTINATARIO.ID_PESSOA(+)");
    	sql.addJoin("DOCTO_SERVICO.ID_CLIENTE_CONSIGNATARIO", "CLIENTE_CONSIGNATARIO.ID_PESSOA(+)");
    	// Endereço Remetente
    	sql.addJoin("MDA.ID_ENDERECO_REMETENTE", "ENDERECO_PESSOA_REMETENTE.ID_ENDERECO_PESSOA");
    	sql.addJoin("ENDERECO_PESSOA_REMETENTE.ID_TIPO_LOGRADOURO", "TIPO_LOGRADOURO_REMETENTE.ID_TIPO_LOGRADOURO");
    	sql.addJoin("ENDERECO_PESSOA_REMETENTE.ID_MUNICIPIO", "MUNICIPIO_REMETENTE.ID_MUNICIPIO");
    	sql.addJoin("MUNICIPIO_REMETENTE.ID_UNIDADE_FEDERATIVA", "UF_REMETENTE.ID_UNIDADE_FEDERATIVA");
    	sql.addJoin("UF_REMETENTE.ID_PAIS", "PAIS_REMETENTE.ID_PAIS");
    	// Endereço Destinatario
    	sql.addJoin("MDA.ID_ENDERECO_DESTINATARIO", "ENDERECO_PESSOA_DESTINATARIO.ID_ENDERECO_PESSOA");
    	sql.addJoin("ENDERECO_PESSOA_DESTINATARIO.ID_TIPO_LOGRADOURO", "TIPO_LOGRADOURO_DESTINATARIO.ID_TIPO_LOGRADOURO");
    	sql.addJoin("ENDERECO_PESSOA_DESTINATARIO.ID_MUNICIPIO", "MUNICIPIO_DESTINATARIO.ID_MUNICIPIO");
    	sql.addJoin("MUNICIPIO_DESTINATARIO.ID_UNIDADE_FEDERATIVA", "UF_DESTINATARIO.ID_UNIDADE_FEDERATIVA");
    	sql.addJoin("UF_DESTINATARIO.ID_PAIS", "PAIS_DESTINATARIO.ID_PAIS");
    	// Endereço Consignatario    
    	sql.addJoin("MDA.ID_ENDERECO_CONSIGNATARIO", "ENDERECO_PESSOA_CONSIGNATARIO.ID_ENDERECO_PESSOA(+)");
    	sql.addJoin("ENDERECO_PESSOA_CONSIGNATARIO.ID_TIPO_LOGRADOURO", "TIPO_LOGRADOURO_CONSIGNATARIO.ID_TIPO_LOGRADOURO(+)");
    	sql.addJoin("ENDERECO_PESSOA_CONSIGNATARIO.ID_MUNICIPIO", "MUNICIPIO_CONSIGNATARIO.ID_MUNICIPIO(+)");
    	sql.addJoin("MUNICIPIO_CONSIGNATARIO.ID_UNIDADE_FEDERATIVA", "UF_CONSIGNATARIO.ID_UNIDADE_FEDERATIVA(+)");
    	sql.addJoin("UF_CONSIGNATARIO.ID_PAIS", "PAIS_CONSIGNATARIO.ID_PAIS(+)");
    	sql.addJoin("MDA.ID_USUARIO_DESTINADO", "USUARIO_DESTINO.ID_USUARIO"); 
    	// Criteria
    	sql.addCriteria("DOCTO_SERVICO.ID_DOCTO_SERVICO", "=", this.getIdDoctoServico());
    	
    	return sql;
    }
    
    
	/**
	 * Método que gera o subrelatorio de Item MDA. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeSubItens() throws Exception {    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection("ITEM_MDA.ID_ITEM_MDA", "ID_ITEM_MDA");
        sql.addProjection("DOCTO_SERVICO.ID_DOCTO_SERVICO", "ID_DOCTO_SERVICO");        
        sql.addProjection("DOCTO_SERVICO.TP_DOCUMENTO_SERVICO", "TP_DOCUMENTO_SERVICO");
        sql.addProjection("FILIAL_ORIGEM.SG_FILIAL", "SG_FILIAL_ORIGEM");
        sql.addProjection("DOCTO_SERVICO.NR_DOCTO_SERVICO", "NR_DOCTO_SERVICO");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("NATUREZA_PRODUTO.DS_NATUREZA_PRODUTO_I"), "DS_NATUREZA_PRODUTO");
        sql.addProjection("ITEM_MDA.QT_VOLUMES", "QT_VOLUMES");
        sql.addProjection("MOEDA.SG_MOEDA", "SG_MOEDA");
        sql.addProjection("MOEDA.DS_SIMBOLO", "DS_SIMBOLO");
        sql.addProjection("ITEM_MDA.DS_MERCADORIA", "DS_MERCADORIA");
        sql.addProjection("ITEM_MDA.PS_ITEM", "PS_ITEM");
        sql.addProjection("ITEM_MDA.VL_MERCADORIA", "VL_MERCADORIA");
        sql.addProjection("ITEM_MDA.OB_ITEM_MDA","OB_ITEM_MDA");

        sql.addFrom("DOCTO_SERVICO", "DOCTO_SERVICO");
        sql.addFrom("FILIAL", "FILIAL_ORIGEM");
        sql.addFrom("NATUREZA_PRODUTO", "NATUREZA_PRODUTO");
        sql.addFrom("MOEDA", "MOEDA");
        sql.addFrom("ITEM_MDA", "ITEM_MDA");
        
        sql.addJoin("ITEM_MDA.ID_DOCTO_SERVICO", "DOCTO_SERVICO.ID_DOCTO_SERVICO(+)");
        sql.addJoin("DOCTO_SERVICO.ID_FILIAL_ORIGEM", "FILIAL_ORIGEM.ID_FILIAL(+)");
        sql.addJoin("ITEM_MDA.ID_NATUREZA_PRODUTO", "NATUREZA_PRODUTO.ID_NATUREZA_PRODUTO");
        sql.addJoin("ITEM_MDA.ID_MOEDA", "MOEDA.ID_MOEDA");
        	
        sql.addCriteria("ITEM_MDA.ID_MDA", "=", this.getIdDoctoServico());
            	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
    
	/**
	 * Método que gera o subrelatorio de Notas Fiscais de Item MDA. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeSubNF(Long idItemMda) throws Exception {    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection("NOTA_FISCAL_CONHECIMENTO.NR_NOTA_FISCAL", "NR_NOTA_FISCAL");

        sql.addFrom("NF_ITEM_MDA", "NF_ITEM_MDA");
        sql.addFrom("NOTA_FISCAL_CONHECIMENTO", "NOTA_FISCAL_CONHECIMENTO");
                
        sql.addJoin("NF_ITEM_MDA.ID_NOTA_FISCAL_CONHECIMENTO", "NOTA_FISCAL_CONHECIMENTO.ID_NOTA_FISCAL_CONHECIMENTO");
        sql.addCriteria("NF_ITEM_MDA.ID_ITEM_MDA", "=", idItemMda);
                    	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }    
    
    /**
     * Configura Dominios
     */
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_DOCUMENTO_SERVICO", "DM_TIPO_DOCUMENTO_SERVICO");
	}        
    

	public Long getIdDoctoServico() {
		return idDoctoServico;
	}
	public void setIdDoctoServico(Long idDoctoServico) {
		this.idDoctoServico = idDoctoServico;
	}

}
