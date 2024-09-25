package com.mercurio.lms.edi.model.dao;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edi.model.ClienteEDIFilialEmbarcadora;
import com.mercurio.lms.edi.model.LogEDI;
import com.mercurio.lms.edi.model.LogEDIComplemento;
import com.mercurio.lms.edi.model.LogEDIDetalhe;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.edi.model.NotaFiscalEdiComplemento;

public class LogEDIDAO extends BaseCrudDao<LogEDI, Long>{

	@Override
	public Class getPersistentClass() {		
		return LogEDI.class;
	}

	private StringBuilder getSqlFindPaginated(TypedFlatMap criteria, boolean find) {
		StringBuilder query = new StringBuilder();
		
		boolean hasDocumentoCliente = MapUtils.getObject(criteria, "dsCampo") != null && MapUtils.getObject(criteria, "valorComplemento") != null;
		
		if(find){
			query.append(" SELECT distinct logArq ");
		}
		query.append(" FROM "+LogEDI.class.getName()+" as logArq ");
		
		if(MapUtils.getObject(criteria, "nrNotaFiscal") != null || hasDocumentoCliente) {
		    query.append(", "+LogEDIDetalhe.class.getName()+" as logDet ");
		}
		
		if(hasDocumentoCliente) {
		    query.append(", "+ LogEDIComplemento.class.getName()+" as LAEDC "); 
		}
		
		query.append("where 1=1 ");
		
		if(MapUtils.getObject(criteria, "idFilial") != null) {
			query.append(buildFilialRestriction());
		}	
		
		appendDatesRestrictions(criteria, query);	

		if(MapUtils.getObject(criteria, "nrNotaFiscal") != null || hasDocumentoCliente) {
		    query.append(" and logDet.logEDI.idLogEdi = logArq.idLogEdi ");

		    if(MapUtils.getObject(criteria, "nrNotaFiscal") != null){
		        query.append(buildNotaFiscalRestriction());
		    }
		}
		
		if(hasDocumentoCliente) {
		    query.append(" and LAEDC.logEDIDetalhe.idLogEdiDetalhe = logDet.idLogEdiDetalhe ");
		    query.append(" and LAEDC.nomeComplemento = :dsCampo ");
		    query.append(" and LAEDC.valorComplemento = :valorComplemento ");
		}
		
		if(MapUtils.getObject(criteria, "idEmpresa") != null && (MapUtils.getObject(criteria, "filialMatriz") == null || !((Boolean)MapUtils.getObject(criteria, "filialMatriz")))) {
			query.append("  and logArq.clienteEDIFilialEmbarcadora.filial.empresa.idEmpresa =:idEmpresa ");
			query.append("  and logArq.filial.empresa.idEmpresa =:idEmpresa ");
		}
		if(MapUtils.getObject(criteria, "nro_doc_cliente") != null) {
			query.append(buildClientRestriction());
		}
		
		if(find){
			query.append("  order by logArq.data desc,logArq.horaInicio desc, logArq.horaFim desc ");
		}
		return query;
	}

	private void appendDatesRestrictions(TypedFlatMap criteria, StringBuilder query) {
		if(MapUtils.getObject(criteria, "dataInicio") != null) {
			query.append(buildDataInicioRestriction());
		}	
		if(MapUtils.getObject(criteria, "dataFim") != null) {
			query.append(buildDataFimRestriction());
		}		
		if(MapUtils.getObject(criteria, "data") != null) {
			query.append(buildDataRestriction());
		}
	}

	private String buildFilialRestriction() {
		return "  and logArq.filial.idFilial =:idFilial ";
	}

	private String buildNotaFiscalRestriction() {
		return "  and logDet.nrNotaFiscal =:nrNotaFiscal ";
	}

	private String buildDataRestriction() {
		return "  and logArq.data =:data ";
	}

	private String buildDataFimRestriction() {
		return " and trunc(logArq.data) <= to_date(:dataFim, 'YYYY-MM-DD')";
	}

	private String buildDataInicioRestriction() {
		return " and trunc(logArq.data) >= to_date(:dataInicio, 'YYYY-MM-DD') ";
	}

	private String buildClientRestriction() {
		StringBuilder builder = new StringBuilder();
		String cliientEDIFilialEmbarcadoraClassName = ClienteEDIFilialEmbarcadora.class.getName();

		builder
		.append("AND logArq.clienteEDIFilialEmbarcadora.idClienteEDIFilialEmbarcadora IN ")
		.append("(	SELECT cefe.idClienteEDIFilialEmbarcadora")
		.append("	FROM  " + cliientEDIFilialEmbarcadoraClassName + " cefe , " + cliientEDIFilialEmbarcadoraClassName + " cliente_edi")
		.append("	WHERE")
		.append("		cliente_edi.clienteEmbarcador.tpSituacao = 'A'")
		.append("		AND cliente_edi.clienteEmbarcador.pessoa.nrIdentificacao = :nro_doc_cliente")			
		.append("		AND cefe.clienteEmbarcador = cliente_edi.cliente")
		.append(")");

		return builder.toString();
	} 
	
	@SuppressWarnings("unchecked")
	public ResultSetPage findPaginatedLog(TypedFlatMap criteria, FindDefinition findDef) {
		StringBuilder query = this.getSqlFindPaginated(criteria,true);		
		String sql = query.toString();
		return getAdsmHibernateTemplate().findPaginated(sql, findDef.getCurrentPage(), findDef.getPageSize(),criteria);
	}
	public Integer getRowCountLog(TypedFlatMap criteria) {
		StringBuilder query = this.getSqlFindPaginated(criteria,false);
		return getAdsmHibernateTemplate().getRowCountForQuery(query.toString(),criteria); 
	}
		
	public Long findSequence(){		
		return Long.valueOf(getSession().createSQLQuery("select LOG_ARQUIVO_EDI_SQ.nextval from dual").uniqueResult().toString());
	}
	
}

