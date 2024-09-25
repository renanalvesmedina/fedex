package com.mercurio.lms.ppd.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import net.sf.jasperreports.engine.data.JRMapArrayDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.ppd.model.PpdLoteJde;
import com.mercurio.lms.ppd.model.PpdLoteRecibo;
import com.mercurio.lms.ppd.model.service.PpdLoteJdeService;
import com.mercurio.lms.ppd.model.service.PpdLoteReciboService;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class PpdExcelRecibosLoteService extends ReportServiceSupport {	
	private PpdLoteReciboService loteReciboService;
	private PpdLoteJdeService loteJdeService;
	
	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {	
		Long idLoteJde = (Long)parameters.get("idLoteJde");
		PpdLoteJde lote = loteJdeService.findById(idLoteJde);
		List<PpdLoteRecibo> lotesRecibos = loteReciboService.findByIdLoteJde(idLoteJde);			
		List<Map<String,Object>> recibosMapped = new ArrayList<Map<String,Object>>();
		
		
		
		
		for(int i=0; i<lotesRecibos.size(); i++) {			
			recibosMapped.add(lotesRecibos.get(i).getRecibo().getMapped());			
			recibosMapped.get(i).put("dtProgramadaPagto",JTFormatUtils.format((YearMonthDay)recibosMapped.get(i).get("dtProgramadaPagto")));			
			//tipo de indenizacao
			recibosMapped.get(i).put("tpIndenizacao", lotesRecibos.get(i).getRecibo().getTpIndenizacao().getDescription().getValue());
		}		
				
		SqlTemplate sql = this.createSqlTemplate();				
		sql.addFilterSummary("numeroLote", lote.getNrLoteJde());		
		
		parameters.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);		
		parameters.put("parametrosPesquisa", sql.getFilterSummary());
		parameters.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());		

		return createReportDataObject(new JRMapArrayDataSource(recibosMapped.toArray()), parameters);
	}

	public void setLoteReciboService(PpdLoteReciboService loteReciboService) {
		this.loteReciboService = loteReciboService;
	}

	public void setLoteJdeService(PpdLoteJdeService loteJdeService) {
		this.loteJdeService = loteJdeService;
	}
}
