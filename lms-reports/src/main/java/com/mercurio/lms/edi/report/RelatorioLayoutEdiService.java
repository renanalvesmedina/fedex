package com.mercurio.lms.edi.report;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edi.model.LayoutEDI;
import com.mercurio.lms.edi.model.service.LayoutEDIService;

/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.edi.relatorioLayoutEdiService"
 * @spring.property name="reportName"
 *                  value="com/mercurio/lms/edi/report/emitirRelatorioLayoutEdi.jasper"
 */

public class RelatorioLayoutEdiService extends ReportServiceSupport {

	private LayoutEDIService layoutEDIService;
	
	public JRReportDataObject execute(Map criteria) throws Exception {

		TypedFlatMap map = (TypedFlatMap) criteria;

		Long idClienteEdi = map.getLong("idClienteEdi");
		Long idLayoutEdi  = map.getLong("idLayoutEdi");
		
		Map parametersReport = new HashMap();
		
		if(idClienteEdi != null){
			parametersReport.put("ID_CLIENTE_EDI",idClienteEdi);
			parametersReport.put("NMPESSOA",map.getString("nmPessoa"));
			parametersReport.put("IDENTIFICACAO",map.getString("nrIdentificacao"));
		}
		
		if(idLayoutEdi != null){			
			LayoutEDI layoutEDI = layoutEDIService.findById(idLayoutEdi);			
			parametersReport.put("ID_LAYOUT",idLayoutEdi);
			parametersReport.put("NMLAYOUT",layoutEDI.getNmLayoutEdi());
			parametersReport.put("TIPOLAYOUT",layoutEDI.getTpLayoutEdi().getDescription().getValue());
			parametersReport.put("TIPODOCUMENTO",layoutEDI.getTipoLayoutDocumento().getDsTipoLayoutDocumento());
			parametersReport.put("TIPOEXTENSAO",layoutEDI.getTipoArquivoEDI().getExtTipoArquivoEDI());			
		}
		
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,JRReportDataObject.EXPORT_PDF);

		StringBuilder sql = createSqlTemplate(map);
		JRReportDataObject jr = executeQuery(sql.toString(), new Object[]{});

		jr.setParameters(parametersReport);

		return jr;
	}

	public JRDataSource generateSubReportCampos(Long idLayoutEdi, Long idRegistroLayout, Long idClienteEdi) throws Exception {
		
		StringBuilder sql = createSubSqlTemplate(idLayoutEdi, idRegistroLayout, idClienteEdi);			
		JRDataSource dataSource = executeQuery(sql.toString(), new Object[]{}).getDataSource();
		
		return dataSource;		
	}
	
	
	private StringBuilder createSqlTemplate(TypedFlatMap map) {
		
		Long idClienteEdi = map.getLong("idClienteEdi");
		Long idLayoutEdi  = map.getLong("idLayoutEdi");
		
		StringBuilder sql = new StringBuilder();
		
			sql = createSqlByLayout(idClienteEdi,idLayoutEdi);
	
		return sql;
	}
	
	private StringBuilder createSqlByLayout(Long idClienteEdi, Long idLayoutEdi) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT distinct ");
		sql.append(" REG.ID_REGISTRO_LAYOUT                  AS ID_REGISTRO           , ");
		sql.append(" REG.IDENTIFICADOR                       AS IDENTIFICADOR_REGISTRO, ");
		sql.append(" REG.DESCRICAO                           AS DESCRICAO_REGISTRO    , ");
		sql.append(" REG.PREENCHIMENTO                       AS PREENCHIMENTO_REGISTRO, ");
		sql.append(" REG.TAMANHO_REGISTRO                    AS TAMANHO_REGISTRO      , ");
		sql.append(" REG.OCORRRENCIAS                        AS OCORRENCIAS_REGISTRO  ,  ");
		sql.append(" REGPAI.IDENTIFICADOR                    AS REGISTRO_PAI          ,  ");

		sql.append("   ( ");
		sql.append(" SELECT "+PropertyVarcharI18nProjection.createProjection("VDM.DS_VALOR_DOMINIO_I")+" "); 
		sql.append(" FROM DOMINIO DM, VALOR_DOMINIO VDM WHERE DM.ID_DOMINIO = VDM.ID_DOMINIO ");
		sql.append(" AND DM.NM_DOMINIO = 'DM_PREENCHIMENTO' ");
		sql.append(" AND VDM.VL_VALOR_DOMINIO = REG.PREENCHIMENTO ");
		sql.append(" ) AS VLPREENCHIMENTO ");  
		
		sql.append(" from composicao_layout_edi comp ");
		sql.append(" join layout_edi layout on layout.id_layout_edi = comp.laed_id_layout_edi ");
		sql.append(" join campo_layout_edi campo on campo.id_campo_layout = comp.cale_id_campo_layout ");
		sql.append(" join registro_layout_edi reg on reg.id_registro_layout = comp.rele_id_registro_layout ");
		sql.append(" left join de_para_edi depara on depara.id_de_para_edi = comp.dped_id_de_para_edi ");
		sql.append(" left join cliente_edi_layout cliente on cliente.id_cliente_edi_layout = comp.cel_id_cliente_edi_layout  ");
		sql.append(" LEFT JOIN REGISTRO_LAYOUT_EDI REGPAI ON REG.REGISTRO_PAI = REGPAI.ID_REGISTRO_LAYOUT ");
		

		sql.append(" where 1 = 1 ");
		
	    if (idLayoutEdi != null){
			sql.append(" and cliente.ce_clie_pess_id_pessoa is null ");
		    sql.append(" and comp.laed_id_layout_edi = ").append(idLayoutEdi);	    	
		} else if(idClienteEdi != null) {
			sql.append(" and (cliente.ce_clie_pess_id_pessoa = ").append(idClienteEdi);	    
			sql.append(" or (cliente.ce_clie_pess_id_pessoa is null " +
					" and comp.laed_id_layout_edi in (select cli.laed_id_layout_edi from cliente_edi_layout cli where cli.ce_clie_pess_id_pessoa = ").append(idClienteEdi).append(") " +
			        " and campo.id_campo_layout not in ( " +
				       " select campo2.id_campo_layout " +
				       " from composicao_layout_edi comp2 " +
				       " join layout_edi layout2 on layout2.id_layout_edi = comp2.laed_id_layout_edi " +
				       " join campo_layout_edi campo2 on campo2.id_campo_layout = comp2.cale_id_campo_layout " +
				       " join registro_layout_edi reg2 on reg2.id_registro_layout = comp2.rele_id_registro_layout " +
				       " left join de_para_edi depara2 on depara2.id_de_para_edi = comp2.dped_id_de_para_edi " +
				       " left join cliente_edi_layout cliente2 on cliente2.id_cliente_edi_layout = comp2.cel_id_cliente_edi_layout " +
				       " LEFT JOIN REGISTRO_LAYOUT_EDI REGPAI2 ON REG2.REGISTRO_PAI = REGPAI2.ID_REGISTRO_LAYOUT " +
				       " where cliente2.ce_clie_pess_id_pessoa = " + idClienteEdi +
				    " ))) ");
		}
		
		sql.append(" ORDER BY IDENTIFICADOR_REGISTRO ");

			return sql;
		}
		
	private StringBuilder createSubSqlTemplate(Long idLayoutEdi, Long idRegistroLayout, Long idClienteEdiLayout) {
	    
		StringBuilder sql = new StringBuilder();
	    
		sql.append(" select ");
	    sql.append(" CAMPO.NOME_CAMPO AS CAMPO, ");  
	    sql.append(" CAMPO.DESCRICAO_CAMPO AS DESCRICAO, ");  
	    sql.append(" COMP.FORMATO AS FORMATO,  ");
	    sql.append(" COMP.TAMANHO AS TAMANHO,  ");
	    sql.append(" COMP.QTDE_DECIMAL AS CDECIMAL, "); 
	    sql.append(" COMP.POSICAO AS POSICAO,  ");
	    sql.append(" CAMPO.OBRIGATORIO AS OBRIGATORIO, "); 
	    sql.append(" DEPARA.NOME AS DEPARA,  ");
	    sql.append(" COMP.COMPL_FORMATO AS COMPLEMENTO, "); 
		sql.append(" COMP.VALOR_DEFAULT AS VLDEFAULT ");

		sql.append(", (SELECT "+PropertyVarcharI18nProjection.createProjection("VDM.DS_VALOR_DOMINIO_I")+" FROM DOMINIO DM, VALOR_DOMINIO VDM "); 
	    sql.append(" WHERE   ");
	    sql.append(" DM.ID_DOMINIO = VDM.ID_DOMINIO AND "); 
	    sql.append(" DM.NM_DOMINIO = 'DM_FORMATO' AND  ");
	    sql.append(" VDM.VL_VALOR_DOMINIO = comp.formato ) AS VLFORMATO "); 		
			
		sql.append(" from composicao_layout_edi comp ");
	    sql.append(" join layout_edi layout on layout.id_layout_edi = comp.laed_id_layout_edi ");
	    sql.append(" join campo_layout_edi campo on campo.id_campo_layout = comp.cale_id_campo_layout ");
	    sql.append(" join registro_layout_edi reg on reg.id_registro_layout = comp.rele_id_registro_layout ");
	    sql.append(" left join de_para_edi depara on depara.id_de_para_edi = comp.dped_id_de_para_edi ");
	    sql.append(" left join cliente_edi_layout cliente on cliente.id_cliente_edi_layout = comp.cel_id_cliente_edi_layout  ");
			
			
	    sql.append(" where 1=1 ");

	    if (idLayoutEdi != null){
			sql.append(" and cliente.ce_clie_pess_id_pessoa is null ");
		    sql.append(" and comp.laed_id_layout_edi = ").append(idLayoutEdi);	    	
		} else if(idClienteEdiLayout != null) {
			sql.append(" and (cliente.ce_clie_pess_id_pessoa = ").append(idClienteEdiLayout);	    
			sql.append(" or (cliente.ce_clie_pess_id_pessoa is null " +
					" and comp.laed_id_layout_edi in (select cli.laed_id_layout_edi from cliente_edi_layout cli where cli.ce_clie_pess_id_pessoa = ").append(idClienteEdiLayout).append(")" +
					        " and campo.id_campo_layout not in ( " +
						       " select campo2.id_campo_layout " +
						       " from composicao_layout_edi comp2 " +
						       " join layout_edi layout2 on layout2.id_layout_edi = comp2.laed_id_layout_edi " +
						       " join campo_layout_edi campo2 on campo2.id_campo_layout = comp2.cale_id_campo_layout " +
						       " join registro_layout_edi reg2 on reg2.id_registro_layout = comp2.rele_id_registro_layout " +
						       " left join de_para_edi depara2 on depara2.id_de_para_edi = comp2.dped_id_de_para_edi " +
						       " left join cliente_edi_layout cliente2 on cliente2.id_cliente_edi_layout = comp2.cel_id_cliente_edi_layout " +
						       " LEFT JOIN REGISTRO_LAYOUT_EDI REGPAI2 ON REG2.REGISTRO_PAI = REGPAI2.ID_REGISTRO_LAYOUT " +
						       " where cliente2.ce_clie_pess_id_pessoa = " + idClienteEdiLayout +
						    " ))) ");
		}
	    
	    sql.append(" and comp.rele_id_registro_layout = ").append(idRegistroLayout);
	    
	    sql.append(" order by COMP.POSICAO ");
				
		return sql;
	}
	

	public LayoutEDIService getLayoutEDIService() {
		return layoutEDIService;
	}

	public void setLayoutEDIService(LayoutEDIService layoutEDIService) {
		this.layoutEDIService = layoutEDIService;
	}
	
	
}
