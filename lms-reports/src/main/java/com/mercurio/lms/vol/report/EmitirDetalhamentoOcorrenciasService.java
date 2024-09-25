package com.mercurio.lms.vol.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.service.OcorrenciaEntregaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * @spring.bean id="lms.vol.emitirDetalhamentoOcorrenciasService"
 * @spring.property name="reportName" value="com/mercurio/lms/vol/report/emitirDetalhamentoOcorrencias.jasper"
 */
public class EmitirDetalhamentoOcorrenciasService extends ReportServiceSupport {
	private FilialService filialService;
	private OcorrenciaEntregaService ocorrenciaEntregaService; 
	public JRReportDataObject execute(Map parameters) throws Exception {
		SqlTemplate sql = getSql(parameters);
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());		
		
		Map parametersReport = new HashMap();
		parametersReport.put("PARAMETROS_PESQUISA", sql.getFilterSummary());
        parametersReport.put("USUARIO_EMISSOR", SessionUtils.getUsuarioLogado().getNmUsuario());
		jr.setParameters(parametersReport);		
		
		return jr; 		
	}
	private SqlTemplate getSql(Map param){
		Long idFilial = Long.valueOf(((Map)param.get("filial")).get("idFilial").toString());
		Long idOcorrenciaEntrega = Long.valueOf((String)param.get("idOcorrenciaEntrega"));
		YearMonthDay dtInicio = (YearMonthDay) ReflectionUtils.toObject(param.get("dataInicial").toString(), YearMonthDay.class);
		YearMonthDay dtFim = (YearMonthDay) ReflectionUtils.toObject(param.get("dataFinal").toString(), YearMonthDay.class);
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.setDistinct();	
		
		sql.addProjection("FROT.nr_frota FROTA");
		sql.addProjection("FILI.sg_Filial || ' ' || LPAD(MAEN.nr_Manifesto_Entrega,8,0) MANIFESTO");
		sql.addProjection("FIOR.sg_Filial || ' ' || LPAD(CTRC.nr_Conhecimento,8,0) CTRC");
		sql.addProjection("nvl(DOSE.ds_Endereco_Entrega_Real,' ') ENDERECO");
		sql.addProjection("PESS.nm_Pessoa CLIENTE");
		sql.addProjection("nvl(sum(NFCO.qt_Volumes),0) VOLUMES");
		sql.addProjection("to_char(MEDO.dh_Ocorrencia,'dd/mm/rr hh24:mi') BAIXA");
		sql.addProjection("to_char(DOSE.dt_Prev_Entrega,'dd/mm/rr')||' ' DPE");
		
		sql.addFrom(
			new StringBuffer()
				.append("manifesto_entrega_documento         medo ")
				.append("inner join manifesto_entrega        maen on maen.id_manifesto_entrega  = medo.id_manifesto_entrega ")
				.append("inner join manifesto                mani on mani.id_manifesto          = maen.id_manifesto_entrega ")
				.append("inner join controle_carga           coca on coca.ID_CONTROLE_CARGA     = mani.ID_CONTROLE_CARGA ")
				.append("inner join meio_transporte          frot on coca.ID_TRANSPORTADO       = frot.id_meio_transporte ")
				.append("inner join docto_servico            dose on medo.ID_DOCTO_SERVICO      = dose.ID_DOCTO_SERVICO ")
				.append("inner join cliente                  clie on clie.id_cliente            = dose.id_cliente_destinatario ")
				.append("inner join pessoa                   pess on pess.id_pessoa             = clie.id_cliente ")
				.append("left  join conhecimento             ctrc on ctrc.id_conhecimento       = dose.ID_DOCTO_SERVICO ")
				.append("left  join nota_fiscal_conhecimento nfco on nfco.id_conhecimento       = ctrc.id_conhecimento ")
				.append("inner join ocorrencia_entrega       ocen on ocen.id_ocorrencia_entrega = medo.id_ocorrencia_entrega ")  
				.append("inner join filial                   fili on fili.id_filial             = maen.id_filial ")
				.append("left  join filial                   fior on ctrc.ID_FILIAL_ORIGEM      = fior.id_filial ")
			.toString()
		);
		
		sql.addCriteria("FILI.id_Filial","=",idFilial);
		sql.addCriteria("MEDO.dh_Ocorrencia",">=",dtInicio);
		sql.addCriteria("MEDO.dh_Ocorrencia","<=",dtFim);
		sql.addCriteria("OCEN.id_Ocorrencia_Entrega","=",idOcorrenciaEntrega);
		
		List l = filialService.findNmSgFilialByIdFilial(idFilial);
		TypedFlatMap tfm = (TypedFlatMap)l.get(0);
		
		sql.addFilterSummary("filial",tfm.getString("sgFilial") + " - " + tfm.getString("pessoa.nmFantasia"));
		sql.addFilterSummary("ocorrencia",ocorrenciaEntregaService.findById(idOcorrenciaEntrega).getDsOcorrenciaEntrega());
		sql.addFilterSummary("periodoInicial",JTFormatUtils.format(dtInicio, JTFormatUtils.SHORT));
		sql.addFilterSummary("periodoFinal",JTFormatUtils.format(dtFim, JTFormatUtils.SHORT));
		
		sql.addGroupBy("FROT.nr_Frota");
		sql.addGroupBy("FILI.sg_Filial");
		sql.addGroupBy("FIOR.sg_Filial");
		sql.addGroupBy("CTRC.nr_Conhecimento");
		sql.addGroupBy("MAEN.nr_Manifesto_Entrega");
		sql.addGroupBy("DOSE.ds_Endereco_Entrega_Real");
		sql.addGroupBy("PESS.nm_Pessoa");
		sql.addGroupBy("MEDO.dh_Ocorrencia");
		sql.addGroupBy(PropertyVarcharI18nProjection.createProjection("OCEN.ds_Ocorrencia_Entrega_i"));
		sql.addGroupBy("DOSE.dt_Prev_Entrega");

		sql.addOrderBy("FROTA");
		sql.addOrderBy("MANIFESTO");
		
		return sql;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setOcorrenciaEntregaService(
			OcorrenciaEntregaService ocorrenciaEntregaService) {
		this.ocorrenciaEntregaService = ocorrenciaEntregaService;
	}
	
}
