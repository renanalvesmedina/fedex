package com.mercurio.lms.coleta.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe responsável pela geração do Relatório de Manifestos Coleta Sem Baixa.
 * Especificação técnica 02.03.02.12
 * @author Christian Brunkow
 * 
 * @spring.bean id="lms.coleta.manifestosColetaSemBaixaService"
 * @spring.property name="reportName" value="com/mercurio/lms/coleta/report/emitirManifestosColetaSemBaixa.jasper"
 */

public class ManifestosColetaSemBaixaService extends ReportServiceSupport{

    /**
     * método responsável por gerar o relatório. 
     */
	
	private DomainValueService domainValueService;
	
    public JRReportDataObject execute(Map parameters) throws Exception {
        SqlTemplate sql = checkSQLCase(parameters);
        
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
         
        if (!this.validateManifestoColeta(parameters, sql)) throw new BusinessException("LMS-02046"); 
        
        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
        
        parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        
        jr.setParameters(parametersReport);
        return jr;
    }
    
    /**
     *  Avalia qual deve ser a SQL aplicada.
     *  E determinada de acordo com os parametros enviados.
     *  
     * @param parameters
     * @return
     */
    private SqlTemplate checkSQLCase(Map parameters) {
    	
    	if (!parameters.get("rotaColetaEntrega.idRotaColetaEntrega").equals("")){
    		return this.mountSqlFirstCase(parameters);
    	} else if (!parameters.get("manifestoColeta.idManifestoColeta").equals("")) {
    		return this.mountSqlSecondCase(parameters);
    	} else if (!parameters.get("meioTransporte.idMeioTransporte").equals("")) {
    		return this.mountSqlThirdCase(parameters);
    	}
    	return this.mountSqlSecondCase(parameters);
    }
    
    /**
     * Verifica se este e o segundo caso de sql (campo manifesto preenchido)
     * e caso o resultado da consulta retorna alguma informacao. 
     * Retorna 'true' caso exista algum registro e 'false' nao encontre nenhum.
     * 
     * @param parameters
     * @param sql
     * @return boolean
     */
    private boolean validateManifestoColeta(Map parameters, SqlTemplate sql) {
    	
    	//Verifica se este e o segundo caso
        if (!parameters.get("manifestoColeta.idManifestoColeta").equals("")) {
        
        	//Busca a consulta a ser gerado e valida a possibilidade de existir dados na mesma...
        	Map resultado = (Map) this.getJdbcTemplate().query("select count(manifestoColeta.ID_MANIFESTO_COLETA) as rowCount " + sql.getSql(false), sql.getCriteria(), new ResultSetExtractor() {
    			public Object extractData(ResultSet rs) throws SQLException {
    				Map mapa = new HashMap();
    				while (rs.next()) {
    					mapa.put("rowCount", rs.getString("rowCount"));
    				}
    				return mapa;
    			}
    		});
        	
        	if (Integer.parseInt(resultado.get("rowCount").toString())==0) return false;
        }
        return true;
    }
    
    /**
     * Método que monta o select para a consulta.
     * 
     * @param map
     * @return SqlTemplate
     */
    private SqlTemplate mountSqlFirstCase(Map parameters) { 
    	
    	SqlTemplate sql = createSqlTemplate();
    	
    	sql.addProjection("manifestoColeta.ID_MANIFESTO_COLETA", "idManifestoColeta");
    	sql.addProjection("manifestoColeta.ID_ROTA_COLETA_ENTREGA", "idRotaColetaEntrega");
    	sql.addProjection("manifestoColeta.ID_FILIAL_ORIGEM", "idFilialOrigem");
    	sql.addProjection("manifestoColeta.TP_STATUS_MANIFESTO_COLETA", "tpStatusManifestoColeta");
    	sql.addProjection("pedidoColeta.TP_STATUS_COLETA", "tpStatusColeta");
    	sql.addProjection("pedidoColeta.ID_ROTA_COLETA_ENTREGA", "idRotaColetaEntrega");
    	
    	sql.addProjection("filialOrigem.SG_FILIAL", "sgFilialOrigem");
    	sql.addProjection("filialResponsavel.SG_FILIAL", "sgFilialResponsavel");
    	sql.addProjection("rotaColetaEntrega.NR_ROTA", "nrRota");
    	sql.addProjection("rotaColetaEntrega.DS_ROTA", "dsRota");
    	sql.addProjection("manifestoColeta.NR_MANIFESTO", "nrManifesto");
    	sql.addProjection("manifestoColeta.DH_EMISSAO", "dhEmissao");
    	sql.addProjection("pessoa.NM_PESSOA", "nmPessoa");
    	sql.addProjection("pedidoColeta.NR_COLETA", "nrColeta");
    	sql.addProjection("pedidoColeta.ED_COLETA", "edColeta");
    	sql.addProjection("pedidoColeta.NR_ENDERECO", "nrEndereco");
    	sql.addProjection("pedidoColeta.DS_COMPLEMENTO_ENDERECO", "dsComplementoEndereco");
    	sql.addProjection("pedidoColeta.PS_TOTAL_VERIFICADO","psTotalVerificado");
    	sql.addProjection("pedidoColeta.QT_TOTAL_VOLUMES_VERIFICADO","qtTotalVolumesVerificado");
    	sql.addProjection("moeda.DS_SIMBOLO","dsSimbolo");
    	sql.addProjection("moeda.SG_MOEDA","sgMoeda");
    	sql.addProjection("pedidoColeta.VL_TOTAL_VERIFICADO","vlTotalVerificado");
    	sql.addProjection("pedidoColeta.OB_PEDIDO_COLETA","obPedidoColeta");
    	sql.addProjection("municipio.NM_MUNICIPIO","nmMunicipio");
    	sql.addProjection("unidadeFederativa.SG_UNIDADE_FEDERATIVA","sgUnidadeFederativa");
    	 
    	sql.addFrom("manifesto_coleta", "manifestoColeta");		 	   
    	sql.addFrom("pedido_coleta", "pedidoColeta");
    	sql.addFrom("filial", "filialOrigem");
    	sql.addFrom("filial", "filialResponsavel");
    	sql.addFrom("rota_coleta_entrega", "rotaColetaEntrega");
    	sql.addFrom("cliente", "cliente");
    	sql.addFrom("pessoa", "pessoa");
    	sql.addFrom("municipio", "municipio");
    	sql.addFrom("unidade_federativa", "unidadeFederativa");
    	sql.addFrom("moeda", "moeda");
    	sql.addFrom("detalhe_coleta", "detalheColeta");
    	
    	sql.addJoin("manifestoColeta.ID_MANIFESTO_COLETA", "pedidoColeta.ID_MANIFESTO_COLETA");
    	sql.addJoin("manifestoColeta.ID_ROTA_COLETA_ENTREGA", "rotaColetaEntrega.ID_ROTA_COLETA_ENTREGA");
    	sql.addJoin("manifestoColeta.ID_FILIAL_ORIGEM", "filialOrigem.ID_FILIAL");
    	sql.addJoin("pedidoColeta.ID_FILIAL_RESPONSAVEL", "filialResponsavel.ID_FILIAL");
    	sql.addJoin("pedidoColeta.ID_CLIENTE", "cliente.ID_CLIENTE");
    	sql.addJoin("cliente.ID_CLIENTE", "pessoa.ID_PESSOA");
    	sql.addJoin("pedidoColeta.ID_MUNICIPIO", "municipio.ID_MUNICIPIO");
    	sql.addJoin("municipio.ID_UNIDADE_FEDERATIVA","unidadeFederativa.ID_UNIDADE_FEDERATIVA");
    	sql.addJoin("pedidoColeta.ID_MOEDA","moeda.ID_MOEDA");
    	sql.addJoin("pedidoColeta.ID_PEDIDO_COLETA","detalheColeta.ID_PEDIDO_COLETA");

        //manifestoColeta.ID_FILIAL_ORIGEM
        String idFilial = String.valueOf(parameters.get("filialByIdFilialResponsavel.idFilial"));
        if(StringUtils.isNotBlank(idFilial)) {
            sql.addCriteria("manifestoColeta.ID_FILIAL_ORIGEM","=", idFilial);
            String sgFilial = String.valueOf(parameters.get("filialByIdFilialResponsavel.sgFilialValue"));
            sql.addFilterSummary("filial", sgFilial);
        }
    	
    	//Faz a validacao do id da RotaColetaEntrega junto
    	String idRota = String.valueOf(parameters.get("rotaColetaEntrega.idRotaColetaEntrega"));
        if(StringUtils.isNotBlank(idRota)) {
            sql.addCriteria("manifestoColeta.ID_ROTA_COLETA_ENTREGA","=", idRota);
            String dsRota = String.valueOf(parameters.get("rotaColetaEntrega.dsRotaValue"));
            sql.addFilterSummary("rotaColetaEntrega", dsRota);
        }
        
        if(StringUtils.isNotBlank(parameters.get("servico.idServico").toString())){
        	Long idServico = Long.valueOf(parameters.get("servico.idServico").toString());
    		sql.addCriteria("detalheColeta.ID_SERVICO", "=", idServico);
    		sql.addFilterSummary("servico", parameters.get("servico.dsServico"));
    	}
    	
        String tpPedidoColeta = String.valueOf(parameters.get("tpPedidoColeta"));
    	if(StringUtils.isNotBlank(tpPedidoColeta)){
    		sql.addCriteria("pedidoColeta.TP_PEDIDO_COLETA", "=", tpPedidoColeta);
    		sql.addFilterSummary("tipoColeta", parameters.get("dsTpPedidoColeta"));
    	}
        
        sql.addCustomCriteria("manifestoColeta.TP_STATUS_MANIFESTO_COLETA NOT IN ('GE', 'FE')");
    	        
        sql.addCustomCriteria("pedidoColeta.TP_STATUS_COLETA NOT IN ('FI', 'NT', 'EX')");
        
        sql.addOrderBy("rotaColetaEntrega.NR_ROTA, manifestoColeta.NR_MANIFESTO, pedidoColeta.NR_COLETA");
        
        return sql;
    }
    
    /**
     * Método que monta o select para a consulta.
     * 
     * @param map
     * @return SqlTemplate
     */
    private SqlTemplate mountSqlSecondCase(Map parameters) { 
    	
    	SqlTemplate sql = createSqlTemplate();
    	sql.addProjection("manifestoColeta.ID_MANIFESTO_COLETA", "idManifestoColeta");
    	sql.addProjection("manifestoColeta.ID_ROTA_COLETA_ENTREGA", "idRotaColetaEntrega");
    	sql.addProjection("manifestoColeta.ID_FILIAL_ORIGEM", "idFilialOrigem");
    	sql.addProjection("manifestoColeta.TP_STATUS_MANIFESTO_COLETA", "tpStatusManifestoColeta");
    	sql.addProjection("pedidoColeta.TP_STATUS_COLETA", "tpStatusColeta");
    	sql.addProjection("pedidoColeta.ID_ROTA_COLETA_ENTREGA", "idRotaColetaEntrega");	
    	sql.addProjection("filialOrigem.SG_FILIAL", "sgFilialOrigem");
    	sql.addProjection("filialResponsavel.SG_FILIAL", "sgFilialResponsavel");
    	sql.addProjection("rotaColetaEntrega.NR_ROTA", "nrRota");
    	sql.addProjection("rotaColetaEntrega.DS_ROTA", "dsRota");
    	sql.addProjection("manifestoColeta.NR_MANIFESTO", "nrManifesto");
    	sql.addProjection("manifestoColeta.DH_EMISSAO", "dhEmissao");
    	sql.addProjection("pessoa.NM_PESSOA", "nmPessoa");
    	sql.addProjection("pedidoColeta.NR_COLETA", "nrColeta");
    	sql.addProjection("pedidoColeta.ED_COLETA", "edColeta");
    	sql.addProjection("pedidoColeta.NR_ENDERECO", "nrEndereco");
    	sql.addProjection("pedidoColeta.DS_COMPLEMENTO_ENDERECO", "dsComplementoEndereco");
    	sql.addProjection("pedidoColeta.PS_TOTAL_VERIFICADO","psTotalVerificado");
    	sql.addProjection("pedidoColeta.QT_TOTAL_VOLUMES_VERIFICADO","qtTotalVolumesVerificado");
    	sql.addProjection("moeda.DS_SIMBOLO","dsSimbolo");
    	sql.addProjection("moeda.SG_MOEDA","sgMoeda");
    	sql.addProjection("pedidoColeta.VL_TOTAL_VERIFICADO","vlTotalVerificado");
    	sql.addProjection("pedidoColeta.OB_PEDIDO_COLETA","obPedidoColeta");
    	sql.addProjection("municipio.NM_MUNICIPIO","nmMunicipio");
    	sql.addProjection("unidadeFederativa.SG_UNIDADE_FEDERATIVA","sgUnidadeFederativa");
    	
    	sql.addFrom("manifesto_coleta", "manifestoColeta");		 	   
    	sql.addFrom("pedido_coleta", "pedidoColeta");
    	sql.addFrom("filial", "filialOrigem");
    	sql.addFrom("filial", "filialResponsavel");
    	sql.addFrom("rota_coleta_entrega", "rotaColetaEntrega");
    	sql.addFrom("cliente", "cliente");
    	sql.addFrom("pessoa", "pessoa");
    	sql.addFrom("municipio", "municipio");
    	sql.addFrom("unidade_federativa", "unidadeFederativa");
    	sql.addFrom("moeda", "moeda");
    	sql.addFrom("detalhe_coleta", "detalheColeta");
    	
    	sql.addJoin("manifestoColeta.ID_MANIFESTO_COLETA", "pedidoColeta.ID_MANIFESTO_COLETA");
    	sql.addJoin("manifestoColeta.ID_ROTA_COLETA_ENTREGA", "rotaColetaEntrega.ID_ROTA_COLETA_ENTREGA");
    	sql.addJoin("manifestoColeta.ID_FILIAL_ORIGEM", "filialOrigem.ID_FILIAL");
    	sql.addJoin("pedidoColeta.ID_FILIAL_RESPONSAVEL", "filialResponsavel.ID_FILIAL");
    	sql.addJoin("pedidoColeta.ID_CLIENTE", "cliente.ID_CLIENTE");
    	sql.addJoin("cliente.ID_CLIENTE", "pessoa.ID_PESSOA");
    	sql.addJoin("pedidoColeta.ID_MUNICIPIO", "municipio.ID_MUNICIPIO");
    	sql.addJoin("municipio.ID_UNIDADE_FEDERATIVA","unidadeFederativa.ID_UNIDADE_FEDERATIVA");
    	sql.addJoin("pedidoColeta.ID_MOEDA","moeda.ID_MOEDA");
    	sql.addJoin("pedidoColeta.ID_PEDIDO_COLETA","detalheColeta.ID_PEDIDO_COLETA");
    	
    	//manifestoColeta.ID_FILIAL_ORIGEM
        String idFilial = String.valueOf(parameters.get("filialByIdFilialResponsavel.idFilial"));
        String sgFilial = null;
        if(StringUtils.isNotBlank(idFilial)) {
            sql.addCriteria("manifestoColeta.ID_FILIAL_ORIGEM","=", idFilial);
            sgFilial = String.valueOf(parameters.get("filialByIdFilialResponsavel.sgFilialValue"));
            sql.addFilterSummary("filial", sgFilial);
        }
        
        //Faz a validacao do id da RotaColetaEntrega junto
    	String idManifestoColeta = String.valueOf(parameters.get("manifestoColeta.idManifestoColeta"));
        if(StringUtils.isNotBlank(idManifestoColeta)) {
            sql.addCriteria("manifestoColeta.ID_MANIFESTO_COLETA","=", idManifestoColeta);
            Long nrManifesto = Long.valueOf(parameters.get("manifestoColeta.nrManifestoValue").toString());
            sql.addFilterSummary("manifesto", FormatUtils.formatSgFilialWithLong(sgFilial, nrManifesto)); 
        }
        
        if(StringUtils.isNotBlank(parameters.get("servico.idServico").toString())){
        	Long idServico = Long.valueOf(parameters.get("servico.idServico").toString());
    		sql.addCriteria("detalheColeta.ID_SERVICO", "=", idServico);
    		sql.addFilterSummary("servico", parameters.get("servico.dsServico"));
    	}
    	
        String tpPedidoColeta = String.valueOf(parameters.get("tpPedidoColeta"));
    	if(StringUtils.isNotBlank(tpPedidoColeta)){
    		sql.addCriteria("pedidoColeta.TP_PEDIDO_COLETA", "=", tpPedidoColeta);
    		sql.addFilterSummary("tipoColeta", parameters.get("dsTpPedidoColeta"));
    	}
    	
        sql.addCustomCriteria("manifestoColeta.TP_STATUS_MANIFESTO_COLETA NOT IN ('GE', 'FE')");
        
        sql.addCustomCriteria("pedidoColeta.TP_STATUS_COLETA NOT IN ('FI', 'NT', 'EX')");
        
        sql.addOrderBy("manifestoColeta.NR_MANIFESTO, pedidoColeta.NR_COLETA");
        
        return sql;
    }
    
    /**
     * Método que monta o select para a consulta.
     * 
     * @param map
     * @return SqlTemplate
     */
    private SqlTemplate mountSqlThirdCase(Map parameters) { 
    	
    	SqlTemplate sql = createSqlTemplate();
    	
    	sql.addProjection("meioTransporte.ID_MEIO_TRANSPORTE", "idMeioTransporte");
    	sql.addProjection("meioTransporte.NR_FROTA", "nrFrota");
    	sql.addProjection("controleCarga.ID_FILIAL_ORIGEM", "idFilialOrigem");
    	sql.addProjection("controleCarga.ID_ROTA_COLETA_ENTREGA", "tpRotaColetaEntrega");
    	sql.addProjection("controleCarga.TP_CONTROLE_CARGA", "tpControleCarga");
    	sql.addProjection("controleCarga.TP_STATUS_CONTROLE_CARGA", "tpStatusControleCarga");
    	sql.addProjection("controleCarga.ID_ROTA_COLETA_ENTREGA", "idRotaColetaEntrega");
    	sql.addProjection("manifestoColeta.ID_FILIAL_ORIGEM", "idFilialOrigem");
    	sql.addProjection("manifestoColeta.TP_STATUS_MANIFESTO_COLETA", "tpStatusManifestoColeta");
    	sql.addProjection("pedidoColeta.ID_ROTA_COLETA_ENTREGA", "idRotaColetaEntrega");
    	sql.addProjection("pedidoColeta.TP_STATUS_COLETA", "tpStatusColeta");
    	
    	sql.addProjection("filialOrigem.SG_FILIAL", "sgFilialOrigem");
    	sql.addProjection("filialResponsavel.SG_FILIAL", "sgFilialResponsavel");
    	sql.addProjection("rotaColetaEntrega.NR_ROTA", "nrRota");
    	sql.addProjection("rotaColetaEntrega.DS_ROTA", "dsRota");
    	sql.addProjection("manifestoColeta.NR_MANIFESTO", "nrManifesto");
    	sql.addProjection("manifestoColeta.DH_EMISSAO", "dhEmissao");
    	sql.addProjection("pessoa.NM_PESSOA", "nmPessoa");
    	sql.addProjection("pedidoColeta.NR_COLETA", "nrColeta");
    	sql.addProjection("pedidoColeta.ED_COLETA", "edColeta");
    	sql.addProjection("pedidoColeta.NR_ENDERECO", "nrEndereco");
    	sql.addProjection("pedidoColeta.DS_COMPLEMENTO_ENDERECO", "dsComplementoEndereco");
    	sql.addProjection("pedidoColeta.PS_TOTAL_VERIFICADO","psTotalVerificado");
    	sql.addProjection("pedidoColeta.QT_TOTAL_VOLUMES_VERIFICADO","qtTotalVolumesVerificado");
    	sql.addProjection("moeda.DS_SIMBOLO","dsSimbolo");
    	sql.addProjection("moeda.SG_MOEDA","sgMoeda");
    	sql.addProjection("pedidoColeta.VL_TOTAL_VERIFICADO","vlTotalVerificado");
    	sql.addProjection("pedidoColeta.OB_PEDIDO_COLETA","obPedidoColeta");
    	sql.addProjection("municipio.NM_MUNICIPIO","nmMunicipio");
    	sql.addProjection("unidadeFederativa.SG_UNIDADE_FEDERATIVA","sgUnidadeFederativa");
    	
    	sql.addFrom("meio_transporte", "meioTransporte");		 	   
    	sql.addFrom("controle_carga", "controleCarga");
    	sql.addFrom("manifesto_coleta", "manifestoColeta");		 	   
    	sql.addFrom("pedido_coleta", "pedidoColeta");
    	sql.addFrom("filial", "filialOrigem");
    	sql.addFrom("filial", "filialResponsavel");
    	sql.addFrom("rota_coleta_entrega", "rotaColetaEntrega");
    	sql.addFrom("cliente", "cliente");
    	sql.addFrom("pessoa", "pessoa");
    	sql.addFrom("municipio", "municipio");
    	sql.addFrom("unidade_federativa", "unidadeFederativa");
    	sql.addFrom("moeda", "moeda");
    	sql.addFrom("detalhe_coleta", "detalheColeta");
    	
    	sql.addJoin("controleCarga.ID_TRANSPORTADO", "meioTransporte.ID_MEIO_TRANSPORTE");
    	sql.addJoin("manifestoColeta.ID_CONTROLE_CARGA", "controleCarga.ID_CONTROLE_CARGA");
    	sql.addJoin("manifestoColeta.ID_MANIFESTO_COLETA", "pedidoColeta.ID_MANIFESTO_COLETA");
    	sql.addJoin("manifestoColeta.ID_ROTA_COLETA_ENTREGA", "rotaColetaEntrega.ID_ROTA_COLETA_ENTREGA");
    	sql.addJoin("manifestoColeta.ID_FILIAL_ORIGEM", "filialOrigem.ID_FILIAL");
    	sql.addJoin("pedidoColeta.ID_FILIAL_RESPONSAVEL", "filialResponsavel.ID_FILIAL");
    	sql.addJoin("pedidoColeta.ID_CLIENTE", "cliente.ID_CLIENTE");
    	sql.addJoin("cliente.ID_CLIENTE", "pessoa.ID_PESSOA");
    	sql.addJoin("pedidoColeta.ID_MUNICIPIO", "municipio.ID_MUNICIPIO");
    	sql.addJoin("municipio.ID_UNIDADE_FEDERATIVA","unidadeFederativa.ID_UNIDADE_FEDERATIVA");
    	sql.addJoin("pedidoColeta.ID_MOEDA","moeda.ID_MOEDA");
    	sql.addJoin("pedidoColeta.ID_PEDIDO_COLETA","detalheColeta.ID_PEDIDO_COLETA");
    	
    	String idFilial = String.valueOf(parameters.get("filialByIdFilialResponsavel.idFilial"));
        if(StringUtils.isNotBlank(idFilial)) {
            sql.addCriteria("manifestoColeta.ID_FILIAL_ORIGEM","=", idFilial);
            String sgFilial = String.valueOf(parameters.get("filialByIdFilialResponsavel.sgFilialValue"));
            sql.addFilterSummary("filial", sgFilial);
        }

        if(StringUtils.isNotBlank(idFilial)) {
            sql.addCriteria("controleCarga.ID_FILIAL_ORIGEM","=", idFilial);
        }
    	String idMeioTransporte = String.valueOf(parameters.get("meioTransporte.idMeioTransporte"));
        if(StringUtils.isNotBlank(idMeioTransporte)) {
            sql.addCriteria("meioTransporte.ID_MEIO_TRANSPORTE","=", idMeioTransporte);
            String nrFrota = String.valueOf(parameters.get("meioTransporte2.nrFrotaValue"));
            String placa = String.valueOf(parameters.get("meioTransporte.nrIdentificadorValue"));
            sql.addFilterSummary("meioTransporte", nrFrota + " - " + placa);
        }
        if(StringUtils.isNotBlank(parameters.get("servico.idServico").toString())){
        	Long idServico = Long.valueOf(parameters.get("servico.idServico").toString());
    		sql.addCriteria("detalheColeta.ID_SERVICO", "=", idServico);
    		sql.addFilterSummary("servico", parameters.get("servico.dsServico"));
    	}
    	
        String tpPedidoColeta = String.valueOf(parameters.get("tpPedidoColeta"));
    	if(StringUtils.isNotBlank(tpPedidoColeta)){
    		sql.addCriteria("pedidoColeta.TP_PEDIDO_COLETA", "=", tpPedidoColeta);
    		sql.addFilterSummary("tipoColeta", parameters.get("dsTpPedidoColeta"));
    	}
        sql.addCriteria("controleCarga.TP_CONTROLE_CARGA","=", "C");
        sql.addCustomCriteria("controleCarga.TP_STATUS_CONTROLE_CARGA NOT IN ('GE', 'CA')");
        sql.addCustomCriteria("manifestoColeta.TP_STATUS_MANIFESTO_COLETA NOT IN ('GE', 'FE')");
        sql.addCustomCriteria("pedidoColeta.TP_STATUS_COLETA NOT IN ('FI', 'NT', 'EX')");
        sql.addOrderBy("rotaColetaEntrega.NR_ROTA, manifestoColeta.NR_MANIFESTO, pedidoColeta.NR_COLETA");
        
        return sql;
    }
    
	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
}
