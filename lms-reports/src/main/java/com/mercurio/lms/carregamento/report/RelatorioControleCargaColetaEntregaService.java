package com.mercurio.lms.carregamento.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;


/**
 * Classe responsável pela geração do Relatório de Controle de Cargas - Coleta-Entrega
 * Especificação técnica 05.01.01.03
 */


public abstract class RelatorioControleCargaColetaEntregaService extends ReportServiceSupport {
	
	private ControleCargaService controleCargaService;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject executeMountCabecalho(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap)parameters;
		Long idControleCarga = tfm.getLong("idControleCarga");
		Long idManifesto = tfm.getLong("idManifesto");
		String tpManifesto = tfm.getString("tpManifesto");

        SqlTemplate sqlCabecalho = mountSqlCabecalho(idControleCarga, idManifesto, tpManifesto);
        JRReportDataObject jrCabecalho = executeQuery(sqlCabecalho.getSql(), sqlCabecalho.getCriteria());
        
        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
                
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
        
        jrCabecalho.setParameters(parametersReport);
        
        return jrCabecalho;		
	}

	
    private SqlTemplate mountSqlCabecalho(Long idControleCarga, Long idManifesto, String tpManifesto ) throws Exception {    	
    	SqlTemplate sql = createSqlTemplate();
    	
    	BigDecimal valorTotal = controleCargaService.findValorTotalFrota(idControleCarga);
    	
    	sql.addProjection("CONTROLE_CARGA.ID_CONTROLE_CARGA", "ID_CONTROLE_CARGA");
    	sql.addProjection("FILIAL.SG_FILIAL", "SG_FILIAL");
    	sql.addProjection("CONTROLE_CARGA.NR_CONTROLE_CARGA", "NR_CONTROLE_CARGA");
    	sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO "
    			+ "			WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_TIPO_CONTROLE_CARGAS') "
    			+ "					AND VL_VALOR_DOMINIO = CONTROLE_CARGA.TP_CONTROLE_CARGA)"
    														,"TP_CONTROLE_CARGA");    	
    	sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO "
    			+ "			WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_STATUS_CONTROLE_CARGA') "
    			+ "					AND VL_VALOR_DOMINIO = CONTROLE_CARGA.TP_STATUS_CONTROLE_CARGA)"
    														,"TP_STATUS_CONTROLE_CARGA");
    	sql.addProjection("MEIO_TRANSPORTE.NR_FROTA", "FROTA_VEICULO");
    	sql.addProjection("MEIO_TRANSPORTE.NR_IDENTIFICADOR", "IDENTIFICADOR_VEICULO");
    	sql.addProjection("PROPRIETARIO_PESSOA.NM_PESSOA", "NM_PROPRIETARIO");
    	sql.addProjection("MOTORISTA_PESSOA.NM_PESSOA", "NM_MOTORISTA");
    	sql.addProjection("MOEDA.SG_MOEDA", "SG_MOEDA");
    	sql.addProjection("MOEDA.DS_SIMBOLO", "DS_SIMBOLO");
    	sql.addProjection(String.valueOf(valorTotal), "VL_TOTAL_FROTA");
    	
    	
    	sql.addProjection("CONTROLE_QUILOMETRAGEM_SAIDA.NR_QUILOMETRAGEM", "NR_QUILOMETRAGEM_SAIDA");
    	sql.addProjection("CONTROLE_QUILOMETRAGEM_RETORNO.NR_QUILOMETRAGEM", "NR_QUILOMETRAGEM_RETORNO");
    	sql.addProjection("SEMI_REBOQUE.NR_FROTA", "FROTA_SEMI_REBOQUE");
    	sql.addProjection("SEMI_REBOQUE.NR_IDENTIFICADOR", "IDENTIFICADOR_SEMI_REBOQUE");       
    	sql.addProjection("FILIAL_NOTA.SG_FILIAL", "SG_FILIAL_NOTA");       
    	sql.addProjection("NOTA_CREDITO.NR_NOTA_CREDITO", "NR_NOTA_CREDITO");       
    	sql.addProjection("ROTA_COLETA_ENTREGA.NR_ROTA", "NR_ROTA");       
    	sql.addProjection("ROTA_COLETA_ENTREGA.DS_ROTA", "DS_ROTA ");  
    	
    	if(idManifesto != null){
    		sql.addProjection("FILIAL_MANIFESTO.SG_FILIAL", "SG_FILIAL_MANIFESTO");
    		sql.addProjection("CONTROLE_CARGA.TP_STATUS_CONTROLE_CARGA", "TP_STATUS_CONTROLE_CARGA");
    		sql.addProjection("CONTROLE_CARGA.PS_TOTAL_FROTA", "PS_TOTAL_FROTA ");
    		sql.addProjection("CONTROLE_CARGA.PS_TOTAL_AFORADO", "PS_TOTAL_AFORADO");
    		sql.addProjection("CONTROLE_CARGA.DH_SAIDA_COLETA_ENTREGA", "DH_SAIDA_COLETA_ENTREGA");
    		sql.addProjection("CONTROLE_CARGA.DH_CHEGADA_COLETA_ENTREGA", "DH_CHEGADA_COLETA_ENTREGA");
    		sql.addProjection("EVENTO_MEIO_TRANSPORTE.DH_GERACAO ", "DH_GERACAO");

    		if( ConstantesExpedicao.TP_RELATORIO_CONTROLE_CARGA_COLETA.equalsIgnoreCase(tpManifesto) ){
    			sql.addProjection("MANIFESTO_COLETA.NR_MANIFESTO", "NR_MANIFESTO");
    			sql.addProjection("'Coleta'", "TP_MANIFESTO");
    		}else if( ConstantesExpedicao.TP_RELATORIO_CONTROLE_CARGA_ENTREGA.equalsIgnoreCase(tpManifesto) ){
    			sql.addProjection("MANIFESTO_ENTREGA.NR_MANIFESTO_ENTREGA", "NR_MANIFESTO");
    			sql.addProjection("'Entrega'", "TP_MANIFESTO");
    		}
    		
    	}
    	
    	sql.addInnerJoin("CONTROLE_CARGA CONTROLE_CARGA");
    	sql.addInnerJoin("FILIAL FILIAL on CONTROLE_CARGA.ID_FILIAL_ORIGEM = FILIAL.ID_FILIAL");
    	sql.addLeftOuterJoin("MEIO_TRANSPORTE MEIO_TRANSPORTE on CONTROLE_CARGA.ID_TRANSPORTADO = MEIO_TRANSPORTE.ID_MEIO_TRANSPORTE");
    	sql.addLeftOuterJoin("MEIO_TRANSPORTE SEMI_REBOQUE on CONTROLE_CARGA.ID_SEMI_REBOCADO = SEMI_REBOQUE.ID_MEIO_TRANSPORTE");
    	sql.addLeftOuterJoin("PESSOA PROPRIETARIO_PESSOA on CONTROLE_CARGA.ID_PROPRIETARIO = PROPRIETARIO_PESSOA.ID_PESSOA");
    	sql.addLeftOuterJoin("PESSOA MOTORISTA_PESSOA on CONTROLE_CARGA.ID_MOTORISTA = MOTORISTA_PESSOA.ID_PESSOA");
    	sql.addLeftOuterJoin("MOEDA MOEDA on CONTROLE_CARGA.ID_MOEDA = MOEDA.ID_MOEDA");
    	sql.addLeftOuterJoin("NOTA_CREDITO NOTA_CREDITO on CONTROLE_CARGA.ID_NOTA_CREDITO = NOTA_CREDITO.ID_NOTA_CREDITO and NOTA_CREDITO.ID_FILIAL = FILIAL.ID_FILIAL");
    	sql.addLeftOuterJoin("FILIAL FILIAL_NOTA on FILIAL_NOTA.ID_FILIAL = NOTA_CREDITO.ID_FILIAL");
    	sql.addLeftOuterJoin("CONTROLE_QUILOMETRAGEM CONTROLE_QUILOMETRAGEM_SAIDA on CONTROLE_CARGA.ID_CONTROLE_CARGA = CONTROLE_QUILOMETRAGEM_SAIDA.ID_CONTROLE_CARGA and CONTROLE_QUILOMETRAGEM_SAIDA.BL_SAIDA = 'S'");
    	sql.addLeftOuterJoin("CONTROLE_QUILOMETRAGEM CONTROLE_QUILOMETRAGEM_RETORNO on CONTROLE_CARGA.ID_CONTROLE_CARGA = CONTROLE_QUILOMETRAGEM_RETORNO.ID_CONTROLE_CARGA and CONTROLE_QUILOMETRAGEM_RETORNO.BL_SAIDA = 'N'");
    	sql.addLeftOuterJoin("ROTA_COLETA_ENTREGA ROTA_COLETA_ENTREGA on CONTROLE_CARGA.ID_ROTA_COLETA_ENTREGA = ROTA_COLETA_ENTREGA.ID_ROTA_COLETA_ENTREGA");
    	
    	if(idManifesto != null){
    		sql.addLeftOuterJoin("EVENTO_MEIO_TRANSPORTE EVENTO_MEIO_TRANSPORTE on CONTROLE_CARGA.ID_CONTROLE_CARGA = EVENTO_MEIO_TRANSPORTE.ID_CONTROLE_CARGA"
																			+ "	and EVENTO_MEIO_TRANSPORTE.TP_SITUACAO_MEIO_TRANSPORTE = '" + ConstantesExpedicao.TP_RELATORIO_CONTROLE_CARGA_COLETA_AGUARDANDO_SAIDA  + "'");
    		if( ConstantesExpedicao.TP_RELATORIO_CONTROLE_CARGA_COLETA.equalsIgnoreCase(tpManifesto) ){
    			sql.addLeftOuterJoin("MANIFESTO_COLETA MANIFESTO_COLETA on MANIFESTO_COLETA.ID_CONTROLE_CARGA = CONTROLE_CARGA.ID_CONTROLE_CARGA");
    			sql.addInnerJoin("FILIAL FILIAL_MANIFESTO on MANIFESTO_COLETA.ID_FILIAL_ORIGEM = FILIAL_MANIFESTO.ID_FILIAL");
    		}else if( ConstantesExpedicao.TP_RELATORIO_CONTROLE_CARGA_ENTREGA.equalsIgnoreCase(tpManifesto) ){
    			sql.addInnerJoin("MANIFESTO MANIFESTO on MANIFESTO.ID_CONTROLE_CARGA = CONTROLE_CARGA.ID_CONTROLE_CARGA");
    			sql.addInnerJoin("MANIFESTO_ENTREGA MANIFESTO_ENTREGA on MANIFESTO_ENTREGA.ID_MANIFESTO_ENTREGA = MANIFESTO.ID_MANIFESTO");
    			sql.addInnerJoin("FILIAL FILIAL_MANIFESTO on MANIFESTO_ENTREGA.ID_FILIAL = FILIAL_MANIFESTO.ID_FILIAL");
    		}
    	}

    	sql.addCriteria("CONTROLE_CARGA.ID_CONTROLE_CARGA", "=", idControleCarga);
    	
    	if(idManifesto != null){
    		if( ConstantesExpedicao.TP_RELATORIO_CONTROLE_CARGA_COLETA.equalsIgnoreCase(tpManifesto) ){
    			sql.addCriteria("MANIFESTO_COLETA.ID_MANIFESTO_COLETA", "=", idManifesto);
    		}else if( ConstantesExpedicao.TP_RELATORIO_CONTROLE_CARGA_ENTREGA.equalsIgnoreCase(tpManifesto) ){
    			sql.addCriteria("MANIFESTO.ID_MANIFESTO", "=", idManifesto);
    			sql.addCriteria("MANIFESTO.TP_MANIFESTO ", "=", ConstantesEntrega.TP_MANIFESTO_ENTREGA);
    		}
    	}
    	
        return sql;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public JRDataSource executeMountManifestos(Long idControleCarga) throws Exception {
        String sqlManifestos = mountSqlManifestos();
        Object[] criteriaManifestos = {idControleCarga, idControleCarga};
        JRReportDataObject jrReportDataObject = executeQuery(sqlManifestos, criteriaManifestos);
        Map parametersReport = new HashMap();
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
        jrReportDataObject.setParameters(parametersReport);
        
        return jrReportDataObject.getDataSource();
    }
    
    private String mountSqlManifestos() throws Exception {    	
    	StringBuilder sql = new StringBuilder();
    	
    	StringBuilder projectionColeta = new StringBuilder("select ");
    	StringBuilder joinColeta = new StringBuilder(" from ");
    	StringBuilder criteriaColeta = new StringBuilder(" where ");
    	
    	StringBuilder orderBy = new StringBuilder(" order by ");
    	
    	projectionColeta.append(" MANIFESTO_COLETA.ID_CONTROLE_CARGA as ID_CONTROLE_CARGA, ");
    	projectionColeta.append(" MANIFESTO_COLETA.ID_MANIFESTO_COLETA as ID_MANIFESTO, ");

    	
    	projectionColeta.append(" CASE WHEN MANIFESTO_COLETA.ID_MANIFESTO_COLETA is not null THEN 'C' END as TIPO, ");
    	projectionColeta.append(" FILIAL_ORIGEM_MANIFESTO.SG_FILIAL as FILIAL_ORIGEM_MANIFESTO, ");
    	projectionColeta.append(" MANIFESTO_COLETA.NR_MANIFESTO as NR_MANIFESTO, ");
    	projectionColeta.append(" CASE WHEN MANIFESTO_COLETA.ID_MANIFESTO_COLETA is not null THEN 'Coleta' END as TP_MANIFESTO, ");
    	
    	projectionColeta.append("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO ")
			.append(" WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_STATUS_MANIFESTO_COLETA') ")
			.append(" AND VL_VALOR_DOMINIO = MANIFESTO_COLETA.TP_STATUS_MANIFESTO_COLETA)").append(" as TP_STATUS_MANIFESTO, ");
    	
    	projectionColeta.append(" (SELECT SUM(PC.PS_TOTAL_VERIFICADO) FROM PEDIDO_COLETA PC ")
    		.append("WHERE PC.ID_MANIFESTO_COLETA = MANIFESTO_COLETA.ID_MANIFESTO_COLETA) ").append("as PS_TOTAL_VERIFICADO, ");
    	
    	projectionColeta.append(" (SELECT SUM(PC.PS_TOTAL_AFORADO_VERIFICADO) FROM PEDIDO_COLETA PC ")
    		.append("WHERE PC.ID_MANIFESTO_COLETA = MANIFESTO_COLETA.ID_MANIFESTO_COLETA) ").append("as PS_TOTAL_AFORADO_VERIFICADO, ");
    	
    	projectionColeta.append("(SELECT M.SG_MOEDA || ' '||M.DS_SIMBOLO FROM PEDIDO_COLETA PC, MOEDA M ")
    		.append("WHERE PC.ID_MANIFESTO_COLETA = MANIFESTO_COLETA.ID_MANIFESTO_COLETA ").append("AND PC.ID_MOEDA = M.ID_MOEDA AND ROWNUM < 2)").append("as MOEDA,");

    	projectionColeta.append(" (SELECT SUM(PC.VL_TOTAL_VERIFICADO) FROM PEDIDO_COLETA PC ")
			.append("WHERE PC.ID_MANIFESTO_COLETA = MANIFESTO_COLETA.ID_MANIFESTO_COLETA) ").append("as VL_TOTAL_VERIFICADO ");
    	
    	joinColeta.append("MANIFESTO_COLETA MANIFESTO_COLETA ");
    	joinColeta.append("LEFT OUTER JOIN FILIAL FILIAL_ORIGEM_MANIFESTO on MANIFESTO_COLETA.ID_FILIAL_ORIGEM = FILIAL_ORIGEM_MANIFESTO.ID_FILIAL  ");
    	
    	criteriaColeta.append("MANIFESTO_COLETA.ID_CONTROLE_CARGA = ? ");
    	
    	StringBuilder projectionEntrega = new StringBuilder(" union select ");
    	StringBuilder joinEntrega = new StringBuilder(" from ");
    	StringBuilder criteriaEntrega = new StringBuilder(" where ");
    	
    	projectionEntrega.append(" MANIFESTO.ID_CONTROLE_CARGA as ID_CONTROLE_CARGA, ");
    	projectionEntrega.append(" MANIFESTO_ENTREGA.ID_MANIFESTO_ENTREGA as ID_MANIFESTO, ");
    	
    	projectionEntrega.append(" CASE WHEN MANIFESTO_ENTREGA.ID_MANIFESTO_ENTREGA is not null THEN 'E' END as TIPO,");
    	projectionEntrega.append(" FILIAL_ORIGEM_MAN_ENTR.SG_FILIAL as FILIAL_ORIGEM_MANIFESTO, ");
    	projectionEntrega.append(" MANIFESTO_ENTREGA.NR_MANIFESTO_ENTREGA as NR_MANIFESTO, ");
    	
    	projectionEntrega.append("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO ")
			.append(" WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_TIPO_MANIFESTO_ENTREGA') ")
			.append(" AND VL_VALOR_DOMINIO = MANIFESTO.TP_MANIFESTO_ENTREGA)")
			.append(" as TP_MANIFESTO, ");
    	
    	projectionEntrega.append("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO ")
			.append(" WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_STATUS_MANIFESTO') ")
			.append(" AND VL_VALOR_DOMINIO = MANIFESTO.TP_STATUS_MANIFESTO )")
			.append(" as TP_STATUS_MANIFESTO, ");
    	
    	
    	projectionEntrega.append("(SELECT SUM(DOCTO_SERVICO.PS_REAL) FROM DOCTO_SERVICO DOCTO_SERVICO, PRE_MANIFESTO_DOCUMENTO PRE_MANIFESTO_DOCUMENTO ")
    		.append(" WHERE PRE_MANIFESTO_DOCUMENTO.ID_DOCTO_SERVICO = DOCTO_SERVICO.ID_DOCTO_SERVICO ")
    		.append(" AND PRE_MANIFESTO_DOCUMENTO.ID_MANIFESTO = manifesto.ID_MANIFESTO) ")
    		.append(" as PS_TOTAL_VERIFICADO, ");
    	
    	projectionEntrega.append("(SELECT SUM(DOCTO_SERVICO.PS_AFORADO) FROM DOCTO_SERVICO DOCTO_SERVICO, PRE_MANIFESTO_DOCUMENTO PRE_MANIFESTO_DOCUMENTO ")
    		.append(" WHERE PRE_MANIFESTO_DOCUMENTO.ID_DOCTO_SERVICO = DOCTO_SERVICO.ID_DOCTO_SERVICO ")
    		.append(" AND PRE_MANIFESTO_DOCUMENTO.ID_MANIFESTO = manifesto.ID_MANIFESTO) ")
    		.append(" as PS_TOTAL_AFORADO_VERIFICADO, ");
    	
    	projectionEntrega.append(" MOEDA_MANIFESTO_ENTREGA.SG_MOEDA || ' ' || MOEDA_MANIFESTO_ENTREGA.DS_SIMBOLO as MOEDA, ");
    	
    	
    	projectionEntrega.append("(SELECT SUM(DOCTO_SERVICO.vl_mercadoria) FROM DOCTO_SERVICO DOCTO_SERVICO, PRE_MANIFESTO_DOCUMENTO PRE_MANIFESTO_DOCUMENTO ")
    		.append(" WHERE PRE_MANIFESTO_DOCUMENTO.ID_DOCTO_SERVICO = DOCTO_SERVICO.ID_DOCTO_SERVICO ")
    		.append(" AND PRE_MANIFESTO_DOCUMENTO.ID_MANIFESTO = manifesto.ID_MANIFESTO) ")
    		.append(" as VL_TOTAL_VERIFICADO ");

    	
    	joinEntrega.append("MANIFESTO MANIFESTO ");
    	joinEntrega.append("LEFT OUTER JOIN MANIFESTO_ENTREGA MANIFESTO_ENTREGA on MANIFESTO.ID_MANIFESTO = MANIFESTO_ENTREGA.ID_MANIFESTO_ENTREGA ");
    	joinEntrega.append("LEFT OUTER JOIN FILIAL FILIAL_ORIGEM_MAN_ENTR on MANIFESTO.ID_FILIAL_ORIGEM = FILIAL_ORIGEM_MAN_ENTR.ID_FILIAL  ");
    	joinEntrega.append("LEFT OUTER JOIN MOEDA MOEDA_MANIFESTO_ENTREGA on MANIFESTO.ID_MOEDA = MOEDA_MANIFESTO_ENTREGA.ID_MOEDA ");
    	
    	criteriaEntrega.append("MANIFESTO.TP_MANIFESTO = '").append(ConstantesEntrega.TP_MANIFESTO_ENTREGA).append("' ");
     	criteriaEntrega.append("and MANIFESTO.ID_CONTROLE_CARGA = ?");
     	
     	orderBy.append("NR_MANIFESTO ASC");
    	
    	sql.append(projectionColeta).append(joinColeta).append(criteriaColeta)
    		.append(projectionEntrega).append(joinEntrega).append(criteriaEntrega).append(orderBy);
    	
    	return sql.toString(); 
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public JRDataSource executeMountPedidosColeta(Long idControleCarga, Long idManifesto) throws Exception {
        String sqlManifestos = mountSqlPedidosColeta();
        Object[] criteriaManifestos = {idControleCarga, idManifesto};
        JRReportDataObject jrReportDataObject = executeQuery(sqlManifestos, criteriaManifestos);
        Map parametersReport = new HashMap();
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
        jrReportDataObject.setParameters(parametersReport);
        
        return jrReportDataObject.getDataSource();
    }

    private String mountSqlPedidosColeta() {

    	StringBuilder sql = new StringBuilder();
    	
    	StringBuilder projection = new StringBuilder("select distinct ");
    	StringBuilder join = new StringBuilder(" from ");
    	StringBuilder criteria = new StringBuilder(" where ");
    	StringBuilder orderBy = new StringBuilder(" order by ");
    	
    	projection.append("FILIAL_ORIGEM_MANIFESTO.SG_FILIAL as SG_FILIAL_ORIGEM, ");
    	projection.append("PEDIDO_COLETA.NR_COLETA as NR_COLETA, ");
    	projection.append("PESSOA.NM_PESSOA as NM_PESSOA, ");
    	projection.append("FILIAL_DESTINO.SG_FILIAL as SG_FILIAL, ");
    	projection.append("PEDIDO_COLETA.DH_PEDIDO_COLETA as DH_PEDIDO_COLETA, ");
    	projection.append("PEDIDO_COLETA.PS_TOTAL_VERIFICADO as PS_TOTAL_VERIFICADO, ");
    	projection.append("PEDIDO_COLETA.PS_TOTAL_AFORADO_VERIFICADO as PS_TOTAL_AFORADO_VERIFICADO, ");
    	projection.append("PEDIDO_COLETA.QT_TOTAL_VOLUMES_VERIFICADO as QT_TOTAL_VOLUMES_VERIFICADO, ");
    	projection.append("MOEDA_PEDIDO_COLETA.SG_MOEDA as SG_MOEDA, ");
    	projection.append("MOEDA_PEDIDO_COLETA.DS_SIMBOLO as DS_SIMBOLO, ");
    	projection.append("PEDIDO_COLETA.VL_TOTAL_VERIFICADO as VL_TOTAL_VERIFICADO, ");
    	projection.append("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO ")
			.append(" WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_TIPO_PEDIDO_COLETA') ")
			.append(" AND VL_VALOR_DOMINIO = PEDIDO_COLETA.TP_PEDIDO_COLETA )").append(" as TP_PEDIDO_COLETA, ");
    	projection.append("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO ")
			.append(" WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_MODO_PEDIDO_COLETA') ")
			.append(" AND VL_VALOR_DOMINIO = PEDIDO_COLETA.TP_MODO_PEDIDO_COLETA )").append(" as TP_MODO_PEDIDO_COLETA, ");
    	projection.append("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO ")
			.append(" WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_STATUS_COLETA') ")
			.append(" AND VL_VALOR_DOMINIO = PEDIDO_COLETA.TP_STATUS_COLETA )").append(" as TP_STATUS_COLETA, ");
    	projection.append("USUARIO.NM_USUARIO as NM_FUNCIONARIO ");
    	
    	
    	join.append("MANIFESTO_COLETA MANIFESTO_COLETA ");
    	join.append("JOIN PEDIDO_COLETA PEDIDO_COLETA on MANIFESTO_COLETA.ID_MANIFESTO_COLETA = PEDIDO_COLETA.ID_MANIFESTO_COLETA ");
    	join.append("LEFT OUTER JOIN FILIAL FILIAL_ORIGEM_MANIFESTO on MANIFESTO_COLETA.ID_FILIAL_ORIGEM = FILIAL_ORIGEM_MANIFESTO.ID_FILIAL ");
    	join.append("LEFT OUTER JOIN MOEDA MOEDA_PEDIDO_COLETA on PEDIDO_COLETA.ID_MOEDA = MOEDA_PEDIDO_COLETA.ID_MOEDA ");
    	join.append("LEFT OUTER JOIN DETALHE_COLETA DETALHE_COLETA on PEDIDO_COLETA.ID_PEDIDO_COLETA = DETALHE_COLETA.ID_PEDIDO_COLETA ");
    	join.append("LEFT OUTER JOIN FILIAL FILIAL_DESTINO on DETALHE_COLETA.ID_FILIAL = FILIAL_DESTINO.ID_FILIAL ");
    	join.append("LEFT OUTER JOIN PESSOA PESSOA on PEDIDO_COLETA.ID_CLIENTE = PESSOA.ID_PESSOA ");
    	join.append("LEFT OUTER JOIN USUARIO USUARIO on PEDIDO_COLETA.ID_USUARIO = USUARIO.ID_USUARIO ");
    	 
    	criteria.append("MANIFESTO_COLETA.ID_CONTROLE_CARGA = ? ");
    	criteria.append("AND MANIFESTO_COLETA.ID_MANIFESTO_COLETA = ? ");
    	
    	orderBy.append("PEDIDO_COLETA.NR_COLETA ASC");
    	
    	sql.append(projection).append(join).append(criteria).append(orderBy);
    	
    	return sql.toString(); 
	}

    @SuppressWarnings({ "unchecked", "rawtypes" })
   	public JRDataSource executeMountDocumentosEntrega(Long idControleCarga, Long idManifesto) throws Exception {
       String sqlManifestos = mountSqlDocumentosEntrega();
       Object[] criteriaManifestos = {idControleCarga, idManifesto};
       JRReportDataObject jrReportDataObject = executeQuery(sqlManifestos, criteriaManifestos);
       Map parametersReport = new HashMap();
       parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
       jrReportDataObject.setParameters(parametersReport);
       
       return jrReportDataObject.getDataSource();
   }

	private String mountSqlDocumentosEntrega() {
		
		StringBuilder sql = new StringBuilder();
		
		StringBuilder projection = new StringBuilder(" select ");
    	StringBuilder join = new StringBuilder(" from ");
    	StringBuilder criteria = new StringBuilder(" where ");
    	StringBuilder orderBy = new StringBuilder(" order by ");
    	

    	projection.append(" DOCTO_SERVICO.TP_DOCUMENTO_SERVICO as TP_DOCUMENTO_SERVICO, ");
    	projection.append(" FILIAL_ORIGEM.SG_FILIAL as SG_FILIAL_ORIGEM, ");
    	projection.append(" DOCTO_SERVICO.NR_DOCTO_SERVICO as NR_DOCTO_SERVICO, ");
    	projection.append(" TRUNC(DOCTO_SERVICO.DT_PREV_ENTREGA) as OTD, ");
    	projection.append(" FILIAL_DESTINO.SG_FILIAL as SG_FILIAL_DESTINO, ");
    	projection.append(" DOCTO_SERVICO.DH_EMISSAO as DH_EMISSAO, ");
    	projection.append(" PESSOA_REMETENTE.NM_PESSOA as NM_PESSOA_REMETENTE, ");
    	projection.append(" PESSOA_DESTINATARIO.NM_PESSOA as NM_PESSOA_DESTINATARIO, ");
    	projection.append(" SERVICO.SG_SERVICO as SG_SERVICO, ");
    	projection.append(" MUNICIPIO.NM_MUNICIPIO as NM_MUNICIPIO, ");
    	projection.append(" NVL((SELECT ROTA_COLETA_ENTREGA.NR_ROTA||' '||ROTA_COLETA_ENTREGA.DS_ROTA FROM ROTA_COLETA_ENTREGA ")
		.append(" WHERE DOCTO_SERVICO.ID_ROTA_COLETA_ENTREGA_REAL = ROTA_COLETA_ENTREGA.ID_ROTA_COLETA_ENTREGA), ")
    	.append(" (SELECT ROTA_COLETA_ENTREGA.NR_ROTA||' '||ROTA_COLETA_ENTREGA.DS_ROTA FROM ROTA_COLETA_ENTREGA ")
    	.append(" WHERE DOCTO_SERVICO.ID_ROTA_COLETA_ENTREGA_SUGERID = ROTA_COLETA_ENTREGA.ID_ROTA_COLETA_ENTREGA)) as DS_ROTA_COLETA_ENTREGA, ");
    	projection.append(" DOCTO_SERVICO.QT_VOLUMES as QT_VOLUMES, ");
    	projection.append(" DOCTO_SERVICO.PS_REAL as PS_REAL, ");
    	projection.append(" DOCTO_SERVICO.PS_AFERIDO as PS_AFERIDO, ");
    	projection.append(" DOCTO_SERVICO.PS_AFORADO as PS_AFORADO, ");
    	projection.append(" DOCTO_SERVICO.PS_REFERENCIA_CALCULO as PS_REFERENCIA_CALCULO, ");
    	projection.append(" MOEDA.SG_MOEDA as SG_MOEDA, ");
    	projection.append(" MOEDA.DS_SIMBOLO as DS_SIMBOLO, ");
    	projection.append(" DOCTO_SERVICO.VL_MERCADORIA as VL_MERCADORIA, ");
    	projection.append(" DOCTO_SERVICO.VL_FRETE_LIQUIDO as VL_FRETE_LIQUIDO, ");
    	projection.append(" VI18N(OCORRENCIA_ENTREGA.DS_OCORRENCIA_ENTREGA_I) as DS_OCORRENCIA_ENTREGA_I, ");
    	projection.append(" DOCTO_SERVICO.OB_COMPLEMENTO_LOCALIZACAO as DS_LOCALIZACAO_MERCADORIA_I, ");
    	projection.append(" (SELECT AGENDAMENTO_ENTREGA.DT_AGENDAMENTO FROM AGENDAMENTO_ENTREGA, AGENDAMENTO_DOCTO_SERVICO ")
    	.append(" WHERE DOCTO_SERVICO.ID_DOCTO_SERVICO = AGENDAMENTO_DOCTO_SERVICO.ID_DOCTO_SERVICO ")
    	.append(" AND AGENDAMENTO_DOCTO_SERVICO.ID_AGENDAMENTO_ENTREGA = AGENDAMENTO_ENTREGA.ID_AGENDAMENTO_ENTREGA ")    	
    	.append(" AND AGENDAMENTO_ENTREGA.TP_SITUACAO_AGENDAMENTO = 'A') as DT_AGENDAMENTO");

    	join.append("MANIFESTO MANIFESTO ");
    	join.append("INNER JOIN MANIFESTO_ENTREGA MANIFESTO_ENTREGA on MANIFESTO.ID_MANIFESTO = MANIFESTO_ENTREGA.ID_MANIFESTO_ENTREGA ");
    	join.append("INNER JOIN MANIFESTO_ENTREGA_DOCUMENTO MANIFESTO_ENTREGA_DOCUMENTO on MANIFESTO_ENTREGA.ID_MANIFESTO_ENTREGA = MANIFESTO_ENTREGA_DOCUMENTO.ID_MANIFESTO_ENTREGA ");
    	join.append("INNER JOIN DOCTO_SERVICO DOCTO_SERVICO on MANIFESTO_ENTREGA_DOCUMENTO.ID_DOCTO_SERVICO = DOCTO_SERVICO.ID_DOCTO_SERVICO ");
    	join.append("INNER JOIN CONHECIMENTO on DOCTO_SERVICO.ID_DOCTO_SERVICO = CONHECIMENTO.ID_CONHECIMENTO ");
    	join.append("INNER JOIN MUNICIPIO on CONHECIMENTO.ID_MUNICIPIO_ENTREGA = MUNICIPIO.ID_MUNICIPIO ");
    	join.append("LEFT OUTER JOIN OCORRENCIA_ENTREGA OCORRENCIA_ENTREGA on MANIFESTO_ENTREGA_DOCUMENTO.ID_OCORRENCIA_ENTREGA = OCORRENCIA_ENTREGA.ID_OCORRENCIA_ENTREGA ");
    	join.append("INNER JOIN FILIAL FILIAL_ORIGEM on DOCTO_SERVICO.ID_FILIAL_ORIGEM = FILIAL_ORIGEM.ID_FILIAL ");
    	join.append("INNER JOIN FILIAL FILIAL_DESTINO on DOCTO_SERVICO.ID_FILIAL_DESTINO = FILIAL_DESTINO.ID_FILIAL ");
    	join.append("INNER JOIN SERVICO SERVICO on DOCTO_SERVICO.ID_SERVICO = SERVICO.ID_SERVICO ");
    	join.append("INNER JOIN PESSOA PESSOA_REMETENTE on DOCTO_SERVICO.ID_CLIENTE_REMETENTE = PESSOA_REMETENTE.ID_PESSOA ");
    	join.append("INNER JOIN PESSOA PESSOA_DESTINATARIO on DOCTO_SERVICO.ID_CLIENTE_DESTINATARIO = PESSOA_DESTINATARIO.ID_PESSOA ");
    	join.append("INNER JOIN MOEDA MOEDA on DOCTO_SERVICO.ID_MOEDA = MOEDA.ID_MOEDA ");
    	join.append("INNER JOIN LOCALIZACAO_MERCADORIA LOCALIZACAO_MERCADORIA on DOCTO_SERVICO.ID_LOCALIZACAO_MERCADORIA = LOCALIZACAO_MERCADORIA.ID_LOCALIZACAO_MERCADORIA ");

    	
    	criteria.append("MANIFESTO.TP_MANIFESTO = '").append(ConstantesEntrega.TP_MANIFESTO_ENTREGA).append("' ");
     	criteria.append("and MANIFESTO.ID_CONTROLE_CARGA = ? ");
     	criteria.append("and MANIFESTO_ENTREGA.ID_MANIFESTO_ENTREGA = ? ");
     	
     	orderBy.append("DOCTO_SERVICO.NR_DOCTO_SERVICO ASC");
     	
     	sql.append(projection).append(join).append(criteria).append(orderBy);
    	
    	return sql.toString();
	}


	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

}