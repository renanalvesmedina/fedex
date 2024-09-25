package com.mercurio.lms.seguros.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Setor;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.SetorService;
import com.mercurio.lms.seguros.model.DoctoProcessoSinistro;
import com.mercurio.lms.seguros.model.ProcessoSinistro;
import com.mercurio.lms.seguros.model.service.DoctoProcessoSinistroService;
import com.mercurio.lms.seguros.model.service.ProcessoSinistroService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author luisfco
 * @spring.bean id="lms.seguros.emitirProtocoloDocEntreguesEntregaService"
 * @spring.property name="reportName" value="com/mercurio/lms/seguros/report/emitirProtocoloDocEntreguesEntrega.jasper"
 */
public class EmitirProtocoloDocEntreguesEntregaService extends ReportServiceSupport {
	
	DoctoProcessoSinistroService doctoProcessoSinistroService;
	ProcessoSinistroService processoSinistroService;
	SetorService setorService;
	
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap)parameters;
		String tpEntregaRecebimento = tfm.getString("tpEntregaRecebimento");
		
		List idsDoctoProcessoSinistro = new ArrayList();
		List preparedStatements = new ArrayList();
		Long maxProtocolo = this.doctoProcessoSinistroService.findMaxNrProtocolo(tpEntregaRecebimento);
		if (maxProtocolo==null)
			maxProtocolo = Long.valueOf(0);
		
		// iteracao que realiza update na data de 
		// emissao e no numero do protocolo
		List list = tfm.getList("selectedIds");
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Long idDoctoProcessoSinistro = Long.valueOf((String)iter.next());
			DoctoProcessoSinistro doctoProcessoSinistro = this.doctoProcessoSinistroService.findById(idDoctoProcessoSinistro);
			doctoProcessoSinistro.setDhEmissaoProtocolo(JTDateTimeUtils.getDataHoraAtual());
			doctoProcessoSinistro.setNrProtocolo(Long.valueOf( maxProtocolo.longValue() + 1 ));
			idsDoctoProcessoSinistro.add(idDoctoProcessoSinistro);
			preparedStatements.add("?");
		}
		
		SqlTemplate sql = compileSqlTemplate(tpEntregaRecebimento, idsDoctoProcessoSinistro, preparedStatements);
		
		ProcessoSinistro processoSinistro = this.processoSinistroService.findById(tfm.getLong("processoSinistro.idProcessoSinistro"));
		Usuario usuario = SessionUtils.getUsuarioLogado();
		Setor setor = this.setorService.findSetorByUsuario(usuario);
		
		Map reportParameters = new HashMap();
		
		reportParameters.put("nrProtocolo", Long.valueOf(maxProtocolo.longValue() + 1));
		reportParameters.put("nrProcessoSinistro", processoSinistro.getNrProcessoSinistro());
		reportParameters.put("tipoSinistro", processoSinistro.getTipoSinistro().getDsTipo().getValue(LocaleContextHolder.getLocale()));
		reportParameters.put("usuario", usuario.getNmUsuario());
		reportParameters.put("setor", setor != null ? setor.getDsSetor() : "");
		
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		
		jr.setParameters(reportParameters);
		
		return jr;
	}
	
	private SqlTemplate compileSqlTemplate(String tpEntregaRecebimento, List idsDoctoProcessoSinistro, List preparedStatements) {

		SqlTemplate sql = createSqlTemplate();
		sql.addProjection("REGULADORA_PESSOA.NM_PESSOA", "PARA");
		sql.addProjection("PS.DH_SINISTRO");
		sql.addProjection("TDS.DS_TIPO", "TIPO_DOCUMENTO_SEGURO");
		sql.addProjection("DPS.NR_DOCUMENTO", "NR_DOCUMENTO");
		
		sql.addFrom("DOCTO_PROCESSO_SINISTRO", "DPS");
		sql.addFrom("PROCESSO_SINISTRO", "PS");
		sql.addFrom("TIPO_DOCUMENTO_SEGURO", "TDS");
		sql.addFrom("TIPO_SINISTRO", "TS");
		sql.addFrom("REGULADORA_SEGURO", "R");
		sql.addFrom("PESSOA", "REGULADORA_PESSOA");
		sql.addJoin("DPS.ID_PROCESSO_SINISTRO", "PS.ID_PROCESSO_SINISTRO");
		sql.addJoin("DPS.ID_TIPO_DOCUMENTO_SEGURO", "TDS.ID_TIPO_DOCUMENTO_SEGURO");
		sql.addJoin("PS.ID_TIPO_SINISTRO", "TS.ID_TIPO_SINISTRO");
		sql.addJoin("PS.ID_REGULADORA", "R.ID_REGULADORA");
		sql.addJoin("R.ID_REGULADORA", "REGULADORA_PESSOA.ID_PESSOA");

		// restringindo apenas aos ids checados da grid
		String in = "DPS.ID_DOCTO_PROCESSO_SINISTRO IN " + SQLUtils.joinExpressionsWithComma(preparedStatements);
		sql.addCustomCriteria(in);
		
		for (Iterator it = idsDoctoProcessoSinistro.iterator(); it.hasNext(); ) {
			sql.addCriteriaValue((Long)it.next());
		}
		
		return sql;
	}

	public void setDoctoProcessoSinistroService(
			DoctoProcessoSinistroService doctoProcessoSinistroService) {
		this.doctoProcessoSinistroService = doctoProcessoSinistroService;
	}

	public void setProcessoSinistroService(
			ProcessoSinistroService processoSinistroService) {
		this.processoSinistroService = processoSinistroService;
	}

	public void setSetorService(SetorService setorService) {
		this.setorService = setorService;
	}
	
}
