package com.mercurio.lms.sgr.report;

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
 * Classe responsável pela geração do Relatório de Enquadramento de Regras
 * Especificação técnica 11.03.01.14
 * @author Cesar Gabbardo
 * 
 * @spring.bean id="lms.sgr.emitirRelatorioEnquadramentoRegrasService"
 * @spring.property name="reportName" value="com/mercurio/lms/sgr/report/emitirRelatorioEnquadramentoRegras.jasper"
 */
public class EmitirRelatorioEnquadramentoRegrasService extends ReportServiceSupport {

	private Long idEnquadramentoRegra;
	
    /**
     * Método responsável por gerar o relatório. 
     */
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap)parameters;
		
		this.setIdEnquadramentoRegra(tfm.getLong("idEnquadramentoRegra"));
		
        SqlTemplate sql = mountSql(tfm);
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
              
        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
        
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        
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
    	
    	sql.addProjection("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "ID_ENQUADRAMENTO_REGRA");
    	sql.addProjection(PropertyVarcharI18nProjection.createProjection("ENQUADRAMENTO_REGRA.DS_ENQUADRAMENTO_REGRA_I"), "DS_ENQUADRAMENTO_REGRA");
    	sql.addProjection("ENQUADRAMENTO_REGRA.DT_VIGENCIA_INICIAL", "DT_VIGENCIA_INICIAL");
    	sql.addProjection("ENQUADRAMENTO_REGRA.DT_VIGENCIA_FINAL", "DT_VIGENCIA_FINAL");
    	sql.addProjection(PropertyVarcharI18nProjection.createProjection("NATUREZA_PRODUTO.DS_NATUREZA_PRODUTO_I"), "DS_NATUREZA_PRODUTO");
    	sql.addProjection("PESSOA.NM_PESSOA", "NM_REGULADORA_SEGURO");
    	sql.addProjection("ENQUADRAMENTO_REGRA.TP_OPERACAO", "TP_OPERACAO");
    	sql.addProjection("ENQUADRAMENTO_REGRA.TP_MODAL", "TP_MODAL");
    	sql.addProjection("ENQUADRAMENTO_REGRA.TP_ABRANGENCIA", "TP_ABRANGENCIA");
    	sql.addProjection("ENQUADRAMENTO_REGRA.BL_SEGURO_MERCURIO", "BL_SEGURO_MERCURIO");
    	sql.addProjection("ENQUADRAMENTO_REGRA.TP_GRAU_RISCO_COLETA_ENTREGA", "TP_GRAU_RISCO_COLETA_ENTREGA");
        
    	sql.addFrom("ENQUADRAMENTO_REGRA");
    	sql.addFrom("NATUREZA_PRODUTO");
    	sql.addFrom("REGULADORA_SEGURO");
    	sql.addFrom("PESSOA");    	
        
    	sql.addJoin("ENQUADRAMENTO_REGRA.ID_NATUREZA_PRODUTO", "NATUREZA_PRODUTO.ID_NATUREZA_PRODUTO(+)");
    	sql.addJoin("ENQUADRAMENTO_REGRA.ID_REGULADORA_SEGURO", "REGULADORA_SEGURO.ID_REGULADORA(+)");
    	sql.addJoin("REGULADORA_SEGURO.ID_REGULADORA", "PESSOA.ID_PESSOA(+)");

    	sql.addCriteria("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "=", this.getIdEnquadramentoRegra());
     
        return sql;
    }

    
	/**
	 * Método que gera o subrelatorio de Cliente. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirRelatorioEnquadramentoRegrasCliente() throws Exception {    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection("PESSOA.NM_PESSOA", "NM_CLIENTE");
        
    	sql.addFrom("ENQUADRAMENTO_REGRA");
    	sql.addFrom("CLIENTE_ENQUADRAMENTO");
    	sql.addFrom("PESSOA");
        
    	sql.addJoin("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "CLIENTE_ENQUADRAMENTO.ID_ENQUADRAMENTO_REGRA(+)");
    	sql.addJoin("CLIENTE_ENQUADRAMENTO.ID_CLIENTE", "PESSOA.ID_PESSOA(+)");

    	sql.addCriteria("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "=", this.getIdEnquadramentoRegra());
    	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    

	/**
	 * Método que gera o subrelatorio de Municipio Origem. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirRelatorioEnquadramentoRegrasMunicipioOrigem() throws Exception {    	
        SqlTemplate sql = createSqlTemplate();
    
        sql.addProjection("MUNICIPIO.NM_MUNICIPIO", "NM_MUNICIPIO_ORIGEM");
        
    	sql.addFrom("ENQUADRAMENTO_REGRA");
    	sql.addFrom("MUNICIPIO_ENQUADRAMENTO");
    	sql.addFrom("MUNICIPIO");
        
    	sql.addJoin("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "MUNICIPIO_ENQUADRAMENTO.ID_ENQUADRAMENTO_REGRA(+)");
    	sql.addJoin("MUNICIPIO_ENQUADRAMENTO.ID_MUNICIPIO", "MUNICIPIO.ID_MUNICIPIO(+)");
    	    	
    	sql.addCriteria("MUNICIPIO_ENQUADRAMENTO.TP_ORIGEM_DESTINO", "=", "O");
    	sql.addCriteria("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "=", this.getIdEnquadramentoRegra());
    	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
    
	/**
	 * Método que gera o subrelatorio de Municipio Destino. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirRelatorioEnquadramentoRegrasMunicipioDestino() throws Exception {    	
        SqlTemplate sql = createSqlTemplate();
    
        sql.addProjection("MUNICIPIO.NM_MUNICIPIO", "NM_MUNICIPIO_DESTINO");
        
    	sql.addFrom("ENQUADRAMENTO_REGRA");
    	sql.addFrom("MUNICIPIO_ENQUADRAMENTO");
    	sql.addFrom("MUNICIPIO");
        
    	sql.addJoin("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "MUNICIPIO_ENQUADRAMENTO.ID_ENQUADRAMENTO_REGRA(+)");
    	sql.addJoin("MUNICIPIO_ENQUADRAMENTO.ID_MUNICIPIO", "MUNICIPIO.ID_MUNICIPIO(+)");
    	    	
    	sql.addCriteria("MUNICIPIO_ENQUADRAMENTO.TP_ORIGEM_DESTINO", "=", "D");
    	sql.addCriteria("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "=", this.getIdEnquadramentoRegra());
    	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }    
    
    
	/**
	 * Método que gera o subrelatorio de Unidade Federativa Origem. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirRelatorioEnquadramentoRegrasUFOrigem() throws Exception {    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection("UNIDADE_FEDERATIVA.SG_UNIDADE_FEDERATIVA", "SG_UF_ORIGEM");
        
    	sql.addFrom("ENQUADRAMENTO_REGRA");
    	sql.addFrom("UF_ENQUADRAMENTO");
    	sql.addFrom("UNIDADE_FEDERATIVA");
        
    	sql.addJoin("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "UF_ENQUADRAMENTO.ID_ENQUADRAMENTO_REGRA(+)");
    	sql.addJoin("UF_ENQUADRAMENTO.ID_UNIDADE_FEDERATIVA", "UNIDADE_FEDERATIVA.ID_UNIDADE_FEDERATIVA(+)");
    	    	
    	sql.addCriteria("UF_ENQUADRAMENTO.TP_ORIGEM_DESTINO", "=", "O");
    	sql.addCriteria("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "=", this.getIdEnquadramentoRegra());
    	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    } 
    
    
	/**
	 * Método que gera o subrelatorio de Unidade Federativa Destino. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirRelatorioEnquadramentoRegrasUFDestino() throws Exception {    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection("UNIDADE_FEDERATIVA.SG_UNIDADE_FEDERATIVA", "SG_UF_DESTINO");
        
    	sql.addFrom("ENQUADRAMENTO_REGRA");
    	sql.addFrom("UF_ENQUADRAMENTO");
    	sql.addFrom("UNIDADE_FEDERATIVA");
        
    	sql.addJoin("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "UF_ENQUADRAMENTO.ID_ENQUADRAMENTO_REGRA(+)");
    	sql.addJoin("UF_ENQUADRAMENTO.ID_UNIDADE_FEDERATIVA", "UNIDADE_FEDERATIVA.ID_UNIDADE_FEDERATIVA(+)");
    	    	
    	sql.addCriteria("UF_ENQUADRAMENTO.TP_ORIGEM_DESTINO", "=", "D");
    	sql.addCriteria("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "=", this.getIdEnquadramentoRegra());
    	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }     
    
    
	/**
	 * Método que gera o subrelatorio de Pais Origem. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirRelatorioEnquadramentoRegrasPaisOrigem() throws Exception {    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAIS.NM_PAIS_I"), "NM_PAIS_ORIGEM");
        
    	sql.addFrom("ENQUADRAMENTO_REGRA");
    	sql.addFrom("PAIS_ENQUADRAMENTO");
    	sql.addFrom("PAIS");
        
    	sql.addJoin("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "PAIS_ENQUADRAMENTO.ID_ENQUADRAMENTO_REGRA(+)");
    	sql.addJoin("PAIS_ENQUADRAMENTO.ID_PAIS", "PAIS.ID_PAIS(+)");
    	    	
    	sql.addCriteria("PAIS_ENQUADRAMENTO.TP_ORIGEM_DESTINO", "=", "O");
    	sql.addCriteria("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "=", this.getIdEnquadramentoRegra());
    	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    

	/**
	 * Método que gera o subrelatorio de Pais Destino. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirRelatorioEnquadramentoRegrasPaisDestino() throws Exception {    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAIS.NM_PAIS_I"), "NM_PAIS_DESTINO");
        
    	sql.addFrom("ENQUADRAMENTO_REGRA");
    	sql.addFrom("PAIS_ENQUADRAMENTO");
    	sql.addFrom("PAIS");
        
    	sql.addJoin("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "PAIS_ENQUADRAMENTO.ID_ENQUADRAMENTO_REGRA(+)");
    	sql.addJoin("PAIS_ENQUADRAMENTO.ID_PAIS", "PAIS.ID_PAIS(+)");
    	    	
    	sql.addCriteria("PAIS_ENQUADRAMENTO.TP_ORIGEM_DESTINO", "=", "D");
    	sql.addCriteria("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "=", this.getIdEnquadramentoRegra());
    	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }    
    
    
	/**
	 * Método que gera o subrelatorio de Filial Origem. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirRelatorioEnquadramentoRegrasFilialOrigem() throws Exception {
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection("FILIAL.SG_FILIAL", "SG_FILIAL_ORIGEM");        
        sql.addProjection("FILIAL_ENQUADRAMENTO.TP_ORIGEM_DESTINO", "TP_ORIGEM_DESTINO");
        
    	sql.addFrom("ENQUADRAMENTO_REGRA");
    	sql.addFrom("FILIAL_ENQUADRAMENTO");
    	sql.addFrom("FILIAL");
        
    	sql.addJoin("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "FILIAL_ENQUADRAMENTO.ID_ENQUADRAMENTO_REGRA(+)");
    	sql.addJoin("FILIAL_ENQUADRAMENTO.ID_FILIAL", "FILIAL.ID_FILIAL(+)");
    	    	
    	sql.addCriteria("FILIAL_ENQUADRAMENTO.TP_ORIGEM_DESTINO", "=", "O");
    	sql.addCriteria("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "=", this.getIdEnquadramentoRegra());
    	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }          
    
    
	/**
	 * Método que gera o subrelatorio de Filial Destino. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirRelatorioEnquadramentoRegrasFilialDestino() throws Exception {    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection("FILIAL.SG_FILIAL", "SG_FILIAL_DESTINO");
        sql.addProjection("FILIAL_ENQUADRAMENTO.TP_ORIGEM_DESTINO", "TP_ORIGEM_DESTINO");
        
    	sql.addFrom("ENQUADRAMENTO_REGRA");
    	sql.addFrom("FILIAL_ENQUADRAMENTO");
    	sql.addFrom("FILIAL");
        
    	sql.addJoin("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "FILIAL_ENQUADRAMENTO.ID_ENQUADRAMENTO_REGRA(+)");
    	sql.addJoin("FILIAL_ENQUADRAMENTO.ID_FILIAL", "FILIAL.ID_FILIAL(+)");
    	    	
    	sql.addCriteria("FILIAL_ENQUADRAMENTO.TP_ORIGEM_DESTINO", "=", "D");
    	sql.addCriteria("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "=", this.getIdEnquadramentoRegra());
    	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }   
    
    
	/**
	 * Método que gera o subrelatorio de Limite Valores. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirRelatorioEnquadramentoRegrasLimiteValores() throws Exception {    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection("FAIXA_DE_VALOR.ID_FAIXA_DE_VALOR", "ID_FAIXA_DE_VALOR");
        sql.addProjection("MOEDA.SG_MOEDA || ' ' || MOEDA.DS_SIMBOLO", "SG_MOEDA__DS_SIMBOLO");        
        sql.addProjection("FAIXA_DE_VALOR.VL_LIMITE_MINIMO", "VL_LIMITE_MINIMO");
        sql.addProjection("FAIXA_DE_VALOR.VL_LIMITE_MAXIMO", "VL_LIMITE_MAXIMO");
        
    	sql.addFrom("ENQUADRAMENTO_REGRA");
    	sql.addFrom("MOEDA");
    	sql.addFrom("FAIXA_DE_VALOR");
         
    	sql.addJoin("ENQUADRAMENTO_REGRA.ID_MOEDA", "MOEDA.ID_MOEDA");
    	sql.addJoin("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "FAIXA_DE_VALOR.ID_ENQUADRAMENTO_REGRA");
   	    	
    	sql.addCriteria("ENQUADRAMENTO_REGRA.ID_ENQUADRAMENTO_REGRA", "=", this.getIdEnquadramentoRegra());
    	
    	sql.addOrderBy("FAIXA_DE_VALOR.VL_LIMITE_MINIMO");
       	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
    
	/**
	 * Método que gera o subrelatorio de Detalhes do Limite Valores. 
	 * @return
	 * @throws Exception
	 */
    public JRDataSource executeEmitirRelatorioEnquadramentoRegrasDetalhes(Long idFaixaDeValor) throws Exception {    	
        SqlTemplate sql = createSqlTemplate();

        sql.addProjection(PropertyVarcharI18nProjection.createProjection("EXIGENCIA_GER_RISCO.DS_RESUMIDA_I"), "DS_RESUMIDA");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("EXIGENCIA_GER_RISCO.DS_COMPLETA_I"), "DS_COMPLETA");
        
    	sql.addFrom("EXIGENCIA_FAIXA_VALOR");
    	sql.addFrom("EXIGENCIA_GER_RISCO");

    	sql.addJoin("EXIGENCIA_FAIXA_VALOR.ID_EXIGENCIA_GER_RISCO", "EXIGENCIA_GER_RISCO.ID_EXIGENCIA_GER_RISCO");
    	    	
    	sql.addCriteria("EXIGENCIA_FAIXA_VALOR.ID_FAIXA_DE_VALOR", "=", idFaixaDeValor);
    	
    	sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("EXIGENCIA_GER_RISCO.DS_RESUMIDA_I"));
    	
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }       

    
    /**
     * Configura Dominios
     */
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_OPERACAO", "DM_TIPO_OPERACAO_ENQ_REGRA");
		config.configDomainField("TP_MODAL", "DM_MODAL");
		config.configDomainField("TP_ABRANGENCIA", "DM_ABRANGENCIA");
		config.configDomainField("BL_SEGURO_MERCURIO", "DM_SIM_NAO");
		config.configDomainField("TP_GRAU_RISCO_COLETA_ENTREGA", "DM_GRAU_RISCO");
	}
    
    
	public Long getIdEnquadramentoRegra() {
		return idEnquadramentoRegra;
	}
	public void setIdEnquadramentoRegra(Long idEnquadramentoRegra) {
		this.idEnquadramentoRegra = idEnquadramentoRegra;
	}	

}
